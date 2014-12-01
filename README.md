<h1>description</h1>
Small RPG game built using LibGDX framework.<br>
Most of the work can be found in:<br>
<a href="https://github.com/JotraN/yosefu/tree/master/core/src/com/yosefu/game">/core/src/com/yosefu/game</a>

<h1>creating a level</h1>
Level/map files should be created in <a href="https://github.com/JotraN/yosefu/tree/master/core/assets/levels">/core/assets/levels/</a>.<br>
Some example files can be found there.<br><br>
The first lines (before the actual tile map) consists of information for any objects in the map.<br>
The characters used for each object correspond to those indicated in the Level class (e.g. doors are lowercase letters a-n).<br>
<em>The following examples are taken from the map_02 file:</em><br>
The first line creates a door:<br>
<code>a:map_01 332 574</code><br>
The first character <code>a</code> indicates that the object is a door.<br>
The characters after the <code>:</code> are the variables for that object.<br>
In this example, <code>map_01 332 574</code> are the variables. Each is seperated by a space.<br>
The first variable declares the map this door leads to, the second and third are the x and y coordinates for the player to move to
(after entering the door).<br><br>
The following line creates an enemy:<br>
<code>o:badguy.png 10 10 1#a:map_01a 332 574</code><br>
Similar to the door, the first character indicates the object is an enemy.<br>
Unlike the door, the variables for the enemy is between the <code>:</code> and the <code>#</code>, rather than everything after the <code>:</code><br>.
The first variable indicates the texture of the enemy, the rest indicates the enemy's health, pp, and attack.<br>
Finally, the <code>#</code> represents an action to be taken upon the enemy's defeat, this portion is optional.<br>
In this case, upon the enemy's death, the <code>a</code> door would be changes its destination from map_01 to map_01a.<br><br>
The rest of the file contains the tile map of the level.<br>
<code>1</code> indicates a wall.<br>
<code>0</code> indicates empty space.<br>
A letter indicates the objects created in the first few lines of the file.<br>

<h1>notes</h1>
Still requires a lot of work:
<ul>
<li>A story.</li>
<li>More content.</li>
<li>Inventory system.</li>
<li>A save system.</li>
<li>Sounds.</li>
<li>FPS limiter.</li>
<li>Endgame screens.</li>
</ul>
