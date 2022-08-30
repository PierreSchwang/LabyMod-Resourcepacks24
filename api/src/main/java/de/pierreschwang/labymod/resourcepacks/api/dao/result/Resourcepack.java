package de.pierreschwang.labymod.resourcepacks.api.dao.result;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import de.pierreschwang.labymod.resourcepacks.api.adapter.ResourcepackTagAdapter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public final class Resourcepack {

  @SerializedName("rp_id")
  private int id;

  @SerializedName("website_name")
  private String name;

  private UUID creator;

  @SerializedName("ingame_name")
  private String username;

  private String thumbnail;

  private long size;

  @SerializedName("download")
  private long downloads;

  private String description;

  private String category;

  private int rating;

  @JsonAdapter(ResourcepackTagAdapter.class)
  private List<String> tags;

  @SerializedName("created_at")
  private Date createdAt;

  @SerializedName("updated_at")
  private Date updatedAt;

  public int id() {
    return id;
  }

  public String name() {
    return name;
  }

  public UUID creator() {
    return creator;
  }

  public String username() {
    return username;
  }

  public String thumbnail() {
    return thumbnail;
  }

  public long size() {
    return size;
  }

  public long downloads() {
    return downloads;
  }

  public String description() {
    return description;
  }

  public String category() {
    return category;
  }

  public int rating() {
    return rating;
  }

  public List<String> tags() {
    return tags;
  }

  public Date createdAt() {
    return createdAt;
  }

  public Date updatedAt() {
    return updatedAt;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCreator(UUID creator) {
    this.creator = creator;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public void setDownloads(long downloads) {
    this.downloads = downloads;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

}
