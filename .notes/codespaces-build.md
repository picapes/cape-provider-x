## Commands to build the mod from GitHub Codespaces
```
sdk install java 21.0.8-tem -y
(Enter Y when it asks for default)
chmod +x ./gradlew
./gradlew clean build
```
Mod JAR file will be at `.gradle/build` after compilation.
Use the JAR without `-sources` or `-javadoc`