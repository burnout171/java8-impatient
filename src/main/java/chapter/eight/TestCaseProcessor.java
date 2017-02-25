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
        System.out.println("Start to process with: ");
        System.out.println(roundEnv);

        StringBuilder builder = new StringBuilder();
        annotations.forEach(annotation ->
            roundEnv.getElementsAnnotatedWith(annotation).forEach(element -> {
                TestCase testCase = element.getAnnotation(TestCase.class);
                if (testCase == null) {
                    return;
                }
                String testLine =
                        String.format("\t\tnew %s().%s(%d)==%d ? System.out.println(\"Passed\") : System.out.println(\"Failed\");\n",
                                element.getEnclosingElement().getSimpleName(), element.getSimpleName(), testCase.argument(), testCase.expected());
                builder.append(testLine);
            })
        );

        return prepareAndRun(builder.toString());
    }

    private boolean prepareAndRun(final String body) {
        try {
            JavaFileObject javaFile = processingEnv.getFiler().createSourceFile("Test");
            try (PrintWriter writer = new PrintWriter(javaFile.openWriter())) {
                writer.println("import static chapter.eight.*;");
                writer.println("public class Chapter8Test {");
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
