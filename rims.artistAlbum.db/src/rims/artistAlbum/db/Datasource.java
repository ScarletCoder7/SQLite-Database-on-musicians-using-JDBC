package rims.artistAlbum.db;



import rims.artistAlbum.common.ArtistClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME="artist.db";
    public static final String CONNECTION_STRING= "jdbc:sqlite:"+DB_NAME;
    public static final String TABLE_ARTIST="artists";
    public static final String FIELD_TABLE_ID="_id";
    public static final String FIELD_TABLE_ARTIST_NAME ="artistName";
    public static final String FIELD_TABLE_ALBUM_NAME ="albumName";
    public static final String FIELD_TABLE_TRACK="track";
    public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS "+TABLE_ARTIST+"("+FIELD_TABLE_ID+" INTEGER, "+ FIELD_TABLE_ARTIST_NAME +" TEXT, "+ FIELD_TABLE_ALBUM_NAME +" TEXT, "+FIELD_TABLE_TRACK+" TEXT)";
    public static final String INSERT_TABLE="INSERT INTO "+TABLE_ARTIST+" ("+FIELD_TABLE_ID+", "+ FIELD_TABLE_ARTIST_NAME +", "+ FIELD_TABLE_ALBUM_NAME +", "+FIELD_TABLE_TRACK+") VALUES(?, ?, ?, ?)";
    public static final String ARTIST_QUERY="SELECT "+FIELD_TABLE_ID+" FROM "+TABLE_ARTIST+" WHERE "+FIELD_TABLE_TRACK+" = ?";
    public static final String QUERY_TABLE_ARTIST="SELECT * FROM "+TABLE_ARTIST;


    private PreparedStatement insertTable;
    private PreparedStatement artistQuery;
    private PreparedStatement queryArtistTable;
    private Connection conn;

    public boolean open(){
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            insertTable = conn.prepareStatement(INSERT_TABLE);
            artistQuery = conn.prepareStatement(ARTIST_QUERY);
            queryArtistTable = conn.prepareStatement(QUERY_TABLE_ARTIST);

            return true;

        }catch(SQLException e){
            System.out.println("Unable to create DB : "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
            try{
                if(queryArtistTable!=null){
                    queryArtistTable.close();
                }
                if(artistQuery!=null){
                    artistQuery.close();
                }
                if(insertTable!=null){
                    insertTable.close();
                }
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException e){
                System.out.println("Unable to close DB : "+e.getMessage());
            }
    }

    public void create_Table(){
        try{
            Statement statement = conn.createStatement();
            statement.execute("DROP TABLE IF EXISTS "+TABLE_ARTIST);
            statement.execute(CREATE_TABLE);

        }catch(SQLException e){
           System.out.println("Unable to create table : "+e.getMessage());
        }
    }

    public boolean insert_Table(int id,String artistName, String albumName, String track){
        try{
            artistQuery.setString(1,track);
            ResultSet results = artistQuery.executeQuery();
            if(results.next()){
                System.out.format("Field already added at ID : %d\n",results.getInt(1));
                return true;
            }
            try{
                conn.setAutoCommit(false);
                insertTable.setInt(1,id);
                insertTable.setString(2,artistName);
                insertTable.setString(3,albumName);
                insertTable.setString(4, track);
                int affectedRows = insertTable.executeUpdate();
                if(affectedRows == 1){
                    conn.commit();
                }else{
                    throw new SQLException("unable to insert Artist details !");
                }
            }catch(Exception e1){
                System.out.println("Performing Exception"+e1.getMessage());
                try{
                    System.out.println("Performing Rollback");
                    conn.rollback();
                }catch(SQLException e2){
                    System.out.println("Unable to perform Rollback : "+ e2.getMessage());
                }
            }finally{
                try{
                    System.out.println("Restoring Autocommit ");
                    conn.setAutoCommit(true);
                }catch(SQLException e3){
                    System.out.println("Unable to set autocommit to true : "+ e3.getMessage());
                }
            }
            return true;
        }catch(SQLException e){
            System.out.println("Unable to perform Insert operation : "+e.getMessage());
            return false;
        }
    }

    public List<ArtistClass> query_ArtistTable(){
        try{
            ResultSet results = queryArtistTable.executeQuery();
            List<ArtistClass> artistList = new ArrayList<ArtistClass>();
            while(results.next()){
                ArtistClass artistClass = new ArtistClass();
                artistClass.setId(results.getInt(1));
                artistClass.setArtistName(results.getString(2));
                artistClass.setAlbumName(results.getString(3));
                artistClass.setTrack(results.getString(4));
                artistList.add(artistClass);
            }
            return artistList;
        }catch(SQLException e){
            System.out.println("Unable to fetch DB! : "+e.getMessage());
            return null;
        }
    }

}
