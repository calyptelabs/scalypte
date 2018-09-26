# Calypte
Cache de propósito geral com suporte transacional.

# Como instalar o servidor?

1. git clone https://github.com/calyptelabs/scalypte.git
2. mvn clean package
3. java -Xmx128m -jar scalypte-1.0.0.0.jar

Mais informações: https://calyptelabs.github.io/install.html

# Como Instalar o cliente?
 
## Instalando com maven

Você pode instalar o cliente Calypte usando o maven.

Adicione no pom.xml:

```
<dependency>
	<groupId>calypte</groupId>
	<artifactId>calypte</artifactId>
	<version>1.0.0.0</version>
</dependency>
```

Mais informações: https://calyptelabs.github.io/java.html

## Instalando com composer

Você pode instalar o cliente Calypte usando o composer:

```
$ composer require calypte/calypte
```

Mais informações: https://calyptelabs.github.io/php.html

Veja mais em: https://calyptelabs.github.io/
