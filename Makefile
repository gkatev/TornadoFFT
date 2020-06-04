tornado = ../tornadovm/bin/bin/tornado
javac = $(JAVA_HOME)/bin/javac

# --------------

libs += $(wildcard lib/*.jar)
# libs += ../tornadovm/tornado-api/target/tornado-api-0.6-5b7f88e.jar
libs += ../tornadovm/tornado-api/target/tornado-api-0.6-1c115f1.jar
lib = $(subst $() $(),:,$(libs))

CLASS = class
SRC = src

sources = $(shell find $(SRC) -type f -name *.java)
classes = $(shell echo "$(sources)" | sed "s|$(SRC)|$(CLASS)|g" | sed 's/\.java/\.class/g')

# --------------

profiler_options = -XX:StartFlightRecording=dumponexit=true,filename=fft.jfr

# --------------

all: $(classes)
	@

run: $(classes)
	$(tornado) -cp $(lib):$(CLASS) FFTImage $(img)

run-profiler: $(classes)
	$(tornado) $(profiler_options) -cp $(lib):$(CLASS) FFTImage $(img)

$(classes): $(sources) $(CLASS) Makefile
	$(javac) -cp $(lib) -d $(CLASS) $(sources)

$(CLASS):
	mkdir $(CLASS)

clean:
	rm -r $(CLASS)
