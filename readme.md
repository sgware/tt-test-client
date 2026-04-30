# Tandem Tales Test Client #

A text-based client written in Java that connects to a
[Tandem Tales Server](https://github.com/sgware/tt-server) from the terminal.
Each time a turn is taken, this client prints out a short update. When it is
this agent's turn, the client prints out a numbered list of all available turns,
making it easy to explore the range of possabilities available in a Tandem Tales
story world.

## Pre-Compiled Executable

The JAR file containing the client executable and all dependencies can be
[downloaded here](jar).

## Build from Source

The Tandem Tales server is written in Java and published as a Maven project. It
depends on the [Tandem Tales Server](https://github.com/sgware/tt-server).

Assuming you have [Git](https://git-scm.com/install), the
[Java Development Kit](https://www.oracle.com/java/technologies/downloads/), and
[Maven](https://maven.apache.org/) installed and on your path, you can download
the dependency and compile Tandem Tales Test Client from source like this.
```
git clone https://github.com/sgware/tt-server.git
cd tt-server
mvn clean install
cd ..
git clone https://github.com/sgware/tt-test-client.git
cd tt-test-client
mvn clean install
```

## Usage

Tandem Tales uses secure sockets via the
[Java Secure Socket Extension (JSSE)](https://docs.oracle.com/en/java/javase/25/security/java-secure-socket-extension-jsse-reference-guide.html).
When connecting to a public server, it should have a certificate issued by a
trusted authority.

An example of how to create a self-signed certificate and export a public key
for testing purposes is included in the
[server readme](https://github.com/sgware/tt-server/blob/main/readme.md).
Assuming you followed that example, you need to copy `test.cer` into this
project's root directory and then execute these commands. Replace `***` below
with a secure password.
```
keytool -importcert -keystore client.truststore -storepass *** -alias test -file test.cer
```

The above command creates a file called `client.truststore`, which stores the
server's public key as a trusted endpoint.

Assuming you are in the project root directory, you can show the test client
usage message like this.
```
java -jar jar/tt-test-client.jar -help
```

Assuming an instance of the server is running on `localhost` and that it has
a world named `tutorial`, you can connect as the game master to play in that
world using the trusted self-signed security certificate like this. Replace
`***` below with the trust store password used above.
```
java -Djavax.net.ssl.trustStore="client.truststore" -Djavax.net.ssl.trustStorePassword="***" -jar jar/tt-test-client.jar -world tutorial -gm
```

To connect as the player, open a separate terminal window and navigate to the
project's root directory.
```
java -Djavax.net.ssl.trustStore="client.truststore" -Djavax.net.ssl.trustStorePassword="***" -jar jar/tt-test-client.jar -world tutorial -player
```

## Documentation

The JavaDoc API for all Java source files can be
[found here](http://sgware.github.io/tt-test-client).

## License ##

Tandem Tales was developed by Stephen G. Ware PhD, Associate Professor of
Computer Science at the University of Kentucky. Development was sponsored in
part by a grant from the US National Science Foundation, #2145153.

This test client is released under the GNU General Public License version 3.0
(GPL 3). This means you are free to share and modify this software, even for
commercial purposes, as long as you give credit to the original creators and you
also release your modifications under the GPL 3 license. See the license file
for details. The University of Kentucky retains all right not specifically
granted.

To license Tandem Tales for a project not compatible with the terms of the GPL
license, contact the University of Kentucky Office of Technology
Commercialization at <otcinfo@uky.edu>.