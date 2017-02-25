package chapter.eight;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({"chapter.eight.TestCase"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TestCaseProcessor extends AbstractProcessor {
    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
                           final RoundEnvironment roundEnv) {
        System.out.println("Start to process.");

        StringBuilder builder = new StringBuilder();
        annotations.forEach(annotation ->
            roundEnv.getElementsAnnotatedWith(annotation).forEach(element -> {
                TestCase testCase = element.getAnnotation(TestCase.class);
                if (testCase == null) {
                    return;
                }
                String fullName = ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
                String testLine =
                        String.format("\t\tif(new %s().%s(%d)==%d) System.out.println(\"Passed\"); else System.out.println(\"Failed\");\n",
                                fullName, element.getSimpleName(), testCase.argument(), testCase.expected());
                builder.append(testLine);
            })
        );

        return prepareAndRun(builder.toString());
    }

    private boolean prepareAndRun(final String body) {
        String className = "AnnotationProcessorTest";
        try {
            JavaFileObject javaFile = processingEnv.getFiler().createSourceFile(className);
            try (PrintWriter writer = new PrintWriter(javaFile.openWriter())) {
                writer.println(String.format("public class %s {", className));
                writer.println("\tpublic static void main(String[] args) {");
                writer.println(body);
                writer.println("\t}");
                writer.println("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
