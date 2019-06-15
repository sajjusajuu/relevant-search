package com.swiggy.search.datalayer.index;

import com.swiggy.search.api.SongDoc;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Index {


    private String indexDir = "index";
    private IndexWriter indexWriter;
    private IndexSearcher indexSearcher;

    public Index() throws IOException {
        Directory directory = FSDirectory.open(new File(indexDir));
        indexWriter = new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30),
                true, IndexWriter.MaxFieldLength.UNLIMITED);
        openSearcher();
    }

    public void index(SongDoc songDoc) throws IOException, IllegalAccessException {

        Document document = getDocument(songDoc);
        indexWriter.addDocument(document);
        indexWriter.commit();
        openSearcher();
    }

    public void indexBatch(List<SongDoc> songDocs) throws IOException, IllegalAccessException {

        if (CollectionUtils.isNotEmpty(songDocs)) {
            for (SongDoc songDoc : songDocs) {
                Document document = getDocument(songDoc);
                indexWriter.addDocument(document);
            }
        }
        indexWriter.commit();
        openSearcher();
    }

    public void delete(SongDoc doc) throws IOException {
        Term term = new Term("id" , doc.getId());
        indexWriter.deleteDocuments(term);
    }


    public List<String> search(String queryString) throws ParseException, IOException {

        QueryParser queryParser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));
        Query query = queryParser.parse(queryString);
        List<String> results = new ArrayList<>();
        TopDocs hits = indexSearcher.search(query, 10);
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            String title = document.get("title");
            results.add(title);
        }
        return results;
    }

    private void openSearcher() throws IOException {
        Directory directory = FSDirectory.open(new File(indexDir));
        IndexSearcher indexSearcher = new IndexSearcher(directory);
    }

    private Document getDocument(SongDoc songDoc) throws IllegalAccessException {

        Document document = new Document();
        StringBuffer stringBuffer = new StringBuffer();
        java.lang.reflect.Field[] fields = SongDoc.class.getFields();
        for(java.lang.reflect.Field field : fields) {
            Object value = field.get(songDoc);
            if(value instanceof  List) {
                List<String> list = (List<String>) value;
                list.forEach(str -> {
                    stringBuffer.append(" " + str);
                });
            }
            else if(value instanceof String) {
                stringBuffer.append(" " + value);
            }
        }
        String content = stringBuffer.toString();
        document.add(new Field("content" , new StringReader(content)));
        document.add(new Field("id" , songDoc.getGenre(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("title" , songDoc.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("artist" , songDoc.getArtist().toString(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("genre" , songDoc.getGenre(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("tags" , songDoc.getTags().toString(), Field.Store.YES, Field.Index.ANALYZED));
        return document;
    }
}
