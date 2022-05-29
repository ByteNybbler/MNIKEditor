# HOTKEY REFERENCE

## NUMERIC FIELDS

* Left click: Adjust up. Holding shift usually adjusts by a greater step size.
* Right click: Adjust down. Holding shift usually adjusts by a greater step size.
* Ctrl+Click: Type value into numeric field.
* Scroll the mouse wheel: Adjust value of numeric field. Holding shift usually adjusts by a greater step size.

## ADVENTURE SELECT

* Scroll the mouse wheel: Navigate the adventure list. Hold shift to scroll faster.
* Page up: Page up.
* Page down: Page down.

## MASTER EDITOR

* Scroll the mouse wheel: Navigate the LV and DG lists depending on where the mouse is hovering. Hold shift to scroll faster.
* Ctrl+Click the LV or DG column: Type in the number of the wlv or dia you want to edit.
* Right click: Right-clicking a wlv or dia number will copy the wlv or dia. Right-click the same wlv or dia again to cancel the copy, or left-click a nonexistent wlv or dia to copy to that wlv or dia.
* Page up: Page up on either the LV or DG column, depending on which one the mouse is hovering over.
* Page down: Page down on either the LV or DG column, depending on which one the mouse is hovering over.
* Home: Move cursor to beginning of line.
* End: Move cursor to end of line.

## LEVEL EDITOR

* Left click: Place objects or tiles. Several objects can be placed on the same tile. Using a larger brush size will place a wider spread of objects or tiles at once.
* Right click: Grab the object or tile at the center of the cursor. This is the easiest way to copy and paste an object or tile. If there are multiple objects stacked on the same tile, right-clicking multiple times will cycle through them.
* Hold right click and drag: Move the grabbed object with the cursor.
* Middle click: Generate adjuster settings for a LevelExit leading to the current level at the cursor's current coordinates. It will also automatically attempt to infer the player's facing direction based on whether it finds an existing LevelExit on an adjacent tile. If a LevelExit wop is already in use, only the relevant adjuster settings will be changed. The only times a LevelExit will not be generated is when the current object is a magic shooter, meteor shooter, or an object that performs either CMD 7 or CMD 61 (including the NPC move command). In these cases, middle-clicking will simply set the destination of the current object. This makes it easy to do things like chain together a long series of movement commands.
* Delete: Delete all objects covered by the cursor brush. If the mouse is hovering over the OBJECTS menu, this will only delete the currently-grabbed object. This is useful if you have multiple objects stacked on the same tile and you only want to delete a specific one.
* Enter: Toggle custom brush mode. The cursor brush will be purple while a custom brush is active. Entering custom brush mode will copy all objects or tiles that are being hovered over by the brush, depending on whether the editor is in object mode or tile mode. This lets you paste those objects or tiles anywhere else. Press enter again to exit custom brush mode. Try it out with a large brush size!
* R: Update the currently-grabbed object. This does the same thing as pressing the Update button.
* Ctrl+Click the FILL button: Set the fill density. This is a value in the range [0.0, 1.0] that determines the percentage of tiles that will get filled based on random chance.
* F6: Toggle orthographic projection.
* F7: Toggle wireframe mode.
* T: Give the current object its default in-game TrueMovement values (TTC, OTC, MovementType, and MovementSpeed) based on its Type, SubType, and sometimes some Data values. This makes it very easy to do various small changes that previously required a lot of manual work setting the TTC and OTC. For instance, you can easily define different MovementSpeeds on objects that formerly did not allow that value to be adjusted without making advanced manual adjustments to TrueMovement. Objects like hyperspeed turtles, hyperspeed MooBots, and hyperspeed Wee Stinkers can be constructed with minimal effort. You can also modify the MovementTypes of these objects easily so that you can have stuff like Wee Stinkers that sleepwalk away from you until you wake them up.
* Ctrl+B: Toggle block mode. The cursor brush will be blue while block mode is active.
* Ctrl+F: Toggle fill mode. The cursor brush will be yellow while fill mode is active.

### CAMERA CONTROLS

* Scroll the mouse wheel: Zoom the target camera in and out or move it up and down. The target camera is determined by the mouse's current location. Hold shift to go faster.
* Arrow keys or WASD or Numpad 2/4/6/8: Pan the target camera. Hold shift to move faster.
* Space+Click+Drag: Pan the level editor camera.
* X or Numpad 5: Reset the target camera's rotation.
* Z or Numpad 1: Rotate the target camera.
* Q or Numpad 7: Rotate the target camera.
* E or Numpad 9: Move the target camera up.
* C or Numpad 3: Move the target camera down.
* Numpad /: Rotate the target camera in a horrible way.
* Numpad *: Rotate the target camera in a horrible way.

### OBJECTS MENU

* Enter: Toggle the randomized state of the object adjuster being hovered over with the mouse. Any objects placed will choose a random value from the range that you define.
* Scroll the mouse wheel: Scroll through objects and object categories.
* Ctrl+Click on "Object": Enter an object name or the first few characters of an object name to instantly jump to that object in the current category.
* Ctrl+Click on "Category": Enter a category name or the first few characters of a category name to instantly jump to that object category.
* Tilde+Click: Find and replace a specific value of the chosen adjuster for all objects in the wlv. Only works on text-based adjusters.

### TILES MENU

* Enter: Toggled the used state of the tile attribute being hovered over with the mouse. This means that any new tiles you "place" won't modify that attribute of the tile(s) you place it on.
* Scroll the mouse wheel: Scroll through tiles and tile categories, as well as tile logics.
* Shift+Enter: Mark all tile attributes as unused except for the tile attribute being hovered over with the mouse.
* Ctrl+Click: As usual, this works on all numeric adjusters, including the tile logic if you're feeling feisty.
* I while hovering mouse over the TILES menu: Rotates the previewed tile counter-clockwise.
* O while hovering mouse over the TILES menu: Rotates the previewed tile clockwise.

### GLOBALS MENU

* Right click: Set a custom leveltex or custom watertex.

## DIALOG EDITOR

* Scroll the mouse wheel: Switch through interchanges, answers, and AskAbouts.
* Ctrl+Click: Switch through interchanges, answers, and AskAbouts by typing the desired number.
* Right click: Make text white from the cursor's position onwards until a different text color is reached.
* Home: Move cursor to beginning of line.
* End: Move cursor to end of line.
* Ctrl+W: Toggle White.
* Ctrl+E: Toggle Grey.
* Ctrl+R: Toggle Red.
* Ctrl+O: Toggle Orange.
* Ctrl+Y: Toggle Yellow.
* Ctrl+G: Toggle Green.
* Ctrl+B: Toggle Blue.
* Ctrl+I: Toggle Indigo.
* Ctrl+V: Toggle Violet.
* Ctrl+A: Toggle Rainbow AKA all colors.
* Ctrl+F: Toggle Blinking AKA flashing white.
* Ctrl+D: Toggle Warning AKA flashing red and DOOMY.
* Ctrl+N: Toggle NO effect.
* Ctrl+S: Toggle SHake.
* Ctrl+J: Toggle JItter.
* Ctrl+X: Toggle WAve.
* Ctrl+Z: Toggle BOunce.
* Ctrl+P: Toggle ZOom.
* Ctrl+Q: Toggle ZoomShake.
* Ctrl+C: Toggle CiRcle.
* Ctrl+M: Toggle figure EIght.
* Ctrl+U: Toggle UpDown.
* Ctrl+L: Toggle LeftRight.
* Ctrl+T: Toggle RT. This effect actually does nothing in-game, at least in vanilla.