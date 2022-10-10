/*
 * 
 */
package inside.dumpster.outside;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
@SupportedAnnotationTypes("inside.dumpster.outside.Buggy")
@SupportedSourceVersion(SourceVersion.RELEASE_13)
public class BuggyClassesProcessor extends AbstractProcessor {

  public BuggyClassesProcessor() {
    System.out.println("Buggy processor cosntr");
  }

  static final String BUGGY_CLASSES_RESOURCENAME = "buggyclasses.properties";
  
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    System.out.println("Process:" + annotations + " re:" + roundEnv.toString());
    if(roundEnv.errorRaised()) {
      System.out.println("Something went wrong");
    }
    if(roundEnv.processingOver()) {
      return true;
    }
    for(Element element : roundEnv.getRootElements()) {
      System.out.println("Hepp: "+element.getSimpleName());
      if(element.getSimpleName().contentEquals("Dooby")) {
        return true;
    }
  }
    
    try {
      
      FileObject fileObject;
      fileObject = processingEnv.getFiler().createSourceFile("inside.dumpster.Dooby");
      try (Writer writter = fileObject.openWriter()) {
        writter.append("package inside.dumpster;\n");
        writter.append("public class Dooby {\n");
        writter.append("  public static final String[] bugs = {\n");
        for (final Element element : roundEnv.getRootElements()){//getElementsAnnotatedWith(Buggy.class)) {
          if (element instanceof TypeElement) {
            final TypeElement typeElement = (TypeElement) element;
            Buggy buggy = typeElement.getAnnotation(Buggy.class);
            if(buggy != null) {
              // buggy class
              writter.append("\""+typeElement.getQualifiedName().toString()+"\",\n") ;
            }
            for(Element innerElement : typeElement.getEnclosedElements()) {
              System.out.println("  Inner: "+innerElement.getSimpleName().toString());
              Buggy innerBug = innerElement.getAnnotation(Buggy.class);
              if(innerBug != null) {
                // buggy method, or var etc
                writter.append("\"");
                writter.append(typeElement.getQualifiedName().toString()+"."+innerElement.getSimpleName().toString()+"\n") ;
                writter.append("\",\n");
              }
            }
          }
        }
        writter.append("  }; //String[] bugs \n");
        writter.append("} //public class Dooby \n");
      }
    } catch (final IOException ex) {
      processingEnv.getMessager().printMessage(Kind.ERROR, ex.getMessage());
    } catch (final Exception ex) {
      processingEnv.getMessager().printMessage(Kind.ERROR, ex.getMessage());
    }
    
    
    try {
      
      FileObject fileObject;
      fileObject = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", 
              BUGGY_CLASSES_RESOURCENAME);
      try (Writer writter = fileObject.openWriter()) {

        for (final Element element : roundEnv.getRootElements()){//getElementsAnnotatedWith(Buggy.class)) {
          if (element instanceof TypeElement) {
            final TypeElement typeElement = (TypeElement) element;
            Buggy buggy = typeElement.getAnnotation(Buggy.class);
            if(buggy != null) {
              // buggy class
              writter.append(typeElement.getQualifiedName().toString()+"\n") ;
            }
            for(Element innerElement : typeElement.getEnclosedElements()) {
              System.out.println("  Inner: "+innerElement.getSimpleName().toString());
              Buggy innerBug = innerElement.getAnnotation(Buggy.class);
              if(innerBug != null) {
                // buggy method, or var etc
                writter.append(typeElement.getQualifiedName().toString()+"."+innerElement.getSimpleName().toString()+"\n") ;
              }
            }
            
          }
        }
      }
    } catch (final IOException ex) {
      processingEnv.getMessager().printMessage(Kind.ERROR, ex.getMessage());
    } catch (final Exception ex) {
      processingEnv.getMessager().printMessage(Kind.ERROR, ex.getMessage());
    }
    return true;
  }

}
