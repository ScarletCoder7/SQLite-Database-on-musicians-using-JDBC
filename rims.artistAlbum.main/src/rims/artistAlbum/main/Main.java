package rims.artistAlbum.main;

import rims.artistAlbum.common.ArtistClass;
import rims.artistAlbum.db.Datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {          // Project Created using IntelliJ IDE and JDK 10
    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()){
            System.out.println("Unable to open database");
            return;
        }
        datasource.create_Table();

        Scanner scanner = new Scanner(System.in);
        int id = 0;
        String artistName;
        String albumName;
        String track;
        String inputValue="";
        while(!inputValue.equals("exit")) {
            id++;
            System.out.println("Enter the Artist name");
            artistName = scanner.nextLine();
            System.out.println("Enter the Album name");
            albumName = scanner.nextLine();
            System.out.println("Enter the Track");
            track = scanner.nextLine();
            if (datasource.insert_Table(id, artistName, albumName, track)) {
                System.out.println("Item successfully added ");
            }
            System.out.println("If you want to continue press any alphabet key and hit enter.\nIf you want to exit type exit");
            inputValue= scanner.nextLine();
        }

        List<ArtistClass> artistlist;
        artistlist = datasource.query_ArtistTable();
        for(ArtistClass artists : artistlist){
            System.out.format("SL No. %d Artist Name : %s, Album Name : %s, Track : %s\n",artists.getId(),artists.getArtistName(),artists.getAlbumName(), artists.getTrack());
        }

        datasource.close();

    }
}
