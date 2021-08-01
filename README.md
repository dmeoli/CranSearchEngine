# CranSearchEngine [![Build Status](https://travis-ci.org/dmeoli/CranSearchEngine.svg?branch=master)](https://travis-ci.org/dmeoli/CranSearchEngine)

CranSearchEngine is a search engine for the [Cranfield collection](http://ir.dcs.gla.ac.uk/resources/test_collections/cran) 
developed during the Information Retrieval course @ [Department of Computer Science](http://www.uniba.it/ricerca/dipartimenti/informatica) 
@ [University of Bari "Aldo Moro"](http://www.uniba.it/) under the supervision of dr. [Pierpaolo Basile](http://www.di.uniba.it/~swap/index.php?n=Membri.Basile).

## Prerequisites

By default, requirement for compilation are:

 - JDK 8+
 - Maven

## Built with Maven

To create a jar file with dependencies:

```
$ mvn install
```

## Running the JAR

```
$ java -jar target/cran-search-engine-1.0-jar-with-dependencies.jar
```

## View Evaluation Measures

To view the [evaluation measures](https://en.wikipedia.org/wiki/Information_retrieval#Performance_and_correctness_measures) 
such as Precision, Recall, MAP, GMAP, etc., go to the root folder and run the evaluation.sh file from the terminal by typing:

```
$ ./evaluation.sh
```

## License [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This software is released under the MIT License. See the [LICENSE](LICENSE) file for details.
