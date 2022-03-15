; Wonderland Adventures Editor
; Open Source Edition
; 
; v10.04
;
;
; COMPILER NOTE:	Requires	User32.decls with Hasfocus() function in Blitz3D/userlibs
;								msfolder.dll and .decls in Blitz3D/userlibs
;
;

AppTitle "Wonderland Adventures MNIKEditor"

Include "particles-define.bb"

Global VersionText$="WA Editor       MNIKSource v10.04 (03/16/22)"

Global MASTERUSER=True

SeedRnd MilliSecs() ; Seed the randomizer with the current system time in milliseconds.

Global LeftMouse,LeftMouseReleased,RightMouse,RightMouseReleased
Global MouseScroll=0
Global ReturnKey,ReturnKeyReleased,DeleteKey,DeleteKeyReleased
Const KeyCount=237
Dim KeyReleased(KeyCount)

Global EditorMode=0		;0-level, 1-textures, 2-sidetextures, 3-objects
						;4-user Select screen
						;5,6,7-adventure select screen (6="edit/delete/move/cancel",7="delete sure?")
						;8-master edit screen
						;9-dialog edit screen
						
Global EditorModeBeforeMasterEdit=0
						
; COLORS

Global RectOnR=255
Global RectOnG=0
Global RectOnB=0
Global RectOffR=100
Global RectOffG=0
Global RectOffB=0
Global RectGlobalsR=50
Global RectGlobalsG=0
Global RectGlobalsB=0
Global RectMarginR=0
Global RectMarginG=0
Global RectMarginB=0
Global RectToolbarR=0
Global RectToolbarG=0
Global RectToolbarB=0
Global TextLevelR=255
Global TextLevelG=255
Global TextLevelB=255
Global TextAdjusterR=255
Global TextAdjusterG=255
Global TextAdjusterB=255
Global TextAdjusterHighlightedR=255
Global TextAdjusterHighlightedG=255
Global TextAdjusterHighlightedB=0
Global TextMenusR=255
Global TextMenusG=255
Global TextMenusB=0
Global TextMenuButtonR=155
Global TextMenuButtonG=155
Global TextMenuButtonB=0
Global TextMenuXR=100
Global TextMenuXG=100
Global TextMenuXB=0

Const ColorsConfig$="colors.cfg"

		
; EDITOR DIALOG DATA

Global CurrentDialog

Global DialogCurrentRed,DialogCurrentGreen,DialogCurrentBlue,DialogCurrentEffect

Const MaxInterChanges=100
Global StartingInterChange
Global NofInterchanges
Dim NofInterChangeTextLines(MaxInterChanges)	
Dim InterChangeTextLine$(MaxInterChanges,7)
Dim DialogTextCommand$(MaxInterChanges,200),DialogTextCommandPos(MaxInterChanges,200), NofTextCommands(MaxInterChanges)
Dim NofInterChangeReplies(MaxInterChanges)
Dim InterChangeReplyText$(MaxInterChanges,8)
Dim InterChangeReplyFunction(MaxInterChanges,8)		
Dim InterChangeReplyData(MaxInterChanges,8)			
Dim InterChangeReplyCommand(MaxInterChanges,8)		
Dim InterChangeReplyCommandData(MaxInterChanges,8,4)

Const MaxAskAbouts=100
Global NofAskAbouts
Global AskAboutTopText$
Dim AskAboutText$(MaxAskAbouts)
Dim AskAboutActive(MaxAskAbouts)
Dim AskAboutInterchange(MaxAskAbouts)
Dim AskAboutRepeat(MaxAskAbouts)


Global ColEffect=-1
Global TxtEffect=-1
Dim CCommands$(20),TCommands$(20)

Restore Commands
For i=0 To 11
	Read CCommands$(i)
Next
For i=0 To 11
	Read TCommands$(i)
Next

Const CharactersPerLine=38
Const CharacterDeleteDelay=50





Global WhichInterChange=0
Global WhichAnswer=0
Global WhichAskAbout=0


; COMPILER DATA

Global NofCompilerFiles
Global NofCustomContentFiles 
Dim CompilerFileName$(500)
Dim CompilerFileSize(500)
						
Dim NofHubCompilerFiles(500)
Dim HubCompilerFileName$(500,500)
Dim HubCompilerFileSize(500,500)

Dim CustomContentFile$(500)

; EDITOR MASTER DATA

Global Dialogtimer

Global Oldx,Oldy,OldMouseX,OldMouseY

Global AdventureStartX,AdventureStartY,AdventureStartDir ; x/y position of player start

Global GateKeyVersion=1

Global AdventureTitle$
Dim AdventureTextLine$(5)

Global MasterDialogListStart=0
Global MasterLevelListStart=0
Const MaxDialog=999
Const MaxLevel=999
Dim MasterDialogList(1000),MasterLevelList(1000)

Global CopyingLevel=False
Global CopiedLevel=-1


Global AdventureExitWonLevel, AdventureExitWonX, AdventureExitWonY ; at what hub level and x/y do you reappear if won.
Global AdventureExitLostLevel, AdventureExitLostX, AdventureExitLostY ; at what hub level and x/y do you reappear if won.


Global AdventureGoal	; when is adventure done
						; 1-NofWeeStinkersInAdventure=0

Dim AdventureWonCommand(3,6)	; 3 commands, each with level/command/fourdata
						
Global StarterItems
Global WidescreenRange=0
Global WidescreenRangeLevel=-1

Dim winningcondition$(20)
Global nofwinningconditions
Restore winning
Repeat
	Read winningcondition$(nofwinningconditions)
	nofwinningconditions=nofwinningconditions+1
Until winningcondition$(nofwinningconditions-1)="Done"
nofwinningconditions=nofwinningconditions-1

Global CurrentLevelNumber

Global ShowingError=False

; END EDITOR MASTER DATA

Global CustomIconName$="Standard"
Global CustomMapName$
						

Global TileOrObject=True 	;true Tile, False Object


Dim LevelMesh(100),LevelSurface(100) ; one for each row
Dim WaterMesh(100),WaterSurface(100)
Dim LogicMesh(100),LogicSurface(100)
Global ShowLogicMesh=False
Global ShowLevelMesh=True
Global ShowObjectMesh=True ; shows/hides objects
Global ShowObjectPositions=False ; this is the marker feature suggested by Samuel
Global BorderExpandOption=0 ;0-current, 1-duplicate
Global BlockMode,FillMode
Global FillDensity#=1.0
Global BlockModeMesh,BlockModeSurface,BlockCornerX,BlockCornerY
Global LevelTextureNum, WaterTextureNum
Dim LevelTextureName$(30),WaterTextureName$(20)
Global LevelTextureCustomName$,WaterTextureCustomName$
Global NofLevelTextures,NofWatertextures,CurrentLevelTexture,CurrentWaterTexture

Global LevelTexture ; the actual texture
Global WaterTexture
Global LevelMusic,LevelWeather

Global Leveltimer

Global CurrentGrabbedObject=-1
Global CurrentGrabbedObjectModified=False
Global BrushSize=1
Global CustomBrush=False
Global CustomBrushEditorMode=-1

Global RandomYawAdjust=False
Global RandomYawAdjustMin#=0.0
Global RandomYawAdjustMax#=360.0
Global RandomRollAdjust=False
Global RandomRollAdjustMin#=0.0
Global RandomRollAdjustMax#=360.0
Global RandomPitchAdjust=False
Global RandomPitchAdjustMin#=0.0
Global RandomPitchAdjustMax#=360.0
Global RandomScaleAdjust=False
Global RandomScaleAdjustMin#=0.5
Global RandomScaleAdjustMax#=1.5
Global RandomXScale=False
Global RandomXScaleMin#=0.5
Global RandomXScaleMax#=1.5
Global RandomYScale=False
Global RandomYScaleMin#=0.5
Global RandomYScaleMax#=1.5
Global RandomZScale=False
Global RandomZScaleMin#=0.5
Global RandomZScaleMax#=1.5
Global RandomXAdjust=False
Global RandomXAdjustMin#=-0.5
Global RandomXAdjustMax#=0.5
Global RandomYAdjust=False
Global RandomYAdjustMin#=-0.5
Global RandomYAdjustMax#=0.5
Global RandomZAdjust=False
Global RandomZAdjustMin#=-0.5
Global RandomZAdjustMax#=0.5
Global RandomTimer=False
Global RandomTimerMin=1
Global RandomTimerMax=100
Global RandomTimerMax1=False
Global RandomTimerMax1Min=1
Global RandomTimerMax1Max=100
Global RandomTimerMax2=False
Global RandomTimerMax2Min=1
Global RandomTimerMax2Max=100
Global RandomID=False
Global RandomIDMin=100
Global RandomIDMax=200
Global RandomMovementSpeed=False
Global RandomMovementSpeedMin=10
Global RandomMovementSpeedMax=40
Global RandomMovementType=False
Global RandomMovementTypeMin=41
Global RandomMovementTypeMax=48
Global RandomType=False
Global RandomTypeMin=170
Global RandomTypeMax=173
Global RandomSubType=False
Global RandomSubTypeMin=0
Global RandomSubTypeMax=8
Global RandomActive=False
Global RandomActiveMin=0
Global RandomActiveMax=1001
Global RandomActivationType=False
Global RandomActivationTypeMin=12
Global RandomActivationTypeMax=16
Global RandomActivationSpeed=False
Global RandomActivationSpeedMin=2
Global RandomActivationSpeedMax=40
Global RandomDefensePower=False
Global RandomDefensePowerMin=0
Global RandomDefensePowerMax=33
Global RandomExclamation=False
Global RandomExclamationMin=0
Global RandomExclamationMax=99
Global RandomMoveXGoal=False
Global RandomMoveXGoalMin=0
Global RandomMoveXGoalMax=39
Global RandomMoveYGoal=False
Global RandomMoveYGoalMin=0
Global RandomMoveYGoalMax=39
Global RandomDead=False
Global RandomDeadMin=0
Global RandomDeadMax=3
Global RandomStatus=False
Global RandomStatusMin=0
Global RandomStatusMax=10
Global RandomTalkable=False
Global RandomTalkableMin=0
Global RandomTalkableMax=0
Global RandomTTC=False
Global RandomOTC=False
Global RandomButtonPush=False
Global RandomTeleportable=False
Dim RandomData(9)
Dim RandomDataMin(9)
Dim RandomDataMax(9)
For i=0 To 9
	RandomDataMax(i)=10
Next

Dim LogicName$(14)
LogicName$(0)="Floor"
LogicName$(1)="Wall"
LogicName$(2)="Water"
LogicName$(3)="Teleporter"
LogicName$(4)="Bridge"
LogicName$(5)="Lava"
LogicName$(6)="06"
LogicName$(7)="07"
LogicName$(8)="08"
LogicName$(9)="Button"
LogicName$(10)="Stinker Exit"
LogicName$(11)="Ice"
LogicName$(12)="Ice Corner"
LogicName$(13)="Ice Wall"
LogicName$(14)="Ice Float"




; Directory Names
Global GlobalDirName$ = "UserData"; SpecialFolderLocation($1c)+"\Midnight Synergy\WA Editor"
CreateDir GlobalDirName$
CreateDir GlobalDirName$+"\Temp"
CreateDir GlobalDirName$+"\Custom"
CreateDir GlobalDirName$+"\Custom\Editing"
CreateDir GlobalDirName$+"\Custom\Editing\Archive"
CreateDir GlobalDirName$+"\Custom\Editing\Current"
CreateDir GlobalDirName$+"\Custom\Editing\Profiles"
CreateDir GlobalDirName$+"\Custom\Editing\Hubs"
CreateDir GlobalDirName$+"\Custom\Downloads Inbox"
CreateDir GlobalDirName$+"\Custom\Downloads Outbox"
CreateDir GlobalDirName$+"\Custom\Leveltextures"
CreateDir GlobalDirName$+"\Custom\Icons"
CreateDir GlobalDirName$+"\Custom\Maps"

Global EditorUserName$=""
Global NofEditorUserNames
Dim EditorUserNamesListed$(100)
Global EditorUserNameEntered$=""

Global AdventureFileName$
Global NofAdventureFileNames
Dim AdventureFileNamesListed$(10000)
Global AdventureNameEntered$=""
Global AdventureFileNamesListedStart
Global AdventureNameSelected
Global AdventureCurrentArchive=0


Global DisplayFullScreen=False


filed=ReadFile (globaldirname$+"\display-ed.wdf")
If filed>0

	DisplayFullScreen=ReadInt(filed)
	CloseFile filed
EndIf



GetTextureNames()





; LEVEL SIZE
; ============
Global LevelWidth=40 ; in tiles
Global LevelHeight=40 ; in tiles

Global LevelEdgeStyle=1

Global WidthLeftChange,WidthRightChange,HeightTopChange,HeightBottomChange

; TILES
; ============
Dim LevelTileTexture(100,100) ; corresponding to squares in LevelTexture
Dim LevelTileRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim LevelTileSideTexture(100,100) ; texture for extrusion walls
Dim LevelTileSideRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim LevelTileRandom#(100,100) ; random height pertubation of tile
Dim LevelTileHeight#(100,100) ; height of "center" - e.g. to make ditches and hills
Dim LevelTileExtrusion#(100,100); extrusion with walls around it 
Dim LevelTileRounding(100,100); 0-no, 1-yes: are floors rounded if on a drop-off corner
Dim LevelTileEdgeRandom(100,100); 0-no, 1-yes: are edges rippled
Dim LevelTileLogic(100,100)

Dim CopyLevelTileTexture(100,100) ; corresponding to squares in LevelTexture
Dim CopyLevelTileRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim CopyLevelTileSideTexture(100,100) ; texture for extrusion walls
Dim CopyLevelTileSideRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim CopyLevelTileRandom#(100,100) ; random height pertubation of tile
Dim CopyLevelTileHeight#(100,100) ; height of "center" - e.g. to make ditches and hills
Dim CopyLevelTileExtrusion#(100,100); extrusion with walls around it 
Dim CopyLevelTileRounding(100,100); 0-no, 1-yes: are floors rounded if on a drop-off corner
Dim CopyLevelTileEdgeRandom(100,100); 0-no, 1-yes: are edges rippled
Dim CopyLevelTileLogic(100,100)

Dim WaterTileTexture(100,100)
Dim WaterTileRotation(100,100)
Dim WaterTileHeight#(100,100)
Dim WaterTileTurbulence#(100,100)

Dim CopyWaterTileTexture(100,100)
Dim CopyWaterTileRotation(100,100)
Dim CopyWaterTileHeight#(100,100)
Dim CopyWaterTileTurbulence#(100,100)

; some helper stuff for the editor
Dim LevelTileVisited(100,100) ; for use in the flood fill algorithm
Dim LevelTileObjectCount(100,100) ; for changing the marker color when there's more than one object present

Dim BrushLevelTileTexture(100,100) ; corresponding to squares in LevelTexture
Dim BrushLevelTileRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim BrushLevelTileSideTexture(100,100) ; texture for extrusion walls
Dim BrushLevelTileSideRotation(100,100) ; 0-3 , and 4-7 for "flipped"
Dim BrushLevelTileRandom#(100,100) ; random height pertubation of tile
Dim BrushLevelTileHeight#(100,100) ; height of "center" - e.g. to make ditches and hills
Dim BrushLevelTileExtrusion#(100,100); extrusion with walls around it 
Dim BrushLevelTileRounding(100,100); 0-no, 1-yes: are floors rounded if on a drop-off corner
Dim BrushLevelTileEdgeRandom(100,100); 0-no, 1-yes: are edges rippled
Dim BrushLevelTileLogic(100,100)

Dim BrushWaterTileTexture(100,100)
Dim BrushWaterTileRotation(100,100)
Dim BrushWaterTileHeight#(100,100)
Dim BrushWaterTileTurbulence#(100,100)

Global ChunkTileU#,ChunkTileV#

Global CurrentMesh,CurrentSurface ; for tile rendering

Global CurrentTileTexture=8
Global CurrentTileRotation
Global CurrentTileSideTexture=6
Global CurrentTileSideRotation
Global CurrentTileRandom#,CurrentTileRandom2
Global CurrentTileHeight#,CurrentTileHeight2
Global CurrentTileExtrusion#,CurrentTileExtrusion2
Global CurrentTileRounding
Global CurrentTileEdgeRandom
Global CurrentTileLogic, CurrentTileLogic2

Global CurrentTileTextureUse=True
Global CurrentTileSideTextureUse=True
Global CurrentTileHeightUse=True
Global CurrentTileExtrusionUse=True
Global CurrentTileRandomUse=True
Global CurrentTileRoundingUse=True
Global CurrentTileEdgeRandomUse=True
Global CurrentTileLogicUse=True

Global CurrentWaterTile,CurrentWaterTileTexture,CurrentWaterTileRotation,CurrentWaterTileHeight#,CurrentWaterTileTurbulence#
Global CurrentWaterTileHeight2,CurrentWaterTileTurbulence2
Global CurrentWaterTileUse=True
Global CurrentWaterTileHeightUse=True
Global CurrentWaterTileTurbulenceUse=True

Global TargetTileTexture=8
Global TargetTileRotation
Global TargetTileSideTexture=6
Global TargetTileSideRotation
Global TargetTileRandom#,TargetTileRandom2
Global TargetTileHeight#,TargetTileHeight2
Global TargetTileExtrusion#,TargetTileExtrusion2
Global TargetTileRounding
Global TargetTileEdgeRandom
Global TargetTileLogic, TargetTileLogic2

Global TargetTileTextureUse=True
Global TargetTileSideTextureUse=True
Global TargetTileHeightUse=True
Global TargetTileExtrusionUse=True
Global TargetTileRandomUse=True
Global TargetTileRoundingUse=True
Global TargetTileEdgeRandomUse=True
Global TargetTileLogicUse=True

Global TargetWaterTile,TargetWaterTileTexture,TargetWaterTileRotation,TargetWaterTileHeight#,TargetWaterTileTurbulence#
Global TargetWaterTileHeight2,TargetWaterTileTurbulence2
Global TargetWaterTileUse=True
Global TargetWaterTileHeightUse=True
Global TargetWaterTileTurbulenceUse=True

Dim FloodStackX(10250) ; no pun intended hahahahaha
Dim FloodStackY(10250)

; TILE PRESETS
; ========================
Global CurrentTilePresetCategory, NofTilePresetCategories
Global CurrentTilePresetTile, NofTilePresetTiles
Dim TilePresetCategoryName$(1000)
Dim TilePresetTileName$(1000)
Dir=ReadDir("Data\Editor\TilePresets")
file$=NextFile$(Dir)
While file$<>""
	If file$<>"." And file$<>".." And FileType("Data\Editor\TilePresets\"+file$)=2
		TilePresetCategoryName$(NofTilePresetCategories)=file$
		NofTilePresetCategories=NofTilePresetCategories+1
	EndIf
	file$=NextFile$(Dir)
Wend
CloseDir dir
i=0
Repeat
	NofTilePresetTiles=0
	Dir=ReadDir("Data\Editor\TilePresets\"+TilePresetCategoryName$(i))
	file$=NextFile$(Dir)
	While file$<>""
		If file$<>"." And file$<>".." And FileType("Data\Editor\TilePresets\"+TilePresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".tp1"
			TilePresetTileName$(NofTilePresetTiles)=file$
			NofTilePresetTiles=NofTilePresetTiles+1
		EndIf
		file$=NextFile$(Dir)
	Wend
	CloseDir dir
	CurrentTilePresetCategory=i
	i=i+1
Until NofTilePresetTiles>0


; OBJECTS
; ------------------------

Global NofObjects=0

Global CurrentObjectModel,CurrentObjectTexture
Global CurrentHatModel,CurrentHatTexture
Global CurrentAccModel,CurrentAccTexture

Dim ObjectEntity(1000),ObjectTexture(1000)
Dim ObjectHatEntity(1000),ObjectHatTexture(1000)
Dim ObjectAccEntity(1000),ObjectAccTexture(1000)
Dim ObjectModelName$(1000)
Dim ObjectTextureName$(1000)
Dim ObjectXScale#(1000)
Dim ObjectZScale#(1000)
Dim ObjectYScale#(1000)
Dim ObjectXAdjust#(1000)
Dim ObjectZAdjust#(1000)
Dim ObjectYAdjust#(1000)
Dim ObjectPitchAdjust#(1000)
Dim ObjectYawAdjust#(1000)
Dim ObjectRollAdjust#(1000)
Dim ObjectX#(1000),ObjectY#(1000),ObjectZ#(1000)
Dim ObjectOldX#(1000),ObjectOldY#(1000),ObjectOldZ#(1000)
Dim ObjectDX#(1000),ObjectDY#(1000),ObjectDZ#(1000)
Dim ObjectPitch#(1000),ObjectYaw#(1000),ObjectRoll#(1000)
Dim ObjectPitch2#(1000),ObjectYaw2#(1000),ObjectRoll2#(1000)
Dim ObjectXGoal#(1000),ObjectYGoal#(1000),ObjectZGoal#(1000)
Dim ObjectMovementType(1000),ObjectMovementTypeData(1000),ObjectSpeed#(1000)
Dim ObjectRadius#(1000),ObjectRadiusType(1000)
Dim ObjectData10(1000)
Dim ObjectPushDX#(1000),ObjectPushDY#(1000)
Dim ObjectAttackPower(1000),ObjectDefensePower(1000),ObjectDestructionType(1000)
Dim ObjectID(1000),ObjectType(1000),ObjectSubType(1000)
Dim ObjectActive(1000),ObjectLastActive(1000),ObjectActivationType(1000),ObjectActivationSpeed(1000)
Dim ObjectStatus(1000),ObjectTimer(1000),ObjectTimerMax1(1000),ObjectTimerMax2(1000)
Dim ObjectTeleportable(1000),ObjectButtonPush(1000),ObjectWaterReact(1000)
Dim ObjectTelekinesisable(1000),ObjectFreezable(1000)
Dim ObjectReactive(1000)
Dim ObjectChild(1000),ObjectParent(1000)
Dim ObjectData(1000,10),ObjectTextData$(1000,4)
Dim ObjectTalkable(1000),ObjectCurrentAnim(1000),ObjectStandardAnim(1000),ObjectTileX(1000),ObjectTileY(1000)
Dim ObjectTileX2(1000),ObjectTileY2(1000),ObjectMovementTimer(1000),ObjectMovementSpeed(1000),ObjectMoveXGoal(1000)
Dim ObjectMoveYGoal(1000),ObjectTileTypeCollision(1000),ObjectObjectTypeCollision(1000),ObjectCaged(1000),ObjectDead(1000)
Dim ObjectDeadTimer(1000),ObjectExclamation(1000),ObjectShadow(1000),ObjectLinked(1000),ObjectLinkBack(1000)
Dim ObjectFlying(1000),ObjectFrozen(1000),ObjectIndigo(1000),ObjectFutureInt24(1000),ObjectFutureInt25(1000)
Dim ObjectScaleAdjust#(1000),ObjectScaleXAdjust#(1000),ObjectScaleYAdjust#(1000),ObjectScaleZAdjust#(1000),ObjectFutureFloat5#(1000)
Dim ObjectFutureFloat6#(1000),ObjectFutureFloat7#(1000),ObjectFutureFloat8#(1000),ObjectFutureFloat9#(1000),ObjectFutureFloat10#(1000)
Dim ObjectFutureString1$(1000),ObjectFutureString2$(1000)

Dim ObjectAdjusterString$(1000,30)

Global HighlightWopAdjusters=True
Global NofWopAdjusters=0
Dim ObjectAdjusterWop$(30)

Dim ObjectPositionMarker(1000)

Dim SimulatedObjectXScale#(1000)
Dim SimulatedObjectZScale#(1000)
Dim SimulatedObjectYScale#(1000)
Dim SimulatedObjectXAdjust#(1000)
Dim SimulatedObjectZAdjust#(1000)
Dim SimulatedObjectYAdjust#(1000)
Dim SimulatedObjectPitchAdjust#(1000)
Dim SimulatedObjectYawAdjust#(1000)
Dim SimulatedObjectRollAdjust#(1000)
Dim SimulatedObjectX#(1000),SimulatedObjectY#(1000),SimulatedObjectZ#(1000)
Dim SimulatedObjectPitch#(1000)
Dim SimulatedObjectYaw#(1000)
Dim SimulatedObjectRoll#(1000)
Dim SimulatedObjectPitch2#(1000),SimulatedObjectYaw2#(1000),SimulatedObjectRoll2#(1000)
Dim SimulatedObjectActive(1000),SimulatedObjectLastActive(1000)
Dim SimulatedObjectStatus(1000)
Dim SimulatedObjectTimer(1000)
Dim SimulatedObjectData(1000,10)
Dim SimulatedObjectMovementSpeed(1000)
Dim SimulatedObjectTileTypeCollision(1000)
;Dim SimulatedObjectScaleAdjust#(1000) ; not useful since ScaleAdjust is set to 1.0 in-game after it is applied to XScale, YScale, and ZScale
Dim SimulatedObjectScaleXAdjust#(1000),SimulatedObjectScaleYAdjust#(1000),SimulatedObjectScaleZAdjust#(1000)

Global CurrentObjectModelName$
Global CurrentObjectTextureName$
Global CurrentObjectXScale#
Global CurrentObjectZScale#
Global CurrentObjectYScale#
Global CurrentObjectXAdjust#
Global CurrentObjectZAdjust#
Global CurrentObjectYAdjust#
Global CurrentObjectPitchAdjust#
Global CurrentObjectYawAdjust#
Global CurrentObjectRollAdjust#
Global CurrentObjectX#,CurrentObjectY#,CurrentObjectZ#
Global CurrentObjectOldX#,CurrentObjectOldY#,CurrentObjectOldZ#
Global CurrentObjectDX#, CurrentObjectDY#, CurrentObjectDZ#
Global CurrentObjectPitch#, CurrentObjectYaw#, CurrentObjectRoll#
Global CurrentObjectPitch2#, CurrentObjectYaw2#, CurrentObjectRoll2#
Global CurrentObjectXGoal#, CurrentObjectYGoal#, CurrentObjectZGoal#
Global CurrentObjectMovementType, CurrentObjectMovementTypeData, CurrentObjectSpeed#
Global CurrentObjectRadius#, CurrentObjectRadiusType
Global CurrentObjectData10
Global CurrentObjectPushDX#, CurrentObjectPushDY#
Global CurrentObjectAttackPower, CurrentObjectDefensePower, CurrentObjectDestructionType
Global CurrentObjectID, CurrentObjectType, CurrentObjectSubType
Global CurrentObjectActive, CurrentObjectLastActive, CurrentObjectActivationType, CurrentObjectActivationSpeed
Global CurrentObjectStatus, CurrentObjectTimer, CurrentObjectTimerMax1, CurrentObjectTimerMax2
Global CurrentObjectTeleportable, CurrentObjectButtonPush, CurrentObjectWaterReact
Global CurrentObjectTelekinesisable, CurrentObjectFreezable
Global CurrentObjectReactive
Global CurrentObjectChild, CurrentObjectParent
Dim CurrentObjectData(10), CurrentObjectTextData$(4)
Global CurrentObjectTalkable,CurrentObjectCurrentAnim,CurrentObjectStandardAnim,CurrentObjectTileX,CurrentObjectTileY
Global CurrentObjectTileX2,CurrentObjectTileY2,CurrentObjectMovementTimer,CurrentObjectMovementSpeed,CurrentObjectMoveXGoal
Global CurrentObjectMoveYGoal,CurrentObjectTileTypeCollision,CurrentObjectObjectTypeCollision,CurrentObjectCaged,CurrentObjectDead
Global CurrentObjectDeadTimer,CurrentObjectExclamation,CurrentObjectShadow,CurrentObjectLinked,CurrentObjectLinkBack
Global CurrentObjectFlying,CurrentObjectFrozen,CurrentObjectIndigo,CurrentObjectFutureInt24,CurrentObjectFutureInt25
Global CurrentObjectScaleAdjust#,CurrentObjectScaleXAdjust#,CurrentObjectScaleYAdjust#,CurrentObjectScaleZAdjust#,CurrentObjectFutureFloat5#
Global CurrentObjectFutureFloat6#,CurrentObjectFutureFloat7#,CurrentObjectFutureFloat8#,CurrentObjectFutureFloat9#,CurrentObjectFutureFloat10#
Global CurrentObjectFutureString1$,CurrentObjectFutureString2$

Global IDFilterEnabled=False
Global IDFilterAllow=-1

Global TexturePrefix$=""

Global SimulationLevel=1
Const SimulationLevelMax=3

Dim BrushObjectModelName$(1000)
Dim BrushObjectTextureName$(1000)
Dim BrushObjectXScale#(1000)
Dim BrushObjectZScale#(1000)
Dim BrushObjectYScale#(1000)
Dim BrushObjectXAdjust#(1000)
Dim BrushObjectZAdjust#(1000)
Dim BrushObjectYAdjust#(1000)
Dim BrushObjectPitchAdjust#(1000)
Dim BrushObjectYawAdjust#(1000)
Dim BrushObjectRollAdjust#(1000)
Dim BrushObjectX#(1000),BrushObjectY#(1000),BrushObjectZ#(1000)
Dim BrushObjectOldX#(1000),BrushObjectOldY#(1000),BrushObjectOldZ#(1000)
Dim BrushObjectDX#(1000),BrushObjectDY#(1000),BrushObjectDZ#(1000)
Dim BrushObjectPitch#(1000),BrushObjectYaw#(1000),BrushObjectRoll#(1000)
Dim BrushObjectPitch2#(1000),BrushObjectYaw2#(1000),BrushObjectRoll2#(1000)
Dim BrushObjectXGoal#(1000),BrushObjectYGoal#(1000),BrushObjectZGoal#(1000)
Dim BrushObjectMovementType(1000),BrushObjectMovementTypeData(1000),BrushObjectSpeed#(1000)
Dim BrushObjectRadius#(1000),BrushObjectRadiusType(1000)
Dim BrushObjectData10(1000)
Dim BrushObjectPushDX#(1000),BrushObjectPushDY#(1000)
Dim BrushObjectAttackPower(1000),BrushObjectDefensePower(1000),BrushObjectDestructionType(1000)
Dim BrushObjectID(1000),BrushObjectType(1000),BrushObjectSubType(1000)
Dim BrushObjectActive(1000),BrushObjectLastActive(1000),BrushObjectActivationType(1000),BrushObjectActivationSpeed(1000)
Dim BrushObjectStatus(1000),BrushObjectTimer(1000),BrushObjectTimerMax1(1000),BrushObjectTimerMax2(1000)
Dim BrushObjectTeleportable(1000),BrushObjectButtonPush(1000),BrushObjectWaterReact(1000)
Dim BrushObjectTelekinesisable(1000),BrushObjectFreezable(1000)
Dim BrushObjectReactive(1000)
Dim BrushObjectChild(1000),BrushObjectParent(1000)
Dim BrushObjectData(1000,10),BrushObjectTextData$(1000,4)
Dim BrushObjectTalkable(1000),BrushObjectCurrentAnim(1000),BrushObjectStandardAnim(1000),BrushObjectTileX(1000),BrushObjectTileY(1000)
Dim BrushObjectTileX2(1000),BrushObjectTileY2(1000),BrushObjectMovementTimer(1000),BrushObjectMovementSpeed(1000),BrushObjectMoveXGoal(1000)
Dim BrushObjectMoveYGoal(1000),BrushObjectTileTypeCollision(1000),BrushObjectObjectTypeCollision(1000),BrushObjectCaged(1000),BrushObjectDead(1000)
Dim BrushObjectDeadTimer(1000),BrushObjectExclamation(1000),BrushObjectShadow(1000),BrushObjectLinked(1000),BrushObjectLinkBack(1000)
Dim BrushObjectFlying(1000),BrushObjectFrozen(1000),BrushObjectIndigo(1000),BrushObjectFutureInt24(1000),BrushObjectFutureInt25(1000)
Dim BrushObjectScaleAdjust#(1000),BrushObjectScaleXAdjust#(1000),BrushObjectScaleYAdjust#(1000),BrushObjectScaleZAdjust#(1000),BrushObjectFutureFloat5#(1000)
Dim BrushObjectFutureFloat6#(1000),BrushObjectFutureFloat7#(1000),BrushObjectFutureFloat8#(1000),BrushObjectFutureFloat9#(1000),BrushObjectFutureFloat10#(1000)
Dim BrushObjectFutureString1$(1000),BrushObjectFutureString2$(1000)

Dim BrushObjectXOffset#(1000),BrushObjectYOffset#(1000)

Global NofBrushObjects=0


; Object PRESETS
; ========================
Global CurrentObjectPresetCategory, NofObjectPresetCategories
Global CurrentObjectPresetObject, NofObjectPresetObjects
Dim ObjectPresetCategoryName$(1000)
Dim ObjectPresetObjectName$(1000)
Dir=ReadDir("Data\Editor\ObjectPresets")
file$=NextFile$(Dir)
While file$<>""
	If file$<>"." And file$<>".." And FileType("Data\Editor\ObjectPresets\"+file$)=2
		ObjectPresetCategoryName$(NofObjectPresetCategories)=file$
		NofObjectPresetCategories=NofObjectPresetCategories+1
	EndIf
	file$=NextFile$(Dir)
Wend

CloseDir dir
i=0
Repeat
	NofObjectPresetObjects=0
	Dir=ReadDir("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(i))
	file$=NextFile$(Dir)
	While file$<>""
		If file$<>"." And file$<>".." And FileType("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".wop"
			ObjectPresetObjectName$(NofObjectPresetObjects)=file$
			NofObjectPresetObjects=NofObjectPresetObjects+1
		EndIf
		file$=NextFile$(Dir)
	Wend
	CloseDir dir
	CurrentObjectPresetCategory=i
	i=i+1
Until NofObjectPresetObjects>0

;Dim ObjectAdjuster$(30)
Dim ObjectAdjuster$(60)
Global NofObjectAdjusters,ObjectAdjusterStart

; GLOBAL LEVELSETTINGS
; ========================
Global WaterFlow=1 
Global WaterTransparent=True
Global WaterGlow=False

Global LightRed=255
Global LightGreen=255
Global LightBlue=255
Global AmbientRed=100
Global AmbientGreen=100
Global AmbientBlue=100

Global SimulatedLightRed,SimulatedLightGreen,SimulatedLightBlue,SimulatedLightRedGoal,SimulatedLightGreenGoal,SimulatedLightBlueGoal,SimulatedLightChangeSpeed
Global SimulatedAmbientRed,SimulatedAmbientGreen,SimulatedAmbientBlue,SimulatedAmbientRedGoal,SimulatedAmbientGreenGoal,SimulatedAmbientBlueGoal,SimulatedAmbientChangeSpeed

; IN-GAME-ESQUE LEVELMESH
; =========================

Global LevelDetail ; 1-x how many subdivisions per tile

Global BackGroundEntity1, BackGroundEntity2
Dim BackGroundTexture1(30),BackGroundTexture2(30)

Global CurrentLevelChunk=0
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

; water control 
Dim ChunkWaterTileTexture(100,100)	; -1: none, 0-3 water textures
Dim ChunkWaterTileRotation(100,100) ; 0-7 
Dim ChunkWaterTileHeight#(100,100)  ; height of water tile

Dim LevelEntity(200),LevelSurface(200) ; one for each chunk
Dim WaterEntity(200),WaterSurface(200)
Dim LevelTextures(30),WaterTextures(10)

; Setup Graphics
; =========================

If displayfullscreen=True
	Graphics3D 800,600,16,1
Else
	Graphics3D 800,600,16,2
EndIf
SetBuffer BackBuffer()

	; Preloaded Level Textures
	LevelTextures(0)=MyLoadTexture("data\leveltextures\leveltex abstract.bmp",1)
	LevelTextures(1)=MyLoadTexture("data\leveltextures\leveltex cave.bmp",1)
	LevelTextures(2)=MyLoadTexture("data\leveltextures\leveltex cellar.bmp",1)
	LevelTextures(3)=MyLoadTexture("data\leveltextures\leveltex forest.bmp",1)
	LevelTextures(4)=MyLoadTexture("data\leveltextures\leveltex hills.bmp",1)
	LevelTextures(5)=MyLoadTexture("data\leveltextures\leveltex snow.bmp",1)
	LevelTextures(6)=MyLoadTexture("data\leveltextures\leveltex temple.bmp",1)
	LevelTextures(7)=MyLoadTexture("data\leveltextures\leveltex town.bmp",1)
	LevelTextures(8)=MyLoadTexture("data\leveltextures\leveltex lava.bmp",1)
	LevelTextures(9)=MyLoadTexture("data\leveltextures\leveltex beach.bmp",1)
	LevelTextures(10)=MyLoadTexture("data\leveltextures\leveltex jungle.bmp",1)
	LevelTextures(11)=MyLoadTexture("data\leveltextures\leveltex jtemple.bmp",1)
	LevelTextures(12)=MyLoadTexture("data\leveltextures\leveltex wonderfalls.bmp",1)
	LevelTextures(13)=MyLoadTexture("data\leveltextures\leveltex cave2.bmp",1)
	LevelTextures(14)=MyLoadTexture("data\leveltextures\leveltex sundog.bmp",1)
	LevelTextures(15)=MyLoadTexture("data\leveltextures\leveltex techno.bmp",1)
	LevelTextures(16)=MyLoadTexture("data\leveltextures\leveltex stars.bmp",1)
	LevelTextures(17)=MyLoadTexture("data\leveltextures\leveltex desert.bmp",1)
	LevelTextures(18)=MyLoadTexture("data\leveltextures\leveltex castle.bmp",1)
	LevelTextures(19)=MyLoadTexture("data\leveltextures\leveltex jaava.bmp",1)
	LevelTextures(20)=MyLoadTexture("data\leveltextures\leveltex uo.bmp",1)
	LevelTextures(21)=MyLoadTexture("data\leveltextures\leveltex spooky.bmp",1)




	Backgroundtexture1(0)=MyLoadTexture("data\leveltextures\backgroundtex abstract1.bmp",1)
	Backgroundtexture2(0)=MyLoadTexture("data\leveltextures\backgroundtex abstract2.bmp",1)
	Backgroundtexture1(1)=MyLoadTexture("Data\leveltextures\backgroundtex cave1.bmp",1)
	Backgroundtexture2(1)=MyLoadTexture("data\leveltextures\backgroundtex cave2.bmp",1)
	Backgroundtexture1(2)=MyLoadTexture("data\leveltextures\backgroundtex cellar1.bmp",1)
	Backgroundtexture2(2)=MyLoadTexture("data\leveltextures\backgroundtex cellar2.bmp",1)
	Backgroundtexture1(3)=MyLoadTexture("data\leveltextures\backgroundtex forest1.bmp",1)
	Backgroundtexture2(3)=MyLoadTexture("data\leveltextures\backgroundtex forest2.bmp",1)
	Backgroundtexture1(4)=MyLoadTexture("data\leveltextures\backgroundtex hills1.bmp",1)
	Backgroundtexture2(4)=MyLoadTexture("data\leveltextures\backgroundtex hills2.bmp",1)
	Backgroundtexture1(5)=MyLoadTexture("data\leveltextures\backgroundtex snow1.bmp",1)
	Backgroundtexture2(5)=MyLoadTexture("data\leveltextures\backgroundtex snow2.bmp",1)
	Backgroundtexture1(6)=MyLoadTexture("data\leveltextures\backgroundtex temple1.bmp",1)
	Backgroundtexture2(6)=MyLoadTexture("data\leveltextures\backgroundtex temple2.bmp",1)
	Backgroundtexture1(7)=MyLoadTexture("data\leveltextures\backgroundtex town1.bmp",1)
	Backgroundtexture2(7)=MyLoadTexture("data\leveltextures\backgroundtex town2.bmp",1)
	Backgroundtexture1(8)=MyLoadTexture("data\leveltextures\backgroundtex lava1.bmp",1)
	Backgroundtexture2(8)=MyLoadTexture("data\leveltextures\backgroundtex lava2.bmp",1)
	Backgroundtexture1(9)=MyLoadTexture("data\leveltextures\backgroundtex beach1.bmp",1)
	Backgroundtexture2(9)=MyLoadTexture("data\leveltextures\backgroundtex beach2.bmp",1)
	Backgroundtexture1(10)=MyLoadTexture("data\leveltextures\backgroundtex jungle1.bmp",1)
	Backgroundtexture2(10)=MyLoadTexture("data\leveltextures\backgroundtex jungle2.bmp",1)
	Backgroundtexture1(11)=MyLoadTexture("data\leveltextures\backgroundtex jtemple1.bmp",1)
	Backgroundtexture2(11)=MyLoadTexture("data\leveltextures\backgroundtex jtemple2.bmp",1)
	Backgroundtexture1(12)=MyLoadTexture("data\leveltextures\backgroundtex wonderfalls1.bmp",1)
	Backgroundtexture2(12)=MyLoadTexture("data\leveltextures\backgroundtex wonderfalls2.bmp",1)
	Backgroundtexture1(13)=MyLoadTexture("Data\leveltextures\backgroundtex cave21.bmp",1)
	Backgroundtexture2(13)=MyLoadTexture("data\leveltextures\backgroundtex cave22.bmp",1)
	Backgroundtexture1(14)=MyLoadTexture("Data\leveltextures\backgroundtex sundog1.bmp",1)
	Backgroundtexture2(14)=MyLoadTexture("data\leveltextures\backgroundtex sundog2.bmp",1)
	Backgroundtexture1(15)=MyLoadTexture("Data\leveltextures\backgroundtex techno1.bmp",1)
	Backgroundtexture2(15)=MyLoadTexture("data\leveltextures\backgroundtex techno2.bmp",1)
	Backgroundtexture1(16)=MyLoadTexture("Data\leveltextures\backgroundtex stars1.bmp",1)
	Backgroundtexture2(16)=MyLoadTexture("data\leveltextures\backgroundtex stars2.bmp",1)
	Backgroundtexture1(17)=MyLoadTexture("Data\leveltextures\backgroundtex desert1.bmp",1)
	Backgroundtexture2(17)=MyLoadTexture("data\leveltextures\backgroundtex desert2.bmp",1)
	Backgroundtexture1(18)=MyLoadTexture("Data\leveltextures\backgroundtex castle1.bmp",1)
	Backgroundtexture2(18)=MyLoadTexture("data\leveltextures\backgroundtex castle2.bmp",1)
	Backgroundtexture1(19)=MyLoadTexture("Data\leveltextures\backgroundtex jaava1.bmp",1)
	Backgroundtexture2(19)=MyLoadTexture("data\leveltextures\backgroundtex jaava2.bmp",1)
	Backgroundtexture1(20)=MyLoadTexture("Data\leveltextures\backgroundtex uo1.bmp",1)
	Backgroundtexture2(20)=MyLoadTexture("data\leveltextures\backgroundtex uo2.bmp",1)
	Backgroundtexture1(21)=MyLoadTexture("Data\leveltextures\backgroundtex spooky1.bmp",1)
	Backgroundtexture2(21)=MyLoadTexture("data\leveltextures\backgroundtex spooky2.bmp",1)


	WaterTextures(0)=MyLoadTexture("data\leveltextures\watertex 1.jpg",1)
	WaterTextures(1)=MyLoadTexture("data\leveltextures\watertex 2.jpg",1)
	WaterTextures(2)=MyLoadTexture("data\leveltextures\watertex 3.jpg",1)
	WaterTextures(3)=MyLoadTexture("data\leveltextures\watertex 4.jpg",1)






; ******************************************************


; Setup Lights, Camera
; ================================

Global Light=CreateLight()
AmbientLight 155,155,155
RotateEntity Light,80,15,0

Global Camera1 ; level camera
Global Camera2
Global camera3
Global camera4 ; object camera
Global camera ; text screen camera
Global CameraPanning=False

Camera1 = CreateCamera()
TurnEntity Camera1,65,0,0
PositionEntity Camera1,7,6,-14 
CameraViewport camera1,0,0,500,500
CameraRange camera1,.1,50

Camera2 = CreateCamera()
CameraClsColor camera2,255,0,0
CameraViewport Camera2,510,20,200,220
CameraRange camera2,.1,1000
RotateEntity Camera2,45,25,0
PositionEntity Camera2,4.9,109,-8
CameraZoom Camera2,5

Camera3 = CreateCamera()
CameraClsColor camera3,255,0,0
CameraViewport Camera3,0,0,500,500
CameraRange camera3,.1,1000
RotateEntity Camera3,90,0,0
PositionEntity Camera3,0.5,210,-0.5
CameraZoom Camera3,20
CameraProjMode Camera3,0

Camera4 = CreateCamera() ; objects menu camera
CameraClsColor camera4,155,0,0
CameraViewport Camera4,695,305,100,125
CameraRange camera4,.1,1000
RotateEntity Camera4,25,0,0
PositionEntity Camera4,0,303.8,-8
Global Camera4Zoom#=8.0
CameraZoom Camera4,Camera4Zoom#

Camera = CreateCamera() ; Text Screen Camera

s=CreateMesh()
su=CreateSurface(s)
AddVertex (su,-1.5,300,1.5)
AddVertex (su,1.5,300,1.5)
AddVertex (su,-1.5,300,-1.5)
AddVertex (su,1.5,300,-1.5)
AddTriangle (su,0,1,2)
AddTriangle (su,2,1,3)
EntityColor s,255,255,255
EntityAlpha s,0.5

; Create Meshes
; =================
; Cursor
Global CursorMesh=CreateCube()
ScaleMesh CursorMesh,0.1,10,0.1
tweeny=CreateCube()
ScaleMesh tweeny,.5,0.1,.5
AddMesh tweeny,CursorMesh
FreeEntity tweeny
EntityAlpha CursorMesh,.3
EntityColor CursorMesh,255,255,200
Global CursorMesh2=CreateCube()
ScaleMesh CursorMesh2,.2,0.01,.2
; MousePlane
Global MousePlane=CreateMesh()
MouseSurface=CreateSurface(MousePlane)
AddVertex MouseSurface,0,0,0
AddVertex MouseSurface,100,0,0
AddVertex MouseSurface,0,0,-100
AddVertex MouseSurface,100,0,-100
AddTriangle MouseSurface,0,1,2
AddTriangle MouseSurface,1,3,2
EntityPickMode MousePlane,2
EntityAlpha MousePlane,0
; TexturePlane
Global TexturePlane=CreateMesh()
TexturePlaneSurface=CreateSurface(TexturePlane)
AddVertex TexturePlaneSurface,0,200,0,0,0
AddVertex TexturePlaneSurface,1,200,0,1,0
AddVertex TexturePlaneSurface,0,200,-1,0,1
AddVertex TexturePlaneSurface,1,200,-1,1,1
AddTriangle TexturePlaneSurface,0,1,2
AddTriangle TexturePlaneSurface,1,3,2
UpdateNormals TexturePlane
EntityPickMode TexturePlane,2
; CurrentTile
CurrentMesh=CreateMesh()
CurrentSurface=CreateSurface(CurrentMesh)
; BlockMode
BlockModeMesh=CreateMesh()
BlockModeSurface=CreateSurface(BlockModeMesh)
AddVertex (BlockModeSurface,0,0,0)
AddVertex (BlockModeSurface,0,0,0)
AddVertex (BlockModeSurface,0,0,0)
AddVertex (BlockModeSurface,0,0,0)
AddTriangle (BlockModeSurface,0,1,2)
AddTriangle (BlockModeSurface,1,3,2)
EntityAlpha BlockModeMesh,0.5
EntityOrder BlockModeMesh,-1
; CurrentObjectMarker
CurrentObjectMarkerMesh=CreateCylinder()
ScaleEntity CurrentObjectMarkerMesh,.01,3,.01
PositionEntity CurrentObjectMarkerMesh,0,300,0
;EntityBlend CurrentObjectMarkerMesh,3

; ObjectPositionMarker
Global ObjectPositionMarkerMesh=CreateCube()
ScaleMesh ObjectPositionMarkerMesh,0.08,90,0.08
;EntityAlpha ObjectPositionMarkerMesh,.3
;EntityColor ObjectPositionMarkerMesh,100,255,100
HideEntity ObjectPositionMarkerMesh

; CurrentGrabbedObjectMarker
Global CurrentGrabbedObjectMarker=CreateCube()
;inside=CreateCube()
;ScaleMesh inside,-1.0,1.0,1.0
;AddMesh inside,CurrentGrabbedObjectMarker
;FreeEntity inside
ScaleMesh CurrentGrabbedObjectMarker,0.5,90,0.5
EntityColor CurrentGrabbedObjectMarker,100,255,100
EntityFX CurrentGrabbedObjectMarker,16 ; disable back-face culling
HideEntity CurrentGrabbedObjectMarker




; Load Textures
; =================
LevelTexture=MyLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
EntityTexture TexturePlane,LevelTexture
WaterTexture=MyLoadTexture("data\Leveltextures\"+WaterTExturename$(currentwatertexture),2)
Global ButtonTexture=MyLoadTexture("data\graphics\buttons1.bmp",4)
Global GateTexture=MyLoadTexture("data\graphics\gates1.bmp",1)
Global CloudTexture=myLoadTexture("Data\graphics\cloud.jpg",4)

Global PlasmaTexture=myLoadTexture("data\models\other\growflower.jpg",1)

Global FloingTexture=myLoadTexture("data\models\other\floingbubble.jpg",1)

Global SkyMachineMapTexture=myLoadTexture("data\models\other\projected.jpg",1)

; Teleport
Dim TeleporterTexture(16)
For i=0 To 8
	TeleporterTexture(i)=MyLoadTexture("data/models/teleport/teleport"+Str$(i)+".jpg",1)
Next


; PreLoad Models
; ==================

; StinkerWee
Global StinkerWeeMesh=MyLoadMD2("data\models\stinkerwee\stinkerwee.md2")
Global StinkerWeeTexture=MyLoadTexture("data\models\stinkerwee\stinkerwee1.jpg",1)
EntityTexture StinkerWeeMesh,StinkerWeeTexture
HideEntity StinkerWeeMesh

; Stinker

Global StinkerMesh=myLoadAnimMesh("data/models/stinker/body.b3d",0)
Global StinkerTexture=MyLoadTexture("data\models\stinker\body001a.jpg",1)
EntityTexture GetChild(StinkerMesh,3),StinkerTexture
HideEntity StinkerMesh


; Cage
Global CageMesh=MyLoadMesh("data/models/cage/cage.3ds",0)
RotateMesh CageMesh,-90,0,0
Global CageTexture=MyLoadTexture("data/models/cage/cage.jpg",1)
EntityTexture CageMesh,CageTexture
HideEntity CageMesh


; AutoDoor
Global AutodoorMesh=CreateCube()
ScaleMesh autodoormesh,.5,.5,.5
PositionMesh Autodoormesh,0,.5,0
Global Autodoortexture=MyLoadTexture("data/models/autodoor/autodoor.jpg",1)
EntityTexture Autodoormesh,autodoortexture
HideEntity autodoormesh

; StarGate
Global StarGateMesh=MyLoadMesh("data/models/gate/gate.3ds",0)
RotateMesh StarGateMesh,-90,0,0
HideEntity StarGateMesh

; Scritter
Global ScritterMesh=MyLoadMesh("data/models/scritter/scritter.3ds",0)
RotateMesh ScritterMesh,-90,0,0
Dim ScritterTexture(7)
For i=0 To 6
	ScritterTexture(i)=MyLoadTexture("data/models/scritter/scritter"+Str$(i)+".jpg",1)
Next
HideEntity ScritterMesh


; Stepping Stone
Global SteppingStoneMesh=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
Dim SteppingStoneTexture(4)
For i=0 To 3
	SteppingStoneTexture(i)=MyLoadTexture("data\models\bridges\bridge"+i+".jpg",1)
Next
HideEntity SteppingStoneMesh

; WaterFall
Dim WaterFallTexture(2)
WaterFallTexture(0)=MyLoadTexture("data\Leveltextures\waterfall.jpg",1)
WaterFallTexture(1)=MyLoadTexture("data\Leveltextures\waterfalllava.jpg",1)
WaterFallTexture(2)=MyLoadTexture("data\Leveltextures\waterfallgreen.jpg",1)
Global WaterFallMesh;

; Star
Global StarMesh=myLoadMesh("data\models\star\star.3ds",0)
Global	GoldStarTexture=myLoadTexture("data\models\star\goldstar.jpg",1)
Dim	WispTexture(10)
For i=0 To 9
	WispTexture(i)=MyLoadTexture("data\models\star\wisp"+Str$(i)+".jpg",1)
Next
EntityTexture StarMesh,GoldStarTexture
HideEntity StarMesh


; Coin
Global CoinMesh=MyLoadMesh("data\models\coin\coin.3ds",0)
Global	GoldCoinTexture=MyLoadTexture("data\models\coin\coin.jpg",1)
Global	TokenCoinTexture=MyLoadTexture("data\models\coin\token.jpg",1)
EntityTexture CoinMesh,GoldCoinTexture
HideEntity CoinMesh

; Key
Global	KeyMesh=myLoadMesh("data\models\keys\key.3ds",0)
HideEntity KeyMesh



; CustomItem
Global IconTextureCustom=0


; Signs
Dim SignMesh(5),SignTexture(5)
For i=0 To 5
	SignMesh(i)=MyLoadMesh("data\models\sign\sign"+Str$(i)+".3ds",0)
	SignTexture(i)=MyLoadTexture("data\models\sign\sign"+Str$(i)+".jpg",1)
	HideEntity SignMesh(i)
Next



; Houses
Dim DoorTexture(10),CottageTexture(10),HouseTexture(10),WindmillTexture(10),FenceTexture(10)
For i=0 To 2
	DoorTexture(i)=MyLoadTexture("data\models\houses\door"+Str$(i)+".png",1)
Next
For i=0 To 1
	CottageTexture(i)=MyLoadTexture("data\models\houses\cottage"+Str$(i)+".png",1)
Next
For i=0 To 2
	HouseTexture(i)=MyLoadTexture("data\models\houses\townhouse"+Str$(i)+".png",1)
Next
For i=0 To 0
	WindmillTexture(i)=MyLoadTexture("data\models\houses\windmill"+Str$(i)+".png",1)
Next
For i=0 To 0
	FenceTexture(i)=MyLoadTexture("data\models\houses\fence"+Str$(i)+".png",1)
Next
Global Fountaintexture=MyLoadTexture("data\models\houses\fountain01.png",1)




; Gems
Dim GemMesh(10)
For i=0 To 2
	GemMesh(i)=myLoadMesh("data\models\gems\gem"+Str$(i)+".3ds",0)
	HideEntity GemMesh(i)
Next

; Turtles
Global TurtleMesh=MyLoadMesh("data\models\turtle\dragonturtle2.3ds",0)
RotateMesh TurtleMesh,-90,0,0
RotateMesh TurtleMesh,0,90,0
Global TurtleTexture=MyLoadTexture("data\models\turtle\dragonturtle2.png",1)
EntityTexture TurtleMesh,TurtleTexture
HideEntity TurtleMesh

; FireFlowers
;Global FireFlowerMesh=myLoadMD2("data\models\fireflower\fireflower.wdf")
;RotateEntity FireFlowerMesh,-90,0,0
;RotateEntity FireFlowerMesh,0,90,0
Global FireFlowerMesh=MyLoadMesh("data\models\fireflower\fireflower2.3ds",0)
RotateMesh FireFlowerMesh,-90,0,0
RotateMesh FireFlowerMesh,0,90,0

Global FireFlowerTexture=MyLoadTexture("data\models\fireflower\fireflower04.png",1)
Global FireFlowerTexture2=myLoadTexture("data\models\fireflower\fireflowerice.png",4)
EntityTexture FireFlowerMesh,FireFlowerTexture
HideEntity FireFlowerMesh

; BurstFlowers
Global BurstFlowerMesh=MyLoadMesh("data\models\burstflower\burstflower.b3d",0)
Global BurstFlowerTexture=MyLoadTexture("data\models\burstflower\burstflower.png",1)
EntityTexture BurstFlowerMesh,BurstFlowerTexture
HideEntity BurstFlowerMesh


; Boxes etc
Global BarrelMesh,BarrelTexture1,BarrelTexture2,BarrelTexture3
BarrelMesh=MyLoadMesh("data\models\barrels\barrel.b3d",0)
BarrelTexture1=MyLoadTexture("Data\models\barrels\barrel1.jpg",1)
BarrelTexture2=MyLoadTexture("Data\models\barrels\barrel2.jpg",1)
BarrelTexture3=MyLoadTexture("Data\newmodels\barrels\barrel3.jpg",1)
HideEntity BarrelMesh

; Prism
Global PrismMesh=MyLoadMesh ("data\newmodels\retro\prism.3ds",0)
Global PrismTexture=MyLoadTexture ("data\newmodels\retro\prism.jpg",1)
EntityTexture PrismMesh,PrismTexture
HideEntity PrismMesh

; Chompers
Global ChomperMesh,ChomperTexture,WaterChomperTexture,MechaChomperTexture
ChomperMesh=MyLoadMD2("data\models\chomper\chomper.md2")
ChomperTexture=MyLoadTexture("Data\models\chomper\chomper.png",1)
WaterChomperTexture=MyLoadTexture("Data\models\chomper\wchomper.png",1)
MechaChomperTexture=MyLoadTexture("Data\models\chomper\mchomper.png",1)
EntityTexture ChomperMesh,ChomperTexture
HideEntity ChomperMesh

; Bowlers
Global BowlerMesh,BowlerTexture
BowlerMesh=MyLoadMesh("data\models\spikyball\spikeyball01.b3d",0)
BowlerTexture=MyLoadTexture("Data\models\spikyball\spikeyball01.png",1)
EntityTexture BowlerMesh,BowlerTexture
HideEntity BowlerMesh

; Busterfly
Global BusterflyMesh,BusterflyTexture
BusterflyMesh=MyLoadMesh("data\models\busterfly\buster2.3ds",0)
BusterflyTexture=MyLoadTexture("Data\models\busterfly\buster1.bmp",4)
EntityTexture BusterflyMesh,BusterflyTexture
ScaleMesh busterflymesh,.01,.01,.01
RotateMesh busterflymesh,-180,0,0
HideEntity BusterflyMesh

; Ducky
Global Rubberduckymesh,rubberduckytexture
rubberduckymesh=myLoadMesh("data\models\rubberducky\rubberducky.b3d",0)
rubberduckytexture=myLoadTexture("data\models\rubberducky\rubberducky.png",1)
EntityTexture rubberduckymesh,rubberduckytexture
HideEntity rubberduckymesh

; Spring
Global SpringTexture=MyLoadTexture("Data\models\bridges\spring.jpg",1)

; Thwarts
Global ThwartMesh=myLoadMD2("data\models\thwart\thwart05.md2")
Dim ThwartTexture(9)
For i=0 To 7
	ThwartTexture(i)=myLoadTexture("data\models\thwart\thwart"+Str$(i)+".jpg",1)
Next
EntityTexture ThwartMesh,ThwartTexture(0)
HideEntity ThwartMesh

; Tentacle
Global TentacleMesh=myLoadAnimMesh("data\models\trees\tentacle.b3d",0)
ExtractAnimSeq GetChild(Tentaclemesh,3),41,60
Global TentacleTexture = myLoadTexture ("data\models\trees\tentacle.jpg",1)

For i=1 To CountChildren(tentaclemesh)
	EntityTexture GetChild(tentaclemesh,i),tentacletexture
Next
;EntityTexture tentaclemesh,tentacletexture
HideEntity TentacleMesh

; Crabs
Global CrabMesh=myLoadMD2("data\models\crab\crab.md2")
Global CrabTexture1=myLoadTexture("data\models\crab\crab03a.jpg",1)
Global CrabTexture2=myLoadTexture("data\models\crab\crab03b.jpg",1)
EntityTexture CrabMesh,CrabTexture1
HideEntity CrabMesh

; Ice Troll
Global TrollMesh=myLoadMD2("data\models\thwart\ice troll.md2")
Global TrollTexture=myLoadTexture("data\models\thwart\icetroll01.bmp",1)
EntityTexture TrollMesh,TrollTexture
HideEntity TrollMesh

; Kaboom
Global KaboomMesh=myLoadMD2("data\models\kaboom\kaboom.md2")
Global KaboomTexture=myLoadTexture("data\models\kaboom\kaboom01.jpg",1)
EntityTexture KaboomMesh,KaboomTexture
HideEntity KaboomMesh

; Retrostuff
Global RetroBoxMesh=myLoadMesh("data\models\retro\box.3ds",0)
Global RetroBoxTexture=myLoadTexture("data\models\retro\woodbox.bmp",1)
EntityTexture Retroboxmesh,retroboxtexture
HideEntity RetroBoxMesh
Global RetroCoilyMesh=myLoadMD2("data\models\retro\coily.md2")
Global RetroCoilyTexture=myLoadTexture("data\models\retro\coily.bmp",1)
EntityTexture Retrocoilymesh,retrocoilytexture
HideEntity RetroCoilyMesh
Global RetroScougeMesh=myLoadMesh("data\models\retro\scouge.3ds",0)
Global RetroScougeTexture=myLoadTexture("data\models\retro\scouge3.bmp",1)
EntityTexture Retroscougemesh,retroscougetexture
RotateMesh RetroScougeMesh,-90,0,0
RotateMesh RetroScougeMesh,0,-90,0
HideEntity RetroScougeMesh
Global RetroUfoMesh=myLoadMesh("data\models\retro\ufo.3ds",0)
Global RetroUfoTexture=myLoadTexture("data\models\retro\ufo.bmp",1)
EntityTexture retroufomesh,retroufotexture
RotateMesh RetroUFOMesh,-90,0,0
RotateMesh RetroUFOMesh,0,-90,0
HideEntity RetroUFOMesh
Global RetroZbotMesh=myLoadMesh("data\models\retro\zbot.3ds",0)
Global RetroZbotTexture=myLoadTexture("data\models\retro\zbot.bmp",1)
EntityTexture retrozbotmesh,retrozbottexture
RotateMesh RetrozbotMesh,-90,0,0
RotateMesh RetrozbotMesh,0,90,0
HideEntity RetrozbotMesh
Global RetroRainbowCoinTexture=myLoadTexture("data\models\retro\rainbowcoin.bmp",1)

; Zbots
Global WeeBotMesh=myLoadMesh("data\models\weebot\weebot.3ds",0)
Global WeebotTexture=myLoadTexture("data\models\weebot\weebot.jpg",1)
EntityTexture Weebotmesh,Weebottexture
RotateMesh WeebotMesh,-90,0,0
RotateMesh WeebotMesh,0,180,0
HideEntity WeebotMesh

Global ZapbotMesh=myLoadMesh("data\models\zapbot\zapbot.3ds",0)
Global ZapbotTexture=myLoadTexture("data\models\zapbot\zapbot.jpg",1)
EntityTexture Zapbotmesh,Zapbottexture
RotateMesh ZapbotMesh,-90,0,0
RotateMesh ZapbotMesh,0,180,0
HideEntity ZapbotMesh

Global PushbotMesh
Global PushbotTexture=myLoadTexture("data\graphics\pushbot.bmp",1)

Global ZbotNPCMesh=myLoadMesh("data\models\zbots\zbotnpc.3ds",0)

RotateMesh ZBOTNPCMesh,-90,0,0
RotateMesh ZBOTNPCMesh,0,-90,0

Dim ZbotNPCTexture(8)
For i=0 To 7
	ZbotNPCTexture(i)=myLoadTexture("data\models\zbots\zbotnpc.jpg",1)
Next
EntityTexture ZbotNPCMesh,ZBotNPCTexture(0)
HideEntity ZbotNPCMesh

Global MothershipMesh=myLoadMesh("data\models\other\starship.3ds",0)
Global	Mothershiptexture=myLoadTexture("data\models\other\mothership.jpg",1)
EntityTexture Mothershipmesh,Mothershiptexture
HideEntity Mothershipmesh


; Portal
Global PortalWarpMesh=CreateCylinder()
	RotateMesh PortalWarpMesh,-90,0,0
	ScaleMesh PortalWarpMesh,2,2,4.5*1.15
	HideEntity PortalWarpMesh

Global StarTexture=myloadTexture("data\graphics\stars.jpg",1)
Global RainbowTexture=myloadTexture("data\graphics\rainbow.jpg",1)
Global RainbowTexture2=myloadTexture("data\graphics\rainbow.jpg",1)

; Lurker
Global LurkerMesh=MyLoadMesh ("data\models\lurker\lurker.3ds",0)
Global LurkerTexture=MyLoadTexture ("data\models\lurker\lurker.jpg",1)
EntityTexture LurkerMesh,LurkerTexture
RotateMesh LurkerMesh,-90,0,0
HideEntity LurkerMesh

; Ghosts
Dim WraithTexture(3)
Global GhostMesh=MyLoadMesh ("data\models\ghost\ghost.3ds",0)
Global GhostTexture=MyLoadTexture ("data\models\ghost\ghost.jpg",1)
RotateMesh ghostmesh,-90,0,0
RotateMesh ghostmesh,0,180,0
EntityTexture GhostMesh,GhostTexture
HideEntity GhostMesh

Global WraithMesh=MyLoadMesh ("data\models\ghost\wraith.3ds",0)
RotateMesh wraithmesh,-90,0,0
RotateMesh wraithmesh,0,180,0

WraithTexture(0)=MyLoadTexture ("data\models\ghost\wraith0.jpg",1)
WraithTexture(1)=MyLoadTexture ("data\models\ghost\wraith1.jpg",1)
WraithTexture(2)=MyLoadTexture ("data\models\ghost\wraith2.jpg",1)
EntityTexture WraithMesh,WraithTexture(0)
HideEntity WraithMesh


; Obstacles
Dim ObstacleMesh(100),ObstacleTexture(100)
Dim MushroomTex(3)

ObstacleMesh(1)=myLoadMesh("data\models\Trees\rock1.3ds",0)
ObstacleTexture(1)=myLoadTexture("data\models\Trees\rocks.jpg",1)
EntityTexture ObstacleMesh(1),ObstacleTexture(1)
HideEntity ObstacleMesh(1)

ObstacleMesh(2)=myLoadMesh("data\models\Trees\rock2.3ds",0)
ObstacleTexture(2)=myLoadTexture("data\models\Trees\rocks2.jpg",1)
EntityTexture ObstacleMesh(2),ObstacleTexture(2)
HideEntity ObstacleMesh(2)

ObstacleMesh(3)=myLoadMesh("data\models\Other\volcano01.b3d",0)
ObstacleTexture(3)=myLoadTexture("data\models\Other\volcano01.bmp",1)
EntityTexture ObstacleMesh(3),ObstacleTexture(3)
HideEntity ObstacleMesh(3)

ObstacleMesh(4)=myLoadMesh("data\models\Other\volcano01.b3d",0)
ObstacleTexture(4)=myLoadTexture("data\models\other\volcano02.jpg",1)
EntityTexture ObstacleMesh(4),ObstacleTexture(4)
HideEntity ObstacleMesh(4)


ObstacleMesh(5)=myLoadMesh("data\models\Trees\flower.3ds",0)
ObstacleTexture(5)=myLoadTexture("data\models\Trees\flower1.jpg",1)
EntityTexture ObstacleMesh(5),ObstacleTexture(5)
HideEntity ObstacleMesh(5)

ObstacleMesh(6)=myLoadMesh("data\models\Trees\flower2.3ds",0)
ObstacleTexture(6)=myLoadTexture("data\models\Trees\flower2.bmp",1)
EntityTexture ObstacleMesh(6),ObstacleTexture(6)
UpdateNormals obstaclemesh(6)
HideEntity ObstacleMesh(6)

ObstacleMesh(7)=myLoadMesh("data\models\Trees\watervine.b3d",0)
ObstacleTexture(7)=myLoadTexture("data\models\Trees\watervine.jpg",1)
EntityTexture ObstacleMesh(7),ObstacleTexture(7)
UpdateNormals obstaclemesh(7)
HideEntity ObstacleMesh(7)

ObstacleMesh(8)=myLoadMesh("data\models\Trees\fern.b3d",0)
ObstacleTexture(8)=myLoadTexture("data\models\Trees\fern.bmp",4)
EntityTexture ObstacleMesh(8),ObstacleTexture(8)
HideEntity ObstacleMesh(8)

ObstacleMesh(9)=myLoadMesh("data\models\Trees\fern02.b3d",0)
ObstacleTexture(9)=myLoadTexture("data\models\Trees\fern.bmp",4)
EntityTexture ObstacleMesh(9),ObstacleTexture(9)
HideEntity ObstacleMesh(9)


ObstacleMesh(10)=myLoadMesh("data\models\Trees\mushroom.3ds",0)
MushroomTex(0)=myLoadTexture("data\models\Trees\mushroom.jpg",1)
MushroomTex(1)=myLoadTexture("data\models\Trees\mushroom2.jpg",1)
MushroomTex(2)=myLoadTexture("data\models\Trees\mushroom3.jpg",1)
HideEntity ObstacleMesh(10)

ObstacleMesh(11)=myLoadMesh("data\models\Trees\fern3.3ds",0)
ObstacleTexture(11)=myLoadTexture("data\models\Trees\fern3.png",4)
EntityTexture ObstacleMesh(11),ObstacleTexture(11)
HideEntity ObstacleMesh(11)


ObstacleMesh(12)=myLoadMesh("data\models\Trees\plant1.3ds",0)
ObstacleTexture(12)=myLoadTexture("data\models\Trees\plant1.png",4)
EntityTexture ObstacleMesh(12),ObstacleTexture(12)
HideEntity ObstacleMesh(12)


ObstacleMesh(13)=myLoadMesh("data\models\Trees\plant2.b3d",0)
ObstacleTexture(13)=myLoadTexture("data\models\Trees\plant2.png",4)
EntityTexture ObstacleMesh(13),ObstacleTexture(13)
HideEntity ObstacleMesh(13)


ObstacleMesh(15)=myLoadMesh("data\models\Trees\leaftree01.b3d",0)
ObstacleTexture(15)=myLoadTexture("data\models\Trees\leaftree01_03.png",4)
EntityTexture ObstacleMesh(15),ObstacleTexture(15)
HideEntity ObstacleMesh(15)

ObstacleMesh(16)=myLoadMesh("data\models\Trees\evergreentree01.b3d",0)
ObstacleTexture(16)=myLoadTexture("data\models\Trees\evergreen_01.png",4)
EntityTexture ObstacleMesh(16),ObstacleTexture(16)
HideEntity ObstacleMesh(16)

ObstacleMesh(17)=myLoadMesh("data\models\Trees\evergreentree01.b3d",0)
ObstacleTexture(17)=myLoadTexture("data\models\Trees\evergreen_02.png",4)
EntityTexture ObstacleMesh(17),ObstacleTexture(17)
HideEntity ObstacleMesh(17)

ObstacleMesh(18)=myLoadMesh("data\models\Trees\leaftree01.b3d",0)
ObstacleTexture(18)=myLoadTexture("data\models\Trees\leaftree01_02.png",4)
EntityTexture ObstacleMesh(18),ObstacleTexture(18)
HideEntity ObstacleMesh(18)

ObstacleMesh(19)=myLoadMesh("data\models\Trees\leaftree01.b3d",0)
ObstacleTexture(19)=myLoadTexture("data\models\Trees\leaftree01_01.png",4)
EntityTexture ObstacleMesh(19),ObstacleTexture(19)
HideEntity ObstacleMesh(19)

ObstacleMesh(20)=myLoadMesh("data\models\Trees\leaftree02.b3d",0)
ObstacleTexture(20)=myLoadTexture("data\models\Trees\leaftree02_01.png",4)
EntityTexture ObstacleMesh(20),ObstacleTexture(20)
HideEntity ObstacleMesh(20)

ObstacleMesh(21)=myLoadMesh("data\models\Trees\leaftree02.b3d",0)
ObstacleTexture(21)=myLoadTexture("data\models\Trees\leaftree02_02.png",4)
EntityTexture ObstacleMesh(21),ObstacleTexture(21)
HideEntity ObstacleMesh(21)

ObstacleMesh(22)=myLoadMesh("data\models\Trees\tree_jungle_typeA.b3d",0)
ObstacleTexture(22)=myLoadTexture("data\models\Trees\tree_jungle_typeA.bmp",4)
EntityTexture ObstacleMesh(22),ObstacleTexture(22)
HideEntity ObstacleMesh(22)

ObstacleMesh(23)=myLoadMesh("data\models\Trees\tree_jungle_typeB.b3d",0)
ObstacleTexture(23)=myLoadTexture("data\models\Trees\tree_jungle_typeB.bmp",4)
EntityTexture ObstacleMesh(23),ObstacleTexture(23)
HideEntity ObstacleMesh(23)

ObstacleMesh(24)=myLoadMesh("data\models\Trees\tree_palm.b3d",0)
ObstacleTexture(24)=myLoadTexture("data\models\Trees\palmtree01.bmp",4)
EntityTexture ObstacleMesh(24),ObstacleTexture(24)
HideEntity ObstacleMesh(24)



ObstacleMesh(25)=myLoadMesh("data\models\Bridges\bridgeend.3ds",0)
ObstacleTexture(25)=myLoadTexture("data\models\Bridges\bridgebrick.png",1)
EntityTexture ObstacleMesh(25),ObstacleTexture(25)
HideEntity ObstacleMesh(25)


ObstacleMesh(26)=myLoadMesh("data\models\houses\canopy.3ds",0)
ObstacleTexture(26)=myLoadTexture("data\models\houses\canopy.jpg",1)
EntityTexture ObstacleMesh(26),ObstacleTexture(26)
HideEntity ObstacleMesh(26)

ObstacleMesh(27)=myLoadMesh("data\models\houses\streetlight01.b3d",0)
ObstacleTexture(27)=myLoadTexture("data\models\houses\streetlight02.png",4)
EntityTexture ObstacleMesh(27),ObstacleTexture(27)
HideEntity ObstacleMesh(27)

ObstacleMesh(28)=myLoadMesh("data\models\houses\pillar.3ds",0)
ObstacleTexture(28)=myLoadTexture("data\models\houses\pillar1.jpg",1)
EntityTexture ObstacleMesh(28),ObstacleTexture(28)
HideEntity ObstacleMesh(28)

ObstacleMesh(29)=myLoadMesh("data\models\ladder\ladder.3ds",0)
ObstacleTexture(29)=myLoadTexture("data\models\houses\pillar1.jpg",1)
EntityTexture ObstacleMesh(29),ObstacleTexture(29)
HideEntity ObstacleMesh(29)

ObstacleMesh(30)=myLoadMesh("data\models\furniture\table.3ds",0)
ObstacleTexture(30)=myLoadTexture("data\models\furniture\table.jpg",1)
EntityTexture ObstacleMesh(30),ObstacleTexture(30)
HideEntity ObstacleMesh(30)

ObstacleMesh(31)=myLoadMesh("data\models\furniture\chair.3ds",0)
ObstacleTexture(31)=myLoadTexture("data\models\furniture\chair.jpg",1)
EntityTexture ObstacleMesh(31),ObstacleTexture(31)
HideEntity ObstacleMesh(31)

ObstacleMesh(32)=myLoadMesh("data\models\furniture\bed.3ds",0)
ObstacleTexture(32)=myLoadTexture("data\models\furniture\bed.jpg",1)
EntityTexture ObstacleMesh(32),ObstacleTexture(32)
HideEntity ObstacleMesh(32)

ObstacleMesh(33)=myLoadMesh("data\models\furniture\bookshelf01.b3d",0)
ObstacleTexture(33)=myLoadTexture("data\models\furniture\bookshelf01.png",1)
EntityTexture ObstacleMesh(33),ObstacleTexture(33)
HideEntity ObstacleMesh(33)

ObstacleMesh(34)=myLoadMesh("data\models\furniture\arcade.3ds",0)
ObstacleTexture(34)=myLoadTexture("data\models\furniture\arcade.jpg",1)
EntityTexture ObstacleMesh(34),ObstacleTexture(34)
HideEntity ObstacleMesh(34)

ObstacleMesh(35)=myLoadMesh("data\models\houses\pyramid.3ds",0)
ObstacleTexture(35)=myLoadTexture("data\models\houses\pyramid.jpg",1)
EntityTexture ObstacleMesh(35),ObstacleTexture(35)
HideEntity ObstacleMesh(35)

ObstacleMesh(36)=myLoadMesh("data\models\houses\cottage.b3d",0)
HideEntity ObstacleMesh(36)

ObstacleMesh(37)=myLoadMesh("data\models\houses\townhouse_01a.b3d",0)
HideEntity ObstacleMesh(37)

ObstacleMesh(38)=myLoadMesh("data\models\houses\townhouse_01b.b3d",0)
HideEntity ObstacleMesh(38)

ObstacleMesh(39)=myLoadMesh("data\models\houses\townhouse_02a.b3d",0)
HideEntity ObstacleMesh(39)

ObstacleMesh(40)=myLoadMesh("data\models\houses\townhouse_02b.b3d",0)
HideEntity ObstacleMesh(40)

ObstacleMesh(41)=myLoadMesh("data\models\houses\windmill_main.b3d",0)
HideEntity ObstacleMesh(41)

ObstacleMesh(42)=myLoadMesh("data\models\houses\windmill_rotor.b3d",0)
	PositionMesh ObstacleMesh(42),0,-5.65/.037,1.25/.037
	HideEntity ObstacleMesh(42)
	
ObstacleMesh(43)=myLoadMesh("data\models\houses\hut01.b3d",0)
ObstacleTexture(43)=myLoadTexture("data\models\houses\hut01.jpg",1)
EntityTexture ObstacleMesh(43),ObstacleTexture(43)
HideEntity ObstacleMesh(43)

ObstacleMesh(44)=myLoadMesh("data\models\other\ship01.b3d",0)
ObstacleTexture(44)=myLoadTexture("data\models\other\ship01.bmp",1)
EntityTexture ObstacleMesh(44),ObstacleTexture(44)
HideEntity ObstacleMesh(44)

ObstacleMesh(45)=myLoadMesh("data\models\houses\waterwheel.3ds",0)
ObstacleTexture(45)=myLoadTexture("data\models\cage\cage.jpg",1)
EntityTexture ObstacleMesh(45),ObstacleTexture(45)
HideEntity ObstacleMesh(45)

ObstacleMesh(46)=myLoadMesh("data\models\houses\bridge.3ds",0)
ObstacleTexture(46)=myLoadTexture("data\models\cage\cage.jpg",1)
EntityTexture ObstacleMesh(46),ObstacleTexture(46)
HideEntity ObstacleMesh(46)

ObstacleMesh(47)=myLoadMesh("data\models\houses\machine.3ds",0)
ObstacleTexture(47)=myLoadTexture("data\models\houses\machine.jpg",1)
EntityTexture ObstacleMesh(47),ObstacleTexture(47)
HideEntity ObstacleMesh(47)

ObstacleMesh(48)=myLoadMesh("data\models\other\starship.3ds",0)
ObstacleTexture(48)=myLoadTexture("data\models\other\starship.jpg",1)
EntityTexture ObstacleMesh(48),ObstacleTexture(48)
HideEntity ObstacleMesh(48)

ObstacleMesh(50)=myLoadMesh("data\models\portal\portal.3ds",0)
RotateMesh ObstacleMesh(50),-90,0,0
ObstacleTexture(50)=myLoadTexture("data\models\portal\portal2.jpg",1)
EntityTexture ObstacleMesh(50),ObstacleTexture(50)
HideEntity ObstacleMesh(50)


ObstacleMesh(51)=myLoadMesh("data\models\newobstacles\cactus1.b3d",0)
ObstacleTexture(51)=myLoadTexture("data\models\newobstacles\cactus1.png",4)
EntityTexture ObstacleMesh(51),ObstacleTexture(51)
FlipMesh ObstacleMesh(51)
HideEntity ObstacleMesh(51)

ObstacleMesh(52)=myLoadMesh("data\models\newobstacles\cactus2.b3d",0)
ObstacleTexture(52)=myLoadTexture("data\models\newobstacles\cactus2.png",4)
EntityTexture ObstacleMesh(52),ObstacleTexture(51)
HideEntity ObstacleMesh(52)

ObstacleMesh(53)=myLoadMesh("data\models\newobstacles\cactus3.b3d",0)
ObstacleTexture(53)=myLoadTexture("data\models\newobstacles\cactus3.png",4)
EntityTexture ObstacleMesh(53),ObstacleTexture(51)
HideEntity ObstacleMesh(53)

ObstacleMesh(54)=myLoadMesh("data\models\newobstacles\cactus4.b3d",0)
ObstacleTexture(54)=myLoadTexture("data\models\newobstacles\cactus4.png",4)
EntityTexture ObstacleMesh(54),ObstacleTexture(51)
HideEntity ObstacleMesh(54)

ObstacleMesh(55)=myLoadMesh("data\models\newobstacles\leafy1.b3d",0)
ObstacleTexture(55)=myLoadTexture("data\models\newobstacles\leafy1.png",4)
EntityTexture ObstacleMesh(55),ObstacleTexture(55)
HideEntity ObstacleMesh(55)

ObstacleMesh(56)=myLoadMesh("data\models\newobstacles\leafy2.b3d",0)
ObstacleTexture(56)=myLoadTexture("data\models\newobstacles\leafy2.png",4)
EntityTexture ObstacleMesh(56),ObstacleTexture(55)
HideEntity ObstacleMesh(56)

ObstacleMesh(57)=myLoadMesh("data\models\newobstacles\leafy3.b3d",0)
ObstacleTexture(57)=myLoadTexture("data\models\newobstacles\leafy3.png",4)
EntityTexture ObstacleMesh(57),ObstacleTexture(55)
HideEntity ObstacleMesh(57)

ObstacleMesh(58)=myLoadMesh("data\models\newobstacles\leafy4.b3d",0)
ObstacleTexture(58)=myLoadTexture("data\models\newobstacles\leafy4.png",4)
EntityTexture ObstacleMesh(58),ObstacleTexture(55)
HideEntity ObstacleMesh(58)

ObstacleMesh(59)=myLoadMesh("data\models\newobstacles\rock1.b3d",0)
ObstacleTexture(59)=myLoadTexture("data\models\newobstacles\rock1.png",1)
EntityTexture ObstacleMesh(59),ObstacleTexture(59)
HideEntity ObstacleMesh(59)

ObstacleMesh(60)=myLoadMesh("data\models\newobstacles\rock2.b3d",0)
ObstacleTexture(60)=myLoadTexture("data\models\newobstacles\rock2.png",1)
EntityTexture ObstacleMesh(60),ObstacleTexture(59)
HideEntity ObstacleMesh(60)


ObstacleMesh(61)=myLoadMesh("data\models\newobstacles\rock3.b3d",0)
ObstacleTexture(61)=myLoadTexture("data\models\newobstacles\rock3.png",1)
EntityTexture ObstacleMesh(61),ObstacleTexture(59)
HideEntity ObstacleMesh(61)

ObstacleMesh(62)=myLoadMesh("data\models\newobstacles\rock4.b3d",0)
ObstacleTexture(62)=myLoadTexture("data\models\newobstacles\rock4.png",1)
EntityTexture ObstacleMesh(62),ObstacleTexture(59)
HideEntity ObstacleMesh(62)




Global Fence1=MyLoadmesh("data\models\houses\fence.3ds",0)
HideEntity Fence1
Global Fence2=MyLoadmesh("data\models\houses\fenceb.b3d",0)
HideEntity Fence2
Global Fencepost=MyLoadmesh("data\models\houses\fence_post.3ds",0)
HideEntity Fencepost

Global GloveTex=MyLoadTexture("data\models\squares\glove.bmp",4)

Global FireTrapTexture=MyLoadTexture("data\models\squares\firetrap.bmp",4)

Global VoidTexture=myLoadTexture("Data\models\void\void.jpg",1)

Global hubmode
Global HubFileName$, HubTitle$, HubDescription$, HubTotalAdventures, HubAdvStart, HubSelectedAdventure
Dim HubAdventuresFilenames$(500)

Global NoOfShards=7
Global CustomShardEnabled
Dim CustomShardCMD(NoOfShards,5)
;Dim CustomShardData1(NoOfShards)
;Dim CustomShardData2(NoOfShards)
;Dim CustomShardData3(NoOfShards)
;Dim CustomShardData4(NoOfShards)

Global NoOfGlyphs=5
Global CustomGlyphEnabled
Dim CustomGlyphCMD(NoOfGlyphs,5)
;Dim CustomGlyphData1(NoOfGlyphs)
;Dim CustomGlyphData2(NoOfGlyphs)
;Dim CustomGlyphData3(NoOfGlyphs)
;Dim CustomGlyphData4(NoOfGlyphs)

Global SelectedShard
Global SelectedGlyph

ParticleTexture=myLoadTexture("data\graphics\particles.bmp",1)
ResetParticles("data/graphics/particles.bmp")
ResetText("data/graphics/font.bmp")



; check if valid username is already selected
flag=True
file=ReadFile(GlobalDirName$+"\custom\Editing\Profiles\currentuser.dat")
If file=0
	flag=False
Else
	EditorUserName$=ReadString$(file)
	If FileType(GlobalDirName$+"\custom\Editing\Profiles\"+editorusername$)<>2
		flag=False
		CloseFile(file)
		DeleteFile GlobalDirName$+"\custom\Editing\Profiles\currentuser.dat"
	EndIf
EndIf



If flag=False
	StartUserSelectScreen()
Else
	StartAdventureSelectScreen()
EndIf
	


ResetLevel()
BuildLevelModel()



LoadTilePreset()
LoadObjectPreset()

Global Mouseimg=LoadImage ("data\Mouseimg.bmp")

testfile=ReadFile(globaldirname$+"\temp\test.dat")
If testfile<>0 ;FileType(globaldirname$+"\custom\editing\")
	AdventureFileName$=ReadString$(testfile)
	HubFileName$=ReadString$(testfile)
	If HubFileName$<>""
		hubmode=true
		StartHub()
	Endif
	StartMaster()
	SetEditorMode(ReadInt(testfile))
	CloseFile testfile
	DeleteFile globaldirname$+"\temp\test.dat"
	
	
EndIf


StartupColors()


TweenPeriod=1000/60;85
TweenTime=MilliSecs()-TweenPeriod

Repeat
		
		If HasFocus()
			
			Repeat
				TweenElapsed=MilliSecs()-TweenTime
			Until TweenElapsed>TweenPeriod
			
			If TweenElapsed>20*TweenPeriod 
				TweenElapsed=20*TweenPeriod
				TweenTime=MilliSecs()-TweenElapsed
			EndIf
		
			;how many 'frames' have elapsed	
			TweenTicks=TweenElapsed/TweenPeriod
			;fractional remainder
			Tween#=Float(TweenElapsed Mod TweenPeriod)/Float(TweenPeriod)
		
			For k=1 To TweenTicks
			
				Tweentime=Tweentime+Tweenperiod
				If k=Tweenticks 
					CaptureWorld
				EndIf
			
				UpdateEditor()
				
			Next
		

		
		Else
		
			Repeat
				Delay 200
			Until HasFocus()
			
			;FlushKeys ; WHY DOES THIS NOT WORK??? Apparently it doesn't get rid of currently-pressed keys.
			
			ForceKeyRelease(56,"left alt")
			ForceKeyRelease(184,"right alt")
			
			ReadColors()
			
		EndIf
	
Until False ;KeyDown(1) ; escape



End


Function UpdateEditor()

	EditorGlobalControls()

	Select EditorMode
	
	Case 0,1,2,3
		EditorMainLoop()
	Case 4
		UserSelectScreen()
	Case 5
		AdventureSelectScreen()
	Case 6
		AdventureSelectScreen2()
	Case 7
		AdventureSelectScreen3()
	Case 8
		MasterMainLoop()
	Case 9
		DialogMainLoop()
	Case 10
		MasterAdvancedLoop()
	Case 11
		HubMainLoop()
	Case 12
		HubAdventureSelectScreen()
	End Select

End Function


Function StartEditorMainLoop()
	Cls
	SetEditorMode(EditorModeBeforeMasterEdit)
	
	CameraProjMode Camera1,1
	CameraProjMode Camera2,1
	CameraProjMode Camera3,0
	CameraProjMode Camera4,1
	CameraProjMode Camera,0
	
	ClearSurface Textsurface
	For p.letter = Each letter
		Delete p
	Next
	


End Function
	



Function EditorMainLoop()

	If displayfullscreen=True Cls
	
	; full window size is 800x600, whereas the level camera viewport is 500x500
	; draw black regions so stray text doesn't linger there
	Color RectMarginR,RectMarginG,RectMarginB
	Rect 500,0,10,500,True ; between level camera and object/tile editors
	Rect 510,0,290,20,True ; backdrop for the labels of TILES and GLOBALS
	Rect 710,20,5,220,True ; between TILES and GLOBALS
	Rect 715,85,80,15 ; between level dimensions and the rest of GLOBALS
	Rect 795,20,5,500,True ; to the right of GLOBALS
	Rect 510,240,205,5,True ; beneath TILES and to the left of GLOBALS
	Rect 510,285,285,20,True ; backdrop for the label of OBJECTS
	Rect 510,455,285,5,True ; between the object adjusters and object categories
	Rect 695,430,100,5,True ; between object camera and More button


	CameraControls()
	
	If CameraPanning=False
		EditorLocalControls()
	EndIf
	
	leveltimer=leveltimer+1
	
	If KeyPressed(35) ; h
		HighlightWopAdjusters=Not HighlightWopAdjusters
	EndIf
	
	EntityAlpha CurrentGrabbedObjectMarker,0.3+0.03*Sin((Float(LevelTimer)*6.0) Mod 360)
	
	ControlLight()
	If SimulationLevel>=1
		ControlObjects()
	EndIf
	If SimulationLevel>=3
		ControlWeather()
	EndIf
	ControlParticles()
	RenderParticles()
	
	UpdateWorld
	RenderWorld
	
	
	
	
	Text 0,5,"ADVENTURE: "+AdventureFileName$
	If CurrentLevelNumber<10
		Text 500-9*8,5,"LEVEL: 0"+CurrentLevelNumber
	Else
		Text 500-9*8,5,"LEVEL: "+CurrentLevelNumber
	EndIf
	
	
		
	StartX=510
	StartY=20
	If CurrentTileTextureUse=False Text StartX+10,StartY+70,"Not Used"
	If CurrentTileSideTextureUse=False Text StartX+10,StartY+130,"Not Used"
	If CurrentWaterTileUse=False Text StartX+120,StartY+100,"Not Used"
	
	Text StartX,StartY,"Xtrude: "+CurrentTileExtrusion
	If CurrentTileExtrusionUse=False Text StartX,StartY,"------------"
	Text StartX+48,StartY,"       Height: "+CurrentTileHeight
	If CurrentTileHeightUse=False Text StartX+48,StartY,"       ------------"
	
	CurrentLogicName$=LogicIdToLogicName$(CurrentTileLogic)
	Text StartX+50,StartY+15,"Logic: "+CurrentLogicName$


	If CurrentTileLogicUse=False Text StartX+50,StartY+15,"  ---------"
	Text StartX+50,StartY+170," Random: "+CurrentTileRandom
	If CurrentTileRandomUse=False Text StartX+50,StartY+170,"--------------"
	
	If CurrentTileRounding=0
		Text StartX,StartY+185,"Corner:Squar"
	Else
		Text StartX,StartY+185,"Corner:Round"
	EndIf
	If CurrentTileRoundingUse=False Text StartX,StartY+185,"------------"
	
	If CurrentTileEdgeRandom=0
		Text StartX+100,StartY+185," Edge:Smooth"
	Else
		Text StartX+100,StartY+185," Edge:Jagged"
	EndIf
	If CurrentTileEdgeRandomUse=False Text StartX+100,StartY+185,"------------"

	Text StartX,StartY+200,"WHeight:"+CurrentWaterTileHeight
	If CurrentWaterTileHeightUse=False Text StartX,StartY+200,"------------"
	Text StartX+100,StartY+200," WTurb:"+CurrentWaterTileTurbulence
	If CurrentWaterTileTurbulenceUse=False Text StartX+100,StartY+200,"------------"

	;Color RectToolbarR,RectToolbarG,RectToolbarB
	;Rect 0,520,800,80,True
	;Rect 0,500,800,100,True
	
	Color TextLevelR,TextLevelG,TextLevelB

	If BlockMode=0
		Text 0+4,520,"   BLOCK"
	Else If blockmode=1
		Text 0+4,520,"  >BLOCK<"
	Else If blockmode=2
		Text 0+4,520," >>BLOCK<<"

	EndIf
	
	
	Text 0,550,"    WIPE"
	
	
	If FillMode=0
		Text 0,580,"    FILL"
	Else If FillMode=1
		If FillDensity#<1.0
			Color 255,155,0
			Text 0,580,">FILL "+FillDensity#+"<"
			Color TextLevelR,TextLevelG,TextLevelB
		Else
			Text 0,580,"   >FILL<"
		EndIf
	EndIf

	
	Text 100,520,"   FLIP"
	;Text 100,520,"   FLIP X"
	;Text 100,550,"   FLIP Y"
	;Text 100+4,580,"  FLIP XY"
	
	If TexturePrefix<>""
		Color 255,155,0
	EndIf
	Text 90,565,"  TEX PREFIX"
	Text 100,580,TexturePrefix$
	Color TextLevelR,TextLevelG,TextLevelB
	
	If ShowObjectPositions=True
		Text 200,520,"    SHOW"
	Else
		Text 200,520,"    HIDE"
	EndIf
	Text 200+4,535,"  MARKERS"
	
	If ShowObjectMesh=True
		Text 200,565,"    SHOW"
	Else
		Text 200,565,"    HIDE"
	EndIf
	Text 200+4,580,"  OBJECTS"

	If ShowLogicMesh=True
		Text 300,520,"    SHOW"
	Else
		Text 300,520,"    HIDE"
	EndIf
	Text 300+4,535,"   LOGIC"
	
	If ShowLevelMesh=True
		Text 300,565,"    SHOW"
	Else
		Text 300,565,"    HIDE"
	EndIf
	Text 300+4,580,"   LEVEL"

	
	Text 400,520,"   BORDER"
	If BorderExpandOption=0
		Text 400+4,535,"  CURRENT"
	Else BorderExpandOption=1
		Text 400+4,535," DUPLICATE"
	EndIf
	
	;Text 400,565," LEVEL BORDER"
	;If LevelEdgeStyle=1
	;	Text 400+4,580,"   DEFAULT"
	;ElseIf LevelEdgeStyle=2
	;	Text 400+4,580,"   BORDER"
	;ElseIf LevelEdgeStyle=3
	;	Text 400+4,580,"   BORDER X"
	;ElseIf LevelEdgeStyle=4
	;	Text 400+4,580,"   NONE"
	;Else
	;	Text 400+4,580,LevelEdgeStyle
	;EndIf
	
	Text 400,565," SIMULATION"
	Text 400+4,580,"  LEVEL "+SimulationLevel
	
	;Text 500,520,"   WIDESCREEN"
	;If WidescreenRangeLevel=0
	;	Text 500,535,"   RANGE: OFF"
	;ElseIf WidescreenRangeLevel=1
	;	Text 500,535,"   RANGE: ON"
	;Else
	;	Text 500,535,"   RANGE: MASTER"
	;EndIf
	
	Text 500,520,"   BRUSH"
	Text 500,535,"   SIZE "+BrushSize
	
	Text 500,565,"   ELEVATE"
	
	If IDFilterEnabled
		Color 255,155,0
	EndIf
	Text 600,520," ID FILTER"
	If IDFilterEnabled
		Text 600,535," "+IDFilterAllow
	Else
		Text 600,535," OFF"
	EndIf
	Color TextLevelR,TextLevelG,TextLevelB
	
	Text 600,565,"  XTRUDE"
	Text 600,580,"  LOGICS"
	
	Color 255,255,0
	
	If MouseX()>700 And MouseY()>515 And MouseY()<555
		Color 255,255,0
		Text 704,520," > CANCEL <"
		Text 704,535," >AND EXIT<"
	Else
		Color TextLevelR,TextLevelG,TextLevelB
		Text 720,520," CANCEL"
		Text 720,535,"AND EXIT"
	EndIf
	
	If MouseX()>700 And MouseY()>560 And MouseY()<600
		Color 255,255,0
		Text 696,565," >SAVE LEVEL<"
		Text 696,580," > AND EXIT <"
	Else
		Color TextLevelR,TextLevelG,TextLevelB
		Text 712,565,"SAVE LEVEL"
		Text 712,580," AND EXIT"
	EndIf

	Color TextLevelR,TextLevelG,TextLevelB
	
	If WaterFlow>=0
		PositionTexture WaterTexture,0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
	EndIf
	If waterflow<0
		; rock
		PositionTexture WaterTexture,0,0.5+0.125*WaterFlow/4*Sin(-(4*LevelTimer*WaterFlow)/10.0)
	EndIf
	
	; Animate Rainbow Magic
	If (CurrentObjectType=200 And CurrentObjectData(0)=8) Then
		For i=0 To 3
		    red=128+120*Sin(Leveltimer Mod 360)
		    green=128+120*Cos(Leveltimer Mod 360)
		    blue=128-120*Sin(Leveltimer Mod 360)
			
		    VertexColor GetSurface(CurrentObjectmodel,1),i,red,green,blue
		Next
	EndIf
	For i = 0 To 1000
		For j=0 To 3
			If ObjectType(i)=200 And ObjectData(i, 0)=8 Then
				red=128+120*Sin(Leveltimer Mod 360)
			    green=128+120*Cos(Leveltimer Mod 360)
			    blue=128-120*Sin(Leveltimer Mod 360)
				
				If ObjectEntity(i)>0 Then
			    	VertexColor GetSurface(ObjectEntity(i),1),j,red,green,blue
				EndIf
			EndIf
		Next
	Next
		
	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf
	
	Flip
	

End Function


Function SetEditorMode(NewMode)

	If NewMode=8
		; prevent garbage input from level editor movement from appearing in adventure description text
		FlushKeys
		
		If EditorMode=0 Or EditorMode=3
			EditorModeBeforeMasterEdit=EditorMode
		EndIf
	EndIf
		
	If (NewMode=0 Or NewMode=3) And NewMode<>CustomBrushEditorMode
		CustomBrush=False
	EndIf
	
	EditorMode=NewMode

End Function


Function SetBrushSize(NewBrushSize)

	BrushSize=NewBrushSize
	
	If BrushSize<1
		BrushSize=1
	EndIf
	
	CustomBrush=False

End Function


Function ReadColors()

	If FileType(ColorsConfig$)=1
		colorsfile=ReadFile(ColorsConfig$)
		ReadColorsWithHandle(colorsfile)
		CloseFile(colorsfile)
	EndIf

End Function


Function ReadColorsWithHandle(colorsfile)

	If ReadLine(colorsfile)="" Then Return
	RectOnR=ReadLine(colorsfile)
	RectOnG=ReadLine(colorsfile)
	RectOnB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	RectOffR=ReadLine(colorsfile)
	RectOffG=ReadLine(colorsfile)
	RectOffB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	RectGlobalsR=ReadLine(colorsfile)
	RectGlobalsG=ReadLine(colorsfile)
	RectGlobalsB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	RectMarginR=ReadLine(colorsfile)
	RectMarginG=ReadLine(colorsfile)
	RectMarginB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	RectToolbarR=ReadLine(colorsfile)
	RectToolbarG=ReadLine(colorsfile)
	RectToolbarB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextLevelR=ReadLine(colorsfile)
	TextLevelG=ReadLine(colorsfile)
	TextLevelB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextAdjusterR=ReadLine(colorsfile)
	TextAdjusterG=ReadLine(colorsfile)
	TextAdjusterB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextAdjusterHighlightedR=ReadLine(colorsfile)
	TextAdjusterHighlightedG=ReadLine(colorsfile)
	TextAdjusterHighlightedB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextMenusR=ReadLine(colorsfile)
	TextMenusG=ReadLine(colorsfile)
	TextMenusB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextMenuButtonR=ReadLine(colorsfile)
	TextMenuButtonG=ReadLine(colorsfile)
	TextMenuButtonB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextMenuXR=ReadLine(colorsfile)
	TextMenuXG=ReadLine(colorsfile)
	TextMenuXB=ReadLine(colorsfile)

End Function


Function WriteColors()
	
	colorsfile=WriteFile(ColorsConfig$)
	
	WriteLine(colorsfile,"// RGB for currently-selected mode")
	WriteLine(colorsfile,RectOnR)
	WriteLine(colorsfile,RectOnG)
	WriteLine(colorsfile,RectOnB)
	WriteLine(colorsfile,"// RGB for currently-deselected mode")
	WriteLine(colorsfile,RectOffR)
	WriteLine(colorsfile,RectOffG)
	WriteLine(colorsfile,RectOffB)
	WriteLine(colorsfile,"// RGB for the GLOBALS section")
	WriteLine(colorsfile,RectGlobalsR)
	WriteLine(colorsfile,RectGlobalsG)
	WriteLine(colorsfile,RectGlobalsB)
	WriteLine(colorsfile,"// RGB for the margins around the TILES and OBJECTS sections")
	WriteLine(colorsfile,RectMarginR)
	WriteLine(colorsfile,RectMarginG)
	WriteLine(colorsfile,RectMarginB)
	WriteLine(colorsfile,"// RGB for the bottom toolbar")
	WriteLine(colorsfile,RectToolbarR)
	WriteLine(colorsfile,RectToolbarG)
	WriteLine(colorsfile,RectToolbarB)
	WriteLine(colorsfile,"// RGB for regular level editor text")
	WriteLine(colorsfile,TextLevelR)
	WriteLine(colorsfile,TextLevelG)
	WriteLine(colorsfile,TextLevelB)
	WriteLine(colorsfile,"// RGB for regular object adjusters")
	WriteLine(colorsfile,TextAdjusterR)
	WriteLine(colorsfile,TextAdjusterG)
	WriteLine(colorsfile,TextAdjusterB)
	WriteLine(colorsfile,"// RGB for highlighted object adjusters")
	WriteLine(colorsfile,TextAdjusterHighlightedR)
	WriteLine(colorsfile,TextAdjusterHighlightedG)
	WriteLine(colorsfile,TextAdjusterHighlightedB)
	WriteLine(colorsfile,"// RGB for menu layout text")
	WriteLine(colorsfile,TextMenusR)
	WriteLine(colorsfile,TextMenusG)
	WriteLine(colorsfile,TextMenusB)
	WriteLine(colorsfile,"// RGB for menu buttons")
	WriteLine(colorsfile,TextMenuButtonR)
	WriteLine(colorsfile,TextMenuButtonG)
	WriteLine(colorsfile,TextMenuButtonB)
	WriteLine(colorsfile,"// RGB for dim menu elements such as the X's")
	WriteLine(colorsfile,TextMenuXR)
	WriteLine(colorsfile,TextMenuXG)
	WriteLine(colorsfile,TextMenuXB)
	
	CloseFile(colorsfile)

End Function


Function StartupColors()

	ReadColors()
	WriteColors()

End Function


Function KeyPressed(i)

	If KeyDown(i) And KeyReleased(i)
		KeyReleased(i)=False
		Return True
	Else
		Return False
	EndIf

End Function


Function ReturnPressed()

	If ReturnKey=True And ReturnKeyReleased=True
		ReturnKeyReleased=False
		Return True
	EndIf
	Return False

End Function


Function EditorGlobalControls()

	ShowingError=False

	If MouseDown(1)=True
		LeftMouse=True
	Else
		LeftMouse=False
		LeftMouseReleased=True
	EndIf

	If MouseDown(2)=True
		RightMouse=True
	Else
		RightMouse=False
		RightMouseReleased=True
	EndIf
	
	MouseScroll=MouseZSpeed()
	
	If KeyDown(28) Or KeyDown(156)
		ReturnKey=True
	Else
		ReturnKey=False
		ReturnKeyReleased=True
	EndIf
	
	If KeyDown(211)
		DeleteKey=True
	Else
		DeleteKey=False
		DeleteKeyReleased=True
	EndIf
	
	For i=1 To KeyCount
		If Not KeyDown(i)
			KeyReleased(i)=True
		EndIf
	Next
	
	;If KeyPressed(25) ; P
	;	ReadColors()
	;EndIf

End Function


Function EditorLocalControls()
	
	MX=MouseX()
	MY=MouseY()
	
	If EditorMode=0
		CameraClsColor camera2,RectOnR,RectOnG,RectOnB
		CameraClsColor camera4,RectOffR,RectOffG,RectOffB
		TileColorR=RectOnR
		TileColorG=RectOnG
		TileColorB=RectOnB
		ObjectColorR=RectOffR
		ObjectColorG=RectOffG
		ObjectColorB=RectOffB
	Else If EditorMode=3
		CameraClsColor camera2,RectOffR,RectOffG,RectOffB
		CameraClsColor camera4,RectOnR,RectOnG,RectOnB
		TileColorR=RectOffR
		TileColorG=RectOffG
		TileColorB=RectOffB
		ObjectColorR=RectOnR
		ObjectColorG=RectOnG
		ObjectColorB=RectOnB
	EndIf
	
	Fast=False
	If ShiftDown() Then Fast=True
	
	
	Color RectToolbarR,RectToolbarG,RectToolbarB
	;Rect 0,500,500,12,True
	Rect 0,500,800,100,True
	
	; *************************************
	; Placing Tiles and Objects on the Editor Field
	; *************************************
	
	If EditorMode=0 Or EditorMode=3
		If mx>=0 And mx<500 And my>=0 And my<500
			Entity=CameraPick(camera1,MouseX(),MouseY())
			If Entity>0
				
				x=Floor(PickedX())
				y=Floor(-PickedZ())
				ShowEntity CursorMesh
				ShowEntity CursorMesh2
				PositionEntity CursorMesh,x+.5,LevelTileExtrusion(x,y)+LevelTileHeight(x,y),-y-.5
				ScaleEntity CursorMesh,BrushSize,1,BrushSize
				PositionEntity CursorMesh2,x+.5,0,-y-.5

				Color TextLevelR,TextLevelG,TextLevelB
				Text 250-4.5*8,500,"X:"+Str$(Abs(x))+", Y:"+Str$(Abs(y))
				
				If FillMode>0
					r=255
					g=255
					b=0
				Else If BlockMode>0
					r=0
					g=255
					b=255
				Else
					If CustomBrush
						r=255
						g=0
						b=255
					Else
						r=255
						g=255
						b=255
					EndIf
				EndIf
				EntityColor CursorMesh,r,g,b
				EntityColor CursorMesh2,r,g,b
				
				
				HideEntity BlockModeMesh
				If BlockMode=2
					; show the block
					ShowEntity BlockModeMesh
					If x>BlockCornerx
						cornleft=Blockcornerx
						cornright=x
					Else
						cornleft=x
						cornright=Blockcornerx
					EndIf
					If y>BlockCornery
						cornup=BlockCornery
						corndown=y
					Else
						cornup=y
						corndown=blockcornery
					EndIf
					VertexCoords BlockModeSurface,0,cornleft-0,0.1,-(cornup)
					VertexCoords BlockModeSurface,1,cornright+1,0.1,-(cornup)
					VertexCoords BlockModeSurface,2,cornleft-0,0.1,-(corndown+1)
					VertexCoords BlockModeSurface,3,cornright+1,0.1,-(corndown+1)
				EndIf
					
				
				If LeftMouse=True And LeftMouseReleased=True
					If BlockMode=1
						; place one corner of block
						BlockCornerX=x
						BlockCornerY=y
						blockmode=2
						Delay 100
					Else If blockmode=2
						; fill block
						If EditorMode=0
							For i=cornleft To cornright
								For j=cornup To corndown
									ChangeLevelTile(i,j,True)
								Next
							Next
						ElseIf EditorMode=3
							For i=cornleft To cornright
								For j=cornup To corndown
									PlaceObject(i,j)
								Next
							Next
						EndIf
						blockmode=1
						Delay 100
					Else If fillmode=1
						; flood fill
						
						; initialize state
						For i=0 To LevelWidth-1
							For j=0 To LevelHeight-1
								LevelTileVisited(i,j)=False
							Next
						Next
						
						SetLevelTileAsTarget(x,y)
						;If EditorMode=0
						;	ChangeLevelTile(x,y,True)
						;ElseIf EditorMode=3
						;	PlaceObject(x,y)
						;EndIf
						FloodStackX(0)=x
						FloodStackY(0)=y
						ElementCount=1
						While ElementCount<>0
							ElementCount=ElementCount-1
							thisx=FloodStackX(ElementCount)
							thisy=FloodStackY(ElementCount)
														
							If thisx>0
								nextx=thisx-1
								nexty=thisy
								If LevelTileVisited(nextx,nexty)=False And LevelTileMatchesTarget(nextx,nexty)
									If Rnd(0.0,1.0)<=FillDensity#
										If EditorMode=0
											ChangeLevelTile(nextx,nexty,True)
										ElseIf EditorMode=3
											PlaceObject(nextx,nexty)
										EndIf
									EndIf
									LevelTileVisited(nextx,nexty)=True
									FloodStackX(ElementCount)=nextx
									FloodStackY(ElementCount)=nexty
									ElementCount=ElementCount+1
								EndIf
							EndIf
							If thisx<LevelWidth-1
								nextx=thisx+1
								nexty=thisy
								If LevelTileVisited(nextx,nexty)=False And LevelTileMatchesTarget(nextx,nexty)
									If Rnd(0.0,1.0)<=FillDensity#
										If EditorMode=0
											ChangeLevelTile(nextx,nexty,True)
										ElseIf EditorMode=3
											PlaceObject(nextx,nexty)
										EndIf
									EndIf
									LevelTileVisited(nextx,nexty)=True
									FloodStackX(ElementCount)=nextx
									FloodStackY(ElementCount)=nexty
									ElementCount=ElementCount+1
								EndIf
							EndIf
							If thisy>0
								nextx=thisx
								nexty=thisy-1
								If LevelTileVisited(nextx,nexty)=False And LevelTileMatchesTarget(nextx,nexty)
									If Rnd(0.0,1.0)<=FillDensity#
										If EditorMode=0
											ChangeLevelTile(nextx,nexty,True)
										ElseIf EditorMode=3
											PlaceObject(nextx,nexty)
										EndIf
									EndIf
									LevelTileVisited(nextx,nexty)=True
									FloodStackX(ElementCount)=nextx
									FloodStackY(ElementCount)=nexty
									ElementCount=ElementCount+1
								EndIf
							EndIf
							If thisy<LevelHeight-1
								nextx=thisx
								nexty=thisy+1
								If LevelTileVisited(nextx,nexty)=False And LevelTileMatchesTarget(nextx,nexty)
									If Rnd(0.0,1.0)<=FillDensity#
										If EditorMode=0
											ChangeLevelTile(nextx,nexty,True)
										ElseIf EditorMode=3
											PlaceObject(nextx,nexty)
										EndIf
									EndIf
									LevelTileVisited(nextx,nexty)=True
									FloodStackX(ElementCount)=nextx
									FloodStackY(ElementCount)=nexty
									ElementCount=ElementCount+1
								EndIf
							EndIf
						Wend
					Else 
						; default
						If CustomBrush
							If EditorMode=0
								BrushOffset=BrushSize/2
								BrushXStart=x-BrushOffset
								BrushYStart=y-BrushOffset
								For i=0 To BrushSize-1
									For j=0 To BrushSize-1
										GrabLevelTileFromBrush(i,j)
										ChangeLevelTile(BrushXStart+i,BrushYStart+j,True)
									Next
								Next
							ElseIf EditorMode=3
								For k=0 To NofBrushObjects-1
									GrabObjectFromBrush(k)
									PlaceObject(x+BrushObjectXOffset#(k),y+BrushObjectYOffset#(k))
								Next
							EndIf
						Else
							BrushOffset=BrushSize/2
							BrushXStart=x-BrushOffset
							BrushYStart=y-BrushOffset
							For i=0 To BrushSize-1
								For j=0 To BrushSize-1
									If EditorMode=0
										ChangeLevelTile(BrushXStart+i,BrushYStart+j,True)
									ElseIf EditorMode=3
										PlaceObject(BrushXStart+i,BrushYStart+j)
									EndIf
								Next
							Next
						EndIf
					EndIf
					
					If EditorMode=3
						LeftMouseReleased=False
					EndIf
				EndIf
				If RightMouse=True And RightMouseReleased=True
					RightMouseReleased=False
					
					If BlockMode=2
						BlockMode=1
					Else				
						If EditorMode=0
							GrabLevelTile(x,y)
						ElseIf EditorMode=3
							GrabObject(x,y)
						EndIf
					EndIf
					;BlockMode=0
					;FillMode=0
				EndIf
			Else
				HideEntity CursorMesh
				HideEntity CursorMesh2
				x=-1
				y=-1
			EndIf
	
			If BlockMode=2 And DeleteKey=True And DeleteKeyReleased=True
				DeleteKeyReleased=False
				
				For i=cornleft To cornright
						For j=cornup To corndown
							DeleteObjectAt(i,j)
						Next
					Next
				
				BlockMode=1
				Delay 100
			Else If FillMode=1 And DeleteKey=True And DeleteKeyReleased=True
				; flood fill but it deletes
				DeleteKeyReleased=False
				
				; initialize state
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						LevelTileVisited(i,j)=False
					Next
				Next
				
				SetLevelTileAsTarget(x,y)
				;DeleteObjectAt(x,y)
				FloodStackX(0)=x
				FloodStackY(0)=y
				ElementCount=1
				While ElementCount<>0
					ElementCount=ElementCount-1
					thisx=FloodStackX(ElementCount)
					thisy=FloodStackY(ElementCount)
												
					If thisx>0
						nextx=thisx-1
						nexty=thisy
						If LevelTileVisited(nextx,nexty)=False And LevelTileMatchesTarget(nextx,nexty)
							DeleteObjectAt(nextx,nexty)
							LevelTileVisited(nextx,nexty)=True
							FloodStackX(ElementCount)=nextx
							FloodStackY(ElementCount)=nexty
							ElementCount=ElementCount+1
						EndIf
					EndIf
					If thisx<LevelWidth-1
						nextx=thisx+1
						nexty=thisy
						If LevelTileVisited(nextx,nexty)=False And LevelTileMatchesTarget(nextx,nexty)
							DeleteObjectAt(nextx,nexty)
							LevelTileVisited(nextx,nexty)=True
							FloodStackX(ElementCount)=nextx
							FloodStackY(ElementCount)=nexty
							ElementCount=ElementCount+1
						EndIf
					EndIf
					If thisy>0
						nextx=thisx
						nexty=thisy-1
						If LevelTileVisited(nextx,nexty)=False And LevelTileMatchesTarget(nextx,nexty)
							DeleteObjectAt(nextx,nexty)
							LevelTileVisited(nextx,nexty)=True
							FloodStackX(ElementCount)=nextx
							FloodStackY(ElementCount)=nexty
							ElementCount=ElementCount+1
						EndIf
					EndIf
					If thisy<LevelHeight-1
						nextx=thisx
						nexty=thisy+1
						If LevelTileVisited(nextx,nexty)=False And LevelTileMatchesTarget(nextx,nexty)
							DeleteObjectAt(nextx,nexty)
							LevelTileVisited(nextx,nexty)=True
							FloodStackX(ElementCount)=nextx
							FloodStackY(ElementCount)=nexty
							ElementCount=ElementCount+1
						EndIf
					EndIf
				Wend
			EndIf
			
			If ReturnKey=True And ReturnKeyReleased=True And BlockMode=0
				ReturnKeyReleased=False
				If CustomBrush
					CustomBrush=False
				Else
					; set custom brush
					CustomBrush=True
					CustomBrushEditorMode=EditorMode
					NofBrushObjects=0
					BrushOffset=BrushSize/2
					BrushXStart=x-BrushOffset
					BrushYStart=y-BrushOffset
					If EditorMode=0
						For i=0 To BrushSize-1
							For j=0 To BrushSize-1
								CopyLevelTileToBrush(BrushXStart+i,BrushYStart+j,i,j)
							Next
						Next
					ElseIf EditorMode=3
						For k=0 To NofObjects-1
							If ObjectX(k)>BrushXStart And ObjectX(k)<BrushXStart+BrushSize And ObjectY(k)>BrushYStart And ObjectY(k)<BrushYStart+BrushSize
								CopyObjectDataToBrush(k,NofBrushObjects,ObjectX(k)-x,ObjectY(k)-y)
								NofBrushObjects=NofBrushObjects+1
							EndIf
						Next
					EndIf
				EndIf
			EndIf
		
		Else
			HideEntity CursorMesh
			HideEntity cursorMesh2
		EndIf
	EndIf
	
	
	If mx>=0 And mx<500 And my>=0 And my<500
		Entity=CameraPick(camera1,MouseX(),MouseY())
		If Entity>0
			
			x=Floor(PickedX())
			y=Floor(-PickedZ())
			; Delete Object
			If DeleteKey=True And DeleteKeyReleased=True
				
				DeleteKeyReleased=False
				
				BrushOffset=BrushSize/2
				BrushXStart=x-BrushOffset
				BrushYStart=y-BrushOffset
				For i=0 To BrushSize-1
					For j=0 To BrushSize-1
						DeleteObjectAt(BrushXStart+i,BrushYStart+j)
					Next
				Next
				
				
			EndIf
			
		Else
			HideEntity CursorMesh
			HideEntity CursorMesh2

			x=-1
			y=-1
		EndIf

	EndIf
	


	

	
	; *************************************
	; Selecting A Texture
	; *************************************
	If EditorMode=1 Or EditorMode=2
		If mx>=0 And mx<500 And my>=0 And my<500 
			ScaleEntity CursorMesh,0.0325,0.01,0.0325
			PositionEntity CursorMesh,Floor(mx/62.5)*0.125+0.0625,200,-Floor(my/62.5)*0.125-0.0625
			ShowEntity CursorMesh	
			ShowEntity CursorMesh2
			If LeftMouse=True
				If editormode=1
					; main texture
					CurrentTileTexture=Floor(mx/62.5)+Floor(my/62.5)*8
				Else If editormode=2
					; main texture
					CurrentTileSideTexture=Floor(mx/62.5)+Floor(my/62.5)*8
				EndIf
				SetEditorMode(0)
				LeftMouseReleased=False
				CameraProjMode Camera1,1
				CameraProjMode Camera3,0
				BuildCurrentTileModel()
				ScaleEntity CursorMesh,1,1,1
			EndIf
	
		Else 
			HideEntity CursorMesh
			HideEntity CursorMesh2

		EndIf
	EndIf

	
	; *************************************
	; Change the CurrentTile
	; *************************************
	
	Color TextLevelR,TextLevelG,TextLevelB
	Text 590,5,"TILES"
		
	StartX=510
	StartY=20
	
	If MX>=StartX And MX<StartX+200 And MY>=StartY And MY<StartY+220
		If LeftMouse=True Or RightMouse=True Or MouseScroll<>0 Or ReturnKey=True
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		RotationSpeed=4
		If KeyDown(23) ; I, formerly A (30)
			TurnEntity CurrentMesh,0,RotationSpeed,0
		EndIf
		If KeyDown(24) ; O, formerly D (32)
			TurnEntity CurrentMesh,0,-RotationSpeed,0
		EndIf
	EndIf
	
	If MX>=StartX And MX<StartX+100 And MY>=StartY+35 And MY<StartY+100
		If RightMouse=True And RightMouseReleased=True 
			; CurrentTileRotation
			RightMouseReleased=False
			CurrentTileRotation=(CurrentTileRotation+1) Mod 8
			BuildCurrentTileModel()
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			; Texture
			CameraProjMode Camera1,0
			CameraProjMode Camera3,1
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
			SetEditorMode(1)
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentTileTextureUse=1-CurrentTileTextureUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
	EndIf
		
	
	; CurrentTileSideRotation/Texture
	If MX>=StartX And MX<StartX+100 And MY>=StartY+100 And MY<StartY+155
		If RightMouse=True And RightMouseReleased=True
			; SideRotation
			RightMouseReleased=False
			CurrentTileSideRotation=(CurrentTileSideRotation+1) Mod 8
			BuildCurrentTileModel()
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			; SideTexture
			CameraProjMode Camera1,0
			CameraProjMode Camera3,1
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
			SetEditorMode(2)
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentTileSideTextureUse=1-CurrentTileSideTextureUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
	EndIf
	
	; WaterTexture/Rotation
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+40 And MY<StartY+115
		If RightMouse=True And RightMouseReleased=True
			CurrentWaterTileRotation=(CurrentWaterTileRotation+1) Mod 8
			RightMouseReleased=False
			BuildCurrentTileModel()
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			CurrentWaterTileTexture=(CurrentWaterTileTexture+1) Mod 8
			LeftMouseReleased=False
			BuildCurrentTIleModel()
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentWaterTileUse=1-CurrentWaterTileUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

	EndIf

	; CurrentTileExtrusion
	If MX>=StartX And MX<StartX+100 And MY>=StartY And MY<StartY+15
		;CurrentTileExtrusion=Float(Floor(CurrentTileExtrusion2/10))/10.0
		CurrentTileExtrusion#=AdjustFloat#("Enter Xtrude: ", CurrentTileExtrusion#, 0.1, 1.0, 150)
		CurrentTileExtrusion2=CurrentTileExtrusion*100
		;If CurrentTileExtrusion#<Infinity
			CurrentTileExtrusion=Float(Floor(CurrentTileExtrusion2/10))/10.0
		;EndIf
		
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentTileExtrusionUse=1-CurrentTileExtrusionUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

 	EndIf
	; CurrentTileHeight
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY And MY<StartY+15
		CurrentTileHeight#=AdjustFloat#("Enter Height: ", CurrentTileHeight#, 0.1, 1.0, 150)
		CurrentTileHeight2=CurrentTileHeight*100
		CurrentTileHeight=Float(Floor(CurrentTileHeight2/10))/10.0

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentTileHeightUse=1-CurrentTileHeightUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

 	EndIf
	; CurrentTileLogic
	If MX>=StartX And MX<StartX+200 And MY>=StartY+15 And MY<StartY+30
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			If CtrlDown()
				CurrentTileLogic=InputInt("Enter Logic: ")
				ReturnKey=False
				ReturnKeyReleased=False
			Else
				Select CurrentTileLogic
					Case 0
						CurrentTileLogic=1
					Case 1
						CurrentTileLogic=2
					Case 2
						CurrentTileLogic=5
					Case 5
						CurrentTileLogic=11
					Case 11
						CurrentTileLogic=12
					Case 12
						CurrentTileLogic=13
					Default
						CurrentTileLogic=0
				End Select
			EndIf
			
			LeftMouseReleased=False
			RightMouseReleased=False
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			Select CurrentTileLogic
				Case 2
					CurrentTileLogic=1
				Case 5
					CurrentTileLogic=2
				Case 11
					CurrentTileLogic=5
				Case 12
					CurrentTileLogic=11
				Case 13
					CurrentTileLogic=12
				Case 0
					CurrentTileLogic=13
				Default
					CurrentTileLogic=0
			End Select
			LeftMouseReleased=False
			RightMouseReleased=False
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentTileLogicUse=1-CurrentTileLogicUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

 	EndIf

	; CurrentTileRandom
	If MX>=StartX And MX<StartX+200 And MY>=StartY+170 And MY<StartY+185
		CurrentTileRandom#=AdjustFloat#("Enter Random: ", CurrentTileRandom#, 0.01, 0.1, 150)
		CurrentTileRandom2=CurrentTileRandom*1000
		CurrentTileRandom=Float(Floor(CurrentTileRandom2/10))/100.0

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentTileRandomUse=1-CurrentTileRandomUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

 	EndIf
	; CurrentTileRounding
	If MX>=StartX And MX<StartX+100 And MY>=StartY+185 And MY<StartY+200
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
			LeftMouseReleased=False
			RigthMouseReleased=False
			CurrentTileRounding=1-CurrentTileRounding
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentTileRoundingUse=1-CurrentTileRoundingUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

	EndIf
	; CurrentTileEdgeRandom
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+185 And MY<StartY+200
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
			LeftMouseReleased=False
			RigthMouseReleased=False
			CurrentTileEdgeRandom=1-CurrentTileEdgeRandom
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentTileEdgeRandomUse=1-CurrentTileEdgeRandomUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

	EndIf
	
	; CurrentWaterTileHeight
	If MX>=StartX And MX<StartX+100 And MY>=StartY+200 And MY<StartY+215
		CurrentWaterTileHeight#=AdjustFloat#("Enter WHeight: ", CurrentWaterTileHeight#, 0.1, 1.0, 150)
		CurrentWaterTileHeight2=CurrentWaterTileHeight*100
		CurrentWaterTileHeight=Float(Floor(CurrentWaterTileHeight2/10))/10.0

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentWaterTileHeightUse=1-CurrentWaterTileHeightUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

 	EndIf
	; CurrentWaterTileTurbulence
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+200 And MY<StartY+215
		CurrentWaterTileTurbulence#=AdjustFloat#("Enter WTurb: ", CurrentWaterTileTurbulence#, 0.1, 1.0, 150)
		CurrentWaterTileTurbulence2=CurrentWaterTileTurbulence*100
		CurrentWaterTileTurbulence=Float(Floor(CurrentWaterTileTurbulence2/10))/10.0

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			CurrentWaterTileTurbulenceUse=1-CurrentWaterTileTurbulenceUse
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB
		EndIf

 	EndIf


	; *************************************
	; Textures and global settings
	; *************************************
	
	Color RectGlobalsR,RectGlobalsG,RectGlobalsB
	Rect 714,100,81,145,True
	Color TextLevelR,TextLevelG,TextLevelB


	Select LevelWeather
	Case 0
		Text 719,100,"Clear Sky"
	Case 1
		Text 715,100,"Light Snow"
	Case 2
		Text 715,100,"Heavy Snow"
	Case 3
		Text 715,100,"BlizzardRL"
	Case 4
		Text 715,100,"BlizzardLR"
	Case 5
		Text 715,100,"   Rain"
	Case 6
		Text 719,100,"  Weird"
	Case 7
		Text 715,100,"ThundrStrm"
	Case 8
		Text 715,100,"  Alarm"
	Case 9
		Text 715,100,"Light Rise"
	Case 10
		Text 715,100,"Light Fall"
	Case 11
		Text 715,100,"Rainb Rise"
	Case 12
		Text 715,100,"Rainb Fall"
	Case 13
		Text 715,100,"  Foggy"
	Case 14
		Text 715,100,"FoggyGreen"
	Case 15
		Text 715,100,"  Leaves"
	Case 16
		Text 715,100,"Sand Storm"
	Case 17
		Text 715,100," Abstract"




	
	End Select

	Select LevelMusic
	Case -1
		Text 715,115,"  Beach  "
	Case 0
		Text 715,115," No Music"
	Case 1
		Text 715,115," WA Intro"
	Case 2
		Text 715,115," Pastoral"
	Case 3
		Text 715,115,"WonderTown"
	Case 4
		Text 715,115,"Dark/Sewer"
	Case 5
		Text 715,115,"Cave/Woods"
	Case 6
		Text 715,115,"Scary/Void"
	Case 7
		Text 715,115,"WondrFalls"
	Case 8
		Text 715,115,"  Jungle  "
	Case 9
		Text 715,115,"KaboomTown"
	Case 10
		Text 715,115,"Acid Pools"
	Case 11
		Text 719,115,"  Retro  "
	Case 12
		Text 719,115,"  Cave  "
	Case 13
		Text 719,115,"POTZ Intro"
	Case 14
		Text 719,115," Uo Sound"
	Case 15
		Text 719,115,"Z-Ambience"
	Case 16
		Text 719,115,"Z-Synchron"
	Case 17
		Text 719,115,"RetroScary"
	Case 18
		Text 719,115,"DesertWind"
	Case 19
		Text 719,115,"DesertCave"
	Case 20
		Text 719,115,"Star World"
	Case 21
		Text 719,115,"  Piano   "
	Default
		Text 719,115,LevelMusic











	

	End Select
	
	
	If LevelEdgeStyle=1
		LevelEdgeStyleString$="-" ; DEFAULT
	ElseIf LevelEdgeStyle=2
		LevelEdgeStyleString$="B" ; BORDER
	ElseIf LevelEdgeStyle=3
		LevelEdgeStyleString$="X" ; BORDER X
	ElseIf LevelEdgeStyle=4
		LevelEdgeStyleString$="N" ; NONE
	Else
		LevelEdgeStyleString$=LevelEdgeStyle
	EndIf


	Text 715,133,"<LevelTex>"
	Text 715,150,"<WaterTex>"
	FlStartX=706 ; formerly 715
	Text FlStartX,165," Fl Tr Gl B"
	Text FlStartX+12,180,Str$(WaterFlow)
	Text FlStartX+36,180,Str$(WaterTransparent)
	Text FlStartX+60,180,Str$(WaterGlow)
	Text FlStartX+80,180,LevelEdgeStyleString$
	Text 723,200,"  Light  "
	Text 712,215,Str$(LightRed)
	Text 741,215,Str$(LightGreen)
	Text 770,215,Str$(LightBlue)
	Text 712,228,Str$(AmbientRed)
	Text 741,228,Str$(AmbientGreen)
	Text 770,228,Str$(AmbientBlue)

	If mx>=715 
				
		If my>=100 And my<115 And ((leftmouse=True And leftmousereleased=True) Or MouseScroll>0)
			leftmousereleased=False
			LevelWeather=LevelWeather+1
			If levelweather=18 Then levelweather=0
			LightingWasChanged()
		EndIf
		If my>=100 And my<115 And ((rightmouse=True And rightmousereleased=True) Or MouseScroll<0)
			rightmousereleased=False
			LevelWeather=LevelWeather-1
			If levelweather=-1 Then levelweather=17
			LightingWasChanged()
		EndIf
		If my>=115 And my<130 And ((leftmouse=True And leftmousereleased=True) Or MouseScroll>0)
			leftmousereleased=False
			If CtrlDown()
				LevelMusic=InputInt("Enter music ID: ")
			Else
				If Fast
					Adj=10
				Else
					Adj=1
				EndIf
				levelmusic=levelmusic+Adj
				If levelmusic=22 Then levelmusic=-1
			EndIf
		EndIf
		If my>=115 And my<130 And ((rightmouse=True And rightmousereleased=True) Or MouseScroll<0)
			rightmousereleased=False
			If Fast
				Adj=10
			Else
				Adj=1
			EndIf
			levelmusic=levelmusic-Adj
			If levelmusic=-2 Then levelmusic=20
		EndIf


		; level/water textures
		If my>133 And my<148 And mx>755 And leftmouse=True And leftmousereleased=True
			CurrentLevelTexture=CurrentLevelTexture+1
			If CurrentLevelTexture=NofLevelTextures Then currentleveltexture=0
			FreeTexture LevelTexture
			LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
			EntityTexture TexturePlane,LevelTexture
			;For j=0 To LevelHeight-1
			;	EntityTexture LevelMesh(j),LevelTexture
			;Next
			leftmousereleased=False
			buildcurrenttilemodel()
		EndIf
		If my>133 And my<148 And mx<=755 And leftmouse=True And leftmousereleased=True
			CurrentLevelTexture=CurrentLevelTexture-1
			If CurrentLevelTexture<0 Then currentleveltexture=NofLevelTextures-1
			FreeTexture LevelTexture
			LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
			EntityTexture TexturePlane,LevelTexture
			For j=0 To LevelHeight-1
				EntityTexture LevelMesh(j),LevelTexture
			Next
			leftmousereleased=False
			buildcurrenttilemodel()
		EndIf

		If my>150 And my<163 And mx>755 And leftmouse=True And leftmousereleased=True
			CurrentWaterTexture=CurrentWaterTexture+1
			
			If CurrentWaterTexture=NofWaterTextures Then currentWatertexture=0
			FreeTexture WaterTexture
			waterTexture=myLoadTexture("data\Leveltextures\"+waterTexturename$(CurrentWaterTexture),2)
			EntityTexture Currentwatertile,WaterTexture
			;For j=0 To LevelHeight-1
			;	EntityTexture WaterMesh(j),WaterTexture
			;Next
			leftmousereleased=False
			buildcurrenttilemodel()

		EndIf
		If my>150 And my<163 And mx<=755 And leftmouse=True And leftmousereleased=True
			CurrentWaterTexture=CurrentWaterTexture-1
			
			If CurrentWaterTexture=-1 Then currentWatertexture=NofWaterTextures-1
			FreeTexture WaterTexture
			waterTexture=myLoadTexture("data\Leveltextures\"+waterTexturename$(CurrentWaterTexture),2)
			EntityTexture Currentwatertile,WaterTexture
			;For j=0 To LevelHeight-1
			;	EntityTexture WaterMesh(j),WaterTexture
			;Next
			leftmousereleased=False
			buildcurrenttilemodel()

		EndIf

		; custom level/water
		If my>133 And my<148 And rightmouse=True And rightmousereleased=True
			FlushKeys
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color TextLevelR,TextLevelG,TextLevelB
			LevelTextureCustomName$=Input$( "Custom Texture Name (e.g. 'customtemplate'):")
			
			If FileType (globaldirname$+"\custom\leveltextures\leveltex "+leveltexturecustomname$+".bmp")<>1 And FileType (globaldirname$+"\custom content\textures\backgroundtex "+leveltexturecustomname$+"1.bmp")<>1 And FileType (globaldirname$+"\custom content\textures\backgroundtex "+leveltexturecustomname$+"2.bmp")<>1
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color 255,255,0
				Print "FILE(S) NOT FOUND!"
				Delay 2000
				
			Else
				FreeTexture LevelTexture
				LevelTexture=0
				LevelTexture=myLoadTexture(globaldirname$+"\custom\leveltextures\leveltex "+leveltexturecustomname$+".bmp",1)
				If LevelTexture=0
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,0
					Print "TEXTURE COULDN'T LOAD!"
					LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
					

					Delay 2000
				Else
					currentleveltexture=-1
				EndIf	
				
				EntityTexture TexturePlane,LevelTexture
				;For j=0 To LevelHeight-1
				;	EntityTexture LevelMesh(j),LevelTexture
				;Next
			EndIf
			rightmousereleased=False
			buildcurrenttilemodel()

		EndIf
		If my>150 And my<163 And rightmouse=True And rightmousereleased=True
			FlushKeys
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color TextLevelR,TextLevelG,TextLevelB
			WaterTextureCustomName$=Input$( "Custom WaterTexture Name (e.g. 'customtemplate'):")
			
			If FileType (globaldirname$+"\custom\leveltextures\watertex "+watertexturecustomname$+".jpg")<>1 
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color 255,255,0
				Print "FILE NOT FOUND!"
				Delay 2000
				
			Else
				FreeTexture WaterTexture
				WaterTexture=0
				WaterTexture=myLoadTexture(globaldirname$+"\custom\leveltextures\watertex "+watertexturecustomname$+".jpg",2)
				If WaterTexture=0
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,0
					Print "TEXTURE COULDN'T LOAD!"
					WaterTexture=myLoadTexture("data\Leveltextures\"+WaterTexturename$(CurrentWaterTexture),2)
					

					Delay 2000
				Else
					currentwatertexture=-1
				EndIf	
				
				EntityTexture Currentwatertile,WaterTexture
				For j=0 To LevelHeight-1
					EntityTexture WaterMesh(j),WaterTexture
				Next

			EndIf
			rightmousereleased=False
			
			buildcurrenttilemodel()


		EndIf

		
		If my>165 And my<195 ;my<185 
			If mx>FlStartX+8 And mx<FlStartX+24
				Waterflow=AdjustInt("Enter Waterflow: ", Waterflow, 1, 10, 150)
;				If (leftmouse=True And leftmousereleased=True) Or MouseScroll>0
;					Waterflow=WaterFlow+1
;					leftmousereleased=False
;				EndIf
;				If (rightmouse=True And rightmousereleased=True) Or MouseScroll<0
;					Waterflow=Waterflow-1
;					rightmousereleased=False
;				EndIf
			EndIf
			If mx>FlStartX+32 And mx<FlStartX+48
				If (leftmouse=True And leftmousereleased=True) Or MouseScroll<>0
					WaterTransparent=1-WaterTransparent
					leftmousereleased=False
				EndIf
				
			EndIf
			If mx>FlStartX+56 And mx<FlStartX+72
				If (leftmouse=True And leftmousereleased=True) Or MouseScroll<>0
					WaterGlow=1-WaterGlow
					leftmousereleased=False
				EndIf
				
			EndIf
			If mx>FlStartX+80 And mx<FlStartX+100
				LevelEdgeStyle=AdjustInt("Enter LevelEdgeStyle: ", LevelEdgeStyle, 1, 10, 150)
				If LevelEdgeStyle<1
					LevelEdgeStyle=4
				ElseIf LevelEdgeStyle>4
					LevelEdgeStyle=1
				EndIf
			EndIf


		EndIf
		
		
		

		
	EndIf
	
	If Fast
		ChangeSpeed=10
	Else
		ChangeSpeed=1
	EndIf
	
	If mx>712 And my>215 And mx<736 And my<228
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				LightRed=InputInt("Enter LightRed: ")
			Else
				LightRed=LightRed+ChangeSpeed
				;If lightred>=256 Then lightred=lightred-256
			EndIf
			LightingWasChanged()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightRed=LightRed-ChangeSpeed
			;If lightred=-1 Then lightred=255
			LightingWasChanged()
		EndIf
	EndIf
	If mx>712+29 And my>215 And mx<736+29 And my<228
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				LightGreen=InputInt("Enter LightGreen: ")
			Else
				LightGreen=LightGreen+ChangeSpeed
				;If LightGreen=256 Then LightGreen=0
			EndIf
			LightingWasChanged()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightGreen=LightGreen-ChangeSpeed
			;If LightGreen=-1 Then LightGreen=255
			LightingWasChanged()
		EndIf
	EndIf
	If mx>712+29+29 And my>215 And mx<736+29+29 And my<228
		If leftmouse=True  Or MouseScroll>0
			If CtrlDown()
				LightBlue=InputInt("Enter LightBlue: ")
			Else
				LightBlue=LightBlue+ChangeSpeed
				;If LightBlue=256 Then LightBlue=0
			EndIf
			LightingWasChanged()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightBlue=LightBlue-ChangeSpeed
			;If LightBlue=-1 Then LightBlue=255
			LightingWasChanged()
		EndIf
	EndIf
	
		If mx>712 And my>215+13 And mx<736 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientRed=InputInt("Enter AmbientRed: ")
			Else
				AmbientRed=AmbientRed+ChangeSpeed
				;If Ambientred=256 Then ambientred=0
			EndIf
			LightingWasChanged()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientRed=AmbientRed-ChangeSpeed
			;If Ambientred=-1 Then ambientred=255
			LightingWasChanged()
		EndIf
	EndIf
	If mx>712+29 And my>215+13 And mx<736+29 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientGreen=InputInt("Enter AmbientGreen: ")
			Else
				AmbientGreen=AmbientGreen+ChangeSpeed
				;If AmbientGreen=256 Then AmbientGreen=0
			EndIf
			LightingWasChanged()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientGreen=AmbientGreen-ChangeSpeed
			;If AmbientGreen=-1 Then AmbientGreen=255
			LightingWasChanged()
		EndIf
	EndIf
	If mx>712+29+29 And my>215+13 And mx<736+29+29 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientBlue=InputInt("Enter AmbientBlue: ")
			Else
				AmbientBlue=AmbientBlue+ChangeSpeed
				;If AmbientBlue=256 Then AmbientBlue=0
			EndIf
			LightingWasChanged()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientBlue=AmbientBlue-ChangeSpeed
			;If AmbientBlue=-1 Then AmbientBlue=255
			LightingWasChanged()
		EndIf
	EndIf





	
		
			

	; *************************************
	; Preset Tiles
	; *************************************
	
	StartX=715
	StartY=190
	
		
	StartX=510
	StartY=245
	Color TileColorR,TileColorG,TileColorB
	Rect StartX,StartY,285,40,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+2,StartY+2,"                                   "
	Text StartX+2+285/2-4*(Len(TilePresetCategoryName$(CurrentTilePresetCategory))+10),StartY,"Category: "+TilePresetCategoryName$(CurrentTilePresetCategory)
	Text StartX+2,StartY+22,"                                   "
	Text StartX+2+285/2-4*(Len(TilePresetTileName$(CurrentTilePresetTile))+2),StartY+22,"Tile: "+Left$(TilePresetTileName$(CurrentTilePresetTile),Len(TilePresetTileName$(CurrentTilePresetTile))-4)
	
	If mx>=startx And mx<startx+285 And my>=StartY+0 And my<StartY+20
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentTilePresetCategory=CurrentTilePresetCategory-1
			If CurrentTilePresetCategory=-1 Then CurrentTilePresetCategory=NofTilePresetCategories-1
			RightMouseReleased=False
			CurrentTilePresetTile=0
			i=CurrentTilePresetCategory
			Repeat
				NofTilePresetTiles=0
				Dir=ReadDir("Data\Editor\TilePresets\"+TilePresetCategoryName$(i))
				file$=NextFile$(Dir)
				While file$<>""
					If file$<>"." And file$<>".." And FileType("Data\Editor\TilePresets\"+TilePresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".tp1"
						TilePresetTileName$(NofTilePresetTiles)=file$
						NofTilePresetTiles=NofTilePresetTiles+1
					EndIf
					file$=NextFile$(Dir)
				Wend
				CloseDir dir
				CurrentTilePresetCategory=i
				i=i-1
				If i=-1 Then i=NofTilePresetCategories-1

			Until NofTilePresetTiles>0
			LoadTilePreset()
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB

		EndIf
	EndIf
	If mx>=startx And mx<startx+285 And my>=StartY+0 And my<StartY+20
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentTilePresetCategory=CurrentTilePresetCategory+1
			If CurrentTilePresetCategory=NofTilePresetCategories Then CurrentTilePresetCategory=0
			LeftMouseReleased=False
			CurrentTilePresetTile=0
			CurrentTilePresetTile=0
			i=CurrentTilePresetCategory
			Repeat
				NofTilePresetTiles=0
				Dir=ReadDir("Data\Editor\TilePresets\"+TilePresetCategoryName$(i))
				file$=NextFile$(Dir)
				While file$<>""
					If file$<>"." And file$<>".." And FileType("Data\Editor\TilePresets\"+TilePresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".tp1"
						TilePresetTileName$(NofTilePresetTiles)=file$
						NofTilePresetTiles=NofTilePresetTiles+1
					EndIf
					file$=NextFile$(Dir)
				Wend
				CloseDir dir
				CurrentTilePresetCategory=i
				i=i+1
				If i=NofTilePresetCategories Then i=0

			Until NofTilePresetTiles>0
			LoadTilePreset()
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB

		EndIf
	EndIf

	
	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentTilePresetTile=CurrentTilePresetTile-1
			If CurrentTilePresetTile=-1 Then CurrentTilePresetTile=NofTilePresetTiles-1
			RightMouseReleased=False
			LoadTilePreset()
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB

		EndIf
	EndIf
	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentTilePresetTile=CurrentTilePresetTile+1
			If CurrentTilePresetTile=NofTilePresetTiles Then CurrentTilePresetTile=0
			LeftMouseReleased=False
			LoadTilePreset()
			SetEditorMode(0)
			CameraProjMode Camera1,1
			CameraProjMode Camera3,0
			CameraClsColor camera2,TileColorR,TileColorG,TileColorB

		EndIf
	EndIf


	
	
	; *************************************
	; LevelSize
	; *************************************

	Text 719,5," GLOBALS"
	StartX=715
	StartY=20
	Color RectOnR,RectOnG,RectOnB
	Rect StartX,StartY,80,35,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+20,StartY+2,"Width"
	Text StartX,StartY+15,"<<"
	Text StartX+80-16,StartY+15,">>"
	If LevelWidth>9
		Text StartX+40-8,StartY+15,Str$(levelWidth)
	Else 
		Text StartX+40-16,StartY+15,Str$(levelWidth)
	EndIf
	If mx>=StartX And mx<StartX+40 And my>=StartY+15 And my<StartY+30 
		If LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			If CtrlDown()
				NewWidth=InputInt("Enter Width: ")
				DeltaWidth=NewWidth-LevelWidth
				;If NewWidth>LevelWidth
				;	For i=1 To DeltaWidth
				;		WidthLeftChange=1
				;		ResizeLevel()
				;	Next
				;ElseIf NewWidth<LevelWidth
				;	For i=1 To -DeltaWidth
				;		WidthLeftChange=-1
				;		ResizeLevel()
				;	Next
				;EndIf
				WidthLeftChange=DeltaWidth
				ReSizeLevel()
			Else
				WidthLeftChange=1
				ReSizeLevel()
			EndIf
		EndIf
		If RightMouse=True And RightMouseReleased=True
			WidthLeftChange=-1
			RightMouseReleased=False
			ReSizeLevel()
		EndIf

	EndIf
	If mx>=StartX+40 And mx<StartX+80 And my>=StartY+15 And my<StartY+30 
		If LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			If CtrlDown()
				NewWidth=InputInt("Enter Width: ")
				DeltaWidth=NewWidth-LevelWidth
				;If NewWidth>LevelWidth
				;	For i=1 To DeltaWidth
				;		WidthRightChange=1
				;		ResizeLevel()
				;	Next
				;ElseIf NewWidth<LevelWidth
				;	For i=1 To -DeltaWidth
				;		WidthRightChange=-1
				;		ResizeLevel()
				;	Next
				;EndIf
				WidthRightChange=DeltaWidth
				ReSizeLevel()
			Else
				WidthRightChange=1
				ReSizeLevel()
			EndIf
		EndIf
		If RightMouse=True And RightMouseReleased=True
			WidthRightChange=-1
			RightMouseReleased=False
			ReSizeLevel()
		EndIf

	EndIf
	
	StartY=50
	Color RectOnR,RectOnG,RectOnB
	Rect StartX,StartY,80,35,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+16,StartY+2,"Height"
	Text StartX,StartY+15,"<<"
	Text StartX+80-16,StartY+15,">>"
	If LevelHeight>9
		Text StartX+40-8,StartY+15,Str$(LevelHeight)
	Else 
		Text StartX+40-16,StartY+15,Str$(LevelHeight)
	EndIf
	If mx>=StartX And mx<StartX+40 And my>=StartY+15 And my<StartY+30 
		If LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			If CtrlDown()
				NewHeight=InputInt("Enter Height: ")
				DeltaHeight=NewHeight-LevelHeight
				;If NewHeight>LevelHeight
				;	For i=1 To DeltaHeight
				;		HeightTopChange=1
				;		ResizeLevel()
				;	Next
				;ElseIf NewHeight<LevelHeight
				;	For i=1 To -DeltaHeight
				;		HeightTopChange=-1
				;		ResizeLevel()
				;	Next
				;EndIf
				HeightTopChange=DeltaHeight
				ResizeLevel()
			Else
				HeightTopChange=1
				ReSizeLevel()
			EndIf
		EndIf
		If RightMouse=True And RightMouseReleased=True
			HeightTopChange=-1
			RightMouseReleased=False
			ReSizeLevel()
		EndIf

	EndIf
	If mx>=StartX+40 And mx<StartX+80 And my>=StartY+15 And my<StartY+30 
		If LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			If CtrlDown()
				NewHeight=InputInt("Enter Height: ")
				DeltaHeight=NewHeight-LevelHeight
				;If NewHeight>LevelHeight
				;	For i=1 To DeltaHeight
				;		HeightBottomChange=1
				;		ResizeLevel()
				;	Next
				;ElseIf NewHeight<LevelHeight
				;	For i=1 To -DeltaHeight
				;		HeightBottomChange=-1
				;		ResizeLevel()
				;	Next
				;EndIf
				HeightBottomChange=DeltaHeight
				ResizeLevel()
			Else
				HeightBottomChange=1
				ReSizeLevel()
			EndIf
		EndIf
		If RightMouse=True And RightMouseReleased=True
			HeightBottomChange=-1
			RightMouseReleased=False
			ReSizeLevel()
		EndIf

	EndIf
	
	; *************************************
	; OBJECTS
	; *************************************
	
	Color TextLevelR,TextLevelG,TextLevelB
	Text 650-7*4,290,"OBJECTS"
	StartX=510
	StartY=305
	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,185,150
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+92-11*4,StartY,"ADJUSTMENTS"
	
	If CurrentGrabbedObject<>-1
		Text StartX+2,StartY,"#"+CurrentGrabbedObject
	EndIf
	
	For i=ObjectAdjusterStart+0 To ObjectAdjusterStart+8
		
		DisplayObjectAdjuster(i)
		
	Next
	
	For i=ObjectAdjusterStart+0 To ObjectAdjusterStart+8
		If mx>=StartX And mx<=StartX+185 And my>=StartY+15+(i-ObjectAdjusterStart)*15 And my<StartY+15+(i-ObjectAdjusterStart)*15+15
			If LeftMouse=True Or RightMouse=True Or MouseScroll<>0 Or ReturnKey=True
				AdjustObjectAdjuster(i)
				SetEditorMode(3)

			EndIf
		EndIf
	Next
	
	; *************************************
	; Preset Objects
	; *************************************
	
	StartX=695
	StartY=435
	

	If mx>500 And my>305 And my<455 ;my<430
		If LeftMouse=True Or RightMouse=True
			SetEditorMode(3)
		EndIf
		If DeleteKey=True And DeleteKeyReleased=True And CurrentGrabbedObject<>-1
			DeleteKeyReleased=False
			DeleteObject(CurrentGrabbedObject)
			SetEditorMode(3)
		EndIf
	EndIf
	
	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,100,20,True
	Color TextLevelR,TextLevelG,TextLevelB
	
	; Placed in code before "More" to eat the click before it hits "More".
	If CurrentGrabbedObject<>-1 And CurrentGrabbedObjectModified
		Text StartX+50,StartY+2,"Update"
		If mx>=StartX+44 And Mx<StartX+100 And my>=StartY And my<StartY+20
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				SetEditorMode(3)
				PasteObjectData(CurrentGrabbedObject)
				CurrentGrabbedObjectModified=False
			EndIf
		EndIf
		If KeyDown(19) ; R
			SetEditorMode(3)
			PasteObjectData(CurrentGrabbedObject)
			CurrentGrabbedObjectModified=False
		EndIf
	EndIf
	
	If NofObjectAdjusters>9
		Text StartX+6,StartY+2,"More"
	EndIf
	If mx>=StartX And Mx<StartX+80 And my>=StartY And my<StartY+20
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			LeftMouseReleased=False
			SetEditorMode(3)
			If ObjectAdjusterStart+9<NofObjectAdjusters
				ObjectAdjusterStart=ObjectAdjusterStart+9
			Else 
				ObjectAdjusterStart=0
			EndIf
		Else If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			RightMouseReleased=False
			SetEditorMode(3)
			If ObjectAdjusterStart=0
				ObjectAdjusterStart=((NofObjectAdjusters-1)/9)*9
			Else 
				ObjectAdjusterStart=ObjectAdjusterStart-9
			EndIf
		EndIf
	EndIf

	
	StartX=510
	StartY=460
	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,285,40,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+2,StartY+2,"                                   "
	Text StartX+2+285/2-4*(Len(ObjectPresetCategoryName$(CurrentObjectPresetCategory))+10),StartY,"Category: "+ObjectPresetCategoryName$(CurrentObjectPresetCategory)
	Text StartX+2,StartY+22,"                                   "
	Text StartX+2+285/2-4*(Len(ObjectPresetObjectName$(CurrentObjectPresetObject))+4),StartY+22,"Object: "+Left$(ObjectPresetObjectName$(CurrentObjectPresetObject),Len(ObjectPresetObjectName$(CurrentObjectPresetObject))-4)
	
	If mx>=startx And mx<startx+285 And my>=StartY+0 And my<StartY+20
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			SetCurrentGrabbedObject(-1)
			CurrentObjectPresetCategory=CurrentObjectPresetCategory-1
			If CurrentObjectPresetCategory=-1 Then CurrentObjectPresetCategory=NofObjectPresetCategories-1
			RightMouseReleased=False
			CurrentObjectPresetObject=0
			i=CurrentObjectPresetCategory
			Repeat
				NofObjectPresetObjects=0
				Dir=ReadDir("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(i))
				file$=NextFile$(Dir)
				While file$<>""
					If file$<>"." And file$<>".." And FileType("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".wop"
						ObjectPresetObjectName$(NofObjectPresetObjects)=file$
						NofObjectPresetObjects=NofObjectPresetObjects+1
					EndIf
					file$=NextFile$(Dir)
				Wend
				CloseDir dir
				CurrentObjectPresetCategory=i
				i=i-1
				If i=-1 Then i=NofObjectPresetCategories-1

			Until NofObjectPresetObjects>0
			SetEditorMode(3)
			LoadObjectPreset()
			BuildCurrentObjectModel()

		EndIf
	EndIf
	If mx>=startx And mx<startx+285 And my>=StartY+0 And my<StartY+20
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			SetCurrentGrabbedObject(-1)
			CurrentObjectPresetCategory=CurrentObjectPresetCategory+1
			If CurrentObjectPresetCategory=NofObjectPresetCategories Then CurrentObjectPresetCategory=0
			LeftMouseReleased=False
			CurrentObjectPresetObject=0
			CurrentObjectPresetObject=0
			i=CurrentObjectPresetCategory
			Repeat
				NofObjectPresetObjects=0
				Dir=ReadDir("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(i))
				file$=NextFile$(Dir)
				While file$<>""
					If file$<>"." And file$<>".." And FileType("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".wop"
						ObjectPresetObjectName$(NofObjectPresetObjects)=file$
						NofObjectPresetObjects=NofObjectPresetObjects+1
					EndIf
					file$=NextFile$(Dir)
				Wend
				CloseDir dir
				CurrentObjectPresetCategory=i
				i=i+1
				If i=NofObjectPresetCategories Then i=0

			Until NofObjectPresetObjects>0
			SetEditorMode(3)
			LoadObjectPreset()
			BuildCurrentObjectModel()

		EndIf
	EndIf

	
	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			SetCurrentGrabbedObject(-1)
			CurrentObjectPresetObject=CurrentObjectPresetObject-1
			If CurrentObjectPresetObject=-1 Then CurrentObjectPresetObject=NofObjectPresetObjects-1
			RightMouseReleased=False
			SetEditorMode(3)
			LoadObjectPreset()
			BuildCurrentObjectModel()

		EndIf
	EndIf
	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			SetCurrentGrabbedObject(-1)
			CurrentObjectPresetObject=CurrentObjectPresetObject+1
			If CurrentObjectPresetObject=NofObjectPresetObjects Then CurrentObjectPresetObject=0
			LeftMouseReleased=False
			SetEditorMode(3)
			LoadObjectPreset()
			BuildCurrentObjectModel()

		EndIf
	EndIf




	
			
	; *************************************
	; load/SAVE/ETC	
	; *************************************

	
	If MX>=00 And Mx<100
		If my>=510 And my<540
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				;block
				If blockmode=0 
					blockmode=1
				Else
					blockmode=0
				EndIf
				FillMode=0
				
				Delay 100
				
			EndIf
		EndIf
	
		If my>=540 And my<570
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				;wipe
				If GetConfirmation("Are you sure you want to wipe?")
					For i=0 To LevelWidth-1
						For j=0 To LevelHeight-1
							ChangeLevelTile(i,j,True)
						Next
					Next
				EndIf
			EndIf
		EndIf
		
		If my>=570 And my<600
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				;fill
				If CtrlDown()
					fillmode=1
					FillDensity#=InputFloat#("Enter fill density (0.0 to 1.0): ")
					If FillDensity#<0.0
						FillDensity#=0.0
					ElseIf FillDensity#>1.0
						FillDensity#=1.0
					EndIf
				ElseIf fillmode=0 
					fillmode=1
					FillDensity#=1.0
				Else
					fillmode=0
				EndIf
				BlockMode=0
				
				Delay 100
				
			EndIf
		EndIf
	
	EndIf

	If MX>=100 And Mx<200
		If my>=510 And my<540
			If LeftMouse=True And LeftMouseReleased=True
				;flip
				Flipped=False
				DesiredFlip$=Upper$(InputString$("Enter desired flip (X, Y, or XY): "))
				If DesiredFlip$="X"
					FlipLevelX()
					Flipped=True
				ElseIf DesiredFlip$="Y"
					FlipLevelY()
					Flipped=True
				ElseIf DesiredFlip$="XY"
					FlipLevelXY()
					Flipped=True
				EndIf
				
				If Flipped
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					Print "Flipped"
					Delay 1000
				EndIf
			EndIf
		EndIf
;		If my>=540 And my<570
;			If LeftMouse=True And LeftMouseReleased=True
;				;flipy
;				FlipLevelY()
;				Locate 0,0
;				Color 0,0,0
;				Rect 0,0,500,40,True
;				Color 255,255,255
;				Print "Flipped"
;				Delay 1000
;			EndIf
;		EndIf
;		If my>=570 And my<600
;			If LeftMouse=True And LeftMouseReleased=True
;				;flipxy
;				FlipLevelXY()
;				Locate 0,0
;				Color 0,0,0
;				Rect 0,0,500,40,True
;				Color 255,255,255
;				Print "Flipped"
;				Delay 1000
;
;			EndIf
;		EndIf

		If my>=565 And my<595
			If LeftMouse=True And LeftMouseReleased=True
				;texture prefix
				FlushKeys
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color 255,255,255
				Print "Enter texture prefix (leave blank to disable texture prefix): "
				TexturePrefix$=Input$("")
			EndIf
		EndIf

	EndIf
	

	If MX>=200 And MX<300
		; show/hide markers
		If my>520 And my<550
			If LeftMouse=True And LeftMouseReleased=True
				ShowObjectPositions=Not ShowObjectPositions
				If ShowObjectPositions=True 
					For j=0 To NofObjects-1
						ShowEntity ObjectPositionMarker(j)
					Next
				EndIf
				If ShowObjectPositions=False
					For j=0 To NofObjects-1
						HideEntity ObjectPositionMarker(j)
					Next
				EndIf
	
				
				Delay 100
			EndIf
		; show/hide objects
		Else If my>565 And my<595
			If LeftMouse=True And LeftMouseReleased=True
				ShowObjectMesh=Not ShowObjectMesh

				For j=0 To NofObjects-1
					UpdateObjectVisibility(j)
				Next
	
				
				Delay 100
			EndIf
		EndIf

	EndIf
	
	
	If MX>=300 And MX<400
		; show/hide logic
		If my>520 And my<550
			If LeftMouse=True And LeftMouseReleased=True
				ShowLogicMesh=Not ShowLogicMesh
				If ShowLogicMesh=True 
					For j=0 To Levelheight-1
						ShowEntity LogicMesh(j)
					Next
				EndIf
				If ShowLogicMesh=False 
					For j=0 To Levelheight-1
						HideEntity LogicMesh(j)
					Next
				EndIf
	
				
				Delay 100
			EndIf
 		 ; show/hide level
		 Else If my>565 And my<595
			If LeftMouse=True And LeftMouseReleased=True
				ShowLevelMesh=Not ShowLevelMesh
				If ShowLevelMesh=True 
					For j=0 To Levelheight-1
						ShowEntity LEvelMesh(j)
					Next
				EndIf
				If ShowLevelMesh=False 
					For j=0 To Levelheight-1
						HideEntity LEvelMesh(j)
					Next
				EndIf
	
				
				Delay 100
			EndIf
		EndIf
	EndIf
	
	If MX>=400 And MX<500
		; border expand
		If my>520 And my<550
			If LeftMouse=True And LeftMouseReleased=True
				BorderExpandOption=Not BorderExpandOption
				
				
				Delay 100
			EndIf
		EndIf
		
		; simulation level
		If my>565 And my<595
			NewValue=AdjustInt("Enter Simulation Level: ", SimulationLevel, 1, 10, 150)
			If NewValue>SimulationLevelMax
				NewValue=0
			ElseIf NewValue<0
				NewValue=SimulationLevelMax
			EndIf
			WasChanged=NewValue<>SimulationLevel
			SimulationLevel=NewValue
			If WasChanged
				; move objects back to their default positions
				ResetSimulatedQuantities()
				For i=0 To NofObjects-1
					SimulateObjectPosition(i)
					SimulateObjectRotation(i)
					SimulateObjectScale(i)
					
					UpdateObjectVisibility(i)
				Next
				
				LightingWasChanged()
			EndIf
			
;			If LeftMouse=True And LeftMouseReleased=True
;				LevelEdgeStyle = LevelEdgeStyle+1				
;				Delay 100
;			EndIf
;			If RightMouse=True And RightMouseReleased=True
;				LevelEdgeStyle = LevelEdgeStyle-1				
;				Delay 100
;			EndIf
		EndIf

	EndIf
	
	If MX>=500 And MX<600
		; brush size
		If my>520 And my<550
			If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
				;BorderExpandOption=Not BorderExpandOption
				;WidescreenRangeLevel=WidescreenRangeLevel+1
				;If WidescreenRangeLevel>1
				;	WidescreenRangeLevel=-1
				;ElseIf WidescreenRangeLevel<-1
				;	WidescreenRangeLevel=1
				;EndIf
				
				SetBrushSize(BrushSize+2)
				If MouseScroll=0 Then Delay 100
			EndIf
			If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
				SetBrushSize(BrushSize-2)
				If MouseScroll=0 Then Delay 100
			EndIf
		EndIf
		
		; elevation
		If my>565 And my<595
			If LeftMouse=True And LeftMouseReleased=True				
				Adjustment#=InputFloat#("Amount to shift level up/down: ")
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						LevelTileExtrusion(i,j)=LevelTileExtrusion(i,j)+Adjustment
						WaterTileHeight(i,j)=WaterTileHeight(i,j)+Adjustment
					Next
				Next
				;ReBuildLevelModel()
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						UpdateTile(i,j)
					Next
				Next
				For i=0 To NofObjects-1
					ObjectZAdjust(i)=ObjectZAdjust(i)+Adjustment
					UpdateObjectPosition(i)
				Next
				CurrentObjectZAdjust=CurrentObjectZAdjust+Adjustment
				BuildCurrentObjectModel()
				SomeObjectWasChanged()
			EndIf
		EndIf
	EndIf
	
	If MX>=600 And MX<700
		; ID filter
		If my>520 And my<550
			If LeftMouse=True And LeftMouseReleased=True
				If IDFilterEnabled=False Or CtrlDown()
					IDFilterAllow=InputInt("Enter the ID to filter for: ")
					IDFilterEnabled=True
				Else
					IDFilterEnabled=False
				EndIf
				For j=0 To NofObjects-1
					UpdateObjectVisibility(j)
				Next
				Delay 100
			EndIf
			If IDFilterEnabled And MouseScroll<>0
				If ShiftDown()
					Adj=50
				Else
					Adj=1
				EndIf
				IDFilterAllow=IDFilterAllow+Adj*MouseScroll
				For j=0 To NofObjects-1
					UpdateObjectVisibility(j)
				Next
			EndIf
		EndIf
		
		; xtrude logics
		If my>565 And my<595
			If LeftMouse=True And LeftMouseReleased=True
				If GetConfirmation("Do you want to set Xtrude logics?")
					Prompt$=InputString$("Enter logic for Xtrude < 0 (leave blank for water): ")
					Logic=LogicNameToLogicId(Prompt$)
					If Logic=-1
						LessThanZero=2
					Else
						LessThanZero=Logic
					EndIf
					Prompt$=InputString$("Enter logic for Xtrude == 0 (leave blank for floor): ")
					Logic=LogicNameToLogicId(Prompt$)
					If Logic=-1
						EqualToZero=0
					Else
						EqualToZero=Logic
					EndIf
					Prompt$=InputString$("Enter logic for Xtrude > 0 (leave blank for wall): ")
					Logic=LogicNameToLogicId(Prompt$)
					If Logic=-1
						GreaterThanZero=1
					Else
						GreaterThanZero=Logic
					EndIf
					SetXtrudeLogics(LessThanZero,EqualToZero,GreaterThanZero)
				EndIf
			EndIf
		EndIf
	EndIf
	
	If LeftMouse=True And LeftMouseReleased=True

		If MX>700
			If my>515 And my<555
				; cancel and exit
				ResumeMaster()
				
				Repeat
				Until MouseDown(1)=False	
				
			Else If my>560 And my<600
				; save and exit
				If CurrentGrabbedObject<>-1 And CurrentGrabbedObjectModified
					FlushKeys
					SetupWarning()
					Print("You have not hit the Update button on the selected object.")
					Confirm$=Input$("Are you sure you want to exit? Type Y to confirm: ")
					If Confirm="Y" Or Confirm="y"
						SaveLevel()
						ResumeMaster()
					EndIf
				Else
					SaveLevel()
					ResumeMaster()
				EndIf
				
				Repeat
				Until MouseDown(1)=False
			
			EndIf
		EndIf
	EndIf

	


	

End Function


Function SetCurrentGrabbedObject(i)

	CurrentGrabbedObject=i
	CurrentGrabbedObjectModified=False
	
	If CurrentGrabbedObject=-1
		HideEntity CurrentGrabbedObjectMarker
	Else
		ShowEntity CurrentGrabbedObjectMarker
		SetEntityPositionToObjectPositionWithoutZ(CurrentGrabbedObjectMarker,CurrentGrabbedObject,0.0)
		;NofWopAdjusters=0
	EndIf

End Function


Function CtrlDown()

	Return KeyDown(29) Or KeyDown(157) ; left ctrl or right ctrl
	
End Function

Function ShiftDown()

	Return KeyDown(42) Or KeyDown(54) ; left shift or right shift

End Function

Function AltDown()

	Return KeyDown(56) Or KeyDown(184) ; left alt or right alt

End Function

Function SetupWarning()

	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,100,100

End Function

Function GetConfirmation(Message$)

	FlushKeys
	SetupWarning()
	Print Message$
	Confirm$=Input$("Type Y to confirm: ")
	Return Confirm="Y" Or Confirm="y"

End Function


Function ForceKeyRelease(keycode, keyname$)

	If KeyDown(keycode)
		SetupWarning()
		Print "Press "+keyname$+" to proceed."
		Print "I am doing this to protect you and your family."
		Repeat Until Not KeyDown(keycode)
	EndIf

End Function


Function LogicIdToLogicName$(TileLogic)

	If TileLogic>=0 And TileLogic<=14
		Return LogicName$(TileLogic)
	Else
		Return TileLogic
	EndIf

End Function

; returns -1 if no matching name is found
Function LogicNameToLogicId(Name$)

	For i=0 To 14
		If Upper$(LogicName$(i))=Upper$(Name$)
			Return i
		EndIf
	Next
	
	Return -1

End Function


Function LevelTileLogicHasVisuals(i,j)

	Return LevelTileLogic(i,j)>0 And LevelTileLogic(i,j)<15

End Function



Function ColorLevelTileLogic(i,j)

	Select LevelTileLogic(i,j)
	Case 1 ; wall
		red=255
		green=0
		blue=0
	Case 2; water
		red=0
		green=0
		blue=255
	Case 3; teleporter
		red=140
		green=100
		blue=0
	Case 4; bridge
		red=70
		green=70
		blue=15
	Case 5; lava
		red=140
		green=0
		blue=255
	Case 6 ; 06
		red=40
		green=40
		blue=40
	Case 7 ; 07
		red=80
		green=80
		blue=80
	Case 8 ; 08
		red=120
		green=120
		blue=120
	Case 9; button
		red=255
		green=255
		blue=0
	Case 10; stinker exit
		red=0
		green=255
		blue=0
	Case 11,12,13; ice
		red=0
		green=255
		blue=255
	Case 14; ice float
		red=255
		green=255
		blue=255
	Default
		red=0
		green=0
		blue=0
	End Select

	
	VertexColor LogicSurface(j),i*4+0,red,green,blue;,.5
	VertexColor LogicSurface(j),i*4+1,red,green,blue;,.5
	VertexColor LogicSurface(j),i*4+2,red,green,blue;,.5
	VertexColor LogicSurface(j),i*4+3,red,green,blue;,.5
		

End Function


Function BlocksForLevelAndWater()

	For j=0 To LevelHeight-1
		ClearSurface LevelSurface(j)
		For i=0 To LevelWidth-1
			; add block
			
			; top face
			CalculateUV(LevelTileTexture(i,j),0,0,LevelTileRotation(i,j),8)
			AddVertex (LevelSurface(j),i,LevelTileExtrusion(i,j),-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileTexture(i,j),1,0,LevelTileRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,LevelTileExtrusion(i,j),-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileTexture(i,j),0,1,LevelTileRotation(i,j),8)
			AddVertex (LevelSurface(j),i,LevelTileExtrusion(i,j),-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileTexture(i,j),1,1,LevelTileRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,LevelTileExtrusion(i,j),-j-1,ChunkTileU,ChunkTileV)
			
			AddTriangle (LevelSurface(j),i*20+0,i*20+1,i*20+2)
			AddTriangle (LevelSurface(j),i*20+1,i*20+3,i*20+2)
			
			; north face
			If j=0
				z#=0.0
			Else
				z#=LevelTileExtrusion(i,j-1)
			EndIf
			CalculateUV(LevelTileSideTexture(i,j),0,0,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,LevelTileExtrusion(i,j),-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),1,0,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i,LevelTileExtrusion(i,j),-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),z,1,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,0,-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),1,1,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i,z,-j,ChunkTileU,ChunkTileV)
			
			AddTriangle (LevelSurface(j),i*20+4,i*20+5,i*20+6)
			AddTriangle (LevelSurface(j),i*20+5,i*20+7,i*20+6)
			
			
			; east face
			CalculateUV(LevelTileSideTexture(i,j),0,0,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,LevelTileExtrusion(i,j),-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),1,0,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,LevelTileExtrusion(i,j),-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),0,1,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,0,-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),1,1,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,0,-j,ChunkTileU,ChunkTileV)
			
			AddTriangle (LevelSurface(j),i*20+8,i*20+9,i*20+10)
			AddTriangle (LevelSurface(j),i*20+9,i*20+11,i*20+10)
			
			; south face
			If j=99
				z#=0.0
			Else
				z#=LevelTileExtrusion(i,j+1)
			EndIf
			CalculateUV(LevelTileSideTexture(i,j),0,0,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i,LevelTileExtrusion(i,j),-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),1,0,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,LevelTileExtrusion(i,j),-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),0,1,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i,z,-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),1,1,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i+1,z,-j-1,ChunkTileU,ChunkTileV)
			
			AddTriangle (LevelSurface(j),i*20+12,i*20+13,i*20+14)
			AddTriangle (LevelSurface(j),i*20+13,i*20+15,i*20+14)
			
			; west face
			CalculateUV(LevelTileSideTexture(i,j),0,0,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i,LevelTileExtrusion(i,j),-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),1,0,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i,LevelTileExtrusion(i,j),-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),0,1,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i,0,-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTileSideTexture(i,j),1,1,LevelTileSideRotation(i,j),8)
			AddVertex (LevelSurface(j),i,0,-j-1,ChunkTileU,ChunkTileV)
			
			AddTriangle (LevelSurface(j),i*20+16,i*20+17,i*20+18)
			AddTriangle (LevelSurface(j),i*20+17,i*20+19,i*20+18)


			
			; replicating the behavior of in-game vertex normals
			For k=0 To 19
                VertexNormal LevelSurface(j),i*20+k,0.0,1.0,0.0
            Next
			
		Next
		;UpdateNormals LevelMesh(j)
		
		;EntityTexture LevelMesh(j),LevelTexture
	Next
	
	; water
	For j=0 To LevelHeight-1
		ClearSurface WaterSurface(j)
		For i=0 To LevelWidth-1
			; top face
			CalculateUV(WaterTileTexture(i,j),0,0,WaterTileRotation(i,j),4)
			AddVertex (WaterSurface(j),i,WaterTileHeight(i,j),-j,ChunkTileU,ChunkTileV)
			CalculateUV(WaterTileTexture(i,j),1,0,WaterTileRotation(i,j),4)
			AddVertex (WaterSurface(j),i+1,WaterTileHeight(i,j),-j,ChunkTileU,ChunkTileV)
			CalculateUV(WaterTileTexture(i,j),0,1,WaterTileRotation(i,j),4)
			AddVertex (WaterSurface(j),i,WaterTileHeight(i,j),-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(WaterTileTexture(i,j),1,1,WaterTileRotation(i,j),4)
			AddVertex (WaterSurface(j),i+1,WaterTileHeight(i,j),-j-1,ChunkTileU,ChunkTileV)
			
			AddTriangle (WaterSurface(j),i*4+0,i*4+1,i*4+2)
			AddTriangle (WaterSurface(j),i*4+1,i*4+3,i*4+2)
			
			VertexColor WaterSurface(j),i*4+0,0,0,0
			VertexColor WaterSurface(j),i*4+1,0,0,0
			VertexColor WaterSurface(j),i*4+2,0,0,0
			VertexColor WaterSurface(j),i*4+3,0,0,0
		Next
		UpdateNormals WaterMesh(j)
		EntityTexture WaterMesh(j),WaterTexture
	
	Next

End Function



Function ReBuildLevelModel()

	;For i=0 To 99
	;	FreeEntity LevelMesh(i)
	;	FreeEntity WaterMesh(i)
	;	FreeEntity LogicMesh(i)
	;Next
	
;	For j=0 To (LevelHeight-1)/ChunkSize
;		For i=0 To (LevelWidth-1)/ChunkSize
;			CreateLevelChunk(i*(ChunkSize)-1,j*(ChunkSize)-1)
;			CurrentLevelChunk=CurrentLevelChunk+1
;		Next
;	Next

	For i=0 To CurrentLevelChunk-1
		FreeEntity LevelEntity(i)
		FreeEntity WaterEntity(i)
	Next

	BuildLevelModel()
	
End Function



Function BuildLevelModel()

	CreateLevel()

	
	For i=0 To 99
;		LevelMesh(i)=CreateMesh()
;		LevelSurface(i)=CreateSurface(LevelMesh(i))
;		EntityFX LevelMesh(i),2
;		
;		Watermesh(i)=CreateMesh()
;		Watersurface(i)=CreateSurface(Watermesh(i))
;		EntityAlpha WaterMesh(i),.5
;		EntityFX WaterMesh(i),2

		; For some reason, this appears to do nothing???
		;If WaterGlow=True 
		;	EntityBlend WaterMesh(i),3
		;Else 
		;	EntityBlend WaterMesh(i),1
		;EndIf
		;If WaterTransparent=True 
		;	EntityAlpha WaterMesh(i),.5
		;Else
		;	EntityAlpha WaterMesh(i),1
		;EndIf
		
		
		Logicmesh(i)=CreateMesh()
		Logicsurface(i)=CreateSurface(Logicmesh(i))
		EntityFX LogicMesh(i),2


	Next
	
	
	
	
	;BlocksForLevelAndWater()
	

	
	; logic
	For j=0 To LevelHeight-1
		ClearSurface LogicSurface(j)
		For i=0 To LevelWidth-1
		
			;If LevelTileLogic(i,j)=1 Or LevelTileLogic(i,j)=2 Or LevelTileLogic(i,j)=11 Or LevelTileLogic(i,j)=12 Or LevelTileLogic(i,j)=13
			If LevelTileLogicHasVisuals(i,j)
				nologicshow=0
			Else
				nologicshow=-300
			EndIf
			; top face
			; pick height of logic mesh as just over maxi(water,tile)
			If WaterTileHeight(i,j)>LevelTileExtrusion(i,j)
				height#=WaterTileHeight(i,j)+0.05
			Else
				height#=LevelTileExtrusion(i,j)+0.05
			EndIf
			AddVertex (LogicSurface(j),i+nologicshow,height,-j)
			AddVertex (LogicSurface(j),i+1+nologicshow,height,-j)
			AddVertex (LogicSurface(j),i+nologicshow,height,-j-1)
			AddVertex (LogicSurface(j),i+1+nologicshow,height,-j-1)
						
			AddTriangle (LogicSurface(j),i*4+0,i*4+1,i*4+2)
			AddTriangle (LogicSurface(j),i*4+1,i*4+3,i*4+2)
			
			ColorLevelTileLogic(i,j)

		Next
		UpdateNormals LogicMesh(j)
		
		If ShowLogicMesh=True 
			ShowEntity LogicMesh(j)
		Else
			HideEntity LogicMesh(j)
		EndIf
;		If ShowLevelMesh=True 
;			ShowEntity LevelMesh(j)
;		Else
;			HideEntity LevelMesh(j)
;		EndIf

		
	Next

	
		
	
End Function

Function ChangeLevelTile(i,j,update)

	If i<0
		Return
	ElseIf i>LevelWidth-1
		Return
	EndIf
	If j<0
		Return
	ElseIf j>LevelHeight-1
		Return
	EndIf
	
	
	; The Tile
	If CurrentTileTextureUse=True
		
		LevelTileTexture(i,j)=CurrentTileTexture ; corresponding to squares in LevelTexture
		LevelTileRotation(i,j)=CurrentTileRotation ; 0-3 , and 4-7 for "flipped"
	EndIf
	If CurrentTileSideTextureUse=True
		LevelTileSideTexture(i,j)=CurrentTileSideTexture ; texture for extrusion walls
		LevelTileSideRotation(i,j)=CurrentTileSideRotation ; 0-3 , and 4-7 for "flipped"
	EndIf
	If CurrentTileRandomUse=True
		LevelTileRandom#(i,j)=CurrentTileRandom ; random height pertubation of tile
	EndIf
	If CurrentTileHeightUse=True
		LevelTileHeight#(i,j)=CurrentTileHeight ; height of "center" - e.g. to make ditches and hills
	EndIf
	If CurrentTileExtrusionUse=True
		LevelTileExtrusion#(i,j)=CurrentTileExtrusion; extrusion with walls around it 
	EndIf
	If CurrentTileRoundingUse=True
		LevelTileRounding(i,j)=CurrentTileRounding; 0-no, 1-yes: are floors rounded if on a drop-off corner
	EndIf
	If CurrentTileEdgeRandomUse=True
		LevelTileEdgeRandom(i,j)=CurrentTileEdgeRandom; 0-no, 1-yes: are edges rippled
	EndIf
	If CurrentTileLogicUse=True
		LevelTileLogic(i,j)=CurrentTileLogic
	EndIf
;	If update=True 
;		UpdateLevelTile(i,j)
;	
;		; Possibly update surrounding tiles
;		If i>0
;			If LevelTileExtrusion(i-1,j)>=LevelTileExtrusion(i,j) UpdateLevelTile(i-1,j)
;		EndIf
;		If i<LevelWidth-1
;			If LevelTileExtrusion(i+1,j)>=LevelTileExtrusion(i,j) UpdateLevelTile(i+1,j)
;		EndIf
;		If j>0
;			If LevelTileExtrusion(i,j-1)>=LevelTileExtrusion(i,j) UpdateLevelTile(i,j-1)
;		EndIf
;		If j<LevelHeight-1
;			If LevelTileExtrusion(i,j+1)>=LevelTileExtrusion(i,j) UpdateLevelTile(i,j+1)
;		EndIf
;	EndIf
			
			
	; the water
	If CurrentWaterTileUse=True 
		WaterTileTexture(i,j)=CurrentWaterTileTexture
		WaterTileRotation(i,j)=CurrentWaterTileRotation
	EndIf
	If CurrentWaterTileHeightUse=True WaterTileHeight(i,j)=CurrentWaterTileHeight
	If CurrentWaterTileTurbulenceUse=True WaterTileTurbulence(i,j)=CurrentWaterTileTurbulence
	If update=True
		;UpdateWaterTile(i,j)
		UpdateLogicTile(i,j)
	EndIf


End Function

Function GrabLevelTile(i,j)

	If i<0
		i=0
	ElseIf i>LevelWidth-1
		i=LevelWidth-1
	EndIf
	If j<0
		j=0
	ElseIf j>LevelHeight-1
		j=LevelHeight-1
	EndIf
	
	CurrentTileTexture=LevelTileTexture(i,j) ; corresponding to squares in LevelTexture
	CurrentTileRotation=LevelTileRotation(i,j) ; 0-3 , and 4-7 for "flipped"
	CurrentTileSideTexture=LevelTileSideTexture(i,j) ; texture for extrusion walls
	CurrentTileSideRotation=LevelTileSideRotation(i,j) ; 0-3 , and 4-7 for "flipped"
	CurrentTileRandom=LevelTileRandom#(i,j) ; random height pertubation of tile
	CurrentTileHeight=LevelTileHeight#(i,j) ; height of "center" - e.g. to make ditches and hills
	CurrentTileExtrusion=LevelTileExtrusion#(i,j); extrusion with walls around it 
	CurrentTileRounding=LevelTileRounding(i,j); 0-no, 1-yes: are floors rounded if on a drop-off corner
	CurrentTileEdgeRandom=LevelTileEdgeRandom(i,j); 0-no, 1-yes: are edges rippled
	CurrentTileLogic=LevelTileLogic(i,j)
	
	CurrentTileRandom2=CurrentTileRandom*1000
	CurrentTileHeight2=CurrentTileHeight*100
	CurrentTileExtrusion2=CurrentTileExtrusion*100
	CurrentTileLogic2=CurrentTileLogic*10
	
	CurrentWaterTileTexture=WaterTileTexture(i,j)
	CurrentWaterTileRotation=WaterTileRotation(i,j)
	CurrentWaterTileHeight=WaterTileHeight(i,j)
	CurrentWaterTileTurbulence=WaterTileTurbulence(i,j)
	CurrentWaterTileHeight2=CurrentWaterTileHeight*100
	CurrentWaterTileTurbulence2=CurrentWaterTileTurbulence*100


	BuildCurrentTileModel()

End Function

Function GrabLevelTileFromBrush(i,j)

	If i<0
		i=0
	ElseIf i>LevelWidth-1
		i=LevelWidth-1
	EndIf
	If j<0
		j=0
	ElseIf j>LevelHeight-1
		j=LevelHeight-1
	EndIf
	
	CurrentTileTexture=BrushLevelTileTexture(i,j) ; corresponding to squares in LevelTexture
	CurrentTileRotation=BrushLevelTileRotation(i,j) ; 0-3 , and 4-7 for "flipped"
	CurrentTileSideTexture=BrushLevelTileSideTexture(i,j) ; texture for extrusion walls
	CurrentTileSideRotation=BrushLevelTileSideRotation(i,j) ; 0-3 , and 4-7 for "flipped"
	CurrentTileRandom=BrushLevelTileRandom#(i,j) ; random height pertubation of tile
	CurrentTileHeight=BrushLevelTileHeight#(i,j) ; height of "center" - e.g. to make ditches and hills
	CurrentTileExtrusion=BrushLevelTileExtrusion#(i,j); extrusion with walls around it 
	CurrentTileRounding=BrushLevelTileRounding(i,j); 0-no, 1-yes: are floors rounded if on a drop-off corner
	CurrentTileEdgeRandom=BrushLevelTileEdgeRandom(i,j); 0-no, 1-yes: are edges rippled
	CurrentTileLogic=BrushLevelTileLogic(i,j)
	
	CurrentTileRandom2=CurrentTileRandom*1000
	CurrentTileHeight2=CurrentTileHeight*100
	CurrentTileExtrusion2=CurrentTileExtrusion*100
	CurrentTileLogic2=CurrentTileLogic*10
	
	CurrentWaterTileTexture=BrushWaterTileTexture(i,j)
	CurrentWaterTileRotation=BrushWaterTileRotation(i,j)
	CurrentWaterTileHeight=BrushWaterTileHeight(i,j)
	CurrentWaterTileTurbulence=BrushWaterTileTurbulence(i,j)
	CurrentWaterTileHeight2=CurrentWaterTileHeight*100
	CurrentWaterTileTurbulence2=CurrentWaterTileTurbulence*100


	BuildCurrentTileModel()

End Function

Function CopyLevelTileToBrush(i,j,iDest,jDest)

	If i<0
		i=0
	ElseIf i>LevelWidth-1
		i=LevelWidth-1
	EndIf
	If j<0
		j=0
	ElseIf j>LevelHeight-1
		j=LevelHeight-1
	EndIf
	
	BrushLevelTileTexture(iDest,jDest)=LevelTileTexture(i,j) ; corresponding to squares in LevelTexture
	BrushLevelTileRotation(iDest,jDest)=LevelTileRotation(i,j) ; 0-3 , and 4-7 for "flipped"
	BrushLevelTileSideTexture(iDest,jDest)=LevelTileSideTexture(i,j) ; texture for extrusion walls
	BrushLevelTileSideRotation(iDest,jDest)=LevelTileSideRotation(i,j) ; 0-3 , and 4-7 for "flipped"
	BrushLevelTileRandom(iDest,jDest)=LevelTileRandom#(i,j) ; random height pertubation of tile
	BrushLevelTileHeight(iDest,jDest)=LevelTileHeight#(i,j) ; height of "center" - e.g. to make ditches and hills
	BrushLevelTileExtrusion(iDest,jDest)=LevelTileExtrusion#(i,j); extrusion with walls around it 
	BrushLevelTileRounding(iDest,jDest)=LevelTileRounding(i,j); 0-no, 1-yes: are floors rounded if on a drop-off corner
	BrushLevelTileEdgeRandom(iDest,jDest)=LevelTileEdgeRandom(i,j); 0-no, 1-yes: are edges rippled
	BrushLevelTileLogic(iDest,jDest)=LevelTileLogic(i,j)
	
	BrushWaterTileTexture(iDest,jDest)=WaterTileTexture(i,j)
	BrushWaterTileRotation(iDest,jDest)=WaterTileRotation(i,j)
	BrushWaterTileHeight(iDest,jDest)=WaterTileHeight(i,j)
	BrushWaterTileTurbulence(iDest,jDest)=WaterTileTurbulence(i,j)

End Function

Function SetLevelTileAsTarget(i,j)
	
	TargetTileTexture=LevelTileTexture(i,j) ; corresponding to squares in LevelTexture
	TargetTileRotation=LevelTileRotation(i,j) ; 0-3 , and 4-7 for "flipped"
	TargetTileSideTexture=LevelTileSideTexture(i,j) ; texture for extrusion walls
	TargetTileSideRotation=LevelTileSideRotation(i,j) ; 0-3 , and 4-7 for "flipped"
	TargetTileRandom=LevelTileRandom#(i,j) ; random height pertubation of tile
	TargetTileHeight=LevelTileHeight#(i,j) ; height of "center" - e.g. to make ditches and hills
	TargetTileExtrusion=LevelTileExtrusion#(i,j); extrusion with walls around it 
	TargetTileRounding=LevelTileRounding(i,j); 0-no, 1-yes: are floors rounded if on a drop-off corner
	TargetTileEdgeRandom=LevelTileEdgeRandom(i,j); 0-no, 1-yes: are edges rippled
	TargetTileLogic=LevelTileLogic(i,j)
	
	;TargetTileRandom2=TargetTileRandom*1000
	;TargetTileHeight2=TargetTileHeight*100
	;TargetTileExtrusion2=TargetTileExtrusion*100
	;TargetTileLogic2=TargetTileLogic*10
	
	TargetWaterTileTexture=WaterTileTexture(i,j)
	TargetWaterTileRotation=WaterTileRotation(i,j)
	TargetWaterTileHeight=WaterTileHeight(i,j)
	TargetWaterTileTurbulence=WaterTileTurbulence(i,j)
	;TargetWaterTileHeight2=TargetWaterTileHeight*100
	;TargetWaterTileTurbulence2=TargetWaterTileTurbulence*100

End Function

Function LevelTileMatchesTarget(i,j)

	If TargetTileTextureUse And TargetTileTexture<>LevelTileTexture(i,j)
		Return False
	EndIf
	;If TargetTileRotation<>LevelTileRotation(i,j)
	;	Return False
	;EndIf
	If TargetTileSideTextureUse And TargetTileSideTexture<>LevelTileSideTexture(i,j)
		Return False
	EndIf
	;If TargetTileSideRotation<>LevelTileSideRotation(i,j)
	;	Return False
	;EndIf
	If TargetTileRandomUse And TargetTileRandom<>LevelTileRandom#(i,j)
		Return False
	EndIf
	If TargetTileHeightUse And TargetTileHeight<>LevelTileHeight#(i,j)
		Return False
	EndIf
	If TargetTileExtrusionUse And TargetTileExtrusion<>LevelTileExtrusion#(i,j)
		Return False
	EndIf
	If TargetTileRoundingUse And TargetTileRounding<>LevelTileRounding(i,j)
		Return False
	EndIf
	If TargetTileEdgeRandomUse And TargetTileEdgeRandom<>LevelTileEdgeRandom(i,j)
		Return False
	EndIf
	If TargetTileLogicUse And TargetTileLogic<>LevelTileLogic(i,j)
		Return False
	EndIf
	
	If TargetWaterTileUse And TargetWaterTileTexture<>WaterTileTexture(i,j)
		Return False
	EndIf
	;If TargetWaterTileRotation<>WaterTileRotation(i,j)
	;	Return False
	;EndIf
	If TargetWaterTileHeightUse And TargetWaterTileHeight<>WaterTileHeight(i,j)
		Return False
	EndIf
	If TargetWaterTileTurbulenceUse And TargetWaterTileTurbulence<>WaterTileTurbulence(i,j)
		Return False
	EndIf
	
	Return True

End Function

Function SetXtrudeLogics(LessThanZero,EqualToZero,GreaterThanZero)

	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			If LevelTileExtrusion#(i,j)<0.0
				LevelTileLogic(i,j)=LessThanZero
			ElseIf LevelTileExtrusion#(i,j)=0.0
				LevelTileLogic(i,j)=EqualToZero
			ElseIf LevelTileExtrusion#(i,j)>0.0
				LevelTileLogic(i,j)=GreaterThanZero
			EndIf
			UpdateLogicTile(i,j)
		Next
	Next

End Function

Function LoadTilePreset()
	
	Filename$="Data\Editor\TilePresets\"+TilePresetCategoryName$(CurrentTilePresetCategory)+"\"+TilePresetTileName$(CurrentTilePresetTile)
	file=ReadFile(filename$)
	CurrentTileTexture=ReadInt(file)
	CurrentTileRotation=ReadInt(file) 
	CurrentTileSideTexture=ReadInt(file)
	CurrentTileSideRotation=ReadInt(file)
	CurrentTileRandom=ReadFloat(file)
	CurrentTileHeight=ReadFloat(file)
	CurrentTileExtrusion=ReadFloat(file)
	CurrentTileRounding=ReadInt(file)
	CurrentTileEdgeRandom=ReadInt(file)
	CurrentTileLogic=ReadInt(file)
	
	
	CurrentTileRandom2=CurrenTileRandom*1000
	CurrentTileHeight2=CurrentTileHeight*100
	CurrentTileExtrusion2=CurrentTileExtrusion*100
	CurrentTileLogic2=CurrentTileLogic*10
	
	CurrentWaterTileTexture=ReadInt(file)
	CurrentWaterTileRotation=ReadInt(file)
	CurrentWaterTileHeight=ReadFloat(file)
	CurrentWaterTileTurbulence=ReadFloat(file)
	CurrentWaterTileHeight2=CurrentWaterTileHeight*100
	CurrentWaterTileTurbulence2=CurrentWaterTileTurbulence*100


	BuildCurrentTileModel()

End Function


Function SaveTilePreset()
	FlushKeys
	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
	Filename$=Input ("FileName: ")
	file=WriteFile ("data\editor\tilepresets\"+filename$+".tp1")
	
	WriteInt file,CurrentTileTexture
	WriteInt file,CurrentTileRotation
	WriteInt file,CurrentTileSideTexture
	WriteInt file,CurrentTileSideRotation
	WriteFloat file,CurrentTileRandom
	WriteFloat file,CurrentTileHeight
	WriteFloat file,CurrentTileExtrusion
	WriteInt file,CurrentTileRounding
	WriteInt file,CurrentTileEdgeRandom
	WriteInt file,CurrentTileLogic
	
	
	WriteInt file,CurrentWaterTileTexture
	WriteInt file,CurrentWaterTileRotation
	WriteFloat file,CurrentWaterTileHeight
	WriteFloat file,CurrentWaterTileTurbulence

	CloseFile file
End Function

Function LoadObjectPreset()
	
	Filename$="Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(CurrentObjectPresetCategory)+"\"+ObjectPresetObjectName$(CurrentObjectPresetObject)

	file=ReadFile(filename$)
	
	CurrentObjectModelName$=ReadString$(file)
	CurrentObjectTextureName$=ReadString$(file)
	CurrentObjectXScale#=ReadFloat(file)
	CurrentObjectYScale#=ReadFloat(file)
	CurrentObjectZScale#=ReadFloat(file)
	CurrentObjectXAdjust#=ReadFloat(file)
	CurrentObjectYAdjust#=ReadFloat(file)
	CurrentObjectZAdjust#=ReadFloat(file)
	CurrentObjectPitchAdjust#=ReadFloat(file)
	CurrentObjectYawAdjust#=ReadFloat(file)
	CurrentObjectRollAdjust#=ReadFloat(file)
	CurrentObjectX#=ReadFloat(file)
	CurrentObjectY#=ReadFloat(file)
	CurrentObjectZ#=ReadFloat(file)
	CurrentObjectOldX#=ReadFloat(file)
	CurrentObjectOldY#=ReadFloat(file)
	CurrentObjectOldZ#=ReadFloat(file)
	CurrentObjectDX#=ReadFloat(file)
	CurrentObjectDY#=ReadFloat(file)
	CurrentObjectDZ#=ReadFloat(file)
	CurrentObjectPitch#=ReadFloat(file)
	CurrentObjectYaw#=ReadFloat(file)
	CurrentObjectRoll#=ReadFloat(file)
	CurrentObjectPitch2#=ReadFloat(file)
	CurrentObjectYaw2#=ReadFloat(file)
	CurrentObjectRoll2#=ReadFloat(file)
	CurrentObjectXGoal#=ReadFloat(file)
	CurrentObjectYGoal#=ReadFloat(file)
	CurrentObjectZGoal#=ReadFloat(file)
	CurrentObjectMovementType=ReadInt(file)
	CurrentObjectMovementTypeData=ReadInt(file)
	CurrentObjectSpeed#=ReadFloat(file)
	CurrentObjectRadius#=ReadFloat(file)
	CurrentObjectRadiusType=ReadInt(file)
	CurrentObjectData10=ReadInt(file)
	CurrentObjectPushDX#=ReadFloat(file)
	CurrentObjectPushDY#=ReadFloat(file)
	CurrentObjectAttackPower=ReadInt(file)
	CurrentObjectDefensePower=ReadInt(file)
	CurrentObjectDestructionType=ReadInt(file)
	CurrentObjectID=ReadInt(file)
	CurrentObjectType=ReadInt(file)
	CurrentObjectSubType=ReadInt(file)
	CurrentObjectActive=ReadInt(file)
	CurrentObjectLastActive=ReadInt(file)
	CurrentObjectActivationType=ReadInt(file)
	CurrentObjectActivationSpeed=ReadInt(file)
	CurrentObjectStatus=ReadInt(file)
	CurrentObjectTimer=ReadInt(file)
	CurrentObjectTimerMax1=ReadInt(file)
	CurrentObjectTimerMax2=ReadInt(file)
	CurrentObjectTeleportable=ReadInt(file)
	CurrentObjectButtonPush=ReadInt(file)
	CurrentObjectWaterReact=ReadInt(file)
	CurrentObjectTelekinesisable=ReadInt(file)
	CurrentObjectFreezable=ReadInt(file)
	CurrentObjectReactive=ReadInt(file)
	CurrentObjectChild=ReadInt(file)
	CurrentObjectParent=ReadInt(file)
	For i=0 To 9
		CurrentObjectData(i)=ReadInt(file)
	Next
	For i=0 To 3
		CurrentObjectTextData$(i)=ReadString$(file)
	Next
	CurrentObjectTalkable=ReadInt(file)
	CurrentObjectCurrentAnim=ReadInt(file)
	CurrentObjectStandardAnim=ReadInt(file)
	CurrentObjectTileX=ReadInt(file)
	CurrentObjectTileY=ReadInt(file)
	CurrentObjectTileX2=ReadInt(file)
	CurrentObjectTileY2=ReadInt(file)
	CurrentObjectMovementTimer=ReadInt(file)
	CurrentObjectMovementSpeed=ReadInt(file)
	CurrentObjectMoveXGoal=ReadInt(file)
	CurrentObjectMoveYGoal=ReadInt(file)
	CurrentObjectTileTypeCollision=ReadInt(file)
	CurrentObjectObjectTypeCollision=ReadInt(file)
	CurrentObjectCaged=ReadInt(file)
	CurrentObjectDead=ReadInt(file)
	CurrentObjectDeadTimer=ReadInt(file)
	CurrentObjectExclamation=ReadInt(file)
	CurrentObjectShadow=ReadInt(file)
	;CurrentObjectLinked=ReadInt(file)
	ReadInt(file)
	CurrentObjectLinked=-1
	;CurrentObjectLinkBack=ReadInt(file)
	ReadInt(file)
	CurrentObjectLinkBack=-1
	CurrentObjectFlying=ReadInt(file)
	CurrentObjectFrozen=ReadInt(file)
	CurrentObjectIndigo=ReadInt(file)
	CurrentObjectFutureInt24=ReadInt(file)
	CurrentObjectFutureInt25=ReadInt(file)

	CurrentObjectScaleAdjust=ReadFloat(file)
	CurrentObjectScaleXAdjust=ReadFloat(file)
	CurrentObjectScaleYAdjust=ReadFloat(file)
	CurrentObjectScaleZAdjust=ReadFloat(file)
	CurrentObjectScaleXAdjust=1.0
	CurrentObjectScaleYAdjust=1.0
	CurrentObjectScaleZAdjust=1.0
	CurrentObjectFutureFloat5=ReadFloat(file)
	CurrentObjectFutureFloat6=ReadFloat(file)
	CurrentObjectFutureFloat7=ReadFloat(file)
	CurrentObjectFutureFloat8=ReadFloat(file)
	CurrentObjectFutureFloat9=ReadFloat(file)
	CurrentObjectFutureFloat10=ReadFloat(file)
	CurrentObjectFutureString1$=ReadString(file)
	CurrentObjectFutureString2$=ReadString(file)
	
	
	
	NofObjectAdjusters=0
	
	; this line can be commented out now that all object adjusters are the same
	;ObjectAdjusterStart=0
	
	NofWopAdjusters=0
	
	For i=0 To 30
		If Eof(file)=False
		;	ObjectAdjuster$(i)=ReadString$(file)
		;	NofObjectAdjusters=NofObjectAdjusters+1
			ObjectAdjusterWop$(i)=ReadString$(file)
			NofWopAdjusters=NofWopAdjusters+1
		;Else
		;	ObjectAdjuster$(i)=""
		EndIf
	Next
	
	; Add these adjusters to every object.
	AddAdjuster("ID")
	AddAdjuster("Active")
	AddAdjuster("ActivationType")
	AddAdjuster("ActivationSpeed")
	AddAdjuster("Type")
	AddAdjuster("SubType")
	AddAdjuster("ModelName")
	AddAdjuster("TextureName")
	AddAdjuster("MovementSpeed")
	AddAdjuster("MovementType")
	;AddAdjuster("MovementTypeData")
	;AddAdjuster("MovementTimer")
	AddAdjuster("TileTypeCollision")
	AddAdjuster("ObjectTypeCollision")
	AddAdjuster("ButtonPush")
	AddAdjuster("XAdjust")
	AddAdjuster("YAdjust")
	AddAdjuster("ZAdjust")
	AddAdjuster("YawAdjust")
	AddAdjuster("RollAdjust")
	AddAdjuster("PitchAdjust")
	AddAdjuster("XScale")
	AddAdjuster("YScale")
	AddAdjuster("ZScale")
	AddAdjuster("ScaleAdjust")
	AddAdjuster("Data0")
	AddAdjuster("Data1")
	AddAdjuster("Data2")
	AddAdjuster("Data3")
	AddAdjuster("Data4")
	AddAdjuster("Data5")
	AddAdjuster("Data6")
	AddAdjuster("Data7")
	AddAdjuster("Data8")
	AddAdjuster("Data9")
	AddAdjuster("ObjectTextData0")
	AddAdjuster("ObjectTextData1")
	AddAdjuster("Talkable")
	AddAdjuster("Exclamation")
	AddAdjuster("Timer")
	AddAdjuster("TimerMax1")
	AddAdjuster("TimerMax2")
	AddAdjuster("DefensePower")
	;AddAdjuster("WaterReact")
	AddAdjuster("DX")
	AddAdjuster("DY")
	AddAdjuster("Frozen")
	AddAdjuster("Teleportable")
	AddAdjuster("Linked")
	AddAdjuster("LinkBack")
	;AddAdjuster("Parent")
	;AddAdjuster("Child")
	AddAdjuster("MoveXGoal")
	AddAdjuster("MoveYGoal")
	AddAdjuster("Dead")
	AddAdjuster("Caged")
	AddAdjuster("Indigo")
	AddAdjuster("DestructionType")
	;AddAdjuster("Flying")
	;AddAdjuster("MovementTimer")
	AddAdjuster("Status")

	
	CloseFile file
	
	
	BuildCurrentObjectModel()
	

End Function


Function AddAdjuster(Name$)

	ObjectAdjuster$(NofObjectAdjusters)=Name$
	NofObjectAdjusters=NofObjectAdjusters+1

End Function


Function ShowMessage(message$, milliseconds)

		Locate 0,0
		Color 0,0,0
		Rect 0,0,500,40,True
		Color 255,255,255
		Print message$
		Delay milliseconds

End Function


; for preventing several of the same message from pausing the same frame for a long time
Function ShowMessageOnce(message$, milliseconds)

	If ShowingError=False
		ShowingError=True ; will reset at the start of every frame
		ShowMessage(message$, milliseconds)
	EndIf

End Function


Function PlaceObject(x#,y#)

	floorx=Floor(x)
	floory=Floor(y)
	
	If floorx<0 Or floory<0 Or floorx>LevelWidth-1 Or floory>LevelHeight-1
		; don't place anything
		Return
	EndIf

	If NofObjects>999
		ShowMessageOnce("1000 object limit reached; refusing to place any more", 1000)
		Return
	EndIf


	; first check if another object exists on the same tile
	;For i=0 To NofObjects-1
	;	If ObjectTileX(i)=Floor(x) And ObjectTileY(i)=Floor(y)
	;		DeleteObject(i)
	;		i=i-1
	;	EndIf
	;Next
	
	ObjectHatEntity(NofObjects)=0
	ObjectHatTexture(NofObjects)=0
	ObjectAccEntity(NofObjects)=0
	ObjectAccTexture(NofObjects)=0


	; And Place
	ObjectTileX(NofObjects)=floorx
	ObjectTileX2(NofObjects)=floorx
	ObjectTileY(NofObjects)=floory
	ObjectTileY2(NofObjects)=floory



	ObjectModelName$(NofObjects)=CurrentObjectModelName$
	ObjectTextureName$(NofObjects)=CurrentObjectTextureName$
	
	If RandomXScale
		CurrentObjectXScale#=Rnd(RandomXScaleMin#,RandomXScaleMax#)
	EndIf
	If RandomYScale
		CurrentObjectYScale#=Rnd(RandomYScaleMin#,RandomYScaleMax#)
	EndIf
	If RandomZScale
		CurrentObjectZScale#=Rnd(RandomZScaleMin#,RandomZScaleMax#)
	EndIf
	
	ObjectXScale#(NofObjects)=CurrentObjectXScale#
	ObjectZScale#(NofObjects)=CurrentObjectZScale#
	ObjectYScale#(NofObjects)=CurrentObjectYScale#
	
	If RandomXAdjust
		CurrentObjectXAdjust#=Rnd(RandomXAdjustMin#,RandomXAdjustMax#)
	EndIf
	If RandomYAdjust
		CurrentObjectYAdjust#=Rnd(RandomYAdjustMin#,RandomYAdjustMax#)
	EndIf
	If RandomZAdjust
		CurrentObjectZAdjust#=Rnd(RandomZAdjustMin#,RandomZAdjustMax#)
	EndIf
	
	ObjectXAdjust#(NofObjects)=CurrentObjectXAdjust#
	ObjectZAdjust#(NofObjects)=CurrentObjectZAdjust#
	ObjectYAdjust#(NofObjects)=CurrentObjectYAdjust#
	
	If RandomPitchAdjust
		CurrentObjectPitchAdjust#=Rnd(RandomPitchAdjustMin,RandomPitchAdjustMax)
	EndIf
	If RandomYawAdjust
		CurrentObjectYawAdjust#=Rnd(RandomYawAdjustMin,RandomYawAdjustMax)
	EndIf
	If RandomRollAdjust
		CurrentObjectRollAdjust#=Rnd(RandomRollAdjustMin,RandomRollAdjustMax)
	EndIf
	
	ObjectPitchAdjust#(NofObjects)=CurrentObjectPitchAdjust#
	ObjectYawAdjust#(NofObjects)=CurrentObjectYawAdjust#
	ObjectRollAdjust#(NofObjects)=CurrentObjectRollAdjust#
	
	If CurrentObjectType=10 And CurrentObjectSubType=1 ; house-door
		If CurrentObjectYawAdjust=90
			ObjectX#(NofObjects)=x+0.5+CurrentObjectX#
			ObjectY#(NofObjects)=y+1.0+CurrentObjectY#
		Else If CurrentObjectYawAdjust=270
			ObjectX#(NofObjects)=x+0.5+CurrentObjectX#
			ObjectY#(NofObjects)=y+CurrentObjectY#
		Else If CurrentObjectYawAdjust=45
			ObjectX#(NofObjects)=x-0.1+CurrentObjectX#
			ObjectY#(NofObjects)=y+0.6+CurrentObjectY#
		Else If CurrentObjectYawAdjust=315
			ObjectX#(NofObjects)=x+0.40+CurrentObjectX#
			ObjectY#(NofObjects)=y-0.1+CurrentObjectY#

			
		Else
			ObjectX#(NofObjects)=x-0.00+CurrentObjectX#
			ObjectY#(NofObjects)=y+0.5+CurrentObjectY#

		EndIf
	Else If CurrentObjectType=10 And CurrentObjectSubType=2 ; dungeon-door
		If CurrentObjectYawAdjust=0
			ObjectX#(NofObjects)=x+CurrentObjectX#
			ObjectY#(NofObjects)=y+1.0+CurrentObjectY#
		Else If CurrentObjectYawAdjust=90
			ObjectX#(NofObjects)=x+1.0+CurrentObjectX#
			ObjectY#(NofObjects)=y+1.0+CurrentObjectY#
		Else If CurrentObjectYawAdjust=180
			ObjectX#(NofObjects)=x+1.0+CurrentObjectX#
			ObjectY#(NofObjects)=y+CurrentObjectY#
		Else
			ObjectX#(NofObjects)=x+0.0+CurrentObjectX#
			ObjectY#(NofObjects)=y+0.0+CurrentObjectY#

		EndIf
	Else If CurrentObjectType=10 And CurrentObjectSubType=3 ; townhouse1-door
		If CurrentObjectYawAdjust=90
			ObjectX#(NofObjects)=x+0.6+CurrentObjectX#
			ObjectY#(NofObjects)=y+1.0+CurrentObjectY#
		Else If CurrentObjectYawAdjust=270
			ObjectX#(NofObjects)=x+0.40+CurrentObjectX#
			ObjectY#(NofObjects)=y+CurrentObjectY#
		Else If CurrentObjectYawAdjust=45
			ObjectX#(NofObjects)=x-0.338+CurrentObjectX#
			ObjectY#(NofObjects)=y+0.342+CurrentObjectY#
		Else If CurrentObjectYawAdjust=315
			ObjectX#(NofObjects)=x+0.637+CurrentObjectX#
			ObjectY#(NofObjects)=y-0.361+CurrentObjectY#

			
		Else
			ObjectX#(NofObjects)=x-0.00+CurrentObjectX#
			ObjectY#(NofObjects)=y+0.6+CurrentObjectY#

		EndIf

	Else If CurrentObjectType=10 And CurrentObjectSubType=4 ; townhouse2-door
		If CurrentObjectYawAdjust=90
			ObjectX#(NofObjects)=x+0.1+CurrentObjectX#
			ObjectY#(NofObjects)=y+1.0+CurrentObjectY#
		Else If CurrentObjectYawAdjust=270
			ObjectX#(NofObjects)=x+0.90+CurrentObjectX#
			ObjectY#(NofObjects)=y+CurrentObjectY#
		Else If CurrentObjectYawAdjust=45
			ObjectX#(NofObjects)=x-0.338-.35+CurrentObjectX#
			ObjectY#(NofObjects)=y+0.342-.35+CurrentObjectY#
		Else If CurrentObjectYawAdjust=315
			ObjectX#(NofObjects)=x+0.637+.35+CurrentObjectX#
			ObjectY#(NofObjects)=y-0.361-.35+CurrentObjectY#

			
		Else
			ObjectX#(NofObjects)=x+0.00+CurrentObjectX#
			ObjectY#(NofObjects)=y+0.1+CurrentObjectY#

		EndIf





	Else
		ObjectX#(NofObjects)=x+0.5+CurrentObjectX#
		ObjectY#(NofObjects)=y+0.5+CurrentObjectY#
	EndIf
	ObjectZ#(NofObjects)=CurrentObjectZ#
	
	; extra adjustments	
	If CurrentObjectType=50 ; spellball
		CurrentObjectData(2)=ObjectTileX(NofObjects)
		CurrentObjectData(3)=ObjectTileY(NofObjects)
		CurrentObjectData(4)=ObjectTileX(NofObjects)
		CurrentObjectData(5)=ObjectTileY(NofObjects)
	EndIf
	
	
	ObjectOldX#(nofobjects)=-999
	ObjectOldY#(nofobjects)=-999
	ObjectOldZ#(nofobjects)=-999
	ObjectDX#(NofObjects)=CurrentObjectDX#
	ObjectDY#(NofObjects)=CurrentObjectDY#
	ObjectDZ#(NofObjects)=CurrentObjectDZ#
	ObjectPitch#(NofObjects)=CurrentObjectPitch#
	ObjectYaw#(NofObjects)=CurrentObjectYaw#
	ObjectRoll#(NofObjects)=CurrentObjectRoll#
	ObjectPitch2#(NofObjects)=CurrentObjectPitch2#
	ObjectYaw2#(NofObjects)=CurrentObjectYaw2#
	ObjectRoll2#(NofObjects)=CurrentObjectRoll2#
	ObjectXGoal#(NofObjects)=CurrentObjectXGoal#
	ObjectYGoal#(NofObjects)=CurrentObjectYGoal#
	ObjectZGoal#(NofObjects)=CurrentObjectZGoal#
	
	If RandomMovementType
		CurrentObjectMovementType=Rand(RandomMovementTypeMin,RandomMovementTypeMax)
	EndIf
	
	ObjectMovementType(NofObjects)=CurrentObjectMovementType
	ObjectMovementTypeData(NofObjects)=CurrentObjectMovementTypeData
	
	ObjectSpeed#(NofObjects)=CurrentObjectSpeed#
	ObjectRadius#(NofObjects)=CurrentObjectRadius#
	ObjectRadiusType(NofObjects)=CurrentObjectRadiusType
	ObjectData10(NofObjects)=CurrentObjectData10
	ObjectPushDX#(NofObjects)=CurrentObjectPushDX#
	ObjectPushDY#(NofObjects)=CurrentObjectPushDY#
	ObjectAttackPower(NofObjects)=CurrentObjectAttackPower
	
	If RandomDefensePower
		CurrentObjectDefensePower=Rand(RandomDefensePowerMin,RandomDefensePowerMax)
	EndIf
	
	ObjectDefensePower(NofObjects)=CurrentObjectDefensePower
	ObjectDestructionType(NofObjects)=CurrentObjectDestructionType
	
	If RandomID
		CurrentObjectID=Rand(RandomIDMin,RandomIDMax)
	EndIf
	
	ObjectID(NofObjects)=CurrentObjectID
	
	If RandomType
		CurrentObjectType=Rand(RandomTypeMin,RandomTypeMax)
	EndIf
	If RandomSubType
		CurrentObjectSubType=Rand(RandomSubTypeMin,RandomSubTypeMax)
	EndIf
	
	ObjectType(NofObjects)=CurrentObjectType
	ObjectSubType(NofObjects)=CurrentObjectSubType
	
	If RandomActive
		CurrentObjectActive=Rand(RandomActiveMin,RandomActiveMax)
	EndIf
	
	ObjectActive(NofObjects)=CurrentObjectActive
	ObjectLastActive(NofObjects)=CurrentObjectLastActive
	
	If RandomActivationType
		CurrentObjectActivationType=Rand(RandomActivationTypeMin,RandomActivationTypeMax)
	EndIf
	If RandomActivationSpeed
		CurrentObjectActivationSpeed=Rand(RandomActivationSpeedMin,RandomActivationSpeedMax)
		; enforce even numbers
		If CurrentObjectActivationSpeed Mod 2=1
			CurrentObjectActivationSpeed=CurrentObjectActivationSpeed+1
		EndIf
	EndIf
	
	ObjectActivationType(NofObjects)=CurrentObjectActivationType
	ObjectActivationSpeed(NofObjects)=CurrentObjectActivationSpeed
	
	If RandomStatus
		CurrentObjectStatus=Rand(RandomStatusMin,RandomStatusMax)
	EndIf
	
	ObjectStatus(NofObjects)=CurrentObjectStatus
	
	If RandomTimer
		CurrentObjectTimer=Rand(RandomTimerMin,RandomTimerMax)
	EndIf
	If RandomTimerMax1
		CurrentObjectTimerMax1=Rand(RandomTimerMax1Min,RandomTimerMax1Max)
	EndIf
	If RandomTimerMax2
		CurrentObjectTimerMax2=Rand(RandomTimerMax2Min,RandomTimerMax2Max)
	EndIf
	
	ObjectTimer(NofObjects)=CurrentObjectTimer
	ObjectTimerMax1(NofObjects)=CurrentObjectTimerMax1
	ObjectTimerMax2(NofObjects)=CurrentObjectTimerMax2
	
	If RandomTeleportable
		CurrentObjectTeleportable=Rand(0,1)
	EndIf
	If RandomButtonPush
		CurrentObjectButtonPush=Rand(0,1)
	EndIf
	
	ObjectTeleportable(NofObjects)=CurrentObjectTeleportable
	ObjectButtonPush(NofObjects)=CurrentObjectButtonPush
	
	ObjectWaterReact(NofObjects)=CurrentObjectWaterReact
	ObjectTelekinesisable(NofObjects)=CurrentObjectTelekinesisable
	ObjectFreezable(NofObjects)=CurrentObjectFreezable
	ObjectReactive(NofObjects)=CurrentObjectReactive
	ObjectChild(NofObjects)=CurrentObjectChild
	ObjectParent(NofObjects)=CurrentObjectParent
	For i=0 To 9
		If RandomData(i)
			CurrentObjectData(i)=Rand(RandomDataMin(i),RandomDataMax(i))
		EndIf
	
		ObjectData(NofObjects,i)=CurrentObjectData(i)
	Next
	For i=0 To 4
		ObjectTextData$(NofObjects,i)=CurrentObjectTextData$(i)
	Next
	
	If RandomTalkable
		CurrentObjectTalkable=Rand(RandomTalkableMin,RandomTalkableMax)
	EndIf
	
	ObjectTalkable(NofObjects)=CurrentObjectTalkable
	ObjectCurrentAnim(NofObjects)=CurrentObjectCurrentAnim
	ObjectStandardAnim(NofObjects)=CurrentObjectStandardAnim
	
	ObjectMovementTimer(NofObjects)=CurrentObjectMovementTimer
	
	If RandomMovementSpeed
		CurrentObjectMovementSpeed=Rand(RandomMovementSpeedMin,RandomMovementSpeedMax)
	EndIf
	
	ObjectMovementSpeed(NofObjects)=CurrentObjectMovementSpeed
	
	If RandomMoveXGoal
		CurrentObjectMoveXGoal=Rand(RandomMoveXGoalMin,RandomMoveXGoalMax)
	EndIf
	If RandomMoveYGoal
		CurrentObjectMoveYGoal=Rand(RandomMoveYGoalMin,RandomMoveYGoalMax)
	EndIf
	
	ObjectMoveXGoal(NofObjects)=CurrentObjectMoveXGoal
	ObjectMoveYGoal(NofObjects)=CurrentObjectMoveYGoal
	
	If RandomTTC
		CurrentObjectTileTypeCollision=0
		For i=0 To 14
			If Rand(0,1)=0
				CurrentObjectTileTypeCollision=CurrentObjectTileTypeCollision+2^i
			EndIf
		Next
	EndIf
	If RandomOTC
		CurrentObjectObjectTypeCollision=0
		For i=1 To 10
			If Rand(0,1)=0
				CurrentObjectObjectTypeCollision=CurrentObjectObjectTypeCollision+2^i
			EndIf
		Next
	EndIf
	
	ObjectTileTypeCollision(NofObjects)=CurrentObjectTileTypeCollision
	ObjectObjectTypeCollision(NofObjects)=CurrentObjectObjectTypeCollision
	
	ObjectCaged(NofObjects)=CurrentObjectCaged
	
	If RandomDead
		CurrentObjectDead=Rand(RandomDeadMin,RandomDeadMax)
	EndIf
	
	ObjectDead(NofObjects)=CurrentObjectDead
	ObjectDeadTimer(NofObjects)=CurrentObjectDeadTimer
	
	If RandomExclamation
		CurrentObjectExclamation=Rand(RandomExclamationMin,RandomExclamationMax)
	EndIf
	
	ObjectExclamation(NofObjects)=CurrentObjectExclamation
	
	ObjectShadow(NofObjects)=CurrentObjectShadow
	ObjectLinked(NofObjects)=CurrentObjectLinked
	ObjectLinkBack(NofObjects)=CurrentObjectLinkBack
	ObjectFlying(NofObjects)=CurrentObjectFlying
	ObjectFrozen(NofObjects)=CurrentObjectFrozen
	ObjectIndigo(NofObjects)=CurrentObjectIndigo
	ObjectFutureInt24(NofObjects)=CurrentObjectFutureInt24
	ObjectFutureInt25(NofObjects)=CurrentObjectFutureInt25
	
	If RandomScaleAdjust
		CurrentObjectScaleAdjust#=Rnd(RandomScaleAdjustMin#,RandomScaleAdjustMax#)
	EndIf

	ObjectScaleAdjust(NofObjects)=CurrentObjectScaleAdjust
	ObjectScaleXAdjust(NofObjects)=CurrentObjectScaleXAdjust
	ObjectScaleYAdjust(NofObjects)=CurrentObjectScaleYAdjust
	ObjectScaleZAdjust(NofObjects)=CurrentObjectScaleZAdjust
	ObjectFutureFloat5(NofObjects)=CurrentObjectFutureFloat5
	ObjectFutureFloat6(NofObjects)=CurrentObjectFutureFloat6
	ObjectFutureFloat7(NofObjects)=CurrentObjectFutureFloat7
	ObjectFutureFloat8(NofObjects)=CurrentObjectFutureFloat8
	ObjectFutureFloat9(NofObjects)=CurrentObjectFutureFloat9
	ObjectFutureFloat10(NofObjects)=CurrentObjectFutureFloat10
	ObjectFutureString1$(NofObjects)=CurrentObjectFutureString1$
	ObjectFutureString2$(NofObjects)=CurrentObjectFutureString2$
	
	For i=0 To 30
		ObjectAdjusterString$(NofObjects,i)=ObjectAdjuster$(i)
	Next
	
	
	; this is only here because of randomized rotation
	BuildCurrentObjectModel()
	
	
	UpdateObjectEntityToCurrent(NofObjects)
	
	
	SetCurrentGrabbedObject(NofObjects)
	
	
	LevelTileObjectCount(floorx,floory)=LevelTileObjectCount(floorx,floory)+1
	CreateObjectPositionMarker(NofObjects)

	
	NofObjects=NofObjects+1
	
	
	UpdateObjectPositionMarkersAtTile(floorx,floory)
	
	
	SomeObjectWasChanged()
	
	

End Function


Function CalculateEffectiveID(Dest)

	;ShowMessage("Calculating effective ID for "+Dest, 50)

	Select ObjectType(Dest)
	Case 10,20,45,210,281,410,424 ; gate, fire trap, conveyor lead, transporter, suction tube straight, flip bridge, laser gate
		If ObjectID(Dest)=-1
			Return 500+5*ObjectData(Dest,0)+ObjectData(Dest,1)
		EndIf
	Case 40,301 ; stepping stone
		Return 500+(8+ObjectData(i,0))*5+ObjectData(i,1)	   
	Case 280 ; spring
		Return 500+5*ObjectData(Dest,0)+ObjectData(Dest,1)
	Case 301 ; rainbow float
		If ObjectID(Dest)=-1
			Return 500+(8+ObjectData(Dest,0))*5+ObjectData(Dest,1)
		EndIf
	End Select
	
	If ObjectModelName$(Dest)="!Cage" Or ObjectModelName$(Dest)="!FlipBridge" Or ObjectModelName$(Dest)="!Spring" Or ObjectModelName$(Dest)="!ColourGate" Or ObjectModelName$(Dest)="!Transporter" Or ObjectModelName$(Dest)="!Teleport" Or ObjectModelName$(Dest)="!Suctube"
		If ObjectID(Dest)=-1
			Return 500+ObjectData(Dest,0)*5+ObjectData(Dest,1)
		EndIf
	EndIf
	If ObjectModelName$(Dest)="!SteppingStone"
		If ObjectID(Dest)=-1
			Return 500+(8+ObjectData(Dest,0))*5+ObjectData(Dest,1)
		EndIf
	EndIf
	
	Return ObjectID(Dest)

End Function


Function ShouldBeInvisibleInGame(Dest)

	If ObjectModelName$(Dest)="!None" Or ObjectModelName$(Dest)="!FloingOrb"
		Return True
	ElseIf ObjectModelName$(Dest)="!Button" And (ObjectSubType(Dest)=11 Or ObjectSubType(Dest)>=32 Or ObjectSubType(Dest)=15) ; NPC move, invisible buttons, general command
		Return True
	ElseIf ObjectModelName$(Dest)="!IceBlock" And ObjectData(Dest,3)<>0 And ObjectData(Dest,3)<>1
		Return True
	Else
		Return False
	EndIf
	
End Function


Function UpdateObjectVisibility(Dest)

	If ShowObjectMesh=False Or (IDFilterEnabled=True And IDFilterAllow<>CalculateEffectiveID(Dest)) ;Or Dest=CurrentGrabbedObject
		If ObjectEntity(Dest)>0
			HideEntity ObjectEntity(Dest)
		EndIf
		If ObjectHatEntity(Dest)>0
			HideEntity ObjectHatEntity(Dest)
		EndIf
		If ObjectAccEntity(Dest)>0
			HideEntity ObjectAccEntity(Dest)
		EndIf
	Else
		If SimulationLevel>=2 And ShouldBeInvisibleInGame(Dest)
			HideEntity ObjectEntity(Dest)
		Else
			If ObjectEntity(Dest)>0
				ShowEntity ObjectEntity(Dest)
			EndIf
			If ObjectHatEntity(Dest)>0
				ShowEntity ObjectHatEntity(Dest)
			EndIf
			If ObjectAccEntity(Dest)>0
				ShowEntity ObjectAccEntity(Dest)
			EndIf
		EndIf
	EndIf

End Function


Function SetEntityPositionToObjectPosition(entity, Dest)

	PositionEntity entity,ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest),-ObjectY(Dest)-ObjectYAdjust(Dest)	

End Function


Function SetEntityPositionToObjectPositionWithoutZ(entity, Dest, z#)

	PositionEntity entity,ObjectX(Dest)+ObjectXAdjust(Dest),z#,-ObjectY(Dest)-ObjectYAdjust(Dest)	

End Function


Function UpdateObjectPosition(Dest)

	SetEntityPositionToObjectPosition(ObjectEntity(Dest), Dest)

	;PositionEntity ObjectEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest),-ObjectY(Dest)-ObjectYAdjust(Dest)
	
	If ObjectHatEntity(Dest)>0
		PositionEntity ObjectHatEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest)+.1+.84*ObjectZScale(Dest)/.035,-ObjectY(Dest)-ObjectYAdjust(Dest)
	EndIf
	
	If ObjectAccEntity(Dest)>0
		PositionEntity ObjectAccEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest)+.1+.84*ObjectZScale(Dest)/.035,-ObjectY(Dest)-ObjectYAdjust(Dest)
	EndIf

End Function


Function UpdateObjectEntityToCurrent(Dest)
	
	ObjectEntity(Dest)=CopyEntity(CurrentObjectModel)
	
	UpdateObjectVisibility(Dest)
	
	If CurrentHatModel>0
		If CurrentObjectData(2)>9 ; two digit
			ObjectHatEntity(Dest)=MyLoadMesh("data/models/stinker/accessory0"+Str$(CurrentObjectData(2))+".3ds",0)
			ObjectHatTexture(Dest)=MyLoadTexture("data/models/stinker/accessory0"+Str$(CurrentObjectData(2))+Chr$(64+CurrentObjectData(3))+".jpg",4)
		Else
			ObjectHatEntity(Dest)=MyLoadMesh("data/models/stinker/accessory00"+Str$(CurrentObjectData(2)+".3ds"),0)
			ObjectHatTexture(Dest)=MyLoadTexture("data/models/stinker/accessory00"+Str$(CurrentObjectData(2))+Chr$(64+CurrentObjectData(3))+".jpg",4)
		EndIf
		
		ScaleEntity ObjectHatEntity(Dest),CurrentObjectYScale*CurrentObjectScaleAdjust,CurrentObjectZScale*CurrentObjectScaleAdjust,CurrentObjectXScale*CurrentObjectScaleAdjust
		
		RotateEntity ObjectHatEntity(Dest),0,0,0
		TurnEntity ObjectHatEntity(Dest),CurrentObjectPitchAdjust,0,CurrentObjectRollAdjust
		TurnEntity ObjectHatEntity(Dest),0,CurrentObjectYawAdjust-90,0
	
		EntityTexture ObjectHatEntity(Dest),ObjectHatTexture(Dest)
	EndIf
	
	If CurrentAccModel>0
		ObjectAccEntity(Dest)=MyLoadMesh("data/models/stinker/accessory"+Str$(CurrentObjectData(4))+".3ds",0)
		ObjectAccTexture(Dest)=MyLoadTexture("data/models/stinker/accessory"+Str$(CurrentObjectData(4))+Chr$(65+CurrentObjectData(5))+".jpg",4)
	
	
		ScaleEntity ObjectAccEntity(Dest),CurrentObjectYScale*CurrentObjectScaleAdjust,CurrentObjectZScale*CurrentObjectScaleAdjust,CurrentObjectXScale*CurrentObjectScaleAdjust
		
		RotateEntity ObjectAccEntity(Dest),0,0,0
		TurnEntity ObjectAccEntity(Dest),CurrentObjectPitchAdjust,0,CurrentObjectRollAdjust
		TurnEntity ObjectAccEntity(Dest),0,CurrentObjectYawAdjust-90,0
	
		EntityTexture ObjectAccEntity(Dest),ObjectAccTexture(Dest)
	EndIf
	
	If ObjectModelName$(Dest)="!Tentacle"
		Animate GetChild(ObjectEntity(Dest),3),1,.1,1,0
	EndIf
	
	UpdateObjectPosition(Dest)

End Function


Function SomeObjectWasChanged()

	ResetSimulatedQuantities()

End Function


Function LightingWasChanged()

	SetLightNow(LightRed,LightGreen,LightBlue,AmbientRed,AmbientGreen,AmbientBlue)

End Function


Function ResetSimulatedQuantities()

	For i=0 To NofObjects-1
		SimulatedObjectXAdjust(i)=ObjectXAdjust(i)
		SimulatedObjectYAdjust(i)=ObjectYAdjust(i)
		SimulatedObjectZAdjust(i)=ObjectZAdjust(i)
		SimulatedObjectX(i)=ObjectX(i)
		SimulatedObjectY(i)=ObjectY(i)
		SimulatedObjectZ(i)=ObjectZ(i)
		SimulatedObjectYaw(i)=ObjectYaw(i)
		SimulatedObjectPitch(i)=ObjectPitch(i)
		SimulatedObjectRoll(i)=ObjectRoll(i)
		SimulatedObjectYawAdjust(i)=ObjectYawAdjust(i)
		SimulatedObjectPitchAdjust(i)=ObjectPitchAdjust(i)
		SimulatedObjectRollAdjust(i)=ObjectRollAdjust(i)
		SimulatedObjectYaw2(i)=ObjectYaw2(i)
		SimulatedObjectPitch2(i)=ObjectPitch2(i)
		SimulatedObjectRoll2(i)=ObjectRoll2(i)
		SimulatedObjectActive(i)=ObjectActive(i)
		SimulatedObjectLastActive(i)=ObjectLastActive(i)
		SimulatedObjectXScale(i)=ObjectXScale(i)
		SimulatedObjectYScale(i)=ObjectYScale(i)
		SimulatedObjectZScale(i)=ObjectZScale(i)
		SimulatedObjectStatus(i)=ObjectStatus(i)
		SimulatedObjectTimer(i)=ObjectTimer(i)
		For j=0 To 10
			SimulatedObjectData(i,j)=ObjectData(i,j)
		Next
		SimulatedObjectMovementSpeed(i)=ObjectMovementSpeed(i)
		SimulatedObjectTileTypeCollision(i)=ObjectTileTypeCollision(i)
		If ObjectScaleAdjust(i)<>0.0
			SimulatedObjectXScale(i)=SimulatedObjectXScale(i)*ObjectScaleAdjust(i)
			SimulatedObjectYScale(i)=SimulatedObjectYScale(i)*ObjectScaleAdjust(i)
			SimulatedObjectZScale(i)=SimulatedObjectZScale(i)*ObjectScaleAdjust(i)
		EndIf
		SimulatedObjectScaleXAdjust(i)=ObjectScaleXAdjust(i)
		SimulatedObjectScaleYAdjust(i)=ObjectScaleYAdjust(i)
		SimulatedObjectScaleZAdjust(i)=ObjectScaleZAdjust(i)
		
		; make sure flipbridges are scaled properly
		If ObjectType(i)=410
			ControlFlipbridge(i)
		EndIf
	Next

End Function


Function SimulateObjectPosition(Dest)

	XP#=SimulatedObjectX(Dest)+SimulatedObjectXAdjust(Dest)
	YP#=SimulatedObjectY(Dest)+SimulatedObjectYAdjust(Dest)
	ZP#=SimulatedObjectZ(Dest)+SimulatedObjectZAdjust(Dest)
	PositionEntity ObjectEntity(Dest),XP#,ZP#,-YP#

End Function

Function SimulateObjectRotation(Dest)

	Pitch#=SimulatedObjectPitch(Dest)+SimulatedObjectPitchAdjust(Dest)
	Roll#=SimulatedObjectRoll(Dest)+SimulatedObjectRollAdjust(Dest)
	Yaw#=SimulatedObjectYaw(Dest)+SimulatedObjectYawAdjust(Dest)
	
	RotateEntity ObjectEntity(Dest),0,0,0
	TurnEntity ObjectEntity(Dest),Pitch#,0,Roll#
	TurnEntity ObjectEntity(Dest),0,Yaw#,0
	TurnEntity ObjectEntity(Dest),SimulatedObjectPitch2(Dest),SimulatedObjectYaw2(Dest),SimulatedObjectRoll2(Dest)

End Function

Function SimulateObjectScale(Dest)

	XS#=SimulatedObjectXScale(Dest)
	YS#=SimulatedObjectYScale(Dest)
	ZS#=SimulatedObjectZScale(Dest)
	
	ScaleEntity ObjectEntity(Dest),XS#,ZS#,YS#
	
End Function


Function ObjectIsAtInt(i,x,y)

	Return Floor(ObjectX(i))=x And Floor(ObjectY(i))=y

End Function

Function ObjectIsAtFloat(i,x#,y#)

	Return ObjectIsAtInt(i,Floor(x),Floor(y))
	;Return Floor(ObjectX(i))=Floor(x) And Floor(ObjectY(i))=Floor(y)

End Function

Function TryGrabObjectLoop(x#,y#,Target)
	For i=0 To NofObjects-1
		If ObjectIsAtFloat(i,x#,y#) And i>Target
			SetCurrentGrabbedObject(i)
			Return True
		EndIf
	Next
	Return False
End Function

Function GrabObject(x#,y#)
	;CachedGrabbedObject=CurrentGrabbedObject
	Flag=TryGrabObjectLoop(x#,y#,CurrentGrabbedObject)
	If Flag=False
		; restart the cycle
		Flag=TryGrabObjectLoop(x#,y#,-1)
	EndIf
	If Flag=False
		; no object found
		;SetCurrentGrabbedObject(CachedGrabbedObject)
		Return
	EndIf
	
	NofWopAdjusters=0
	
	Dest=CurrentGrabbedObject

	CurrentObjectModelName$=ObjectModelName$(Dest)
	CurrentObjectTextureName$=ObjectTextureName$(Dest)
	CurrentObjectXScale#=ObjectXScale#(Dest)
	CurrentObjectZScale#=ObjectZScale#(Dest)
	CurrentObjectYScale#=ObjectYScale#(Dest)
	CurrentObjectXAdjust#=ObjectXAdjust#(Dest)
	CurrentObjectZAdjust#=ObjectZAdjust#(Dest)
	CurrentObjectYAdjust#=ObjectYAdjust#(Dest)
	CurrentObjectPitchAdjust#=ObjectPitchAdjust#(Dest)
	CurrentObjectYawAdjust#=ObjectYawAdjust#(Dest)
	CurrentObjectRollAdjust#=ObjectRollAdjust#(Dest)
	CurrentObjectX#=ObjectX#(Dest)-x-0.5
	CurrentObjectY#=ObjectY#(Dest)-y-0.5
	CurrentObjectZ#=ObjectZ#(Dest)
	; oldxyz is not grabbed
	CurrentObjectDX#=ObjectDX#(Dest)
	CurrentObjectDY#=ObjectDY#(Dest)
	CurrentObjectDZ#=ObjectDZ#(Dest)
	CurrentObjectPitch#=ObjectPitch#(Dest)
	CurrentObjectYaw#=ObjectYaw#(Dest)
	CurrentObjectRoll#=ObjectRoll#(Dest)
	CurrentObjectPitch2#=ObjectPitch2#(Dest)
	CurrentObjectYaw2#=ObjectYaw2#(Dest)
	CurrentObjectRoll2#=ObjectRoll2#(Dest)
	CurrentObjectXGoal#=ObjectXGoal#(Dest)
	CurrentObjectYGoal#=ObjectYGoal#(Dest)
	CurrentObjectZGoal#=ObjectZGoal#(Dest)
	CurrentObjectMovementType=ObjectMovementType(Dest)
	CurrentObjectMovementTypeData=ObjectMovementTypeData(Dest)
	CurrentObjectSpeed#=ObjectSpeed#(Dest)
	CurrentObjectRadius#=ObjectRadius#(Dest)
	CurrentObjectRadiusType=ObjectRadiusType(Dest)
	CurrentObjectData10=ObjectData10(Dest)
	CurrentObjectPushDX#=ObjectPushDX#(Dest)
	CurrentObjectPushDY#=ObjectPushDY#(Dest)
	CurrentObjectAttackPower=ObjectAttackPower(Dest)
	CurrentObjectDefensePower=ObjectDefensePower(Dest)
	CurrentObjectDestructionType=ObjectDestructionType(Dest)
	CurrentObjectID=ObjectID(Dest)
	CurrentObjectType=ObjectType(Dest)
	CurrentObjectSubType=ObjectSubType(Dest)
	CurrentObjectActive=ObjectActive(Dest)
	CurrentObjectLastActive=ObjectLastActive(Dest)
	CurrentObjectActivationType=ObjectActivationType(Dest)
	CurrentObjectActivationSpeed=ObjectActivationSpeed(Dest)
	CurrentObjectStatus=ObjectStatus(Dest)
	CurrentObjectTimer=ObjectTimer(Dest)
	CurrentObjectTimerMax1=ObjectTimerMax1(Dest)
	CurrentObjectTimerMax2=ObjectTimerMax2(Dest)
	CurrentObjectTeleportable=ObjectTeleportable(Dest)
	CurrentObjectButtonPush=ObjectButtonPush(Dest)
	CurrentObjectWaterReact=ObjectWaterReact(Dest)
	CurrentObjectTelekinesisable=ObjectTelekinesisable(Dest)
	CurrentObjectFreezable=ObjectFreezable(Dest)
	CurrentObjectReactive=ObjectReactive(Dest)
	CurrentObjectChild=ObjectChild(Dest)
	CurrentObjectParent=ObjectParent(Dest)
	For i=0 To 9
		CurrentObjectData(i)=ObjectData(Dest,i)
	Next
	For i=0 To 4
		CurrentObjectTextData$(i)=ObjectTextData$(Dest,i)
	Next	
	
	CurrentObjectTalkable=ObjectTalkable(Dest)
	CurrentObjectCurrentAnim=ObjectCurrentAnim(Dest)
	CurrentObjectStandardAnim=ObjectStandardAnim(Dest)
	CurrentObjectTileX=ObjectTileX(Dest)
	CurrentObjectTileY=ObjectTileY(Dest)
	CurrentObjectTileX2=ObjectTileX2(Dest)
	CurrentObjectTileY2=ObjectTileY2(Dest)
	CurrentObjectMovementTimer=ObjectMovementTimer(Dest)
	CurrentObjectMovementSpeed=ObjectMovementSpeed(Dest)
	CurrentObjectMoveXGoal=ObjectMoveXGoal(Dest)
	CurrentObjectMoveYGoal=ObjectMoveYGoal(Dest)
	CurrentObjectTileTypeCollision=ObjectTileTypeCollision(Dest)
	CurrentObjectObjectTypeCollision=ObjectObjectTypeCollision(Dest)
	CurrentObjectCaged=ObjectCaged(Dest)
	CurrentObjectDead=ObjectDead(Dest)
	CurrentObjectDeadTimer=ObjectDeadTimer(Dest)
	CurrentObjectExclamation=ObjectExclamation(Dest)
	CurrentObjectShadow=ObjectShadow(Dest)
	CurrentObjectLinked=ObjectLinked(Dest)
	CurrentObjectLinkBack=ObjectLinkBack(Dest)
	CurrentObjectFlying=ObjectFlying(Dest)
	CurrentObjectFrozen=ObjectFrozen(Dest)
	CurrentObjectIndigo=ObjectIndigo(Dest)
	CurrentObjectFutureInt24=ObjectFutureInt24(Dest)
	CurrentObjectFutureInt25=ObjectFutureInt25(Dest)

	CurrentObjectScaleAdjust=ObjectScaleAdjust(Dest)
	CurrentObjectScaleXAdjust=ObjectScaleXAdjust(Dest)
	CurrentObjectScaleYAdjust=ObjectScaleYAdjust(Dest)
	CurrentObjectScaleZAdjust=ObjectScaleZAdjust(Dest)
	CurrentObjectFutureFloat5=ObjectFutureFloat5(Dest)
	CurrentObjectFutureFloat6=ObjectFutureFloat6(Dest)
	CurrentObjectFutureFloat7=ObjectFutureFloat7(Dest)
	CurrentObjectFutureFloat8=ObjectFutureFloat8(Dest)
	CurrentObjectFutureFloat9=ObjectFutureFloat9(Dest)
	CurrentObjectFutureFloat10=ObjectFutureFloat10(Dest)
	CurrentObjectFutureString1$=ObjectFutureString1$(Dest)
	CurrentObjectFutureString2$=ObjectFutureString2$(Dest)
	
	;NofObjectAdjusters=0
	;ObjectAdjusterStart=0
	;For i=0 To 30
	;	ObjectAdjuster$(i)=ObjectAdjusterString$(Dest,i)
	;	If ObjectAdjuster$(i) <>""
	;		NofObjectAdjusters=NofObjectAdjusters+1
	;	EndIf
	;Next
	;
	;If CurrentObjectType=110
	;	ObjectAdjuster$(18)="DefensePower"
	;EndIf
	;If CurrentObjectType=433
	;	ObjectAdjuster$(12)="DefensePower"
	;EndIf
	;If CurrentObjectType=330
	;	ObjectAdjuster$(7)="DefensePower"
	;EndIf
	;If CurrentObjectType=390
	;	ObjectAdjuster$(12)="DefensePower"
	;EndIf
	;If CurrentObjectType=380
	;	ObjectAdjuster$(10)="DefensePower"
	;EndIf
	;If CurrentObjectType=290
	;	ObjectAdjuster$(11)="DefensePower"
	;EndIf






	BuildCurrentObjectModel()
	

End Function



Function GrabObjectFromBrush(i)	
	Dest=i

	CurrentObjectModelName$=BrushObjectModelName$(Dest)
	CurrentObjectTextureName$=BrushObjectTextureName$(Dest)
	CurrentObjectXScale#=BrushObjectXScale#(Dest)
	CurrentObjectZScale#=BrushObjectZScale#(Dest)
	CurrentObjectYScale#=BrushObjectYScale#(Dest)
	CurrentObjectXAdjust#=BrushObjectXAdjust#(Dest)
	CurrentObjectZAdjust#=BrushObjectZAdjust#(Dest)
	CurrentObjectYAdjust#=BrushObjectYAdjust#(Dest)
	CurrentObjectPitchAdjust#=BrushObjectPitchAdjust#(Dest)
	CurrentObjectYawAdjust#=BrushObjectYawAdjust#(Dest)
	CurrentObjectRollAdjust#=BrushObjectRollAdjust#(Dest)
	CurrentObjectX#=BrushObjectX#(Dest)-x-0.5
	CurrentObjectY#=BrushObjectY#(Dest)-y-0.5
	CurrentObjectZ#=BrushObjectZ#(Dest)
	; oldxyz is not grabbed
	CurrentObjectDX#=BrushObjectDX#(Dest)
	CurrentObjectDY#=BrushObjectDY#(Dest)
	CurrentObjectDZ#=BrushObjectDZ#(Dest)
	CurrentObjectPitch#=BrushObjectPitch#(Dest)
	CurrentObjectYaw#=BrushObjectYaw#(Dest)
	CurrentObjectRoll#=BrushObjectRoll#(Dest)
	CurrentObjectPitch2#=BrushObjectPitch2#(Dest)
	CurrentObjectYaw2#=BrushObjectYaw2#(Dest)
	CurrentObjectRoll2#=BrushObjectRoll2#(Dest)
	CurrentObjectXGoal#=BrushObjectXGoal#(Dest)
	CurrentObjectYGoal#=BrushObjectYGoal#(Dest)
	CurrentObjectZGoal#=BrushObjectZGoal#(Dest)
	CurrentObjectMovementType=BrushObjectMovementType(Dest)
	CurrentObjectMovementTypeData=BrushObjectMovementTypeData(Dest)
	CurrentObjectSpeed#=BrushObjectSpeed#(Dest)
	CurrentObjectRadius#=BrushObjectRadius#(Dest)
	CurrentObjectRadiusType=BrushObjectRadiusType(Dest)
	CurrentObjectData10=BrushObjectData10(Dest)
	CurrentObjectPushDX#=BrushObjectPushDX#(Dest)
	CurrentObjectPushDY#=BrushObjectPushDY#(Dest)
	CurrentObjectAttackPower=BrushObjectAttackPower(Dest)
	CurrentObjectDefensePower=BrushObjectDefensePower(Dest)
	CurrentObjectDestructionType=BrushObjectDestructionType(Dest)
	CurrentObjectID=BrushObjectID(Dest)
	CurrentObjectType=BrushObjectType(Dest)
	CurrentObjectSubType=BrushObjectSubType(Dest)
	CurrentObjectActive=BrushObjectActive(Dest)
	CurrentObjectLastActive=BrushObjectLastActive(Dest)
	CurrentObjectActivationType=BrushObjectActivationType(Dest)
	CurrentObjectActivationSpeed=BrushObjectActivationSpeed(Dest)
	CurrentObjectStatus=BrushObjectStatus(Dest)
	CurrentObjectTimer=BrushObjectTimer(Dest)
	CurrentObjectTimerMax1=BrushObjectTimerMax1(Dest)
	CurrentObjectTimerMax2=BrushObjectTimerMax2(Dest)
	CurrentObjectTeleportable=BrushObjectTeleportable(Dest)
	CurrentObjectButtonPush=BrushObjectButtonPush(Dest)
	CurrentObjectWaterReact=BrushObjectWaterReact(Dest)
	CurrentObjectTelekinesisable=BrushObjectTelekinesisable(Dest)
	CurrentObjectFreezable=BrushObjectFreezable(Dest)
	CurrentObjectReactive=BrushObjectReactive(Dest)
	CurrentObjectChild=BrushObjectChild(Dest)
	CurrentObjectParent=BrushObjectParent(Dest)
	For i=0 To 9
		CurrentObjectData(i)=BrushObjectData(Dest,i)
	Next
	For i=0 To 4
		CurrentObjectTextData$(i)=BrushObjectTextData$(Dest,i)
	Next	
	
	CurrentObjectTalkable=BrushObjectTalkable(Dest)
	CurrentObjectCurrentAnim=BrushObjectCurrentAnim(Dest)
	CurrentObjectStandardAnim=BrushObjectStandardAnim(Dest)
	CurrentObjectTileX=BrushObjectTileX(Dest)
	CurrentObjectTileY=BrushObjectTileY(Dest)
	CurrentObjectTileX2=BrushObjectTileX2(Dest)
	CurrentObjectTileY2=BrushObjectTileY2(Dest)
	CurrentObjectMovementTimer=BrushObjectMovementTimer(Dest)
	CurrentObjectMovementSpeed=BrushObjectMovementSpeed(Dest)
	CurrentObjectMoveXGoal=BrushObjectMoveXGoal(Dest)
	CurrentObjectMoveYGoal=BrushObjectMoveYGoal(Dest)
	CurrentObjectTileTypeCollision=BrushObjectTileTypeCollision(Dest)
	CurrentObjectObjectTypeCollision=BrushObjectObjectTypeCollision(Dest)
	CurrentObjectCaged=BrushObjectCaged(Dest)
	CurrentObjectDead=BrushObjectDead(Dest)
	CurrentObjectDeadTimer=BrushObjectDeadTimer(Dest)
	CurrentObjectExclamation=BrushObjectExclamation(Dest)
	CurrentObjectShadow=BrushObjectShadow(Dest)
	CurrentObjectLinked=BrushObjectLinked(Dest)
	CurrentObjectLinkBack=BrushObjectLinkBack(Dest)
	CurrentObjectFlying=BrushObjectFlying(Dest)
	CurrentObjectFrozen=BrushObjectFrozen(Dest)
	CurrentObjectIndigo=BrushObjectIndigo(Dest)
	CurrentObjectFutureInt24=BrushObjectFutureInt24(Dest)
	CurrentObjectFutureInt25=BrushObjectFutureInt25(Dest)

	CurrentObjectScaleAdjust=BrushObjectScaleAdjust(Dest)
	CurrentObjectScaleXAdjust=BrushObjectScaleXAdjust(Dest)
	CurrentObjectScaleYAdjust=BrushObjectScaleYAdjust(Dest)
	CurrentObjectScaleZAdjust=BrushObjectScaleZAdjust(Dest)
	CurrentObjectFutureFloat5=BrushObjectFutureFloat5(Dest)
	CurrentObjectFutureFloat6=BrushObjectFutureFloat6(Dest)
	CurrentObjectFutureFloat7=BrushObjectFutureFloat7(Dest)
	CurrentObjectFutureFloat8=BrushObjectFutureFloat8(Dest)
	CurrentObjectFutureFloat9=BrushObjectFutureFloat9(Dest)
	CurrentObjectFutureFloat10=BrushObjectFutureFloat10(Dest)
	CurrentObjectFutureString1$=BrushObjectFutureString1$(Dest)
	CurrentObjectFutureString2$=BrushObjectFutureString2$(Dest)
		
	BuildCurrentObjectModel()
	

End Function



Function CreateObjectPositionMarker(i)

	ObjectPositionMarker(i)=CopyEntity(ObjectPositionMarkerMesh)
	EntityAlpha ObjectPositionMarker(i),.8
	;EntityColor ObjectPositionMarker(i),255,100,100
	PositionEntity ObjectPositionMarker(i),ObjectX(i),0,-ObjectY(i)
	
	If ShowObjectPositions=False
		HideEntity ObjectPositionMarker(i)
	EndIf
	
End Function

Function UpdateObjectPositionMarkersAtTile(tilex,tiley)

	;ShowMessage("Updating object position markers...", 100)

	;LevelTileObjectCount(tilex,tiley)=0

	For i=0 To NofObjects-1
		If ObjectTileX(i)=tilex And ObjectTileY(i)=tiley
		;If ObjectIsAtInt(i,tilex,tiley)
			If LevelTileObjectCount(tilex,tiley)=1
				EntityColor ObjectPositionMarker(i),255,100,100
			Else
				EntityColor ObjectPositionMarker(i),255,255,100
			EndIf
		EndIf
	Next
	
	;ShowMessage("Update successful.", 100)

End Function

Function FreeModel(i)

	If ObjectEntity(i)>0
		FreeEntity ObjectEntity(i)
		ObjectEntity(i)=0
	EndIf
	
	If ObjectTexture(i)>0
		FreeTexture ObjectTexture(i)
		ObjectTexture(i)=0
	EndIf

	;ShowMessage("Checking ObjectHatEntity for freeing on "+i+": "+ObjectHatEntity(i), 1000)
	If ObjectHatEntity(i)>0
		FreeEntity ObjectHatEntity(i)
		ObjectHatEntity(i)=0
	EndIf

	;ShowMessage("Checking ObjectAccEntity for freeing on "+i+": "+ObjectAccEntity(i), 1000)
	If ObjectAccEntity(i)>0
		FreeEntity ObjectAccEntity(i)
		ObjectAccEntity(i)=0
	EndIf
	
	;ShowMessage("Checking ObjectHatTexture for freeing on "+i+": "+ObjectHatTexture(i), 1000)
	If ObjectHatTexture(i)>0
		FreeTexture ObjectHatTexture(i)
		ObjectHatTexture(i)=0
	EndIf

	;ShowMessage("Checking ObjectAccTexture for freeing on "+i+": "+ObjectAccTexture(i), 1000)
	If ObjectAccTexture(i)>0
		FreeTexture ObjectAccTexture(i)
		ObjectAccTexture(i)=0
	EndIf

End Function

Function FreeObject(i)

	FreeModel(i)

	If ObjectPositionMarker(i)>0
		FreeEntity ObjectPositionMarker(i)
		ObjectPositionMarker(i)=0
	EndIf

End Function

Function DeleteObject(i)

	;ShowMessage("Deleting object "+i, 100)

	tilex=ObjectTileX(i)
	tiley=ObjectTileY(i)
	LevelTileObjectCount(tilex,tiley)=LevelTileObjectCount(tilex,tiley)-1

	FreeObject(i)
	
	;ShowMessage("Copying object data...", 100)

	For j=i+1 To NofObjects-1
		CopyObjectData(j,j-1)
	Next

	;ShowMessage("Setting current grabbed object...", 100)
	
	If i=CurrentGrabbedObject
		SetCurrentGrabbedObject(-1)
	Else If i<CurrentGrabbedObject
		SetCurrentGrabbedObject(CurrentGrabbedObject-1)
	EndIf
	
	NofObjects=NofObjects-1
	
	For j=0 To NofObjects-1
		If ObjectLinked(j)=i
			ObjectLinked(j)=-1
		Else If ObjectLinked(j)>i
			ObjectLinked(j)=ObjectLinked(j)-1
		EndIf
		
		If ObjectLinkBack(j)=i
			ObjectLinkBack(j)=-1
		Else If ObjectLinkBack(j)>i
			ObjectLinkBack(j)=ObjectLinkBack(j)-1
		EndIf
		
		If ObjectParent(j)=i
			ObjectParent(j)=-1
		Else If ObjectParent(j)>i
			ObjectParent(j)=ObjectParent(j)-1
		EndIf
		
		If ObjectChild(j)=i
			ObjectChild(j)=-1
		Else If ObjectChild(j)>i
			ObjectChild(j)=ObjectChild(j)-1
		EndIf
	Next
	
	If CurrentObjectLinked=i
		CurrentObjectLinked=-1
	Else If CurrentObjectLinked>i
		CurrentObjectLinked=CurrentObjectLinked-1
	EndIf
	
	If CurrentObjectLinkBack=i
		CurrentObjectLinkBack=-1
	Else If CurrentObjectLinkBack>i
		CurrentObjectLinkBack=CurrentObjectLinkBack-1
	EndIf
	
	If CurrentObjectParent=i
		CurrentObjectParent=-1
	Else If CurrentObjectParent>i
		CurrentObjectParent=CurrentObjectParent-1
	EndIf
	
	If CurrentObjectChild=i
		CurrentObjectChild=-1
	Else If CurrentObjectChild>i
		CurrentObjectChild=CurrentObjectChild-1
	EndIf
	
	UpdateObjectPositionMarkersAtTile(tilex,tiley)
	
	SomeObjectWasChanged()
	

End Function

Function DeleteObjectAt(x,y)

	DeleteCount=0
	For i=0 To NofObjects-1
		If Floor(ObjectX(i))=x And Floor(ObjectY(i))=y
			DeleteObject(i)
			SetEditorMode(3)
			i=i-1
			DeleteCount=DeleteCount+1
		EndIf
	Next
	;ShowMessage(DeleteCount, 1000)
	Return DeleteCount

End Function

Function CopyObjectData(Source,Dest)

	ObjectEntity(Dest)=ObjectEntity(Source)
	ObjectTexture(Dest)=ObjectTexture(Source)
	ObjectHatEntity(Dest)=ObjectHatEntity(Source)
	ObjectAccEntity(Dest)=ObjectAccEntity(Source)
	ObjectHatTexture(Dest)=ObjectHatTexture(Source)
	ObjectAccTexture(Dest)=ObjectAccTexture(Source)
	; making sure there is no aliasing since that previously caused occasional MAVs
	ObjectEntity(Source)=0
	ObjectTexture(Source)=0
	ObjectHatEntity(Source)=0
	ObjectAccEntity(Source)=0
	ObjectHatTexture(Source)=0
	ObjectAccTexture(Source)=0
	

	ObjectModelName$(Dest)=ObjectModelName$(Source)
	ObjectTextureName$(Dest)=ObjectTextureName$(Source)
	ObjectXScale#(Dest)=ObjectXScale#(Source)
	ObjectZScale#(Dest)=ObjectZScale#(Source)
	ObjectYScale#(Dest)=ObjectYScale#(Source)
	ObjectXAdjust#(Dest)=ObjectXAdjust#(Source)
	ObjectZAdjust#(Dest)=ObjectZAdjust#(Source)
	ObjectYAdjust#(Dest)=ObjectYAdjust#(Source)
	ObjectPitchAdjust#(Dest)=ObjectPitchAdjust#(Source)
	ObjectYawAdjust#(Dest)=ObjectYawAdjust#(Source)
	ObjectRollAdjust#(Dest)=ObjectRollAdjust#(Source)
		
	ObjectX(Dest)=ObjectX(Source)
	ObjectY(Dest)=ObjectY(Source)
	ObjectZ(Dest)=ObjectZ(Source)
	;oldxyz is not copied
	ObjectDX(Dest)=ObjectDX(Source)
	ObjectDY(Dest)=ObjectDY(Source)
	ObjectDZ(Dest)=ObjectDZ(Source)
	
	ObjectPitch(Dest)=ObjectPitch(Source)
	ObjectYaw(Dest)=ObjectYaw(Source)
	ObjectRoll(Dest)=ObjectRoll(Source)
	ObjectPitch2(Dest)=ObjectPitch2(Source)
	ObjectYaw2(Dest)=ObjectYaw2(Source)
	ObjectRoll2(Dest)=ObjectRoll2(Source)


	ObjectXGoal(Dest)=ObjectXGoal(Source)
	ObjectYGoal(Dest)=ObjectYGoal(Source)
	ObjectZGoal(Dest)=ObjectZGoal(Source)
	
	ObjectMovementType(Dest)=ObjectMovementType(Source)
	ObjectMovementTypeData(Dest)=ObjectMovementTypeData(Source)
	ObjectSpeed(Dest)=ObjectSpeed(Source)
	ObjectRadius(Dest)=ObjectRadius(Source)
	ObjectRadiusType(Dest)=ObjectRadiusType(Source)
	
	ObjectData10(Dest)=ObjectData10(Source)
	
	ObjectPushDX(Dest)=ObjectPushDX(Source)
	ObjectPushDY(Dest)=ObjectPushDY(Source)

	
	ObjectAttackPower(Dest)=ObjectAttackPower(Source)
	ObjectDefensePower(Dest)=ObjectDefensePower(Source)
	ObjectDestructionType(Dest)=ObjectDestructionType(Source)
	

	ObjectID(Dest)=ObjectID(Source)
	ObjectType(Dest)=ObjectType(Source)
	ObjectSubType(Dest)=ObjectSubType(Source)
	
	ObjectActive(Dest)=ObjectActive(Source)
	ObjectLastActive(Dest)=ObjectLastActive(Source)
	ObjectActivationType(Dest)=ObjectActivationType(Source)
	ObjectActivationSpeed(Dest)=ObjectActivationSpeed(Source)
	
	ObjectStatus(Dest)=ObjectStatus(Source)
	ObjectTimer(Dest)=ObjectTimer(Source)
	ObjectTimerMax1(Dest)=ObjectTimerMax1(Source)
	ObjectTimerMax2(Dest)=ObjectTimerMax2(Source)
	
	ObjectTeleportable(Dest)=ObjectTeleportable(Source)
	ObjectButtonPush(Dest)=ObjectButtonPush(Source)
	ObjectWaterReact(Dest)=ObjectWaterReact(Source)
	
	ObjectTelekinesisable(Dest)=ObjectTelekinesisable(Source)
	ObjectFreezable(Dest)=ObjectFreezable(Source)
	
	ObjectReactive(Dest)=ObjectReactive(Source)
	
	ObjectChild(Dest)=ObjectChild(Source)
	ObjectParent(Dest)=ObjectParent(Source)

	
	For k=0 To 9
		ObjectData(Dest,k)=ObjectData(Source,k)
	Next
	For k=0 To 3
		ObjectTextData$(Dest,k)=ObjectTextData$(Source,k)
	Next
	
	ObjectTalkable(Dest)=ObjectTalkable(Source)
	ObjectCurrentAnim(Dest)=ObjectCurrentAnim(Source)
	ObjectStandardAnim(Dest)=ObjectStandardAnim(Source)
	ObjectTileX(Dest)=ObjectTileX(Source)
	ObjectTileY(Dest)=ObjectTileY(Source)
	ObjectTileX2(Dest)=ObjectTileX2(Source)
	ObjectTileY2(Dest)=ObjectTileY2(Source)
	ObjectMovementTimer(Dest)=ObjectMovementTimer(Source)
	ObjectMovementSpeed(Dest)=ObjectMovementSpeed(Source)
	ObjectMoveXGoal(Dest)=ObjectMoveXGoal(Source)
	ObjectMoveYGoal(Dest)=ObjectMoveYGoal(Source)
	ObjectTileTypeCollision(Dest)=ObjectTileTypeCollision(Source)
	ObjectObjectTypeCollision(Dest)=ObjectObjectTypeCollision(Source)
	ObjectCaged(Dest)=ObjectCaged(Source)
	ObjectDead(Dest)=ObjectDead(Source)
	ObjectDeadTimer(Dest)=ObjectDeadTimer(Source)
	ObjectExclamation(Dest)=ObjectExclamation(Source)
	ObjectShadow(Dest)=ObjectShadow(Source)
	ObjectLinked(Dest)=ObjectLinked(Source)
	ObjectLinkBack(Dest)=ObjectLinkBack(Source)
	ObjectFlying(Dest)=ObjectFlying(Source)
	ObjectFrozen(Dest)=ObjectFrozen(Source)
	ObjectIndigo(Dest)=ObjectIndigo(Source)
	ObjectFutureInt24(Dest)=ObjectFutureInt24(Source)
	ObjectFutureInt25(Dest)=ObjectFutureInt25(Source)
	ObjectScaleAdjust(Dest)=ObjectScaleAdjust(Source)
	ObjectScaleXAdjust(Dest)=ObjectScaleXAdjust(Source)
	ObjectScaleYAdjust(Dest)=ObjectScaleYAdjust(Source)
	ObjectScaleZAdjust(Dest)=ObjectScaleZAdjust(Source)
	ObjectFutureFloat5(Dest)=ObjectFutureFloat5(Source)
	ObjectFutureFloat6(Dest)=ObjectFutureFloat6(Source)
	ObjectFutureFloat7(Dest)=ObjectFutureFloat7(Source)
	ObjectFutureFloat8(Dest)=ObjectFutureFloat8(Source)
	ObjectFutureFloat9(Dest)=ObjectFutureFloat9(Source)
	ObjectFutureFloat10(Dest)=ObjectFutureFloat10(Source)
	ObjectFutureString1$(Dest)=ObjectFutureString1$(Source)
	ObjectFutureString2$(Dest)=ObjectFutureString1$(Source)
	
	For i=0 To 30
		ObjectAdjusterString$(Dest,i)=ObjectAdjusterString$(Source,i)
	Next
	
	ObjectPositionMarker(Dest)=ObjectPositionMarker(Source)

	
End Function


Function CopyObjectDataToBrush(Source,Dest,XOffset#,YOffset#)

	BrushObjectXOffset#(Dest)=XOffset#-0.5
	BrushObjectYOffset#(Dest)=YOffset#-0.5

	BrushObjectModelName$(Dest)=ObjectModelName$(Source)
	BrushObjectTextureName$(Dest)=ObjectTextureName$(Source)
	BrushObjectXScale#(Dest)=ObjectXScale#(Source)
	BrushObjectZScale#(Dest)=ObjectZScale#(Source)
	BrushObjectYScale#(Dest)=ObjectYScale#(Source)
	BrushObjectXAdjust#(Dest)=ObjectXAdjust#(Source)
	BrushObjectZAdjust#(Dest)=ObjectZAdjust#(Source)
	BrushObjectYAdjust#(Dest)=ObjectYAdjust#(Source)
	BrushObjectPitchAdjust#(Dest)=ObjectPitchAdjust#(Source)
	BrushObjectYawAdjust#(Dest)=ObjectYawAdjust#(Source)
	BrushObjectRollAdjust#(Dest)=ObjectRollAdjust#(Source)
		
	BrushObjectX(Dest)=0.5 ;ObjectX(Source)
	BrushObjectY(Dest)=0.5 ;ObjectY(Source)
	BrushObjectZ(Dest)=ObjectZ(Source)
	;oldxyz is not copied
	BrushObjectDX(Dest)=ObjectDX(Source)
	BrushObjectDY(Dest)=ObjectDY(Source)
	BrushObjectDZ(Dest)=ObjectDZ(Source)
	
	BrushObjectPitch(Dest)=ObjectPitch(Source)
	BrushObjectYaw(Dest)=ObjectYaw(Source)
	BrushObjectRoll(Dest)=ObjectRoll(Source)
	BrushObjectPitch2(Dest)=ObjectPitch2(Source)
	BrushObjectYaw2(Dest)=ObjectYaw2(Source)
	BrushObjectRoll2(Dest)=ObjectRoll2(Source)


	BrushObjectXGoal(Dest)=ObjectXGoal(Source)
	BrushObjectYGoal(Dest)=ObjectYGoal(Source)
	BrushObjectZGoal(Dest)=ObjectZGoal(Source)
	
	BrushObjectMovementType(Dest)=ObjectMovementType(Source)
	BrushObjectMovementTypeData(Dest)=ObjectMovementTypeData(Source)
	BrushObjectSpeed(Dest)=ObjectSpeed(Source)
	BrushObjectRadius(Dest)=ObjectRadius(Source)
	BrushObjectRadiusType(Dest)=ObjectRadiusType(Source)
	
	BrushObjectData10(Dest)=ObjectData10(Source)
	
	BrushObjectPushDX(Dest)=ObjectPushDX(Source)
	BrushObjectPushDY(Dest)=ObjectPushDY(Source)

	
	BrushObjectAttackPower(Dest)=ObjectAttackPower(Source)
	BrushObjectDefensePower(Dest)=ObjectDefensePower(Source)
	BrushObjectDestructionType(Dest)=ObjectDestructionType(Source)
	

	BrushObjectID(Dest)=ObjectID(Source)
	BrushObjectType(Dest)=ObjectType(Source)
	BrushObjectSubType(Dest)=ObjectSubType(Source)
	
	BrushObjectActive(Dest)=ObjectActive(Source)
	BrushObjectLastActive(Dest)=ObjectLastActive(Source)
	BrushObjectActivationType(Dest)=ObjectActivationType(Source)
	BrushObjectActivationSpeed(Dest)=ObjectActivationSpeed(Source)
	
	BrushObjectStatus(Dest)=ObjectStatus(Source)
	BrushObjectTimer(Dest)=ObjectTimer(Source)
	BrushObjectTimerMax1(Dest)=ObjectTimerMax1(Source)
	BrushObjectTimerMax2(Dest)=ObjectTimerMax2(Source)
	
	BrushObjectTeleportable(Dest)=ObjectTeleportable(Source)
	BrushObjectButtonPush(Dest)=ObjectButtonPush(Source)
	BrushObjectWaterReact(Dest)=ObjectWaterReact(Source)
	
	BrushObjectTelekinesisable(Dest)=ObjectTelekinesisable(Source)
	BrushObjectFreezable(Dest)=ObjectFreezable(Source)
	
	BrushObjectReactive(Dest)=ObjectReactive(Source)
	
	BrushObjectChild(Dest)=ObjectChild(Source)
	BrushObjectParent(Dest)=ObjectParent(Source)

	
	For k=0 To 9
		BrushObjectData(Dest,k)=ObjectData(Source,k)
	Next
	For k=0 To 3
		BrushObjectTextData$(Dest,k)=ObjectTextData$(Source,k)
	Next
	
	BrushObjectTalkable(Dest)=ObjectTalkable(Source)
	BrushObjectCurrentAnim(Dest)=ObjectCurrentAnim(Source)
	BrushObjectStandardAnim(Dest)=ObjectStandardAnim(Source)
	BrushObjectTileX(Dest)=ObjectTileX(Source)
	BrushObjectTileY(Dest)=ObjectTileY(Source)
	BrushObjectTileX2(Dest)=ObjectTileX2(Source)
	BrushObjectTileY2(Dest)=ObjectTileY2(Source)
	BrushObjectMovementTimer(Dest)=ObjectMovementTimer(Source)
	BrushObjectMovementSpeed(Dest)=ObjectMovementSpeed(Source)
	BrushObjectMoveXGoal(Dest)=ObjectMoveXGoal(Source)
	BrushObjectMoveYGoal(Dest)=ObjectMoveYGoal(Source)
	BrushObjectTileTypeCollision(Dest)=ObjectTileTypeCollision(Source)
	BrushObjectObjectTypeCollision(Dest)=ObjectObjectTypeCollision(Source)
	BrushObjectCaged(Dest)=ObjectCaged(Source)
	BrushObjectDead(Dest)=ObjectDead(Source)
	BrushObjectDeadTimer(Dest)=ObjectDeadTimer(Source)
	BrushObjectExclamation(Dest)=ObjectExclamation(Source)
	BrushObjectShadow(Dest)=ObjectShadow(Source)
	BrushObjectLinked(Dest)=ObjectLinked(Source)
	BrushObjectLinkBack(Dest)=ObjectLinkBack(Source)
	BrushObjectFlying(Dest)=ObjectFlying(Source)
	BrushObjectFrozen(Dest)=ObjectFrozen(Source)
	BrushObjectIndigo(Dest)=ObjectIndigo(Source)
	BrushObjectFutureInt24(Dest)=ObjectFutureInt24(Source)
	BrushObjectFutureInt25(Dest)=ObjectFutureInt25(Source)
	BrushObjectScaleAdjust(Dest)=ObjectScaleAdjust(Source)
	BrushObjectScaleXAdjust(Dest)=ObjectScaleXAdjust(Source)
	BrushObjectScaleYAdjust(Dest)=ObjectScaleYAdjust(Source)
	BrushObjectScaleZAdjust(Dest)=ObjectScaleZAdjust(Source)
	BrushObjectFutureFloat5(Dest)=ObjectFutureFloat5(Source)
	BrushObjectFutureFloat6(Dest)=ObjectFutureFloat6(Source)
	BrushObjectFutureFloat7(Dest)=ObjectFutureFloat7(Source)
	BrushObjectFutureFloat8(Dest)=ObjectFutureFloat8(Source)
	BrushObjectFutureFloat9(Dest)=ObjectFutureFloat9(Source)
	BrushObjectFutureFloat10(Dest)=ObjectFutureFloat10(Source)
	BrushObjectFutureString1$(Dest)=ObjectFutureString1$(Source)
	BrushObjectFutureString2$(Dest)=ObjectFutureString1$(Source)

End Function


Function PasteObjectData(Dest)

	;FreeClothes(Dest)
	
	;ObjectHatEntity(Dest)=CurrentObjectHatEntity
	;ObjectAccEntity(Dest)=CurrentObjectAccEntity
	;ObjectHatTexture(Dest)=CurrentObjectHatTexture
	;ObjectAccTexture(Dest)=CurrentObjectAccTexture

	ObjectModelName$(Dest)=CurrentObjectModelName$
	ObjectTextureName$(Dest)=CurrentObjectTextureName$
	ObjectXScale#(Dest)=CurrentObjectXScale#
	ObjectZScale#(Dest)=CurrentObjectZScale#
	ObjectYScale#(Dest)=CurrentObjectYScale#
	ObjectXAdjust#(Dest)=CurrentObjectXAdjust#
	ObjectZAdjust#(Dest)=CurrentObjectZAdjust#
	ObjectYAdjust#(Dest)=CurrentObjectYAdjust#
	ObjectPitchAdjust#(Dest)=CurrentObjectPitchAdjust#
	ObjectYawAdjust#(Dest)=CurrentObjectYawAdjust#
	ObjectRollAdjust#(Dest)=CurrentObjectRollAdjust#
		
	;xyz position is not changed
	;oldxyz is not copied
	ObjectDX(Dest)=CurrentObjectDX
	ObjectDY(Dest)=CurrentObjectDY
	ObjectDZ(Dest)=CurrentObjectDZ
	
	ObjectPitch(Dest)=CurrentObjectPitch
	ObjectYaw(Dest)=CurrentObjectYaw
	ObjectRoll(Dest)=CurrentObjectRoll
	ObjectPitch2(Dest)=CurrentObjectPitch2
	ObjectYaw2(Dest)=CurrentObjectYaw2
	ObjectRoll2(Dest)=CurrentObjectRoll2


	ObjectXGoal(Dest)=CurrentObjectXGoal
	ObjectYGoal(Dest)=CurrentObjectYGoal
	ObjectZGoal(Dest)=CurrentObjectZGoal
	
	ObjectMovementType(Dest)=CurrentObjectMovementType
	ObjectMovementTypeData(Dest)=CurrentObjectMovementTypeData
	ObjectSpeed(Dest)=CurrentObjectSpeed
	ObjectRadius(Dest)=CurrentObjectRadius
	ObjectRadiusType(Dest)=CurrentObjectRadiusType
	
	ObjectData10(Dest)=CurrentObjectData10
	
	ObjectPushDX(Dest)=CurrentObjectPushDX
	ObjectPushDY(Dest)=CurrentObjectPushDY

	
	ObjectAttackPower(Dest)=CurrentObjectAttackPower
	ObjectDefensePower(Dest)=CurrentObjectDefensePower
	ObjectDestructionType(Dest)=CurrentObjectDestructionType
	

	ObjectID(Dest)=CurrentObjectID
	ObjectType(Dest)=CurrentObjectType
	ObjectSubType(Dest)=CurrentObjectSubType
	
	ObjectActive(Dest)=CurrentObjectActive
	ObjectLastActive(Dest)=CurrentObjectLastActive
	ObjectActivationType(Dest)=CurrentObjectActivationType
	ObjectActivationSpeed(Dest)=CurrentObjectActivationSpeed
	
	ObjectStatus(Dest)=CurrentObjectStatus
	ObjectTimer(Dest)=CurrentObjectTimer
	ObjectTimerMax1(Dest)=CurrentObjectTimerMax1
	ObjectTimerMax2(Dest)=CurrentObjectTimerMax2
	
	ObjectTeleportable(Dest)=CurrentObjectTeleportable
	ObjectButtonPush(Dest)=CurrentObjectButtonPush
	ObjectWaterReact(Dest)=CurrentObjectWaterReact
	
	ObjectTelekinesisable(Dest)=CurrentObjectTelekinesisable
	ObjectFreezable(Dest)=CurrentObjectFreezable
	
	ObjectReactive(Dest)=CurrentObjectReactive
	
	ObjectChild(Dest)=CurrentObjectChild
	ObjectParent(Dest)=CurrentObjectParent

	
	For k=0 To 9
		ObjectData(Dest,k)=CurrentObjectData(k)
	Next
	For k=0 To 3
		ObjectTextData$(Dest,k)=CurrentObjectTextData$(k)
	Next
	
	ObjectTalkable(Dest)=CurrentObjectTalkable
	ObjectCurrentAnim(Dest)=CurrentObjectCurrentAnim
	ObjectStandardAnim(Dest)=CurrentObjectStandardAnim
	;ObjectTileX(Dest)=CurrentObjectTileX
	;ObjectTileY(Dest)=CurrentObjectTileY
	;ObjectTileX2(Dest)=CurrentObjectTileX2
	;ObjectTileY2(Dest)=CurrentObjectTileY2
	ObjectMovementTimer(Dest)=CurrentObjectMovementTimer
	ObjectMovementSpeed(Dest)=CurrentObjectMovementSpeed
	ObjectMoveXGoal(Dest)=CurrentObjectMoveXGoal
	ObjectMoveYGoal(Dest)=CurrentObjectMoveYGoal
	ObjectTileTypeCollision(Dest)=CurrentObjectTileTypeCollision
	ObjectObjectTypeCollision(Dest)=CurrentObjectObjectTypeCollision
	ObjectCaged(Dest)=CurrentObjectCaged
	ObjectDead(Dest)=CurrentObjectDead
	ObjectDeadTimer(Dest)=CurrentObjectDeadTimer
	ObjectExclamation(Dest)=CurrentObjectExclamation
	ObjectShadow(Dest)=CurrentObjectShadow
	ObjectLinked(Dest)=CurrentObjectLinked
	ObjectLinkBack(Dest)=CurrentObjectLinkBack
	ObjectFlying(Dest)=CurrentObjectFlying
	ObjectFrozen(Dest)=CurrentObjectFrozen
	ObjectIndigo(Dest)=CurrentObjectIndigo
	ObjectFutureInt24(Dest)=CurrentObjectFutureInt24
	ObjectFutureInt25(Dest)=CurrentObjectFutureInt25
	ObjectScaleAdjust(Dest)=CurrentObjectScaleAdjust
	ObjectScaleXAdjust(Dest)=CurrentObjectScaleXAdjust
	ObjectScaleYAdjust(Dest)=CurrentObjectScaleYAdjust
	ObjectScaleZAdjust(Dest)=CurrentObjectScaleZAdjust
	ObjectFutureFloat5(Dest)=CurrentObjectFutureFloat5
	ObjectFutureFloat6(Dest)=CurrentObjectFutureFloat6
	ObjectFutureFloat7(Dest)=CurrentObjectFutureFloat7
	ObjectFutureFloat8(Dest)=CurrentObjectFutureFloat8
	ObjectFutureFloat9(Dest)=CurrentObjectFutureFloat9
	ObjectFutureFloat10(Dest)=CurrentObjectFutureFloat10
	ObjectFutureString1$(Dest)=CurrentObjectFutureString1$
	ObjectFutureString2$(Dest)=CurrentObjectFutureString1$
	
	For i=0 To 30
		ObjectAdjusterString$(Dest,i)="" ;ObjectAdjuster$(i)
	Next
	
	FreeModel(Dest)
	
	UpdateObjectEntityToCurrent(Dest)
	
	SetEntityPositionToObjectPositionWithoutZ(CurrentGrabbedObjectMarker,CurrentGrabbedObject,0.0)
	
	SomeObjectWasChanged()

	
End Function




Function DisplayAsBinaryString$(value)

Result$=""

;Str$(CurrentObjectTileTypeCollision)+"000 0000 0000 00"

For i=0 To 14
	If i Mod 5 = 0 And i>0
		Result$=Result$+" "
	EndIf
	If (value And 2^i)<>0
		Result$=Result$+"1"
	Else
		Result$=Result$+"0"
	EndIf
Next

Return Result$

End Function



Function AdjusterAppearsInWop(adjuster$)

	For i=0 To NofWopAdjusters-1
		If ObjectAdjusterWop$(i)=adjuster$
			Return True
		EndIf
	Next
	
	Return False

End Function



Function DisplayObjectAdjuster(i)

	tex2$=ObjectAdjuster$(i)
	CrossedOut=False
	StartX=510
	StartY=305

	Select ObjectAdjuster$(i)
	Case "TextureName"
		tex2$="Texture"
		tex$=CurrentObjectTextureName$
		;If Left$(tex$,1)="?" ; object re-texture
		;	tex$=Right$(tex$,Len(tex$)-1)
		;EndIf
	
	Case "ModelName"
		tex2$="Model"
		tex$=CurrentObjectModelName$
		;If Left$(tex$,1)="?" ; object re-model
		;	tex$=Right$(tex$,Len(tex$)-1)
		;EndIf
	
	Case "XAdjust"
		tex$=Str$(CurrentObjectXAdjust)
		CrossedOut=RandomXAdjust
		LeftAdj$=RandomXAdjustMin
		RightAdj$=RandomXAdjustMax
	Case "YAdjust"
		tex$=Str$(CurrentObjectYAdjust)
		CrossedOut=RandomYAdjust
		LeftAdj$=RandomYAdjustMin
		RightAdj$=RandomYAdjustMax
	Case "ZAdjust"
		tex$=Str$(CurrentObjectZAdjust)
		CrossedOut=RandomZAdjust
		LeftAdj$=RandomZAdjustMin
		RightAdj$=RandomZAdjustMax
	
	Case "XScale"
		tex$=Str$(CurrentObjectXScale)
		CrossedOut=RandomXScale
		LeftAdj$=RandomXScaleMin
		RightAdj$=RandomXScaleMax
	Case "YScale"
		tex$=Str$(CurrentObjectYScale)
		CrossedOut=RandomYScale
		LeftAdj$=RandomYScaleMin
		RightAdj$=RandomYScaleMax
	Case "ZScale"
		tex$=Str$(CurrentObjectZScale)
		CrossedOut=RandomZScale
		LeftAdj$=RandomZScaleMin
		RightAdj$=RandomZScaleMax
		
	Case "DefensePower"
		tex$=Str$(CurrentObjectDefensePower)
		tex2$="Greeting"
		Select CurrentObjectDefensePower
		Case 0
			tex$="Stinky1"
		Case 1
			tex$="Stinky2"
		Case 2
			tex$="Loof1"
		Case 3
			tex$="Loof2"
		Case 4
			tex$="Qookie1"
		Case 5
			tex$="Qookie2"
		Case 6
			tex$="Peegue1"
		Case 7
			tex$="Peegue2"
		Case 8
			tex$="Qookie3"
		Case 9
			tex$="Qookie4"

		Case 10,11,12,13,14,15,16,17,18
			tex$="Wee "+Str$(CurrentObjectDefensePower-9)
		Case 19,20,21
			tex$="Kaboom "+Str$(CurrentObjectDefensePower-18)

		Case 22,23,24
			tex$="ZBot "+Str$(CurrentObjectDefensePower-21)

		Case 25
			tex$="Chomper"
		Case 26
			tex$="Thwart 1"
		Case 27
			tex$="Thwart 2"
		Case 28
			tex$="Troll 1"
		Case 29
			tex$="Troll 2"
		Case 30
			tex$="Monster"
		Case 31
			tex$="Stinky 3"
		Case 32
			tex$="Stinky 4"
		Case 33
			tex$="Stinky 5"

		End Select
		
		CrossedOut=RandomDefensePower
		LeftAdj$=RandomDefensePowerMin
		RightAdj$=RandomDefensePowerMax
		
		
		
	Case "AttackPower"
		tex$=Str$(CurrentObjectAttackPower)
		
	Case "DestructionType"
		tex$=Str$(CurrentObjectDestructionType)
		Select CurrentObjectDestructionType
			Case 0
				tex$="None"
			Case 1
				tex$="White"
			Case 2
				tex$="MODDED" ; Purple
		End Select


	Case "YawAdjust"
		tex$=Str$(CurrentObjectYawAdjust)
		CrossedOut=RandomYawAdjust
		LeftAdj$=RandomYawAdjustMin
		RightAdj$=RandomYawAdjustMax
	Case "PitchAdjust"
		tex$=Str$(CurrentObjectPitchAdjust)
		CrossedOut=RandomPitchAdjust
		LeftAdj$=RandomPitchAdjustMin
		RightAdj$=RandomPitchAdjustMax
	Case "RollAdjust"
		tex$=Str$(CurrentObjectRollAdjust)
		CrossedOut=RandomRollAdjust
		LeftAdj$=RandomRollAdjustMin
		RightAdj$=RandomRollAdjustMax
	
	Case "ID"
		tex$=Str$(CurrentObjectID)
		CrossedOut=RandomID
		LeftAdj$=RandomIDMin
		RightAdj$=RandomIDMax
	Case "Type"
		tex$=Str$(CurrentObjectType)
		CrossedOut=RandomType
		LeftAdj$=RandomTypeMin
		RightAdj$=RandomTypeMax
	Case "SubType"
		tex$=Str$(CurrentObjectSubType)
		CrossedOut=RandomSubType
		LeftAdj$=RandomSubTypeMin
		RightAdj$=RandomSubTypeMax
		
		If CurrentObjectModelName$="!CustomItem"
			tex2$="Fn"
			If CurrentObjectSubType>=0 And CurrentObjectSubType<30
				If CurrentObjectSubType Mod 10=0
					tex$="None"
				Else If CurrentObjectSubType Mod 10=1
					tex$="Win Adventure"
				Else If CurrentObjectSubType Mod 10=2
					tex$="ID On Local"
				Else If CurrentObjectSubType Mod 10=3
					tex$="ID Off Local"
				Else If CurrentObjectSubType Mod 10=4
					tex$="ID Tog Local"
				Else If CurrentObjectSubType Mod 10=5
					tex$="ID On Global"
				Else If CurrentObjectSubType Mod 10=6
					tex$="ID Off Global"
				Else If CurrentObjectSubType Mod 10=7
					tex$="ID Tog Global"
				EndIf
				If CurrentObjectSubType>=10 And CurrentObjectSubType<20
					tex$=tex$+" *"
				EndIf
				If CurrentObjectSubType>=20 And CurrentObjectSubType<30
					tex$=tex$+" +"
				EndIf
			Else If CurrentObjectSubType=-1
				tex$="Gloves"
			Else If CurrentObjectSubType=-2
				tex$="Lamp"
			Else If CurrentObjectSubType=-3
				tex$="GlowGem"
			Else If CurrentObjectSubType=-4
				tex$="Spy-Eye"
			Else If CurrentObjectSubType=-5
				tex$="Glyph"
			Else If CurrentObjectSubType=-6
				tex$="MapPiece"
			Else If CurrentObjectSubType=-100
				tex$="RawKey"
			Else If CurrentObjectSubType=-99
				tex$="Whistle"
			Else If CurrentObjectSubType=-98
				tex$="RawShard"
			Else If CurrentObjectSubType=-300
				tex$="RawGloves"
			Else If CurrentObjectSubType=-200
				tex$="RawLamp"
			Else If CurrentObjectSubType=-199
				tex$="RawGlowGem"
			Else If CurrentObjectSubType=-198
				tex$="RawSpyEye"
			Else If CurrentObjectSubType=-197
				tex$="RawToken"
			Else If CurrentObjectSubType=-196
				tex$="RawGlyph"
			Else If CurrentObjectSubType=-195
				tex$="RawMapPiece"
			Else If CurrentObjectSubType=-400
				tex$="Rucksack"
			Else If CurrentObjectSubType=509
				tex$="Empty"

			EndIf
			



			
		
		EndIf
		If CurrentObjectModelName$="!FireFlower"
			tex2$="Turning"
			If CurrentObjectSubType=0
				tex$="None"
			Else If CurrentObjectSubType=1
				tex$="Player"
			Else If CurrentObjectSubType=2
				tex$="ClockW"
			Else If CurrentObjectSubType=3
				tex$="CountW"
			EndIf
		EndIf
		If CurrentObjectModelName$="!Crab"
			tex2$="Type"
			If CurrentObjectSubType=0
				tex$="Green"
			Else If CurrentObjectSubType=1
				tex$="Red"
			
			EndIf
		EndIf


			
		
	Case "TimerMax1"
		tex$=Str$(CurrentObjectTimerMax1)
		CrossedOut=RandomTimerMax1
		LeftAdj$=RandomTimerMax1Min
		RightAdj$=RandomTimerMax1Max
	Case "TimerMax2"
		tex$=Str$(CurrentObjectTimerMax2)
		CrossedOut=RandomTimerMax2
		LeftAdj$=RandomTimerMax2Min
		RightAdj$=RandomTimerMax2Max
	Case "Timer"
		tex$=Str$(CurrentObjectTimer)
		CrossedOut=RandomTimer
		LeftAdj$=RandomTimerMin
		RightAdj$=RandomTimerMax


	Case "ObjectTextData0"
		; custom model
		tex2$=""
		tex$=CurrentObjectTextData$(0)
		
	Case "ObjectTextData1"
		tex2$=""
		tex$=CurrentObjectTextData$(1)

		
	Case "Active"
		If CurrentObjectActive=0
			tex$="No (0)"
		Else If CurrentObjectActive=1001
			tex$="Yes (1001)"
		Else If CurrentObjectActive Mod 2=0
			tex$="Soon No ("+CurrentObjectActive+")"
		Else
			tex$="Soon Yes ("+CurrentObjectActive+")"
		EndIf
		CrossedOut=RandomActive
		LeftAdj$=RandomActiveMin
		RightAdj$=RandomActiveMax
		
	Case "ActivationSpeed"
		tex$=Str$(CurrentObjectActivationSpeed)
		CrossedOut=RandomActivationSpeed
		LeftAdj$=RandomActivationSpeedMin
		RightAdj$=RandomActivationSpeedMax
		
	Case "ActivationType"
		If CurrentObjectActivationType=1
			tex$="GrowZ"
		Else If CurrentObjectActivationType=2
			tex$="GrowXYZ"
		Else If CurrentObjectActivationType=3
			tex$="GrowXY"
		Else If CurrentObjectActivationType=11
			tex$="GoDown"
		Else If CurrentObjectActivationType=12
			tex$="Bridge1"
		Else If CurrentObjectActivationType=13
			tex$="Bridge2"
		Else If CurrentObjectActivationType=14
			tex$="Bridge3"
		Else If CurrentObjectActivationType=15
			tex$="Bridge4"
		Else If CurrentObjectActivationType=16
			tex$="Bridge5"
		Else If CurrentObjectActivationType=17
			tex$="GoNorth"
		Else If CurrentObjectActivationType=18
			tex$="GoEast"
		Else If CurrentObjectActivationType=19
			tex$="GoSouth"
		Else If CurrentObjectActivationType=20
			tex$="GoWest"
		Else If CurrentObjectActivationType=21
			tex$="Fade"
		Else If CurrentObjectActivationType=31
			tex$="Cage"
		Else If CurrentObjectActivationType=41
			tex$="DungeonDoor"
		Else
			tex$=Str$(CurrentObjectActivationType)
		EndIf
		CrossedOut=RandomActivationType
		LeftAdj$=RandomActivationTypeMin
		RightAdj$=RandomActivationTypeMax



	Case "ButtonPush"
		tex$=Str$(CurrentObjectButtonPush)
		CrossedOut=RandomButtonPush
		LeftAdj$=""
		RightAdj$=""
		
	Case "WaterReact"
		tex$=Str$(CurrentObjectWaterReact)
		
	Case "Freezable"
		tex$=Str$(CurrentObjectFreezable)
		
	Case "Frozen"
		tex$=Str$(CurrentObjectFrozen)
		
	Case "Teleportable"
		tex$=Str$(CurrentObjectTeleportable)
		CrossedOut=RandomTeleportable
		LeftAdj$=""
		RightAdj$=""
	
	Case "Data0"
		tex$=Str$(CurrentObjectData(0))
		CrossedOut=RandomData(0)
		LeftAdj$=RandomDataMin(0)
		RightAdj$=RandomDataMax(0)
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="YawAnim"
		EndIf
		If CurrentObjectModelName$="!Scritter" Or CurrentObjectModelName$="!Cuboid" Or CurrentObjectModelName$="!Spring" Or CurrentObjectModelName$="!SteppingStone" Or CurrentObjectModelName$="!Transporter" Or CurrentObjectModelName$="!ColourGate" Or CurrentObjectModelName$="!Door" Or CurrentObjectModelName$="!Key" Or CurrentObjectModelName$="!KeyCard" Or CurrentObjectModelName$="!Teleport" Or CurrentObjectModelName$="!Cage"  Or CurrentObjectTextureName$="!FireTrap" Or CurrentObjectModelName$="!FlipBridge" Or CurrentObjectType=424 Or CurrentObjectModelName$="!Pushbot" Or CurrentObjectModelName$="!Autodoor" Or CurrentObjectModelName$="!Suctube" Or CurrentObjectModelName$="!Conveyor"
			tex2$="Colour"
		EndIf
		
		If CurrentObjectModelName$="!Obstacle51" Or CurrentObjectModelName$="!Obstacle55" Or CurrentObjectModelName$="!Obstacle59"
			tex2$="Shape"
		EndIf
		
		If CurrentObjectModelName$="!Obstacle48" ; (wysp ship)
			tex$="Turning"
			Select CurrentObjectData(0)
				Case 0
					tex2$="Yes"
				Default
					tex2$="No"
			End Select
		EndIf
				

 
		
		If CurrentObjectModelName$="!Button"
			If (CurrentObjectSubType Mod 32)<5
				tex2$="Colour1"
			Else If (CurrentObjectSubType Mod 32)<10
				tex2$="Col From"
			Else If (CurrentObjectSubType Mod 32)=15
				tex2$="Command"
			Else If (CurrentObjectSubType Mod 32)=16 Or (CurrentObjectSubType Mod 32)=17
				tex2$="Colour"


			EndIf
		EndIf
		If CurrentObjectModelName$="!WaterFall"
			tex2$="Type"
			Select CurrentObjectData(0)
				Case 0
					tex$="Water"
				Case 1
					tex$="Lava"
				Case 2
					tex$="Green"
			End Select
			
		
		EndIf

		If CurrentObjectType=190 Or CurrentObjectType=164
			tex2$="Particle ID"
		EndIf
		If CurrentObjectModelName$="!StarGate"
			tex2$="Cost"
		EndIf
		If CurrentObjectModelName$="!Wisp"
			tex2$="Texture"
		EndIf

		
		If CurrentObjectModelName$="!SteppingStone" tex$=Str$(CurrentObjectData(0)+8)
		
		If CurrentObjectModelName$="!CustomItem"
			tex2$="Texture"
		EndIf
		If CurrentObjectModelName$="!Gem"
			tex2$="Shape"
		EndIf
		If CurrentObjectModelName$="!Crystal"
			tex2$="Type"
			Select CurrentObjectData(0)
			Case 0
				tex$="Rainbow"
			Case 1
				tex$="Void"
			End Select

		EndIf

		If CurrentObjectModelName$="!Bowler"
			tex2$="Direction"
			If CurrentObjectData(1)=2
				Select CurrentObjectData(0)
				Case 0
					tex$="North"
				Case 1
					tex$="NorthEast"
				Case 2
					tex$="East"
				Case 3
					tex$="SouthEast"
				Case 4
					tex$="South"
				Case 5
					tex$="SouthWest"
				Case 6
					tex$="West"
				Case 7
					tex$="NorthWest"
				End Select
				
			Else
				Select CurrentObjectData(0)
				Case 0
					tex$="North"
				Case 1
					tex$="East"
				Case 2
					tex$="South"
				Case 3
					tex$="West"
				End Select
				
			EndIf
		EndIf
		If CurrentObjectModelName$="!Chomper"
			tex2$="Speed"
			tex$="+"+Str$(CurrentObjectData(0))
			
			
		EndIf
		If CurrentObjectModelName$="!FireFlower"
			tex2$="Direction"
			
			Select CurrentObjectData(0)
			Case 0
				tex$="North"
			Case 1
				tex$="NorthEast"
			Case 2
				tex$="East"
			Case 3
				tex$="SouthEast"
			Case 4
				tex$="South"
			Case 5
				tex$="SouthWest"
			Case 6
				tex$="West"
			Case 7
				tex$="NorthWest"
			End Select
		EndIf
		If CurrentObjectModelName$="!Turtle" Or (Left$(CurrentObjectModelName$,6)="!Retro" And CurrentObjectType<>424) Or CurrentObjectModelName$="!Weebot"  Or CurrentObjectModelName$="!Zapbot"
			tex2$="Direction"
			
			Select CurrentObjectData(0)
			Case 0
				tex$="North"
			Case 1
				tex$="East"
			Case 2
				tex$="South"
			Case 3
				tex$="West"
			End Select
				
			
		EndIf
		If CurrentObjectModelName$="!NPC"
			tex2$="Texture"
			
			Select CurrentObjectData(0)
			Case 1
				tex$="Blue"
			Case 2
				tex$="Purple"
			Case 3
				tex$="Red"
			Case 4
				tex$="Dark"
			Case 5
				tex$="Shadow"
			Case 6
				tex$="Fire"
			Case 7
				tex$="Green"
			Case 8
				tex$="White"
			End Select
				
			
		EndIf
		If CurrentObjectModelName$="!Kaboom"
			tex2$="Texture"
			
			Select CurrentObjectData(0)
			Case 1
				tex$="Blue"
			Case 2
				tex$="Purple"
			Case 3
				tex$="Red"
			Case 4
				tex$="Gold"
			Case 5
				tex$="Dark"
			End Select
				
			
		EndIf

		If CurrentObjectModelName$="!Rubberducky"
			tex2$="Move"
			If CurrentObjectData(0)=0 
				tex$="No"
			Else
				tex$="Yes"
			EndIf
			
			
		EndIf
		

		If CurrentObjectType=51 Or CurrentObjectTextureName$="!GloveTex" ; spellball generator
			tex2$="Spell"
			tex$=GetMagicNameAndId(CurrentObjectData(0))
		EndIf
		
		If CurrentObjectModelName$="!Mothership"
			tex2$="TimerMax"
		EndIf	
		If CurrentObjectModelName$="!Ghost" Or CurrentObjectModelName$="!Wraith"
			tex2$="Radius"
		EndIf
		
		If CurrentObjectModelName$="!GrowFlower"
			tex2$="TileLogic"
			tex$=LogicIdToLogicName$(CurrentObjectData(0))
		EndIf






	Case "Data1"
		tex$=Str$(CurrentObjectData(1))
		CrossedOut=RandomData(1)
		LeftAdj$=RandomDataMin(1)
		RightAdj$=RandomDataMax(1)
		
		If CurrentObjectModelName$="!Spring" Or CurrentObjectModelName$="!SteppingStone" Or CurrentObjectModelName$="!Transporter" Or CurrentObjectModelName$="!ColourGate" Or CurrentObjectModelName$="!Door" Or CurrentObjectModelName$="!Key"  Or CurrentObjectModelName$="!KeyCard" Or CurrentObjectModelName$="!Teleport" Or CurrentObjectModelName$="!Cage" Or CurrentObjectTextureName$="!FireTrap" Or CurrentObjectModelName$="!FlipBridge" Or CurrentObjectModelName$="!Retrolasergate" Or CurrentObjectModelName$="!Pushbot" Or CurrentObjectModelname$="!Autodoor" Or CurrentObjectModelname$="!Suctube" Or CurrentObjectModelName$="!Conveyor"
			tex2$="SubColour"
		EndIf
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="PitchAnim"
		EndIf

		
		If CurrentObjectModelName$="!Obstacle51" Or CurrentObjectModelName$="!Obstacle55" Or CurrentObjectModelName$="!Obstacle59"
			tex2$="Texture"
		EndIf
		
		If CurrentObjectModelName$="!Cuboid"
			tex2$="Turning"
			If CurrentObjectData(1)=0 
				tex$="No"
			Else
				tex$="Yes"
			EndIf
		EndIf
		
		If CurrentObjectModelName$="!Button"
			If (CurrentObjectSubType Mod 32)<5
				tex2$="Colour2"
			Else If (CurrentObjectSubType Mod 32)<10
				tex2$="Col To"
			Else If (CurrentObjectSubType Mod 32)=16 Or (CurrentObjectSubType Mod 32)=17
				tex2$="SubColour"
			Else If CurrentObjectSubType = 10
				tex2$="Dest Level"
			Else If CurrentObjectSubType = 11
				tex2$="NPC ID"
			EndIf
			
		EndIf
		
		If CurrentObjectType=190
			tex2$="Intensity"
			If CurrentObjectData(1)=1 tex$="Low"
			If CurrentObjectData(1)=2 tex$="Reg"
			If CurrentObjectData(1)=3 tex$="High"
			
		EndIf
		If CurrentObjectModelName$="!StarGate" 
			tex2$="Type"
			If CurrentObjectData(1)=0 
				tex$="Star"
			Else
				tex$="Coin"
			EndIf
			
		EndIf
		If CurrentObjectModelName$="!FireFlower"
			tex2$="Type"
			
			Select CurrentObjectData(1)
			Case 0
				tex$="Fire"
			Case 1
				tex$="Ice"
			Case 2
				tex$="Null"
			Case 3
				tex$="Bounce"
			End Select
		EndIf

		If CurrentObjectModelName$="!CustomItem"
			tex2$="Fn ID"
		EndIf
		If CurrentObjectModelName$="!Gem"
			tex2$="Colour"
		EndIf

		If CurrentObjectModelName$="!Bowler"
			tex2$="Type"
			If CurrentObjectData(1)=0
				tex$="Bounce Left"
			Else If CurrentObjectData(1)=1
				tex$="Bounce Right"
			Else
				tex$="Bounce Diag"
			EndIf
		EndIf
		If CurrentObjectModelName$="!Chomper"
			tex2$="Special"
			If CurrentObjectData(1)=0
				tex$="---"
			Else If currentobjectData(1)=1
				tex$="Ghost"
			Else If currentobjectData(1)=2
				tex$="Glow"
			Else
				tex$="Mecha"
			EndIf
			
			
		EndIf
		If CurrentObjectModelName$="!Turtle"
			tex2$="Turn"
			If CurrentObjectData(1)=0
				tex$="Left"
			Else If CurrentObjectData(1)=1
				tex$="Right"
			
			EndIf
		EndIf
		
		If CurrentObjectModelName$="!Crab"
			tex2$="Status"
			If CurrentObjectData(1)=0
				tex$="Awake"
			Else If CurrentObjectData(1)=1
				tex$="Curious"
			Else If CurrentObjectData(1)=2
				tex$="Asleep"
			Else If CurrentObjectData(1)=3
				tex$="Disabled"

			
			EndIf
		EndIf



		If CurrentObjectModelName$="!NPC" 
			tex2$="Expression"
			
			Select CurrentObjectData(1)
			Case 0
				tex$="Happy"
			Case 1
				tex$="Surprised"
			Case 2
				tex$="Sad"
			Case 3
				tex$="Asleep"
			Case 4
				tex$="Angry"
			End Select
				
			
		EndIf
		
	

		If CurrentObjectModelName$="!Thwart" Or CurrentObjectModelName$="!Troll"
			tex2$="Anim"
			If CurrentObjectData(1)=0
				tex$="Normal"
			Else If CurrentObjectData(1)=1
				tex$="Hands Up"
			
			EndIf
		EndIf
		


		If CurrentObjectModelName$="!Sign"
			tex2$="Texture"
		EndIf
		
		If CurrentObjectType=51 ; spellball generator
			tex2$="Goal X"
		EndIf

		If  (Left$(CurrentObjectModelName$,6)="!Retro" And CurrentObjectModelName$<>"!Retrolasergate") Or CurrentObjectModelName$="!Weebot"  Or CurrentObjectModelName$="!Zapbot"

			tex2$="Turning"
			If CurrentObjectData(1)=0
				tex$="Left"
			Else If CurrentObjectData(1)=1
				tex$="Right"
			
			EndIf
		EndIf
		
		If CurrentObjectModelName$="!Ghost" 
			tex2$="Speed"
		EndIf
		If CurrentObjectModelName$="!Wraith"
			tex2$="ShotTime"
		EndIf
		


		
	Case "Data2" 
		tex$=Str$(CurrentObjectData(2))
		CrossedOut=RandomData(2)
		LeftAdj$=RandomDataMin(2)
		RightAdj$=RandomDataMax(2)
		
		If CurrentObjectModelName$="!Spring"  Or CurrentObjectModelName$="!Transporter" Or CurrentObjectModelName$="!FlipBridge"  Or CurrentObjectModelName$="!Pushbot" Or CurrentObjectModelName$="!Suctube"  Or CurrentObjectModelName$="!SuctubeX" Or CurrentObjectModelName$="!Conveyor"

			tex2$="Direction"
			If CurrentObjectModelName$="!Transporter" Then tex$=Str$(3-CurrentObjectData(2))
		EndIf
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="RollAnim"
		EndIf

		If CurrentObjectModelName$="!Button"
			If (CurrentObjectSubType Mod 32)<5
				tex2$="Colour3"
			Else If (CurrentObjectSubType Mod 32)<10
				tex2$="SubCol From"
			Else If (CurrentObjectSubType Mod 32)=16 Or (CurrentObjectSubType Mod 32)=17
				tex2$="Direction"
			Else If CurrentObjectSubType = 10
				tex2$="Dest X"
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=0
				tex2$="X Goal"
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=1
				tex2$="Dialog"
				If CurrentObjectData(2)=0 Then	tex$="None"
				If CurrentObjectData(2)=-1 Then	tex$="No Change"
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=2
				tex2$="Particle #"



			EndIf
		EndIf
		If CurrentObjectType=190
			tex2$="Direction"
			If CurrentObjectData(2)=0 tex$="Up"
			If CurrentObjectData(2)=1 tex$="Down"
			If CurrentObjectData(2)=2 tex$="East"
			If CurrentObjectData(2)=3 tex$="West"
			If CurrentObjectData(2)=4 tex$="North"
			If CurrentObjectData(2)=5 tex$="South"
			
		EndIf
		If CurrentObjectModelName$="!ColourGate"
			tex2$="Frame"
		EndIf
		If CurrentObjectModelName$="!Bowler"
			tex2$="Speed"
			tex$="+"+Str$(CurrentObjectData(2))
			
			
		EndIf
		
		If CurrentObjectModelName$="!Gem"
			tex2$="XOffset"

		EndIf

		
		If CurrentObjectModelName$="!NPC"
			tex2$="Hat"
			
			Select CurrentObjectData(2)
			Case 0
				tex$="None"
			Case 1
				tex$="Cap"
			Case 2
				tex$="Top Hat"
			Case 3
				tex$="Builder"
			Case 4
				tex$="Farmer"
			Case 5
				tex$="Wizard"
			Case 6
				tex$="Bowler"
			Case 7
				tex$="BaseBall"
			Case 8
				tex$="Beanie"
			Case 9
				tex$="Crown"
			Case 10
				tex$="Cape"
			Case 11
				tex$="Clown"
			Case 12
				tex$="Jewels"
			Case 13
				tex$="Feather"
			Case 14
				tex$="Flowerpot"
			Case 15
				tex$="SillyBase"
			Case 16
				tex$="Pirate"
			Case 17
				tex$="Safari"
			Case 18
				tex$="RobinHood"
			Case 19
				tex$="Snowball"
			Case 20
				tex$="Sombrero"
			Case 21
				tex$="ZBot"
			Case 22
				tex$="Santa"
			Case 23
				tex$="Captain"
			Case 24
				tex$="Bicorn"
			Case 25
				tex$="Cowboy"
			Case 26
				tex$="FlatRed"
			Case 27
				tex$="Flower1"
			Case 28
				tex$="Flower2"
			Case 29
				tex$="Legion"
			Case 30
				tex$="Hat-Ring"
			Case 31
				tex$="BandRing1"
			Case 32
				tex$="BandRing2"
			Case 33
				tex$="Fedora"
			Case 34
				tex$="Leaf"
			Case 35
				tex$="Nest"
			Case 36
				tex$="Pirate1"
			Case 37
				tex$="Pirate2"
			Case 38
				tex$="Sailor1"
			Case 39
				tex$="Sailor2"
			Case 40
				tex$="Wrap"
			Case 41
				tex$="Sunhat"
			Case 42
				tex$="Helmet"
			Case 43
				tex$="Fez"
			Case 44
				tex$="Sunhat2"
			Case 45
				tex$="Chef"
			Case 46
				tex$="Bowtie"
			Case 47
				tex$="Helmet2"
			Case 48
				tex$="Headphone"
			Case 49
				tex$="Viking"
			Case 50
				tex$="Welder"
			Case 51
				tex$="Punk"
			Case 52
				tex$="Ninja"
			Case 53
				tex$="Bike"
			Case 54
				tex$="RainbwCap"
			Case 55
				tex$="Antenna"
			Case 56
				tex$="Janet"













				

						
			End Select
				
			
		EndIf
		
		If CurrentObjectModelName$="!Thwart" 
			tex2$="Colour"
			If CurrentObjectData(2)=0
				tex$="Standard"
			Else If CurrentObjectData(2)=1
				tex$="Red"
			Else If CurrentObjectData(2)=2
				tex$="Orange"
			Else If CurrentObjectData(2)=3
				tex$="Yellow"
			Else If CurrentObjectData(2)=4
				tex$="Green"
			Else If CurrentObjectData(2)=5
				tex$="Blue"
			Else If CurrentObjectData(2)=6
				tex$="Indigo"
			Else If CurrentObjectData(2)=7
				tex$="Purple"

			
			EndIf
		EndIf
		If CurrentObjectModelName$="!ZbotNPC" 
			tex2$="Turning"
			If CurrentObjectData(2)=0
				tex$="Player"
			Else
				tex$="Fixed"
			EndIf
			
		EndIf

		If CurrentObjectModelName$="!Sign"
			tex2$="Move"
			
				
			Select CurrentObjectData(2)
			Case 0
				tex$="No"
			Case 1
				tex$="Sway"
			Case 2
				tex$="Bounce"
			Case 3
				tex$="Turn"
			End Select
				
			
		EndIf
		If CurrentObjectType=51 ; spellball generator
			tex2$="Goal Y"
		EndIf
		
		If  CurrentObjectModelName$="!Zapbot"
			tex2$="Speed"
		EndIf
		
		If  CurrentObjectModelName$="!Cuboid"
			tex2$="Explo Command"
		EndIf
		
		If CurrentObjectModelName$="!Mothership"
			tex2$="SourceX"
		EndIf
		
		If CurrentObjectModelName$="!Wraith"
			tex2$="Magic"
			Select CurrentObjectData(2)
			Case 0
				tex$="Fire"
			Case 1
				tex$="Ice"
			Case 2
				tex$="Grow"
			End Select
		EndIf



		
	Case "Data3"
		tex$=Str$(CurrentObjectData(3))
		CrossedOut=RandomData(3)
		LeftAdj$=RandomDataMin(3)
		RightAdj$=RandomDataMax(3)
		
		If CurrentObjectModelName$="!Suctube"  Or CurrentObjectModelName$="!SuctubeX"
			tex2$="Style"
			
		EndIf
		If CurrentObjectModelName$="!CustomModel"
			tex2$="XAnim"
		EndIf

		If CurrentObjectModelName$="!Pushbot" Or CurrentObjectModelName$="!Transporter" Or CurrentObjectType=45
			tex2$="Turn"
			If CurrentObjectData(3)=0
				tex$="Left"
			Else If CurrentObjectData(3)=1

				tex$="Right"
			Else ; only for pushbots
				tex$="180"
			EndIf
		EndIf
		If currentObjectType=46
			tex2$="Cycles"
		EndIf
		If CurrentObjectModelName$="!SteppingStone"
			tex2$="Sound"
			If CurrentObjectData(3)=0
				tex$="Water"
			Else If CurrentObjectData(3)=1
				tex$="Mecha"
			Else If CurrentObjectData(3)=2
				tex$="Magic"
			Else
				tex$="Silent/Glitched"
			EndIf
		EndIf
		If CurrentObjectType=190
			tex2$="Sound"
			If CurrentObjectData(3)=0 tex$="None"
			If CurrentObjectData(3)=1 
				If CurrentObjectSubType=4 tex$="Spark"
				If CurrentObjectSubType=5 tex$="QuietMagic"

			EndIf
			If CurrentObjectData(3)=2 
				If CurrentObjectSubType=5 tex$="LoudMecha"
			EndIf
			If CurrentObjectData(3)=3 
				If CurrentObjectSubType=5 tex$="Var.Gong"
			EndIf
			If CurrentObjectData(3)=4 
				If CurrentObjectSubType=5 tex$="Grow Magic"
			EndIf
			If CurrentObjectData(3)=5 
				If CurrentObjectSubType=5 tex$="Floing Magic"
			EndIf
			If CurrentObjectData(3)=6 
				If CurrentObjectSubType=5 tex$="Gem"
			EndIf


		EndIf

		If CurrentObjectModelName$="!Button"
			If (CurrentObjectSubType Mod 32)<5
				tex2$="Colour4"
			Else If (CurrentObjectSubType Mod 32)<10
				tex2$="SubCol To"
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=0
				tex2$="Y Goal"
			Else If CurrentObjectSubType = 10
				tex2$="Dest Y"

			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=1
				tex2$="Expression"
				If CurrentObjectData(3)=0 Then tex$="Happy"
				If CurrentObjectData(3)=1 Then tex$="Surprised"
				If CurrentObjectData(3)=2 Then tex$="Sad"
				If CurrentObjectData(3)=3 Then tex$="Asleep"
				If CurrentObjectData(3)=4 Then tex$="Angry"
				If CurrentObjectData(3)=-1 Then	tex$="No Change"
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=2
				tex2$="How Many"
	

			EndIf
		EndIf
		If CurrentObjectModelName$="!FireFlower"
			tex2$="HitPoints"
		EndIf
		If CurrentObjectModelName$="!NPC"
			tex2$="Colour"
			
			Select CurrentObjectData(2)
			Case 1
				Select CurrentObjectData(3)
				Case 1
					tex$="Blue"
				Case 2
					tex$="Rainbow"
				Case 3
					tex$="Red"
				Case 4
					tex$="Green"
				Case 5
					tex$="Orange"
				Case 6
					tex$="LightBlue"
				Case 7
					tex$="Purple"
				End Select
			Case 2
				Select CurrentObjectData(3)
				Case 1
					tex$="Blue"
				Case 2
					tex$="Purple"
				Case 3
					tex$="Red"
				Case 4
					tex$="Green"
				Case 5
					tex$="Orange"
				End Select
			Case 3
				Select CurrentObjectData(3)
				Case 1
					tex$="Red"
				Case 2
					tex$="Green"
				Case 3
					tex$="Blue"
				
				End Select
			Case 5
				Select CurrentObjectData(3)
				Case 1
					tex$="Red"
				Case 2
					tex$="Orange"
				Case 3
					tex$="Yellow"
				Case 4
					tex$="Green"
				Case 5
					tex$="Blue"
				Case 6
					tex$="Indigo"
				Case 7
					tex$="Purple"
				End Select
			Case 6
				Select CurrentObjectData(3)
				Case 1
					tex$="Black"
				Case 2
					tex$="Blue"
				Case 3
					tex$="Red"
				
				End Select
			Case 7
				Select CurrentObjectData(3)
				Case 1
					tex$="WS"
				Case 2
					tex$="Red"
				Case 3
					tex$="Blue S"
				
				End Select
			Case 10
				Select CurrentObjectData(3)
				Case 1
					tex$="Blue"
				Case 2
					tex$="Purple"
				End Select
			Case 27
				Select CurrentObjectData(3)
				Case 1
					tex$="Red"
				Case 2
					tex$="Purple"
				Case 3
					tex$="Gold"
				Case 4
					tex$="Green"
				
				End Select
			Case 28
				Select CurrentObjectData(3)
				Case 1
					tex$="RedYel"
				Case 2
					tex$="YelGreen"
				Case 3
					tex$="BluePurp"
				Case 4
					Tex$="PurpRed"
				
				End Select

			Case 46
				Select CurrentObjectData(3)
				Case 1
					tex$="RedPink"
				Case 2
					tex$="BlueGold"
				Case 3
					tex$="GreeWhit"
				Case 4
					Tex$="Fall"
				Case 5
					tex$="Frosty"
				Case 6
					tex$="FullPink"
				
				End Select

			Default
				tex$="Default"
			End Select


			
				
			
		EndIf
		
		If  CurrentObjectModelName$="!Zapbot"
			tex2$="Range"
		EndIf
		
		If  CurrentObjectModelName$="!Cuboid"
			tex2$="Cmd Data1"
		EndIf
		
		If CurrentObjectModelName$="!Mothership"
			tex2$="SourceY"
		EndIf



	Case "Data4"
		tex$=Str$(CurrentObjectData(4))
		CrossedOut=RandomData(4)
		LeftAdj$=RandomDataMin(4)
		RightAdj$=RandomDataMax(4)
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="YAnim"
		EndIf

		If CurrentObjectModelName$="!Button"
			If (CurrentObjectSubType Mod 32)<5
				tex2$="SubColour1"
				
			Else If CurrentObjectSubType = 10
				tex2$="PlayerStartingYaw"
				tex$=Str$((currentobjectData(4)+180) Mod 360)
				
				
			Else If CurrentObjectSubType = 11 And (CurrentObjectData(0)=0 Or CurrentObjectData(0)=2)
				tex2$="Repeatable"
				If CurrentObjectData(4)=0
					tex$="Yes"
				Else
					tex$="No"
				EndIf
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=1
				tex2$="Yaw"
				If CurrentObjectData(4)=-1 Then	tex$="No Change"


			EndIf
		EndIf
		If CurrentObjectModelName$="!Conveyor"
			tex2$="Visual Type"
		EndIf
		
		If CurrentObjectModelName$="!Suctube"  
			tex2$="Sound"
			If CurrentObjectData(4)=0
				tex$="Normal"
			Else 
				tex$="Portal"
			EndIf
		EndIf
		

		
		If CurrentObjectType=190
			tex2$="Timing"
			If CurrentObjectData(4)=0 tex$="Random"
			If CurrentObjectData(4)=1 tex$="Synchro"
		EndIf

		If CurrentObjectModelName$="!NPC"
			tex2$="Glasses"
			
			Select CurrentObjectData(4)
			Case 0
				tex$="None"
			Case 101
				tex$="Thick Frame"
			Case 102
				tex$="Thin Large"
			Case 103
				tex$="Eyepatch L"
			Case 104
				tex$="Eyepatch R"
			Case 105
				tex$="Goggles"
			Case 106
				tex$="Parrot"
			Case 107
				tex$="Square"
			Case 108
				tex$="Round"
			Case 109
				tex$="Pink"
			Case 110
				tex$="Sword"
			Case 111
				tex$="Moustache"
			Case 112
				tex$="Rose"
			Case 113
				tex$="3D"
			Case 114
				tex$="Bolt"
			Case 115
				tex$="Monocle"
			Case 116
				tex$="Bowtie"

			Default
				tex$="Default"
			End Select
		EndIf
		
		If  CurrentObjectModelName$="!Zapbot" Or CurrentObjectModelName$="!Ufo"

			tex2$="Track"
		EndIf
		
		If CurrentObjectModelName$="!Autodoor"
			If CurrentObjectData(4)>=0
				tex2$="ActivateID"
			Else
				tex2$="ActivateType"
				tex$=Str$(-CurrentObjectData(4))
			EndIf
		EndIf

		If  CurrentObjectModelName$="!Cuboid"
			tex2$="Cmd Data2"
		EndIf
		If CurrentObjectModelName$="!Mothership"
			tex2$="FlyGoalX1"
		EndIf


	Case "Data5"
		tex$=Str$(CurrentObjectData(5))
		CrossedOut=RandomData(5)
		LeftAdj$=RandomDataMin(5)
		RightAdj$=RandomDataMax(5)
		
		If CurrentObjectModelName$="!Suctube"
			tex2$="Particles"
			If CurrentObjectData(5)=0
				tex$="Yes"
			Else
				tex$="No"
			EndIf
			
		EndIf
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="ZAnim"
		EndIf

		

		If CurrentObjectModelName$="!Button"
			If (CurrentObjectSubType Mod 32)<5
				tex2$="SubColour2"
			Else If CurrentObjectSubType = 10
				tex2$="FlyOver"
				If CurrentObjectData(5)=0
					tex$="No"
				Else
					tex$="Yes"
				EndIf


			Else If (CurrentObjectSubType Mod 32)=15 Or (CurrentObjectSubType = 11 And CurrentObjectData(0)=1)
				tex2$="Repeatable"
				If CurrentObjectData(5)=0
					tex$="Yes"
				Else
					tex$="No"
				EndIf
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=0
				tex2$="DelayTimer"


			EndIf
		EndIf
		If CurrentObjectModelName$="!Door" Or CurrentObjectModelName$="!Obstacle36" Or CurrentObjectModelName$="!Obstacle37" Or CurrentObjectModelName$="!Obstacle38" Or CurrentObjectModelName$="!Obstacle39" Or CurrentObjectModelName$="!Obstacle40"
			tex2$="Style"
		EndIf
		If CurrentObjectModelName$="!Conveyor"
			tex2$="Logic"
			If CurrentObjectData(5)=0
				tex$="Move"
			Else
				tex$="Step"
			EndIf
		EndIf
		
		
		If CurrentObjectModelName$="!NPC"
			tex2$="Colour"
			
			Select CurrentObjectData(4)
			Case 101
				Select CurrentObjectData(5)
				Case 0
					tex$="Normal"
				Case 1
					tex$="Sunglass"
				
				End Select
			Case 102
				Select CurrentObjectData(5)
				Case 0
					tex$="Black"
				Case 1
					tex$="Red"
				End Select
			End Select
		EndIf
		If CurrentObjectModelName$="!GlowWorm" Or CurrentObjectModelName$="!Zipper"
			tex2$="Red"
		EndIf
		
		If CurrentObjectModelName$="!Autodoor"
			If CurrentObjectData(5)>=0
				tex2$="ActivateID"
			Else
				tex2$="ActivateType"
				tex$=Str$(-CurrentObjectData(5))
			EndIf
		EndIf
		
		If  CurrentObjectModelName$="!Cuboid"
			tex2$="Cmd Data3"
		EndIf


		If CurrentObjectModelName$="!Mothership"
			tex2$="FlyGoalY1"
		EndIf


		
	Case "Data6"
		tex$=Str$(CurrentObjectData(6))
		CrossedOut=RandomData(6)
		LeftAdj$=RandomDataMin(6)
		RightAdj$=RandomDataMax(6)
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="XSpeed"
		EndIf

		If CurrentObjectModelName$="!Button"
			If (CurrentObjectSubType Mod 32)<5
				tex2$="SubColour3"
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=0
				tex2$="DelayReset"
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=1
				tex2$="WalkAnim"
				If CurrentObjectData(6)=0 tex$="Waddle"
				If CurrentObjectData(6)=1 tex$=" Walk "
				If CurrentObjectData(6)=2 tex$=" Run! "
				If CurrentObjectData(6)=-1 Then	tex$="No Change"
				
				


			EndIf
			
		EndIf
		If CurrentObjectModelName$="!NPC"
			tex2$="WalkAnim"
			
			Select CurrentObjectData(6)
			Case 0
				tex$="Waddle"
			Case 1
				tex$="Walk"
			Case 2
				tex$="Run"
			End Select
		EndIf
		
		If CurrentObjectModelName$="!Thwart" Or CurrentObjectModelName$="!Troll" Or CurrentObjectModelName$="!ZbotNPC"

			tex2$="Shooter"
			Select CurrentObjectData(6)
			Case 0
				tex$="No"
			Case 1
				tex$="Yes"
			
			End Select
		EndIf
		If CurrentObjectModelName$="!GlowWorm"  Or CurrentObjectModelName$="!Zipper"
			tex2$="Green"
		EndIf

		If CurrentObjectModelName$="!Autodoor"
			If CurrentObjectData(6)>=0
				tex2$="ActivateID"
			Else
				tex2$="ActivateType"
				tex$=Str$(-CurrentObjectData(6))
			EndIf
		EndIf
		If CurrentObjectModelName$="!Conveyor"
			tex2$="ActivationWait"
		EndIf
		
		If  CurrentObjectModelName$="!Cuboid"
			tex2$="Cmd Data4"
		EndIf

		If CurrentObjectModelName$="!Mothership"
			tex2$="FlyGoalX2"
		EndIf


	Case "Data7"
		tex$=Str$(CurrentObjectData(7))
		CrossedOut=RandomData(7)
		LeftAdj$=RandomDataMin(7)
		RightAdj$=RandomDataMax(7)
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="YSpeed"
		EndIf

		If CurrentObjectModelName$="!Button"
			If (CurrentObjectSubType Mod 32)<5
				tex2$="SubColour4"
			Else If CurrentObjectSubType = 11 And CurrentObjectData(0)=1
				tex2$="Turn"
				If (CurrentObjectData(7) Mod 10)=0 tex$="Fixed"
				If (CurrentObjectData(7) Mod 10)=1 tex$="Player"
				If (CurrentObjectData(7) Mod 10)=2 tex$="Clock Slow"
				If (CurrentObjectData(7) Mod 10)=3 tex$="Clock Fast"
				If (CurrentObjectData(7) Mod 10)=4 tex$="Count Slow"
				If (CurrentObjectData(7) Mod 10)=5 tex$="Count Fast"
				If CurrentObjectData(7) >=10 And CurrentObjectData(7)<20 tex$=tex$+"Bounce"
				If CurrentObjectData(7) >=20 And CurrentObjectData(7)<30 tex$=tex$+"BouFas"
				If CurrentObjectData(7)=-1 Then	tex$="No Change"
				
				
			EndIf
		EndIf
		If CurrentObjectModelName$="!NPC" Or CurrentObjectModelName$="!Kaboom"
		
			tex2$="Turn"
			If (CurrentObjectData(7) Mod 10)=0 tex$="Fixed"
			If (CurrentObjectData(7) Mod 10)=1 tex$="Player"
			If (CurrentObjectData(7) Mod 10)=2 tex$="Clock Slow"
			If (CurrentObjectData(7) Mod 10)=3 tex$="Clock Fast"
			If (CurrentObjectData(7) Mod 10)=4 tex$="Count Slow"
			If (CurrentObjectData(7) Mod 10)=5 tex$="Count Fast"
			If CurrentObjectData(7) >=10 And CurrentObjectData(7)<20 tex$=tex$+"Bounce"
			If CurrentObjectData(7) >=20 And CurrentObjectData(7)<30 tex$=tex$+"BouFas"
		EndIf
		
		If CurrentObjectModelName$="!Thwart" Or CurrentObjectModelName$="!Troll" Or CurrentObjectModelName$="!ZbotNPC"

			tex2$="TimerMax1"
		EndIf
		If CurrentObjectModelName$="!GlowWorm"  Or CurrentObjectModelName$="!Zipper"
			tex2$="Blue"
		EndIf
		
		If CurrentObjectModelName$="!Autodoor"
			tex2$="StayOnTimer"
			
		EndIf

		If CurrentObjectModelName$="!Mothership"
			tex2$="FlyGoalY2"
		EndIf



	Case "Data8"
		tex$=Str$(CurrentObjectData(8))
		CrossedOut=RandomData(8)
		LeftAdj$=RandomDataMin(8)
		RightAdj$=RandomDataMax(8)
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="ZSpeed"
		EndIf

		If CurrentObjectModelName$="!Transporter" Or CurrentObjectModelName$="!Button"
			tex2$="ActivateID"
			If CurrentObjectData(8)=0
				tex$="All"
			Else If CurrentObjectData(8)=-2
				tex$="Pla"
			EndIf
		EndIf
		If CurrentObjectModelName$="!NPC"
			
			tex2$="Anim"
			If CurrentObjectData(8)=0 tex$="Sway"
			If CurrentObjectData(8)=1 tex$="Wave Sometime"
			If CurrentObjectData(8)=2 tex$="Wave Constant"
			If CurrentObjectData(8)=3 tex$="Foot Sometime"
			If CurrentObjectData(8)=4 tex$="Foot Constant"
			If CurrentObjectData(8)=5 tex$="Dance"
			If CurrentObjectData(8)=6 tex$="Sit Constant"
			If CurrentObjectData(8)=7 tex$="Sit/Stand"
			If CurrentObjectData(8)=8 tex$="Sit/Stand/Wave"
			If CurrentObjectData(8)=9 tex$="Panic Sometime"
			If CurrentObjectData(8)=10 tex$="Panic Constant"
			
			
		EndIf
		
		If CurrentObjectModelName$="!Kaboom"
			
			tex2$="Anim"
			If CurrentObjectData(8)=0 tex$="Stand"
			If CurrentObjectData(8)=1 tex$="Sit"
			If CurrentObjectData(8)=2 tex$="Sit/Stand"
			If CurrentObjectData(8)=3 tex$="Shiver Some"
			If CurrentObjectData(8)=4 tex$="Shiver Constant"
			If CurrentObjectData(8)=5 tex$="Exercise"
	
			
			
		EndIf
		
		If CurrentObjectModelName$="!BabyBoomer"
			
			tex2$="Boom"
			If CurrentObjectData(8)=0 tex$="No"
			If CurrentObjectData(8)=1 tex$="Yes"
				
			
			
		EndIf
		If CurrentObjectModelName$="!StinkerWee"
			
			tex2$="Type"
			If CurrentObjectData(8)=0 tex$="Normal"
			If CurrentObjectData(8)=1 tex$="Green"
			If CurrentObjectData(8)=2 tex$="White"

				
			
			
		EndIf
		
		If CurrentObjectModelName$="!ZbotNPC"
			
			tex2$="IntroSound"
			If CurrentObjectData(8)=0 tex$="On"
			If CurrentObjectData(8)=1 tex$="Off"
			
		EndIf



		If CurrentObjectModelName$="!Mothership"
			tex2$="FlyGoalX3"
		EndIf



	Case "Data9"
		tex$=Str$(CurrentObjectData(9))
		CrossedOut=RandomData(9)
		LeftAdj$=RandomDataMin(9)
		RightAdj$=RandomDataMax(9)
		
		If CurrentObjectModelName$="!CustomModel"
			tex2$="Deadly"
			If CurrentObjectData(9)=0 tex$="No"
			If CurrentObjectData(9)=1 tex$="Yes"
			

		EndIf

		If CurrentObjectModelName$="!Button"
			If CurrentObjectSubType = 11 And CurrentObjectData(0)=1
				tex2$="Anim"
				If CurrentObjectData(9)=0 tex$="Sway"
				If CurrentObjectData(9)=1 tex$="Wave Sometime"
				If CurrentObjectData(9)=2 tex$="Wave Constant"
				If CurrentObjectData(9)=3 tex$="Foot Sometime"
				If CurrentObjectData(9)=4 tex$="Foot Constant"
				If CurrentObjectData(9)=5 tex$="Dance"
				If CurrentObjectData(9)=6 tex$="Sit Constant"
				If CurrentObjectData(9)=7 tex$="Sit/Stand"
				If CurrentObjectData(9)=8 tex$="Sit/Stand/Wave"
				If CurrentObjectData(9)=9 tex$="Panic Sometime"
				If CurrentObjectData(9)=10 tex$="Panic Constant"
				If CurrentObjectData(9)=-1 Then	tex$="No Change"
			EndIf
		EndIf
		
		If CurrentObjectModelName$="!Conveyor"
			tex2$="Tail Length"
		EndIf
		
				
				
		If CurrentObjectModelName$="!Mothership"
			tex2$="FlyGoalY3"
		EndIf

		
	Case "Talkable"
		tex$=Str$(CurrentObjectTalkable)
		tex2$="Dialog"
		CrossedOut=RandomTalkable
		LeftAdj$=RandomTalkableMin
		RightAdj$=RandomTalkableMax
		
	Case "MovementType"
		tex$=Str$(CurrentObjectMovementType)
		CrossedOut=RandomMovementType
		LeftAdj$=RandomMovementTypeMin
		RightAdj$=RandomMovementTypeMax
	Case "MovementTypeData"
		tex$=Str$(CurrentObjectMovementTypeData)
	Case "MovementSpeed"
		tex$=Str$(CurrentObjectMovementSpeed)
		CrossedOut=RandomMovementSpeed
		LeftAdj$=RandomMovementSpeedMin
		RightAdj$=RandomMovementSpeedMax
		
	Case "TileTypeCollision"
		tex$=DisplayAsBinaryString$(CurrentObjectTileTypeCollision)
		tex2$="TTC"
		CrossedOut=RandomTTC
		LeftAdj$=""
		RightAdj$=""
	Case "ObjectTypeCollision"
		tex$=DisplayAsBinaryString$(CurrentObjectObjectTypeCollision)
		tex2$="OTC"
		CrossedOut=RandomOTC
		LeftAdj$=""
		RightAdj$=""
						
	Case "ScaleAdjust"
		tex$=Str$(CurrentObjectScaleAdjust)
		CrossedOut=RandomScaleAdjust
		LeftAdj$=RandomScaleAdjustMin
		RightAdj$=RandomScaleAdjustMax
	Case "Exclamation"
		tex$=Str$(CurrentObjectExclamation)
		CrossedOut=RandomExclamation
		LeftAdj$=RandomExclamationMin
		RightAdj$=RandomExclamationMax
		
	Case "Linked"
		If CurrentObjectLinked=-1
			tex$="None"
		ElseIf CurrentObjectLinked=-2
			tex$="Pla"
		Else
			tex$=Str$(CurrentObjectLinked)
		EndIf
	Case "LinkBack"
		If CurrentObjectLinkBack=-1
			tex$="None"
		ElseIf CurrentObjectLinkBack=-2
			tex$="Pla"
		Else
			tex$=Str$(CurrentObjectLinkBack)
		EndIf
	
	Case "Parent"
		tex$=Str$(CurrentObjectParent)
	Case "Child"
		tex$=Str$(CurrentObjectChild)
		
	Case "DX"
		tex$=Str$(CurrentObjectDX)
	Case "DY"
		tex$=Str$(CurrentObjectDY)
	Case "DZ"
		tex$=Str$(CurrentObjectDZ)
		
	Case "MoveXGoal"
		tex$=Str$(CurrentObjectMoveXGoal)
		CrossedOut=RandomMoveXGoal
		LeftAdj$=RandomMoveXGoalMin
		RightAdj$=RandomMoveXGoalMax
	Case "MoveYGoal"
		tex$=Str$(CurrentObjectMoveYGoal)
		CrossedOut=RandomMoveYGoal
		LeftAdj$=RandomMoveYGoalMin
		RightAdj$=RandomMoveYGoalMax
		
	Case "Data10"
		tex$=Str$(CurrentObjectData10)
		
	Case "Caged"
		tex$=Str$(CurrentObjectCaged)
		
	Case "Dead"
		tex$=Str$(CurrentObjectDead)
		Select CurrentObjectDead
			Case 1
				tex$="Spinning"
			Case 3
				tex$="Sinking"
		End Select
		CrossedOut=RandomDead
		LeftAdj$=RandomDeadMin
		RightAdj$=RandomDeadMax
		
	Case "DeadTimer"
		tex$=Str$(CurrentObjectDeadTimer)
	Case "MovementTimer"
		tex$=Str$(CurrentObjectMovementTimer)
		
	Case "Flying"
		State$="Grounded"
		If CurrentObjectFlying/10 = 1	; bounced by spring
			If CurrentObjectFlying Mod 10 >=1 And CurrentObjectFlying Mod 10<=3 Then State$="Spr East"
			If CurrentObjectFlying Mod 10 >=5 And CurrentObjectFlying Mod 10<=7 Then State$="Spr West"
			If CurrentObjectFlying Mod 10 >=3 And CurrentObjectFlying Mod 10<=5 Then State$="Spr South"
			If CurrentObjectFlying Mod 10 >=7 Or CurrentObjectFlying Mod 10<=1 Then State$="Spr North"
		EndIf
	
		If CurrentObjectFlying/10 = 2	; on ice
			If CurrentObjectFlying Mod 10 >=1 And CurrentObjectFlying Mod 10<=3 Then State$="Ice East"
			If CurrentObjectFlying Mod 10 >=5 And CurrentObjectFlying Mod 10<=7 Then State$="Ice West"
			If CurrentObjectFlying Mod 10 >=3 And CurrentObjectFlying Mod 10<=5 Then State$="Ice South"
			If CurrentObjectFlying Mod 10 >=7 Or CurrentObjectFlying Mod 10<=1 Then State$="Ice North"
		EndIf

		tex$=CurrentObjectFlying+" ("+State+")"
		
	Case "Indigo"
		tex$=Str$(CurrentObjectIndigo)
		
	Case "Speed"
		tex$=Str$(CurrentObjectSpeed)
	Case "Radius"
		tex$=Str$(CurrentObjectRadius)
		
	Case "Status"
		tex$=Str$(CurrentObjectStatus)
		CrossedOut=RandomStatus
		LeftAdj$=RandomStatusMin
		RightAdj$=RandomStatusMax





	
	End Select	
	
	
	If HighlightWopAdjusters And AdjusterAppearsInWop(ObjectAdjuster$(i))
		Color TextAdjusterHighlightedR,TextAdjusterHighlightedG,TextAdjusterHighlightedB
	Else
		Color TextAdjusterR,TextAdjusterG,TextAdjusterB
	EndIf
	
	TextY=StartY+15+(i-ObjectAdjusterStart)*15
	If CrossedOut
		;Text StartX+8,StartY+15+(i-ObjectAdjusterStart)*15,"--------------------"
		tex$=tex2$
		Dashes$=""
		For t=1 To Len(tex2$)
			Dashes$=Dashes$+"-"
		Next
		
		HalfNameWidth=4*Len(tex$)
		
		Text StartX+92-HalfNameWidth,TextY,Dashes$
		
		;Text StartX+18-4*Len(LeftAdj$),TextY,LeftAdj$
		;Text StartX+166-4*Len(RightAdj$),TextY,RightAdj$
		
		Text StartX+2,TextY,LeftAdj$
		Text StartX+182-8*Len(RightAdj$),TextY,RightAdj$
		
		;Text StartX+80-HalfNameWidth-8*Len(LeftAdj$),TextY,LeftAdj$
		;Text StartX+104+HalfNameWidth,TextY,RightAdj$
		
	ElseIf tex2$<>"" And ObjectAdjuster$(i)<>"ObjectTextData0" And ObjectAdjuster$(i)<>"ObjectTextData1"
		tex$=tex2$+": "+tex$
	EndIf
	
	Text StartX+92-4*Len(tex$),TextY,tex$

End Function

Function GetMagicName$(id)
	Select id
		Case 0
			Return "Floing"
		Case 1
			Return "Pow"
		Case 2
			Return "Pop"
		Case 3
			Return "Grow"
		Case 4
			Return "Brrr"
		Case 5
			Return "Flash"
		Case 6
			Return "Blink"
		Case 7
			Return "Null"
		Case 8
			Return "Bounce"
		Case 9
			Return "Barrel"
	End Select
End Function

Function GetMagicNameAndId$(id)
	Return Str(id) + ". " + GetMagicName(id)
End Function


Function GapSubType(SmallerExclusive,LargerExclusive)

	If CurrentObjectSubType>SmallerExclusive And CurrentObjectSubType<LargerExclusive-20
		CurrentObjectSubType=LargerExclusive
	Else If CurrentObjectSubType>LargerExclusive-19 And CurrentObjectSubType<LargerExclusive
		CurrentObjectSubType=SmallerExclusive
	EndIf

End Function

Function OnLeftHalfAdjuster()

	Return MouseX()<602

End Function

Function InputString$(title$)
	FlushKeys
	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
	Result$=Input$(title$)
	ReturnKeyReleased=False
	Return Result$
End Function

Function InputInt(title$)
	FlushKeys
	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
	Result=Input(title$)
	ReturnKeyReleased=False
	Return Result
End Function

Function InputFloat#(title$)
	FlushKeys
	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
	Result$=Input$(title$)
	ReturnKeyReleased=False
	Return Result$
End Function


Function AdjustInt(ValueName$, CurrentValue, NormalSpeed, FastSpeed, DelayTime)

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True
	
	Adj=NormalSpeed
	If Fast Adj=FastSpeed
	
	If LeftMouse=True
		If RawInput=True
			Return InputInt(ValueName$)
		Else
			Delay DelayTime
			Return CurrentValue+Adj
		EndIf
	EndIf
	If RightMouse=True
		Delay DelayTime
		Return CurrentValue-Adj
	EndIf
	Return CurrentValue+MouseScroll*Adj

End Function

Function AdjustFloat#(ValueName$, CurrentValue#, NormalSpeed#, FastSpeed#, DelayTime)

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True
	
	Adj#=NormalSpeed
	If Fast Adj#=FastSpeed
	
	If LeftMouse=True
		If RawInput=True
			Return InputFloat#(ValueName$)
		Else
			Delay DelayTime
			Return CurrentValue+Adj
		EndIf
	EndIf
	If RightMouse=True
		Delay DelayTime
		Return CurrentValue-Adj
	EndIf
	Return CurrentValue+MouseScroll*Adj

End Function


Function AdjustObjectData(i, NormalSpeed, FastSpeed, DelayTime)

	If RandomData(i)
		If OnLeftHalfAdjuster()
			RandomDataMin(i)=AdjustInt("Data"+i+" Min: ", RandomDataMin(i), NormalSpeed, FastSpeed, DelayTime)
		Else
			RandomDataMax(i)=AdjustInt("Data"+i+" Max: ", RandomDataMax(i), NormalSpeed, FastSpeed, DelayTime)
		EndIf
	Else
		CurrentObjectData(i)=AdjustInt("Data"+i+": ", CurrentObjectData(i), NormalSpeed, FastSpeed, DelayTime)
	EndIf
	If ReturnPressed()
		RandomData(i)=Not RandomData(i)
	EndIf

End Function


Function InputTextureName(Prompt$)

	CurrentObjectTextureName$=InputString$(Prompt$)
	If Left$(CurrentObjectTextureName$,1)="/"
		CurrentObjectTextureName$="userdata/custom/models/"+Right$(CurrentObjectTextureName$,Len(CurrentObjectTextureName$)-1)
	Else
		CurrentObjectTextureName$=TexturePrefix$+CurrentObjectTextureName$
	EndIf

End Function


Function InputModelName(Prompt$)

	CurrentObjectModelName$=InputString$(Prompt$)
	If CurrentObjectModelName$="!CustomModel"
		CurrentObjectTextData$(0)=InputString$("Enter custom model name (e.g. Default): ")
	ElseIf Left$(CurrentObjectModelName$,1)="/" Or Left$(CurrentObjectModelName$,1)="?"
		CurrentObjectTextData$(0)=Right$(CurrentObjectModelName$,Len(CurrentObjectModelName$)-1)
		CurrentObjectModelName$="!CustomModel"
	EndIf

End Function


Function ConfirmFindAndReplace()

	Return GetConfirmation("Find and replace on matching values for ALL objects?")

End Function


Function AdjustObjectAdjuster(i)

	; avoid false positives from pressing enter
	If LeftMouse=True Or RightMouse=True Or MouseScroll<>0
		CurrentGrabbedObjectModified=True
	EndIf

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True
	
	DelayTime=150
	SlowInt=1
	FastInt=10
	FastID=50
	FastTimer=25
	FastRotate=45
	SlowFloat#=0.01
	FastFloat#=0.1
	SlowScale#=0.001

	Select ObjectAdjuster$(i)
	Case "ObjectTextData0"
		If LeftMouse=True
			If AltDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObjectTextData$(0)
					CurrentObjectTextData$(0)=InputString$("Replacement TextData0: ")
					For j=0 To NofObjects-1
						If ObjectTextData$(j,0)=Target$
							ObjectTextData$(j,0)=CurrentObjectTextData$(0)
							UpdateObjectModel(j)
						EndIf
					Next
				EndIf
			Else
				CurrentObjectTextData$(0)=InputString$("TextData0: ")
			EndIf
		EndIf
	Case "ObjectTextData1"
		If LeftMouse=True
			If AltDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObjectTextData$(1)
					CurrentObjectTextData$(1)=InputString$("Replacement TextData1: ")
					For j=0 To NofObjects-1
						If ObjectTextData$(j,1)=Target$
							ObjectTextData$(j,1)=CurrentObjectTextData$(1)
							UpdateObjectModel(j)
						EndIf
					Next
				EndIf
			Else
				CurrentObjectTextData$(1)=InputString$("TextData1: ")
			EndIf
		EndIf
	Case "TextureName"
		If LeftMouse=True
			If AltDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObjectTextureName$
					InputTextureName("Replacement TextureName: ")
					For j=0 To NofObjects-1
						If ObjectTextureName$(j)=Target$
							ObjectTextureName$(j)=CurrentObjectTextureName$
							UpdateObjectModel(j)
						EndIf
					Next
				EndIf
			Else
				InputTextureName("TextureName: ")
			EndIf
		EndIf
		
	Case "ModelName"
		If LeftMouse=True
			If AltDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObjectModelName$
					InputModelName("Replacement ModelName: ")
					For j=0 To NofObjects-1
						If ObjectModelName$(j)=Target$
							ObjectModelName$(j)=CurrentObjectModelName$
							If CurrentObjectModelName$="!CustomModel"
								ObjectTextData$(j,0)=CurrentObjectTextData$(0)
							EndIf
							UpdateObjectModel(j)
						EndIf
					Next
				EndIf
			Else
				InputModelName("ModelName: ")
			EndIf
		EndIf
		
	Case "DefensePower"
		If RandomDefensePower
			If OnLeftHalfAdjuster()
				RandomDefensePowerMin=AdjustInt("DefensePower Min: ", RandomDefensePowerMin, SlowInt, FastInt, DelayTime)
			Else
				RandomDefensePowerMax=AdjustInt("DefensePower Max: ", RandomDefensePowerMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectDefensePower=AdjustInt("DefensePower: ", CurrentObjectDefensePower, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomDefensePower=Not RandomDefensePower
		EndIf
		
		If CurrentObjectDefensePower>=34 Then CurrentObjectDefensePower=0
		If CurrentObjectDefensePower<0 Then CurrentObjectDefensePower=33
		
	Case "AttackPower"
		CurrentObjectAttackPower=AdjustInt("AttackPower: ", CurrentObjectAttackPower, SlowInt, FastInt, DelayTime)
		
	Case "DestructionType"
		CurrentObjectDestructionType=AdjustInt("DestructionType: ", CurrentObjectDestructionType, SlowInt, FastInt, DelayTime)
		
	
	Case "YawAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		If RandomYawAdjust
			If OnLeftHalfAdjuster()
				RandomYawAdjustMin=AdjustFloat#("YawAdjust Min: ", RandomYawAdjustMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomYawAdjustMax=AdjustFloat#("YawAdjust Max: ", RandomYawAdjustMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectYawAdjust=AdjustFloat#("YawAdjust: ", CurrentObjectYawAdjust, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomYawAdjust=Not RandomYawAdjust
		EndIf
		
		If CurrentObjectYawAdjust>=360 Then CurrentObjectYawAdjust=CurrentObjectYawAdjust-360
		If CurrentObjectYawAdjust<0 Then CurrentObjectYawAdjust=CurrentObjectYawAdjust+360
		
	Case "PitchAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		If RandomPitchAdjust
			If OnLeftHalfAdjuster()
				RandomPitchAdjustMin=AdjustFloat#("PitchAdjust Min: ", RandomPitchAdjustMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomPitchAdjustMax=AdjustFloat#("PitchAdjust Max: ", RandomPitchAdjustMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectPitchAdjust=AdjustFloat#("PitchAdjust: ", CurrentObjectPitchAdjust, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomPitchAdjust=Not RandomPitchAdjust
		EndIf
		
		If CurrentObjectPitchAdjust>=360 Then CurrentObjectPitchAdjust=CurrentObjectPitchAdjust-360
		If CurrentObjectPitchAdjust<0 Then CurrentObjectPitchAdjust=CurrentObjectPitchAdjust+360
		
	Case "RollAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		If RandomRollAdjust
			If OnLeftHalfAdjuster()
				RandomRollAdjustMin=AdjustFloat#("RollAdjust Min: ", RandomRollAdjustMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomRollAdjustMax=AdjustFloat#("RollAdjust Max: ", RandomRollAdjustMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectRollAdjust=AdjustFloat#("RollAdjust: ", CurrentObjectRollAdjust, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomRollAdjust=Not RandomRollAdjust
		EndIf
		
		If CurrentObjectRollAdjust>=360 Then CurrentObjectRollAdjust=CurrentObjectRollAdjust-360
		If CurrentObjectRollAdjust<0 Then CurrentObjectRollAdjust=CurrentObjectRollAdjust+360


		
	Case "XAdjust"
		If RandomXAdjust
			If OnLeftHalfAdjuster()
				RandomXAdjustMin=AdjustFloat#("XAdjust Min: ", RandomXAdjustMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomXAdjustMax=AdjustFloat#("XAdjust Max: ", RandomXAdjustMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectXAdjust=AdjustFloat#("XAdjust: ", CurrentObjectXAdjust, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomXAdjust=Not RandomXAdjust
		EndIf
	Case "YAdjust"
		If RandomYAdjust
			If OnLeftHalfAdjuster()
				RandomYAdjustMin=AdjustFloat#("YAdjust Min: ", RandomYAdjustMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomYAdjustMax=AdjustFloat#("YAdjust Max: ", RandomYAdjustMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectYAdjust=AdjustFloat#("YAdjust: ", CurrentObjectYAdjust, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomYAdjust=Not RandomYAdjust
		EndIf
	Case "ZAdjust"
		If RandomZAdjust
			If OnLeftHalfAdjuster()
				RandomZAdjustMin=AdjustFloat#("ZAdjust Min: ", RandomZAdjustMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomZAdjustMax=AdjustFloat#("ZAdjust Max: ", RandomZAdjustMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectZAdjust=AdjustFloat#("ZAdjust: ", CurrentObjectZAdjust, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomZAdjust=Not RandomZAdjust
		EndIf
		
		
	Case "XScale"
		SlowFloat#=SlowScale#
		If RandomXScale
			If OnLeftHalfAdjuster()
				RandomXScaleMin=AdjustFloat#("XScale Min: ", RandomXScaleMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomXScaleMax=AdjustFloat#("XScale Max: ", RandomXScaleMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectXScale=AdjustFloat#("XScale: ", CurrentObjectXScale, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomXScale=Not RandomXScale
		EndIf
	Case "YScale"
		SlowFloat#=SlowScale#
		If RandomYScale
			If OnLeftHalfAdjuster()
				RandomYScaleMin=AdjustFloat#("YScale Min: ", RandomYScaleMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomYScaleMax=AdjustFloat#("YScale Max: ", RandomYScaleMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectYScale=AdjustFloat#("YScale: ", CurrentObjectYScale, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomYScale=Not RandomYScale
		EndIf
	Case "ZScale"
		SlowFloat#=SlowScale#
		If RandomZScale
			If OnLeftHalfAdjuster()
				RandomZScaleMin=AdjustFloat#("ZScale Min: ", RandomZScaleMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomZScaleMax=AdjustFloat#("ZScale Max: ", RandomZScaleMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectZScale=AdjustFloat#("ZScale: ", CurrentObjectZScale, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomZScale=Not RandomZScale
		EndIf


	Case "ID"
		FastInt=FastID
		If RandomID
			If OnLeftHalfAdjuster()
				RandomIDMin=AdjustInt("ID Min: ", RandomIDMin, SlowInt, FastInt, DelayTime)
			Else
				RandomIDMax=AdjustInt("ID Max: ", RandomIDMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectID=AdjustInt("ID: ", CurrentObjectID, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomID=Not RandomID
		EndIf

	Case "Type"
		If RandomType
			If OnLeftHalfAdjuster()
				RandomTypeMin=AdjustInt("Type Min: ", RandomTypeMin, SlowInt, FastInt, DelayTime)
			Else
				RandomTypeMax=AdjustInt("Type Max: ", RandomTypeMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectType=AdjustInt("Type: ", CurrentObjectType, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomType=Not RandomType
		EndIf
	Case "SubType"
		If RandomSubType
			If OnLeftHalfAdjuster()
				RandomSubTypeMin=AdjustInt("SubType Min: ", RandomSubTypeMin, SlowInt, FastInt, DelayTime)
			Else
				RandomSubTypeMax=AdjustInt("SubType Max: ", RandomSubTypeMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectSubType=AdjustInt("SubType: ", CurrentObjectSubType, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomSubType=Not RandomSubType
		EndIf
				
		If CurrentObjectModelName$="!CustomItem"
		
			Min=-400
			Max=509
		
			If CurrentObjectSubType<Min
				CurrentObjectSubType=Max
			EndIf
				
			GapSubType(-400,-300)
			GapSubType(-300,-200)
			GapSubType(-195,-100)
			GapSubType(-98,-6)
				
			If CurrentObjectSubType>27 And CurrentObjectSubType<490
				CurrentObjectSubType=509
			Else If CurrentObjectSubType>489 And CurrentObjectSubType<509
				CurrentObjectSubType=27
				
			Else If CurrentObjectSubType>Max
				CurrentObjectSubType=Min

			Else If CurrentObjectSubType=8
				CurrentObjectSubType=10
			Else If CurrentObjectSubType=9
				CurrentObjectSubType=7
				
			Else If CurrentObjectSubType=18
				CurrentObjectSubType=20
			Else If CurrentObjectSubType=19
				CurrentObjectSubType=17
				
			EndIf
		
		EndIf
		If CurrentObjectModelName$="!FireFlower"
			If CurrentObjectSubType<0 Then CUrrentObjectSubType=3
			If CurrentObjectSubType>3 Then CurrentObjectSubType=0
		EndIf
		If CurrentObjectModelName$="!Crab"
			If CurrentObjectSubType<0 Then CUrrentObjectSubType=1
			If CurrentObjectSubType>1 Then CurrentObjectSubType=0
		EndIf
		

	Case "Active"
		If RandomActive
			If OnLeftHalfAdjuster()
				RandomActiveMin=AdjustInt("Active Min: ", RandomActiveMin, SlowInt, FastInt, DelayTime)
			Else
				RandomActiveMax=AdjustInt("Active Max: ", RandomActiveMax, SlowInt, FastInt, DelayTime)
			EndIf
		ElseIf ReturnKey=False
			If RawInput=True
				CurrentObjectActive=InputInt("Active: ")
			Else
				If CurrentObjectActive=0
					CurrentObjectActive=1001
				Else
					CurrentObjectActive=0
				EndIf
			EndIf
			If MouseScroll=0
				Delay DelayTime
			EndIf
		EndIf
		If ReturnPressed()
			RandomActive=Not RandomActive
		EndIf
	Case "ActivationSpeed"
		SlowInt=SlowInt*2
		FastInt=FastInt*2
		If RandomActivationSpeed
			If OnLeftHalfAdjuster()
				RandomActivationSpeedMin=AdjustInt("ActivationSpeed Min: ", RandomActivationSpeedMin, SlowInt, FastInt, DelayTime)
			Else
				RandomActivationSpeedMax=AdjustInt("ActivationSpeed Max: ", RandomActivationSpeedMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectActivationSpeed=AdjustInt("ActivationSpeed: ", CurrentObjectActivationSpeed, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomActivationSpeed=Not RandomActivationSpeed
		EndIf
	Case "ActivationType"
		If RandomActivationType
			If OnLeftHalfAdjuster()
				RandomActivationTypeMin=AdjustInt("ActivationType Min: ", RandomActivationTypeMin, SlowInt, FastInt, DelayTime)
			Else
				RandomActivationTypeMax=AdjustInt("ActivationType Max: ", RandomActivationTypeMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectActivationType=AdjustInt("ActivationType: ", CurrentObjectActivationType, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomActivationType=Not RandomActivationType
		EndIf
			
		;If CurrentObjectModelName$="!SteppingStone"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObjectActivationType=3 
		;			CurrentObjectActivationType=16
		;		Else If CurrentObjectActivationType=16
		;			CurrentObjectActivationType=21
		;		Else
		;			CurrentObjectActivationType=3
		;		EndIf
		;	EndIf
		;Else If CurrentObjectModelName$="!ColourGate"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObjectActivationType=1 
		;			CurrentObjectActivationType=2
		;		Else If CurrentObjectActivationType=2
		;			CurrentObjectActivationType=3
		;		Else If CurrentObjectActivationType=3
		;			CurrentObjectActivationType=11
		;		Else If CurrentObjectActivationType=11
		;			CurrentObjectActivationType=21
		;		Else
		;			CurrentObjectActivationType=1
		;		EndIf
		;	EndIf
		;Else If CurrentObjectModelName$="!Autodoor"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObjectActivationType=11 
		;			CurrentObjectActivationType=17
		;		Else If CurrentObjectActivationType=17
		;			CurrentObjectActivationType=18
		;		Else If CurrentObjectActivationType=18
		;			CurrentObjectActivationType=19
		;		Else If CurrentObjectActivationType=19
		;			CurrentObjectActivationType=20
		;		Else
		;			CurrentObjectActivationType=11
		;		EndIf
		;	EndIf
		
	Case "TimerMax1"
		FastInt=FastTimer
		If RandomTimerMax1
			If OnLeftHalfAdjuster()
				RandomTimerMax1Min=AdjustInt("TimerMax1 Min: ", RandomTimerMax1Min, SlowInt, FastInt, DelayTime)
			Else
				RandomTimerMax1Max=AdjustInt("TimerMax1 Max: ", RandomTimerMax1Max, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectTimerMax1=AdjustInt("TimerMax1: ", CurrentObjectTimerMax1, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomTimerMax1=Not RandomTimerMax1
		EndIf
	Case "TimerMax2"
		FastInt=FastTimer
		If RandomTimerMax2
			If OnLeftHalfAdjuster()
				RandomTimerMax2Min=AdjustInt("TimerMax2 Min: ", RandomTimerMax2Min, SlowInt, FastInt, DelayTime)
			Else
				RandomTimerMax2Max=AdjustInt("TimerMax2 Max: ", RandomTimerMax2Max, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectTimerMax2=AdjustInt("TimerMax2: ", CurrentObjectTimerMax2, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomTimerMax2=Not RandomTimerMax2
		EndIf
	Case "Timer"
		FastInt=FastTimer
		If RandomTimer
			If OnLeftHalfAdjuster()
				RandomTimerMin=AdjustInt("Timer Min: ", RandomTimerMin, SlowInt, FastInt, DelayTime)
			Else
				RandomTimerMax=AdjustInt("Timer Max: ", RandomTimerMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectTimer=AdjustInt("Timer: ", CurrentObjectTimer, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomTimer=Not RandomTimer
		EndIf
		
	Case "ButtonPush"
		If Not RandomButtonPush And ReturnKey=False
			If LeftMouse=True Or RightMouse=True Or MouseScroll<>0
				CurrentObjectButtonPush=1-CurrentObjectButtonPush
				If MouseScroll=0
					Delay 150
				EndIf
			EndIf
		EndIf
		If ReturnPressed()
			RandomButtonPush=Not RandomButtonPush
		EndIf

	Case "WaterReact"
		CurrentObjectWaterReact=AdjustInt("WaterReact: ", CurrentObjectWaterReact, SlowInt, FastInt, DelayTime)
	Case "Freezable"
		CurrentObjectFreezable=AdjustInt("Freezable: ", CurrentObjectFreezable, SlowInt, FastInt, DelayTime)
	Case "Frozen"
		CurrentObjectFrozen=AdjustInt("Frozen: ", CurrentObjectFrozen, SlowInt, FastInt, DelayTime)		
	Case "Teleportable"
		If Not RandomTeleportable And ReturnKey=False
			If LeftMouse=True Or RightMouse=True Or MouseScroll<>0
				CurrentObjectTeleportable=1-CurrentObjectTeleportable
				If MouseScroll=0
					Delay 150
				EndIf
			EndIf
		EndIf
		If ReturnPressed()
			RandomTeleportable=Not RandomTeleportable
		EndIf
		
	Case "Data0"
		;CurrentObjectData(0)=AdjustInt("Data0: ", CurrentObjectData(0), SlowInt, FastInt, DelayTime)
		AdjustObjectData(0, SlowInt, FastInt, DelayTime)
		
		If CurrentObjectModelName$="!Scritter" ;Or CurrentObjectModelName$="!Cuboid" Or CurrentObjectType=424
			; colours 0-6
			If CurrentObjectData(0)>6 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=6
		;Else If CurrentObjectTextureName$="!GloveTex" 
			;If currentobjectdata(0)<0 Then currentobjectdata(0)=9
			;If currentobjectdata(0)>9 Then currentobjectdata(0)=0
	
		Else If CurrentObjectModelName$="!Obstacle51" Or CurrentObjectModelName$="!Obstacle55" Or CurrentObjectModelName$="!Obstacle59"
			If CurrentObjectData(0)>3 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=3

	

		;Else If CurrentObjectModelName$="!Spring" Or CurrentObjectModelName$="!FlipBridge" Or CurrentObjectModelName$="!SteppingStone" Or CurrentObjectModelName$="!Transporter" Or (CurrentObjectModelName$="!Button" And ((CurrentObjectSubType Mod 32)<10 Or (CurrentObjectSubType Mod 32)=16 Or (CurrentObjectSubType Mod 32)=17)) Or CurrentObjectModelName$="!Door" Or CurrentObjectModelName$="!Key" Or CurrentObjectModelName$="!Cage" Or CurrentObjectTextureName$="!FireTrap" Or CurrentObjectModelName$="!ColourGate"  Or CurrentObjectModelName$="!Pushbot" Or CurrentObjectModelName$="!Autodoor" Or CurrentObjectModelName$="!Suctube" Or CurrentObjectModelName$="!Conveyor" 

			; colours 0-15
			;If CurrentObjectData(0)>15 CurrentObjectData(0)=0
			;If CurrentObjectData(0)<0 CurrentObjectData(0)=15
		;Else If CurrentObjectModelName$="!KeyCard"
			; colours 0-7
			;If CurrentObjectData(0)>7 CurrentObjectData(0)=0
			;If CurrentObjectData(0)<0 CurrentObjectData(0)=7

		;Else If CurrentObjectModelName$="!Teleport" 
			; colours 0-8
			;If CurrentObjectData(0)>8 CurrentObjectData(0)=0
			;If CurrentObjectData(0)<0 CurrentObjectData(0)=8

		EndIf
		
		If CurrentObjectModelName$="!Obstacle48" ; (wysp ship)
			If CurrentObjectData(0)>1 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=1

		EndIf

		
		If CurrentObjectType=190 Or CurrentObjectType=164
			; particle spray
			If CurrentObjectData(0)>63 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=63
		EndIf
		;If CurrentObjectModelName$="!StarGate"
		;	If CurrentObjectData(0)<0 CurrentObjectData(0)=0
		;EndIf
		;If CurrentObjectModelName$="!CustomItem"
		;	If CurrentObjectData(0)<0 CurrentObjectData(0)=62
		;	If CurrentObjectData(0)>62 CurrentObjectData(0)=0
		;EndIf
		If CurrentObjectModelName$="!Gem"
			If CurrentObjectData(0)>2 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=2
		EndIf
		If CurrentObjectModelName$="!Crystal"
			If CurrentObjectData(0)>1 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=1


		EndIf
		If CurrentObjectModelName$="!Bowler"
			If CurrentObjectData(1)=2
				If CurrentObjectData(0)>7 CurrentObjectData(0)=0
				If CurrentObjectData(0)<0 CurrentObjectData(0)=7
			Else
				If CurrentObjectData(0)>3 CurrentObjectData(0)=0
				If CurrentObjectData(0)<0 CurrentObjectData(0)=3
			EndIf
		EndIf
		If CurrentObjectModelName$="!FireFlower"
			If CurrentObjectData(0)>7 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=7
		EndIf
		If CurrentObjectModelName$="!Turtle" Or (Left$(CurrentObjectModelName$,6)="!Retro" And CurrentObjectType<>424) Or CurrentObjectModelName$="!Weebot" Or Currentobjectmodelname$="!Zapbot"
			If CurrentObjectData(0)>3 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=3
		EndIf
		If  CurrentObjectModelName="!Kaboom"

			If CurrentObjectData(0)>5 CurrentObjectData(0)=1
			If CurrentObjectData(0)<1 CurrentObjectData(0)=5
		EndIf
		
		If CurrentObjectModelName$="!NPC"

			If CurrentObjectData(0)>8 CurrentObjectData(0)=1
			If CurrentObjectData(0)<1 CurrentObjectData(0)=8
		EndIf

		
		If CurrentObjectModelName$="!Wisp"
			If CurrentObjectData(0)>9 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=9
		EndIf

		


		If CurrentObjectModelName$="!Rubberducky"
			If CurrentObjectData(0)>1 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=1
		EndIf
		
		If CurrentObjectType=51
			If CurrentObjectData(0)>9 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=9
		EndIf
		
		If CurrentObjectModelName$="!WaterFall"
			If CurrentObjectData(0)>2 CurrentObjectData(0)=0
			If CurrentObjectData(0)<0 CurrentObjectData(0)=2
		EndIf
		
		If CurrentObjectModelName$="!Ghost" Or CurrentObjectModelName$="!Wraith"
			
			If CurrentObjectData(1)<2 CurrentObjectData(1)=2
			

		EndIf



			

	Case "Data1"
		;CurrentObjectData(1)=AdjustInt("Data1: ", CurrentObjectData(1), SlowInt, FastInt, DelayTime)
		AdjustObjectData(1, SlowInt, FastInt, DelayTime)
		
		;If CurrentObjectModelName$="!Spring" Or CurrentObjectModelName$="!FlipBridge" Or CurrentObjectModelName$="!SteppingStone" Or CurrentObjectModelName$="!Transporter"  Or (CurrentObjectModelName$="!Button" And ((CurrentObjectSubType Mod 32)=16 Or (CurrentObjectSubType Mod 32)=17)) Or CurrentObjectModelName$="!Door" Or CurrentObjectModelName$="!Key" Or CurrentObjectModelName$="!KeyCard" Or CurrentObjectModelName$="!Teleport" Or CurrentObjectModelName$="!Cage" Or CurrentObjectTextureName$="!FireTrap" Or CurrentObjectModelName$="!Retrolasergate"  Or CurrentObjectModelName$="!Pushbot" Or CurrentObjectModelName$="!Autodoor" Or CurrentObjectModelName$="!Suctube" Or CurrentObjectModelName$="!Conveyor"


			; subcolours 0-4
		;	If CurrentObjectData(1)>4 CurrentObjectData(1)=0
		;	If CurrentObjectData(1)<0 CurrentObjectData(1)=4
		;Else If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)<10) 

			; colours 0-15
		;	If CurrentObjectData(1)>15 CurrentObjectData(1)=0
		;	If CurrentObjectData(1)<0 CurrentObjectData(1)=15
		;EndIf
		
		If CurrentObjectModelName$="!Obstacle51" Or CurrentObjectModelName$="!Obstacle55" Or CurrentObjectModelName$="!Obstacle59"
			If CurrentObjectData(1)>3 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=3
		EndIf
	
		
		If CurrentObjectType=190
			; particle spray intensity
			If CurrentObjectData(1)>3 CurrentObjectData(1)=1
			If CurrentObjectData(1)<1 CurrentObjectData(1)=3
		EndIf
		If CurrentObjectModelName$="!StarGate"
			If CurrentObjectData(1)>1 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=1

		EndIf
		If CurrentObjectModelName$="!FireFlower" 
			If CurrentObjectData(1)>3 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=3
		EndIf 
		
		If CurrentObjectModelName$="!Cuboid"

			If CurrentObjectData(1)>1 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=1

		EndIf

		If CurrentObjectModelName$="!Gem"
			If CurrentObjectData(1)>6 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=6

		EndIf
		If CurrentObjectModelName$="!Bowler"
			If CurrentObjectData(1)>2 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=2

			If CurrentObjectData(1)=2
				If CurrentObjectData(0)>7 CurrentObjectData(0)=0
				If CurrentObjectData(0)<0 CurrentObjectData(0)=7
			Else
				If CurrentObjectData(0)>3 CurrentObjectData(0)=0
				If CurrentObjectData(0)<0 CurrentObjectData(0)=3
			EndIf
		EndIf
		If CurrentObjectModelName$="!Chomper"
			If CurrentObjectData(1)>3 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=3
		EndIf
		If CurrentObjectModelName$="!Turtle"
			If CurrentObjectData(1)>1 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=1
		EndIf
		If CurrentObjectModelName$="!Crab"
			If CurrentObjectData(1)>3 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=3
		EndIf

		

		If CurrentObjectModelName$="!NPC"
			If CurrentObjectData(1)>4 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=4
			

		EndIf
		If CurrentObjectModelName$="!Thwart" Or CurrentObjectModelName$="!Troll" Or (Left$(CurrentObjectModelName$,6)="!Retro" And CurrentObjectModelName$<>"!Retrolasergate")  Or CurrentObjectModelName$="!Weebot" Or Currentobjectmodelname$="!Zapbot" Or CurrentObjectModelname$="!Portal Warp"

			If CurrentObjectData(1)>1 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=1
			

		EndIf
		
		If CurrentObjectModelName$="!Sign"
			If CurrentObjectData(1)>5 CurrentObjectData(1)=0
			If CurrentObjectData(1)<0 CurrentObjectData(1)=5
			

		EndIf
		If CurrentObjectModelName$="!Ghost"
			If CurrentObjectData(1)>9 CurrentObjectData(1)=1
			If CurrentObjectData(1)<1 CurrentObjectData(1)=9
			

		EndIf


		


	Case "Data2"
		;CurrentObjectData(2)=AdjustInt("Data2: ", CurrentObjectData(2), SlowInt, FastInt, DelayTime)
		AdjustObjectData(2, SlowInt, FastInt, DelayTime)
		
		If CurrentObjectModelName$="!Spring" Or CurrentObjectModelName$="!FlipBridge"
			; direction 0-7
			If CurrentObjectData(2)>7 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=7
		EndIf
		If CurrentObjectModelName$="!Suctube"  Or CurrentObjectModelName$="!SuctubeX" Or CurrentObjectModelName$="!Conveyor"
			; direction 0-3
			If CurrentObjectData(2)>3 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=3
		EndIf

		If CurrentObjectModelName$="!Transporter"  Or CurrentObjectModelName$="!Weebot" Or Currentobjectmodelname$="!Zapbot"  Or CurrentObjectModelName$="!Pushbot"
			; direction 0-3 (or speed for zap/weebot)
			If CurrentObjectData(2)>3 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=3
		EndIf
		;If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)<5)
			; colours 0-15
		;	If CurrentObjectData(2)>15 CurrentObjectData(2)=0
		;	If CurrentObjectData(2)<0 CurrentObjectData(2)=15
		;EndIf
		;If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)>=5 And (CurrentObjectSubType Mod 32)<10)
			; subcolours 0-4
		;	If CurrentObjectData(2)>4 CurrentObjectData(2)=0
		;	If CurrentObjectData(2)<0 CurrentObjectData(2)=4
		;EndIf
		If  (CurrentObjectModelName$="!Button" And ((CurrentObjectSubType Mod 32)=16 Or (CurrentObjectSubType Mod 32)=17))
			; direction 0-1
			If CurrentObjectData(2)>1 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=1

		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=0
			; x goal
			If CurrentobjectData(2)<0 CurrentObjectData(2)=0
		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=1
			; talkable
			If CurrentobjectData(2)<-1 CurrentObjectData(2)=-1
		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=2
			; particle
			If CurrentobjectData(2)<0 CurrentObjectData(2)=63
			If CurrentobjectData(2)>63 CurrentObjectData(2)=0

		EndIf
		If CurrentObjectType=190
			; particle spray dir
			If CurrentObjectData(2)>5 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=5
		EndIf
		
		;If  CurrentObjectModelName$="!ColourGate"
		;	If CurrentObjectData(2)>2 CurrentObjectData(2)=0
		;	If CurrentObjectData(2)<0 CurrentObjectData(2)=2
		;EndIf
		
		If CurrentObjectModelName$="!Gem"
			If CurrentObjectData(0)>2 CurrentObjectData(0)=-2
			If CurrentObjectData(0)<-2 CurrentObjectData(0)=2
		EndIf


		
		If CurrentObjectModelName$="!NPC"
			If CurrentObjectData(2)>56 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=56
			CurrentObjectData(3)=1

		EndIf
		
		If CurrentObjectModelName$="!Thwart" Or CurrentObjectModelName$="!ZbotNPC"
			If CurrentObjectData(2)>7 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=7
			

		EndIf



		If CurrentObjectModelName$="!Sign"
			If CurrentObjectData(2)>3 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=3
			

		EndIf
		
		If CurrentObjectModelName$="!Wraith"
			If CurrentObjectData(2)>2 CurrentObjectData(2)=0
			If CurrentObjectData(2)<0 CurrentObjectData(2)=2

		EndIf



	Case "Data3"
		;CurrentObjectData(3)=AdjustInt("Data3: ", CurrentObjectData(3), SlowInt, FastInt, DelayTime)
		AdjustObjectData(3, SlowInt, FastInt, DelayTime)
		
		If CurrentObjectType=190
			If CurrentObjectData(3)<0 Then CurrentObjectData(3)=0
			Select CurrentObjectSubType
			Case 4
				If CurrentObjectData(3)>1 Then CurrentObjectData(3)=0
			Case 5
				If CurrentObjectData(3)>6 Then CurrentObjectData(3)=0
			End Select
		EndIf

		If CurrentObjectModelName$="!SteppingStone"
			; sound
			If CurrentObjectData(3)>3 CurrentObjectData(3)=0
			If CurrentObjectData(3)<0 CurrentObjectData(3)=3
		EndIf
		;If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)<5)
			; colours 0-15
		;	If CurrentObjectData(3)>15 CurrentObjectData(3)=0
		;	If CurrentObjectData(3)<0 CurrentObjectData(3)=15
		;EndIf
		;If (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)>=5 And (CurrentObjectSubType Mod 32)<10)
			; subcolour 0-4
		;	If CurrentObjectData(3)>4 CurrentObjectData(3)=0
		;	If CurrentObjectData(3)<0 CurrentObjectData(3)=4
		;EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=0
			; y goal
			If CurrentobjectData(3)<0 CurrentObjectData(3)=0
		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=1
			; y goal
			If CurrentobjectData(3)<-1 CurrentObjectData(3)=4
			If CurrentobjectData(3)>4 CurrentObjectData(3)=-1
			
		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=2
			; how many particles
			If CurrentobjectData(3)<0 CurrentObjectData(3)=9
			If CurrentobjectData(3)>9 CurrentObjectData(3)=0
			
		EndIf
		If  CurrentObjectModelName$="!FireFlower" 
			; hitpoints
			If CurrentobjectData(3)<0 CurrentObjectData(3)=0
		EndIf
		If CurrentObjectModelName$="!NPC"

			Select CurrentObjectData(2)
			Case 1,5			
				If CurrentObjectData(3)>7 CurrentObjectData(3)=1
				If CurrentObjectData(3)<1 CurrentObjectData(3)=7
			Case 2			
				If CurrentObjectData(3)>5 CurrentObjectData(3)=1
				If CurrentObjectData(3)<1 CurrentObjectData(3)=5
			Case 3,6,7			
				If CurrentObjectData(3)>3 CurrentObjectData(3)=1
				If CurrentObjectData(3)<1 CurrentObjectData(3)=3
			Case 10			
				If CurrentObjectData(3)>2 CurrentObjectData(3)=1
				If CurrentObjectData(3)<1 CurrentObjectData(3)=2
			Case 27
				If CurrentObjectData(3)>4 CurrentObjectData(3)=1
				If CurrentObjectData(3)<1 CurrentObjectData(3)=4
			Case 28
				If CurrentObjectData(3)>4 CurrentObjectData(3)=1
				If CurrentObjectData(3)<1 CurrentObjectData(3)=4
			Case 46
				If CurrentObjectData(3)>6 CurrentObjectData(3)=1
				If CurrentObjectData(3)<1 CurrentObjectData(3)=6



			Default
				CurrentObjectData(3)=1
			End Select



		EndIf
		;If  Currentobjectmodelname$="!Zapbot"
			; zapbot range
		;	If CurrentobjectData(3)<4 CurrentObjectData(3)=4
		;	If CurrentobjectData(3)>30 CurrentObjectData(3)=30
		;EndIf
		If  Currentobjectmodelname$="!Pushbot" 
			; pushbot left/right turn,
			If CurrentobjectData(3)<0 CurrentObjectData(3)=0
			If CurrentobjectData(3)>2 CurrentObjectData(3)=2
		EndIf
		If  CurrentObjectType=45
			; conveyor lead
			If CurrentobjectData(3)<0 CurrentObjectData(3)=0
			If CurrentobjectData(3)>1 CurrentObjectData(3)=1
		EndIf

		If  CurrentObjectType=46
			; pushbot left/right turn,
			If CurrentobjectData(3)<1 CurrentObjectData(3)=1
			
		EndIf

		If  Currentobjectmodelname$="!Suctube" Or CurrentObjectModelName$="!SuctubeX"
			; Suctube tex
			If CurrentobjectData(3)<0 CurrentObjectData(3)=0
			If CurrentobjectData(3)>2 CurrentObjectData(3)=2
		EndIf


	Case "Data4"
		Adj=1
		AdjFast=10
		If CurrentObjectModelName$="!Button" And CurrentObjectSubType=10
			Adj=45
			AdjFast=45
		EndIf
		
		;CurrentObjectData(4)=AdjustInt("Data4: ", CurrentObjectData(4), Adj, AdjFast, DelayTime)
		AdjustObjectData(4, Adj, AdjFast, DelayTime)

		;If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)<5)
			; subcolours 0-4
		;	If CurrentObjectData(4)>4 CurrentObjectData(4)=0
		;	If CurrentObjectData(4)<0 CurrentObjectData(4)=4
		;EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=10
			;playerstartingyaw
			If CurrentObjectData(4)<0 Then CurrentObjectData(4)=360-45
			If CurrentObjectData(4)>359 Then CurrentObjectData(4)=0
			
		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And (CurrentObjectData(0)=0 Or CurrentObjectData(0)=2)
			; repeatable
			If CurrentobjectData(4)<0 CurrentObjectData(4)=1
			If CurrentobjectData(4)>1 CurrentObjectData(4)=0
		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=1
			; yaw
			If CurrentobjectData(4)<-1 CurrentObjectData(4)=359
			If CurrentobjectData(4)>359 CurrentObjectData(4)=0
		EndIf
		If CurrentObjectModelName$="!NPC"
			If CurrentObjectData(4)=-1 CurrentObjectData(4)=116
			If CurrentObjectData(4)=1 CurrentObjectData(4)=101
			If CurrentObjectData(4)=100 CurrentObjectData(4)=0
			If CurrentObjectData(4)=117 CurrentObjectData(4)=0

		

		EndIf
		
		If CurrentObjectType=190
			If CurrentObjectData(4)<0 Then CurrentObjectData(4)=0
			If CurrentObjectData(4)>1 Then CurrentObjectData(4)=0
		EndIf
		
		
		If  Currentobjectmodelname$="!Zapbot" Or CurrentObjectModelName$="!Ufo"

			; zapbot track?
			If CurrentobjectData(4)<0 CurrentObjectData(4)=0
			If CurrentobjectData(4)>1 CurrentObjectData(4)=1
		EndIf
		
		
		If  Currentobjectmodelname$="!Conveyor"
			; visual type
			If CurrentobjectData(4)<0 CurrentObjectData(4)=0
			If CurrentobjectData(4)>4 CurrentObjectData(4)=4
		EndIf


		If CurrentObjectModelName$="!Suctube"  
			If CurrentobjectData(4)<0 CurrentObjectData(4)=0
			If CurrentobjectData(4)>1 CurrentObjectData(4)=1
		EndIf



	Case "Data5"
		;CurrentObjectData(5)=AdjustInt("Data5: ", CurrentObjectData(5), SlowInt, FastInt, DelayTime)
		AdjustObjectData(5, SlowInt, FastInt, DelayTime)
		
		;If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)<5)
		;	; subcolours 0-4
		;	If CurrentObjectData(5)>4 CurrentObjectData(5)=0
		;	If CurrentObjectData(5)<0 CurrentObjectData(5)=4
		;Else
		If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)=15)
			; repeatable
			If CurrentObjectData(5)>1 CurrentObjectData(5)=0
			If CurrentObjectData(5)<0 CurrentObjectData(5)=1

		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=0
			; timer
			If CurrentobjectData(5)<0 CurrentObjectData(5)=0
		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=1
			; repeatable
			If CurrentObjectData(5)>1 CurrentObjectData(5)=0
			If CurrentObjectData(5)<0 CurrentObjectData(5)=1

		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=10
			; levelexit flyover
			If CurrentObjectData(5)>1 CurrentObjectData(5)=0
			If CurrentObjectData(5)<0 CurrentObjectData(5)=1

		EndIf


		If CurrentObjectModelName$="!NPC" Or Currentobjectmodelname$="!Conveyor"
			If CurrentObjectData(5)>1 CurrentObjectData(5)=0
			If CurrentObjectData(5)<0 CurrentObjectData(5)=1
			

		EndIf
		If CurrentObjectModelName$="!NPC" And (CurrentObjectData(4)<>101 And CurrentObjectData(4)<>102) Then CurrentObjectData(5)=0
		
		If CurrentObjectModelName$="!GlowWorm"  Or CurrentObjectModelName$="!Zipper"
			If CurrentObjectData(5)>255 CurrentObjectData(5)=0
			If CurrentObjectData(5)<0 CurrentObjectData(5)=255
			

		EndIf
		
		If CurrentObjectModelName$="!Suctube"
			If CurrentObjectData(5)>1 CurrentObjectData(5)=0
			If CurrentObjectData(5)<0 CurrentObjectData(5)=1
			

		EndIf

			


	Case "Data6"
		;CurrentObjectData(6)=AdjustInt("Data6: ", CurrentObjectData(6), 1, 10, 150)
		AdjustObjectData(6, SlowInt, FastInt, DelayTime)
		
		;If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)<5)
		;	; subcolours 0-4
		;	If CurrentObjectData(6)>4 CurrentObjectData(6)=0
		;	If CurrentObjectData(6)<0 CurrentObjectData(6)=4
		;EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=0
			; timer reset
			If CurrentobjectData(6)<0 CurrentObjectData(6)=0
		EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=1
			; walk anim
			If CurrentobjectData(6)<-1 CurrentObjectData(6)=2
			If CurrentobjectData(6)>2 CurrentObjectData(6)=-1

		EndIf
		If CurrentObjectModelName$="!NPC"
			If CurrentObjectData(6)>2 CurrentObjectData(6)=0
			If CurrentObjectData(6)<0 CurrentObjectData(6)=2
			

		EndIf
		If CurrentObjectModelName$="!Thwart" Or CurrentObjectModelName$="!Troll" Or CurrentObjectModelName$="!ZbotNPC"
			If CurrentObjectData(6)>1 CurrentObjectData(6)=0
			If CurrentObjectData(6)<0 CurrentObjectData(6)=1
			

		EndIf

		If CurrentObjectModelName$="!GlowWorm"  Or CurrentObjectModelName$="!Zipper"
			If CurrentObjectData(6)>255 CurrentObjectData(6)=0
			If CurrentObjectData(6)<0 CurrentObjectData(6)=255
			

		EndIf



	Case "Data7"
		;CurrentObjectData(7)=AdjustInt("Data7: ", CurrentObjectData(7), 1, 10, 150)
		AdjustObjectData(7, SlowInt, FastInt, DelayTime)
		
		;If  (CurrentObjectModelName$="!Button" And (CurrentObjectSubType Mod 32)<5)
		;	; subcolours 0-4
		;	If CurrentObjectData(7)>4 CurrentObjectData(7)=0
		;	If CurrentObjectData(7)<0 CurrentObjectData(7)=4
		;EndIf
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=1
			; turn
			If CurrentobjectData(7)=-2 CurrentObjectData(7)=25
			If CurrentobjectData(7)=26 CurrentObjectData(7)=-1
			If CurrentobjectData(7)=6 CurrentObjectData(7)=10
			If CurrentobjectData(7)=9 CurrentObjectData(7)=5
			If CurrentobjectData(7)=16 CurrentObjectData(7)=20
			If CurrentobjectData(7)=19 CurrentObjectData(7)=15

		EndIf
		If CurrentObjectModelName$="!NPC"  Or CurrentObjectModelName="!Kaboom"

			If CurrentobjectData(7)=-2 CurrentObjectData(7)=25
			If CurrentobjectData(7)=26 CurrentObjectData(7)=-1
			If CurrentobjectData(7)=6 CurrentObjectData(7)=10
			If CurrentobjectData(7)=9 CurrentObjectData(7)=5
			If CurrentobjectData(7)=16 CurrentObjectData(7)=20
			If CurrentobjectData(7)=19 CurrentObjectData(7)=15


		EndIf
		
		If CurrentObjectModelName$="!GlowWorm"  Or CurrentObjectModelName$="!Zipper"
			If CurrentObjectData(7)>255 CurrentObjectData(7)=0
			If CurrentObjectData(7)<0 CurrentObjectData(7)=255
			

		EndIf


	Case "Data8"
		;CurrentObjectData(8)=AdjustInt("Data8: ", CurrentObjectData(8), 1, 10, 150)
		AdjustObjectData(8, SlowInt, FastInt, DelayTime)

		If CurrentObjectModelName$="!Transporter" Or CurrentObjectModelName$="!Button"
			If LeftMouse=True
				If CurrentObjectData(8)<0 Then CurrentObjectData(8)=0
			ElseIf RightMouse=True
				If CurrentObjectData(8)<0 Then CurrentObjectData(8)=-2
			EndIf
		EndIf
		If CurrentObjectModelName$="!NPC"
			If CurrentObjectData(8)>10 CurrentObjectData(8)=0
			If CurrentObjectData(8)<0 CurrentObjectData(8)=10
			

		EndIf
		
		If CurrentObjectModelName$="!Kaboom"
			If CurrentObjectData(8)>5 CurrentObjectData(8)=0
			If CurrentObjectData(8)<0 CurrentObjectData(8)=5
			

		EndIf

		If CurrentObjectModelName$="!BabyBoomer" Or CurrentObjectModelName$="!ZbotNPC"

			If CurrentObjectData(8)>1 CurrentObjectData(8)=0
			If CurrentObjectData(8)<0 CurrentObjectData(8)=1
		EndIf
		
		If CurrentObjectModelName$="!StinkerWee"

			If CurrentObjectData(8)>2 CurrentObjectData(8)=0
			If CurrentObjectData(8)<0 CurrentObjectData(8)=2
		EndIf



	Case "Data9"
		;CurrentObjectData(9)=AdjustInt("Data9: ", CurrentObjectData(9), 1, 10, 150)
		AdjustObjectData(9, SlowInt, FastInt, DelayTime)
		
		If  CurrentObjectModelName$="!CustomModel"
			If CurrentobjectData(9)>1 CurrentObjectData(9)=1
			If CurrentobjectData(9)<0 CurrentObjectData(9)=0
		EndIf
		
		If  CurrentObjectModelName$="!Button" And CurrentObjectSubType=11 And CurrentObjectData(0)=1
			; anim
			If CurrentobjectData(9)<-1 CurrentObjectData(9)=10
			If CurrentobjectData(9)>10 CurrentObjectData(9)=-1
		EndIf
		
		If CurrentObjectModelName$="!Conveyor"

			
			If CurrentObjectData(9)<1 CurrentObjectData(9)=1
		EndIf


	Case "Talkable"
		If RandomTalkable
			If OnLeftHalfAdjuster()
				RandomTalkableMin=AdjustInt("Talkable Min: ", RandomTalkableMin, SlowInt, FastInt, DelayTime)
			Else
				RandomTalkableMax=AdjustInt("Talkable Max: ", RandomTalkableMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectTalkable=AdjustInt("Talkable: ", CurrentObjectTalkable, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomTalkable=Not RandomTalkable
		EndIf
		
		;If CurrentObjectTalkable<0 CurrentObjectTalkable=0
		;If  CurrentObjectModelName$="!Sign" And CurrentObjectTalkable<10001 CurrentObjectTalkable=10001


	Case "MovementSpeed"
		If RandomMovementSpeed
			If OnLeftHalfAdjuster()
				RandomMovementSpeedMin=AdjustInt("MovementSpeed Min: ", RandomMovementSpeedMin, SlowInt, FastInt, DelayTime)
			Else
				RandomMovementSpeedMax=AdjustInt("MovementSpeed Max: ", RandomMovementSpeedMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectMovementSpeed=AdjustInt("MovementSpeed: ", CurrentObjectMovementSpeed, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomMovementSpeed=Not RandomMovementSpeed
		EndIf
		
	Case "MovementType"
		If RandomMovementType
			If OnLeftHalfAdjuster()
				RandomMovementTypeMin=AdjustInt("MovementType Min: ", RandomMovementTypeMin, SlowInt, FastInt, DelayTime)
			Else
				RandomMovementTypeMax=AdjustInt("MovementType Max: ", RandomMovementTypeMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectMovementType=AdjustInt("MovementType: ", CurrentObjectMovementType, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomMovementType=Not RandomMovementType
		EndIf
		
	Case "MovementTypeData"
		CurrentObjectMovementTypeData=AdjustInt("MovementTypeData: ", CurrentObjectMovementTypeData, 1, 10, 150)
		
	Case "TileTypeCollision"
		If Not RandomTTC And ReturnKey=False
			Adj=0
			If KeyDown(11) Adj=2^0
			If KeyDown(2) Adj=2^1
			If KeyDown(3) Adj=2^2
			If KeyDown(4) Adj=2^3
			If KeyDown(5) Adj=2^4
			If KeyDown(6) Adj=2^5
			If KeyDown(7) Adj=2^6
			If KeyDown(8) Adj=2^7
			If KeyDown(9) Adj=2^8
			If KeyDown(10) Adj=2^9
			If Fast Adj=Adj*2^10
			If LeftMouse=True Or RightMouse=True Then CurrentObjectTileTypeCollision=CurrentObjectTileTypeCollision Xor Adj
			Delay DelayTime
		EndIf
		If ReturnPressed()
			RandomTTC=Not RandomTTC
		EndIf
		
	Case "ObjectTypeCollision"
		If Not RandomOTC And ReturnKey=False
			Adj=0
			If KeyDown(11) Adj=2^0
			If KeyDown(2) Adj=2^1
			If KeyDown(3) Adj=2^2
			If KeyDown(4) Adj=2^3
			If KeyDown(5) Adj=2^4
			If KeyDown(6) Adj=2^5
			If KeyDown(7) Adj=2^6
			If KeyDown(8) Adj=2^7
			If KeyDown(9) Adj=2^8
			If KeyDown(10) Adj=2^9
			If Fast Adj=Adj*2^10
			If LeftMouse=True Or RightMouse=True Then CurrentObjectObjectTypeCollision=CurrentObjectObjectTypeCollision Xor Adj
			Delay DelayTime
		EndIf
		If ReturnPressed()
			RandomOTC=Not RandomOTC
		EndIf

		
	Case "ScaleAdjust"
		If RandomScaleAdjust
			If OnLeftHalfAdjuster()
				RandomScaleAdjustMin=AdjustFloat#("ScaleAdjust Min: ", RandomScaleAdjustMin, SlowFloat#, FastFloat#, DelayTime)
			Else
				RandomScaleAdjustMax=AdjustFloat#("ScaleAdjust Max: ", RandomScaleAdjustMax, SlowFloat#, FastFloat#, DelayTime)
			EndIf
		Else
			CurrentObjectScaleAdjust=AdjustFloat#("ScaleAdjust: ", CurrentObjectScaleAdjust, SlowFloat#, FastFloat#, DelayTime)
		EndIf
		If ReturnPressed()
			RandomScaleAdjust=Not RandomScaleAdjust
		EndIf

	Case "Exclamation"
		If RandomExclamation
			If OnLeftHalfAdjuster()
				RandomExclamationMin=AdjustInt("Exclamation Min: ", RandomExclamationMin, SlowInt, FastInt, DelayTime)
			Else
				RandomExclamationMax=AdjustInt("Exclamation Max: ", RandomExclamationMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectExclamation=AdjustInt("Exclamation: ", CurrentObjectExclamation, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomExclamation=Not RandomExclamation
		EndIf
	
	Case "Linked"
		CurrentObjectLinked=AdjustInt("Linked: ", CurrentObjectLinked, 1, 10, 150)
	Case "LinkBack"
		CurrentObjectLinkBack=AdjustInt("LinkBack: ", CurrentObjectLinkBack, 1, 10, 150)
		
	Case "Parent"
		CurrentObjectParent=AdjustInt("Parent: ", CurrentObjectParent, 1, 10, 150)
	Case "Child"
		CurrentObjectChild=AdjustInt("Child: ", CurrentObjectChild, 1, 10, 150)
		
	Case "DX"
		CurrentObjectDX=AdjustFloat#("DX: ", CurrentObjectDX, 0.01, 0.1, 150)
	Case "DY"
		CurrentObjectDY=AdjustFloat#("DY: ", CurrentObjectDY, 0.01, 0.1, 150)
	Case "DZ"
		CurrentObjectDZ=AdjustFloat#("DZ: ", CurrentObjectDZ, 0.01, 0.1, 150)
		
	Case "MoveXGoal"
		If RandomMoveXGoal
			If OnLeftHalfAdjuster()
				RandomMoveXGoalMin=AdjustInt("MoveXGoal Min: ", RandomMoveXGoalMin, SlowInt, FastInt, DelayTime)
			Else
				RandomMoveXGoalMax=AdjustInt("MoveXGoal Max: ", RandomMoveXGoalMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectMoveXGoal=AdjustInt("MoveXGoal: ", CurrentObjectMoveXGoal, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomMoveXGoal=Not RandomMoveXGoal
		EndIf
	Case "MoveYGoal"
		If RandomMoveYGoal
			If OnLeftHalfAdjuster()
				RandomMoveYGoalMin=AdjustInt("MoveYGoal Min: ", RandomMoveYGoalMin, SlowInt, FastInt, DelayTime)
			Else
				RandomMoveYGoalMax=AdjustInt("MoveYGoal Max: ", RandomMoveYGoalMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectMoveYGoal=AdjustInt("MoveYGoal: ", CurrentObjectMoveYGoal, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomMoveYGoal=Not RandomMoveYGoal
		EndIf
		
	Case "Data10"
		CurrentObjectData10=AdjustInt("Data10: ", CurrentObjectData10, 1, 10, 150)
		
	Case "Caged"
		CurrentObjectCaged=AdjustInt("Caged: ", CurrentObjectCaged, 1, 10, 150)
	Case "Dead"
		If RandomDead
			If OnLeftHalfAdjuster()
				RandomDeadMin=AdjustInt("Dead Min: ", RandomDeadMin, SlowInt, FastInt, DelayTime)
			Else
				RandomDeadMax=AdjustInt("Dead Max: ", RandomDeadMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectDead=AdjustInt("Dead: ", CurrentObjectDead, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomDead=Not RandomDead
		EndIf
	Case "DeadTimer"
		CurrentObjectDeadTimer=AdjustInt("DeadTimer: ", CurrentObjectDeadTimer, 1, 25, 150)
	Case "MovementTimer"
		CurrentObjectMovementTimer=AdjustInt("MovementTimer: ", CurrentObjectMovementTimer, 1, 25, 150)
		
	Case "Flying"
		CurrentObjectFlying=AdjustInt("Flying: ", CurrentObjectFlying, 1, 10, 150)
		
	Case "Indigo"
		CurrentObjectIndigo=AdjustInt("Indigo: ", CurrentObjectIndigo, 1, 10, 150)
		
	Case "Speed"
		CurrentObjectSpeed=AdjustFloat#("Speed: ", CurrentObjectSpeed, 0.01, 0.1, 150)
	Case "Radius"
		CurrentObjectRadius=AdjustFloat#("Radius: ", CurrentObjectRadius, 0.01, 0.1, 150)
		
	Case "Status"
		If RandomStatus
			If OnLeftHalfAdjuster()
				RandomStatusMin=AdjustInt("Status Min: ", RandomStatusMin, SlowInt, FastInt, DelayTime)
			Else
				RandomStatusMax=AdjustInt("Status Max: ", RandomStatusMax, SlowInt, FastInt, DelayTime)
			EndIf
		Else
			CurrentObjectStatus=AdjustInt("Status: ", CurrentObjectStatus, SlowInt, FastInt, DelayTime)
		EndIf
		If ReturnPressed()
			RandomStatus=Not RandomStatus
		EndIf






	
	

			
		

	
	End Select
	BuildCurrentObjectModel()

End Function 


Function UpdateLevelTile(i,j)

	If i<0 Or j<0 Or i>=levelwidth Or j>=levelheight Then Return
	
	; top face
	CalculateUV(LevelTileTexture(i,j),0,0,LevelTileRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+0,i,LevelTileExtrusion(i,j)+LevelTileHeight(i,j),-j
	VertexTexCoords LevelSurface(j),i*20+0,ChunkTileU,ChunkTileV
;	VertexColor LevelSurface(j),i*20+0,(LevelTileHeight(i,j)+8.0)/15.0*255,(LevelTileHeight(i,j)+8.0)/15.0*255,(LevelTileHeight(i,j)+8.0)/15.0*255
	CalculateUV(LevelTileTexture(i,j),1,0,LevelTileRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+1,i+1,LevelTileExtrusion(i,j)+LevelTileHeight(i,j),-j
	VertexTexCoords LevelSurface(j),i*20+1,ChunkTileU,ChunkTileV
;	VertexColor LevelSurface(j),i*20+1,(LevelTileHeight(i,j)+8.0)/15.0*255,(LevelTileHeight(i,j)+8.0)/15.0*255,(LevelTileHeight(i,j)+8.0)/15.0*255
	CalculateUV(LevelTileTexture(i,j),0,1,LevelTileRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+2,i,LevelTileExtrusion(i,j)+LevelTileHeight(i,j),-j-1
	VertexTexCoords LevelSurface(j),i*20+2,ChunkTileU,ChunkTileV
;	VertexColor LevelSurface(j),i*20+2,(LevelTileHeight(i,j)+8.0)/15.0*255,(LevelTileHeight(i,j)+8.0)/15.0*255,(LevelTileHeight(i,j)+8.0)/15.0*255
	CalculateUV(LevelTileTexture(i,j),1,1,LevelTileRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+3,i+1,LevelTileExtrusion(i,j)+LevelTileHeight(i,j),-j-1
	VertexTexCoords LevelSurface(j),i*20+3,ChunkTileU,ChunkTileV
;	VertexColor LevelSurface(j),i*20+3,(LevelTileHeight(i,j)+8.0)/15.0*255,(LevelTileHeight(i,j)+8.0)/15.0*255,(LevelTileHeight(i,j)+8.0)/15.0*255
			
	; north face
	z2=0
	If j=0
		z#=0.0
	Else
		z#=LevelTileExtrusion(i,j-1)
		If LevelTileExtrusion(i,j-1)>=LevelTileExtrusion(i,j) z2=-100
	EndIf

	CalculateUV(LevelTileSideTexture(i,j),0,0,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+4,i+1,LevelTileExtrusion(i,j)+z2,-j
	VertexTexCoords LevelSurface(j),i*20+4,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),1,0,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+5,i,LevelTileExtrusion(i,j)+z2,-j
	VertexTexCoords LevelSurface(j),i*20+5,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),0,1,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+6,i+1,z+z2,-j
	VertexTexCoords LevelSurface(j),i*20+6,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),1,1,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+7,i,z+z2,-j
	VertexTexCoords LevelSurface(j),i*20+7,ChunkTileU,ChunkTileV
	
	
			
			
	; east face
	z2=0
	If i=LevelWidth-1
		z#=0.0
	Else
		z#=LevelTileExtrusion(i+1,j)
		If LevelTileExtrusion(i+1,j)>=LevelTileExtrusion(i,j) z2=-100
	EndIf

	CalculateUV(LevelTileSideTexture(i,j),0,0,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+8,i+1,LevelTileExtrusion(i,j)+z2,-j-1
	VertexTexCoords LevelSurface(j),i*20+8,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),1,0,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+9,i+1,LevelTileExtrusion(i,j)+z2,-j
	VertexTexCoords LevelSurface(j),i*20+9,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),0,1,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+10,i+1,z+z2,-j-1
	VertexTexCoords LevelSurface(j),i*20+10,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),1,1,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+11,i+1,z+z2,-j
	VertexTexCoords LevelSurface(j),i*20+11,ChunkTileU,ChunkTileV
		
			
	; south face
	z2=0
	If j=LevelHeight-1
		z#=0.0
	Else
		z#=LevelTileExtrusion(i,j+1)
		If LevelTileExtrusion(i,j+1)>=LevelTileExtrusion(i,j) z2=-100
	EndIf

	CalculateUV(LevelTileSideTexture(i,j),0,0,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+12,i,LevelTileExtrusion(i,j)+z2,-j-1
	VertexTexCoords LevelSurface(j),i*20+12,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),1,0,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+13,i+1,LevelTileExtrusion(i,j)+z2,-j-1
	VertexTexCoords LevelSurface(j),i*20+13,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),0,1,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+14,i,z+z2,-j-1
	VertexTexCoords LevelSurface(j),i*20+14,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),1,1,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+15,i+1,z+z2,-j-1
	VertexTexCoords LevelSurface(j),i*20+15,ChunkTileU,ChunkTileV
			
	
			
	; west face
	z2=0
	If i=0
		z#=0.0
	Else
		z#=LevelTileExtrusion(i-1,j)
		If LevelTileExtrusion(i-1,j)>=LevelTileExtrusion(i,j) z2=-100
	EndIf

	CalculateUV(LevelTileSideTexture(i,j),0,0,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+16,i,LevelTileExtrusion(i,j)+z2,-j
	VertexTexCoords LevelSurface(j),i*20+16,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),1,0,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+17,i,LevelTileExtrusion(i,j)+z2,-j-1
	VertexTexCoords LevelSurface(j),i*20+17,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),0,1,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+18,i,z+z2,-j
	VertexTexCoords LevelSurface(j),i*20+18,ChunkTileU,ChunkTileV
	CalculateUV(LevelTileSideTexture(i,j),1,1,LevelTileSideRotation(i,j),8)
	VertexCoords LevelSurface(j),i*20+19,i,z+z2,-j-1
	VertexTexCoords LevelSurface(j),i*20+19,ChunkTileU,ChunkTileV
		
End Function


Function UpdateWaterTile(i,j)
	
	If i<0 Or j<0 Or i>=levelwidth Or j>=levelheight Then Return

	
	; top face
	CalculateUV(WaterTileTexture(i,j),0,0,WaterTileRotation(i,j),4)
	VertexCoords WaterSurface(j),i*4+0,i,WaterTileHeight(i,j),-j
	VertexTexCoords WaterSurface(j),i*4+0,ChunkTileU,ChunkTileV
	If WaterTileTexture(i,j)>=0
		VertexColor WaterSurface(j),i*4+0,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+0,0,0,0
	EndIf
	
	CalculateUV(WaterTileTexture(i,j),1,0,WaterTileRotation(i,j),4)
	VertexCoords WaterSurface(j),i*4+1,i+1,WaterTileHeight(i,j),-j
	VertexTexCoords WaterSurface(j),i*4+1,ChunkTileU,ChunkTileV
	If WaterTileTexture(i,j)>=0
		VertexColor WaterSurface(j),i*4+1,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+1,0,0,0
	EndIf

	CalculateUV(WaterTileTexture(i,j),0,1,WaterTileRotation(i,j),4)
	VertexCoords WaterSurface(j),i*4+2,i,WaterTileHeight(i,j),-j-1
	VertexTexCoords WaterSurface(j),i*4+2,ChunkTileU,ChunkTileV
	If WaterTileTexture(i,j)>=0
		VertexColor WaterSurface(j),i*4+2,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+2,0,0,0
	EndIf

	CalculateUV(WaterTileTexture(i,j),1,1,WaterTileRotation(i,j),4)
	VertexCoords WaterSurface(j),i*4+3,i+1,WaterTileHeight(i,j),-j-1
	VertexTexCoords WaterSurface(j),i*4+3,ChunkTileU,ChunkTileV
	If WaterTileTexture(i,j)>=0
		VertexColor WaterSurface(j),i*4+3,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+3,0,0,0
	EndIf

End Function

Function UpdateLogicTile(i,j)

	; top face
	
	If LevelTileLogicHasVisuals(i,j)
		nologicshow=0
	Else
		nologicshow=-300
	EndIf
	
	If WaterTileHeight(i,j)>LevelTileExtrusion(i,j)
		height#=WaterTileHeight(i,j)+0.05
	Else
		height#=LevelTileExtrusion(i,j)+0.05
	EndIf
	VertexCoords LogicSurface(j),i*4,i+nologicshow,height,-j
	VertexCoords LogicSurface(j),i*4+1,i+1+nologicshow,height,-j
	VertexCoords LogicSurface(j),i*4+2,i+nologicshow,height,-j-1
	VertexCoords LogicSurface(j),i*4+3,i+1+nologicshow,height,-j-1
				
	
	ColorLevelTileLogic(i,j)

End Function

Function CreateSpellBallMesh(subtype)

	entity=CreateSphere(4)
	Select subtype
	Case 7,8
		EntityColor Entity,255,255,255
	Case 0
		EntityColor Entity,255,120,0
	Case 1
		EntityColor Entity,255,180,50
	Case 2
		EntityColor Entity,255,255,50
	Case 3
		EntityColor Entity,50,255,50
	Case 4
		EntityColor Entity,100,200,255
	Case 5
		EntityColor Entity,50,50,255
	Case 6
		EntityColor Entity,255,50,255
	Case 9
		EntityColor Entity,67,67,67
	End Select 
	ScaleMesh Entity,.15,.15,.15
	EntityBlend Entity,3
	
	Return Entity

End Function

Function CreateFloingBubbleMesh()

	Entity=CreateSphere()
	s=CreateCylinder()
	ScaleMesh s,0.5,0.01,0.5
	PositionMesh s,0,-0.58,0
	AddMesh s,Entity
	FreeEntity s
	EntityTexture Entity,FloingTexture
	EntityAlpha Entity,0.5
	EntityBlend Entity,3
	Return Entity

End Function

Function CreateGrowFlowerMesh(tilelogic)

	If tilelogic=2
		Return CopyEntity(ObstacleMesh(7))
	Else If tilelogic=11 Or tilelogic=12
		Return CopyEntity(ObstacleMesh(16))
		;EntityTexture ObjectEntity(Dest),waterfalltexture
	Else
		Return CopyEntity(ObstacleMesh(10))
	EndIf

End Function

Function CreateIceBlockMesh(btype)

	; type- 0-ice, 1-floing

	If btype=0
		Entity=CreateCube()
		ScaleMesh Entity,.52,.75,.52
		PositionMesh Entity,0,.75,0
		EntityAlpha Entity,.5
		EntityColor Entity,100,255,255
		EntityBlend Entity,3
	Else If btype=1
		Entity=CreateSphere()
		ScaleMesh Entity,.8,.8,.8
		PositionMesh Entity,0,.6,0
		EntityTexture Entity,FloingTexture
		EntityAlpha Entity,0.5
		EntityBlend Entity,3
	Else
		Entity=CreateSphere()
		;EntityColor Entity,255,0,0
		;ShowMessageOnce("!IceBlock with a Data3 that isn�t 0 or 1 will be invisible in-game.", 2000)
	EndIf
	

	
	
	Return Entity
End Function

Function CreateIceFloatMesh()
	Entity=CreateCylinder(16,True)
	;For i=1 To CountVertices (GetSurface(entity,1))-1
	;	VertexCoords GetSurface(entity,1),i,VertexX(GetSurface(entity,1),i)+Rnd(-.1,.1),VertexY(GetSurface(entity,1),i),VertexZ(GetSurface(entity,1),i)+Rnd(-.1,.1)
	;Next
	ScaleMesh Entity,.45,.05,.45
	PositionMesh Entity,0,-.1,0
	EntityAlpha Entity,.8
	;EntityBlend Entity,3
	EntityColor Entity,255,255,255
	Return Entity
End Function

Function CreatePlantFloatMesh()
	Entity=CreateCylinder(9,True)
	;For i=1 To CountVertices (GetSurface(entity,1))-1
	;	VertexCoords GetSurface(entity,1),i,VertexX(GetSurface(entity,1),i)+Rnd(-.1,.1),VertexY(GetSurface(entity,1),i),VertexZ(GetSurface(entity,1),i)+Rnd(-.1,.1)
	;Next
	ScaleMesh Entity,.45,.05,.45
	PositionMesh Entity,0,-.1,0
	EntityAlpha Entity,.7
	EntityBlend Entity,3
	EntityColor Entity,0,255,0
	Return Entity
End Function

Function CreateFlipBridgeMesh(tex)
	

	subtype=3
	
	Pivot=CreateMesh()
	Entity=CreateMesh()
	Surface=CreateSurface(Entity)
	
	; Top 
	AddVertex (surface,-.25,.1,.49,.76,.01)
	AddVertex (surface,.25,.1,.49,.76+.24,.01)
	AddVertex (surface,-.25,.1,-.49,.76,.24)
	AddVertex (surface,.25,.1,-.49,.76+.24,.24)
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	AddVertex (surface,-.20,.105,.45,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
	AddVertex (surface,-.10,.105,.45,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
	AddVertex (surface,-.20,.105,-.45,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
	AddVertex (surface,-.10,.105,-.45,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	AddTriangle (surface,4,5,6)
	AddTriangle (surface,5,7,6)
	AddVertex (surface,.10,.105,.45,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
	AddVertex (surface,.20,.105,.45,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
	AddVertex (surface,.10,.105,-.45,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
	AddVertex (surface,.20,.105,-.45,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	AddTriangle (surface,8,9,10)
	AddTriangle (surface,9,11,10)

	
	
	
	; Sides
	For i=0 To 3
		Select i
		Case 0
			x1#=-.25
			x2#=.25
			y1#=-.49
			y2#=-.49
		Case 1
			x1#=.25
			x2#=.25
			y1#=-.49
			y2#=.49
		Case 2
			x1#=.25
			x2#=-.25
			y1#=.49
			y2#=.49
		Case 3
			x1#=-.25
			x2#=-.25
			y1#=.49
			y2#=-.49
		End Select

	
			
		AddVertex (surface,x1,.104,y1,subtype*0.25+.01,.01)
		AddVertex (surface,x2,.104,y2,subtype*0.25+.24,.01)
		AddVertex (surface,x1,-.4,y1,subtype*0.25+.01,.24)
		AddVertex (surface,x2,-.4,y2,subtype*0.25+.24,.24)
		AddTriangle (surface,12+i*4,13+i*4,14+i*4)
		AddTriangle (surface,13+i*4,15+i*4,14+i*4)
;		AddVertex (surface,x1*1.01,.8,y1*1.01,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
;		AddVertex (surface,x2*1.01,.8,y2*1.01,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
;		AddVertex (surface,x1*1.01,.6,y1*1.01,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
;		AddVertex (surface,x2*1.01,.6,y2*1.01,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	
;		AddTriangle (surface,16+i*8,17+i*8,18+i*8)
;		AddTriangle (surface,17+i*8,19+i*8,18+i*8)
		
		
	Next
	
	
	

	
	
	UpdateNormals Entity
	
	EntityTexture Entity,GateTexture
	
	YScale=6.6 ; to reflect active state
	ScaleEntity Entity,1.0,1.0,YScale
	
	;Entity2=CreateCylinder()
	;ScaleEntity Entity2,.25,.11,.25
	;AddMesh Entity2,Entity
	;FreeEntity Entity2

	Entity2=CreateCylinder(32)
	ScaleMesh Entity2,.35,.35,.35
	PositionMesh Entity2,0.0,-.241,0.0
	EntityTexture Entity2,CageTexture
	
	EntityParent Entity,Pivot
	EntityParent Entity2,Pivot
	;AddMesh Entity2,Entity
	;FreeEntity Entity2
	
	Return Pivot

End Function

Function CreateVoidMesh()

	LevelExitEntity=CreateMesh()
	surface=CreateSurface(LevelExitEntity)
	For i=0 To 17
		AddVertex surface,.5*Sin(i*20),1,.5*Cos(i*20),i*.0555,0
		AddVertex surface,.1*Sin(i*20),0,.1*Cos(i*20),i*.0555,1
	Next
	For i=0 To 16
		AddTriangle surface,i*2,i*2+2,i*2+1
		AddTriangle surface,i*2+2,i*2+3,i*2+1
		AddTriangle surface,i*2+1,i*2+2,i*2
		AddTriangle surface,i*2+1,i*2+3,i*2+2

	Next
	AddTriangle surface,34,0,35
	AddTriangle surface,0,1,35
	AddTriangle surface,35,0,34
	AddTriangle surface,35,1,1
	For i=0 To 17
		AddVertex surface,.7*Sin(i*20),.5,.7*Cos(i*20),i*.0555,0
		AddVertex surface,.15*Sin(i*20),0,.15*Cos(i*20),i*.0555,1
	Next
	For i=18 To 34
		AddTriangle surface,i*2,i*2+2,i*2+1
		AddTriangle surface,i*2+2,i*2+3,i*2+1
		AddTriangle surface,i*2+1,i*2+2,i*2
		AddTriangle surface,i*2+1,i*2+3,i*2+2

	Next
	AddTriangle surface,70,36,71
	AddTriangle surface,36,37,71
	AddTriangle surface,71,36,70
	AddTriangle surface,71,37,36


	UpdateNormals LevelExitEntity
	EntityBlend LevelExitEntity,3
;	EntityAlpha LevelExitEntity,.5
	
	EntityTexture LevelExitEntity,VoidTexture
	
;	PositionEntity LevelExitEntity,x+.5,0,-y-.5



	Return LevelExitEntity
End Function


Function BuildCurrentTileModel()
	
	j=0
	i=0
	ClearSurface CurrentSurface
	
	; add block
	
	; top face
	CalculateUV(CurrentTileTexture,0,0,CurrentTileRotation,8)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileTexture,1,0,CurrentTileRotation,8)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileTexture,0,1,CurrentTileRotation,8)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileTexture,1,1,CurrentTileRotation,8)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)
	
	AddTriangle (CurrentSurface,i*20+0,i*20+1,i*20+2)
	AddTriangle (CurrentSurface,i*20+1,i*20+3,i*20+2)
	
	; north face
	CalculateUV(CurrentTileSideTexture,0,0,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,1,0,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,0,1,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,1,99,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,1,1,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,-1,99,1,ChunkTileU,ChunkTileV)

	
	AddTriangle (CurrentSurface,i*20+4,i*20+5,i*20+6)
	AddTriangle (CurrentSurface,i*20+5,i*20+7,i*20+6)
	
	
	; east face
	CalculateUV(CurrentTileSideTexture,0,0,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,1,0,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,0,1,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,1,99,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,1,1,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,1,99,1,ChunkTileU,ChunkTileV)

	
	AddTriangle (CurrentSurface,i*20+8,i*20+9,i*20+10)
	AddTriangle (CurrentSurface,i*20+9,i*20+11,i*20+10)
	
	; south face
	CalculateUV(CurrentTileSideTexture,0,0,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,1,0,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,0,1,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,-1,99,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,1,1,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,1,99,-1,ChunkTileU,ChunkTileV)

	
	AddTriangle (CurrentSurface,i*20+12,i*20+13,i*20+14)
	AddTriangle (CurrentSurface,i*20+13,i*20+15,i*20+14)
	
	; west face
	CalculateUV(CurrentTileSideTexture,0,0,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,1,0,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,0,1,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,-1,99,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTileSideTexture,1,1,CurrentTileSideRotation,8)
	AddVertex (CurrentSurface,-1,99,-1,ChunkTileU,ChunkTileV)

	
	AddTriangle (CurrentSurface,i*20+16,i*20+17,i*20+18)
	AddTriangle (CurrentSurface,i*20+17,i*20+19,i*20+18)

	
	UpdateNormals CurrentMesh
	EntityTexture CurrentMesh,LevelTexture
	
	; and the water tile
	; top face
	
	If CurrentWaterTile>0 Then FreeEntity CurrentWaterTile
	
	CurrentWaterTile=CreateMesh()
	mySurface=CreateSurface(CurrentWaterTile)
	
	If CurrentWaterTileRotation=0
		AddVertex (mySurface,1,99.5,3,CurrentWaterTileTexture/4.0,0)
		AddVertex (mySurface,3,99.5,3,CurrentWaterTileTexture/4.0+.25,0)
		AddVertex (mySurface,1,99.5,1,CurrentWaterTileTexture/4.0,.25)
		AddVertex (mySurface,3,99.5,1,CurrentWaterTileTexture/4.0+.25,.25)
		
	EndIf
	If CurrentWaterTileRotation=1
		AddVertex (mySurface,1,99.5,3,CurrentWaterTileTexture/4.0,.25)
		AddVertex (mySurface,3,99.5,3,CurrentWaterTileTexture/4.0,0)
		AddVertex (mySurface,1,99.5,1,CurrentWaterTileTexture/4.0+.25,.25)
		AddVertex (mySurface,3,99.5,1,CurrentWaterTileTexture/4.0+.25,0)
		
	EndIf
	If CurrentWaterTileRotation=2
		AddVertex (mySurface,1,99.5,3,CurrentWaterTileTexture/4.0+.25,.25)
		AddVertex (mySurface,3,99.5,3,CurrentWaterTileTexture/4.0,.25)
		AddVertex (mySurface,1,99.5,1,CurrentWaterTileTexture/4.0+.25,0)
		AddVertex (mySurface,3,99.5,1,CurrentWaterTileTexture/4.0,0)
		
	EndIf
	If CurrentWaterTileRotation=3
		AddVertex (mySurface,1,99.5,3,CurrentWaterTileTexture/4.0+.25,0)
		AddVertex (mySurface,3,99.5,3,CurrentWaterTileTexture/4.0+.25,.25)
		AddVertex (mySurface,1,99.5,1,CurrentWaterTileTexture/4.0,0)
		AddVertex (mySurface,3,99.5,1,CurrentWaterTileTexture/4.0,.25)
		
	EndIf
	If CurrentWaterTileRotation=4
		AddVertex (mySurface,1,99.5,3,CurrentWaterTileTexture/4.0+.25,0)
		AddVertex (mySurface,3,99.5,3,CurrentWaterTileTexture/4.0,0)
		AddVertex (mySurface,1,99.5,1,CurrentWaterTileTexture/4.0+.25,.25)
		AddVertex (mySurface,3,99.5,1,CurrentWaterTileTexture/4.0,.25)
		
	EndIf
	If CurrentWaterTileRotation=5
		AddVertex (mySurface,1,99.5,3,CurrentWaterTileTexture/4.0,0)
		AddVertex (mySurface,3,99.5,3,CurrentWaterTileTexture/4.0,.25)
		AddVertex (mySurface,1,99.5,1,CurrentWaterTileTexture/4.0+.25,0)
		AddVertex (mySurface,3,99.5,1,CurrentWaterTileTexture/4.0+.25,.25)
		
	EndIf
	If CurrentWaterTileRotation=6
		AddVertex (mySurface,1,99.5,3,CurrentWaterTileTexture/4.0,.25)
		AddVertex (mySurface,3,99.5,3,CurrentWaterTileTexture/4.0+.25,.25)
		AddVertex (mySurface,1,99.5,1,CurrentWaterTileTexture/4.0,0)
		AddVertex (mySurface,3,99.5,1,CurrentWaterTileTexture/4.0+.25,0)
		
	EndIf
	If CurrentWaterTileRotation=7
		AddVertex (mySurface,1,99.5,3,CurrentWaterTileTexture/4.0+.25,.25)
		AddVertex (mySurface,3,99.5,3,CurrentWaterTileTexture/4.0+.25,0)
		AddVertex (mySurface,1,99.5,1,CurrentWaterTileTexture/4.0,.25)
		AddVertex (mySurface,3,99.5,1,CurrentWaterTileTexture/4.0,0)
		
	EndIf
	AddTriangle (MySurface,0,1,2)
	AddTriangle (MySurface,2,1,3)
	UpdateNormals CurrentWaterTile
	EntityTexture CurrentWaterTile,WaterTexture
	
End Function

Function BuildCurrentObjectModel()
	
	If CurrentObjectModel>0 
		FreeEntity CurrentObjectModel
		CurrentObjectModel=0
	EndIf
	If CurrentObjectTexture>0 
		FreeTexture CurrentObjectTexture
		CurrentObjectTexture=0
	EndIf
	
	If CurrentHatModel>0
		FreeEntity CurrentHatModel
		CurrentHatModel=0
	EndIf
	If CurrentAccModel>0
		FreeEntity CurrentAccModel
		CurrentAccModel=0
	EndIf
	If CurrentHatTexture>0
		FreeTexture CurrentHatTexture
		CurrentHatTexture=0
	EndIf
	If CurrentAccTexture>0
		FreeTexture CurrentAccTexture
		CurrentAccTexture=0
	EndIf



	
	
	

	If CurrentObjectModelName$="!Button"
		If CurrentObjectSubType=16 And CurrentObjectData(2)=1 Then CurrentObjectSubType=17
		If CurrentObjectSubType=17 And CurrentObjectData(2)=0 Then CurrentObjectSubType=16
		If CurrentObjectSubType=16+32 And CurrentObjectData(2)=1 Then CurrentObjectSubType=17+32
		If CurrentObjectSubType=17+32 And CurrentObjectData(2)=0 Then CurrentObjectSubType=16+32

		CurrentObjectModel=CreateButtonMesh(CurrentObjectSubType,CurrentObjectData(0),CurrentObjectData(1),CurrentObjectData(2),CurrentObjectData(3))
		
		
	Else If CurrentObjectModelName$="!CustomModel"
		
		If FileType("UserData\Custom\Models\"+currentObjectTextData(0)+".3ds")<>1 Or FileType("UserData\Custom\Models\"+currentObjectTextData(0)+".jpg")<>1
			Print "Couldn't Load 3ds/jpg."
			Print "Reverting to 'Default' Custom Model."
 			Delay 2000
			
			CurrentObjectTextData(0)="Default"
		EndIf
		CurrentObjectModel=LoadMesh("UserData\Custom\Models\"+currentObjectTextData(0)+".3ds")
		CurrentObjectTexture=LoadTexture("UserData\Custom\Models\"+currentObjectTextData(0)+".jpg")
		EntityTexture CurrentObjectModel,CurrentObjectTexture

		
	
	
	Else If CurrentObjectModelName$="!Teleport"
		CurrentObjectModel=CreateTeleporterMesh(CurrentObjectData(0))
	Else If CurrentObjectModelName$="!Item"
		CurrentObjectModel=CreatePickupItemMesh(CurrentObjectData(2))
	Else If CurrentObjectModelName$="!Stinker" Or CurrentObjectModelName$="!NPC"
		CurrentObjectModel=CopyEntity(StinkerMesh)
		
		If CurrentObjectData(0)=5
			CurrentObjectTexture=Waterfalltexture(0) ;MyLoadTexture("Data\leveltextures\waterfall.jpg",1)
		Else If CurrentObjectData(0)=6
			CurrentObjectTexture=Waterfalltexture(1) ;MyLoadTexture("Data\leveltextures\waterfalllava.jpg",1)

		Else
			CurrentObjectTexture=MyLoadTexture("data/models/stinker/body00"+Str$(CurrentObjectData(0))+Chr$(65+CurrentObjectData(1))+".jpg",1)
		EndIf
		EntityTexture GetChild(CurrentObjectModel,3),CurrentObjectTexture
		
		
		
		If CurrentObjectData(2)>0	; hat
			If CurrentObjectData(2)>9 ; two digit
				CurrentHatModel=MyLoadMesh("data/models/stinker/accessory0"+Str$(CurrentObjectData(2))+".3ds",0)
				CurrentHatTexture=MyLoadTexture("data/models/stinker/accessory0"+Str$(CurrentObjectData(2))+Chr$(64+CurrentObjectData(3))+".jpg",4)
			Else
				CurrentHatModel=MyLoadMesh("data/models/stinker/accessory00"+Str$(CurrentObjectData(2)+".3ds"),0)
				CurrentHatTexture=MyLoadTexture("data/models/stinker/accessory00"+Str$(CurrentObjectData(2))+Chr$(64+CurrentObjectData(3))+".jpg",4)
			EndIf
		EndIf
		
		If CurrentObjectData(4)>100	; acc
			
				CurrentAccModel=MyLoadMesh("data/models/stinker/accessory"+Str$(CurrentObjectData(4))+".3ds",0)
				CurrentAccTexture=MyLoadTexture("data/models/stinker/accessory"+Str$(CurrentObjectData(4))+Chr$(65+CurrentObjectData(5))+".jpg",4)
			
		EndIf

		


	
	
	Else If CurrentObjectModelName$="!ColourGate"
		CurrentObjectModel=CreateColourGateMesh(CurrentObjectData(2),CurrentObjectData(0))
	Else If CurrentObjectModelName$="!Transporter"
		CurrentObjectModel=CreateTransporterMesh(CurrentObjectData(0),3)
		RotateMesh CurrentObjectModel,0,90*CurrentObjectData(2),0
		
	Else If CurrentObjectModelName$="!Conveyor"
		If CurrentObjectData(4)=4
			CurrentObjectModel=CreateCloudMesh(CurrentObjectData(0))
		Else
			CurrentObjectModel=CreateTransporterMesh(CurrentObjectData(0),CurrentObjectData(4))
		EndIf
		RotateMesh CurrentObjectModel,0,-90*CurrentObjectData(2),0
		If CurrentObjectType=46 ScaleMesh CurrentObjectmodel,.5,.5,.5

	Else If CurrentObjectModelName$="!Autodoor"
		CurrentObjectModel=CopyEntity(AutodoorMesh)
		
		
		
	Else If CurrentObjectModelName$="!Key"
		CurrentObjectModel=CreateKeyMesh(CurrentObjectData(0))
	Else If CurrentObjectModelName$="!KeyCard" 
		CurrentObjectModel=CreateKeyCardMesh(CurrentObjectData(0))

		
	Else If CurrentObjectModelName$="!StinkerWee"
		CurrentObjectModel=CopyEntity(StinkerWeeMesh)
	Else If CurrentObjectModelName$="!Cage"
		CurrentObjectModel=CopyEntity(CageMesh)
		Else If CurrentObjectModelName$="!StarGate"
		CurrentObjectModel=CopyEntity(StarGateMesh)
	Else If CurrentObjectModelName$="!Scritter"
		CurrentObjectModel=CopyEntity(ScritterMesh)
		EntityTexture CurrentObjectModel,ScritterTexture(CurrentObjectData(0))
	Else If CurrentObjectModelName$="!RainbowBubble"
		CurrentObjectModel=CreateSphere()
		;ScaleMesh CurrentObjectModel,.4,.4,.4
		;PositionMesh CurrentObjectModel,0,1,0
		ScaleMesh CurrentObjectModel,.5,.5,.5
		EntityTexture CurrentObjectModel,Rainbowtexture2
		
	Else If CurrentObjectModelName$="!IceBlock"
		CurrentObjectModel=CreateIceBlockMesh(CurrentObjectData(3))
		
	Else If CurrentObjectModelName$="!PlantFloat"
		CurrentObjectModel=CreatePlantFloatMesh()
		;CurrentObjectModel=CreateSphere()
		;ScaleMesh CurrentObjectModel,.4,.1,.4
;		PositionMesh CurrentObjectModel,0,1,0
		;EntityTexture CurrentObjectModel,Rainbowtexture
		
	Else If CurrentObjectModelName$="!IceFloat"
		CurrentObjectModel=CreateIceFloatMesh()
		;CurrentObjectModel=CreateSphere()
		;ScaleMesh CurrentObjectModel,.4,.1,.4
;		PositionMesh CurrentObjectModel,0,1,0




	Else If CurrentObjectModelName$="!Chomper"
		CurrentObjectModel=CopyEntity(ChomperMesh)
		If CurrentObjectSubType=1 
			EntityTexture CurrentObjectModel,WaterChomperTexture
		Else If CurrentObjectData(1)=3 
			EntityTexture CurrentObjectModel,MechaChomperTexture
		Else
			EntityTexture CurrentObjectModel,ChomperTexture
		EndIf
	Else If CurrentObjectModelName$="!Bowler"
		CurrentObjectModel=CopyEntity(BowlerMesh)
		Direction=CurrentObjectData(0)
		If CurrentObjectData(1)<>2
			Direction=Direction*2
		EndIf
		CurrentObjectYawAdjust=(-45*Direction +3600) Mod 360
	Else If CurrentObjectModelName$="!Turtle"
		CurrentObjectModel=CopyEntity(TurtleMesh)
		CurrentObjectYawAdjust=(-90*CurrentObjectData(0) +3600) Mod 360
	Else If CurrentObjectModelName$="!Thwart"
		CurrentObjectModel=CopyEntity(ThwartMesh)
		EntityTexture CurrentObjectModel,ThwartTexture(CurrentObjectData(2))
	Else If CurrentObjectModelName$="!Tentacle"
		CurrentObjectModel=CopyEntity(TentacleMesh)
		Animate GetChild(CurrentObjectModel,3),1,.1,1,0
	Else If CurrentObjectModelName$="!Lurker"
		CurrentObjectModel=CopyEntity(LurkerMesh)
	Else If CurrentObjectModelName$="!Ghost"
		CurrentObjectModel=CopyEntity(GhostMesh)
	Else If CurrentObjectModelName$="!Wraith"
		CurrentObjectModel=CopyEntity(WraithMesh)
		EntityTexture CurrentObjectModel,WraithTexture(CurrentObjectData(2))

	

	Else If CurrentObjectModelName$="!Crab"
		CurrentObjectModel=CopyEntity(CrabMesh)
		If CurrentObjectSubType=0 Then EntityTexture CurrentObjectModel,CrabTexture2
	Else If CurrentObjectModelName$="!Troll"
		CurrentObjectModel=CopyEntity(TrollMesh)
	Else If CurrentObjectModelName$="!Kaboom"
		CurrentObjectModel=CopyEntity(KaboomMesh)
	Else If CurrentObjectModelName$="!BabyBoomer"
		CurrentObjectModel=CopyEntity(KaboomMesh)




	Else If CurrentObjectModelName$="!FireFlower"
		CurrentObjectModel=CopyEntity(FireFlowerMesh)
		If CurrentObjectSubType<>1
			CurrentObjectYawAdjust=(-45*CurrentObjectData(0) +3600) Mod 360
		Else
			CurrentObjectYawAdjust=0
		EndIf
		If CurrentObjectData(1)=1
			EntityTexture CurrentObjectModel,FireFlowerTexture2
		EndIf
		
	Else If CurrentObjectModelName$="!BurstFlower"
		CurrentObjectModel=CopyEntity(BurstFlowerMesh)

	Else If CurrentObjectModelName$="!Busterfly"
		CurrentObjectModel=CopyEntity(BusterflyMesh)
		
		
	Else If CurrentObjectModelName$="!GlowWorm"  Or CurrentObjectModelName$="!Zipper"
		CurrentObjectModel=CreateSphere(12)
		ScaleMesh CurrentObjectModel,.1,.1,.1
		EntityColor CurrentObjectModel,CurrentObjectData(5),CurrentObjectData(6),CurrentObjectData(7)
	Else If CurrentObjectModelName$="!Void"
		;CurrentObjectModel=CreateSphere(12)
		;ScaleMesh CurrentObjectModel,.4,.15,.4
		CurrentObjectModel=CreateVoidMesh()
	Else If CurrentObjectModelName$="!Rubberducky"
		CurrentObjectModel=CopyEntity(RubberduckyMesh)

	Else If CurrentObjectModelName$="!Barrel1"
		CurrentObjectModel=CopyEntity(BarrelMesh)
		EntityTexture CurrentObjectModel,BarrelTexture1
	Else If CurrentObjectModelName$="!Barrel2"
		CurrentObjectModel=CopyEntity(BarrelMesh)
		EntityTexture CurrentObjectModel,BarrelTexture2
	Else If CurrentObjectModelName$="!Barrel3"
		CurrentObjectModel=CopyEntity(BarrelMesh)
		EntityTexture CurrentObjectModel,BarrelTexture3
	Else If CurrentObjectModelName$="!Cuboid"
		CurrentObjectModel=CreateCube()
		ScaleMesh CurrentObjectModel,0.4,0.4,0.4
		PositionMesh CurrentObjectModel,0,0.5,0
		If CurrentObjectData(0)<0 Or CurrentObjectData(0)>8 Then CurrentObjectData(0)=0
		EntityTexture CurrentObjectModel,TeleporterTexture(CurrentObjectData(0))
		
	Else If CurrentObjectModelName$="!Prism"
		CurrentObjectModel=CopyEntity(PrismMesh)
		EntityTexture CurrentObjectModel,PrismTexture
			
	Else If  CurrentObjectModelName$="!Obstacle10" 
		CurrentObjectModel=CopyEntity(  ObstacleMesh(10 ))
		EntityTexture CurrentObjectModel, MushroomTex(  (Abs(CurrentObjectData(0))) Mod 3)

	

		
	Else If  CurrentObjectModelName$="!Obstacle51" Or CurrentObjectModelName$="!Obstacle55" Or CurrentObjectModelName$="!Obstacle59"
		CurrentObjectModel=CopyEntity(  ObstacleMesh((Asc(Mid$(CurrentObjectModelName$,10,1))-48)*10+(Asc(Mid$(CurrentObjectModelName$,11,1))-48)+CurrentObjectData(0))  )
		EntityTexture CurrentObjectModel, ObstacleTexture((Asc(Mid$(CurrentObjectModelName$,10,1))-48)*10+(Asc(Mid$(CurrentObjectModelName$,11,1))-48)+CurrentObjectData(1))

	Else If Left$(CurrentObjectModelName$,9)="!Obstacle"
		CurrentObjectModel=CopyEntity(ObstacleMesh((Asc(Mid$(CurrentObjectModelName$,10,1))-48)*10+(Asc(Mid$(CurrentObjectModelName$,11,1))-48)))

	Else If CurrentObjectModelName$="!WaterFall"
		CurrentObjectModel=CreateWaterFallMesh(CurrentObjectData(0))
	Else If CurrentObjectModelName$="!Star"
		CurrentObjectModel=CopyEntity(StarMesh)
		EntityTexture CurrentObjectModel,GoldStarTexture
	Else If CurrentObjectModelName$="!Wisp"
		CurrentObjectModel=CopyEntity(StarMesh)
		EntityTexture CurrentObjectModel,WispTexture(CurrentObjectData(0))
	
	
	Else If CurrentObjectModelName$="!Portal Warp"
		CurrentObjectModel=CopyEntity(PortalWarpMesh)
		If CurrentObjectData(1)=0
			EntityTexture CurrentObjectModel,StarTexture
		Else
			EntityTexture CurrentObjectModel,RainbowTexture
		EndIf
		
	Else If CurrentObjectModelName$="!Sun Sphere1"
		CurrentObjectModel=CreateSphere()
		EntityColor CurrentObjectModel,CurrentObjectData(0),CurrentObjectData(1),CurrentObjectData(2)
		EntityBlend CurrentObjectModel,3
		
	Else If CurrentObjectModelName$="!Sun Sphere2"
		CurrentObjectModel=CreateSphere()
		ScaleMesh CurrentObjectModel,.5,.5,.5



	Else If CurrentObjectModelName$="!Coin"
		CurrentObjectModel=CopyEntity(CoinMesh)
		EntityTexture CurrentObjectModel,GoldCoinTexture
		If CurrentObjectType=425 EntityTexture CurrentObjectModel,Retrorainbowcointexture
	Else If CurrentObjectModelName$="!Token"
		CurrentObjectModel=CopyEntity(CoinMesh)
		EntityTexture CurrentObjectModel,TokenCoinTexture
	Else If CurrentObjectModelName$="!Gem"
		If currentobjectdata(0)<0 Or currentobjectdata(0)>2 Then currentobjectdata(0)=0
		CurrentObjectModel=CopyEntity(GemMesh(CurrentObjectData(0)))
		If currentobjectdata(1)<0 Or currentobjectdata(1)>7 Then currentobjectdata(1)=0
		EntityTexture CurrentObjectModel,TeleporterTexture(CurrentObjectData(1))
	Else If CurrentObjectModelName$="!Crystal"
		CurrentObjectModel=CopyEntity(GemMesh(2))
		If currentobjectdata(0)=0
			EntityTexture currentobjectmodel,rainbowtexture
		Else
			EntityTexture currentobjectmodel,ghosttexture
		EndIf
			


	Else If CurrentObjectModelName$="!Sign"
		CurrentObjectModel=CopyEntity(SignMesh(CurrentObjectData(0)))
		EntityTexture CurrentObjectModel,SignTexture(CurrentObjectData(1))


	Else If CurrentObjectModelName$="!CustomItem"
		CurrentObjectModel=CreateCustomItemMesh(CurrentObjectData(0))

		
	Else If CurrentObjectModelName$="!SteppingStone"
		CurrentObjectModel=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
		If CurrentObjectData(0)<0 Or CurrentObjectData(0)>3
			CurrentObjectData(0)=0
		EndIf
		EntityTexture CurrentObjectModel,SteppingStoneTexture(CurrentObjectData(0))
	Else If CurrentObjectModelName$="!Spring" 
		CurrentObjectModel=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
		RotateMesh CurrentObjectModel,90,0,0
		CurrentObjectYawAdjust=(-45*CurrentObjectData(2) +3600) Mod 360


		EntityTexture CurrentObjectModel,Springtexture
	Else If CurrentObjectModelName$="!Suctube" 
		CurrentObjectModel=CreateSuctubemesh(CurrentObjectData(3),CurrentObjectData(0),True)
		
		CurrentObjectYawAdjust=(-90*CurrentObjectData(2) +3600) Mod 360
		
		Redosuctubemesh(CurrentObjectModel, CurrentObjectData(0), CurrentObjectActive, CurrentObjectData(2), CurrentObjectYawAdjust)

	Else If CurrentObjectModelName$="!SuctubeX" 
		CurrentObjectModel=CreateSuctubeXmesh(CurrentObjectData(3))
		CurrentObjectYawAdjust=(-90*CurrentObjectData(2) +3600) Mod 360


		

		
	Else If CurrentObjectModelName$="!FlipBridge"
		;CurrentObjectModel=CreateCube()
		;ScaleMesh CurrentObjectModel,.35,.1,.5
		
		CurrentObjectModel=CreateFlipBridgeMesh(CurrentObjectData(0))
		;EntityTexture CurrentObjectModel,GateTexture
		
		CurrentObjectYawAdjust=(-45*CurrentObjectData(2) +3600) Mod 360
	
	Else If CurrentObjectModelName$="!Door"
		CurrentObjectModel=MyLoadmesh("data\models\houses\door01.3ds",0)
	Else If CurrentObjectModelName$="!Square"
		CurrentObjectModel=MyLoadmesh("data\models\squares\square1.b3d",0)
		
	Else If CurrentObjectModelName$="!SpellBall"
		CurrentObjectModel=CreateSpellBallMesh(7) ; use white magic spellball mesh
		
	Else If CurrentObjectModelName$="!Fence1"
		CurrentObjectModel=CopyEntity(fence1)
	Else If CurrentObjectModelName$="!Fence2"
		CurrentObjectModel=CopyEntity(fence2)
	Else If CurrentObjectModelName$="!Fencepost"
		CurrentObjectModel=CopyEntity(fencepost)
	Else If CurrentObjectModelName$="!Fountain"
		CurrentObjectModel=MyLoadmesh("data\models\houses\fountain01.b3d",0)
		EntityTexture CurrentObjectModel,FountainTexture
		
	Else If CurrentObjectModelName$="!Retrobox"
		CurrentObjectModel=CopyEntity(RetroBoxMesh)
		
	Else If CurrentObjectModelName$="!Retrocoily"
		CurrentObjectModel=CopyEntity(RetroCoilyMesh)
		
	Else If CurrentObjectModelName$="!Retroscouge"
		CurrentObjectModel=CopyEntity(RetroScougeMesh)
		CurrentObjectYawAdjust=(-90*CurrentObjectData(0) +3600) Mod 360
	
	Else If CurrentObjectModelName$="!Retrozbot"
		CurrentObjectModel=CopyEntity(RetroZbotMesh)
		CurrentObjectYawAdjust=(-90*CurrentObjectData(0) +3600) Mod 360
		
	Else If CurrentObjectModelName$="!Retroufo"
		CurrentObjectModel=CopyEntity(RetroUFOMesh)
		CurrentObjectYawAdjust=(-90*CurrentObjectData(0) +3600) Mod 360
	
	Else If CurrentObjectModelName$="!Retrolasergate"
		CurrentObjectModel=CreateretrolasergateMesh(Currentobjectdata(0))
		
	Else If CurrentObjectModelName$="!Weebot"
		CurrentObjectModel=CopyEntity(WeebotMesh)
		CurrentObjectYawAdjust=(-90*CurrentObjectData(0) +3600) Mod 360
		
	Else If CurrentObjectModelName$="!Zapbot"
		CurrentObjectModel=CopyEntity(ZapbotMesh)
		CurrentObjectYawAdjust=(-90*CurrentObjectData(0) +3600) Mod 360

	Else If CurrentObjectModelName$="!Pushbot"
		CurrentObjectModel=CreatePushbotMesh(CurrentObjectData(0),CurrentObjectData(3))
		CurrentObjectYawAdjust=-CurrentObjectData(2)*90
		
	Else If CurrentObjectModelName$="!ZbotNPC"
		CurrentObjectModel=CopyEntity(ZbotNPCMesh)
		EntityTexture CurrentObjectModel,ZBotNPCTexture(CurrentObjectData(2))
	
	Else If CurrentObjectModelName$="!Mothership"
		CurrentObjectModel=CopyEntity(MothershipMesh)


		
	Else If CurrentObjectModelName="!FloingOrb" ; not to be confused with !FloingBubble
		CurrentObjectModel=CreateSphere()
		ScaleMesh CurrentObjectModel,.3,.3,.3
		EntityColor CurrentObjectModel,255,0,0
	
	Else If CurrentObjectModelName="!MagicMirror"
		CurrentObjectModel=CreateCube()
		ScaleMesh CurrentObjectModel,3.49,2.49,.52
		EntityColor CurrentObjectModel,255,0,0
		EntityAlpha CurrentObjectModel,.5

	
	
	Else If CurrentObjectModelName$="!SkyMachineMap"
		CurrentObjectModel=CreateCube()
		ScaleMesh CurrentObjectModel,2.5,.01,2.5
		PositionMesh CurrentObjectModel,0,0,-1
		EntityTexture CurrentObjectModel,SkyMachineMapTexture
		EntityBlend CurrentObjectModel,3
		
	
	Else If CurrentObjectModelName$="!GrowFlower"
		CurrentObjectModel=CreateGrowFlowerMesh(CurrentObjectData(0))

	Else If CurrentObjectModelName$="!FloingBubble"
		CurrentObjectModel=CreateFloingBubbleMesh()

		
	Else If CurrentObjectModelName$="!None"
		CurrentObjectModel=CreateSphere()
		ScaleMesh CurrentObjectModel,.2,.2,.2
		;EntityAlpha CurrentObjectModel,.5
		
	Else ;unknown model
		CurrentObjectModel=CreateSphere()
		ScaleMesh CurrentObjectModel,.3,.3,.3
		EntityColor CurrentObjectModel,255,0,255
	

	EndIf

	If CurrentObjectModelName$="!FlipBridge"
		TextureTarget=GetChild(CurrentObjectModel,1)
	Else
		TextureTarget=CurrentObjectModel
	EndIf

	If CurrentObjectTextureName$="!None" 
		CurrentObjectTexture=0
	Else If CurrentObjectTextureName$="!Door"
		If CurrentObjectData(5)<0 Then CurrentObjectData(5)=0
		If CurrentObjectData(5)>2 Then CurrentObjectData(5)=2
		If DoorTexture(CurrentObjectData(5))=0 Then CurrentObjectData(5)=0
		EntityTexture TextureTarget,DoorTexture(CurrentObjectData(5))
	Else If CurrentObjectTextureName$="!Cottage"
		If CurrentObjectData(5)<0 Then CurrentObjectData(5)=0
		If CottageTexture(CurrentObjectData(5))=0 Then CurrentObjectData(5)=0
		EntityTexture TextureTarget,CottageTexture(CurrentObjectData(5))	
	Else If CurrentObjectTextureName$="!Townhouse"
		If CurrentObjectData(5)<0 Then CurrentObjectData(5)=0
		If HouseTexture(CurrentObjectData(5))=0 Then CurrentObjectData(5)=0
		EntityTexture TextureTarget,HouseTexture(CurrentObjectData(5))	
	Else If CurrentObjectTextureName$="!Windmill"
		If CurrentObjectData(5)<0 Then CurrentObjectData(5)=0
		If WindmillTexture(CurrentObjectData(5))=0 Then CurrentObjectData(5)=0
		EntityTexture TextureTarget,WindmillTexture(CurrentObjectData(5))	
	Else If CurrentObjectTextureName$="!Fence"
		If CurrentObjectData(5)<0 Then CurrentObjectData(5)=0
		If FenceTexture(CurrentObjectData(5))=0 Then CurrentObjectData(5)=0
		EntityTexture TextureTarget,FenceTexture(CurrentObjectData(5))	
	Else If CurrentObjectTextureName$="!FireTrap"
		EntityTexture TextureTarget,FireTrapTexture

	Else If Left$(CurrentObjectTextureName$,2)="!T"
		
		
		EntityTexture TextureTarget,StinkerTexture

		For i=1 To CountChildren(TextureTarget)
			child=GetChild(TextureTarget,i)
			EntityTexture child,StinkerTexture
		Next
	Else If CurrentObjectType=200 ; magic glove
		EntityTexture TextureTarget,GloveTex
			EntityFX TextureTarget,2
			For i=0 To 3
				Select CurrentObjectData(0)
				Case 0
					red=255
				Case 1
					red=255
					green=100
				Case 2
					red=255
					green=255
				Case 3
					green=255
				Case 4
					green=255
					blue=255
				Case 5
					blue=255
				Case 6
					red=255
					blue=255
				Case 7
					red=255
					blue=255
					green=255
				Case 8
					red=ii*64
					;green=255-ii*32
					;blue=255-ii*64
					green=64+ii*32
					blue=255-ii*32
				Case 9
					red=67
					blue=67
					green=67
				End Select
			
				VertexColor GetSurface(TextureTarget,1),i,red,green,blue
			Next

	Else If Left(CurrentObjectTextureName$,1)="?"
		; custom texture For existing objects
		If Lower(Right(CurrentObjectTextureName$,4))=".jpg" Or Lower(Right(CurrentObjectTextureName$,4))=".bmp" Or Lower(Right(CurrentObjectTextureName$,4))=".png"
			tname$="UserData\Custom\Objecttextures\"+Right(CurrentObjectTextureName$,Len(CurrentObjectTextureName$)-1)
		Else
			tname$="UserData\Custom\Objecttextures\"+Right(CurrentObjectTextureName$,Len(CurrentObjectTextureName$)-1)+".jpg"
		EndIf
		If FileType(tname$)<>1 
			tname$="UserData\Custom\Objecttextures\default.jpg"
			CurrentObjectTextureName$="?Default"
		EndIf
		
		If Lower(Right(tname$,4))=".png"
			; if png load texture with alpha map
			CurrentObjectTexture=LoadTexture(tname$,3)
		Else
			CurrentObjectTexture=LoadTexture(tname$,4)
		EndIf
		EntityTexture TextureTarget,CurrentObjectTexture
		
	Else If CurrentObjectTextureName$<>"" And CurrentObjectTextureName$<>"!None" And Left$(CurrentObjectTextureName$,1)<>"!"  And CurrentObjectmodelname$<>"!Button"
		If myFileType(CurrentObjectTextureName$)=1 Or FileType(CurrentObjectTextureName$)=1
			CurrentObjectTexture=myLoadTexture(CurrentObjectTextureName$,4)
			EntityTexture TextureTarget,CurrentObjectTexture
		Else
			Print "WARNING!"
			Print "Couldn't load texture: " + CurrentObjectTextureName$
			Print "The adventure may be unplayable in game"
			Delay 2000
		EndIf
		
				
	EndIf
	
	If CurrentObjectScaleAdjust=0.0 Then CurrentObjectScaleAdjust=1.0
	
	If CurrentObjectModelName$<>"!None"
		ScaleEntity CurrentObjectModel,CurrentObjectXScale*CurrentObjectScaleAdjust,CurrentObjectZScale*CurrentObjectScaleAdjust,CurrentObjectYScale*CurrentObjectScaleAdjust
		;RotateEntity CurrentObjectModel,CurrentObjectPitchAdjust,CurrentObjectYawAdjust,CurrentObjectRollAdjust
		RotateEntity CurrentObjectModel,0,0,0
		TurnEntity CurrentObjectModel,CurrentObjectPitchAdjust,0,CurrentObjectRollAdjust
		TurnEntity CurrentObjectModel,0,CurrentObjectYawAdjust,0
		
		If CurrentObjectModelName$="!Kaboom" Then TurnEntity CurrentObjectModel,0,90,0


	;	PositionEntity CurrentObjectModel,CurrentObjectXAdjust,CurrentObjectZAdjust+CurrentObjectZ,-CurrentObjectYAdjust
		
	EndIf

	PositionEntity CurrentObjectModel,0+CurrentObjectXAdjust,300+CurrentObjectZAdjust+CurrentObjectZ,0-CurrentObjectYAdjust
	
	If CurrentHatModel>0
	
		
		EntityTexture CurrentHatModel,CurrentHatTexture
		ScaleEntity CurrentHatModel,CurrentObjectYScale*CurrentObjectScaleAdjust,CurrentObjectZScale*CurrentObjectScaleAdjust,CurrentObjectXScale*CurrentObjectScaleAdjust
		;RotateEntity CurrentObjectModel,CurrentObjectPitchAdjust,CurrentObjectYawAdjust,CurrentObjectRollAdjust
		RotateEntity CurrentHatModel,0,0,0
		TurnEntity CurrentHatModel,CurrentObjectPitchAdjust,0,CurrentObjectRollAdjust
		TurnEntity CurrentHatModel,0,CurrentObjectYawAdjust-90,0
		
		bone=FindChild(CurrentObjectModel,"hat_bone")
	
		PositionEntity CurrentHatModel,0+CurrentObjectXAdjust,300+CurrentObjectZAdjust+CurrentObjectZ+.1+.84*CurrentObjectZScale/.035,0-CurrentObjectYAdjust


	EndIf
	
	If CurrentAccModel>0
	
		
		EntityTexture CurrentAccModel,CurrentAccTexture
		ScaleEntity CurrentAccModel,CurrentObjectYScale*CurrentObjectScaleAdjust,CurrentObjectZScale*CurrentObjectScaleAdjust,CurrentObjectXScale*CurrentObjectScaleAdjust
		;RotateEntity CurrentObjectModel,CurrentObjectPitchAdjust,CurrentObjectYawAdjust,CurrentObjectRollAdjust
		RotateEntity CurrentAccModel,0,0,0
		TurnEntity CurrentAccModel,CurrentObjectPitchAdjust,0,CurrentObjectRollAdjust
		TurnEntity CurrentAccModel,0,CurrentObjectYawAdjust-90,0
		
		bone=FindChild(CurrentObjectModel,"hat_bone")
	
		PositionEntity CurrentAccModel,0+CurrentObjectXAdjust,300+CurrentObjectZAdjust+CurrentObjectZ+.1+.84*CurrentObjectZScale/.035,0-CurrentObjectYAdjust


	EndIf

	

End Function



Function ResetLevel()

	SetCurrentGrabbedObject(-1)

	For i=0 To 99
		For j=0 To 99
			LevelTileTexture(i,j)=0 ; corresponding to squares in LevelTexture
			LevelTileRotation(i,j)=0 ; 0-3 , and 4-7 for "flipped"
			LevelTileSideTexture(i,j)=13 ; texture for extrusion walls
			LevelTileSideRotation(i,j)=0 ; 0-3 , and 4-7 for "flipped"
			LevelTileRandom#(i,j)=0 ; random height pertubation of tile
			LevelTileHeight#(i,j)=0 ; height of "center" - e.g. to make ditches and hills
			LevelTileExtrusion#(i,j)=0; extrusion with walls around it 
			LevelTileRounding(i,j)=0; 0-no, 1-yes: are floors rounded if on a drop-off corner
			LevelTileEdgeRandom(i,j)=0; 0-no, 1-yes: are edges rippled
			LevelTileLogic(i,j)=0
			
			LevelTileObjectCount(i,j)=0
		Next
	Next
	
	For i=0 To 99
		For j=0 To 99
			WaterTileHeight(i,j)=-0.2
			WaterTileTexture(i,j)=0
			WaterTileRotation(i,j)=0
			WaterTileTurbulence#(i,j)=0.1
		Next
	Next
	
	SomeObjectWasChanged()
	
	ResetParticles("data/graphics/particles.bmp")

End Function

Function CopyLevel()

	For i=0 To 99
		For j=0 To 99
			CopyLevelTileTexture(i,j)=LevelTileTexture(i,j) ; corresponding to squares in LevelTexture
			CopyLevelTileRotation(i,j)=LevelTileRotation(i,j) ; 0-3 , and 4-7 for "flipped"
			CopyLevelTileSideTexture(i,j)=LevelTileSideTexture(i,j) ; texture for extrusion walls
			CopyLevelTileSideRotation(i,j)=LevelTileSideRotation(i,j) ; 0-3 , and 4-7 for "flipped"
			CopyLevelTileRandom#(i,j)=LevelTileRandom#(i,j) ; random height pertubation of tile
			CopyLevelTileHeight#(i,j)=LevelTileHeight#(i,j) ; height of "center" - e.g. to make ditches and hills
			CopyLevelTileExtrusion#(i,j)=LevelTileExtrusion#(i,j); extrusion with walls around it 
			CopyLevelTileRounding(i,j)=LevelTileRounding(i,j); 0-no, 1-yes: are floors rounded if on a drop-off corner
			CopyLevelTileEdgeRandom(i,j)=LevelTileEdgeRandom(i,j); 0-no, 1-yes: are edges rippled
			CopyLevelTileLogic(i,j)=LevelTileLogic(i,j)
		Next
	Next
	
	For i=0 To 99
		For j=0 To 99
			CopyWaterTileHeight(i,j)=WaterTileHeight(i,j)
			CopyWaterTileTexture(i,j)=WaterTileTexture(i,j)
			CopyWaterTileRotation(i,j)=WaterTileRotation(i,j)
			CopyWaterTileTurbulence#(i,j)=WaterTileTurbulence#(i,j)
		Next
	Next

End Function

Function CopyTile(SourceX,SourceY,DestX,DestY)
	LevelTileTexture(DestX,DestY)=CopyLevelTileTexture(SourceX,SourceY) ; corresponding to squares in LevelTexture
	LevelTileRotation(DestX,DestY)=CopyLevelTileRotation(SourceX,SourceY) ; 0-3 , and 4-7 for "flipped"
	LevelTileSideTexture(DestX,DestY)=CopyLevelTileSideTexture(SourceX,SourceY) ; texture for extrusion walls
	LevelTileSideRotation(DestX,DestY)=CopyLevelTileSideRotation(SourceX,SourceY) ; 0-3 , and 4-7 for "flipped"
	LevelTileRandom#(DestX,DestY)=CopyLevelTileRandom#(SourceX,SourceY) ; random height pertubation of tile
	LevelTileHeight#(DestX,DestY)=CopyLevelTileHeight#(SourceX,SourceY) ; height of "center" - e.g. to make ditches and hills
	LevelTileExtrusion#(DestX,DestY)=CopyLevelTileExtrusion#(SourceX,SourceY); extrusion with walls around it 
	LevelTileRounding(DestX,DestY)=CopyLevelTileRounding(SourceX,SourceY); 0-no, 1-yes: are floors rounded if on a drop-off corner
	LevelTileEdgeRandom(DestX,DestY)=CopyLevelTileEdgeRandom(SourceX,SourceY); 0-no, 1-yes: are edges rippled
	LevelTileLogic(DestX,DestY)=CopyLevelTileLogic(SourceX,SourceY)

	WaterTileHeight(DestX,DestY)=CopyWaterTileHeight(SourceX,SourceY)
	WaterTileTexture(DestX,DestY)=CopyWaterTileTexture(SourceX,SourceY)
	WaterTileRotation(DestX,DestY)=CopyWaterTileRotation(SourceX,SourceY)
	WaterTileTurbulence#(DestX,DestY)=CopyWaterTileTurbulence#(SourceX,SourceY)

End Function


Function UpdateTile(i,j)
	;UpdateLevelTile(i,j)
	;UpdateWaterTile(i,j)
	UpdateLogicTile(i,j)
End Function


Function ReSizeLevel()

	If LevelWidth+WidthLeftChange>100
		WidthLeftChange=100-LevelWidth
	EndIf
	If LevelWidth+WidthRightChange>100
		WidthRightChange=100-LevelWidth
	EndIf
	If LevelHeight+HeightTopChange>100
		HeightTopChange=100-LevelHeight
	EndIf
	If LevelHeight+HeightBottomChange>100
		HeightBottomChange=100-LevelHeight
	EndIf

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			If (WidthLeftChange>=0 Or i>=-WidthLeftChange) And (HeightTopChange>=0 Or j>=-HeightTopChange)
				CopyTile(i,j,i+WidthLeftChange,j+HeightTopChange)
			EndIf
		Next
	Next
	
	LevelWidthOld=LevelWidth
	LevelHeightOld=LevelHeight
	LevelWidth=LevelWidth+WidthLeftChange+WidthRightChange
	LevelHeight=LevelHeight+HeightTopChange+HeightBottomChange
	
	; and edge
	If BorderExpandOption=0
		; use current
		If WidthLeftChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthLeftChange-1
					ChangeLevelTile(k,j,False)
				Next
			Next
		EndIf
		If WidthRightChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthRightChange-1
					ChangeLevelTile(LevelWidthOld+k,j,False)
				Next
			Next
		EndIf
		If HeightTopChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightTopChange-1
					ChangeLevelTile(i,k,False)
				Next
			Next
		EndIf
		If HeightBottomChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightBottomChange-1
					ChangeLevelTile(i,LevelHeightOld+k,False)
				Next
			Next
		EndIf
	EndIf

	If BorderExpandOption=1
		; use duplicate
		If WidthLeftChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthLeftChange-1
					CopyTile(0,j,k,j)
				Next
			Next
		EndIf
		If WidthRightChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthRightChange-1
					CopyTile(LevelWidthOld-1,j,LevelWidthOld+k,j)
				Next
			Next
		EndIf
		If HeightTopChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightTopChange-1
					CopyTile(i,0,i,k)
				Next
			Next
		EndIf
		If HeightBottomChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightBottomChange-1
					CopyTile(i,LevelHeightOld-1,i,LevelHeightOld+k)
				Next
			Next
		EndIf
	EndIf

	
	;LevelWidth=LevelWidth+WidthLeftChange+WidthRightChange
	;LevelHeight=LevelHeight+HeightTopChange+HeightBottomChange
	ReBuildLevelModel()
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next
	
	
	; and move the object
	If WidthLeftChange<>0
		For i=0 To NofObjects-1
			ObjectX(i)=ObjectX(i)+WidthLeftChange
			ObjectTileX(i)=ObjectTileX(i)+WidthLeftChange
			If Floor(ObjectX(i))<0 Or Floor(ObjectX(i))>=LevelWidth
				DeleteObject(i)
			Else
				PositionEntity ObjectEntity(i),ObjectX(i),ObjectZ(i),-ObjectY(i)
				
				; extra adjustments	
				If ObjectType(i)=50 ; spellball
					ObjectData(i,2)=ObjectTileX(i)
					ObjectData(i,3)=ObjectTileY(i)
					ObjectData(i,4)=ObjectTileX(i)
					ObjectData(i,5)=ObjectTileY(i)
				EndIf
			EndIf
		Next
	EndIf
	If HeightTopChange<>0
		For i=0 To NofObjects-1
			ObjectY(i)=ObjectY(i)+HeightTopChange
			ObjectTileY(i)=ObjectTileY(i)+HeightTopChange
			If Floor(ObjectY(i))<0 Or Floor(ObjectY(i))>=LevelHeight
				DeleteObject(i)
			Else
				PositionEntity ObjectEntity(i),ObjectX(i),ObjectZ(i),-ObjectY(i)
				
				; extra adjustments	
				If ObjectType(i)=50 ; spellball
					ObjectData(i,2)=ObjectTileX(i)
					ObjectData(i,3)=ObjectTileY(i)
					ObjectData(i,4)=ObjectTileX(i)
					ObjectData(i,5)=ObjectTileY(i)
				EndIf
			EndIf
		Next
	EndIf

	
	WidthLeftChange=0
	WidthRightChange=0
	HeightTopChange=0
	HeightBottomChange=0

End Function

Function FlipLevelX()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyTile(LevelWidth-1-i,j,i,j)
			
		Next
	Next
	ReBuildLevelModel()
	
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next
	
	; and move the object
	
	For i=0 To NofObjects-1
		ObjectX(i)=Float(LevelWidth)-ObjectX(i)
		
		ObjectTileX(i)=LevelWidth-ObjectTileX(i)
	

		

		
		PositionEntity ObjectEntity(i),ObjectX(i),ObjectZ(i),-ObjectY(i)
		
	Next
	
	SomeObjectWasChanged()
	
	
End Function

Function FlipLevelY()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyTile(i,LevelHeight-1-j,i,j)
			
		Next
	Next
	ReBuildLevelModel()
	
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next
	
	; and move the object
	
	For i=0 To NofObjects-1
		ObjectY(i)=Float(LevelHeight)-ObjectY(i)
		
		ObjectTileY(i)=LevelHeight-ObjectTileY(i)
		

		

		
		PositionEntity ObjectEntity(i),ObjectX(i),ObjectZ(i),-ObjectY(i)
		
	Next
	
	SomeObjectWasChanged()
	
	
End Function



Function FlipLevelXY()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyTile(j,i,i,j)
			
		Next
	Next
	x=LevelWidth
	LevelWidth=LevelHeight
	LevelHeight=x
	ReBuildLevelModel()
	
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next
	
	; and move the object
	
	For i=0 To NofObjects-1
		x2#=ObjectX(i)
		ObjectX(i)=ObjectY(i)
		ObjectY(i)=x2#
		x=ObjectTileX(i)
		ObjectTileX(i)=ObjectTileY(i)
		ObjectTileY(i)=x

		

		
		PositionEntity ObjectEntity(i),ObjectX(i),ObjectZ(i),-ObjectY(i)
		
	Next
	
	SomeObjectWasChanged()
	
	
End Function


			

Function CalculateUV(Texture,i2,j2,Rotation,size)

	; calculuates UV coordinates of a point on "texture" (0-7... ie the field on the 256x256 big texture)
	; at position i2/j2 (with resolution LevelDetail) and given Rotation (0-7)
	
	; returns results as Globals ChunkTileU/ChunkTileV
	uoverlap#=0
	voverlap#=0
	
	
	
	If j2=0 Or j2=1 Or i2=0 Or i2=1
		If i2=0 
			uoverlap#=.001
		Else If i2=1
			uoverlap#=-.001
		Else
			uoverlap#=0
		EndIf
		If j2=0 
			voverlap#=.001
		Else If j2=1
			voverlap#=-.001
		Else
			voverlap#=0
		EndIf
	EndIf
	
	LevelDetail=1
	
	Select Rotation
	Case 0
		ChunkTileu#=Float((Texture Mod size))/size+(Float(i2)/Float(LevelDetail))/size+uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(LevelDetail))/size+voverlap
	Case 1
		ChunkTileu#=Float(((Texture Mod size)+0))/size+(Float(j2)/Float(LevelDetail))/size+voverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(i2)/Float(LevelDetail))/size-uoverlap
	Case 2
		ChunkTileu#=Float(((Texture Mod size)+1))/size-(Float(i2)/Float(LevelDetail))/size-uoverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(j2)/Float(LevelDetail))/size-voverlap
	Case 3
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(j2)/Float(LevelDetail))/size-voverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(i2)/Float(LevelDetail))/size+uoverlap
	Case 4
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(i2)/Float(LevelDetail))/size-uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(LevelDetail))/size+voverlap
	Case 7
		ChunkTileu#=Float(((Texture Mod size))+0)/size+(Float(j2)/Float(LevelDetail))/size+voverlap
		ChunkTilev#=Float(((Texture)/size)+0)/size+(Float(i2)/Float(LevelDetail))/size+uoverlap
	Case 6
		ChunkTileu#=Float(((Texture Mod size)+0))/size+(Float(i2)/Float(LevelDetail))/size+uoverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(j2)/Float(LevelDetail))/size-voverlap
	Case 5
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(j2)/Float(LevelDetail))/size-voverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(i2)/Float(LevelDetail))/size-uoverlap

	Default
		ChunkTileu#=Float((Texture Mod size))/size+(Float(i2)/Float(LevelDetail))/size+uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(LevelDetail))/size+voverlap
	End Select

End Function


Function CreateObjectModel(Dest)

		If ObjectModelName$(Dest)="!Button"
			ObjectEntity(Dest)=CreateButtonMesh(ObjectSubType(Dest),ObjectData(Dest,0),ObjectData(Dest,1),ObjectData(Dest,2),ObjectData(Dest,3))
	
		Else If ObjectModelName$(Dest)="!CustomModel"
		
			If FileType("UserData\Custom\Models\"+ObjectTextData(Dest,0)+".3ds")<>1 Or FileType("UserData\Custom\Models\"+ObjectTextData(Dest,0)+".jpg")<>1
				ObjectTextData(Dest,0)="Default"
			EndIf
			ObjectEntity(Dest)=LoadMesh("UserData\Custom\Models\"+ObjectTextData(Dest,0)+".3ds")
			ObjectTexture(Dest)=LoadTexture("UserData\Custom\Models\"+ObjectTextData(Dest,0)+".jpg")
			EntityTexture ObjectEntity(Dest),ObjectTexture(Dest)

	
		Else If ObjectModelName$(Dest)="!Teleport"
			ObjectEntity(Dest)=CreateTeleporterMesh(ObjectData(Dest,0))
		Else If ObjectModelName$(Dest)="!Item"
			ObjectEntity(Dest)=CreatePickupItemMesh(ObjectData(Dest,2))
		Else If ObjectModelName$(Dest)="!Stinker" Or ObjectModelName$(Dest)="!NPC"
			ObjectEntity(Dest)=CopyEntity(StinkerMesh)
			
			
		
			If ObjectData(Dest,0)=5
				ObjectTexture(Dest)=Waterfalltexture(0) ;MyLoadTexture("Data\leveltextures\waterfall.jpg",1)
			Else If ObjectData(Dest,0)=6
				ObjectTexture(Dest)=Waterfalltexture(1) ;MyLoadTexture("Data\leveltextures\waterfalllava.jpg",1)
	
			Else
				ObjectTexture(Dest)=MyLoadTexture("data/models/stinker/body00"+Str$(ObjectData(Dest,0))+Chr$(65+ObjectData(Dest,1))+".jpg",1)
			EndIf
			EntityTexture GetChild(ObjectEntity(Dest),3),ObjectTexture(Dest)
			
			
			If ObjectData(Dest,2)>0	; hat
				If ObjectData(Dest,2)>9 ; two digit
					ObjectHatEntity(Dest)=MyLoadMesh("data/models/stinker/accessory0"+Str$(ObjectData(Dest,2))+".3ds",0)
					ObjectHatTexture(Dest)=MyLoadTexture("data/models/stinker/accessory0"+Str$(ObjectData(Dest,2))+Chr$(64+ObjectData(Dest,3))+".jpg",4)
				Else
					ObjectHatEntity(Dest)=MyLoadMesh("data/models/stinker/accessory00"+Str$(ObjectData(Dest,2)+".3ds"),0)
					ObjectHatTexture(Dest)=MyLoadTexture("data/models/stinker/accessory00"+Str$(ObjectData(Dest,2))+Chr$(64+ObjectData(Dest,3))+".jpg",4)
				EndIf
				EntityTexture ObjectHatEntity(Dest),ObjectHatTexture(Dest)
				ScaleEntity ObjectHatEntity(Dest),ObjectXScale(Dest)*ObjectScaleAdjust(Dest),ObjectZScale(Dest)*ObjectScaleAdjust(Dest),ObjectYScale(Dest)*ObjectScaleAdjust(Dest)
		
				RotateEntity ObjectHatEntity(Dest),0,0,0
				TurnEntity ObjectHatEntity(Dest),ObjectPitchAdjust(dest),0,ObjectRollAdjust(dest)
				TurnEntity ObjectHatEntity(Dest),0,ObjectYawAdjust(dest)-90,0

					PositionEntity ObjectHatEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest)+.1+.84*ObjectZScale(Dest)/.035,-ObjectY(Dest)-ObjectYAdjust(Dest)

			EndIf
			
			If ObjectData(Dest,4)>100	; acc
				
					ObjectAccEntity(Dest)=MyLoadMesh("data/models/stinker/accessory"+Str$(ObjectData(Dest,4))+".3ds",0)
					ObjectAccTexture(Dest)=MyLoadTexture("data/models/stinker/accessory"+Str$(ObjectData(Dest,4))+Chr$(65+ObjectData(Dest,5))+".jpg",4)
				EntityTexture ObjectAccEntity(Dest),ObjectAccTexture(Dest)
				
				ScaleEntity ObjectAccEntity(Dest),ObjectXScale(Dest)*ObjectScaleAdjust(Dest),ObjectZScale(Dest)*ObjectScaleAdjust(Dest),ObjectYScale(Dest)*ObjectScaleAdjust(Dest)
		
				RotateEntity ObjectAccEntity(Dest),0,0,0
				TurnEntity ObjectAccEntity(Dest),ObjectPitchAdjust(dest),0,ObjectRollAdjust(dest)
				TurnEntity ObjectAccEntity(Dest),0,ObjectYawAdjust(dest)-90,0

				
				
				PositionEntity ObjectAccEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest)+.1+.84*ObjectZScale(Dest)/.035,-ObjectY(Dest)-ObjectYAdjust(Dest)



			EndIf
			
			
			
			
		If CurrentHatModel>0
		
			
			EntityTexture CurrentHatModel,CurrentHatTexture
			ScaleEntity CurrentHatModel,CurrentObjectYScale*CurrentObjectScaleAdjust,CurrentObjectZScale*CurrentObjectScaleAdjust,CurrentObjectXScale*CurrentObjectScaleAdjust
			;RotateEntity CurrentObjectModel,CurrentObjectPitchAdjust,CurrentObjectYawAdjust,CurrentObjectRollAdjust
			RotateEntity CurrentHatModel,0,0,0
			TurnEntity CurrentHatModel,CurrentObjectPitchAdjust,0,CurrentObjectRollAdjust
			TurnEntity CurrentHatModel,0,CurrentObjectYawAdjust-90,0
			
			bone=FindChild(CurrentObjectModel,"hat_bone")
		
			PositionEntity CurrentHatModel,0+CurrentObjectXAdjust,300+CurrentObjectZAdjust+CurrentObjectZ+.1+.84*CurrentObjectZScale/.035,0-CurrentObjectYAdjust
	
	
		EndIf
		
		If CurrentAccModel>0
		
			
			EntityTexture CurrentAccModel,CurrentAccTexture
			ScaleEntity CurrentAccModel,CurrentObjectYScale*CurrentObjectScaleAdjust,CurrentObjectZScale*CurrentObjectScaleAdjust,CurrentObjectXScale*CurrentObjectScaleAdjust
			;RotateEntity CurrentObjectModel,CurrentObjectPitchAdjust,CurrentObjectYawAdjust,CurrentObjectRollAdjust
			RotateEntity CurrentAccModel,0,0,0
			TurnEntity CurrentAccModel,CurrentObjectPitchAdjust,0,CurrentObjectRollAdjust
			TurnEntity CurrentAccModel,0,CurrentObjectYawAdjust-90,0
			
			bone=FindChild(CurrentObjectModel,"hat_bone")
		
			PositionEntity CurrentAccModel,0+CurrentObjectXAdjust,300+CurrentObjectZAdjust+CurrentObjectZ+.1+.84*CurrentObjectZScale/.035,0-CurrentObjectYAdjust
	
	
		EndIf


	
		



		Else If ObjectModelName$(Dest)="!StinkerWee"
			ObjectEntity(Dest)=CopyEntity(StinkerWeeMesh)
		Else If ObjectModelName$(Dest)="!Cage"
			ObjectEntity(Dest)=CopyEntity(CageMesh)
		Else If ObjectModelName$(Dest)="!StarGate"
			ObjectEntity(Dest)=CopyEntity(StarGateMesh)

		Else If ObjectModelName$(Dest)="!Scritter"
			ObjectEntity(Dest)=CopyEntity(ScritterMesh)
			EntityTexture ObjectENtity(Dest),ScritterTexture(ObjectData(Dest,0))
			
		Else If ObjectModelName$(Dest)="!RainbowBubble"
			ObjectEntity(Dest)=CreateSphere()
			;ScaleMesh ObjectEntity(Dest),.4,.4,.4
			;PositionMesh ObjectEntity(Dest),0,1,0
			ScaleMesh ObjectEntity(Dest),.5,.5,.5
			EntityTexture ObjectEntity(Dest),Rainbowtexture
			
		Else If ObjectModelName$(Dest)="!IceBlock"
			ObjectEntity(Dest)=CreateIceBlockMesh(ObjectData(Dest,3))
			
		Else If ObjectModelName$(Dest)="!PlantFloat"
			ObjectEntity(Dest)=CreatePlantFloatMesh()
			;ObjectEntity(Dest)=CreateSphere()
			;ScaleMesh ObjectEntity(Dest),.4,.1,.4
;			PositionMesh ObjectEntity(Dest),0,1,0
			;EntityTexture ObjectEntity(Dest),Rainbowtexture
			
		Else If ObjectModelName$(Dest)="!IceFloat"
			ObjectEntity(Dest)=CreateIceFloatMesh()
			;ObjectEntity(Dest)=CreateSphere()
			;ScaleMesh ObjectEntity(Dest),.4,.1,.4
;			PositionMesh ObjectEntity(Dest),0,1,0
		



		Else If ObjectModelName$(Dest)="!Chomper"
			ObjectEntity(Dest)=CopyEntity(ChomperMesh)
			If ObjectSubtype(Dest)=1 
				EntityTexture ObjectEntity(Dest),WaterChomperTexture
			Else If ObjectData(Dest,1)=3
				EntityTexture ObjectEntity(Dest),MechaChomperTexture
			Else
				EntityTexture ObjectEntity(Dest),ChomperTexture
			EndIf
		Else If ObjectModelName$(Dest)="!Bowler"
			ObjectEntity(Dest)=CopyEntity(BowlerMesh)
		Else If ObjectModelName$(Dest)="!Tentacle"
			ObjectEntity(Dest)=CopyEntity(TentacleMesh)
			Animate GetChild(ObjectEntity(Dest),3),1,.1,1,0


		Else If ObjectModelName$(Dest)="!Turtle"
			ObjectEntity(Dest)=CopyEntity(TurtleMesh)
		Else If ObjectModelName$(Dest)="!Thwart"
			ObjectEntity(Dest)=CopyEntity(ThwartMesh)
			EntityTexture ObjectEntity(Dest),ThwartTexture(ObjectData(Dest,0))

		Else If ObjectModelName$(Dest)="!FireFlower"
			ObjectEntity(Dest)=CopyEntity(FireFlowerMesh)
			If ObjectData(Dest,1)=1 EntityTexture ObjectEntity(Dest),FireFlowerTexture2
		Else If ObjectModelName$(Dest)="!BurstFlower"
			ObjectEntity(Dest)=CopyEntity(BurstFlowerMesh)
			
		Else If ObjectModelName$(Dest)="!Busterfly"
			ObjectEntity(Dest)=CopyEntity(busterflyMesh)
			
		Else If ObjectModelName$(Dest)="!Rubberducky"
			ObjectEntity(Dest)=CopyEntity(rubberduckymesh)
			
		Else If ObjectModelName$(Dest)="!Crab"
			ObjectEntity(Dest)=CopyEntity(CrabMesh)
			If ObjectSubType(Dest)=0 Then EntityTexture ObjectEntity(Dest),CrabTexture2
		Else If ObjectModelName$(Dest)="!Troll"
			ObjectEntity(Dest)=CopyEntity(TrollMesh)
		Else If ObjectModelName$(Dest)="!Kaboom"
			ObjectEntity(Dest)=CopyEntity(KaboomMesh)
			

		Else If ObjectModelName$(Dest)="!BabyBoomer"
			ObjectEntity(Dest)=CopyEntity(KaboomMesh)
			
		Else If ObjectModelName$(Dest)="!Lurker"
			ObjectEntity(Dest)=CopyEntity(LurkerMesh)
		Else If ObjectModelName$(Dest)="!Ghost"
			ObjectEntity(Dest)=CopyEntity(GhostMesh)

		Else If ObjectModelName$(Dest)="!Wraith"
			ObjectEntity(Dest)=CopyEntity(WraithMesh)
			EntityTexture ObjectEntity(Dest),WraithTexture(ObjectData(Dest,2))





			
		Else If ObjectModelName$(Dest)="!GlowWorm"  Or ObjectModelName$(Dest)="!Zipper"
			ObjectEntity(Dest)=CreateSphere(12)
			ScaleMesh ObjectEntity(Dest),.1,.1,.1
			EntityColor ObjectEntity(Dest),ObjectData(Dest,5),ObjectData(Dest,6),ObjectData(Dest,7)
		Else If ObjectModelName$(Dest)="!Void"
			;ObjectEntity(Dest)=CreateSphere(12)
			;ScaleMesh ObjectEntity(Dest),.4,.15,.4
			ObjectEntity(Dest)=CreateVoidMesh()


		Else If ObjectModelName$(Dest)="!Spring"
			ObjectEntity(Dest)=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
			RotateMesh ObjectEntity(Dest),90,0,0
			EntityTexture ObjectEntity(Dest),Springtexture
		
		Else If ObjectModelName$(Dest)="!Suctube"
			ObjectEntity(Dest)=CreateSucTubeMesh(ObjectData(Dest,3),ObjectData(Dest,0),True)
			Redosuctubemesh(ObjectEntity(Dest), ObjectData(Dest,0), ObjectActive(Dest), ObjectData(Dest,2), ObjectYawAdjust(Dest))
		Else If ObjectModelName$(Dest)="!SuctubeX"
			ObjectEntity(Dest)=CreateSucTubeXMesh(ObjectData(Dest,3))




		Else If ObjectModelName$(Dest)="!FlipBridge"
			;ObjectEntity(Dest)=CreateCube()
			;ScaleMesh ObjectEntity(Dest),.35,.1,.5
			
			ObjectEntity(Dest)=CreateFlipBridgeMesh(ObjectData(Dest,0))
			;EntityTexture ObjectEntity(Dest),GateTexture
			



		;	RotateEntity ObjectEntity(Dest),0,90*ObjectData(Dest,0),0
		
		Else If ObjectModelName$(Dest)="!Obstacle10"
			ObjectEntity(Dest)=CopyEntity(  ObstacleMesh(10)  )
			EntityTexture ObjectEntity(Dest), MushroomTex(  (Abs(objectData(Dest,0))) Mod 3)
		
		Else If ObjectModelName$(Dest)="!Obstacle51" Or ObjectModelName$(Dest)="!Obstacle55" Or ObjectModelName$(Dest)="!Obstacle59"
			ObjectEntity(Dest)=CopyEntity(  ObstacleMesh((Asc(Mid$(ObjectModelName$(Dest),10,1))-48)*10+(Asc(Mid$(ObjectModelName$(Dest),11,1))-48)+ObjectData(Dest,0))  )
			EntityTexture ObjectEntity(Dest), ObstacleTexture((Asc(Mid$(ObjectModelName$(Dest),10,1))-48)*10+(Asc(Mid$(ObjectModelName$(Dest),11,1))-48)+ObjectData(Dest,1))



		Else If Left$(ObjectModelName$(Dest),9)="!Obstacle"
			ObjectEntity(Dest)=CopyEntity(ObstacleMesh((Asc(Mid$(ObjectModelName$(Dest),10,1))-48)*10+(Asc(Mid$(ObjectModelName$(Dest),11,1))-48)))


		Else If ObjectModelName$(Dest)="!WaterFall"
			ObjectEntity(Dest)=CreateWaterFallMesh(ObjectData(Dest,0))
		Else If ObjectModelName$(Dest)="!Star"
			ObjectEntity(Dest)=CopyMesh(StarMesh)
			EntityTexture ObjectEntity(Dest),GoldStarTexture
		Else If ObjectModelName$(Dest)="!Wisp"
			ObjectEntity(Dest)=CopyMesh(StarMesh)
			EntityTexture ObjectEntity(Dest),WispTexture(ObjectData(Dest,0))
			
	
			
		Else If ObjectModelName$(Dest)="!Portal Warp"
			ObjectEntity(Dest)=CopyEntity(PortalWarpMesh)
			If ObjectData(Dest,1)=0
				EntityTexture ObjectEntity(Dest),StarTexture
			Else
				EntityTexture ObjectEntity(Dest),RainbowTexture
			EndIf
			
		Else If ObjectModelName$(Dest)="!Sun Sphere1"
			ObjectEntity(Dest)=CreateSphere()
			;PositionMesh ObjectEntity(Dest),0,1.5,0
			;EntityAlpha ObjectEntity(Dest),.5
			EntityColor ObjectEntity(Dest),ObjectData(Dest,0),ObjectData(Dest,1),ObjectData(Dest,2)
			EntityBlend ObjectEntity(Dest),3
			
		Else If ObjectModelName$(Dest)="!Sun Sphere2"
			ObjectEntity(Dest)=CreateSphere()
			ScaleMesh ObjectEntity(Dest),.5,.5,.5




		Else If ObjectModelName$(Dest)="!Coin"
			ObjectEntity(Dest)=CopyMesh(CoinMesh)
			EntityTexture ObjectEntity(Dest),GoldCoinTexture
			If ObjectType(Dest)=425 Then EntityTexture ObjectEntity(Dest),RetroRainbowCoinTexture
		Else If ObjectModelName$(Dest)="!Token"
			ObjectEntity(Dest)=CopyMesh(CoinMesh)
			EntityTexture ObjectEntity(Dest),TokenCoinTexture

		Else If ObjectModelName$(Dest)= "!Gem"
			ObjectEntity(Dest)=CopyEntity(GemMesh(ObjectData(Dest,0))) 
			EntityTexture ObjectEntity(Dest),GoldStarTexture
			EntityTexture ObjectEntity(Dest),TeleporterTexture(ObjectData(Dest,1))
		
		Else If ObjectModelName$(dest)="!Crystal"
			ObjectEntity(Dest)=CopyEntity(GemMesh(2))
			If objectdata(dest,0)=0
				EntityTexture ObjectEntity(Dest),rainbowtexture
			Else
				EntityTexture ObjectEntity(Dest),ghosttexture
			EndIf


		Else If ObjectModelName$(Dest)="!CustomItem"
			ObjectEntity(Dest)=CreateCustomItemMesh(ObjectData(Dest,0))
		Else If ObjectModelName$(Dest)="!Sign"
			ObjectEntity(Dest)=CopyEntity(SignMesh(ObjectData(Dest,0)))
			EntityTexture ObjectEntity(Dest),SignTexture(ObjectData(Dest,1))
		Else If ObjectModelName$(Dest)="!Barrel1"
			ObjectEntity(Dest)=CopyEntity(BarrelMesh)
			EntityTexture ObjectEntity(Dest),BarrelTexture1
		Else If ObjectModelName$(Dest)="!Barrel2"
			ObjectEntity(Dest)=CopyEntity(BarrelMesh)
			EntityTexture ObjectEntity(Dest),BarrelTexture2
		Else If ObjectModelName$(Dest)="!Barrel3"
			ObjectEntity(Dest)=CopyEntity(BarrelMesh)
			EntityTexture ObjectEntity(Dest),BarrelTexture3
		Else If ObjectModelName$(Dest)="!Cuboid"
			ObjectEntity(Dest)=CreateCube()
			ScaleMesh ObjectEntity(Dest),0.4,0.4,0.4
			PositionMesh ObjectEntity(Dest),0,0.5,0
			If ObjectData(Dest,0)<0 Or ObjectData(Dest,0)>8 Then ObjectData(Dest,0)=0
			EntityTexture ObjectEntity(Dest),TeleporterTexture(ObjectData(Dest,0))
	
		Else If ObjectModelName$(Dest)="!Prism"
			ObjectEntity(Dest)=CopyEntity(PrismMesh)
			EntityTexture ObjectEntity(Dest),PrismTexture
				
		Else If ObjectModelName$(Dest)="!Square"
			ObjectEntity(Dest)=MyLoadmesh("data\models\squares\square1.b3d",0)
			
		Else If ObjectModelName$(Dest)="!FloingOrb" ; not to be confused with !FloingBubble
			ObjectEntity(Dest)=CreateSphere()
			ScaleMesh ObjectEntity(Dest),.3,.3,.3
			EntityColor ObjectEntity(Dest),255,0,0
		
		Else If ObjectModelName$(Dest)="!MagicMirror"
			ObjectEntity(Dest)=CreateCube()
			ScaleMesh ObjectEntity(Dest),3.49,2.49,.52
			EntityColor ObjectEntity(Dest),255,0,0
			EntityAlpha ObjectEntity(Dest),.5






		Else If ObjectModelName$(Dest)="!ColourGate"
			ObjectEntity(Dest)=CreateColourGateMesh(ObjectData(Dest,2),ObjectData(Dest,0))
			
		Else If ObjectModelName$(Dest)="!Autodoor"
			ObjectEntity(Dest)=CopyEntity(Autodoormesh)
		Else If ObjectModelName$(Dest)="!Transporter"
			ObjectEntity(Dest)=CreateTransporterMesh(ObjectData(Dest,0),3)
			RotateMesh ObjectEntity(Dest),0,90*ObjectData(Dest,2),0
			
		Else If ObjectModelName$(Dest)="!Conveyor"
			If ObjectData(Dest,4)=4
				ObjectEntity(Dest)=CreateCloudMesh(ObjectData(Dest,0))
			Else
				ObjectEntity(Dest)=CreateTransporterMesh(ObjectData(Dest,0),ObjectData(Dest,4))
			EndIf
			RotateMesh ObjectEntity(Dest),0,-90*ObjectData(Dest,2),0
			If objecttype(Dest)=46 Then ScaleMesh ObjectEntity(Dest),.5,.5,.5

		Else If ObjectModelName$(Dest)="!Key"
			ObjectEntity(Dest)=CreateKeyMesh(ObjectData(Dest,0))
		Else If ObjectModelName$(Dest)="!KeyCard"
			ObjectEntity(Dest)=CreateKeyCardMesh(ObjectData(Dest,0))


		Else If ObjectModelName$(Dest)="!SteppingStone"
			ObjectENtity(Dest)=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
			EntityTexture ObjectEntity(Dest),SteppingStoneTexture(ObjectData(Dest,0))
		Else If ObjectModelName$(Dest)="!Door"
			ObjectENtity(Dest)=MyLoadmesh("data\models\houses\door01.3ds",0)
			
		Else If ObjectModelName$(Dest)="!SpellBall"
			ObjectEntity(Dest)=CreateSpellBallMesh(7) ; use white magic spellball mesh
			
		Else If ObjectModelName$(Dest)="!Fence1"
			ObjectENtity(Dest)=CopyEntity(fence1)
		Else If ObjectModelName$(Dest)="!Fence2"
			ObjectENtity(Dest)=CopyEntity(fence2)
		Else If ObjectModelName$(Dest)="!Fencepost"
			ObjectENtity(Dest)=CopyEntity(fencepost)
		Else If ObjectModelName$(Dest)="!Fountain"
			ObjectENtity(Dest)=MyLoadmesh("data\models\houses\fountain01.b3d",0)
			EntityTexture ObjectEntity(Dest),FountainTexture
		
		Else If ObjectModelName$(Dest)="!Retrobox"
			ObjectEntity(Dest)=CopyEntity(RetroBoxMesh)
			
		
		Else If ObjectModelName$(Dest)="!Retrocoily"
			ObjectEntity(Dest)=CopyEntity(RetroCoilyMesh)
			

			
		Else If ObjectModelName$(Dest)="!Retroscouge"
			ObjectEntity(Dest)=CopyEntity(RetroScougeMesh)
			ObjectYawAdjust(Dest)=(-90*ObjectData(Dest,0) +3600) Mod 360
		

		
		Else If ObjectModelName$(Dest)="!Retrozbot"
			ObjectEntity(Dest)=CopyEntity(RetroZbotMesh)
			ObjectYawAdjust(Dest)=(-90*ObjectData(Dest,0) +3600) Mod 360
		

		
		Else If ObjectModelName$(Dest)="!Retroufo"
			ObjectEntity(Dest)=CopyEntity(RetroUFOMesh)
			ObjectYawAdjust(Dest)=(-90*ObjectData(Dest,0) +3600) Mod 360


		Else If ObjectModelName$(Dest)="!Retrolasergate"
			ObjectEntity(Dest)=Createretrolasergatemesh(ObjectData(Dest,0))
			
			
		Else If ObjectModelName$(Dest)="!Weebot"
			ObjectEntity(Dest)=CopyEntity(WeebotMesh)
			ObjectYawAdjust(Dest)=(-90*ObjectData(Dest,0) +3600) Mod 360

		Else If ObjectModelName$(Dest)="!Zapbot"
			ObjectEntity(Dest)=CopyEntity(ZapbotMesh)
			ObjectYawAdjust(Dest)=(-90*ObjectData(Dest,0) +3600) Mod 360

		Else If ObjectModelName$(Dest)="!Pushbot"
			ObjectEntity(Dest)=CreatePushbotMesh(ObjectData(Dest,0),ObjectData(Dest,3))
			ObjectYawAdjust(Dest)=-90*ObjectData(Dest,2)
			
		Else If ObjectModelName$(Dest)="!ZbotNPC"
			ObjectEntity(Dest)=CopyEntity(ZbotNPCMesh)
			EntityTexture ObjectEntity(Dest),ZbotNPCTexture(ObjectData(Dest,2))

		Else If ObjectModelName$(Dest)="!Mothership"
			ObjectEntity(Dest)=CopyEntity(MothershipMesh)
			

		Else If ObjectModelName$(Dest)="!SkyMachineMap"
			ObjectEntity(Dest)=CreateCube()
			ScaleMesh ObjectEntity(Dest),2.5,.01,2.5
			PositionMesh ObjectEntity(Dest),0,0,-1
			EntityTexture ObjectEntity(Dest),SkyMachineMapTexture
			EntityBlend ObjectEntity(Dest),3
			
			
		Else If ObjectModelName$(Dest)="!GrowFlower"
			ObjectEntity(Dest)=CreateGrowFlowerMesh(ObjectData(Dest,0))
	
		Else If ObjectModelName$(Dest)="!FloingBubble"
			ObjectEntity(Dest)=CreateFloingBubbleMesh()
			
			
 		Else If ObjectModelName$(Dest)="!None"
			ObjectEntity(Dest)=CreateSphere()
			ScaleMesh ObjectEntity(Dest),.2,.2,.2
			;EntityAlpha ObjectEntity(Dest),.5
			
		Else ; Unknown model
			ObjectEntity(Dest)=CreateSphere()
			ScaleMesh ObjectEntity(Dest),.3,.3,.3
			EntityColor ObjectEntity(Dest),255,0,255
		
		EndIf
		
		
		
		If ObjectModelName$(Dest)="!FlipBridge"
			TextureTarget=GetChild(ObjectEntity(Dest),1)
		Else
			TextureTarget=ObjectEntity(Dest)
		EndIf
		
		
		If ObjectTextureName$(Dest)="!None"
			ObjectTexture(Dest)=0
			
			
		Else If ObjectTextureName$(Dest)="!Door"
			If ObjectData(Dest,5)<0 Then ObjectData(Dest,5)=0
			If ObjectData(Dest,5)>2 Then ObjectData(Dest,5)=2
			If DoorTexture(ObjectData(Dest,5))=0 Then ObjectData(Dest,5)=0
			EntityTexture TextureTarget,DoorTexture(ObjectData(Dest,5))
		Else If ObjectTextureName$(Dest)="!Cottage"
			If CottageTexture(ObjectData(Dest,5))=0 Then ObjectData(Dest,5)=0
			EntityTexture TextureTarget,CottageTexture(ObjectData(Dest,5))	
		Else If ObjectTextureName$(Dest)="!Townhouse"
			
			If HouseTexture(ObjectData(Dest,5))=0 Then ObjectData(Dest,5)=0
			EntityTexture TextureTarget,HouseTexture(ObjectData(Dest,5))	
		Else If ObjectTextureName$(Dest)="!Windmill"
			
			If WindmillTexture(ObjectData(Dest,5))=0 Then ObjectData(Dest,5)=0
			EntityTexture TextureTarget,WindmillTexture(ObjectData(Dest,5))	
		Else If ObjectTextureName$(Dest)="!Fence"
			
			If FenceTexture(ObjectData(Dest,5))=0 Then ObjectData(Dest,5)=0
			EntityTexture TextureTarget,FenceTexture(ObjectData(Dest,5))	
		Else If ObjectTextureName$(Dest)="!FireTrap"
			EntityTexture TextureTarget,FireTrapTexture
			
		
		Else If Left(ObjectTextureName$(Dest),1)="?"
			; custom texture for existing objects
		
			If Lower(Right(ObjectTextureName$(Dest),4))=".jpg" Or Lower(Right(ObjectTextureName$(Dest),4))=".bmp" Or Lower(Right(ObjectTextureName$(Dest),4))=".png"
				tname$="UserData\Custom\Objecttextures\"+Right(ObjectTextureName$(Dest),Len(ObjectTextureName$(Dest))-1)
			Else
				tname$="UserData\Custom\Objecttextures\"+Right(ObjectTextureName$(Dest),Len(ObjectTextureName$(Dest))-1)+".jpg"
			EndIf
	
			If FileType(tname$)<>1 
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color 255,255,255
				Print "Couldn't load texture: " + tname$
				Print "Reverting to default..."
				Delay 2000
				tname$="UserData\Custom\Objecttextures\default.jpg"		
				ObjectTextureName$(Dest)="?Default"
			EndIf
			
			If Lower(Right(tname$,4))=".png"
				; if png load with alpha map
				ObjectTexture(Dest)=LoadTexture(tname$,6)
			Else
				ObjectTexture(Dest)=LoadTexture(tname$,4)
			EndIf
			
			EntityTexture TextureTarget,ObjectTexture(Dest)
		
		Else If ObjectTextureName$(Dest)<>"" And ObjectTextureName$(Dest)<>""<>"!None" And Left$(ObjectTextureName$(Dest),1)<>"!"  And Objectmodelname$(Dest)<>"!Button"
			; this entire block has been annoyingly problematic

			If myFileType(ObjectTextureName$(Dest))=1 Or FileType(ObjectTextureName$(Dest))=1
				ObjectTexture(Dest)=myLoadTexture(ObjectTextureName$(Dest),4)
				EntityTexture TextureTarget,ObjectTexture(Dest)
				For j=1 To CountChildren (ObjectEntity(Dest))
					child=GetChild(TextureTarget,j)
					EntityTexture child,ObjectTexture(Dest)
				Next
			Else
				If ShowingError=False
					ShowingError=True
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					Print "Texture doesn't exist: " + ObjectTextureName$(Dest)
					Print "Reverting..."
					Delay 2000
				EndIf
				ObjectTexture(Dest)=myLoadTexture("UserData\Custom\Objecttextures\default.jpg",4)
				EntityTexture TextureTarget,ObjectTexture(Dest)
				For j=1 To CountChildren (TextureTarget)
					child=GetChild(TextureTarget,j)
					EntityTexture child,ObjectTexture(Dest)
				Next
			EndIf


		Else If Left$(ObjectTextureName$(Dest),2)="!T"
			;ObjectTexture(Dest)=LoadTexture("data2/models/stinker/body001a.jpg")
			EntityTexture TextureTarget,StinkerTexture
			For k=1 To CountChildren(TextureTarget)
				child=GetChild(TextureTarget,k)
				EntityTexture child,StinkerTexture
			Next

		Else
			
			If ObjectType(Dest)=200 ; magic glove
				EntityTexture TextureTarget,GloveTex

				EntityFX TextureTarget,2
				red=0
				green=0
				blue=0
				For ii=0 To 3
					Select ObjectData(Dest,0)
					Case 0
						red=255
					Case 1
						red=255
						green=100
					Case 2
						red=255
						green=255
					Case 3
						green=255
					Case 4
						green=255
						blue=255
					Case 5
						blue=255
					Case 6
						red=255
						blue=255
					Case 7
						red=255
						blue=255
						green=255
					Case 8
						red=ii*64
						green=255-ii*32
						blue=255-ii*64
					Case 9
						red=67
						blue=67
						green=67
					End Select
				
					VertexColor GetSurface(TextureTarget,1),ii,red,green,blue
				Next
			EndIf

		EndIf
		
		
		
		
		If ObjectModelName$(Dest)="!StinkerWee"
			; special case for MD2s
			ScaleEntity ObjectEntity(Dest),ObjectXScale(Dest),ObjectZScale(Dest),ObjectYScale(Dest)
			RotateEntity ObjectEntity(Dest),0,0,0
	
					TurnEntity ObjectEntity(Dest),ObjectPitchAdjust(Dest),0,ObjectRollAdjust(Dest)
					TurnEntity ObjectEntity(Dest),0,ObjectYawAdjust(Dest),0

		EndIf
		
		If ObjectModelName$(Dest)<>"!None"
		



			ScaleEntity ObjectEntity(Dest),ObjectXScale(Dest)*ObjectScaleAdjust(Dest),ObjectZScale(Dest)*ObjectScaleAdjust(Dest),ObjectYScale(Dest)*ObjectScaleAdjust(Dest)
			;PositionEntity ObjectEntity(Dest),ObjectXAdjust(Dest),ObjectZAdjust(Dest),-ObjectYAdjust(Dest)
			RotateEntity ObjectEntity(Dest),0,0,0
	
			TurnEntity ObjectEntity(Dest),ObjectPitchAdjust(Dest),0,ObjectRollAdjust(Dest)
			TurnEntity ObjectEntity(Dest),0,ObjectYawAdjust(Dest),0
			
			If ObjectModelName$(Dest)="!Kaboom" TurnEntity ObjectEntity(Dest),0,90,0


		EndIf
		
		
		UpdateObjectVisibility(Dest)

		PositionEntity ObjectEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest),-ObjectY(Dest)-ObjectYAdjust(Dest)


End Function


Function UpdateObjectModel(Dest)

	;ShowMessage("Freeing object model "+Dest+": "+ObjectModelName$(Dest),10)

	FreeModel(Dest)
	
	;ShowMessage("Creating object model "+Dest+": "+ObjectModelName$(Dest),10)
	
	CreateObjectModel(Dest)

End Function


Function CameraControls()
	MouseDeltaX = MouseXSpeed()
	MouseDeltaY = MouseYSpeed()
	mx = MouseX()
	my = MouseY()
	
	Adj#=0.1
	If ShiftDown() Then Adj=0.4
	
	If KeyDown(57) ; space bar
		CameraPanning=True
		If LeftMouse=True
			SpeedFactor#=0.25*Adj
			TranslateEntity Camera1,-MouseDeltaX * SpeedFactor,0,MouseDeltaY * SpeedFactor
		EndIf
	Else
		CameraPanning=False
	EndIf
	
	Target=-1
	
	If EditorMode=3 And mx>=695 And my>=305 And mx<=795 And my<=430 ; camera4 viewport space
		Target=Camera4 ; object camera
	;ElseIf EditorMode=0 And mx>=510 And mx<710 And my>=20 And my<240
	;	Target=-1 ; tile camera
	Else
		Target=Camera1 ; level camera
	EndIf
	
	If Target=-1
		Return
	EndIf

	If KeyDown(75) Or KeyDown(203) Or KeyDown(30) ; numpad 4 or left arrow or A
			
		TranslateEntity Target,-Adj,0,0
	EndIf
	If KeyDown(77) Or KeyDown(205) Or KeyDown(32) ; numpad 6 or right arrow or D
		
		TranslateEntity Target,Adj,0,0
	EndIf
	If KeyDown(72) Or KeyDown(200) Or KeyDown(17) ; numpad 8 or up arrow or W
	
		TranslateEntity Target,0,0,Adj
	EndIf
	If KeyDown(80) Or KeyDown(208) Or KeyDown(31) ; numpad 2 or down arrow or S
	
		TranslateEntity Target,0,0,-Adj
	EndIf
	If KeyDown(73) Or KeyDown(18) ; numpad 9 or E
	
		TranslateEntity Target,0,Adj,0
	EndIf
	If KeyDown(81) Or KeyDown(46) ; numpad 3 or C
	
		TranslateEntity Target,0,-Adj,0
	EndIf
	If KeyDown(71) Or KeyDown(16) ; numpad 7 or Q
		
		TurnEntity Target,1,0,0
	EndIf
	If KeyDown(79) Or KeyDown(44) ; numpad 1 or Z
	
		TurnEntity Target,-1,0,0
	EndIf
	If KeyDown(181) ;Or KeyDown(3) ; numpad /
		
		TurnEntity Target,0,1,0
	EndIf
	If KeyDown(55) ;Or KeyDown(4) ; numpad *
		
		TurnEntity Target,0,-1,0
	EndIf
	
	If KeyDown(76) Or KeyDown(45) ; numpad 5 or X
		; reset camera rotation
		If Target=Camera1
			RotateEntity Camera1,65,0,0
		ElseIf Target=Camera4
			RotateEntity Camera4,25,0,0
			PositionEntity Camera4,0,303.8,-8
			Camera4Zoom#=8
			CameraZoom Camera4,Camera4Zoom#
		EndIf
	EndIf
	
	If MouseScroll<>0
		If Target=Camera1 And mx<510 And my>=0 And my<500 ; mouse position check here because we don't want to move the camera when using scroll wheel on object adjusters
			SpeedFactor#=3.0*Adj
			TranslateEntity Camera1,0,-MouseScroll * SpeedFactor,0
		ElseIf Target=Camera4
			ZoomSpeed#=12.0*Adj
			If MouseScroll>0
				Camera4Zoom#=Camera4Zoom#*ZoomSpeed
			ElseIf MouseScroll<0
				Camera4Zoom#=Camera4Zoom#/ZoomSpeed
			EndIf
			If Camera4Zoom#<0.1
				Camera4Zoom#=0.1
			EndIf
			CameraZoom Camera4,Camera4Zoom#
		EndIf
	EndIf

		
	
End Function


Function SaveLevel()


	
		
	If AdventureCurrentArchive=1
		ex2$="Archive\"
	Else
		ex2$="Current\"
	EndIf


	

	file=WriteFile (globaldirname$+"\custom\editing\"+ex2$+AdventureFileName$+"\"+currentlevelnumber+".wlv")
	
	If (currentlevelnumber>94 And currentlevelnumber<99) Or Left$(Upper$(adventurefilename$),5)="ZACHY"
		; WA3 VAULTS
		WriteInt file,LevelWidth+121
	Else
		WriteInt file,LevelWidth
	EndIf
	WriteInt file,LevelHeight
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			WriteInt file,LevelTileTexture(i,j) ; corresponding to squares in LevelTexture
			WriteInt file,LevelTileRotation(i,j) ; 0-3 , and 4-7 for "flipped"
			WriteInt file,LevelTileSideTexture(i,j) ; texture for extrusion walls
			WriteInt file,LevelTileSideRotation(i,j) ; 0-3 , and 4-7 for "flipped"
			WriteFloat file,LevelTileRandom#(i,j) ; random height pertubation of tile
			WriteFloat file,LevelTileHeight#(i,j) ; height of "center" - e.g. to make ditches and hills
			WriteFloat file,LevelTileExtrusion#(i,j); extrusion with walls around it 
			WriteInt file,LevelTileRounding(i,j); 0-no, 1-yes: are floors rounded if on a drop-off corner
			WriteInt file,LevelTileEdgeRandom(i,j); 0-no, 1-yes: are edges rippled
			
			
			WriteInt file,LevelTileLogic(i,j)
			

			
		Next
	Next
	
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			WriteInt file,WaterTileTexture(i,j)
			WriteInt file,WaterTileRotation(i,j)
			WriteFloat file,WaterTileHeight(i,j)
			WriteFloat file,WaterTileTurbulence(i,j)
		Next
	Next
	
	; Globals
	WriteInt file,WaterFlow
	WriteInt file,WaterTransparent
	WriteInt file,WaterGlow
	
	If CurrentLevelTexture=-1
		WriteString file, LevelTextureCustomName$
	Else
		WriteString file,LevelTextureName$(CurrentLevelTexture)
	EndIf
	If CurrentWaterTexture=-1
		WriteString file, WaterTextureCustomName$
	Else
		WriteString file,WaterTextureName$(CurrentWaterTexture)
	EndIf
	
	

	
	
	
	
	; Objects
	
	PlayerIndex=ObjectIndexEditorToGameInner(NofObjects)
	
	WriteInt file,NofObjects
	For i=0 To NofObjects-1
		Dest=i
		
		WriteString file,ObjectModelName$(i)
		WriteString file,ObjectTextureName$(i)
		WriteFloat file,ObjectXScale(i)
		WriteFloat file,ObjectYScale(i)
		WriteFloat file,ObjectZScale(i)
		WriteFloat file,ObjectXAdjust(i)
		WriteFloat file,ObjectYAdjust(i)
		WriteFloat file,ObjectZAdjust(i)
		WriteFloat file,ObjectPitchAdjust(i)
		WriteFloat file,ObjectYawAdjust(i)
		WriteFloat file,ObjectRollAdjust(i)
	
		WriteFloat file,ObjectX(Dest)
		WriteFloat file,ObjectY(Dest)
		WriteFloat file,ObjectZ(Dest)
		WriteFloat file,ObjectOldX(Dest)
		WriteFloat file,ObjectOldY(Dest)
		WriteFloat file,ObjectOldZ(Dest)

		WriteFloat file,ObjectDX(Dest)
		WriteFloat file,ObjectDY(Dest)
		WriteFloat file,ObjectDZ(Dest)
	
		WriteFloat file,ObjectPitch(Dest)
		WriteFloat file,ObjectYaw(Dest)
		WriteFloat file,ObjectRoll(Dest)
		WriteFloat file,ObjectPitch2(Dest)
		WriteFloat file,ObjectYaw2(Dest)
		WriteFloat file,ObjectRoll2(Dest)

		WriteFloat file,ObjectXGoal(Dest)
		WriteFloat file,ObjectYGoal(Dest)
		WriteFloat file,ObjectZGoal(Dest)
	
		WriteInt file,ObjectMovementType(Dest)
		WriteInt file,ObjectMovementTypeData(Dest)
		WriteFloat file,ObjectSpeed(Dest)
		WriteFloat file,ObjectRadius(Dest)
		WriteInt file,ObjectRadiusType(Dest)
	
		WriteInt file,ObjectData10(Dest)
	
		WriteFloat file,ObjectPushDX(Dest)
		WriteFloat file,ObjectPushDY(Dest)
	
		WriteInt file,ObjectAttackPower(Dest)
		WriteInt file,ObjectDefensePower(Dest)
		WriteInt file,ObjectDestructionType(Dest)
	
		WriteInt file,ObjectID(Dest)
		WriteInt file,ObjectType(Dest)
		WriteInt file,ObjectSubType(Dest)
	
		WriteInt file,ObjectActive(Dest)
		WriteInt file,ObjectLastActive(Dest)
		WriteInt file,ObjectActivationType(Dest)
		WriteInt file,ObjectActivationSpeed(Dest)
	
		WriteInt file,ObjectStatus(Dest)
		WriteInt file,ObjectTimer(Dest)
		WriteInt file,ObjectTimerMax1(Dest)
		WriteInt file,ObjectTimerMax2(Dest)
	
		WriteInt file,ObjectTeleportable(Dest)
		WriteInt file,ObjectButtonPush(Dest)
		WriteInt file,ObjectWaterReact(Dest)
	
		WriteInt file,ObjectTelekinesisable(Dest)
		WriteInt file,ObjectFreezable(Dest)
	
		WriteInt file,ObjectReactive(Dest)

		;WriteInt file,ObjectChild(Dest)
		WriteInt file,ObjectIndexEditorToGame(ObjectChild(Dest), PlayerIndex)
		;WriteInt file,ObjectParent(Dest)
		WriteInt file,ObjectIndexEditorToGame(ObjectParent(Dest), PlayerIndex)
	
		For k=0 To 9
			WriteInt file,ObjectData(Dest,k)
		Next
		For k=0 To 3
			WriteString file,ObjectTextData(Dest,k)
		Next
		
		WriteInt file,ObjectTalkable(Dest)
		WriteInt file,ObjectCurrentAnim(Dest)
		WriteInt file,ObjectStandardAnim(Dest)
		WriteInt file,ObjectTileX(Dest)
		WriteInt file,ObjectTileY(Dest)
		WriteInt file,ObjectTileX2(Dest)
		WriteInt file,ObjectTileY2(Dest)
		WriteInt file,ObjectMovementTimer(Dest)
		WriteInt file,ObjectMovementSpeed(Dest)
		WriteInt file,ObjectMoveXGoal(Dest)
		WriteInt file,ObjectMoveYGoal(Dest)
		WriteInt file,ObjectTileTypeCollision(Dest)
		WriteInt file,ObjectObjectTypeCollision(Dest)
		WriteInt file,ObjectCaged(Dest)
		WriteInt file,ObjectDead(Dest)
		WriteInt file,ObjectDeadTimer(Dest)
		WriteInt file,ObjectExclamation(Dest)
		WriteInt file,ObjectShadow(Dest)
		;WriteInt file,-1;ObjectLinked(Dest)
		WriteInt file,ObjectIndexEditorToGame(ObjectLinked(Dest), PlayerIndex)
		;WriteInt file,-1;ObjectLinkBack(Dest)
		WriteInt file,ObjectIndexEditorToGame(ObjectLinkBack(Dest), PlayerIndex)
		WriteInt file,ObjectFlying(Dest)
		WriteInt file,ObjectFrozen(Dest)
		WriteInt file,ObjectIndigo(Dest)
		WriteInt file,ObjectFutureInt24(Dest)
		WriteInt file,ObjectFutureInt25(Dest)
		WriteFloat file,ObjectScaleAdjust(Dest)
		WriteFloat file,ObjectScaleXAdjust(Dest)
		WriteFloat file,ObjectScaleYAdjust(Dest)
		WriteFloat file,ObjectScaleZAdjust(Dest)
		WriteFloat file,ObjectFutureFloat5(Dest)
		WriteFloat file,ObjectFutureFloat6(Dest)
		WriteFloat file,ObjectFutureFloat7(Dest)
		WriteFloat file,ObjectFutureFloat8(Dest)
		WriteFloat file,ObjectFutureFloat9(Dest)
		WriteFloat file,ObjectFutureFloat10(Dest)
		WriteString file,ObjectFutureString1$(Dest)
		WriteString file,ObjectFutureString2$(Dest)
		
		For k=0 To 30
			;WriteString file,ObjectAdjusterString$(Dest,k)
			WriteString file,""
		Next
		


	Next

	WriteInt file,LevelEdgeStyle
	
	WriteInt file,LightRed
	WriteInt file,LightGreen
	WriteInt file,LightBlue
	
	WriteInt file,AmbientRed
	WriteInt file,AmbientGreen
	WriteInt file,AmbientBlue


	
	WriteInt file,LevelMusic
	WriteInt file,LevelWeather
	
	WriteString file,adventuretitle$
	
	WriteInt file,-2
	WriteInt file,WidescreenRangeLevel
	
	CloseFile file

	

End Function



Function LoadLevel(levelnumber)

	CurrentLevelNumber=levelnumber

	resetlevel()
	
		
		
	If AdventureCurrentArchive=1
		ex2$="Archive\"
	Else
		ex2$="Current\"
	EndIf

	
	

	
	; clear current objects first
	;ShowMessage("Freeing " + NofObjects + " objects...", 1000)
	For i=0 To NofObjects-1
		;DeleteObject(i)
		FreeObject(i)
	Next

	file=ReadFile (globaldirname$+"\custom\editing\"+ex2$+AdventureFileName$+"\"+levelnumber+".wlv")
	LevelWidth=ReadInt(File)
	If levelwidth>121
		; WA3 VAULTS
		LevelWidth=LevelWidth-121
	EndIf

	LevelHeight=ReadInt(File)
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			LevelTileTexture(i,j)=ReadInt(file) ; corresponding to squares in LevelTexture
			LevelTileRotation(i,j)=ReadInt(file) ; 0-3 , and 4-7 for "flipped"
			LevelTileSideTexture(i,j)=ReadInt(file) ; texture for extrusion walls
			LevelTileSideRotation(i,j)=ReadInt(file) ; 0-3 , and 4-7 for "flipped"
			LevelTileRandom#(i,j)=ReadFloat(file) ; random height pertubation of tile
			LevelTileHeight#(i,j)=ReadFloat(file) ; height of "center" - e.g. to make ditches and hills
			LevelTileExtrusion#(i,j)=ReadFloat(file); extrusion with walls around it 
			LevelTileRounding(i,j)=ReadInt(file); 0-no, 1-yes: are floors rounded if on a drop-off corner
			LevelTileEdgeRandom(i,j)=ReadInt(file); 0-no, 1-yes: are edges rippled
			LevelTileLogic(i,j)=ReadInt(file)
			
			LevelTileObjectCount(i,j)=0
			
		Next
	Next
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			WaterTileTexture(i,j)=ReadInt(file)
			WaterTileRotation(i,j)=ReadInt(file)
			WaterTileHeight(i,j)=ReadFloat(file)
			WaterTileTurbulence(i,j)=ReadFloat(file)
		Next
	Next
	WaterFlow=ReadInt(file)
	WaterTransparent=ReadInt(File)
	WaterGlow=ReadInt(File)
	
	currentleveltexture=-1
	currentwatertexture=-1
	a$=ReadString$(file)
	For i=0 To nofleveltextures-1
		If a$=levelTextureName$(i) Then CurrentLevelTexture=i
	Next
	If currentleveltexture=-1
		LevelTextureCustomName$=a$
	EndIf
	
	a$=ReadString$(file)
	For i=0 To nofwatertextures-1
		If a$=waterTextureName$(i) Then CurrentwaterTexture=i
	Next
	If currentwatertexture=-1
		WaterTextureCustomName$=a$
	EndIf


	FreeTexture leveltexture
	FreeTexture watertexture
	leveltexture=0
	If currentleveltexture=-1
		LevelTexture=LoadTexture(globaldirname$+"\custom\leveltextures\leveltex "+LevelTextureCustomName$+".bmp",1)
		If leveltexture=0
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color 255,255,0
			Print "CUSTOM TEXTURE NOT FOUND... REVERTING."
			Delay 2000
			currentleveltexture=1
			LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
		EndIf
	Else
		LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
		
	EndIf
	
	watertexture=0
	If currentwatertexture=-1
		WaterTexture=LoadTexture(globaldirname$+"\custom\leveltextures\watertex "+WaterTextureCustomName$+".jpg",2)
		If Watertexture=0
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color 255,255,0
			Print "CUSTOM WATERTEXTURE NOT FOUND... REVERTING."
			Delay 2000
			currentwatertexture=0
			WaterTexture=myLoadTexture("data\Leveltextures\"+WaterTexturename$(CurrentWaterTexture),2)
		EndIf
	Else
		waterTexture=myLoadTexture("data\Leveltextures\"+waterTexturename$(CurrentWaterTexture),2)
	EndIf
	
	EntityTexture TexturePlane,LevelTexture
	EntityTexture CurrentWaterTile,WaterTexture
	
	leftmousereleased=False
		



	NofObjects=0
	ReadObjectCount=ReadInt(file)
	For i=0 To ReadObjectCount-1
		Dest=i
		ObjectModelName$(i)=ReadString$(file)
		ObjectTextureName$(i)=ReadString$(file)
		ObjectXScale(i)=ReadFloat(file)
		ObjectYScale(i)=ReadFloat(file)
		ObjectZScale(i)=ReadFloat(file)
		ObjectXAdjust(i)=ReadFloat(file)
		ObjectYAdjust(i)=ReadFloat(file)
		ObjectZAdjust(i)=ReadFloat(file)
		ObjectPitchAdjust(i)=ReadFloat(file)
		ObjectYawAdjust(i)=ReadFloat(file)
		ObjectRollAdjust(i)=ReadFloat(file)
	
		ObjectX(Dest)=ReadFloat(file)
		ObjectY(Dest)=ReadFloat(file)
		ObjectZ(Dest)=ReadFloat(file)
		ObjectOldX(Dest)=ReadFloat(file)
		ObjectOldY(Dest)=ReadFloat(file)
		ObjectOldZ(Dest)=ReadFloat(file)

		ObjectDX(Dest)=ReadFloat(file)
		ObjectDY(Dest)=ReadFloat(file)
		ObjectDZ(Dest)=ReadFloat(file)
	
		ObjectPitch(Dest)=ReadFloat(file)
		ObjectYaw(Dest)=ReadFloat(file)
		ObjectRoll(Dest)=ReadFloat(file)
		ObjectPitch2(Dest)=ReadFloat(file)
		ObjectYaw2(Dest)=ReadFloat(file)
		ObjectRoll2(Dest)=ReadFloat(file)

		ObjectXGoal(Dest)=ReadFloat(file)
		ObjectYGoal(Dest)=ReadFloat(file)
		ObjectZGoal(Dest)=ReadFloat(file)
	
		ObjectMovementType(Dest)=ReadInt(file)
		ObjectMovementTypeData(Dest)=ReadInt(file)
		ObjectSpeed(Dest)=ReadFloat(file)
		ObjectRadius(Dest)=ReadFloat(file)
		ObjectRadiusType(Dest)=ReadInt(file)
	
		ObjectData10(Dest)=ReadInt(file)
	
		ObjectPushDX(Dest)=ReadFloat(file)
		ObjectPushDY(Dest)=ReadFloat(file)
	
		ObjectAttackPower(Dest)=ReadInt(file)
		ObjectDefensePower(Dest)=ReadInt(file)
		ObjectDestructionType(Dest)=ReadInt(file)
	
		ObjectID(Dest)=ReadInt(file)
		
		ObjectType(Dest)=ReadInt(file)
		ObjectSubType(Dest)=ReadInt(file)
	
		ObjectActive(Dest)=ReadInt(file)
		ObjectLastActive(Dest)=ReadInt(file)
		ObjectActivationType(Dest)=ReadInt(file)
		ObjectActivationSpeed(Dest)=ReadInt(file)
	
		ObjectStatus(Dest)=ReadInt(file)
		ObjectTimer(Dest)=ReadInt(file)
		ObjectTimerMax1(Dest)=ReadInt(file)
		ObjectTimerMax2(Dest)=ReadInt(file)
	
		ObjectTeleportable(Dest)=ReadInt(file)
		ObjectButtonPush(Dest)=ReadInt(file)
		ObjectWaterReact(Dest)=ReadInt(file)
	
		ObjectTelekinesisable(Dest)=ReadInt(file)
		ObjectFreezable(Dest)=ReadInt(file)
	
		ObjectReactive(Dest)=ReadInt(file)

		ObjectChild(Dest)=ReadInt(file)
		ObjectParent(Dest)=ReadInt(file)


	
		For k=0 To 9
			ObjectData(Dest,k)=ReadInt(file)
		Next
		For k=0 To 3
			ObjectTextData$(Dest,k)=ReadString$(file)
		Next
		
		ObjectTalkable(Dest)=ReadInt(file)
		ObjectCurrentAnim(Dest)=ReadInt(file)
		ObjectStandardAnim(Dest)=ReadInt(file)
		;ObjectTileX(Dest)=ReadInt(file)
		;ObjectTileY(Dest)=ReadInt(file)
		tilex=ReadInt(file)
		tiley=ReadInt(file)
		ObjectTileX(Dest)=tilex
		ObjectTileY(Dest)=tiley
		ObjectTileX2(Dest)=ReadInt(file)
		ObjectTileY2(Dest)=ReadInt(file)
		ObjectMovementTimer(Dest)=ReadInt(file)
		ObjectMovementSpeed(Dest)=ReadInt(file)
		ObjectMoveXGoal(Dest)=ReadInt(file)
		ObjectMoveYGoal(Dest)=ReadInt(file)
		ObjectTileTypeCollision(Dest)=ReadInt(file)
		ObjectObjectTypeCollision(Dest)=ReadInt(file)
		ObjectCaged(Dest)=ReadInt(file)
		ObjectDead(Dest)=ReadInt(file)
		ObjectDeadTimer(Dest)=ReadInt(file)
		ObjectExclamation(Dest)=ReadInt(file)
		ObjectShadow(Dest)=ReadInt(file)
		ObjectLinked(Dest)=ReadInt(file)
		ObjectLinkBack(Dest)=ReadInt(file)
		ObjectFlying(Dest)=ReadInt(file)
		ObjectFrozen(Dest)=ReadInt(file)
		ObjectIndigo(Dest)=ReadInt(file)
		ObjectFutureInt24(Dest)=ReadInt(file)
		ObjectFutureInt25(Dest)=ReadInt(file)
		ObjectScaleAdjust(Dest)=ReadFloat(file)
		ObjectScaleXAdjust(Dest)=ReadFloat(file)	
		ObjectScaleYAdjust(Dest)=ReadFloat(file)
		ObjectScaleZAdjust(Dest)=ReadFloat(file)
		ObjectScaleXAdjust(Dest)=1.0
		ObjectScaleYAdjust(Dest)=1.0
		ObjectScaleZAdjust(Dest)=1.0
		ObjectFutureFloat5(Dest)=ReadFloat(file)
		ObjectFutureFloat6(Dest)=ReadFloat(file)
		ObjectFutureFloat7(Dest)=ReadFloat(file)	
		ObjectFutureFloat8(Dest)=ReadFloat(file)
		ObjectFutureFloat9(Dest)=ReadFloat(file)
		ObjectFutureFloat10(Dest)=ReadFloat(file)
		ObjectFutureString1$(Dest)=ReadString(file)
		ObjectFutureString2$(Dest)=ReadString(file)
		
		For k=0 To 30
			ObjectAdjusterString$(Dest,k)=ReadString(file)
		Next
		
		CreateObjectModel(Dest)

		LevelTileObjectCount(tilex,tiley)=LevelTileObjectCount(tilex,tiley)+1
		CreateObjectPositionMarker(Dest)
		

		NofObjects=NofObjects+1
		
		
		UpdateObjectPositionMarkersAtTile(tilex,tiley)
	Next
	
	
	
	; finalize object data
	PlayerIndex=NofObjects
	For j=0 To NofObjects-1
		ObjectLinked(j)=ObjectIndexGameToEditor(ObjectLinked(j), PlayerIndex)
		ObjectLinkBack(j)=ObjectIndexGameToEditor(ObjectLinkBack(j), PlayerIndex)
		ObjectParent(j)=ObjectIndexGameToEditor(ObjectParent(j), PlayerIndex)
		ObjectChild(j)=ObjectIndexGameToEditor(ObjectChild(j), PlayerIndex)
	Next
	
	SomeObjectWasChanged()
	
	
	LevelEdgeStyle=ReadInt(file)
	
	LightRed=ReadInt(file)
	LightGreen=ReadInt(file)
	LightBlue=ReadInt(file)
	
	AmbientRed=ReadInt(file)
	AmbientGreen=ReadInt(file)
	AmbientBlue=ReadInt(file)

	LevelMusic=ReadInt(file)
	LevelWeather=ReadInt(file)

	If Eof(file)=False
		ReadString(file)
		ReadInt(file)
		WidescreenRangeLevel=ReadInt(file)
	EndIf
	
	CloseFile file
	
	ReBuildLevelModel()
;	For j=0 To LevelHeight-1
;		EntityTexture LevelMesh(j),LevelTexture
;		EntityTexture WaterMesh(j),WaterTexture
;
;	Next
	BuildCurrentTileModel()
	
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next
	
	LightingWasChanged()
	
End Function

Function NewLevel(levelnumber)

	; new level
	CurrentLevelNumber=levelnumber
	resetlevel()
	; clear current objects 
	
	For ig=0 To NofObjects-1
		FreeObject(ig)				
	Next

	
	
	
	NofObjects=0
	; reset textures
	FreeTexture leveltexture
	FreeTexture watertexture
	CurrentLevelTexture=0
	LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
	CurrentWaterTexture=0	
	waterTexture=myLoadTexture("data\Leveltextures\"+waterTexturename$(CurrentWaterTexture),2)
	EntityTexture TexturePlane,LevelTexture
	EntityTexture CurrentWaterTile,WaterTexture


	rebuildlevelmodel()
	BuildCurrentTileModel()

End Function

Function CompileLevel()

End Function

Function AccessLevel(levelnumber)

	If LevelExists(levelnumber)=True
		LoadLevel(levelnumber)
	Else
		NewLevel(levelnumber)
	EndIf

End Function

Function CreateButtonMesh(btype,col1,col2,col3,col4)
	; texture is the available "colour" from 0-15
	btype=btype Mod 32
	If btype=15 Then btype=11	
	
	If btype>=5 And btype<10
		col3=col1
		col4=col2
	EndIf
	
	If btype=16 Or btype=17; rotator
		;If col3=1 Then btype=17
		col2=col1
		col3=col1
		col4=col1
	EndIf

	
	Entity=CreateMesh()
	surface=CreateSurface(Entity)
	
	; first, the outline shape
	AddVertex (surface,-.45,0.01,.45,(btype Mod 8)*0.125,(btype/8)*0.125)
	AddVertex (surface,.45,0.01,.45,(btype Mod 8)*0.125+0.125,(btype/8)*0.125)
	AddVertex (surface,-.45,0.01,-.45,(btype Mod 8)*0.125,(btype/8)*0.125+0.125)
	AddVertex (surface,.45,0.01,-.45,(btype Mod 8)*0.125+0.125,(btype/8)*0.125+0.125)
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	
	If btype<10 Or btype>13
		; now the four colours - the placement of these depend on the btype shape
		Select (btype Mod 32)
		Case 0,5
			; square
			nudge#=.01
			radius#=.4
			alt=True
		Case 1,6,16,17
			; circle
			nudge#=.01
			radius#=.3
			alt=True
		Case 2,3,7,8
			; diamond
			nudge#=.01
			radius#=.4
			alt=False
		Case 4,9
			; star
			nudge#=.01
			radius#=.28
			alt=True
					
		End Select
		
		AddVertex (Surface,-radius,0.005,radius,(col1 Mod 8)*0.125+.01,(col1/8)*0.125+0.5+.01)
		AddVertex (Surface,0.01,0.005,radius,(col1 Mod 8)*0.125+0.125-.01,(col1/8)*0.125+.5+.01)
		AddVertex (Surface,-radius,0.005,-0.01,(col1 Mod 8)*0.125+.01,(col1/8)*0.125+0.5+.125-.01)
		AddVertex (Surface,0.01,0.005,-0.01,(col1 Mod 8)*0.125+0.125-.01,(col1/8)*0.125+.5+.125-.01)
		If alt=True
			
			AddTriangle (surface,4,5,6)
			AddTriangle (surface,5,7,6)
		Else
			AddTriangle (surface,5,7,6)
		EndIf
		
		AddVertex (Surface,-0.01,0.005,radius,(col2 Mod 8)*0.125+.01,(col2/8)*0.125+0.5+.01)
		AddVertex (Surface,radius,0.005,radius,(col2 Mod 8)*0.125+0.125-.01,(col2/8)*0.125+.5+.01)
		AddVertex (Surface,-0.01,0.005,-0.01,(col2 Mod 8)*0.125+.01,(col2/8)*0.125+0.5+.125-.01)
		AddVertex (Surface,radius,0.005,-0.01,(col2 Mod 8)*0.125+0.125-.01,(col2/8)*0.125+.5+.125-.01)
		If alt=True
			AddTriangle (surface,8,9,10)
			AddTriangle (surface,9,11,10)
		Else
			AddTriangle (surface,8,11,10)
		EndIf
		
		AddVertex (Surface,-radius,0.005,.01,(col3 Mod 8)*0.125+.01,(col3/8)*0.125+0.5+.01)
		AddVertex (Surface,.01,0.005,.01,(col3 Mod 8)*0.125+0.125-.01,(col3/8)*0.125+.5+.01)
		AddVertex (Surface,-radius,0.005,-radius,(col3 Mod 8)*0.125+.01,(col3/8)*0.125+0.5+.125-.01)
		AddVertex (Surface,.01,0.005,-radius,(col3 Mod 8)*0.125+0.125-.02,(col3/8)*0.125+.5+.125-.01)
		If alt=True
			AddTriangle (surface,12,13,14)
			AddTriangle (surface,13,15,14)
		Else
			AddTriangle (surface,12,13,15)
		EndIf
		
		AddVertex (Surface,-.01,0.005,.01,(col4 Mod 8)*0.125+.01,(col4/8)*0.125+0.5+.01)
		AddVertex (Surface,radius,0.005,.01,(col4 Mod 8)*0.125+0.125-.01,(col4/8)*0.125+.5+.01)
		AddVertex (Surface,-.01,0.005,-radius,(col4 Mod 8)*0.125+.01,(col4/8)*0.125+0.5+.125-.01)
		AddVertex (Surface,radius,0.005,-radius,(col4 Mod 8)*0.125+0.125-.01,(col4/8)*0.125+.5+.125-.01)
		If alt=True
			AddTriangle (surface,16,17,18)
			AddTriangle (surface,17,19,18)
		Else
			AddTriangle (surface,16,17,18)
		EndIf
	
	EndIf
		
	
	UpdateNormals Entity
	
	EntityTexture Entity,ButtonTexture
	
	Return ENtity

End Function

Function CreateColourGateMesh(subtype,tex)
	
	Entity=CreateMesh()
	Surface=CreateSurface(Entity)
	
	; Top 
	AddVertex (surface,-.495,1.01,.495,subtype*0.25+.01,.26)
	AddVertex (surface,.495,1.01,.495,subtype*0.25+.24,.26)
	AddVertex (surface,-.495,1.01,-.495,subtype*0.25+.01,.49)
	AddVertex (surface,.495,1.01,-.495,subtype*0.25+.24,.49)
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	AddVertex (surface,-.25,1.02,.25,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
	AddVertex (surface,.25,1.02,.25,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
	AddVertex (surface,-.25,1.02,-.25,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
	AddVertex (surface,.25,1.02,-.25,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	AddTriangle (surface,4,5,6)
	AddTriangle (surface,5,7,6)
	
	; Sides
	For i=0 To 3
		Select i
		Case 0
			x1#=-.49
			x2#=.49
			y1#=-.49
			y2#=-.49
		Case 1
			x1#=.49
			x2#=.49
			y1#=-.49
			y2#=.49
		Case 2
			x1#=.49
			x2#=-.49
			y1#=.49
			y2#=.49
		Case 3
			x1#=-.49
			x2#=-.49
			y1#=.49
			y2#=-.49
		End Select

	
			
		AddVertex (surface,x1,1,y1,subtype*0.25+.01,.01)
		AddVertex (surface,x2,1,y2,subtype*0.25+.24,.01)
		AddVertex (surface,x1,0,y1,subtype*0.25+.01,.24)
		AddVertex (surface,x2,0,y2,subtype*0.25+.24,.24)
		AddTriangle (surface,8+i*8,9+i*8,10+i*8)
		AddTriangle (surface,9+i*8,11+i*8,10+i*8)
		AddVertex (surface,x1*1.01,.8,y1*1.01,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
		AddVertex (surface,x2*1.01,.8,y2*1.01,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
		AddVertex (surface,x1*1.01,.6,y1*1.01,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
		AddVertex (surface,x2*1.01,.6,y2*1.01,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	
		AddTriangle (surface,12+i*8,13+i*8,14+i*8)
		AddTriangle (surface,13+i*8,15+i*8,14+i*8)
		
		
	Next


	
	
	UpdateNormals Entity
	
	EntityTexture Entity,GateTexture
	Return Entity

End Function

Function CreateRetroLaserGateMesh(col)
		
	
	; create the mesh if laser gate
	Entity=CreateMesh() 
	cyl=CreateCylinder (6,False) ; an individual cylinder
	ScaleMesh cyl,0.05,0.5,0.05
	RotateMesh cyl,0,0,90
	PositionMesh cyl,0,.25,0.0
	AddMesh cyl,Entity
	PositionMesh cyl,0,-.375,.2165
	AddMesh cyl,Entity
	PositionMesh cyl,0,0,-.433
	AddMesh cyl,Entity
	FreeEntity cyl
	
	EntityAlpha Entity,0.5
	
	If col=0
		EntityColor Entity,255,0,0
	Else If col=1
		EntityColor Entity,255,128,0
	Else If col=2
		EntityColor Entity,255,255,0
	Else If col=3
		EntityColor Entity,0,255,0
	Else If col=4
		EntityColor Entity,0,255,255
	Else If col=5
		EntityColor Entity,0,0,255
	Else 
		EntityColor Entity,255,0,255
	EndIf
	
	Return Entity
	
End Function


Function CreateTransporterMesh(tex,subtype)
	
	Entity=CreateMesh()
	Surface=CreateSurface(Entity)
	
	
	
	; Top 
	AddVertex (surface,-.495,0.01,.495,subtype*0.25+.01,.26)
	AddVertex (surface,.495,0.01,.495,subtype*0.25+.24,.26)
	AddVertex (surface,-.495,0.01,-.495,subtype*0.25+.01,.49)
	AddVertex (surface,.495,0.01,-.495,subtype*0.25+.24,.49)
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	AddVertex (surface,-.25,0.02,.25,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
	AddVertex (surface,.25,0.02,.25,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
	AddVertex (surface,-.25,0.02,-.25,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
	AddVertex (surface,.25,0.02,-.25,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	AddTriangle (surface,4,5,6)
	AddTriangle (surface,5,7,6)
	
	; Sides
	For i=0 To 3
		Select i
		Case 0
			x1#=-.49
			x2#=.49
			y1#=-.49
			y2#=-.49
		Case 1
			x1#=.49
			x2#=.49
			y1#=-.49
			y2#=.49
		Case 2
			x1#=.49
			x2#=-.49
			y1#=.49
			y2#=.49
		Case 3
			x1#=-.49
			x2#=-.49
			y1#=.49
			y2#=-.49
		End Select

	
			
		AddVertex (surface,x1,0,y1,subtype*0.25+.01,.01)
		AddVertex (surface,x2,0,y2,subtype*0.25+.24,.01)
		AddVertex (surface,x1,-.3,y1,subtype*0.25+.01,.24)
		AddVertex (surface,x2,-.3,y2,subtype*0.25+.24,.24)
		AddTriangle (surface,8+i*4,9+i*4,10+i*4)
		AddTriangle (surface,9+i*4,11+i*4,10+i*4)
		
		
	Next


	
	
	UpdateNormals Entity
	
	EntityTexture Entity,GateTexture
	Return Entity

End Function

Function CreateCloudMesh(col)

	Select col
	Case 0
		red=255
		green=0
		blue=0
	Case 1
		red=255
		green=128
		blue=0
	Case 2
		red=255
		green=255
		blue=100
	Case 3
		red=0
		green=255
		blue=0
	Case 4
		red=0
		green=255
		blue=255
	Case 5
		red=0
		green=0
		blue=255
	Case 6
		red=255
		green=0
		blue=255
	Default
		red=255
		green=255
		blue=255
	End Select

	Entity=CreateMesh()
	Surface=CreateSurface(Entity)
	
	
	
	
	AddVertex (surface,-.495,0.01,.495,0,0)
	AddVertex (surface,.495,0.01,.495,0,1)
	AddVertex (surface,-.495,0.01,-.495,1,0)
	AddVertex (surface,.495,0.01,-.495,1,1)
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	
	AddVertex (surface,-.495,-0.1,.495,0,0)
	AddVertex (surface,.495,-0.1,.495,0,1)
	AddVertex (surface,-.495,-0.1,-.495,1,0)
	AddVertex (surface,.495,-0.1,-.495,1,1)
	AddTriangle (surface,4,5,6)
	AddTriangle (surface,5,6,6)


	AddVertex (surface,-.495,-0.2,.495,0,0)
	AddVertex (surface,.495,-0.2,.495,0,1)
	AddVertex (surface,-.495,-0.2,-.495,1,0)
	AddVertex (surface,.495,-0.2,-.495,1,1)
	AddTriangle (surface,8,9,10)
	AddTriangle (surface,9,11,10)



	UpdateNormals Entity
	
	EntityTexture Entity,CloudTexture
	EntityAlpha Entity,.8
	EntityColor entity,red,green,blue
	Return Entity


End Function



Function CreateTeleporterMesh(tex)


	
	entity=CreateCylinder(16,False)
	;ObjectTexture(i)=TeleporterTexture(texture)	
	
	PositionMesh entity,0,1,0
	ScaleMesh entity,.4,2,.4
	EntityAlpha entity,.6
	If tex<0 Or tex>8
		tex=0
	EndIf
	EntityTexture entity,TeleporterTexture(tex)
	EntityBlend entity,3
	EntityFX entity,2
	For j=0 To 16
		VertexColor GetSurface(entity,1),j*2,0,0,0
	Next
	
	Return entity
End Function
	

Function CreatePickUpItemMesh(tex)
	entity=CreateMesh()
	surface=CreateSurface(entity)
	
	AddVertex (surface,0,.1,0,.9375,.9375)
	
	R#=.3
	AddVertex (surface,R,.1,.5+R,.875,.875)
	AddVertex (surface,.5+R,.1,R,.875,1)
	AddTriangle (surface,0,1,2)
	AddVertex (surface,.5+R,.1,-R,1,1)
	AddTriangle (surface,0,2,3)
	AddVertex (surface,+R,.1,-.5-R,1,.875)
	AddTriangle (surface,0,3,4)
	AddVertex (surface,-R,.1,-.5-R,.875,.875)
	AddTriangle (surface,0,4,5)
	AddVertex (surface,-.5-R,.1,-R,1,.875)
	AddTriangle (surface,0,5,6)
	AddVertex (surface,-.5-R,.1,R,1,1)
	AddTriangle (surface,0,6,7)
	AddVertex (surface,-R,.1,.5+R,.875,1)
	AddTriangle (surface,0,7,8)
	AddTriangle (surface,0,8,1)
	
	AddVertex (surface,-.5,.2,.5,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,.5,.2,.5,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,-.5,.2,-.5,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,.5,.2,-.5,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddTriangle (surface,9,10,11)
	AddTriangle (surface,10,12,11)
	
	AddVertex(surface,0,1.5,0,.9375,.9375)
	AddTriangle (surface,13,1,2)
	AddTriangle (surface,13,2,3)
	AddTriangle (surface,13,3,4)
	AddTriangle (surface,13,4,5)
	AddTriangle (surface,13,5,6)
	AddTriangle (surface,13,6,7)
	AddTriangle (surface,13,7,8)
	AddTriangle (surface,13,8,1)
	
	 
	For j=0 To 8
		VertexColor  surface,j,255,255,255,.5
	Next
	VertexColor  surface,13,255,255,255,.5

	EntityFX Entity,32+2

	ScaleMesh Entity,.5,.5,.5
	
	UpdateNormals Entity
;	EntityTexture Entity,IconTextureStandard
	
	Return Entity
End Function

Function CreateWaterFallMesh(tex)
	
	Entity=CreateMesh()
	surface=CreateSurface(Entity)
	
	AddVertex surface,-.5,0.81,-.51,0,0
	AddVertex surface,.5,0.81,-.51,1,0
	AddVertex surface,-.5,-0.21,-.51,0,1
	AddVertex surface,.5,-0.21,-.51,1,1
	
	AddVertex surface,-.5,0.31,-.51,0,1
	AddVertex surface,.5,0.31,-.51,1,1
	AddVertex surface,-.5,0.31,-.51,0,0
	AddVertex surface,.5,0.31,-.51,1,0
	
	AddTriangle (surface,0,1,2)
	AddTriangle (surface,1,3,2)
	
	AddTriangle (surface,0,1,4)
	AddTriangle (surface,1,5,4)
	AddTriangle (surface,6,7,2)
	AddTriangle (surface,7,3,2)
	
	AddTriangle (surface,1,0,3)
	AddTriangle (surface,0,2,3)
	
	EntityTexture Entity,WaterFallTexture(tex)
	EntityAlpha Entity,.7
		
	UpdateNormals Entity
	
	Return Entity

End Function

Function CreateKeyMesh(col)

	Entity=CopyMesh(KeyMesh)
	
	;adjust the colour
	

	For i=0 To CountVertices (GetSurface(Entity,1))-1
		
		VertexTexCoords GetSurface(Entity,1),i,VertexU(GetSurface(Entity,1),i)+(col Mod 8)*0.125,VertexV(GetSurface(Entity,1),i)+0.5+Floor(col/8)*0.125
	Next
	UpdateNormals Entity
	EntityTexture Entity,ButtonTexture
	Return Entity
End Function

Function CreateKeyCardMesh(col)

	tex=24+col

	entity=CreateMesh()


	surface=CreateSurface(entity)
	
	AddVertex (surface,-.4,.4,-.1,(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,.4,.4,-.10,(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,-.4,-.4,-.10,(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.125)
	AddVertex (surface,.4,-.4,0-.1,(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.125)
	
	AddVertex (surface,-.4,.4,.10,(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,.4,.4,.10,(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.000)
	AddVertex (surface,-.4,-.4,.10,(tex Mod 8)*0.125+0.000,Floor((tex Mod 64)/8)*0.125+0.125)
	AddVertex (surface,.4,-.4,.10,(tex Mod 8)*0.125+0.125,Floor((tex Mod 64)/8)*0.125+0.125)



	AddTriangle(surface,0,1,2)
	AddTriangle(surface,1,3,2)
	
	AddTriangle(surface,5,4,6)
	AddTriangle(surface,5,6,7)
	
	; editor rotation only
	;RotateMesh Entity,90,0,0
	;PositionMesh Entity,0,.3,0

	
	
	
	UpdateNormals Entity
	EntityTexture Entity,ButtonTexture
	Return Entity
End Function


Function CreateCustomItemMesh(tex)
	entity=CreateMesh()


	surface=CreateSurface(entity)
	
	AddVertex (surface,-.4,.4,-.1,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,.4,.4,-.10,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,-.4,-.4,-.10,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,.4,-.4,0-.1,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.0945)
	
	AddVertex (surface,-.4,.4,.10,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,.4,.4,.10,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,-.4,-.4,.10,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,.4,-.4,.10,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.0945)


	AddTriangle(surface,0,1,2)
	AddTriangle(surface,1,3,2)
	
	AddTriangle(surface,5,4,6)
	AddTriangle(surface,5,6,7)
	
	
	tex=63
	AddVertex (surface,-.45,.45,-.0951,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,.45,.45,-.09510,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,-.45,-.45,-.09510,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,.45,-.45,0-.0951,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.0945)
	
	AddVertex (surface,-.45,.45,.09510,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,.45,.45,.09510,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.013)
	AddVertex (surface,-.45,-.45,.09510,(tex Mod 8)*0.125+0.013,Floor((tex Mod 64)/8)*0.125+0.0945)
	AddVertex (surface,.45,-.45,.09510,(tex Mod 8)*0.125+0.1135,Floor((tex Mod 64)/8)*0.125+0.0945)


	AddTriangle(surface,8,9,10)
	AddTriangle(surface,9,11,10)
	
	AddTriangle(surface,13,12,14)
	AddTriangle(surface,13,14,15)
	
	
	AddTriangle(surface,12,13,9)
	AddTriangle(surface,8,12,9)
	
	
	AddTriangle(surface,11,15,14)
	AddTriangle(surface,11,14,10)
	
	
	AddTriangle(surface,12,8,14)
	AddTriangle(surface,8,10,14)
	
	
	AddTriangle(surface,9,13,15)
	AddTriangle(surface,9,15,11)







	
	
	UpdateNormals Entity
	EntityTexture Entity,IconTextureCustom
	
	; new to editor to make custom item face upwards
	;RotateMesh Entity,90,0,0
	;PositionMesh Entity,0,.3,0
	
	Return Entity

End Function

Function CreatePushbotMesh(tex,dir)
	
	Entity=CreateMesh()
	Surface=CreateSurface(Entity)
	
	If dir=2 ;(180 turn around)
		dir=0
		;front
		AddVertex (surface,-.4,0,.4,0,.25+.25*dir)
		AddVertex (surface,+.4,0,.4,0,0+.25*dir)
		AddVertex (surface,-.2,.3,.2,.25,.20+.25*dir)
		AddVertex (surface,+.2,.3,.2,.25,.05+.25*dir)
		AddTriangle (surface,0,1,2)
		AddTriangle (surface,1,3,2)
		; Top
		AddVertex (surface,-.4,.4,-.4,.5,.20+.25*dir)
		AddVertex (surface,+.4,.4,-.4,.5,.05+.25*dir)
		AddTriangle (surface,2,3,4)
		AddTriangle (surface,3,5,4)
		;Back
		AddVertex (surface,-.45,0,-.45,.75,.25+.25*dir)
		AddVertex (surface,+.45,0,-.45,.75,0+.25*dir)
		AddTriangle (surface,4,5,6)
		AddTriangle (surface,5,7,6)
		; Left
		AddVertex (surface,-.4,0,.4,.75,.25+.25*dir)
		AddVertex (surface,-.45,0,-.45,.75,0+.25*dir)
		AddVertex (surface,-.2,.3,.2,1,.25+.25*dir)
		AddVertex (surface,-.4,.4,-.4,1,0+.25*dir)
		AddTriangle (surface,10,9,8)
		AddTriangle (surface,10,11,9)
		; Right
		AddVertex (surface,.4,0,.4,.75,.25+.25*dir)
		AddVertex (surface,.45,0,-.45,.75,0+.25*dir)
		AddVertex (surface,.2,.3,.2,1,.25+.25*dir)
		AddVertex (surface,.4,.4,-.4,1,0+.25*dir)
		AddTriangle (surface,12,13,14)
		AddTriangle (surface,13,15,14)

	
	Else
		dir=1-dir
		; Front
		AddVertex (surface,-.4,0,.4,0,.25+.25*dir)
		AddVertex (surface,+.4,0,.4,0,0+.25*dir)
		AddVertex (surface,-.2,.3,.2,.25,.25+.25*dir)
		AddVertex (surface,+.2,.3,.2,.25,0+.25*dir)
		AddTriangle (surface,0,1,2)
		AddTriangle (surface,1,3,2)
		; Top
		AddVertex (surface,-.4,.4,-.4,.5,.25+.25*dir)
		AddVertex (surface,+.4,.4,-.4,.5,0+.25*dir)
		AddTriangle (surface,2,3,4)
		AddTriangle (surface,3,5,4)
		;Back
		AddVertex (surface,-.45,0,-.45,.75,.25+.25*dir)
		AddVertex (surface,+.45,0,-.45,.75,0+.25*dir)
		AddTriangle (surface,4,5,6)
		AddTriangle (surface,5,7,6)
		; Left
		AddVertex (surface,-.4,0,.4,.75,.25+.25*dir)
		AddVertex (surface,-.45,0,-.45,.75,0+.25*dir)
		AddVertex (surface,-.2,.3,.2,1,.25+.25*dir)
		AddVertex (surface,-.4,.4,-.4,1,0+.25*dir)
		AddTriangle (surface,10,9,8)
		AddTriangle (surface,10,11,9)
		; Right
		AddVertex (surface,.4,0,.4,.75,.25+.25*dir)
		AddVertex (surface,.45,0,-.45,.75,0+.25*dir)
		AddVertex (surface,.2,.3,.2,1,.25+.25*dir)
		AddVertex (surface,.4,.4,-.4,1,0+.25*dir)
		AddTriangle (surface,12,13,14)
		AddTriangle (surface,13,15,14)
	EndIf

	
	
	; Col
	AddVertex (surface,-.05,.33,.05,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51)
	AddVertex (surface,.05,.33,.05,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51)
	AddVertex (surface,-.25,.39,-.35,(tex Mod 8)*0.125+.01,(tex/8)*0.125+.51+.115)
	AddVertex (surface,.25,.39,-.35,(tex Mod 8)*0.125+.115,(tex/8)*0.125+.51+.115)
	AddTriangle (surface,16,17,18)
	AddTriangle (surface,17,19,18)
	
		
	
	UpdateNormals Entity
	
	EntityTexture Entity,PushbotTexture
	Return Entity

End Function

Function CreateSuctubeMesh(tex,col,active)
	
	If active Mod 2 =1
		active=0
	Else
		active=1
	EndIf
	
	Entity=CreateMesh()
	Surface=CreateSurface(Entity)
	
	nofsegments#=16
	
	i=0
	angle#=-(360.0/nofsegments)/2.0+i*(360.0/nofsegments)
	; top triangle
	AddVertex (surface,-0.3,1.71,-0.3,(col Mod 8)*0.125+.01,(col/8)*0.125+.51+.25*active)
	AddVertex (surface,+0.3,1.71,-0.3,(col Mod 8)*0.125+.115,(col/8)*0.125+.51+.25*active)
	AddVertex (surface,0,1.71,+0.3,(col Mod 8)*0.125+.01,(col/8)*0.125+.51+.115+.25*active)
	;AddTriangle (surface,0,1,2)
	AddTriangle (surface,0,2,1)
	
	
	
	
	; point arrow
	If dir=0
		VertexCoords surface,0,-0.3,1.71,-0.3
		VertexCoords surface,1,+0.3,1.71,-0.3
		VertexCoords surface,2,0,1.71,+0.3
	Else
		VertexCoords surface,0,-0.3,1.71,+0.3
		VertexCoords surface,2,+0.3,1.71,+0.3
		VertexCoords surface,1,0,1.71,-0.3
	EndIf


	
	For i=0 To nofsegments-1
		angle#=-(360.0/nofsegments)/2.0+i*(360.0/nofsegments)
		AddVertex (surface,1.5*Sin(angle),0.7+1.0*Cos(angle),-0.505,0.25*tex,107.0/512.0)
		AddVertex (surface,1.5*Sin(angle),0.7+1.0*Cos(angle),+0.505,0.25*tex,88.0/512.0)
		AddVertex (surface,1.5*Sin(angle+(360.0/nofsegments)),0.7+1.0*Cos(angle+(360.0/nofsegments)),-0.505,0.25*tex+0.25,107.0/512.0)
		AddVertex (surface,1.5*Sin(angle+(360.0/nofsegments)),0.7+1.0*Cos(angle+(360.0/nofsegments)),+0.505,0.25*tex+0.25,88.0/512.0)

		;i=i+1 ; to account for the first four vertices
		AddTriangle(surface,i*4+0+3,i*4+1+3,i*4+2+3)
		AddTriangle(surface,i*4+1+3,i*4+3+3,i*4+2+3)
		
		AddTriangle(surface,i*4+2+3,i*4+1+3,i*4+0+3)
		AddTriangle(surface,i*4+2+3,i*4+3+3,i*4+1+3)
		;i=i-1
	Next
	

	

	UpdateNormals Entity
	
	EntityTexture Entity,GateTexture
	Return Entity
End Function

Function CreateSuctubeXMesh(tex)
	
	Entity=CreateMesh()
	Surface=CreateSurface(Entity)
	
	nofsegments#=16
	nofarcpoints#=8
	
		

	For j=0 To nofarcpoints
		angle2#=(90.0/nofarcpoints)*j
		For i=0 To nofsegments-1
			angle#=-(360.0/nofsegments)/2.0+i*(360.0/nofsegments)
			height#=0.7+1.0*Cos(angle)
			radius#=1.5-1.5*Sin(angle)
			
			
			If i Mod 2 =0
				xtex#=0.25
			Else
				xtex#=0.0
			EndIf

			If j Mod 2 =0
				ytex#=19.0
			Else
				ytex#=0.0
			EndIf
			
			AddVertex (surface,1.5-radius*Cos(angle2),height,-1.5+radius*Sin(angle2),0.25*tex+xtex,(107.0-ytex)/512.0)
			
		Next
	Next
	
	For j=0 To nofarcpoints-1
		For i=0 To nofsegments-1
		
			AddTriangle(surface,j*nofsegments+i,j*nofsegments+((i+1) Mod nofsegments),(j+1)*nofsegments+i)
			AddTriangle(surface,(j+1)*nofsegments+i,j*nofsegments+((i+1) Mod nofsegments),(j+1)*nofsegments+((i+1) Mod nofsegments))
			
			AddTriangle(surface,j*nofsegments+((i+1) Mod nofsegments),j*nofsegments+i,(j+1)*nofsegments+i)
			AddTriangle(surface,(j+1)*nofsegments+((i+1) Mod nofsegments),j*nofsegments+((i+1) Mod nofsegments),(j+1)*nofsegments+i)


			
		Next
	Next
	

	

	UpdateNormals Entity
	
	EntityTexture Entity,GateTexture
	Return Entity
End Function







Function GetTextureNames()

	dir=ReadDir("data\leveltextures")
	
	NofLevelTextures=0
	NofWaterTextures=0

	file$=NextFile$(dir)
	While file$<>""
		If Lower$(Right$(file$,4))=".wdf" And Lower$(Left$(file$,9))="mfwfmufy "
			file$=decode$(Mid$(file$,10,Len(file$)-13))
			Leveltexturename$(NofLevelTextures)="leveltex "+file$+".bmp"
			NofLevelTextures=NofLevelTextures+1
		EndIf
		If Lower$(Right$(file$,4))=".wdf" And Lower$(Left$(file$,9))="xbufsufy "
			file$=decode$(Mid$(file$,10,Len(file$)-13))
			Watertexturename$(NofWaterTextures)="watertex "+file$+".jpg"
			NofWaterTextures=NofWaterTextures+1
		EndIf
		file$=NextFile$(dir)

	Wend
	
	CloseDir dir

		
	
End Function

Function MyLoadTexture(ex$,flag)

;	MyWriteString(debugfile,"Tex: "+ex$)

	exbackup$=ex$
	
	j=Len(ex$)
	Repeat
		j=j-1
	Until Mid$(ex$,j,1)="/" Or Mid$(ex$,j,1)="\" Or j=1
	
	If j=1
		ex2$=""
		j=0
	Else
		ex2$=Left$(ex$,j)
	EndIf
	
	Repeat
		j=j+1
		b=Asc(Mid$(Lower$(ex$),j,1))
		If b>=97 And b<=122
			b=b+1
			If b=123 Then b=97
		EndIf
		ex2$=ex2$+Chr$(b)
	Until Mid$(ex$,j,1)="."
	ex2$=ex2$+"wdf"
	
 
	;Print ex2$
	a=LoadTexture(ex2$,flag)
	If a=0
	
		a=LoadTexture(exbackup$,flag)
		If a=0
			jj=0
			Repeat
				jj=jj+1
			Until FileType(globaldirname$+"\temp\debug."+Str$(jj))=0
			
			debugfile=WriteFile (globaldirname$+"\temp\debug."+Str$(jj))
			Print "Couldn't Load Texture:"+ex$
			Delay 5000
			WriteString debugfile,ex$
			WriteString debugfile,ex2$
			If FileType(ex2$)=1
				WriteString debugfile,"File Exists!"
			Else
				WriteString debugfile,"File Doesn't Exist!"
				If ex$="load.jpg"
					Print ""
					Print "Please ensure that you install any updates INTO"
					Print "your existing Wonderland Adventures directory"
					Print "(i.e. overwriting some of the existing files)."
					Print ""
					Print "Do not simply run the .exe file from within the"
					Print "update files, or you will receive this error."
					Print "Unzip the files into your Wonderland directory first."
					
					Delay 1000
					Print ""
					Print "Exiting... Press Any Key."
					WaitKey()
					
					End
				EndIf
			EndIf
			WriteInt debugfile,TotalVidMem()
			WriteInt debugfile,AvailVidMem()
			Print "Trying Again"
			a=LoadTexture(ex2$,flag)
			If a=0
				Print "Nope."
				Delay 5000
				End
			EndIf
		EndIf
	EndIf
	Return a
End Function

Function MyLoadMesh(ex$,parent)
;	MyWriteString(debugfile,"Mesh: "+ex$)

	j=Len(ex$)
	Repeat
		j=j-1
	Until Mid$(ex$,j,1)="/" Or Mid$(ex$,j,1)="\" Or j=1
	
	If j=1
		ex2$=""
		j=0
	Else
		ex2$=Left$(ex$,j)
	EndIf
	
	Repeat
		j=j+1
		b=Asc(Mid$(Lower$(ex$),j,1))
		If b>=97 And b<=122
			b=b+1
			If b=123 Then b=97
		EndIf
		ex2$=ex2$+Chr$(b)
	Until Mid$(ex$,j,1)="."
	If Lower$(Right$(ex$,3))="3ds"
		ex2$=ex2$+"wd3"
	Else
		ex2$=ex2$+"wd1"
	EndIf
	
	;Print ex2$
	
	

;	Print ex2$
	
	CopyFile ex2$,globaldirname$+"\temp\debug."+Right$(ex$,3)
 
	;Print ex2$
	If parent>0
		a=LoadMesh(globaldirname$+"\temp\debug."+Right$(ex$,3),parent)
	Else
		a=LoadMesh(globaldirname$+"\temp\debug."+Right$(ex$,3))
	EndIf
	DeleteFile globaldirname$+"\temp\debug."+Right$(ex$,3)

	If a=0
		jj=0
		Repeat
			jj=jj+1
		Until FileType(globaldirname$+"\temp\debug."+Str$(jj))=0
		
		debugfile=WriteFile (globaldirname$+"\temp\debug."+Str$(jj))
		Print "Couldn't Load Mesh:"+ex$
		Delay 5000
		WriteString debugfile,ex$
		WriteString debugfile,ex2$
		
		WriteInt debugfile,TotalVidMem()
		WriteInt debugfile,AvailVidMem()
		End
	EndIf
	Return a
End Function


Function MyLoadAnimMesh(ex$,parent)
;	MyWriteString(debugfile,"AnimMesh: "+ex$)

	ex4$=ex$

	j=Len(ex$)
	Repeat
		j=j-1
	Until Mid$(ex$,j,1)="/" Or Mid$(ex$,j,1)="\" Or j=1
	
	If j=1
		ex2$=""
		j=0
	Else
		ex2$=Left$(ex$,j)
	EndIf
	
	j2=j
	
	Repeat
		j=j+1
		b=Asc(Mid$(Lower$(ex$),j,1))
		If b>=97 And b<=122
			b=b+1
			If b=123 Then b=97
		EndIf
		ex2$=ex2$+Chr$(b)
	Until Mid$(ex$,j,1)="."
	If Lower$(Right$(ex$,3))="3ds"
		ex2$=ex2$+"wd3"
	Else
		ex2$=ex2$+"wd1"
	EndIf
	
	

;	Print ex2$
;	Print ex$
	If j2>0
		ex$=Right$(ex$,Len(ex$)-j2)
	EndIf
	
;	Print ex$
		
	ex$=GlobalDirName$+"\Temp\"+ex$
	
	
	
	CopyFile ex2$,ex$

 
	;Print ex2$
	If parent>0
		a=LoadAnimMesh(ex$,parent)
	Else
		a=LoadAnimMesh(ex$)
	EndIf
	DeleteFile ex$
	If a=0
		jj=0
		Repeat
			jj=jj+1
		Until FileType("debug."+Str$(jj))=0
		
		debugfile=WriteFile ("debug."+Str$(jj))
		Print "Couldn't Load AnimMesh:"+ex$
		Delay 5000
		WriteString debugfile,ex$
		WriteString debugfile,ex2$
		
		WriteInt debugfile,TotalVidMem()
		WriteInt debugfile,AvailVidMem()
		End
	EndIf

	Return a
End Function

Function MyLoadMD2(ex$)
;	MyWriteString(debugfile,"MD2: "+ex$)

	j=Len(ex$)
	Repeat
		j=j-1
	Until Mid$(ex$,j,1)="/" Or Mid$(ex$,j,1)="\" Or j=1
	
	If j=1
		ex2$=""
		j=0
	Else
		ex2$=Left$(ex$,j)
	EndIf
	
	Repeat
		j=j+1
		b=Asc(Mid$(Lower$(ex$),j,1))
		If b>=97 And b<=122
			b=b+1
			If b=123 Then b=97
		EndIf
		ex2$=ex2$+Chr$(b)
	Until Mid$(ex$,j,1)="."
	ex2$=ex2$+"wd2"
	
 
	;Print ex2$
	a=LoadMD2(ex2$,parent)
	
	If a=0
		jj=0
		Repeat
			jj=jj+1
		Until FileType(globaldirname$+"\temp\debug."+Str$(jj))=0
		
		debugfile=WriteFile (globaldirname$+"\temp\debug."+Str$(jj))
		Print "Couldn't Load MD2:"+ex$
		Delay 5000
		WriteString debugfile,ex$
		WriteString debugfile,ex2$
		
		WriteInt debugfile,TotalVidMem()
		WriteInt debugfile,AvailVidMem()
		End
	EndIf

	Return a
End Function

Function myFileType(ex$)
	j=Len(ex$)
	
	If j<>1
		Repeat
			j=j-1
		Until Mid$(ex$,j,1)="/" Or Mid$(ex$,j,1)="\" Or j=1
	EndIf
	
	If j=1
		ex2$=""
		j=0
	Else
		ex2$=Left$(ex$,j)
	EndIf
	
	Repeat
		j=j+1
		b=Asc(Mid$(Lower$(ex$),j,1))
		If b>=97 And b<=122
			b=b+1
			If b=123 Then b=97
		EndIf
		ex2$=ex2$+Chr$(b)
	Until Mid$(ex$,j,1)="." Or j>=4096
	ex2$=ex2$+"wdf"
	Return FileType(ex2$)

End Function

Function DisplayText2(mytext$,x#,y#,red,green,blue,widthmult#=1.0)
	
	For i=1 To Len(mytext$)
		let=Asc(Mid$(mytext$,i,1))-32
		AddLetter(let,-.97+(x+(i-1)*widthmult)*.045,.5-(y-4+j)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,red,green,blue)
	Next
	
End Function

Function StartUserSelectScreen()
	SetEditorMode(4)
	
	EditorUserNameEntered$=""
	NofEditorUserNames=0
	
	dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Profiles")
	
	Repeat
		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(GlobalDirName$+"\custom\Editing\Profiles\"+ex$)=2
			EditorUserNamesListed$(NofEditorUserNames)=ex$
			NofEditorUserNames=NofEditorUserNames+1
		EndIf
	Until ex$=""
	
	CloseDir dirfile
	
	CameraProjMode Camera1,0
	CameraProjMode Camera2,0
	CameraProjMode Camera3,0
	CameraProjMode Camera4,0
	CameraProjMode Camera,1
		
End Function

Function UserSelectScreen()

	leveltimer=leveltimer+1
	
	my=MouseY()
	If mY>=306
		EditorUserNameSelected=(my-306)/20
	Else
		EditorUserNameSelected=-1
	EndIf
	
	DisplayText2("Editor Profile Name Selector",0,0,255,255,255)
	DisplayText2("-------------------------------------------",0,1,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Select your Editor Profile Name. This name",0,2,155,155,155)
	DisplayText2("is attached to all levels you create. You",0,3,155,155,155)
	DisplayText2("should use the same name as your Wonderland",0,4,155,155,155)
	DisplayText2("Forum login, so other players can identify",0,5,155,155,155)
	DisplayText2("your levels easily.",0,6,155,155,155)
	 
	DisplayText2("-------------------------------------------",0,7,TextMenusR,TextMenusG,TextMenusB)
	
	DisplayText2("Enter New Profile Name (use Keyboard):",0,10,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(EditorUserNameEntered$,0,11,255,255,255)
	If leveltimer Mod 100 <50
		DisplayText2(":",Len(EditorUserNameEntered$),11,255,255,255)
	EndIf
	
	
	If NofEditorUserNames>0
		DisplayText2("Or Select Existing Profile (use Mouse):",0,14,TextMenusR,TextMenusG,TextMenusB)
		For i=0 To NofEditorUserNames-1
			If i=EditorUserNameSelected
				DisplayText2(EditorUserNamesListed$(i),5,15+i,255,255,255)
			Else
				DisplayText2(EditorUserNamesListed$(i),5,15+i,155,155,155)
			EndIf
				
		Next
	EndIf
	
	
	
;	DisplayText2("---CANCEL---",16.5,27,TextMenusR,TextMenusG,TextMenusB)
	
	; Entering New Name
	let=GetKey()
	If let>=32 And let<=122 And Len(editorusernameentered$)<20
		EditorUserNameEntered$=EditorUserNameEntered$+Chr$(let)
	EndIf
	If KeyDown(14)
		; backspace
		If Len(EditorUserNameEntered$)>0
			EditorUserNameEntered$=Left$(EditorUserNameEntered$,Len(EditorUserNameEntered$)-1)
			Delay CharacterDeleteDelay
		EndIf
	EndIf
	If KeyDown(211)
		; delete
		EditorUserNameEntered$=""
		Delay CharacterDeleteDelay
	EndIf
	If KeyDown(28) Or KeyDown(156)
		; Enter
		
		If EditorUserNameEntered$=""
			DisplayText2("INVALID PROFILE NAME - Empty Name!",0,12,TextMenusR,TextMenusG,TextMenusB)
		Else If FileType(GlobalDirName$+"\Editor Profiles\"+EditorUserNameEntered$)=2
			DisplayText2("INVALID PROFILE NAME - Already Exists!",0,12,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("SAVING PROFILE NAME - Please Wait!",0,12,TextMenusR,TextMenusG,TextMenusB)
			CreateDir GlobalDirName$+"\custom\Editing\Profiles\"+EditorUserNameEntered$

			EditorUserName$=EditorUserNameEntered$			
			file=WriteFile(GlobalDirName$+"\custom\Editing\Profiles\currentuser.dat")
			WriteString file,EditorUserName$
			CloseFile file

			StartAdventureSelectScreen()
		EndIf
		waitflag=True
	EndIf
	
	If MouseDown(1)
		If FileType(GlobalDirName$+"\custom\Editing\Profiles\"+EditorUserNamesListed$(EditorUserNameSelected))=2 And EditorUserNamesListed$(EditorUserNameSelected)<>""
			EditorUserName$=EditorUserNamesListed$(EditorUserNameSelected)
			file=WriteFile(GlobalDirName$+"\custom\Editing\Profiles\currentuser.dat")
			WriteString file,EditorUserName$
			CloseFile file
			StartAdventureSelectScreen()
			DisplayText2("---->",0,15+EditorUserNameSelected,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2("<----",39,15+EditorUserNameSelected,TextMenusR,TextMenusG,TextMenusB)
			waitflag=True
		EndIf
	EndIf


	

	
	RenderLetters()
	UpdateWorld 
	RenderWorld
	
	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf

	Flip
	
	If waitflag=True Delay 2000


End Function

Function StartAdventureSelectScreen()

	SetEditorMode(5)
	CameraProjMode Camera1,0
	CameraProjMode Camera2,0
	CameraProjMode Camera3,0
	CameraProjMode Camera4,0
	CameraProjMode Camera,1
	
	If AdventureCurrentArchive=0
		GetCurrentAdventures()
	Else
		GetArchiveAdventures()
	EndIf
	
	
	AdventureNameEntered$=""
	


End Function

Function AdventureSelectScreen()

	leveltimer=leveltimer+1

	mx=MouseX()
	my=MouseY()
	If mY>=165 And my<544 And mx>50 And mx<748
		AdventureNameSelected=(my-165)/20
	Else
		AdventureNameSelected=-1
	EndIf


	DisplayText2(Versiontext$,0,0,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("================================",0,1,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("            ====================",0,1,TextMenusR,TextMenusG,TextMenusB)
	If displayfullscreen=True
		DisplayText2("                                (FullScreen)",0,1,255,255,255)
	Else
		DisplayText2("                                ( Windowed )",0,1,255,255,255)
	EndIf
	;hubmode=True
	If hubmode=True
		DisplayText2("(   Hubs   )",0,1,255,255,255)
		DisplayText2("Enter New Hub Filename (e.g. 'TestHub12345')",0,3,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("(Adventures)",0,1,255,255,255)
		DisplayText2("Enter New Adventure Filename (e.g. 'Test34')",0,3,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	
	
	If leveltimer Mod 100 <50
		DisplayText2(":",Len(AdventureNameEntered$),4,255,255,255)
	EndIf
	DisplayText2(AdventureNameEntered$,0,4,255,255,255)

	
	DisplayText2("Or Select Existing To Edit:                 ",0,6,TextMenusR,TextMenusG,TextMenusB)
	If hubmode=True
		DisplayText2("===================================================",0,7,TextMenusR,TextMenusG,TextMenusB)
	Else
		If AdventureCurrentArchive=0
			DisplayText2("Current",28,6,255,255,255)
			DisplayText2("Archive",37,6,155,155,155)
		Else
			DisplayText2("Current",28,6,155,155,155)
			DisplayText2("Archive",37,6,255,255,255)
		EndIf
		DisplayText2("/",35.5,5.9,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("============================================",0,7,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	If NofAdventureFileNames>19
		For i=0 To 18
			displaytext2(":",2,8+i,TextMenusR,TextMenusG,TextMenusB)
			displaytext2(":",41,8+i,TextMenusR,TextMenusG,TextMenusB)
		Next
		DisplayText2("--",0,8,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",0,9,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Up",0,10,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,11,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",0,24,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Dn",0,25,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,26,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2("--",42,8,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",42,9,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Up",42,10,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",42,11,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2("--",42,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",42,24,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Dn",42,25,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",42,26,TextMenusR,TextMenusG,TextMenusB)
	EndIf

	If AdventureNameSelected>=AdventureFileNamesListedStart+NofAdventureFileNames
		AdventureNameSelected=-1
	EndIf

	For i=0 To 18
		If AdventureFileNamesListedStart+i<NofAdventureFileNames
			If i=AdventureNameSelected
				DisplayText2(AdventureFileNamesListed$(AdventureFileNamesListedStart+i),22-Len(AdventureFileNamesListed$(AdventureFileNamesListedStart+i))/2,8+i,255,255,255)
			Else
				DisplayText2(AdventureFileNamesListed$(AdventureFileNamesListedStart+i),22-Len(AdventureFileNamesListed$(AdventureFileNamesListedStart+i))/2,8+i,155,155,155)
			EndIf
		EndIf
	Next


	DisplayText2("============================================",0,27,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("User:",0,28,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(EditorUserName$,6,28,255,255,255)
	DisplayText2("(CHANGE)",36,28,TextMenusR,TextMenusG,TextMenusB)
	
	
	; Entering New Name
	let=GetKey()
	If let>=32 And let<=122 And Len(AdventureNameEntered$)<38
		AdventureNameEntered$=AdventureNameEntered$+Chr$(let)
	EndIf
	If KeyDown(14)
		; backspace
		If Len(AdventureNameEntered$)>0
			AdventureNameEntered$=Left$(AdventureNameEntered$,Len(AdventureNameEntered$)-1)
			Delay CharacterDeleteDelay
		EndIf
	EndIf
	If KeyDown(211)
		; delete
		AdventureNameEntered$=""
		Delay CharacterDeleteDelay
	EndIf
	If KeyDown(28) Or KeyDown(156)
		; Enter
		If hubmode
			If AdventureNameEntered$=""
				DisplayText2(" INVALID HUB NAME - Empty Name!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GlobalDirName$+"\Custom\Editing\Hubs\"+AdventureNameEntered$)=2 
				DisplayText2(" INVALID HUB NAME - Already Exists!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else
				DisplayText2("--> STARTING HUB EDITOR - Please Wait!",0,5,TextMenusR,TextMenusG,TextMenusB)
				CreateDir GlobalDirName$+"\Custom\Editing\Hubs\"+AdventureNameEntered$
	
				HubFileName$=AdventureNameEntered$
				
				GetHubs()
				
				For i=0 To NofAdventureFileNames-1	
					If HubFileName$=AdventureFileNamesListed$(i)
						AdventureNameSelected=i	
						Repeat
						Until MouseDown(1)=0
						StartHub()
					EndIf
				Next
				
			EndIf
		Else
			If AdventureNameEntered$=""
				DisplayText2(" INVALID ADVENTURE NAME - Empty Name!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GlobalDirName$+"\Custom\Editing\Current\"+AdventureNameEntered$)=2 
				DisplayText2(" INVALID ADVENTURE NAME - Already Exists!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GlobalDirName$+"\Custom\Editing\Archive\"+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Archive!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else
				DisplayText2("--> STARTING MAIN EDITOR - Please Wait!",0,5,TextMenusR,TextMenusG,TextMenusB)
				CreateDir GlobalDirName$+"\Custom\Editing\Current\"+AdventureNameEntered$
	
				AdventureFileName$=AdventureNameEntered$
				
				GetCurrentAdventures()
				
				For i=0 To NofAdventureFileNames-1	
					If AdventureFileName$=AdventureFileNamesListed$(i)
						AdventureNameSelected=i	
						Repeat
						Until MouseDown(1)=0
						StartMaster()
					EndIf
				Next
			
			
				
			EndIf
		EndIf
		waitflag=True
	EndIf
	
	
	
	
	If MouseDown(1)
		If mx>650 And my>560
			; change user
			StartUserSelectScreen()
			Repeat
			Until MouseDown(1)=0
		EndIf
		
		If my<50 And mx>650
			; switch window/fullscreen
			DisplayFullScreen = Not DisplayFullScreen
			filed=WriteFile (globaldirname$+"\display-ed.wdf")
			If filed>0
			
				WriteInt filed,DisplayFullScreen
				CloseFile filed
			EndIf
			
			; and restart
			Cls
			Flip
			Print "Note: Screenmode will be switched upon next restart."
			Delay 4000
			 
			
		EndIf

		
		If my>123 And my<143 And mx>507 And mx<650 And AdventureCurrentArchive=1 And hubmode=False
			GetCurrentAdventures()
		EndIf
		If my>123 And my<143 And mx>650 And AdventureCurrentArchive=0 And hubmode=False
			GetArchiveAdventures()
		EndIf

		If mx<220 And my>23 And my<43 ;hubmode
			hubmode=Not hubmode
			If hubmode
				GetHubs()
			Else
				GetCurrentAdventures()
			EndIf
			Repeat
			Until MouseDown(1)=0
		EndIf
		
		If AdventureNameSelected>=0
		
			Repeat
			Until MouseDown(1)=0
			SetEditorMode(6)
		EndIf

	EndIf

	If my>165 And my<545
		If MouseScroll<>0
			Speed=1
			If ShiftDown()
				Speed=10
			EndIf
			AdventureFileNamesListedStart=AdventureFileNamesListedStart-MouseScroll*Speed
		EndIf
		If (mx<50 Or mx>748) And MouseDown(1)
			If my>175 And my<235
				; Page Up
				AdventureFileNamesListedStart=AdventureFileNamesListedStart-19
				Repeat
				Until MouseDown(1)=0
			ElseIf my>475 And my<535
				; Page Down
				AdventureFileNamesListedStart=AdventureFileNamesListedStart+19
				Repeat
				Until MouseDown(1)=0
			EndIf
		EndIf
		If AdventureFileNamesListedStart>NofAdventureFileNames-19 Then AdventureFileNamesListedStart=NofAdventureFileNames-19
		If AdventureFileNamesListedStart<0 Then AdventureFileNamesListedStart=0
	EndIf
		
	
	RenderLetters()
	UpdateWorld 
	RenderWorld
	
	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf
	Flip
	If waitflag=True Delay 2000
	

	
End Function
Function AdventureSelectScreen2()

	MX=MouseX()
	my=MouseY()
	If mx>300 And mx<500 And my>175 And my<175+160
		Selected=(my-175)/40
	Else 
		Selected=-1
	EndIf

	DisplayText2(Versiontext$,0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("============================================",0,1,TextMenusR,TextMenusG,TextMenusB)

	If hubmode
		DisplayText2("               Hub Selected:",0.5,3,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("             Adventure Selected:",0,3,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	
	AdventureFileName$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
	DisplayText2(AdventureFileName$,22-Len(AdventureFileName$)/2,4,255,255,255)
	
	If Not hubmode
		If MasterFileExists()
			LoadMasterFile()
			DisplayText2(AdventureTitle$,22-Len(AdventureTitle$)/2,5,255,255,255)
		EndIf
	EndIf
	
		
	DisplayText2("               Choose Option:",0,6,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("============================================",0,7,TextMenusR,TextMenusG,TextMenusB)

	If AdventureCurrentArchive=0

		If Selected=0
			DisplayText2("EDIT",20,9,255,255,255)
		Else
			DisplayText2("EDIT",20,9,155,155,155)
		EndIf
	EndIf
	If AdventureCurrentArchive=0 And hubmode=False
		If Selected=1 
			DisplayText2("MOVE TO ARCHIVE",14.5,11,255,255,255)
		Else
			DisplayText2("MOVE TO ARCHIVE",14.5,11,155,155,155)
		EndIf
	EndIf
	If AdventureCurrentArchive=1 And hubmode=False
		If Selected=1 And AdventureCurrentArchive=1
			DisplayText2("MOVE TO CURRENT",14.5,11,255,255,255)
		Else
			DisplayText2("MOVE TO CURRENT",14.5,11,155,155,155)
		EndIf
	EndIf
	
	If AdventureCurrentArchive=0

		If Selected=2
			DisplayText2("DELETE",19,13,255,255,255)
		Else
			DisplayText2("DELETE",19,13,155,155,155)
		EndIf
	EndIf
	If Selected=3
		DisplayText2("CANCEL",19,15,255,255,255)
	Else
		DisplayText2("CANCEL",19,15,155,155,155)
	EndIf

	If MouseDown(1)
		If selected=0 And AdventureCurrentArchive=0
			If hubmode
				HubFileName$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
				StartHub()
			Else
				AdventureFileName$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
				MasterDialogListStart=0
				MasterLevelListStart=0
				StartMaster()
			EndIf
			Repeat
			Until MouseDown(1)=0
		EndIf

		If selected=1
			ex$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
			If adventurecurrentarchive=0
				CreateDir GlobalDirName$+"\Custom\Editing\Archive\"+ex$
				dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Current\"+ex$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						CopyFile GlobalDirName$+"\Custom\Editing\Current\"+ex$+"\"+ex2$,GlobalDirName$+"\Custom\Editing\Archive\"+ex$+"\"+ex2$
						DeleteFile GlobalDirName$+"\Custom\Editing\Current\"+ex$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile
				DeleteDir GlobalDirName$+"\Custom\Editing\Current\"+ex$
				GetCurrentAdventures()
			Else
				CreateDir GlobalDirName$+"\Custom\Editing\Current\"+ex$
				dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Archive\"+ex$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						CopyFile GlobalDirName$+"\Custom\Editing\Archive\"+ex$+"\"+ex2$,GlobalDirName$+"\Custom\Editing\Current\"+ex$+"\"+ex2$
						DeleteFile GlobalDirName$+"\Custom\Editing\Archive\"+ex$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile
				DeleteDir GlobalDirName$+"\Custom\Editing\Archive\"+ex$
				GetArchiveAdventures()
			EndIf
			SetEditorMode(5)
			Repeat
			Until MouseDown(1)=0
		EndIf
		If selected=2 And AdventureCurrentArchive=0
			SetEditorMode(7)
			Repeat
			Until MouseDown(1)=0
		EndIf

		If selected=3
			SetEditorMode(5)
			Repeat
			Until MouseDown(1)=0
		EndIf
	EndIf
			
	RenderLetters()
	UpdateWorld 
	RenderWorld
	
	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf
	Flip
	If waitflag=True Delay 2000

	
End Function

Function AdventureSelectScreen3()

	MX=MouseX()
	my=MouseY()
	If mx>300 And mx<500 And my>175 And my<175+80
		Selected=(my-175)/40
	Else 
		Selected=-1
	EndIf


	DisplayText2(Versiontext$,0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("============================================",0,1,TextMenusR,TextMenusG,TextMenusB)


	If hubmode
		DisplayText2("               Hub Selected:",0.5,3,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("             Adventure Selected:",0,3,TextMenusR,TextMenusG,TextMenusB)
	EndIf	
	DisplayText2(AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart),22-Len(AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart))/2,4,255,255,255)
	
	If hubmode
		DisplayText2("         DELETE HUB - ARE YOU SURE?",0,6,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("      DELETE ADVENTURE - ARE YOU SURE?",0,6,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	DisplayText2("============================================",0,7,TextMenusR,TextMenusG,TextMenusB)
	
	If Selected=0
		DisplayText2("YES!",20,9,255,255,255)
	Else
		DisplayText2("YES!",20,9,155,155,155)
	EndIf
	If Selected=1
		DisplayText2("NO!!",20,11,255,255,255)
	Else
		DisplayText2("NO!!",20,11,155,155,155)
	EndIf
	

	If MouseDown(1)
		If selected=0
			ex$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
			If hubmode
				dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Hubs\"+ex$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						DeleteFile GlobalDirName$+"\Custom\Editing\Hubs\"+ex$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile
				DeleteDir GlobalDirName$+"\Custom\Editing\Hubs\"+ex$
				GetHubs()
			ElseIf adventurecurrentarchive=0
				dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Current\"+ex$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						DeleteFile GlobalDirName$+"\Custom\Editing\Current\"+ex$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile
				DeleteDir GlobalDirName$+"\Custom\Editing\Current\"+ex$
				GetCurrentAdventures()
			Else
				dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Archive\"+ex$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						DeleteFile GlobalDirName$+"\Custom\Editing\Archive\"+ex$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile
				DeleteDir GlobalDirName$+"\Custom\Editing\Archive\"+ex$
				GetArchiveAdventures()
			EndIf

			
			Repeat
			Until MouseDown(1)=0
			SetEditorMode(5)
		EndIf
		If selected=1
			SetEditorMode(5)
			Repeat
			Until MouseDown(1)=0
		EndIf
	EndIf

	RenderLetters()
	UpdateWorld 
	RenderWorld
	
	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf
	Flip
	If waitflag=True Delay 2000

	
End Function


Function GetCurrentAdventures()

	NofAdventureFileNames=0
	AdventureFileNamesListedStart=0
	AdventureCurrentArchive=0
	
	dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Current")
	
	Repeat
		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(GlobalDirName$+"\Custom\Editing\Current\"+ex$)=2
			; check if there's a hash or name is too long
			flag=True
			For i=1 To Len(ex$)
				If Mid$(ex$,i,1)="#" Then flag=False
			Next
			If Len(ex$)>38 Then flag=False
			If flag=True
				; good file name - add to list
				AdventureFileNamesListed$(NofAdventureFileNames)=ex$
				NofAdventureFileNames=NofAdventureFileNames+1
			EndIf
		EndIf
	Until ex$=""
	
	CloseDir dirfile


End Function

Function GetArchiveAdventures()
	
	NofAdventureFileNames=0
	AdventureFileNamesListedStart=0
	AdventureCurrentArchive=1
	
	dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Archive")
	
	Repeat
		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(GlobalDirName$+"\Custom\Editing\Archive\"+ex$)=2
			; check if there's a hash or name is too long
			flag=True
			For i=1 To Len(ex$)
				If Mid$(ex$,i,1)="#" Then flag=False
			Next
			If Len(ex$)>38 Then flag=False
			If flag=True
				; good file name - add to list
				AdventureFileNamesListed$(NofAdventureFileNames)=ex$
				NofAdventureFileNames=NofAdventureFileNames+1
			EndIf
		EndIf
	Until ex$=""
	
	CloseDir dirfile



End Function

Function GetHubs()

	NofAdventureFileNames=0
	AdventureFileNamesListedStart=0
	AdventureCurrentArchive=0
	
	dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Hubs")
	
	Repeat
		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(GlobalDirName$+"\Custom\Editing\Hubs\"+ex$)=2
			; check if there's a hash or name is too long
			flag=True
			For i=1 To Len(ex$)
				If Mid$(ex$,i,1)="#" Then flag=False
			Next
			If Len(ex$)>38 Then flag=False
			If flag=True
				; good file name - add to list
				AdventureFileNamesListed$(NofAdventureFileNames)=ex$
				NofAdventureFileNames=NofAdventureFileNames+1
			EndIf
		EndIf
	Until ex$=""
	
	CloseDir dirfile


End Function

Function LevelExists(levelnumber)

	If AdventureCurrentArchive=1
		ex$="Archive\"
	Else
		ex$="Current\"
	EndIf

	If FileType(GlobalDirName$+"\Custom\Editing\"+ex$+AdventureFileName$+"\"+Str$(levelnumber)+".wlv")=1
		Return True
	Else
		Return False
	EndIf

End Function

Function DialogExists(dialognumber)

	If AdventureCurrentArchive=1
		ex$="Archive\"
	Else
		ex$="Current\"
	EndIf

	If FileType(GlobalDirName$+"\Custom\Editing\"+ex$+AdventureFileName$+"\"+Str$(dialognumber)+".dia")=1
		Return True
	Else
		Return False
	EndIf

End Function

Function StartMaster()
	SetEditorMode(8)
	
	CopyingLevel=False
	
	CameraProjMode Camera1,0
	CameraProjMode Camera2,0
	CameraProjMode Camera3,0
	CameraProjMode Camera4,0
	CameraProjMode Camera,1
	
	
	If AdventureCurrentArchive=1
		ex$="Archive\"
	Else
		ex$="Current\"
	EndIf
	
	For i=1 To MaxLevel
		; check existence of wlv and dia files
		MasterLevelList(i)=0
		If LevelExists(i)
			MasterLevelList(i)=1
		EndIf
	Next
	
	For i=1 To MaxDialog
		MasterDialogList(i)=0
		If DialogExists(i)
			MasterDialogList(i)=1
		EndIf
	Next
	
	; check existence of a master.dat file
	If IconTextureCustom>0 
		FreeTexture IconTextureCustom
		IconTextureCustom=0
	EndIf
	If FileType(globaldirname$+"\Custom\editing\"+ex$+AdventureFileName$+"\master.dat")=1
		LoadMasterFile()
		If customiconname$="Standard"
			IconTextureCustom=myLoadTexture("data\Graphics\icons-custom.bmp",4)
		Else
			IconTextureCustom=myLoadTexture(globaldirname$+"\Custom\Icons\icons "+customiconname$+".bmp",4)
		EndIf
	Else
		CustomIconName$="Standard"
		CustomMapName$=""
		IconTextureCustom=myLoadTexture("data\Graphics\icons-custom.bmp",4)
		AdventureStartX=1
		AdventureStartY=1 ; x/y position of player start
		AdventureStartDir=0
		AdventureTitle$=""
		For i=0 To 4
			AdventureTextLine$(i)=""
		Next
		MasterDialogListStart=0
		MasterLevelListStart=0
		AdventureExitWonLevel=0
		AdventureExitWonX=0
		AdventureExitWonY=0 ; at what hub level and x/y do you reappear if won.
		AdventureExitLostLevel=0
		AdventureExitLostX=0
		AdventureExitLostY=0; at what hub level And x/y do you reappear If won.
		AdventureGoal=0	; when is adventure done
							; 1-NofWeeStinkersInAdventure=0
		For i=0 To 2
			For j=0 To 5
				AdventureWonCommand(i,j)=0
			Next	; 3 commands, each with level/command/fourdata
		Next
		
		StarterItems=7 ;to do: decide the default value for new adventures
		WidescreenRange=0
		
		SelectedShard=0
		SelectedGlyph=0
		CustomShardEnabled=0
		CustomGlyphEnabled=0
		For i=0 To NoOfShards-1
			For j=0 To 4
				CustomShardCMD(i,j)=0
			Next
		Next
		For i=0 To NoOfGlyphs-1
			For j=0 To 4
				CustomGlyphCMD(i,j)=0
			Next
		Next
		
	EndIf


End Function

Function StartHub()
	SetEditorMode(11)
	HubAdvStart=0
	HubSelectedAdventure=-1
	If FileType(globaldirname$+"\Custom\editing\Hubs\"+HubFileName$+"\hub.dat")=1
		LoadHubFile()
	Else
		HubTitle$=""
		HubDescription$=""
		HubTotalAdventures=0
	EndIf
End Function

Function ResumeMaster()

	SetEditorMode(8)
	
	CopyingLevel=False
	
	CameraProjMode Camera1,0
	CameraProjMode Camera2,0
	CameraProjMode Camera3,0
	CameraProjMode Camera4,0
	CameraProjMode Camera,1

	
	For i=1 To MaxLevel
		; check existence of wlv and dia files
		MasterLevelList(i)=0
		If LevelExists(i)
			MasterLevelList(i)=1
		EndIf
	Next
	For i=1 To MaxDialog
		
		MasterDialogList(i)=0
		If DialogExists(i)
			MasterDialogList(i)=1
		EndIf
	Next

	
	
End Function

Function MasterMainLoop()
	
	If (KeyDown(157) Or KeyDown(29)) And KeyDown(20)
		StartTestMode()
	EndIf
	
	dialogtimer=dialogtimer+1
	adj=1
	If KeyDown(42) Or KeyDown(54) Then adj=10
	
	DisplayText2("Adventure File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(AdventureFileName$,0,1,255,255,255)
	If MouseY()<22 And  MouseX()>430 And MouseX()<700
		DisplayText2("                        (Adv. Options)",0,0,255,255,255)
	Else
		DisplayText2("                        (Adv. Options)",0,0,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	
	If MOdified>=0
	
		For i=0 To 37
		
			AddLetter(Asc("X")-32,-.97+i*.045,.5-0*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
			For j=0 To 4
				AddLetter(Asc("X")-32,-.97+i*.045,.5-(3+j)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
			Next
			
		
				
	
		Next
		
		For i=0 To 25
			DisplayText2(":",38,i,TextMenusR,TextMenusG,TextMenusB)
		Next
		DisplayText2("EDIT",39.5,0,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("LV DG",39,1,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("-----",39,2,TextMenusR,TextMenusG,TextMenusB)
		
		If MouseX()>700 And MouseX()<750 And MouseY()>62 And MouseY()<82
			DisplayText2("-",39.5,3,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("-",39.5,3,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf 
		If MouseX()>750 And MouseX()<800 And MouseY()>62 And MouseY()<82
			DisplayText2("-",42.5,3,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("-",42.5,3,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf
		
		
		DigitSpaceMult#=0.8
		For i=1 To 20
			flag=False
			If MouseX()>700 And MouseX()<750 And MouseY()>62+i*20 And MouseY()<=82+i*20
				flag=True
			EndIf
			
			If i+MasterLevelListStart<10
				ex$="00"+Str$(i+MasterLevelListStart)
			Else If i+MasterLevelListStart<100
				ex$="0"+Str$(i+MasterLevelListStart)
			Else
				ex$=Str$(i+MasterLevelListStart)
			EndIf
		
			If flag=True ; mouse over
				r=255
				g=255
				b=100
			Else If MasterLevelList(i+MasterLevelListStart)=0
				r=100
				g=100
				b=100
			Else If CopyingLevel And CopiedLevel=i+MasterLevelListStart
				r=0
				g=255
				b=255
			Else
				r=210
				g=210
				b=210
			EndIf
			DisplayText2(ex$,38.7,3+i,r,g,b,DigitSpaceMult) ; previously, x=39
			
			flag=False
			If MouseX()>750 And MouseX()<800 And MouseY()>62+i*20 And MouseY()<=82+i*20
				flag=True
			EndIf
			
			If i+MasterDialogListStart<10
				ex$="00"+Str$(i+MasterDialogListStart)
			Else If i+MasterDialogListStart<100
				ex$="0"+Str$(i+MasterDialogListStart)
			Else
				ex$=Str$(i+MasterDialogListStart)
			EndIf
			
			If flag=True
				r=255
				g=255
				b=100
			Else If MasterDialogList(i+MasterDialogListStart)=0
				r=100
				g=100
				b=100
			Else
				r=210
				g=210
				b=210
			EndIf
			DisplayText2(ex$,41.8,3+i,r,g,b,DigitSpaceMult) ; previously, x=41.5
	
		Next
		
		If MouseX()>700 And MouseX()<750 And MouseY()>482 And MouseY()<502
			DisplayText2("+",39.5,24,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("+",39.5,24,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf 
		If MouseX()>750 And MouseX()<800 And MouseY()>482 And MouseY()<502
			DisplayText2("+",42.5,24,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("+",42.5,24,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf

		displayText2("-----",39,25,TextMenusR,TextMenusG,TextMenusB)
		
	
		DisplayText2("--------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)
	
		
		DisplayText2("Adventure Title:",0,3,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2(AdventureTitle$,0,4,255,255,255)
		DisplayText2("--------------------------------------",0,5,TextMenusR,TextMenusG,TextMenusB)
		

		
		DisplayText2("Introductory Text:",0,6,TextMenusR,TextMenusG,TextMenusB)
		For i=0 To 4
			DisplayText2(AdventureTextline$(i),0,7+i,255,255,255)
		Next
		DisplayText2("--------------------------------------",0,12,TextMenusR,TextMenusG,TextMenusB)
	
		DisplayText2("Starting Coord. (Lv 01)",0,13,TextMenusR,TextMenusG,TextMenusB)
		displaytext2("X:      Y:      Dir:",0,14,TextMenusR,TextMenusG,TextMenusB)
		displaytext2(Str$(adventurestartx),2,14,255,255,255)
		displaytext2(Str$(adventurestarty),10,14,255,255,255)
		displaytext2(Str$((adventurestartdir+180) Mod 360),20,14,255,255,255)

		DisplayText2("--------------------------------------",0,15,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2("Winning Condition:",0,16,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(WinningCondition$(AdventureGoal),0,17,255,255,255)
		DisplayText2("--------------------------------------",0,18,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2(":Gate/Keys",25,13,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(":Version #",25,14,255,255,255)
		DisplayText2(Str$(GateKeyVersion),35,14,255,255,255)

		
		DisplayText2(":Custom Icons",25,16,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(":",25,17,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(Left$(CustomIconName$,12),26,17,255,255,255)
	; PUT BACK IN FOR ME
	
		If MASTERUSER=True
			DisplayText2("Hub Commands:",0,19,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2("WonExit Lv    X    Y",0,20,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(Str$(adventureexitwonlevel),10,20,255,255,255)
			DisplayText2(Str$(adventureexitwonx),15,20,255,255,255)
			DisplayText2(Str$(adventureexitwony),20,20,255,255,255)
			DisplayText2("LostExt Lv    X    Y",0,21,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(Str$(adventureexitlostlevel),10,21,255,255,255)
			DisplayText2(Str$(adventureexitlostx),15,21,255,255,255)
			DisplayText2(Str$(adventureexitlosty),20,21,255,255,255)
			For i=0 To 2
				Displaytext2("WonCMD: Lv    C    D    D    D    D",0,22+i,TextMenusR,TextMenusG,TextMenusB)
				For j=0 To 5
					Displaytext2(Str$(adventurewoncommand(i,j)),10+j*5,22+i,255,255,255)
				Next
			Next
		EndIf
		DisplayText2("--------------------------------------",0,25,TextMenusR,TextMenusG,TextMenusB)
	
		
		DisplayText2("========== ========== ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2(":        : :        : :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2(":        : :        : :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("========== ========== ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		
		If hubmode
			DisplayText2("                                 ==========",0.5,26,50,50,0)
			DisplayText2("                                 :        :",0.5,27,50,50,0)
			DisplayText2("                                 :        :",0.5,28,50,50,0)
			DisplayText2("                                 ==========",0.5,29,50,50,0)
		Else
			DisplayText2("                                 ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("                                 :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("                                 :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("                                 ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		EndIf
		
	

	If MouseY()>550 And MouseX()<200
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>550 And MouseX()>200 And MouseX()<400		
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	
	If MouseY()>550 And MouseX()>400 And MouseX()<600		
		DisplayText2(" SAVE",24.5,27,255,255,255)
		DisplayText2("+TEST",25,28,255,255,255)
	Else
		DisplayText2(" SAVE",24.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+TEST",25,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	If hubmode
		DisplayText2("COMPILE",35,27,50,50,0)
		DisplayText2("+EXIT",36,28,50,50,0)
	Else
		If MouseY()>550 And MouseX()>600
			DisplayText2("COMPILE",35,27,255,255,255)
			DisplayText2("+EXIT",36,28,255,255,255)
		Else
			DisplayText2("COMPILE",35,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("+EXIT",36,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		EndIf
	EndIf

		
			
		; Mouse
		
		If MouseX()<>OldMouseX Or MouseY()<>OldMouseY
			ShowPointer
		EndIf
		OldMouseX=MouseX()
		OldMouseY=MouseY()
		; Mouse Pos
		Entering=0
		
		x=MouseX()/18
		y=(MouseY()-84)/21
	
		debug1=MouseY()
		debug2=y
		
		; cursor
		If x<38 And MouseY()>=84 And y>2 And y<8 
			Entering=1
			If x>Len(AdventureTextLine$(y-3)) Then x=Len(AdventureTextLine$(y-3))
			If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
				AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
			EndIf
		EndIf	
		If x<38 And y=0
			Entering=2
			If x>Len(AdventureTitle$) Then x=Len(AdventureTitle$)
			If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
				AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
			EndIf
		EndIf
	; PUT BACK IN FOR ME - (HELP LINES _ DON"T NEED)
	;	If x<10 And y>9 And y<13 
	;		Entering=3
	;		If x>Len(AdventureHelpLine$(y-10)) Then x=Len(AdventureHelpLine$(y-10))
	;		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
	;			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
	;		EndIf
	;	EndIf	

		

	
		OldX=x
		OldY=y
		; entering text
		let=GetKey()
		If Entering>0
		
			Select entering
			Case 1
				tex$=AdventureTextline$(y-3)
			Case 2
				tex$=AdventureTitle$
	; PUT BACK IN FOR ME - HELP LINE (DON"T NEED)
	;		Case 3
	;			tex$=AdventureHelpLine$(y-10)
				
			End Select
			
			If let>=32 And let<=122
				; place letter
				tex$=Left$(tex$,x)+Chr$(let)+Right$(tex$,Len(tex$)-x)
				tex$=Left$(tex$,38)
				; and advance cursor
				If x<37
					MoveMouse (x+1)*18,84+y*21
					OldMouseX=MouseX()
					HidePointer()
				EndIf
				ColEffect=-1
				TxtEffect=-1

			EndIf
			If KeyDown(14)
				; backspace
				If x>0 
					tex$=Left$(tex$,x-1)+Right$(tex$,Len(tex$)-x)
					MoveMouse (x-1)*18,84+y*21
					OldMouseX=MouseX()
					HidePointer()
					Delay CharacterDeleteDelay
				EndIf
				ColEffect=-1
				TxtEffect=-1

			EndIf
			If KeyDown(211)
				; delete
				If x<Len(tex$) 
					tex$=Left$(tex$,x)+Right$(tex$,Len(tex$)-x-1)
					HidePointer
					Delay CharacterDeleteDelay
				EndIf
				ColEffect=-1
				TxtEffect=-1

			EndIf
			
			Select entering
			Case 1
				AdventureTextline$(y-3)=tex$
			Case 2
				AdventureTitle$=tex$
		;	Case 3
		;		AdventureHelpLine$(y-10)=tex$
				
			End Select
			
			; cursor movement
			If (KeyDown(200) Or KeyDown(72)) 
				If (y>3 And y<8) Or y=11 Or y=12 
					MoveMouse (x+0)*18,84+(y-1)*21
				Else If y=0
					MoveMouse (x+0)*18,84+12*21
				Else If y=3
					MoveMouse x*18,84+0*21
				Else If y=10
					MoveMouse x*18,84+7*21
				EndIf
				OldMouseY=MouseY()
				HidePointer()
				Delay 100
				ColEffect=-1
				TxtEffect=-1

		
			EndIf
			If (KeyDown(208) Or KeyDown(28) Or KeyDown(156)) 
				If (y>2 And y<7) Or y=10 Or y=11
					MoveMouse (x+0)*18,84+(y+1)*21
				Else If y=0
					MoveMouse (x+0)*18,84+3*21
				Else If y=7
					MoveMouse x*18,84+10*21
				Else If y=12
					MoveMouse x*18,84
				EndIf
				OldMouseY=MouseY()
				HidePointer()
				Delay 100
				ColEffect=-1
				TxtEffect=-1

	
			EndIf
			If (KeyDown(203)) And x>0
				MoveMouse (x-1)*18,84+(y-0)*21
				OldMouseX=MouseX()
				HidePointer()
				Delay 100
				ColEffect=-1
				TxtEffect=-1

	
			EndIf
			If (KeyDown(205)) And x<Len(tex$)
				MoveMouse (x+1)*18,84+(y-0)*21
				OldMouseX=MouseX()
				HidePointer()
				Delay 100
				ColEffect=-1
				TxtEffect=-1

	
			EndIf
			
			
			

	
	
		EndIf
		

	EndIf
	
	mb=0
	If MouseDown(1) mb=1
	If MouseDown(2) mb=2
	
	; level/dialog list start
	If MouseX()>700 And MouseX()<750
		If (MouseY()>482 And MouseY()<502 And mb>0) Or MouseScroll<0
			MasterLevelListStart=MasterLevelListStart+adj
			If MasterLevelListStart>MaxLevel-20 Then MasterLevelListStart=MaxLevel-20
			If MouseScroll=0 Then Delay 150
		Else If (MouseY()>62 And MouseY()<82 And mb>0) Or MouseScroll>0
			MasterLevelListStart=MasterLevelListStart-adj
			If MasterLevelListStart<0 Then MasterLevelListStart=0
			If MouseScroll=0 Then Delay 150
		EndIf
	EndIf
	If MouseX()>750 And MouseX()<800
		If (MouseY()>482 And MouseY()<502 And mb>0) Or MouseScroll<0
			MasterDialogListStart=MasterDialogListStart+adj
			If MasterDialogListStart>MaxDialog-20 Then MasterDialogListStart=MaxDialog-20
			If MouseScroll=0 Then Delay 150
		Else If (MouseY()>62 And MouseY()<82 And mb>0) Or MouseScroll>0
			MasterDialogListStart=MasterDialogListStart-adj
			If MasterDialogListStart<0 Then MasterDialogListStart=0
			If MouseScroll=0 Then Delay 150
		EndIf
	EndIf
	
	
	If mb>0
	
		;new advanced mode 2019
		If MouseY()<22 And  MouseX()>430 And MouseX()<700
			SetEditorMode(10)
			Repeat
			Until MouseDown(1)=0 And MouseDown(2)=0
		EndIf
		; Change Adventure
	
		; change textures
	
		; startpos
		If MouseY()>365-80 And MouseY()<385-80
			If MouseX()>00 And MouseX()<100
				If mb=1 adventurestartx=adventurestartx+adj
				If mb=2 adventurestartx=adventurestartx-adj
				Delay 150
			EndIf
			If MouseX()>160 And MouseX()<260
				If mb=1 adventurestarty=adventurestarty+adj
				If mb=2 adventurestarty=adventurestarty-adj
				Delay 150
			EndIf
			If MouseX()>300 And MouseX()<440
				If mb=1 adventurestartdir=adventurestartdir+45
				If mb=2 adventurestartdir=adventurestartdir-45
				If adventurestartdir>=360 Then adventurestartdir=adventurestartdir-360
				If adventurestartdir<0 Then adventurestartdir=adventurestartdir+360
				Delay 150
			EndIf

		EndIf
		
		
		
			


	; PUT BACK IN FOR ME
		If MASTERUSER=True
			; change hub data
			For i=0 To 5
				For j=0 To 4
					If MouseX()>160+i*90-18 And MouseX()<240+i*90-18 And MouseY()>365+(j+2)*21 And MouseY()<386+(j+2)*21
						If j=0 And i=0
							If mb=1 Adventureexitwonlevel=Adventureexitwonlevel+adj
							If mb=2 Adventureexitwonlevel=Adventureexitwonlevel-adj
	
						EndIf
						If j=0 And i=1
							If mb=1 Adventureexitwonx=Adventureexitwonx+adj
							If mb=2 Adventureexitwonx=Adventureexitwonx-adj
						EndIf
						If j=0 And i=2
							If mb=1 Adventureexitwony=Adventureexitwony+adj
							If mb=2 Adventureexitwony=Adventureexitwony-adj
						EndIf
						If j=1 And i=0
							If mb=1 Adventureexitlostlevel=Adventureexitlostlevel+adj
							If mb=2 Adventureexitlostlevel=Adventureexitlostlevel-adj
						EndIf
						If j=1 And i=1
							If mb=1 Adventureexitlostx=Adventureexitlostx+adj
							If mb=2 Adventureexitlostx=Adventureexitlostx-adj
						EndIf
						If j=1 And i=2
							If mb=1 Adventureexitlosty=Adventureexitlosty+adj
							If mb=2 Adventureexitlosty=Adventureexitlosty-adj
						EndIf
						If j=2 Or j=3 Or j=4
							If mb=1 Adventurewoncommand(j-2,i)=Adventurewoncommand(j-2,i)+adj
							If mb=2 Adventurewoncommand(j-2,i)=Adventurewoncommand(j-2,i)-adj
						EndIf
						
						Delay 150
					EndIf
					
				Next
			Next
		
		
		
		
		EndIf
		
		
		; adventure goal
		If MouseY()>500+32-200 And MouseY()<523+42-200 And MouseX()<450
			If mb=1 AdventureGoal=AdventureGoal+1
			If mb=2 AdventureGoal=AdventureGoal-1
			If AdventureGoal=-1 Then AdventureGOal=nofwinningconditions-1
			If AdventureGoal=Nofwinningconditions Then adventuregoal=0
			Delay 200
		EndIf
		
		; GateKeyVersion
		If MouseY()>500+32-260 And MouseY()<523+42-260 And MouseX()>480 And MouseX()<700
			If mb=1 GateKeyVersion=GateKeyVersion+1
			If mb=2 GateKeyVersion=GateKeyVersion-1
			If GateKeyVersion=0 Then GateKeyVersion=3
			If GateKeyVersion=4 Then GateKeyVersion=1
			Delay 200
			FreeTexture buttontexture
			FreeTexture gatetexture
			ButtonTexture=MyLoadTexture("data\graphics\buttons"+Str$(GateKeyVersion)+".bmp",4)
			GateTexture=MyLoadTexture("data\graphics\gates"+Str$(GateKeyVersion)+".bmp",1)
			
			
		EndIf

		
		; custom icon
		If MouseY()>500+32-200 And MouseY()<523+42-200 And MouseX()>480 And MouseX()<700
			
			FreeTexture IconTextureCustom
			IconTextureCustom=0
			
			FlushKeys
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color 255,255,255
			CustomIconName$=Input$( "Enter Custom Icon Texture Name (e.g. 'standard'):")
						
			If CustomIconName$="" Or CustomIconName$="Standard"
				CustomIconName$="Standard"
			Else
				If FileType(globaldirname$+"\custom\icons\icons "+CustomIconName$+".bmp")<>1
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,60,True
					Color 255,255,0
					Print "Error: Custom Icon File '"+customiconname$+"' not found."
					Print "Reverting to 'Standard' Custom Icon Texture."
					Delay 2000	
					
					CustomIconName$="Standard"
				EndIf
				
			EndIf

			IconTextureCustom=myLoadTexture(globaldirname$+"\Custom\Icons\icons "+customiconname$+".bmp",4)



		EndIf

		
		; load level
		If MouseX()>700 And MouseX()<750
			If CtrlDown()
				SelectedLevel=InputInt("Enter wlv number: ")
				AccessLevel(SelectedLevel)
				StartEditorMainLoop()
			Else
				For i=1 To 20
					If MouseY()>62+i*20 And MouseY()<=82+i*20
						SelectedLevel=i+MasterLevelListStart
						If mb=1
							If CopyingLevel And LevelExists(SelectedLevel)=False
								; copy from CopiedLevel
								If AdventureCurrentArchive=1
									ex2$="Archive\"
								Else
									ex2$="Current\"
								EndIf

								dirbase$=globaldirname$+"\custom\editing\"+ex2$+AdventureFileName$+"\"
								CopyFile(dirbase$+CopiedLevel+".wlv",dirbase$+SelectedLevel+".wlv")
								MasterLevelList(SelectedLevel)=1
								
								CopyingLevel=False
							Else
								AccessLevel(SelectedLevel)
								StartEditorMainLoop()
							EndIf
							
							Repeat
							Until MouseDown(1)=0
						ElseIf mb=2 And LevelExists(SelectedLevel)=True
							If CopyingLevel And SelectedLevel=CopiedLevel
								CopyingLevel=False
							Else
								CopyingLevel=True
								CopiedLevel=SelectedLevel
							EndIf
							
							Repeat
							Until MouseDown(2)=0
						EndIf
					EndIf
				Next
			EndIf
		EndIf
		
		; load dialog
		If MouseX()>750 And MouseX()<800
			If CtrlDown()
				currentdialog=InputInt("Enter dia number: ")
				StartDialog()
			Else
				For i=1 To 20
					If MouseY()>62+i*20 And MouseY()<=82+i*20
						SelectedDialog = i+MasterDialogListStart
						Currentdialog=SelectedDialog
						StartDialog()
						Repeat
						Until MouseDown(1)=0
					EndIf
				Next
			EndIf
		EndIf

		
		
		
		

		If MouseY()>550 And MouseX()<200	
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf
		EndIf
		
		If MouseY()>550 And MouseX()>200 And MouseX()<400
			DisplayText2(">       <",12,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",12,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			SaveMasterFile()
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf

		EndIf
		
		If MouseY()>550 And MouseX()>400 And MouseX()<600
			DisplayText2(">       <",23,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",23,28,TextMenusR,TextMenusG,TextMenusB)
			StartTestMode()
		EndIf
		If MouseY()>550 And MouseX()>600 And hubmode=False
			If KeyDown(46) 
				PackContent=True
			Else
				PackContent=False
			EndIf
			DisplayText2(">       <",34,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",34,28,TextMenusR,TextMenusG,TextMenusB)
			SaveMasterFile()
			If CompileAdventure(PackContent)=True
				StartAdventureSelectScreen()
			EndIf
			If hubmode
				hubmode=False
				SaveHubFile()
			EndIf
			Repeat
			Until MouseDown(1)=False
			
		EndIf

		
		

	EndIf
		
			

		
	
	
	RenderLetters()
	RenderWorld()
	
	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf
	Flip
	
	If waitflag=True Delay 1000
	



End Function

Function MasterAdvancedLoop()

	If KeyDown(157) And KeyDown(20)
		StartTestMode()
	EndIf
	
	adj=1
	If KeyDown(42) Or KeyDown(54) Then adj=10
	
	DisplayText2("Adventure File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(AdventureFileName$,0,1,255,255,255)
	DisplayText2("--------------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)
	
	If MouseY()<22 And  MouseX()>540
		DisplayText2("                              (Main Options)",0,0,255,255,255)
	Else
		DisplayText2("                              (Main Options)",0,0,TextMenusR,TextMenusG,TextMenusB)
	EndIf
		
	DisplayText2("Adventure Title:",0,3,TextMenusR,TextMenusG,TextMenusB)
	
	DisplayText2(AdventureTitle$,0,4,255,255,255)
	DisplayText2("--------------------------------------------",0,5,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Starting items:",0,6,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Gloves:         GlowGem:         Spy-eye:",0,7,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Gloves:Yes                      Spy-eye:Yes",0,7,255,255,255)
	;DisplayText2("               GlowGem:Yes",0.5,7,255,255,255)
	;If MouseY()<22 And  MouseX()>580
	If StarterItems And 1
		DisplayText2("       Yes",0,7,255,255,255)
	Else
		DisplayText2("       No",0,7,255,255,255)
	EndIf
	
	If StarterItems And 2
		DisplayText2("       Yes",17,7,255,255,255)
	Else
		DisplayText2("       No",17,7,255,255,255)
	EndIf
	
	If StarterItems And 4
		DisplayText2("       Yes",34,7,255,255,255)
	Else
		DisplayText2("       No",34,7,255,255,255)
	EndIf
	
	DisplayText2("--------------------------------------------",0,8,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Widescreen Spell Range:",0,9,TextMenusR,TextMenusG,TextMenusB)
	
	If WidescreenRange
		DisplayText2("                       On ",0,9,255,255,255)
	Else
		DisplayText2("                       Off ",0,9,255,255,255)
	EndIf
	DisplayText2("--------------------------------------------",0,10,TextMenusR,TextMenusG,TextMenusB)
	Displaytext2("ShardCMD:  #    C    D    D    D    D",0,11,TextMenusR,TextMenusG,TextMenusB)
	;Displaytext2(Str$(adventurewoncommand(i,j)),12+j*5,11,255,255,255)
	Displaytext2(SelectedShard,12,11,255,255,255)
	For j=0 To 4
		Displaytext2(Str$(CustomShardCMD(SelectedShard,j)),17+j*5,11,255,255,255)
	Next
	DisplayText2("--------------------------------------------",0,12,TextMenusR,TextMenusG,TextMenusB)
	Displaytext2("GlyphCMD:  #    C    D    D    D    D",0,13,TextMenusR,TextMenusG,TextMenusB)
	Displaytext2(SelectedGlyph,12,13,255,255,255)
	For j=0 To 4
		Displaytext2(Str$(CustomGlyphCMD(SelectedGlyph,j)),17+j*5,13,255,255,255)
	Next
	DisplayText2("--------------------------------------------",0,14,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Custom Map Name:",0,15,TextMenusR,TextMenusG,TextMenusB)
	If CustomMapName$=""
		DisplayText2("None",16,15,150,150,150)
	Else
		DisplayText2(CustomMapName$,16,15,255,255,255)
	EndIf
	DisplayText2("--------------------------------------------",0,16,TextMenusR,TextMenusG,TextMenusB)
		
	DisplayText2("--------------------------------------------",0,25,TextMenusR,TextMenusG,TextMenusB)
	
		
	DisplayText2("========== ========== ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2(":        : :        : :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2(":        : :        : :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2("========== ========== ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	
	If hubmode
		DisplayText2("                                 ==========",0.5,26,50,50,0)
		DisplayText2("                                 :        :",0.5,27,50,50,0)
		DisplayText2("                                 :        :",0.5,28,50,50,0)
		DisplayText2("                                 ==========",0.5,29,50,50,0)
	Else
		DisplayText2("                                 ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("                                 :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("                                 :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("                                 ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf
		
	

	If MouseY()>550 And MouseX()<200
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>550 And MouseX()>200 And MouseX()<400		
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	
	If MouseY()>550 And MouseX()>400 And MouseX()<600		
		DisplayText2(" SAVE",24.5,27,255,255,255)
		DisplayText2("+TEST",25,28,255,255,255)
	Else
		DisplayText2(" SAVE",24.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+TEST",25,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	If hubmode
		DisplayText2("COMPILE",35,27,50,50,0)
		DisplayText2("+EXIT",36,28,50,50,0)
	Else
		If MouseY()>550 And MouseX()>600
			DisplayText2("COMPILE",35,27,255,255,255)
			DisplayText2("+EXIT",36,28,255,255,255)
		Else
			DisplayText2("COMPILE",35,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("+EXIT",36,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		EndIf
	EndIf
		
	mb=0
	If MouseDown(1) mb=1
	If MouseDown(2) mb=2
	If mb>0
		If MouseY()<22 And  MouseX()>540
			SetEditorMode(8)
			Repeat
			Until MouseDown(1)=0 And MouseDown(2)=0
		EndIf
		
		If MouseY()>143 And MouseY()<163
			If MouseX()<190
				StarterItems=StarterItems Xor 1
				Repeat
				Until MouseDown(1)=0 And MouseDown(2)=0
			EndIf
			
			If MouseX()>280 And MouseX()<500
				StarterItems=StarterItems Xor 2
				Repeat
				Until MouseDown(1)=0 And MouseDown(2)=0
			EndIf
			
			If MouseX()>590
				StarterItems=StarterItems Xor 4
				Repeat
				Until MouseDown(1)=0 And MouseDown(2)=0
			EndIf
			
		EndIf 
		
		If MouseY()>183 And MouseY()<203 And MouseX()<480
			WidescreenRange=Not WidescreenRange
			Repeat
			Until MouseDown(1)=0 And MouseDown(2)=0
		EndIf
		
		If MouseY()>550 And MouseX()<200	
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf
		EndIf
		
		If MouseY()>550 And MouseX()>200 And MouseX()<400
			DisplayText2(">       <",12,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",12,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			SaveMasterFile()
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf

		EndIf
		
		If MouseY()>550 And MouseX()>400 And MouseX()<600
			DisplayText2(">       <",23,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",23,28,TextMenusR,TextMenusG,TextMenusB)
			StartTestMode()
		EndIf
		
		If MouseY()>550 And MouseX()>600 And hubmode=False
			If KeyDown(46)
				PackContent=True
			Else
				PackContent=False
			EndIf
			DisplayText2(">       <",34,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",34,28,TextMenusR,TextMenusG,TextMenusB)
			SaveMasterFile()
			If CompileAdventure(PackContent)=True
				StartAdventureSelectScreen()
			EndIf
			If hubmode
				hubmode=False
				SaveHubFile()
			EndIf
			Repeat
			Until MouseDown(1)=False
			
		EndIf
		
		For i=0 To 4
			For j=0 To 2
				If MouseX()>200+i*90-18 And MouseX()<280+i*90-18 And MouseY()>176+(j+2)*21 And MouseY()<197+(j+2)*21
					;Locate 0,0
					;Print "j="+j+" i="+i
					If j=0 And i=0
						;Print "here "+SelectedShard
						If mb=1 SelectedShard=SelectedShard+adj
						If mb=2 SelectedShard=SelectedShard-adj
						If SelectedShard<0 Then SelectedShard=NoOfShards-1
						If SelectedShard>=NoOfShards Then SelectedShard=0
					EndIf
					If j=2 And i=0
						If mb=1 SelectedGlyph=SelectedGlyph+adj
						If mb=2 SelectedGlyph=SelectedGlyph-adj
						If SelectedGlyph<0 Then SelectedGlyph=NoOfGlyphs-1
						If SelectedGlyph>=NoOfGlyphs Then SelectedGlyph=0
					EndIf
					If j=0 And i>0
						If mb=1 CustomShardCMD(SelectedShard,i-1)=CustomShardCMD(SelectedShard,i-1)+adj
						If mb=2 CustomShardCMD(SelectedShard,i-1)=CustomShardCMD(SelectedShard,i-1)-adj
					EndIf
					If j=2 And i>0
						If mb=1 CustomGlyphCMD(SelectedGlyph,i-1)=CustomGlyphCMD(SelectedGlyph,i-1)+adj
						If mb=2 CustomGlyphCMD(SelectedGlyph,i-1)=CustomGlyphCMD(SelectedGlyph,i-1)-adj
					EndIf
					Delay 150
				EndIf
				
			Next
		Next
		
		If MouseY()>302 And MouseY()<323 And MouseX()>290
			FlushKeys
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color 255,255,255
			CustomMapName$=Input$( "Enter Custom Map Name:")
			If CustomMapName$<>""
				For i=0 To 8
					If FileType(GlobalDirName$+"\Custom\Maps\"+CustomMapName$+"\mappiece"+Str$(i)+".bmp")<>1
						;NoMapFlag=True
						Cls
						Print "Error: Custom Map piece '"+CustomMapName$+"\mappiece"+Str$(i)+".bmp' not found."
						Print "Reverting to no map."
						Delay 2000
						CustomMapName$=""
						Exit
					EndIf
				Next
			EndIf
		EndIf
	EndIf
		
	RenderLetters()
	RenderWorld()
	
	If displayfullscreen=True
		DrawImage mouseimg,MouseX(),MouseY()
	EndIf
	Flip
	
	If waitflag=True Delay 1000
End Function

Function HubMainLoop()

	;If KeyDown(157) And KeyDown(20)
	;	WaitFlag=True
	;	SaveMasterFile()
	;	file=WriteFile("test.dat")
	;	WriteString file,AdventureFileName$
	;	CloseFile file
	;	ExecFile ("wg.exe")
	;	End
	;EndIf
	dialogtimer=dialogtimer+1
	DisplayText2("Hub File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(HubFileName$,0,1,255,255,255)
	DisplayText2("--------------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)
	
	adj=1
	If KeyDown(42) Or KeyDown(54) Then adj=10
		
	For i=0 To 43
		AddLetter(Asc("X")-32,-.97+i*.045,.5-0*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		For j=0 To 0
			AddLetter(Asc("X")-32,-.97+i*.045,.5-(3+j)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		Next
	Next
	
	DisplayText2("Hub Title:",0,3,TextMenusR,TextMenusG,TextMenusB)
	
	DisplayText2(HubTitle$,0,4,255,255,255)
	DisplayText2("--------------------------------------------",0,5,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Hub Description:",0,6,TextMenusR,TextMenusG,TextMenusB)
	
	DisplayText2(HubDescription$,0,7,255,255,255)
	DisplayText2("--------------------------------------------",0,8,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Starting items:",0,6,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Gloves:         GlowGem:         Spy-eye:",0,7,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Gloves:Yes                      Spy-eye:Yes",0,7,255,255,255)
	;DisplayText2("               GlowGem:Yes",0.5,7,255,255,255)
	;If MouseY()<22 And  MouseX()>580
	
	For i=9 To 25
		DisplayText2("    :                         :",0,i,TextMenusR,TextMenusG,TextMenusB)
		
	Next
	
	;HubAdvStart=0
	For i=0 To 12
		c=HubAdvStart+i
		flag=False
		If MouseX()<82 And MouseY()>222+i*20 And MouseY()<=242+i*20
			flag=True
		EndIf
		
		If c=0
			s$="Hub"
		Else
			If c<10
				s$="00"+Str(c)
			ElseIf c<100
				s$="0"+Str(c)
			Else
				s$=Str(c)
			EndIf
		EndIf
		If flag
			DisplayText2(s$,0.5,11+i,255,255,100)
		ElseIf HubAdventuresFilenames$(c)<>"" And c<=HubTotalAdventures
			DisplayText2(s$,0.5,11+i,210,210,210)
		Else
			DisplayText2(s$,0.5,11+i,100,100,100)
		EndIf
		If c<=HubTotalAdventures
			DisplayText2(HubAdventuresFilenames$(c),5,11+i,210,210,210)
		EndIf
	Next
	
	If HubSelectedAdventure=-1
		HubSelectedAdventureText$="---"
	ElseIf HubSelectedAdventure=0
		HubSelectedAdventureText$="Hub"
	ElseIf HubSelectedAdventure<10
		HubSelectedAdventureText$="00"+HubSelectedAdventure
	ElseIf HubSelectedAdventure<100
		HubSelectedAdventureText$="0"+HubSelectedAdventure
	Else
		HubSelectedAdventureText$=HubSelectedAdventure
	EndIf
	DisplayText2("Adv# FileName                  Selected: "+HubSelectedAdventureText,0,9,TextMenusR,TextMenusG,TextMenusB)
	flag2=False
	If HubSelectedAdventure>=0
		If HubAdventuresFilenames$(HubSelectedAdventure)<>""
			flag2=True
		EndIf
	EndIf
	If flag2
		If MouseX()>632 And MouseX()<722 And MouseY()>262 And MouseY()<282
			DisplayText2("                                   EDIT",0.5,13,255,255,255)
		Else
			DisplayText2("                                   EDIT",0.5,13,180,180,180)
		EndIf
		If MouseX()>607 And MouseX()<745 And MouseY()>342 And MouseY()<362
			DisplayText2("                                  REPLACE",0,17,255,255,255)
		Else
			DisplayText2("                                  REPLACE",0,17,180,180,180)
		EndIf
		If MouseX()>612 And MouseX()<732 And MouseY()>422 And MouseY()<442
			DisplayText2("                                  REMOVE",0.5,21,255,255,255)
		Else
			DisplayText2("                                  REMOVE",0.5,21,180,180,180)
		EndIf
	Else
	DisplayText2("                                   EDIT",0.5,13,100,100,100)
	DisplayText2("                                  REPLACE",0,17,100,100,100)
	DisplayText2("                                  REMOVE",0.5,21,100,100,100)
	EndIf
	If MouseX()<82 And MouseY()>202 And MouseY()<222
		DisplayText2(" -",0.5,10,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2(" -",0.5,10,TextMenuXR,TextMenuXG,TextMenuXB)
	EndIf
	If MouseX()<82 And MouseY()>482 And MouseY()<502
		DisplayText2(" +",0.5,24,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2(" +",0.5,24,TextMenuXR,TextMenuXG,TextMenuXB)
	EndIf
	;DisplayText2("Adv#:FileName                   :EDIT:REMOVE",0,9,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("----:---------------------------: -  :  -",0,10,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Hub :myHub                      :Edit:Remove",0,11,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("001 :(Click to add)             :Edit:Remove",0,12,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("002 :(Click to add)             :Edit:Remove",0,13,TextMenusR,TextMenusG,TextMenusB)
	
	;DisplayText2("Adv#:FileName                 :Selected: 001",0,9,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("     ------------------------- -------------",0,10,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Hub :myHub                    : ",0,11,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("001 :(Click to add)           :Edit:Remove",0,12,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("002 :(Click to add)           :Edit:Remove",0,13,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("--------------------------------------------",0,10,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("   ",0,24,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("---- ------------------------- -------------",0,25,TextMenusR,TextMenusG,TextMenusB)
	
		
	DisplayText2("========== ========== ========== ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2(":        : :        : :        : :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2(":        : :        : :        : :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2("========== ========== ========== ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	
	
	If MouseY()>550 And MouseX()<200
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>550 And MouseX()>200 And MouseX()<400		
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	
	If MouseY()>550 And MouseX()>400 And MouseX()<600		
		DisplayText2("BUILD",25,27,255,255,255)
		DisplayText2("+EXIT",25,28,255,255,255)
	Else
		DisplayText2("BUILD",25,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",25,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	
	If MouseY()>550 And MouseX()>600
		DisplayText2("COMPILE",35,27,255,255,255)
		DisplayText2("+EXIT",36,28,255,255,255)
	Else
		DisplayText2("COMPILE",35,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",36,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf
		
	mb=0
	If MouseDown(1) mb=1
	If MouseDown(2) mb=2
	If mb>0
		;If MouseY()<22 And  MouseX()>540
		;	SetEditorMode(8)
		;	Repeat
		;	Until MouseDown(1)=0 And MouseDown(2)=0
		;EndIf
		If MouseX()<82 And MouseY()>202 And MouseY()<222
			HubAdvStart=HubAdvStart-adj
			Delay 150
			If HubAdvStart<0
				HubAdvStart=0
			EndIf
		EndIf
		
		If MouseX()<82 And MouseY()>482 And MouseY()<502
			HubAdvStart=HubAdvStart+adj
			Delay 150
			If HubAdvStart+12>500
				HubAdvStart=488
			EndIf
		EndIf
		For i=0 To 12
			If MouseX()<82 And MouseY()>222+i*20 And MouseY()<=242+i*20
				HubSelectedAdventure=HubAdvStart+i
				If HubAdventuresFilenames$(HubSelectedAdventure)="" Or HubSelectedAdventure>HubTotalAdventures
					GetCurrentAdventures()
					AdventureNameEntered$=""
					SetEditorMode(12)
				EndIf
				Repeat
				Until MouseDown(1)=0 
			EndIf
		Next
		
		; edit
		If MouseX()>632 And MouseX()<722 And MouseY()>262 And MouseY()<282 And HubSelectedAdventure>=0
			AdventureFileName$=HubAdventuresFilenames$(HubSelectedAdventure)
			MasterDialogListStart=0
			MasterLevelListStart=0
			StartMaster()
			Repeat
			Until MouseDown(1)=0 
		EndIf
		
		; replace
		If MouseX()>607 And MouseX()<745 And MouseY()>342 And MouseY()<362 And HubSelectedAdventure>=0
			GetCurrentAdventures()
			SetEditorMode(12)
			Repeat
			Until MouseDown(1)=0 
		EndIf
		
		; remove
		If MouseX()>612 And MouseX()<732 And MouseY()>422 And MouseY()<442 And HubSelectedAdventure>=0
			HubAdventuresFilenames$(HubSelectedAdventure)=""
			;also check if this is the bigest number and update HubTotalAdventures
			If HubTotalAdventures=HubSelectedAdventure
				;find the new HubTotalAdventures
				For i=HubTotalAdventures To 1 Step-1
					If HubAdventuresFilenames$(i)<>""
						Exit
					EndIf
					HubTotalAdventures=HubTotalAdventures-1
				Next
			EndIf
			HubSelectedAdventure=-1
			Repeat
			Until MouseDown(1)=0
		EndIf
		If MouseY()>550 And MouseX()<200	
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			hubmode=False
			StartAdventureSelectScreen()
		EndIf
		
		If MouseY()>550 And MouseX()>200 And MouseX()<400
			DisplayText2(">       <",12,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",12,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			SaveHubFile()
			hubmode=False
			StartAdventureSelectScreen()

		EndIf
		
		If MouseY()>550 And MouseX()>400 And MouseX()<600
			DisplayText2(">       <",23,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",23,28,TextMenusR,TextMenusG,TextMenusB)
			;WaitFlag=True
			If BuildHub()=True
				SaveHubFile()
				hubmode=False
				StartAdventureSelectScreen()
			EndIf
			Repeat
			Until MouseDown(1)=False
		EndIf
		
		If MouseY()>550 And MouseX()>600
			DisplayText2(">       <",34,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",34,28,TextMenusR,TextMenusG,TextMenusB)
			If KeyDown(46)
				PackContent=True
			Else
				PackContent=False
			EndIf
			;WaitFlag=True
			;SaveMasterFile()
			If CompileHub(PackContent)=True
				SaveHubFile()
				hubmode=False
				StartAdventureSelectScreen()
			EndIf
			Repeat
			Until MouseDown(1)=False
			
		EndIf
	EndIf
	If MouseX()<>OldMouseX Or MouseY()<>OldMouseY
		ShowPointer
	EndIf
	OldMouseX=MouseX()
	OldMouseY=MouseY()
	
	Entering=0
	x=MouseX()/18
	y=(MouseY()-84)/21
	If x<38 And MouseY()>=84 And y=3
		Entering=1
		If x>Len(HubDescription$) Then x=Len(HubDescription$)
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf	
	If x<38 And y=0
		Entering=2
		If x>Len(HubTitle$) Then x=Len(HubTitle$)
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf
		
	OldX=x
	OldY=y
	; entering text
	let=GetKey()
	If Entering>0
	
		Select entering
		Case 1
			tex$=HubDescription$
		Case 2
			tex$=HubTitle$
; PUT BACK IN FOR ME - HELP LINE (DON"T NEED)
;		Case 3
;			tex$=AdventureHelpLine$(y-10)
			
		End Select
		
		If let>=32 And let<=122
			; place letter
			tex$=Left$(tex$,x)+Chr$(let)+Right$(tex$,Len(tex$)-x)
			tex$=Left$(tex$,38)
			; and advance cursor
			If x<37
				MoveMouse (x+1)*18,84+y*21
				OldMouseX=MouseX()
				HidePointer()
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(14)
			; backspace
			If x>0 
				tex$=Left$(tex$,x-1)+Right$(tex$,Len(tex$)-x)
				MoveMouse (x-1)*18,84+y*21
				OldMouseX=MouseX()
				HidePointer()
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(211)
			; delete
			If x<Len(tex$) 
				tex$=Left$(tex$,x)+Right$(tex$,Len(tex$)-x-1)
				HidePointer
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		
		Select entering
		Case 1
			HubDescription$=tex$
		Case 2
			HubTitle$=tex$
	;	Case 3
	;		AdventureHelpLine$(y-10)=tex$
			
		End Select
		
		; cursor movement
		If (KeyDown(200) Or KeyDown(72)) 
			If (y>3 And y<8) Or y=11 Or y=12 
				MoveMouse (x+0)*18,84+(y-1)*21
			Else If y=0
				MoveMouse (x+0)*18,84+12*21
			Else If y=3
				MoveMouse x*18,84+0*21
			Else If y=10
				MoveMouse x*18,84+7*21
			EndIf
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1

	
		EndIf
		If (KeyDown(208) Or KeyDown(28) Or KeyDown(156)) 
			If (y>2 And y<7) Or y=10 Or y=11
				MoveMouse (x+0)*18,84+(y+1)*21
			Else If y=0
				MoveMouse (x+0)*18,84+3*21
			Else If y=7
				MoveMouse x*18,84+10*21
			Else If y=12
				MoveMouse x*18,84
			EndIf
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(203)) And x>0
			MoveMouse (x-1)*18,84+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(205)) And x<Len(tex$)
			MoveMouse (x+1)*18,84+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		
		
		



		EndIf	
		
	
	RenderLetters()
	RenderWorld()
	
	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf
	Flip
	
	If waitflag=True Delay 1000
End Function

Function HubAdventureSelectScreen()

	leveltimer=leveltimer+1

	mx=MouseX()
	my=MouseY()
	If mY>=165 And my<544 And mx>50 And mx<748
		AdventureNameSelected=(my-165)/20
	Else
		AdventureNameSelected=-1
	EndIf


	;DisplayText2(Versiontext$,0,0,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("================================",0,1,TextMenusR,TextMenusG,TextMenusB)
	
	DisplayText2("Hub File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(HubFileName$,0,1,255,255,255)
	DisplayText2("--------------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)
	If HubSelectedAdventure<10
		x=2
	ElseIf HubSelectedAdventure<100
		x=1
	Else
		x=0
	EndIf
	If HubSelectedAdventure=0
		DisplayText2("Hub",41,0,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("Adventure"+HubSelectedAdventure,32+x,0,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	
	DisplayText2("Enter New Adventure Filename (e.g. 'Test34')",0,3,TextMenusR,TextMenusG,TextMenusB)

	
	If leveltimer Mod 100 <50
		DisplayText2(":",Len(AdventureNameEntered$),4,255,255,255)
	EndIf
	DisplayText2(AdventureNameEntered$,0,4,255,255,255)

	
	DisplayText2("Or Select Existing To Add:",0,6,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("===================================================",0,7,TextMenusR,TextMenusG,TextMenusB)

	If NofAdventureFileNames>19
		For i=0 To 18
			displaytext2(":",2,8+i,TextMenusR,TextMenusG,TextMenusB)
			displaytext2(":",41,8+i,TextMenusR,TextMenusG,TextMenusB)
		Next
		DisplayText2("--",0,8,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",0,9,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Up",0,10,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,11,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",0,24,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Dn",0,25,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,26,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2("--",42,8,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",42,9,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Up",42,10,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",42,11,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2("--",42,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",42,24,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Dn",42,25,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",42,26,TextMenusR,TextMenusG,TextMenusB)
	EndIf

	If AdventureNameSelected>=AdventureFileNamesListedStart+NofAdventureFileNames
		AdventureNameSelected=-1
	EndIf

	For i=0 To 18
		If AdventureFileNamesListedStart+i<NofAdventureFileNames
			If i=AdventureNameSelected
				DisplayText2(AdventureFileNamesListed$(AdventureFileNamesListedStart+i),22-Len(AdventureFileNamesListed$(AdventureFileNamesListedStart+i))/2,8+i,255,255,255)
			Else
				DisplayText2(AdventureFileNamesListed$(AdventureFileNamesListedStart+i),22-Len(AdventureFileNamesListed$(AdventureFileNamesListedStart+i))/2,8+i,155,155,155)
			EndIf
		EndIf
	Next


	DisplayText2("============================================",0,27,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("(BACK)",0,28,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2(EditorUserName$,6,28,255,255,255)
	;DisplayText2("(CHANGE)",36,28,TextMenusR,TextMenusG,TextMenusB)
	
	
	; Entering New Name
	let=GetKey()
	If let>=32 And let<=122 And Len(AdventureNameEntered$)<38
		AdventureNameEntered$=AdventureNameEntered$+Chr$(let)
	EndIf
	If KeyDown(14)
		; backspace
		If Len(AdventureNameEntered$)>0
			AdventureNameEntered$=Left$(AdventureNameEntered$,Len(AdventureNameEntered$)-1)
			Delay CharacterDeleteDelay
		EndIf
	EndIf
	If KeyDown(211)
		; delete
		AdventureNameEntered$=""
		Delay CharacterDeleteDelay
	EndIf
	If KeyDown(28) Or KeyDown(156)
		; Enter
		
		If AdventureNameEntered$=""
			DisplayText2(" INVALID ADVENTURE NAME - Empty Name!",0,5,TextMenusR,TextMenusG,TextMenusB)
		Else If FileType(GlobalDirName$+"\Custom\Editing\Current\"+AdventureNameEntered$)=2 
			DisplayText2(" INVALID ADVENTURE NAME - Already Exists!",0,5,TextMenusR,TextMenusG,TextMenusB)
		Else If FileType(GlobalDirName$+"\Custom\Editing\Archive\"+AdventureNameEntered$)=2
			DisplayText2(" INVALID ADVENTURE NAME - Already in Archive!",0,5,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("--> STARTING MAIN EDITOR - Please Wait!",0,5,TextMenusR,TextMenusG,TextMenusB)
			CreateDir GlobalDirName$+"\Custom\Editing\Current\"+AdventureNameEntered$

			AdventureFileName$=AdventureNameEntered$
			
			GetCurrentAdventures()
			
			For i=0 To NofAdventureFileNames-1	
				If AdventureFileName$=AdventureFileNamesListed$(i)
					AdventureNameSelected=i	
					Repeat
					Until MouseDown(1)=0
					StartMaster()
					HubAdventuresFilenames$(HubSelectedAdventure)=AdventureFileNamesListed$(AdventureNameSelected)
					If HubSelectedAdventure>HubTotalAdventures
						HubTotalAdventures=HubSelectedAdventure
					EndIf
				EndIf
			Next
		
		
			
		EndIf
		waitflag=True
	EndIf
	
	
	
	
	If MouseDown(1)
		If mx<130 And my>560
			; go back to hub menu
			SetEditorMode(11)
			If HubAdventuresFilenames$(HubSelectedAdventure)=""
				HubSelectedAdventure=-1
			EndIf
			Repeat
			Until MouseDown(1)=0
		EndIf
				
		;If my>123 And my<143 And mx>507 And mx<650 ;And AdventureCurrentArchive=1
		;	GetCurrentAdventures()
		;EndIf
		;If my>123 And my<143 And mx>650 And AdventureCurrentArchive=0
		;	GetArchiveAdventures()
		;EndIf
		
		If (mx<50 Or mx>748) And my>175 And my<235
			; Page Up
			AdventureFileNamesListedStart=AdventureFileNamesListedStart-19
			If AdventureFileNamesListedStart<0 Then AdventureFileNamesListedStart=0
			Repeat
			Until MouseDown(1)=0
		EndIf
		If (mx<50 Or mx>748) And my>475 And my<535
			; Page Down
			AdventureFileNamesListedStart=AdventureFileNamesListedStart+19
			If AdventureFileNamesListedStart>NofAdventureFileNames-19 Then AdventureFileNamesListedStart=NofAdventureFileNames-19
			Repeat
			Until MouseDown(1)=0
		EndIf
		
		If AdventureNameSelected>=0
		
			Repeat
			Until MouseDown(1)=0
			SetEditorMode(11)
			HubAdventuresFilenames$(HubSelectedAdventure)=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
			If HubSelectedAdventure>HubTotalAdventures
				HubTotalAdventures=HubSelectedAdventure
			EndIf
		EndIf

	EndIf
		
	
	RenderLetters()
	UpdateWorld 
	RenderWorld
	
	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf
	Flip
	If waitflag=True Delay 2000
	

	
End Function

Function StartTestMode()
	WaitFlag=True
	SaveMasterFile()
	file=WriteFile(globaldirname$+"\temp\test.dat")
	WriteString file,AdventureFileName$
	If hubmode
		WriteString file,HubFileName$
	Else
		WriteString file,""
	EndIf
	
	WriteInt file, EditorMode
	CloseFile file
	ExecFile ("wg.exe")
	End
End Function

Function MasterFileExists()

	If AdventureCurrentArchive=1
		ex2$="Archive\"
	Else
		ex2$="Current\"
	EndIf

	If FileType(globaldirname$+"\Custom\editing\"+ex2$+AdventureFileName$+"\master.dat")=1
		Return True
	Else
		Return False
	EndIf

End Function

Function LoadMasterFile()


	If AdventureCurrentArchive=1
		ex2$="Archive\"
	Else
		ex2$="Current\"
	EndIf


	

	file=ReadFile (globaldirname$+"\Custom\editing\"+ex2$+AdventureFileName$+"\master.dat")

	
	
	adventuretitle$=ReadString$(file)
	For i=0 To 4
		adventuretextline$(i)=ReadString$(file)
	Next
	
	ReadString$(file) ;user (not loaded)
	CustomIconName$=ReadString$(file)
	If CustomIconName$="" Or CustomIconName$="Standard"
		CustomIconName$="Standard"
	Else
		If FileType(globaldirname$+"\custom\icons\icons "+CustomIconName$+".bmp")<>1
			Cls
			Print "Error: Custom Icon File '"+customiconname$+"' not found."
			Print "Reverting to 'Standard' Custom Icon Texture."
			Delay 2000
			CustomIconName$="Standard"
		EndIf
		
	EndIf
	
	CustomMapName$=ReadString$(file)
	If CustomMapName$<>""
		For i=0 To 8
			If FileType(GlobalDirName$+"\Custom\Maps\"+CustomMapName$+"\mappiece"+Str$(i)+".bmp")<>1
				Cls
				Print "Error: Custom Map piece'"+CustomMapName$+"\mappiece"+Str$(i)+".bmp' not found."
				Print "Reverting to no map."
				Delay 2000
				CustomMapName$=""
				Exit
			EndIf
		Next
	EndIf
	ReadString$(file)
	ReadString$(file)
	ReadString$(file)
	ReadString$(file)
	
	
		
	AdventureExitWonLevel=ReadInt(file)
	AdventureExitWonX=ReadInt(file)
	AdventureExitWonY =ReadInt(file); at what hub level and x/y do you reappear if won.
	AdventureExitLostLevel=ReadInt(file)
	AdventureExitLostX=ReadInt(file)
	AdventureExitLostY =ReadInt(file); at what hub level and x/y do you reappear if won.


	AdventureGoal=ReadInt(file)	; when is adventure done
						; 1-NofWeeStinkersInAdventure=0


	For i=0 To 2
		For j=0 To 5
			AdventureWonCommand(i,j)=ReadInt(file)
		Next
	Next
	
	adventurestartx=ReadInt(file)
	adventurestarty=ReadInt(file)
	GateKeyVersion=1
	If Eof(file)=False GateKeyVersion=ReadInt(file)
		
	adventurestartdir=0
	If Eof(file)=False Adventurestartdir=ReadInt(file)
	
	StarterItems=7
	If Eof(file)=False StarterItems=ReadInt(file)
	WidescreenRange=0
	If Eof(file)=False WidescreenRange=ReadInt(file)
	
	; reset
	CustomShardEnabled=0
	CustomGlyphEnabled=0
	SelectedShard=0
	SelectedGlyph=0
	For i=0 To NoOfShards-1
		For j=0 To 4
			CustomShardCMD(i,j)=0
		Next
	Next
	For i=0 To NoOfGlyphs-1
		For j=0 To 4
			CustomGlyphCMD(i,j)=0
		Next
	Next
	
	; load
	If Eof(file)=False
		CustomShardEnabled=ReadInt(file)
		If CustomShardEnabled>0
			For i=0 To CustomShardEnabled-1
				For j=0 To 4
					CustomShardCMD(i,j)=ReadInt(file)
				Next
			Next
		EndIf
		CustomGlyphEnabled=ReadInt(file)
		If CustomGlyphEnabled>0
			For i=0 To CustomGlyphEnabled-1
				For j=0 To 4
					CustomGlyphCMD(i,j)=ReadInt(file)
				Next
			Next
		EndIf
	EndIf
	CloseFile file	

	
	
	FreeTexture buttontexture
	FreeTexture gatetexture
	ButtonTexture=MyLoadTexture("data\graphics\buttons"+Str$(GateKeyVersion)+".bmp",4)
	GateTexture=MyLoadTexture("data\graphics\gates"+Str$(GateKeyVersion)+".bmp",1)

	

End Function

Function LoadHubFile()
	file=ReadFile(globaldirname$+"\Custom\editing\Hubs\"+HubFileName$+"\hub.dat")
	version=ReadInt(file)
	flag=False
	If version=0
		HubTitle$=ReadString(file)
		HubDescription$=ReadString(file)
		HubTotalAdventures=ReadInt(file)
		For i=0 To HubTotalAdventures
			HubAdventuresFilenames$(i)=ReadString(file)
			If FileType(globaldirname$+"\custom\editing\current\"+HubAdventuresFilenames$(i))<>2
				HubAdventuresFilenames$(i)="" ; remove
				If HubTotalAdventures=i
					;find the new HubTotalAdventures
					For i=HubTotalAdventures To 1 Step-1
						If HubAdventuresFilenames$(i)<>""
							Exit
						EndIf
						HubTotalAdventures=HubTotalAdventures-1
					Next
					flag=True
				EndIf
			EndIf
		Next
		If flag
			Cls
			Locate 0,0
			Print "Warning: At least one adventure is missing."
			Print "Missing adventures are removed from the hub automatically."
			Delay 3000
		EndIf
	Else
		Cls
		Locate 0,0
		Print "Error: Unsupported hub file version."
		Print "Please update your editor."
		Delay 2000
	EndIf
End Function

Function SaveHubFile()
	file=WriteFile(globaldirname$+"\Custom\editing\Hubs\"+HubFileName$+"\hub.dat")
	WriteInt file,0
	WriteString file,HubTitle$
	WriteString file,HubDescription$
	WriteInt file,HubTotalAdventures
	For i=0 To HubTotalAdventures
		WriteString file,HubAdventuresFilenames$(i)
	Next
	CloseFile file
End Function

Function SaveMasterFIle()

	
	If AdventureCurrentArchive=1
		ex2$="Archive\"
	Else
		ex2$="Current\"
	EndIf


	

	file=WriteFile (globaldirname$+"\Custom\editing\"+ex2$+AdventureFileName$+"\master.dat")


	WriteString file,adventuretitle$
	For i=0 To 4
		WriteString file,adventuretextline$(i)
	Next
	
	WriteString file,EditorUserName$
	WriteString file,CustomIconName$
	WriteString file,CustomMapName$ ;""
	WriteString file,""
	WriteString file,""
	WriteString file,""
	WriteString file,""
	

	
		
	WriteInt file, AdventureExitWonLevel
	WriteInt file, AdventureExitWonX
	WriteInt file, AdventureExitWonY ; at what hub level and x/y do you reappear if won.
	WriteInt file, AdventureExitLostLevel
	WriteInt file, AdventureExitLostX
	WriteInt file, AdventureExitLostY ; at what hub level and x/y do you reappear if won.


	WriteInt file, AdventureGoal	; when is adventure done
						; 1-NofWeeStinkersInAdventure=0

	For i=0 To 2
		For j=0 To 5
			WriteInt file, AdventureWonCommand(i,j)
		Next
	Next
	
	WriteInt file,adventurestartx
	WriteInt file,adventurestarty
	
	WriteInt file,GateKeyVersion
	
	WriteInt file,AdventureStartDir
	
	WriteInt file,StarterItems 
	WriteInt file,WidescreenRange
	
	CustomShardEnabled=0
	For i=0 To NoOfShards-1
		If CustomShardCMD(i,0)>0
			CustomShardEnabled=NoOfShards
		EndIf
	Next
	
	CustomGlyphEnabled=0
	For i=0 To NoOfGlyphs-1
		If CustomGlyphCMD(i,0)>0
			CustomGlyphEnabled=NoOfGlyphs
		EndIf
	Next
	
	WriteInt file,CustomShardEnabled
	If CustomShardEnabled>0
		For i=0 To CustomShardEnabled-1
			For j=0 To 4
				WriteInt file,CustomShardCMD(i,j)
			Next
		Next
	EndIf

	WriteInt file,CustomGlyphEnabled
	If CustomGlyphEnabled>0
		For i=0 To CustomGlyphEnabled-1
			For j=0 To 4
				WriteInt file,CustomGlyphCMD(i,j)
			Next
		Next
	EndIf
	CloseFile file	
	

End Function

Function SetInterChange(i)

	If i<0
		i=0
	ElseIf i>MaxInterChanges
		i=MaxInterChanges
	EndIf
	
	If i<>WhichInterChange
		WhichAnswer=0
		ColEffect=-1
		TxtEffect=-1
	EndIf
	
	WhichInterChange=i
	
	If WhichInterChange>=NofInterChanges NofInterChanges=WhichInterChange+1
	
	DeduplicateDialogTextCommands() ; for old dialogs

End Function

Function SetAnswer(i)

	If i<0
		i=0
	ElseIf i>7
		i=7
	EndIf
	
	If i<>WhichAnswer
		ColEffect=-1
		TxtEffect=-1
	EndIf
	
	WhichAnswer=i

End Function

Function SetAskabout(i)

	If i<0
		i=0
	ElseIf i>100
		i=100
	EndIf
	
	If i<>WhichAskabout
		ColEffect=-1
		TxtEffect=-1
	EndIf
	
	WhichAskabout=i

End Function

Function StartDialog()

	SetEditorMode(9)
	
	CameraProjMode Camera1,0
	CameraProjMode Camera2,0
	CameraProjMode Camera3,0
	CameraProjMode Camera4,0
	CameraProjMode Camera,1

	
	If AdventureCurrentArchive=1
		ex$="Archive\"
	Else
		ex$="Current\"
	EndIf
	
	; check existence of this dialog file
	
	If FileType(globaldirname$+"\Custom\editing\"+ex$+AdventureFileName$+"\"+Str$(currentdialog)+".dia")=1
		LoadDialogFile()
	Else
		ClearDialogFile()
		
	EndIf
	
	SetInterChange(0)
	WhichAnswer=0
	WhichAskAbout=0
	
	ColEffect=-1
	TxtEffect=-1


End Function


Function DialogTextCommandIsColor(k)

	Return Left$(DialogTextCommand$(WhichInterChange,k),1)="C"

End Function

Function DialogTextCommandIsEffect(k)

	Return Left$(DialogTextCommand$(WhichInterChange,k),1)="E"

End Function

Function CopyDialogTextCommand(Source,Dest)

	DialogTextCommandPos(WhichInterChange,Dest)=DialogTextCommandPos(WhichInterChange,Source)
	DialogTextCommand$(WhichInterChange,Dest)=DialogTextCommand$(WhichInterChange,Source)

End Function

Function SwapDialogTextCommand(Source,Dest)

	temp=DialogTextCommandPos(WhichInterChange,Dest)
	DialogTextCommandPos(WhichInterChange,Dest)=DialogTextCommandPos(WhichInterChange,Source)
	DialogTextCommandPos(WhichInterChange,Source)=temp
	
	temp2$=DialogTextCommand$(WhichInterChange,Dest)
	DialogTextCommand$(WhichInterChange,Dest)=DialogTextCommand$(WhichInterChange,Source)
	DialogTextCommand$(WhichInterChange,Source)=temp2$

End Function

Function DeleteDialogTextCommand(k)

	For j=k+1 To NofTextCommands(WhichInterChange)-1
		CopyDialogTextCommand(j,j-1)
	Next

	NofTextCommands(WhichInterChange)=NofTextCommands(WhichInterChange)-1

End Function

Function AddDialogTextCommand(x,y,command$)

	DialogTextCommandPos(WhichInterChange,NofTextCommands(WhichInterChange))=x+(y*CharactersPerLine)
	DialogTextCommand$(WhichInterChange,NofTextCommands(WhichInterChange))=command$
	NofTextCommands(WhichInterChange)=NofTextCommands(WhichInterChange)+1

End Function

Function SortDialogTextCommands()

	; insertion sort
	i=1
	While i<NofTextCommands(WhichInterChange)
		j=i
		While j>0 And DialogTextCommandPos(WhichInterChange,j-1)>DialogTextCommandPos(WhichInterChange,j)
			SwapDialogTextCommand(j,j-1)
			j=j-1
		Wend
		i=i+1
	Wend

End Function

Function DeduplicateDialogTextCommands()

	SortDialogTextCommands()

	; remove duplicates (except for rainbow)
	; start with initial states for a dialog
	LatestColor$="CWHI" ; white color
	LatestEffect$="ENON" ; no effect
	For k=0 To NofTextCommands(WhichInterChange)-1
		If DialogTextCommandIsColor(k)
			CurrentColor$=DialogTextCommand$(WhichInterChange,k)
			If CurrentColor$=LatestColor$ And DialogTextCommand$(WhichInterChange,k)<>"CRAI" ; rainbow
				DeleteDialogTextCommand(k)
				k=k-1
			Else
				LatestColor$=CurrentColor$
			EndIf
		ElseIf DialogTextCommandIsEffect(k)
			CurrentEffect$=DialogTextCommand$(WhichInterChange,k)
			If CurrentEffect$=LatestEffect$
				DeleteDialogTextCommand(k)
				k=k-1
			Else
				LatestEffect$=CurrentEffect$
			EndIf
		EndIf
	Next

End Function


Function DialogMainLoop()
	
	DialogTimer=DialogTimer+1
	
	adj=1
	If ShiftDown() Then adj=10
	
	
	DisplayText2("Adventure: "+Left$(AdventureFileName$,20),0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Dialog #"+Str$(CurrentDialog),34,0,TextMenusR,TextMenusG,TextMenusB)
	
	
	
	DisplayText2("W",39,3,255,255,255)
	DisplayText2("G",41,3,195,195,195)
	DisplayText2("R",43,3,255,100,100)
	DisplayText2("O",39,4,255,155,0)
	DisplayText2("Y",41,4,255,255,0)
	DisplayText2("G",43,4,0,255,0)
	DisplayText2("B",39,5,0,255,255)
	DisplayText2("I",41,5,130,130,255)
	DisplayText2("V",43,5,255,100,255)
	DisplayText2("R",39,6,Rand(0,255),Rand(0,255),Rand(0,255))
	DisplayText2("B",41,6,150+105*Sin(DialogTimer*8),150+105*Sin(DialogTimer*8),150+105*Sin(DialogTimer*8))
	DisplayText2("W",43,6,150+105*Sin(DialogTimer*8),60,60)
	
	DisplayText2("NO SH",39,7,255,255,255)
	DisplayText2("JI WA",39,8,255,255,255)
	DisplayText2("BO ZO",39,9,255,255,255)
	DisplayText2("ZS CR",39,10,255,255,255)
	DisplayText2("EI UD",39,11,255,255,255)
	DisplayText2("LR RT",39,12,255,255,255)
	DisplayText2("CLEAR",39,14,255,255,0)
	
	
	If MouseX()>690 And MouseY()>460 And MouseY()<505
		DisplayText2("CANCEL",38.2,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("+EXIT",38.7,24,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("CANCEL",38.2,23,255,255,255)
		DisplayText2("+EXIT",38.7,24,255,255,255)

	EndIf
	
	If MouseX()>590 And MouseY()>540
		DisplayText2("SAVE",39.2,27,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("+EXIT",38.7,28,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("SAVE",39.2,27,255,255,255)
		DisplayText2("+EXIT",38.7,28,255,255,255)

	EndIf

	If ColEffect>-1
		DisplayText2("_",39+2*(ColEffect Mod 3),3+(ColEffect/3),TextMenusR,TextMenusG,TextMenusB)
	EndIf
	If TxtEffect>-1
		DisplayText2("__",39+3*(TxtEffect Mod 2),7+(TxtEffect/2),TextMenusR,TextMenusG,TextMenusB)
	EndIf


	
		
	DisplayText2("--------------------------------------------",0,1,TextMenusR,TextMenusG,TextMenusB)

	;DisplayText2("InterChange #"+Str$(WhichInterChange),20,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("----- INTERCHANGE #"+Str$(WhichInterChange)+" -----",0,3,TextMenusR,TextMenusG,TextMenusB)
	
	DisplayText2("--------------------------------------",0,11,TextMenusR,TextMenusG,TextMenusB)
	
	DisplayText2("----- ANSWER #"+Str$(WhichAnswer)+" -----",0,13,TextMenusR,TextMenusG,TextMenusB)
	
	DisplayText2("FNC  Data  CMD  Dat1  Dat2  Dat3  Dat4",0,15,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(Str$(InterChangeReplyFunction(WhichInterChange,WhichAnswer)),0,16,255,255,255)		;**
	DisplayText2(Str$(InterChangeReplyData(WhichInterChange,WhichAnswer)),5,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommand(WhichInterChange,WhichAnswer)),11,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)),17,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)),23,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)),29,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)),35,16,255,255,255)
	DisplayText2("--------------------------------------",0,17,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("----- ASKABOUT #"+Str$(WhichAskAbout)+" -----",0,22,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Active    InterChange   Repeat",0,24,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(Str$(AskAboutActive(WhichAskAbout)),0,25,255,255,255)	
	
	DisplayText2(Str$(AskAboutInterChange(WhichAskAbout)),12,25,255,255,255)	
	DisplayText2(Str$(AskAboutRepeat(WhichAskAbout)),24,25,255,255,255)	
	DisplayText2("AskAbout Title Line:",0,27,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(AskAboutTopText$,0,28,255,255,255)
;	DisplayText2("--------------------------------------",0,28,TextMenusR,TextMenusG,TextMenusB)


	
	For i=0 To 37
	
		For j=0 To 6
			AddLetter(Asc("X")-32,-.97+i*.045,.5-j*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		Next
		AddLetter(Asc("X")-32,-.97+i*.045,.5-10*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		AddLetter(Asc("X")-32,-.97+i*.045,.5-19*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		AddLetter(Asc("X")-32,-.97+i*.045,.5-24*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)





	Next
	
	; Display
	DialogCurrentRed=255
	DialogCurrentGreen=255
	DialogCurrentBlue=255
	DialogCurrentEffect=0
	totalletters=0
	For j=0 To 6
		For i=0 To Len(InterChangeTextLine$(WhichInterChange,j))
			; check special commands
			For k=0 To NofTextCommands(WhichInterChange)-1
				If DialogTextCommandPos(WhichInterChange,k)=j*38+i
					; yup - enact
					Select DialogTextCommand$(WhichInterChange,k)
					Case "CWHI"
						DialogCurrentRed=255
						DialogCurrentGreen=255
						DialogCurrentBlue=255
					Case "CGRY"
						DialogCurrentRed=195
						DialogCurrentGreen=195
						DialogCurrentBlue=195
					Case "CRED"
						DialogCurrentRed=255
						DialogCurrentGreen=100
						DialogCurrentBlue=100
					Case "CORA"
						DialogCurrentRed=255
						DialogCurrentGreen=155
						DialogCurrentBlue=000
					Case "CYEL"
						DialogCurrentRed=255
						DialogCurrentGreen=255
						DialogCurrentBlue=000
					Case "CGRE"
						DialogCurrentRed=0
						DialogCurrentGreen=255
						DialogCurrentBlue=0
					Case "CCYA"
						DialogCurrentRed=0
						DialogCurrentGreen=255
						DialogCurrentBlue=255
					Case "CBLU"
						DialogCurrentRed=130
						DialogCurrentGreen=130
						DialogCurrentBlue=255
					Case "CPUR"
						DialogCurrentRed=255
						DialogCurrentGreen=100
						DialogCurrentBlue=255
					Case "CRAI"
						DialogCurrentRed=Rand(0,255)
						DialogCurrentGreen=Rand(0,255)
						DialogCurrentBlue=Rand(0,255)
					Case "CBLI"
						DialogCurrentRed=150+105*Sin(DialogTimer*8)
						DialogCurrentGreen=150+105*Sin(DialogTimer*8)
						DialogCurrentBlue=150+105*Sin(DialogTimer*8)
					Case "CWAR"
						DialogCurrentRed=150+105*Sin(DialogTimer*8)
						DialogCurrentGreen=60
						DialogCurrentBlue=60
					Case "ENON"
						DialogCurrentEffect=0
					Case "ESHI"
						DialogCurrentEffect=1
					Case "EJIT"
						DialogCurrentEffect=2
					Case "EWAV"
						DialogCurrentEffect=3
					Case "EBOU"
						DialogCurrentEffect=4
					Case "EZOO"
						DialogCurrentEffect=5
					Case "EZSH"
						DialogCurrentEffect=6
					Case "ECIR"
						DialogCurrentEffect=7
					Case "EEIG"
						DialogCurrentEffect=8
					Case "EUPD"
						DialogCurrentEffect=9
					Case "ELER"
						DialogCurrentEffect=10
					Case "EROT"
						DialogCurrentEffect=11

						
					End Select
				EndIf
			Next
			
			size#=1.0
			spacing#=1.0
			angle#=0.0
			xoff#=0.0
			yoff#=0.0
			rot#=0.0
			Select DialogCurrentEffect
			Case 1
				xoff#=Rnd(-.1,.1)
			Case 2
				xoff#=Rnd(-.15,.15)
				yoff#=Rnd(-.1,.1)
			Case 3
				yoff#=0.2*Sin((totalletters+dialogtimer)*10)
			Case 4
				size=1.0+0.3*Sin((totalletters+dialogtimer)*10)
				spacing=1.0/size
			Case 5
				size=1.4
				spacing=1.0/size

			Case 6
				size=1.4
				spacing=1.0/size

				xoff#=Rnd(-.15,.15)
				yoff#=Rnd(-.1,.1)
			Case 7
				xoff#=Cos(dialogtimer*4)
				yoff#=Sin(dialogtimer*4)
			Case 8
				xoff#=Cos(dialogtimer*2)
				yoff#=Sin(dialogtimer*4)
			Case 9
				yoff#=Sin(dialogtimer*8)
			Case 10
				xoff#=Cos(dialogtimer*8)
			Case 11
				If Abs((-dialogtimer*8+(i+j*75)*10)) Mod 3600 <3400
					size=1.0



				Else
					size=1.3
				EndIf
				spacing=1.0/size
			;	yoff#=0.2*Sin(dialogtimer*8+i*180)

			
				

			

			End Select
			
			x#=(i)+xoff
			y#=YOffset/2.0+j+yoff
			
			
			AddLetter(Asc(Mid$(InterChangeTextLine$(WhichInterChange,j),i+1,1))-32,(-.97+x*.045*size*spacing)/1.0,(.5-y*.05*size*spacing)/1.0,1.0,rot,.04*size/1.0,0,0,0,0,0,0,0,0,0,dialogcurrentred,dialogcurrentgreen,dialogcurrentblue)
		
 				totalletters=totalletters+1
		;	AddLetter(Asc(Mid$(InterChangeTextLine$(WhichInterChange,j),i+1,1))-32,-.97+i*.045,.5-j*.05,1,0,.04,0,0,0,0,0,0,0,0,0,DialogCurrentRed,DialogCurrentGreen,DialogCurrentBlue)
		Next
	Next
	
	
	For i=0 To Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))
		AddLetter(Asc(Mid$(InterChangeReplyText$(WhichInterChange,WhichAnswer),i+1,1))-32,-.97+i*.045,.5-(10)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,255,255,255)
	Next
	
	For i=0 To Len(AskAboutText$(WhichAskAbout))
		AddLetter(Asc(Mid$(AskAboutText$(WhichAskAbout),i+1,1))-32,-.97+i*.045,.5-(19)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,255,255,255)
	Next
	For i=0 To Len(AskAboutTopText$)
		AddLetter(Asc(Mid$(AskAboutTopText$,i+1,1))-32,-.97+i*.045,.5-(24)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,255,255,255)
	Next



	; Mouse
	
	If MouseX()<>OldMouseX Or MouseY()<>OldMouseY
		ShowPointer
	EndIf
	OldMouseX=MouseX()
	OldMouseY=MouseY()
	; Mouse Pos
	Entering=0
	
	x=MouseX()/18
	If MouseY()<284
		y=(MouseY()-84)/21
	Else If MouseY()<300
		y=(MouseY()-76)/21
	Else 
		y=(MouseY()-63)/21

	EndIf
	debug1=MouseY()
	debug2=y
	
	; cursor
	If x<CharactersPerLine And MouseY()>=84 And y<7 
		Entering=1
		If x>Len(InterChangeTextLine$(WhichInterChange,y)) Then x=Len(InterChangeTextLine$(WhichInterChange,y))
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
		; Effects and Colours
		MouseButtonUsed=0
		If MouseDown(1)
			MouseButtonUsed=1
		EndIf
		If MouseDown(2)
			MouseButtonUsed=2
		EndIf
		If MouseButtonUsed<>0 And x<Len(InterChangeTextLine$(WhichInterChange,y))
			If ColEffect>=0 Or MouseButtonUsed=2
				If MouseButtonUsed=1
					Effect$=CCommands(ColEffect)
				Else
					Effect$="CWHI"
				EndIf
				; check if already one there
				flag7=False
				For k=0 To NofTextCommands(WhichInterChange)-1
					If DialogTextCommandPos(WhichInterChange,k)=x+(y*CharactersPerLine) And DialogTextCommandIsColor(k)
						; yes, replace
						flag7=True
						DialogTextCommand$(WhichInterChange,k)=Effect$
					EndIf
				Next
				If flag7=False
					; add new
					AddDialogTextCommand(x,y,Effect$)
				EndIf
			EndIf
			If TxtEffect>=0 And MouseButtonUsed=1
				; check if already one there
				flag7=False
				For k=0 To NofTextCommands(WhichInterChange)-1
					If DialogTextCommandPos(WhichInterChange,k)=x+(y*CharactersPerLine) And DialogTextCommandIsEffect(k)
						; yes, replace
						flag7=True
						DialogTextCommand$(WhichInterChange,k)=TCommands(TxtEffect)
					EndIf
				Next
				If flag7=False
					; add new
					AddDialogTextCommand(x,y,TCommands(TxtEffect))
				EndIf

			EndIf
			;ColEffect=-1
			;TxtEffect=-1
			
			DeduplicateDialogTextCommands()

		EndIf
		
					
	EndIf
	If x<CharactersPerLine And y=10
		Entering=2
		If x>Len(InterChangeReplyText$(WhichInterChange,WhichAnswer)) Then x=Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf
	
	If x<CharactersPerLine And y=19
		Entering=3
		If x>Len(AskAboutText$(WhichAskAbout)) Then x=Len(AskAboutText$(WhichAskAbout))
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf
	
	If x<CharactersPerLine And y=24
		Entering=4
		If x>Len(AskAboutTopText$) Then x=Len(AskAboutTopText$)
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf



	OldX=x
	OldY=y
	; entering text
	let=GetKey()
	If Entering=1
		
		If let>=32 And let<=122
			; place letter
			InterChangeTextLine$(WhichInterChange,y)=Left$(InterChangeTextLine$(WhichInterChange,y),x)+Chr$(let)+Right$(InterChangeTextLine$(WhichInterChange,y),Len(InterChangeTextLine$(WhichInterChange,y))-x)
			InterChangeTextLine$(WhichInterChange,y)=Left$(InterChangeTextLine$(WhichInterChange,y),38)
			; and advance cursor
			If x<37
				MoveMouse (x+1)*18,84+y*21
				OldMouseX=MouseX()
				HidePointer()
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(14)
			; backspace
			If x>0 
				InterChangeTextLine$(WhichInterChange,y)=Left$(InterChangeTextLine$(WhichInterChange,y),x-1)+Right$(InterChangeTextLine$(WhichInterChange,y),Len(InterChangeTextLine$(WhichInterChange,y))-x)
				MoveMouse (x-1)*18,84+y*21
				OldMouseX=MouseX()
				HidePointer()
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(211)
			; delete
			If x<Len(InterChangeTextLine$(WhichInterChange,y)) 
				InterChangeTextLine$(WhichInterChange,y)=Left$(InterChangeTextLine$(WhichInterChange,y),x)+Right$(InterChangeTextLine$(WhichInterChange,y),Len(InterChangeTextLine$(WhichInterChange,y))-x-1)
				HidePointer
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		; cursor movement
		If (KeyDown(200) Or KeyDown(72)) 
			If y>0
				MoveMouse (x+0)*18,84+(y-1)*21
			Else
				MoveMouse (x+0)*18,525
			EndIf
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1

	
		EndIf
		If (KeyDown(208) Or KeyDown(28) Or KeyDown(156)) 
			If y<6
				MoveMouse (x+0)*18,84+(y+1)*21
			Else
				MoveMouse (x+0)*18,76+(10)*21
			EndIf
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(203)) And x>0
			MoveMouse (x-1)*18,84+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(205)) And x<Len(InterChangeTextLine$(WhichInterChange,y))
			MoveMouse (x+1)*18,84+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		
		If KeyDown(199) ; home
			MoveMouse 0,84+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1
		EndIf
		
		If KeyDown(207) ; end
			endx=Len(InterChangeTextLine$(WhichInterChange,y))
			If endx>37
				endx=37
			EndIf
			MoveMouse endx*18,84+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1
		EndIf



	EndIf
	If Entering=2
		
		If let>=32 And let<=122
			; place letter
			InterChangeReplyText$(WhichInterChange,WhichAnswer)=Left$(InterChangeReplyText$(WhichInterChange,WhichAnswer),x)+Chr$(let)+Right$(InterChangeReplyText$(WhichInterChange,WhichAnswer),Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))-x)
			InterChangeReplyText$(WhichInterChange,WhichAnswer)=Left$(InterChangeReplyText$(WhichInterChange,WhichAnswer),38)
			; and advance cursor
			If x<37
				MoveMouse (x+1)*18,76+y*21
				OldMouseX=MouseX()
				HidePointer()
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(14)
			; backspace
			If x>0 
				InterChangeReplyText$(WhichInterChange,WhichAnswer)=Left$(InterChangeReplyText$(WhichInterChange,WhichAnswer),x-1)+Right$(InterChangeReplyText$(WhichInterChange,WhichAnswer),Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))-x)
				MoveMouse (x-1)*18,76+y*21
				OldMouseX=MouseX()
				HidePointer()
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(211)
			; delete
			If x<Len(InterChangeReplyText$(WhichInterChange,WhichAnswer)) 
				InterChangeReplyText$(WhichInterChange,WhichAnswer)=Left$(InterChangeReplyText$(WhichInterChange,WhichAnswer),x)+Right$(InterChangeReplyText$(WhichInterChange,WhichAnswer),Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))-x-1)
				HidePointer
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		; cursor movement
		If (KeyDown(200) Or KeyDown(72)) 
			MoveMouse (x+0)*18,76+(7)*21
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			Entering=1
			ColEffect=-1
			TxtEffect=-1

	
		EndIf
		If (KeyDown(208) Or KeyDown(28) Or KeyDown(156)) 
			MoveMouse (x+0)*18,465
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			Entering=1
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(203)) And x>0
			MoveMouse (x-1)*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(205)) And x<Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))
			MoveMouse (x+1)*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		
		If KeyDown(199) ; home
			MoveMouse 0,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1
		EndIf
		
		If KeyDown(207) ; end
			endx=Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))
			If endx>37
				endx=37
			EndIf
			MoveMouse endx*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1
		EndIf



	EndIf
	If Entering=3
		
		If let>=32 And let<=122
			; place letter
			AskaboutText$(WhichAskAbout)=Left$(AskaboutText$(WhichAskAbout),x)+Chr$(let)+Right$(AskaboutText$(WhichAskAbout),Len(AskaboutText$(WhichAskAbout))-x)
			AskaboutText$(WhichAskAbout)=Left$(AskaboutText$(WhichAskAbout),38)
			; and advance cursor
			If x<37
				MoveMouse (x+1)*18,76+y*21
				OldMouseX=MouseX()
				HidePointer()
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(14)
			; backspace
			If x>0 
				AskaboutText$(WhichAskAbout)=Left$(AskaboutText$(WhichAskAbout),x-1)+Right$(AskaboutText$(WhichAskAbout),Len(AskaboutText$(WhichAskAbout))-x)
				MoveMouse (x-1)*18,76+y*21
				OldMouseX=MouseX()
				HidePointer()
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(211)
			; delete
			If x<Len(AskaboutText$(WhichAskAbout)) 
				AskaboutText$(WhichAskAbout)=Left$(AskaboutText$(WhichAskAbout),x)+Right$(AskaboutText$(WhichAskAbout),Len(AskaboutText$(WhichAskAbout))-x-1)
				HidePointer
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		; cursor movement
		If (KeyDown(200) Or KeyDown(72)) ; up arrow or numpad 8
			MoveMouse (x+0)*18,76+(10)*21
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			Entering=1
			ColEffect=-1
			TxtEffect=-1

	
		EndIf
		If (KeyDown(208) Or KeyDown(28) Or KeyDown(156)) 
			MoveMouse (x+0)*18,525
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			Entering=1
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(203)) And x>0
			MoveMouse (x-1)*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(205)) And x<Len(AskaboutText$(WhichAskAbout))
			MoveMouse (x+1)*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		
		If KeyDown(199) ; home
			MoveMouse 0,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1
		EndIf
		
		If KeyDown(207) ; end
			endx=Len(AskaboutText$(WhichAskAbout))
			If endx>37
				endx=37
			EndIf
			MoveMouse endx*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1
		EndIf



	EndIf
	If Entering=4
		
		If let>=32 And let<=122
			; place letter
			AskaboutTopText$=Left$(AskaboutTopText$,x)+Chr$(let)+Right$(AskaboutTopText$,Len(AskaboutTopText$)-x)
			AskaboutTopText$=Left$(AskaboutTopText$,38)
			; and advance cursor
			If x<37
				MoveMouse (x+1)*18,76+y*21
				OldMouseX=MouseX()
				HidePointer()
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(14)
			; backspace
			If x>0 
				AskaboutTopText$=Left$(AskaboutTopText$,x-1)+Right$(AskaboutTopText$,Len(AskaboutTopText$)-x)
				MoveMouse (x-1)*18,76+y*21
				OldMouseX=MouseX()
				HidePointer()
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		If KeyDown(211)
			; delete
			If x<Len(AskaboutTopText$) 
				AskaboutTopText$=Left$(AskaboutTopText$,x)+Right$(AskaboutTopText$,Len(AskaboutTopText$)-x-1)
				HidePointer
				Delay CharacterDeleteDelay
			EndIf
			ColEffect=-1
			TxtEffect=-1

		EndIf
		; cursor movement
		If (KeyDown(200) Or KeyDown(72)) 
			MoveMouse (x+0)*18,465
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			Entering=1
			ColEffect=-1
			TxtEffect=-1

	
		EndIf
		If (KeyDown(208) Or KeyDown(28) Or KeyDown(156)) 
			MoveMouse (x+0)*18,86;+210
			OldMouseY=MouseY()
			HidePointer()
			Delay 100
			Entering=1
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(203)) And x>0
			MoveMouse (x-1)*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		If (KeyDown(205)) And x<Len(AskaboutTopText$)
			MoveMouse (x+1)*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1


		EndIf
		
		
		If KeyDown(199) ; home
			MoveMouse 0,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1
		EndIf
		
		If KeyDown(207) ; end
			endx=Len(AskaboutTopText$)
			If endx>37
				endx=37
			EndIf
			MoveMouse endx*18,76+(y-0)*21
			OldMouseX=MouseX()
			HidePointer()
			Delay 100
			ColEffect=-1
			TxtEffect=-1
		EndIf
		
		



	EndIf


	
	mb=0
	If MouseDown(1) mb=1
	If MouseDown(2) mb=2
	Modified=mb<>0 Or MouseScroll<>0
	If mb>0
		; Change Adventure
		; Load/Save
		
		If MouseX()>690 And MouseY()>460 And MouseY()<505
			ClearDialogFIle()
			ResumeMaster()
			Repeat
			Until MouseDown(1)=0
		EndIf
	
		If MouseX()>590 And MouseY()>540
			SaveDialogFile()
			ClearDialogFile()
			ResumeMaster()
			Repeat
			Until MouseDown(1)=0

		EndIf

	EndIf

	RawInput=CtrlDown()

	; Change InterChange
	If MouseY()>60 And MouseY()<80 And MouseX()>100 And MouseX()<400		
		target=AdjustInt("Interchange: ", WhichInterChange, 1, 10, 150)
		SetInterChange(target)
	EndIf
	
	; Change Answer
	If MouseY()>260 And MouseY()<280 And MouseX()>100 And MouseX()<400		
		target=AdjustInt("Answer: ", WhichAnswer, 1, 10, 150)
		SetAnswer(target)
	EndIf
	; Change AnswerData
	If MouseY()>305 And MouseY()<345
		Select MouseX()/100
		Case 0			
			InterChangeReplyFunction(WhichInterChange,WhichAnswer)=AdjustInt("FNC: ", InterChangeReplyFunction(WhichInterChange,WhichAnswer), 1, 10, 150)
		Case 1			
			InterChangeReplyData(WhichInterChange,WhichAnswer)=AdjustInt("Data: ", InterChangeReplyData(WhichInterChange,WhichAnswer), 1, 10, 150)
		Case 2
			InterChangeReplyCommand(WhichInterChange,WhichAnswer)=AdjustInt("CMD: ", InterChangeReplyCommand(WhichInterChange,WhichAnswer), 1, 10, 150)
		Case 3
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)=AdjustInt("Data1: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0), 1, 10, 150)
		Case 4
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)=AdjustInt("Data2: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1), 1, 10, 150)
		Case 5
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)=AdjustInt("Data3: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2), 1, 10, 150)
		Case 6
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)=AdjustInt("Data4: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3), 1, 10, 150)
		End Select
		
		If Modified
			ColEffect=-1
			TxtEffect=-1
		EndIf
	EndIf
		

	
	; Change Askabout
	If MouseY()>441 And MouseY()<460 And MouseX()>100 And MouseX()<400
		target=AdjustInt("AskAbout: ", WhichAskabout, 1, 10, 150)
		SetAskabout(target)
	EndIf
	; Change AskaboutData
	If MouseY()>490 And MouseY()<520
		If MouseX()<170
			AskAboutActive(WhichAskAbout)=AdjustInt("Active: ", AskAboutActive(WhichAskAbout), 1, 10, 150)
		Else If MouseX()<400
			AskAboutInterChange(WhichAskAbout)=AdjustInt("Interchange: ", AskAboutInterChange(WhichAskAbout), 1, 10, 150)
		Else
			AskAboutRepeat(WhichAskAbout)=AdjustInt("Repeat: ", AskAboutRepeat(WhichAskAbout), 1, 10, 150)
		EndIf
		
		If Modified
			ColEffect=-1
			TxtEffect=-1
		EndIf
	EndIf
	
	; Colours/Effects
	If LeftMouse=True And LeftMouseReleased=True
		LeftMouseReleased=False
		For i=0 To 11
			If MouseX()>=706+(i Mod 3)*35 And MouseX()<=706+20+(i Mod 3)*35 And MouseY()>=65 + 20*(i/3) And MouseY()<85+20*(i/3)
				ToggleColEffect(i)
			EndIf
		Next
		For i=0 To 11
			If MouseX()>=706+(i Mod 2)*60 And MouseX()<=706+40+(i Mod 2)*60 And MouseY()>=146 + 20*(i/2) And MouseY()<166+20*(i/2)
				ToggleTxtEffect(i)
			EndIf
		Next
		If MouseX()>706 And MouseY()>282 And MouseY()<302
			;clear
			For i=0 To NofTextCommands(WhichInterChange)-1
				DialogTextCommand$(WhichInterChange,i)=""
				DialogTextCommandpos(WhichInterChange,i)=-1
			Next
			NofTextCommands(WhichInterChange)=0
		EndIf
	EndIf
		
			
	If CtrlDown()
		If KeyPressed(17) Then ToggleColEffect(0) ; w: white
		If KeyPressed(18) Then ToggleColEffect(1) ; e: grey
		If KeyPressed(19) Then ToggleColEffect(2) ; r: red
		If KeyPressed(24) Then ToggleColEffect(3) ; o: orange
		If KeyPressed(21) Then ToggleColEffect(4) ; y: yellow
		If KeyPressed(34) Then ToggleColEffect(5) ; g: green
		If KeyPressed(48) Then ToggleColEffect(6) ; b: blue
		If KeyPressed(23) Then ToggleColEffect(7) ; i: indigo
		If KeyPressed(47) Then ToggleColEffect(8) ; v: violet
		If KeyPressed(30) Then ToggleColEffect(9) ; a: rainbow (all)
		If KeyPressed(33) Then ToggleColEffect(10) ; f: black+white (flashy)
		If KeyPressed(32) Then ToggleColEffect(11) ; d: warning (doomy)
		If KeyPressed(49) Then ToggleTxtEffect(0) ; n: none
		If KeyPressed(31) Then ToggleTxtEffect(1) ; s: shake
		If KeyPressed(36) Then ToggleTxtEffect(2) ; j: jitter
		If KeyPressed(45) Then ToggleTxtEffect(3) ; x: wave
		If KeyPressed(44) Then ToggleTxtEffect(4) ; z: bounce
		If KeyPressed(25) Then ToggleTxtEffect(5) ; p: zoom
		If KeyPressed(16) Then ToggleTxtEffect(6) ; q: zoom shake
		If KeyPressed(46) Then ToggleTxtEffect(7) ; c: circle
		If KeyPressed(50) Then ToggleTxtEffect(8) ; m: mobius
		If KeyPressed(22) Then ToggleTxtEffect(9) ; u: up+down
		If KeyPressed(38) Then ToggleTxtEffect(10) ; l: left+right
		If KeyPressed(20) Then ToggleTxtEffect(11) ; t: rt
	EndIf
		
	
	
	RenderLetters()
	RenderWorld()
	

	If displayfullscreen=True
				DrawImage mouseimg,MouseX(),MouseY()
			EndIf
	Flip
	
	


End Function

Function ToggleColEffect(i)
	If ColEffect=i
		ColEffect=-1
	Else
		ColEffect=i
	EndIf
End Function

Function ToggleTxtEffect(i)
	If TxtEffect=i
		TxtEffect=-1
	Else
		TxtEffect=i
	EndIf
End Function

Function ClearDialogFile()
	; first clear all data
	NofInterchanges=1
	For i=0 To MaxInterChanges-1
		NofInterChangeTextLines(i)=0	
		For j=0 To 6
			InterChangeTextLine$(i,j)=""
		Next
		NofTextCommands(i)=0
		For j=0 To 199
			DialogTextCommand$(i,j)=""
			DialogTextCommandPos(i,j)=-1
		Next
		NofInterChangeReplies(i)=1
		For j=0 To 7
			InterChangeReplyText$(i,j)=""
			InterChangeReplyFunction(i,j)=0
			InterChangeReplyData(i,j)=0
			InterChangeReplyCommand(i,j)=0
			For k=0 To 3
				InterChangeReplyCommandData(i,j,k)=0
			Next
		Next
		
	Next
	NofAskAbouts=0
	AskAboutTopText$=""
	For i=0 To MaxAskAbouts-1
		AskAboutText$(i)=""
		AskAboutActive(i)=-2
		AskAboutInterchange(i)=0
		AskAboutRepeat(i)=-1
	Next
End Function

Function LoadDialogFile()

	If AdventureCurrentArchive=1
		ex$="Archive\"
	Else
		ex$="Current\"
	EndIf


	ClearDialogFile()
		
	; yep - load
	file=ReadFile(globaldirname$+"\Custom\editing\"+ex$+AdventureFileName$+"\"+Str$(currentdialog)+".dia")

	NofInterchanges=ReadInt(file)
	For i=0 To NofInterchanges-1
		NofInterChangeTextLines(i)=ReadInt(file)	
		For j=0 To NofInterChangeTextLines(i)-1
			InterChangeTextLine$(i,j)=ReadString$(file)
		Next
		NofTextCommands(i)=ReadInt(file)
		
		For j=0 To NofTextCommands(i)-1
			DialogTextCommand$(i,j)=ReadString$(file)
			DialogTextCommandPos(i,j)=ReadInt(file)
		Next
		NofInterChangeReplies(i)=ReadInt(file)
		For j=0 To NofInterChangeReplies(i)-1
			InterChangeReplyText$(i,j)=ReadString$(file)
			InterChangeReplyFunction(i,j)=ReadInt(file)
			InterChangeReplyData(i,j)=ReadInt(file)
			InterChangeReplyCommand(i,j)=ReadInt(file)
			For k=0 To 3
				InterChangeReplyCommandData(i,j,k)=ReadInt(file)
			Next
		Next
	Next
	NofAskAbouts=ReadInt(file)
	AskAboutTopText$=ReadString$(file)
	For i=0 To NofAskAbouts-1
		AskAboutText$(i)=ReadString$(File)
		AskAboutActive(i)=ReadInt(file)
		AskAboutInterchange(i)=ReadInt(file)
		AskAboutRepeat(i)=ReadInt(file)
	Next
	CloseFile file

End Function

Function SaveDialogFile()
		
			
		
	If AdventureCurrentArchive=1
		ex$="Archive\"
	Else
		ex$="Current\"
	EndIf


	file=WriteFile(globaldirname$+"\Custom\editing\"+ex$+AdventureFileName$+"\"+Str$(currentdialog)+".dia")
	
	;NofInterChanges=NofInterChanges+1 ; MS, why would you do this?
		
	WriteInt File,NofInterchanges
	
	For i=0 To NofInterchanges-1
		; calculuate nofinterchangetextlines
		For j=7 To 0 Step -1
			If InterChangeTextLine$(i,j)<>"" 
				NofInterChangeTextLines(i)=j+1
				j=-3
			EndIf
		Next
		If j=-1 NofInterChangeTextLines(i)=0
		
		WriteInt File,NofInterChangeTextLines(i)
		For j=0 To NofInterChangeTextLines(i)-1
			WriteString file,InterChangeTextLine$(i,j)
		Next
		WriteInt File,NofTextCommands(i)
		For j=0 To NofTextCommands(i)-1
			WriteString file,DialogTextCommand$(i,j)
			WriteInt File,DialogTextCommandPos(i,j)
		Next
		
		; calculate nofinterchangereplies
		For j=0 To 7
			If InterChangeReplyText$(i,j)=""
				NofInterChangeReplies(i)=j
				j=17
			EndIf
		Next
		If j=8 Then NofInterChangeReplies(i)=8


		WriteInt File,NofInterChangeReplies(i)
		For j=0 To NofInterChangeReplies(i)-1
			WriteString file,InterChangeReplyText$(i,j)
			WriteInt File,InterChangeReplyFunction(i,j)
			WriteInt File,InterChangeReplyData(i,j)
			WriteInt File,InterChangeReplyCommand(i,j)
			For k=0 To 3
				WriteInt File,InterChangeReplyCommandData(i,j,k)
			Next
		Next
	Next
	; Calculate NofAskAbouts
	For j=0 To 99
		If AskAboutText$(j)=""
			NofAskAbouts=j
			j=200
		EndIf
	Next
	If j=100 Then NofAskAbouts=100
	WriteInt File,NofAskAbouts
	WriteString file,AskAboutTopText$
	For i=0 To NofAskAbouts-1
		WriteString file,AskAboutText$(i)
		WriteInt File,AskAboutActive(i)
		WriteInt File,AskAboutInterchange(i)
		WriteInt File,AskAboutRepeat(i)
	Next
	CloseFile file
	
End Function

Function BuildHub()
	Cls
	Locate 0,0
	Print ""
	Print "Building..."
	Print "------------"
	Print ""
	Print ""
	
	If HubTitle$=""
		Print "ERROR: No Hub Title set."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	If HubAdventuresFilenames$(0)=""
		Print "ERROR: No Hub defined."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	fn$=HubTitle$
	If HubDescription$<>""
		fn$=HubTitle$+"#"+HubDescription$
	EndIf
	CreateDir(globaldirname$+"\Custom\hubs\"+fn$)
	
	If FileType(globaldirname$+"\Custom\hubs\"+fn$)<>2
		Print "ERROR: Unable to create directory."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	; clear directory first
	dirfileclear=ReadDir(globaldirname$+"\Custom\hubs\"+fn$)
	Repeat
		f$=NextFile$(dirfileclear)
		If FileType(globaldirname$+"\Custom\hubs\"+fn$+"\"+f$)=1 And f$<>""
			DeleteFile globaldirname$+"\Custom\hubs\"+fn$+"\"+f$
		ElseIf FileType(globaldirname$+"\Custom\hubs\"+fn$+"\"+f$)=2 And f$<>"." And f$<>".." And f$<>""
			dirfileclearsub=ReadDir(globaldirname$+"\Custom\hubs\"+fn$+"\"+f$)
			Repeat
				f1$=NextFile$(dirfileclearsub)
				If FileType(globaldirname$+"\Custom\hubs\"+fn$+"\"+f$+"\"+f1$)=1
					DeleteFile globaldirname$+"\Custom\hubs\"+fn$+"\"+f$+"\"+f1$
				EndIf
			Until f1$=""
			DeleteDir globaldirname$+"\Custom\hubs\"+fn$+"\"+f$
		EndIf
	Until f$=""
	;WaitKey()
	
	; copy files
	For i=0 To HubTotalAdventures
		AdvFilename$=""
		If i=0
			AdvFilename$="Hub"
		Else
			AdvFilename$="Adventure"+Str(i)
		EndIf
		If HubAdventuresFilenames$(i)<>""
			
			CreateDir(globaldirname$+"\Custom\hubs\"+fn$+"\"+AdvFilename$)
			dirfile=ReadDir(globaldirname$+"\Custom\editing\current\"+HubAdventuresFilenames$(i))
			Print "Building "+AdvFilename$+"..."

			Repeat
				ex$=NextFile$(dirfile)
				If Upper$(ex$)="MASTER.DAT" Or Upper$(Right$(ex$,4))=".WLV" Or Upper$(Right$(ex$,4))=".DIA"
					Print "Copying... "+ex$
					CopyFile globaldirname$+"\Custom\editing\current\"+HubAdventuresFilenames$(i)+"\"+ex$, globaldirname$+"\Custom\hubs\"+fn$+"\"+AdvFilename$+"\"+ex$
				EndIf
			Until ex$=""
			
		EndIf
	Next
	
	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		Print "Copying hublogo.jpg..."
		CopyFile globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg", globaldirname$+"\Custom\hubs\"+fn$+"\hublogo.jpg"
	EndIf
	
	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")
		Print "Copying wonderlandadventures.bmp..."		CopyFile globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp", globaldirname$+"\Custom\hubs\"+fn$+"\wonderlandadventures.bmp"
	EndIf
	
	Print "Build Completed..."
	Print "You can now play/test your hub."
	
	Delay 500
	Print ""
	Print "Click to Continue."
	Repeat
	Until MouseDown(1)=True
	Repeat
	Until MouseDown(1)=False
	Return True
	
End Function

Function CompileHub(PackContent)
	Cls
	Locate 0,0
	
	Print ""
	Print "Compiling..."
	Print "------------"
	Print ""
	
	
	If HubTitle$=""
		Print "ERROR: No Hub Title set."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	If HubAdventuresFilenames$(0)=""
		Print "ERROR: No Hub defined."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	fn$=HubTitle$
	If HubDescription$<>""
		fn$=HubTitle$+"#"+HubDescription$
	EndIf

	For i=0 To HubTotalAdventures
		AdvFilename$=""
		If i=0
			AdvFilename$="Hub"
		Else
			AdvFilename$="Adventure"+Str(i)
		EndIf
		If HubAdventuresFilenames$(i)<>""
			
			If PackContent
				SearchForCustomContent(globaldirname$+"\Custom\editing\current\"+HubAdventuresFilenames$(i),True)
			EndIf
			dirfile=ReadDir(globaldirname$+"\Custom\editing\current\"+HubAdventuresFilenames$(i))
			Print ""
			Print "Reading "+AdvFilename$+"..."
			NofHubCompilerFiles(i)=0
			Repeat
				ex$=NextFile$(dirfile)
				If Upper$(ex$)="MASTER.DAT" Or Upper$(Right$(ex$,4))=".WLV" Or Upper$(Right$(ex$,4))=".DIA"
					;CopyFile globaldirname$+"\Custom\editing\current\"+HubAdventuresFilenames$(i)+"\"+ex$, globaldirname$+"\Custom\hubs\"+fn$+"\"+AdvFilename$+"\"+ex$
					Print "Reading... "+ex$
					HubCompilerFileName$(i,NofHubCompilerFiles(i))=ex$
					HubCompilerFileSize(i,NofHubCompilerFiles(i))=FileSize(globaldirname$+"\Custom\editing\current\"+HubAdventuresFilenames$(i)+"\"+ex$)
					NofHubCompilerFiles(i)=NofHubCompilerFiles(i)+1		
				EndIf
			Until ex$=""
			
		EndIf
	Next
	Delay  1000
	Print ""
	Print ""
	Print "Writing WAH File to Downloads Inbox..."
	Print ""
	file1=WriteFile(globaldirname$+"\Custom\downloads inbox\"+fn$+".wah")
	
	HubTotal=0
	For k=0 To HubTotalAdventures
		If HubAdventuresFilenames$(k)<>""
			HubTotal=HubTotal+1
		EndIf
	Next

	WriteInt file1,HubTotal
	For k=0 To HubTotalAdventures
		If HubAdventuresFilenames$(k)<>""
			Print ""
			If k=0 
				Print "Writing Hub..."
			Else
				Print "Writing Adventure"+Str$(k)+"..."
			EndIf
			WriteInt file1,k
			WriteInt file1,NofHubCompilerFiles(k)
			For i=0 To NofHubCompilerFiles(k)-1
				Print "Writing... "+HubCompilerFileName$(k,i)
				
				WriteString file1,HubCompilerFileName$(k,i)
				WriteInt file1,HubCompilerFileSize(k,i)
		
				file2=ReadFile(globaldirname$+"\Custom\editing\current\"+HubAdventuresFilenames$(k)+"\"+HubCompilerFileName$(k,i))
				
				For j=0 To HubCompilerFileSize(k,i)-1
					WriteByte file1,ReadByte (file2)
				Next
				CloseFile file2
				
			Next
		EndIf
	Next
	
	If PackContent
		Print 
		Print "Packing custom content..."
		WriteInt file1, NofCustomContentFiles
		For i=0 To NofCustomContentFiles-1
			Print "Writing... " + CustomContentFile$(i)
			size=FileSize(CustomContentFile$(i))
			WriteString file1, CustomContentFile$(i)
			WriteInt file1, size
			file2=ReadFile(CustomContentFile$(i))
			For j=0 To size-1
				WriteByte file1, ReadByte(file2)
			Next
			CloseFile file2
		Next
	Else
		WriteInt file1,0
	EndIf
	
	
	;hublogo.jpg
	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		file2=ReadFile(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		logosize=FileSize(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		Print "Packing hublogo.jpg..."+logosize
		WriteInt file1,logosize
		For j=0 To logosize-1
			WriteByte file1,ReadByte (file2)
		Next
	Else
		WriteInt file1,0
	EndIf
	
	;wonderlandadventures.bmp
	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")	
		file2=ReadFile(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")
		logosize=FileSize(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")
		Print "Packing wonderlandadventures.bmp..."+logosize	
		WriteInt file1,logosize
		For j=0 To logosize-1
			WriteByte file1,ReadByte (file2)
		Next
	Else
		WriteInt file1,0
	EndIf
	
	CloseFile file1
	Delay 1000
	Print ""
	Print ""
	Print "Copying File to Downloads Outbox..."
	CopyFile globaldirname$+"\Custom\downloads inbox\"+fn$+".wah",globaldirname$+"\Custom\downloads outbox\"+fn$+".wah"
	Print ""
	Print ""
	Delay 1000
	Print "Compile Completed... Filename: "+fn$+".wah"
	Print "You can now play/test your hub."
	
	Delay 500
	Print ""
	Print "Click to Continue."
	Repeat
	Until MouseDown(1)=True
	Repeat
	Until MouseDown(1)=False
	Return True
End Function

Function CompileAdventure(PackCustomContent)
	Cls
	Locate 0,0
	Print ""
	Print "Compiling..."
	Print "------------"
	Print ""
	Print ""
	; do some basic checks
	If adventuretitle$=""
		Print "ERROR: No Adventure Title set."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	If FileType(globaldirname$+"\Custom\editing\current\"+AdventureFileName$+"\1.wlv")=0
		
		Print "ERROR: No Level 1 present."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	If PackCustomContent
		; find custom contentt
		Print "Finding Custom Content..."
		SearchForCustomContent(globaldirname$+"\custom\editing\current\"+AdventureFileName$)
		Print "Number of Custom Content Files: " + NofCustomContentFiles
		Print
	EndIf
	
	; now go through directory and check names and sizes of files
	dirfile=ReadDir(globaldirname$+"\Custom\editing\current\"+AdventureFileName$)
	
	NofCompilerFiles=0
	Repeat
		ex$=NextFile$(dirfile)
		If Upper$(ex$)="MASTER.DAT" Or Upper$(Right$(ex$,4))=".WLV" Or Upper$(Right$(ex$,4))=".DIA"
			Print "Reading... "+ex$
			CompilerFileName$(NofCompilerFiles)=ex$
			CompilerFileSize(NofCompilerFiles)=FileSize(globaldirname$+"\Custom\editing\current\"+AdventureFileName$+"\"+ex$)
			NofCompilerFiles=NofCompilerFiles+1			
		EndIf
	Until ex$=""
	
	If PackCustomContent 
		; read custom content
		For i=0 To NofCustomContentFiles-1
			Print "Reading... "+CustomContentFile$(i)
			CompilerFileName$(NofCompilerFiles)=CustomContentFile$(i)
			CompilerFileSize(NofCompilerFiles)=FileSize(CustomContentFile$(i))
			NofCompilerFiles=NofCompilerFiles+1
		Next
	EndIf
	
	Delay 1000
	; and now make the master file
	Print ""
	Print ""
	Print "Writing WA3 File to Downloads Inbox..."
	Print ""
	Print ""
	file1=WriteFile(globaldirname$+"\Custom\downloads inbox\"+EditorUserName$+"#"+AdventureFileName$+".wa3")
	If file1=0
		Print "ERROR: Cannot Write "+globaldirname$+"\Custom\downloads inbox\"+EditorUserName$+"#"+AdventureFileName$+".wa3"
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
		
	WriteInt file1,NofCompilerFiles

	For i=0 To NofCompilerFiles-1
		Print "Writing... "+CompilerFileName$(i)
		
		WriteString file1,CompilerFileName$(i)
		WriteInt file1,CompilerFileSize(i)

		file2=ReadFile(globaldirname$+"\Custom\editing\current\"+AdventureFileName$+"\"+CompilerFileName$(i))
		If Not file2
			file2=ReadFile(CompilerFileName$(i))
		EndIf
		
		For j=0 To CompilerFileSize(i)-1
			WriteByte file1,ReadByte (file2)
		Next
		
		CloseFile file2
	Next
	CloseFile file1
	Delay 1000
	Print ""
	Print ""
	Print "Copying File to Downloads Outbox..."
	CopyFile globaldirname$+"\Custom\downloads inbox\"+EditorUserName$+"#"+AdventureFileName$+".wa3",globaldirname$+"\Custom\downloads outbox\"+EditorUserName$+"#"+AdventureFileName$+".wa3"
	Print ""
	Print ""
	Delay 1000
	Print "Compile Completed... Filename: "+EditorUserName$+"#"+AdventureFileName$+".wa3"
	Print "You can now play/test your level."
	
	Delay 500
	Print ""
	Print "Click to Continue."
	Repeat
	Until MouseDown(1)=True
	Repeat
	Until MouseDown(1)=False
	Return True
End Function

Function SearchForCustomContent(ex$,ishub=False)
	If ishub=False NofCustomContentFiles=0
	
	Local cc=NofCustomContentFiles
	
	Local mydir = ReadDir(ex$)
	
	Repeat
	
	dirfile$ = NextFile(mydir)
	filename$=ex$+"\"+dirfile$
	
	If Upper$(Right$(dirfile$,3))="WLV"

	file=ReadFile (filename$)
	LevelWidth=ReadInt(File)
	If levelwidth>121
		; WA3 VAULTS
		LevelWidth=LevelWidth-121
	EndIf

	LevelHeight=ReadInt(File)
	
	; skip tiles and other unused things
	SeekFile file,(FilePos(file)+(LevelWidth*LevelHeight)*(4*14)) + (4*3)
	
	; now get custom texture
	currentleveltexture=-1
	currentwatertexture=-1
	a$=ReadString$(file)
	For i=0 To nofleveltextures-1
		If a$=levelTextureName$(i) Then CurrentLevelTexture=i
	Next
	If currentleveltexture=-1
		LevelTextureCustomName$=a$
	EndIf
	
	a$=ReadString$(file)
	For i=0 To nofwatertextures-1
		If a$=waterTextureName$(i) Then CurrentwaterTexture=i
	Next
	If currentwatertexture=-1
		WaterTextureCustomName$=a$
	EndIf
	
	; found custom leveltexture
	If currentleveltexture=-1 And FileType(globaldirname$+"\custom\leveltextures\leveltex "+LevelTextureCustomName$+".bmp")=1
		isthere=False
		For ii=0 To cc
			If CustomContentFile$(ii) = globaldirname$+"\custom\leveltextures\leveltex "+LevelTextureCustomName$+".bmp"
				isthere=True
			EndIf
		Next
		If isthere=False
			CustomContentFile$(cc) = globaldirname$+"\custom\leveltextures\leveltex "+LevelTextureCustomName$+".bmp"
			CustomContentFile$(cc+1) = globaldirname$+"\custom\leveltextures\backgroundtex "+LevelTextureCustomName$+"1.bmp"
			CustomContentFile$(cc+2) = globaldirname$+"\custom\leveltextures\backgroundtex "+LevelTextureCustomName$+"2.bmp"
			cc = cc + 3
		EndIf
		isthere=False
	EndIf
	
	If currentwatertexture=-1 And FileType(globaldirname$+"\custom\leveltextures\watertex "+WaterTextureCustomName$+".jpg")
		For ii=0 To cc
			If CustomContentFile$(ii) = globaldirname$+"\custom\leveltextures\watertex "+WaterTextureCustomName$+".jpg"
				isthere=True
			EndIf
		Next
		If isthere=False
			CustomContentFile$(cc) = globaldirname$+"\custom\leveltextures\watertex "+WaterTextureCustomName$+".jpg"
			cc=cc+1
		EndIf
		isthere=True
	EndIf
		
	
	;OBJECTS
	NofObjects=ReadInt(file)
	For i=0 To NofObjects-1
		
		Dest=i
		ObjectModelName$(i)=ReadString$(file)
		ObjectTextureName$(i)=ReadString$(file)
		
		; skip unused adjusters
		SeekFile file,FilePos(file)+((57+10)*4)

		For k=0 To 3
			ObjectTextData$(Dest,k)=ReadString$(file)
		Next
		
		; skip unused adjusters
		SeekFile file,FilePos(file)+(35*4)
		
		ReadString(file)
		ReadString(file)
		
		For k=0 To 30
			ReadString(file)
		Next
		
		If Left$(ObjectTextureName$(Dest),1)<>"!"
			For ii=0 To cc
				If CustomContentFile$(ii)=ObjectTextureName$(Dest)
					isthere=True
				EndIf
			Next
			If FileType(ObjectTextureName$(Dest))=1 And isthere=False
				CustomContentFile$(cc) = ObjectTextureName$(Dest)
				cc=cc+1
			EndIf
			isthere=False
		ElseIf Left$(ObjectTextureName$(Dest),1)="?"
			If Lower(Right(ObjectTextureName$(Dest),4))=".jpg" Or Lower(Right(ObjectTextureName$(Dest),4))=".bmp" Or Lower(Right(ObjectTextureName$(Dest),4))=".png"
				tname$="UserData\Custom\Objecttextures\"+Right(ObjectTextureName$(Dest),Len(ObjectTextureName$(Dest))-1)
			Else
				tname$="UserData\Custom\Objecttextures\"+Right(ObjectTextureName$(Dest),Len(ObjectTextureName$(Dest))-1)+".jpg"
			EndIf
			For ii=0 To cc
				If CustomContentFile$(ii)=tname$
					isthere=True
				EndIf
			Next
			If FileType(tname)=1 And isthere=False
				CustomContentFile$(cc)=tname$
				cc=cc+1
			EndIf
			isthere=False				
		EndIf
		
		If ObjectModelName$(Dest)="!CustomModel"
			;If FileType("UserData\Custom\Models\"+ObjectTextData(Dest,0)+".3ds")<>1 Or FileType("UserData\Custom\Models\"+ObjectTextData(Dest,0)+".jpg")<>1
			;	ObjectTextData(Dest,0)="Default"
			;EndIf
			For ii=0 To cc
				If CustomContentFile$(ii)="UserData\Custom\Models\"+ObjectTextData(Dest,0)+".3ds"
					isthere=True
				EndIf
			Next
			If isthere=False
			CustomContentFile$(cc)="UserData\Custom\Models\"+ObjectTextData(Dest,0)+".3ds"
			CustomContentFile$(cc+1)="UserData\Custom\Models\"+ObjectTextData(Dest,0)+".jpg"
			cc=cc+2
			EndIf
			isthere=False
		EndIf
		
	Next
	
	CloseFile file
	EndIf
	
	If Upper$(dirfile$)="MASTER.DAT"
		file=ReadFile (filename$)
		ReadString$(file)
		For i=0 To 4
				ReadString$(file)
		Next
		ReadString$(file) ;user (not loaded)
			
		CustomIconName$=ReadString$(file)
		If CustomIconName$<>"Standard" And FileType(globaldirname$+"\custom\icons\icons "+CustomIconName$+".bmp")=1
			CustomContentFile(cc)=globaldirname$+"\custom\icons\icons "+CustomIconName$+".bmp"
			cc=cc+1
		EndIf
		CloseFile file
	EndIf
	
	
	Until dirfile$=""
	
	NofCustomContentFiles=cc
	
	;For i=0 To cc-1
	;	Print "Custom Content is: " + CustomContentFile$(i)
	;Next
	
End Function

Function decode$(ex$)
	output$=""
	For i=1 To Len(ex$)
		b=Asc(Mid$(ex$,i,1))
		If b>=97 And b<=122
			output$=output$+Chr$(b-1)
		Else
			output$=output$+Chr$(b)
		EndIf
	Next
	Return output$
End Function


Function SpecialFolderLocation$(folderID)

	str_bank = CreateBank(256)
	If GetSpecialFolder(folderID,str_bank) = 1 Then
		For loop = 0 To 255
			byte = PeekByte(str_bank,loop)
			If byte <> 0
				location$ = location$ + Chr(byte)
			EndIf
		Next
	Else
		location$ = ""
	EndIf
	
	FreeBank str_bank
	
	Return location$

End Function

Function ObjectIndexEditorToGameInner(Index)

	Result=Index
	
	For i=0 To Index-1
		If ObjectHasShadow(i)
			Result=Result+1
		EndIf
		Result=Result+ObjectCountAccessories(i)
	Next
	
	Return Result

End Function

Function ObjectIndexEditorToGame(Index, PlayerIndex)

	If Index=-2
		Return PlayerIndex
	Else
		Return ObjectIndexEditorToGameInner(Index)
	EndIf

End Function

Function ObjectIndexGameToEditorInner(Index)

	Result=Index
	
	For i=0 To Result-1
		If ObjectHasShadow(i)
			Result=Result-1
		EndIf
		Result=Result-ObjectCountAccessories(i)
	Next
	
	Return Result

End Function

Function ObjectIndexGameToEditor(Index, PlayerIndex)

	Result=ObjectIndexGameToEditorInner(Index)
	If Result=PlayerIndex
		Return -2
	Else
		Return Result
	EndIf
	
	;If Index=PlayerIndex
	;	Return -2
	;Else
	;	Return ObjectIndexGameToEditorInner(Index)
	;EndIf

End Function

; reflects the in-game logic for spawning shadows
Function ObjectHasShadow(Dest)

	Select ObjectModelName$(Dest)

	Case "!StinkerWee","!Scritter","!BabyBoomer","!RainbowBubble"
		Return True
	Case "!Turtle","!Thwart","!Troll"
		Return True
	Case "!Chomper","!Bowler","!Kaboom"
		Return True
	Case "!Crab"
		Return True
	Case "!FlipBridge"
		Return True
	Case "!NPC" ; Normally this shadow is created by the CreateStinkerModel function.
		Return True
	Default
		Return False
	End Select

End Function


Function ObjectCountAccessories(Dest)

	If ObjectModelName$(Dest)="!NPC"
		Code$="!T"
		If ObjectData(Dest,0)<10
			Code$=Code$+"00"+Str$(ObjectData(Dest,0))
		Else If ObjectData(Dest,0)<100
			Code$=Code$+"0"+Str$(ObjectData(Dest,0))
		Else
			Code$=Code$+Str$(ObjectData(Dest,0))
		EndIf
		Code$=Code$+Chr$(65+ObjectData(Dest,1))
		If ObjectData(Dest,2)>0
			If ObjectData(Dest,2)<10
				 Code$=Code$+"00"+Str$(ObjectData(Dest,2))
			Else If ObjectData(Dest,2)<100
				 Code$=Code$+"0"+Str$(ObjectData(Dest,2))
			Else
				 Code$=Code$+""+Str$(ObjectData(Dest,2))
			EndIf
			Code$=Code$+Chr$(64+ObjectData(dest,3))+"0"
		EndIf
		If ObjectData(Dest,4)>0 And ObjectData(Dest,2)>0 Then Code$=Code$+" "
		If ObjectData(Dest,4)>0
			If ObjectData(Dest,4)<10
				 Code$=Code$+"00"+Str$(ObjectData(Dest,4))
			Else If ObjectData(Dest,4)<100
				 Code$=Code$+"0"+Str$(ObjectData(Dest,4))
			Else
				 Code$=Code$+""+Str$(ObjectData(Dest,4))
			EndIf
			Code$=Code$+Chr$(65+ObjectData(dest,5))+"0"
		EndIf
		
		Return CodeCountAccessories(Code$)
	Else
		Return 0
	EndIf

End Function


Function CodeCountAccessories(code$)

	Result=0
	
	For j=1 To (Len(code$)-5)/6
		Result=Result+1
	Next
	
	Return Result

End Function


Function ControlParticleEmitters(i)
	
	;If ObjectActive(i)=0 Then Return
	
	Select ObjectSubType(i)
	Case 1
		; steam
		If SimulatedObjectStatus(i)=0
			; not steaming - check if start
			If Rand(0,400)<=ObjectData(i,1)*2
				SimulatedObjectStatus(i)=1
			EndIf
		Else
			If Rand(0,200*ObjectData(i,1))<2
				
				SimulatedObjectStatus(i)=0
				
			EndIf
			Select ObjectData(i,2)
			Case 0
				If Rand(0,25)<ObjectData(i,1)*3-2 AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,0,.03,0,0,.01,0,0,0,100,3)
			Case 1
				If Rand(0,10)<ObjectData(i,1) AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,0,-.03,0,0,.01,0,0,0,100,3)
			Case 2
				If Rand(0,10)<ObjectData(i,1) AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,.03,0,0,0,.01,0,0,0,100,3)
			Case 3
				If Rand(0,10)<ObjectData(i,1) AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,-.03,0,0,0,.01,0,0,0,100,3)
			Case 4
				If Rand(0,10)<ObjectData(i,1) AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,0,0,.03,0,.01,0,0,0,100,3)
			Case 5
				If Rand(0,10)<ObjectData(i,1) AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,0,0,-.03,0,.01,0,0,0,100,3)
			End Select
					
				
		EndIf
	Case 2
		; splish
		If Rand (0,1000)<=ObjectData(i,1)*2
			AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.01,0,0,0,0,.01,0,0,0,100,4)
		EndIf
		
	Case 3
		; fountain
		Select ObjectData(i,2)
		Case 0
			If Rand(0,12)<ObjectData(i,1)*3-2 AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,Rnd(-.01,.01),Rnd(.05,.07),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 1
			If Rand(0,12)<ObjectData(i,1)*3-2 AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,Rnd(-.01,.01),-Rnd(0,.02),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 2
			If Rand(0,12)<ObjectData(i,1)*3-2 AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,Rnd(.05,.07),Rnd(.02,.01),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 3
			If Rand(0,12)<ObjectData(i,1)*3-2 AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,-Rnd(.05,.07),Rnd(.02,.01),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 4
			If Rand(0,12)<ObjectData(i,1)*3-2 AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,Rnd(-.01,.01),Rnd(.02,.01),Rnd(.05,.07),0,.001,0,-.001,0,100,3)
		Case 5
			If Rand(0,12)<ObjectData(i,1)*3-2 AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,Rnd(-.01,.01),Rnd(.02,.01),-Rnd(.05,.07),0,.001,0,-.001,0,100,3)
		End Select
		
	Case 4
		; sparks
		If Rand(0,1000)<ObjectData(i,1)*ObjectData(i,1)
			For j=0 To ObjectData(i,1)*30
				Select ObjectData(i,2)
				Case 0
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0.01,.2,Rnd(-.01,.01),Rnd(.09,.11),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 1
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0.01,.2,Rnd(-.01,.01),-Rnd(0,.02),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 2
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0.01,.2,Rnd(.01,.04),Rnd(.03,.01),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 3
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0.01,.2,-Rnd(.01,.04),Rnd(.03,.01),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 4
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0.01,.2,Rnd(-.01,.01),Rnd(.03,.01),Rnd(.01,.04),0,.0001,0,-.0015,0,50,3)
				Case 5
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0.01,.2,Rnd(-.01,.01),Rnd(.03,.01),-Rnd(.01,.04),0,.0001,0,-.0015,0,50,3)
				End Select
			Next
		EndIf
	Case 5
		; blinker
		If (ObjectData(i,4)=0 And Rand(0,200)<ObjectData(i,1)) Or (ObjectData(i,4)=1 And LevelTimer Mod (500-ObjectData(i,1)*100)=0)

			AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0.01,.4,0,0,0,0,.0005,0,0,0,100,3)
			
		EndIf
		
	Case 6
		; circle
		If (ObjectData(i,4)=0 And Rand(0,200)<ObjectData(i,1)) Or (ObjectData(i,4)=1 And LevelTimer Mod (500-ObjectData(i,1)*100)=0)

			For j=0 To 44
				Select ObjectData(i,2)
				Case 0,1
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,.01*ObjectData(i,1)*Cos(j*8),0,.01*ObjectData(i,1)*Sin(j*8),0,.001,0,0,0,100,3)
				Case 2,3
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,0,.01*ObjectData(i,1)*Cos(j*8),.01*ObjectData(i,1)*Sin(j*8),0,.001,0,0,0,100,3)
				Case 4,5
					AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,.01*ObjectData(i,1)*Cos(j*8),.01*ObjectData(i,1)*Sin(j*8),0,0,.001,0,0,0,100,3)
				
				End Select
			Next
		EndIf

		
	Case 7
		; spiral
		Select ObjectData(i,2)
		Case 0
			AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,.02*ObjectData(i,1)*Cos((Leveltimer*ObjectData(i,1)) Mod 360),0,.02*ObjectData(i,1)*Sin((Leveltimer*ObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 2
			AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,0,.02*ObjectData(i,1)*Cos((Leveltimer*ObjectData(i,1)) Mod 360),.02*ObjectData(i,1)*Sin((Leveltimer*ObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 4
			AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,.02*ObjectData(i,1)*Cos((Leveltimer*ObjectData(i,1)) Mod 360),.02*ObjectData(i,1)*Sin((Leveltimer*ObjectData(i,1)) Mod 360),0,0,.001,0,0,0,100,3)
		
		Case 1
			AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,-.02*ObjectData(i,1)*Cos((Leveltimer*ObjectData(i,1)) Mod 360),0,.02*ObjectData(i,1)*Sin((Leveltimer*ObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 3
			AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,0,-.02*ObjectData(i,1)*Cos((Leveltimer*ObjectData(i,1)) Mod 360),.02*ObjectData(i,1)*Sin((Leveltimer*ObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 5
			AddParticle(ObjectData(i,0),ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,-.02*ObjectData(i,1)*Cos((Leveltimer*ObjectData(i,1)) Mod 360),.02*ObjectData(i,1)*Sin((Leveltimer*ObjectData(i,1)) Mod 360),0,0,.001,0,0,0,100,3)

		End Select
	


		
	




	


	
	End Select

End Function


Function ControlWaterfall(i)

	If ObjectYawAdjust(i)=0
		k1=1
		k2=0
	EndIf
	If ObjectYawAdjust(i)=90
		k1=0
		k2=1
	EndIf
	If ObjectYawAdjust(i)=-90 Or ObjectYawAdjust(i)=270
		k1=0
		k2=-1
	EndIf

	If Rand(0,100)<10  	
		If ObjectData(i,0)=1
			AddParticle(1,ObjectX(i)+k1*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))+k2*Rnd(0.55,0.6),ObjectZAdjust(i),-ObjectY(i)+k2*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))-k1*Rnd(0.55,0.6),0,.11,k1*Rnd(-.005,0.005)+k2*Rnd(0,.005),Rnd(0.01,0.03),-k1*Rnd(0,.001)+k2*Rnd(-.005,0.005),0,0,0,-0.0004,0,100,3)
		Else 
			AddParticle(5,ObjectX(i)+k1*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))+k2*Rnd(0.55,0.6),ObjectZAdjust(i),-ObjectY(i)+k2*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))-k1*Rnd(0.55,0.6),0,.11,k1*Rnd(-.005,0.005)+k2*Rnd(0,.005),Rnd(0.01,0.03),-k1*Rnd(0,.001)+k2*Rnd(-.005,0.005),0,0,0,-0.0004,0,100,3)
		EndIf
	EndIf

	If Rand(0,100)<3 
		If ObjectData(i,0)=0
			AddParticle(6,ObjectX(i)+k1*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(ObjectZAdjust(i),ObjectZAdjust(i)+ObjectZScale(i)/2.0),-ObjectY(i)+k2*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		Else If ObjectData(i,0)=1
			AddParticle(24,ObjectX(i)+k1*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(ObjectZAdjust(i),ObjectZAdjust(i)+ObjectZScale(i)/2.0),-ObjectY(i)+k2*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		Else
			AddParticle(27,ObjectX(i)+k1*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(ObjectZAdjust(i),ObjectZAdjust(i)+ObjectZScale(i)/2.0),-ObjectY(i)+k2*Rnd(-.5*ObjectXScale(i),.5*ObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		EndIf
	EndIf
	If Rand(0,100)<10 
		If ObjectData(i,0)=1
			AddParticle(32,ObjectX(i)+k1*Rnd(-.35*ObjectXScale(i),.35*ObjectXScale(i))+k2*0.5,(-.2*ObjectZScale(i))+ObjectZAdjust(i),-ObjectY(i)+k2*Rnd(-.35*ObjectXScale(i),.35*ObjectXScale(i))-k1*0.5,0,.1,0,0,0,0,.012,0,0,0,100,4)
		Else 
			AddParticle(4,ObjectX(i)+k1*Rnd(-.35*ObjectXScale(i),.35*ObjectXScale(i))+k2*0.5,(-.2*ObjectZScale(i))+ObjectZAdjust(i),-ObjectY(i)+k2*Rnd(-.35*ObjectXScale(i),.35*ObjectXScale(i))-k1*0.5,0,.2,0,0,0,0,.012,0,0,0,100,4)
		EndIf
	EndIf


End Function


Function ControlVoidTexture(i)
	
	PositionTexture voidtexture,((leveltimer/10) Mod 100)/100.0,((leveltimer/10) Mod 100)/100.0
	
End Function


Function ControlTeleporter(i)

	If Rand(0,100)<5 And (SimulatedObjectActive(i)>0 Or SimulationLevel<2)
		a=Rand(0,360)
		b#=Rnd(0.002,0.006)
		AddParticle(23,ObjectX(i)+0.5*Sin(a),0,-ObjectY(i)-0.5*Cos(a),0,.2,b*Sin(a),0.015,-b*Cos(a),1,0,0,0,0,150,3)
	EndIf
	
	MyId=CalculateEffectiveID(i)
	
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+.5
	If MyId Mod 5<3
		; standard effect
		;ScaleEntity ObjectEntity(i),1,ObjectActive(i)/1001.0,1
	Else If MyId Mod 5=3
		; unstable effect
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+4.5
		SimulatedObjectXScale(i)=0.6+0.4*Sin ((LevelTimer/7) Mod 360)
		;SimulatedObjectZScale(i)=ObjectActive(i)/1001.0
		SimulatedObjectYScale(i)=0.6+0.4*Cos ((LevelTimer/2) Mod 360)
		;ScaleEntity ObjectEntity(i),0.6+0.4*Sin ((LevelTimer/7) Mod 360),,
	Else
		; pulsating effect
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+9.5
		SimulatedObjectXScale(i)=0.6+0.4*Sin ((LevelTimer) Mod 360)
		;SimulatedObjectZScale(i)=ObjectActive(i)/1001.0
		SimulatedObjectYScale(i)=0.6+0.4*Sin ((LevelTimer) Mod 360)

		;ScaleEntity ObjectEntity(i),,ObjectActive(i)/1001.0,)
	EndIf

	;SimulateObjectRotation(i)
	;SimulateObjectScale(i)

End Function


Function ControlObstacle(i)

	; (no control, but used to adjust leveltilelogic)
	If ObjectModelName$(i)="!Obstacle03" ; volcano
		If Rand(0,40)=0
			AddParticle(Rand(24,26),ObjectX(i)+Rnd(-.7,.7),1.8+ObjectZAdjust(i),-ObjectY(i)+Rnd(-.9,.7),0,.2,0,Rnd(0.01,0.03),0,0,.03,0,0,0,100,3)
		EndIf
		If Rand(0,10)=0
			If Rand(0,5)=0
				part22=1
			Else
				part22=0
			EndIf
			AddParticle(part22,ObjectX(i)+Rnd(-.3,.3),1.5+ObjectZAdjust(i),-ObjectY(i)+Rnd(-.5,.3),0,.6,0,Rnd(0.01,0.03),0,0,.01,0,0,0,100,3)
		EndIf
	Else If ObjectModelName$(i)="!Obstacle04" ; acid pool
		If Rand(0,100)=0
			AddParticle(27,ObjectX(i)+Rnd(-.5,.5),1+ObjectZAdjust(i),-ObjectY(i)+Rnd(-.7,.5),0,.11,0,Rnd(0.01,0.03),0,0,.01,0,0,0,100,3)
		EndIf
		If Rand(0,100)=0
			AddParticle(35,ObjectX(i)+Rnd(-.3,.6),2.0+ObjectZAdjust(i),-ObjectY(i)+Rnd(-.6,.3),0,.04,0,0,0,0,.001,0,0,0,100,4)
		EndIf
		
	Else If ObjectModelName$(i)="!Obstacle45" ; waterwheel
		If ObjectYawAdjust(i)=0
			SimulatedObjectRoll(i)=SimulatedObjectRoll(i)+2
		EndIf
		If ObjectYawAdjust(i)=180
			SimulatedObjectRoll(i)=SimulatedObjectRoll(i)-2
		EndIf
		If ObjectYawAdjust(i)=90
			SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+2
		EndIf
		If ObjectYawAdjust(i)=270
			SimulatedObjectPitch(i)=SimulatedObjectPitch(i)-2
		EndIf
	
	Else If ObjectModelName$(i)="!Obstacle48" ; UFO - by mistake in here
		If ObjectData(i,0)=0
			SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+1
		EndIf
	Else If ObjectModelName$(i)="!Crystal" ; UFO - by mistake in here
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+1

					
	

	EndIf
	If ObjectModelName$(i)="!CustomModel"	; Custom Model 
		ControlCustomModel(i)
	EndIf

	;SimulateObjectRotation(i)

End Function


Function ControlCustomModel(i)

;	If ObjectOldX(i)=-999;0 And ObjectOldY(i)=0 And ObjectOldZ(i)=0
;		ObjectOldX(i)=ObjectXAdjust(i)
;		ObjectOldY(i)=ObjectYAdjust(i)
;		ObjectOldZ(i)=ObjectZAdjust(i)
;	EndIf
	


	;ObjectScaleAdjust(i)*(1.5+0.8*Sin((leveltimer+ObjectData(i,7)+30) Mod 360))
	
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+ObjectData(i,0)
	If SimulatedObjectYaw(i)>360 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-360
	If SimulatedObjectYaw(i)<0 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+360
	
	SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+ObjectData(i,1)
	If SimulatedObjectPitch(i)>360 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)-360
	If SimulatedObjectPitch(i)<0 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+360
	
	SimulatedObjectroll(i)=SimulatedObjectroll(i)+ObjectData(i,2)
	If SimulatedObjectroll(i)>360 Then SimulatedObjectroll(i)=SimulatedObjectroll(i)-360
	If SimulatedObjectroll(i)<0 Then SimulatedObjectroll(i)=SimulatedObjectroll(i)+360
	
	If ObjectData(i,3)>0
		; Technically these ObjectX/Y/ZAdjust instances should be OldX/Y/Z. But no one's crazy enough to edit OldX/Y/Z directly, right?
		SimulatedObjectXAdjust(i)=ObjectXAdjust(i)+Float(ObjectData(i,3))*Sin((leveltimer Mod 36000)*Float(ObjectData(i,6)/100.0))
	Else
		SimulatedObjectXAdjust(i)=ObjectXAdjust(i)+Float(ObjectData(i,3))*Cos((leveltimer Mod 36000)*Float(ObjectData(i,6)/100.0))
	EndIf
	If ObjectData(i,4)>0
		SimulatedObjectYAdjust(i)=ObjectYAdjust(i)+Float(ObjectData(i,4))*Sin((leveltimer Mod 36000)*Float(ObjectData(i,7)/100.0))
	Else
		SimulatedObjectYAdjust(i)=ObjectYAdjust(i)+Float(ObjectData(i,4))*Cos((leveltimer Mod 36000)*Float(ObjectData(i,7)/100.0))
	EndIf
	If ObjectData(i,5)>0
		SimulatedObjectZAdjust(i)=ObjectZAdjust(i)+Float(ObjectData(i,5))*Sin((leveltimer Mod 36000)*Float(ObjectData(i,8)/100.0))
	Else
		SimulatedObjectZAdjust(i)=ObjectZAdjust(i)+Float(ObjectData(i,5))*Cos((leveltimer Mod 36000)*Float(ObjectData(i,8)/100.0))
	EndIf
	
	;SimulateObjectPosition(i)
	; object rotation is simulated by ControlObstacle

End Function


Function ControlGoldStar(i)

	SimulatedObjectZ(i)=.8

	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2

	a=Rand(0,300)

	If a<50
		AddParticle(19,ObjectTileX(i)+0.5,.7+ObjectZAdjust(i),-ObjectTileY(i)-0.5,Rand(0,360),0.16,Rnd(-.015,.015),0.03,Rnd(-.015,.015),0,0.001,0,-.00025,0,100,3)
	EndIf
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function


Function ControlGoldCoin(i)

	If ObjectActive(i)<1001 And ObjectActive(i)>0
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10
		
		If ObjectActive(i)>600
			SimulatedObjectZ(i)=.2+Float(1000-ObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=1.2
		EndIf
		If ObjectActive(i)=400
			; Little Spark
			For j=1 To 20
				AddParticle(19,ObjectTileX(i)+0.5,1.6,-ObjectTileY(i)-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
;		If ObjectActive(i)<600
;			ObjectScaleXAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleYAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleZAdjust(i)=Float(ObjectActive(i))/600.0
;
;		EndIf
	Else
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+3
		SimulatedObjectZ(i)=0
	EndIf
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function


Function ControlGem(i)

	If ObjectActive(i) Mod 2=1 Then ShowEntity ObjectEntity(i)
	
	If ObjectActive(i)<1001 And ObjectActive(i)>0
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If ObjectActive(i)>600
			SimulatedObjectZ(i)=.6+Float(1000-ObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=1.6
		EndIf
		If ObjectActive(i)=400
			
			; Little Spark
			For j=1 To 20
				AddParticle(19,ObjectTileX(i)+0.5,1.6,-ObjectTileY(i)-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
			If WaEpisode=1 And (adventurecurrentnumber>=200 And adventurecurrentnumber<=203) 
				; not in pacman level or WA1
			
			Else If ObjectID(i)=-1
				If ObjectData(i,0)=0 Then AddParticle(14,ObjectTileX(i)+0.5,1.6,-ObjectTileY(i)-0.5,0,1,0,0.01,0,0,.01,0,0,0,50,3)
				If ObjectData(i,0)=1 Then AddParticle(15,ObjectTileX(i)+0.5,1.6,-ObjectTileY(i)-0.5,0,1,0,0.01,0,0,.01,0,0,0,50,3)
			EndIf
		EndIf
;		If ObjectActive(i)<600
;			ObjectScaleXAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleYAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleZAdjust(i)=Float(ObjectActive(i))/600.0
;
;		EndIf
		
		
		
	Else
		If ObjectData(i,0)=2
			If Rand(0,10)<3
				
				AddParticle(16+ObjectData(i,1)+Rand(0,1)*8,ObjectTileX(i)+.5,Rnd(0,1),-ObjectTileY(i)-.5,0,.01,Rnd(-.01,.01),Rnd(-.01,.01),Rnd(0,.02),Rnd(-4,4),.01,0,0,0,70,3)
			EndIf
		EndIf
	
		If ObjectYaw(i)=0 And ObjectData(i,0)<>1
			ObjectRoll(i)=Rand(-10,10)
			ObjectYaw(i)=Rand(1,180)
	
		EndIf
		If ObjectData(i,0)=0 Or ObjectData(i,0)=2 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Rnd(1.8,2.2)
		If ObjectData(i,0)=1 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+Rnd(2,3)+(i Mod 3)/3.0
		SimulatedObjectZ(i)=.4
	EndIf
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)
	
End Function


Function ControlKey(i)

	If ObjectActive(i)<1001 And ObjectActive(i)>0
	
		; picked up animation
		
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If ObjectActive(i)>600
			SimulatedObjectZ(i)=.6+2*Float(1000-ObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.6
		EndIf
		If ObjectActive(i)=400
			; Little Spark
			For j=1 To 60
				AddParticle(Rnd(16,23),ObjectTileX(i)+0.5,2.6,-ObjectTileY(i)-0.5,Rand(0,360),0.2,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
;		If ObjectActive(i)<600
;			ObjectScaleXAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleYAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleZAdjust(i)=Float(ObjectActive(i))/600.0
;
;		EndIf
		
		
		
	Else
	;	ObjectYaw(i)=ObjectYaw(i)+2
		If ObjectModelName$(i)="!KeyCard"
			SimulatedObjectYaw(i)=((leveltimer) Mod 90)*4
		Else
			SimulatedObjectRoll(i)=30*Sin((leveltimer) Mod 360)
		EndIf
		SimulatedObjectZ(i)=.4
	EndIf
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function


Function ControlCustomItem(i)
	
	If ObjectActive(i)<1001 And ObjectActive(i)>0
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10
		


		If ObjectActive(i)>600
			SimulatedObjectZ(i)=.6+2*Float(1000-ObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.6
		EndIf
		If ObjectActive(i)=400
			; Little Spark
			For j=1 To 60
				AddParticle(Rnd(16,23),ObjectTileX(i)+0.5,2.6,-ObjectTileY(i)-0.5,Rand(0,360),0.2,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
;		If ObjectActive(i)<600
;			ObjectScaleXAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleYAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleZAdjust(i)=Float(ObjectActive(i))/600.0
;
;
;		EndIf
		
		
		
		
	Else
		
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Cos(Leveltimer Mod 360)
		
		SimulatedObjectZ(i)=.5

	EndIf
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)
	
End Function


Function ControlSigns(i)
	
	
	Select ObjectData(i,2)
	
	Case 0
		; nuthin'
	Case 1
		; Sway
		SimulatedObjectPitch(i)=5*Sin((leveltimer*1.5) Mod 360)
		SimulatedObjectYaw(i)=5*Sin((leveltimer/2) Mod 360)
		SimulatedObjectRoll(i)=5*Sin(leveltimer Mod 360)

	Case 2
		; Bounce
		SimulatedObjectZScale(i)=1.0+0.15*Sin((Leveltimer*4) Mod 360)
		SimulatedObjectRoll(i)=5*Sin((leveltimer*2) Mod 360)
	Case 3
		; turn
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+3
	End Select

	;SimulateObjectRotation(i)
	;SimulateObjectScale(i)
		

End Function


Function ControlRetroRainbowCoin(i)
	
	If SimulatedObjectActive(i)<1001
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10
		


		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=1.2+Float(1000-ObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.2
		EndIf
		If SimulatedObjectActive(i)=400
			; Little Spark
			For j=1 To 20
				AddParticle(19,ObjectTileX(i)+0.5,2.6,-ObjectTileY(i)-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
		If SimulatedObjectActive(i)<600
			SimulatedObjectScaleXAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleYAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleZAdjust(i)=Float(SimulatedObjectActive(i))/600.0
		EndIf
		
		
		
	Else
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+3
		SimulatedObjectZ(i)=0
	EndIf
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function


Function ControlWisp(i)

	EntityFX ObjectEntity(i),1
	
	If Leveltimer Mod 360 =0
		a=Rand(0,100)
		If a<60
			SimulatedObjectStatus(i)=0
		Else If a<80
			
			SimulatedObjectStatus(i)=1
		Else If a<90
		

			Simulatedobjectstatus(i)=2
		Else
		

			Simulatedobjectstatus(i)=3
		EndIf
		
	EndIf
	If SimulatedObjectStatus(i)=0
		SimulatedObjectZ(i)=0.6+.1*Sin(leveltimer Mod 360)
		SimulatedObjectYaw(i)=30*Sin((leveltimer*4) Mod 360)
		SimulatedObjectRoll(i)=20*Sin((leveltimer*2) Mod 360)
	Else If SimulatedObjectStatus(i)=1

		SimulatedObjectZ(i)=0.6+.1*Sin(leveltimer Mod 360)
		SimulatedObjectYaw(i)=180*Sin((leveltimer) Mod 360)
		SimulatedObjectRoll(i)=20*Sin((leveltimer*2) Mod 360)
	Else If SimulatedObjectStatus(i)=2

		SimulatedObjectZ(i)=0.6+.1*Sin(leveltimer Mod 360)
		SimulatedObjectYaw(i)=360*Sin((leveltimer/2) Mod 360)
		SimulatedObjectRoll(i)=180*Sin((leveltimer*2) Mod 360)
	Else If SimulatedObjectStatus(i)=3
		SimulatedObjectZ(i)=0.6+.4*Sin(leveltimer Mod 180)
		SimulatedObjectYaw(i)=60*Sin((leveltimer*4) Mod 360)
		SimulatedObjectRoll(i)=20*Sin((leveltimer*2) Mod 360)


	EndIf
	If Rand(0,100)<3 And ObjectActive(i)=1001	AddParticle(Rand(16,23),ObjectTileX(i)+0.5,.7,-ObjectTileY(i)-0.5,Rand(0,360),0.16,Rnd(-.015,.015),0.03,Rnd(-.015,.015),0,0.001,0,-.00025,0,100,3)

	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function


Function ControlRetroZbotUfo(i)

	If ObjectType(i)<>423 And ObjectType(i)<>430
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2
	EndIf

	;SimulateObjectRotation(i)

End Function


Function ControlRetroLaserGate(i)
	
	If ObjectYawAdjust(i)=0 Or ObjectYawAdjust(i)=180
		SimulatedObjectPitch(i)=(SimulatedObjectPitch(i)+2) Mod 360
	Else
		SimulatedObjectRoll(i)=(SimulatedObjectRoll(i)+2) Mod 360
	EndIf
	
	;SimulateObjectRotation(i)
			
End Function


Function ControlTentacle(i)

	If SimulatedObjectData(i,0)=0 Then SimulatedObjectData(i,0)=Rand(-10,10)
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Float(SimulatedObjectData(i,0))/10.0
	
	;SimulateObjectRotation(i)
	
End Function


Function ControlRainbowBubble(i)

	If SimulatedObjectStatus(i)=0
		SimulatedObjectStatus(i)=1
		SimulatedObjectData(i,2)=Rand(0,360)
	EndIf

	EntityAlpha ObjectEntity(i),.8
	EntityBlend ObjectEntity(i),3
	
	l=leveltimer/4+SimulatedObjectData(i,2)
	
	SimulatedObjectXScale(i)=0.5-0.1*Sin((leveltimer + SimulatedObjectData(i,2)) Mod 360)

	SimulatedObjectYScale(i)=0.5-0.1*Sin((leveltimer + SimulatedObjectData(i,2)) Mod 360)

	SimulatedObjectZScale(i)=0.6+0.2*Sin((leveltimer + SimulatedObjectData(i,2)) Mod 360)
	
	SimulatedObjectPitch(i)=(SimulatedObjectPitch(i)+1) Mod 360
	SimulatedObjectYaw(i)=360*Sin(l Mod 360)
	SimulatedObjectRoll(i)=180*Cos(l Mod 360)
	
	SimulatedObjectZ(i)=0.5+0.3*Abs(Sin((leveltimer + SimulatedObjectData(i,2)) Mod 360))
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)
	;SimulateObjectScale(i)

End Function


Function ControlBowler(i)

	Direction=SimulatedObjectData(i,0)
	If SimulatedObjectData(i,1)<>2
		Direction=Direction*2
	EndIf
	SimulatedObjectYawAdjust(i)=(-45*Direction +3600) Mod 360
	
	SimulatedObjectPitch2(i)=(SimulatedObjectPitch2(i)+Rnd(3,5)) Mod 360
	SimulatedObjectZ(i)=.4+.4*Sin((Leveltimer*4) Mod 180)
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function


Function ControlMothership(i)

	SimulatedObjectYaw(i)=((SimulatedObjectYaw(i)+.3) Mod 360)
	
	If SimulatedObjectMovementSpeed(i)<>10 ; first time
		SimulatedObjectData(i,1)=-1
		SimulatedObjectYawAdjust(i)=0
		SimulatedObjectMovementSpeed(i)=10
		SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectZ(i)=4
		
		SimulateObjectPosition(i)
		
		;CreateShadow(i,ObjectScaleAdjust(i)*5)
	EndIf
	
	;SimulateObjectRotation(i)
	
End Function


Function ControlRubberducky(i)

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=-1
		SimulatedObjectData(i,1)=Rand(1,3)
		SimulatedObjectData(i,2)=Rand(0,360)
	EndIf	
		
	SimulatedObjectroll(i)=1*SimulatedObjectData(i,1)*Sin((LevelTimer+SimulatedObjectData(i,2)) Mod 360)
	SimulatedObjectpitch(i)=2*SimulatedObjectData(i,1)*Cos((LevelTimer*3+SimulatedObjectData(i,2))  Mod 360)
	
	;SimulateObjectRotation(i)

End Function


Function ControlGloveCharge(i)

	SimulatedObjectZ(i)=0.04
	
	myparticle=16+ObjectData(i,0)
	
	j2=Rand(0,359)
	If objectsubtype(i)=1 ; one time charge
		If leveltimer Mod 5 = 0
			AddParticle(myparticle,ObjectTileX(i)+.5+.1*Sin(j2*3),0,-ObjectTileY(i)-.5-.1*Cos(j2*3),0,.2,0,.03,0,4,0,0,0,0,50,3)
		EndIf
	Else If objectsubtype(i)=0; multi-charge
		If leveltimer Mod 2 = 0
			AddParticle(myparticle,ObjectTileX(i)+.5+.3*Sin(j2*3),0,-ObjectTileY(i)-.5-.3*Cos(j2*3),0,.3,0,.04,0,4,0,0,0,0,50,3)
		EndIf
	EndIf
	
	;SimulateObjectPosition(i)
	
End Function


Function ControlWindmillRotor(i)

	If ObjectYawAdjust(i)=0 Or ObjectYawAdjust(i)=180
		SimulatedObjectRoll(i)=SimulatedObjectRoll(i)+1
	Else 
		SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+1
	EndIf
	;SimulatedObjectZ(i)=5.65
	ObjectZAdjust(i)=5.65
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function


Function ControlIceFloat(i)	
	SimulatedObjectPitch(i)=2*ObjectData(i,2)*Sin((LevelTimer + ObjectData(i,1)) Mod 360)
	SimulatedObjectRoll(i)=3*ObjectData(i,3)*Cos((LevelTimer+ ObjectData(i,1))  Mod 360)
	
	;SimulateObjectRotation(i)

End Function


Function ControlPlantFloat(i)

	If SimulatedObjectData(i,2)=0 Then SimulatedObjectData(i,2)=Rand(1,360)

	l=leveltimer+SimulatedObjectData(i,2)
	EntityColor ObjectEntity(i),128+120*Cos(l Mod 360),128+120*Sin(l Mod 360),200+50*Cos(l Mod 360)
	
	;ObjectPitch(i)=4*ObjectData(i,2)*Sin((LevelTimer + ObjectData(i,1)) Mod 360)
	;Objectroll(i)=6*ObjectData(i,3)*Cos((LevelTimer+ ObjectData(i,1))  Mod 360)
	SimulatedObjectYaw(i)=leveltimer Mod 360
	
	;SimulateObjectRotation(i)

End Function


Function ControlBurstFlower(i)

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=1
		SimulatedObjectData(i,0)=Rand(0,360)
	EndIf
	
	SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)+1) Mod 720
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+.5*Sin(SimulatedObjectData(i,0)/2)
	SimulatedObjectXScale(i)=0.3+0.02*Cos(SimulatedObjectData(i,0)*2)
	SimulatedObjectYScale(i)=0.3+0.02*Cos(SimulatedObjectData(i,0)*2)

	If SimulatedObjectData(i,1)>=0 And Rand(0,100)<2 And ObjectIndigo(i)=0 AddParticle(7,ObjectX(i),0.5,-ObjectY(i),Rand(0,360),0.4,0,0.02,0,Rnd(0,2),.01,0,0,0,50,4)

	If SimulatedObjectData(i,1)<0 Then ObjectData(i,1)=ObjectData(i,1)+1
	
	;SimulateObjectRotation(i)
	;SimulateObjectScale(i)

End Function


Function ControlLurker(i)

	If SimulatedObjectData(i,0)=0
		; lurking
		If SimulatedObjectYawAdjust(i)<>0
			SimulatedObjectYaw(i)=SimulatedObjectYawAdjust(i)
			SimulatedObjectYawAdjust(i)=0
		EndIf
		SimulatedObjectPitch2(i)=180
		SimulatedObjectZ(i)=-0.1
		SimulatedObjectData(i,2)=-1
	EndIf
	
	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function


Function ControlSunSphere1(i)

	If SimulatedObjectData(i,9)=0
		SimulatedObjectData(i,9)=1
		
		SimulatedObjectData(i,7)=Rand(0,360)
		SimulatedObjectData(i,8)=Rand(50,100)
		;CreateSunSphere2(i)
	EndIf
	
	; in-game this uses ScaleAdjust as a multiplier, but this is pointless since it always gets set to 1.0 when nonzero
	SimulatedObjectZ(i)=1.5+0.8*Sin((leveltimer+SimulatedObjectData(i,7)+30) Mod 360)
	
	;SimulateObjectPosition(i)
	

End Function

Function ControlSunSphere2(i)
	SimulatedObjectZ(i)=1.5+0.8*Sin((leveltimer+SimulatedObjectData(i,7)) Mod 360)
	SimulatedObjectXScale(i)=0.5*(1+0.1*Cos(leveltimer Mod 360))
	SimulatedObjectYScale(i)=0.5*(1+0.1*Cos((leveltimer+30) Mod 360))
	SimulatedObjectZScale(i)=0.5*(1+0.1*Cos((leveltimer+60) Mod 360))


	
	If Rand(0,100)<3 AddParticle(Rand(16,23),SimulatedObjectX(i),SimulatedObjectZ(i),-SimulatedObjectY(i),Rand(0,360),0.16,Rnd(-.025,.025),Rnd(-.025,.025),Rnd(-.025,.025),0,0.001,0,0,0,100,3)

	;SimulateObjectPosition(i)
	;SimulateObjectScale(i)

End Function


Function ControlStinkerWee(i)

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		SimulatedObjectMovementSpeed(i)=35	
		;SimulatedObjectSubType(i)=0  ; -2 dying, -1 exiting, 0- asleep, 1-follow, 2-directive, 3-about to fall asleep (still walking), 4 caged
		;AnimateMD2 ObjectEntity(i),1,Rnd(.002,.008),217,219,1
		;SimulatedObjectCurrentAnim(i)=1 ; 1-asleep, 2-getting up, 3-idle, 4-wave, 5-tap, 6-walk, 7 sit down, 8-fly, 9-sit on ice	
		SimulatedObjectXScale(i)=0.025
		SimulatedObjectYScale(i)=0.025
		SimulatedObjectZScale(i)=0.025
		
		;SimulateObjectScale(i)
		
	EndIf
	
	If ObjectDead(i)=1
		; spinning out of control
		SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360
		;ObjectZ(i)=ObjectZ(i)+.01
		;ObjectSubType(i)=-2
		Return
	EndIf
	If ObjectDead(i)=3
		; drowning
		SimulatedObjectYaw(i)=90
		;ObjectZ(i)=ObjectZ(i)-.005
		;ObjectSubType(i)=-2
		Return
	EndIf

End Function

Function ControlStinkerWeeExit(i)
	If LevelTimer Mod 3 = 0
		AddParticle(Rand(16,23),ObjectTileX(i)+0.5+0.2*Sin(LevelTimer*10),0,-ObjectTileY(i)-0.5-0.2*Cos(LevelTimer*10),Rand(0,360),0.1,0,0.02,0,0,0.005,0,0,0,100,3)
	EndIf
End Function


Function ControlCrab(i)

	;subtype -0-male, 1-female
	;data1 - 0-normal,1-curious, 2- asleep, 3- disabled
	;status - 0 normal, 2 submerged

	If SimulatedObjectTileTypeCollision(i)=0
		; First time (should later be put into object creation at level editor)
		If ObjectSubType(i)=0
			; male
			SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
;			Select SimulatedObjectData(i,1)
;			Case 0
;				; normal
;				ObjectMovementType(i)=0
;			Case 1
;				; curious
;				ObjectMovementType(i)=14
;			Case 2,3
;				; asleep
;				ObjectMovementType(i)=0
;				AnimateMD2 ObjectEntity(i),3,1,48,49
;			End Select
			SimulatedObjectXScale(i)=0.006
			SimulatedObjectYScale(i)=0.006
			SimulatedObjectZScale(i)=0.006
			
			;SimulateObjectScale(i)
		Else
			;female
			SimulatedObjectTileTypeCollision(i)=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
;			Select ObjectData(i,1)
;			Case 0
;				; normal
;				ObjectMovementType(i)=32
;			Case 1
;				; curious
;				ObjectMovementType(i)=14
;			Case 2,3
;				; asleep
;				ObjectMovementType(i)=0
;				AnimateMD2 ObjectEntity(i),3,1,48,49
;			End Select

		EndIf
		
	EndIf
End Function


Function ControlTrap(i)

	SimulatedObjectZ(i)=0.04

	If ObjectActive(i)=1001 Or SimulationLevel<2
		If SimulatedObjectStatus(i)=0
			; currently off
			SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
			If SimulatedObjectTimer(i)<=0
				; turn on
				SimulatedObjectTimer(i)=ObjectTimerMax1(i)
				SimulatedObjectStatus(i)=1
			EndIf
		Else
			; currently on
			SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
			If SimulatedObjectTimer(i)<=0
				; turn off
				SimulatedObjectTimer(i)=ObjectTimerMax2(i)
				SimulatedObjectStatus(i)=0
			EndIf
		EndIf
	
		
		Select ObjectSubType(i)
			Case 0
				; fire - create particle when on
				
			If SimulatedObjectStatus(i)=1
				;If Rand(0,100)<50 AddParticle(2,ObjectX(i)+Rnd(-.1,.1),ObjectZAdjust(i),-ObjectY(i),0-Rnd(-.1,.1),.5,Rnd(-.005,.005),.05,Rnd(-.005,.005),0,.01,0,-.0001,0,50,0)
				If Rand(0,100)<50 AddParticle(2,ObjectX(i)+Rnd(-.1,.1),ObjectZAdjust(i),-ObjectY(i),0-Rnd(-.1,.1),.5,Rnd(-.005,.005),.05,Rnd(-.005,.005),0,.01,0,-.0001,0,50,4)
			EndIf
			
		End Select
	Else
		
		;If Rand(0,100)<2 AddParticle(0,ObjectX(i)+Rnd(-.1,.1),ObjectZAdjust(i),-ObjectY(i),0-Rnd(-.1,.1),.3,Rnd(-.005,.005),.02,Rnd(-.005,.005),0,.01,0,-.0001,0,50,0)
		If Rand(0,100)<2 AddParticle(0,ObjectX(i)+Rnd(-.1,.1),ObjectZAdjust(i),-ObjectY(i),0-Rnd(-.1,.1),.3,Rnd(-.005,.005),.02,Rnd(-.005,.005),0,.01,0,-.0001,0,50,4)
		
	EndIf
	
	;SimulateObjectPosition(i)

End Function


Function ControlFireFlower(i)

	If (SimulatedObjectTimer(i)>=0 And SimulatedObjectData(i,2)=0) Or (SimulatedObjectData(i,2)=2 And SimulatedObjectTimer(i)=ObjectTimerMax1(i))
		SimulatedObjectData(i,2)=1
		;AnimateMD2 ObjectEntity(i),1,.2,1,20,1
	EndIf
	
	If ObjectActive(i)<1001
		SimulatedObjectTimer(i)=ObjectTimerMax1(i)
	EndIf
	
	If SimulatedObjectTimer(i)<0

		If SimulatedObjectData(i,2)=1
			;AnimateMD2 ObjectEntity(i),1,.5,21,60,1
			SimulatedObjectData(i,2)=0
		EndIf
	
		If SimulatedObjectTimer(i)=-80
			SimulatedObjectTimer(i)=ObjectTimerMax1(i)
		EndIf
		
		; and fire
		If SimulatedObjectTimer(i)=-60

			If ObjectSubType(i)=2
				SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)+1) Mod 8
			EndIf
			If ObjectSubType(i)=3
				SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)-1) Mod 8
			EndIf

		EndIf

		
	EndIf


End Function

; col is data0 and direction is data2
Function RedoSuctubeMesh(Entity, col, objactive, direction, yawadjust)

	Surface=GetSurface(Entity,1)
	If objactive Mod 2 = 1
		active=0
	Else
		active=1
	EndIf
	If yawadjust=(-90*direction +3600) Mod 360
		; in original position
		dir=0
	Else
		; switched from original
		dir=1
	EndIf
	
	; point arrow
	If dir=0
		VertexCoords surface,0,-0.3,1.71,-0.3
		VertexCoords surface,1,+0.3,1.71,-0.3
		VertexCoords surface,2,0,1.71,+0.3
	Else
		VertexCoords surface,0,-0.3,1.71,+0.3
		VertexCoords surface,2,+0.3,1.71,+0.3
		VertexCoords surface,1,0,1.71,-0.3
	EndIf
	
	; and give colour
	
	VertexTexCoords surface,0,(col Mod 8)*0.125+.01,(col/8)*0.125+.51+.25*active
	VertexTexCoords surface,1,(col Mod 8)*0.125+.115,(col/8)*0.125+.51+.25*active
	VertexTexCoords surface,2,(col Mod 8)*0.125+.051,(col/8)*0.125+.51+.115+.25*active

	UpdateNormals Entity
	


End Function


Function ToggleObject(i)
	; switches objects from activating to deactivating or vice versa
	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i) Mod 2 =0
		If ObjectType(i)=410
			;ActivateFlipBridge(i)
		Else
			SimulatedObjectActive(i)=SimulatedObjectActive(i)+ObjectActivationSpeed(i)+1
			
		EndIf
		If SimulatedObjectActive(i)>1001 Then SimulatedObjectActive(i)=1001
	Else If SimulatedObjectActive(i)>0 And SimulatedObjectActive(i) Mod 2 =1
		If ObjectType(i)=410
			;DeActivateFlipBridge(i)
		Else
			SimulatedObjectActive(i)=SimulatedObjectActive(i)-ObjectActivationSpeed(i)-1
			
		EndIf
		If SimulatedObjectActive(i)<0 Then SimulatedObjectActive(i)=0
	EndIf
	If ObjectType(i)=281 And ObjectModelName$(i)="!SucTube"
		Redosuctubemesh(ObjectEntity(i), ObjectData(i,0), ObjectActive(i), ObjectData(i,2), ObjectYawAdjust(i))
	EndIf
	
End Function


Function ControlChangeActive(i)

	If SimulatedObjectActive(i)>0 And SimulatedObjectActive(i) Mod 2 = 0
		; deactivating
		SimulatedObjectActive(i)=SimulatedObjectActive(i)-ObjectActivationSpeed(i)
	Else If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i) Mod 2=1
		; activating
		SimulatedObjectActive(i)=SimulatedObjectActive(i)+ObjectActivationSpeed(i)
		
	EndIf
	If SimulatedObjectActive(i)<0 	SimulatedObjectActive(i)=0
	If SimulatedObjectActive(i)>1001 SimulatedObjectActive(i)=1001

End Function


Function ControlSteppingStone(i)

	; Data(2) - Alternating?
	If SimulatedObjectData(i,2)=2
		SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
		If SimulatedObjectTimer(i)<0 Then SimulatedObjectTimer(i)=ObjectTimerMax1(i)
		If SimulatedObjectTimer(i)=0
			ToggleObject(i)
			SimulatedObjectTimer(i)=ObjectTimerMax1(i)
		EndIf
		
		ControlChangeActive(i)
	EndIf
		
	
	; 0-submerged, 1001-surfaced
	If (SimulatedObjectActive(i)<1001-4*ObjectActivationSpeed(i)) And SimulatedObjectLastActive(i)>=1001-4*ObjectActivationSpeed(i)

		; just submerged
		If SimulatedObjectData(i,3)=0
			AddParticle(4,Floor(SimulatedObjectX(i))+0.5,WaterTileHeight(Floor(SimulatedObjectX(i)),Floor(SimulatedObjectY(i)))-0.2,-Floor(SimulatedObjectY(i))-0.5,0,.6,0,0,0,0,.006,0,0,0,50,4)
		EndIf
	
	EndIf
	
	If (SimulatedObjectActive(i)=>1001-4*ObjectActivationSpeed(i)) And SimulatedObjectLastActive(i)<1001-4*ObjectActivationSpeed(i)
		
		; just emerged
		If SimulatedObjectData(i,3)=0
			AddParticle(4,Floor(SimulatedObjectX(i))+0.5,WaterTileHeight(Floor(SimulatedObjectX(i)),Floor(SimulatedObjectY(i)))-0.2,-Floor(SimulatedObjectY(i))-0.5,0,1,0,0,0,0,.006,0,0,0,100,4)
		EndIf
				
	EndIf

	
End Function



Function ControlFlipBridge(i)
	
	YScale#=6.6
	
	If (SimulatedObjectActive(i)<>0 And SimulatedObjectActive(i)<>1001) Or SimulationLevel>=2
		YScale#=1+5.6*Float(SimulatedObjectActive(i))/1001.0
	EndIf
	
	If ObjectModelName$(i)="!FlipBridge"
		ScaleEntity GetChild(ObjectEntity(i),1),1.0,1.0,YScale#
	Else
		SimulatedObjectScaleYAdjust(i)=YScale#
		;SimulateObjectScale(i)
	EndIf

End Function


Function ControlSpring(i)

	SimulatedObjectZ(i)=.5
	
	;SimulateObjectPosition(i)

End Function


Function ControlBabyBoomer(i)

	If ObjectDead(i)=1
		; spinning out of control
		SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360
		;ObjectZ(i)=ObjectZ(i)+.01
		;ObjectSubType(i)=-2
		Return
	EndIf
	If ObjectDead(i)=3
		; drowning
		SimulatedObjectYaw(i)=90
		;ObjectZ(i)=ObjectZ(i)-.005
		;ObjectSubType(i)=-2
		Return
	EndIf
	
	If ObjectData(i,8)=1
		; lit
;		For j=1 To 5
			If Rand(0,100)<20
				AddParticle(23,ObjectX(i),Rnd(0.7,0.8),-ObjectY(i),0,.05,Rnd(-0.005,0.005),Rnd(0,0.005),Rnd(-0.005,0.005),0,.004,0,0,0,50,3)
			EndIf
;		Next
	EndIf
	
	If ObjectData(i,8)>0
		;EntityTexture ObjectEntity(i),KaboomTextureSquint
		; lit and burning
		For j=1 To 5
			If Rand(0,100)<ObjectData(i,8)
				AddParticle(Rand(16,18),ObjectX(i),Rnd(0.7,0.8),-ObjectY(i),0,.1,Rnd(-0.02,0.02),Rnd(0,0.02),Rnd(-0.02,0.02),0,.004,0,-.0001,0,50,3)
			EndIf
		Next
	EndIf

End Function


Function ControlSuctube(i)

	If SimulatedObjectActive(i)<>1001 Then Return

	suck=True
	blow=True

	
	; check if sucking/blowing active (e.g. if another tube in front of it)
	For j=0 To NofObjects-1
		If ObjectType(j)=281 And i<>j
			; found another suctube
			If ObjectData(i,2)=ObjectData(j,2) And ObjectData(i,0)=ObjectData(j,0) And ObjectData(i,1)=ObjectData(j,1)
				; same direction
				If ObjectData(i,2)=0 
					If ObjectTileX(i)=ObjectTileX(j) And ObjectTileY(i)=ObjectTileY(j)-1
						suck=False
					EndIf
					If ObjectTileX(i)=ObjectTileX(j) And ObjectTileY(i)=ObjectTileY(j)+1
						blow=False
					EndIf
				Else If ObjectData(i,2)=1 
					If ObjectTileX(i)=ObjectTileX(j)+1 And ObjectTileY(i)=ObjectTileY(j)
						suck=False
					EndIf
					If  ObjectTileX(i)=ObjectTileX(j)-1 And ObjectTileY(i)=ObjectTileY(j)
						blow=False
					EndIf
				Else If ObjectData(i,2)=2 
					If ObjectTileX(i)=ObjectTileX(j) And ObjectTileY(i)=ObjectTileY(j)+1
						suck=False
					EndIf
					If ObjectTileX(i)=ObjectTileX(j) And ObjectTileY(i)=ObjectTileY(j)-1
						blow=False
					EndIf
				Else If ObjectData(i,2)=3 
					If ObjectTileX(i)=ObjectTileX(j)-1 And ObjectTileY(i)=ObjectTileY(j)
						suck=False
					EndIf
					If ObjectTileX(i)=ObjectTileX(j)+1 And ObjectTileY(i)=ObjectTileY(j)
						blow=False
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
	If ObjectData(i,5)=0
		; particle effects
		If Rand(0,100)<30
			psize#=Rnd(0.1,0.2)
			pspeed#=Rnd(1,2)
			parttex=Rand(16,23)
			If suck=True
				Select ObjectData(i,2)
				Case 0
					AddParticle(parttex,ObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-ObjectY(i)-Rnd(1.0,1.9),0,psize,0.0,0.0,-Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 1
					AddParticle(parttex,ObjectX(i)-Rnd(1.0,1.5),Rnd(0.5,1.4),-ObjectY(i)+Rnd(-1,1),0,psize,Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		Case 2
				;	AddParticle(0,0,Rnd(0.5,5.5),0,0,5,0.0,0.0,Rnd(-0.01,-0.02),0,0,0,0,0,Rand(10,50),3)
		
					AddParticle(parttex,ObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-ObjectY(i)+Rnd(1.0,1.9),0,psize,0.0,0.0,Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 3
					AddParticle(parttex,ObjectX(i)+Rnd(1.0,1.5),Rnd(0.5,1.4),-ObjectY(i)+Rnd(-1,1),0,psize,-Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		End Select
			EndIf
			If blow=True
				Select ObjectData(i,2)
				Case 0
					AddParticle(parttex,ObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-ObjectY(i)+Rnd(0.0,0.5),0,psize,0.0,0.0,-Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 1
					AddParticle(parttex,ObjectX(i)+Rnd(0,0.5),Rnd(0.5,1.4),-ObjectY(i)+Rnd(-1,1),0,psize,Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		Case 2
					AddParticle(parttex,ObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-ObjectY(i)-Rnd(0.0,0.5),0,psize,0.0,0.0,Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 3
					AddParticle(parttex,ObjectX(i)-Rnd(0.0,0.5),Rnd(0.5,1.4),-ObjectY(i)+Rnd(-1,1),0,psize,-Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		End Select
			EndIf
		EndIf
	EndIf


End Function


Function ControlRetroCoily(i)

	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2

End Function


Function SetLight(red,green,blue,speed,ared,agreen,ablue,aspeed)
	SimulatedLightRedGoal=Red
	SimulatedLightGreenGoal=Green
	SimulatedLightBlueGoal=Blue
	SimulatedLightChangeSpeed=speed
	
	SimulatedAmbientRedGoal=aRed
	SimulatedAmbientGreenGoal=aGreen
	SimulatedAmbientBlueGoal=aBlue
	SimulatedAmbientChangeSpeed=aSpeed
End Function

Function SetLightNow(red,green,blue,ared,agreen,ablue)
	SimulatedLightRed=Red
	SimulatedLightGreen=Green
	SimulatedLightBlue=Blue
	SimulatedLightRedGoal=Red
	SimulatedLightGreenGoal=Green
	SimulatedLightBlueGoal=Blue
	
	SimulatedAmbientRed=aRed
	SimulatedAmbientBlue=aBlue
	SimulatedAmbientGreen=aGreen
	SimulatedAmbientRedGoal=aRed
	SimulatedAmbientGreenGoal=aGreen
	SimulatedAmbientBlueGoal=aBlue
End Function


Function ControlLight()
	
	If SimulationLevel>=3
		
		If SimulatedLightRed>SimulatedLightRedGoal
			SimulatedLightRed=SimulatedLightRed-SimulatedLightChangeSpeed
			If SimulatedLightRed<SimulatedLightRedGoal Then SimulatedLightRed=SimulatedLightRedGoal
		Else If SimulatedLightRed<SimulatedLightRedGoal
			SimulatedLightRed=SimulatedLightRed+SimulatedLightChangeSpeed
			If SimulatedLightRed>SimulatedLightRedGoal Then SimulatedLightRed=SimulatedLightRedGoal
		EndIf
		If SimulatedLightGreen>SimulatedLightGreenGoal
			SimulatedLightGreen=SimulatedLightGreen-SimulatedLightChangeSpeed
			If SimulatedLightGreen<SimulatedLightGreenGoal Then SimulatedLightGreen=SimulatedLightGreenGoal
		Else If SimulatedLightGreen<SimulatedLightGreenGoal
			SimulatedLightGreen=SimulatedLightGreen+SimulatedLightChangeSpeed
			If SimulatedLightGreen>SimulatedLightGreenGoal Then SimulatedLightGreen=SimulatedLightGreenGoal
		EndIf
		If SimulatedLightBlue>SimulatedLightBlueGoal
			SimulatedLightBlue=SimulatedLightBlue-SimulatedLightChangeSpeed
			If SimulatedLightBlue<SimulatedLightBlueGoal Then SimulatedLightBlue=SimulatedLightBlueGoal
		Else If SimulatedLightBlue<SimulatedLightBlueGoal
			SimulatedLightBlue=SimulatedLightBlue+SimulatedLightChangeSpeed
			If SimulatedLightBlue>SimulatedLightBlueGoal Then SimulatedLightBlue=SimulatedLightBlueGoal
		EndIf
	
		If SimulatedAmbientRed>SimulatedAmbientRedGoal
			
			SimulatedAmbientRed=SimulatedAmbientRed-SimulatedAmbientChangeSpeed
			If SimulatedAmbientRed<SimulatedAmbientRedGoal Then SimulatedAmbientRed=SimulatedAmbientRedGoal
		Else If SimulatedAmbientRed<SimulatedAmbientRedGoal
			
			SimulatedAmbientRed=SimulatedAmbientRed+SimulatedAmbientChangeSpeed
			If SimulatedAmbientRed>SimulatedAmbientRedGoal Then SimulatedAmbientRed=SimulatedAmbientRedGoal
		EndIf
		If SimulatedAmbientGreen>SimulatedAmbientGreenGoal
			SimulatedAmbientGreen=SimulatedAmbientGreen-SimulatedAmbientChangeSpeed
			If SimulatedAmbientGreen<SimulatedAmbientGreenGoal Then SimulatedAmbientGreen=SimulatedAmbientGreenGoal
		Else If SimulatedAmbientGreen<SimulatedAmbientGreenGoal
			SimulatedAmbientGreen=SimulatedAmbientGreen+SimulatedAmbientChangeSpeed
			If SimulatedAmbientGreen>SimulatedAmbientGreenGoal Then SimulatedAmbientGreen=SimulatedAmbientGreenGoal
		EndIf
		If SimulatedAmbientBlue>SimulatedAmbientBlueGoal
			SimulatedAmbientBlue=SimulatedAmbientBlue-SimulatedAmbientChangeSpeed
			If SimulatedAmbientBlue<SimulatedAmbientBlueGoal Then SimulatedAmbientBlue=SimulatedAmbientBlueGoal
		Else If SimulatedAmbientBlue<SimulatedAmbientBlueGoal
			SimulatedAmbientBlue=SimulatedAmbientBlue+SimulatedAmbientChangeSpeed
			If SimulatedAmbientBlue>SimulatedAmbientBlueGoal Then SimulatedAmbientBlue=SimulatedAmbientBlueGoal
		EndIf
	
		LightColor Light,SimulatedLightRed,SimulatedLightGreen,SimulatedLightBlue
		AmbientLight SimulatedAmbientRed,SimulatedAmbientGreen,SimulatedAmbientBlue
		RotateEntity Light,35,-35,0
		
	Else
	
		LightColor Light,255,255,255
		AmbientLight 155,155,155
		RotateEntity Light,80,15,0
		
	EndIf

End Function


Function ControlWeather()

	CentreX#=EntityX(Camera1)
	CentreY#=EntityZ(Camera1)
	
	Select levelweather
	Case 1
		; light snow
		If Rand(1,3)=3 	AddParticle(40,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,Rnd(-.01,0.01),-.03,Rnd(-.01,0.01),2,0,0,0,0,200,3)

	Case 2
		; heavy snow
		AddParticle(40,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.3,0,-.05,0,2,0,0,0,0,120,3)
	Case 3
		; very heavy snow right to left
		AddParticle(40,CentreX+Rnd(5,10),5,CentreY+Rnd(-8,4),0,.4,-0.3,-.09,0,2,0,0,0,0,80,3)
	Case 4
		; very heavy snow left to right
		AddParticle(40,CentreX+Rnd(-10,-5),5,CentreY+Rnd(-8,4),0,.4,0.3,-.09,0,2,0,0,0,0,80,3)


	Case 5
		; rain
		AddParticle(41,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,0,-.2,0,0,0,0,0,0,60,2)
	
		; leaves
	;	If Rand(1,3)=1 AddParticle(42,Objectx(CameraFocusObject)+Rnd(-10,10),5,-ObjectY(CameraFocusObject)+Rnd(-10,10),0,1,Rnd(0,.2),Rnd(-.1,0),0,Rand(1,5),0,0,0,0,60,3)

	Case 6
		If leveltimer<1000000000
			; void
			If Rand(0,200)<2 
				SetLight(Rand(0,255),Rand(0,255),Rand(0,255),2,Rand(0,255),Rand(0,255),Rand(0,255),2)
			EndIf
		EndIf
	Case 7
		If leveltimer<1000000000

			;lightning storm
			If lightningstorm<100 Then lightningstorm=100
			AddParticle(41,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,0.05,-.2,0,0,0,0,0,0,60,2)
			AddParticle(41,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,0.05,-.2,0,0,0,0,0,0,60,2)
			If lightningstorm>100
				If (lightningstorm-100)<7 lightningstorm=Rand(180,255)
				lightningstorm=lightningstorm-10
				If lightningstorm<100 Then lightningstorm=100
			Else
				If Rand(0,300)=10
					lightningstorm=Rand(180,255)
					;playsoundfx(Rand(155,157),-1,-1)
				EndIf
			EndIf
			
			SetLight(lightningstorm,lightningstorm,lightningstorm,10,70,70,70,10)
		EndIf
	Case 8
		If leveltimer<1000000000
			; red alert
			

			alarm=leveltimer Mod 240
			;If alarm=1 Then playsoundfxnow(98)
			If alarm <120
			
				SetLight(alarm*2,0,0,10,70,20,20,10)
			Else
				SetLight(240-alarm*2,0,0,10,70,20,20,10)
			EndIf
		EndIf
		
	Case 9
	
		; light rising
		If Rand(1,8)=3 	AddParticle(1,CentreX+Rnd(-10,10),0,CentreY+Rnd(-10,10),0,.2,0,+.03,0,2,0,0,0,0,200,3)

	Case 10
	
		; light falling
		If Rand(1,8)=3 	AddParticle(1,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,0,-.03,0,2,0,0,0,0,200,3)


	Case 11
	
		; stars rising
		If Rand(1,5)=3 	AddParticle(Rand(32,38),CentreX+Rnd(-10,10),0,CentreY+Rnd(-10,10),0,.8,0,+.03,0,2,0,0,0,0,200,3)


	Case 12
	
		; stars rising
		If Rand(1,5)=3 	AddParticle(Rand(32,38),CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.8,0,-.03,0,2,0,0,0,0,200,3)

	Case 13
	
		; foggy
		If Rand(1,3)=3 	AddParticle(0,CentreX+Rnd(-10,10),-.8,CentreY+Rnd(-10,10),0,2,0,+.005,0,2,0,0,0,0,500,2)

	Case 14
	
		; green foggy
		If Rand(1,3)=3 	AddParticle(27,CentreX+Rnd(-10,10),-.8,CentreY+Rnd(-10,10),0,2,0,+.005,0,2,0,0,0,0,500,2)

	Case 15
		; leaves
		If Rand(1,8)=3 	AddParticle(42,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,1,Rnd(-.03,0.01),-.03,Rnd(-.03,0.01),2,0,0,0,0,200,3)

	Case 16
		; sandstorm
		AddParticle(Rand(25,26),CentreX+Rnd(5,10),5,CentreY+Rnd(-8,4),0,.2,-0.3,-.09,0,2,0,0,0,0,80,3)
		AddParticle(Rand(24,26),CentreX+Rnd(5,10),5,CentreY+Rnd(-8,4),0,.1,-0.3,-.09,0,2,0,0,0,0,80,3)

	Case 17
		; abstract
		If Rand(1,40)=3 	AddParticle(Rand(43,45),CentreX+Rnd(-10,10),Rnd(1,2),CentreY+Rnd(-10,10),0,Rnd(1,4),0,Rnd(.001,0.01),0,2,0,0,0,0,200,Rand(2,3))

	End Select

End Function


Function ControlObjects()

	For i=0 To NofObjects-1
	
		If ObjectReactive(i)=True
		
			
			; Get Scale
			ObjXScale#=SimulatedObjectXScale(i)*SimulatedObjectScaleXAdjust(i)
			ObjYScale#=SimulatedObjectYScale(i)*SimulatedObjectScaleYAdjust(i)
			ObjZScale#=SimulatedObjectZScale(i)*SimulatedObjectScaleZAdjust(i)
		
			If (ObjectActive(i)<>0 And ObjectActive(i)<>1001) Or SimulationLevel>=2
			
				; Select Visual Animation	
				Select ObjectActivationType(i)
				Case 0
					; nothing
				Case 1
					; Scale Vertical 0-1
					ObjZScale=ObjZScale*Float(SimulatedObjectActive(i))/1001.0
				
				
				Case 2
					; scale all directions 0-1
					ObjXScale=ObjXScale*Float(SimulatedObjectActive(i))/1001.0
					ObjYScale=ObjYScale*Float(SimulatedObjectActive(i))/1001.0
					ObjZScale=ObjZScale*Float(SimulatedObjectActive(i))/1001.0
				Case 3
					; scale planar only
					ObjXScale=ObjXScale*Float(SimulatedObjectActive(i))/1001.0
					ObjYScale=ObjYScale*Float(SimulatedObjectActive(i))/1001.0
			
			
				Case 11
					; push up from -1.01 to -0.01
					SimulatedObjectZ#(i)=-0.99+Float(SimulatedObjectActive(i))/1001.0
					
				Case 12,13,14,15,16
					; push up from -x.01 to -5.01 (used for stepping stones)
					SimulatedObjectZ#(i)=-(ObjectActivationType(i)-6)-.01+(ObjectActivationType(i)-11)*Float(SimulatedObjectActive(i))/1001.0
					
				Case 17 ; *** THESE ONLY WORK FOR AUTODOORS - OBJECTTILEX MUST BE PRE_SET
					; push north
					SimulatedObjectY#(i)=ObjectTileY(i)+0.5-SimulatedObjectYScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
				Case 18
					; push East
					SimulatedObjectX#(i)=ObjectTileX(i)+0.5+SimulatedObjectXScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
			
				Case 19
					; push south
					SimulatedObjectY#(i)=ObjectTileY(i)+0.5+SimulatedObjectYScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
			
				Case 20
					; push west
					SimulatedObjectX#(i)=ObjectTileX(i)+0.5-SimulatedObjectXScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
			
			
			
				
				
				Case 21
					If ObjectModelName$(i)="!NPC" Or ObjectModelName$(i)="!Tentacle"
						Entity=GetChild(ObjectEntity(i),3)
					Else
						Entity=ObjectEntity(i)
					EndIf
					; Fade in
					EntityAlpha Entity,Float(SimulatedObjectActive(i))/1001.0
											
				Case 31
					; push down from 1.01 to 0.01
					SimulatedObjectZ#(i)=1.01-Float(SimulatedObjectActive(i))/1001.0
					
				Case 41
					; rotate out (doors)
					SimulatedObjectYaw#(i)=-160+160*Float(SimulatedObjectActive(i))/1001.0
			
				
						
				End Select
				
			EndIf
			
			
			
			
			
			Select ObjectType(i)
			
			Case 20
				ControlTrap(i)
			Case 30
				ControlTeleporter(i)
			Case 40
				ControlSteppingStone(i)
			Case 120
				ControlStinkerWee(i)
			Case 130
				ControlStinkerWeeExit(i)
			Case 151
				ControlRainbowBubble(i)
			Case 160
				ControlObstacle(i)
			Case 161
				ControlWaterfall(i)
			Case 163
				ControlWindmillRotor(i)
			Case 170
				ControlGoldStar(i)
			Case 171,174
				ControlGoldCoin(i)
			Case 172
				ControlKey(i)
			Case 173
				ControlGem(i)
			Case 179
				ControlCustomItem(i)
			Case 180
				ControlSigns(i)
			Case 190
				ControlParticleEmitters(i)
			Case 200
				ControlGloveCharge(i)
			Case 230
				ControlFireFlower(i)
			Case 260
				ControlBowler(i)
			Case 280
				ControlSpring(i)
			Case 281
				ControlSucTube(i)
			Case 300
				ControlIceFloat(i)
			Case 301
				ControlPlantFloat(i)
			Case 310
				ControlRubberducky(i)
			Case 320
				ControlVoidTexture(i)
			Case 330
				ControlWisp(i)
			Case 340
				ControlTentacle(i)
			Case 370 
				ControlCrab(i)
			Case 400
				ControlBabyBoomer(i)
			Case 410
				ControlFlipBridge(i)
			Case 420
				ControlRetroCoily(i)
			Case 422,423,430,431
				ControlRetroZbotUfo(i)
			Case 424
				ControlRetroLaserGate(i)
			Case 425
				ControlRetroRainbowCoin(i)
			Case 434
				ControlMothership(i)
			Case 441
				ControlSunSphere1(i)
			Case 442
				ControlSunSphere2(i)
			Case 450
				ControlLurker(i)
			Case 460
				ControlBurstFlower(i)
				
			End Select
			
			
			
			SimulateObjectPosition(i)
			SimulateObjectRotation(i)
			ScaleEntity ObjectEntity(i),ObjXScale#,ObjZScale#,ObjYScale#
			
			SimulatedObjectLastActive(i)=SimulatedObjectActive(i)
			
		;Else
		;	AddParticle(2,ObjectXAdjust(i)+ObjectTileX(i)+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-ObjectTileY(i)-.5,0,.2,0,.03,0,0,.01,0,0,0,100,3)
			
		EndIf
	
	Next
	
	; Scroll Teleporters
	For i=0 To 9
		If TeleporterTexture(i)>0
			PositionTexture TeleporterTexture(i),0,-Float((LevelTimer/3) Mod 100)/100.0
		EndIf
	Next
	
	PositionTexture StarTexture,0,Float(leveltimer Mod 1000) / 1000.0
	PositionTexture RainbowTexture,0,Float(leveltimer Mod 1000) / 1000.0
	PositionTexture GhostTexture,0,Float(leveltimer Mod 1000) / 1000.0
	For i=0 To 2
		PositionTexture WraithTexture(i),Float(leveltimer Mod 100) / 100.0,0
	Next
	
	For i=0 To 2
		PositionTexture WaterFallTexture(i),0,((LevelTimer) Mod 50)/50.0
	Next
	PositionTexture FloingTexture,(leveltimer Mod 700)/700.0,(leveltimer Mod 100)/100.0
	PositionTexture PlasmaTexture,3*Sin((LevelTimer/20.0) Mod 360),4*Cos((LevelTimer/20.0) Mod 360)
	ScaleTexture Plasmatexture,1.1+Sin((LevelTimer/2) Mod 360),1.1+Sin((LevelTimer/2) Mod 360)
	PositionTexture RainbowTexture2,(leveltimer Mod 7000)/7000.0,(leveltimer Mod 1000)/1000.0

End Function



Function CreateLevelEdge()

	; side/wall effects
	
	Select LevelEdgeStyle
	Case 0
		; nuthin'
	
	Case 1
		; basic wall at height 1
		
		 
		
		
	;	BackGroundTexture1=myLoadTexture("data\graphics\backgroundtex "+ex$+"1.bmp",1)
	;	BackGroundTexture2=myLoadTexture("data\graphics\backgroundtex "+ex$+"2.bmp",1)
		
		;widescreen: we need all levelwidth increased by 2 so all |10| must be |12| when widescreen is enabled and 20 to 24
		extra=0
		extradouble=0
		If widescreen
			extra=2
			extradouble=extra*2
		EndIf
		BackGroundEntity1=CreateMesh()
		surface=CreateSurface(BackGroundEntity1)
		AddVertex (surface,-10-extra,1,10,0,0)
		AddVertex (surface,LevelWidth+10+extra,1,10,LevelWidth+20+extradouble,0)
		AddVertex (surface,-10-extra,1,0,0,10)
		AddVertex (surface,LevelWidth+10+extra,1,0,LevelWidth+20+extradouble,10)
		AddTriangle (surface,0,1,2)
		AddTriangle (surface,2,1,3)
		
		AddVertex (surface,-10-extra,1,-LevelHeight,0,0)
		AddVertex (surface,LevelWidth+10+extra,1,-LevelHeight,LevelWidth+20+extradouble,0)
		AddVertex (surface,-10-extra,1,-LevelHeight-10,0,10)
		AddVertex (surface,LevelWidth+10+extra,1,-LevelHeight-10,LevelWidth+20+extradouble,10)
		AddTriangle (surface,4,5,6)
		AddTriangle (surface,6,5,7)
		
		AddVertex (surface,-10-extra,1,0,0,0)
		AddVertex (surface,0,1,0,10+extra,0)
		AddVertex (surface,-10-extra,1,-LevelHeight,0,LevelHeight)
		AddVertex (surface,0,1,-LevelHeight,10+extra,LevelHeight)
		AddTriangle (surface,8,9,10)
		AddTriangle (surface,10,9,11)
		
		AddVertex (surface,LevelWidth,1,0,0,0)
		AddVertex (surface,LevelWidth+10+extra,1,0,10+extra,0)
		AddVertex (surface,LevelWidth,1,-LevelHeight,0,LevelHeight)
		AddVertex (surface,LevelWidth+10+extra,1,-LevelHeight,10+extra,LevelHeight)
		AddTriangle (surface,12,13,14)
		AddTriangle (surface,14,13,15)
		
		UpdateNormals BackGroundEntity1
		leveltexturen$=leveltexturename$(leveltexturenum)
		ex$=Mid$(leveltexturen$,10,Len(leveltexturen$)-9-4)
		
		
		If usecustomleveltexture=True
			EntityTexture BackgroundEntity1,custombgtexture1
		Else
			EntityTexture BackGroundEntity1,BackGroundTexture1(whichleveltexture(ex$))
		EndIf
		
		BackGroundEntity2=CreateMesh()
		surface=CreateSurface(BackGroundEntity2)
		AddVertex (surface,0,1,0,0,0)
		AddVertex (surface,LevelWidth,1,0,LevelWidth,0)
		AddVertex (surface,0,0,0,0,1)
		AddVertex (surface,LevelWidth,0,0,LevelWidth,1)
		AddTriangle (surface,0,1,2)
		AddTriangle (surface,2,1,3)
		
		AddVertex (surface,LevelWidth,1,0,0,0)
		AddVertex (surface,LevelWidth,1,-LevelHeight,LevelHeight,0)
		AddVertex (surface,LevelWidth,0,0,0,1)
		AddVertex (surface,LevelWidth,0,-LevelHeight,LevelHeight,1)
		AddTriangle (surface,4,5,6)
		AddTriangle (surface,6,5,7)
		
		AddVertex (surface,LevelWidth,1,-LevelHeight,0,0)
		AddVertex (surface,0,1,-LevelHeight,LevelWidth,0)
		AddVertex (surface,LevelWidth,0,-LevelHeight,0,1)
		AddVertex (surface,0,0,-LevelHeight,LevelWidth,1)
		AddTriangle (surface,8,9,10)
		AddTriangle (surface,10,9,11)
		
		AddVertex (surface,0,1,-LevelHeight,0,0)
		AddVertex (surface,0,1,0,LevelHeight,0)
		AddVertex (surface,0,0,-LevelHeight,0,1)
		AddVertex (surface,0,0,0,LevelHeight,1)
		AddTriangle (surface,12,13,14)
		AddTriangle (surface,14,13,15)
		
		UpdateNormals BackGroundEntity2
		If usecustomleveltexture=True
			EntityTexture BackgroundEntity2,custombgtexture2
		Else
			EntityTexture BackGroundEntity2,BackGroundTexture2(whichleveltexture(ex$))
		EndIf


	End Select

End Function

Function CreateLevel()

	; Requires a LevelWidth*LevelHeight Field field with LevelTileTexture/Rotation/Height/etc.

	; First divvy up and create chunks
	
	; chunks get their data from the main level file. Each chunk also creates a 1 tile overlap 
	; border around it that isn't rendered, but used to calculate matching heights etc.
	; Hence the increase in ChunkSize by 2, and rendering the chunk at position (-1,-1)
	CurrentLevelChunk=0	
	ChunkSize=12
	WaterTurbulenceGlobal=False
	For j=0 To (LevelHeight-1)/ChunkSize
		For i=0 To (LevelWidth-1)/ChunkSize
			; get the size of the chunk (smaller if at edge)
			If i=(LevelWidth-1)/ChunkSize And (LevelWidth Mod ChunkSize)>0
				ChunkWidth=(LevelWidth Mod ChunkSize)+2
			Else
				ChunkWidth=ChunkSize+2
			EndIf
			If j=(LevelHeight-1)/ChunkSize And (LevelHeight Mod ChunkSize)>0
				ChunkHeight=(LevelHeight Mod ChunkSize)+2
			Else
				ChunkHeight=ChunkSize+2
			EndIf
			
			; now fill up the chunkdata
			
			For i2=0 To ChunkWidth-1
				For j2=0 To ChunkHeight-1
				
					If i2=0 And i=0
						i3=i*ChunkSize
					Else If i2=ChunkWidth-1 And i=(LevelWidth-1)/ChunkSize
						i3=i*ChunkSize+ChunkWidth-3
					Else
						i3=i*ChunkSize+i2-1
					EndIf
					If j2=0 And j=0
						j3=j*ChunkSize
					Else If j2=ChunkHeight-1 And j=(LevelHeight-1)/ChunkSize
						j3=j*ChunkSize+ChunkHeight-3
					Else
						j3=j*ChunkSize+j2-1
					EndIf

					
					
					
					ChunkTileTexture(i2,j2)=LevelTileTexture(i3,j3)
					ChunkTileRotation(i2,j2)=LevelTileRotation(i3,j3)
					ChunkTileSideTexture(i2,j2)=LevelTileSideTexture(i3,j3)
					ChunkTileSideRotation(i2,j2)=LevelTileSideRotation(i3,j3)
					ChunkTileRandom(i2,j2)=LevelTileRandom(i3,j3)
					ChunkTileExtrusion(i2,j2)=LevelTileExtrusion(i3,j3)
					ChunkTileRounding(i2,j2)=LevelTileRounding(i3,j3)
					ChunkTileEdgeRandom(i2,j2)=LevelTileEdgeRandom(i3,j3)
					ChunkTileHeight(i2,j2)=LevelTileHeight(i3,j3)
					
					ChunkWaterTileTexture(i2,j2)=WaterTileTexture(i3,j3)
					ChunkWaterTileRotation(i2,j2)=WaterTileRotation(i3,j3)
					ChunkWaterTileHeight(i2,j2)=WaterTileHeight(i3,j3)
					If WaterTileTurbulence(i3,j3)>0 WaterTurbulenceGlobal=True
					If ChunkWaterTileTexture(i2,j2)<0 Then ChunkWaterTileHeight(i2,j2)=-10


				Next
			Next
			
			
			
			CreateLevelChunk(i*(ChunkSize)-1,j*(ChunkSize)-1)
			CurrentLevelChunk=CurrentLevelChunk+1
		Next
	Next
	
	; CreateLevelEdgeStyle()
	
End Function

Function CreateLevelChunk(ChunkX,ChunkZ)

	; Creates a chunk of level with index "chunk"


	LevelEntity(CurrentLevelChunk)=CreateMesh()
	LevelSurface(CurrentLevelChunk)=CreateSurface(LevelEntity(CurrentLevelChunk))
	mySurface=LevelSurface(CurrentLevelChunk)
	; Create the LevelMesh
	
	; PART 1 - BUILD THE FLAT MESH (THE GROUND)
	; -----------------------------------------
	
	; Create The Vertices
	For j=1 To ChunkHeight-2
		For i=1 To ChunkWidth-2
			; do each tile with subdivision of detail level
			; First, create the vertices
			For j2=0 To LevelDetail
				For i2=0 To LevelDetail
				
					xoverlap#=0
					yoverlap#=0
					zoverlap#=0
					If j2=0 Or j2=LevelDetail Or i2=0 Or i2=LevelDetail
						If i2=0 
							xoverlap#=-0.005
						;	zoverlap#=+0.005
						EndIf
						If j2=0
							yoverlap#=0.005
						;	zoverlap#=+0.005
						EndIf
						height=0
					EndIf

					CalculateUV(ChunkTileTexture(i,j),i2,j2,ChunkTileRotation(i,j),8)
										
					
					AddVertex(mySurface,ChunkX+i+Float(i2)/Float(LevelDetail)+xoverlap,height+zoverlap,-(ChunkZ+j+Float(j2)/Float(LevelDetail))+yoverlap,ChunkTileu,ChunkTilev)
				Next
			Next
			; Now create the triangles
		;	SV=(j*ChunkWidth+i)*(LevelDetail+1)*(LevelDetail+1) ; startingvertex
			For j2=0 To LevelDetail-1
				For i2=0 To LevelDetail-1
					AddTriangle (mySurface,GetLevelVertex(i,j,i2,j2),GetLevelVertex(i,j,i2+1,j2),GetLevelVertex(i,j,i2,j2+1))
					AddTriangle (mySurface,GetLevelVertex(i,j,i2+1,j2),GetLevelVertex(i,j,i2+1,j2+1),GetLevelVertex(i,j,i2,j2+1))
				;	AddTriangle (mySurface,SV+j2*(LevelDetail+1)+i2,SV+j2*(LevelDetail+1)+i2+1,SV+(j2+1)*(LevelDetail+1)+i2)
				;	AddTriangle (mySurface,SV+j2*(LevelDetail+1)+i2+1,SV+(j2+1)*(LevelDetail+1)+i2+1,SV+(j2+1)*(LevelDetail+1)+i2)

				Next
			Next
		Next
	Next
	
	; add randomness
	For j=1 To ChunkHeight-2
		For i=1 To ChunkWidth-2
			For i2=0 To LevelDetail
				For j2=0 To LevelDetail
					If i2=0 And j2=0
						random#=Minimum4(ChunkTileRandom(i-1,j-1),ChunkTileRandom(i,j-1),ChunkTileRandom(i-1,j),ChunkTileRandom(i,j))
					Else If j2=0 And i2=LevelDetail
						random#=Minimum4(ChunkTileRandom(i+1,j-1),ChunkTileRandom(i,j-1),ChunkTileRandom(i+1,j),ChunkTileRandom(i,j))
					Else If j2=LevelDetail And i2=0
						random#=Minimum4(ChunkTileRandom(i-1,j+1),ChunkTileRandom(i-1,j),ChunkTileRandom(i,j+1),ChunkTileRandom(i,j))
					Else If i2=LevelDetail And j2=LevelDetail
						random#=Minimum4(ChunkTileRandom(i+1,j+1),ChunkTileRandom(i,j+1),ChunkTileRandom(i+1,j),ChunkTileRandom(i,j))
					Else If j2=0
						random#=Minimum2(ChunkTileRandom(i,j-1),ChunkTileRandom(i,j))
					Else If j2=LevelDetail
						random#=Minimum2(ChunkTileRandom(i,j+1),ChunkTileRandom(i,j))
					Else If i2=0
						random#=Minimum2(ChunkTileRandom(i-1,j),ChunkTileRandom(i,j))
					Else If i2=LevelDetail
						random#=Minimum2(ChunkTileRandom(i+1,j),ChunkTileRandom(i,j))
					Else
						random#=ChunkTileRandom(i,j)
					EndIf
					
					vertex=GetLevelVertex(i,j,i2,j2)
					random2#=random*LevelVertexRandom(Float(i2),Float(j2))

					VertexCoords mySurface,vertex,VertexX(mysurface,vertex),VertexY(mysurface,vertex)+random2,VertexZ(mysurface,vertex)					
				
				Next
			Next
		
		Next
	Next
	
	
	; now adjust heights - linearly increase/decrease heights based on heights of neighbouring tiles
	
	; general idea: go through center rows of each tile,
	; calculate vertex heights based on two tile heights (linear interpolation) - store this height
	; as of the second row, build a column from the previous center row to this one.
	; This creates a grid of lines with the centres of each tile "anchored" at LevelTileHeight
	
	
	If LevelDetail<2 Or Floor(LevelDetail/2)*2<>LevelDetail
		; must be divisible by two, or disable height function
		Goto NoLevelHeight
	EndIf
	
	Dim ChunkStoredVHeight#(ChunkWidth*(LevelDetail+1))	
	
	For j=0 To ChunkHeight-1
		For i=0 To ChunkWidth-1
			For i2=0 To LevelDetail
				If i2<LevelDetail/2
					; first half of tile, compare with left neighbour
					If i=0 
						OtherHeight#=0.0
					Else
						OtherHeight#=ChunkTileHeight(i-1,j)
					EndIf
					NewHeight#=OtherHeight+(ChunkTileHeight(i,j)-OtherHeight)*Float(i2+Float(LevelDetail)/2.0)/Float(LevelDetail)
				Else
					; second half of tile, compare with right neighbour
					If i=ChunkWidth-1 
						OtherHeight#=0.0
					Else
						OtherHeight#=ChunkTileHeight(i+1,j)
					EndIf
					NewHeight#=ChunkTileHeight(i,j)+(OtherHeight-ChunkTileHeight(i,j))*Float(i2-LevelDetail/2)/Float(LevelDetail)
					
				EndIf
				
				; but don't adjust vertices in the chunk-border
				If i>0 And j>0 And i<ChunkWidth-1 And j<ChunkHeight-1
					vertex=GetLevelVertex(i,j,i2,LevelDetail/2)
					VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+NewHeight,VertexZ(mySurface,vertex)
				EndIf
		
				If j>0
					; as of second row, build vertical bridge to first row
					For j2=LevelDetail/2+1 To LevelDetail
						; first half is actually 2nd half of previous row
						; (also no need to lift first vertex of that part, that's already the center of
						;  the row and hence lifted above)
						OtherHeight#=ChunkStoredVHeight(i*(LevelDetail+1)+i2)
						ThisVertexesHeight#=OtherHeight#+(NewHeight-OtherHeight)*Float(j2-LevelDetail/2)/Float(LevelDetail)
						If i>0 And j>1 And i<ChunkWidth-1; And j<ChunkHeight-1
							vertex=GetLevelVertex(i,j-1,i2,j2)
							VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+ThisVertexesHeight,VertexZ(mySurface,vertex)
						EndIf
					Next
					For j2=0 To LevelDetail/2-1
						; 2nd half (we're now in the top half of this row)
						OtherHeight#=ChunkStoredVHeight(i*(LevelDetail+1)+i2)
						ThisVertexesHeight#=OtherHeight#+(NewHeight-OtherHeight)*Float(j2+LevelDetail/2)/Float(LevelDetail)
						If i>0 And j>0 And i<ChunkWidth-1 And j<ChunkHeight-1
							vertex=GetLevelVertex(i,j,i2,j2)
							VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+ThisVertexesHeight,VertexZ(mySurface,vertex)
						EndIf
					Next
					
				EndIf
				ChunkStoredVHeight(i*(LevelDetail+1)+i2)=NewHeight
		
			Next
			
			
		Next
	Next
	
	.nolevelheight
	
	
	
	
	; finally, lift tiles by extrusion
	; first we simply add a set height for all vertices in the entire tile
	For j=1 To Chunkheight-2
		For i=1 To Chunkwidth-2
			For j2=0 To LevelDetail
				For i2=0 To LevelDetail
					vertex=GetLevelVertex(i,j,i2,j2)
					VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+ChunkTileExtrusion(i,j),VertexZ(mySurface,vertex)
				Next
			Next
		Next
	Next
	
	; now, how about rounding corners, etc?
	
	; general idea: 
	
	; first, deal with rounding of corners. If there is a drop-off on two sides, then
	; calculate a reduction of the corner pixels in x/y by a circular shape
	; However - Disable rounding if drop-off on two sides isn't equal - otherwise sidewalls will have gaps
	
	
	For j=1 To Chunkheight-2
		For i=1 To Chunkwidth-2
			If ChunkTileRounding(i,j)=1		
			
					
				; is there a drop-off NE corner:
				If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i+1,j) And ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i,j-1) And ChunkTileExtrusion(i+1,j)=ChunkTileExtrusion(i,j-1)
					; yep: round-off
					For i2=(LevelDetail/2)+1 To LevelDetail
						For j2=(LevelDetail/2)+1 To Leveldetail
							vertex=GetLevelVertex(i,j,i2,LevelDetail-j2)
							; convert (i2,j2) to (0...1)
							a#=Float(i2-(LevelDetail/2))/Float(LevelDetail/2)
							b#=Float(j2-(LevelDetail/2))/Float(LevelDetail/2)
							r#=Float(maximum2(i2,j2)-(LevelDetail/2))/Float(LevelDetail/2)
							x#=r/Sqr(1+b^2/a^2)
							y#=Sqr(r^2-x^2)
												
							VertexCoords mySurface,vertex,ChunkX+i+0.5+x#/2.0,VertexY(mySurface,vertex),-(ChunkZ+j+0.5-y#/2.0)
						Next
					Next
					
				EndIf
	
					
				; is there a drop-off SE corner:
				If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i+1,j) And ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i,j+1) And ChunkTileExtrusion(i+1,j)=ChunkTileExtrusion(i,j+1)
					; yep: round-off
					For i2=(LevelDetail/2)+1 To LevelDetail
						For j2=(LevelDetail/2)+1 To Leveldetail
							vertex=GetLevelVertex(i,j,i2,j2)
							; convert (i2,j2) to (0...1)
							a#=Float(i2-(LevelDetail/2))/Float(LevelDetail/2)
							b#=Float(j2-(LevelDetail/2))/Float(LevelDetail/2)
							r#=Float(maximum2(i2,j2)-(LevelDetail/2))/Float(LevelDetail/2)
							x#=r/Sqr(1+b^2/a^2)
							y#=Sqr(r^2-x^2)
												
							VertexCoords mySurface,vertex,ChunkX+i+0.5+x#/2.0,VertexY(mySurface,vertex),-(ChunkZ+j+0.5+y#/2.0)
						Next
					Next
					
				EndIf
				; SW corner
				If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i-1,j) And ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i,j+1) And ChunkTileExtrusion(i-1,j)=ChunkTileExtrusion(i,j+1)
					; yep: round-off
					For i2=(LevelDetail/2)+1 To LevelDetail
						For j2=(LevelDetail/2)+1 To Leveldetail
							vertex=GetLevelVertex(i,j,LevelDetail-i2,j2)
							; convert (i2,j2) to (0...1)
							a#=Float(i2-(LevelDetail/2))/Float(LevelDetail/2)
							b#=Float(j2-(LevelDetail/2))/Float(LevelDetail/2)
							r#=Float(maximum2(i2,j2)-(LevelDetail/2))/Float(LevelDetail/2)
							x#=r/Sqr(1+b^2/a^2)
							y#=Sqr(r^2-x^2)
												
							VertexCoords mySurface,vertex,ChunkX+i+0.5-x#/2.0,VertexY(mySurface,vertex),-(ChunkZ+j+0.5+y#/2.0)
						Next
					Next
					
				EndIf
				
				; is there a drop-off NW corner:
				If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i-1,j) And ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i,j-1) And ChunkTileExtrusion(i-1,j)=ChunkTileExtrusion(i,j-1)
					; yep: round-off
					For i2=(LevelDetail/2)+1 To LevelDetail
						For j2=(LevelDetail/2)+1 To Leveldetail
							vertex=GetLevelVertex(i,j,LevelDetail-i2,LevelDetail-j2)
							; convert (i2,j2) to (0...1)
							a#=Float(i2-(LevelDetail/2))/Float(LevelDetail/2)
							b#=Float(j2-(LevelDetail/2))/Float(LevelDetail/2)
							r#=Float(maximum2(i2,j2)-(LevelDetail/2))/Float(LevelDetail/2)
							x#=r/Sqr(1+b^2/a^2)
							y#=Sqr(r^2-x^2)
												
							VertexCoords mySurface,vertex,ChunkX+i+0.5-x#/2.0,VertexY(mySurface,vertex),-(ChunkZ+j+0.5-y#/2.0)
						Next
					Next
					
				EndIf
			
			EndIf
						
		Next
	Next
	
	
	
	; next, deal with randomness along edges:
	; check e.g. the Right edge. If there is a drop-off (extrude) Then we push left the vertex along the edge by up to #randommax
	; (but don't touch corner vertices)
	
	
	
	randommax#=0.1
	
	
	For j=1 To ChunkHeight-2
		For i=1 To ChunkWidth-2
			If ChunkTileEdgeRandom(i,j)=1
				; north side
				If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i,j-1)
					
					j2=0
					For i2=0 To LevelDetail
						vertex=GetLevelVertex(i,j,i2,j2)
						If i2=0 
							If ChunkTileExtrusion(i-1,j)=ChunkTileExtrusion(i,j-1)
								random#=1.0
							Else 
								random#=0
							EndIf
						Else If i2=LevelDetail
							If ChunkTIleExtrusion(i+1,j)=ChunkTileExtrusion(i,j-1)
								random#=1.0
							Else
								random#=0
							EndIf
						Else
							random#=Rnd(0,1)
						EndIf

						
						VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)-random*randommax
					Next
					
				EndIf
				; east side
				If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i+1,j)
					
					i2=LevelDetail
					For j2=0 To LevelDetail
						vertex=GetLevelVertex(i,j,i2,j2)
						If j2=0 
							If ChunkTileExtrusion(i+1,j)=ChunkTileExtrusion(i,j-1)
								random#=1.0
							Else 
								random#=0
							EndIf
						Else If j2=LevelDetail
							If ChunkTIleExtrusion(i+1,j)=ChunkTileExtrusion(i,j+1)
								random#=1.0
							Else
								random#=0
							EndIf
						Else
							random#=Rnd(0,1)
						EndIf
						VertexCoords mySurface,vertex,VertexX(mySurface,vertex)-random*randommax,VertexY(mySurface,vertex),VertexZ(mySurface,vertex)
					Next
					
					
				EndIf
				; south side
				If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i,j+1)
					
					j2=LevelDetail
					For i2=0 To LevelDetail
						vertex=GetLevelVertex(i,j,i2,j2)
						If i2=0 
							If ChunkTileExtrusion(i-1,j)=ChunkTileExtrusion(i,j+1)
								random#=1.0
							Else 
								random#=0
							EndIf
						Else If i2=LevelDetail
							If ChunkTIleExtrusion(i+1,j)=ChunkTileExtrusion(i,j+1)
								random#=1.0
							Else
								random#=0
							EndIf
						Else
							random#=Rnd(0,1)
						EndIf
						VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)+random*randommax
					Next
					
				EndIf
				; west side
				If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i-1,j)
					
					i2=0
					For j2=0 To LevelDetail
						vertex=GetLevelVertex(i,j,i2,j2)
						If j2=0 
							If ChunkTileExtrusion(i-1,j)=ChunkTileExtrusion(i,j-1)
								random#=1.0
							Else 
								random#=0
							EndIf
						Else If j2=LevelDetail
							If ChunkTIleExtrusion(i-1,j)=ChunkTileExtrusion(i,j+1)
								random#=1.0
							Else
								random#=0
							EndIf
						Else
							random#=Rnd(0,1)
						EndIf
						VertexCoords mySurface,vertex,VertexX(mySurface,vertex)+random*randommax,VertexY(mySurface,vertex),VertexZ(mySurface,vertex)
					Next
					
					
				EndIf
			EndIf

		Next
	Next
					
	
	
	; now deal with the "bottom"
	
	; * not yet implemented *
	
	
	
	
	
	
	; PART 2 - HANDLE EXTRUSION WALLS
	; -------------------------------
	
	; general idea - for each tile walk around for sides. if our extrusion is higher than theirs,
	; create a wall connecting the two 
	
	; from here on new vertices won't be trackable or countable (since not all tiles will have walls)
	; * future possibility - if absolutely necessary an idea would be to create all vertices, but not
	;	connect them with triangles. Test if/how that affects framerates
	
	; get the newest one, and increment from there
	currentvertex=GetLevelVertex(ChunkWidth-2,ChunkHeight-2,LevelDetail,LevelDetail)+1
	
	
	For j=1 To Chunkheight-2
		For i=1 To Chunkwidth-2
		
			; here we also calculate how much the bottom edge of the side wall should be pushed "out"
			; the maxfactor is the maximum (corners are not pushed out)
			If ChunkTileEdgeRandom(i,j)=1
				randommax#=0.2
			Else
				randommax#=0.0
			EndIf
			
			overhang#=0.0
			
			; north side
			random#=0 ; this is the random for the lower edge - set to zero and only caclulate for the second pixel,
						; that way, the first pixel of the next square will have the same random factor
			If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i,j-1) 
				; yep, add two triangles per LevelDetail connecting the two bordering coordinates
				For i2=0 To LevelDetail-1
					vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
					CalculateUV(ChunkTileSideTexture(i,j),i2,0,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)-overhang,ChunkTileU,ChunkTileV)
					
					vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
					CalculateUV(ChunkTileSideTexture(i,j),i2+1,0,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)-overhang,ChunkTileU,ChunkTileV)

					vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
					CalculateUV(ChunkTileSideTexture(i,j),i2,LevelDetail,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,ChunkX+i+Float(LevelDetail-i2)/Float(LevelDetail),VertexY(mySurface,vertex)-ChunkTileExtrusion(i,j)+ChunkTileExtrusion(i,j-1),-ChunkZ-j+random,ChunkTileU,ChunkTileV)
					
					vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
					If i2<LevelDetail-1
						random#=Rnd(0,randommax)
					Else
						random#=0
					EndIf

					CalculateUV(ChunkTileSideTexture(i,j),i2+1,LevelDetail,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,ChunkX+i+Float(LevelDetail-i2-1)/Float(LevelDetail),VertexY(mySurface,vertex)-ChunkTileExtrusion(i,j)+ChunkTileExtrusion(i,j-1),-ChunkZ-j+random,ChunkTileU,ChunkTileV)
					
					AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
					AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)
					
					currentvertex=currentvertex+4
				Next
			EndIf

			; east side
			random#=0
			If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i+1,j) 
				; yep, add two triangles per LevelDetail connecting the two bordering coordinates
				For j2=0 To LevelDetail-1
					vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
					CalculateUV(ChunkTileSideTexture(i,j),j2,0,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)

					vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
					CalculateUV(ChunkTileSideTexture(i,j),j2+1,0,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)

					vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
					CalculateUV(ChunkTileSideTexture(i,j),j2,LevelDetail,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,ChunkX+i+1+random,VertexY(mySurface,vertex)-ChunkTileExtrusion(i,j)+ChunkTileExtrusion(i+1,j),-(ChunkZ+j+Float(LevelDetail-j2)/Float(LevelDetail)),ChunkTileU,ChunkTileV)

					vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
					If j2<LevelDetail-1
						random#=Rnd(0,randommax)
					Else
						random#=0
					EndIf

					CalculateUV(ChunkTileSideTexture(i,j),j2+1,LevelDetail,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,ChunkX+i+1+random,VertexY(mySurface,vertex)-ChunkTileExtrusion(i,j)+ChunkTileExtrusion(i+1,j),-(ChunkZ+j+Float(LevelDetail-j2-1)/Float(LevelDetail)),ChunkTileU,ChunkTileV)
					
					AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
					AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)
					
					currentvertex=currentvertex+4
				Next
			EndIf

			

					
			; south side
			random#=0
			If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i,j+1) 
				; yep, add two triangles per LevelDetail connecting the two bordering coordinates
				For i2=0 To LevelDetail-1
					vertex=GetLevelVertex(i,j,i2,LevelDetail)
					CalculateUV(ChunkTileSideTexture(i,j),i2,0,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)+overhang,ChunkTileU,ChunkTileV)
					vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
					CalculateUV(ChunkTileSideTexture(i,j),i2+1,0,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)+overhang,ChunkTileU,ChunkTileV)

					vertex=GetLevelVertex(i,j,i2,LevelDetail)
					CalculateUV(ChunkTileSideTexture(i,j),i2,LevelDetail,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,ChunkX+i+Float(i2)/Float(LevelDetail),VertexY(mySurface,vertex)-ChunkTileExtrusion(i,j)+ChunkTileExtrusion(i,j+1),-(ChunkZ+j+1+random),ChunkTileU,ChunkTileV)
					
					vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
					If i2<LevelDetail-1
						random#=Rnd(0,randommax)
					Else
						random#=0
					EndIf

					CalculateUV(ChunkTileSideTexture(i,j),i2+1,LevelDetail,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,ChunkX+i+Float(i2+1)/Float(LevelDetail),VertexY(mySurface,vertex)-ChunkTileExtrusion(i,j)+ChunkTileExtrusion(i,j+1),-(ChunkZ+j+1+random),ChunkTileU,ChunkTileV)
					
					AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
					AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)
					
					currentvertex=currentvertex+4
				Next
			EndIf
			
			; west side
			random#=0
			If ChunkTileExtrusion(i,j)>ChunkTileExtrusion(i-1,j) 
				; yep, add two triangles per LevelDetail connecting the two bordering coordinates
				For j2=0 To LevelDetail-1
					vertex=GetLevelVertex(i,j,0,j2)
					CalculateUV(ChunkTileSideTexture(i,j),j2,0,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)

					vertex=GetLevelVertex(i,j,0,j2+1)
					CalculateUV(ChunkTileSideTexture(i,j),j2+1,0,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)

					vertex=GetLevelVertex(i,j,0,j2)
					CalculateUV(ChunkTileSideTexture(i,j),j2,LevelDetail,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,ChunkX+i-random,VertexY(mySurface,vertex)-ChunkTileExtrusion(i,j)+ChunkTileExtrusion(i-1,j),-(ChunkZ+j+Float(j2)/Float(LevelDetail)),ChunkTileU,ChunkTileV)
					
					vertex=GetLevelVertex(i,j,0,j2+1)
					If j2<LevelDetail-1
						random#=Rnd(0,randommax)
					Else
						random#=0
					EndIf

					CalculateUV(ChunkTileSideTexture(i,j),j2+1,LevelDetail,ChunkTileSideRotation(i,j),8)
					AddVertex (mySurface,ChunkX+i-random,VertexY(mySurface,vertex)-ChunkTileExtrusion(i,j)+ChunkTileExtrusion(i-1,j),-(ChunkZ+j+Float(j2+1)/Float(LevelDetail)),ChunkTileU,ChunkTileV)
					
					AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
					AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)
					
					currentvertex=currentvertex+4
				Next
			EndIf

		Next
	Next

	
	
	
	
	; now, how about creating overhangs? (i.e. also pushing in upper vertices of wall)
	; * not yet implemented *

	

						
	UpdateNormals LevelEntity(CurrentLevelChunk)
	
	; and point all edge vertex normals "up" (to smooth lighting)
	
	For j=1 To Chunkheight-2
		For i=1 To Chunkwidth-2
			For i2=0 To LevelDetail
				For j2=0 To LevelDetail
					If i2=0 Or i2=LevelDetail Or j2=0 Or j2=LevelDetail
						vertex=GetLevelVertex(i,j,i2,j2)
						VertexNormal mySurface,vertex,0.0,1.0,0.0
					EndIf

				Next
			Next
			
		

		Next
	Next
	
	leveltexturen$=leveltexturename$(leveltexturenum)
	ex$=Mid$(leveltexturen$,10,Len(leveltexturen$)-9-4)
	
	If usecustomleveltexture=True
		EntityTexture LevelEntity(CurrentLevelChunk),customleveltexture
	Else
		EntityTexture LevelEntity(CurrentLevelChunk),LevelTextures(WhichLevelTexture(ex$))	
	EndIf

	
	; PART 3 - WATER
	; -------------------------------

	
	
	WaterEntity(CurrentLevelChunk)=CreateMesh()
	WaterSurface(CurrentLevelChunk)=CreateSurface(WaterEntity(CurrentLevelChunk))
	mySurface=WaterSurface(CurrentLevelChunk)
	
	For j=1 To Chunkheight-2
		For i=1 To Chunkwidth-2
			CalculateUV(ChunkWaterTileTexture(i,j),0,0,ChunkWaterTileRotation(i,j),4)
			AddVertex (mySurface,ChunkX+i,ChunkWaterTileHeight(i,j),-ChunkZ-j,ChunkTileU,ChunkTileV)
			CalculateUV(ChunkWaterTileTexture(i,j),LevelDetail,0,ChunkWaterTileRotation(i,j),4)
			AddVertex (mySurface,ChunkX+i+1,ChunkWaterTileHeight(i,j),-ChunkZ-j,ChunkTileU,ChunkTileV)
			CalculateUV(ChunkWaterTileTexture(i,j),0,LevelDetail,ChunkWaterTileRotation(i,j),4)
			AddVertex (mySurface,ChunkX+i,ChunkWaterTileHeight(i,j),-ChunkZ-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(ChunkWaterTileTexture(i,j),LevelDetail,LevelDetail,ChunkWaterTileRotation(i,j),4)
			AddVertex (mySurface,ChunkX+i+1,ChunkWaterTileHeight(i,j),-ChunkZ-j-1,ChunkTileU,ChunkTileV)
			
			AddTriangle (mySurface,(i-1)*4+(j-1)*4*(ChunkWidth-2)+0,(i-1)*4+(j-1)*4*(ChunkWidth-2)+1,(i-1)*4+(j-1)*4*(ChunkWidth-2)+2)
			AddTriangle (mySurface,(i-1)*4+(j-1)*4*(ChunkWidth-2)+1,(i-1)*4+(j-1)*4*(ChunkWidth-2)+3,(i-1)*4+(j-1)*4*(ChunkWidth-2)+2)


		Next
	Next
	UpdateNormals WaterEntity(CurrentLevelChunk)
	If WaterGlow=True 
		EntityBlend WaterEntity(CurrentLevelChunk),3
	Else 
		EntityBlend WaterEntity(CurrentLevelChunk),1
	EndIf
	If WaterTransparent=True 
		EntityAlpha WaterEntity(CurrentLevelChunk),.5
	Else
		EntityAlpha WaterEntity(CurrentLevelChunk),1
	EndIf
	
	waterTextureN$=waterTextureName$(waterTextureNum)
	ex$=Mid$(waterTextureN$,10,Len(waterTextureN$)-9-4)
	
	If usecustomwatertexture=True
		EntityTexture WaterEntity(CurrentLevelChunk),customwatertexture
	Else
		EntityTexture WaterEntity(CurrentLevelChunk),WaterTextures(WhichWaterTexture(ex$))
	EndIf
	
	TranslateEntity WaterEntity(CurrentLevelChunk),0,-0.04,0

			
End Function

Function GetLevelVertex(i,j,i2,j2)
	; Gets the index number of the vertex at chunk tile (i,j) with detail subdivision (i2,j2)
	; in the currentchunk
	
	; since the chunk has a border around it, we decrease i and j by 1, and reduce width by 2
	i=i-1
	j=j-1
	n=(i+j*(ChunkWidth-2))*(LevelDetail+1)*(LevelDetail+1) ; get to start of tile
	n=n+j2*(LevelDetail+1)+i2
;	Print n
;	Delay 10
	Return n
End Function

Function LevelVertexRandom#(x#,y#)
	; creates a random number between 0 and 1 based on two input numbers from 0 to LevelDetail (i.e.i2/j2)
	; used to create random pertubations in vertices in order to ensure that neighbouring vertices
	; (i.e. same x/y coordinates, but in neightbouring chunks) get the same perturbation
	
	If Floor(x)>0 And Floor(y)>0 And Floor(x)<LevelDetail And Floor(y)<LevelDetail
		; in interior of tile - do true random
		Return Rnd(0,1)
	EndIf
	
	x#=Abs(x-LevelDetail/2) ; take the i2/j2 and rework as "distance from center"
	y#=Abs(y-LevelDetail/2) ; so that opposing sides are treated equally 
							; (they must, since a right side of tile i is the left side of i+1)
	
	random#=(x+.59)*(y+.73)*241783
	intrandom=Int(random)
	intrandom=(intrandom Mod 700) + (intrandom Mod 300)
	random=Float(intrandom)/1000.0
	Return Random#
End Function

Function UpdateWater(x,y,radius)
	
	For i=0 To 2
		PositionTexture WaterFallTexture(i),0,((LevelTimer) Mod 50)/50.0
	Next
	PositionTexture FloingTexture,(leveltimer Mod 700)/700.0,(leveltimer Mod 100)/100.0
	PositionTexture PlasmaTexture,3*Sin((LevelTimer/20.0) Mod 360),4*Cos((LevelTimer/20.0) Mod 360)
	ScaleTexture Plasmatexture,1.1+Sin((LevelTimer/2) Mod 360),1.1+Sin((LevelTimer/2) Mod 360)
	PositionTexture RainbowTexture2,(leveltimer Mod 7000)/7000.0,(leveltimer Mod 1000)/1000.0

	
	If WaterFlow>=0
		; move
	;	PositionTexture WaterTexture,0,((WaterFlow*(LevelTimer/10.0)) Mod 50)/50.0
		PositionTexture WaterTextures(0),0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
		PositionTexture WaterTextures(1),0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
		PositionTexture WaterTextures(2),0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
		PositionTexture WaterTextures(3),0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0

		If customwatertexture>0 PositionTexture customwatertexture,0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0

;		PositionTexture WaterTexture,0,-((LevelTimer*WaterFlow) Mod 10000)/10000.0
	EndIf
	If waterflow<0
		; rock
		PositionTexture WaterTextures(0),0,0.5+0.125*WaterFlow/4*Sin(-(4*LevelTimer*WaterFlow)/10.0)
		If customwatertexture>0 PositionTexture customwatertexture,0,0.5+0.125*WaterFlow/4*Sin(-(4*LevelTimer*WaterFlow)/10.0)

	EndIf
	
	If WaterTurbulenceGlobal=True And LevelTimer<1000000000
		; turbulence
		; find what chunk x/y is in
		For j2=Floor(y)-radius To Floor(y)+radius
			For i2=Floor(x)-radius To Floor(x)+radius
				If i2>=0 And j2>=0 And i2<LevelWidth And j2<LevelHeight 
				    If WaterTileTexture(i2,j2)>=0 And WaterTileTurbulence(i2,j2)>0
						; find what chunk and at what position this tile is
						ChunksPerRow=Floor((LevelWidth-1)/ChunkSize)+1
						CurrentChunk=Floor(i2/ChunkSize)+Floor(j2/ChunkSize)*ChunksPerRow
						; get the width of this chunk
						If i2>=(ChunksPerRow-1)*ChunkSize
							ThisWidth=((LevelWidth-1) Mod ChunkSize)+1
						Else
							ThisWidth=ChunkSize
						EndIf
						i=i2 Mod ChunkSize
						j=j2 Mod ChunkSize
						
						mySurface=WaterSurface(CurrentChunk)
						VertexCoords mySurface,(i+j*ThisWidth)*4+0,VertexX(mySurface,(i+j*ThisWidth)*4+0),WaterTileHeight(i2,j2)+WaterTileTurbulence(i2,j2)*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180),VertexZ(mySurface,(i+j*ThisWidth)*4+0)
						VertexCoords mySurface,(i+j*ThisWidth)*4+1,VertexX(mySurface,(i+j*ThisWidth)*4+1),WaterTileHeight(i2,j2)+WaterTileTurbulence(i2,j2)*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180+90),VertexZ(mySurface,(i+j*ThisWidth)*4+1)
	
						VertexCoords mySurface,(i+j*ThisWidth)*4+2,VertexX(mySurface,(i+j*ThisWidth)*4+2),WaterTileHeight(i2,j2)+WaterTileTurbulence(i2,j2)*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180-180),VertexZ(mySurface,(i+j*ThisWidth)*4+2)
						VertexCoords mySurface,(i+j*ThisWidth)*4+3,VertexX(mySurface,(i+j*ThisWidth)*4+3),WaterTileHeight(i2,j2)+WaterTileTurbulence(i2,j2)*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180+90-180),VertexZ(mySurface,(i+j*ThisWidth)*4+3)
					EndIf
				
				
				EndIf
			Next
		Next
	
	
	EndIf
	
	


End Function

Function WhichLevelTexture(ex$)

	Select Lower$(ex$)
	Case "abstract"
		Return 0
	Case "cave"
		Return 1
	Case "cellar"
		Return 2
	Case "forest"
		Return 3
	Case "hills"
		Return 4
	Case "snow"
		Return 5
	Case "temple"
		Return 6
	Case "town"
		Return 7
	Case "lava"
		Return 8
	Case "beach"
		Return 9
	Case "jungle"
		Return 10
	Case "jtemple"
		Return 11
	Case "wonderfalls"
		Return 12
	Case "cave2"
		Return 13
	Case "sundog"
		Return 14
	Case "techno"
		Return 15
	Case "stars"
		Return 16
	Case "desert"
		Return 17
	Case "castle"
		Return 18
	Case "jaava"
		Return 19
	Case "uo"
		Return 20
	Case "spooky"
		Return 21








		
	
	End Select
	Return -1
End Function

Function WhichWaterTexture(ex$)

	
	Select Lower$(ex$)
	Case "1"
		Return 0
	Case "2"
		Return 1
	Case "3"
		Return 2
	Case "4"
		Return 3
	
	
	End Select
	Return -1
End Function

Function Minimum2#(x#,y#)
	If x<y
		Return x
	Else
		Return y
	EndIf
End Function
Function Minimum4#(x#,y#,z#,w#)
	If x<=y And x<=z And x<=w
		Return x
	Else If y<=x And y<=z And y<=W
		Return y
	Else If z<=x And z<=y And z<=w
		Return z
	Else 
		Return w
	EndIf
End Function
Function Maximum2#(x#,y#)
	If x>y
		Return x
	Else
		Return y
	EndIf
End Function






Include "particles.bb"
		

.winning
Data "None (e.g. collect star)","Rescue All Stinkers","Capture/Destroy Scritters","Collect All Gems","Destroy All Bricks","Destroy FireFlowers","Race","Capture/Destroy Crabs","Rescue All BabyBoomers","Destroy All ZBots"
Data "Done"

.Commands
Data "CWHI","CGRY","CRED","CORA","CYEL","CGRE","CCYA","CBLU","CPUR","CRAI","CBLI","CWAR"
Data "ENON","ESHI","EJIT","EWAV","EBOU","EZOO","EZSH","ECIR","EEIG","EUPD","ELER","EROT"










		