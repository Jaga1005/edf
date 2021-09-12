package com.edf.db.orm;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RESOURCE", uniqueConstraints = {@UniqueConstraint(columnNames = {"userName", "filename"})})
public class Resource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "seq_resource")
    private long id;

    @Column(name = "USERNAME", nullable = false)
    private String userName;

    @Column(name = "FILENAME", nullable = false)
    private String filename;

    @Column(name = "DIRECTORY", nullable = false, unique = true)
    private String directory;

    @SuppressWarnings("serializable")
    public Resource() {
    }

    public Resource(String userName, String filename, String directory) {
        this.userName = userName;
        this.directory = directory;
        this.filename = filename;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
