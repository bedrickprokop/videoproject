package br.com.videoproject.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Video {

    public Video(){}

    public Video(String name, String description, String inputFormat, byte[] bytes){
        this.name = name;
        this.description = description;
        this.inputFormat = inputFormat;
        this.bytes = bytes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @Transient
    private byte[] bytes;

    @Column(name = "input_format")
    private String inputFormat;

    @Column(name = "output_format")
    private String outputFormat;

    @Column(name = "input_url")
    private String inputUrl;

    @Column(name = "output_url")
    private String outputUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getInputUrl() {
        return inputUrl;
    }

    public void setInputUrl(String inputUrl) {
        this.inputUrl = inputUrl;
    }

    public String getOutputUrl() {
        return outputUrl;
    }

    public void setOutputUrl(String outputUrl) {
        this.outputUrl = outputUrl;
    }
}
