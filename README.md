# CranSearchEngine [![Build Status](https://travis-ci.org/DonatoMeoli/CranSearchEngine.svg?branch=master)](https://travis-ci.org/DonatoMeoli/CranSearchEngine)

CranSearchEngine is a search engine for the [Cranfield collection](http://ir.dcs.gla.ac.uk/resources/test_collections/cran) 
developed during the Models and Methods for Information Retrieval course @ [Department of Computer Science](http://www.uniba.it/ricerca/dipartimenti/informatica) @ [University of Bari "Aldo Moro"](http://www.uniba.it/).

## Prerequisites

To run the software you must extract the collection folder from the collection.tar.gz archive placed into cran directory by typing into the terminal:

```
$ tar zxvf collection.tar.gz
```

## Built with Maven

```
$ mvn clean compile assembly:single
```

## Running the JAR

```
$ java -jar target/cran-search-engine-1.0-jar-with-dependencies.jar
```

## Running the tests

To view the [evaluation measure](https://en.wikipedia.org/wiki/Information_retrieval#Performance_and_correctness_measures) such as Precision, Recall, MAP, GMAP, etc., go to the root folder and run the evaluation.sh file from the terminal by typing:

```
$ ./evaluation.sh
```

## License [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This software is released under the MIT License. See the [LICENSE](LICENSE) file for details.
