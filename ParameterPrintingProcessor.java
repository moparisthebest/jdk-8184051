import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("javax.annotation.processing.SupportedSourceVersion")
public class ParameterPrintingProcessor extends AbstractProcessor {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        if (annotations.isEmpty() || roundEnv.processingOver())
            return false; // done
        for (final Element element : roundEnv.getElementsAnnotatedWith(SupportedSourceVersion.class)) {
            for(final Element method : ((TypeElement) element).getEnclosedElements()) {
                if(method.getKind() != ElementKind.METHOD)
                    continue;
                final TypeMirror returnType = ((ExecutableElement)method).getReturnType();
                if(returnType.getKind() != TypeKind.DECLARED)
                    continue;
                final List<? extends Element> methodsAndConstructors = ((TypeElement) ((DeclaredType) returnType).asElement()).getEnclosedElements();
                processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                        methodsAndConstructors.stream().filter(e -> e.getKind() == ElementKind.CONSTRUCTOR && e.getModifiers().contains(Modifier.PUBLIC)).map(e -> e.toString() +
                                ": '" + ((ExecutableElement) e).getParameters().stream().map(param -> param.asType() + " " + param.getSimpleName().toString()).collect(java.util.stream.Collectors.joining(", ")) + "'"
                        ).collect(java.util.stream.Collectors.joining(", ")));
            }
        }
        return false;
    }
}
