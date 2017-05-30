programs = $(wildcard src/raytracer/*/*.java)

compile: FORCE
	mkdir -p out
	javac -d out  $(programs)

run: compile
	java -cp out raytracer.raytracer.Main

clean: FORCE
	rm -r out

.PHONY: FORCE
