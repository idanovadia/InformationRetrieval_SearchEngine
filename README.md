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

> When using BM25 and Cosine Similarity we need to take all the tf and df from our indexer and make whights tf idf, 
