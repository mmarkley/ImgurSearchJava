package networking;

import android.net.Uri;

/**
 * https://api.imgur.com/3/gallery/search/time/{page number}?
 * q={search parameters}
 */
public class ImgurRequest {
    private static final String IMGUR_URL = "https://api.imgur.com/3/gallery/search/time/";

    private String searchTerm;
    private long pageNumber;

    /**
     * Method to get the search term used to build this object
     * @return A {@link String} containing the search term
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * Method to get the page number specified
     * @return A {@link long} containing the page number
     */
    public long getPageNumber() {
        return pageNumber;
    }

    /**
     * Single argument constructor that takes only a search term
     * @param searchTerm The {@link String} to use for searching
     */
    public ImgurRequest(String searchTerm) {
        this(0, searchTerm);
    }

    /**
     * Two argument constructor that takes a page number and a search term
     * @param pageNumber A {@link long} with the page number to search with
     * @param searchTerm A {@link String} containing the search term
     */
    public ImgurRequest(long pageNumber, String searchTerm) {
        this.searchTerm = searchTerm;
        this.pageNumber = pageNumber;
    }

    public String getUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.imgur.com")
                .appendPath("3")
                .appendPath("gallery")
                .appendPath("search")
                .appendPath("time")
                .appendPath(Long.toString(pageNumber))
                .appendQueryParameter("q", searchTerm)
                .appendQueryParameter("mature", "false");

        return builder.toString();
    }
}
