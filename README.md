# Calypte
Calypte é um sistema de cache de propósito geral com suporte transacional. É extremamente rápido, trabalha de forma eficiente com pouca memória e permite o armazenamento de dados em memória secundária.

# Como instalar o servidor?

1. git clone https://github.com/calyptelabs/scalypte.git
2. mvn clean package
3. java -Xmx300m -jar scalypte-1.0.2.1.jar

Mais informações: https://calyptelabs.github.io/install.html

# Como Instalar o cliente?
 
## Instalando com maven

Você pode instalar o cliente Calypte usando o maven.

Adicione no pom.xml:

```
<repositories>
	<repository>
	<id>calypte-repo</id>
		<name>Calypte repository.</name>
		<url>https://calypte.sourceforge.io/maven/2</url>
	</repository>
</repositories>

...

<dependency>
	<groupId>calypte</groupId>
	<artifactId>calypte</artifactId>
	<version>1.0.2.1</version>
</dependency>
```

Mais informações: https://calypte.sourceforge.io/java.html

## Instalando com composer

Você pode instalar o cliente Calypte usando o composer:

```
$ composer require calypte/calypte
```

Mais informações: https://calypte.sourceforge.io/php.html

Veja mais em: https://calypte.sourceforge.io/
