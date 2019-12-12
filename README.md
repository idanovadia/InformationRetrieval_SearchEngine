# InformationRetrieval_SearchEngine

 ### Description

> This project is implementation of Search Engine ,
> It include two main parts :
> - Creating Indexer - by parsing all data in our corpus, we created inverted files, it help to retrieve data faster and more accurate.
>	  - doc -> term,  count 
>   - term -> doc
>   - doc  - >  lines number , count 
>   
>   sort all Inxer for better performence
>  - Using information retrieval algorithms for more accurate results

>  ### Information retrieval algorithms 

>   - Cosine Similarity
>   ![alt text](https://github.com/idanovadia/InformationRetrieval_SearchEngine/blob/master/markdownPhostos/cosim.png)
>   - BM25
>   ![BN25](https://github.com/idanovadia/InformationRetrieval_SearchEngine/blob/master/markdownPhostos/bm25.png)
>   - Semantics for better results.
>   - Headers of the docs.

> ## Results 
> - All the results presents :  Query, Precision and Recall.

## Departments
> - Doc - have all the information on each document that exsit in our database.
> - Parser - analyzing documents, make words to phrases and create dict from them, In addition it use to parse queries.
> - Term - presenting Phrase in the data as objects.
> - Stemmer - using to do stemming on phrases ( user option ).
> - Pointer - pointer in the dict.
> - Indexer - store the inverted indexes, cache dict and posting files. ( Singleton ).
> - City - presenting a city and it feature.
> - CityIndexer - index of cities ( Singleton).
> - Controller - connecting the program to GUI.
> - Query - present a query that use in the search engine.
> - QueryFile - query as file ( parsing the file and create set of queries ).
> - DocComperator - parse the query file.
> - RankingInstance - help to get all the information for ranking term.
> - RankingObject - present a Doc for ranking.
> - IRankerFunction - interface for all the Ranking classes.
> - ARankFunction - abatract function that implement IRankerFunction.
> - BM25 - using to rank docs by using bm25 function.


## External Libs
> - Jsoup 
> - snowball Stemmer
> - OkHttp
> - json-simple
> - Apache-commoms
> - Rest Countries API
