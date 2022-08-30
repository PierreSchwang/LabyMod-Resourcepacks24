package de.pierreschwang.labymod.resourcepacks.api.dao.result;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Represents a paginated result of {@link T}.
 *
 * @param <T> The type of the embedded data.
 */
public class PaginatedResult<T> {

  private List<T> data;
  @SerializedName("meta")
  private Paginator paginator;

  public PaginatedResult() {
  }

  public PaginatedResult(List<T> data, Paginator paginator) {
    this.data = data;
    this.paginator = paginator;
  }

  public List<T> data() {
    return data;
  }

  public Paginator paginator() {
    return paginator;
  }

  public static final class Paginator {

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("last_page")
    private int totalPages;

    @SerializedName("from")
    private int startingEntryIndex;

    @SerializedName("to")
    private int endingEntryIndex;

    @SerializedName("total")
    private int totalEntries;

    @SerializedName("per_page")
    private int entriesPerPage;

    public Paginator(int currentPage, int totalPages, int startingEntryIndex, int endingEntryIndex,
        int totalEntries, int entriesPerPage) {
      this.currentPage = currentPage;
      this.totalPages = totalPages;
      this.startingEntryIndex = startingEntryIndex;
      this.endingEntryIndex = endingEntryIndex;
      this.totalEntries = totalEntries;
      this.entriesPerPage = entriesPerPage;
    }

    public int currentPage() {
      return currentPage;
    }

    public int totalPages() {
      return totalPages;
    }

    public int startingEntryIndex() {
      return startingEntryIndex;
    }

    public int endingEntryIndex() {
      return endingEntryIndex;
    }

    public int totalEntries() {
      return totalEntries;
    }

    public int entriesPerPage() {
      return entriesPerPage;
    }
  }

}
