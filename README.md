# HOTKEY REFERENCE

## NUMERIC FIELDS

* Left click: Adjust up. Holding shift usually adjusts by a greater step size.
* Right click: Adjust down. Holding shift usually adjusts by a greater step size.
* Ctrl+Click: Type value into numeric field.
* Scroll the mouse wheel: Adjust value of numeric field. Holding shift usually adjusts by a greater step size.

## ADVENTURE SELECT

* Scroll the mouse wheel: Navigate the adventure list. Hold shift to scroll faster.
* PageUp: Page up.
* PageDown: Page down.

## MASTER EDITOR

* Scroll the mouse wheel: Navigate the LV and DG lists depending on where the mouse is hovering. Hold shift to scroll faster.
* Ctrl+Click the LV or DG column: Type in the number of the wlv or dia to edit.
* Right click: Right-clicking a wlv or dia number will copy the wlv or dia. Right-click the same wlv or dia again to cancel the copy, or left-click a nonexistent wlv or dia to copy to that wlv or dia.
* Middle click: Middle-clicking a wlv or dia number will mark the wlv or dia for swapping. Middle-click the same wlv or dia again to cancel the swap, or left-click a different wlv or dia to swap with that wlv or dia. Swapping with nonexistent wlvs or dias is allowed.
* PageUp: Page up on either the LV or DG column, depending on which one the mouse is hovering over.
* PageDown: Page down on either the LV or DG column, depending on which one the mouse is hovering over.
* Home: Move cursor to beginning of line.
* End: Move cursor to end of line.
* Ctrl+S: Save master.dat.
* Ctrl+O: Type in the number of the wlv to edit, unless the mouse is hovering over the dialogs column, in which case type in the number of the dia to edit.
* Ctrl+T: Save+Test.
* Ctrl+Click the Compile+Exit button: Compile adventure and pack custom content.

## LEVEL EDITOR

* Left click: Place objects or tiles. Several objects can be placed on the same tile. Using a larger brush size will place a wider spread of objects or tiles at once.
* Right click: Select the object or tile at the center of the cursor. This is the easiest way to copy and paste an object or tile. If there are multiple objects stacked on the same tile, right-clicking multiple times will cycle through them.
* Hold right click and drag: Move the grabbed objects with the cursor.
* Shift + right click: Select all objects inside the brush.
* Ctrl + right click: Add or remove an object from the tile at the center of the cursor to or from the current selection.
* Ctrl + Shift + right click: Add all objects from inside the brush to the current selection.
* Tilde + right click: Select all objects that have the same Type and ModelName as the clicked object.
* Middle click: Generate adjuster settings for a LevelExit leading to the current level at the cursor's current coordinates. It will also automatically attempt to infer the player's facing direction based on whether it finds an existing LevelExit on an adjacent tile. If a LevelExit wop is already in use, only the relevant adjuster settings will be changed. The only times a LevelExit will not be generated is when the current object is a magic shooter, meteor shooter, or an object that performs any of CMD 7, CMD 11, or CMD 61 (including the NPC move command). In these cases, middle-clicking will simply set the destination of the current object. This makes it easy to do things like chain together a long series of movement commands.
* Delete: Delete all objects covered by the cursor. Pressing Delete while creating a block in block mode will delete all objects in the selected region. Pressing Delete in any fill mode will delete all objects in the selected tile region.
* Enter: Copy all objects or tiles that are being hovered over by the brush, depending on whether the editor is in object mode or tile mode. This lets you paste those objects or tiles anywhere else. Try it out with a large brush size!
* R: Update the currently-grabbed object. This does the same thing as pressing the Update button.
* Tab: Switch between object mode and tile mode.
* Shift+scroll the mouse wheel while hovering mouse over level view: Change the brush width.
* Ctrl+scroll the mouse wheel while hovering mouse over level view: Change the brush height. Can be done at the same time as shift+scroll.
* G: Pressing G when hovering the cursor over a LevelExit follows that LevelExit to its destination. A yellow marker will briefly appear at the destination coordinates when the destination level loads. This also works with general commands and cuboids.
* F6: Toggle orthographic projection.
* F7: Toggle wireframe mode.
* T: Give the current object its default in-game TrueMovement values (TTC, OTC, MovementType, and MovementSpeed) based on its Type, SubType, and sometimes some Data values. This makes it very easy to do various small changes that previously required a lot of manual work setting the TTC and OTC. For instance, you can easily define different MovementSpeeds on objects that formerly did not allow that value to be adjusted without making advanced manual adjustments to TrueMovement. Objects like hyperspeed turtles, hyperspeed MooBots, and hyperspeed Wee Stinkers can be constructed with minimal effort. You can also modify the MovementTypes of these objects easily so that you can have stuff like Wee Stinkers that sleepwalk away from you until you wake them up.
* F: Flip the brush horizontally.
* V: Rotate the brush 90 degrees.
* Ctrl+S: Save level.
* Ctrl+T: Update the currently-grabbed object, save level, and activate the "Test Level At Cursor" brush mode.
* Ctrl+C: Copy all selected objects to the brush.
* Ctrl+O: Type in the number of the wlv you want to open.
* Ctrl+[: Open the previous wlv that was opened. This can be done numerous times in a row to retrace your steps, perhaps after having followed LevelExits by pressing G. The last 100 opened wlvs are tracked.
* Ctrl+PageDown: Go to the next numbered wlv.
* Ctrl+PageUp: Go to the previous numbered wlv.
* Ctrl+N: Go to normal brush mode.
* Ctrl+B: Toggle block mode. The cursor brush will be blue while block mode is active.
* Ctrl+F: Toggle fill mode. The cursor brush will be yellow while fill mode is active.
* Ctrl+I: Toggle inline hard mode. The cursor brush will be red while inline hard mode is active.
* Ctrl+U: Toggle inline soft mode. The cursor brush will be orange while inline soft mode is active.
* Ctrl+K: Toggle outline hard mode. The cursor brush will be indigo while outline hard mode is active.
* Ctrl+J: Toggle outline soft mode. The cursor brush will be a slightly lighter indigo while outline soft mode is active.

### CAMERA CONTROLS

* Scroll the mouse wheel: Zoom the target camera in and out or move it up and down. The target camera is determined by the mouse's current location.
* Arrow keys or WASD or Numpad 2/4/6/8: Pan the target camera. Hold shift to move faster.
* Space+Click+Drag: Pan the level editor camera. Hold shift to move faster.
* X or Numpad 5: Reset the target camera's rotation.
* Shift+X or Shift+Numpad 5: Reset the target camera's position and rotation.
* Z or Numpad 1: Pitch the target camera up. Hold shift to rotate faster.
* Q or Numpad 7: Pitch the target camera down. Hold shift to rotate faster.
* E or Numpad 9: Move the target camera up. Hold shift to move faster.
* C or Numpad 3: Move the target camera down. Hold shift to move faster.
* I or Numpad /: Yaw the target camera left. Hold shift to rotate faster.
* O or Numpad *: Yaw the target camera right. Hold shift to rotate faster.

### OBJECTS MENU

* Enter: Toggle the randomized state of the object adjuster being hovered over with the mouse. Any objects placed will choose a random value from the range that you define.
* Scroll the mouse wheel: Scroll through objects and object categories.
* Delete while hovering mouse over OBJECTS menu: Delete the currently-selected object(s). This is useful if you have multiple objects stacked on the same tile and you only want to delete a specific one.
* Ctrl+Click on "Object": Enter an object name or a few characters of an object name to instantly jump to that object. All object categories will be searched.
* Ctrl+Click on "Category": Enter a category name or a few characters of a category name to instantly jump to that object category.
* Tilde+Click: Find and replace a specific value of the chosen adjuster for all objects in the wlv. Only works on text-based adjusters.

### TILES MENU

* Enter: Toggled the used state of the tile attribute being hovered over with the mouse. This means that any new tiles you "place" won't modify that attribute of the tile(s) you place it on.
* Scroll the mouse wheel: Scroll through tiles and tile categories as well as tile logics and tile textures.
* Shift+Enter: Mark all tile attributes as unused except for the used tile attribute being hovered over with the mouse. If the tile attribute being hovered over is unused, mark all tile attributes as used.
* Ctrl+Click: As usual, this works on all numeric adjusters, including the tile logic if you're feeling feisty. It can also be used to type tile texture IDs. If you enter very large numbers, you will get bizarre metatextures.
* Ctrl+Enter while hovering the mouse over a numeric tile adjuster: Set a step size for that adjuster. This will modify the value by that amount every time the left mouse button is pressed to place some tiles. To disable the step size, press Ctrl+Enter while hovering the mouse over that adjuster.
* I while hovering mouse over the TILES menu: Rotates the previewed tile counter-clockwise.
* O while hovering mouse over the TILES menu: Rotates the previewed tile clockwise.

### GLOBALS MENU

* Ctrl+Click the arrows next to Width and Height: Type in the level width or level height.
* Right click: Set a custom leveltex or custom watertex.

## DIALOG EDITOR

* Scroll the mouse wheel: Switch through interchanges, answers, and AskAbouts.
* Ctrl+Click: Switch through interchanges, answers, and AskAbouts by typing the desired number.
* Right click: Make text white from the cursor's position onwards until a different text color is reached.
* Home: Move cursor to beginning of line.
* End: Move cursor to end of line.
* Ctrl+S: Save dialog.
* Ctrl+O: Type in the number of the dialog you want to open.
* Ctrl+PageDown: Go to the next numbered dialog.
* Ctrl+PageUp: Go to the previous numbered dialog.
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