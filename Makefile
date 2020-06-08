TORNADO_HOME = ../tornadovm

tornado = $(TORNADO_HOME)/bin/bin/tornado
javac = $(JAVA_HOME)/bin/javac

# --------------

libs += $(wildcard lib/*.jar)
libs += $(shell find $(TORNADO_HOME)/tornado-api/target -name "tornado-api-*.jar" | head -n 1)
lib = $(subst $() $(),:,$(libs))

CLASS = class
SRC = src

sources = $(shell find $(SRC) -type f -name *.java)
classes = $(shell echo "$(sources)" | sed "s|$(SRC)|$(CLASS)|g" | sed 's/\.java/\.class/g')

# --------------

tornado_options = -Dtornado.heap.allocation=2GB
# -Dtornado.fullDebug=true
# -Dtornado.profiler=true
# --debug

profiler_options = $(tornado_options) -XX:StartFlightRecording=dumponexit=true,filename=fft.jfr

# --------------

all: $(classes)
	@

run: $(classes)
	$(tornado) $(tornado_options) -cp $(lib):$(CLASS) FFTImage $(img)

run-profiler: $(classes)
	$(tornado) $(profiler_options) -cp $(lib):$(CLASS) FFTImage $(img)

$(classes): $(sources) $(CLASS) Makefile
	$(javac) -cp $(lib) -d $(CLASS) $(sources)

$(CLASS):
	mkdir $(CLASS)

clean:
	rm -rf $(CLASS)
