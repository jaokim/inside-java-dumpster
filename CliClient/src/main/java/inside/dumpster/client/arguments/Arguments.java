/*
 *
 */
package inside.dumpster.client.arguments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Joakim Nordström
 */
public class Arguments<T extends Arguments> {
    public static class Arg<T> {

        public final String param;
        public final String description;
        String value;
        public final String def;
        public final String prop;
        public final Is is;
        public final Class type;
        public final boolean askable;

        public String name() {
            return prop;
        }

        public boolean isTrue() {
            return Boolean.parseBoolean(getValue().toString());
        }

//        public String getValue() {
//            // If it is required, return whatever it is set to
//            // otherwise return default whe not set
//            return is.required() ? value : value == null ? def : value;
//        }

        public String getValue() {
            // If it is required, return whatever it is set to
            // otherwise return default whe not set
            return is.required ? value : value == null ? def : value;
        }

        public Integer getInteger() {
            // If it is required, return whatever it is set to
            // otherwise return default whe not set
            return Integer.parseInt(getValue().toString());
        }

        public boolean isSet() {
            return value != null;
        }

        @Override
        public String toString() {
            return String.format("%-15s %-7s    %s%s",
                    param,
                    type != null ? type == Boolean.class ? "" : type.getSimpleName().replace("java.lang.", "") : "",
                    description,
                    def != null ? (", default=" + def) : "");
        }

        Arg(String param, String prop, Class type, Is required, String description, String def) {
            this(param, prop, type, required, description, def, Askable.No);

        }

        public enum Is {
            Optional(false),
            Required(true);
            private final boolean required;

            private Is(boolean required) {
                this.required = required;
            }
        }
        public enum Askable {
            /**
             * An argument that is not askable can only be set on commandline,
             * or in properties -- not be asked interactively.
             */
            No(false),
            /**
             * An argument that is askable can be asked interactively when set
             * to questionmark, "?".
             */
            Yes(true);
            private final boolean askable;

            private Askable(boolean askable) {
                this.askable = askable;
            }
        }
        Arg(String param, String prop, Class type, Is required, String description, String def, Askable askable) {
            this.param = param;
            this.type = type;
            this.prop = prop;
            this.description = description;
            this.is = required;
            this.def = def;
            this.askable = askable.askable;
        }
    }

    public Arguments (String [] args) throws Exception {

    }

    public Arguments () {

    }


    public final Arg Properties = new Arg("-prop", null, String.class, Arg.Is.Optional, "Name of the properties file.", null);
    public final Arg PropertiesImports = new Arg("-prop.imports", "properties.imports", String.class, Arg.Is.Optional, "Comma separated list of more properties files to include", null, Arg.Askable.No);
    public final Arg PropertiesVariable = new Arg("-var", "properties.var", String.class, Arg.Is.Optional, "Comma separated list of property substitution varaibles. F.i. \"customer=acme\", will replace all occurences of %customer% in a properties file with \"acme\".", null, Arg.Askable.No);
    public final Arg Verbose = new Arg("-verbose", null, Boolean.class, Arg.Is.Optional, "Be verbose.", null);
    public final Arg VeryVerbose = new Arg("-veryverbose", null, Boolean.class, Arg.Is.Optional, "Be very verbose.", null);
    public final Arg Help = new Arg("-h", "help", Boolean.class, Arg.Is.Optional, "Get help.", "true");


    private static final Logger logger = System.getLogger("BugsBunny");

    private Arg[] init() {
        int i=0;
        for(Field f : this.getClass().getFields()) {
            if(f.getType().equals(Arg.class)) {
                i++;
            }
        }
        Arg[] values = new Arg[i];
        i = 0;
        for(Field f : this.getClass().getFields()) {
            if(f.getType().equals(Arg.class)) {
                try {
                    values[i++] = (Arg)f.get(this);
                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
        return values;
    }

    Arg[] values() {
        return init();
    }
    private final Properties properties = new Properties();
    public Properties getProperties() {
        return properties;
    }

    /**
     * Prints how to use the program.
     * @return
     */
    public String getUsage() {
        StringBuilder str = new StringBuilder("Usage:\n");
        Arguments temp = this;
        for (Arg arg : this.values()) {
            str.append(arg != null ? arg.toString(): "arg is null!!!!!");
            str.append("\n");
        }
        return str.toString();
    }


    /**
     * Add values from properties to teh arguments.
     * @param props
     */
    private void loadProperties(Properties props) {
        // check that all properties are valid
        outer:
        for (String prop : props.stringPropertyNames()) {
            for (Arg arg : values()) {
                if (prop.equals(arg.prop)) {
                    continue outer;
                } else if(arg.prop != null && arg.prop.endsWith("*") && prop.startsWith(arg.prop.replace("*", ""))) {
                    continue outer;
                }
            }

            throw new IllegalArgumentException("Unknown property: " + prop);
        }
        for (Arg arg : values()) {
            if (arg.prop != null) {
                if (props.getProperty(arg.prop) != null) {
//                    if(arg.type == DatePattern.class) {
//                        arg.value = DatePattern.ParseDatePatternArgument(props.getProperty(arg.prop, arg.def), Calendar.getInstance());
//                    } else {
                        arg.value = props.getProperty(arg.prop, arg.def);
//                    }

                    logger.log(Logger.Level.INFO, "Prop: " + arg.prop + " : " + arg.value);
                }
            }
        }
        for(String prop : props.stringPropertyNames()) {
            properties.setProperty(prop, props.getProperty(prop));
        }
    }

    /**
     * Interactively ask user to enter value for arguments set to question mark.
     */
    private void askForAskables() {

        BufferedReader reader = null;
        try {
            for (Arg inarg : values()) {
              if(inarg == null) {
                    continue;
                }
                if (inarg.askable
                        && ((!inarg.isSet() && inarg.is.required) || (inarg.isSet() && inarg.value.equals("?"))
                        || (PropertiesVariable.prop.equals(inarg.prop) && inarg.value != null && inarg.value.contains("?")))) {
                    if(inarg == PropertiesVariable) {
                        // if a properties variable has a question mark,
                        // we ask for that variable
                        String newValue = "";
                        for(String var_value : inarg.value.split(",")) {
                            String varname = var_value.split("=")[0];
                            String value = var_value.split("=")[1];
                            if(value.equals("?")) {
                                System.out.print("Enter value for variable \"" + varname + "\": ");
                                if (reader == null) {
                                    reader = new BufferedReader(new InputStreamReader(System.in));
                                }
                                try {
                                    value = reader.readLine();
                                } catch (IOException ex) {

                                }
                            }
                            newValue += varname+"="+value+",";
                        }
                        inarg.value = newValue;
                    } else {
                        System.out.println(inarg.name() + ": " + inarg.description);
                        System.out.print("Enter value for " + inarg.name() + ": ");
                        if (reader == null) {
                            reader = new BufferedReader(new InputStreamReader(System.in));
                        }
                        try {
                            inarg.value = reader.readLine();
                        } catch (IOException ex) {
                            System.out.println("Abort, setting to default: " + inarg.def);
                            inarg.value = inarg.def;
                        }
                    }

                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
    }
    private static boolean throwException = true;
    /**
     * Parse command line arguments.
     * @param args
     * @throws Exception
     */
    private T parseArgsNoException(String... args) throws Exception {
        throwException = false;
        return parseArgs(args);
    }

    T parseArgs(String... args) throws Exception {
        if(args == null) return (T)this;
        for (Arg inarg : values()) {
            if(inarg != null) {
                inarg.value = null;
            }
        }
        boolean found = false;
        int idx = 0;
        while (idx < args.length) {
            found = false;
            for (Arg inarg : values()) {
                if(inarg == null) {
                    continue;
                }
                if (inarg.param != null && inarg.param.equals(args[idx])) {
                    found = true;
                    if (inarg.type == Boolean.class) {
                        inarg.value = "true";
                    } else {
                        idx++;
                        if (idx < args.length) {
//                            if(inarg.type == DatePattern.class) {
//                                inarg.value = DatePattern.ParseDatePatternArgument(args[idx], Calendar.getInstance());
//                                logger.info("Arg: " + inarg.prop + " : " + inarg.value);
//                            } else  {
                                inarg.value = args[idx];
                                logger.log(Logger.Level.INFO, "Arg: " + inarg.prop + " : " + inarg.value);
//                            }

                        }
                    }
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Unknown argument: " + args[idx]);
            }

            idx++;
        }

        if(this.Help.isSet() && Help.isTrue()) {
          System.out.println( getUsage() );
          System.exit(0);
        }
        // Add all set arguemnt as properties
        for(Arg arg : values()) {
            if(arg == null) {
                continue;
            }
            if (arg.prop != null) {
                if (arg.isSet()) {
                    properties.setProperty(arg.prop, arg.value);
                }
            }
        }
        if (Properties.isSet()) {
            Properties properties = new Properties();
            File f = new File(Properties.value);
            logger.log(Logger.Level.INFO, "Loading properties from: " + f.getAbsolutePath());
            properties.load(new FileReader(f));
            loadProperties(properties);
        }
        if (PropertiesImports.isSet()) {
            for(String filename : PropertiesImports.value.split(",")) {
                Properties properties = new Properties();
                File f = new File(filename);
                logger.log(Logger.Level.INFO, "Loading properties from: " + f.getAbsolutePath());
                properties.load(new FileReader(f));
                loadProperties(properties);
            }
        }

        askForAskables();

        final Map<String,String> replacements = new HashMap<String, String>();
        if(PropertiesVariable.isSet()) {
            for(String var_value : PropertiesVariable.value.split(",")) {
                replacements.put(var_value.split("=")[0], var_value.split("=")[1]);
            }
        }


        for (Arg arg : values()) {
            if(arg == null) {
                continue;
            }

            if (throwException && arg.is.required && arg.value == null) {
                throw new IllegalArgumentException("Required argument missing: " + arg.toString());
            }

            // replace variable values
            for(String var : replacements.keySet()) {
                if(arg.value != null && arg.value.contains("%"+var+"%")) {
                    arg.value = arg.value.replace("%"+var+"%", replacements.get(var));
                }
            }
        }
        return (T)this;
    }

    /**
     * Generate the properties file content.
     */
    static void generatePropertiesContent() throws Exception {
        Arguments temp = new Arguments(null);
        for (Arg arg : temp.values()) {
            if (arg.prop != null) {
                System.out.println("# Property: " + arg.prop + " (type: " + arg.type.getSimpleName() + ")" + (arg.is.required ? " mandatory" : " optional"));
                System.out.println("# Command argument: " + arg.param);
                System.out.println("# Description: " + arg.description);
                if (arg.askable) {
                    System.out.println("#              Set to \"?\" to prompt for value interactively.");
                }
                System.out.println((arg.is.required ? "" : "#") + arg.prop + "=" + (arg.def != null ? arg.def : ""));
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Arguments.generatePropertiesContent();
    }

}