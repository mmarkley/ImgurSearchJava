#ImgurSearch

An application for rudimentary searching of Imgur. The application uses the `mature=false` query parameter to eliminate mature content. It also uses the getNsfw field of the Imgur Response to determine whether or not to show the image.
Application currently only supports display of the first image listing in the getImages field of the ImgurResponse

#MainActivity/SearchFragment
Uses Android Navigation framework to navigate between Search and Full Screen display

#Limitations
Only displays images, does not display or support video such as MP4
Only displays the first image that is part of each response object and does not show the user that there are additional images.


