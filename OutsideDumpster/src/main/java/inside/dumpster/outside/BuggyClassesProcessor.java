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
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuggyClassesProcessor extends AbstractProcessor {
  static final String BUGGY_CLASSES_RESOURCENAME = "buggyclasses.properties";

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if(roundEnv.errorRaised()) {
      System.out.println("Something went wrong");
    }
    if(roundEnv.processingOver()) {
      return true;
    }

    try {
      FileObject fileObject;
      fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "",
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
