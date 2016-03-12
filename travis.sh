echo "Installing dependencies not found in maven central"

echo "Installing Dependometer dependencies"
mvn install:install-file -Dfile=dependencies/org.eclipse.cdt.core-5.3.1.201109151620.jar -DpomFile=dependencies/org.eclipse.cdt.core-5.3.1.201109151620.pom

mvn install:install-file -Dfile=dependencies/dependometer-core-1.2.9.jar -DpomFile=dependencies/dependometer-core-1.2.9.pom

mvn install:install-file -Dfile=dependencies/dependometer-cpp-1.2.9.jar -DpomFile=dependencies/dependometer-cpp-1.2.9.pom

mvn install:install-file -Dfile=dependencies/dependometer-java-1.2.9.jar -DpomFile=dependencies/dependometer-java-1.2.9.pom

mvn install:install-file -Dfile=dependencies/dependencyfinder-jar-1.2.3.jar -DpomFile=dependencies/dependencyfinder-jar-1.2.3.pom

mvn install:install-file -Dfile=dependencies/ckjm-1.9.1.jar -DpomFile=dependencies/ckjm-1.9.1.pom


echo "Building all modules, ignoring  tools/peixe-espada until solving its dependencies"
mvn -T 1C -pl '!tools/peixe-espada' -DskipTests -fae clean install


