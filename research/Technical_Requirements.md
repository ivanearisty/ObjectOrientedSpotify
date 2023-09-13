# Techical Requirements
## User
- User account
    1. (U) Verifiable account by email address
    2. (U) User ID not shown to user
    3. Name
    4. Bio
    5. Phone
    6. Spotify
    7. All info must be updatable
- Password reset
    - Password reset link expiring in 24 hours
- User login (email and password)
    - Token based auth (JWT token)
    - Token refresh
- Account Link
    - Link Spotify account to user account
- Brute force attack mitigation
    - 30 minute lockout after multiple unsuccessful attempts
- User can only see his own data
- Two factor authentication (optional using phone number)
- Track user activity
    - Ability to report suspicious activity
    - Tracking info:
        1. IP
        2. Browser
        3. Device
        4. Date
## Playlists, PlaylistSongs, Songs
A songs table can become absolutely massive because it will store all 
the songs of every user. Also, multiple users might have the same song, 
or multiple playlists might have the same song. 
**Is there a way to make this more efficient?**

We can't just start storing information for every user 
using ArrayLists in a SQL Database.  

So, my goal was to create a more efficient data modeling approach that 
allows for better storage utilization and eliminates data duplication. 
One such approach is to implement a Many-to-Many relationship for users, 
playlists, and songs.

### Playlists Table 
Store playlist-specific information:
- playlist_id
- user_id (as a foreign key referencing the Users table)
- playlist_name
- playlist owner

NOTE: specifying who the owner of a playlist is, is required for the proposed
architecture to work. This is because playlists can be owned by others 

### Songs Table
Store song-specific information 
- song_id
- song_name
- etc

### PlaylistSongs Table (The Many-to-Many Relationship) 
Create a separate table to manage the relationship between playlists and songs.
This table will store pairs of `playlist_id` and `song_id`, 
representing which songs are included in which playlists. 
This way, we avoid storing song data redundantly and can associate 
multiple playlists with the same song without duplication.

### Data Normalization
By using this approach, instead of duplicating song data across playlists, 
we only store song information once in the Songs table. 
This helps in reducing data redundancy and maintaining data integrity.
After all, songs are essentially immutable elements. 
A regular spotify user can only edit their playlists.

### Query Optimization

When fetching songs for a playlist or playlists containing a song, 
this  efficiently query the PlaylistSongs table to retrieve the relevant song IDs for a playlist or playlist IDs for a song. Then, you can join this information with the Songs and Playlists tables to fetch the complete details.

## Login Page

- Sign-up with Password, Email, and Spotify (enforce 6 letters on password)
    - Login with Password, Email, and Spotify

      Login Page

- Email verification
- Enforce wait on server responses
- Enforce email verification to login
- Bad Credentials and Password reset
- Expiring password reset links
- Enforce complete forms
- Enforce unique emails
- Confirm account creation with confirmation screen
- Brute force attack mitigation, login attempt cap (fix user lockout by bad agent)
- MFA login with verification code showing last 4 of phone number*

## Verification Page

- Application state for account verification
- Difference between already verified accounts and just verified accounts

## Main Page

### List

- List showing all playlists in the order that they appear
- Cover of playlist
- Name of Playlist (clickable, opens playlist)
- Genre Drop Down
    - Select from current genres or create new genre
    - Not applicable if playlist is not owned by you (does not apply if you are participant)
- Sub-Genre Drop Down, disabled if no Genre selected
    - Select from current genres or create new genre
    - Not applicable if playlist is not owned by you (does not apply if you are participant)
- “In development?” toggle ☑️
    - Sets Genre to “In Development” and prevents edits to Genre until un-toggled
- Track Count
- Owner
- Copy button, copies playlist url 🔘

### Around or above List

- Big “Guide” Button that pops up into an explanation
- Edit “Inheritance thresholds” Button that creates a popup that allows you to edit how tight or lose you want inheritance values of extended and implements to be
- Go to Abstract Playlists send you to Abstract Playlists page
- Delete empty Genres button 🔘
    - Pop up: Are you sure you want to delete all empty Genres? The Genres to be deleted are: for name in names show name
- Delete empty Sub-Genres button 🔘
    - Pop up: Are you sure you want to delete all empty Sub-genres? The Sub-Genres to be deleted are: for name in names show name
- Genres only toggle (enforces seeing playlists with inheritance structure) ☑️
- Sort hierarchical 🔘
    - Pop up: Are you sure you want to sort your playlist hierarchy? This action cannot be undone. Final sort will look like: sorted using inheritance → your other playlists → playlists created by Spotify → playlists created by other people
- Manage Genres button launches **Genres** page
- Export button (opens **export** **popup**) 🔘
- Import button (opens **import** **popup**) 🔘
- Help button (opens **help popup**) 🔘
- Total Playlists count ( yours / total )

## Genres view / page

### List of Genres

- Name of Genre (clickable, opens Genre view)
- Children playlists number
- If “Expand Sub-Genres” is toggled, shows all Sub-Genres (clickable, opens playlist view)
- Delete red button, launch Pop-up:
    - If genre is not empty display “you can only delete empty genres!”
    - If genre is empty display “are you sure you want to delete this genre?” Do not delete and get rid of it options with do not delete highlighted in blue.
- “Set Private” toggle ☑️ sets all the playlists in the genre to private (add a little help ? question mark that displays: If you set a genre to private all child playlists will be set to private, when unchecking it sets them all to public. However, if a playlist has toggled **“true private”** in its settings, it will not be set to public)

### Around or above List

- “Expand Sub-Genres” toggle ☑️
- Join: opens a popup that allows you to join two Genres into one (select)
- Split: opens a popup that allows you to split a Genre into two (drag and drop)
- “Edit Names” Button 🔘 allows for you to edit the names of all genres in a popup

## Genre view / page

### List of Subgenres

- Name (clickable, opens Sub-Genre view)
- Total tracks
- Delete red button, launch Pop-up:are you sure you want to delete this genre? 
Do not delete and get rid of it options with do not delete highlighted in blue.
- “Set Private” toggle ☑️ set the playlists to private
- 
### Around or above List

- Join: opens a popup that allows you to join two playlists into one (select)
- Split: opens a popup that allows you to split a playlists into two (drag and drop)
- “Edit Names” Button 🔘 allows for you to edit the names of all playlists in a popup

## Playlist or Sub-Genre view / page

### List of Tracks

- “Cover art”
- Number (Sortable by click)
- Name (Sortable by click)
- Artist (Sortable by click)
- Len (Time of track, Sortable by click)
- Release (Release Date, Sortable by click)
- BPM (Rounded, Sortable by click)
- Key (In numerical Scale, Sortable by click)
- Popularity (Sortable by click)
- Random(Sortable by click)

### Around or above List

- Filter by BPM
- Filter by Key
- Extended SubGenre

    - Green or Red Status depending on whether everything is ok or not
    - Drop down menu that displays all the SubGenres in the current SubGenre, and allows you to choose which SubGenre to extend
    - ? symbol on the side displays when hovered:
    - Extending a playlist (SubGenre) means that either

      a) 20% or more of the songs in this playlist are also present in the extended playlist

      b) 20% or more of the songs in the extended playlist are also present in this playlist

      You can edit these values in Inheritance Editor

- Implemented SubGenre(s)
    - Green or Red Status depending on whether everything is ok or not
    - Drop down menu that displays all the SubGenres in the current SubGenre, and allows you to check which SubGenre or Abstract Playlist this playlist will implement
    - ? symbol on the side displays when hovered:

      Implementing a playlist (SubGenre) means that 100% of the songs in the implemented playlist must be present in this playlist

- Name (Clickable, opens Spotify playlist)
- Two Green Buttons side to side Save and Save As… (Save as creates playlist with a name that is provided from the user on popup) 🔘
- Export Button
    - Export downloads the current playlist as an XSPF in the users download folder
- Import Button:
    - Creates a popup that allows you to insert an XSPF file and appends the songs to the bottom of this list

## Abstract Playlists Page

### List

- Name (clickable, opens Abstract Playlist view)
- Encapsulating Genre
- All Implementing Subgenres
- Description

### Around or above List

Abstract Playlists are not playable and only exist in the context of this app.

They can be ideas with a brief description and no songs, or they can be fully fledged playlists you create using the Abstract Playlists editor.

The power of abstract playlists is that they allow you to create playlists that exist nowhere in your actual Spotify library, but that “force” the playlists in your Spotify library to comply with including 100% of their contents. Some examples of usage:

- You have a few songs of a band you really like and would like to have multiple playlists always include them.
- You have a few songs that are huge crowd pleasers, and no matter the set, you want them to be available always - so every playlist you use professionally can automatically implement them.
- You can also have empty Abstract playlists whose names are purely descriptive like “Good Vibes” or “Beach Vibes”. Since abstract playlists can be implemented across Genres, a “Beach Vibes” abstract playlist can be implemented by a playlist full of rock music or house music equally.

## Abstract Playlist View

### List

- “Cover art”
- Number (Sortable by click)
- Name (Sortable by click)
- Artist (Sortable by click)
- Len (Time of track, Sortable by click)
- Release (Release Date, Sortable by click)
- BPM (Rounded, Sortable by click)
- Key (In numerical Scale, Sortable by click)
- Popularity (Sortable by click)
- Random(Sortable by click)

### Around or above List

- Playlist Name
- Description
- Delete Button (with Pop-up that asks to confirm)
- All Implementing Subgenres

## Export popup

- Export all option
- Allows you to export playlists you select into a zip of XML Shareable Playlist Format (XSPF) files

## Import popup

- Allows you to drag and drop a file and save it into a playlist
- Accepts XML Shareable Playlist Format (XSPF)

## Profile page

- Account activity
- Link and unlink Spotify account
- Reset password with old + new password (enforce 6 letters)
- Enable MFA with US phone number
- Get report of data to email

## Help pop-up

- Just an App Summary

## Limits

- Limit user requests per day
- Limit IP requests per day
- Limit total server usage so I don’t end up paying half a liver for this demo