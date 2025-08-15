package Alura_Challenge.demo.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String title;

    @Column(name = "languages")
    @Convert(converter = StringListConverter.class)
    private List<String> languages;

    @Column(name = "download_count")
    private int download_count;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "fecha_nacimiento")
    private Integer birth_year;

    @Column(name = "fecha_muerte")
    private Integer death_year;

    public Libro() {

    }
    public Libro(String title, List<String> languages, int download_count, String authorName, Integer birth_year,Integer death_year) {
        this.title = title;
        this.languages = languages;
        this.download_count = download_count;
        this.authorName = authorName;
        this.death_year = death_year;
        this.birth_year = birth_year;
    }

    @Override
    public String toString() {
        return
                "_Title= " + title + "\n" +
                "_Lenguaje= " + languages + "\n" +
                "_Autor= " + authorName + "\n" +
                "_Descargas= " + download_count + "\n";
    }

    public Integer getDeath_year() {
        return death_year;
    }

    public void setDeath_year(Integer death_year) {
        this.death_year = death_year;
    }

    public Integer getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(Integer birth_year) {
        this.birth_year = birth_year;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }


}
