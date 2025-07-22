# Variables de directorios y archivos
CLASS_INPUT = ./Font/classes/*.java \
              ./Font/domaincontrollers/*.java \
              ./Font/Exceptions/*.java \
              ./Font/datacontrollers/*.java \
              ./Font/presentation/*.java \
              ./Font/forms/*.java

CLASS_OUTPUT = ./EXE/classes/
CLASS_OUTPUT_TEST =./EXE/classes/
JSON_OUTPUT = ./EXE/classes/
JAR_OUTPUT = ./EXE/
JUNIT_JARS = ./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar:./lib/mockito-core-4.9.0.jar:./lib/objenesis-3.3.jar:./lib/byte-buddy-agent-1.12.16.jar:./lib/byte-buddy-1.12.16.jar

JSON_JAR = ./lib/json-simple-1.1.1.jar
FORMS_RT_JAR = ./lib/forms_rt.jar

# Archivos de pruebas
JUNIT_TESTS = ./Font/Tests/*.java
JUNIT_TEST_CLASSES = $(subst /,.,$(patsubst ./%.java,%,$(wildcard ./Font/Tests/*.java)))


# Ejecutable principal
MAIN_PROGRAM = ./Font/Main.java
MAIN_VISUAL_PROGRAM = ./Font/MainVisual.java

# Compilación general del proyecto
all:
	mkdir -p $(CLASS_OUTPUT)
		javac -d $(CLASS_OUTPUT) -cp $(JSON_JAR):$(FORMS_RT_JAR):$(CLASS_OUTPUT) $(CLASS_INPUT) $(MAIN_PROGRAM) $(MAIN_VISUAL_PROGRAM)

# Crear todos los archivos jar para los drivers
jars:
	mkdir -p $(CLASS_OUTPUT)
	javac -cp $(JSON_JAR):$(FORMS_RT_JAR):$(CLASS_OUTPUT) -d $(CLASS_OUTPUT) $(CLASS_INPUT) $(MAIN_PROGRAM)
	jar cmvf ./Font/manifest.txt $(JAR_OUTPUT)MainProgram.jar -C $(CLASS_OUTPUT) .

jars-visual:
	mkdir -p $(CLASS_OUTPUT)
	javac -cp $(JSON_JAR):$(FORMS_RT_JAR):$(CLASS_OUTPUT) -d $(CLASS_OUTPUT) $(CLASS_INPUT) $(MAIN_VISUAL_PROGRAM)
	jar cmvf ./Font/manifest_visual.txt $(JAR_OUTPUT)MainVisualProgram.jar -C $(CLASS_OUTPUT) .

execute:
	java -cp $(CLASS_OUTPUT):$(JSON_JAR):$(FORMS_RT_JAR) Font.Main

execute-visual:
	java -jar $(JAR_OUTPUT)MainVisualProgram.jar

# Compilar y ejecutar todas las pruebas con JUnit
fulltest: all
	# Compilar las pruebas JUnit
	javac -cp $(JUNIT_JARS):$(CLASS_OUTPUT) -d $(CLASS_OUTPUT) $(JUNIT_TESTS)
	# Ejecutar las pruebas JUnit
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT_TEST) org.junit.runner.JUnitCore  Font.Tests.UsuarioTest Font.Tests.AlgoritmoFuerzaBrutaTest Font.Tests.AlgoritmoAproximadoKruskalTest   Font.Tests.GestorAlgoritmoTest  Font.Tests.GestorProductosTests Font.Tests.GestorSimilitudesTest Font.Tests.GrupoEstanteriaOrdenadaTestSuite Font.Tests.GrupoSimilitudesTestSuite Font.Tests.ProductoTest Font.Tests.GestorUsuariosTest

# Ejecutar el programa principal
run:
	@if [ -z "$(FILE)" ]; then \
		echo "Por favor, proporciona el nombre del fichero de entrada usando FILE=<nombre_fichero>"; \
		exit 1; \
	fi
	java -jar $(JAR_OUTPUT)MainProgram.jar < $(FILE)

# Limpiar archivos compilados
clean:
	rm -rf $(CLASS_OUTPUT)/*
