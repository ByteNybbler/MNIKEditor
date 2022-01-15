



Dim LevelEntity(200),LevelSurface(200) ; one for each chunk
Dim WaterEntity(200),WaterSurface(200)
Dim LevelTexture(30),WaterTexture(10)
Global LevelTextureName$,WaterTextureName$
Global LevelDetail ; 1-x how many subdivisions per tile

;custom
Global customleveltexture,custombgtexture1,custombgtexture2
Global usecustomleveltexture=False
Global customwatertexture
Global usecustomwatertexture=False

; global level
Global LevelWidth, LevelHeight ; in tiles
Global LevelEdgeStyle 

Global BackGroundEntity1, BackGroundEntity2
Dim BackGroundTexture1(30),BackGroundTexture2(30)

; Info on the global level - these might need to be gotten from somewhere else
; for a large-sized seamless level. Bits and pieces are taken out and written into Chunks when needed
Dim LevelTileTexture(100,100) ; corresponding to squares in LevelTexture
Dim LevelTileRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim LevelTileSideTexture(100,100) ; texture for extrusion walls
Dim LevelTileSideRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim LevelTileRandom#(100,100) ; random height pertubation of tile
Dim LevelTileHeight#(100,100) ; height of "center" - e.g. to make ditches and hills
Dim LevelTileExtrusion#(100,100); extrusion with walls around it 
Dim LevelTileRounding(100,100); 0-no, 1-yes: are floors rounded if on a drop-off corner
Dim LevelTileEdgeRandom(100,100); 0-no, 1-yes: are edges rippled

Dim WaterTileTexture(100,100)
Dim WaterTileRotation(100,100)
Dim WaterTileHeight#(100,100)
Dim WaterTileTurbulence#(100,100)
Global WaterFlow, WaterTransparent,WaterGlow  ; 0-10: speed of wateranimation, t/f, t/f
Global WaterTurbulenceGlobal ; true or false (set at levelcreate)


Global CurrentLevelChunk
; All of these are for a given chunk
; maximum chunk size right now is 100x100 (but can of course be smaller)
Global ChunkSize ; in general the x by x size of chunks
Global ChunkWidth, ChunkHeight ; size of the current chunk (could be smaller if at edge)
Dim ChunkTileTexture(100,100) ; corresponding to squares in LevelTexture
Dim ChunkTileRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim ChunkTileSideTexture(100,100) ; texture for extrusion walls
Dim ChunkTileSideRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim ChunkTileRandom#(100,100) ; random height pertubation of tile
Dim ChunkTileHeight#(100,100) ; height of "center" - e.g. to make ditches and hills
Dim ChunkStoredVHeight#(0) ; used for height calculations (re-dimmed in CreateLevel()
Dim ChunkTileExtrusion#(100,100); extrusion with walls around it 
Dim ChunkTileRounding(100,100); 0-no, 1-yes: are floors rounded if on a drop-off corner
Dim ChunkTileEdgeRandom(100,100); 0-no, 1-yes: are edges rippled
Global ChunkTileU#,ChunkTileV#

; water control 
Dim ChunkWaterTileTexture(100,100)	; -1: none, 0-3 water textures
Dim ChunkWaterTileRotation(100,100) ; 0-7 
Dim ChunkWaterTileHeight#(100,100)  ; height of water tile

; Camera
Global Camera,CameraZoomLevel#
Global CameraAddX#,CameraAddY#,CameraAddZ#,CameraAddXCurrent#,CameraAddYCurrent#,CameraAddZCurrent#
Global CameraAddZoom#, CameraAddZoomCurrent#
Global CameraShakeTimer

; Lights
Global LevelLight,SpotLight
Global LightRed,LightGreen,LightBlue,LightRedGoal,LightGreenGoal,LightBlueGoal,LightChangeSpeed,LightRedGoal2,LightGreenGoal2,LightBlueGoal2
Global AmbientRed,AmbientGreen,AmbientBlue,AmbientRedGoal,AmbientGreenGoal,AmbientBlueGoal,AmbientChangeSpeed,AmbientRedGoal2,AmbientGreenGoal2,AmbientBlueGoal2
LightRedGoal2=-1