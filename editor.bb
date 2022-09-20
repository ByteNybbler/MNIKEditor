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

Global VersionDate$="09/14/22"
AppTitle "Wonderland Adventures MNIKEditor (Version "+VersionDate$+")"

Include "particles-define.bb"
Include "sound-define.bb"

Global VersionText$="WA Editor       MNIKSource v10.04 ("+VersionDate$+")"

Global MASTERUSER=True

SeedRnd MilliSecs() ; Seed the randomizer with the current system time in milliseconds.

Global LeftMouse,LeftMouseReleased,RightMouse,RightMouseReleased
Global MouseScroll=0
Global MouseDebounceTimer=0
Global ReturnKey,ReturnKeyReleased,DeleteKey,DeleteKeyReleased
Const KeyCount=237 ; How many keys to track for their state.
Dim KeyReleased(KeyCount)

Global EditorMode=0		;0-level, 1-textures, 2-sidetextures, 3-objects
						;4-user Select screen
						;5,6,7-adventure select screen (6="edit/delete/move/cancel",7="delete sure?")
						;8-master edit screen
						;9-dialog edit screen

Const EditorModeTile=0
Const EditorModeObject=3
Const EditorModeDialog=9

Global EditorModeBeforeMasterEdit=0

; Whether or not the level or dialog has unsaved changes.
Global UnsavedChanges=0

Function AddUnsavedChange()
	
	If UnsavedChanges<999999
		UnsavedChanges=UnsavedChanges+1
	EndIf
	
End Function

; KEYS

Global KeyMoveNorth=17 ; W key
Global KeyMoveWest=30 ; A key
Global KeyMoveSouth=31 ; S key
Global KeyMoveEast=32 ; D key

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
Global TextWarningR=255
Global TextWarningG=100
Global TextWarningB=100

Global ModelErrorR=255
Global ModelErrorG=0
Global ModelErrorB=255

Const ColorsConfig$="colors.cfg"

; Set at runtime
Global ObjectColorR
Global ObjectColorG
Global ObjectColorB
Global TileColorR
Global TileColorG
Global TileColorB

		
; EDITOR DIALOG DATA

Global CurrentDialog
Global PreviewCurrentDialog

Global DialogCurrentRed,DialogCurrentGreen,DialogCurrentBlue,DialogCurrentEffect

Const MaxInterChanges=100
Const MaxAskAbouts=100
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

Global NofAskAbouts
Global AskAboutTopText$
Dim AskAboutText$(MaxAskAbouts)
Dim AskAboutActive(MaxAskAbouts)
Dim AskAboutInterchange(MaxAskAbouts)
Dim AskAboutRepeat(MaxAskAbouts)


Global PreviewNofInterchanges
Dim PreviewNofInterChangeTextLines(MaxInterChanges)	
Dim PreviewInterChangeTextLine$(MaxInterChanges,7)
Dim PreviewDialogTextCommand$(MaxInterChanges,200),PreviewDialogTextCommandPos(MaxInterChanges,200), PreviewNofTextCommands(MaxInterChanges)
Dim PreviewNofInterChangeReplies(MaxInterChanges)
Dim PreviewInterChangeReplyText$(MaxInterChanges,8)
Dim PreviewInterChangeReplyFunction(MaxInterChanges,8)		
Dim PreviewInterChangeReplyData(MaxInterChanges,8)			
Dim PreviewInterChangeReplyCommand(MaxInterChanges,8)		
Dim PreviewInterChangeReplyCommandData(MaxInterChanges,8,4)

Global PreviewNofAskAbouts
Global PreviewAskAboutTopText$
Dim PreviewAskAboutText$(MaxAskAbouts)
Dim PreviewAskAboutActive(MaxAskAbouts)
Dim PreviewAskAboutInterchange(MaxAskAbouts)
Dim PreviewAskAboutRepeat(MaxAskAbouts)


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



; 0 is master.dat editor, 1 is dialog editor, 2 is hub editor
Dim MouseTextEntryLineMax(2)
MouseTextEntryLineMax(0)=5
MouseTextEntryLineMax(1)=9
MouseTextEntryLineMax(2)=1

Dim MouseTextEntryLineY(2,9)
Dim MouseTextEntryLineYAdjust(2,9)

MouseTextEntryLineY(0,0)=0
MouseTextEntryLineY(0,1)=3
MouseTextEntryLineY(0,2)=4
MouseTextEntryLineY(0,3)=5
MouseTextEntryLineY(0,4)=6
MouseTextEntryLineY(0,5)=7

MouseTextEntryLineY(1,0)=0
MouseTextEntryLineY(1,1)=1
MouseTextEntryLineY(1,2)=2
MouseTextEntryLineY(1,3)=3
MouseTextEntryLineY(1,4)=4
MouseTextEntryLineY(1,5)=5
MouseTextEntryLineY(1,6)=6
MouseTextEntryLineY(1,7)=10
MouseTextEntryLineY(1,8)=19
MouseTextEntryLineY(1,9)=24
MouseTextEntryLineYAdjust(1,8)=-8
MouseTextEntryLineYAdjust(1,9)=-8

MouseTextEntryLineY(2,0)=0
MouseTextEntryLineY(2,1)=3


; COMPILER DATA

Const MaxCompilerFile=700 ; 500

Global NofCompilerFiles
Global NofCustomContentFiles
Dim CustomContentFile$(MaxCompilerFile)

Dim CompilerFileName$(MaxCompilerFile)
Dim CompilerFileSize(MaxCompilerFile)
						
Dim NofHubCompilerFiles(MaxCompilerFile)
Dim HubCompilerFileName$(MaxCompilerFile,MaxCompilerFile)
Dim HubCompilerFileSize(MaxCompilerFile,MaxCompilerFile)

Global NofWlvFiles
Global NofDiaFiles

Function FileSatisfiesCompiler(ex$,AddToCounts)

	If Upper$(ex$)="MASTER.DAT"
		Return True
	ElseIf Upper$(Right$(ex$,4))=".WLV"
		If AddToCounts
			NofWlvFiles=NofWlvFiles+1
		EndIf
		Return True
	ElseIf Upper$(Right$(ex$,4))=".DIA"
		If AddToCounts
			NofDiaFiles=NofDiaFiles+1
		EndIf
		Return True
	Else
		Return False
	EndIf

End Function

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
Const MaxInterchange=100
Dim MasterDialogList(1000),MasterLevelList(1000)

Const StateNotSpecial=0
Const StateCopying=1
Const StateSwapping=2
Global CopyingLevel=StateNotSpecial
Global CopiedLevel=-1
Global CopyingDialog=StateNotSpecial
Global CopiedDialog=-1


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

Global CurrentLevelNumber=0

; Use a ring buffer to track which wlvs have been visited.
Const PreviousLevelNumberBufferSize=100
; Waste a slot in the buffer to differentiate between empty and full states.
; The alternative is using a bool, but since bools and ints seem to be the same type in Blitz and thus the same size, there's no reason to put forth the implementation effort.
Const PreviousLevelNumberBufferMax=PreviousLevelNumberBufferSize
Dim PreviousLevelNumberBuffer(PreviousLevelNumberBufferMax)
Global PreviousLevelNumberBufferStart
Global PreviousLevelNumberBufferCurrent
Global OpenedFirstLevelYet ; This is necessary to prevent CurrentLevelNumber's initial value from being added to the ring buffer.

Function ResetPreviousLevelNumberBuffer()

	PreviousLevelNumberBufferStart=0
	PreviousLevelNumberBufferCurrent=0
	OpenedFirstLevelYet=False

End Function

Global ShowingError=False
Global UsingWireFrame=False

; END EDITOR MASTER DATA

Global CustomIconName$="Standard"
Global CustomMapName$

; The default level coordinates to focus on when opening a level.
; No longer in use since the camera is now centered when opening a level.
Const DefaultCameraFocusX=7
Const DefaultCameraFocusY=10

Dim LevelMesh(MaxLevelCoordinate),LevelSurface(MaxLevelCoordinate) ; one for each row
Dim WaterMesh(MaxLevelCoordinate),WaterSurface(MaxLevelCoordinate)
Dim LogicMesh(MaxLevelCoordinate),LogicSurface(MaxLevelCoordinate)
Dim DirtyNormals(MaxLevelCoordinate)
Global ShowLogicMesh=False

Const ShowLevelMeshHide=0
Const ShowLevelMeshTransparent=1
Const ShowLevelMeshShow=2

Const ShowLevelMeshMax=2
Global ShowLevelMesh=ShowLevelMeshShow

Const ShowObjectHide=0
Const ShowObjectNormal=1
Const ShowObjectMeshIds=2
Const ShowObjectMeshIndices=3
Const ShowObjectMeshCount=4

Const ShowObjectMeshMax=4
Global ShowObjectMesh=ShowObjectNormal

Const BrushWrapRelative=0
Const BrushWrapModulus=1
Const BrushWrapRandom=2
Const BrushWrapMirrorX=200
Const BrushWrapMirrorY=300
Const BrushWrapMirrorXY=400

Const BrushWrapMax=2
Global BrushWrap=BrushWrapRelative

Const StepPerClick=0
Const StepPerPlacement=1
Const StepPerTile=2

Const StepPerMax=2
Global StepPer=StepPerPlacement

Global DidStepPerClick=False

Global ShowObjectPositions=False ; this is the marker feature suggested by Samuel
Global BorderExpandOption=0 ;0-current, 1-duplicate

Global PreventPlacingObjectsOutsideLevel=True

Function ToolbarPositionX(StepsFromToolbarLeft)
	
	PartitionCount=8
	Partition=GfxWidth/PartitionCount
	Return Partition*(StepsFromToolbarLeft-1)+Partition/2
	
End Function

Function ToolbarPositionY(StepsFromToolbarTop)
	
	Return GfxHeight-125+45*StepsFromToolbarTop
	
End Function

Function IsMouseOverToolbarItem(x,y)

	mx=MouseX()
	my=MouseY()
	Return mx>=x-50 And mx<x+50 And my>y And my<y+30

End Function

; The position of the level editor cursor.
Const BrushCursorInvalid=-100000
Global BrushCursorX=BrushCursorInvalid
Global BrushCursorY=BrushCursorInvalid

Const BrushModeNormal=0
Const BrushModeBlock=1
Const BrushModeBlockPlacing=2
Const BrushModeFill=3
Const BrushModeInlineHard=4
Const BrushModeInlineSoft=5
Const BrushModeOutlineHard=6
Const BrushModeOutlineSoft=7
Const BrushModeRow=8
Const BrushModeColumn=9

; Negative brush mode IDs can't be selected from the normal brush mode menu.
; These are placed here to be adjacent to normal brush mode.
Const BrushModeTestLevel=-2
Const BrushModeSetMirror=-3

Const MaxBrushMode=9
Global BrushMode=BrushModeNormal

Const BlockPlacingModeNone=-1
Const BlockPlacingModePlace=0
Const BlockPlacingModeCopy=1
Const BlockPlacingModeDelete=2
Global BlockPlacingMode=BlockPlacingModeNone

Const DupeModeNone=0
Const DupeModeX=1
Const DupeModeY=2
Const DupeModeXPlusY=3

Const DupeModeMax=3
Global DupeMode=DupeModeNone

Global MirrorPositionX=-1
Global MirrorPositionY=-1

Global MirrorEntityX
Global MirrorEntityY

Function IsBrushInBlockMode()
	Return BrushMode=BrushModeBlock Or BrushMode=BrushModeBlockPlacing
End Function

Function IsBrushInInlineMode()
	Return BrushMode=BrushModeInlineSoft Or BrushMode=BrushModeInlineHard
End Function

Function IsBrushInOutlineMode()
	Return BrushMode=BrushModeOutlineSoft Or BrushMode=BrushModeOutlineHard
End Function

Function StartBlockModeBlock(NewBlockPlacingMode)

	BlockCornerX=BrushCursorX
	BlockCornerY=BrushCursorY
	cornleft=BlockCornerX
	cornright=BlockCornerX
	cornup=BlockCornerY
	corndown=BlockCornerY
	SetBrushMode(BrushModeBlockPlacing)
	BlockPlacingMode=NewBlockPlacingMode

End Function

Global PlacementDensity#=1.0

Global BlockModeMesh,BlockModeSurface,BlockCornerX,BlockCornerY
Global cornleft,cornright,cornup,corndown
Global LevelTextureNum, WaterTextureNum
Dim LevelTextureName$(30),WaterTextureName$(20)
Global LevelTextureCustomName$,WaterTextureCustomName$
Global NofLevelTextures,NofWatertextures,CurrentLevelTexture,CurrentWaterTexture

Global LevelTexture ; the actual texture
Global WaterTexture
Global LevelMusic,LevelWeather

Global Leveltimer

Global BrushWidth=1
Global BrushHeight=1

Dim LogicName$(14)
LogicName$(0)="Floor"
LogicName$(1)="Wall"
LogicName$(2)="Water"
LogicName$(3)="Teleporter"
LogicName$(4)="Bridge"
LogicName$(5)="Lava"
LogicName$(6)="06"
LogicName$(7)="07"
LogicName$(8)="Cage"
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
CreateDir "Data"
CreateDir "Data\Adventures"

Global EditorUserName$=""
Global AdventureUserName$=""
Global NofEditorUserNames
Dim EditorUserNamesListed$(100)
Global EditorUserNameEntered$=""

Global AdventureFileName$
Global NofAdventureFileNames
Dim AdventureFileNamesListed$(10000)
Global AdventureNameEntered$=""
Global AdventureFileNamesListedStart
Global AdventureNameSelected


Const AdventureCurrentArchiveCurrent=0
Const AdventureCurrentArchiveArchive=1
Const AdventureCurrentArchivePlayer=2
Const AdventureCurrentArchiveDataAdventures=3
Const MaxAdventureCurrentArchive=3

Global AdventureCurrentArchive=AdventureCurrentArchiveCurrent

Global DisplayFullScreen=False


;filed=ReadFile (globaldirname$+"\display-ed.wdf")
;If filed>0
;
;	DisplayFullScreen=ReadInt(filed)
;	CloseFile filed
;EndIf

Global EditorControls$=GlobalDirName$+"\editorcontrols.wdf"



GetTextureNames()





; LEVEL SIZE
; ============
Global LevelWidth=40 ; in tiles
Global LevelHeight=40 ; in tiles

Const MaxLevelSize=101
Const MaxLevelCoordinate=MaxLevelSize-1
Const MaxTilesPerLevel=MaxLevelSize*MaxLevelSize

Global LevelEdgeStyle=1

Global WidthLeftChange,WidthRightChange,HeightTopChange,HeightBottomChange

; TILES
; ============

Type TerrainTile

Field Texture ; corresponding to squares in LevelTexture
Field Rotation ; 0-3, and 4-7 for "flipped"
Field SideTexture ; texture for extrusion walls
Field SideRotation ; 0-3, and 4-7 for "flipped"
Field Random# ; random height pertubation of tile
Field Height# ; height of "center" - e.g. to make ditches and hills
Field Extrusion# ; extrusion with walls around it
Field Rounding ; 0-no, 1-yes: are floors rounded if on a drop-off corner
Field EdgeRandom ; 0-no, 1-yes: are edges rippled
Field Logic

End Type

Type WaterTile

Field Texture
Field Rotation
Field Height#
Field Turbulence#

End Type

Type Tile

Field Terrain.TerrainTile
Field Water.WaterTile

End Type

Function NewTile.Tile()

	Result.Tile=New Tile
	Result\Terrain=New TerrainTile
	Result\Terrain\Texture=8
	Result\Terrain\SideTexture=6
	Result\Water=New WaterTile
	Return Result

End Function

Dim LevelTiles.Tile(MaxLevelCoordinate,MaxLevelCoordinate)
Dim CopyLevelTiles.Tile(MaxLevelCoordinate,MaxLevelCoordinate)
Dim BrushTiles.Tile(MaxLevelCoordinate,MaxLevelCoordinate)

Dim DraggedTiles.Tile(MaxLevelCoordinate,MaxLevelCoordinate)
Dim DraggedTilesEnabled(MaxLevelCoordinate,MaxLevelCoordinate)
Global DraggedTilesSpotX=-1
Global DraggedTilesSpotY=-1

For i=0 To MaxLevelCoordinate
	For j=0 To MaxLevelCoordinate
		LevelTiles(i,j)=NewTile()
		CopyLevelTiles(i,j)=NewTile()
		BrushTiles(i,j)=NewTile()
		DraggedTiles(i,j)=NewTile()
		DraggedTilesEnabled(i,j)=False
	Next
Next

Global CurrentTile.Tile=NewTile()
Global TargetTile.Tile=NewTile()
Global TempTile.Tile=NewTile()

Dim LevelTileObjectCount(MaxLevelCoordinate,MaxLevelCoordinate) ; for changing the marker color when there's more than one object present

Global ChunkTileU#,ChunkTileV#

Global CurrentMesh,CurrentSurface ; for tile rendering in tile camera

Global LevelDetail=4
Global CurrentVertex=0

Global CurrentTileTextureUse=True
Global CurrentTileSideTextureUse=True
Global CurrentTileHeightUse=True
Global CurrentTileExtrusionUse=True
Global CurrentTileRandomUse=True
Global CurrentTileRoundingUse=True
Global CurrentTileEdgeRandomUse=True
Global CurrentTileLogicUse=True

Global CurrentWaterTileTextureUse=True
Global CurrentWaterTileHeightUse=True
Global CurrentWaterTileTurbulenceUse=True

Global StepSizeTileRandom#
Global StepSizeTileHeight#
Global StepSizeTileExtrusion#
Global StepSizeWaterTileHeight#
Global StepSizeWaterTileTurbulence#

Global OnceTilePlacement=True

Function IsAnyStepSizeActive()

	Return StepSizeTileRandom#<>0 Or StepSizeTileHeight#<>0 Or StepSizeTileExtrusion#<>0 Or StepSizeWaterTileHeight#<>0 Or StepSizeWaterTileTurbulence#<>0

End Function

Global TargetTileTextureUse=True
Global TargetTileSideTextureUse=True
Global TargetTileHeightUse=True
Global TargetTileExtrusionUse=True
Global TargetTileRandomUse=True
Global TargetTileRoundingUse=True
Global TargetTileEdgeRandomUse=True
Global TargetTileLogicUse=True

Global TargetWaterTileUse=True
Global TargetWaterTileHeightUse=True
Global TargetWaterTileTurbulenceUse=True

; used for flood fill algorithm
Dim LevelTileVisited(MaxLevelCoordinate,MaxLevelCoordinate)
Dim FloodStackX(MaxTilesPerLevel) ; no pun intended hahahahaha
Dim FloodStackY(MaxTilesPerLevel)
Dim FloodedStackX(MaxTilesPerLevel) ; Stores positions to flood when done visiting. Useful for flood fill as used in inline mode.
Dim FloodedStackY(MaxTilesPerLevel)
Global FloodElementCount ; I wish I had OOP
Global FloodedElementCount
Global FloodOutsideAdjacent

Global ParticlePreview
Global ParticlePreviewSurface

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
Const MaxNofObjects=1000

Dim ObjectAdjusterString$(MaxNofObjects,30)

Global HighlightWopAdjusters=True
Global NofWopAdjusters=0
Dim ObjectAdjusterWop$(30)

Dim ObjectPositionMarker(MaxNofObjects)
Dim WorldAdjusterPositionMarker(3)
Global CurrentObjectMoveXYGoalMarker
Global WhereWeEndedUpMarker ; For traveling with G between LevelExits.
Global WhereWeEndedUpAlpha#=0.0

Dim SimulatedObjectXScale#(MaxNofObjects)
Dim SimulatedObjectZScale#(MaxNofObjects)
Dim SimulatedObjectYScale#(MaxNofObjects)
Dim SimulatedObjectXAdjust#(MaxNofObjects)
Dim SimulatedObjectZAdjust#(MaxNofObjects)
Dim SimulatedObjectYAdjust#(MaxNofObjects)
Dim SimulatedObjectPitchAdjust#(MaxNofObjects)
Dim SimulatedObjectYawAdjust#(MaxNofObjects)
Dim SimulatedObjectRollAdjust#(MaxNofObjects)
Dim SimulatedObjectX#(MaxNofObjects),SimulatedObjectY#(MaxNofObjects),SimulatedObjectZ#(MaxNofObjects)
Dim SimulatedObjectPitch#(MaxNofObjects)
Dim SimulatedObjectYaw#(MaxNofObjects)
Dim SimulatedObjectRoll#(MaxNofObjects)
Dim SimulatedObjectPitch2#(MaxNofObjects),SimulatedObjectYaw2#(MaxNofObjects),SimulatedObjectRoll2#(MaxNofObjects)
Dim SimulatedObjectActive(MaxNofObjects),SimulatedObjectLastActive(MaxNofObjects)
Dim SimulatedObjectStatus(MaxNofObjects)
Dim SimulatedObjectTimer(MaxNofObjects)
Dim SimulatedObjectData(MaxNofObjects,10)
Dim SimulatedObjectCurrentAnim(MaxNofObjects)
Dim SimulatedObjectMovementSpeed(MaxNofObjects)
Dim SimulatedObjectMoveXGoal(MaxNofObjects),SimulatedObjectMoveYGoal(MaxNofObjects)
Dim SimulatedObjectData10(MaxNofObjects)
Dim SimulatedObjectSubType(MaxNofObjects)
Dim SimulatedObjectTileTypeCollision(MaxNofObjects)
Dim SimulatedObjectExclamation(MaxNofObjects)
Dim SimulatedObjectFrozen(MaxNofObjects)
;Dim SimulatedObjectScaleAdjust#(MaxNofObjects) ; not useful since ScaleAdjust is set to 1.0 in-game after it is applied to XScale, YScale, and ZScale
Dim SimulatedObjectScaleXAdjust#(MaxNofObjects),SimulatedObjectScaleYAdjust#(MaxNofObjects),SimulatedObjectScaleZAdjust#(MaxNofObjects)


Type GameObject

Field Model.GameObjectModel
Field Attributes.GameObjectAttributes
Field Position.GameObjectPosition

End Type


Type GameObjectModel

Field Entity
Field Texture

Field HatEntity
Field HatTexture

Field AccEntity
Field AccTexture

End Type


Type GameObjectAttributes

Field ModelName$,TexName$ ; Formerly TextureName$, but that's a Blitz3d keyword.
Field XScale#,YScale#,ZScale#
Field XAdjust#,YAdjust#,ZAdjust#
Field PitchAdjust#,YawAdjust#,RollAdjust#
Field DX#,DY#,DZ#
Field Pitch#,Yaw#,Roll#
Field Pitch2#,Yaw2#,Roll2#
Field XGoal#,YGoal#,ZGoal#
Field MovementType,MovementTypeData,Speed#,Radius#,RadiusType
Field Data10
Field PushDX#,PushDY#,AttackPower,DefensePower,DestructionType
Field ID,LogicType,LogicSubType ; Again, Type is a Blitz3d keyword.
Field Active,LastActive,ActivationType,ActivationSpeed
Field Status,Timer,TimerMax1,TimerMax2
Field Teleportable,ButtonPush,WaterReact
Field Telekinesisable,Freezable,Reactive,Child,Parent
Field Data0,Data1,Data2,Data3,Data4,Data5,Data6,Data7,Data8,Data9 ; Oh my god. I guess even the slightest convenience has a price.
Field TextData0$,TextData1$,TextData2$,TextData3$
Field Talkable,CurrentAnim,StandardAnim
Field MovementTimer,MovementSpeed,MoveXGoal,MoveYGoal,TileTypeCollision,ObjectTypeCollision
Field Caged,Dead,DeadTimer,Exclamation,Shadow,Linked,LinkBack,Flying,Frozen,Indigo,FutureInt24,FutureInt25
Field ScaleAdjust#,ScaleXAdjust#,ScaleYAdjust#,ScaleZAdjust#
Field FutureFloat5#,FutureFloat6#,FutureFloat7#,FutureFloat8#,FutureFloat9#,FutureFloat10#,FutureString1$,FutureString2$

End Type


Type GameObjectPosition

Field X#,Y#,Z#
Field OldX#,OldY#,OldZ#
Field TileX,TileY,TileX2,TileY2

End Type

Function NewGameObject.GameObject()

	Result.GameObject=New GameObject
	Result\Model=New GameObjectModel
	Result\Attributes=New GameObjectAttributes
	Result\Position=New GameObjectPosition
	Return Result

End Function


Global CurrentObject.GameObject=NewGameObject()
Global TempObject.GameObject=NewGameObject()

Dim LevelObjects.GameObject(MaxNofObjects)
Dim BrushObjects.GameObject(MaxNofObjects)
;Dim BrushObjectModels.GameObjectModel(MaxNofObjects)
;Dim BrushObjectAttributes.GameObjectAttributes(MaxNofObjects)

Global NofPreviewObjects=0
Dim PreviewObjects(MaxNofObjects)

For i=0 To MaxNofObjects
	LevelObjects.GameObject(i)=NewGameObject()
	BrushObjects.GameObject(i)=NewGameObject()
	;BrushObjectModels.GameObjectModels(i)=New GameObjectModel
	;BrushObjectAttributes.GameObjectAttributes(i)=New GameObjectAttributes
Next

Global NofSelectedObjects=0
Dim SelectedObjects(MaxNofObjects)
Global NofDraggedObjects=0
Dim DraggedObjects(MaxNofObjects)

; Whether the dragged objects or tiles have been moved at all.
Global DragChange=False

Global CurrentGrabbedObjectModified=False
Global PreviousSelectedObject=-1
Global NewSelectedObjectCount=0
Global ReadyToCopyFirstSelected=True

Global SelectionMinTileX=101
Global SelectionMaxTileX=101
Global SelectionMinTileY=-1
Global SelectionMaxTileY=-1

Dim CurrentGrabbedObjectMarkers(MaxNofObjects)

Global DragSpotX=-1
Global DragSpotY=-1
Global DragMinTileX=101
Global DragMaxTileX=101
Global DragMinTileY=-1
Global DragMaxTileY=-1

Global TileDragging=False

Function ResetDragSize()

	DragMinTileX=101
	DragMinTileY=101
	DragMaxTileX=-1
	DragMaxTileY=-1

End Function

Function UpdateDragSize(LevelObjectIndex)

	Pos.GameObjectPosition=LevelObjects(LevelObjectIndex)\Position
	If Pos\TileX<DragMinTileX
		DragMinTileX=Pos\TileX
	EndIf
	If Pos\TileX>DragMaxTileX
		DragMaxTileX=Pos\TileX
	EndIf
	If Pos\TileY<DragMinTileY
		DragMinTileY=Pos\TileY
	EndIf
	If Pos\TileY>DragMaxTileY
		DragMaxTileY=Pos\TileY
	EndIf

End Function

Function RecalculateDragSize()

	ResetDragSize()
	For i=0 To NofDraggedObjects-1
		UpdateDragSize(DraggedObjects(i))
	Next

End Function

Function ResetSelectionSize()

	SelectionMinTileX=101
	SelectionMinTileY=101
	SelectionMaxTileX=-1
	SelectionMaxTileY=-1

End Function

Function UpdateSelectionSize(LevelObjectIndex)

	Pos.GameObjectPosition=LevelObjects(LevelObjectIndex)\Position
	If Pos\TileX<SelectionMinTileX
		SelectionMinTileX=Pos\TileX
	EndIf
	If Pos\TileX>SelectionMaxTileX
		SelectionMaxTileX=Pos\TileX
	EndIf
	If Pos\TileY<SelectionMinTileY
		SelectionMinTileY=Pos\TileY
	EndIf
	If Pos\TileY>SelectionMaxTileY
		SelectionMaxTileY=Pos\TileY
	EndIf

End Function

Function RecalculateSelectionSize()

	ResetSelectionSize()
	For i=0 To NofSelectedObjects-1
		UpdateSelectionSize(SelectedObjects(i))
	Next

End Function

Function RecalculateObjectAdjusterModes()

	ReadyToCopyFirstSelected=True
	CurrentGrabbedObjectModified=False

	For i=0 To NofSelectedObjects-1
		ReadObjectIntoCurrentObject(LevelObjects(SelectedObjects(i)))
	Next
	
	BuildCurrentObjectModel()

End Function

Function GetSelectedObjectIndexInSelectedObjects(LevelObjectIndex)

	For i=0 To NofSelectedObjects-1
		If SelectedObjects(i)=LevelObjectIndex
			Return i
		EndIf
	Next
	
	Return -1

End Function

Function GetDraggedObjectIndexInDraggedObjects(LevelObjectIndex)

	For i=0 To NofDraggedObjects-1
		If DraggedObjects(i)=LevelObjectIndex
			Return i
		EndIf
	Next
	
	Return -1

End Function

Function IsObjectSelected(LevelObjectIndex)

	Return GetSelectedObjectIndexInSelectedObjects(LevelObjectIndex)<>-1

End Function

Function IsObjectDragged(LevelObjectIndex)

	Return GetDraggedObjectIndexInDraggedObjects(LevelObjectIndex)<>-1

End Function

Function IsOnlyObjectSelected(LevelObjectIndex)

	Return NofSelectedObjects=1 And IsObjectSelected(LevelObjectIndex)

End Function

Function StartObjectDrag()

	DragSpotX=BrushCursorX
	DragSpotY=BrushCursorY
	RecalculateDragSize()

End Function

Function ClearObjectDrag()

	NofDraggedObjects=0

End Function

Function ClearObjectSelection()

	NofSelectedObjects=0
	CurrentGrabbedObjectModified=False
	For i=0 To MaxNofObjects-1
		HideSelectedObjectMarker(i)
	Next
	MakeAllObjectAdjustersAbsolute()
	ReadyToCopyFirstSelected=True

End Function

Function AddSelectObject(LevelObjectIndex)

	If Not IsObjectSelected(LevelObjectIndex)
		AddSelectObjectInner(LevelObjectIndex)
	EndIf

End Function

Function AddSelectObjectInner(LevelObjectIndex)

	SelectedObjects(NofSelectedObjects)=LevelObjectIndex
	;ReadObjectIntoCurrentObject(LevelObjects(LevelObjectIndex))
	ShowSelectedObjectMarker(LevelObjectIndex)
	PreviousSelectedObject=LevelObjectIndex
	NofSelectedObjects=NofSelectedObjects+1
	NewSelectedObjectCount=NewSelectedObjectCount+1
	
	; Doing this discards any non-Updated changes to previously-selected objects.
	RecalculateObjectAdjusterModes()

End Function

Function RemoveSelectObject(LevelObjectIndex)

	Index=GetSelectedObjectIndexInSelectedObjects(LevelObjectIndex)
	If Index<>-1
		RemoveSelectObjectInner(Index)
		HideSelectedObjectMarker(LevelObjectIndex)
	EndIf

End Function

Function RemoveSelectObjectInner(Index)
	
	For i=Index To NofSelectedObjects-2
		SelectedObjects(i)=SelectedObjects(i+1)
	Next
	NofSelectedObjects=NofSelectedObjects-1
	
	RecalculateObjectAdjusterModes()
	
End Function

Function AddDragObject(LevelObjectIndex)

	If Not IsObjectDragged(LevelObjectIndex)
		AddDragObjectInner(LevelObjectIndex)
	EndIf

End Function

Function AddDragObjectInner(LevelObjectIndex)

	DraggedObjects(NofDraggedObjects)=LevelObjectIndex
	NofDraggedObjects=NofDraggedObjects+1

End Function

Function RemoveDraggedObject(LevelObjectIndex)

	Index=GetDraggedObjectIndexInDraggedObjects(LevelObjectIndex)
	If Index<>-1
		RemoveDraggedObjectInner(Index)
	EndIf

End Function

Function RemoveDraggedObjectInner(Index)

	For i=Index To NofDraggedObjects-2
		DraggedObjects(i)=DraggedObjects(i+1)
	Next
	NofDraggedObjects=NofDraggedObjects-1

End Function

Function ToggleSelectObject(LevelObjectIndex)
	
	Index=GetSelectedObjectIndexInSelectedObjects(LevelObjectIndex)
	If Index=-1
		AddSelectObjectInner(LevelObjectIndex)
	Else
		RemoveSelectObjectInner(Index)
		HideSelectedObjectMarker(LevelObjectIndex)
	EndIf

End Function

Function PrepareObjectSelection()

	If (Not CtrlDown())
		ClearObjectSelection()
	EndIf
	
	NewSelectedObjectCount=0

End Function

Function FinishObjectSelection()

	If NewSelectedObjectCount<>0
		BuildCurrentObjectModel()
		If AreAllObjectAdjustersAbsolute()
			SetBrushToCurrentObject()
		EndIf
	EndIf
	
	NofDraggedObjects=NofSelectedObjects
	For i=0 To NofDraggedObjects-1
		DraggedObjects(i)=SelectedObjects(i)
	Next
	StartObjectDrag()

End Function

Global CurrentAdjusterRandomized=False
Global CurrentAdjusterAbsolute=True
Global CurrentAdjusterZero=False
Global LeftAdj$=""
Global RightAdj$=""


Type ObjectAdjusterInt

Field Name$
Field RandomEnabled,RandomMin,RandomMax,RandomMinDefault,RandomMaxDefault
Field Absolute

End Type


Type ObjectAdjusterFloat

Field Name$
Field RandomEnabled,RandomMin#,RandomMax#,RandomMinDefault#,RandomMaxDefault#
Field Absolute

End Type

Type ObjectAdjusterString

Field Name$
Field RandomEnabled
Field Absolute

End Type


Function NewObjectAdjusterInt.ObjectAdjusterInt(Name$,RandomMin,RandomMax)

	Result.ObjectAdjusterInt=New ObjectAdjusterInt
	Result\Name$=Name$
	Result\RandomEnabled=False
	Result\RandomMin=RandomMin
	Result\RandomMax=RandomMax
	Result\RandomMinDefault=RandomMin
	Result\RandomMaxDefault=RandomMax
	Result\Absolute=True
	Return Result

End Function

Function NewObjectAdjusterFloat.ObjectAdjusterFloat(Name$,RandomMin#,RandomMax#)

	Result.ObjectAdjusterFloat=New ObjectAdjusterFloat
	Result\Name$=Name$
	Result\RandomEnabled=False
	Result\RandomMin#=RandomMin
	Result\RandomMax#=RandomMax
	Result\RandomMinDefault#=RandomMin
	Result\RandomMaxDefault#=RandomMax
	Result\Absolute=True
	Return Result

End Function

Function NewObjectAdjusterString.ObjectAdjusterString(Name$)

	Result.ObjectAdjusterString=New ObjectAdjusterString
	Result\Name$=Name$
	Result\RandomEnabled=False
	Result\Absolute=True
	Return Result

End Function


Function AdjustObjectAdjusterInt(ObjectAdjuster.ObjectAdjusterInt,CurrentValue,SlowInt,FastInt,DelayTime)

	If ObjectAdjuster\RandomEnabled
		If OnLeftHalfAdjuster()
			ObjectAdjuster\RandomMin=AdjustInt(ObjectAdjuster\Name$+" Min: ", ObjectAdjuster\RandomMin, SlowInt, FastInt, DelayTime)
		Else
			ObjectAdjuster\RandomMax=AdjustInt(ObjectAdjuster\Name$+" Max: ", ObjectAdjuster\RandomMax, SlowInt, FastInt, DelayTime)
		EndIf
	Else
		CurrentValue=AdjustInt(ObjectAdjuster\Name$+": ", CurrentValue, SlowInt, FastInt, DelayTime)
		If UsedRawInput
			ObjectAdjuster\Absolute=True
		EndIf
	EndIf
	If ReturnPressed()
		ObjectAdjuster\RandomEnabled=Not ObjectAdjuster\RandomEnabled
		ObjectAdjuster\RandomMin=ObjectAdjuster\RandomMinDefault
		ObjectAdjuster\RandomMax=ObjectAdjuster\RandomMaxDefault
	EndIf
	Return CurrentValue

End Function


Function AdjustObjectAdjusterFloat#(ObjectAdjuster.ObjectAdjusterFloat,CurrentValue#,SlowFloat#,FastFloat#,DelayTime)

	If ObjectAdjuster\RandomEnabled
		If OnLeftHalfAdjuster()
			ObjectAdjuster\RandomMin=AdjustFloat#(ObjectAdjuster\Name$+" Min: ", ObjectAdjuster\RandomMin, SlowFloat#, FastFloat#, DelayTime)
		Else
			ObjectAdjuster\RandomMax=AdjustFloat#(ObjectAdjuster\Name$+" Max: ", ObjectAdjuster\RandomMax, SlowFloat#, FastFloat#, DelayTime)
		EndIf
	Else
		CurrentValue#=AdjustFloat#(ObjectAdjuster\Name$+": ", CurrentValue, SlowFloat#, FastFloat#, DelayTime)
		If UsedRawInput
			ObjectAdjuster\Absolute=True
		EndIf
	EndIf
	If ReturnPressed()
		ObjectAdjuster\RandomEnabled=Not ObjectAdjuster\RandomEnabled
		ObjectAdjuster\RandomMin=ObjectAdjuster\RandomMinDefault
		ObjectAdjuster\RandomMax=ObjectAdjuster\RandomMaxDefault
	EndIf
	Return CurrentValue

End Function


Function AdjustObjectAdjusterToggle(ObjectAdjuster.ObjectAdjusterInt,CurrentValue,SlowInt,FastInt,RawInput,ValueLow,ValueHigh,DelayTime)
	
	If ObjectAdjuster\RandomEnabled
		If OnLeftHalfAdjuster()
			ObjectAdjuster\RandomMin=AdjustInt(ObjectAdjuster\Name$+" Min: ", ObjectAdjuster\RandomMin, SlowInt, FastInt, DelayTime)
		Else
			ObjectAdjuster\RandomMax=AdjustInt(ObjectAdjuster\Name$+" Max: ", ObjectAdjuster\RandomMax, SlowInt, FastInt, DelayTime)
		EndIf
	ElseIf ReturnKey=False And MouseDebounceFinished()
		If RawInput=True
			CurrentValue=InputInt(ObjectAdjuster\Name$+": ")
			ObjectAdjuster\Absolute=True
		Else
			If ObjectAdjuster\Absolute
				If CurrentValue=ValueLow
					CurrentValue=ValueHigh
				Else
					CurrentValue=ValueLow
				EndIf
			Else
				CurrentValue=AdjustInt(ObjectAdjuster\Name$+": ", CurrentValue, SlowInt, FastInt, DelayTime)
			EndIf
		EndIf
		If MouseScroll=0
			MouseDebounceSet(DelayTime)
		EndIf
	EndIf
	If ReturnPressed()
		ObjectAdjuster\RandomEnabled=Not ObjectAdjuster\RandomEnabled
		ObjectAdjuster\RandomMin=ObjectAdjuster\RandomMinDefault
		ObjectAdjuster\RandomMax=ObjectAdjuster\RandomMaxDefault
	EndIf
	Return CurrentValue
	
End Function


Function AdjustObjectAdjusterBits(ObjectAdjuster.ObjectAdjusterInt,CurrentValue,i,DelayTime)

	If ObjectAdjuster\Absolute And (Not ObjectAdjuster\RandomEnabled) And (LeftMouse=True Or RightMouse=True Or MouseScroll<>0) And MouseDebounceFinished()
		StartX=SidebarX+10
		StartY=SidebarY+305
		StartY=StartY+15+(i-ObjectAdjusterStart)*15
		tex2$="TTC"
		tex$="00000 00000 00000"
		
		HalfNameWidth=4*Len(tex2$+": "+tex$)
		BitStartX=StartX+92-HalfNameWidth+8*Len(tex2$+": ")
		
		BitPositionIndex=GetBitPositionIndex(BitStartX)
		BitIndex=BitPositionIndexToBitIndex(BitPositionIndex)
		If BitIndexIsValid(BitIndex) And BitPositionIndexIsValid(BitPositionIndex)
			CurrentValue=CurrentValue Xor 2^BitIndex
		EndIf
		
		If LeftMouse=True Or RightMouse=True
			MouseDebounceSet(DelayTime)
		EndIf
	EndIf
	If CtrlDown()
		CurrentValue=InputInt(ObjectAdjuster\Name$+": ")
		ObjectAdjuster\Absolute=True
	EndIf
	If ReturnPressed()
		ObjectAdjuster\RandomEnabled=Not ObjectAdjuster\RandomEnabled
	EndIf
	Return CurrentValue

End Function


Function RandomObjectAdjusterInt(ObjectAdjuster.ObjectAdjusterInt)

	Return Rand(ObjectAdjuster\RandomMin,ObjectAdjuster\RandomMax)

End Function

Function RandomObjectAdjusterFloat#(ObjectAdjuster.ObjectAdjusterFloat)

	Return Rnd#(ObjectAdjuster\RandomMin,ObjectAdjuster\RandomMax)

End Function


Function SetAdjusterDisplayInt$(ObjectAdjuster.ObjectAdjusterInt,CurrentValue,tex$)

	CurrentAdjusterRandomized=ObjectAdjuster\RandomEnabled
	CurrentAdjusterAbsolute=ObjectAdjuster\Absolute
	CurrentAdjusterZero=(CurrentValue=0)
	LeftAdj$=ObjectAdjuster\RandomMin
	RightAdj$=ObjectAdjuster\RandomMax
	If CurrentAdjusterAbsolute
		Return tex$
	Else
		Return CurrentValue
	EndIf

End Function

Function SetAdjusterDisplayFloat$(ObjectAdjuster.ObjectAdjusterFloat,CurrentValue#,tex$)

	CurrentAdjusterRandomized=ObjectAdjuster\RandomEnabled
	CurrentAdjusterAbsolute=ObjectAdjuster\Absolute
	CurrentAdjusterZero=(CurrentValue=0)
	LeftAdj$=ObjectAdjuster\RandomMin
	RightAdj$=ObjectAdjuster\RandomMax
	If CurrentAdjusterAbsolute
		Return tex$
	Else
		Return CurrentValue
	EndIf

End Function

Function SetAdjusterDisplayString$(ObjectAdjuster.ObjectAdjusterString,CurrentValue$,tex$)

	CurrentAdjusterRandomized=ObjectAdjuster\RandomEnabled
	CurrentAdjusterAbsolute=ObjectAdjuster\Absolute
	CurrentAdjusterZero=True
	LeftAdj$=""
	RightAdj$=""
	If CurrentAdjusterAbsolute
		Return tex$
	Else
		Return "..."
	EndIf

End Function


Global ObjectAdjusterDefensePower.ObjectAdjusterInt=NewObjectAdjusterInt("DefensePower",0,33)
Global ObjectAdjusterAttackPower.ObjectAdjusterInt=NewObjectAdjusterInt("AttackPower",0,33)
Global ObjectAdjusterDestructionType.ObjectAdjusterInt=NewObjectAdjusterInt("DestructionType",0,1)
Global ObjectAdjusterID.ObjectAdjusterInt=NewObjectAdjusterInt("ID",100,200)
Global ObjectAdjusterLogicType.ObjectAdjusterInt=NewObjectAdjusterInt("Type",170,173)
Global ObjectAdjusterLogicSubType.ObjectAdjusterInt=NewObjectAdjusterInt("SubType",0,8)
Global ObjectAdjusterActivationSpeed.ObjectAdjusterInt=NewObjectAdjusterInt("ActivationSpeed",2,40)
Global ObjectAdjusterActivationType.ObjectAdjusterInt=NewObjectAdjusterInt("ActivationType",12,16)
Global ObjectAdjusterTimerMax1.ObjectAdjusterInt=NewObjectAdjusterInt("TimerMax1",1,100)
Global ObjectAdjusterTimerMax2.ObjectAdjusterInt=NewObjectAdjusterInt("TimerMax2",1,100)
Global ObjectAdjusterTimer.ObjectAdjusterInt=NewObjectAdjusterInt("Timer",1,100)
Global ObjectAdjusterWaterReact.ObjectAdjusterInt=NewObjectAdjusterInt("WaterReact",0,10)
Global ObjectAdjusterFreezable.ObjectAdjusterInt=NewObjectAdjusterInt("Freezable",0,1)
Global ObjectAdjusterFrozen.ObjectAdjusterInt=NewObjectAdjusterInt("Frozen",0,100)
Global ObjectAdjusterTalkable.ObjectAdjusterInt=NewObjectAdjusterInt("Talkable",0,100)
Global ObjectAdjusterMovementSpeed.ObjectAdjusterInt=NewObjectAdjusterInt("MovementSpeed",10,40)
Global ObjectAdjusterMovementType.ObjectAdjusterInt=NewObjectAdjusterInt("MovementType",41,48)
Global ObjectAdjusterMovementTypeData.ObjectAdjusterInt=NewObjectAdjusterInt("MovementTypeData",0,30)
Global ObjectAdjusterExclamation.ObjectAdjusterInt=NewObjectAdjusterInt("Exclamation",0,99)
Global ObjectAdjusterLinked.ObjectAdjusterInt=NewObjectAdjusterInt("Linked",0,10)
Global ObjectAdjusterLinkBack.ObjectAdjusterInt=NewObjectAdjusterInt("LinkBack",0,10)
Global ObjectAdjusterShadow.ObjectAdjusterInt=NewObjectAdjusterInt("Shadow",0,10)
Global ObjectAdjusterParent.ObjectAdjusterInt=NewObjectAdjusterInt("Parent",0,10)
Global ObjectAdjusterChild.ObjectAdjusterInt=NewObjectAdjusterInt("Child",0,10)
Global ObjectAdjusterData10.ObjectAdjusterInt=NewObjectAdjusterInt("Data10",0,10)
Global ObjectAdjusterCaged.ObjectAdjusterInt=NewObjectAdjusterInt("Caged",0,1)
Global ObjectAdjusterDead.ObjectAdjusterInt=NewObjectAdjusterInt("Dead",0,3)
Global ObjectAdjusterDeadTimer.ObjectAdjusterInt=NewObjectAdjusterInt("DeadTimer",1,100)
Global ObjectAdjusterMovementTimer.ObjectAdjusterInt=NewObjectAdjusterInt("MovementTimer",0,1000)
Global ObjectAdjusterFlying.ObjectAdjusterInt=NewObjectAdjusterInt("Flying",0,20)
Global ObjectAdjusterIndigo.ObjectAdjusterInt=NewObjectAdjusterInt("Indigo",0,1)
Global ObjectAdjusterStatus.ObjectAdjusterInt=NewObjectAdjusterInt("Status",0,10)
Global ObjectAdjusterButtonPush.ObjectAdjusterInt=NewObjectAdjusterInt("ButtonPush",0,1)
Global ObjectAdjusterTeleportable.ObjectAdjusterInt=NewObjectAdjusterInt("Teleportable",0,1)
Global ObjectAdjusterTileTypeCollision.ObjectAdjusterInt=NewObjectAdjusterInt("TileTypeCollision",0,1)
Global ObjectAdjusterObjectTypeCollision.ObjectAdjusterInt=NewObjectAdjusterInt("ObjectTypeCollision",0,1)
Global ObjectAdjusterActive.ObjectAdjusterInt=NewObjectAdjusterInt("Active",0,1001)
Global ObjectAdjusterMoveXGoal.ObjectAdjusterInt=NewObjectAdjusterInt("MoveXGoal",0,39)
Global ObjectAdjusterMoveYGoal.ObjectAdjusterInt=NewObjectAdjusterInt("MoveYGoal",0,39)
Global ObjectAdjusterData0.ObjectAdjusterInt=NewObjectAdjusterInt("Data0",0,10)
Global ObjectAdjusterData1.ObjectAdjusterInt=NewObjectAdjusterInt("Data1",0,10)
Global ObjectAdjusterData2.ObjectAdjusterInt=NewObjectAdjusterInt("Data2",0,10)
Global ObjectAdjusterData3.ObjectAdjusterInt=NewObjectAdjusterInt("Data3",0,10)
Global ObjectAdjusterData4.ObjectAdjusterInt=NewObjectAdjusterInt("Data4",0,10)
Global ObjectAdjusterData5.ObjectAdjusterInt=NewObjectAdjusterInt("Data5",0,10)
Global ObjectAdjusterData6.ObjectAdjusterInt=NewObjectAdjusterInt("Data6",0,10)
Global ObjectAdjusterData7.ObjectAdjusterInt=NewObjectAdjusterInt("Data7",0,10)
Global ObjectAdjusterData8.ObjectAdjusterInt=NewObjectAdjusterInt("Data8",0,10)
Global ObjectAdjusterData9.ObjectAdjusterInt=NewObjectAdjusterInt("Data9",0,10)
Global ObjectAdjusterCurrentAnim.ObjectAdjusterInt=NewObjectAdjusterInt("CurrentAnim",0,10)
Global ObjectAdjusterStandardAnim.ObjectAdjusterInt=NewObjectAdjusterInt("StandardAnim",0,10)

Global ObjectAdjusterYawAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("YawAdjust",0.0,360.0)
Global ObjectAdjusterRollAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("RollAdjust",0.0,360.0)
Global ObjectAdjusterPitchAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("PitchAdjust",0.0,360.0)
Global ObjectAdjusterXAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("XAdjust",-0.5,0.5)
Global ObjectAdjusterYAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("YAdjust",-0.5,0.5)
Global ObjectAdjusterZAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ZAdjust",-0.5,0.5)
Global ObjectAdjusterXScale.ObjectAdjusterFloat=NewObjectAdjusterFloat("XScale",0.5,1.5)
Global ObjectAdjusterYScale.ObjectAdjusterFloat=NewObjectAdjusterFloat("YScale",0.5,1.5)
Global ObjectAdjusterZScale.ObjectAdjusterFloat=NewObjectAdjusterFloat("ZScale",0.5,1.5)
Global ObjectAdjusterScaleAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ScaleAdjust",0.5,1.5)
Global ObjectAdjusterScaleXAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ScaleXAdjust",0.5,1.5)
Global ObjectAdjusterScaleYAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ScaleYAdjust",0.5,1.5)
Global ObjectAdjusterScaleZAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ScaleZAdjust",0.5,1.5)
Global ObjectAdjusterX.ObjectAdjusterFloat=NewObjectAdjusterFloat("X",-0.5,0.5)
Global ObjectAdjusterY.ObjectAdjusterFloat=NewObjectAdjusterFloat("Y",-0.5,0.5)
Global ObjectAdjusterZ.ObjectAdjusterFloat=NewObjectAdjusterFloat("Z",-0.5,0.5)
Global ObjectAdjusterDX.ObjectAdjusterFloat=NewObjectAdjusterFloat("DX",-1.0,1.0)
Global ObjectAdjusterDY.ObjectAdjusterFloat=NewObjectAdjusterFloat("DY",-1.0,1.0)
Global ObjectAdjusterDZ.ObjectAdjusterFloat=NewObjectAdjusterFloat("DZ",-1.0,1.0)
Global ObjectAdjusterSpeed.ObjectAdjusterFloat=NewObjectAdjusterFloat("Speed",-0.5,0.5)
Global ObjectAdjusterRadius.ObjectAdjusterFloat=NewObjectAdjusterFloat("Radius",-0.5,0.5)

Global ObjectAdjusterTextureName.ObjectAdjusterString=NewObjectAdjusterString("TextureName")
Global ObjectAdjusterModelName.ObjectAdjusterString=NewObjectAdjusterString("ModelName")
Global ObjectAdjusterTextData0.ObjectAdjusterString=NewObjectAdjusterString("TextData0")
Global ObjectAdjusterTextData1.ObjectAdjusterString=NewObjectAdjusterString("TextData1")

Const CurrentObjectTargetIDCount=4
Dim CurrentObjectTargetID(CurrentObjectTargetIDCount-1)
Dim CurrentObjectTargetIDEnabled(CurrentObjectTargetIDCount-1)

Const PlayerActivateId=-2
Const CurrentObjectActivateIdCount=3
Dim CurrentObjectActivateId(CurrentObjectActivateIdCount-1)
Dim CurrentObjectActivateIdEnabled(CurrentObjectActivateIdCount-1)

Global IDFilterEnabled=False
Global IDFilterInverted=False
Global IDFilterAllow=-1

Global TexturePrefix$=""

Global SimulationLevel=1
Const SimulationLevelMax=4
Const SimulationLevelAnimation=1
Const SimulationLevelMusic=4

;Dim BrushObjectXOffset#(1000),BrushObjectYOffset#(1000)
Dim BrushObjectTileXOffset(1000),BrushObjectTileYOffset(1000)

Global NofBrushObjects=0

Global BrushSpaceOriginX
Global BrushSpaceOriginY
Global BrushSpaceWidth=1
Global BrushSpaceHeight=1


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








; ******************************************************


; Setup Graphics, Lights, Camera
; ================================
;If displayfullscreen=True
;	Graphics3D 800,600,16,1
;	SetBuffer BackBuffer()
;Else
;	Graphics3D 800,600,16,2
;	SetBuffer BackBuffer()
;	Graphics3D 800,600,16,3
;EndIf




Global NofMyGfxModes, GfxMode
Dim MyGfxModeWidth(1000),MyGfxModeHeight(1000),MyGfxModeDepth(1000)
Global GfxWidth,GfxHeight,GfxDepth,GfxWindowed
Global GfxZoomScaling#
Global TilePickerZoomScaling#

Const EditorDisplayFile$="displaymnikeditor.wdf"

Function ReadDisplayFile()

	file=ReadFile (globaldirname$+"\"+EditorDisplayFile$)
	If file>0
	
		NofMyGfxModes=ReadInt(file)
		For i=0 To NofMyGfxModes-1
			MyGfxModeWidth(i)=ReadInt(file)
			MyGfxModeHeight(i)=ReadInt(file)
			MyGfxModeDepth(i)=ReadInt(file)
		Next
		GfxMode=ReadInt(file)
		GfxWindowed=ReadInt(file)
		GfxWidth=MyGfxModeWidth(gfxmode)
		GfxHeight=MyGfxModeHeight(gfxmode)
		GfxDepth=MyGfxModeDepth(gfxmode)
	Else
		PopulateGfxModes()
		GfxWidth=800
		GfxHeight=600
		GfxWindowed=1
		GfxDepth=16
	EndIf

End Function

Function WriteDisplayFile()

	file=WriteFile(globaldirname$+"\"+EditorDisplayFile$)

	WriteInt file,NofMyGfxModes
	
	For i=0 To NofMyGfxModes-1
		WriteInt file,MyGfxModeWidth(i)
		WriteInt file,MyGfxModeHeight(i)
		WriteInt file,MyGfxModeDepth(i)
		
		
	Next
	
	WriteInt file,GfxMode
	WriteInt file,GfxWindowed
	
	CloseFile file

End Function

Function PopulateGfxModes()
	
	j=0
	For i=1 To CountGfxModes3D()
		ratio#=Float(GfxModeWidth(i))/Float(GfxModeHeight(i))
		If ratio#>1.33 And ratio#<1.34 And GfxModeWidth(i)>=640
			; list all 4:3 modes above 640x480
			MyGfxModeWidth(j)=GfxModeWidth(i)
			MyGfxModeHeight(j)=GfxModeHeight(i)
			MyGfxModeDepth(j)=GfxModeDepth(i)
			;IsWideScreen(j)=False
			j=j+1
		ElseIf ratio#>1.77 And ratio#<1.78 And GfxModeWidth(i)>=640 ;And widescreen=True
			MyGfxModeWidth(j)=GfxModeWidth(i)
			MyGfxModeHeight(j)=GfxModeHeight(i)
			MyGfxModeDepth(j)=GfxModeDepth(i)
			;IsWideScreen(j)=True
			j=j+1
		EndIf
	Next
	NofMyGfxModes=j
	
	GfxMode=-1	
	
	If GfxMode=-1	
		For j=0 To NofMyGfxModes-1
			If MyGfxModeWidth(j)=800 And MyGfxModeheight(j)=600 And MyGfxModeDepth(j)=32
				GfxMode=j
			EndIf
		Next
	EndIf
	If GfxMode=-1
		For j=0 To NofMyGfxModes-1
			If MyGfxModeWidth(j)=800 And MyGfxModeHeight(j)=600 And MyGfxModeDepth(j)=16
				GfxMode=j
			EndIf
		Next
	EndIf

End Function

ReadDisplayFile()

; something is wrong withe graphics mode: try different versions
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	GfxWidth=800
	GfxHeight=600
	GfxDepth=16
EndIf
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	GfxWidth=800
	GfxHeight=600
	GfxDepth=32
EndIf
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	GfxWidth=640
	GfxHeight=480
	GfxDepth=16
EndIf
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	GfxWidth=640
	GfxHeight=480
	GfxDepth=32
EndIf
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	Print "Unable to set graphics mode!"
	Print ""
	Print "Please ensure that your video card drivers"
	Print "are up-to-date, or use the graphic options"
	Print "to select a different display mode."
	Print ""
	Print "Exiting... press any key."
	WaitKey()
	End
EndIf

;widescreen
Global widescreen=False
Global wideicons=True
Global FitForWidescreenGlobal ;read from master.dat
Global FitForWidescreenGlobalHub ;reserved when starting an adventure in a custom hub
Global FitForWidescreen ;read from .wlv
ratio#=Float(GfxWidth)/Float(GfxHeight)

If ratio#>1.77 And ratio#<1.78 ;aspect ratio must be 16:9
	widescreen=True
EndIf

Const OriginalRatio#=800.0/600.0
Global GfxAspectRatio#

GfxWindowed=2 ; Force windowed mode

Graphics3D GfxWidth,GfxHeight,GfxDepth,GfxWindowed
SetBuffer BackBuffer()

If GfxWindowed=1
	displayfullscreen=True
Else
	displayfullscreen=False
EndIf

;ShowMessage("Graphics3D: "+GfxWidth+" x "+GfxHeight+" with depth "+GfxDepth,1000)

Const LettersCountX=44
Const LettersCountY=30

Const ToolbarHeight=100
Const SidebarWidth=300

Global ToolbarBrushModeX
Global ToolbarBrushModeY

Global ToolbarBrushSizeX
Global ToolbarBrushSizeY

Global ToolbarTexPrefixX
Global ToolbarTexPrefixY

Global ToolbarDensityX
Global ToolbarDensityY

Global ToolbarElevateX
Global ToolbarElevateY

Global ToolbarBrushWrapX
Global ToolbarBrushWrapY

Global ToolbarStepPerX
Global ToolbarStepPerY

Global ToolbarShowMarkersX
Global ToolbarShowMarkersY

Global ToolbarShowObjectsX
Global ToolbarShowObjectsY

Global ToolbarShowLogicX
Global ToolbarShowLogicY

Global ToolbarShowLevelX
Global ToolbarShowLevelY

Global ToolbarIDFilterX
Global ToolbarIDFilterY

Global ToolbarSimulationLevelX
Global ToolbarSimulationLevelY

Global ToolbarExitX
Global ToolbarExitY

Global ToolbarSaveX
Global ToolbarSaveY

Global LetterWidth#
Global LetterHeight#

Const CharWidth#=0.045
Const CharHeight#=0.05

Global LevelViewportWidth
Global LevelViewportHeight

Global SidebarX
Global SidebarY

Global FlStartX
Global FlStartY

Global LowerButtonsCutoff

;ahaha goodness...

Function LetterX(x#)

	Return GfxWidth/2+LetterWidth*(x#-LettersCountX/2)

End Function

Function LevelViewportX(x#)

	Return -(LevelViewportWidth-LevelViewportHeight)/2+x#
	
End Function






Global Light
AmbientLight 155,155,155

Global Camera1 ; level camera
Global Camera2
Global camera3
Global camera4 ; object camera
Global camera ; text screen camera

; the current projection mode for each camera
Global Camera1Proj=0
Global Camera2Proj=0
Global Camera3Proj=0
Global Camera4Proj=0
Global CameraProj=0

Global CameraPanning=False
Global GameCamera=False ; whether "game camera mode" is active (simulates the in-game camera)

Global Camera1PerspectiveZoom#
Global Camera1OrthographicZoom#
Global Camera1Zoom#
Global Camera4Zoom#

Global Camera1StartY=6 ;/GfxZoomScaling# ;*GfxAspectRatio#
; saved when entering orthographic mode since orthographic mode mouse wheel scrolling does not change the height, unlike perspective mode mouse wheel scrolling
Global Camera1PerspectiveY#=Camera1StartY
Global Camera1SavedProjMode=1 ; the projection mode to return to after being in projection mode 0 (which means the camera is disabled)

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
Dim CursorMeshPillar(3)
Dim CursorMeshOpaque(3)
Global CursorMeshTexturePicker

Global BrushMesh
Global BrushSurface
Global BrushSurfaceVertexCount=0

Global BrushTextureMesh
Global BrushTextureSurface

Const BrushMeshAlpha#=0.3
Const BrushMeshObjectAlpha#=0.5
Const BrushMeshOffsetY#=0.01

Function ClearBrushSurface()
	
	ClearSurface BrushSurface
	
	ClearBrushPreviewSurface()
	
End Function

Function ClearBrushPreviewSurface()

	For i=0 To NofPreviewObjects-1
		FreeEntity PreviewObjects(i)
	Next
	NofPreviewObjects=0
	
	ClearSurface BrushTextureSurface
	BrushSurfaceVertexCount=0

End Function

Function ShowBrushSurface()

	ShowEntity BrushMesh
	ShowEntity BrushTextureMesh

End Function

Function HideBrushSurface()

	HideEntity BrushMesh
	HideEntity BrushTextureMesh
	
End Function

Function FinishBrushSurface()
	
	UpdateNormals BrushMesh
	UpdateNormals BrushTextureMesh
	
End Function

Function AddSquareToBrushSurface(TheSurface,i,j,y#,SetTexCoords)

	; Stupid hack to prevent MAVs from too many vertices at immense brush sizes.
	; It's meant to be a temporary solution but I might also just keep this forever. That's the nature of software engineering.
	If BrushSurfaceVertexCount=32000
		Return
	EndIf
	
	StartingVertex=BrushSurfaceVertexCount
	
	AddVertex TheSurface,i,y#+BrushMeshOffsetY#,-j
	AddVertex TheSurface,i+1,y#+BrushMeshOffsetY#,-j
	AddVertex TheSurface,i,y#+BrushMeshOffsetY#,-j-1
	AddVertex TheSurface,i+1,y#+BrushMeshOffsetY#,-j-1
	
	AddTriangle TheSurface,StartingVertex+0,StartingVertex+1,StartingVertex+2
	AddTriangle TheSurface,StartingVertex+1,StartingVertex+3,StartingVertex+2
		
	If SetTexCoords
		TheTile.Tile=BrushTiles(LevelSpaceToBrushSpaceX(i,BrushWrap),LevelSpaceToBrushSpaceY(j,BrushWrap))
		CalculateUV(TheTile\Terrain\Texture,0,0,TheTile\Terrain\Rotation,8,1)
		VertexTexCoords(TheSurface,StartingVertex+0,ChunkTileU#,ChunkTileV#)
		CalculateUV(TheTile\Terrain\Texture,1,0,TheTile\Terrain\Rotation,8,1)
		VertexTexCoords(TheSurface,StartingVertex+1,ChunkTileU#,ChunkTileV#)
		CalculateUV(TheTile\Terrain\Texture,0,1,TheTile\Terrain\Rotation,8,1)
		VertexTexCoords(TheSurface,StartingVertex+2,ChunkTileU#,ChunkTileV#)
		CalculateUV(TheTile\Terrain\Texture,1,1,TheTile\Terrain\Rotation,8,1)
		VertexTexCoords(TheSurface,StartingVertex+3,ChunkTileU#,ChunkTileV#)
	EndIf

	BrushSurfaceVertexCount=BrushSurfaceVertexCount+4

End Function

Function AddTileToBrushSurfaceActual(TheSurface,x,y,BrushSpaceX,BrushSpaceY,SetTexCoords)

	AddSquareToBrushSurface(TheSurface,x,y,0.0,SetTexCoords)
	
	If IsPositionInLevel(x,y)
		SquareHeight#=GetTileTotalHeight(LevelTiles(x,y))
		If SquareHeight#<>0.0
			AddSquareToBrushSurface(TheSurface,x,y,SquareHeight#,SetTexCoords)
		EndIf
	EndIf

End Function

Function AddTileToBrushSurface(TheSurface,x,y,BrushSpaceX,BrushSpaceY,SetTexCoords)

	If BrushMode=BrushModeSetMirror
		Return
	EndIf
	
	BrushSpaceX=LevelSpaceToBrushSpaceX(x,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(y,BrushWrap)

	AddTileToBrushSurfaceActual(TheSurface,x,y,BrushSpaceX,BrushSpaceY,SetTexCoords)
	
	If DupeMode=DupeModeX
		TargetX=MirrorAcrossInt(x,MirrorPositionX)
		AddTileToBrushSurfaceActual(TheSurface,TargetX,y,BrushSpaceX,BrushSpaceY,SetTexCoords)
	ElseIf DupeMode=DupeModeY
		TargetY=MirrorAcrossInt(y,MirrorPositionY)
		AddTileToBrushSurfaceActual(TheSurface,x,TargetY,BrushSpaceX,BrushSpaceY,SetTexCoords)
	ElseIf DupeMode=DupeModeXPlusY
		TargetX=MirrorAcrossInt(x,MirrorPositionX)
		TargetY=MirrorAcrossInt(y,MirrorPositionY)
		AddTileToBrushSurfaceActual(TheSurface,TargetX,y,BrushSpaceX,BrushSpaceY,SetTexCoords)
		AddTileToBrushSurfaceActual(TheSurface,x,TargetY,BrushSpaceX,BrushSpaceY,SetTexCoords)
		AddTileToBrushSurfaceActual(TheSurface,TargetX,TargetY,BrushSpaceX,BrushSpaceY,SetTexCoords)
	EndIf

End Function

Function AddTileToBrushPreview(x,y)

	BrushSpaceX=LevelSpaceToBrushSpaceX(x,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(y,BrushWrap)

	If EditorMode=0
		AddTileToBrushSurface(BrushTextureSurface,x,y,BrushSpaceX,BrushSpaceY,True)
	ElseIf EditorMode=3
		For k=0 To NofBrushObjects-1
			If BrushObjectTileXOffset(k)=BrushSpaceX And BrushObjectTileYOffset(k)=BrushSpaceY
				If NofPreviewObjects<MaxNofObjects-NofObjects
					Preview=CopyEntity(BrushObjects(k)\Model\Entity)
					SetEntityAlphaWithModelName(Preview,BrushMeshObjectAlpha#,BrushObjects(k)\Attributes\ModelName$)
					PositionEntityWithXYZAdjust(Preview,x+0.5,y+0.5,BrushObjects(k)\Position\Z,BrushObjects(k)\Attributes)
					PreviewObjects(NofPreviewObjects)=Preview
					NofPreviewObjects=NofPreviewObjects+1
				EndIf
			EndIf
		Next
	EndIf

End Function

; MousePlane
;Const MouseSurfaceMinX=0
;Const MouseSurfaceMinY=0
;Const MouseSurfaceMaxX=100
;Const MouseSurfaceMaxY=100
Const MouseSurfaceMinX=-100
Const MouseSurfaceMinY=-100
Const MouseSurfaceMaxX=200
Const MouseSurfaceMaxY=200

Global MousePlane=CreateMesh()
MouseSurface=CreateSurface(MousePlane)
AddVertex MouseSurface,MouseSurfaceMinX,0,-MouseSurfaceMinY ;0,0,0
AddVertex MouseSurface,MouseSurfaceMaxX,0,-MouseSurfaceMinY ;100,0,0
AddVertex MouseSurface,MouseSurfaceMinX,0,-MouseSurfaceMaxY ;0,0,-100
AddVertex MouseSurface,MouseSurfaceMaxX,0,-MouseSurfaceMaxY ;100,0,-100
AddTriangle MouseSurface,0,1,2
AddTriangle MouseSurface,1,3,2
EntityPickMode MousePlane,2
EntityAlpha MousePlane,0
; TexturePlane
Global TexturePlane
; CurrentTile
CurrentMesh=CreateMesh()
CurrentSurface=CreateSurface(CurrentMesh)
; CurrentWaterTile
Global CurrentWaterTile
Global CurrentWaterTileSurface

; CurrentObjectMarker
Global CurrentObjectMarkerMesh

; ObjectPositionMarker
Global ObjectPositionMarkerMesh=CreateCube()
ScaleMesh ObjectPositionMarkerMesh,0.08,90,0.08
;EntityAlpha ObjectPositionMarkerMesh,.3
;EntityColor ObjectPositionMarkerMesh,100,255,100
HideEntity ObjectPositionMarkerMesh



; Load Textures
; =================
Global ButtonTexture, GateTexture
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
Dim StinkerWeeTexture(3)
Dim StinkerWeeTextureSleep(3)
Dim StinkerWeeTextureSad(3)
For i=1 To 3
	StinkerWeeTexture(i)=MyLoadTexture("data/models/stinkerwee/stinkerwee"+Str$(i)+".jpg",1)
	StinkerWeeTextureSleep(i)=MyLoadTexture("data/models/stinkerwee/stinkerwee"+Str$(i)+"sleep.jpg",1)
	StinkerWeeTextureSad(i)=MyLoadTexture("data/models/stinkerwee/stinkerwee"+Str$(i)+"sad.jpg",1)
Next
EntityTexture StinkerWeeMesh,StinkerWeeTexture(1)
HideEntity StinkerWeeMesh

; Stinker

Global StinkerMesh=myLoadAnimMesh("data/models/stinker/body.b3d",0)
UpdateNormals GetChild(StinkerMesh,3)
ExtractAnimSeq GetChild(StinkerMesh,3),1,20		; 1 - waddle, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),21,40	; 2 - walk, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),41,60	; 3 - run, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),61,100	; 4 - spell, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),61,80	; 5 - spell on, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),80,84	; 6 - spell hold, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),84,100	; 7 - spell off, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),101,120	; 8 - wave, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),121,140	; 9 - foottap, speed .15
ExtractAnimSeq GetChild(StinkerMesh,3),141,160	; 10 - idle, speed .05
ExtractAnimSeq GetChild(StinkerMesh,3),161,180	; 11 - death, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),181,200	; 12 - dance, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),201,220	; 13 - sit, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),201,218	; 14 - sit, speed not too far back .2
ExtractAnimSeq GetChild(StinkerMesh,3),109,112	; 15 - constant wave, speed .2
ExtractAnimSeq GetChild(StinkerMesh,3),221,240	; 16 - use
ExtractAnimSeq GetChild(StinkerMesh,3),201,220	; 17 - sit, again (for backwards eg after ice)

Global StinkerTexture=MyLoadTexture("data\models\stinker\body001a.jpg",1)
;EntityTexture GetChild(StinkerMesh,3),StinkerTexture
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
Global FireFlowerMesh=myLoadMD2("data\models\fireflower\fireflower.wdf")
;Global FireFlowerMesh=MyLoadMesh("data\models\fireflower\fireflower2.3ds",0)
;RotateMesh FireFlowerMesh,-90,0,0
;RotateMesh FireFlowerMesh,0,90,0

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
;BusterflyMesh=MyLoadMesh("data\models\busterfly\buster2.3ds",0)
BusterflyMesh=myLoadMD2("data\models\busterfly\buster.md2")
BusterflyTexture=MyLoadTexture("Data\models\busterfly\buster1.bmp",4)
EntityTexture BusterflyMesh,BusterflyTexture
;ScaleMesh busterflymesh,.01,.01,.01
;RotateMesh busterflymesh,-180,0,0
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
Dim KaboomTexture(5)
For i=1 To 5
		
		
		KaboomTexture(i)=myLoadTexture("data\models\kaboom\kaboom0"+Str$(i)+".jpg",1)
		
Next
EntityTexture KaboomMesh,KaboomTexture(1)
;TurnEntity KaboomMesh,0,90,0
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
RotateMesh ZBOTNPCMesh,0,90,0 ; formerly -90
ScaleMesh ZBotNPCMesh,1,1.5,1

Dim ZbotNPCTexture(8)
For i=0 To 7
	ZbotNPCTexture(i)=myLoadTexture("data\models\zbots\zbotnpc.jpg",1)
Next
;EntityTexture ZbotNPCMesh,ZBotNPCTexture(0)
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


Global Cylinder=MyLoadmesh("data\models\bridges\cylinder1.b3d",0)
HideEntity Cylinder


Global Fence1=MyLoadmesh("data\models\houses\fence.3ds",0)
HideEntity Fence1
Global Fence2=MyLoadmesh("data\models\houses\fenceb.b3d",0)
HideEntity Fence2
Global Fencepost=MyLoadmesh("data\models\houses\fence_post.3ds",0)
HideEntity Fencepost

Global GloveTex=MyLoadTexture("data\models\squares\glove.bmp",4)

Global FireTrapTexture=MyLoadTexture("data\models\squares\firetrap.bmp",4)

Global VoidTexture=myLoadTexture("Data\models\void\void.jpg",1)

Dim MirrorTexture(5)
For i=1 To 5
	MirrorTexture(i)=myLoadTexture("data\models\mirror\mirror"+Str$(i)+".jpg",1)
Next



Global hubmode
Global HubFileName$, HubTitle$, HubDescription$, HubTotalAdventures, HubAdvStart, HubSelectedAdventure
Const HubAdvMax=MaxCompilerFile ;500
Dim HubAdventuresFilenames$(HubAdvMax)
Dim HubAdventuresMissing(HubAdvMax)
Dim HubAdventuresIncludeInTotals(HubAdvMax)

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

Global MaxParticleWarningTimer=0 ; number of frames remaining before the "too many particles" warning message disappears



LoadSounds()

CalculateUIValues()
InitializeGraphicsCameras() ; needed for loading particles
InitializeGraphicsEntities()
InitializeGraphicsTextures()



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


StartupConfigs()

TweenPeriod=1000/60;85
TweenTime=MilliSecs()-TweenPeriod

ReadTestFile()

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
			
			OnRegainFocus()
			
		EndIf
	
Until False ;KeyDown(1) ; escape



EndApplication()

Function OnRegainFocus()

	;FlushKeys ; WHY DOES THIS NOT WORK??? Apparently it doesn't get rid of currently-pressed keys.
	
	;ForceKeyRelease(56,"left alt")
	;ForceKeyRelease(184,"right alt")
	
	ForceKeyRelease(42,"left shift")
	ForceKeyRelease(54,"right shift")
	
	ForceKeyRelease(29,"left ctrl")
	ForceKeyRelease(157,"right ctrl")
	
	; Apparently Blitz doesn't pick up on the Tab key when alt-tabbing. That's pretty nice.
	
	ReadConfigs()

End Function

Function CalculateUIValues()

	GfxAspectRatio#=Float#(GfxWidth)/Float#(GfxHeight)
	GfxZoomScaling#=OriginalRatio#/GfxAspectRatio#
	
	ToolbarBrushModeX=ToolbarPositionX(1)
	ToolbarBrushModeY=ToolbarPositionY(1)
	
	ToolbarBrushSizeX=ToolbarPositionX(2)
	ToolbarBrushSizeY=ToolbarPositionY(1)
	
	ToolbarTexPrefixX=ToolbarPositionX(2)
	ToolbarTexPrefixY=ToolbarPositionY(2)
	
	ToolbarDensityX=ToolbarPositionX(3)
	ToolbarDensityY=ToolbarPositionY(1)
	
	ToolbarElevateX=ToolbarPositionX(3)
	ToolbarElevateY=ToolbarPositionY(2)
	
	ToolbarBrushWrapX=ToolbarPositionX(4)
	ToolbarBrushWrapY=ToolbarPositionY(1)
	
	ToolbarStepPerX=ToolbarPositionX(4)
	ToolbarStepPerY=ToolbarPositionY(2)
	
	ToolbarShowMarkersX=ToolbarPositionX(5)
	ToolbarShowMarkersY=ToolbarPositionY(1)
	
	ToolbarShowObjectsX=ToolbarPositionX(5)
	ToolbarShowObjectsY=ToolbarPositionY(2)
	
	ToolbarShowLogicX=ToolbarPositionX(6)
	ToolbarShowLogicY=ToolbarPositionY(1)
	
	ToolbarShowLevelX=ToolbarPositionX(6)
	ToolbarShowLevelY=ToolbarPositionY(2)
	
	ToolbarIDFilterX=ToolbarPositionX(7)
	ToolbarIDFilterY=ToolbarPositionY(1)
	
	ToolbarSimulationLevelX=ToolbarPositionX(7)
	ToolbarSimulationLevelY=ToolbarPositionY(2)
	
	ToolbarExitX=ToolbarPositionX(8)
	ToolbarExitY=ToolbarPositionY(1)
	
	ToolbarSaveX=ToolbarPositionX(8)
	ToolbarSaveY=ToolbarPositionY(2)
	
	LetterWidth#=Float#(GfxWidth)/Float#(LettersCountX)*GfxZoomScaling#
	LetterHeight#=Float#(GfxHeight)/Float#(LettersCountY)
	
	LevelViewportWidth=GfxWidth-SidebarWidth
	LevelViewportHeight=GfxHeight-ToolbarHeight
	TilePickerZoomScaling#=Float#(LevelViewportHeight)/Float#(LevelViewportWidth) ; The numerator is 1 because the original 500x500 viewport is a 1:1 ratio.
	
	SidebarX=LevelViewportWidth
	SidebarY=0
	
	FlStartX=SidebarX+206 ; 706
	FlStartY=SidebarY+165
	
	LowerButtonsCutoff=LetterHeight*26
	
End Function

Function InitializeGraphicsTextures()

	ParticleTexture=myLoadTexture("data\graphics\particles.bmp",1)
	ResetParticles("data/graphics/particles.bmp")
	EntityTexture ParticlePreview,ParticleTexture
	
	TextTexture=myLoadTexture("Data/Graphics/font.bmp",4)
	ResetText("data/graphics/font.bmp")
	
	UpdateButtonGateTexture()
	LoadLevelTextureDefault()
	LoadWaterTextureDefault()

End Function

Function ResetGraphicsTextures()

	; Setting these handles to 0 since the pointees will be lost by a call to Graphics3D.
	TextTexture=0
	ParticleTexture=0
	TextMesh=0
	ParticleMesh=0
	ParticleMesh2=0
	
	InitializeGraphicsTextures()

End Function

Function InitializeGraphicsCameras()

	CameraPanning=False
	GameCamera=False
	
	Camera1 = CreateCamera() ; level camera
	Camera1PerspectiveZoom#=1.0*GfxZoomScaling#
	Camera1OrthographicZoom#=0.015*GfxZoomScaling#
	Camera1Zoom#=Camera1PerspectiveZoom#
	Camera4Zoom#=8.0
	
	TurnEntity Camera1,65,0,0
	PositionEntity Camera1,7,Camera1StartY,-14
	CameraViewport camera1,0,0,LevelViewportWidth,LevelViewportHeight
	CameraRange camera1,.1,1000 ;50
	
	Camera2 = CreateCamera() ; tile camera
	CameraClsColor camera2,255,0,0
	CameraViewport Camera2,SidebarX+10,SidebarY+20,200,220
	CameraRange camera2,.1,1000
	RotateEntity Camera2,45,25,0
	PositionEntity Camera2,4.9,109,-8
	CameraZoom Camera2,5
	
	Camera3 = CreateCamera() ; texture picker camera
	CameraClsColor camera3,0,0,0 ;255,0,0
	CameraViewport Camera3,0,0,LevelViewportWidth,LevelViewportHeight
	CameraRange camera3,.1,50 ;1000
	RotateEntity Camera3,90,0,0
	PositionEntity Camera3,0.5,210,-0.5
	CameraZoom Camera3,20.0*TilePickerZoomScaling#
	
	Camera4 = CreateCamera() ; objects menu camera
	CameraClsColor camera4,155,0,0
	CameraViewport Camera4,SidebarX+195,SidebarY+305,100,125
	CameraRange camera4,.1,1000
	RotateEntity Camera4,25,0,0
	PositionEntity Camera4,0,303.8,-8
	
	Camera = CreateCamera() ; Text Screen Camera
	CameraZoom Camera,GfxZoomScaling#
	
	UpdateCameraProj()
	UpdateCameraClsColor()

End Function

Function CreateBrushMesh()

	BrushMesh=CreateMesh()
	BrushSurface=CreateSurface(BrushMesh)
	EntityAlpha BrushMesh,BrushMeshAlpha
	
	BrushTextureMesh=CreateMesh()
	BrushTextureSurface=CreateSurface(BrushTextureMesh)
	EntityAlpha BrushTextureMesh,0.6 ;BrushMeshAlpha
	
	; This translation is needed to prevent z-fighting and to give Blitz3D a hint about the sorting order between the two transparent entities.
	TranslateEntity BrushTextureMesh,0,-0.005,0

End Function

Function GenerateCurrentGrabbedObjectMarkerEntity()

	CurrentGrabbedObjectMarkers(0)=CreateMesh()
	
	; Lots and lots of duplicated code here. Haha, whoops!
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	RotateMesh CurrentGrabbedObjectMarkers(0),90,0,0
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	RotateMesh CurrentGrabbedObjectMarkers(0),0,0,90
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole
	
	PositionMesh CurrentGrabbedObjectMarkers(0),0,0.5,0
	ScaleMesh CurrentGrabbedObjectMarkers(0),0.95,0.95,0.95
	
	EntityColor CurrentGrabbedObjectMarkers(0),100,255,100
	EntityFX CurrentGrabbedObjectMarkers(0),1 ; fullbright
	EntityOrder CurrentGrabbedObjectMarkers(0),-1 ; disable depth sorting
	HideEntity(CurrentGrabbedObjectMarkers(0))
	
	EntityAlpha CurrentGrabbedObjectMarkers(0),0.5
	
	For i=1 To MaxNofObjects-1
		CurrentGrabbedObjectMarkers(i)=CopyEntity(CurrentGrabbedObjectMarkers(0))
	Next

End Function

Function InitializeGraphicsEntities()

	Light=CreateLight()
	RotateEntity Light,80,15,0

	TexturePlane=CreateMesh()
	TexturePlaneSurface=CreateSurface(TexturePlane)
	AddVertex TexturePlaneSurface,0,200,0,0,0
	AddVertex TexturePlaneSurface,1,200,0,1,0
	AddVertex TexturePlaneSurface,0,200,-1,0,1
	AddVertex TexturePlaneSurface,1,200,-1,1,1
	AddTriangle TexturePlaneSurface,0,1,2
	AddTriangle TexturePlaneSurface,1,3,2
	UpdateNormals TexturePlane
	EntityPickMode TexturePlane,2
	
	For i=0 To 3
		; Pillar
		CursorMeshPillar(i)=CreateCube()
		ScaleMesh CursorMeshPillar(i),0.1,10,0.1
		EntityAlpha CursorMeshPillar(i),BrushMeshAlpha
		EntityColor CursorMeshPillar(i),255,255,200
		
		; Little square at the center of the brush
		CursorMeshOpaque(i)=CreateCube()
		ScaleMesh CursorMeshOpaque(i),.2,0.01,.2
	Next
	
	; The square region that the brush covers, used only by the texture picker
	CursorMeshTexturePicker=CreateCube()
	ScaleMesh CursorMeshTexturePicker,.5,0.1,.5
	EntityAlpha CursorMeshTexturePicker,BrushMeshAlpha
	EntityColor CursorMeshTexturePicker,255,255,200
	HideEntity CursorMeshTexturePicker
	
	CreateBrushMesh()
	
	CurrentObjectMarkerMesh=CreateCylinder()
	ScaleEntity CurrentObjectMarkerMesh,.01,3,.01
	PositionEntity CurrentObjectMarkerMesh,0,300,0
	
	LightPillar=CreateCube()
	ScaleMesh LightPillar,0.5,90,0.5
	EntityColor LightPillar,100,255,100
	EntityFX LightPillar,16 ; disable back-face culling
	HideEntity LightPillar
	
	GenerateCurrentGrabbedObjectMarkerEntity()	

	WorldAdjusterPositionMarker(0)=CopyEntity(LightPillar)
	EntityColor WorldAdjusterPositionMarker(0),100,200,255
	For i=1 To 3
		WorldAdjusterPositionMarker(i)=CopyEntity(WorldAdjusterPositionMarker(0))
	Next
	
	CurrentObjectMoveXYGoalMarker=CopyEntity(LightPillar)
	EntityColor CurrentObjectMoveXYGoalMarker,255,100,100
	
	WhereWeEndedUpMarker=CopyEntity(LightPillar)
	EntityColor WhereWeEndedUpMarker,255,255,0
	ShowEntity WhereWeEndedUpMarker
	
	MirrorEntityX=CreateCube()
	ScaleMesh MirrorEntityX,0.1,0.3,200.0
	EntityColor MirrorEntityX,GetBrushModeColor(BrushModeSetMirror,0),GetBrushModeColor(BrushModeSetMirror,1),GetBrushModeColor(BrushModeSetMirror,2)
	EntityAlpha MirrorEntityX,0.3
	HideEntity MirrorEntityX
	
	MirrorEntityY=CreateCube()
	ScaleMesh MirrorEntityY,200.0,0.3,0.1
	EntityColor MirrorEntityY,GetBrushModeColor(BrushModeSetMirror,0),GetBrushModeColor(BrushModeSetMirror,1),GetBrushModeColor(BrushModeSetMirror,2)
	EntityAlpha MirrorEntityY,0.3
	HideEntity MirrorEntityY
	
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
	
	CurrentWaterTile=CreateMesh()
	CurrentWaterTileSurface=CreateSurface(CurrentWaterTile)
	
	AddVertex (CurrentWaterTileSurface,1,99.5,3,CurrentTile\Water\Texture/4.0,0)
	AddVertex (CurrentWaterTileSurface,3,99.5,3,CurrentTile\Water\Texture/4.0+.25,0)
	AddVertex (CurrentWaterTileSurface,1,99.5,1,CurrentTile\Water\Texture/4.0,.25)
	AddVertex (CurrentWaterTileSurface,3,99.5,1,CurrentTile\Water\Texture/4.0+.25,.25)
	AddTriangle (CurrentWaterTileSurface,0,1,2)
	AddTriangle (CurrentWaterTileSurface,2,1,3)
	UpdateNormals CurrentWaterTile
	
	FreeEntity LightPillar
	
	ParticlePreview=CreateMesh()
	ParticlePreviewSurface=CreateSurface(ParticlePreview)
	ParticlePreviewSize#=10.0 ;0.04
	AddVertex ParticlePreviewSurface,-ParticlePreviewSize#,ParticlePreviewSize#,0
	AddVertex ParticlePreviewSurface,ParticlePreviewSize#,ParticlePreviewSize#,0
	AddVertex ParticlePreviewSurface,-ParticlePreviewSize#,-ParticlePreviewSize#,0
	AddVertex ParticlePreviewSurface,ParticlePreviewSize#,-ParticlePreviewSize#,0
	
	AddTriangle ParticlePreviewSurface,0,1,2
	AddTriangle ParticlePreviewSurface,1,3,2
	UpdateNormals ParticlePreview
	
	EntityOrder ParticlePreview,-10
	EntityFX ParticlePreview,1
	
	HideEntity ParticlePreview

End Function

Function ResetGraphicsEntities()
	
	InitializeGraphicsCameras()
	InitializeGraphicsEntities()
	ResetGraphicsTextures()
	
	; reload object entities
	For i=0 To NofObjects-1
		BuildLevelObjectModel(i)
	Next

End Function

Function ResetWindowSize()

	;EndGraphics
	Graphics3D 800,600,16,2
	SetBuffer BackBuffer()
	Graphics3D 800,600,16,3
	
	ResetGraphicsEntities()

End Function

Function ResolutionWasChanged()

	Return ; Exit for now until InitializeGraphicsEntities is complete, assuming that ever happens.
	
	CalculateUIValues()
	ResetGraphicsEntities()

End Function

Function ShowParticlePreview(x#,y#,tex)

	Return ; This crap doesn't work. Thank you Blitz3D for making it incredibly difficult to draw textured rectangle UI.

	u1#=Float(tex Mod 8)*0.125+nudge
	u2#=u1+0.125-2*nudge
	v1#=Float(Floor(tex/8))*0.125+nudge
	v2#=v1+0.125-2*nudge

	VertexTexCoords(ParticlePreviewSurface,0,u1#,v1#)
	VertexTexCoords(ParticlePreviewSurface,1,u2#,v2#)
	VertexTexCoords(ParticlePreviewSurface,2,u2#,v1#)
	VertexTexCoords(ParticlePreviewSurface,3,u1#,v2#)
	
	ShowEntity ParticlePreview
	
	If EditorMode=EditorModeDialog
		TargetEntity=camera
	Else
		TargetEntity=Camera1
	EndIf
	StartX#=EntityX#(TargetEntity)
	StartY#=EntityY#(TargetEntity)
	
	CameraZoomLevel#=Camera1Zoom#
	x#=x#/CameraZoomLevel
	y#=y#/CameraZoomLevel
	
	;PositionEntity ParticlePreview,x*GfxZoomScaling#,y*GfxZoomScaling#,1
	;PositionEntity ParticlePreview,x/GfxZoomScaling#,y/GfxZoomScaling#,1
	;PositionEntity ParticlePreview,x,y,1
	PositionEntity ParticlePreview,StartX#+x,StartY#+y,1

End Function

Function UpdateCameraProj()

	CameraProjMode Camera1,Camera1Proj
	CameraProjMode Camera2,Camera2Proj
	CameraProjMode Camera3,Camera3Proj
	CameraProjMode Camera4,Camera4Proj
	CameraProjMode Camera,CameraProj
	
	CameraZoom Camera1,Camera1Zoom#
	CameraZoom Camera4,Camera4Zoom#

End Function

Function UpdateCameraClsColor()

	CameraClsColor camera2,TileColorR,TileColorG,TileColorB
	CameraClsColor camera4,ObjectColorR,ObjectColorG,ObjectColorB

End Function

Function Camera1To3Proj()

	;Camera1Proj=0
	Camera3Proj=1
	UpdateCameraProj()
	
End Function

; Before this function was invented, the level editor camera was originally positioned to approximately be focused on the coordinates 7,10.
Function PositionCameraInLevel(FocusOnTileX,FocusOnTileY)

	PositionEntity Camera1,FocusOnTileX+0.5,EntityY(Camera1),-FocusOnTileY+0.5-4

End Function

Function UpdateButtonGateTexture()
	
	ButtonTexture=MyLoadTexture("data\graphics\buttons"+Str$(GateKeyVersion)+".bmp",4)
	GateTexture=MyLoadTexture("data\graphics\gates"+Str$(GateKeyVersion)+".bmp",1)
	
End Function

Function CurrentLevelTextureName$()

	If CurrentLevelTexture=-1
		Return LevelTextureCustomName$
	Else
		Return LevelTextureName$(CurrentLevelTexture)
	EndIf

End Function

Function CurrentWaterTextureName$()

	If CurrentWaterTexture=-1
		Return WaterTextureCustomName$
	Else
		Return WaterTextureName$(CurrentWaterTexture)
	EndIf
	
End Function

Function LoadLevelTextureDefault()

	LevelTexture=myLoadTexture("data\Leveltextures\"+CurrentLevelTextureName$(),1)

End Function

Function LoadWaterTextureDefault()

	WaterTexture=MyLoadTexture("data\Leveltextures\"+CurrentWaterTextureName$(),1)

End Function

Function UpdateLevelTexture()

	EntityTexture TexturePlane,LevelTexture
	For j=0 To LevelHeight-1
		EntityTexture LevelMesh(j),LevelTexture
	Next

	EntityTexture BrushTextureMesh,LevelTexture
	
	;ShowMessage("LevelTexture update!",1000)

End Function

Function UpdateLevelTextureDefault()

	LoadLevelTextureDefault()
	UpdateLevelTexture()

End Function

Function UpdateWaterTexture()
	
	EntityTexture Currentwatertile,WaterTexture
	For j=0 To LevelHeight-1
		EntityTexture WaterMesh(j),WaterTexture
	Next
	
End Function

Function UpdateWaterTextureDefault()
	
	LoadWaterTextureDefault()
	UpdateWaterTexture()
	
End Function


Function FinishDrawing()

	;Color 255,255,255
	;Text 0,0,"Mouse: "+MouseX()+", "+MouseY()
	
	DrawTooltip(CurrentTooltipStartX,CurrentTooltipStartY,CurrentTooltip$)
	CurrentTooltip$=""

	If displayfullscreen=True
		DrawImage mouseimg,MouseX(),MouseY()
	EndIf
	
	Flip

End Function



Function EndApplication()

	Color 0,0,0
	Rect 0,0,GfxWidth,GfxHeight
	
	Flip

	End

End Function



Function UpdateEditor()

	EditorGlobalControls()

	Select EditorMode
	
	Case 0,1,2,3
		EditorMainLoop()
	Case 4
		UserSelectScreen()
	Case 5,12
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
	Case 13
		SettingsMainLoop()
	End Select

End Function


Function StartEditorMainLoop()
	Cls
	SetEditorMode(EditorModeBeforeMasterEdit)
	WireFrame UsingWireFrame
	
	Camera1Proj=Camera1SavedProjMode
	Camera2Proj=1
	Camera3Proj=0
	Camera4Proj=1
	CameraProj=0
	UpdateCameraProj()
	
	ClearSurface Textsurface
	For p.letter = Each letter
		Delete p
	Next
	


End Function


Function EditorMainLoop()

	If displayfullscreen=True Cls

	If EditorMode=0 Or EditorMode=3
		CameraControls()
	EndIf
	
	RenderToolbar()
	If CameraPanning=False
		EditorLocalControls()
	EndIf
	EditorLocalRendering()
	
	leveltimer=leveltimer+1
	
	If KeyPressed(35) ; h key
		HighlightWopAdjusters=Not HighlightWopAdjusters
	EndIf
	
	MarkerAlpha#=0.3+0.03*Sin((Float(LevelTimer)*6.0) Mod 360)
	;For i=0 To MaxNofObjects-1
	;	EntityAlpha CurrentGrabbedObjectMarkers(i),MarkerAlpha#
	;Next
	For i=0 To 3
		EntityAlpha WorldAdjusterPositionMarker(i),MarkerAlpha#
	Next
	EntityAlpha CurrentObjectMoveXYGoalMarker,MarkerAlpha#
	
	WhereWeEndedUpAlpha#=WhereWeEndedUpAlpha#-0.002
	EntityAlpha WhereWeEndedUpMarker,WhereWeEndedUpAlpha#
	
	ControlLight()
	If SimulationLevel>=1
		ControlObjects()
	EndIf
	If SimulationLevel>=3
		ControlWeather()
	EndIf
	
	ResetSounds()
	If SimulationLevel>=SimulationLevelMusic And EditorMode<>8
		ControlSoundscapes()
		LoopMusic()
		PlayAllSounds()
	EndIf
	
	ControlParticles()
	RenderParticles()
	
	;ControlLetters()
	;RenderLetters()
	
	UpdateWorld
	RenderWorld
	
	
	
	Color TextLevelR,TextLevelG,TextLevelB
	
	Text 0,5,"ADVENTURE: "+AdventureFileName$
	If CurrentLevelNumber<10 And CurrentLevelNumber>=0
		Line1$="LEVEL: 0"+CurrentLevelNumber
	Else
		Line1$="LEVEL: "+CurrentLevelNumber
	EndIf
	RightAlignedText(LevelViewportWidth,5,Line1$)
	
	If EditorMode=0 Or EditorMode=3
		
		; it's a bit less than the viewport size because the text would otherwise overlap with the x/y coordinate listing on the bottom bar as well as the right margin
		ProjectedTextLimitX=LevelViewportWidth-10
		ProjectedTextLimitY=LevelViewportHeight-10
		
		For i=0 To NofObjects-1
;			If LevelObjects(i)\Attributes\LogicType=90 And LevelObjects(i)\Attributes\LogicSubType=15 ; General Command
;				Command$=LevelObjects(i)\Attributes\Data0
;				Pos.GameObjectPosition=LevelObjects(i)\Position
;				DisplayTextFacingUp(Command$,Pos\X,Pos\Y,Pos\Z+0.05,255,255,0)
;			EndIf
		
			MyEffectiveId=CalculateEffectiveId(LevelObjects(i)\Attributes)
			
			HitTargetID=False
			For j=0 To CurrentObjectTargetIDCount-1
				If CurrentObjectTargetIDEnabled(j) And MyEffectiveId=CurrentObjectTargetID(j)
					HitTargetID=True
				
					CameraProject(Camera1,LevelObjects(i)\Position\X,0.5,-LevelObjects(i)\Position\Y)
					x#=ProjectedX#()
					y#=ProjectedY#()
					If x#<ProjectedTextLimitX And y#<ProjectedTextLimitY
						StringOnObject$=MyEffectiveId
						x#=x#-4*Len(StringOnObject$)
	
						OutlinedText(x#,y#,StringOnObject$,255,255,0)
					EndIf
				EndIf
			Next
			
			If Not HitTargetID
				For j=0 To CurrentObjectActivateIDCount-1
					If CurrentObjectActivateIDEnabled(j) And MyEffectiveId=CurrentObjectActivateID(j) And CurrentObjectActivateID(j)>0
						HitTargetID=True
					
						CameraProject(Camera1,LevelObjects(i)\Position\X,0.5,-LevelObjects(i)\Position\Y)
						x#=ProjectedX#()
						y#=ProjectedY#()
						If x#<ProjectedTextLimitX And y#<ProjectedTextLimitY
							StringOnObject$=MyEffectiveId
							x#=x#-4*Len(StringOnObject$)
			
							OutlinedText(x#,y#,StringOnObject$,100,255,255)
						EndIf
					EndIf
				Next
			EndIf
			
			If (Not HitTargetID)
				If (i=CurrentObject\Attributes\Linked And ObjectAdjusterLinked\Absolute) Or (i=CurrentObject\Attributes\LinkBack And ObjectAdjusterLinkBack\Absolute)
					HitTargetID=True
					
					CameraProject(Camera1,LevelObjects(i)\Position\X,0.5,-LevelObjects(i)\Position\Y)
					x#=ProjectedX#()
					y#=ProjectedY#()
					
					StringOnObject$="#"+i
					HalfLength=4*Len(StringOnObject$)
					
					If x#<HalfLength
						x#=HalfLength
					EndIf
					If x#>LevelViewportWidth-HalfLength
						x#=LevelViewportWidth-HalfLength
					EndIf
					If y#<0
						y#=0
					EndIf
					If y#>ProjectedTextLimitY
						y#=ProjectedTextLimitY
					EndIf
					
					x#=x#-HalfLength
	
					OutlinedText(x#,y#,StringOnObject$,255,0,0)
				EndIf
			EndIf
			
			If (Not HitTargetID) And ShowObjectMesh>=2
				CameraProject(Camera1,LevelObjects(i)\Position\X,0.5,-LevelObjects(i)\Position\Y)
				x#=ProjectedX#()
				y#=ProjectedY#()
				If x#<ProjectedTextLimitX And y#<ProjectedTextLimitY
					If ShowObjectMesh=ShowObjectMeshIndices
						; display object indices
						StringOnObject$="#"+i
					ElseIf ShowObjectMesh=ShowObjectMeshIds
						; display object IDs
						StringOnObject$=CalculateEffectiveId(LevelObjects(i)\Attributes)
					ElseIf ShowObjectMesh=ShowObjectMeshCount
						; display object counts
						StringOnObject$="x"+LevelTileObjectCount(LevelObjects(i)\Position\TileX,LevelObjects(i)\Position\TileY)
					EndIf
					OutlinedText(x#-4*Len(StringOnObject$),y#,StringOnObject$,255,255,255)
				EndIf
			EndIf
		Next
		
	EndIf
	
	Color TextLevelR,TextLevelG,TextLevelB
		
	StartX=SidebarX+10
	StartY=20
	If CurrentTileTextureUse=False Text StartX+10,StartY+70,"Not Used"
	If CurrentTileSideTextureUse=False Text StartX+10,StartY+130,"Not Used"
	If CurrentWaterTileTextureUse=False Text StartX+120,StartY+100,"Not Used"
	
	Text StartX,StartY,"Xtrude: "+CurrentTile\Terrain\Extrusion
	If CurrentTileExtrusionUse=False Text StartX,StartY,"------------"
	If StepSizeTileExtrusion#<>0.0 DrawStepSize(StartX,StartY+60,StepSizeTileExtrusion#)
	Color TextLevelR,TextLevelG,TextLevelB
	
	Text StartX+48,StartY,"       Height: "+CurrentTile\Terrain\Height
	If CurrentTileHeightUse=False Text StartX+48,StartY,"       ------------"
	If StepSizeTileHeight#<>0.0 DrawStepSize(StartX+160,StartY+60,StepSizeTileHeight#)
	Color TextLevelR,TextLevelG,TextLevelB
	
	CurrentLogicName$=LogicIdToLogicName$(CurrentTile\Terrain\Logic)
	Text StartX+50,StartY+15,"Logic: "+CurrentLogicName$
	If CurrentTileLogicUse=False Text StartX+50,StartY+15,"  ---------"
	
	Text StartX+50,StartY+170," Random: "+CurrentTile\Terrain\Random
	If CurrentTileRandomUse=False Text StartX+50,StartY+170,"--------------"
	If StepSizeTileRandom#<>0.0 DrawStepSize(StartX+80,StartY+180,StepSizeTileRandom#)
	Color TextLevelR,TextLevelG,TextLevelB
	
	If CurrentTile\Terrain\Rounding=0
		Text StartX,StartY+185,"Corner:Squar"
	Else
		Text StartX,StartY+185,"Corner:Round"
	EndIf
	If CurrentTileRoundingUse=False Text StartX,StartY+185,"------------"
	
	If CurrentTile\Terrain\EdgeRandom=0
		Text StartX+100,StartY+185," Edge:Smooth"
	Else
		Text StartX+100,StartY+185," Edge:Jagged"
	EndIf
	If CurrentTileEdgeRandomUse=False Text StartX+100,StartY+185,"------------"

	Text StartX,StartY+200,"WHeight:"+CurrentTile\Water\Height
	If CurrentWaterTileHeightUse=False Text StartX,StartY+200,"------------"
	If StepSizeWaterTileHeight#<>0.0 DrawStepSize(StartX,StartY+190,StepSizeWaterTileHeight#)
	Color TextLevelR,TextLevelG,TextLevelB
	
	Text StartX+100,StartY+200," WTurb:"+CurrentTile\Water\Turbulence
	If CurrentWaterTileTurbulenceUse=False Text StartX+100,StartY+200,"------------"
	If StepSizeWaterTileTurbulence#<>0.0 DrawStepSize(StartX+170,StartY+180,StepSizeWaterTileTurbulence#)

	;Color RectToolbarR,RectToolbarG,RectToolbarB
	;Rect 0,520,800,80,True
	;Rect 0,500,800,100,True
	
	Color GetBrushModeColor(BrushMode,0),GetBrushModeColor(BrushMode,1),GetBrushModeColor(BrushMode,2)
	
	CenteredText(ToolbarBrushModeX,ToolbarBrushModeY,GetBrushModeName$(BrushMode))
	
	Color TextLevelR,TextLevelG,TextLevelB
	
	
	CenteredText(ToolbarBrushModeX,GfxHeight-50,"WIPE/FLIP")
	
	

	If DupeMode<>DupeModeNone
		Color 255,155,0
	EndIf
	
	CenteredText(ToolbarBrushModeX,GfxHeight-20,GetDupeModeName$(DupeMode))

	Color TextLevelR,TextLevelG,TextLevelB
	
	CenteredText(ToolbarBrushSizeX,ToolbarBrushSizeY,"BRUSH SIZE")
	CenteredText(ToolbarBrushSizeX,ToolbarBrushSizeY+15,BrushWidth+" x "+BrushHeight)
	
	If TexturePrefix<>""
		Color 255,155,0
	EndIf
	CenteredText(ToolbarTexPrefixX,ToolbarTexPrefixY,"TEX PREFIX")
	CenteredText(ToolbarTexPrefixX,ToolbarTexPrefixY+15,TexturePrefix$)
	;Text 90,565,"  TEX PREFIX"
	;Text 100,580,TexturePrefix$
	Color TextLevelR,TextLevelG,TextLevelB
	
	If ShowObjectPositions=True
		Line1$="SHOW"
	Else
		Line1$="HIDE"
	EndIf
	CenteredText(ToolbarShowMarkersX,ToolbarShowMarkersY,Line1$)
	CenteredText(ToolbarShowMarkersX,ToolbarShowMarkersY+15,"MARKERS")
	
	If ShowObjectMesh=0
		Color 255,155,0
		Line1$="HIDE"
	Else
		Line1$="SHOW"
	EndIf
	If ShowObjectMesh=ShowObjectMeshIndices
		Line2$="INDICES"
	ElseIf ShowObjectMesh=ShowObjectMeshIds
		Line2$="IDS"
	ElseIf ShowObjectMesh=ShowObjectMeshCount
		Line2$="COUNTS"
	Else
		Line2$="OBJECTS"
	EndIf
	CenteredText(ToolbarShowObjectsX,ToolbarShowObjectsY,Line1$)
	CenteredText(ToolbarShowObjectsX,ToolbarShowObjectsY+15,Line2$)
	
	Color TextLevelR,TextLevelG,TextLevelB

	If ShowLogicMesh=True
		Line1$="SHOW"
	Else
		Line1$="HIDE"
	EndIf
	CenteredText(ToolbarShowLogicX,ToolbarShowLogicY,Line1$)
	CenteredText(ToolbarShowLogicX,ToolbarShowLogicY+15,"LOGIC")
	
	If ShowLevelMesh=ShowLevelMeshShow
		Line1$="SHOW"
	ElseIf ShowLevelMesh=ShowLevelMeshHide
		Line1$="HIDE"
	ElseIf ShowLevelMesh=ShowLevelMeshTransparent
		Line1$="TRANSPARENT"
	EndIf
	CenteredText(ToolbarShowLevelX,ToolbarShowLevelY,Line1$)
	CenteredText(ToolbarShowLevelX,ToolbarShowLevelY+15,"LEVEL")
	
	CenteredText(ToolbarSimulationLevelX,ToolbarSimulationLevelY,"SIMULATION")
	CenteredText(ToolbarSimulationLevelX,ToolbarSimulationLevelY+15,"LEVEL "+SimulationLevel)
	
	CenteredText(ToolbarElevateX,ToolbarElevateY,"ELEVATE")
	
	Select BrushWrap
	Case BrushWrapRelative
		Line1$="RELATIVE"
	Case BrushWrapModulus
		Line1$="MODULUS"
	Case BrushWrapRandom
		Line1$="RANDOM"
	Case BrushWrapMirrorX
		Line1$="MIRROR X"
	Case BrushWrapMirrorY
		Line1$="MIRROR Y"
	Case BrushWrapMirrorXY
		Line1$="MIRROR XY"
	End Select
	CenteredText(ToolbarBrushWrapX,ToolbarBrushWrapY,"BRUSH WRAP")
	CenteredText(ToolbarBrushWrapX,ToolbarBrushWrapY+15,Line1$)
	
	If StepPer=StepPerPlacement
		Line1$="PLACEMENT"
	ElseIf StepPer=StepPerTile
		Line1$="TILE"
	ElseIf StepPer=StepPerClick
		Line1$="CLICK"
	EndIf
	CenteredText(ToolbarStepPerX,ToolbarStepPerY,"STEP PER")
	CenteredText(ToolbarStepPerX,ToolbarStepPerY+15,Line1$)
	
	If IDFilterEnabled
		Color 255,155,0
	EndIf
	CenteredText(ToolbarIDFilterX,ToolbarIDFilterY,"ID FILTER")
	If IDFilterEnabled
		Line1$="= "+IDFilterAllow
		If IDFilterInverted
			Line1$="!"+Line1$
		EndIf
	Else
		Line1$="OFF"
	EndIf
	CenteredText(ToolbarIDFilterX,ToolbarIDFilterY+15,Line1$)
	Color TextLevelR,TextLevelG,TextLevelB
	
	;Text 600,565,"  XTRUDE"
	;Text 600,580,"  LOGICS"
	
	If PlacementDensity#<1.0
		Color 255,155,0
	EndIf
	CenteredText(ToolbarDensityX,ToolbarDensityY,"DENSITY")
	CenteredText(ToolbarDensityX,ToolbarDensityY+15,PlacementDensity#)
	
	Line1$="   EXIT   "
	If IsMouseOverToolbarItem(ToolbarExitX,ToolbarExitY) ;MouseX()>700 And MouseY()>515 And MouseY()<555
		Color 255,255,0
		Line1$=">"+Line1$+"<"
	Else
		Color TextLevelR,TextLevelG,TextLevelB
	EndIf
	CenteredText(ToolbarExitX,ToolbarExitY,Line1$)
	
	UsingCarrots=False
	If UnsavedChanges<>0
		Line1$="SAVE LEVEL"
		Line2$=""
	Else
		If BrushMode=BrushModeTestLevel
			UsingCarrots=True
			Line1$="CLICK TILE"
			Line2$="TO TEST"
		Else
			Line1$="TEST LEVEL"
			Line2$="AT CURSOR"
		EndIf
	EndIf
	
	ShakeX=0
	ShakeY=0
	If IsMouseOverToolbarItem(ToolbarSaveX,ToolbarSaveY)
		Color 255,255,0
		UsingCarrots=True
	Else
		If UnsavedChanges<20
			Color TextLevelR,TextLevelG,TextLevelB
		ElseIf UnsavedChanges<40
			Color 200,200,0
		ElseIf UnsavedChanges<100
			Color 200,200,0
			ShakeX=Rand(-1,1)
			ShakeY=Rand(-1,1)
			;TheTimer=LevelTimer/8
			;Color GetAnimatedFlashing(TheTimer),GetAnimatedFlashing(TheTimer),0
		Else
			Color GetAnimatedFlashing(LevelTimer),60,60
			ShakeX=Rand(-3,3)
			ShakeY=Rand(-3,3)
		EndIf
	EndIf
	CenteredText(ToolbarSaveX+ShakeX,ToolbarSaveY+ShakeY,Line1$)
	CenteredText(ToolbarSaveX+ShakeX,ToolbarSaveY+ShakeY+15,Line2$)
	If UsingCarrots
		Line1$=">          <"
		CenteredText(ToolbarSaveX,ToolbarSaveY,Line1$)
		If Line2$<>""
			CenteredText(ToolbarSaveX,ToolbarSaveY+15,Line1$)
		EndIf
	EndIf

	Color TextLevelR,TextLevelG,TextLevelB
	
	UpdateWater()
	
	AnimateColors(CurrentObject)
	For i=0 To NofObjects-1
		AnimateColors(LevelObjects(i))
	Next
	
	;DrawTooltip(CurrentTooltipStartX,CurrentTooltipStartY,CurrentTooltip$)
	;CurrentTooltip$=""
	
	If NofParticles=MaxNofParticles
		MaxParticleWarningTimer=60
	EndIf
	
	If MaxParticleWarningTimer<>0
		MaxParticleWarningTimer=MaxParticleWarningTimer-1
		StartX=LevelViewportWidth/2
		StartY=LevelViewportHeight/2
		Message$="WARNING! Too many particles! This will most likely MAV in-game!"
		TextOffset=GetCenteredTextOffset(Message$)
		Color RectToolbarR,RectToolbarG,RectToolbarB
		Rect StartX-TextOffset,StartY-10,TextOffset*2,30,True
		Color TextWarningR,TextWarningG,TextWarningB
		Text StartX-TextOffset,StartY,Message$
	EndIf
	
	FinishDrawing()

End Function


Function AnimateColors(Obj.GameObject)

	If Obj\Attributes\LogicType=90 And Obj\Attributes\LogicSubType=15 And Obj\Attributes\Data0=5 ; General Command 5
		EntityColor Obj\Model\Entity,GetAnimatedFlashing(LevelTimer),60,60
	ElseIf Obj\Attributes\LogicType=200 And Obj\Attributes\Data0=8
		; Animate Rainbow Magic
		For i=0 To 3
		    red=GetAnimatedRainbowRed()
		    green=GetAnimatedRainbowGreen()
		    blue=GetAnimatedRainbowBlue()
			
		    VertexColor GetSurface(Obj\Model\Entity,1),i,red,green,blue
		Next
	EndIf

End Function


Function SetEditorMode(NewMode)

	If NewMode=8
		; prevent garbage input from level editor movement from appearing in adventure description text
		FlushKeys
		
		WireFrame False
		
		If EditorMode=EditorModeTile Or EditorMode=EditorModeObject
			EditorModeBeforeMasterEdit=EditorMode
		EndIf
	EndIf
	
	If NewMode=0 Or NewMode=3 ; If EditorMode=1 Or EditorMode=2
		Camera1Proj=Camera1SavedProjMode
		Camera3Proj=0
		UpdateCameraProj()
	EndIf
	
	OldEditorMode=EditorMode
	EditorMode=NewMode
	
	If OldEditorMode=3 And NewMode=0
		SetBrushToCurrentTile()
	ElseIf OldEditorMode=0 And NewMode=3
		SetBrushToCurrentObject()
	EndIf
	
	UpdateAllSelectedObjectMarkersVisibility()
	
	; Edge case: the mouse will be hidden when typing in the editor's real-time textboxes. This line accounts for that.
	ShowPointer()

End Function


Function SetBrushWidth(NewBrushWidth)

	BrushWidth=NewBrushWidth
	
	If BrushWidth<1
		BrushWidth=1
	ElseIf BrushWidth>100
		BrushWidth=100
	EndIf
	
	BrushCursorStateWasChanged()

End Function

Function SetBrushHeight(NewBrushHeight)

	BrushHeight=NewBrushHeight
	
	If BrushHeight<1
		BrushHeight=1
	ElseIf BrushHeight>100
		BrushHeight=100
	EndIf
	
	BrushCursorStateWasChanged()

End Function


Function PassesPlacementDensityTest()

	If Rnd(0.0,1.0)<=PlacementDensity#
		Return True
	Else
		Return False
	EndIf

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


Function ReadControls()

	file=ReadFile(EditorControls$)
	KeyMoveNorth=ReadInt(file)
	KeyMoveWest=ReadInt(file)
	KeyMoveSouth=ReadInt(file)
	KeyMoveEast=ReadInt(file)
	CloseFile file

End Function

Function WriteControls()

	file=WriteFile(EditorControls$)
	WriteInt(file,KeyMoveNorth)
	WriteInt(file,KeyMoveWest)
	WriteInt(file,KeyMoveSouth)
	WriteInt(file,KeyMoveEast)
	CloseFile file

End Function


Function StartupControls()

	If FileExists(EditorControls$)
		ReadControls()
	Else
		WriteControls()
	EndIf

End Function


Function StartupConfigs()

	StartupColors()
	StartupControls()

End Function


Function ReadConfigs()

	ReadColors()
	ReadControls()

End Function


Function GetKeyFromUser()
	
	While True
		For i=0 To 237
			If KeyDown(i)
				Return i
			EndIf
		Next
	Wend
	
End Function


Function AnyKeyDown()

	For i=0 To 237
		If KeyDown(i)
			Return True
		EndIf
	Next
	Return False

End Function


Function ConfigureKeyboardKey(KeyName$)

	PrintMessageForInstant("Press the key you want to use to "+KeyName$+".")
	Result=GetKeyFromUser()
	
	Repeat
	Until Not KeyDown(Result)
	
	Return Result

End Function


Function ConfigureControls()

	If Not GetConfirmation("Do you want to configure your keyboard mappings?")
		Return
	EndIf
	
	Repeat
	Until Not AnyKeyDown()
	
	FlushKeys

	KeyMoveNorth=ConfigureKeyboardKey("MOVE NORTH")
	KeyMoveWest=ConfigureKeyboardKey("MOVE WEST")
	KeyMoveSouth=ConfigureKeyboardKey("MOVE SOUTH")
	KeyMoveEast=ConfigureKeyboardKey("MOVE EAST")
	
	WriteControls()
	
	ShowMessage("Controls configured!",1000)

End Function


Function GetCenteredTextOffset(Message$)

	Return 4*Len(Message$)

End Function

Function GetTextPixelLength(Message$)

	Return 8*Len(Message$)

End Function


Function CenteredText(StartX,StartY,Message$)

	Text StartX-GetCenteredTextOffset(Message$),StartY,Message$

End Function

Function RightAlignedText(StartX,StartY,Message$)

	Text StartX-GetTextPixelLength(Message$),StartY,Message$

End Function

Function OutlinedText(x#,y#,Message$,InsideColorR,InsideColorG,InsideColorB)

	Color 0,0,0
	Text x#+1,y#+1,Message$
	Text x#+1,y#-1,Message$
	Text x#-1,y#+1,Message$
	Text x#-1,y#-1,Message$
	Color InsideColorR,InsideColorG,InsideColorB
	Text x#,y#,Message$

End Function

Function DrawTextRectangle(StartX,StartY,Message$,RectR,RectG,RectB,TextR,TextG,TextB)

	If Message$="" Then Return
	
	TextPixelLength=GetTextPixelLength(Message$)
	HorizontalPadding=10
	TotalHorizontalPadding=HorizontalPadding*2
	RectangleWidth=TextPixelLength+TotalHorizontalPadding
	StartX=StartX-TotalHorizontalPadding
	
	; clamp the rectangle position so that the rectangle doesn't spill outside the window
	If StartX+RectangleWidth>GfxWidth
		StartX=GfxWidth-RectangleWidth
	ElseIf StartX<0
		StartX=0
	EndIf

	Color RectR,RectG,RectB
	Rect StartX,StartY-40,RectangleWidth,30,True
	Color TextR,TextG,TextB
	Text StartX+HorizontalPadding,StartY-30,Message$


End Function

Function DrawTooltip(StartX,StartY,Message$)

	DrawTextRectangle(StartX,StartY,Message$,RectToolbarR,RectToolbarG,RectToolbarB,TextLevelR,TextLevelG,TextLevelB)

End Function

Global CurrentTooltipStartX
Global CurrentTooltipStartY
Global CurrentTooltip$

Function ShowTooltipLeftAligned(StartX,StartY,Message$)

	CurrentTooltipStartX=StartX
	CurrentTooltipStartY=StartY
	CurrentTooltip$=Message$
	
End Function

Function ShowTooltipRightAligned(StartX,StartY,Message$)

	CurrentTooltipStartX=StartX-GetTextPixelLength(Message$)
	CurrentTooltipStartY=StartY
	CurrentTooltip$=Message$
	
End Function

Function ShowTooltipCenterAligned(StartX,StartY,Message$)

	CurrentTooltipStartX=StartX-GetCenteredTextOffset(Message$)
	CurrentTooltipStartY=StartY
	CurrentTooltip$=Message$
	
End Function


Function SetDupeMode(NewDupeMode)

	DupeMode=NewDupeMode
	
	If DupeMode<0
		DupeMode=DupeModeMax
	ElseIf DupeMode>DupeModeMax
		DupeMode=0
	EndIf
	
	Select DupeMode
	Case DupeModeX
		SetBrushMode(BrushModeSetMirror)
		ShowEntity(MirrorEntityX)
		HideEntity(MirrorEntityY)
	Case DupeModeY
		SetBrushMode(BrushModeSetMirror)
		HideEntity(MirrorEntityX)
		ShowEntity(MirrorEntityY)
	Case DupeModeXPlusY
		SetBrushMode(BrushModeSetMirror)
		ShowEntity(MirrorEntityX)
		ShowEntity(MirrorEntityY)
	Default
		If BrushMode=BrushModeSetMirror
			SetBrushMode(BrushModeNormal)
		EndIf
		HideEntity(MirrorEntityX)
		HideEntity(MirrorEntityY)
	End Select

End Function


Function SetBrushMode(NewBrushMode)

	FloodedElementsClear()
	BrushMode=NewBrushMode
	BlockPlacingMode=BlockPlacingModeNone
	BrushCursorStateWasChanged()
	
End Function


Function SetBrushCursorPosition(x,y)

	PositionChanged=Not PositionIsEqual(x,y,BrushCursorX,BrushCursorY)
	
	BrushCursorX=x
	BrushCursorY=y
	
	;HideEntity BlockModeMesh
	If BrushCursorX<>BrushCursorInvalid And BrushCursorY<>BrushCursorInvalid And BrushMode=BrushModeBlockPlacing
		; show the block
		;ShowEntity BlockModeMesh
		;EntityColor BlockModeMesh,BrushR,BrushG,BrushB
		If BrushCursorX>BlockCornerx
			cornleft=Blockcornerx
			cornright=BrushCursorX
		Else
			cornleft=BrushCursorX
			cornright=Blockcornerx
		EndIf
		If BrushCursorY>BlockCornery
			cornup=BlockCornery
			corndown=BrushCursorY
		Else
			cornup=BrushCursorY
			corndown=blockcornery
		EndIf
		;VertexCoords BlockModeSurface,0,cornleft-0,0.1,-(cornup)
		;VertexCoords BlockModeSurface,1,cornright+1,0.1,-(cornup)
		;VertexCoords BlockModeSurface,2,cornleft-0,0.1,-(corndown+1)
		;VertexCoords BlockModeSurface,3,cornright+1,0.1,-(corndown+1)
	EndIf
	
	If PositionChanged
		BrushCursorPositionWasChanged()
	EndIf

End Function

Function BrushCursorPositionWasChanged()
	
	; object dragging
	If NofDraggedObjects<>0
		DragChange=True
	
		OldX=DragSpotX
		DragSpotDeltaX=BrushCursorX-DragSpotX
		If DragSpotDeltaX>0
			If DragMaxTileX+DragSpotDeltaX<=100
				DragSpotX=DragSpotX+DragSpotDeltaX
			Else
				DragSpotX=100-(DragMaxTileX-DragSpotX)
			EndIf
		Else
			If DragMinTileX+DragSpotDeltaX>=0
				DragSpotX=DragSpotX+DragSpotDeltaX
			Else
				DragSpotX=DragSpotX-DragMinTileX
			EndIf
		EndIf
		DragSpotDeltaX=DragSpotX-OldX
		DragMinTileX=DragMinTileX+DragSpotDeltaX
		DragMaxTileX=DragMaxTileX+DragSpotDeltaX
		
		OldY=DragSpotY
		DragSpotDeltaY=BrushCursorY-DragSpotY
		If DragSpotDeltaY>0
			If DragMaxTileY+DragSpotDeltaY<=100
				DragSpotY=DragSpotY+DragSpotDeltaY
			Else
				DragSpotY=100-(DragMaxTileY-DragSpotY)
			EndIf
		Else
			If DragMinTileY+DragSpotDeltaY>=0
				DragSpotY=DragSpotY+DragSpotDeltaY
			Else
				DragSpotY=DragSpotY-DragMinTileY
			EndIf
		EndIf
		DragSpotDeltaY=DragSpotY-OldY
		DragMinTileY=DragMinTileY+DragSpotDeltaY
		DragMaxTileY=DragMaxTileY+DragSpotDeltaY
	
		For i=0 To NofDraggedObjects-1
			CurrentDraggedObject=DraggedObjects(i)
			DraggedPosition.GameObjectPosition=LevelObjects(CurrentDraggedObject)\Position
			TileX=DraggedPosition\TileX
			TileY=DraggedPosition\TileY
			DecrementLevelTileObjectCount(TileX,TileY)
			SetObjectPosition(CurrentDraggedObject,TileX+DragSpotDeltaX,TileY+DragSpotDeltaY)
			UpdateObjectPosition(CurrentDraggedObject)
			SomeObjectWasChanged()
		Next
	EndIf
	
	; tile dragging
	If TileDragging=True
		DragChange=True
		
		PasteLevelFromCopy()
		
		DeltaX=BrushCursorX-DraggedTilesSpotX
		DeltaY=BrushCursorY-DraggedTilesSpotY
		
		For i=0 To LevelWidth-1
			For j=0 To LevelHeight-1
				TargetX=i+DeltaX
				TargetY=j+DeltaY
				If IsPositionInLevel(TargetX,TargetY)
					If DraggedTilesEnabled(i,j)
						CopyTile(DraggedTiles(i,j),LevelTiles(TargetX,TargetY))
						
						;ShowMessage(i+","+j+" to "+TargetX+","+TargetY+" with texture "+DraggedTiles(i,j)\Terrain\Texture,1000)
					EndIf
				EndIf
			Next
		Next
		
		For i=0 To LevelWidth-1
			For j=0 To LevelHeight-1
				UpdateTile(i,j)
			Next
		Next
		
		SomeTileWasChanged()
	EndIf
	
	If BrushMode=BrushModeSetMirror
		MirrorPositionX=BrushCursorX
		MirrorPositionY=BrushCursorY
		
		PositionEntityInLevel(MirrorEntityX,BrushCursorX+0.5,0.5)
		PositionEntityInLevel(MirrorEntityY,0.5,BrushCursorY+0.5)
	ElseIf MirrorPositionX=BrushCursorInvalid And MirrorPositionY=BrushCursorInvalid
		HideEntity MirrorEntityX
		HideEntity MirrorEntityY
	EndIf
	
	OnceTilePlacement=True
	BrushCursorStateWasChanged()

End Function

Function BrushCursorOffMap()
	
	HideCursors()
	HideBrushSurface()
	SetBrushCursorPosition(BrushCursorInvalid,BrushCursorInvalid)
	
End Function

Function BrushCursorProbablyModifiedTiles()

	ClearBrushSurface()
	
	FloodedElementsClear()
	
	BrushCursorStateWasChanged()
	
	For j=0 To MaxLevelCoordinate
		If DirtyNormals(j)
			DirtyNormals(j)=False
			RecalculateNormals(j)
		EndIf
	Next

End Function

Function BrushCursorStateWasChanged()

	;ShowMessage("Brush cursor state changed",1000)
	
	CalculateBrushTargets()

End Function

Function CalculateBrushTargets()

	If BrushMode=BrushModeNormal
		FloodedElementsClear()
		BrushXStart=GetBrushXStart()
		BrushYStart=GetBrushYStart()
		For i=0 To BrushWidth-1
			For j=0 To BrushHeight-1
				AddToFloodedStack(BrushXStart+i,BrushYStart+j)
			Next
		Next
		GenerateBrushSurface()
	ElseIf BrushMode=BrushModeBlockPlacing
		FloodedElementsClear()
		For i=cornleft To cornright
			For j=cornup To corndown
				If FloodedElementCount<>MaxTilesPerLevel
					AddToFloodedStack(i,j)
				EndIf
			Next
		Next

		GenerateBrushSurface()
	ElseIf BrushMode=BrushModeFill Or IsBrushInInlineMode() Or IsBrushInOutlineMode() Or BrushMode=BrushModeRow Or BrushMode=BrushModeColumn
		; Don't redo the flood fill if it is unnecessary.
		If Not FloodedStackHasTile(BrushCursorX,BrushCursorY)
			If BrushMode=BrushModeFill
				FloodFill(BrushCursorX,BrushCursorY)
			ElseIf IsBrushInInlineMode()
				FloodFillInline(BrushCursorX,BrushCursorY,BrushMode=BrushModeInlineHard)
			ElseIf IsBrushInOutlineMode()
				FloodFillOutline(BrushCursorX,BrushCursorY,BrushMode=BrushModeOutlineHard)
			ElseIf BrushMode=BrushModeRow
				FloodFillRow(BrushCursorX,BrushCursorY)
			ElseIf BrushMode=BrushModeColumn
				FloodFillColumn(BrushCursorX,BrushCursorY)
			EndIf
			GenerateBrushSurface()
		EndIf
	Else
		FloodedElementsClear()
		GenerateBrushSurface()
	EndIf
	
	GenerateBrushPreview()

End Function

Function GenerateBrushSurface()

	ClearBrushSurface()
	
	If BrushCursorX<>BrushCursorInvalid And BrushCursorY<>BrushCursorInvalid		
		If FloodedElementCount=0
			AddToFloodedStack(BrushCursorX,BrushCursorY)
		EndIf
	EndIf
	
	BrushSpaceX=LevelSpaceToBrushSpaceX(x,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(y,BrushWrap)
	
	For i=0 To FloodedElementCount-1
		thisx=FloodedStackX(i)
		thisy=FloodedStackY(i)
		AddTileToBrushSurface(BrushSurface,thisx,thisy,BrushSpaceX,BrushSpaceY,False)
	Next
	
	FinishBrushSurface()

End Function

Function GenerateBrushPreview()

	ClearBrushPreviewSurface()

	For i=0 To FloodedElementCount-1
		thisx=FloodedStackX(i)
		thisy=FloodedStackY(i)
		AddTileToBrushPreview(thisx,thisy)
	Next

End Function


; Wow! What a great Object-Oriented Programming Constructor I have just written!
Function FloodFillInitializeState(StartX,StartY)

	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			LevelTileVisited(i,j)=False
		Next
	Next
	
	FloodedElementsClear()
	
	If IsPositionInLevel(StartX,StartY)
		SetLevelTileAsTarget(StartX,StartY)
		FloodStackX(0)=StartX
		FloodStackY(0)=StartY
		LevelTileVisited(StartX,StartY)=True
		FloodElementCount=1
	Else
		FloodElementCount=0
	EndIf

End Function


Function FloodedElementsClear()

	FloodedElementCount=0

End Function


Function AddToFloodedStack(NewX,NewY)

	FloodedStackX(FloodedElementCount)=NewX
	FloodedStackY(FloodedElementCount)=NewY
	FloodedElementCount=FloodedElementCount+1

End Function


Function FloodFillVisitLevelTile(nextx,nexty)

	If LevelTileMatchesTarget(nextx,nexty)
		If LevelTileVisited(nextx,nexty)=False
			LevelTileVisited(nextx,nexty)=True
			FloodFillPlanToVisitLevelTile(nextx,nexty)
		EndIf
	Else
		FloodOutsideAdjacent=True
	EndIf

End Function


Function FloodFillVisitLevelTileOutline(nextx,nexty)

	If LevelTileVisited(nextx,nexty)=False
		LevelTileVisited(nextx,nexty)=True
		If LevelTileMatchesTarget(nextx,nexty)
			FloodFillPlanToVisitLevelTile(nextx,nexty)
		Else
			AddToFloodedStack(nextx,nexty)
		EndIf
	EndIf

End Function


Function FloodFillPlanToVisitLevelTile(nextx,nexty)

	FloodStackX(FloodElementCount)=nextx
	FloodStackY(FloodElementCount)=nexty
	FloodElementCount=FloodElementCount+1

End Function


Function FloodedStackHasTile(x,y)

	For i=0 To FloodedElementCount-1
		thisx=FloodedStackX(i)
		thisy=FloodedStackY(i)
		If PositionIsEqual(x,y,thisx,thisy)
			Return True
		EndIf
	Next
	Return False

End Function

Function FloodFill(StartX,StartY)

	FloodFillInitializeState(BrushCursorX,BrushCursorY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)
		
		FloodFillVisitLevelTile(thisx-1,thisy)
		FloodFillVisitLevelTile(thisx+1,thisy)
		FloodFillVisitLevelTile(thisx,thisy-1)
		FloodFillVisitLevelTile(thisx,thisy+1)
		
		AddToFloodedStack(thisx,thisy)
	Wend

End Function

Function FloodFillRow(StartX,StartY)

	FloodFillInitializeState(BrushCursorX,BrushCursorY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)
		
		FloodFillVisitLevelTile(thisx-1,thisy)
		FloodFillVisitLevelTile(thisx+1,thisy)
		
		AddToFloodedStack(thisx,thisy)
	Wend

End Function

Function FloodFillColumn(StartX,StartY)

	FloodFillInitializeState(BrushCursorX,BrushCursorY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)
		
		FloodFillVisitLevelTile(thisx,thisy-1)
		FloodFillVisitLevelTile(thisx,thisy+1)
		
		AddToFloodedStack(thisx,thisy)
	Wend

End Function

Function FloodFillInline(StartX,StartY,IsHard)

	FloodFillInitializeState(StartX,StartY)
	While FloodElementCount<>0
		FloodOutsideAdjacent=False
		
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)

		FloodFillVisitLevelTile(thisx-1,thisy)
		FloodFillVisitLevelTile(thisx+1,thisy)
		FloodFillVisitLevelTile(thisx,thisy-1)
		FloodFillVisitLevelTile(thisx,thisy+1)
		
		If IsHard
			If (Not LevelTileMatchesTarget(thisx-1,thisy-1)) Or (Not LevelTileMatchesTarget(thisx+1,thisy-1)) Or (Not LevelTileMatchesTarget(thisx-1,thisy+1)) Or (Not LevelTileMatchesTarget(thisx+1,thisy+1))
				FloodOutsideAdjacent=True
			EndIf
		EndIf
		
		If FloodOutsideAdjacent
			AddToFloodedStack(thisx,thisy)
		EndIf
	Wend

End Function

Function FloodFillOutline(StartX,StartY,IsHard)

	FloodFillInitializeState(StartX,StartY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)
		
		FloodFillVisitLevelTileOutline(thisx-1,thisy)
		FloodFillVisitLevelTileOutline(thisx+1,thisy)
		FloodFillVisitLevelTileOutline(thisx,thisy-1)
		FloodFillVisitLevelTileOutline(thisx,thisy+1)
		
		If IsHard
			FloodFillVisitLevelTileOutline(thisx-1,thisy-1)
			FloodFillVisitLevelTileOutline(thisx+1,thisy-1)
			FloodFillVisitLevelTileOutline(thisx-1,thisy+1)
			FloodFillVisitLevelTileOutline(thisx+1,thisy+1)
		EndIf
	Wend

End Function


Function KeyPressed(i)

	If KeyDown(i) And KeyReleased(i)
		KeyReleased(i)=False
		Return True
	Else
		Return False
	EndIf

End Function


Function TrueToYes$(Bool)

	If Bool=True
		Return "Yes"
	Else
		Return "No"
	EndIf

End Function

Function OneToYes$(Value)

	If Value=1
		Return "Yes"
	Else
		Return "No"
	EndIf

End Function

Function ZeroToYes$(Value)

	If Value=0
		Return "Yes"
	Else
		Return "No"
	EndIf

End Function

Function MaybePluralize$(TargetString$,Count)

	If Count=1
		Return TargetString$
	Else
		If TargetString$="has"
			Return "have"
		Else
			Return TargetString$+"s"
		EndIf
	EndIf

End Function


Function ReturnPressed()

	If ReturnKey=True And ReturnKeyReleased=True
		ReturnKeyReleased=False
		Return True
	EndIf
	Return False

End Function

Function MouseDebounceFinished()

	Return MouseDebounceTimer=0

End Function

Function MouseDebounceSet(Timer)

	MouseDebounceTimer=Timer

End Function


Function EditorGlobalControls()

	ShowingError=False
	
	If MouseDebounceTimer>0
		MouseDebounceTimer=MouseDebounceTimer-1
	EndIf

	If MouseDown(1)=True
		LeftMouse=True
	Else
		If LeftMouse=True
			LeftMouse=False
			MouseDebounceTimer=0
		EndIf
		
		LeftMouseReleased=True
	EndIf

	If MouseDown(2)=True
		RightMouse=True
	Else
		If RightMouse=True
			RightMouse=False
			MouseDebounceTimer=0
		EndIf
			
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
	
	; Disabled indefinitely due to potential instability.
	; Getting this to work would take a huge amount of effort.
	;If KeyPressed(66) And displayfullscreen=False ; F8 key
	;	ResetWindowSize()
	;EndIf

End Function


Function RenderToolbar()

	Color RectToolbarR,RectToolbarG,RectToolbarB
	;Rect 0,500,500,12,True
	Rect 0,LevelViewportHeight,GfxWidth,100,True

End Function


Function DrawStepSize(x,y,StepSize#)

	DrawTextRectangle(x,y,StepSize#, 255,100,0, 0,0,0)

End Function


Function RunStepSize()
	
	For i=0 To BrushSpaceWidth-1
		For j=0 To BrushSpaceHeight-1
			RunStepSizeForTile(BrushTiles(i,j))
		Next
	Next
	
End Function

Function RunStepSizeForTile(TheTile.Tile)

	TheTile\Terrain\Random#=TheTile\Terrain\Random#+StepSizeTileRandom#
	TheTile\Terrain\Height#=TheTile\Terrain\Height#+StepSizeTileHeight#
	TheTile\Terrain\Extrusion#=TheTile\Terrain\Extrusion#+StepSizeTileExtrusion#
	TheTile\Water\Height#=TheTile\Water\Height#+StepSizeWaterTileHeight#
	TheTile\Water\Turbulence#=TheTile\Water\Turbulence#+StepSizeWaterTileTurbulence#

End Function


Function SetUseStateOfAllTileAttributes(NewState)

	CurrentTileTextureUse=NewState
	CurrentTileSideTextureUse=NewState
	CurrentTileHeightUse=NewState
	CurrentTileExtrusionUse=NewState
	CurrentTileRandomUse=NewState
	CurrentTileRoundingUse=NewState
	CurrentTileEdgeRandomUse=NewState
	CurrentTileLogicUse=NewState
	CurrentWaterTileTextureUse=NewState
	CurrentWaterTileHeightUse=NewState
	CurrentWaterTileTurbulenceUse=NewState

End Function


Function MaybeUnuseAllTileAttributes(MyState)

	If ShiftDown()
		If MyState=False
			SetUseStateOfAllTileAttributes(True)
			Return False
		Else
			SetUseStateOfAllTileAttributes(False)
			Return True
		EndIf
	EndIf
	Return True

End Function


Function GetNumberOfCursorsInDupeMode(Value)

	Select Value
	Case DupeModeNone
		Return 1
	Case DupeModeX,DupeModeY
		Return 2
	Case DupeModeXPlusY
		Return 4
	End Select
	
End Function


Function PositionCursorEntity(i,x,y)

	ShowEntity CursorMeshPillar(i)
	ShowEntity CursorMeshOpaque(i)
	PositionEntity CursorMeshPillar(i),x+.5,GetLevelTileTotalHeight(x,y),-y-.5
	PositionEntity CursorMeshOpaque(i),x+.5,0,-y-.5

End Function

Function GetTileTotalHeight#(TheTile.Tile)

	Return TheTile\Terrain\Extrusion+TheTile\Terrain\Height ;+TheTile\Terrain\Random

End Function

Function GetLevelTileTotalHeight#(x,y)

	If IsPositionInLevel(x,y)
		Return GetTileTotalHeight(LevelTiles(x,y))
	Else
		Return 0
	EndIf

End Function


Function HideCursors()

	ClearBrushSurface()

	For i=0 To 3
		HideEntity CursorMeshPillar(i)
		HideEntity CursorMeshOpaque(i)
	Next

End Function


Function SetWhereWeEndedUpMarker(x,y)

	;ShowMessage("Marker ended up at "+x+","+y,1000)
	SetEntityPositionInWorld(WhereWeEndedUpMarker,Float(x)+0.5,Float(y)+0.5,0.0)
	WhereWeEndedUpAlpha#=0.5

End Function


Function EndUpAt(level,x,y)

	AccessLevelAt(level,x,y)
	SetWhereWeEndedUpMarker(x,y)

End Function

; Returns True if the object is at x,y.
Function TryLevelGoto(i,x,y,D1,D2,D3)

	If LevelObjects(i)\Position\TileX=x And LevelObjects(i)\Position\TileY=y
		Attributes.GameObjectAttributes=LevelObjects(i)\Attributes
		ToLevel=GetDataByIndex(Attributes,D1)
		If ToLevel=CurrentLevelNumber
			PositionCameraInLevel(GetDataByIndex(Attributes,D2),GetDataByIndex(Attributes,D3))
			SetWhereWeEndedUpMarker(GetDataByIndex(Attributes,D2),GetDataByIndex(Attributes,D3))
		ElseIf AskToSaveLevelAndExit()
			; Destination level might have changed from possible object update event, so we read from Data1 again.
			EndUpAt(GetDataByIndex(Attributes,D1),GetDataByIndex(Attributes,D2),GetDataByIndex(Attributes,D3))
		EndIf
		
		Return True
	Else
		Return False
	EndIf

End Function


Function EditorLocalRendering()

	; full window size is 800x600, whereas the level camera viewport is 500x500
	; draw black regions so stray text doesn't linger there
	Color RectMarginR,RectMarginG,RectMarginB
	Rect SidebarX,SidebarY,10,LevelViewportHeight,True ; between level camera and object/tile editors
	Rect SidebarX+10,SidebarY,290,20,True ; backdrop for the labels of TILES and GLOBALS
	Rect SidebarX+210,SidebarY+20,5,220,True ; between TILES and GLOBALS
	Rect SidebarX+215,SidebarY+85,80,15 ; between level dimensions and the rest of GLOBALS
	Rect SidebarX+295,SidebarY+20,5,LevelViewportHeight,True ; to the right of GLOBALS
	Rect SidebarX+10,SidebarY+240,205,5,True ; beneath TILES and to the left of GLOBALS
	Rect SidebarX+10,SidebarY+285,285,20,True ; backdrop for the label of OBJECTS
	Rect SidebarX+10,SidebarY+455,285,5,True ; between the object adjusters and object categories
	Rect SidebarX+195,SidebarY+430,100,5,True ; between object camera and More button

	If EditorMode=0
		TileColorR=RectOnR
		TileColorG=RectOnG
		TileColorB=RectOnB
		ObjectColorR=RectOffR
		ObjectColorG=RectOffG
		ObjectColorB=RectOffB
		UpdateCameraClsColor()
	Else If EditorMode=3
		TileColorR=RectOffR
		TileColorG=RectOffG
		TileColorB=RectOffB
		ObjectColorR=RectOnR
		ObjectColorG=RectOnG
		ObjectColorB=RectOnB
		UpdateCameraClsColor()
	EndIf
	
	Color TextLevelR,TextLevelG,TextLevelB
	Text SidebarX+90,SidebarY+5,"TILES"
	
	Color RectGlobalsR,RectGlobalsG,RectGlobalsB
	Rect SidebarX+214,SidebarY+100,81,145,True
	Color TextLevelR,TextLevelG,TextLevelB


	LevelWeatherString$=GetWeatherName$(LevelWeather)
	;If Len(LevelWeatherString$) Mod 2=0
	;	Text 715,100,LevelWeatherString$
	;Else
	;	Text 719,100,LevelWeatherString$
	;EndIf
	CenteredText(SidebarX+254,SidebarY+100,LevelWeatherString$)
	
	Line1$=GetMusicName$(LevelMusic)
	;Text SidebarX+219,SidebarY+115,Line1$
	CenteredText(SidebarX+254,SidebarY+115,Line1$)
	
	
	LevelEdgeStyleString$=GetLevelEdgeStyleChar$(LevelEdgeStyle)


	Text SidebarX+215,SidebarY+133,"<LevelTex>"
	Text SidebarX+215,SidebarY+150,"<WaterTex>"
	Text FlStartX,FlStartY," Fl Tr Gl B"
	Text FlStartX+12,SidebarY+180,Str$(WaterFlow)
	Text FlStartX+36,SidebarY+180,Str$(WaterTransparent)
	Text FlStartX+60,SidebarY+180,Str$(WaterGlow)
	Text FlStartX+80,SidebarY+180,LevelEdgeStyleString$
	Text SidebarX+223,SidebarY+200,"  Light  "
	Text SidebarX+212,SidebarY+215,Str$(LightRed)
	Text SidebarX+241,SidebarY+215,Str$(LightGreen)
	Text SidebarX+270,SidebarY+215,Str$(LightBlue)
	Text SidebarX+212,SidebarY+228,Str$(AmbientRed)
	Text SidebarX+241,SidebarY+228,Str$(AmbientGreen)
	Text SidebarX+270,SidebarY+228,Str$(AmbientBlue)
	
	
	StartX=SidebarX+10
	StartY=SidebarY+245
	Color TileColorR,TileColorG,TileColorB
	Rect StartX,StartY,285,40,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+2,StartY+2,"                                   "
	Text StartX+2+285/2-4*(Len(TilePresetCategoryName$(CurrentTilePresetCategory))+10),StartY,"Category: "+TilePresetCategoryName$(CurrentTilePresetCategory)
	Text StartX+2,StartY+22,"                                   "
	Text StartX+2+285/2-4*(Len(TilePresetTileName$(CurrentTilePresetTile))+2),StartY+22,"Tile: "+Left$(TilePresetTileName$(CurrentTilePresetTile),Len(TilePresetTileName$(CurrentTilePresetTile))-4)
	
	
	StartX=SidebarX+195 ;695
	StartY=SidebarY+435
	
	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,100,20,True
	Color TextLevelR,TextLevelG,TextLevelB
	
	If NofSelectedObjects<>0 And CurrentGrabbedObjectModified
		Text StartX+50,StartY+2,"Update"
	EndIf
	
	If NofObjectAdjusters>9
		; formerly the "More" button, located at StartX+6
		; ceiling division would be nice at NofObjectAdjusters...
		Text StartX,StartY+2,"Pg"+(ObjectAdjusterStart/9+1)+"/"+((NofObjectAdjusters-1)/9+1)
	EndIf
	
	StartX=SidebarX+10 ;510
	StartY=SidebarY+460
	
	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,285,40,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+2,StartY+2,"                                   "
	Text StartX+2+285/2-4*(Len(ObjectPresetCategoryName$(CurrentObjectPresetCategory))+10),StartY,"Category: "+ObjectPresetCategoryName$(CurrentObjectPresetCategory)
	Text StartX+2,StartY+22,"                                   "
	Text StartX+2+285/2-4*(Len(ObjectPresetObjectName$(CurrentObjectPresetObject))+4),StartY+22,"Object: "+Left$(ObjectPresetObjectName$(CurrentObjectPresetObject),Len(ObjectPresetObjectName$(CurrentObjectPresetObject))-4)
	
	StartX=SidebarX+215 ;715
	StartY=SidebarY+20
	Text StartX+4,StartY-15," GLOBALS" ;719,5," GLOBALS"
	Color RectGlobalsR,RectGlobalsG,RectGlobalsB
	Rect StartX,StartY,80,35,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+20,StartY+2,"Width"
	Text StartX,StartY+15,"<<"
	Text StartX+80-16,StartY+15,">>"
	CenteredText(StartX+40,StartY+15,Str$(LevelWidth))
	
	StartY=50
	Color RectGlobalsR,RectGlobalsG,RectGlobalsB
	Rect StartX,StartY,80,35,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+16,StartY+2,"Height"
	Text StartX,StartY+19,"^^" ; Formerly +15
	Text StartX+80-16,StartY+15,"vv"
	CenteredText(StartX+40,StartY+15,Str$(LevelHeight))
	
	Color TextLevelR,TextLevelG,TextLevelB
	Text SidebarX+150-7*4,SidebarY+290,"OBJECTS"
	StartX=SidebarX+10
	StartY=SidebarY+305
	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,185,150
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+92-11*4,StartY,"ADJUSTMENTS"
	
	If NofSelectedObjects=1
		Text StartX+2,StartY,"#"+SelectedObjects(0)
	ElseIf NofSelectedObjects>1
		Text StartX+2,StartY,"x"+NofSelectedObjects
	EndIf
	
	For i=ObjectAdjusterStart+0 To ObjectAdjusterStart+8
		
		DisplayObjectAdjuster(i)
		
	Next
	
	; local rendering end


End Function


Function EditorLocalControls()

	If KeyPressed(64) ; F6 key
		; toggle orthographic projection
		If Camera1Proj=1
			; to orthographic
			Camera1Proj=2
			Camera1Zoom#=Camera1OrthographicZoom#
			Camera1PerspectiveY#=EntityY(Camera1) ; save to return to it later
			PositionEntity Camera1,EntityX(Camera1),Camera1StartY*2,EntityZ(Camera1)
		Else
			; to perspective
			Camera1Proj=1
			Camera1Zoom#=Camera1PerspectiveZoom#
			PositionEntity Camera1,EntityX(Camera1),Camera1PerspectiveY#,EntityZ(Camera1)
		EndIf
		UpdateCameraProj()
		CameraZoom Camera1,Camera1Zoom#
		Camera1SavedProjMode=Camera1Proj
	EndIf

	If KeyPressed(65) ; F7 key
		; toggle wireframe mode
		UsingWireFrame=Not UsingWireFrame
		WireFrame UsingWireFrame
	EndIf
	
	MX=MouseX()
	MY=MouseY()
	
	Fast=False
	If ShiftDown() Then Fast=True
	
	
	; *************************************
	; Placing Tiles and Objects on the Editor Field
	; *************************************
	
	If EditorMode=EditorModeTile Or EditorMode=EditorModeObject
		If mx>=0 And mx<LevelViewportWidth And my>=0 And my<LevelViewportHeight
			Entity=CameraPick(camera1,MouseX(),MouseY())
			If Entity>0
				
				SetBrushCursorPosition(Floor(PickedX()),Floor(-PickedZ()))
				
				; Hide all cursors except for cursor 0.
				For i=1 To 3
					HideEntity CursorMeshPillar(i)
					HideEntity CursorMeshOpaque(i)
				Next
				
				PositionCursorEntity(0,BrushCursorX,BrushCursorY)
				If BrushMode<>BrushModeSetMirror
					If DupeMode=DupeModeX
						TargetX=MirrorAcrossInt(BrushCursorX,MirrorPositionX)
						PositionCursorEntity(1,TargetX,BrushCursorY)
					EndIf
					If DupeMode=DupeModeY
						TargetY=MirrorAcrossInt(BrushCursorY,MirrorPositionY)
						PositionCursorEntity(1,BrushCursorX,TargetY)
					EndIf
					If DupeMode=DupeModeXPlusY
						TargetX=MirrorAcrossInt(BrushCursorX,MirrorPositionX)
						TargetY=MirrorAcrossInt(BrushCursorY,MirrorPositionY)
						PositionCursorEntity(1,TargetX,BrushCursorY)
						PositionCursorEntity(2,BrushCursorX,TargetY)
						PositionCursorEntity(3,TargetX,TargetY)
					EndIf
				EndIf
				
				ShowBrushSurface()
				
				BrushR=GetBrushModeColor(BrushMode,0)
				BrushG=GetBrushModeColor(BrushMode,1)
				BrushB=GetBrushModeColor(BrushMode,2)
				
				For i=0 To 3
					EntityColor CursorMeshPillar(i),BrushR,BrushG,BrushB
					EntityColor CursorMeshOpaque(i),BrushR,BrushG,BrushB
					EntityColor CursorMeshTexturePicker,BrushR,BrushG,BrushB
					EntityColor BrushMesh,BrushR,BrushG,BrushB
					EntityColor BrushTextureMesh,BrushR,BrushG,BrushB
				Next
				
				Color TextLevelR,TextLevelG,TextLevelB
				Text LevelViewportWidth/2-4.5*8,LevelViewportHeight,"X:"+Str$(BrushCursorX)+", Y:"+Str$(BrushCursorY)
				
				If LeftMouse=False
					If BlockPlacingMode=BlockPlacingModePlace ; Release left mouse button to place block.
						If EditorMode=0
							For i=cornleft To cornright
								For j=cornup To corndown
									ChangeLevelTile(i,j,True)
								Next
							Next
						ElseIf EditorMode=3
							PrepareObjectSelection()
							For i=cornleft To cornright
								For j=cornup To corndown
									PlaceObject(i,j)
								Next
							Next
						EndIf
						SetBrushMode(BrushModeBlock)
						AddUnsavedChange()
					EndIf
				
					OnceTilePlacement=True
					DidStepPerClick=False
				ElseIf DidStepPerClick=False
					DidStepPerClick=True
					If StepPer=StepPerClick
						RunStepSize()
					EndIf
					
					If BrushMode<>BrushModeBlock And BrushMode<>BrushModeTestLevel And BrushMode<>BrushModeSetMirror
						AddUnsavedChange()
					EndIf
				EndIf
				
				If LeftMouse=True And LeftMouseReleased=True And (EditorMode<>0 Or OnceTilePlacement=True)
					OnceTilePlacement=False
					
					If EditorMode=EditorModeTile And BrushMode<>BrushModeBlock ; Don't want to run a step size when just trying to select a block region.
						If StepPer=StepPerPlacement
							RunStepSize()
						EndIf
					EndIf
					
					If BrushMode=BrushModeBlock
						LeftMouseReleased=False
						StartBlockModeBlock(BlockPlacingModePlace)
					ElseIf BrushMode=BrushModeTestLevel
						If AskToSaveLevelAndExit()
							; Just in case the user is cheeky and decides to use Test Level At Brush in a brand new wlv.
							If Not LevelExists(CurrentLevelNumber)
								SaveLevel()
							EndIf
							
							StartTestModeAt(CurrentLevelNumber,BrushCursorX,BrushCursorY)
						EndIf
					ElseIf BrushMode=BrushModeSetMirror
						SetBrushMode(BrushModeNormal)
					Else
						PrepareObjectSelection()
						For i=0 To FloodedElementCount-1
							thisx=FloodedStackX(i)
							thisy=FloodedStackY(i)
							PlaceObjectOrChangeLevelTile(thisx,thisy)
						Next
					EndIf
						
					If EditorMode=EditorModeObject
						LeftMouseReleased=False
					EndIf
					
					If EditorMode=EditorModeTile
						BrushCursorProbablyModifiedTiles()
					EndIf
				EndIf
				If RightMouse=True
					If RightMouseReleased=True
						RightMouseReleased=False
						
						; grab object or tile
						
						If EditorMode=0
							GrabLevelTile(BrushCursorX,BrushCursorY)
							
							CopyLevel()
							
							DraggedTilesSpotX=BrushCursorX
							DraggedTilesSpotY=BrushCursorY
							
							For i=0 To MaxLevelCoordinate
								For j=0 To MaxLevelCoordinate
									DraggedTilesEnabled(i,j)=False
								Next
							Next
							
							ClearObjectDrag()
							For i=0 To FloodedElementCount-1
								thisx=FloodedStackX(i)
								thisy=FloodedStackY(i)
								
								If IsPositionInLevelArrayBounds(thisx,thisy)
									DraggedTilesEnabled(thisx,thisy)=True
									CopyTile(LevelTiles(thisx,thisy),DraggedTiles(thisx,thisy))
									CopyTile(CurrentTile,CopyLevelTiles(thisx,thisy))
									
									If LevelTileObjectCount(thisx,thisy)<>0
										For j=0 To NofObjects-1
											If ObjectIsAtFloat(LevelObjects(j),thisx,thisy)
												AddDragObject(j)
											EndIf
										Next
									EndIf
								EndIf
							Next
							StartObjectDrag()
							
							TileDragging=True
						ElseIf EditorMode=3
							If BrushMode=BrushModeBlock
								PrepareObjectSelection()
								StartBlockModeBlock(BlockPlacingModeCopy)
							ElseIf BrushMode<>BrushModeBlockPlacing
								PrepareObjectSelection()
								If KeyDown(41) ; tilde key
									If LevelTileObjectCount(BrushCursorX,BrushCursorY)<>0
										GrabObject(BrushCursorX,BrushCursorY,False)
										TargetObject.GameObject=LevelObjects(SelectedObjects(NofSelectedObjects-1))
										TargetAttributes.GameObjectAttributes=TargetObject\Attributes
										For i=0 To NofObjects-1
											Obj.GameObject=LevelObjects(i)
											Attributes.GameObjectAttributes=Obj\Attributes
											If Attributes\LogicType=TargetAttributes\LogicType And Attributes\ModelName=TargetAttributes\ModelName
												AddOrToggleSelectObject(i)
											EndIf
										Next
									EndIf
								Else
									If BrushMode=BrushModeNormal And (Not ShiftDown())
										GrabObject(BrushCursorX,BrushCursorY,False)
									Else
										For i=0 To FloodedElementCount-1
											thisx=FloodedStackX(i)
											thisy=FloodedStackY(i)
											GrabObject(thisx,thisy,True)
										Next
									EndIf
								EndIf
								FinishObjectSelection()
							EndIf
						EndIf
					EndIf
				Else
					If BlockPlacingMode=BlockPlacingModeCopy
						For i=0 To FloodedElementCount-1
							thisx=FloodedStackX(i)
							thisy=FloodedStackY(i)
							GrabObject(thisx,thisy,True)
						Next
						FinishObjectSelection()
						SetBrushMode(BrushModeBlock)
					Else
						If DragChange
							DragChange=False
							AddUnsavedChange()
						EndIf
					EndIf
					
					TileDragging=False
					NofDraggedObjects=0
				EndIf
				
				If MouseDown(3) ; middle click / ; middle mouse
					SetCurrentObjectTargetLocation(BrushCursorX,BrushCursorY)
					SetEditorMode(3)
				EndIf
			Else
				BrushCursorOffMap()
			EndIf
	
			If DeleteKey=True
				If DeleteKeyReleased=True
					DeleteKeyReleased=False
					If BrushMode=BrushModeBlock
						StartBlockModeBlock(BlockPlacingModeDelete)
					ElseIf BrushMode<>BrushModeBlockPlacing
						For i=0 To FloodedElementCount-1
							thisx=FloodedStackX(i)
							thisy=FloodedStackY(i)
							DeleteObjectAt(thisx,thisy)
						Next
						
						AddUnsavedChange()
					EndIf
					
					RecalculateDragSize()
				EndIf
			Else
				If BlockPlacingMode=BlockPlacingModeDelete
					For i=cornleft To cornright
						For j=cornup To corndown
							DeleteObjectAt(i,j)
						Next
					Next
					SetBrushMode(BrushModeBlock)
					
					AddUnsavedChange()
				EndIf
			EndIf
			
			If ReturnKey=True And ReturnKeyReleased=True
				ReturnKeyReleased=False

				; set custom brush
				BrushXStart=GetBrushXStart()
				BrushYStart=GetBrushYStart()
				BrushXEnd=GetBrushXEnd(BrushXStart)
				BrushYEnd=GetBrushYEnd(BrushYStart)
				BrushSpaceOriginX=BrushCursorX
				BrushSpaceOriginY=BrushCursorY
				BrushSpaceWidth=BrushWidth
				BrushSpaceHeight=BrushHeight
				If EditorMode=0
					For i=BrushXStart To BrushXEnd
						For j=BrushYStart To BrushYEnd
							BrushSpaceX=LevelSpaceToBrushSpaceX(i,BrushWrapModulus)
							BrushSpaceY=LevelSpaceToBrushSpaceY(j,BrushWrapModulus)
							CopyLevelTileToBrush(i,j,BrushSpaceX,BrushSpaceY)
						Next
					Next
					GenerateBrushPreview()
				ElseIf EditorMode=3
					NofBrushObjects=0
					
					For k=0 To NofObjects-1
						ObjTileX=LevelObjects(k)\Position\TileX
						ObjTileY=LevelObjects(k)\Position\TileY
						If ObjTileX>=BrushXStart And ObjTileX<BrushXStart+BrushWidth And ObjTileY>=BrushYStart And ObjTileY<BrushYStart+BrushHeight
							BrushSpaceX=LevelSpaceToBrushSpaceX(ObjTileX,BrushWrapModulus)
							BrushSpaceY=LevelSpaceToBrushSpaceY(ObjTileY,BrushWrapModulus)
							CopyObjectToBrush(LevelObjects(k),NofBrushObjects,BrushSpaceX,BrushSpaceY)
							NofBrushObjects=NofBrushObjects+1
						EndIf
					Next
					
					If NofBrushObjects=0
						SetBrushToCurrentObject()
					Else
						GenerateBrushPreview()
						;ShowMessage(NofBrushObjects+" objects found in brush.",1000)
					EndIf
				EndIf
			EndIf
		
		Else
			BrushCursorOffMap()
		EndIf
	EndIf
	
	
	
	
	
	; *************************************
	; Selecting A Texture / Picking a Texture / Texture Picker / Tile Picker
	; *************************************
	If EditorMode=1 Or EditorMode=2
		If mx>=0 And mx<LevelViewportWidth And my>=0 And my<LevelViewportHeight
			nmx=LevelViewportX(mx)
			nmy=my
			DivisorNumerator=LevelViewportHeight
			DivisorX#=Float#(DivisorNumerator)/8.0
			DivisorY#=Float#(DivisorNumerator)/8.0
			StepSize#=1.0/8.0
			ScaleEntity CursorMeshPillar(0),0.0325,0.01,0.0325
			ScaleEntity CursorMeshOpaque(0),0.0325,0.01,0.0325
			ScaleEntity CursorMeshTexturePicker,0.0325,0.01,0.0325
			tilepickx=Floor(nmx/DivisorX#)
			tilepicky=Floor(nmy/DivisorY#)
			tilepickx=ClampInt(0,7,tilepickx)
			tilepicky=ClampInt(0,7,tilepicky)
			PositionEntity CursorMeshPillar(0),tilepickx*StepSize#+StepSize#/2.0,200,-tilepicky*StepSize#-StepSize#/2.0,200
			PositionEntity CursorMeshOpaque(0),tilepickx*StepSize#+StepSize#/2.0,200,-tilepicky*StepSize#-StepSize#/2.0,200
			PositionEntity CursorMeshTexturePicker,tilepickx*StepSize#+StepSize#/2.0,200,-tilepicky*StepSize#-StepSize#/2.0,200
			ShowEntity CursorMeshPillar(0)
			ShowEntity CursorMeshOpaque(0)
			ShowEntity CursorMeshTexturePicker
			If LeftMouse=True
				If editormode=1
					; main texture
					CurrentTile\Terrain\Texture=tilepickx+tilepicky*8
				Else If editormode=2
					; main texture
					CurrentTile\Terrain\SideTexture=tilepickx+tilepicky*8
				EndIf
				SetEditorMode(0)
				LeftMouseReleased=False
				BuildCurrentTileModel()
				SetBrushToCurrentTile()
				ScaleEntity CursorMeshPillar(0),1,1,1
			EndIf
		Else
			HideCursors()
		EndIf
	EndIf

	
	; *************************************
	; Change the CurrentTile
	; *************************************
		
	StartX=SidebarX+10
	StartY=SidebarY+20
	
	DelayTime=10
	
	If MX>=StartX And MX<StartX+200 And MY<StartY+220
		If LeftMouse=True Or RightMouse=True Or MouseScroll<>0 Or ReturnKey=True
			SetEditorMode(0)
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
			; CurrentTile\Terrain\Rotation
			RightMouseReleased=False
			CurrentTile\Terrain\Rotation=(CurrentTile\Terrain\Rotation+1) Mod 8
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
			SetEditorMode(0)
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			; Texture
			If CtrlDown()
				CurrentTile\Terrain\Texture=InputInt("Enter Texture ID: ")
				BuildCurrentTileModel()
				SetBrushToCurrentTile()
			Else
				Camera1To3Proj()
				SetEditorMode(1)
			EndIf
		ElseIf MouseScroll<>0
			CurrentTile\Terrain\Texture=CurrentTile\Terrain\Texture+MouseScroll
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileTextureUse)
				CurrentTileTextureUse=1-CurrentTileTextureUse
			EndIf
			SetEditorMode(0)
		EndIf
		ShowTooltipRightAligned(StartX,StartY+95,"Texture ID: "+CurrentTile\Terrain\Texture)
	EndIf
		
	
	; CurrentTile\Terrain\SideRotation/Texture
	If MX>=StartX And MX<StartX+100 And MY>=StartY+100 And MY<StartY+155
		If RightMouse=True And RightMouseReleased=True
			; SideRotation
			RightMouseReleased=False
			CurrentTile\Terrain\SideRotation=(CurrentTile\Terrain\SideRotation+1) Mod 8
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
			SetEditorMode(0)
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			; SideTexture
			If CtrlDown()
				CurrentTile\Terrain\SideTexture=InputInt("Enter SideTexture ID: ")
				BuildCurrentTileModel()
				SetBrushToCurrentTile()
			Else
				Camera1To3Proj()
				SetEditorMode(2)
			EndIf
		ElseIf MouseScroll<>0
			CurrentTile\Terrain\SideTexture=CurrentTile\Terrain\SideTexture+MouseScroll
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileSideTextureUse)
				CurrentTileSideTextureUse=1-CurrentTileSideTextureUse
			EndIf
			SetEditorMode(0)
		EndIf
		ShowTooltipRightAligned(StartX,StartY+140,"SideTexture ID: "+CurrentTile\Terrain\SideTexture)
	EndIf
	
	; WaterTexture/Rotation
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+40 And MY<StartY+115
		If RightMouse=True And RightMouseReleased=True
			CurrentTile\Water\Rotation=(CurrentTile\Water\Rotation+1) Mod 8
			RightMouseReleased=False
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
			SetEditorMode(0)
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			CurrentTile\Water\Texture=(CurrentTile\Water\Texture+1) Mod 8
			LeftMouseReleased=False
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
			SetEditorMode(0)
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentWaterTileTextureUse)
				CurrentWaterTileTextureUse=1-CurrentWaterTileTextureUse
			EndIf
			SetEditorMode(0)
		EndIf

	EndIf

	; CurrentTile\Terrain\Extrusion
	If MX>=StartX And MX<StartX+100 And MY>=StartY And MY<StartY+15
		CurrentTile\Terrain\Extrusion#=AdjustFloat#("Enter Xtrude: ", CurrentTile\Terrain\Extrusion#, 0.1, 1.0, DelayTime)
		
		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf
		
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeTileExtrusion#=0.0
					StepSizeTileExtrusion#=InputFloat#("Enter step size for Xtrude: ")
				Else
					StepSizeTileExtrusion#=0.0
				EndIf
			Else
				If MaybeUnuseAllTileAttributes(CurrentTileExtrusionUse)
					CurrentTileExtrusionUse=1-CurrentTileExtrusionUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf
 	EndIf
	; CurrentTile\Terrain\Height
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY And MY<StartY+15
		CurrentTile\Terrain\Height#=AdjustFloat#("Enter Height: ", CurrentTile\Terrain\Height#, 0.1, 1.0, DelayTime)
		
		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeTileHeight#=0.0
					StepSizeTileHeight#=InputFloat#("Enter step size for Height: ")
				Else
					StepSizeTileHeight#=0.0
				EndIf
				SetBrushToCurrentTile()
			Else
				If MaybeUnuseAllTileAttributes(CurrentTileHeightUse)
					CurrentTileHeightUse=1-CurrentTileHeightUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf
 	EndIf
	; CurrentTile\Terrain\Logic
	If MX>=StartX And MX<StartX+200 And MY>=StartY+15 And MY<StartY+30
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			If CtrlDown()
				CurrentTile\Terrain\Logic=InputInt("Enter Logic: ")
				ReturnKey=False
				ReturnKeyReleased=False
			Else
				Select CurrentTile\Terrain\Logic
					Case 0
						CurrentTile\Terrain\Logic=1
					Case 1
						CurrentTile\Terrain\Logic=2
					Case 2
						CurrentTile\Terrain\Logic=5
					Case 5
						CurrentTile\Terrain\Logic=11
					Case 11
						CurrentTile\Terrain\Logic=12
					Case 12
						CurrentTile\Terrain\Logic=13
					Default
						CurrentTile\Terrain\Logic=0
				End Select
			EndIf
			SetBrushToCurrentTile()
			
			LeftMouseReleased=False
			RightMouseReleased=False
			SetEditorMode(0)
		EndIf
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			Select CurrentTile\Terrain\Logic
				Case 2
					CurrentTile\Terrain\Logic=1
				Case 5
					CurrentTile\Terrain\Logic=2
				Case 11
					CurrentTile\Terrain\Logic=5
				Case 12
					CurrentTile\Terrain\Logic=11
				Case 13
					CurrentTile\Terrain\Logic=12
				Case 0
					CurrentTile\Terrain\Logic=13
				Default
					CurrentTile\Terrain\Logic=0
			End Select
			SetBrushToCurrentTile()
			
			LeftMouseReleased=False
			RightMouseReleased=False
			SetEditorMode(0)
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileLogicUse)
				CurrentTileLogicUse=1-CurrentTileLogicUse
			EndIf
			SetEditorMode(0)
		EndIf

 	EndIf

	; CurrentTile\Terrain\Random
	If MX>=StartX And MX<StartX+200 And MY>=StartY+170 And MY<StartY+185
		CurrentTile\Terrain\Random#=AdjustFloat#("Enter Random: ", CurrentTile\Terrain\Random#, 0.01, 0.1, DelayTime)
		
		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeTileRandom#=0.0
					StepSizeTileRandom#=InputFloat#("Enter step size for Random: ")
				Else
					StepSizeTileRandom#=0.0
				EndIf
			Else
				If MaybeUnuseAllTileAttributes(CurrentTileRandomUse)
					CurrentTileRandomUse=1-CurrentTileRandomUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf

		SetBrushToCurrentTile()
 	EndIf
	; CurrentTile\Terrain\Rounding
	If MX>=StartX And MX<StartX+100 And MY>=StartY+185 And MY<StartY+200
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
			LeftMouseReleased=False
			RightMouseReleased=False
			CurrentTile\Terrain\Rounding=1-CurrentTile\Terrain\Rounding
			SetEditorMode(0)
			SetBrushToCurrentTile()
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileRoundingUse)
				CurrentTileRoundingUse=1-CurrentTileRoundingUse
			EndIf
			SetEditorMode(0)
		EndIf
	EndIf
	; CurrentTile\Terrain\EdgeRandom
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+185 And MY<StartY+200
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
			LeftMouseReleased=False
			RightMouseReleased=False
			CurrentTile\Terrain\EdgeRandom=1-CurrentTile\Terrain\EdgeRandom
			SetEditorMode(0)
			SetBrushToCurrentTile()
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileEdgeRandomUse)
				CurrentTileEdgeRandomUse=1-CurrentTileEdgeRandomUse
			EndIf
			SetEditorMode(0)
		EndIf
	EndIf
	
	; CurrentTile\Water\Height
	If MX>=StartX And MX<StartX+100 And MY>=StartY+200 And MY<StartY+215
		CurrentTile\Water\Height#=AdjustFloat#("Enter WHeight: ", CurrentTile\Water\Height#, 0.1, 1.0, DelayTime)
		
		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeWaterTileHeight#=0.0
					StepSizeWaterTileHeight#=InputFloat#("Enter step size for WHeight: ")
				Else
					StepSizeWaterTileHeight#=0.0
				EndIf
			Else
				If MaybeUnuseAllTileAttributes(CurrentWaterTileHeightUse)
					CurrentWaterTileHeightUse=1-CurrentWaterTileHeightUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf

		SetBrushToCurrentTile()
 	EndIf
	; CurrentTile\Water\Turbulence
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+200 And MY<StartY+215
		CurrentTile\Water\Turbulence#=AdjustFloat#("Enter WTurb: ", CurrentTile\Water\Turbulence#, 0.1, 1.0, DelayTime)
		
		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeWaterTileTurbulence#=0.0
					StepSizeWaterTileTurbulence#=InputFloat#("Enter step size for WTurb: ")
				Else
					StepSizeWaterTileTurbulence#=0.0
				EndIf
			Else
				If MaybeUnuseAllTileAttributes(CurrentWaterTileTurbulenceUse)
					CurrentWaterTileTurbulenceUse=1-CurrentWaterTileTurbulenceUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf

 	EndIf


	; *************************************
	; Textures and global settings
	; *************************************
	

	If mx>=SidebarX+215 
				
		If my>=100 And my<115 And ((leftmouse=True And leftmousereleased=True) Or MouseScroll>0)
			leftmousereleased=False
			LevelWeather=LevelWeather+1
			If levelweather=18 Then levelweather=0
			LightingWasChanged() ; Necessary for if alarm weather was being used.
			AddUnsavedChange()
		EndIf
		If my>=100 And my<115 And ((rightmouse=True And rightmousereleased=True) Or MouseScroll<0)
			rightmousereleased=False
			LevelWeather=LevelWeather-1
			If levelweather=-1 Then levelweather=17
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		OldValue=LevelMusic
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
			UpdateMusic()
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
			UpdateMusic()
		EndIf
		If LevelMusic<>OldValue
			AddUnsavedChange()
		EndIf


		; level/water textures
		If my>133 And my<148
			If mx>SidebarX+255 And leftmouse=True And leftmousereleased=True
				CurrentLevelTexture=CurrentLevelTexture+1
				If CurrentLevelTexture=NofLevelTextures Then currentleveltexture=0
				
				FreeTexture LevelTexture
				UpdateLevelTextureDefault()
				
				For j=0 To LevelHeight-1
					EntityTexture LevelMesh(j),LevelTexture
				Next
				
				leftmousereleased=False
				buildcurrenttilemodel()
				AddUnsavedChange()
			EndIf
			If mx<=SidebarX+255 And leftmouse=True And leftmousereleased=True
				CurrentLevelTexture=CurrentLevelTexture-1
				If CurrentLevelTexture<0 Then currentleveltexture=NofLevelTextures-1
				
				FreeTexture LevelTexture
				UpdateLevelTextureDefault()
				
				For j=0 To LevelHeight-1
					EntityTexture LevelMesh(j),LevelTexture
				Next
				
				leftmousereleased=False
				buildcurrenttilemodel()
				AddUnsavedChange()
			EndIf
			ShowTooltipRightAligned(SidebarX+210,163,CurrentLevelTextureName$())
		EndIf

		If my>150 And my<163 
			If mx>SidebarX+255 And leftmouse=True And leftmousereleased=True
				CurrentWaterTexture=CurrentWaterTexture+1
				
				If CurrentWaterTexture=NofWaterTextures Then currentWatertexture=0
				
				FreeTexture WaterTexture
				UpdateWaterTextureDefault()
				
				For j=0 To LevelHeight-1
					EntityTexture WaterMesh(j),WaterTexture
				Next
				leftmousereleased=False
				buildcurrenttilemodel()
				AddUnsavedChange()
			EndIf
			If mx<=SidebarX+255 And leftmouse=True And leftmousereleased=True
				CurrentWaterTexture=CurrentWaterTexture-1
				
				If CurrentWaterTexture=-1 Then currentWatertexture=NofWaterTextures-1
				
				FreeTexture WaterTexture
				UpdateWaterTextureDefault()
				
				For j=0 To LevelHeight-1
					EntityTexture WaterMesh(j),WaterTexture
				Next
				leftmousereleased=False
				buildcurrenttilemodel()
				AddUnsavedChange()
			EndIf
			ShowTooltipRightAligned(SidebarX+210,180,CurrentWaterTextureName$())
		EndIf

		; custom level/water
		If my>133 And my<148 And rightmouse=True And rightmousereleased=True
			FlushKeys
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color TextLevelR,TextLevelG,TextLevelB
			LevelTextureCustomName$=Input$( "Custom Texture Name (e.g. 'customtemplate'):")
			
			If FileType (globaldirname$+"\custom\leveltextures\leveltex "+leveltexturecustomname$+".bmp")<>1 And FileType (globaldirname$+"\custom content\Model\Textures\backgroundtex "+leveltexturecustomname$+"1.bmp")<>1 And FileType (globaldirname$+"\custom content\Model\Textures\backgroundtex "+leveltexturecustomname$+"2.bmp")<>1
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
					UpdateLevelTextureDefault()
					

					Delay 2000
				Else
					currentleveltexture=-1
					UpdateLevelTexture()
				EndIf
			EndIf
			
			rightmousereleased=False
			buildcurrenttilemodel()
			AddUnsavedChange()
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
				WaterTexture=myLoadTexture(globaldirname$+"\custom\leveltextures\watertex "+watertexturecustomname$+".jpg",1)
				If WaterTexture=0
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,0
					Print "TEXTURE COULDN'T LOAD!"
					UpdateWaterTextureDefault()
					

					Delay 2000
				Else
					currentwatertexture=-1
					UpdateWaterTexture()
				EndIf
			EndIf
			
			rightmousereleased=False
			buildcurrenttilemodel()
			AddUnsavedChange()
		EndIf

		
		If my>165 And my<195 ;my<185 
			If mx>FlStartX+8 And mx<FlStartX+24
				OldValue=WaterFlow
				Waterflow=AdjustInt("Enter Waterflow: ", Waterflow, 1, 10, DelayTime)
				ShowTooltipCenterAligned(FlStartX+16,FlStartY+8,"Water Flow Speed: "+Waterflow)
				If OldValue<>WaterFlow
					AddUnsavedChange()
				EndIf
			EndIf
			If mx>FlStartX+32 And mx<FlStartX+48
				If (leftmouse=True And leftmousereleased=True) Or (rightmouse=True And rightmousereleased=True) Or MouseScroll<>0
					WaterTransparent=1-WaterTransparent
					UpdateAllWaterMeshTransparent()
					leftmousereleased=False
					rightmousereleased=False
					AddUnsavedChange()
				EndIf
				ShowTooltipCenterAligned(FlStartX+40,FlStartY+8,"Water is Transparent: "+TrueToYes$(WaterTransparent))
			EndIf
			If mx>FlStartX+56 And mx<FlStartX+72
				If (leftmouse=True And leftmousereleased=True) Or (rightmouse=True And rightmousereleased=True) Or MouseScroll<>0
					WaterGlow=1-WaterGlow
					UpdateAllWaterMeshGlow()
					leftmousereleased=False
					rightmousereleased=False
					AddUnsavedChange()
				EndIf
				ShowTooltipCenterAligned(FlStartX+64,FlStartY+8,"Water Glows: "+TrueToYes$(WaterGlow))
			EndIf
			If mx>FlStartX+80 And mx<FlStartX+100
				OldValue=LevelEdgeStyle
				LevelEdgeStyle=AdjustInt("Enter LevelEdgeStyle: ", LevelEdgeStyle, 1, 10, DelayTime)
				If LevelEdgeStyle<1
					LevelEdgeStyle=4
				ElseIf LevelEdgeStyle>4
					LevelEdgeStyle=1
				EndIf
				ShowTooltipCenterAligned(FlStartX+90,FlStartY+8,"Level Edge Style: "+GetLevelEdgeStyleName$(LevelEdgeStyle))
				If OldValue<>LevelEdgeStyle
					AddUnsavedChange()
				EndIf
			EndIf
		EndIf
	EndIf
	
	If Fast
		ChangeSpeed=10
	Else
		ChangeSpeed=1
	EndIf
	
	StartX=SidebarX+212
	
	If mx>StartX And my>215-13 And my<228-13
		If leftmouse=True And CtrlDown()
			TheString$=InputString$("Enter color for all light values (or leave blank to cancel): ")
			If TheString$<>""
				LightValue=TheString$
				LightRed=LightValue
				LightGreen=LightValue
				LightBlue=LightValue
				AmbientRed=LightValue
				AmbientGreen=LightValue
				AmbientBlue=LightValue
				LightingWasChanged()
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf
	If mx>StartX And my>215 And mx<StartX+24 And my<228
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				LightRed=InputInt("Enter LightRed: ")
			Else
				LightRed=LightRed+ChangeSpeed
				;If lightred>=256 Then lightred=lightred-256
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightRed=LightRed-ChangeSpeed
			;If lightred=-1 Then lightred=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	If mx>StartX+29 And my>215 And mx<StartX+24+29 And my<228
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				LightGreen=InputInt("Enter LightGreen: ")
			Else
				LightGreen=LightGreen+ChangeSpeed
				;If LightGreen=256 Then LightGreen=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightGreen=LightGreen-ChangeSpeed
			;If LightGreen=-1 Then LightGreen=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	If mx>StartX+29+29 And my>215 And mx<StartX+24+29+29 And my<228
		If leftmouse=True  Or MouseScroll>0
			If CtrlDown()
				LightBlue=InputInt("Enter LightBlue: ")
			Else
				LightBlue=LightBlue+ChangeSpeed
				;If LightBlue=256 Then LightBlue=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightBlue=LightBlue-ChangeSpeed
			;If LightBlue=-1 Then LightBlue=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	
	If mx>StartX And my>215+13 And mx<StartX+24 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientRed=InputInt("Enter AmbientRed: ")
			Else
				AmbientRed=AmbientRed+ChangeSpeed
				;If Ambientred=256 Then ambientred=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientRed=AmbientRed-ChangeSpeed
			;If Ambientred=-1 Then ambientred=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	If mx>StartX+29 And my>215+13 And mx<StartX+24+29 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientGreen=InputInt("Enter AmbientGreen: ")
			Else
				AmbientGreen=AmbientGreen+ChangeSpeed
				;If AmbientGreen=256 Then AmbientGreen=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientGreen=AmbientGreen-ChangeSpeed
			;If AmbientGreen=-1 Then AmbientGreen=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	If mx>StartX+29+29 And my>215+13 And mx<StartX+24+29+29 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientBlue=InputInt("Enter AmbientBlue: ")
			Else
				AmbientBlue=AmbientBlue+ChangeSpeed
				;If AmbientBlue=256 Then AmbientBlue=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientBlue=AmbientBlue-ChangeSpeed
			;If AmbientBlue=-1 Then AmbientBlue=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf





	
		
			

	; *************************************
	; Preset Tiles
	; *************************************
	
	StartX=SidebarX+10
	StartY=SidebarY+245
	
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

		EndIf
	EndIf

	
	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentTilePresetTile=CurrentTilePresetTile-1
			If CurrentTilePresetTile=-1 Then CurrentTilePresetTile=NofTilePresetTiles-1
			RightMouseReleased=False
			LoadTilePreset()
			SetEditorMode(0)

		EndIf
	EndIf
	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentTilePresetTile=CurrentTilePresetTile+1
			If CurrentTilePresetTile=NofTilePresetTiles Then CurrentTilePresetTile=0
			LeftMouseReleased=False
			LoadTilePreset()
			SetEditorMode(0)

		EndIf
	EndIf


	
	
	; *************************************
	; LevelSize
	; *************************************
	
	StartX=SidebarX+215 ;715
	StartY=SidebarY+20
	
	If ShiftDown()
		Adj=10
	Else
		Adj=1
	EndIf
	
	If mx>=StartX And mx<StartX+80 And my>=StartY+15 And my<80
	
		If MouseScroll<>0
			BorderExpandOption=Not BorderExpandOption
		EndIf
		
		If BorderExpandOption=0
			ResizeName$="Current"
		Else
			ResizeName$="Duplicate"
		EndIf
	
		; Formerly StartX,StartY+60
		ShowTooltipRightAligned(GfxWidth,StartY+105,"Scroll the mouse wheel to change the resize setting: Resize "+ResizeName$)
	
		If mx<StartX+40 And my<StartY+30 
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				If CtrlDown()
					TheString$=InputString$("Enter Width: ")
					If TheString$<>""
						NewWidth=TheString$
						DeltaWidth=NewWidth-LevelWidth
						WidthLeftChange=DeltaWidth
						ReSizeLevel()
					EndIf
				Else
					WidthLeftChange=Adj
					ReSizeLevel()
				EndIf
			EndIf
			If RightMouse=True And RightMouseReleased=True
				If MaybeGetResizeConfirmation(Adj)
					WidthLeftChange=-Adj
					RightMouseReleased=False
					ReSizeLevel()
				EndIf
			EndIf
		EndIf	
		If mx>=StartX+40 And my<StartY+30 
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				If CtrlDown()
					TheString$=InputString$("Enter Width: ")
					If TheString$<>""
						NewWidth=TheString$
						DeltaWidth=NewWidth-LevelWidth
						WidthRightChange=DeltaWidth
						ReSizeLevel()
					EndIf
				Else
					WidthRightChange=Adj
					ReSizeLevel()
				EndIf
			EndIf
			If RightMouse=True And RightMouseReleased=True
				If MaybeGetResizeConfirmation(Adj)
					WidthRightChange=-Adj
					RightMouseReleased=False
					ReSizeLevel()
				EndIf
			EndIf
		EndIf
		
		StartY=50
		
		If mx<StartX+40 And my>=StartY+15 And my<StartY+30 
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				If CtrlDown()
					TheString$=InputString$("Enter Height: ")
					If TheString$<>""
						NewHeight=TheString$
						DeltaHeight=NewHeight-LevelHeight
						HeightTopChange=DeltaHeight
						ResizeLevel()
					EndIf
				Else
					HeightTopChange=Adj
					ReSizeLevel()
				EndIf
			EndIf
			If RightMouse=True And RightMouseReleased=True
				If MaybeGetResizeConfirmation(Adj)
					HeightTopChange=-Adj
					RightMouseReleased=False
					ReSizeLevel()
				EndIf
			EndIf
		EndIf
		If mx>=StartX+40 And my>=StartY+15 And my<StartY+30 
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				If CtrlDown()
					TheString$=InputString$("Enter Height: ")
					If TheString$<>""
						NewHeight=TheString$
						DeltaHeight=NewHeight-LevelHeight
						HeightBottomChange=DeltaHeight
						ResizeLevel()
					EndIf
				Else
					HeightBottomChange=Adj
					ReSizeLevel()
				EndIf
			EndIf
			If RightMouse=True And RightMouseReleased=True
				If MaybeGetResizeConfirmation(Adj)
					HeightBottomChange=-Adj
					RightMouseReleased=False
					ReSizeLevel()
				EndIf
			EndIf
		EndIf
	EndIf
	
	; *************************************
	; OBJECTS
	; *************************************
	
	If mx>LevelViewportWidth And my>SidebarY+285
		If (LeftMouse=True Or RightMouse=True) And my<LevelViewportHeight
			SetEditorMode(3)
		EndIf
		If my<SidebarY+455
			If DeleteKey=True And DeleteKeyReleased=True And NofSelectedObjects<>0
				DeleteKeyReleased=False
				
				While NofSelectedObjects<>0
					DeleteObject(SelectedObjects(0))
				Wend
				
				RecalculateDragSize()
				SetEditorMode(3)
				
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf
	
	StartX=SidebarX+10 ;510
	StartY=SidebarY+305
	
	If mx>=StartX And mx<=StartX+185
		If my>=StartY And my<StartY+15
			TooltipLeftY=StartY+30
			Percent#=Float#(NofObjects)/Float#(MaxNofObjects)*100.0
			ShowTooltipRightAligned(StartX,TooltipLeftY,NofObjects+"/"+MaxNofObjects+" ("+Percent#+"%) level object slots used")
			If NofSelectedObjects=1 And LeftMouse=True And LeftMouseReleased=True And CtrlDown()
				LeftMouseReleased=False
				TakenString$=InputString$("Set object index: ")
				If TakenString$<>""
					TargetIndex=TakenString$
					If TargetIndex>NofObjects-1
						TargetIndex=NofObjects-1
					ElseIf TargetIndex<0
						TargetIndex=0
					EndIf
					SetObjectIndex(SelectedObjects(0),TargetIndex)
					RemoveSelectObject(SelectedObjects(0))
					AddSelectObject(TargetIndex)
				EndIf
			EndIf
		EndIf
	
		For i=ObjectAdjusterStart+0 To ObjectAdjusterStart+8
			If my>=StartY+15+(i-ObjectAdjusterStart)*15 And my<StartY+15+(i-ObjectAdjusterStart)*15+15
				If LeftMouse=True Or RightMouse=True Or MouseScroll<>0 Or ReturnKey=True
					AdjustObjectAdjuster(i)
					SetEditorMode(3)
				EndIf
				HoverOverObjectAdjuster(i)
			EndIf
		Next
	EndIf
	
	; *************************************
	; Preset Objects
	; *************************************
	
	StartX=SidebarX+195 ;695
	StartY=SidebarY+435
	
	If CtrlDown()
		If KeyPressed(49) ; Ctrl+N
			SetBrushMode(BrushModeNormal)
		EndIf
		If KeyPressed(48) ; Ctrl+B
			ToggleBlockMode()
		EndIf
		If KeyPressed(33) ; Ctrl+F
			ToggleFillMode()
		EndIf
		If KeyPressed(23) ; Ctrl+I
			ToggleInlineHardMode()
		EndIf
		If KeyPressed(22) ; Ctrl+U
			ToggleInlineSoftMode()
		EndIf
		If KeyPressed(37) ; Ctrl+K
			ToggleOutlineHardMode()
		EndIf
		If KeyPressed(36) ; Ctrl+J
			ToggleOutlineSoftMode()
		EndIf
		
		If KeyPressed(46) ; Ctrl+C
			CopySelectedObjectsToBrush()
		EndIf
	EndIf
	
	; Placed in code before the adjuster page switch button to eat the click before that.
	If NofSelectedObjects<>0 And CurrentGrabbedObjectModified
		; Update button
		If mx>=StartX+44 And Mx<StartX+100 And my>=StartY And my<StartY+20
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				UpdateSelectedObjects()
			EndIf
		EndIf
		If KeyDown(19) ; R key
			UpdateSelectedObjects()
		EndIf
	EndIf
	
	If KeyPressed(20) ; T key
		If CtrlDown() ; Ctrl+T
			UpdateSelectedObjectsIfExists()
			SaveLevel()
			SetBrushMode(BrushModeTestLevel)
		Else
			If GetConfirmation("Give the current object its default TrueMovement values?")
				If RetrieveDefaultTrueMovement()
					SetEditorMode(3)
				Else
					ShowMessage("No default values exist for the current object Type!", 2000)
				EndIf
			EndIf
		EndIf
	EndIf
	
	If KeyPressed(34) ; G key
		For i=0 To NofObjects-1
			TheType=LevelObjects(i)\Attributes\LogicType
			TheSubType=LevelObjects(i)\Attributes\LogicSubType
			If TheType=90 And (TheSubType=10 Or (TheSubType=15 And LevelObjects(i)\Attributes\Data0=7)) ; LevelExit or CMD 7
				If TryLevelGoto(i,BrushCursorX,BrushCursorY,1,2,3)
					Exit
				EndIf
			ElseIf TheType=242 And LevelObjects(i)\Attributes\Data2=7 ; Cuboid with CMD 7
				If TryLevelGoto(i,BrushCursorX,BrushCursorY,3,4,5)
					Exit
				EndIf
			EndIf
		Next
	EndIf
	
	If KeyPressed(33) ; F key
		; Flip brush horizontally
		BrushSpaceStartX=GetBrushSpaceXStart()
		BrushSpaceStartY=GetBrushSpaceYStart()
		BrushSpaceEndX=GetBrushSpaceXEnd(BrushSpaceStartX)
		If EditorMode=0
			For i=0 To BrushSpaceWidth/2-1
				For j=0 To BrushSpaceHeight-1
					X1=BrushSpaceWrapX(BrushSpaceStartX+i)
					Y=BrushSpaceWrapY(BrushSpaceStartY+j)
					X2=BrushSpaceWrapX(BrushSpaceEndX-i)
					
					SwapTiles(BrushTiles(X1,Y),BrushTiles(X2,Y))
				Next
			Next
		ElseIf EditorMode=3
			For k=0 To NofBrushObjects-1
				TheThingy=(BrushWidth+1) Mod 2 ; what the fuck?
				X2=BrushSpaceWrapX(BrushSpaceWidth-TheThingy-BrushObjectTileXOffset(k))
				BrushObjectTileXOffset(k)=X2
			Next
		EndIf
		
		GenerateBrushPreview()
	EndIf
	
	If KeyPressed(47) ; V key
		; Rotate brush 90 degrees
		; TODO: Write this code.
		BrushSpaceStartX=GetBrushSpaceXStart()
		BrushSpaceStartY=GetBrushSpaceYStart()
		BrushSpaceEndX=GetBrushSpaceXEnd(BrushSpaceStartX)
		If EditorMode=0
			For i=0 To BrushSpaceWidth-1
				For j=0 To BrushSpaceHeight-1
					X1=BrushSpaceWrapX(BrushSpaceStartX+i)
					Y=BrushSpaceWrapY(BrushSpaceStartY+j)
					X2=BrushSpaceWrapX(BrushSpaceEndX-i)
					
					SwapTiles(BrushTiles(X1,Y),BrushTiles(X2,Y))
				Next
			Next
		ElseIf EditorMode=3
			For k=0 To NofBrushObjects-1
				TheThingy=(BrushWidth+1) Mod 2 ; what the fuck?
				X2=BrushSpaceWrapX(BrushSpaceWidth-TheThingy-BrushObjectTileXOffset(k))
				BrushObjectTileXOffset(k)=X2
			Next
		EndIf
		
		Temp=BrushSpaceWidth
		BrushSpaceWidth=BrushSpaceHeight
		BrushSpaceHeight=Temp
		
		Temp=BrushWidth
		SetBrushWidth(BrushHeight)
		SetBrushHeight(Temp)
		
		GenerateBrushPreview()
	EndIf
	
	If KeyPressed(15) ; tab key
		If EditorMode=EditorModeTile
			SetEditorMode(EditorModeObject)
		ElseIf EditorMode=EditorModeObject
			SetEditorMode(EditorModeTile)
		EndIf
	EndIf
	
	If HotkeySave()
		SaveLevel()
	ElseIf HotkeyOpen()
		If AskToSaveLevelAndExit()
			OpenTypedLevel()
		EndIf
	EndIf
	
	If CtrlDown()
		If KeyPressed(26) ; Ctrl+[
			TryPopPreviousLevel()
		ElseIf KeyPressed(209) ; Ctrl+PageDown
			If AskToSaveLevelAndExit()
				AccessLevelAtCenter(CurrentLevelNumber+1)
			EndIf
		ElseIf KeyPressed(201) ; Ctrl+PageUp
			If AskToSaveLevelAndExit()
				AccessLevelAtCenter(CurrentLevelNumber-1)
			EndIf
		EndIf
	EndIf
	
	; More button / Page switch button
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

	
	StartX=SidebarX+10
	StartY=SidebarY+460

	
	If mx>=startx And mx<startx+285 And my>=StartY+0 And my<StartY+20	
		If CtrlDown() And LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			Query$=InputString$("Enter category name (or part of the name): ")
			For i=0 To NofObjectPresetCategories-1
				If SubstringMatchesAnywhere(Query$,ObjectPresetCategoryName$(i))
					CurrentObjectPresetCategory=i
					
					CurrentObjectPresetObject=0
					i=CurrentObjectPresetCategory
					
					ReadObjectPresetDirectory(i)
					
					SetEditorMode(3)
					LoadObjectPreset()
					
					Exit
				EndIf
			Next
		EndIf
	
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentObjectPresetCategory=CurrentObjectPresetCategory-1
			If CurrentObjectPresetCategory=-1 Then CurrentObjectPresetCategory=NofObjectPresetCategories-1
			RightMouseReleased=False
			CurrentObjectPresetObject=0
			i=CurrentObjectPresetCategory
			Repeat
				ReadObjectPresetDirectory(i)
				CurrentObjectPresetCategory=i
				i=i-1
				If i=-1 Then i=NofObjectPresetCategories-1

			Until NofObjectPresetObjects>0
			SetEditorMode(3)
			LoadObjectPreset()

		EndIf

		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentObjectPresetCategory=CurrentObjectPresetCategory+1
			If CurrentObjectPresetCategory=NofObjectPresetCategories Then CurrentObjectPresetCategory=0
			LeftMouseReleased=False
			CurrentObjectPresetObject=0
			i=CurrentObjectPresetCategory
			Repeat
				ReadObjectPresetDirectory(i)
				CurrentObjectPresetCategory=i
				i=i+1
				If i=NofObjectPresetCategories Then i=0

			Until NofObjectPresetObjects>0
			SetEditorMode(3)
			LoadObjectPreset()

		EndIf
	EndIf

	
	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If CtrlDown() And LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			Query$=InputString$("Enter object name (or part of the name): ")

			FormerCategory=CurrentObjectPresetCategory
			FormerObject=CurrentObjectPresetObject
			
			; do two passes: first checking from the start, and then checking from anywhere
			
			For i=0 To NofObjectPresetCategories-1
				ReadObjectPresetDirectory(i)
				CurrentObjectPresetCategory=i
				For j=0 To NofObjectPresetObjects-1
					If SubstringMatchesStart(Query$,ObjectPresetObjectName$(j))
						CurrentObjectPresetObject=j
						
						SetEditorMode(3)
						LoadObjectPreset()
						
						Return
					EndIf
				Next
			Next
			
			For i=0 To NofObjectPresetCategories-1
				ReadObjectPresetDirectory(i)
				CurrentObjectPresetCategory=i
				For j=0 To NofObjectPresetObjects-1
					If SubstringMatchesAnywhere(Query$,ObjectPresetObjectName$(j))
						CurrentObjectPresetObject=j
						
						SetEditorMode(3)
						LoadObjectPreset()
						
						Return
					EndIf
				Next
			Next

			CurrentObjectPresetCategory=FormerCategory
			ReadObjectPresetDirectory(CurrentObjectPresetCategory)
			CurrentObjectPresetObject=FormerObject
		EndIf	

		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentObjectPresetObject=CurrentObjectPresetObject-1
			If CurrentObjectPresetObject=-1 Then CurrentObjectPresetObject=NofObjectPresetObjects-1
			RightMouseReleased=False
			
			SetEditorMode(3)
			LoadObjectPreset()
		EndIf

		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentObjectPresetObject=CurrentObjectPresetObject+1
			If CurrentObjectPresetObject=NofObjectPresetObjects Then CurrentObjectPresetObject=0
			LeftMouseReleased=False
			
			SetEditorMode(3)
			LoadObjectPreset()
		EndIf
	EndIf




	
			
	; *************************************
	; load/SAVE/ETC	
	; *************************************
	
	If IsMouseOverToolbarItem(ToolbarBrushModeX,ToolbarBrushModeY-10)
		; brush mode
		BrushChange=0
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			LeftMouseReleased=False

			ChangeBrushModeByDelta(1)
		EndIf
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			RightMouseReleased=False

			ChangeBrushModeByDelta(-1)
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarBrushModeX,ToolbarBrushModeY+20)
		If LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			; wipe ; flip
			
			SetupPrompt()
			ReturnKeyReleased=False
			Print("Type W to wipe, or type X, Y, or XY to flip across the chosen")
			DesiredAction$=Upper$(Input$("axes: "))
			
			If DesiredAction$="W"
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						ChangeLevelTile(i,j,True)
					Next
				Next
				AddUnsavedChange()
			ElseIf DesiredAction$="X"
				FlipLevelX()
				AddUnsavedChange()
			ElseIf DesiredAction="Y"
				FlipLevelY()
				AddUnsavedChange()
			ElseIf DesiredAction="XY"
				FlipLevelXY()
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf
		
	If IsMouseOverToolbarItem(ToolbarBrushModeX,ToolbarBrushModeY+50)
		; dupe mode
		
		NewValue=AdjustInt("Enter dupe mode: ",DupeMode,1,1,10)
		If NewValue<>DupeMode
			SetDupeMode(NewValue)
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarBrushSizeX,ToolbarBrushSizeY) And MouseDebounceFinished()
		; brush size
		If ShiftDown()
			Adj=10
		Else
			Adj=1
		EndIf
		ShouldChangeBrushWidth=my<ToolbarBrushSizeY+15 Or mx<ToolbarBrushSizeX+10
		ShouldChangeBrushHeight=my<ToolbarBrushSizeY+15 Or mx>ToolbarBrushSizeX-10
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			If ShouldChangeBrushWidth
				SetBrushWidth(BrushWidth+Adj)
			EndIf
			If ShouldChangeBrushHeight
				SetBrushHeight(BrushHeight+Adj)
			EndIf
			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			If ShouldChangeBrushWidth
				SetBrushWidth(BrushWidth-Adj)
			EndIf
			If ShouldChangeBrushHeight
				SetBrushHeight(BrushHeight-Adj)
			EndIf
			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
	EndIf
		
	If IsMouseOverToolbarItem(ToolbarTexPrefixX,ToolbarTexPrefixY)
		If LeftMouse=True And LeftMouseReleased=True
			;texture prefix
			FlushKeys
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color 255,255,255
			Print "Enter texture prefix (leave blank to disable texture prefix): "
			TexturePrefix$=Input$("")
			ReturnKeyReleased=False
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarShowMarkersX,ToolbarShowMarkersY) And MouseDebounceFinished()
		; show/hide markers
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
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
			
			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarShowObjectsX,ToolbarShowObjectsY)
		; show/hide objects
		NewValue=AdjustInt("Enter object mesh visibility level: ", ShowObjectMesh, 1, 10, DelayTime)
		If NewValue>ShowObjectMeshMax
			NewValue=0
		ElseIf NewValue<0
			NewValue=ShowObjectMeshMax
		EndIf
		WasChanged=ShowObjectMesh<>NewValue
		If WasChanged
			ShowObjectMesh=NewValue
			For j=0 To NofObjects-1
				UpdateObjectVisibility(LevelObjects(j))
			Next
		EndIf
	EndIf
	
	
	If IsMouseOverToolbarItem(ToolbarShowLogicX,ToolbarShowLogicY) And MouseDebounceFinished()
		; show/hide logic
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
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
			
			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarShowLevelX,ToolbarShowLevelY) And MouseDebounceFinished()
 		 ; show/hide level
		NewValue=AdjustInt("Enter level mesh visibility level: ", ShowLevelMesh, 1, 10, DelayTime)
		If NewValue>ShowLevelMeshMax
			NewValue=0
		ElseIf NewValue<0
			NewValue=ShowLevelMeshMax
		EndIf
		WasChanged=ShowLevelMesh<>NewValue
		If WasChanged
			ShowLevelMesh=NewValue
			If ShowLevelMesh=ShowLevelMeshShow
				For j=0 To LevelHeight-1
					ShowEntity LevelMesh(j)
					EntityAlpha LevelMesh(j),1.0
				Next
			ElseIf ShowLevelMesh=ShowLevelMeshHide
				For j=0 To LevelHeight-1
					HideEntity LevelMesh(j)
				Next
			ElseIf ShowLevelMesh=ShowLevelMeshTransparent
				For j=0 To LevelHeight-1
					ShowEntity LevelMesh(j)
					EntityAlpha LevelMesh(j),0.5
				Next
			EndIf
						
			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarSimulationLevelX,ToolbarSimulationLevelY)
		; simulation level
		NewValue=AdjustInt("Enter Simulation Level: ", SimulationLevel, 1, 10, DelayTime)
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
				
				UpdateObjectVisibility(LevelObjects(i))
				UpdateObjectAnimation(LevelObjects(i))
			Next
			
			If SimulationLevel>=3
				For j=0 To LevelHeight-1
					RecalculateNormals(j)
				Next
			EndIf
			
			ResetSounds()
			UpdateMusic()
			
			LightingWasChanged()
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarBrushWrapX,ToolbarBrushWrapY)
		; brush wrap
		BrushWrap=AdjustInt("Enter Brush Wrap: ", BrushWrap, 1, 10, DelayTime)
		If BrushWrap>BrushWrapMax
			BrushWrap=0
		ElseIf BrushWrap<0
			BrushWrap=BrushWrapMax
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarStepPerX,ToolbarStepPerY)
		; step per
		StepPer=AdjustInt("Enter Step Per: ", StepPer, 1, 10, DelayTime)
		If StepPer>StepPerMax
			StepPer=0
		ElseIf StepPer<0
			StepPer=StepPerMax
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarElevateX,ToolbarElevateY)
		; elevate
		If LeftMouse=True And LeftMouseReleased=True
			SetupPrompt()
			Print("Amount to shift level up/down (or type X to skip to Xtrude")
			Amount$=Input$("Logics): ")
			ReturnKeyReleased=False
			Adjustment#=Amount
			If Amount$="x" Or Amount$="X"
				XtrudeLogics()
				
				AddUnsavedChange()
			ElseIf Adjustment#<>0.0
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						LevelTiles(i,j)\Terrain\Extrusion=LevelTiles(i,j)\Terrain\Extrusion+Adjustment
						LevelTiles(i,j)\Water\Height=LevelTiles(i,j)\Water\Height+Adjustment
						;UpdateLevelTile(i,j)
						;UpdateWaterTile(i,j)
					Next
				Next
				;ReBuildLevelModel()
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						UpdateTile(i,j)
					Next
				Next
				;SomeTileWasChanged()
				For i=0 To NofObjects-1
					LevelObjects(i)\Attributes\ZAdjust=LevelObjects(i)\Attributes\ZAdjust+Adjustment
					UpdateObjectPosition(i)
				Next
				CurrentObject\Attributes\ZAdjust=CurrentObject\Attributes\ZAdjust+Adjustment
				BuildCurrentObjectModel()
				SomeObjectWasChanged()
				
				If GetConfirmation("Do you want to set Xtrude logics?")
					XtrudeLogics()
				EndIf
				
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarIDFilterX,ToolbarIDFilterY) And MouseDebounceFinished()
		; id filter
		If LeftMouse=True And LeftMouseReleased=True
			If IDFilterEnabled=False Or CtrlDown()
				IDFilterAllow=InputInt("Enter the ID to filter for: ")
				IDFilterEnabled=True
			Else
				IDFilterEnabled=False
				IDFilterInverted=False
			EndIf
			For j=0 To NofObjects-1
				UpdateObjectVisibility(LevelObjects(j))
			Next
			MouseDebounceSet(10)
		EndIf
		If IDFilterEnabled
			If MouseScroll<>0
				If ShiftDown()
					Adj=50
				Else
					Adj=1
				EndIf
				IDFilterAllow=IDFilterAllow+Adj*MouseScroll
				For j=0 To NofObjects-1
					UpdateObjectVisibility(LevelObjects(j))
				Next
			EndIf
			If RightMouse=True And RightMouseReleased=True
				IDFilterInverted=Not IDFilterInverted
				For j=0 To NofObjects-1
					UpdateObjectVisibility(LevelObjects(j))
				Next
				MouseDebounceSet(10)
			EndIf
		EndIf
	EndIf
	
	If IsMouseOverToolbarItem(ToolbarDensityX,ToolbarDensityY)
		; placement density
		Value#=AdjustFloat#("Enter placement density (0.0 to 1.0): ",PlacementDensity#,0.05,0.2,DelayTime)
		SetPlacementDensity(Value#)
	EndIf
	
	If LeftMouse=True And LeftMouseReleased=True
		If IsMouseOverToolbarItem(ToolbarExitX,ToolbarExitY)
			; exit ; cancel and exit
			If AskToSaveLevelAndExit()
				ResumeMaster()
			EndIf
			
			Repeat
			Until MouseDown(1)=False
		EndIf
		
		If IsMouseOverToolbarItem(ToolbarSaveX,ToolbarSaveY)
			; save ; save and exit
			;If SaveLevelAndExit()
			;	ResumeMaster()
			;EndIf
			If UnsavedChanges
				SaveLevel()
			Else
				If BrushMode=BrushModeTestLevel
					SetBrushMode(BrushModeNormal)
				Else
					SetBrushMode(BrushModeTestLevel)
				EndIf
			EndIf
			
			Repeat
			Until MouseDown(1)=False
		
		EndIf
	EndIf
	
End Function


; Returns True if the user chose to save.
Function SaveLevelAndExit()

	If CurrentObjectCanBeUpdated()
		FlushKeys
		SetupWarning()
		Print("You have not hit the Update button on the selected object.")
		Print("Type R to update the object and save and exit.")
		Confirm$=Upper$(Input$("Type E to save and exit without updating: "))
		ReturnKeyReleased=False
		If Confirm="E"
			SaveLevel()
			Return True
		ElseIf Confirm="R"
			UpdateSelectedObjects()
			SaveLevel()
			Return True
		Else
			Return False
		EndIf
	Else
		SaveLevel()
		Return True
	EndIf

End Function


; Returns True if the user chooses to proceed, with or without saving.
Function AskToSaveLevelAndExit()
	
	If UnsavedChanges
		FlushKeys
		SetupWarning()
		Print("This level has unsaved changes. Type R to save and exit.")
		Typed$=Upper$(Input$("Type E to exit without saving: "))
		ReturnKeyReleased=False
		If Typed$="R"
			If SaveLevelAndExit()
				Return True
			Else
				Return False
			EndIf
		ElseIf Typed$="E"
			Return True
		Else
			Return False
		EndIf
	ElseIf CurrentObjectCanBeUpdated()
		FlushKeys
		SetupWarning()
		Print("You have not hit the Update button on the selected object.")
		Print("Type R to update the object and save and exit.")
		Confirm$=Upper$(Input$("Type E to exit without updating: "))
		ReturnKeyReleased=False
		If Confirm="E"
			Return True
		ElseIf Confirm="R"
			UpdateSelectedObjects()
			SaveLevel()
			Return True
		Else
			Return False
		EndIf
	Else
		Return True
	EndIf

End Function


Function SaveDialogAndExit()

	SaveDialogFile()
	ClearDialogFile()
	ResumeMaster()

End Function


; Returns True if the user chooses to proceed, with or without saving.
Function AskToSaveDialogAndExit()
	
	If UnsavedChanges
		FlushKeys
		SetupWarning()
		Print("This dialog has unsaved changes. Type R to save and exit.")
		Typed$=Upper$(Input$("Type E to exit without saving: "))
		ReturnKeyReleased=False
		If Typed$="R"
			SaveDialogFile()
			Return True
		ElseIf Typed$="E"
			Return True
		Else
			Return False
		EndIf
	Else
		Return True
	EndIf
	
End Function


Function CurrentObjectCanBeUpdated()

	Return NofSelectedObjects<>0 And CurrentGrabbedObjectModified

End Function


Function PositionIsEqual(x1,y1,x2,y2)

	Return x1=x2 And y1=y2

End Function


Function GetBrushSpaceXStart()

	; The parenthesis are necessary because otherwise BASIC will evaluate this differently for no reason. Great!
	Return -(BrushSpaceWidth/2)

End Function

Function GetBrushSpaceYStart()

	Return -(BrushSpaceHeight/2)

End Function

Function GetBrushSpaceXEnd(BrushXStart)

	Return BrushXStart+BrushSpaceWidth-1

End Function

Function GetBrushSpaceYEnd(BrushYStart)

	Return BrushYStart+BrushSpaceHeight-1

End Function

Function GetBrushXStart()

	Return BrushCursorX-BrushWidth/2

End Function

Function GetBrushYStart()

	Return BrushCursorY-BrushHeight/2

End Function

Function GetBrushXEnd(BrushXStart)

	Return BrushXStart+BrushWidth-1

End Function

Function GetBrushYEnd(BrushYStart)

	Return BrushYStart+BrushHeight-1

End Function

Function BrushSpaceWrapX(X)

	Return EuclideanRemainderInt(X,BrushSpaceWidth)

End Function

Function BrushSpaceWrapY(Y)

	Return EuclideanRemainderInt(Y,BrushSpaceHeight)

End Function

Function LevelSpaceToBrushSpaceX(X,WrapMode)

	Select WrapMode
	Case BrushWrapModulus
		Return BrushSpaceWrapX(X-BrushSpaceOriginX)
	Case BrushWrapRelative
		Return BrushSpaceWrapX(X-BrushCursorX)
	Case BrushWrapRandom
		Return Rand(0,BrushSpaceWidth-1)
	End Select

End Function

Function LevelSpaceToBrushSpaceY(Y,WrapMode)

	Select WrapMode
	Case BrushWrapModulus
		Return BrushSpaceWrapY(Y-BrushSpaceOriginY)
	Case BrushWrapRelative
		Return BrushSpaceWrapY(Y-BrushCursorY)
	Case BrushWrapRandom
		Return Rand(0,BrushSpaceHeight-1)
	End Select

End Function


Function XtrudeLogics()

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

End Function


Function ToggleBlockMode()

	If IsBrushInBlockMode()
		SetBrushMode(BrushModeNormal)
	Else
		SetBrushMode(BrushModeBlock)
	EndIf
	
End Function


Function ToggleFillMode()

	ToggleFromNormalBrush(BrushModeFill)

End Function

Function ToggleInlineSoftMode()

	ToggleFromNormalBrush(BrushModeInlineSoft)
	
End Function

Function ToggleInlineHardMode()

	ToggleFromNormalBrush(BrushModeInlineHard)
	
End Function

Function ToggleOutlineSoftMode()

	ToggleFromNormalBrush(BrushModeOutlineSoft)
	
End Function

Function ToggleOutlineHardMode()

	ToggleFromNormalBrush(BrushModeOutlineHard)
	
End Function

Function ToggleFromNormalBrush(TargetBrushMode)

	If BrushMode=TargetBrushMode
		SetBrushMode(BrushModeNormal)
	Else
		SetBrushMode(TargetBrushMode)
	EndIf

End Function

Function SetPlacementDensity(Value#)

	PlacementDensity#=Value#
	If PlacementDensity#<0.0
		PlacementDensity#=0.0
	ElseIf PlacementDensity#>1.0
		PlacementDensity#=1.0
	EndIf
	PlacementDensity#=ZeroRoundFloat#(PlacementDensity#)

End Function


Function ReadObjectPresetDirectory(index)

	NofObjectPresetObjects=0
	Dir=ReadDir("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(index))
	file$=NextFile$(Dir)
	While file$<>""
		If file$<>"." And file$<>".." And FileType("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(index)+"\"+file$)=1 And Lower$(Right$(file$,4))=".wop"
			ObjectPresetObjectName$(NofObjectPresetObjects)=file$
			NofObjectPresetObjects=NofObjectPresetObjects+1
		EndIf
		file$=NextFile$(Dir)
	Wend
	CloseDir dir

End Function


Function AddOrToggleSelectObject(i)
	
	If CtrlDown() And (Not ShiftDown())
		ToggleSelectObject(i)
	Else
		AddSelectObject(i)
	EndIf

End Function

Function ShowSelectedObjectMarker(i)

	ShowEntity CurrentGrabbedObjectMarkers(i)
	UpdateCurrentGrabbedObjectMarkerPosition(i)

End Function

Function HideSelectedObjectMarker(i)

	HideEntity CurrentGrabbedObjectMarkers(i)

End Function

Function UpdateSelectedObjectMarkerVisibility(i)
	
	If IsObjectSelected(i) And EditorMode=3
		ShowSelectedObjectMarker(i)
	Else
		HideSelectedObjectMarker(i)
	EndIf
	
End Function

Function UpdateAllSelectedObjectMarkersVisibility()

	For i=0 To MaxNofObjects-1
		UpdateSelectedObjectMarkerVisibility(i)
	Next

End Function


; these could potentially be made smarter
Function SubstringMatchesStart(Query$,Subject$)

	; make case insensitive
	Query$=Upper$(Query$)
	Subject$=Upper$(Subject$)
	
	; truncate subject to be length of query
	Subject$=Left$(Subject$,Len(Query$))
	
	Return Query$=Subject$

End Function

Function SubstringMatchesAnywhere(Query$,Subject$)

	; make case insensitive
	Query$=Upper$(Query$)
	Subject$=Upper$(Subject$)
	
	Return Instr(Subject$,Query$)<>0

End Function


Function CtrlDown()

	Return KeyDown(29) Or KeyDown(157) ; left ctrl or right ctrl
	
End Function

Function ShiftDown()

	Return KeyDown(42) Or KeyDown(54) ; left shift or right shift

End Function

; DO NOT USE!!! Alt-tabbing out of the application will make it think alt is still being pressed when you return.
;Function AltDown()
;
;	Return KeyDown(56) Or KeyDown(184) ; left alt or right alt
;
;End Function

Function FindAndReplaceKeyDown()

	Return KeyDown(41) ; tilde key

End Function

Function HotkeySave()

	Return CtrlDown() And KeyPressed(31) ; Ctrl+S

End Function

Function HotkeyOpen()

	Return CtrlDown() And KeyPressed(24) ; Ctrl+O

End Function

Function SetupWarning()

	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color TextWarningR,TextWarningG,TextWarningB

End Function

Function GetConfirmation(Message$)

	FlushKeys
	SetupWarning()
	Print Message$
	Confirm$=Input$("Type Y to confirm: ")
	ReturnKeyReleased=False
	Return Confirm="Y" Or Confirm="y"

End Function

Function MaybeGetResizeConfirmation(Adj)

	If NofObjects<>0 And Adj>1
		Return GetConfirmation("Are you sure you want to reduce the level size by "+Adj+"?")
	Else
		Return True
	EndIf

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

	Return LevelTiles(i,j)\Terrain\Logic>0 And LevelTiles(i,j)\Terrain\Logic<15

End Function


Function RemoveEntityTexture(Entity)

	NewEntity=CopyMesh(Entity)
	FreeEntity(Entity)
	Return NewEntity

End Function


Function ObstacleNameToObstacleId(ModelName$)

	FirstDigit=Asc(Mid$(ModelName$,10,1))-48
	SecondDigit=Asc(Mid$(ModelName$,11,1))-48
	Return FirstDigit*10+SecondDigit

End Function

Function TryGetObstacleMesh(ObstacleId)

	If ObstacleId>0 And ObstacleId<>14 And ObstacleId<>49 And ObstacleId<63
		Return CopyEntity(ObstacleMesh(ObstacleId))
	Else
		Return CreateErrorMesh()
	EndIf

End Function

Function TryUseObstacleTexture(Entity,ObstacleId)

	If ObstacleId>0 And ObstacleId<>10 And ObstacleId<>14 And (ObstacleId<36 Or ObstacleId>42) And ObstacleId<>49 And ObstacleId<63
		EntityTexture Entity,ObstacleTexture(ObstacleId)
	Else
		Entity=RemoveEntityTexture(Entity)
		
		UseErrorColor(Entity)
	EndIf
	
	Return Entity

End Function


Function ReplyFunctionToName$(Fnc)

	Select Fnc
	Case 1
		Return "End Dialog"
	Case 2
		Return "Continue Dialog"
	Case 3
		Return "Open AskAbout"
	Case 4
		Return "Consume Coins (+1 Interchange if player doesn't have enough, +2 otherwise)"
	Case 5
		Return "Consume Item (+1 Interchange if player doesn't have item, +2 otherwise)"
	Case 6
		Return "Check For Item (+1 Interchange if player doesn't have item, +2 otherwise)"
	Default
		Return "Do Nothing"
	End Select

End Function

Function ReplyFunctionToDataName$(Fnc)

	Select Fnc
	Case 1,2
		Return "Destination Interchange"
	Case 4
		Return "Number of Coins"
	Case 5
		Return "Item FNC ID"
	Case 6
		Return "Item FNC ID"
	Default
		Return "N/A"
	End Select

End Function

Function GetAskAboutActiveName$(Active)

	Select Active
	Case -2
		Return "Active"
	Case -1
		Return "Inactive (must be activated with CMD 23, 25, or 26)"
	Default
		If Active>-1
			Return "Active if MasterAskAbout "+Active+" is active (modify with CMD 28, 29, or 30)"
		Else
			Return "???"
		EndIf
	End Select

End Function

Function GetAskAboutActiveNameShort$(Active)

	Select Active
	Case -2
		Return "Active"
	Case -1
		Return "Inactive"
	Default
		If Active>-1
			Return "MasterAA "+Active
		Else
			Return Active
		EndIf
	End Select

End Function

Function GetAskAboutRepeatName$(Value)

	If Value<0
		Return "Can be used infinitely"
	ElseIf Value=0
		Return "Never available"
	Else
		Return "Can be used "+Value+" "+MaybePluralize$("time",Value)
	EndIf

End Function

Function ObjectTypeCollisionBitToName$(BitIndex)

	Select BitIndex
	Case 1
		Return "Player"
	Case 2
		Return "Talkable NPCs and Signs"
	Case 3
		Return "Wee Stinkers and Baby Boomers"
	Case 4
		Return "Items"
	Case 5
		Return "Scritters and Tentacles"
	Case 6
		Return "Sunken Turtles and (if TTC has Bridge) Transporters"
	Case 7
		Return "Untalkable NPCs, Signs, Unsunken Turtles, FireFlowers, Ducks, Moobots, Burstflowers"
	Case 8
		Return "Chompers, Spikeyballs, Ghosts, and Retro Monsters"
	Case 9
		Return "Barrels, Cuboids, Boxes, and GrowFlowers"
	Case 10
		Return "Frozen Objects"
	Default
		Return "???"
	End Select

End Function

Function BitPositionIndexToBitIndex(BitPositionIndex)

	BitIndex=BitPositionIndex
	If BitIndex>10
		BitIndex=BitIndex-1
	EndIf
	If BitIndex>5
		BitIndex=BitIndex-1
	EndIf
	Return BitIndex

End Function

Function BitIndexIsValid(BitIndex)

	Return BitIndex>-1 And BitIndex<15

End Function

Function BitPositionIndexIsValid(BitPositionIndex)

	Return BitPositionIndex<>5 And BitPositionIndex<>11

End Function

Function GetBitPositionIndex(BitStartX)

	Return (MouseX()-BitStartX)/8

End Function



Function ColorLevelTileLogic(i,j)

	Select LevelTiles(i,j)\Terrain\Logic
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



Function ReBuildLevelModel()

	For i=0 To MaxLevelCoordinate
		FreeEntity LevelMesh(i)
		FreeEntity WaterMesh(i)
		FreeEntity LogicMesh(i)
	Next

	BuildLevelModel()
	
End Function



Function CreateLevelTileTop(i,j)
	mySurface=LevelSurface(j)

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

			CalculateUV(LevelTiles(i,j)\Terrain\Texture,i2,j2,LevelTiles(i,j)\Terrain\Rotation,8,LevelDetail)
								
			
			AddVertex(mySurface,i+Float(i2)/Float(LevelDetail)+xoverlap,height+zoverlap,-(j+Float(j2)/Float(LevelDetail))+yoverlap,ChunkTileu,ChunkTilev)
		Next
	Next
	; Now create the triangles
	For j2=0 To LevelDetail-1
		For i2=0 To LevelDetail-1
			AddTriangle (mySurface,GetLevelVertex(i,j,i2,j2),GetLevelVertex(i,j,i2+1,j2),GetLevelVertex(i,j,i2,j2+1))
			AddTriangle (mySurface,GetLevelVertex(i,j,i2+1,j2),GetLevelVertex(i,j,i2+1,j2+1),GetLevelVertex(i,j,i2,j2+1))
		Next
	Next
	
	ShiftLevelTileByRandom(i,j)
	ShiftLevelTileByHeight(i,j)
	ShiftLevelTileByExtrude(i,j)
	ShiftLevelTileEdges(i,j)

End Function

Function CreateLevelTileSides(i,j)
	mySurface=LevelSurface(j)

	; here we also calculate how much the bottom edge of the side wall should be pushed "out"
	; the maxfactor is the maximum (corners are not pushed out)
	If LevelTiles(i,j)\Terrain\EdgeRandom=1
		randommax#=0.2
	Else
		randommax#=0.0
	EndIf
	
	overhang#=0.0
	
	; north side
	random#=0 ; this is the random for the lower edge - set to zero and only caclulate for the second pixel,
				; that way, the first pixel of the next square will have the same random factor
	If j>0
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,j-1)\Terrain\Extrusion 
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For i2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)-overhang,ChunkTileU,ChunkTileV)
				
				vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)-overhang,ChunkTileU,ChunkTileV)
	
				vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+Float(LevelDetail-i2)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j-1)\Terrain\Extrusion,-j+random,ChunkTileU,ChunkTileV)
				
				vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
				If i2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf
	
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+Float(LevelDetail-i2-1)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j-1)\Terrain\Extrusion,-j+random,ChunkTileU,ChunkTileV)
				
				AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
				AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)
				
				currentvertex=currentvertex+4
			Next
		;EndIf
	EndIf

	; east side
	random#=0
	If i<LevelWidth-1
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i+1,j)\Terrain\Extrusion 
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For j2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)
	
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)
	
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+1+random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i+1,j)\Terrain\Extrusion,-(j+Float(LevelDetail-j2)/Float(LevelDetail)),ChunkTileU,ChunkTileV)
	
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
				If j2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf
	
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+1+random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i+1,j)\Terrain\Extrusion,-(j+Float(LevelDetail-j2-1)/Float(LevelDetail)),ChunkTileU,ChunkTileV)
				
				AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
				AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)
				
				currentvertex=currentvertex+4
			Next
		;EndIf
	EndIf

	

			
	; south side
	random#=0
	If j<LevelHeight-1
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,j+1)\Terrain\Extrusion 
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For i2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,i2,LevelDetail)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)+overhang,ChunkTileU,ChunkTileV)
				vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)+overhang,ChunkTileU,ChunkTileV)
	
				vertex=GetLevelVertex(i,j,i2,LevelDetail)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+Float(i2)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j+1)\Terrain\Extrusion,-(j+1+random),ChunkTileU,ChunkTileV)
				
				vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
				If i2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf
	
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+Float(i2+1)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j+1)\Terrain\Extrusion,-(j+1+random),ChunkTileU,ChunkTileV)
				
				AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
				AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)
				
				currentvertex=currentvertex+4
			Next
		;EndIf
	EndIf
	
	; west side
	random#=0
	If i>0
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i-1,j)\Terrain\Extrusion 
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For j2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,0,j2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)
	
				vertex=GetLevelVertex(i,j,0,j2+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)
	
				vertex=GetLevelVertex(i,j,0,j2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i-random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i-1,j)\Terrain\Extrusion,-(j+Float(j2)/Float(LevelDetail)),ChunkTileU,ChunkTileV)
				
				vertex=GetLevelVertex(i,j,0,j2+1)
				If j2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf
	
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i-random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i-1,j)\Terrain\Extrusion,-(j+Float(j2+1)/Float(LevelDetail)),ChunkTileU,ChunkTileV)
				
				AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
				AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)
				
				currentvertex=currentvertex+4
			Next
		;EndIf
	EndIf


End Function

Const SideNotInLevelZ=-10000 ;-100

Function UpdateLevelTileSides(i,j)
	mySurface=LevelSurface(j)
	
	; here we also calculate how much the bottom edge of the side wall should be pushed "out"
	; the maxfactor is the maximum (corners are not pushed out)
	If LevelTiles(i,j)\Terrain\EdgeRandom=1
		randommax#=0.2
	Else
		randommax#=0.0
	EndIf
	
	overhang#=0.0
	
	CurrentIndex=0

	; north side
	random#=0 ; this is the random for the lower edge - set to zero and only caclulate for the second pixel,
				; that way, the first pixel of the next square will have the same random factor
	If j>0
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,j-1)\Terrain\Extrusion
			z2=0
			If LevelTiles(i,j-1)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion z2=SideNotInLevelZ
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For i2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail-i2,0)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)-overhang
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail-i2-1,0)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)-overhang
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
	
				vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail-i2,0)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+Float(LevelDetail-i2)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j-1)\Terrain\Extrusion+z2,-j+random
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail-i2-1,0)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+3)
				If i2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf
	
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+Float(LevelDetail-i2-1)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j-1)\Terrain\Extrusion+z2,-j+random
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				CurrentIndex=CurrentIndex+4
			Next
		;EndIf
	EndIf

	; east side
	random#=0
	If i<LevelWidth-1
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i+1,j)\Terrain\Extrusion
			z2=0
			If LevelTiles(i+1,j)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion z2=SideNotInLevelZ
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For j2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail,LevelDetail-j2)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
	
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail,LevelDetail-j2-1)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
	
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail,LevelDetail-j2)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+1+random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i+1,j)\Terrain\Extrusion+z2,-(j+Float(LevelDetail-j2)/Float(LevelDetail))
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
	
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail,LevelDetail-j2-1)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+3)
				If j2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf
	
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+1+random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i+1,j)\Terrain\Extrusion+z2,-(j+Float(LevelDetail-j2-1)/Float(LevelDetail))
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				CurrentIndex=CurrentIndex+4
			Next
		;EndIf
	EndIf

	

			
	; south side
	random#=0
	If j<LevelHeight-1
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,j+1)\Terrain\Extrusion
			z2=0
			If LevelTiles(i,j+1)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion z2=SideNotInLevelZ
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For i2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,i2,LevelDetail)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)+overhang
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)+overhang
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
	
				vertex=GetLevelVertex(i,j,i2,LevelDetail)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+Float(i2)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j+1)\Terrain\Extrusion+z2,-(j+1+random)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+3)
				If i2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf
	
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+Float(i2+1)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j+1)\Terrain\Extrusion+z2,-(j+1+random)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				CurrentIndex=CurrentIndex+4
			Next
		;Else
		;	For i2=0 To LevelDetail-1
		;		vertex=GetLevelVertex(i,j,i2,LevelDetail)
		;		vertexside=GetLevelSideVertex(i,j,CurrentIndex)
		;		CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8)
		;		VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)
		;		VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
		;	Next
		;EndIf
	EndIf
	
	; west side
	random#=0
	If i>0
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i-1,j)\Terrain\Extrusion
			z2=0
			If LevelTiles(i-1,j)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion z2=SideNotInLevelZ
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For j2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,0,j2)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				vertex=GetLevelVertex(i,j,0,j2+1)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
	
				vertex=GetLevelVertex(i,j,0,j2)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i-random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i-1,j)\Terrain\Extrusion+z2,-(j+Float(j2)/Float(LevelDetail))
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				vertex=GetLevelVertex(i,j,0,j2+1)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+3)
				If j2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf
	
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i-random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i-1,j)\Terrain\Extrusion+z2,-(j+Float(j2+1)/Float(LevelDetail))
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
				
				CurrentIndex=CurrentIndex+4
			Next
		;EndIf
	EndIf

End Function

Function ClampInt(min,max,value)

	If value<min
		Return min
	ElseIf value>max
		Return max
	Else
		Return value
	EndIf	

End Function

Function ClampToLevelWidth(value)

	Return ClampInt(0,LevelWidth-1,value)

End Function

Function ClampToLevelHeight(value)

	Return ClampInt(0,LevelHeight-1,value)

End Function

Function ShiftLevelTileByRandom(i,j)
	mySurface=LevelSurface(j)
	
	iMinusOne=Maximum2(i-1,0)
	iPlusOne=Minimum2(i+1,LevelWidth-1)
	jMinusOne=Maximum2(j-1,0)
	jPlusOne=Minimum2(j+1,LevelHeight-1)
	

	For i2=0 To LevelDetail
		For j2=0 To LevelDetail
			If i2=0 And j2=0
				random#=Minimum4(LevelTiles(IMinusOne,jMinusOne)\Terrain\Random,LevelTiles(i,jMinusOne)\Terrain\Random,LevelTiles(iMinusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If j2=0 And i2=LevelDetail
				random#=Minimum4(LevelTiles(IPlusOne,jMinusOne)\Terrain\Random,LevelTiles(i,jMinusOne)\Terrain\Random,LevelTiles(IPlusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If j2=LevelDetail And i2=0
				random#=Minimum4(LevelTiles(IMinusOne,jPlusOne)\Terrain\Random,LevelTiles(iMinusOne,j)\Terrain\Random,LevelTiles(i,jPlusOne)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If i2=LevelDetail And j2=LevelDetail
				random#=Minimum4(LevelTiles(IPlusOne,jPlusOne)\Terrain\Random,LevelTiles(i,jPlusOne)\Terrain\Random,LevelTiles(IPlusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If j2=0
				random#=Minimum2(LevelTiles(i,jMinusOne)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If j2=LevelDetail
				random#=Minimum2(LevelTiles(i,jPlusOne)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If i2=0
				random#=Minimum2(LevelTiles(IMinusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If i2=LevelDetail
				random#=Minimum2(LevelTiles(IPlusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else
				random#=LevelTiles(i,j)\Terrain\Random
			EndIf
			
			vertex=GetLevelVertex(i,j,i2,j2)
			random2#=random*LevelVertexRandom(Float(i2),Float(j2))

			VertexCoords mySurface,vertex,VertexX(mysurface,vertex),VertexY(mysurface,vertex)+random2,VertexZ(mysurface,vertex)					
		
		Next
	Next

End Function

Function HeightAtRowVertex#(i,j,i2)

	If i2<LevelDetail/2
		; first half of tile, compare with left neighbour
		If i=0 
			OtherHeight#=LevelTiles(i,j)\Terrain\Height ;0.0
		Else
			OtherHeight#=LevelTiles(i-1,j)\Terrain\Height
		EndIf
		Return OtherHeight+(LevelTiles(i,j)\Terrain\Height-OtherHeight)*Float(i2+Float(LevelDetail)/2.0)/Float(LevelDetail)
	Else
		; second half of tile, compare with right neighbour
		If i=LevelWidth-1 
			OtherHeight#=LevelTiles(i,j)\Terrain\Height ;0.0
		Else
			OtherHeight#=LevelTiles(i+1,j)\Terrain\Height
		EndIf
		Return LevelTiles(i,j)\Terrain\Height+(OtherHeight-LevelTiles(i,j)\Terrain\Height)*Float(i2-LevelDetail/2)/Float(LevelDetail)
		
	EndIf

End Function

Function ShiftLevelTileByHeight(i,j)
;	If LevelDetail<2 Or Floor(LevelDetail/2)*2<>LevelDetail
;		; must be divisible by two, or disable height function
;		Return
;	EndIf

	mySurface=LevelSurface(j)

	For i2=0 To LevelDetail

		NewHeight#=HeightAtRowVertex#(i,j,i2)

		vertex=GetLevelVertex(i,j,i2,LevelDetail/2)
		VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+NewHeight,VertexZ(mySurface,vertex)

		If j=LevelHeight-1
			OtherHeight#=LevelTiles(i,j)\Terrain\Height
		Else
			OtherHeight#=HeightAtRowVertex#(i,j+1,i2)
		EndIf
			
		; as of second row, build vertical bridge to first row
		For j2=LevelDetail/2+1 To LevelDetail
			; first half is actually 2nd half of previous row
			; (also no need to lift first vertex of that part, that's already the center of
			;  the row and hence lifted above)
			ThisVertexesHeight#=NewHeight#+(OtherHeight-NewHeight)*Float(j2-LevelDetail/2)/Float(LevelDetail)
			If i>=0 And j<=LevelHeight-1 And i<=LevelWidth-1
				vertex=GetLevelVertex(i,j,i2,j2)
				VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+ThisVertexesHeight,VertexZ(mySurface,vertex)
			EndIf
		Next
		
		If j=0
			OtherHeight#=LevelTiles(i,j)\Terrain\Height
		Else
			OtherHeight#=HeightAtRowVertex#(i,j-1,i2)
		EndIf
		
		For j2=0 To LevelDetail/2-1
			; 2nd half (we're now in the top half of this row)
			ThisVertexesHeight#=OtherHeight#+(NewHeight-OtherHeight)*Float(j2+LevelDetail/2)/Float(LevelDetail)
			vertex=GetLevelVertex(i,j,i2,j2)
			VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+ThisVertexesHeight,VertexZ(mySurface,vertex)
		Next

	Next
	
End Function

Function ShiftLevelTileByExtrude(i,j)
	mySurface=LevelSurface(j)

	For j2=0 To LevelDetail
		For i2=0 To LevelDetail
			vertex=GetLevelVertex(i,j,i2,j2)
			VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+LevelTiles(i,j)\Terrain\Extrusion,VertexZ(mySurface,vertex)
			;VertexCoords mySurface,vertex,VertexX(mySurface,vertex),LevelTiles(i,j)\Terrain\Extrusion,VertexZ(mySurface,vertex)
		Next
	Next

End Function

Function ResetLevelTile(i,j)
	mySurface=LevelSurface(j)

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

			;CalculateUV(LevelTiles(i,j)\Terrain\Texture,i2,j2,LevelTiles(i,j)\Terrain\Rotation,8)
								
			vertex=GetLevelVertex(i,j,i2,j2)
			VertexCoords mySurface,vertex,i+Float(i2)/Float(LevelDetail)+xoverlap,height+zoverlap,-(j+Float(j2)/Float(LevelDetail))+yoverlap
		Next
	Next

End Function

Function ShiftLevelTileEdges(i,j)
	mySurface=LevelSurface(j)

	iMinusOne=Maximum2(i-1,0)
	iPlusOne=Minimum2(i+1,LevelWidth-1)
	jMinusOne=Maximum2(j-1,0)
	jPlusOne=Minimum2(j+1,LevelHeight-1)

	If LevelTiles(i,j)\Terrain\Rounding=1		
	
			
		; is there a drop-off NE corner:
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iPlusOne,j)\Terrain\Extrusion And LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jMinusOne)\Terrain\Extrusion And LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
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
										
					VertexCoords mySurface,vertex,i+0.5+x#/2.0,VertexY(mySurface,vertex),-(j+0.5-y#/2.0)
				Next
			Next
			
		EndIf

			
		; is there a drop-off SE corner:
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iPlusOne,j)\Terrain\Extrusion And LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jPlusOne)\Terrain\Extrusion And LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
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
										
					VertexCoords mySurface,vertex,i+0.5+x#/2.0,VertexY(mySurface,vertex),-(j+0.5+y#/2.0)
				Next
			Next
			
		EndIf
		; SW corner
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iMinusOne,j)\Terrain\Extrusion And LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jPlusOne)\Terrain\Extrusion And LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
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
										
					VertexCoords mySurface,vertex,i+0.5-x#/2.0,VertexY(mySurface,vertex),-(j+0.5+y#/2.0)
				Next
			Next
			
		EndIf
		
		; is there a drop-off NW corner:
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iMinusOne,j)\Terrain\Extrusion And LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jMinusOne)\Terrain\Extrusion And LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
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
										
					VertexCoords mySurface,vertex,i+0.5-x#/2.0,VertexY(mySurface,vertex),-(j+0.5-y#/2.0)
				Next
			Next
			
		EndIf
	
	EndIf
	
	randommax#=0.1

	If LevelTiles(i,j)\Terrain\EdgeRandom=1
		; north side
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jMinusOne)\Terrain\Extrusion
			
			j2=0
			For i2=0 To LevelDetail
				vertex=GetLevelVertex(i,j,i2,j2)
				If i2=0 
					If LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
						random#=1.0
					Else 
						random#=0
					EndIf
				Else If i2=LevelDetail
					If LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
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
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iPlusOne,j)\Terrain\Extrusion
			
			i2=LevelDetail
			For j2=0 To LevelDetail
				vertex=GetLevelVertex(i,j,i2,j2)
				If j2=0 
					If LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
						random#=1.0
					Else 
						random#=0
					EndIf
				Else If j2=LevelDetail
					If LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
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
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jPlusOne)\Terrain\Extrusion
			
			j2=LevelDetail
			For i2=0 To LevelDetail
				vertex=GetLevelVertex(i,j,i2,j2)
				If i2=0 
					If LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
						random#=1.0
					Else 
						random#=0
					EndIf
				Else If i2=LevelDetail
					If LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
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
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iMinusOne,j)\Terrain\Extrusion
			
			i2=0
			For j2=0 To LevelDetail
				vertex=GetLevelVertex(i,j,i2,j2)
				If j2=0 
					If LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
						random#=1.0
					Else 
						random#=0
					EndIf
				Else If j2=LevelDetail
					If LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
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

End Function

Function UpdateLevelTileTexture(i,j)

	For j2=0 To LevelDetail
		For i2=0 To LevelDetail
			CalculateUV(LevelTiles(i,j)\Terrain\Texture,i2,j2,LevelTiles(i,j)\Terrain\Rotation,8,LevelDetail)
			vertex=GetLevelVertex(i,j,i2,j2)
			VertexTexCoords LevelSurface(j),vertex,ChunkTileU,ChunkTileV
		Next
	Next

End Function

Function GetLevelVertex(i,j,i2,j2)
	; Gets the index number of the vertex at chunk tile (i,j) with detail subdivision (i2,j2)
	; in the currentchunk
	
	; since the chunk has a border around it, we decrease i and j by 1, and reduce width by 2
	;i=i-1
	;j=j-1
	;n=(i+j*(LevelWidth))*(LevelDetail+1)*(LevelDetail+1) ; get to start of tile
	n=i*(LevelDetail+1)*(LevelDetail+1)
	n=n+j2*(LevelDetail+1)+i2
	Return n
End Function

Function GetLevelSideVertex(i,j,index)
	; edge vertex generation order: north -> east -> south -> west
	; start at the end of all the regular vertices
	n=LevelWidth*(LevelDetail+1)*(LevelDetail+1)
	If j=0 Or j=LevelHeight-1
		VerticesPerTile=12*LevelDetail
	Else
		VerticesPerTile=16*LevelDetail
	EndIf
	If i>0
		; account for westmost face being missing on level border
		index=index-4*LevelDetail
	EndIf
	n=n+i*VerticesPerTile+index
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

Function DeltaTo(Start,Destination)
	Return Destination-Start
End Function

Function MirrorAcrossInt(MyPosition, MirrorPosition)
	Delta=DeltaTo(MyPosition,MirrorPosition)
	Return Delta+MirrorPosition
End Function
Function MirrorAcrossFloat#(MyPosition#, MirrorPosition#)
	Delta#=DeltaTo(MyPosition#,MirrorPosition#)
	Return Delta#+MirrorPosition#
End Function

Function EuclideanRemainderInt(Value,Divisor)

	Result=Value Mod Divisor
	If Result<0
		Result=Result+Divisor
	EndIf
	Return Result

End Function

Function EuclideanRemainderFloat#(Value#,Divisor#)

	Result#=Value# Mod Divisor#
	If Result#<0
		Result#=Result#+Divisor#
	EndIf
	Return Result#

End Function

Function UpdateWaterMeshGlow(Entity)

	If WaterGlow=True 
		EntityBlend Entity,3
	Else 
		EntityBlend Entity,1
	EndIf

End Function

Function UpdateWaterMeshTransparent(Entity)

	If WaterTransparent=True 
		EntityAlpha Entity,.5
	Else
		EntityAlpha Entity,1
	EndIf

End Function

Function UpdateAllWaterMeshGlow()

	For i=0 To MaxLevelCoordinate
		UpdateWaterMeshGlow(WaterMesh(i))
	Next
	UpdateWaterMeshGlow(CurrentWaterTile)

End Function

Function UpdateAllWaterMeshTransparent()

	For i=0 To MaxLevelCoordinate
		UpdateWaterMeshTransparent(WaterMesh(i))
	Next
	UpdateWaterMeshTransparent(CurrentWaterTile)

End Function

Function RecalculateNormals(j)

	UpdateNormals LevelMesh(j)
	For i=0 To LevelWidth-1
		For i2=0 To LevelDetail
			For j2=0 To LevelDetail
				If i2=0 Or i2=LevelDetail Or j2=0 Or j2=LevelDetail
					vertex=GetLevelVertex(i,j,i2,j2)
					VertexNormal LevelSurface(j),vertex,0.0,1.0,0.0
				EndIf
			Next
		Next
	Next
	
End Function


Function BuildLevelModel()

	
	For i=0 To MaxLevelCoordinate
		LevelMesh(i)=CreateMesh()
		LevelSurface(i)=CreateSurface(LevelMesh(i))
		;EntityFX LevelMesh(i),2
		
		Watermesh(i)=CreateMesh()
		Watersurface(i)=CreateSurface(Watermesh(i))
		;EntityAlpha WaterMesh(i),.5
		;EntityFX WaterMesh(i),2
		;UpdateWaterMeshGlow(i)
		;UpdateWaterMeshTransparent(i)
		
		Logicmesh(i)=CreateMesh()
		Logicsurface(i)=CreateSurface(Logicmesh(i))
		EntityFX LogicMesh(i),2


	Next
	UpdateAllWaterMeshGlow()
	UpdateAllWaterMeshTransparent()

	
	For j=0 To LevelHeight-1
		ClearSurface LevelSurface(j)
		
		For i=0 To LevelWidth-1
			CreateLevelTileTop(i,j)
		Next
		;UpdateNormals LevelMesh(j)
		EntityTexture LevelMesh(j),LevelTexture
	Next
	
	For j=0 To LevelHeight-1
		; get the newest one, and increment from there
		currentvertex=GetLevelVertex(LevelWidth-1,0,LevelDetail,LevelDetail)+1
		
		For i=0 To LevelWidth-1
			CreateLevelTileSides(i,j)
		Next
	Next
	
	; and point all edge vertex normals "up" (to smooth lighting)
	
	For j=0 To LevelHeight-1
		RecalculateNormals(j)
	Next
	
	
	; water
	For j=0 To LevelHeight-1
		ClearSurface WaterSurface(j)
		For i=0 To LevelWidth-1
			; top face
			CalculateUV(LevelTiles(i,j)\Water\Texture,0,0,LevelTiles(i,j)\Water\Rotation,4,1)
			AddVertex (WaterSurface(j),i,LevelTiles(i,j)\Water\Height,-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTiles(i,j)\Water\Texture,1,0,LevelTiles(i,j)\Water\Rotation,4,1)
			AddVertex (WaterSurface(j),i+1,LevelTiles(i,j)\Water\Height,-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTiles(i,j)\Water\Texture,0,1,LevelTiles(i,j)\Water\Rotation,4,1)
			AddVertex (WaterSurface(j),i,LevelTiles(i,j)\Water\Height,-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTiles(i,j)\Water\Texture,1,1,LevelTiles(i,j)\Water\Rotation,4,1)
			AddVertex (WaterSurface(j),i+1,LevelTiles(i,j)\Water\Height,-j-1,ChunkTileU,ChunkTileV)
			
			AddTriangle (WaterSurface(j),i*4+0,i*4+1,i*4+2)
			AddTriangle (WaterSurface(j),i*4+1,i*4+3,i*4+2)
			
			;VertexColor WaterSurface(j),i*4+0,0,0,0
			;VertexColor WaterSurface(j),i*4+1,0,0,0
			;VertexColor WaterSurface(j),i*4+2,0,0,0
			;VertexColor WaterSurface(j),i*4+3,0,0,0
		Next
		UpdateNormals WaterMesh(j)
		EntityTexture WaterMesh(j),WaterTexture
		
		TranslateEntity WaterMesh(j),0,-0.04,0
	Next
		
	; logic
	For j=0 To LevelHeight-1
		ClearSurface LogicSurface(j)
		For i=0 To LevelWidth-1
		
			;If LevelTiles(i,j)\Terrain\Logic=1 Or LevelTiles(i,j)\Terrain\Logic=2 Or LevelTiles(i,j)\Terrain\Logic=11 Or LevelTiles(i,j)\Terrain\Logic=12 Or LevelTiles(i,j)\Terrain\Logic=13
			If LevelTileLogicHasVisuals(i,j)
				nologicshow=0
			Else
				nologicshow=-300
			EndIf
			; top face
			; pick height of logic mesh as just over maxi(water,tile)
			If LevelTiles(i,j)\Water\Height>LevelTiles(i,j)\Terrain\Extrusion
				height#=LevelTiles(i,j)\Water\Height+0.05
			Else
				height#=LevelTiles(i,j)\Terrain\Extrusion+0.05
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
		
		If ShowLevelMesh=ShowLevelMeshShow
			ShowEntity LevelMesh(j)
			EntityAlpha LevelMesh(j),1.0
		ElseIf ShowLevelMesh=ShowLevelMeshHide
			HideEntity LevelMesh(j)
		ElseIf ShowLevelMesh=ShowLevelMeshTransparent
			ShowEntity LevelMesh(j)
			EntityAlpha LevelMesh(j),0.5
		EndIf
	Next
	
End Function

Function ChangeLevelTile(i,j,update)

	If Not PassesPlacementDensityTest()
		Return
	EndIf

	BrushSpaceX=LevelSpaceToBrushSpaceX(i,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(j,BrushWrap)

	If StepPer=StepPerTile
		RunStepSize()
		;RunStepSize(BrushSpaceX,BrushSpaceY)
	EndIf
	
	GrabLevelTileFromBrush(BrushSpaceX,BrushSpaceY)

	ChangeLevelTileActual(i,j,update)
	
	If DupeMode=DupeModeX
		TargetX=MirrorAcrossInt(i,MirrorPositionX)
		ChangeLevelTileActual(TargetX,j,update)
	ElseIf DupeMode=DupeModeY
		TargetY=MirrorAcrossInt(j,MirrorPositionY)
		ChangeLevelTileActual(i,TargetY,update)
	ElseIf DupeMode=DupeModeXPlusY
		TargetX=MirrorAcrossInt(i,MirrorPositionX)
		TargetY=MirrorAcrossInt(j,MirrorPositionY)
		ChangeLevelTileActual(TargetX,j,update)
		ChangeLevelTileActual(i,TargetY,update)
		ChangeLevelTileActual(TargetX,TargetY,update)
	EndIf

End Function

Function ChangeLevelTileActual(i,j,update)

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
	
	HeightWasChanged=False
	
	; The Tile
	If CurrentTileTextureUse=True
		LevelTiles(i,j)\Terrain\Texture=CurrentTile\Terrain\Texture ; corresponding to squares in LevelTexture
		LevelTiles(i,j)\Terrain\Rotation=CurrentTile\Terrain\Rotation ; 0-3 , and 4-7 for "flipped"
	EndIf
	If CurrentTileSideTextureUse=True
		LevelTiles(i,j)\Terrain\SideTexture=CurrentTile\Terrain\SideTexture ; texture for extrusion walls
		LevelTiles(i,j)\Terrain\SideRotation=CurrentTile\Terrain\SideRotation ; 0-3 , and 4-7 for "flipped"
	EndIf
	If CurrentTileRandomUse=True
		LevelTiles(i,j)\Terrain\Random=CurrentTile\Terrain\Random ; random height pertubation of tile
	EndIf
	If CurrentTileHeightUse=True
		If LevelTiles(i,j)\Terrain\Height<>CurrentTile\Terrain\Height
			HeightWasChanged=True
		EndIf
		LevelTiles(i,j)\Terrain\Height=CurrentTile\Terrain\Height ; height of "center" - e.g. to make ditches and hills
	EndIf
	If CurrentTileExtrusionUse=True
		LevelTiles(i,j)\Terrain\Extrusion=CurrentTile\Terrain\Extrusion; extrusion with walls around it 
	EndIf
	If CurrentTileRoundingUse=True
		LevelTiles(i,j)\Terrain\Rounding=CurrentTile\Terrain\Rounding; 0-no, 1-yes: are floors rounded if on a drop-off corner
	EndIf
	If CurrentTileEdgeRandomUse=True
		LevelTiles(i,j)\Terrain\EdgeRandom=CurrentTile\Terrain\EdgeRandom; 0-no, 1-yes: are edges rippled
	EndIf
	If CurrentTileLogicUse=True
		LevelTiles(i,j)\Terrain\Logic=CurrentTile\Terrain\Logic
	EndIf
	If update=True 
		UpdateLevelTile(i,j)
		
		HasWest=i>0
		HasEast=i<LevelWidth-1
		HasNorth=j>0
		HasSouth=j<LevelHeight-1
	
		; Possibly update surrounding tiles (Height also needs to update diagonals)
		If HasWest
			If LevelTiles(i-1,j)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion ;Or HeightWasChanged
				UpdateLevelTile(i-1,j)
			EndIf
			If HeightWasChanged
				If HasNorth
					UpdateLevelTile(i-1,j-1)
				EndIf
				If HasSouth
					UpdateLevelTile(i-1,j+1)
				EndIf
			EndIf
		EndIf
		If HasEast
			If LevelTiles(i+1,j)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion ;Or HeightWasChanged
				UpdateLevelTile(i+1,j)
			EndIf
			If HeightWasChanged
				If HasNorth
					UpdateLevelTile(i+1,j-1)
				EndIf
				If HasSouth
					UpdateLevelTile(i+1,j+1)
				EndIf
			EndIf
		EndIf
		If HasNorth
			If LevelTiles(i,j-1)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion ;Or HeightWasChanged
				UpdateLevelTile(i,j-1)
			EndIf
		EndIf
		If HasSouth
			If LevelTiles(i,j+1)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion ;Or HeightWasChanged
				UpdateLevelTile(i,j+1)
			EndIf
		EndIf
		
		If SimulationLevel>=3
			;RecalculateNormals(j)
			DirtyNormals(j)=True
			If HasNorth
				;RecalculateNormals(j-1)
				DirtyNormals(j-1)=True
			EndIf
			If HasSouth
				;RecalculateNormals(j+1)
				DirtyNormals(j+1)=True
			EndIf
		EndIf
	EndIf
			
			
	; the water
	If CurrentWaterTileTextureUse=True 
		LevelTiles(i,j)\Water\Texture=CurrentTile\Water\Texture
		LevelTiles(i,j)\Water\Rotation=CurrentTile\Water\Rotation
	EndIf
	If CurrentWaterTileHeightUse=True LevelTiles(i,j)\Water\Height=CurrentTile\Water\Height
	If CurrentWaterTileTurbulenceUse=True LevelTiles(i,j)\Water\Turbulence=CurrentTile\Water\Turbulence
	If update=True
		UpdateWaterTile(i,j)
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
	
	CopyTile(LevelTiles(i,j),CurrentTile)
	
	BuildCurrentTileModel()
	SetBrushToCurrentTile()

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
	
	CopyTile(BrushTiles(i,j),CurrentTile)

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
	
	CopyTile(LevelTiles(i,j),BrushTiles(iDest,jDest))

End Function

Function LevelTileIsInBrush(tilex,tiley,cursorx,cursory)

	HalfBrushSize=BrushSize/2
	Return tilex>=cursorx-HalfBrushSize And tilex<=cursorx+HalfBrushSize And tiley>=cursory-HalfBrushSize And tiley<=cursory+HalfBrushSize

End Function

Function SetLevelTileAsTarget(i,j)

	CopyTile(LevelTiles(i,j),TargetTile)

End Function

Function LevelTileMatchesTarget(i,j)

	If i<0
		Return False
	ElseIf i>=LevelWidth
		Return False
	EndIf
	
	If j<0
		Return False
	ElseIf j>=LevelHeight
		Return False
	EndIf
	
	;If LevelTileIsInBrush(i,j)
	;	Return True
	;EndIf

	If TargetTileTextureUse And TargetTile\Terrain\Texture<>LevelTiles(i,j)\Terrain\Texture
		Return False
	EndIf
	;If TargetTile\Terrain\Rotation<>LevelTiles(i,j)\Terrain\Rotation
	;	Return False
	;EndIf
	If TargetTileSideTextureUse And TargetTile\Terrain\SideTexture<>LevelTiles(i,j)\Terrain\SideTexture
		Return False
	EndIf
	;If TargetTile\Terrain\SideRotation<>LevelTiles(i,j)\Terrain\SideRotation
	;	Return False
	;EndIf
	If TargetTileRandomUse And TargetTile\Terrain\Random<>LevelTiles(i,j)\Terrain\Random
		Return False
	EndIf
	If TargetTileHeightUse And TargetTile\Terrain\Height<>LevelTiles(i,j)\Terrain\Height
		Return False
	EndIf
	If TargetTileExtrusionUse And TargetTile\Terrain\Extrusion<>LevelTiles(i,j)\Terrain\Extrusion
		Return False
	EndIf
	If TargetTileRoundingUse And TargetTile\Terrain\Rounding<>LevelTiles(i,j)\Terrain\Rounding
		Return False
	EndIf
	If TargetTileEdgeRandomUse And TargetTile\Terrain\EdgeRandom<>LevelTiles(i,j)\Terrain\EdgeRandom
		Return False
	EndIf
	If TargetTileLogicUse And TargetTile\Terrain\Logic<>LevelTiles(i,j)\Terrain\Logic
		Return False
	EndIf
	
	If TargetWaterTileUse And TargetTile\Water\Texture<>LevelTiles(i,j)\Water\Texture
		Return False
	EndIf
	;If TargetTile\Water\Rotation<>LevelTiles(i,j)\Water\Rotation
	;	Return False
	;EndIf
	If TargetWaterTileHeightUse And TargetTile\Water\Height<>LevelTiles(i,j)\Water\Height
		Return False
	EndIf
	If TargetWaterTileTurbulenceUse And TargetTile\Water\Turbulence<>LevelTiles(i,j)\Water\Turbulence
		Return False
	EndIf
	
	Return True

End Function

Function SetXtrudeLogics(LessThanZero,EqualToZero,GreaterThanZero)

	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			If LevelTiles(i,j)\Terrain\Extrusion<0.0
				LevelTiles(i,j)\Terrain\Logic=LessThanZero
			ElseIf LevelTiles(i,j)\Terrain\Extrusion=0.0
				LevelTiles(i,j)\Terrain\Logic=EqualToZero
			ElseIf LevelTiles(i,j)\Terrain\Extrusion>0.0
				LevelTiles(i,j)\Terrain\Logic=GreaterThanZero
			EndIf
			UpdateLogicTile(i,j)
		Next
	Next

End Function

Function LoadTilePreset()
	
	Filename$="Data\Editor\TilePresets\"+TilePresetCategoryName$(CurrentTilePresetCategory)+"\"+TilePresetTileName$(CurrentTilePresetTile)
	file=ReadFile(filename$)
	CurrentTile\Terrain\Texture=ReadInt(file)
	CurrentTile\Terrain\Rotation=ReadInt(file) 
	CurrentTile\Terrain\SideTexture=ReadInt(file)
	CurrentTile\Terrain\SideRotation=ReadInt(file)
	CurrentTile\Terrain\Random=ReadFloat(file)
	CurrentTile\Terrain\Height=ReadFloat(file)
	CurrentTile\Terrain\Extrusion=ReadFloat(file)
	CurrentTile\Terrain\Rounding=ReadInt(file)
	CurrentTile\Terrain\EdgeRandom=ReadInt(file)
	CurrentTile\Terrain\Logic=ReadInt(file)
	
	CurrentTile\Water\Texture=ReadInt(file)
	CurrentTile\Water\Rotation=ReadInt(file)
	CurrentTile\Water\Height=ReadFloat(file)
	CurrentTile\Water\Turbulence=ReadFloat(file)

	SetBrushToCurrentTile()
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
	
	WriteInt file,CurrentTile\Terrain\Texture
	WriteInt file,CurrentTile\Terrain\Rotation
	WriteInt file,CurrentTile\Terrain\SideTexture
	WriteInt file,CurrentTile\Terrain\SideRotation
	WriteFloat file,CurrentTile\Terrain\Random
	WriteFloat file,CurrentTile\Terrain\Height
	WriteFloat file,CurrentTile\Terrain\Extrusion
	WriteInt file,CurrentTile\Terrain\Rounding
	WriteInt file,CurrentTile\Terrain\EdgeRandom
	WriteInt file,CurrentTile\Terrain\Logic
	
	
	WriteInt file,CurrentTile\Water\Texture
	WriteInt file,CurrentTile\Water\Rotation
	WriteFloat file,CurrentTile\Water\Height
	WriteFloat file,CurrentTile\Water\Turbulence

	CloseFile file
End Function

Function BlankObjectPreset(ModelName$,ObjType,ObjSubType)

	CurrentObject\Attributes\ModelName$=ModelName$
	CurrentObject\Attributes\TexName$="!None"
	CurrentObject\Attributes\XScale#=1
	CurrentObject\Attributes\YScale#=1
	CurrentObject\Attributes\ZScale#=1
	CurrentObject\Attributes\XAdjust#=0.0
	CurrentObject\Attributes\YAdjust#=0.0
	CurrentObject\Attributes\ZAdjust#=0.0
	CurrentObject\Attributes\PitchAdjust#=0.0
	CurrentObject\Attributes\YawAdjust#=0.0
	CurrentObject\Attributes\RollAdjust#=0.0
	CurrentObject\Position\X#=0.0
	CurrentObject\Position\Y#=0.0
	CurrentObject\Position\Z#=0.0
	CurrentObject\Position\OldX#=-999
	CurrentObject\Position\OldY#=-999
	CurrentObject\Position\OldZ#=-999
	CurrentObject\Attributes\DX#=0
	CurrentObject\Attributes\DY#=0
	CurrentObject\Attributes\DZ#=0
	CurrentObject\Attributes\Pitch#=0
	CurrentObject\Attributes\Yaw#=0
	CurrentObject\Attributes\Roll#=0
	CurrentObject\Attributes\Pitch2#=0
	CurrentObject\Attributes\Yaw2#=0
	CurrentObject\Attributes\Roll2#=0
	CurrentObject\Attributes\XGoal#=0
	CurrentObject\Attributes\YGoal#=0
	CurrentObject\Attributes\ZGoal#=0
	CurrentObject\Attributes\MovementType=0
	CurrentObject\Attributes\MovementTypeData=0
	CurrentObject\Attributes\Speed#=0
	CurrentObject\Attributes\Radius#=0
	CurrentObject\Attributes\RadiusType=0
	CurrentObject\Attributes\Data10=0
	CurrentObject\Attributes\PushDX#=0
	CurrentObject\Attributes\PushDY#=0
	CurrentObject\Attributes\AttackPower=0
	CurrentObject\Attributes\DefensePower=0
	CurrentObject\Attributes\DestructionType=0
	CurrentObject\Attributes\ID=-1
	CurrentObject\Attributes\LogicType=ObjType
	CurrentObject\Attributes\LogicSubType=ObjSubType
	CurrentObject\Attributes\Active=1001
	CurrentObject\Attributes\LastActive=1001
	CurrentObject\Attributes\ActivationType=0
	CurrentObject\Attributes\ActivationSpeed=0
	CurrentObject\Attributes\Status=0
	CurrentObject\Attributes\Timer=0
	CurrentObject\Attributes\TimerMax1=0
	CurrentObject\Attributes\TimerMax2=0
	CurrentObject\Attributes\Teleportable=False
	CurrentObject\Attributes\ButtonPush=False
	CurrentObject\Attributes\WaterReact=0
	CurrentObject\Attributes\Telekinesisable=0
	CurrentObject\Attributes\Freezable=0
	CurrentObject\Attributes\Reactive=True
	CurrentObject\Attributes\Child=-1
	CurrentObject\Attributes\Parent=-1
	CurrentObject\Attributes\Data0=0
	CurrentObject\Attributes\Data1=0
	CurrentObject\Attributes\Data2=0
	CurrentObject\Attributes\Data3=0
	CurrentObject\Attributes\Data4=0
	CurrentObject\Attributes\Data5=0
	CurrentObject\Attributes\Data6=0
	CurrentObject\Attributes\Data7=0
	CurrentObject\Attributes\Data8=0
	CurrentObject\Attributes\Data9=0
	CurrentObject\Attributes\TextData0=""
	CurrentObject\Attributes\TextData1=""
	CurrentObject\Attributes\TextData2=""
	CurrentObject\Attributes\TextData3=""
	CurrentObject\Attributes\Talkable=0
	CurrentObject\Attributes\CurrentAnim=0
	CurrentObject\Attributes\StandardAnim=0
	CurrentObject\Position\TileX=0
	CurrentObject\Position\TileY=0
	CurrentObject\Position\TileX2=0
	CurrentObject\Position\TileY2=0
	CurrentObject\Attributes\MovementTimer=0
	CurrentObject\Attributes\MovementSpeed=0
	CurrentObject\Attributes\MoveXGoal=0
	CurrentObject\Attributes\MoveYGoal=0
	CurrentObject\Attributes\TileTypeCollision=0
	CurrentObject\Attributes\ObjectTypeCollision=0
	CurrentObject\Attributes\Caged=0
	CurrentObject\Attributes\Dead=0
	CurrentObject\Attributes\DeadTimer=0
	CurrentObject\Attributes\Exclamation=0
	CurrentObject\Attributes\Shadow=-1
	CurrentObject\Attributes\Linked=-1
	CurrentObject\Attributes\LinkBack=-1
	CurrentObject\Attributes\Flying=0
	CurrentObject\Attributes\Frozen=0
	CurrentObject\Attributes\Indigo=0
	CurrentObject\Attributes\FutureInt24=0
	CurrentObject\Attributes\FutureInt25=0

	CurrentObject\Attributes\ScaleAdjust=1.0
	CurrentObject\Attributes\ScaleXAdjust=1.0
	CurrentObject\Attributes\ScaleYAdjust=1.0
	CurrentObject\Attributes\ScaleZAdjust=1.0
	CurrentObject\Attributes\FutureFloat5=0.0
	CurrentObject\Attributes\FutureFloat6=0.0
	CurrentObject\Attributes\FutureFloat7=0.0
	CurrentObject\Attributes\FutureFloat8=0.0
	CurrentObject\Attributes\FutureFloat9=0.0
	CurrentObject\Attributes\FutureFloat10=0.0
	CurrentObject\Attributes\FutureString1$=""
	CurrentObject\Attributes\FutureString2$=""

End Function

Function LoadObjectPreset()
	
	Filename$="Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(CurrentObjectPresetCategory)+"\"+ObjectPresetObjectName$(CurrentObjectPresetObject)

	file=ReadFile(filename$)
	
	CurrentObject\Attributes\ModelName$=ReadString$(file)
	CurrentObject\Attributes\TexName$=ReadString$(file)
	CurrentObject\Attributes\XScale#=ReadFloat(file)
	CurrentObject\Attributes\YScale#=ReadFloat(file)
	CurrentObject\Attributes\ZScale#=ReadFloat(file)
	CurrentObject\Attributes\XAdjust#=ReadFloat(file)
	CurrentObject\Attributes\YAdjust#=ReadFloat(file)
	CurrentObject\Attributes\ZAdjust#=ReadFloat(file)
	CurrentObject\Attributes\PitchAdjust#=ReadFloat(file)
	CurrentObject\Attributes\YawAdjust#=ReadFloat(file)
	CurrentObject\Attributes\RollAdjust#=ReadFloat(file)
	CurrentObject\Position\X#=ReadFloat(file)
	CurrentObject\Position\Y#=ReadFloat(file)
	CurrentObject\Position\Z#=ReadFloat(file)
	CurrentObject\Position\OldX#=ReadFloat(file)
	CurrentObject\Position\OldY#=ReadFloat(file)
	CurrentObject\Position\OldZ#=ReadFloat(file)
	CurrentObject\Attributes\DX#=ReadFloat(file)
	CurrentObject\Attributes\DY#=ReadFloat(file)
	CurrentObject\Attributes\DZ#=ReadFloat(file)
	CurrentObject\Attributes\Pitch#=ReadFloat(file)
	CurrentObject\Attributes\Yaw#=ReadFloat(file)
	CurrentObject\Attributes\Roll#=ReadFloat(file)
	CurrentObject\Attributes\Pitch2#=ReadFloat(file)
	CurrentObject\Attributes\Yaw2#=ReadFloat(file)
	CurrentObject\Attributes\Roll2#=ReadFloat(file)
	CurrentObject\Attributes\XGoal#=ReadFloat(file)
	CurrentObject\Attributes\YGoal#=ReadFloat(file)
	CurrentObject\Attributes\ZGoal#=ReadFloat(file)
	CurrentObject\Attributes\MovementType=ReadInt(file)
	CurrentObject\Attributes\MovementTypeData=ReadInt(file)
	CurrentObject\Attributes\Speed#=ReadFloat(file)
	CurrentObject\Attributes\Radius#=ReadFloat(file)
	CurrentObject\Attributes\RadiusType=ReadInt(file)
	CurrentObject\Attributes\Data10=ReadInt(file)
	CurrentObject\Attributes\PushDX#=ReadFloat(file)
	CurrentObject\Attributes\PushDY#=ReadFloat(file)
	CurrentObject\Attributes\AttackPower=ReadInt(file)
	CurrentObject\Attributes\DefensePower=ReadInt(file)
	CurrentObject\Attributes\DestructionType=ReadInt(file)
	CurrentObject\Attributes\ID=ReadInt(file)
	CurrentObject\Attributes\LogicType=ReadInt(file)
	CurrentObject\Attributes\LogicSubType=ReadInt(file)
	CurrentObject\Attributes\Active=ReadInt(file)
	CurrentObject\Attributes\LastActive=ReadInt(file)
	CurrentObject\Attributes\ActivationType=ReadInt(file)
	CurrentObject\Attributes\ActivationSpeed=ReadInt(file)
	CurrentObject\Attributes\Status=ReadInt(file)
	CurrentObject\Attributes\Timer=ReadInt(file)
	CurrentObject\Attributes\TimerMax1=ReadInt(file)
	CurrentObject\Attributes\TimerMax2=ReadInt(file)
	CurrentObject\Attributes\Teleportable=ReadInt(file)
	CurrentObject\Attributes\ButtonPush=ReadInt(file)
	CurrentObject\Attributes\WaterReact=ReadInt(file)
	CurrentObject\Attributes\Telekinesisable=ReadInt(file)
	CurrentObject\Attributes\Freezable=ReadInt(file)
	CurrentObject\Attributes\Reactive=ReadInt(file)
	CurrentObject\Attributes\Child=ReadInt(file)
	CurrentObject\Attributes\Parent=ReadInt(file)
	CurrentObject\Attributes\Data0=ReadInt(file)
	CurrentObject\Attributes\Data1=ReadInt(file)
	CurrentObject\Attributes\Data2=ReadInt(file)
	CurrentObject\Attributes\Data3=ReadInt(file)
	CurrentObject\Attributes\Data4=ReadInt(file)
	CurrentObject\Attributes\Data5=ReadInt(file)
	CurrentObject\Attributes\Data6=ReadInt(file)
	CurrentObject\Attributes\Data7=ReadInt(file)
	CurrentObject\Attributes\Data8=ReadInt(file)
	CurrentObject\Attributes\Data9=ReadInt(file)
	CurrentObject\Attributes\TextData0$=ReadString$(file)
	CurrentObject\Attributes\TextData1$=ReadString$(file)
	CurrentObject\Attributes\TextData2$=ReadString$(file)
	CurrentObject\Attributes\TextData3$=ReadString$(file)
	CurrentObject\Attributes\Talkable=ReadInt(file)
	CurrentObject\Attributes\CurrentAnim=ReadInt(file)
	CurrentObject\Attributes\StandardAnim=ReadInt(file)
	CurrentObject\Position\TileX=ReadInt(file)
	CurrentObject\Position\TileY=ReadInt(file)
	CurrentObject\Position\TileX2=ReadInt(file)
	CurrentObject\Position\TileY2=ReadInt(file)
	CurrentObject\Attributes\MovementTimer=ReadInt(file)
	CurrentObject\Attributes\MovementSpeed=ReadInt(file)
	CurrentObject\Attributes\MoveXGoal=ReadInt(file)
	CurrentObject\Attributes\MoveYGoal=ReadInt(file)
	CurrentObject\Attributes\TileTypeCollision=ReadInt(file)
	CurrentObject\Attributes\ObjectTypeCollision=ReadInt(file)
	CurrentObject\Attributes\Caged=ReadInt(file)
	CurrentObject\Attributes\Dead=ReadInt(file)
	CurrentObject\Attributes\DeadTimer=ReadInt(file)
	CurrentObject\Attributes\Exclamation=ReadInt(file)
	CurrentObject\Attributes\Shadow=ReadInt(file)
	;CurrentObject\Attributes\Linked=ReadInt(file)
	ReadInt(file)
	CurrentObject\Attributes\Linked=-1
	;CurrentObject\Attributes\LinkBack=ReadInt(file)
	ReadInt(file)
	CurrentObject\Attributes\LinkBack=-1
	CurrentObject\Attributes\Flying=ReadInt(file)
	CurrentObject\Attributes\Frozen=ReadInt(file)
	CurrentObject\Attributes\Indigo=ReadInt(file)
	CurrentObject\Attributes\FutureInt24=ReadInt(file)
	CurrentObject\Attributes\FutureInt25=ReadInt(file)

	CurrentObject\Attributes\ScaleAdjust=ReadFloat(file)
	If CurrentObject\Attributes\ScaleAdjust=0.0
		CurrentObject\Attributes\ScaleAdjust=1.0
	EndIf
	
	CurrentObject\Attributes\ScaleXAdjust=ReadFloat(file)
	CurrentObject\Attributes\ScaleYAdjust=ReadFloat(file)
	CurrentObject\Attributes\ScaleZAdjust=ReadFloat(file)
	CurrentObject\Attributes\ScaleXAdjust=1.0
	CurrentObject\Attributes\ScaleYAdjust=1.0
	CurrentObject\Attributes\ScaleZAdjust=1.0
	CurrentObject\Attributes\FutureFloat5=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat6=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat7=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat8=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat9=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat10=ReadFloat(file)
	CurrentObject\Attributes\FutureString1$=ReadString(file)
	CurrentObject\Attributes\FutureString2$=ReadString(file)
	
	
	
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
	AddAdjuster("ScaleAdjust")
	
	;AddAdjuster("X")
	;AddAdjuster("Y")
	;AddAdjuster("Z")
	
	AddAdjuster("XAdjust")
	AddAdjuster("YAdjust")
	AddAdjuster("ZAdjust")
	AddAdjuster("YawAdjust")
	AddAdjuster("RollAdjust")
	AddAdjuster("PitchAdjust")
	AddAdjuster("XScale")
	AddAdjuster("YScale")
	AddAdjuster("ZScale")
	
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
	AddAdjuster("TextData0")
	AddAdjuster("TextData1")
	AddAdjuster("Talkable")
	AddAdjuster("Exclamation")
	AddAdjuster("Timer")
	AddAdjuster("TimerMax1")
	AddAdjuster("TimerMax2")
	AddAdjuster("DefensePower")
	
	AddAdjuster("DX")
	AddAdjuster("DY")
	AddAdjuster("Frozen")
	AddAdjuster("Teleportable")
	AddAdjuster("ButtonPush")
	AddAdjuster("MovementSpeed")
	AddAdjuster("MovementType")
	;AddAdjuster("MovementTypeData")
	;AddAdjuster("MovementTimer")
	AddAdjuster("TileTypeCollision")
	AddAdjuster("ObjectTypeCollision")
	
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
	
	
	MakeAllObjectAdjustersAbsolute()
	
	BuildCurrentObjectModel()
	;SetBrushToCurrentObject()
	CurrentObjectWasChanged()

End Function


Function AddAdjuster(Name$)

	ObjectAdjuster$(NofObjectAdjusters)=Name$
	NofObjectAdjusters=NofObjectAdjusters+1

End Function


Function PrintMessageForInstant(Message$)

	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
	Print message$

End Function


Function ShowMessage(message$, milliseconds)

	PrintMessageForInstant(message$)
	Delay milliseconds

End Function


; for preventing several of the same message from pausing the same frame for a long time
Function ShowMessageOnce(message$, milliseconds)

	If ShowingError=False
		ShowingError=True ; will reset at the start of every frame
		ShowMessage(message$, milliseconds)
	EndIf

End Function

Function GetObjectOffset#(Attributes.GameObjectAttributes,index)

	; Type-specific placements
	If Attributes\LogicType=10 And Attributes\LogicSubType=1 ; house-door
		If Attributes\YawAdjust=90
			xoffset#=0.5
			yoffset#=1.0
		Else If Attributes\YawAdjust=270
			xoffset#=0.5
			yoffset#=0.0
		Else If Attributes\YawAdjust=45
			xoffset#=-0.1
			yoffset#=0.6
		Else If Attributes\YawAdjust=315
			xoffset#=0.40
			yoffset#=-0.1

			
		Else
			xoffset#=-0.00
			yoffset#=0.5

		EndIf
	Else If Attributes\LogicType=10 And Attributes\LogicSubType=2 ; dungeon-door
		If Attributes\YawAdjust=0
			xoffset#=0.0
			yoffset#=1.0
		Else If Attributes\YawAdjust=90
			xoffset#=1.0
			yoffset#=1.0
		Else If Attributes\YawAdjust=180
			xoffset#=1.0
			yoffset#=0.0
		Else
			xoffset#=0.0
			yoffset#=0.0

		EndIf
	Else If Attributes\LogicType=10 And Attributes\LogicSubType=3 ; townhouse1-door
		If Attributes\YawAdjust=90
			xoffset#=0.6
			yoffset#=1.0
		Else If Attributes\YawAdjust=270
			xoffset#=+0.40
			yoffset#=0.0
		Else If Attributes\YawAdjust=45
			xoffset#=-0.338
			yoffset#=0.342
		Else If Attributes\YawAdjust=315
			xoffset#=0.637
			yoffset#=-0.361

			
		Else
			xoffset#=-0.00
			yoffset#=0.6

		EndIf

	Else If Attributes\LogicType=10 And Attributes\LogicSubType=4 ; townhouse2-door
		If Attributes\YawAdjust=90
			xoffset#=0.1
			yoffset#=1.0
		Else If Attributes\YawAdjust=270
			xoffset#=0.90
			yoffset#=0.0
		Else If Attributes\YawAdjust=45
			xoffset#=-0.338-.35
			yoffset#=0.342-.35
		Else If Attributes\YawAdjust=315
			xoffset#=0.637+.35
			yoffset#=-0.361-.35

			
		Else
			xoffset#=0.00
			yoffset#=0.1

		EndIf


	Else
		xoffset#=0.5
		yoffset#=0.5
	EndIf
	
	If index=0
		Return xoffset#
	Else
		Return yoffset#
	EndIf

End Function


Function IsPositionInLevel(x,y)

	Return x>=0 And y>=0 And x<LevelWidth And y<LevelHeight

End Function

Function IsPositionInLevelArrayBounds(x,y)

	Return x>=0 And y>=0 And x<=100 And y<=100

End Function

Function SetObjectPosition(i,x#,y#)

	floorx=Floor(x)
	floory=Floor(y)
	
	SetObjectTileXY(i,floorx,floory)
	
	xoffset#=GetObjectOffset#(LevelObjects(i)\Attributes,0)
	yoffset#=GetObjectOffset#(LevelObjects(i)\Attributes,1)
	
	LevelObjects(i)\Position\X#=x#+xoffset#
	LevelObjects(i)\Position\Y#=y#+yoffset#
	
	Return True

End Function


Function PlaceObjectOrChangeLevelTile(x,y)

	If EditorMode=0
		ChangeLevelTile(x,y,True)
	ElseIf EditorMode=3
		PlaceObject(x,y)
	EndIf

End Function


Function PlaceObject(x#,y#)

	If Not PassesPlacementDensityTest()
		Return
	EndIf

	BrushSpaceX=LevelSpaceToBrushSpaceX(x,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(y,BrushWrap)

	PlaceObjectActual(x#,y#,BrushSpaceX,BrushSpaceY)
	
	If DupeMode=DupeModeX
		TargetX#=MirrorAcrossFloat#(x#,MirrorPositionX)
		If TargetX#<>x#
			PlaceObjectActual(TargetX#,y#,BrushSpaceX,BrushSpaceY)
		EndIf
	ElseIf DupeMode=DupeModeY
		TargetY#=MirrorAcrossFloat#(y#,MirrorPositionY)
		If TargetY#<>y#
			PlaceObjectActual(x#,LevelHeight-1-y#,BrushSpaceX,BrushSpaceY)
		EndIf
	ElseIf DupeMode=DupeModeXPlusY
		TargetX#=MirrorAcrossFloat#(x#,MirrorPositionX)
		TargetY#=MirrorAcrossFloat#(y#,MirrorPositionY)
		If TargetX#<>x#
			PlaceObjectActual(TargetX#,y#,BrushSpaceX,BrushSpaceY)
		EndIf
		If TargetY#<>y#
			PlaceObjectActual(x#,TargetY#,BrushSpaceX,BrushSpaceY)
		EndIf
		If TargetX#<>x# And TargetY#<>y#
			PlaceObjectActual(TargetX#,TargetY#,BrushSpaceX,BrushSpaceY)
		EndIf
	EndIf

End Function


Function PlaceObjectActual(x#,y#,BrushSpaceX,BrushSpaceY)

	For k=0 To NofBrushObjects-1
		If BrushObjectTileXOffset(k)=BrushSpaceX And BrushObjectTileYOffset(k)=BrushSpaceY
			CopyObjectFromBrush(k,TempObject)
			PlaceThisObject(x,y,TempObject)
		EndIf
	Next

End Function


Function PlaceThisObject(x#,y#,SourceObject.GameObject)

	If NofObjects>=MaxNofObjects
		ShowMessageOnce(MaxNofObjects+" object limit reached; refusing to place any more", 1000)
		Return
	EndIf

	If PreventPlacingObjectsOutsideLevel And (Not IsPositionInLevelArrayBounds(Floor(x#),Floor(y#)))
		Return
	EndIf
	
	SourceAttributes.GameObjectAttributes=SourceObject\Attributes
	SourcePosition.GameObjectPosition=SourceObject\Position

	RandomizeObjectData(SourceObject)
	
	NewObject.GameObject=LevelObjects(NofObjects)
	
	CopyObjectAttributes(SourceAttributes,NewObject\Attributes)
	CopyObjectPosition(SourcePosition,NewObject\Position)
	
	SetObjectPosition(NofObjects,x#,y#)
	
	NewObject\Model\HatEntity=0
	NewObject\Model\HatTexture=0
	NewObject\Model\AccEntity=0
	NewObject\Model\AccTexture=0

	NewObject\Position\OldX#=-999
	NewObject\Position\OldY#=-999
	NewObject\Position\OldZ#=-999
	
	;For i=0 To 30
	;	ObjectAdjusterString$(NofObjects,i)=ObjectAdjuster$(i)
	;Next
	
	
	ThisObject=NofObjects
	NofObjects=NofObjects+1
	
	
	BuildLevelObjectModel(ThisObject)
	
	
	CreateObjectPositionMarker(ThisObject)
	
	
	AddOrToggleSelectObject(ThisObject)
	
	
	SomeObjectWasChanged()
	
	

End Function


Function RandomizeObjectData(SourceObj.GameObject)

	SourceAttributes.GameObjectAttributes=SourceObj\Attributes
	SourcePosition.GameObjectPosition=SourceObj\Position

	If ObjectAdjusterLogicType\RandomEnabled
		SourceAttributes\LogicType=RandomObjectAdjusterInt(ObjectAdjusterLogicType)
	EndIf
	If ObjectAdjusterLogicSubType\RandomEnabled
		SourceAttributes\LogicSubType=RandomObjectAdjusterInt(ObjectAdjusterLogicSubType)
	EndIf
	
	If ObjectAdjusterPitchAdjust\RandomEnabled
		SourceAttributes\PitchAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterPitchAdjust)
	EndIf
	If ObjectAdjusterYawAdjust\RandomEnabled
		SourceAttributes\YawAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterYawAdjust)
	EndIf
	If ObjectAdjusterRollAdjust\RandomEnabled
		SourceAttributes\RollAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterRollAdjust)
	EndIf
	
	If ObjectAdjusterData0\RandomEnabled
		SourceAttributes\Data0=RandomObjectAdjusterInt(ObjectAdjusterData0)
	EndIf
	If ObjectAdjusterData1\RandomEnabled
		SourceAttributes\Data1=RandomObjectAdjusterInt(ObjectAdjusterData1)
	EndIf
	If ObjectAdjusterData2\RandomEnabled
		SourceAttributes\Data2=RandomObjectAdjusterInt(ObjectAdjusterData2)
	EndIf
	If ObjectAdjusterData3\RandomEnabled
		SourceAttributes\Data3=RandomObjectAdjusterInt(ObjectAdjusterData3)
	EndIf
	If ObjectAdjusterData4\RandomEnabled
		SourceAttributes\Data4=RandomObjectAdjusterInt(ObjectAdjusterData4)
	EndIf
	If ObjectAdjusterData5\RandomEnabled
		SourceAttributes\Data5=RandomObjectAdjusterInt(ObjectAdjusterData5)
	EndIf
	If ObjectAdjusterData6\RandomEnabled
		SourceAttributes\Data6=RandomObjectAdjusterInt(ObjectAdjusterData6)
	EndIf
	If ObjectAdjusterData7\RandomEnabled
		SourceAttributes\Data7=RandomObjectAdjusterInt(ObjectAdjusterData7)
	EndIf
	If ObjectAdjusterData8\RandomEnabled
		SourceAttributes\Data8=RandomObjectAdjusterInt(ObjectAdjusterData8)
	EndIf
	If ObjectAdjusterData9\RandomEnabled
		SourceAttributes\Data9=RandomObjectAdjusterInt(ObjectAdjusterData9)
	EndIf
	
	If ObjectAdjusterXScale\RandomEnabled
		SourceAttributes\XScale#=RandomObjectAdjusterFloat#(ObjectAdjusterXScale)
	EndIf
	If ObjectAdjusterYScale\RandomEnabled
		SourceAttributes\YScale#=RandomObjectAdjusterFloat#(ObjectAdjusterYScale)
	EndIf
	If ObjectAdjusterZScale\RandomEnabled
		SourceAttributes\ZScale#=RandomObjectAdjusterFloat#(ObjectAdjusterZScale)
	EndIf
	
	If ObjectAdjusterXAdjust\RandomEnabled
		SourceAttributes\XAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterXAdjust)
	EndIf
	If ObjectAdjusterYAdjust\RandomEnabled
		SourceAttributes\YAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterYAdjust)
	EndIf
	If ObjectAdjusterZAdjust\RandomEnabled
		SourceAttributes\ZAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterZAdjust)
	EndIf
	
	If ObjectAdjusterX\RandomEnabled
		SourcePosition\X#=RandomObjectAdjusterFloat#(ObjectAdjusterX)
	EndIf
	If ObjectAdjusterY\RandomEnabled
		SourcePosition\Y#=RandomObjectAdjusterFloat#(ObjectAdjusterY)
	EndIf
	If ObjectAdjusterZ\RandomEnabled
		SourcePosition\Z#=RandomObjectAdjusterFloat#(ObjectAdjusterZ)
	EndIf
	
	If ObjectAdjusterDX\RandomEnabled
		SourceAttributes\DX#=RandomObjectAdjusterFloat#(ObjectAdjusterDX)
	EndIf
	If ObjectAdjusterDY\RandomEnabled
		SourceAttributes\DY#=RandomObjectAdjusterFloat#(ObjectAdjusterDY)
	EndIf
	If ObjectAdjusterDZ\RandomEnabled
		SourceAttributes\DZ#=RandomObjectAdjusterFloat#(ObjectAdjusterDZ)
	EndIf
	
	If ObjectAdjusterMovementType\RandomEnabled
		SourceAttributes\MovementType=RandomObjectAdjusterInt(ObjectAdjusterMovementType)
	EndIf
	
	If ObjectAdjusterDefensePower\RandomEnabled
		SourceAttributes\DefensePower=RandomObjectAdjusterInt(ObjectAdjusterDefensePower)
	EndIf
	
	If ObjectAdjusterID\RandomEnabled
		SourceAttributes\ID=RandomObjectAdjusterInt(ObjectAdjusterID)
	EndIf
	
	If ObjectAdjusterActive\RandomEnabled
		SourceAttributes\Active=RandomObjectAdjusterInt(ObjectAdjusterActive)
	EndIf
	If ObjectAdjusterActivationType\RandomEnabled
		SourceAttributes\ActivationType=RandomObjectAdjusterInt(ObjectAdjusterActivationType)
	EndIf
	If ObjectAdjusterActivationSpeed\RandomEnabled
		SourceAttributes\ActivationSpeed=RandomObjectAdjusterInt(ObjectAdjusterActivationSpeed)
		; enforce even numbers
		If SourceAttributes\ActivationSpeed Mod 2=1
			SourceAttributes\ActivationSpeed=SourceAttributes\ActivationSpeed+1
		EndIf
	EndIf
	
	If ObjectAdjusterStatus\RandomEnabled
		SourceAttributes\Status=RandomObjectAdjusterInt(ObjectAdjusterStatus)
	EndIf
	
	If ObjectAdjusterTimer\RandomEnabled
		SourceAttributes\Timer=RandomObjectAdjusterInt(ObjectAdjusterTimer)
	EndIf
	If ObjectAdjusterTimerMax1\RandomEnabled
		SourceAttributes\TimerMax1=RandomObjectAdjusterInt(ObjectAdjusterTimerMax1)
	EndIf
	If ObjectAdjusterTimerMax2\RandomEnabled
		SourceAttributes\TimerMax2=RandomObjectAdjusterInt(ObjectAdjusterTimerMax2)
	EndIf
	
	If ObjectAdjusterTeleportable\RandomEnabled
		SourceAttributes\Teleportable=RandomObjectAdjusterInt(ObjectAdjusterTeleportable)
	EndIf
	If ObjectAdjusterButtonPush\RandomEnabled
		SourceAttributes\ButtonPush=RandomObjectAdjusterInt(ObjectAdjusterButtonPush)
	EndIf
	
	If ObjectAdjusterTalkable\RandomEnabled
		SourceAttributes\Talkable=RandomObjectAdjusterInt(ObjectAdjusterTalkable)
	EndIf
	
	If ObjectAdjusterMovementSpeed\RandomEnabled
		SourceAttributes\MovementSpeed=RandomObjectAdjusterInt(ObjectAdjusterMovementSpeed)
	EndIf
	
	If ObjectAdjusterMoveXGoal\RandomEnabled
		SourceAttributes\MoveXGoal=RandomObjectAdjusterInt(ObjectAdjusterMoveXGoal)
	EndIf
	If ObjectAdjusterMoveYGoal\RandomEnabled
		SourceAttributes\MoveYGoal=RandomObjectAdjusterInt(ObjectAdjusterMoveYGoal)
	EndIf
	
	If ObjectAdjusterTileTypeCollision\RandomEnabled
		SourceAttributes\TileTypeCollision=0
		For i=0 To 14
			If Rand(0,1)=0
				SourceAttributes\TileTypeCollision=SourceAttributes\TileTypeCollision+2^i
			EndIf
		Next
	EndIf
	If ObjectAdjusterObjectTypeCollision\RandomEnabled
		SourceAttributes\ObjectTypeCollision=0
		For i=1 To 10
			If Rand(0,1)=0
				SourceAttributes\ObjectTypeCollision=SourceAttributes\ObjectTypeCollision+2^i
			EndIf
		Next
	EndIf
	
	If ObjectAdjusterDead\RandomEnabled
		SourceAttributes\Dead=RandomObjectAdjusterInt(ObjectAdjusterDead)
	EndIf
	
	If ObjectAdjusterCaged\RandomEnabled
		SourceAttributes\Caged=RandomObjectAdjusterInt(ObjectAdjusterCaged)
	EndIf
	
	If ObjectAdjusterIndigo\RandomEnabled
		SourceAttributes\Indigo=RandomObjectAdjusterInt(ObjectAdjusterIndigo)
	EndIf
	
	If ObjectAdjusterFrozen\RandomEnabled
		SourceAttributes\Frozen=RandomObjectAdjusterInt(ObjectAdjusterFrozen)
	EndIf
	
	If ObjectAdjusterExclamation\RandomEnabled
		SourceAttributes\Exclamation=RandomObjectAdjusterInt(ObjectAdjusterExclamation)
	EndIf
	
	If ObjectAdjusterDestructionType\RandomEnabled
		SourceAttributes\DestructionType=RandomObjectAdjusterInt(ObjectAdjusterDestructionType)
	EndIf
	
	If ObjectAdjusterLinked\RandomEnabled
		SourceAttributes\Linked=RandomObjectAdjusterInt(ObjectAdjusterLinked)
	EndIf
	
	If ObjectAdjusterLinkBack\RandomEnabled
		SourceAttributes\LinkBack=RandomObjectAdjusterInt(ObjectAdjusterLinkBack)
	EndIf
	
	If ObjectAdjusterScaleAdjust\RandomEnabled
		SourceAttributes\ScaleAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterScaleAdjust)
	EndIf

End Function


Function CalculateEffectiveID(Attributes.GameObjectAttributes)

	Return CalculateEffectiveIDWith(Attributes\LogicType,Attributes\ID,Attributes\Data0,Attributes\Data1,Attributes\TileTypeCollision,Attributes\ModelName$)

End Function

Function CalculateEffectiveIDWith(TargetType,TargetID,Data0,Data1,TargetTileTypeCollision,ModelName$)

	Select TargetType
	Case 10,20,45,210,281,410,424 ; gate, fire trap, conveyor lead, transporter, suction tube straight, flip bridge, laser gate
		If TargetID=-1
			Return 500+5*Data0+Data1
		EndIf
	Case 40 ; stepping stone
		If TargetID=-1
			Return 500+(8+Data0)*5+Data1
		EndIf
	Case 280 ; spring
		Return 500+5*Data0+Data1
	Case 301 ; rainbow float
		If TargetID=-1
			Return 500+(8+Data0)*5+Data1
		EndIf
	Case 432 ; moobot
		If TargetTileTypeCollision=0
			Return 500+Data0*5+Data1
		EndIf
	End Select
	
	If ModelName$="!Cage" Or ModelName$="!FlipBridge" Or ModelName$="!Spring" Or ModelName$="!ColourGate" Or ModelName$="!Transporter" Or ModelName$="!Teleport" Or ModelName$="!Suctube"
		If TargetID=-1
			Return 500+Data0*5+Data1
		EndIf
	EndIf
	If ModelName$="!SteppingStone"
		If TargetID=-1
			Return 500+(8+Data0)*5+Data1
		EndIf
	EndIf
	
	Return TargetID

End Function

Function ShouldBeInvisibleInGame(Attributes.GameObjectAttributes)

	If Attributes\ModelName$="!None" Or Attributes\ModelName$="!FloingOrb"
		Return True
	ElseIf Attributes\ModelName$="!Button" And (Attributes\LogicSubType=11 Or Attributes\LogicSubType>=32 Or Attributes\LogicSubType=15) ; NPC move, invisible buttons, general command
		Return True
	ElseIf Attributes\ModelName$="!IceBlock" And Attributes\Data3<>0 And Attributes\Data3<>1
		Return True
	ElseIf Attributes\Active=0 And (Attributes\ModelName$="!NPC" Or Attributes\LogicType=424) ; NPCs, OpenWA retro laser gates
		Return True
	Else
		Return False
	EndIf
	
End Function


Function HideObjectModel(Model.GameObjectModel)

	If Model\Entity>0
		HideEntity Model\Entity
	EndIf
	If Model\HatEntity>0
		HideEntity Model\HatEntity
	EndIf
	If Model\AccEntity>0
		HideEntity Model\AccEntity
	EndIf

End Function


Function ShowObjectModel(Obj.GameObject)

	Model.GameObjectModel=Obj\Model
	
	If Model\Entity>0
		ShowEntity Model\Entity
	EndIf
	If Model\HatEntity>0
		ShowEntity Model\HatEntity
	EndIf
	If Model\AccEntity>0
		ShowEntity Model\AccEntity
	EndIf
	
	UpdateObjectAlpha(Obj)

End Function


Function IDFilterShouldHide(Attributes.GameObjectAttributes)

If IDFilterEnabled
	If IDFilterInverted=True
		Return IDFilterAllow=CalculateEffectiveID(Attributes)
	Else
		Return IDFilterAllow<>CalculateEffectiveID(Attributes)
	EndIf
Else
	Return False
EndIf

End Function


Function UpdateObjectVisibility(Obj.GameObject)

	If ShowObjectMesh=0 Or IDFilterShouldHide(Obj\Attributes)
		HideObjectModel(Obj\Model)
	Else
		If SimulationLevel>=2 And ShouldBeInvisibleInGame(Obj\Attributes)
			HideObjectModel(Obj\Model)
		Else
			ShowObjectModel(Obj)
		EndIf
	EndIf

End Function


Function SetEntityAlphaWithModelName(Entity,Alpha#,ModelName$)

	If ModelName$="!NPC" Or ModelName$="!Tentacle"
		Entity=GetChild(Entity,3)
	EndIf

	EntityAlpha Entity,Alpha#

End Function


Function UpdateObjectAlpha(Obj.GameObject)

	SetEntityAlphaWithModelName(Obj\Model\Entity,BaseObjectAlpha#(Obj\Attributes),Obj\Attributes\ModelName$)

End Function


Function BaseObjectAlpha#(Attributes.GameObjectAttributes)

	If Attributes\ModelName$="!FloingBubble"
		Return 0.5
	;ElseIf Attributes\ModelName$="!MagicMirror"
	;	Return 0.5
	ElseIf Attributes\ModelName$="!IceFloat"
		Return 0.8
	ElseIf Attributes\ModelName$="!PlantFloat"
		Return 0.7
	ElseIf Attributes\ModelName$="!Retrolasergate"
		Return 0.5
	ElseIf Attributes\ModelName$="!Teleport"
		Return 0.6
	ElseIf Attributes\ModelName$="!WaterFall"
		Return 0.7
	ElseIf Attributes\ModelName$="!IceBlock" And (Attributes\Data3=0 Or Attributes\Data3=1)
		Return 0.5
	ElseIf Attributes\ModelName$="!Conveyor" And Attributes\Data4=4
		Return 0.8
	Else
		Return 1.0
	EndIf
	
	; rainbow bubble alpha is set to 0.8 during gameplay/simulation

End Function


Function ObjectSumX#(Obj.GameObject)
	Return Obj\Position\X#+Obj\Attributes\XAdjust#
End Function

Function ObjectSumY#(Obj.GameObject)
	Return Obj\Position\Y#+Obj\Attributes\YAdjust#
End Function

Function ObjectSumZ#(Obj.GameObject)
	Return Obj\Position\Z#+Obj\Attributes\ZAdjust#
End Function

Function ObjectSumYaw#(Obj.GameObject)
	Return Obj\Attributes\Yaw#+Obj\Attributes\YawAdjust#
End Function


Function SetEntityPositionToWorldPosition(entity,XP#,YP#,ZP#,TargetType,Yaw#,XScale#,YScale#)

	If TargetType=230
		; adjustment for fireflower position (MS why did you put yourself through this??)
		xadjust#=-26.0
		yadjust#=0.0
		
		ScaleThingXCos#=XScale*xadjust*Cos(Yaw#)
		ScaleThingXSin#=XScale*xadjust*Sin(Yaw#)
		ScaleThingYCos#=YScale*yadjust*Cos(Yaw#)
		ScaleThingYSin#=YScale*yadjust*Sin(Yaw#)
		XP#=XP#+ScaleThingXCos#+ScaleThingYSin#
		YP#=YP#-ScaleThingXSin#+ScaleThingYCos#
	EndIf
	
	SetEntityPositionInWorld(entity,XP#,YP#,ZP#)

End Function


Function SetEntityPositionToObjectPosition(entity, Obj.GameObject)

	TheX#=ObjectSumX#(Obj)
	TheY#=ObjectSumY#(Obj)
	TheZ#=ObjectSumZ#(Obj)
	TheYaw#=ObjectSumYaw#(Obj)
	SetEntityPositionToWorldPosition(entity,TheX,TheY,TheZ,Obj\Attributes\LogicType,TheYaw#,Obj\Attributes\XScale,Obj\Attributes\YScale)	

End Function


Function SetEntityPositionToObjectPositionWithoutZ(entity, Obj.GameObject, z#)

	SetEntityPositionInWorld(entity,ObjectSumX#(Obj),ObjectSumY#(Obj),z#)

End Function


Function SetEntityPositionInWorld(entity,x#,y#,z#)

	PositionEntity entity,x#,z#,-y#

End Function


Function UpdateObjectPosition(Dest)

	Obj.GameObject=LevelObjects(Dest)

	SetEntityPositionToObjectPosition(Obj\Model\Entity, Obj)
	
	;PositionEntity ObjectEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest),-ObjectY(Dest)-ObjectYAdjust(Dest)
	
;	If ObjectHatEntity(Dest)>0
;		PositionEntity ObjectHatEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest)+.1+.84*ObjectZScale(Dest)/.035,-ObjectY(Dest)-ObjectYAdjust(Dest)
;	EndIf
	
;	If ObjectAccEntity(Dest)>0
;		PositionEntity ObjectAccEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest)+.1+.84*ObjectZScale(Dest)/.035,-ObjectY(Dest)-ObjectYAdjust(Dest)
;	EndIf
	
	If Obj\Model\HatEntity>0
		TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)
	EndIf
	If Obj\Model\AccEntity>0
		TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)
	EndIf
	
	PositionObjectPositionMarker(Dest)
	
	If IsObjectSelected(Dest)
		UpdateCurrentGrabbedObjectMarkerPosition(Dest)
	EndIf

End Function


Function UpdateObjectEntityToCurrent(Dest)
	
	Obj.GameObject=LevelObjects(Dest)
	Obj\Model\Entity=CopyEntity(CurrentObject\Model\Entity)
	
	UpdateObjectVisibility(Obj)
	
	If CurrentObject\Model\HatEntity>0
	
		Obj\Model\HatEntity=CreateAccEntity(CurrentObject\Attributes\Data2)
		Obj\Model\HatTexture=CreateHatTexture(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		
		ScaleEntity Obj\Model\HatEntity,CurrentObject\Attributes\YScale*CurrentObject\Attributes\ScaleAdjust,CurrentObject\Attributes\ZScale*CurrentObject\Attributes\ScaleAdjust,CurrentObject\Attributes\XScale*CurrentObject\Attributes\ScaleAdjust
		
		;RotateEntity ObjectHatEntity(Dest),0,0,0
		;TurnEntity ObjectHatEntity(Dest),CurrentObject\Attributes\PitchAdjust,0,CurrentObject\Attributes\RollAdjust
		;TurnEntity ObjectHatEntity(Dest),0,CurrentObject\Attributes\YawAdjust-90,0
	
		If Obj\Model\HatTexture=0
			EntityColor Obj\Model\HatEntity,ModelErrorR,ModelErrorG,ModelErrorB
		Else
			EntityTexture Obj\Model\HatEntity,Obj\Model\HatTexture
		EndIf
	EndIf
	
	If CurrentObject\Model\AccEntity>0
		Obj\Model\AccEntity=CreateAccEntity(CurrentObject\Attributes\Data4)
		Obj\Model\AccTexture=CreateGlassesTexture(CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5)
	
	
		ScaleEntity Obj\Model\AccEntity,CurrentObject\Attributes\YScale*CurrentObject\Attributes\ScaleAdjust,CurrentObject\Attributes\ZScale*CurrentObject\Attributes\ScaleAdjust,CurrentObject\Attributes\XScale*CurrentObject\Attributes\ScaleAdjust
		
		;RotateEntity ObjectAccEntity(Dest),0,0,0
		;TurnEntity ObjectAccEntity(Dest),CurrentObject\Attributes\PitchAdjust,0,CurrentObject\Attributes\RollAdjust
		;TurnEntity ObjectAccEntity(Dest),0,CurrentObject\Attributes\YawAdjust-90,0
	
		If Obj\Model\AccTexture=0
			EntityColor Obj\Model\AccEntity,ModelErrorR,ModelErrorG,ModelErrorB
		Else
			EntityTexture Obj\Model\AccEntity,Obj\Model\AccTexture
		EndIf
	EndIf
	
	UpdateObjectAnimation(Obj)
	
	UpdateObjectPosition(Dest)

End Function


Function UpdateObjectAnimation(Obj.GameObject)

	ModelName$=Obj\Attributes\ModelName$
	Entity=Obj\Model\Entity

	If ModelName$="!BabyBoomer"
		AnimateMD2 Entity,0,.2,1,2
	ElseIf ModelName$="!Busterfly"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			AnimateMD2 Entity,2,.4,2,9
		EndIf
	ElseIf ModelName$="!Chomper"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			AnimateMD2 Entity,1,.6,1,29
		EndIf
	ElseIf ModelName$="!Crab"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			Select Obj\Attributes\Data1
			Case 2,3
				; asleep
				AnimateMD2 Entity,3,1,48,49
			End Select
		EndIf
	ElseIf ModelName$="!FireFlower"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		EndIf
	ElseIf ModelName$="!Kaboom"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		EndIf
	ElseIf ModelName$="!NPC"
		If SimulationLevel<SimulationLevelAnimation
			Animate GetChild(Entity,3),0
		Else
			Animate GetChild(Entity,3),1,.05,10
		EndIf
	ElseIf ModelName$="!StinkerWee"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		EndIf
	ElseIf ModelName$="!Tentacle"
		If SimulationLevel<SimulationLevelAnimation
			Animate GetChild(Entity,3),0
		Else
			Animate GetChild(Entity,3),1,.1,1,0
		EndIf
	ElseIf Obj\Attributes\LogicType=290 And ModelName$="!Thwart"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			AnimateMD2 Entity,2,0.005,81,82
		EndIf
	ElseIf ModelName$="!Troll"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			AnimateMD2 Entity,2,0.005,81,82
		EndIf
	EndIf

End Function


; Returns True if the entity gets animated.
Function MaybeAnimate(Entity,mode=1,speed#=1,sequence=0,transition#=0)
	
	If SimulationLevel<SimulationLevelAnimation
		Return False
	Else
		Animate Entity,mode,speed#,sequence,transition#
		Return True
	EndIf
	
End Function

Function MaybeAnimateMD2(Entity,mode=1,speed#=1,FirstFrame=1,LastFrame=1,transition#=0)
	
	If SimulationLevel<SimulationLevelAnimation
		Return False
	Else
		AnimateMD2 Entity,mode,speed#,FirstFrame,LastFrame,transition#
		Return True
	EndIf
	
End Function



Function SomeObjectWasChanged()

	ResetSimulatedQuantities()
	FinalizeCurrentObject()
	;AddUnsavedChange()

End Function


Function SomeTileWasChanged()

	;AddUnsavedChange()

End Function


Function LightingWasChanged()

	SetLightNow(LightRed,LightGreen,LightBlue,AmbientRed,AmbientGreen,AmbientBlue)

End Function


Function ResetSimulatedQuantities()

	For i=0 To NofObjects-1
		Obj.GameObject=LevelObjects(i)
		SimulatedObjectXAdjust(i)=Obj\Attributes\XAdjust
		SimulatedObjectYAdjust(i)=Obj\Attributes\YAdjust
		SimulatedObjectZAdjust(i)=Obj\Attributes\ZAdjust
		SimulatedObjectX(i)=Obj\Position\X
		SimulatedObjectY(i)=Obj\Position\Y
		SimulatedObjectZ(i)=Obj\Position\Z
		SimulatedObjectYaw(i)=Obj\Attributes\Yaw
		SimulatedObjectPitch(i)=Obj\Attributes\Pitch
		SimulatedObjectRoll(i)=Obj\Attributes\Roll
		SimulatedObjectYawAdjust(i)=Obj\Attributes\YawAdjust
		SimulatedObjectPitchAdjust(i)=Obj\Attributes\PitchAdjust
		SimulatedObjectRollAdjust(i)=Obj\Attributes\RollAdjust
		SimulatedObjectYaw2(i)=Obj\Attributes\Yaw2
		SimulatedObjectPitch2(i)=Obj\Attributes\Pitch2
		SimulatedObjectRoll2(i)=Obj\Attributes\Roll2
		SimulatedObjectActive(i)=Obj\Attributes\Active
		SimulatedObjectLastActive(i)=Obj\Attributes\LastActive
		SimulatedObjectXScale(i)=Obj\Attributes\XScale
		SimulatedObjectYScale(i)=Obj\Attributes\YScale
		SimulatedObjectZScale(i)=Obj\Attributes\ZScale
		SimulatedObjectStatus(i)=Obj\Attributes\Status
		SimulatedObjectTimer(i)=Obj\Attributes\Timer
		
		SimulatedObjectData(i,0)=Obj\Attributes\Data0
		SimulatedObjectData(i,1)=Obj\Attributes\Data1
		SimulatedObjectData(i,2)=Obj\Attributes\Data2
		SimulatedObjectData(i,3)=Obj\Attributes\Data3
		SimulatedObjectData(i,4)=Obj\Attributes\Data4
		SimulatedObjectData(i,5)=Obj\Attributes\Data5
		SimulatedObjectData(i,6)=Obj\Attributes\Data6
		SimulatedObjectData(i,7)=Obj\Attributes\Data7
		SimulatedObjectData(i,8)=Obj\Attributes\Data8
		SimulatedObjectData(i,9)=Obj\Attributes\Data9
		
		SimulatedObjectCurrentAnim(i)=Obj\Attributes\CurrentAnim
		SimulatedObjectMovementSpeed(i)=Obj\Attributes\MovementSpeed
		SimulatedObjectMoveXGoal(i)=Obj\Attributes\MoveXGoal
		SimulatedObjectMoveYGoal(i)=Obj\Attributes\MoveYGoal
		SimulatedObjectData10(i)=Obj\Attributes\Data10
		SimulatedObjectSubType(i)=Obj\Attributes\LogicSubType
		SimulatedObjectTileTypeCollision(i)=Obj\Attributes\TileTypeCollision
		SimulatedObjectExclamation(i)=Obj\Attributes\Exclamation
		SimulatedObjectFrozen(i)=Obj\Attributes\Frozen
		If Obj\Attributes\ScaleAdjust<>0.0
			SimulatedObjectXScale(i)=SimulatedObjectXScale(i)*Obj\Attributes\ScaleAdjust
			SimulatedObjectYScale(i)=SimulatedObjectYScale(i)*Obj\Attributes\ScaleAdjust
			SimulatedObjectZScale(i)=SimulatedObjectZScale(i)*Obj\Attributes\ScaleAdjust
		EndIf
		SimulatedObjectScaleXAdjust(i)=Obj\Attributes\ScaleXAdjust
		SimulatedObjectScaleYAdjust(i)=Obj\Attributes\ScaleYAdjust
		SimulatedObjectScaleZAdjust(i)=Obj\Attributes\ScaleZAdjust
		
		; make sure flipbridges are scaled properly
		If Obj\Attributes\LogicType=410
			ControlFlipbridge(i)
		EndIf
	Next
	
	; This solves the flickering issue with objects that change scale in their control code.
	If SimulationLevel>=1
		ControlObjects()
	EndIf

End Function


Function SimulateObjectPosition(Dest)

	XP#=SimulatedObjectX(Dest)+SimulatedObjectXAdjust(Dest)
	YP#=SimulatedObjectY(Dest)+SimulatedObjectYAdjust(Dest)
	ZP#=SimulatedObjectZ(Dest)+SimulatedObjectZAdjust(Dest)
	
	Entity=LevelObjects(Dest)\Model\Entity
	TheType=LevelObjects(Dest)\Attributes\LogicType
	
	SetEntityPositionToWorldPosition(Entity,XP#,YP#,ZP#,TheType,SimulatedObjectYaw(Dest)+SimulatedObjectYawAdjust(Dest),SimulatedObjectXScale(Dest),SimulatedObjectYScale(Dest))

End Function

Function SimulateObjectRotation(Dest)

	Pitch#=SimulatedObjectPitch(Dest)+SimulatedObjectPitchAdjust(Dest)
	Roll#=SimulatedObjectRoll(Dest)+SimulatedObjectRollAdjust(Dest)
	Yaw#=SimulatedObjectYaw(Dest)+SimulatedObjectYawAdjust(Dest)
	
	Entity=LevelObjects(Dest)\Model\Entity
	GameLikeRotation(Entity,Yaw#,Pitch#,Roll#)
	TurnEntity Entity,SimulatedObjectPitch2(Dest),SimulatedObjectYaw2(Dest),SimulatedObjectRoll2(Dest)
	
	ModelName$=LevelObjects(Dest)\Attributes\ModelName$
	If ModelName$="!Troll" Or ModelName$="!Crab" Then TurnEntity Entity,0,-90,0
	If ModelName$="!Kaboom" Or ModelName$="!BabyBoomer" Then TurnEntity Entity,0,90,0

End Function

Function SimulateObjectScale(Dest)

	XS#=SimulatedObjectXScale(Dest)
	YS#=SimulatedObjectYScale(Dest)
	ZS#=SimulatedObjectZScale(Dest)
	
	Entity=LevelObjects(Dest)\Model\Entity
	ScaleEntity Entity,XS#,ZS#,YS#
	
End Function

Function GameLikeRotation(Entity,Yaw#,Pitch#,Roll#)

	RotateEntity Entity,0,0,0
	TurnEntity Entity,Pitch#,0,Roll#
	TurnEntity Entity,0,Yaw#,0

End Function


Function CopyObjectPosition(SourceAttributes.GameObjectPosition,DestAttributes.GameObjectPosition)

	; oldxyz is not grabbed
	DestAttributes\X#=SourceAttributes\X#
	DestAttributes\Y#=SourceAttributes\Y#
	DestAttributes\Z#=SourceAttributes\Z#
	DestAttributes\TileX=SourceAttributes\TileX
	DestAttributes\TileY=SourceAttributes\TileY
	DestAttributes\TileX2=SourceAttributes\TileX2
	DestAttributes\TileY2=SourceAttributes\TileY2

End Function


Function CopyObjectAttributes(SourceAttributes.GameObjectAttributes,DestAttributes.GameObjectAttributes)

	DestAttributes\ModelName$=SourceAttributes\ModelName$
	DestAttributes\TexName$=SourceAttributes\TexName$
	DestAttributes\XScale#=SourceAttributes\XScale#
	DestAttributes\ZScale#=SourceAttributes\ZScale#
	DestAttributes\YScale#=SourceAttributes\YScale#
	DestAttributes\XAdjust#=SourceAttributes\XAdjust#
	DestAttributes\ZAdjust#=SourceAttributes\ZAdjust#
	DestAttributes\YAdjust#=SourceAttributes\YAdjust#
	DestAttributes\PitchAdjust#=SourceAttributes\PitchAdjust#
	DestAttributes\YawAdjust#=SourceAttributes\YawAdjust#
	DestAttributes\RollAdjust#=SourceAttributes\RollAdjust#
	DestAttributes\DX#=SourceAttributes\DX#
	DestAttributes\DY#=SourceAttributes\DY#
	DestAttributes\DZ#=SourceAttributes\DZ#
	DestAttributes\Pitch#=SourceAttributes\Pitch#
	DestAttributes\Yaw#=SourceAttributes\Yaw#
	DestAttributes\Roll#=SourceAttributes\Roll#
	DestAttributes\Pitch2#=SourceAttributes\Pitch2#
	DestAttributes\Yaw2#=SourceAttributes\Yaw2#
	DestAttributes\Roll2#=SourceAttributes\Roll2#
	DestAttributes\XGoal#=SourceAttributes\XGoal#
	DestAttributes\YGoal#=SourceAttributes\YGoal#
	DestAttributes\ZGoal#=SourceAttributes\ZGoal#
	DestAttributes\MovementType=SourceAttributes\MovementType
	DestAttributes\MovementTypeData=SourceAttributes\MovementTypeData
	DestAttributes\Speed#=SourceAttributes\Speed#
	DestAttributes\Radius#=SourceAttributes\Radius#
	DestAttributes\RadiusType=SourceAttributes\RadiusType
	DestAttributes\Data10=SourceAttributes\Data10
	DestAttributes\PushDX#=SourceAttributes\PushDX#
	DestAttributes\PushDY#=SourceAttributes\PushDY#
	DestAttributes\AttackPower=SourceAttributes\AttackPower
	DestAttributes\DefensePower=SourceAttributes\DefensePower
	DestAttributes\DestructionType=SourceAttributes\DestructionType
	DestAttributes\ID=SourceAttributes\ID
	DestAttributes\LogicType=SourceAttributes\LogicType
	DestAttributes\LogicSubType=SourceAttributes\LogicSubType
	DestAttributes\Active=SourceAttributes\Active
	DestAttributes\LastActive=SourceAttributes\LastActive
	DestAttributes\ActivationType=SourceAttributes\ActivationType
	DestAttributes\ActivationSpeed=SourceAttributes\ActivationSpeed
	DestAttributes\Status=SourceAttributes\Status
	DestAttributes\Timer=SourceAttributes\Timer
	DestAttributes\TimerMax1=SourceAttributes\TimerMax1
	DestAttributes\TimerMax2=SourceAttributes\TimerMax2
	DestAttributes\Teleportable=SourceAttributes\Teleportable
	DestAttributes\ButtonPush=SourceAttributes\ButtonPush
	DestAttributes\WaterReact=SourceAttributes\WaterReact
	DestAttributes\Telekinesisable=SourceAttributes\Telekinesisable
	DestAttributes\Freezable=SourceAttributes\Freezable
	DestAttributes\Reactive=SourceAttributes\Reactive
	DestAttributes\Child=SourceAttributes\Child
	DestAttributes\Parent=SourceAttributes\Parent
	
	DestAttributes\Data0=SourceAttributes\Data0
	DestAttributes\Data1=SourceAttributes\Data1
	DestAttributes\Data2=SourceAttributes\Data2
	DestAttributes\Data3=SourceAttributes\Data3
	DestAttributes\Data4=SourceAttributes\Data4
	DestAttributes\Data5=SourceAttributes\Data5
	DestAttributes\Data6=SourceAttributes\Data6
	DestAttributes\Data7=SourceAttributes\Data7
	DestAttributes\Data8=SourceAttributes\Data8
	DestAttributes\Data9=SourceAttributes\Data9
	
	DestAttributes\TextData0$=SourceAttributes\TextData0$
	DestAttributes\TextData1$=SourceAttributes\TextData1$
	DestAttributes\TextData2$=SourceAttributes\TextData2$
	DestAttributes\TextData3$=SourceAttributes\TextData3$
	
	DestAttributes\Talkable=SourceAttributes\Talkable
	DestAttributes\CurrentAnim=SourceAttributes\CurrentAnim
	DestAttributes\StandardAnim=SourceAttributes\StandardAnim
	DestAttributes\MovementTimer=SourceAttributes\MovementTimer
	DestAttributes\MovementSpeed=SourceAttributes\MovementSpeed
	DestAttributes\MoveXGoal=SourceAttributes\MoveXGoal
	DestAttributes\MoveYGoal=SourceAttributes\MoveYGoal
	DestAttributes\TileTypeCollision=SourceAttributes\TileTypeCollision
	DestAttributes\ObjectTypeCollision=SourceAttributes\ObjectTypeCollision
	DestAttributes\Caged=SourceAttributes\Caged
	DestAttributes\Dead=SourceAttributes\Dead
	DestAttributes\DeadTimer=SourceAttributes\DeadTimer
	DestAttributes\Exclamation=SourceAttributes\Exclamation
	DestAttributes\Shadow=SourceAttributes\Shadow
	DestAttributes\Linked=SourceAttributes\Linked
	DestAttributes\LinkBack=SourceAttributes\LinkBack
	DestAttributes\Flying=SourceAttributes\Flying
	DestAttributes\Frozen=SourceAttributes\Frozen
	DestAttributes\Indigo=SourceAttributes\Indigo
	DestAttributes\FutureInt24=SourceAttributes\FutureInt24
	DestAttributes\FutureInt25=SourceAttributes\FutureInt25

	DestAttributes\ScaleAdjust=SourceAttributes\ScaleAdjust
	DestAttributes\ScaleXAdjust=SourceAttributes\ScaleXAdjust
	DestAttributes\ScaleYAdjust=SourceAttributes\ScaleYAdjust
	DestAttributes\ScaleZAdjust=SourceAttributes\ScaleZAdjust
	DestAttributes\FutureFloat5=SourceAttributes\FutureFloat5
	DestAttributes\FutureFloat6=SourceAttributes\FutureFloat6
	DestAttributes\FutureFloat7=SourceAttributes\FutureFloat7
	DestAttributes\FutureFloat8=SourceAttributes\FutureFloat8
	DestAttributes\FutureFloat9=SourceAttributes\FutureFloat9
	DestAttributes\FutureFloat10=SourceAttributes\FutureFloat10
	DestAttributes\FutureString1$=SourceAttributes\FutureString1$
	DestAttributes\FutureString2$=SourceAttributes\FutureString2$

End Function


Function ObjectIsAtInt(Obj.GameObject,x,y)

	MyX#=Obj\Position\X-GetObjectOffset#(Obj\Attributes,0)
	MyY#=Obj\Position\Y-GetObjectOffset#(Obj\Attributes,1)
	Return Floor(MyX#)=x And Floor(MyY#)=y

End Function

Function ObjectIsAtFloat(Obj.GameObject,x#,y#)

	Return ObjectIsAtInt(Obj,Floor(x),Floor(y))

End Function

Function GetFirstObjectAtFloat(x#,y#)

	For i=0 To NofObjects-1
		If ObjectIsAtFloat(LevelObjects(i),x#,y#)
			Return i
		EndIf
	Next
	Return -1

End Function

Function TryGrabObjectLoop(x#,y#,Target)
	For i=0 To NofObjects-1
		If ObjectIsAtFloat(LevelObjects(i),x#,y#) And i>Target
			AddOrToggleSelectObject(i)
			Return True
		EndIf
	Next
	Return False
End Function

Function GrabObject(x#,y#,SelectAllOnTile)

	If LevelTileObjectCount(Floor(x#),Floor(y#))=0
		Return
	EndIf
	
	If SelectAllOnTile
		For i=0 To NofObjects-1
			If ObjectIsAtFloat(LevelObjects(i),x#,y#)
				AddOrToggleSelectObject(i)
			EndIf
		Next
	Else
		Flag=TryGrabObjectLoop(x#,y#,PreviousSelectedObject)
		If Flag=False
			; restart the cycle
			Flag=TryGrabObjectLoop(x#,y#,-1)
;			If Flag=False
;				; no object found
;				Return
;			EndIf
		EndIf
	EndIf

End Function
	
Function ReadObjectIntoCurrentObject(Obj.GameObject)

	If ReadyToCopyFirstSelected=True
		ReadyToCopyFirstSelected=False
		
		NofWopAdjusters=0
	
		CopyObjectAttributes(Obj\Attributes,CurrentObject\Attributes)
		CopyObjectPosition(Obj\Position,CurrentObject\Position)
	
		CurrentObject\Position\X#=CurrentObject\Position\X#-x-0.5
		CurrentObject\Position\Y#=CurrentObject\Position\Y#-y-0.5
		
		;NofObjectAdjusters=0
		;ObjectAdjusterStart=0
		;For i=0 To 30
		;	ObjectAdjuster$(i)=ObjectAdjusterString$(Dest,i)
		;	If ObjectAdjuster$(i) <>""
		;		NofObjectAdjusters=NofObjectAdjusters+1
		;	EndIf
		;Next
		
		MakeAllObjectAdjustersAbsolute()
		
		;BuildCurrentObjectModel()
	Else
		CompareObjectToCurrent(Obj)
	EndIf

End Function



Function CopyObjectFromBrush(i,DestObject.GameObject)

	CopyObjectAttributes(BrushObjects(i)\Attributes,DestObject\Attributes)
	CopyObjectPosition(BrushObjects(i)\Position,DestObject\Position)

End Function



Function CreateObjectPositionMarker(i)

	ObjectPositionMarker(i)=CopyEntity(ObjectPositionMarkerMesh)
	EntityAlpha ObjectPositionMarker(i),.8
	PositionObjectPositionMarker(i)

	UpdateObjectPositionMarkersAtTile(LevelObjects(i)\Position\TileX,LevelObjects(i)\Position\TileY)
	
	If ShowObjectPositions=False
		HideEntity ObjectPositionMarker(i)
	EndIf
	
End Function

Function IncrementLevelTileObjectCount(x,y)

	LevelTileObjectCount(x,y)=LevelTileObjectCount(x,y)+1
	UpdateObjectPositionMarkersAtTile(x,y)

End Function

Function DecrementLevelTileObjectCount(x,y)

	LevelTileObjectCount(x,y)=LevelTileObjectCount(x,y)-1
	UpdateObjectPositionMarkersAtTile(x,y)

End Function

Function IncrementLevelTileObjectCountFor(Attributes.GameObjectPosition)

	IncrementLevelTileObjectCount(Attributes\TileX,Attributes\TileY)

End Function

Function DecrementLevelTileObjectCountFor(Attributes.GameObjectPosition)

	DecrementLevelTileObjectCount(Attributes\TileX,Attributes\TileY)

End Function

Function PositionObjectPositionMarker(i)

	Obj.GameObject=LevelObjects(i)
	PositionEntityInLevel(ObjectPositionMarker(i),Obj\Position\X,Obj\Position\Y)

End Function

Function PositionEntityInLevel(Entity,x#,y#)

	PositionEntity Entity,x#,0,-y#

End Function

Function UpdateObjectPositionMarkersAtTile(tilex,tiley)

	;ShowMessage("Updating object position markers...", 100)

	;LevelTileObjectCount(tilex,tiley)=0

	For i=0 To NofObjects-1
		If LevelObjects(i)\Position\TileX=tilex And LevelObjects(i)\Position\TileY=tiley
		;If ObjectIsAtInt(i,tilex,tiley)
			If LevelTileObjectCount(tilex,tiley)=1
				EntityColor ObjectPositionMarker(i),255,100,100
			ElseIf LevelTileObjectCount(tilex,tiley)>1
				EntityColor ObjectPositionMarker(i),255,255,100
			Else ; ERROR: LevelTileObjectCount is zero or less!
				EntityColor ObjectPositionMarker(i),255,100,255
			EndIf
		EndIf
	Next
	
	;ShowMessage("Update successful.", 100)

End Function

Function FreeObjectModel(Model.GameObjectModel)

	If Model\Entity>0 
		FreeEntity Model\Entity
		Model\Entity=0
	EndIf
	If Model\Texture>0 
		FreeTexture Model\Texture
		Model\Texture=0
	EndIf
	
	If Model\HatEntity>0
		FreeEntity Model\HatEntity
		Model\HatEntity=0
	EndIf
	If Model\AccEntity>0
		FreeEntity Model\AccEntity
		Model\AccEntity=0
	EndIf
	If Model\HatTexture>0
		FreeTexture Model\HatTexture
		Model\HatTexture=0
	EndIf
	If Model\AccTexture>0
		FreeTexture Model\AccTexture
		Model\AccTexture=0
	EndIf

End Function

Function FreeObject(i)

	Obj.GameObject=LevelObjects(i)

	FreeObjectModel(Obj\Model)
	
	tilex=Obj\Position\TileX
	tiley=Obj\Position\TileY
	LevelTileObjectCount(tilex,tiley)=LevelTileObjectCount(tilex,tiley)-1

	If ObjectPositionMarker(i)>0
		FreeEntity ObjectPositionMarker(i)
		ObjectPositionMarker(i)=0
	EndIf

End Function

; Move object indices around without deleting any of them.
Function SetObjectIndex(SourceIndex,TargetIndex)

	If TargetIndex<SourceIndex
		MoveObjectData(SourceIndex,1000) ; Move to temp.
		i=SourceIndex-1
		While i>=TargetIndex
			MoveObjectData(i,i+1)
			i=i-1
		Wend
		MoveObjectData(1000,TargetIndex)
		
		For j=0 To NofObjects-1
			Obj.GameObject=LevelObjects(j)
			
			If Obj\Attributes\Linked=SourceIndex
				Obj\Attributes\Linked=TargetIndex
			Else If Obj\Attributes\Linked>=TargetIndex And Obj\Attributes\Linked<SourceIndex
				Obj\Attributes\Linked=Obj\Attributes\Linked+1
			EndIf
			
			If Obj\Attributes\LinkBack=SourceIndex
				Obj\Attributes\LinkBack=TargetIndex
			Else If Obj\Attributes\LinkBack>=TargetIndex And Obj\Attributes\LinkBack<SourceIndex
				Obj\Attributes\LinkBack=Obj\Attributes\LinkBack+1
			EndIf
			
			If Obj\Attributes\Parent=SourceIndex
				Obj\Attributes\Parent=TargetIndex
			Else If Obj\Attributes\Parent>=TargetIndex And Obj\Attributes\Parent<SourceIndex
				Obj\Attributes\Parent=Obj\Attributes\Parent+1
			EndIf
			
			If Obj\Attributes\Child=SourceIndex
				Obj\Attributes\Child=TargetIndex
			Else If Obj\Attributes\Child>=TargetIndex And Obj\Attributes\Child<SourceIndex
				Obj\Attributes\Child=Obj\Attributes\Child+1
			EndIf
		Next
		
		SomeObjectWasChanged()
	ElseIf TargetIndex>SourceIndex
		MoveObjectData(SourceIndex,1000) ; Move to temp.
		i=SourceIndex+1
		While i<=TargetIndex
			MoveObjectData(i,i-1)
			i=i+1
		Wend
		MoveObjectData(1000,TargetIndex)
		
		For j=0 To NofObjects-1
			Obj.GameObject=LevelObjects(j)
			
			If Obj\Attributes\Linked=SourceIndex
				Obj\Attributes\Linked=TargetIndex
			Else If Obj\Attributes\Linked>SourceIndex And Obj\Attributes\Linked<=TargetIndex
				Obj\Attributes\Linked=Obj\Attributes\Linked-1
			EndIf
			
			If Obj\Attributes\LinkBack=SourceIndex
				Obj\Attributes\LinkBack=TargetIndex
			Else If Obj\Attributes\LinkBack>SourceIndex And Obj\Attributes\LinkBack<=TargetIndex
				Obj\Attributes\LinkBack=Obj\Attributes\LinkBack-1
			EndIf
			
			If Obj\Attributes\Parent=SourceIndex
				Obj\Attributes\Parent=TargetIndex
			Else If Obj\Attributes\Parent>SourceIndex And Obj\Attributes\Parent<=TargetIndex
				Obj\Attributes\Parent=Obj\Attributes\Parent-1
			EndIf
			
			If Obj\Attributes\Child=SourceIndex
				Obj\Attributes\Child=TargetIndex
			Else If Obj\Attributes\Child>SourceIndex And Obj\Attributes\Child<=TargetIndex
				Obj\Attributes\Child=Obj\Attributes\Child-1
			EndIf
		Next
		
		SomeObjectWasChanged()
	EndIf

End Function

Function DeleteObject(i)

	;ShowMessage("Deleting object "+i, 100)

	FreeObject(i)
	
	If IsObjectSelected(i)
		RemoveSelectObject(i)
	EndIf
	For j=0 To NofSelectedObjects-1
		If i<SelectedObjects(j)
			SelectedObjects(j)=SelectedObjects(j)-1
		EndIf
	Next
	
	If IsObjectDragged(i)
		RemoveDraggedObject(i)
	EndIf
	For j=0 To NofDraggedObjects-1
		If i<DraggedObjects(j)
			DraggedObjects(j)=DraggedObjects(j)-1
		EndIf
	Next
	
	;ShowMessage("Moving object data...", 100)

	For j=i+1 To NofObjects-1
		MoveObjectData(j,j-1)
	Next

	;ShowMessage("Setting current grabbed object...", 100)
	
	NofObjects=NofObjects-1
	
	For j=0 To NofObjects-1
		Obj.GameObject=LevelObjects(j)
		
		If Obj\Attributes\Linked=i
			Obj\Attributes\Linked=-1
		Else If Obj\Attributes\Linked>i
			Obj\Attributes\Linked=Obj\Attributes\Linked-1
		EndIf
		
		If Obj\Attributes\LinkBack=i
			Obj\Attributes\LinkBack=-1
		Else If Obj\Attributes\LinkBack>i
			Obj\Attributes\LinkBack=Obj\Attributes\LinkBack-1
		EndIf
		
		If Obj\Attributes\Parent=i
			Obj\Attributes\Parent=-1
		Else If Obj\Attributes\Parent>i
			Obj\Attributes\Parent=Obj\Attributes\Parent-1
		EndIf
		
		If Obj\Attributes\Child=i
			Obj\Attributes\Child=-1
		Else If Obj\Attributes\Child>i
			Obj\Attributes\Child=Obj\Attributes\Child-1
		EndIf
	Next
	
	If CurrentObject\Attributes\Linked=i
		CurrentObject\Attributes\Linked=-1
	Else If CurrentObject\Attributes\Linked>i
		CurrentObject\Attributes\Linked=CurrentObject\Attributes\Linked-1
	EndIf
	
	If CurrentObject\Attributes\LinkBack=i
		CurrentObject\Attributes\LinkBack=-1
	Else If CurrentObject\Attributes\LinkBack>i
		CurrentObject\Attributes\LinkBack=CurrentObject\Attributes\LinkBack-1
	EndIf
	
	If CurrentObject\Attributes\Parent=i
		CurrentObject\Attributes\Parent=-1
	Else If CurrentObject\Attributes\Parent>i
		CurrentObject\Attributes\Parent=CurrentObject\Attributes\Parent-1
	EndIf
	
	If CurrentObject\Attributes\Child=i
		CurrentObject\Attributes\Child=-1
	Else If CurrentObject\Attributes\Child>i
		CurrentObject\Attributes\Child=CurrentObject\Attributes\Child-1
	EndIf
	
	UpdateObjectPositionMarkersAtTile(tilex,tiley)
	
	SomeObjectWasChanged()
	

End Function

Function DeleteObjectAt(x,y)
	
	If Not PassesPlacementDensityTest()
		Return
	EndIf
	
	DeleteObjectAtActual(x,y)
	
	If DupeMode=DupeModeX
		TargetX=MirrorAcrossInt(x,MirrorPositionX)
		DeleteObjectAtActual(TargetX,y)
	ElseIf DupeMode=DupeModeY
		TargetY=MirrorAcrossInt(y,MirrorPositionY)
		DeleteObjectAtActual(x,TargetY)
	ElseIf DupeMode=DupeModeXPlusY
		TargetX=MirrorAcrossInt(x,MirrorPositionX)
		TargetY=MirrorAcrossInt(y,MirrorPositionY)
		DeleteObjectAtActual(TargetX,y)
		DeleteObjectAtActual(x,TargetY)
		DeleteObjectAtActual(TargetX,TargetY)
	EndIf
	
End Function

Function DeleteObjectAtActual(x,y)
	
	DeleteCount=0
	For i=0 To NofObjects-1
		Pos.GameObjectPosition=LevelObjects(i)\Position
		If Floor(Pos\X)=x And Floor(Pos\Y)=y
			DeleteObject(i)
			SetEditorMode(3)
			i=i-1
			DeleteCount=DeleteCount+1
		EndIf
	Next
	;ShowMessage(DeleteCount, 1000)
	Return DeleteCount
	
End Function

Function MoveObjectData(Source,Dest)

	ObjSource.GameObject=LevelObjects(Source)
	ObjDest.GameObject=LevelObjects(Dest)

	MoveObjectModel(ObjSource\Model,ObjDest\Model)
	
	
	CopyObjectAttributes(ObjSource\Attributes,ObjDest\Attributes)
	CopyObjectPosition(ObjSource\Position,ObjDest\Position)
		
	;For i=0 To 30
	;	ObjectAdjusterString$(Dest,i)=ObjectAdjusterString$(Source,i)
	;Next
	
	ObjectPositionMarker(Dest)=ObjectPositionMarker(Source)
	
End Function


Function MoveObjectModel(Source.GameObjectModel,Dest.GameObjectModel)

	Dest\Entity=Source\Entity
	Dest\Texture=Source\Texture
	Dest\HatEntity=Source\HatEntity
	Dest\AccEntity=Source\AccEntity
	Dest\HatTexture=Source\HatTexture
	Dest\AccTexture=Source\AccTexture
	; making sure there is no aliasing since that previously caused occasional MAVs
	Source\Entity=0
	Source\Texture=0
	Source\HatEntity=0
	Source\AccEntity=0
	Source\HatTexture=0
	Source\AccTexture=0

End Function


Function CopyObjectToBrush(Source.GameObject,Dest,XOffset,YOffset)

	;BrushObjectXOffset#(Dest)=XOffset#-0.5
	;BrushObjectYOffset#(Dest)=YOffset#-0.5
	BrushObjectTileXOffset(Dest)=XOffset
	BrushObjectTileYOffset(Dest)=YOffset
	
	;BrushObjects(Dest)\X=0.5 ;ObjectX(Source)
	;BrushObjects(Dest)\Y=0.5 ;ObjectY(Source)
	;BrushObjects(Dest)\Z=LevelObjects(Source)\Attributes\Z
	;BrushObjectTileX(Dest)=ObjectTileX(Source)
	;BrushObjectTileY(Dest)=ObjectTileY(Source)
	;BrushObjectTileX2(Dest)=ObjectTileX2(Source)
	;BrushObjectTileY2(Dest)=ObjectTileY2(Source)
	
	CopyObjectAttributes(Source\Attributes,BrushObjects(Dest)\Attributes)
	CopyObjectPosition(Source\Position,BrushObjects(Dest)\Position)
	CopyObjectModel(Source\Model,BrushObjects(Dest)\Model)
	HideObjectModel(BrushObjects(Dest)\Model)

End Function


Function CopySelectedObjectsToBrush()

	; set custom brush
	If EditorMode=3 And NofSelectedObjects<>0
		RecalculateSelectionSize()
	
		NofBrushObjects=NofSelectedObjects
		BrushSpaceWidth=SelectionMaxTileX-SelectionMinTileX+1
		BrushSpaceHeight=SelectionMaxTileY-SelectionMinTileY+1
		BrushSpaceOriginX=SelectionMinTileX+BrushSpaceWidth/2
		BrushSpaceOriginY=SelectionMinTileY+BrushSpaceHeight/2
		BrushWidth=BrushSpaceWidth
		BrushHeight=BrushSpaceHeight
		For i=0 To NofSelectedObjects-1
			LevelObject.GameObject=LevelObjects(SelectedObjects(i))
			BrushSpaceX=LevelSpaceToBrushSpaceX(LevelObject\Position\TileX,BrushWrapModulus)
			BrushSpaceY=LevelSpaceToBrushSpaceY(LevelObject\Position\TileY,BrushWrapModulus)
			CopyObjectToBrush(LevelObject,i,BrushSpaceX,BrushSpaceY)
		Next
		BrushCursorStateWasChanged()
	EndIf

End Function


Function CopyObjectModel(Source.GameObjectModel,Dest.GameObjectModel)

	FreeObjectModel(Dest)
	
	If Source\Entity>0
		Dest\Entity=CopyEntity(Source\Entity)
	EndIf
	If Source\Texture>0
		Dest\Texture=0 ;Source\Texture
	EndIf
	If Source\HatEntity>0
		Dest\HatEntity=CopyEntity(Source\HatEntity)
	EndIf
	If Source\AccEntity>0
		Dest\AccEntity=CopyEntity(Source\AccEntity)
	EndIf
	If Source\HatTexture>0
		Dest\HatTexture=0 ;Source\HatTexture
	EndIf
	If Source\AccTexture>0
		Dest\AccTexture=0 ;Source\AccTexture
	EndIf

End Function


Function UpdateSelectedObjects()

	SetEditorMode(3)
	For i=0 To NofSelectedObjects-1
		CurrentGrabbedObject=SelectedObjects(i)
		PasteObjectData(CurrentGrabbedObject)
	Next
	;If AreAllObjectAdjustersAbsolute() ; Allows reapplication of the same relative change.
	;	CurrentGrabbedObjectModified=False
	;EndIf
	
	; Zero all relative object adjusters.
	RecalculateObjectAdjusterModes()
	
	AddUnsavedChange()

End Function


Function UpdateSelectedObjectsIfExists()

	If NofSelectedObjects<>0 And CurrentGrabbedObjectModified
		UpdateSelectedObjects()
	EndIf

End Function


Function PasteObjectData(Dest)

	;xy position is not changed
	
	;CopyObjectAttributes(CurrentObject\Attributes,LevelObjects(Dest)\Attributes)
	
	SourceObject.GameObject=CurrentObject
	DestObject.GameObject=LevelObjects(Dest)
	
	SourceAttributes.GameObjectAttributes=SourceObject\Attributes
	DestAttributes.GameObjectAttributes=DestObject\Attributes
	
	If ObjectAdjusterZ\Absolute
		DestObject\Position\Z=SourceObject\Position\Z
	EndIf
	
	If ObjectAdjusterModelName\Absolute
		DestAttributes\ModelName$=SourceAttributes\ModelName$
	EndIf
	If ObjectAdjusterTextureName\Absolute
		DestAttributes\TexName$=SourceAttributes\TexName$
	EndIf
	
	If ObjectAdjusterXScale\Absolute
		DestAttributes\XScale#=SourceAttributes\XScale#
	Else
		DestAttributes\XScale#=DestAttributes\XScale#+SourceAttributes\XScale#
	EndIf
	If ObjectAdjusterYScale\Absolute
		DestAttributes\YScale#=SourceAttributes\YScale#
	Else
		DestAttributes\YScale#=DestAttributes\YScale#+SourceAttributes\YScale#
	EndIf
	If ObjectAdjusterZScale\Absolute
		DestAttributes\ZScale#=SourceAttributes\ZScale#
	Else
		DestAttributes\ZScale#=DestAttributes\ZScale#+SourceAttributes\ZScale#
	EndIf
	
	If ObjectAdjusterXAdjust\Absolute
		DestAttributes\XAdjust#=SourceAttributes\XAdjust#
	Else
		DestAttributes\XAdjust#=DestAttributes\XAdjust#+SourceAttributes\XAdjust#
	EndIf
	If ObjectAdjusterYAdjust\Absolute
		DestAttributes\YAdjust#=SourceAttributes\YAdjust#
	Else
		DestAttributes\YAdjust#=DestAttributes\YAdjust#+SourceAttributes\YAdjust#
	EndIf
	If ObjectAdjusterZAdjust\Absolute
		DestAttributes\ZAdjust#=SourceAttributes\ZAdjust#
	Else
		DestAttributes\ZAdjust#=DestAttributes\ZAdjust#+SourceAttributes\ZAdjust#
	EndIf
	
	If ObjectAdjusterPitchAdjust\Absolute
		DestAttributes\PitchAdjust#=SourceAttributes\PitchAdjust#
	Else
		DestAttributes\PitchAdjust#=DestAttributes\PitchAdjust#+SourceAttributes\PitchAdjust#
	EndIf
	If ObjectAdjusterYawAdjust\Absolute
		DestAttributes\YawAdjust#=SourceAttributes\YawAdjust#
	Else
		DestAttributes\YawAdjust#=DestAttributes\YawAdjust#+SourceAttributes\YawAdjust#
	EndIf
	If ObjectAdjusterRollAdjust\Absolute
		DestAttributes\RollAdjust#=SourceAttributes\RollAdjust#
	Else
		DestAttributes\RollAdjust#=DestAttributes\RollAdjust#+SourceAttributes\RollAdjust#
	EndIf
	
	If ObjectAdjusterDX\Absolute
		DestAttributes\DX#=SourceAttributes\DX#
	Else
		DestAttributes\DX#=DestAttributes\DX#+SourceAttributes\DX#
	EndIf
	If ObjectAdjusterDY\Absolute
		DestAttributes\DY#=SourceAttributes\DY#
	Else
		DestAttributes\DY#=DestAttributes\DY#+SourceAttributes\DY#
	EndIf
	If ObjectAdjusterDZ\Absolute
		DestAttributes\DZ#=SourceAttributes\DZ#
	Else
		DestAttributes\DZ#=DestAttributes\DZ#+SourceAttributes\DZ#
	EndIf
	
	DestAttributes\Pitch#=SourceAttributes\Pitch#
	DestAttributes\Yaw#=SourceAttributes\Yaw#
	DestAttributes\Roll#=SourceAttributes\Roll#
	DestAttributes\Pitch2#=SourceAttributes\Pitch2#
	DestAttributes\Yaw2#=SourceAttributes\Yaw2#
	DestAttributes\Roll2#=SourceAttributes\Roll2#
	DestAttributes\XGoal#=SourceAttributes\XGoal#
	DestAttributes\YGoal#=SourceAttributes\YGoal#
	DestAttributes\ZGoal#=SourceAttributes\ZGoal#
	
	If ObjectAdjusterMovementType\Absolute
		DestAttributes\MovementType=SourceAttributes\MovementType
	Else
		DestAttributes\MovementType=DestAttributes\MovementType+SourceAttributes\MovementType
	EndIf
	If ObjectAdjusterMovementTypeData\Absolute
		DestAttributes\MovementTypeData=SourceAttributes\MovementTypeData
	Else
		DestAttributes\MovementTypeData=DestAttributes\MovementTypeData+SourceAttributes\MovementTypeData
	EndIf
	
	DestAttributes\Speed#=SourceAttributes\Speed#
	DestAttributes\Radius#=SourceAttributes\Radius#
	DestAttributes\RadiusType=SourceAttributes\RadiusType
	
	If ObjectAdjusterData10\Absolute
		DestAttributes\Data10=SourceAttributes\Data10
	Else
		DestAttributes\Data10=DestAttributes\Data10+SourceAttributes\Data10
	EndIf
	
	DestAttributes\PushDX#=SourceAttributes\PushDX#
	DestAttributes\PushDY#=SourceAttributes\PushDY#
	DestAttributes\AttackPower=SourceAttributes\AttackPower
	
	If ObjectAdjusterDefensePower\Absolute
		DestAttributes\DefensePower=SourceAttributes\DefensePower
	Else
		DestAttributes\DefensePower=DestAttributes\DefensePower+SourceAttributes\DefensePower
	EndIf
	If ObjectAdjusterDestructionType\Absolute
		DestAttributes\DestructionType=SourceAttributes\DestructionType
	Else
		DestAttributes\DestructionType=DestAttributes\DestructionType+SourceAttributes\DestructionType
	EndIf
	If ObjectAdjusterID\Absolute
		DestAttributes\ID=SourceAttributes\ID
	Else
		DestAttributes\ID=DestAttributes\ID+SourceAttributes\ID
	EndIf
	If ObjectAdjusterLogicType\Absolute
		DestAttributes\LogicType=SourceAttributes\LogicType
	Else
		DestAttributes\LogicType=DestAttributes\LogicType+SourceAttributes\LogicType
	EndIf
	If ObjectAdjusterLogicSubType\Absolute
		DestAttributes\LogicSubType=SourceAttributes\LogicSubType
	Else
		DestAttributes\LogicSubType=DestAttributes\LogicSubType+SourceAttributes\LogicSubType
	EndIf
	If ObjectAdjusterActive\Absolute
		DestAttributes\Active=SourceAttributes\Active
	Else
		DestAttributes\Active=DestAttributes\Active+SourceAttributes\Active
	EndIf
	
	DestAttributes\LastActive=SourceAttributes\LastActive
	
	If ObjectAdjusterActivationType\Absolute
		DestAttributes\ActivationType=SourceAttributes\ActivationType
	Else
		DestAttributes\ActivationType=DestAttributes\Active+SourceAttributes\ActivationType
	EndIf
	If ObjectAdjusterActivationSpeed\Absolute
		DestAttributes\ActivationSpeed=SourceAttributes\ActivationSpeed
	Else
		DestAttributes\ActivationSpeed=DestAttributes\Active+SourceAttributes\ActivationSpeed
	EndIf
	If ObjectAdjusterStatus\Absolute
		DestAttributes\Status=SourceAttributes\Status
	Else
		DestAttributes\Status=DestAttributes\Status+SourceAttributes\Status
	EndIf
	If ObjectAdjusterTimer\Absolute
		DestAttributes\Timer=SourceAttributes\Timer
	Else
		DestAttributes\Timer=DestAttributes\Timer+SourceAttributes\Timer
	EndIf
	If ObjectAdjusterTimerMax1\Absolute
		DestAttributes\TimerMax1=SourceAttributes\TimerMax1
	Else
		DestAttributes\TimerMax1=DestAttributes\TimerMax1+SourceAttributes\TimerMax1
	EndIf
	If ObjectAdjusterTimerMax2\Absolute
		DestAttributes\TimerMax2=SourceAttributes\TimerMax2
	Else
		DestAttributes\TimerMax2=DestAttributes\TimerMax2+SourceAttributes\TimerMax2
	EndIf
	If ObjectAdjusterTeleportable\Absolute
		DestAttributes\Teleportable=SourceAttributes\Teleportable
	Else
		DestAttributes\Teleportable=DestAttributes\Teleportable+SourceAttributes\Teleportable
	EndIf
	If ObjectAdjusterButtonPush\Absolute
		DestAttributes\ButtonPush=SourceAttributes\ButtonPush
	Else
		DestAttributes\ButtonPush=DestAttributes\ButtonPush+SourceAttributes\ButtonPush
	EndIf
	If ObjectAdjusterWaterReact\Absolute
		DestAttributes\WaterReact=SourceAttributes\WaterReact
	Else
		DestAttributes\WaterReact=DestAttributes\WaterReact+SourceAttributes\WaterReact
	EndIf
	
	DestAttributes\Telekinesisable=SourceAttributes\Telekinesisable
	DestAttributes\Freezable=SourceAttributes\Freezable
	DestAttributes\Reactive=SourceAttributes\Reactive
	
	
	If ObjectAdjusterChild\Absolute
		DestAttributes\Child=SourceAttributes\Child
	Else
		DestAttributes\Child=DestAttributes\Child+SourceAttributes\Child
	EndIf
	If ObjectAdjusterParent\Absolute
		DestAttributes\Parent=SourceAttributes\Parent
	Else
		DestAttributes\Parent=DestAttributes\Parent+SourceAttributes\Parent
	EndIf
	
	If ObjectAdjusterData0\Absolute
		DestAttributes\Data0=SourceAttributes\Data0
	Else
		DestAttributes\Data0=DestAttributes\Data0+SourceAttributes\Data0
	EndIf
	If ObjectAdjusterData1\Absolute
		DestAttributes\Data1=SourceAttributes\Data1
	Else
		DestAttributes\Data1=DestAttributes\Data1+SourceAttributes\Data1
	EndIf
	If ObjectAdjusterData2\Absolute
		DestAttributes\Data2=SourceAttributes\Data2
	Else
		DestAttributes\Data2=DestAttributes\Data2+SourceAttributes\Data2
	EndIf
	If ObjectAdjusterData3\Absolute
		DestAttributes\Data3=SourceAttributes\Data3
	Else
		DestAttributes\Data3=DestAttributes\Data3+SourceAttributes\Data3
	EndIf
	If ObjectAdjusterData4\Absolute
		DestAttributes\Data4=SourceAttributes\Data4
	Else
		DestAttributes\Data4=DestAttributes\Data4+SourceAttributes\Data4
	EndIf
	If ObjectAdjusterData5\Absolute
		DestAttributes\Data5=SourceAttributes\Data5
	Else
		DestAttributes\Data5=DestAttributes\Data5+SourceAttributes\Data5
	EndIf
	If ObjectAdjusterData6\Absolute
		DestAttributes\Data6=SourceAttributes\Data6
	Else
		DestAttributes\Data6=DestAttributes\Data6+SourceAttributes\Data6
	EndIf
	If ObjectAdjusterData7\Absolute
		DestAttributes\Data7=SourceAttributes\Data7
	Else
		DestAttributes\Data7=DestAttributes\Data7+SourceAttributes\Data7
	EndIf
	If ObjectAdjusterData8\Absolute
		DestAttributes\Data8=SourceAttributes\Data8
	Else
		DestAttributes\Data8=DestAttributes\Data8+SourceAttributes\Data8
	EndIf
	If ObjectAdjusterData9\Absolute
		DestAttributes\Data9=SourceAttributes\Data9
	Else
		DestAttributes\Data9=DestAttributes\Data9+SourceAttributes\Data9
	EndIf
	
	If ObjectAdjusterTextData0\Absolute
		DestAttributes\TextData0$=SourceAttributes\TextData0$
	EndIf
	If ObjectAdjusterTextData1\Absolute
		DestAttributes\TextData1$=SourceAttributes\TextData1$
	EndIf
	
	DestAttributes\TextData2$=SourceAttributes\TextData2$
	DestAttributes\TextData3$=SourceAttributes\TextData3$
	
	If ObjectAdjusterTalkable\Absolute
		DestAttributes\Talkable=SourceAttributes\Talkable
	Else
		DestAttributes\Talkable=DestAttributes\Talkable+SourceAttributes\Talkable
	EndIf
	If ObjectAdjusterCurrentAnim\Absolute
		DestAttributes\CurrentAnim=SourceAttributes\CurrentAnim
	Else
		DestAttributes\CurrentAnim=DestAttributes\CurrentAnim+SourceAttributes\CurrentAnim
	EndIf
	If ObjectAdjusterStandardAnim\Absolute
		DestAttributes\StandardAnim=SourceAttributes\StandardAnim
	Else
		DestAttributes\StandardAnim=DestAttributes\StandardAnim+SourceAttributes\StandardAnim
	EndIf
	If ObjectAdjusterMovementTimer\Absolute
		DestAttributes\MovementTimer=SourceAttributes\MovementTimer
	Else
		DestAttributes\MovementTimer=DestAttributes\MovementTimer+SourceAttributes\MovementTimer
	EndIf
	If ObjectAdjusterMovementSpeed\Absolute
		DestAttributes\MovementSpeed=SourceAttributes\MovementSpeed
	Else
		DestAttributes\MovementSpeed=DestAttributes\MovementSpeed+SourceAttributes\MovementSpeed
	EndIf
	
	If ObjectAdjusterMoveXGoal\Absolute
		DestAttributes\MoveXGoal=SourceAttributes\MoveXGoal
	Else
		DestAttributes\MoveXGoal=DestAttributes\MoveXGoal+SourceAttributes\MoveXGoal
	EndIf
	If ObjectAdjusterMoveYGoal\Absolute
		DestAttributes\MoveYGoal=SourceAttributes\MoveYGoal
	Else
		DestAttributes\MoveYGoal=DestAttributes\MoveYGoal+SourceAttributes\MoveYGoal
	EndIf
	
	If ObjectAdjusterTileTypeCollision\Absolute
		DestAttributes\TileTypeCollision=SourceAttributes\TileTypeCollision
	Else
		DestAttributes\TileTypeCollision=DestAttributes\TileTypeCollision+SourceAttributes\TileTypeCollision
	EndIf
	If ObjectAdjusterObjectTypeCollision\Absolute
		DestAttributes\ObjectTypeCollision=SourceAttributes\ObjectTypeCollision
	Else
		DestAttributes\ObjectTypeCollision=DestAttributes\ObjectTypeCollision+SourceAttributes\ObjectTypeCollision
	EndIf
	
	If ObjectAdjusterCaged\Absolute
		DestAttributes\Caged=SourceAttributes\Caged
	Else
		DestAttributes\Caged=DestAttributes\Caged+SourceAttributes\Caged
	EndIf
	If ObjectAdjusterDead\Absolute
		DestAttributes\Dead=SourceAttributes\Dead
	Else
		DestAttributes\Dead=DestAttributes\Dead+SourceAttributes\Dead
	EndIf
	If ObjectAdjusterDeadTimer\Absolute
		DestAttributes\DeadTimer=SourceAttributes\DeadTimer
	Else
		DestAttributes\DeadTimer=DestAttributes\DeadTimer+SourceAttributes\DeadTimer
	EndIf
	If ObjectAdjusterExclamation\Absolute
		DestAttributes\Exclamation=SourceAttributes\Exclamation
	Else
		DestAttributes\Exclamation=DestAttributes\Exclamation+SourceAttributes\Exclamation
	EndIf
	If ObjectAdjusterShadow\Absolute
		DestAttributes\Shadow=SourceAttributes\Shadow
	Else
		DestAttributes\Shadow=DestAttributes\Shadow+SourceAttributes\Shadow
	EndIf
	If ObjectAdjusterLinked\Absolute
		DestAttributes\Linked=SourceAttributes\Linked
	Else
		DestAttributes\Linked=DestAttributes\Linked+SourceAttributes\Linked
	EndIf
	If ObjectAdjusterLinkBack\Absolute
		DestAttributes\LinkBack=SourceAttributes\LinkBack
	Else
		DestAttributes\LinkBack=DestAttributes\LinkBack+SourceAttributes\LinkBack
	EndIf
	If ObjectAdjusterFlying\Absolute
		DestAttributes\Flying=SourceAttributes\Flying
	Else
		DestAttributes\Flying=DestAttributes\Flying+SourceAttributes\Flying
	EndIf
	If ObjectAdjusterFrozen\Absolute
		DestAttributes\Frozen=SourceAttributes\Frozen
	Else
		DestAttributes\Frozen=DestAttributes\Frozen+SourceAttributes\Frozen
	EndIf
	If ObjectAdjusterIndigo\Absolute
		DestAttributes\Indigo=SourceAttributes\Indigo
	Else
		DestAttributes\Indigo=DestAttributes\Indigo+SourceAttributes\Indigo
	EndIf
	
	DestAttributes\FutureInt24=SourceAttributes\FutureInt24
	DestAttributes\FutureInt25=SourceAttributes\FutureInt25
	
	If ObjectAdjusterScaleAdjust\Absolute
		DestAttributes\ScaleAdjust=SourceAttributes\ScaleAdjust
	Else
		DestAttributes\ScaleAdjust=DestAttributes\ScaleAdjust+SourceAttributes\ScaleAdjust
	EndIf
	If ObjectAdjusterScaleXAdjust\Absolute
		DestAttributes\ScaleXAdjust=SourceAttributes\ScaleXAdjust
	Else
		DestAttributes\ScaleXAdjust=DestAttributes\ScaleXAdjust+SourceAttributes\ScaleXAdjust
	EndIf
	If ObjectAdjusterScaleYAdjust\Absolute
		DestAttributes\ScaleYAdjust=SourceAttributes\ScaleYAdjust
	Else
		DestAttributes\ScaleYAdjust=DestAttributes\ScaleYAdjust+SourceAttributes\ScaleYAdjust
	EndIf
	If ObjectAdjusterScaleZAdjust\Absolute
		DestAttributes\ScaleZAdjust=SourceAttributes\ScaleZAdjust
	Else
		DestAttributes\ScaleZAdjust=DestAttributes\ScaleZAdjust+SourceAttributes\ScaleZAdjust
	EndIf
	DestAttributes\FutureFloat5=SourceAttributes\FutureFloat5
	DestAttributes\FutureFloat6=SourceAttributes\FutureFloat6
	DestAttributes\FutureFloat7=SourceAttributes\FutureFloat7
	DestAttributes\FutureFloat8=SourceAttributes\FutureFloat8
	DestAttributes\FutureFloat9=SourceAttributes\FutureFloat9
	DestAttributes\FutureFloat10=SourceAttributes\FutureFloat10
	DestAttributes\FutureString1$=SourceAttributes\FutureString1$
	DestAttributes\FutureString2$=SourceAttributes\FutureString2$
		
	;For i=0 To 30
	;	ObjectAdjusterString$(Dest,i)="" ;ObjectAdjuster$(i)
	;Next
	
	RandomizeObjectData(DestObject)
	
	FreeObjectModel(LevelObjects(Dest)\Model)
	
	BuildLevelObjectModel(Dest)
	
	UpdateCurrentGrabbedObjectMarkerPosition(Dest)
	
	SomeObjectWasChanged()

	
End Function

Function CompareObjectToCurrent(Obj.GameObject)

	SourceObject.GameObject=CurrentObject
	DestObject.GameObject=Obj
	
	SourceAttributes.GameObjectAttributes=SourceObject\Attributes
	DestAttributes.GameObjectAttributes=DestObject\Attributes
	
	If DestObject\Position\Z<>SourceObject\Position\Z
		ObjectAdjusterZ\Absolute=False
	EndIf

	If DestAttributes\ModelName$<>SourceAttributes\ModelName$
		ObjectAdjusterModelName\Absolute=False
	EndIf
	If DestAttributes\TexName$<>SourceAttributes\TexName$
		ObjectAdjusterTextureName\Absolute=False
	EndIf
	
	If DestAttributes\XScale#<>SourceAttributes\XScale#
		ObjectAdjusterXScale\Absolute=False
		SourceAttributes\XScale=0
	EndIf
	If DestAttributes\YScale#<>SourceAttributes\YScale#
		ObjectAdjusterYScale\Absolute=False
		SourceAttributes\YScale=0
	EndIf
	If DestAttributes\ZScale#<>SourceAttributes\ZScale#
		ObjectAdjusterZScale\Absolute=False
		SourceAttributes\ZScale=0
	EndIf
	
	If DestAttributes\XAdjust#<>SourceAttributes\XAdjust#
		ObjectAdjusterXAdjust\Absolute=False
		SourceAttributes\XAdjust=0
	EndIf
	If DestAttributes\YAdjust#<>SourceAttributes\YAdjust#
		ObjectAdjusterYAdjust\Absolute=False
		SourceAttributes\YAdjust=0
	EndIf
	If DestAttributes\ZAdjust#<>SourceAttributes\ZAdjust#
		ObjectAdjusterZAdjust\Absolute=False
		SourceAttributes\ZAdjust=0
	EndIf
	
	If DestAttributes\PitchAdjust#<>SourceAttributes\PitchAdjust#
		ObjectAdjusterPitchAdjust\Absolute=False
		SourceAttributes\PitchAdjust=0
	EndIf
	If DestAttributes\YawAdjust#<>SourceAttributes\YawAdjust#
		ObjectAdjusterYawAdjust\Absolute=False
		SourceAttributes\YawAdjust=0
	EndIf
	If DestAttributes\RollAdjust#<>SourceAttributes\RollAdjust#
		ObjectAdjusterRollAdjust\Absolute=False
		SourceAttributes\RollAdjust=0
	EndIf
	
	If DestAttributes\DX#<>SourceAttributes\DX#
		ObjectAdjusterDX\Absolute=False
		SourceAttributes\DX=0
	EndIf
	If DestAttributes\DY#<>SourceAttributes\DY#
		ObjectAdjusterDY\Absolute=False
		SourceAttributes\DY=0
	EndIf
	If DestAttributes\DZ#<>SourceAttributes\DZ#
		ObjectAdjusterDZ\Absolute=False
		SourceAttributes\DZ=0
	EndIf
	
	If DestAttributes\MovementType<>SourceAttributes\MovementType
		ObjectAdjusterMovementType\Absolute=False
		SourceAttributes\MovementType=0
	EndIf
	If DestAttributes\MovementTypeData<>SourceAttributes\MovementTypeData
		ObjectAdjusterMovementTypeData\Absolute=False
		SourceAttributes\MovementTypeData=0
	EndIf
	
	If DestAttributes\Data10<>SourceAttributes\Data10
		ObjectAdjusterData10\Absolute=False
		SourceAttributes\Data10=0
	EndIf
	
	If DestAttributes\DefensePower<>SourceAttributes\DefensePower
		ObjectAdjusterDefensePower\Absolute=False
		SourceAttributes\DefensePower=0
	EndIf
	If DestAttributes\DestructionType<>SourceAttributes\DestructionType
		ObjectAdjusterDestructionType\Absolute=False
		SourceAttributes\DestructionType=0
	EndIf
	If DestAttributes\ID<>SourceAttributes\ID
		ObjectAdjusterID\Absolute=False
		SourceAttributes\ID=0
	EndIf
	If DestAttributes\LogicType<>SourceAttributes\LogicType
		ObjectAdjusterLogicType\Absolute=False
		SourceAttributes\LogicType=0
	EndIf
	If DestAttributes\LogicSubType<>SourceAttributes\LogicSubType
		ObjectAdjusterLogicSubType\Absolute=False
		SourceAttributes\LogicSubType=0
	EndIf
	If DestAttributes\Active<>SourceAttributes\Active
		ObjectAdjusterActive\Absolute=False
		SourceAttributes\Active=0
	EndIf
	
	If DestAttributes\ActivationType<>SourceAttributes\ActivationType
		ObjectAdjusterActivationType\Absolute=False
		SourceAttributes\ActivationType=0
	EndIf
	If DestAttributes\ActivationSpeed<>SourceAttributes\ActivationSpeed
		ObjectAdjusterActivationSpeed\Absolute=False
		SourceAttributes\ActivationSpeed=0
	EndIf
	If DestAttributes\Status<>SourceAttributes\Status
		ObjectAdjusterStatus\Absolute=False
		SourceAttributes\Status=0
	EndIf
	If DestAttributes\Timer<>SourceAttributes\Timer
		ObjectAdjusterTimer\Absolute=False
		SourceAttributes\Timer=0
	EndIf
	If DestAttributes\TimerMax1<>SourceAttributes\TimerMax1
		ObjectAdjusterTimerMax1\Absolute=False
		SourceAttributes\TimerMax1=0
	EndIf
	If DestAttributes\TimerMax2<>SourceAttributes\TimerMax2
		ObjectAdjusterTimerMax2\Absolute=False
		SourceAttributes\TimerMax2=0
	EndIf
	If DestAttributes\Teleportable<>SourceAttributes\Teleportable
		ObjectAdjusterTeleportable\Absolute=False
		SourceAttributes\Teleportable=0
	EndIf
	If DestAttributes\ButtonPush<>SourceAttributes\ButtonPush
		ObjectAdjusterButtonPush\Absolute=False
		SourceAttributes\ButtonPush=0
	EndIf
	If DestAttributes\WaterReact<>SourceAttributes\WaterReact
		ObjectAdjusterWaterReact\Absolute=False
		SourceAttributes\WaterReact=0
	EndIf
	
	If DestAttributes\Child<>SourceAttributes\Child
		ObjectAdjusterChild\Absolute=False
		SourceAttributes\Child=0
	EndIf
	If DestAttributes\Parent<>SourceAttributes\Parent
		ObjectAdjusterParent\Absolute=False
		SourceAttributes\Parent=0
	EndIf
	
	If DestAttributes\Data0<>SourceAttributes\Data0
		ObjectAdjusterData0\Absolute=False
		SourceAttributes\Data0=0
	EndIf
	If DestAttributes\Data1<>SourceAttributes\Data1
		ObjectAdjusterData1\Absolute=False
		SourceAttributes\Data1=0
	EndIf
	If DestAttributes\Data2<>SourceAttributes\Data2
		ObjectAdjusterData2\Absolute=False
		SourceAttributes\Data2=0
	EndIf
	If DestAttributes\Data3<>SourceAttributes\Data3
		ObjectAdjusterData3\Absolute=False
		SourceAttributes\Data3=0
	EndIf
	If DestAttributes\Data4<>SourceAttributes\Data4
		ObjectAdjusterData4\Absolute=False
		SourceAttributes\Data4=0
	EndIf
	If DestAttributes\Data5<>SourceAttributes\Data5
		ObjectAdjusterData5\Absolute=False
		SourceAttributes\Data5=0
	EndIf
	If DestAttributes\Data6<>SourceAttributes\Data6
		ObjectAdjusterData6\Absolute=False
		SourceAttributes\Data6=0
	EndIf
	If DestAttributes\Data7<>SourceAttributes\Data7
		ObjectAdjusterData7\Absolute=False
		SourceAttributes\Data7=0
	EndIf
	If DestAttributes\Data8<>SourceAttributes\Data8
		ObjectAdjusterData8\Absolute=False
		SourceAttributes\Data8=0
	EndIf
	If DestAttributes\Data9<>SourceAttributes\Data9
		ObjectAdjusterData9\Absolute=False
		SourceAttributes\Data9=0
	EndIf
	
	If DestAttributes\TextData0$<>SourceAttributes\TextData0$
		ObjectAdjusterTextData0\Absolute=False
	EndIf
	If DestAttributes\TextData1$<>SourceAttributes\TextData1$
		ObjectAdjusterTextData1\Absolute=False
	EndIf
	
	If DestAttributes\Talkable<>SourceAttributes\Talkable
		ObjectAdjusterTalkable\Absolute=False
		SourceAttributes\Talkable=0
	EndIf
	If DestAttributes\CurrentAnim<>SourceAttributes\CurrentAnim
		ObjectAdjusterCurrentAnim\Absolute=False
		SourceAttributes\CurrentAnim=0
	EndIf
	If DestAttributes\StandardAnim<>SourceAttributes\StandardAnim
		ObjectAdjusterStandardAnim\Absolute=False
		SourceAttributes\StandardAnim=0
	EndIf
	If DestAttributes\MovementTimer<>SourceAttributes\MovementTimer
		ObjectAdjusterMovementTimer\Absolute=False
		SourceAttributes\MovementTimer=0
	EndIf
	If DestAttributes\MovementSpeed<>SourceAttributes\MovementSpeed
		ObjectAdjusterMovementSpeed\Absolute=False
		SourceAttributes\MovementSpeed=0
	EndIf
	
	If DestAttributes\MoveXGoal<>SourceAttributes\MoveXGoal
		ObjectAdjusterMoveXGoal\Absolute=False
		SourceAttributes\MoveXGoal=0
	EndIf
	If DestAttributes\MoveYGoal<>SourceAttributes\MoveYGoal
		ObjectAdjusterMoveYGoal\Absolute=False
		SourceAttributes\MoveYGoal=0
	EndIf
	
	If DestAttributes\TileTypeCollision<>SourceAttributes\TileTypeCollision
		ObjectAdjusterTileTypeCollision\Absolute=False
		SourceAttributes\TileTypeCollision=0
	EndIf
	If DestAttributes\ObjectTypeCollision<>SourceAttributes\ObjectTypeCollision
		ObjectAdjusterObjectTypeCollision\Absolute=False
		SourceAttributes\ObjectTypeCollision=0
	EndIf
	
	If DestAttributes\Caged<>SourceAttributes\Caged
		ObjectAdjusterCaged\Absolute=False
		SourceAttributes\Caged=0
	EndIf
	If DestAttributes\Dead<>SourceAttributes\Dead
		ObjectAdjusterDead\Absolute=False
		SourceAttributes\Dead=0
	EndIf
	If DestAttributes\DeadTimer<>SourceAttributes\DeadTimer
		ObjectAdjusterDeadTimer\Absolute=False
		SourceAttributes\DeadTimer=0
	EndIf
	If DestAttributes\Exclamation<>SourceAttributes\Exclamation
		ObjectAdjusterExclamation\Absolute=False
		SourceAttributes\Exclamation=0
	EndIf
	If DestAttributes\Shadow<>SourceAttributes\Shadow
		ObjectAdjusterShadow\Absolute=False
		SourceAttributes\Shadow=0
	EndIf
	If DestAttributes\Linked<>SourceAttributes\Linked
		ObjectAdjusterLinked\Absolute=False
		SourceAttributes\Linked=0
	EndIf
	If DestAttributes\LinkBack<>SourceAttributes\LinkBack
		ObjectAdjusterLinkBack\Absolute=False
		SourceAttributes\LinkBack=0
	EndIf
	If DestAttributes\Flying<>SourceAttributes\Flying
		ObjectAdjusterFlying\Absolute=False
		SourceAttributes\Flying=0
	EndIf
	If DestAttributes\Frozen<>SourceAttributes\Frozen
		ObjectAdjusterFrozen\Absolute=False
		SourceAttributes\Frozen=0
	EndIf
	If DestAttributes\Indigo<>SourceAttributes\Indigo
		ObjectAdjusterIndigo\Absolute=False
		SourceAttributes\Indigo=0
	EndIf
	
	If DestAttributes\ScaleAdjust<>SourceAttributes\ScaleAdjust
		ObjectAdjusterScaleAdjust\Absolute=False
		SourceAttributes\ScaleAdjust=0
	EndIf
	If DestAttributes\ScaleXAdjust<>SourceAttributes\ScaleXAdjust
		ObjectAdjusterScaleXAdjust\Absolute=False
		SourceAttributes\ScaleXAdjust=0
	EndIf
	If DestAttributes\ScaleYAdjust<>SourceAttributes\ScaleYAdjust
		ObjectAdjusterScaleYAdjust\Absolute=False
		SourceAttributes\ScaleYAdjust=0
	EndIf
	If DestAttributes\ScaleZAdjust<>SourceAttributes\ScaleZAdjust
		ObjectAdjusterScaleZAdjust\Absolute=False
		SourceAttributes\ScaleZAdjust=0
	EndIf

End Function

Function AreAllObjectAdjustersAbsolute()

	For ObjAdjusterInt.ObjectAdjusterInt=Each ObjectAdjusterInt
		If Not ObjAdjusterInt\Absolute
			Return False
		EndIf
	Next
	For ObjAdjusterFloat.ObjectAdjusterFloat=Each ObjectAdjusterFloat
		If Not ObjAdjusterFloat\Absolute
			Return False
		EndIf
	Next
	For ObjAdjusterString.ObjectAdjusterString=Each ObjectAdjusterString
		If Not ObjAdjusterString\Absolute
			Return False
		EndIf
	Next

	Return True

End Function

Function MakeAllObjectAdjustersAbsolute()

	For ObjAdjusterInt.ObjectAdjusterInt=Each ObjectAdjusterInt
		ObjAdjusterInt\Absolute=True
	Next
	For ObjAdjusterFloat.ObjectAdjusterFloat=Each ObjectAdjusterFloat
		ObjAdjusterFloat\Absolute=True
	Next
	For ObjAdjusterString.ObjectAdjusterString=Each ObjectAdjusterString
		ObjAdjusterString\Absolute=True
	Next

End Function

Function UpdateCurrentGrabbedObjectMarkerPosition(i)

	SetEntityPositionToObjectPositionWithoutZ(CurrentGrabbedObjectMarkers(i),LevelObjects(i),0.0)

End Function




Function DisplayAsBinaryString$(value)

Result$=""

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


Function TooltipTargetsEffectiveID(StartX,StartY,Index)

	If CurrentObjectTargetIDEnabled(Index)
		TooltipTargetsEffectiveIDInner(StartX,StartY,CurrentObjectTargetID(Index))
	EndIf

End Function

Function TooltipTargetsEffectiveIDInner(StartX,StartY,EffectiveID)

	Count=CountObjectEffectiveIDs(EffectiveID)
	ShowTooltipRightAligned(StartX,StartY,"Targets effective ID "+EffectiveID+", which matches "+Count+" "+MaybePluralize$("object",Count)+" in this level.")

End Function

Function TooltipHasActivateID(StartX,StartY,Index)

	If CurrentObjectActivateIDEnabled(Index)
		TooltipHasActivateIDInner(StartX,StartY,CurrentObjectActivateID(Index))
	EndIf

End Function

Function TooltipHasActivateIDInner(StartX,StartY,ActivateID)

	If ActivateID>0
		Count=CountObjectEffectiveIDs(ActivateID)
		ShowTooltipRightAligned(StartX,StartY,"Effective ID "+ActivateID+" matches "+Count+" "+MaybePluralize$("object",Count)+" in this level.")
	EndIf

End Function


Function HoverOverObjectAdjuster(i)

	StartX=SidebarX+10
	StartY=SidebarY+305
	StartY=StartY+15+(i-ObjectAdjusterStart)*15
	
	CenterX=StartX+92
	TooltipLeftY=StartY+30
	TooltipAboveY=StartY+8
	
	Select ObjectAdjuster$(i)
	
	Case "Data0"
		If CurrentObject\Attributes\LogicType=90
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 Or (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; ColorX2Y or Rotator or ???
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=165 ; Arcade Cabinet
			tex$=""
			If CurrentObjectTargetIDEnabled(0)
				TargetID=CurrentObjectTargetID(0)
				Count=CountObjectEffectiveIDs(TargetID)
				tex$=tex$+"ID "+TargetID+" matches "+Count+"."
			EndIf
			For i=1 To CurrentObjectTargetIDCount-1
				If CurrentObjectTargetIDEnabled(i)
					TargetID=CurrentObjectTargetID(i)
					Count=CountObjectEffectiveIDs(TargetID)
					tex$=tex$+" ID "+TargetID+" matches "+Count+"."
				EndIf
			Next
			If CurrentObjectTargetIDEnabled(0)
				ShowTooltipRightAligned(StartX,TooltipLeftY,tex$)
			EndIf
		EndIf
		
	Case "Data1"
		If CurrentObject\Attributes\LogicType=90
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,1)
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
				If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
					If CurrentObject\Attributes\Data0>=21 And CurrentObject\Attributes\Data0<=27
						ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(CurrentObject\Attributes\Data1,0))
					EndIf
				EndIf
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=11 Or (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; NPC Modifier or Rotator
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			EndIf
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
				If CurrentObject\Attributes\Data0>=21 And CurrentObject\Attributes\Data0<=27
					ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(CurrentObject\Attributes\Data1,0))
				EndIf
			EndIf
		EndIf
	
	Case "Data2"
		If CurrentObject\Attributes\LogicType=90
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,2)
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute And ObjectAdjusterData2\Absolute
					ShowTooltipRightAligned(StartX,TooltipLeftY,GetCmdData2ExtraInfo$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2))
				EndIf
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; ColorX2Y
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			EndIf
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute And ObjectAdjusterData2\Absolute
				ShowTooltipRightAligned(StartX,TooltipLeftY,GetCmdData2ExtraInfo$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2))
			EndIf
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC" And ObjectAdjusterModelName\Absolute And ObjectAdjusterData2\Absolute
			If CurrentObject\Attributes\Data2>0
				ShowTooltipRightAligned(StartX,TooltipLeftY,MyProcessFileNameModel$(GetAccFilenameModel$(CurrentObject\Attributes\Data2)))
			EndIf
		EndIf
		
	Case "Data3"
		If CurrentObject\Attributes\LogicType=90
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,3)
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute And ObjectAdjusterData3\Absolute And CurrentObject\Attributes\Data0=27
					ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data3))
				EndIf
			EndIf
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute And ObjectAdjusterData3\Absolute And CurrentObject\Attributes\Data0=27
				ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data3))
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=242 ; Cuboid
			If CurrentObjectTargetIDCount<>0
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			EndIf
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC" And ObjectAdjusterModelName\Absolute And ObjectAdjusterData2\Absolute And ObjectAdjusterData3\Absolute
			If CurrentObject\Attributes\Data2>0
				ShowTooltipRightAligned(StartX,TooltipLeftY,MyProcessFileNameTexture$(GetAccFilenameTexture$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)))
			EndIf
		EndIf
		
	Case "Data4"
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
		ElseIf IsObjectLogicAutodoor(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipHasActivateID(StartX,TooltipLeftY,0)
		EndIf
	
		If CurrentObject\Attributes\ModelName$="!NPC" And ObjectAdjusterModelName\Absolute And ObjectAdjusterData4\Absolute
			If CurrentObject\Attributes\Data4>0
				ShowTooltipRightAligned(StartX,TooltipLeftY,MyProcessFileNameModel$(GetAccFilenameModel$(CurrentObject\Attributes\Data4)))
			EndIf
		EndIf
		
	Case "Data5"
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,1)
		ElseIf IsObjectLogicAutodoor(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipHasActivateID(StartX,TooltipLeftY,1)
		EndIf
	
		If CurrentObject\Attributes\ModelName$="!NPC" And ObjectAdjusterModelName\Absolute And ObjectAdjusterData4\Absolute And ObjectAdjusterData5\Absolute
			If CurrentObject\Attributes\Data4>0
				ShowTooltipRightAligned(StartX,TooltipLeftY,MyProcessFileNameTexture$(GetAccFilenameTexture$(CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5+1)))
			EndIf
		EndIf
		
	Case "Data6"
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,2)
		ElseIf IsObjectLogicAutodoor(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipHasActivateID(StartX,TooltipLeftY,2)
		EndIf
		
	Case "Data7"
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,3)
		EndIf
		
	Case "Data8"
		If CurrentObject\Attributes\LogicType=90 And ObjectAdjusterLogicType\Absolute
			TooltipHasActivateID(StartX,TooltipLeftY,0)
		EndIf
	
	Case "TileTypeCollision"
		If ObjectAdjusterTileTypeCollision\Absolute
			tex2$="TTC"
			tex$="00000 00000 00000"
				
			HalfNameWidth=4*Len(tex2$+": "+tex$)
			BitStartX=CenterX-HalfNameWidth+8*Len(tex2$+": ")
			
			BitPositionIndex=GetBitPositionIndex(BitStartX)
			BitIndex=BitPositionIndexToBitIndex(BitPositionIndex)
			If BitIndexIsValid(BitIndex) And BitPositionIndexIsValid(BitPositionIndex)
				ShowTooltipCenterAligned(BitStartX+BitPositionIndex*8+12,TooltipAboveY,LogicIdToLogicName$(BitIndex))
			EndIf
		EndIf
		
	Case "ObjectTypeCollision"
		If ObjectAdjusterObjectTypeCollision\Absolute
			tex2$="OTC"
			tex$="00000 00000 00000"
				
			HalfNameWidth=4*Len(tex2$+": "+tex$)
			BitStartX=CenterX-HalfNameWidth+8*Len(tex2$+": ")
			
			BitPositionIndex=GetBitPositionIndex(BitStartX)
			BitIndex=BitPositionIndexToBitIndex(BitPositionIndex)
			If BitIndexIsValid(BitIndex) And BitPositionIndexIsValid(BitPositionIndex)
				ShowTooltipCenterAligned(BitStartX+BitPositionIndex*8+12,TooltipAboveY,ObjectTypeCollisionBitToName$(BitIndex))
			EndIf
		EndIf
		
	Case "Type"
		If ObjectAdjusterLogicType\Absolute
			Count=CountObjectTypes(CurrentObject\Attributes\LogicType)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this Type.")
		EndIf
	
	Case "SubType"
		If ObjectAdjusterLogicType\Absolute And ObjectAdjusterLogicSubType\Absolute
			Count=CountObjectLogics(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this object logic.")
		EndIf
		
	Case "ModelName"
		If ObjectAdjusterModelName\Absolute
			Count=CountObjectModelNames(CurrentObject\Attributes\ModelName$)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this ModelName.")
		EndIf
	
	Case "TextureName"
		If ObjectAdjusterTextureName\Absolute
			Count=CountObjectTextureNames(CurrentObject\Attributes\TexName$)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this TextureName.")
		EndIf
		
	Case "ID"
		If ObjectAdjusterID\Absolute
			EffectiveID=CalculateEffectiveID(CurrentObject\Attributes)
			Count=CountObjectEffectiveIDs(EffectiveID)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this effective ID, which is "+EffectiveID+".")
		EndIf
		
	Case "Talkable"
		If ObjectAdjusterTalkable\Absolute
			TheDialog=CurrentObject\Attributes\Talkable
			If TheDialog<>0
				If TheDialog>=10001
					TheDialog=TheDialog-10000
				EndIf
				ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(TheDialog,0))
			EndIf
		EndIf
	
	Case "Exclamation"
		ShowParticlePreview(StartX,TooltipLeftY,CurrentObject\Attributes\Exclamation)
	
	End Select

End Function


Function DisplayObjectAdjuster(i)

	tex2$=ObjectAdjuster$(i)
	
	CurrentAdjusterRandomized=False
	CurrentAdjusterAbsolute=True
	CurrentAdjusterZero=False
	LeftAdj$=""
	RightAdj$=""
	
	StartX=SidebarX+10
	StartY=SidebarY+305
	StartY=StartY+15+(i-ObjectAdjusterStart)*15
	
	Select ObjectAdjuster$(i)
	Case "TextureName"
		tex2$="Texture"
		tex$=CurrentObject\Attributes\TexName$
		tex$=SetAdjusterDisplayString(ObjectAdjusterTextureName,CurrentObject\Attributes\TexName$,tex$)
	
	Case "ModelName"
		tex2$="Model"
		tex$=CurrentObject\Attributes\ModelName$
		tex$=SetAdjusterDisplayString(ObjectAdjusterModelName,CurrentObject\Attributes\ModelName$,tex$)
		
	Case "X"
		tex$=Str$(CurrentObject\Position\X)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterX,CurrentObject\Position\X,tex$)
	Case "Y"
		tex$=Str$(CurrentObject\Position\Y)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterY,CurrentObject\Position\Y,tex$)
	Case "Z"
		tex$=Str$(CurrentObject\Position\Z)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterZ,CurrentObject\Position\Z,tex$)
	
	Case "XAdjust"
		tex$=Str$(CurrentObject\Attributes\XAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterXAdjust,CurrentObject\Attributes\XAdjust,tex$)
	Case "YAdjust"
		tex$=Str$(CurrentObject\Attributes\YAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterYAdjust,CurrentObject\Attributes\YAdjust,tex$)
	Case "ZAdjust"
		tex$=Str$(CurrentObject\Attributes\ZAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterZAdjust,CurrentObject\Attributes\ZAdjust,tex$)
	
	Case "XScale"
		tex$=Str$(CurrentObject\Attributes\XScale)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterXScale,CurrentObject\Attributes\XScale,tex$)
	Case "YScale"
		tex$=Str$(CurrentObject\Attributes\YScale)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterYScale,CurrentObject\Attributes\YScale,tex$)
	Case "ZScale"
		tex$=Str$(CurrentObject\Attributes\ZScale)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterZScale,CurrentObject\Attributes\ZScale,tex$)
		
	Case "DefensePower"
		tex$=Str$(CurrentObject\Attributes\DefensePower)
		tex2$="Greeting"
		Select CurrentObject\Attributes\DefensePower
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
			tex$="Wee "+Str$(CurrentObject\Attributes\DefensePower-9)
		Case 19,20,21
			tex$="Kaboom "+Str$(CurrentObject\Attributes\DefensePower-18)

		Case 22,23,24
			tex$="ZBot "+Str$(CurrentObject\Attributes\DefensePower-21)

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
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterDefensePower,CurrentObject\Attributes\DefensePower,tex$)
		
	Case "AttackPower"
		tex$=Str$(CurrentObject\Attributes\AttackPower)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterAttackPower,CurrentObject\Attributes\AttackPower,tex$)
		
	Case "DestructionType"
		tex$=Str$(CurrentObject\Attributes\DestructionType)
		
		Select CurrentObject\Attributes\DestructionType
			Case 0
				tex$="None"
			Case 1
				tex$="White"
			Case 2
				tex$="MODDED" ; Purple
		End Select
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterDestructionType,CurrentObject\Attributes\DestructionType,tex$)
		
	Case "YawAdjust"
		tex$=Str$(CurrentObject\Attributes\YawAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterYawAdjust,CurrentObject\Attributes\YawAdjust,tex$)
	Case "PitchAdjust"
		tex$=Str$(CurrentObject\Attributes\PitchAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterPitchAdjust,CurrentObject\Attributes\PitchAdjust,tex$)
	Case "RollAdjust"
		tex$=Str$(CurrentObject\Attributes\RollAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterRollAdjust,CurrentObject\Attributes\RollAdjust,tex$)
	
	Case "ID"
		tex$=Str$(CurrentObject\Attributes\ID)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterID,CurrentObject\Attributes\ID,tex$)
	Case "Type"
		tex$=Str$(CurrentObject\Attributes\LogicType)+"/"+GetTypeString$(CurrentObject\Attributes\LogicType)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterLogicType,CurrentObject\Attributes\LogicType,tex$)
	Case "SubType"
		tex$=Str$(CurrentObject\Attributes\LogicSubType)
		
		If CurrentObject\Attributes\ModelName$="!Crab"
			tex2$="Color"
			If CurrentObject\Attributes\LogicSubType=0
				tex$="Green"
			Else If CurrentObject\Attributes\LogicSubType=1
				tex$="Red"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=165 ; Arcade Cabinet
			If CurrentObject\Attributes\LogicSubType=1
				tex$="Sold Out"
			Else
				tex$="Available"
			EndIf
			tex$=CurrentObject\Attributes\LogicSubType+"/"+tex$
		EndIf
		
		If CurrentObject\Attributes\LogicType=179 ; Custom Item
			tex2$="Fn"
			If CurrentObject\Attributes\LogicSubType>=0 And CurrentObject\Attributes\LogicSubType<30
				tex$=GetItemFnName$(4001+CurrentObject\Attributes\LogicSubType*10)
			Else If CurrentObject\Attributes\LogicSubType=-1
				tex$="Gloves"
			Else If CurrentObject\Attributes\LogicSubType=-2
				tex$="Lamp"
			Else If CurrentObject\Attributes\LogicSubType=-3
				tex$="GlowGem"
			Else If CurrentObject\Attributes\LogicSubType=-4
				tex$="Spy-Eye"
			Else If CurrentObject\Attributes\LogicSubType=-5
				tex$="Glyph"
			Else If CurrentObject\Attributes\LogicSubType=-6
				tex$="MapPiece"
			Else
				tex$="Raw"+GetItemFnName$(4001+CurrentObject\Attributes\LogicSubType*10)
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=190 ; Particle Emitter
			Select CurrentObject\Attributes\LogicSubType
			Case 1
				tex$="Steam"
			Case 2
				tex$="Splish"
			Case 3
				tex$="Spray"
			Case 4
				tex$="Sparks"
			Case 5
				tex$="Blinker"
			Case 6
				tex$="CircleBurst"
			Case 7
				tex$="Spiral"
			End Select
		EndIf
		If CurrentObject\Attributes\LogicType=200 ; Magic charger
			If CurrentObject\Attributes\LogicSubType=0
				tex$=CurrentObject\Attributes\LogicSubType+"/Regular"
			ElseIf CurrentObject\Attributes\LogicSubType=1
				tex$=CurrentObject\Attributes\LogicSubType+"/Faint"
			Else
				tex$=CurrentObject\Attributes\LogicSubType+"/OneByOne"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="Turning"
			If CurrentObject\Attributes\LogicSubType=0
				tex$="None"
			Else If CurrentObject\Attributes\LogicSubType=1
				tex$="Player"
			Else If CurrentObject\Attributes\LogicSubType=2
				tex$="ClockW"
			Else If CurrentObject\Attributes\LogicSubType=3
				tex$="CountW"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=370 ; Crab
			tex2$="Color"
			If CurrentObject\Attributes\LogicSubType=0
				tex$="Green"
			Else If CurrentObject\Attributes\LogicSubType=1
				tex$="Red"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="Spell"
			tex$=GetMagicNameAndId$(CurrentObject\Attributes\LogicSubType)
		EndIf
		If CurrentObject\Attributes\LogicType=54 ; Magic Mirror
			tex2$="Glyph"
			Select CurrentObject\Attributes\LogicSubType
			Case 0
				tex$="Inactive"
			Case 1
				tex$="Fire"
			Case 2
				tex$="Ice"
			Case 3
				tex$="Time"
			Case 4
				tex$="Acid"
			Case 5
				tex$="Home"
			End Select
		EndIf
		If CurrentObject\Attributes\LogicType=90 ; button
			Select CurrentObject\Attributes\LogicSubType
			Case 0
				tex$="Square"
			Case 1
				tex$="Round"
			Case 2
				tex$="DiamondOnce"
			Case 3
				tex$="Diamond"
			Case 4
				tex$="Star"
			Case 5
				tex$="X2Y Square"
			Case 6
				tex$="X2Y Round"
			Case 7
				tex$="X2Y Once"
			Case 8
				tex$="X2Y Diamond"
			Case 9
				tex$="X2Y Star"
			Case 10
				tex$="LevelExit"
			Case 11
				tex$="NPC Modifier"
			Case 12
				tex$="FakeStnkerExit"
			Case 13
				tex$="AdventureStar"
			Case 14
				tex$="AdventuredStar"
			Case 15
				tex$="GeneralCommand"
			Case 16
				tex$="Rotator"
			Case 32
				tex$="InvSquare"
			Case 33
				tex$="InvRound"
			Case 34
				tex$="InvOnce"
			Case 35
				tex$="InvDiamond"
			Case 36
				tex$="InvStar"
			Case 37
				tex$="InvX2Y Square"
			Case 38
				tex$="InvX2Y Round"
			Case 39
				tex$="InvX2Y Once"
			Case 40
				tex$="InvX2Y Diamond"
			Case 41
				tex$="InvX2Y Star"
			Case 48
				tex$="InvRotator"
			End Select
		EndIf
		
		If CurrentObject\Attributes\LogicType=120 ; Wee Stinker
			Select CurrentObject\Attributes\LogicSubType
			Case -2
				tex$="Dying?"
			Case -1
				tex$="Exiting"
			Case 0
				tex$="Asleep"
			Case 1
				tex$="Following"
			Case 2
				tex$="Stationary"
			Case 3
				tex$="FallingAsleep"
			Case 4
				tex$="Caged"
			End Select
		EndIf
		
		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="AudioTimeOffset"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterLogicSubType,CurrentObject\Attributes\LogicSubType,tex$)
		
	Case "TimerMax1"
		tex$=Str$(CurrentObject\Attributes\TimerMax1)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTimerMax1,CurrentObject\Attributes\TimerMax1,tex$)
	Case "TimerMax2"
		tex$=Str$(CurrentObject\Attributes\TimerMax2)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTimerMax2,CurrentObject\Attributes\TimerMax2,tex$)
	Case "Timer"
		tex$=Str$(CurrentObject\Attributes\Timer)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTimer,CurrentObject\Attributes\Timer,tex$)

	Case "TextData0"
		; custom model
		tex2$=""
		tex$=CurrentObject\Attributes\TextData0$
		tex$=SetAdjusterDisplayString(ObjectAdjusterTextData0,CurrentObject\Attributes\TextData0$,tex$)
		
	Case "TextData1"
		tex2$=""
		tex$=CurrentObject\Attributes\TextData1$
		tex$=SetAdjusterDisplayString(ObjectAdjusterTextData1,CurrentObject\Attributes\TextData1$,tex$)

		
	Case "Active"
		If CurrentObject\Attributes\Active=0
			tex$="No (0)"
		Else If CurrentObject\Attributes\Active=1001
			tex$="Yes (1001)"
		Else If CurrentObject\Attributes\Active Mod 2=0
			tex$="Soon No ("+CurrentObject\Attributes\Active+")"
		Else
			tex$="Soon Yes ("+CurrentObject\Attributes\Active+")"
		EndIf
		tex$=SetAdjusterDisplayInt(ObjectAdjusterActive,CurrentObject\Attributes\Active,tex$)
		
	Case "ActivationSpeed"
		tex$=Str$(CurrentObject\Attributes\ActivationSpeed)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterActivationSpeed,CurrentObject\Attributes\ActivationSpeed,tex$)
		
	Case "ActivationType"
		tex$=GetActivationTypeString$(CurrentObject\Attributes\ActivationType)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterActivationType,CurrentObject\Attributes\ActivationType,tex$)
		
	Case "ButtonPush"
		tex$=CurrentObject\Attributes\ButtonPush+"/"+OneToYes$(CurrentObject\Attributes\ButtonPush)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterButtonPush,CurrentObject\Attributes\ButtonPush,tex$)
		
	Case "WaterReact"
		tex$=Str$(CurrentObject\Attributes\WaterReact)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterWaterReact,CurrentObject\Attributes\WaterReact,tex$)
		
	Case "Freezable"
		tex$=Str$(CurrentObject\Attributes\Freezable)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterFreezable,CurrentObject\Attributes\Freezable,tex$)
		
	Case "Frozen"
		tex$=Str$(CurrentObject\Attributes\Frozen)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterFrozen,CurrentObject\Attributes\Frozen,tex$)
		
	Case "Teleportable"
		tex$=CurrentObject\Attributes\Teleportable+"/"+OneToYes$(CurrentObject\Attributes\Teleportable)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTeleportable,CurrentObject\Attributes\Teleportable,tex$)
	
	Case "Data0"
		tex$=Str$(CurrentObject\Attributes\Data0)
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="YawAnim"
		EndIf
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!Obstacle48" ; (wysp ship)
			tex2$="Turning"
			Select CurrentObject\Attributes\Data0
				Case 0
					tex$="Yes"
				Default
					tex$="No"
			End Select
			tex$=CurrentObject\Attributes\Data0+"/"+tex$
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Scritter" Or CurrentObject\Attributes\ModelName$="!Cuboid" Or CurrentObject\Attributes\ModelName$="!Spring" Or CurrentObject\Attributes\ModelName$="!SteppingStone" Or CurrentObject\Attributes\ModelName$="!Transporter" Or CurrentObject\Attributes\ModelName$="!ColourGate" Or CurrentObject\Attributes\ModelName$="!Key" Or CurrentObject\Attributes\ModelName$="!KeyCard" Or CurrentObject\Attributes\ModelName$="!Teleport" Or CurrentObject\Attributes\ModelName$="!FlipBridge" Or CurrentObject\Attributes\ModelName$="!Pushbot" Or CurrentObject\Attributes\ModelName$="!Suctube" Or CurrentObject\Attributes\ModelName$="!Conveyor"		
			tex2$="Colour"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Obstacle51" Or CurrentObject\Attributes\ModelName$="!Obstacle55" Or CurrentObject\Attributes\ModelName$="!Obstacle59"
			tex2$="Shape"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!CustomItem"
			tex2$="Texture"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!WaterFall"
			tex2$="Type"
			Select CurrentObject\Attributes\Data0
				Case 0
					tex$="Water"
				Case 1
					tex$="Lava"
				Case 2
					tex$="Green"
			End Select
		EndIf
		
		
		If CurrentObject\Attributes\ModelName$="!Gem"
			tex2$="Shape"
		EndIf
		If CurrentObject\Attributes\ModelName$="!Sign"
			tex2$="Shape"
		EndIf
		If CurrentObject\Attributes\ModelName$="!Crystal"
			tex2$="Type"
			Select CurrentObject\Attributes\Data0
			Case 0
				tex$="Rainbow"
			Case 1
				tex$="Void"
			End Select

		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Kaboom"
			tex2$="Texture"
			
			Select CurrentObject\Attributes\Data0
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
		
		If CurrentObject\Attributes\ModelName$="!Wisp"
			tex2$="Texture"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Sun Sphere1"
			tex2$="Red"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!GrowFlower"
			tex2$="TileLogic"
			tex$=LogicIdToLogicName$(CurrentObject\Attributes\Data0)
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC" Or CurrentObject\Attributes\LogicType=110
			tex2$="Texture"
			
			Select CurrentObject\Attributes\Data0
			Case 1
				tex$="1/Blue"
			Case 2
				tex$="2/Purple"
			Case 3
				tex$="3/Red"
			Case 4
				tex$="4/Dark"
			Case 5
				tex$="5/Shadow"
			Case 6
				tex$="6/Fire"
			Case 7
				tex$="7/Green"
			Case 8
				tex$="8/White"
			End Select
		EndIf
		
		; Model checks are separated from Type checks so that the Type can override the model.
		
		If CurrentObject\Attributes\LogicType=51 Or CurrentObject\Attributes\LogicType=200 Or CurrentObject\Attributes\LogicType=201 ; spellball generator or glovecharge or glove discharge
			tex2$="Spell"
			tex$=GetMagicNameAndId(CurrentObject\Attributes\Data0)
		EndIf
		
		If CurrentObject\Attributes\LogicType=179 ; Custom Item
			tex2$="Texture"
		EndIf
		
		If CurrentObject\Attributes\LogicType=320 ; Void
			tex2$="TimeOffset"
			If CurrentObject\Attributes\Data0=0
				tex$="Random"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=350 ; GrowFlower
			tex2$="TileLogic"
			tex$=LogicIdToLogicName$(CurrentObject\Attributes\Data0)
		EndIf

		If CurrentObject\Attributes\LogicType=280 Or CurrentObject\Attributes\LogicType=40 Or CurrentObject\Attributes\LogicType=210 Or CurrentObject\Attributes\LogicType=10 Or CurrentObject\Attributes\LogicType=172 Or CurrentObject\Attributes\LogicType=30 Or CurrentObject\Attributes\LogicType=140 Or CurrentObject\Attributes\LogicType=20 Or CurrentObject\Attributes\LogicType=410 Or CurrentObject\Attributes\LogicType=424 Or CurrentObject\Attributes\LogicType=432 Or CurrentObject\Attributes\LogicType=281 Or CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46
			tex2$="Colour"
		EndIf
		
		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$="CMD"
			tex$=Str(CurrentObject\Attributes\Data0)+"/"+GetCommandName$(CurrentObject\Attributes\Data0)
		EndIf
		
		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="Colour1"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; ColX2Y Button
				tex2$="Col From"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$="CMD"
				tex$=Str(CurrentObject\Attributes\Data0)+"/"+GetCommandName$(CurrentObject\Attributes\Data0)
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; Rotator or ???
				tex2$="Colour"
			Else If CurrentObject\Attributes\LogicSubType=13 ; Adventure Star
				tex2$="Adventure ID"
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="GoalX"
		EndIf
		If CurrentObject\Attributes\LogicType=190 Or CurrentObject\Attributes\LogicType=164 ; Particle Spawner or Fountain
			tex2$="Particle ID"
		EndIf
		If CurrentObject\Attributes\LogicType=11 ; TollGate
			tex2$="Cost"
		EndIf
		
		If CurrentObject\Attributes\LogicType=165 ; Arcade Cabinet
			tex2$="Activates"
			Data0=CurrentObject\Attributes\Data0
			tex$=Data0
			While True
				Data0=Data0+1
				If ((Data0-200) Mod 3)=0
					Exit
				Else
					tex$=tex$+", "+Data0
				EndIf
			Wend
		EndIf

		
		If CurrentObject\Attributes\LogicType=40 ; bridge
			tex$=Str$(CurrentObject\Attributes\Data0+8)
		EndIf
		
		If CurrentObject\Attributes\LogicType=70 ; Beta Pickup Item
			tex2$="Fn"
			tex$=GetItemFnName$(CurrentObject\Attributes\Data0)
		EndIf

		If CurrentObject\Attributes\LogicType=260 ; spikeyball
			tex2$="Direction"
			If CurrentObject\Attributes\Data1=2
				Select CurrentObject\Attributes\Data0
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
				Select CurrentObject\Attributes\Data0
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
		If CurrentObject\Attributes\LogicType=250 ; chomper
			tex2$="Speed"
			tex$="+"+Str$(CurrentObject\Attributes\Data0)
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; fireflower
			tex2$="Direction"
			
			Select CurrentObject\Attributes\Data0
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
		; turtle or scouge or ufo or retro z-bot or zipbot or zapbot
		If CurrentObject\Attributes\LogicType=220 Or CurrentObject\Attributes\LogicType=421 Or CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=423 Or CurrentObject\Attributes\LogicType=430 Or CurrentObject\Attributes\LogicType=431
			tex2$="Direction"
			
			Select CurrentObject\Attributes\Data0
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

		If CurrentObject\Attributes\LogicType=310 ; duck
			tex2$="Move"
			If CurrentObject\Attributes\Data0=1 
				tex$="Yes"
			Else
				tex$="No"
			EndIf
			tex$=CurrentObject\Attributes\Data0+"/"+tex$
		EndIf
		
		If CurrentObject\Attributes\LogicType=433 ; Z-Bot NPC
			tex2$="Exploding"
			If CurrentObject\Attributes\Data0<=0
				tex$="No"
			ElseIf CurrentObject\Attributes\Data0<120
				tex$="Yes"
			Else
				tex$="Always"
			EndIf
			tex$=CurrentObject\Attributes\Data0+"/"+tex$
		EndIf
		
		If CurrentObject\Attributes\LogicType=434 ; mothership
			tex2$="SpawnTimer" ; Formerly TimerMax
			If CurrentObject\Attributes\Data0=0
				tex$="No Spawns"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=460 ; BurstFlower
			tex2$="TimeOffset"
		EndIf
		
		If CurrentObject\Attributes\LogicType=470 Or CurrentObject\Attributes\LogicType=471 ; ghost or wraith
			tex2$="Radius"
		EndIf
		
		If CurrentObject\Attributes\LogicType=52 ; meteor shooter
			tex2$="StartZ"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData0,CurrentObject\Attributes\Data0,tex$)



	Case "Data1"
		tex$=Str$(CurrentObject\Attributes\Data1)

		If CurrentObject\Attributes\ModelName$="!Obstacle51" Or CurrentObject\Attributes\ModelName$="!Obstacle55" Or CurrentObject\Attributes\ModelName$="!Obstacle59"
			tex2$="Texture"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Gem"
			tex2$="Colour"
		EndIf
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="PitchAnim"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Chomper"
			tex2$="Special"
			If CurrentObject\Attributes\Data1=0
				tex$="---"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Ghost"
			Else If CurrentObject\Attributes\Data1=2
				tex$="Glow"
			Else If CurrentObject\Attributes\Data1=3
				tex$="Mecha"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Sun Sphere1"
			tex2$="Green"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Sign"
			tex2$="Texture"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Crab"
			tex2$="Status"
			If CurrentObject\Attributes\Data1=0
				tex$="Awake"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Curious"
			Else If CurrentObject\Attributes\Data1=2
				tex$="Asleep"
			Else If CurrentObject\Attributes\Data1=3
				tex$="Disabled"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC" Or CurrentObject\Attributes\LogicType=110
			tex2$="Expression"
			tex$=GetStinkerExpressionName$(CurrentObject\Attributes\Data1)
		EndIf
		
		; spring or bridge or transporter or gate or key or teleporter or cage or fire trap or laser gate or moobot or suctube or conveyor
		If CurrentObject\Attributes\LogicType=280 Or CurrentObject\Attributes\LogicType=40 Or CurrentObject\Attributes\LogicType=210 Or CurrentObject\Attributes\LogicType=10 Or CurrentObject\Attributes\LogicType=172 Or CurrentObject\Attributes\LogicType=30 Or CurrentObject\Attributes\LogicType=140 Or CurrentObject\Attributes\LogicType=20 Or CurrentObject\Attributes\LogicType=410 Or CurrentObject\Attributes\LogicType=424 Or CurrentObject\Attributes\LogicType=432 Or CurrentObject\Attributes\LogicType=281 Or CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46
			tex2$="SubColour"
		EndIf
		
		If CurrentObject\Attributes\LogicType=242 ; cuboid
			tex2$="Turning"
			If CurrentObject\Attributes\Data1=0 
				tex$="No"
			Else
				tex$="Yes"
			EndIf
		EndIf
		
		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$=GetCMDData1Name$(CurrentObject\Attributes\Data0)
			tex$=GetCmdData1ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
		EndIf
		
		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="Colour2"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; Color Changer
				tex2$="Col To"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$=GetCMDData1Name$(CurrentObject\Attributes\Data0)
				tex$=GetCmdData1ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; Rotator or ???
				tex2$="SubColour"
			Else If CurrentObject\Attributes\LogicSubType = 10 ; LevelExit
				tex2$="Dest Level"
			Else If CurrentObject\Attributes\LogicSubType = 11 ; NPC Modifier
				If CurrentObject\Attributes\Data0=2 ; NPC Exclamation
					tex2$="Target ID"
					If CurrentObject\Attributes\Data1=-1
						tex$="Pla"
					EndIf
				Else
					tex2$="NPC ID"
				EndIf
			EndIf
			
		EndIf
		
		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="GoalY"
		EndIf
		If CurrentObject\Attributes\LogicType=190 ; Particle Emitter
			tex2$="Intensity"
			If CurrentObject\Attributes\Data1=1 tex$="Low"
			If CurrentObject\Attributes\Data1=2 tex$="Reg"
			If CurrentObject\Attributes\Data1=3 tex$="High"
			
		EndIf
		If CurrentObject\Attributes\LogicType=200 ; Glovecharge
			tex2$="Usability"
			If CurrentObject\Attributes\Data1<1
				tex$="Always"
			ElseIf CurrentObject\Attributes\Data1=1
				tex$="Once"
			ElseIf CurrentObject\Attributes\Data1>1
				tex$="Unusable"
			EndIf
			tex$=CurrentObject\Attributes\Data1+"/"+tex$
		EndIf
		If CurrentObject\Attributes\LogicType=11 ; TollGate
			tex2$="Type"
			If CurrentObject\Attributes\Data1=0 
				tex$="Star"
			Else
				tex$="Coin"
			EndIf
			tex$=CurrentObject\Attributes\Data1+"/"+tex$
			
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="Type"
			
			Select CurrentObject\Attributes\Data1
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

		If CurrentObject\Attributes\LogicType=70 Or CurrentObject\Attributes\LogicType=179 ; Beta Pickup Item or Custom Item
			tex2$="Fn ID"
		EndIf

		If CurrentObject\Attributes\LogicType=260 ; Spikeyball
			tex2$="Type"
			If CurrentObject\Attributes\Data1=0
				tex$="Bounce Left"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Bounce Right"
			Else
				tex$="Bounce Diag"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=250 ; Chomper
			tex2$="Special"
			If CurrentObject\Attributes\Data1=0
				tex$="---"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Ghost"
			Else If CurrentObject\Attributes\Data1=2
				tex$="Glow"
			Else If CurrentObject\Attributes\Data1=3
				tex$="Mecha"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=220 ; Turtle
			tex2$="Turn"
			If CurrentObject\Attributes\Data1=0
				tex$="Left"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Right"
			
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=300 ; Brr float
			tex2$="TimeOffset"
		EndIf
		
		If CurrentObject\Attributes\LogicType=310 ; RubberDucky
			tex2$="TiltMagnitude"
		EndIf
		
		If CurrentObject\Attributes\LogicType=370 ; Crab
			tex2$="Status"
			If CurrentObject\Attributes\Data1=0
				tex$="Awake"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Curious"
			Else If CurrentObject\Attributes\Data1=2
				tex$="Asleep"
			Else If CurrentObject\Attributes\Data1=3
				tex$="Disabled"
			EndIf
		EndIf
		
	

		If CurrentObject\Attributes\LogicType=290 Or CurrentObject\Attributes\LogicType=380 ; Thwart or Ice Troll
			tex2$="WalkAnim"
			If CurrentObject\Attributes\Data1=1
				tex$="Hands Up"
			Else
				tex$="Normal"
			EndIf
			tex$=CurrentObject\Attributes\Data1+"/"+tex$
		EndIf
		
		
		If CurrentObject\Attributes\LogicType=51 ; spellball generator
			tex2$="Goal X"
		EndIf
		
		; ufo or retro z-bot or zipbot or zapbot
		If CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=423 Or CurrentObject\Attributes\LogicType=430 Or CurrentObject\Attributes\LogicType=431
			tex2$="Turning"
			If CurrentObject\Attributes\Data1=0
				tex$="Left"
			Else If CurrentObject\Attributes\Data1=1
				tex$="Right"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=460 ; BurstFlower
			tex2$="BurstProgress"
			If CurrentObject\Attributes\Data1=150
				tex$="Fire"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=470 ; Ghost
			tex2$="Speed"
		EndIf
		If CurrentObject\Attributes\LogicType=471 ; Wraith
			tex2$="ShotTime"
		EndIf
		
		If CurrentObject\Attributes\LogicType=52 ; meteor shooter
			tex2$="TargetX"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData1,CurrentObject\Attributes\Data1,tex$)


		
	Case "Data2"
		tex$=Str$(CurrentObject\Attributes\Data2)
		
		If CurrentObject\Attributes\ModelName$="!ColourGate"
			tex2$="Frame"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Gem"
			tex2$="XOffset"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC"
			tex2$="Acc1"
			
			tex$=GetAccessoryName$(CurrentObject\Attributes\Data2)
			
			tex$=CurrentObject\Attributes\Data2+"/"+tex$
				
			
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Thwart" 
			tex2$="Colour"
			If CurrentObject\Attributes\Data2=0
				tex$="Standard"
			Else If CurrentObject\Attributes\Data2=1
				tex$="Red"
			Else If CurrentObject\Attributes\Data2=2
				tex$="Orange"
			Else If CurrentObject\Attributes\Data2=3
				tex$="Yellow"
			Else If CurrentObject\Attributes\Data2=4
				tex$="Green"
			Else If CurrentObject\Attributes\Data2=5
				tex$="Blue"
			Else If CurrentObject\Attributes\Data2=6
				tex$="Indigo"
			Else If CurrentObject\Attributes\Data2=7
				tex$="Purple"

			
			EndIf
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Sun Sphere1"
			tex2$="Blue"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Wraith"
			tex2$="Magic"
			Select CurrentObject\Attributes\Data2
			Case 0
				tex$="Fire"
			Case 1
				tex$="Ice"
			Case 2
				tex$="Grow"
			Default
				tex$="None"
			End Select
			tex$=CurrentObject\Attributes\Data2+"/"+tex$
		EndIf
		
		; spring or transporter or flipbridge or suctube or suctubex or conveyor
		If CurrentObject\Attributes\LogicType=280 Or CurrentObject\Attributes\LogicType=210 Or CurrentObject\Attributes\LogicType=410 Or CurrentObject\Attributes\LogicType=281 Or CurrentObject\Attributes\LogicType=282 Or CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46
			tex2$="Direction"
			If CurrentObject\Attributes\LogicType=210 ; transporter
				tex$=Str$(3-CurrentObject\Attributes\Data2)
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=432 ; Moobot
			tex2$="Direction"
			Select CurrentObject\Attributes\Data2
			Case 0
				tex$="North"
			Case 1
				tex$="East"
			Case 2
				tex$="South"
			Case 3
				tex$="West"
			Default
				tex$=CurrentObject\Attributes\Data2+"/NoMove"
			End Select
		EndIf
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="RollAnim"
		EndIf
		
		If CurrentObject\Attributes\LogicType=471 ; Wraith
			tex2$="Magic"
			Select CurrentObject\Attributes\Data2
			Case 0
				tex$="Fire"
			Case 1
				tex$="Ice"
			Case 2
				tex$="Grow"
			Default
				tex$="None"
			End Select
			tex$=CurrentObject\Attributes\Data2+"/"+tex$
		EndIf
		
		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$=GetCMDData2Name$(CurrentObject\Attributes\Data0)
			tex$=GetCmdData2ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2)
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="Colour3"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; Color Changer
				tex2$="SubCol From"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$=GetCMDData2Name$(CurrentObject\Attributes\Data0)
				tex$=GetCmdData2ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2)
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; Rotator or ???
				tex2$="Direction"
			Else If CurrentObject\Attributes\LogicSubType = 10 ; LevelExit
				tex2$="Dest X"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=0 ; NPC Move
				tex2$="X Goal"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Change
				tex2$="Dialog"
				If CurrentObject\Attributes\Data2=0 Then tex$="None"
				If CurrentObject\Attributes\Data2=-1 Then	tex$="No Change"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=2 ; NPC Exclamation
				tex2$="Particle ID"



			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="SourceX"
		EndIf
		
		If CurrentObject\Attributes\LogicType=70 ; Beta Pickup Item
			tex2$="Texture"
		EndIf
		
		If CurrentObject\Attributes\LogicType=190
			tex2$="Direction"
			If CurrentObject\Attributes\Data2=0 tex$="Up"
			If CurrentObject\Attributes\Data2=1 tex$="Down"
			If CurrentObject\Attributes\Data2=2 tex$="East"
			If CurrentObject\Attributes\Data2=3 tex$="West"
			If CurrentObject\Attributes\Data2=4 tex$="North"
			If CurrentObject\Attributes\Data2=5 tex$="South"
			
		EndIf
		
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="State"
		EndIf
		
		If CurrentObject\Attributes\LogicType=260 ; Spikeyball
			tex2$="Speed"
			tex$="+"+Str$(CurrentObject\Attributes\Data2)
		EndIf
		
		If CurrentObject\Attributes\LogicType=300 ; Brr float
			tex2$="PitchAnim"
		EndIf
		
		If CurrentObject\Attributes\LogicType=301 ; Rainbow float
			tex2$="ColorOffset"
			If CurrentObject\Attributes\Data2=0
				tex$="Random"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=310 ; RubberDucky
			tex2$="TimeOffset"
		EndIf
		
		If CurrentObject\Attributes\LogicType=320 ; Void
			tex2$="SizeAdjust"
		EndIf
		
		If CurrentObject\Attributes\LogicType=433 ; Z-Bot NPC
			tex2$="Turning"
			If CurrentObject\Attributes\Data2=0
				tex$="Player"
			Else
				tex$="Fixed"
			EndIf
			tex$=CurrentObject\Attributes\Data2+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=180 ; Sign
			tex2$="Move"
			
				
			Select CurrentObject\Attributes\Data2
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
		If CurrentObject\Attributes\LogicType=51 ; spellball generator
			tex2$="Goal Y"
		EndIf
		
		If CurrentObject\Attributes\LogicType=431 ; zapbot
			tex2$="Speed"
		EndIf
		
		If CurrentObject\Attributes\LogicType=242 ; cuboid
			tex2$="CMD" ;"Explo CMD"
			tex$=CurrentObject\Attributes\Data2+"/"+GetCommandName$(CurrentObject\Attributes\Data2)
		EndIf
		
		If CurrentObject\Attributes\LogicType=434 ; mothership
			tex2$="SourceX"
		EndIf
		
		If CurrentObject\Attributes\LogicType=52 ; meteor shooter
			tex2$="TargetY"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData2,CurrentObject\Attributes\Data2,tex$)

		
	Case "Data3"
		tex$=Str$(CurrentObject\Attributes\Data3)
		
		If CurrentObject\Attributes\ModelName$="!Suctube" Or CurrentObject\Attributes\ModelName$="!SuctubeX"
			tex2$="Style"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!IceBlock"
			tex2$="Style"
			Select CurrentObject\Attributes\Data3
			Case 0
				tex$="Ice"
			Case 1
				tex$="Floing"
			End Select
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC"
			tex2$="Colour1"
			tex$=GetAccessoryColorNameWithColorInt$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		EndIf
		
		If CurrentObject\Attributes\LogicType=432 Or CurrentObject\Attributes\ModelName$="!Pushbot" ; Moobots
			tex2$="Turn"
			If CurrentObject\Attributes\Data3=2
				tex$="180"
			Else
				If CurrentObject\Attributes\Data3 Mod 2=0
					tex$="Left"
				Else
					If CurrentObject\Attributes\Data3<0
						tex$="NoMove"
					Else
						tex$="Right"
					EndIf
				EndIf
			EndIf
			tex$=CurrentObject\Attributes\Data3+"/"+tex$
		EndIf
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="XAnim"
		EndIf
		
		If CurrentObject\Attributes\LogicType=45 ; Conveyor heads
			tex2$="Turn"
			If CurrentObject\Attributes\Data3=0
				tex$="Left"
			Else If CurrentObject\Attributes\Data3=1
				tex$="Right"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=210 ; Transporters
			tex2$="Turn"
			If CurrentObject\Attributes\Data3=0
				tex$="180"
			Else If CurrentObject\Attributes\Data3=1
				tex$="(MOD) Left"
			Else If CurrentObject\Attributes\Data3=2
				tex$="(MOD) Right"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=46 ; conveyor tail
			tex2$="Cycles"
		EndIf
		If CurrentObject\Attributes\LogicType=40 ; stepping stone
			tex2$="Sound"
			If CurrentObject\Attributes\Data3=0
				tex$="Water"
			Else If CurrentObject\Attributes\Data3=1
				tex$="Mecha"
			Else If CurrentObject\Attributes\Data3=2
				tex$="Magic"
			Else
				tex$="Silent/Glitched"
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="SourceY"
		EndIf
		If CurrentObject\Attributes\LogicType=190 ; Particle Spawner
			If CurrentObject\Attributes\LogicSubType=4 Or CurrentObject\Attributes\LogicSubType=5
				tex2$="Sound"
				If CurrentObject\Attributes\Data3=0 tex$="None"
			EndIf
			
			If CurrentObject\Attributes\Data3=1
				If CurrentObject\Attributes\LogicSubType=4 tex$="Spark"
				If CurrentObject\Attributes\LogicSubType=5 tex$="QuietMagic"
			EndIf
			If CurrentObject\Attributes\Data3=2
				If CurrentObject\Attributes\LogicSubType=5 tex$="LoudMecha"
			EndIf
			If CurrentObject\Attributes\Data3=3
				If CurrentObject\Attributes\LogicSubType=5 tex$="Var.Gong"
			EndIf
			If CurrentObject\Attributes\Data3=4
				If CurrentObject\Attributes\LogicSubType=5 tex$="Grow Magic"
			EndIf
			If CurrentObject\Attributes\Data3=5
				If CurrentObject\Attributes\LogicSubType=5 tex$="Floing Magic"
			EndIf
			If CurrentObject\Attributes\Data3=6
				If CurrentObject\Attributes\LogicSubType=5 tex$="Gem"
			EndIf
		EndIf
		
		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$=GetCMDData3Name$(CurrentObject\Attributes\Data0)
			tex$=GetCmdData3ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="Colour4"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; Color Changer
				tex2$="SubCol To"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=0 ; NPC Modifier: NPC Move
				tex2$="Y Goal"
			Else If CurrentObject\Attributes\LogicSubType = 10 ; LevelExit
				tex2$="Dest Y"

			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="Expression"
				If CurrentObject\Attributes\Data3=-1
					tex$="No Change"
				Else
					tex$=GetStinkerExpressionName$(CurrentObject\Attributes\Data3)
				EndIf
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=2 ; NPC Modifier: NPC Exclamation
				tex2$="Count"
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$=GetCMDData3Name$(CurrentObject\Attributes\Data0)
				tex$=GetCmdData3ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="HitPoints"
		EndIf
		
		If CurrentObject\Attributes\LogicType=300 ; Brr float
			tex2$="RollAnim"
		EndIf
		
		If  CurrentObject\Attributes\LogicType=431 ; Zapbot
			tex2$="Range"
		EndIf
		
		If  CurrentObject\Attributes\LogicType=242 ; Cuboid
			;tex2$="Cmd Data1"
			tex2$=GetCMDData1Name$(CurrentObject\Attributes\Data2)
			tex$=GetCmdData1ValueName$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		EndIf
		
		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="SourceY"
		EndIf
		
		If CurrentObject\Attributes\LogicType=52 ; meteor shooter
			tex2$="TargetZ"
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterData3,CurrentObject\Attributes\Data3,tex$)

	Case "Data4"
		tex$=Str$(CurrentObject\Attributes\Data4)
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="YAnim"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Conveyor"
			tex2$="Visual Type"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC"
			tex2$="Acc2" ;"Glasses"
			
			tex$=GetAccessoryName$(CurrentObject\Attributes\Data4)
			
			tex$=CurrentObject\Attributes\Data4+"/"+tex$
		EndIf
		
		If IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			tex2$=GetCMDData4Name$(CurrentObject\Attributes\Data0)
			tex$=GetCmdData4ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data4)
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="SubColour1"

			Else If CurrentObject\Attributes\LogicSubType=10 ; LevelExit
				tex2$="PlayerYaw"
				DisplayedRotation=(CurrentObject\Attributes\Data4+180) Mod 360
				tex$=GetDirectionString$(DisplayedRotation)
				
			Else If CurrentObject\Attributes\LogicSubType=11 And (CurrentObject\Attributes\Data0=0 Or CurrentObject\Attributes\Data0=2) ; NPC Modifier: NPC Move or NPC Exclamation
				tex2$="Repeatable"
				If CurrentObject\Attributes\Data4=0
					tex$="Yes"
				Else
					tex$="No"
				EndIf
			Else If CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="Yaw"
				If CurrentObject\Attributes\Data4=-1
					tex$="No Change"
				Else
					;tex$=GetDirectionString$(CurrentObject\Attributes\Data4)
					tex$=CurrentObject\Attributes\Data4
				EndIf
			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				tex2$=GetCMDData4Name$(CurrentObject\Attributes\Data0)
				tex$=GetCmdData4ValueName$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data4)
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=281 ; suctube 
			tex2$="Sound"
			If CurrentObject\Attributes\Data4=0
				tex$="Normal"
			Else 
				tex$="Portal"
			EndIf
		EndIf
		

		
		If CurrentObject\Attributes\LogicType=190 ; Particle Emitter
			tex2$="Timing"
			If CurrentObject\Attributes\Data4=0 tex$="Random"
			If CurrentObject\Attributes\Data4=1 tex$="Synchro"
		EndIf
		
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			tex2$="PreviousHP"
		EndIf
		
		If CurrentObject\Attributes\LogicType=10 And CurrentObject\Attributes\LogicSubType=9 ; Autodoor
			tex2$=GetAutodoorActivateName$(CurrentObject\Attributes\Data4)
			tex$=GetAutodoorActivateValueName$(CurrentObject\Attributes\Data4)
		EndIf
		
		If CurrentObject\Attributes\LogicType=200 ; Glovecharge
			tex2$="(MOD) Homing"
			tex$=CurrentObject\Attributes\Data4+"/"+OneToYes$(CurrentObject\Attributes\Data4)
		EndIf

		If CurrentObject\Attributes\LogicType=242 ; Cuboid
			;tex2$="Cmd Data2"
			tex2$=GetCMDData2Name$(CurrentObject\Attributes\Data2)
			tex$=GetCmdData2ValueName$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data4)
		EndIf
		
		If CurrentObject\Attributes\LogicType=431 Or CurrentObject\Attributes\LogicType=422 ; Zapbot or UFO
			tex2$="Track"
			tex$=CurrentObject\Attributes\Data4+"/"+OneToYes$(CurrentObject\Attributes\Data4)
		EndIf
		
		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalX1"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData4,CurrentObject\Attributes\Data4,tex$)


	Case "Data5"
		tex$=Str$(CurrentObject\Attributes\Data5)
		
		If CurrentObject\Attributes\ModelName$="!NPC"
			tex2$="Colour2"
			tex$=GetAccessoryColorNameWithColorInt$(CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5+1)
		EndIf
		
		CurrentObjectModelName$=CurrentObject\Attributes\ModelName$
		If CurrentObjectModelName$="!Door" Or CurrentObjectModelName$="!Obstacle36" Or CurrentObjectModelName$="!Obstacle37" Or CurrentObjectModelName$="!Obstacle38" Or CurrentObjectModelName$="!Obstacle39" Or CurrentObjectModelName$="!Obstacle40"
			tex2$="Style"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!GlowWorm" Or CurrentObject\Attributes\ModelName$="!Zipper"
			tex2$="Red"
		EndIf
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="ZAnim"
		EndIf
		
		If CurrentObject\Attributes\LogicType=281 ; Suctube
			tex2$="Particles"
			If CurrentObject\Attributes\Data5=0
				tex$="Yes"
			Else
				tex$="No"
			EndIf
		EndIf
		

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="SubColour2"
			Else If CurrentObject\Attributes\LogicSubType = 10
				tex2$="FlyOver"
				If CurrentObject\Attributes\Data5=0
					tex$="No"
				Else
					tex$="Yes"
				EndIf


			Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 Or (CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1)
				tex2$="Repeatable"
				If CurrentObject\Attributes\Data5=0
					tex$="Yes"
				Else
					tex$="No"
				EndIf
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=0
				tex2$="DelayTimer"


			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor (should the tail really be here too?)
			tex2$="Logic"
			If CurrentObject\Attributes\Data5=0
				tex$="Move"
			Else
				tex$="Step"
			EndIf
		EndIf
		
		
		
		If CurrentObject\Attributes\LogicType=10 And CurrentObject\Attributes\LogicSubType=9 ; Autodoor
			tex2$=GetAutodoorActivateName$(CurrentObject\Attributes\Data5)
			tex$=GetAutodoorActivateValueName$(CurrentObject\Attributes\Data5)
		EndIf
		
		If CurrentObject\Attributes\LogicType=242 ; Cuboid
			;tex2$="Cmd Data3"
			tex2$=GetCMDData3Name$(CurrentObject\Attributes\Data2)
			tex$=GetCmdData3ValueName$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5)
		EndIf
		
		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalY1"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData5,CurrentObject\Attributes\Data5,tex$)
		
	Case "Data6"
		tex$=Str$(CurrentObject\Attributes\Data6)
		
		If CurrentObject\Attributes\ModelName$="!GlowWorm" Or CurrentObject\Attributes\ModelName$="!Zipper"
			tex2$="Green"
		EndIf
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="XSpeed"
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="SubColour3"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=0 ; NPC Modifier: NPC Move
				tex2$="DelayReset"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="WalkAnim"
				If CurrentObject\Attributes\Data6=-1
					tex$="No Change"
				Else
					tex$=GetStinkerNPCWalkAnimName$(CurrentObject\Attributes\Data6)
				EndIf
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			tex2$="WalkAnim"
			tex$=GetStinkerNPCWalkAnimName$(CurrentObject\Attributes\Data6)
		EndIf
		
		If CurrentObject\Attributes\LogicType=120 ; Wee Stinker
			tex2$="Burning"
			If CurrentObject\Attributes\Data6=600 tex$="Death"
		EndIf
		
		; Thwart, Ice Troll, Z-Bot NPC
		If CurrentObject\Attributes\LogicType=290 Or CurrentObject\Attributes\LogicType=380 Or CurrentObject\Attributes\LogicType=433
			tex2$="Shooter"
			If CurrentObject\Attributes\Data6>0
				tex$="Yes"
			Else
				tex$="No"
			EndIf
			tex$=CurrentObject\Attributes\Data6+"/"+tex$
		EndIf

		If CurrentObject\Attributes\LogicType=10 And CurrentObject\Attributes\LogicSubType=9 ; Autodoor
			tex2$=GetAutodoorActivateName$(CurrentObject\Attributes\Data6)
			tex$=GetAutodoorActivateValueName$(CurrentObject\Attributes\Data6)
		EndIf
		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor (is tail relevant here?)
			tex2$="ActivationWait"
		EndIf
		
		If CurrentObject\Attributes\LogicType=242 ; Cuboid
			;tex2$="Cmd Data4"
			tex2$=GetCMDData4Name$(CurrentObject\Attributes\Data2)
			tex$=GetCmdData4ValueName$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data6)
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalX2"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData6,CurrentObject\Attributes\Data6,tex$)

	Case "Data7"
		tex$=Str$(CurrentObject\Attributes\Data7)
		
		If CurrentObject\Attributes\ModelName$="!GlowWorm"  Or CurrentObject\Attributes\ModelName$="!Zipper"
			tex2$="Blue"
		EndIf
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="YSpeed"
		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				tex2$="SubColour4"
			Else If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="Turn"
				If CurrentObject\Attributes\Data7=-1
					tex$="No Change"
				Else
					tex$=GetNPCTurningName$(CurrentObject\Attributes\Data7)
				EndIf
				
				
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=110 Or CurrentObject\Attributes\LogicType=390 ; Stinker NPC or Kaboom NPC
		
			tex2$="Turn"
			tex$=GetNPCTurningName$(CurrentObject\Attributes\Data7)
		EndIf
		
		If CurrentObject\Attributes\LogicType=290 Or CurrentObject\Attributes\LogicType=380 Or CurrentObject\Attributes\LogicType=433 ; Thwart, Ice Troll, and Z-Bot NPC

			tex2$="AttackTimer" ; "TimerMax1"
		EndIf
		
		
		If CurrentObject\Attributes\LogicType=10 And CurrentObject\Attributes\LogicSubType=9 ; Autodoor
			tex2$="StayOnTimer"
			
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalY2"
		EndIf
		
		If CurrentObject\Attributes\LogicType=441 ; Sun Sphere 1
			tex2$="TimeOffset"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData7,CurrentObject\Attributes\Data7,tex$)

	Case "Data8"
		tex$=Str$(CurrentObject\Attributes\Data8)
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="ZSpeed"
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!StinkerWee"
			
			tex2$="Type"
			If CurrentObject\Attributes\Data8=0 tex$="Normal"
			If CurrentObject\Attributes\Data8=1 tex$="Green"
			If CurrentObject\Attributes\Data8=2 tex$="White"

		EndIf

		If CurrentObject\Attributes\LogicType=90 Or CurrentObject\Attributes\LogicType=210 ; button or transporter
			tex2$="ActivateID"
			If CurrentObject\Attributes\Data8=0
				tex$="All"
			Else If CurrentObject\Attributes\Data8=-2
				tex$="Pla"
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			
			tex2$="IdleAnim"
			tex$=GetStinkerNPCIdleAnimName$(CurrentObject\Attributes\Data8)			
			
		EndIf
		
		If CurrentObject\Attributes\LogicType=390 ; Kaboom NPC
			
			tex2$="Anim"
			If CurrentObject\Attributes\Data8=0 tex$="Stand"
			If CurrentObject\Attributes\Data8=1 tex$="Sit"
			If CurrentObject\Attributes\Data8=2 tex$="Sit/Stand"
			If CurrentObject\Attributes\Data8=3 tex$="Shiver Some"
			If CurrentObject\Attributes\Data8=4 tex$="Shiver Constant"
			If CurrentObject\Attributes\Data8=5 tex$="Exercise"
	
			
			
		EndIf
		
		If CurrentObject\Attributes\LogicType=400 ; Baby Boomer
			
			tex2$="Boom"
			If CurrentObject\Attributes\Data8=0 tex$="No"
			If CurrentObject\Attributes\Data8=1 tex$="Yes"
			
		EndIf
		
		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="FromZapbot"
			If CurrentObject\Attributes\Data8=0 tex$="No"
			If CurrentObject\Attributes\Data8=-99 tex$="Yes"
		EndIf
		
		If CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=431 ; UFO or Zapbot
			tex2$="LastShotTimer"
		EndIf

		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalX3"
		EndIf
		
		If CurrentObject\Attributes\LogicType=471 ; Wraith
			tex2$="TimeOffset"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData8,CurrentObject\Attributes\Data8,tex$)

	Case "Data9"
		tex$=Str$(CurrentObject\Attributes\Data9)
		
		If CurrentObject\Attributes\LogicType=160 And CurrentObject\Attributes\ModelName$="!CustomModel"
			tex2$="Deadly"
			If CurrentObject\Attributes\Data9=0 tex$="No"
			If CurrentObject\Attributes\Data9=1 tex$="Yes"
			

		EndIf

		If CurrentObject\Attributes\LogicType=90 ; button
			If CurrentObject\Attributes\LogicSubType = 11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier: NPC Change
				tex2$="IdleAnim"
				If CurrentObject\Attributes\Data9=-1
					tex$="No Change"
				Else
					tex$=GetStinkerNPCIdleAnimName$(CurrentObject\Attributes\Data9)
				EndIf
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor head (or conveyor tail, assuming this is needed?)
			tex2$="Tail Length"
		EndIf
		
		If CurrentObject\Attributes\LogicType=200
			tex2$="ReadyForSound"
			tex$=CurrentObject\Attributes\Data9+"/"+ZeroToYes$(CurrentObject\Attributes\Data9)
		EndIf
		
		If CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=430 Or CurrentObject\Attributes\LogicType=431 ; UFO or Zipbot or Zapbot
			tex2$="TrackTextureID"
			If CurrentObject\Attributes\Data9=-1
				tex$="Current"
			EndIf
		EndIf
				
		If CurrentObject\Attributes\LogicType=434 ; Mothership
			tex2$="FlyGoalY3"
		EndIf
		
		
		If CurrentObject\Attributes\LogicType=441 ; Sun Sphere 1
			tex2$="Empty"
			If CurrentObject\Attributes\Data9=0 tex$="No"
			If CurrentObject\Attributes\Data9=1 tex$="Yes"
		EndIf
		
		If CurrentObject\Attributes\LogicType=470 Or CurrentObject\Attributes\LogicType=471 ; Ghost or Wraith
			tex2$="Visibility"
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData9,CurrentObject\Attributes\Data9,tex$)
		
	Case "Talkable"
		tex2$="Dialog"
		If CurrentObject\Attributes\Talkable=0
			tex$="None"
		Else
			tex$=Str$(CurrentObject\Attributes\Talkable)
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTalkable,CurrentObject\Attributes\Talkable,tex$)
		
	Case "MovementType"
		tex$=CurrentObject\Attributes\MovementType+"/"+GetMovementTypeString$(CurrentObject\Attributes\MovementType)
		tex2$="MvmtType"
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMovementType,CurrentObject\Attributes\MovementType,tex$)
	Case "MovementTypeData"
		tex$=Str$(CurrentObject\Attributes\MovementTypeData)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMovementTypeData,CurrentObject\Attributes\MovementTypeData,tex$)
		
	Case "MovementSpeed"
		tex$=Str$(CurrentObject\Attributes\MovementSpeed)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMovementSpeed,CurrentObject\Attributes\MovementSpeed,tex$)
		
	Case "TileTypeCollision"
		tex$=DisplayAsBinaryString$(CurrentObject\Attributes\TileTypeCollision)
		tex2$="TTC"
		tex$=SetAdjusterDisplayInt(ObjectAdjusterTileTypeCollision,CurrentObject\Attributes\TileTypeCollision,tex$)
		
	Case "ObjectTypeCollision"
		tex$=DisplayAsBinaryString$(CurrentObject\Attributes\ObjectTypeCollision)
		tex2$="OTC"
		tex$=SetAdjusterDisplayInt(ObjectAdjusterObjectTypeCollision,CurrentObject\Attributes\ObjectTypeCollision,tex$)
		
	Case "ScaleAdjust"
		tex$=Str$(CurrentObject\Attributes\ScaleAdjust)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterScaleAdjust,CurrentObject\Attributes\ScaleAdjust,tex$)
	Case "Exclamation"
		If CurrentObject\Attributes\Exclamation=-1
			tex$="None"
		Else
			tex$=Str$(CurrentObject\Attributes\Exclamation)
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterExclamation,CurrentObject\Attributes\Exclamation,tex$)
		
	Case "Linked"
		If CurrentObject\Attributes\Linked=-1
			tex$="None"
		ElseIf CurrentObject\Attributes\Linked=-2
			tex$="Pla"
		Else
			tex$=Str$(CurrentObject\Attributes\Linked)
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterLinked,CurrentObject\Attributes\Linked,tex$)
		
	Case "LinkBack"
		If CurrentObject\Attributes\LinkBack=-1
			tex$="None"
		ElseIf CurrentObject\Attributes\LinkBack=-2
			tex$="Pla"
		Else
			tex$=Str$(CurrentObject\Attributes\LinkBack)
		EndIf
		
		tex$=SetAdjusterDisplayInt(ObjectAdjusterLinkBack,CurrentObject\Attributes\LinkBack,tex$)
	
	Case "Parent"
		tex$=Str$(CurrentObject\Attributes\Parent)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterParent,CurrentObject\Attributes\Parent,tex$)
		
	Case "Child"
		tex$=Str$(CurrentObject\Attributes\Child)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterChild,CurrentObject\Attributes\Child,tex$)
		
	Case "DX"
		tex$=Str$(CurrentObject\Attributes\DX)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterDX,CurrentObject\Attributes\DX,tex$)
		
	Case "DY"
		tex$=Str$(CurrentObject\Attributes\DY)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterDY,CurrentObject\Attributes\DY,tex$)
		
	Case "DZ"
		tex$=Str$(CurrentObject\Attributes\DZ)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterDZ,CurrentObject\Attributes\DZ,tex$)
		
	Case "MoveXGoal"
		tex$=Str$(CurrentObject\Attributes\MoveXGoal)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMoveXGoal,CurrentObject\Attributes\MoveXGoal,tex$)
		
	Case "MoveYGoal"
		tex$=Str$(CurrentObject\Attributes\MoveYGoal)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMoveYGoal,CurrentObject\Attributes\MoveYGoal,tex$)
		
	Case "Data10"
		tex$=Str$(CurrentObject\Attributes\Data10)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterData10,CurrentObject\Attributes\Data10,tex$)
		
	Case "Caged"
		tex$=Str$(CurrentObject\Attributes\Caged)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterCaged,CurrentObject\Attributes\Caged,tex$)
		
	Case "Dead"
		tex$=Str$(CurrentObject\Attributes\Dead)
		Select CurrentObject\Attributes\Dead
			Case 1
				tex$="Spinning"
			Case 3
				tex$="Sinking"
		End Select
		tex$=SetAdjusterDisplayInt(ObjectAdjusterDead,CurrentObject\Attributes\Dead,tex$)
		
	Case "DeadTimer"
		tex$=Str$(CurrentObject\Attributes\DeadTimer)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterDeadTimer,CurrentObject\Attributes\DeadTimer,tex$)
		
	Case "MovementTimer"
		tex$=Str$(CurrentObject\Attributes\MovementTimer)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterMovementTimer,CurrentObject\Attributes\MovementTimer,tex$)
		
	Case "Flying"
		State$="Grounded"
		If CurrentObject\Attributes\Flying/10 = 1	; bounced by spring
			If CurrentObject\Attributes\Flying Mod 10 >=1 And CurrentObject\Attributes\Flying Mod 10<=3 Then State$="Spr East"
			If CurrentObject\Attributes\Flying Mod 10 >=5 And CurrentObject\Attributes\Flying Mod 10<=7 Then State$="Spr West"
			If CurrentObject\Attributes\Flying Mod 10 >=3 And CurrentObject\Attributes\Flying Mod 10<=5 Then State$="Spr South"
			If CurrentObject\Attributes\Flying Mod 10 >=7 Or CurrentObject\Attributes\Flying Mod 10<=1 Then State$="Spr North"
		EndIf
	
		If CurrentObject\Attributes\Flying/10 = 2	; on ice
			If CurrentObject\Attributes\Flying Mod 10 >=1 And CurrentObject\Attributes\Flying Mod 10<=3 Then State$="Ice East"
			If CurrentObject\Attributes\Flying Mod 10 >=5 And CurrentObject\Attributes\Flying Mod 10<=7 Then State$="Ice West"
			If CurrentObject\Attributes\Flying Mod 10 >=3 And CurrentObject\Attributes\Flying Mod 10<=5 Then State$="Ice South"
			If CurrentObject\Attributes\Flying Mod 10 >=7 Or CurrentObject\Attributes\Flying Mod 10<=1 Then State$="Ice North"
		EndIf

		tex$=CurrentObject\Attributes\Flying+" ("+State+")"
		tex$=SetAdjusterDisplayInt(ObjectAdjusterFlying,CurrentObject\Attributes\Flying,tex$)
		
	Case "Indigo"
		tex$=Str$(CurrentObject\Attributes\Indigo)
		tex$=SetAdjusterDisplayInt(ObjectAdjusterIndigo,CurrentObject\Attributes\Indigo,tex$)
		
	Case "Speed"
		tex$=Str$(CurrentObject\Attributes\Speed)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterSpeed,CurrentObject\Attributes\Speed,tex$)
		
	Case "Radius"
		tex$=Str$(CurrentObject\Attributes\Radius)
		tex$=SetAdjusterDisplayFloat(ObjectAdjusterRadius,CurrentObject\Attributes\Radius,tex$)
		
	Case "Status"
		tex$=Str$(CurrentObject\Attributes\Status)
		
		If CurrentObject\Attributes\LogicType=50 ; spellball
			tex2$="FromPlayer"
			If CurrentObject\Attributes\Status=0 tex$="No"
			If CurrentObject\Attributes\Status=1 tex$="Yes"
		EndIf
		
		If CurrentObject\Attributes\LogicType=434 ; Mothership
			If CurrentObject\Attributes\Status=0
				tex$="Goal 1"
			ElseIf CurrentObject\Attributes\Status=1
				tex$="Goal 2"
			ElseIf CurrentObject\Attributes\Status=2
				tex$="Goal 3"
			ElseIf CurrentObject\Attributes\Status<-199
				tex$=CurrentObject\Attributes\Status+"/Eversplode"
			ElseIf CurrentObject\Attributes\Status<0
				tex$="Exploding "+Str$(-CurrentObject\Attributes\Status)
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=470 Or CurrentObject\Attributes\LogicType=471 ; Wraith or Ghost
			Select CurrentObject\Attributes\Status
			Case 0
				tex$="Hidden"
			Case 1
				tex$="Attacking"
			Case 2
				tex$="Watching"
			End Select
		EndIf

		tex$=SetAdjusterDisplayInt(ObjectAdjusterStatus,CurrentObject\Attributes\Status,tex$)
	
	End Select	
	
	
	If HighlightWopAdjusters And AdjusterAppearsInWop(ObjectAdjuster$(i))
		Color TextAdjusterHighlightedR,TextAdjusterHighlightedG,TextAdjusterHighlightedB
	Else
		Color TextAdjusterR,TextAdjusterG,TextAdjusterB
	EndIf
	
	CrossedOut=CurrentAdjusterRandomized
	
	If CrossedOut
		tex$=tex2$
		Dashes$=""
		For t=1 To Len(tex2$)
			Dashes$=Dashes$+"-"
		Next
		
		HalfNameWidth=4*Len(tex$)
		
		Text StartX+92-HalfNameWidth,StartY,Dashes$
		
		Text StartX+2,StartY,LeftAdj$
		Text StartX+182-8*Len(RightAdj$),StartY,RightAdj$
		
	ElseIf tex2$<>"" And ObjectAdjuster$(i)<>"TextData0" And ObjectAdjuster$(i)<>"TextData1"
		If CurrentAdjusterAbsolute
			tex$=tex2$+": "+tex$
		Else
			If CurrentAdjusterZero
				tex$=tex2$+": ..."
			Else
				tex$=tex2$+" += "+tex$
			EndIf
		EndIf
	EndIf
	
	Text StartX+92-4*Len(tex$),StartY,tex$

End Function

Function GetMagicName$(id)
	Select id
		Case -1
			Return "No Charge"
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
		Case 10
			Return "Turret"
		;Default
		;	Return id
	End Select
End Function

Function GetMagicNameAndId$(id)
	Return Str(id) + ". " + GetMagicName(id)
End Function

Function GetMagicColor(id,index)

	Select id
	Case -1 ; no charge (white in OpenWA, black in vanilla; same as not having any magic charged to your gloves)
		red=255
		green=255
		blue=255
	Case 0 ; floing
		red=255
	Case 1 ; pow
		red=255
		green=100
	Case 2 ; pop
		red=255
		green=255
	Case 3 ; grow
		green=255
	Case 4 ; brr
		green=255
		blue=255
	Case 5 ; flash
		blue=255
	Case 6 ; blink
		red=255
		blue=255
	Case 7 ; null
		red=255
		blue=255
		green=255
	Case 8 ; rainbow
		;red=ii*64
		;green=255-ii*32
		;blue=255-ii*64
		red=255
		green=255
		blue=255
	Case 9 ; barrel
		red=67
		blue=67
		green=67
	Case 10 ; turret
		red=107
		green=0
		blue=153
	End Select
	
	If index=0 Return red
	If index=1 Return green
	If index=2 Return blue
	
	Return 255

End Function


Function GapSubType(SmallerExclusive,LargerExclusive)

	If CurrentObject\Attributes\LogicSubType>SmallerExclusive And CurrentObject\Attributes\LogicSubType<LargerExclusive-20
		CurrentObject\Attributes\LogicSubType=LargerExclusive
	Else If CurrentObject\Attributes\LogicSubType>LargerExclusive-19 And CurrentObject\Attributes\LogicSubType<LargerExclusive
		CurrentObject\Attributes\LogicSubType=SmallerExclusive
	EndIf

End Function

Function OnLeftHalfAdjuster()

	Return MouseX()<SidebarX+102 ;602

End Function

Function SetupPrompt()
	FlushKeys
	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
End Function

Function InputString$(title$)
	SetupPrompt()
	Result$=Input$(title$)
	ReturnKeyReleased=False
	Return Result$
End Function

Function InputInt(title$)
	SetupPrompt()
	Result=Input(title$)
	ReturnKeyReleased=False
	Return Result
End Function

Function InputFloat#(title$)
	SetupPrompt()
	Result#=Input$(title$)
	ReturnKeyReleased=False
	Return Result#
End Function

Global SomethingWasAdjusted=False
Global UsedRawInput=False

Function WasAdjusted()

	Result=SomethingWasAdjusted
	SomethingWasAdjusted=False
	Return Result

End Function

Function AdjustInt(ValueName$, CurrentValue, NormalSpeed, FastSpeed, DelayTime)

	SomethingWasAdjusted=False
	UsedRawInput=False

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True
	
	Adj=NormalSpeed
	If Fast Adj=FastSpeed
	
	If LeftMouse=True
		If RawInput=True
			SomethingWasAdjusted=True
			UsedRawInput=True
			Return InputInt(ValueName$)
		ElseIf MouseDebounceFinished()
			SomethingWasAdjusted=True
			MouseDebounceSet(DelayTime)
			Return CurrentValue+Adj
		EndIf
	EndIf
	If RightMouse=True And MouseDebounceFinished()
		SomethingWasAdjusted=True
		MouseDebounceSet(DelayTime)
		Return CurrentValue-Adj
	EndIf
	If MouseScroll<>0
		SomethingWasAdjusted=True
	EndIf
	Return CurrentValue+MouseScroll*Adj

End Function

Function ZeroRoundFloat#(Value#)

	If Value#>-0.00001 And Value#<0.00001
		Return 0.0
	Else
		Return Value#
	EndIf

End Function

Function AdjustFloat#(ValueName$, CurrentValue#, NormalSpeed#, FastSpeed#, DelayTime)

	Result#=AdjustFloatWithoutZeroRounding#(ValueName$, CurrentValue#, NormalSpeed#, FastSpeed#, DelayTime)
	Result#=ZeroRoundFloat#(Result#)
	Return Result#

End Function

Function AdjustFloatWithoutZeroRounding#(ValueName$, CurrentValue#, NormalSpeed#, FastSpeed#, DelayTime)

	SomethingWasAdjusted=False
	UsedRawInput=False

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True
	
	Adj#=NormalSpeed
	If Fast Adj#=FastSpeed
	
	If LeftMouse=True
		If RawInput=True
			SomethingWasAdjusted=True
			UsedRawInput=True
			Return InputFloat#(ValueName$)
		ElseIf MouseDebounceFinished()
			SomethingWasAdjusted=True
			MouseDebounceSet(DelayTime)
			Return CurrentValue+Adj
		EndIf
	EndIf
	If RightMouse=True And MouseDebounceFinished()
		SomethingWasAdjusted=True
		MouseDebounceSet(DelayTime)
		Return CurrentValue-Adj
	EndIf
	If MouseScroll<>0
		SomethingWasAdjusted=True
	EndIf
	Return CurrentValue+MouseScroll*Adj

End Function


Function InputTextureName(Prompt$)

	CurrentObject\Attributes\TexName$=InputString$(Prompt$)
	ObjectAdjusterTextureName\Absolute=True
	If Left$(CurrentObject\Attributes\TexName$,1)="/"
		CurrentObject\Attributes\TexName$="userdata/custom/models/"+Right$(CurrentObject\Attributes\TexName$,Len(CurrentObject\Attributes\TexName$)-1)
	Else
		CurrentObject\Attributes\TexName$=TexturePrefix$+CurrentObject\Attributes\TexName$
	EndIf

End Function


Function InputModelName(Prompt$)

	CurrentObject\Attributes\ModelName$=InputString$(Prompt$)
	ObjectAdjusterModelName\Absolute=True
	If CurrentObject\Attributes\ModelName$="!CustomModel"
		CurrentObject\Attributes\TextData0$=InputString$("Enter custom model name (e.g. Default): ")
	ElseIf Left$(CurrentObject\Attributes\ModelName$,1)="/" Or Left$(CurrentObject\Attributes\ModelName$,1)="?"
		CurrentObject\Attributes\TextData0$=Right$(CurrentObject\Attributes\ModelName$,Len(CurrentObject\Attributes\ModelName$)-1)
		CurrentObject\Attributes\ModelName$="!CustomModel"
	EndIf

End Function


Function ConfirmFindAndReplace()

	Return GetConfirmation("Find and replace on matching values for ALL objects?")

End Function


Function CurrentObjectWasChanged()

	CurrentGrabbedObjectModified=True
	
	If AreAllObjectAdjustersAbsolute()
		SetBrushToCurrentObject()
	EndIf

End Function

Function SetBrushToCurrentObject()

	NofBrushObjects=1
	BrushSpaceWidth=1
	BrushSpaceHeight=1
	CopyObjectToBrush(CurrentObject,0,0,0)
	GenerateBrushPreview()

End Function

Function CurrentTileWasChanged()

	SetBrushToCurrentTile()

End Function

Function SetBrushToCurrentTile()

	BrushSpaceWidth=1
	BrushSpaceHeight=1
	CopyTile(CurrentTile,BrushTiles(0,0))
	GenerateBrushPreview()

End Function


Function AdjustObjectAdjuster(i)

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True
	
	DelayTime=10 ;150
	SlowInt=1
	FastInt=10
	FastID=50
	FastTimer=25
	FastRotate=45
	SlowFloat#=0.01
	FastFloat#=0.1
	SlowScale#=0.001

	Select ObjectAdjuster$(i)
	Case "TextData0"
		If LeftMouse=True
			If FindAndReplaceKeyDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObject\Attributes\TextData0
					CurrentObject\Attributes\TextData0$=InputString$("Replacement TextData0: ")
					ObjectAdjusterTextData0\Absolute=True
					For j=0 To NofObjects-1
						LevelObject.GameObject=LevelObjects(j)
						If LevelObject\Attributes\TextData0$=Target$
							LevelObject\Attributes\TextData0$=CurrentObject\Attributes\TextData0$
							UpdateLevelObjectModel(j)
						EndIf
					Next
					
					AddUnsavedChange()
				EndIf
			Else
				CurrentObject\Attributes\TextData0=InputString$("TextData0: ")
				ObjectAdjusterTextData0\Absolute=True
			EndIf
		EndIf
	Case "TextData1"
		If LeftMouse=True
			If FindAndReplaceKeyDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObject\Attributes\TextData1$
					CurrentObject\Attributes\TextData1$=InputString$("Replacement TextData1: ")
					ObjectAdjusterTextData1\Absolute=True
					For j=0 To NofObjects-1
						LevelObject.GameObject=LevelObjects(j)
						If LevelObject\Attributes\TextData1$=Target$
							LevelObject\Attributes\TextData1$=CurrentObject\Attributes\TextData1$
							UpdateLevelObjectModel(j)
						EndIf
					Next
					
					AddUnsavedChange()
				EndIf
			Else
				CurrentObject\Attributes\TextData1$=InputString$("TextData1: ")
				ObjectAdjusterTextData1\Absolute=True
			EndIf
		EndIf
	Case "TextureName"
		If LeftMouse=True
			If FindAndReplaceKeyDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObject\Attributes\TexName$
					InputTextureName("Replacement TextureName: ")
					For j=0 To NofObjects-1
						LevelObject.GameObject=LevelObjects(j)
						If LevelObject\Attributes\TexName$=Target$
							LevelObject\Attributes\TexName$=CurrentObject\Attributes\TexName$
							UpdateLevelObjectModel(j)
						EndIf
					Next
					
					AddUnsavedChange()
				EndIf
			Else
				InputTextureName("TextureName: ")
			EndIf
		EndIf
		
	Case "ModelName"
		If MouseScroll<>0
			If Left$(CurrentObject\Attributes\ModelName$,9)="!Obstacle"
				If ShiftDown()
					TheInt=FastInt
				Else
					TheInt=SlowInt
				EndIf
				ObstacleId=ObstacleNameToObstacleId(CurrentObject\Attributes\ModelName$)
				ObstacleId=ObstacleId+MouseScroll*TheInt
				If ObstacleId<0
					ObstacleId=0
				ElseIf ObstacleId>99
					ObstacleId=99
				EndIf
				If ObstacleId<10
					ObstacleIdString$="0"+ObstacleId
				Else
					ObstacleIdString$=ObstacleId
				EndIf
				CurrentObject\Attributes\ModelName$="!Obstacle"+ObstacleIdString$
			EndIf
		ElseIf LeftMouse=True
			If FindAndReplaceKeyDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObject\Attributes\ModelName$
					InputModelName("Replacement ModelName: ")
					For j=0 To NofObjects-1
						LevelObject.GameObject=LevelObjects(j)
						If LevelObject\Attributes\ModelName$=Target$
							LevelObject\Attributes\ModelName$=CurrentObject\Attributes\ModelName$
							If CurrentObject\Attributes\ModelName$="!CustomModel"
								LevelObject\Attributes\TextData0$=CurrentObject\Attributes\TextData0$
							EndIf
							UpdateLevelObjectModel(j)
						EndIf
					Next
					
					AddUnsavedChange()
				EndIf
			Else
				InputModelName("ModelName: ")
			EndIf
		EndIf
		
	Case "DefensePower"
		CurrentObject\Attributes\DefensePower=AdjustObjectAdjusterInt(ObjectAdjusterDefensePower,CurrentObject\Attributes\DefensePower,SlowInt,FastInt,DelayTime)
		
		If CurrentObject\Attributes\DefensePower>=34 Then CurrentObject\Attributes\DefensePower=0
		If CurrentObject\Attributes\DefensePower<0 Then CurrentObject\Attributes\DefensePower=33
		
	Case "AttackPower"
		CurrentObject\Attributes\AttackPower=AdjustObjectAdjusterInt(ObjectAdjusterAttackPower,CurrentObject\Attributes\AttackPower,SlowInt,FastInt,DelayTime)
		
	Case "DestructionType"
		CurrentObject\Attributes\DestructionType=AdjustObjectAdjusterInt(ObjectAdjusterDestructionType,CurrentObject\Attributes\DestructionType,SlowInt,FastInt,DelayTime)
	
	Case "YawAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		CurrentObject\Attributes\YawAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterYawAdjust,CurrentObject\Attributes\YawAdjust,SlowFloat#,FastFloat#,DelayTime)
		
		If CurrentObject\Attributes\YawAdjust>=360 Then CurrentObject\Attributes\YawAdjust=CurrentObject\Attributes\YawAdjust-360
		If CurrentObject\Attributes\YawAdjust<0 Then CurrentObject\Attributes\YawAdjust=CurrentObject\Attributes\YawAdjust+360
		
	Case "PitchAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		CurrentObject\Attributes\PitchAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterPitchAdjust,CurrentObject\Attributes\PitchAdjust,SlowFloat#,FastFloat#,DelayTime)
		
		If CurrentObject\Attributes\PitchAdjust>=360 Then CurrentObject\Attributes\PitchAdjust=CurrentObject\Attributes\PitchAdjust-360
		If CurrentObject\Attributes\PitchAdjust<0 Then CurrentObject\Attributes\PitchAdjust=CurrentObject\Attributes\PitchAdjust+360
		
	Case "RollAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		CurrentObject\Attributes\RollAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterRollAdjust,CurrentObject\Attributes\RollAdjust,SlowFloat#,FastFloat#,DelayTime)
		
		If CurrentObject\Attributes\RollAdjust>=360 Then CurrentObject\Attributes\RollAdjust=CurrentObject\Attributes\RollAdjust-360
		If CurrentObject\Attributes\RollAdjust<0 Then CurrentObject\Attributes\RollAdjust=CurrentObject\Attributes\RollAdjust+360


		
	Case "XAdjust"
		CurrentObject\Attributes\XAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterXAdjust,CurrentObject\Attributes\XAdjust,SlowFloat#,FastFloat#,DelayTime)
	Case "YAdjust"
		CurrentObject\Attributes\YAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterYAdjust,CurrentObject\Attributes\YAdjust,SlowFloat#,FastFloat#,DelayTime)
	Case "ZAdjust"
		CurrentObject\Attributes\ZAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterZAdjust,CurrentObject\Attributes\ZAdjust,SlowFloat#,FastFloat#,DelayTime)
		
		
	Case "XScale"
		SlowFloat#=SlowScale#
		CurrentObject\Attributes\XScale=AdjustObjectAdjusterFloat(ObjectAdjusterXScale,CurrentObject\Attributes\XScale,SlowFloat#,FastFloat#,DelayTime)
	Case "YScale"
		SlowFloat#=SlowScale#
		CurrentObject\Attributes\YScale=AdjustObjectAdjusterFloat(ObjectAdjusterYScale,CurrentObject\Attributes\YScale,SlowFloat#,FastFloat#,DelayTime)
	Case "ZScale"
		SlowFloat#=SlowScale#
		CurrentObject\Attributes\ZScale=AdjustObjectAdjusterFloat(ObjectAdjusterZScale,CurrentObject\Attributes\ZScale,SlowFloat#,FastFloat#,DelayTime)


	Case "ID"
		FastInt=FastID
		CurrentObject\Attributes\ID=AdjustObjectAdjusterInt(ObjectAdjusterID,CurrentObject\Attributes\ID,SlowInt,FastInt,DelayTime)

	Case "Type"
		CurrentObject\Attributes\LogicType=AdjustObjectAdjusterInt(ObjectAdjusterLogicType,CurrentObject\Attributes\LogicType,SlowInt,FastInt,DelayTime)
	Case "SubType"
		CurrentObject\Attributes\LogicSubType=AdjustObjectAdjusterInt(ObjectAdjusterLogicSubType,CurrentObject\Attributes\LogicSubType,SlowInt,FastInt,DelayTime)
				
		If CurrentObject\Attributes\LogicType=179 ; Custom Item
		
			Min=-400
			Max=509
		
			If CurrentObject\Attributes\LogicSubType<Min
				CurrentObject\Attributes\LogicSubType=Max
			EndIf
				
			GapSubType(-400,-300)
			GapSubType(-300,-200)
			GapSubType(-195,-100)
			GapSubType(-98,-6)
				
			If CurrentObject\Attributes\LogicSubType>27 And CurrentObject\Attributes\LogicSubType<490
				CurrentObject\Attributes\LogicSubType=509
			Else If CurrentObject\Attributes\LogicSubType>489 And CurrentObject\Attributes\LogicSubType<509
				CurrentObject\Attributes\LogicSubType=27
				
			Else If CurrentObject\Attributes\LogicSubType>Max
				CurrentObject\Attributes\LogicSubType=Min

			Else If CurrentObject\Attributes\LogicSubType=8
				CurrentObject\Attributes\LogicSubType=10
			Else If CurrentObject\Attributes\LogicSubType=9
				CurrentObject\Attributes\LogicSubType=7
				
			Else If CurrentObject\Attributes\LogicSubType=18
				CurrentObject\Attributes\LogicSubType=20
			Else If CurrentObject\Attributes\LogicSubType=19
				CurrentObject\Attributes\LogicSubType=17
				
			EndIf
		
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			If CurrentObject\Attributes\LogicSubType<0 Then CurrentObject\Attributes\LogicSubType=3
			If CurrentObject\Attributes\LogicSubType>3 Then CurrentObject\Attributes\LogicSubType=0
		EndIf
		If CurrentObject\Attributes\LogicType=370 ; Crab
			If CurrentObject\Attributes\LogicSubType<0 Then CurrentObject\Attributes\LogicSubType=1
			If CurrentObject\Attributes\LogicSubType>1 Then CurrentObject\Attributes\LogicSubType=0
		EndIf
		

	Case "Active"
		CurrentObject\Attributes\Active=AdjustObjectAdjusterToggle(ObjectAdjusterActive,CurrentObject\Attributes\Active,SlowInt,FastInt,RawInput,0,1001,DelayTime)
		
	Case "ActivationSpeed"
		SlowInt=SlowInt*2
		FastInt=FastInt*2
		CurrentObject\Attributes\ActivationSpeed=AdjustObjectAdjusterInt(ObjectAdjusterActivationSpeed,CurrentObject\Attributes\ActivationSpeed,SlowInt,FastInt,DelayTime)
	Case "ActivationType"
		CurrentObject\Attributes\ActivationType=AdjustObjectAdjusterInt(ObjectAdjusterActivationType,CurrentObject\Attributes\ActivationType,SlowInt,FastInt,DelayTime)
			
		;If CurrentObject\Attributes\ModelName$="!SteppingStone"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObject\Attributes\ActivationType=3 
		;			CurrentObject\Attributes\ActivationType=16
		;		Else If CurrentObject\Attributes\ActivationType=16
		;			CurrentObject\Attributes\ActivationType=21
		;		Else
		;			CurrentObject\Attributes\ActivationType=3
		;		EndIf
		;	EndIf
		;Else If CurrentObject\Attributes\ModelName$="!ColourGate"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObject\Attributes\ActivationType=1 
		;			CurrentObject\Attributes\ActivationType=2
		;		Else If CurrentObject\Attributes\ActivationType=2
		;			CurrentObject\Attributes\ActivationType=3
		;		Else If CurrentObject\Attributes\ActivationType=3
		;			CurrentObject\Attributes\ActivationType=11
		;		Else If CurrentObject\Attributes\ActivationType=11
		;			CurrentObject\Attributes\ActivationType=21
		;		Else
		;			CurrentObject\Attributes\ActivationType=1
		;		EndIf
		;	EndIf
		;Else If CurrentObject\Attributes\ModelName$="!Autodoor"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObject\Attributes\ActivationType=11 
		;			CurrentObject\Attributes\ActivationType=17
		;		Else If CurrentObject\Attributes\ActivationType=17
		;			CurrentObject\Attributes\ActivationType=18
		;		Else If CurrentObject\Attributes\ActivationType=18
		;			CurrentObject\Attributes\ActivationType=19
		;		Else If CurrentObject\Attributes\ActivationType=19
		;			CurrentObject\Attributes\ActivationType=20
		;		Else
		;			CurrentObject\Attributes\ActivationType=11
		;		EndIf
		;	EndIf
		
	Case "TimerMax1"
		FastInt=FastTimer
		CurrentObject\Attributes\TimerMax1=AdjustObjectAdjusterInt(ObjectAdjusterTimerMax1,CurrentObject\Attributes\TimerMax1,SlowInt,FastInt,DelayTime)
	Case "TimerMax2"
		FastInt=FastTimer
		CurrentObject\Attributes\TimerMax2=AdjustObjectAdjusterInt(ObjectAdjusterTimerMax2,CurrentObject\Attributes\TimerMax2,SlowInt,FastInt,DelayTime)
	Case "Timer"
		FastInt=FastTimer
		CurrentObject\Attributes\Timer=AdjustObjectAdjusterInt(ObjectAdjusterTimer,CurrentObject\Attributes\Timer,SlowInt,FastInt,DelayTime)
		
	Case "ButtonPush"
		CurrentObject\Attributes\ButtonPush=AdjustObjectAdjusterToggle(ObjectAdjusterButtonPush,CurrentObject\Attributes\ButtonPush,SlowInt,FastInt,RawInput,0,1,DelayTime)
		
	Case "WaterReact"
		CurrentObject\Attributes\WaterReact=AdjustObjectAdjusterInt(ObjectAdjusterWaterReact,CurrentObject\Attributes\WaterReact,SlowInt,FastInt,DelayTime)
	Case "Freezable"
		CurrentObject\Attributes\Freezable=AdjustObjectAdjusterInt(ObjectAdjusterFreezable,CurrentObject\Attributes\Freezable,SlowInt,FastInt,DelayTime)
	Case "Frozen"
		CurrentObject\Attributes\Frozen=AdjustObjectAdjusterInt(ObjectAdjusterFrozen,CurrentObject\Attributes\Frozen,SlowInt,FastInt,DelayTime)		
	Case "Teleportable"
		CurrentObject\Attributes\Teleportable=AdjustObjectAdjusterToggle(ObjectAdjusterTeleportable,CurrentObject\Attributes\Teleportable,SlowInt,FastInt,RawInput,0,1,DelayTime)
		
	Case "Data0"
		If CurrentObject\Attributes\ModelName$="!Gem"
			ObjectAdjusterData0\RandomMinDefault=0
			ObjectAdjusterData0\RandomMaxDefault=2
		Else
			ObjectAdjusterData0\RandomMinDefault=0
			ObjectAdjusterData0\RandomMaxDefault=10
		EndIf
		
		OldData=CurrentObject\Attributes\Data0
		
		;CurrentObject\Attributes\Data0=AdjustInt("Data0: ", CurrentObject\Attributes\Data0, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data0=AdjustObjectAdjusterInt(ObjectAdjusterData0,CurrentObject\Attributes\Data0,SlowInt,FastInt,DelayTime)
		
		CurrentObject\Attributes\ModelName$=CurrentObject\Attributes\ModelName$
		CurrentObject\Attributes\LogicType=CurrentObject\Attributes\LogicType
		
		If CurrentObject\Attributes\ModelName$="!Scritter" ;Or CurrentObject\Attributes\ModelName$="!Cuboid" Or CurrentObject\Attributes\LogicType=424
			; colours 0-6
			If CurrentObject\Attributes\Data0>6 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=6
	
;		Else If CurrentObject\Attributes\ModelName$="!Obstacle51" Or CurrentObject\Attributes\ModelName$="!Obstacle55" Or CurrentObject\Attributes\ModelName$="!Obstacle59"
			; Shape
;			If CurrentObject\Attributes\Data0>3 CurrentObject\Attributes\Data0=0
;			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=3
		EndIf
		
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			SetThreeOtherDataIfNotEqual(1,2,3,0,OldData)
		EndIf

		
		If CurrentObject\Attributes\LogicType=190 Or CurrentObject\Attributes\LogicType=164
			; particle spray
			If CurrentObject\Attributes\Data0>63 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=63
		EndIf
		If CurrentObject\Attributes\ModelName$="!Gem"
			; Shape
			If CurrentObject\Attributes\Data0>2 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=2
		EndIf
;		If CurrentObject\Attributes\ModelName$="!Crystal"
;			If CurrentObject\Attributes\Data0>1 CurrentObject\Attributes\Data0=0
;			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=1
;		EndIf
		If CurrentObject\Attributes\LogicType=260 ; Spikeyball
			If CurrentObject\Attributes\Data1=2
				If CurrentObject\Attributes\Data0>7 CurrentObject\Attributes\Data0=0
				If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=7
			Else
				If CurrentObject\Attributes\Data0>3 CurrentObject\Attributes\Data0=0
				If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=3
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			If CurrentObject\Attributes\Data0>7 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=7
		EndIf
		If CurrentObject\Attributes\LogicType=220 ; Turtle
			; Direction
			If CurrentObject\Attributes\Data0>3 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=3
		EndIf
		If CurrentObject\Attributes\ModelName="!Kaboom"
			; texture
			If CurrentObject\Attributes\Data0>5 CurrentObject\Attributes\Data0=1
			If CurrentObject\Attributes\Data0<1 CurrentObject\Attributes\Data0=5
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Wisp"
			; texture
			If CurrentObject\Attributes\Data0>9 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=9
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Sign"
			; shape
			If CurrentObject\Attributes\Data0>5 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=5
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!WaterFall"
			; liquid type
			If CurrentObject\Attributes\Data0>2 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=2
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC"
			; Texture
			If CurrentObject\Attributes\Data0>8 CurrentObject\Attributes\Data0=1
			If CurrentObject\Attributes\Data0<1 CurrentObject\Attributes\Data0=8
		EndIf
		
		If CurrentObject\Attributes\LogicType=470 Or CurrentObject\Attributes\LogicType=471 ; ghost or wraith
			If CurrentObject\Attributes\Data1<2 CurrentObject\Attributes\Data1=2
		EndIf



			

	Case "Data1"
		If CurrentObject\Attributes\ModelName$="!Gem"
			ObjectAdjusterData1\RandomMinDefault=0
			ObjectAdjusterData1\RandomMaxDefault=6
		Else
			ObjectAdjusterData1\RandomMinDefault=0
			ObjectAdjusterData1\RandomMaxDefault=10
		EndIf
	
		;CurrentObject\Attributes\Data1=AdjustInt("Data1: ", CurrentObject\Attributes\Data1, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data1=AdjustObjectAdjusterInt(ObjectAdjusterData1,CurrentObject\Attributes\Data1,SlowInt,FastInt,DelayTime)
		
;		If CurrentObject\Attributes\ModelName$="!Obstacle51" Or CurrentObject\Attributes\ModelName$="!Obstacle55" Or CurrentObject\Attributes\ModelName$="!Obstacle59"
;			; Texture
;			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=0
;			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=3
;		EndIf
	
		
		If CurrentObject\Attributes\LogicType=190
			; particle spray intensity
			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=1
			If CurrentObject\Attributes\Data1<1 CurrentObject\Attributes\Data1=3
		EndIf
		
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=3
		EndIf 
		
		If CurrentObject\Attributes\LogicType=242 ; Cuboid

			If CurrentObject\Attributes\Data1>1 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=1

		EndIf
		
		If CurrentObject\Attributes\LogicType=260 ; SpikeyBall
			If CurrentObject\Attributes\Data1>2 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=2

			If CurrentObject\Attributes\Data1=2
				If CurrentObject\Attributes\Data0>7 CurrentObject\Attributes\Data0=0
				If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=7
			Else
				If CurrentObject\Attributes\Data0>3 CurrentObject\Attributes\Data0=0
				If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=3
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=250 ; Chomper
			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=3
		EndIf
		If CurrentObject\Attributes\LogicType=220 ; Turtle
			If CurrentObject\Attributes\Data1>1 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=1
		EndIf
		If CurrentObject\Attributes\LogicType=370 ; Crab
			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=3
		EndIf

		

		If CurrentObject\Attributes\ModelName$="!NPC"
			; Expression
			If CurrentObject\Attributes\Data1>4 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=4
		EndIf
		
		; ufo or retro z-bot or zipbot or zapbot
;		If CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=423 Or CurrentObject\Attributes\LogicType=430 Or CurrentObject\Attributes\LogicType=431
;			; turning
;			If CurrentObject\Attributes\Data1>1 CurrentObject\Attributes\Data1=0
;			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=1
;		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Portal Warp"
			; ???
			If CurrentObject\Attributes\Data1>1 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=1
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Sign"
			; texture
			If CurrentObject\Attributes\Data1>5 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=5
		EndIf


		


	Case "Data2"
		If CurrentObject\Attributes\ModelName$="!NPC"
			; Use vanilla ranges
			; Hats
			ObjectAdjusterData2\RandomMinDefault=0
			ObjectAdjusterData2\RandomMaxDefault=56
		Else
			ObjectAdjusterData2\RandomMinDefault=0
			ObjectAdjusterData2\RandomMaxDefault=10
		EndIf
	
		;CurrentObject\Attributes\Data2=AdjustInt("Data2: ", CurrentObject\Attributes\Data2, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data2=AdjustObjectAdjusterInt(ObjectAdjusterData2,CurrentObject\Attributes\Data2,SlowInt,FastInt,DelayTime)
		
		If CurrentObject\Attributes\LogicType=280 Or CurrentObject\Attributes\LogicType=410 ; Spring or FlipBridge
			; direction 0-7
			If CurrentObject\Attributes\Data2>7 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=7
		EndIf
		If CurrentObject\Attributes\LogicType=281 Or CurrentObject\Attributes\LogicType=282 Or CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Suctube or Suctube X or Conveyor
			; direction 0-3
			If CurrentObject\Attributes\Data2>3 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=3
		EndIf

		; transporter, pushbot
		If CurrentObject\Attributes\LogicType=210 Or CurrentObject\Attributes\LogicType=432
			; direction 0-3
			If CurrentObject\Attributes\Data2>3 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=3
		EndIf

		If CurrentObject\Attributes\LogicType=90
			If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17
				; direction 0-1
				If CurrentObject\Attributes\Data2>1 CurrentObject\Attributes\Data2=0
				If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=1
			EndIf
			If CurrentObject\Attributes\LogicSubType=11
				Select CurrentObject\Attributes\Data0
				Case 0
					; x goal
					If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=0
				Case 1
					; talkable
					If CurrentObject\Attributes\Data2<-1 CurrentObject\Attributes\Data2=-1
				Case 2
					; particle
					If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=63
					If CurrentObject\Attributes\Data2>63 CurrentObject\Attributes\Data2=0
				End Select
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=190
			; particle spray direction
			If CurrentObject\Attributes\Data2>5 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=5
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!NPC"
			;If CurrentObject\Attributes\Data2>56 CurrentObject\Attributes\Data2=0
			;If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=56
			CurrentObject\Attributes\Data3=1

		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Thwart"
			; colour
			If CurrentObject\Attributes\Data2>7 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=7
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!Wraith"
			; Doubles as both magic type and texture
			If CurrentObject\Attributes\Data2>2 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=2
		EndIf

		
;		If CurrentObject\Attributes\LogicType=433 ; Z-Bot NPC
;			If CurrentObject\Attributes\Data2>1 CurrentObject\Attributes\Data2=0
;			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=1
;		EndIf
		

	Case "Data3"
		;CurrentObject\Attributes\Data3=AdjustInt("Data3: ", CurrentObject\Attributes\Data3, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data3=AdjustObjectAdjusterInt(ObjectAdjusterData3,CurrentObject\Attributes\Data3,SlowInt,FastInt,DelayTime)
		
		If CurrentObject\Attributes\LogicType=190
			If CurrentObject\Attributes\Data3<0 Then CurrentObject\Attributes\Data3=0
			Select CurrentObject\Attributes\LogicSubType
			Case 4
				If CurrentObject\Attributes\Data3>1 Then CurrentObject\Attributes\Data3=0
			Case 5
				If CurrentObject\Attributes\Data3>6 Then CurrentObject\Attributes\Data3=0
			End Select
		EndIf

		If CurrentObject\Attributes\LogicType=40 ; stepping stone
			; sound
			If CurrentObject\Attributes\Data3>3 CurrentObject\Attributes\Data3=0
			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=3
		EndIf
		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=11 ; button
			Select CurrentObject\Attributes\Data0
			Case 0
				; y goal
				If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=0
			Case 1
				; y goal
				If CurrentObject\Attributes\Data3<-1 CurrentObject\Attributes\Data3=4
				If CurrentObject\Attributes\Data3>4 CurrentObject\Attributes\Data3=-1
			Case 2
				; how many particles
				If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=9
				If CurrentObject\Attributes\Data3>9 CurrentObject\Attributes\Data3=0
			End Select
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			; hitpoints
			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=0
		EndIf

;		If CurrentObject\Attributes\LogicType=432 ; moobot
			; pushbot left/right turn,
;			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=2
;			If CurrentObject\Attributes\Data3>2 CurrentObject\Attributes\Data3=0
;		EndIf
		If  CurrentObject\Attributes\LogicType=45 ; conveyor lead
			; turn direction
			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=1
			If CurrentObject\Attributes\Data3>1 CurrentObject\Attributes\Data3=0
		EndIf

;		If  CurrentObject\Attributes\LogicType=46 ; conveyor tail
;			If CurrentObject\Attributes\Data3<1 CurrentObject\Attributes\Data3=1
;			
;		EndIf

		If CurrentObject\Attributes\ModelName$="!Suctube" Or CurrentObject\Attributes\ModelName$="!SuctubeX"
			; Suctube tex
			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=0
			If CurrentObject\Attributes\Data3>2 CurrentObject\Attributes\Data3=2
		EndIf


	Case "Data4"
		If CurrentObject\Attributes\ModelName$="!NPC"
			; Glasses
			ObjectAdjusterData4\RandomMinDefault=101
			ObjectAdjusterData4\RandomMaxDefault=116
		Else
			ObjectAdjusterData4\RandomMinDefault=0
			ObjectAdjusterData4\RandomMaxDefault=10
		EndIf
	
		Adj=1
		AdjFast=10
		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=10 ; LevelExit
			Adj=45
			AdjFast=45
		EndIf
		
		OldData=CurrentObject\Attributes\Data4
		
		;CurrentObject\Attributes\Data4=AdjustInt("Data4: ", CurrentObject\Attributes\Data4, Adj, AdjFast, DelayTime)
		CurrentObject\Attributes\Data4=AdjustObjectAdjusterInt(ObjectAdjusterData4,CurrentObject\Attributes\Data4,Adj,AdjFast,DelayTime)

		If CurrentObject\Attributes\LogicType=90
			If CurrentObject\Attributes\LogicSubType=10 ; LevelExit
				;playerstartingyaw
				If CurrentObject\Attributes\Data4<0 Then CurrentObject\Attributes\Data4=360-45
				If CurrentObject\Attributes\Data4>359 Then CurrentObject\Attributes\Data4=0
			ElseIf CurrentObject\Attributes\LogicSubType=11 ; NPC Modifier
				If (CurrentObject\Attributes\Data0=0 Or CurrentObject\Attributes\Data0=2)
					; repeatable
					If CurrentObject\Attributes\Data4<0 CurrentObject\Attributes\Data4=1
					If CurrentObject\Attributes\Data4>1 CurrentObject\Attributes\Data4=0
				ElseIf CurrentObject\Attributes\Data0=1
					; yaw
					If CurrentObject\Attributes\Data4<-1 CurrentObject\Attributes\Data4=359
					If CurrentObject\Attributes\Data4>359 CurrentObject\Attributes\Data4=0
				EndIf
			ElseIf IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				SetThreeOtherDataIfNotEqual(5,6,7,4,OldData)
			EndIf
		EndIf

		If CurrentObject\Attributes\ModelName$="!NPC"
;			If CurrentObject\Attributes\Data4=-1 CurrentObject\Attributes\Data4=116
;			If CurrentObject\Attributes\Data4=1 CurrentObject\Attributes\Data4=101
;			If CurrentObject\Attributes\Data4=100 CurrentObject\Attributes\Data4=0
;			If CurrentObject\Attributes\Data4=117 CurrentObject\Attributes\Data4=0

			; Set the glasses color back to 1.
			CurrentObject\Attributes\Data5=0
		EndIf
		
		If CurrentObject\Attributes\LogicType=190
			If CurrentObject\Attributes\Data4<0 Then CurrentObject\Attributes\Data4=0
			If CurrentObject\Attributes\Data4>1 Then CurrentObject\Attributes\Data4=0
		EndIf
		
		
;		If CurrentObject\Attributes\LogicType=431 Or CurrentObject\Attributes\LogicType=422 ; Zapbot or UFO
;			; zapbot track?
;			If CurrentObject\Attributes\Data4<0 CurrentObject\Attributes\Data4=1
;			If CurrentObject\Attributes\Data4>1 CurrentObject\Attributes\Data4=0
;		EndIf
		
		
		If  CurrentObject\Attributes\ModelName$="!Conveyor"
			; visual type
			If CurrentObject\Attributes\Data4<0 CurrentObject\Attributes\Data4=4
			If CurrentObject\Attributes\Data4>4 CurrentObject\Attributes\Data4=0
		EndIf


		If CurrentObject\Attributes\LogicType=281 ; Suctube
			; sound
			If CurrentObject\Attributes\Data4<0 CurrentObject\Attributes\Data4=1
			If CurrentObject\Attributes\Data4>1 CurrentObject\Attributes\Data4=0
		EndIf



	Case "Data5"
		;CurrentObject\Attributes\Data5=AdjustInt("Data5: ", CurrentObject\Attributes\Data5, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data5=AdjustObjectAdjusterInt(ObjectAdjusterData5,CurrentObject\Attributes\Data5,SlowInt,FastInt,DelayTime)
		
		If CurrentObject\Attributes\LogicType=90 ; button
			If (CurrentObject\Attributes\LogicSubType Mod 32)=15
				; repeatable
				If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
				If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
			EndIf
			If CurrentObject\Attributes\LogicSubType=11
				If CurrentObject\Attributes\Data0=0
					; timer
					If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=0
				ElseIf CurrentObject\Attributes\Data0=1
					; repeatable
					If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
					If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
				EndIf
			ElseIf CurrentObject\Attributes\LogicSubType=10
				; levelexit flyover
				If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
				If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor
			; Logic
			If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
			If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!GlowWorm"  Or CurrentObject\Attributes\ModelName$="!Zipper"
			If CurrentObject\Attributes\Data5>255 CurrentObject\Attributes\Data5=0
			If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=255
		EndIf
		
		If CurrentObject\Attributes\LogicType=281 ;CurrentObject\Attributes\ModelName$="!Suctube"
			; particles
			If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
			If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
		EndIf

			


	Case "Data6"
		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			ObjectAdjusterData6\RandomMinDefault=0
			ObjectAdjusterData6\RandomMaxDefault=2
		Else
			ObjectAdjusterData6\RandomMinDefault=0
			ObjectAdjusterData6\RandomMaxDefault=10
		EndIf
	
		;CurrentObject\Attributes\Data6=AdjustInt("Data6: ", CurrentObject\Attributes\Data6, 1, 10, 150)
		CurrentObject\Attributes\Data6=AdjustObjectAdjusterInt(ObjectAdjusterData6,CurrentObject\Attributes\Data6,SlowInt,FastInt,DelayTime)
		
		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=11 ; NPC Modifier
			If CurrentObject\Attributes\Data0=0 ; NPC Move
				; timer reset
				If CurrentObject\Attributes\Data6<0 CurrentObject\Attributes\Data6=0
			;ElseIf CurrentObject\Attributes\Data0=1
				; walk anim
				;If CurrentObject\Attributes\Data6<-1 CurrentObject\Attributes\Data6=2
				;If CurrentObject\Attributes\Data6>2 CurrentObject\Attributes\Data6=-1
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			; WalkAnim
			;If CurrentObject\Attributes\Data6>2 CurrentObject\Attributes\Data6=0
			;If CurrentObject\Attributes\Data6<0 CurrentObject\Attributes\Data6=2
		EndIf

;		If CurrentObject\Attributes\LogicType=290 Or CurrentObject\Attributes\LogicType=380 Or CurrentObject\Attributes\LogicType=433 ; Thwart or Ice Troll or Z-Bot NPC
;			; Shooter
;			If CurrentObject\Attributes\Data6>1 CurrentObject\Attributes\Data6=0
;			If CurrentObject\Attributes\Data6<0 CurrentObject\Attributes\Data6=1
;		EndIf

		If CurrentObject\Attributes\ModelName$="!GlowWorm"  Or CurrentObject\Attributes\ModelName$="!Zipper"
			If CurrentObject\Attributes\Data6>255 CurrentObject\Attributes\Data6=0
			If CurrentObject\Attributes\Data6<0 CurrentObject\Attributes\Data6=255
		EndIf



	Case "Data7"
		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			ObjectAdjusterData7\RandomMinDefault=0
			ObjectAdjusterData7\RandomMaxDefault=30
		Else
			ObjectAdjusterData7\RandomMinDefault=0
			ObjectAdjusterData7\RandomMaxDefault=10
		EndIf

		CurrentObject\Attributes\Data7=AdjustObjectAdjusterInt(ObjectAdjusterData7,CurrentObject\Attributes\Data7,SlowInt,FastInt,DelayTime)
		
		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier
			; turn

			If CurrentObject\Attributes\Data7=-2 CurrentObject\Attributes\Data7=25
			If CurrentObject\Attributes\Data7=26 CurrentObject\Attributes\Data7=-1
			If CurrentObject\Attributes\Data7=6 CurrentObject\Attributes\Data7=10
			If CurrentObject\Attributes\Data7=9 CurrentObject\Attributes\Data7=5
			If CurrentObject\Attributes\Data7=16 CurrentObject\Attributes\Data7=20
			If CurrentObject\Attributes\Data7=19 CurrentObject\Attributes\Data7=15
			
;			If CurrentObject\Attributes\Data7=-2 CurrentObject\Attributes\Data7=35
;			If CurrentObject\Attributes\Data7=36 CurrentObject\Attributes\Data7=-1
;			
;			If CurrentObject\Attributes\Data7=6 CurrentObject\Attributes\Data7=10
;			If CurrentObject\Attributes\Data7=9 CurrentObject\Attributes\Data7=5
;			If CurrentObject\Attributes\Data7=16 CurrentObject\Attributes\Data7=20
;			If CurrentObject\Attributes\Data7=19 CurrentObject\Attributes\Data7=15
;			If CurrentObject\Attributes\Data7=26 CurrentObject\Attributes\Data7=30
;			If CurrentObject\Attributes\Data7=29 CurrentObject\Attributes\Data7=25

		EndIf
		
		If CurrentObject\Attributes\ModelName$="!GlowWorm"  Or CurrentObject\Attributes\ModelName$="!Zipper"
			If CurrentObject\Attributes\Data7>255 CurrentObject\Attributes\Data7=0
			If CurrentObject\Attributes\Data7<0 CurrentObject\Attributes\Data7=255
			

		EndIf
		
;		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
;
;			; Turn ; Stinker NPC Turn
;			If CurrentObject\Attributes\Data7=-1 CurrentObject\Attributes\Data7=35
;			If CurrentObject\Attributes\Data7=36 CurrentObject\Attributes\Data7=0
;			
;			If CurrentObject\Attributes\Data7=6 CurrentObject\Attributes\Data7=10
;			If CurrentObject\Attributes\Data7=9 CurrentObject\Attributes\Data7=5
;			If CurrentObject\Attributes\Data7=16 CurrentObject\Attributes\Data7=20
;			If CurrentObject\Attributes\Data7=19 CurrentObject\Attributes\Data7=15
;			If CurrentObject\Attributes\Data7=26 CurrentObject\Attributes\Data7=30
;			If CurrentObject\Attributes\Data7=29 CurrentObject\Attributes\Data7=25
;
;		EndIf
		
		If CurrentObject\Attributes\LogicType=110 Or CurrentObject\Attributes\LogicType=390 ; Stinker NPC or Kaboom NPC
			; Turn
			If CurrentObject\Attributes\Data7=-1 CurrentObject\Attributes\Data7=25
			If CurrentObject\Attributes\Data7=26 CurrentObject\Attributes\Data7=0
			If CurrentObject\Attributes\Data7=6 CurrentObject\Attributes\Data7=10
			If CurrentObject\Attributes\Data7=9 CurrentObject\Attributes\Data7=5
			If CurrentObject\Attributes\Data7=16 CurrentObject\Attributes\Data7=20
			If CurrentObject\Attributes\Data7=19 CurrentObject\Attributes\Data7=15
		EndIf


	Case "Data8"
		;CurrentObject\Attributes\Data8=AdjustInt("Data8: ", CurrentObject\Attributes\Data8, 1, 10, 150)
		PrevValue=CurrentObject\Attributes\Data8
		CurrentObject\Attributes\Data8=AdjustObjectAdjusterInt(ObjectAdjusterData8,CurrentObject\Attributes\Data8,SlowInt,FastInt,DelayTime)
		NewValue=CurrentObject\Attributes\Data8

		If CurrentObject\Attributes\LogicType=90 Or CurrentObject\Attributes\LogicType=210 ; button or transporter
			; ActivateID (Pla is -2, so skip -1 to get there)
			If NewValue>PrevValue
				If CurrentObject\Attributes\Data8<0 Then CurrentObject\Attributes\Data8=0
			Else
				If CurrentObject\Attributes\Data8<0 Then CurrentObject\Attributes\Data8=-2
			EndIf
;			If LeftMouse=True
;				If CurrentObject\Attributes\Data8<0 Then CurrentObject\Attributes\Data8=0
;			ElseIf RightMouse=True
;				If CurrentObject\Attributes\Data8<0 Then CurrentObject\Attributes\Data8=-2
;			EndIf
		EndIf
		
		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			; IdleAnim
			If CurrentObject\Attributes\Data8>10 CurrentObject\Attributes\Data8=0
			If CurrentObject\Attributes\Data8<0 CurrentObject\Attributes\Data8=10
		EndIf
		
		If CurrentObject\Attributes\LogicType=390 ; Kaboom NPC
			; IdleAnim
			If CurrentObject\Attributes\Data8>5 CurrentObject\Attributes\Data8=0
			If CurrentObject\Attributes\Data8<0 CurrentObject\Attributes\Data8=5
		EndIf

		If CurrentObject\Attributes\LogicType=400 ; Baby Boomer
			; Boom?
			If CurrentObject\Attributes\Data8>1 CurrentObject\Attributes\Data8=0
			If CurrentObject\Attributes\Data8<0 CurrentObject\Attributes\Data8=1
		EndIf
		
		If CurrentObject\Attributes\ModelName$="!StinkerWee"
			; Texture
			If CurrentObject\Attributes\Data8>2 CurrentObject\Attributes\Data8=0
			If CurrentObject\Attributes\Data8<0 CurrentObject\Attributes\Data8=2
		EndIf



	Case "Data9"
		;CurrentObject\Attributes\Data9=AdjustInt("Data9: ", CurrentObject\Attributes\Data9, 1, 10, 150)
		CurrentObject\Attributes\Data9=AdjustObjectAdjusterInt(ObjectAdjusterData9,CurrentObject\Attributes\Data9,SlowInt,FastInt,DelayTime)
		
		If CurrentObject\Attributes\ModelName$="!CustomModel" And CurrentObject\Attributes\LogicType=160
			; Deadly
			If CurrentObject\Attributes\Data9>1 CurrentObject\Attributes\Data9=0
			If CurrentObject\Attributes\Data9<0 CurrentObject\Attributes\Data9=1
		EndIf
		
		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=1 ; NPC Change
			; anim
			If CurrentObject\Attributes\Data9<-1 CurrentObject\Attributes\Data9=10
			If CurrentObject\Attributes\Data9>10 CurrentObject\Attributes\Data9=-1
		EndIf
		
		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor
			If CurrentObject\Attributes\Data9<1 CurrentObject\Attributes\Data9=1
		EndIf

	Case "Talkable"
		CurrentObject\Attributes\Talkable=AdjustObjectAdjusterInt(ObjectAdjusterTalkable,CurrentObject\Attributes\Talkable,SlowInt,FastInt,DelayTime)

	Case "MovementSpeed"
		CurrentObject\Attributes\MovementSpeed=AdjustObjectAdjusterInt(ObjectAdjusterMovementSpeed,CurrentObject\Attributes\MovementSpeed,SlowInt,FastInt,DelayTime)
		
	Case "MovementType"
		CurrentObject\Attributes\MovementType=AdjustObjectAdjusterInt(ObjectAdjusterMovementType,CurrentObject\Attributes\MovementType,SlowInt,FastInt,DelayTime)
		
	Case "MovementTypeData"
		CurrentObject\Attributes\MovementTypeData=AdjustObjectAdjusterInt(ObjectAdjusterMovementTypeData,CurrentObject\Attributes\MovementTypeData,SlowInt,FastInt,DelayTime)
		
	Case "TileTypeCollision"
		CurrentObject\Attributes\TileTypeCollision=AdjustObjectAdjusterBits(ObjectAdjusterTileTypeCollision,CurrentObject\Attributes\TileTypeCollision,i,DelayTime)
		
	Case "ObjectTypeCollision"
		CurrentObject\Attributes\ObjectTypeCollision=AdjustObjectAdjusterBits(ObjectAdjusterObjectTypeCollision,CurrentObject\Attributes\ObjectTypeCollision,i,DelayTime)
		
	Case "ScaleAdjust"
		CurrentObject\Attributes\ScaleAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterScaleAdjust,CurrentObject\Attributes\ScaleAdjust,SlowFloat#,FastFloat#,DelayTime)
		
	Case "Exclamation"
		CurrentObject\Attributes\Exclamation=AdjustObjectAdjusterInt(ObjectAdjusterExclamation,CurrentObject\Attributes\Exclamation,SlowInt,FastInt,DelayTime)
		
	Case "Linked"
		CurrentObject\Attributes\Linked=AdjustObjectAdjusterInt(ObjectAdjusterLinked,CurrentObject\Attributes\Linked,SlowInt,FastInt,DelayTime)
	Case "LinkBack"
		CurrentObject\Attributes\LinkBack=AdjustObjectAdjusterInt(ObjectAdjusterLinkBack,CurrentObject\Attributes\LinkBack,SlowInt,FastInt,DelayTime)
		
	Case "Parent"
		CurrentObject\Attributes\Parent=AdjustObjectAdjusterInt(ObjectAdjusterParent,CurrentObject\Attributes\Parent,SlowInt,FastInt,DelayTime)
	Case "Child"
		CurrentObject\Attributes\Child=AdjustObjectAdjusterInt(ObjectAdjusterChild,CurrentObject\Attributes\Child,SlowInt,FastInt,DelayTime)
		
	Case "DX"
		CurrentObject\Attributes\DX=AdjustObjectAdjusterFloat(ObjectAdjusterDX,CurrentObject\Attributes\DX,SlowFloat#,FastFloat#,DelayTime)
	Case "DY"
		CurrentObject\Attributes\DY=AdjustObjectAdjusterFloat(ObjectAdjusterDY,CurrentObject\Attributes\DY,SlowFloat#,FastFloat#,DelayTime)
	Case "DZ"
		CurrentObject\Attributes\DZ=AdjustObjectAdjusterFloat(ObjectAdjusterDZ,CurrentObject\Attributes\DZ,SlowFloat#,FastFloat#,DelayTime)
		
	Case "MoveXGoal"
		ObjectAdjusterMoveXGoal\RandomMaxDefault=LevelWidth-1
		CurrentObject\Attributes\MoveXGoal=AdjustObjectAdjusterInt(ObjectAdjusterMoveXGoal,CurrentObject\Attributes\MoveXGoal,SlowInt,FastInt,DelayTime)
	Case "MoveYGoal"
		ObjectAdjusterMoveYGoal\RandomMaxDefault=LevelHeight-1
		CurrentObject\Attributes\MoveYGoal=AdjustObjectAdjusterInt(ObjectAdjusterMoveYGoal,CurrentObject\Attributes\MoveYGoal,SlowInt,FastInt,DelayTime)
		
	Case "Data10"
		CurrentObject\Attributes\Data10=AdjustObjectAdjusterInt(ObjectAdjusterData10,CurrentObject\Attributes\Data10,SlowInt,FastInt,DelayTime)
		
	Case "Caged"
		CurrentObject\Attributes\Caged=AdjustObjectAdjusterInt(ObjectAdjusterCaged,CurrentObject\Attributes\Caged,SlowInt,FastInt,DelayTime)
	Case "Dead"
		CurrentObject\Attributes\Dead=AdjustObjectAdjusterInt(ObjectAdjusterDead,CurrentObject\Attributes\Dead,SlowInt,FastInt,DelayTime)
	Case "DeadTimer"
		CurrentObject\Attributes\DeadTimer=AdjustObjectAdjusterInt(ObjectAdjusterDeadTimer,CurrentObject\Attributes\DeadTimer,SlowInt,25,DelayTime)
	Case "MovementTimer"
		CurrentObject\Attributes\MovementTimer=AdjustObjectAdjusterInt(ObjectAdjusterMovementTimer,CurrentObject\Attributes\MovementTimer,SlowInt,25,DelayTime)
		
	Case "Flying"
		CurrentObject\Attributes\Flying=AdjustObjectAdjusterInt(ObjectAdjusterDead,CurrentObject\Attributes\Flying,SlowInt,FastInt,DelayTime)
		
	Case "Indigo"
		CurrentObject\Attributes\Indigo=AdjustObjectAdjusterInt(ObjectAdjusterIndigo,CurrentObject\Attributes\Indigo,SlowInt,FastInt,DelayTime)
		
	Case "Speed"
		CurrentObject\Attributes\Speed=AdjustObjectAdjusterFloat(ObjectAdjusterSpeed,CurrentObject\Attributes\Speed,SlowFloat#,FastFloat#,DelayTime)
	Case "Radius"
		CurrentObject\Attributes\Radius=AdjustObjectAdjusterFloat(ObjectAdjusterRadius,CurrentObject\Attributes\Radius,SlowFloat#,FastFloat#,DelayTime)
		
	Case "Status"
		CurrentObject\Attributes\Status=AdjustObjectAdjusterInt(ObjectAdjusterStatus,CurrentObject\Attributes\Status,SlowInt,FastInt,DelayTime)
		
		
		
	End Select
	
	; avoid false positives from pressing enter
	If LeftMouse=True Or RightMouse=True Or MouseScroll<>0
		BuildCurrentObjectModel()
		CurrentObjectWasChanged()
	EndIf

End Function


Function UpdateTile(i,j)

	UpdateLevelTile(i,j)
	UpdateWaterTile(i,j)
	UpdateLogicTile(i,j)

End Function

Function UpdateLevelTile(i,j)

	If i<0 Or j<0 Or i>=levelwidth Or j>=levelheight Then Return

	ResetLevelTile(i,j)
	UpdateLevelTileTexture(i,j)
	ShiftLevelTileByExtrude(i,j)
	ShiftLevelTileByRandom(i,j)
	ShiftLevelTileByHeight(i,j)
	ShiftLevelTileEdges(i,j)
	UpdateLevelTileSides(i,j)

End Function

Function UpdateWater()

	If WaterFlow>=0
		PositionTexture WaterTexture,0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
	EndIf
	If waterflow<0
		; rock
		PositionTexture WaterTexture,0,0.5+0.125*WaterFlow/4*Sin(-(4*LevelTimer*WaterFlow)/10.0)
	EndIf
	
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateWaterVertices(i,j)
		Next
	Next

End Function

Function UpdateWaterVertices(i,j)
	mySurface=WaterSurface(j)
	Turbulence#=LevelTiles(i,j)\Water\Turbulence
	If Turbulence#<0.0
		Turbulence=0.0
	EndIf
	
	VertexCoords mySurface,i*4+0,VertexX(mySurface,i*4+0),LevelTiles(i,j)\Water\Height+Turbulence*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180),VertexZ(mySurface,i*4+0)
	VertexCoords mySurface,i*4+1,VertexX(mySurface,i*4+1),LevelTiles(i,j)\Water\Height+Turbulence*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180+90),VertexZ(mySurface,i*4+1)

	VertexCoords mySurface,i*4+2,VertexX(mySurface,i*4+2),LevelTiles(i,j)\Water\Height+Turbulence*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180-180),VertexZ(mySurface,i*4+2)
	VertexCoords mySurface,i*4+3,VertexX(mySurface,i*4+3),LevelTiles(i,j)\Water\Height+Turbulence*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180+90-180),VertexZ(mySurface,i*4+3)
End Function

Function UpdateWaterTile(i,j)
	
	If i<0 Or j<0 Or i>=levelwidth Or j>=levelheight Then Return
	
	; top face
	CalculateUV(LevelTiles(i,j)\Water\Texture,0,0,LevelTiles(i,j)\Water\Rotation,4,1)
	VertexCoords WaterSurface(j),i*4+0,i,LevelTiles(i,j)\Water\Height,-j
	VertexTexCoords WaterSurface(j),i*4+0,ChunkTileU,ChunkTileV
	If LevelTiles(i,j)\Water\Texture>=0
		VertexColor WaterSurface(j),i*4+0,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+0,0,0,0
	EndIf
	
	CalculateUV(LevelTiles(i,j)\Water\Texture,1,0,LevelTiles(i,j)\Water\Rotation,4,1)
	VertexCoords WaterSurface(j),i*4+1,i+1,LevelTiles(i,j)\Water\Height,-j
	VertexTexCoords WaterSurface(j),i*4+1,ChunkTileU,ChunkTileV
	If LevelTiles(i,j)\Water\Texture>=0
		VertexColor WaterSurface(j),i*4+1,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+1,0,0,0
	EndIf

	CalculateUV(LevelTiles(i,j)\Water\Texture,0,1,LevelTiles(i,j)\Water\Rotation,4,1)
	VertexCoords WaterSurface(j),i*4+2,i,LevelTiles(i,j)\Water\Height,-j-1
	VertexTexCoords WaterSurface(j),i*4+2,ChunkTileU,ChunkTileV
	If LevelTiles(i,j)\Water\Texture>=0
		VertexColor WaterSurface(j),i*4+2,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+2,0,0,0
	EndIf

	CalculateUV(LevelTiles(i,j)\Water\Texture,1,1,LevelTiles(i,j)\Water\Rotation,4,1)
	VertexCoords WaterSurface(j),i*4+3,i+1,LevelTiles(i,j)\Water\Height,-j-1
	VertexTexCoords WaterSurface(j),i*4+3,ChunkTileU,ChunkTileV
	If LevelTiles(i,j)\Water\Texture>=0
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
	
	If LevelTiles(i,j)\Water\Height>LevelTiles(i,j)\Terrain\Extrusion
		height#=LevelTiles(i,j)\Water\Height+0.05
	Else
		height#=LevelTiles(i,j)\Terrain\Extrusion+0.05
	EndIf
	VertexCoords LogicSurface(j),i*4,i+nologicshow,height,-j
	VertexCoords LogicSurface(j),i*4+1,i+1+nologicshow,height,-j
	VertexCoords LogicSurface(j),i*4+2,i+nologicshow,height,-j-1
	VertexCoords LogicSurface(j),i*4+3,i+1+nologicshow,height,-j-1
				
	
	ColorLevelTileLogic(i,j)

End Function

; Used for models that do not exist.
Function CreateErrorMesh()

	Entity=CreateSphere()
	ScaleMesh Entity,.3,.3,.3
	UseErrorColor(Entity)
	Return Entity

End Function

; Used for textures that do not exist.
Function UseErrorColor(Entity)

	EntityColor Entity,ModelErrorR,ModelErrorG,ModelErrorB

End Function

Function UseMagicColor(Entity,Col)

	EntityColor Entity,GetMagicColor(Col,0),GetMagicColor(Col,1),GetMagicColor(Col,2)

End Function

Function CreateSignMesh(Data0,Data1)

	If Data0>-1 And Data0<6
		Entity=CopyEntity(SignMesh(Data0))
		If Data1>-1 And Data1<6
			EntityTexture Entity,SignTexture(Data1)
		Else
			UseErrorColor(Entity)
		EndIf
	Else
		Entity=CreateErrorMesh()
	EndIf
	
	Return Entity

End Function

Function CreateNoneMesh()

	Entity=CreateSphere()
	ScaleMesh Entity,.2,.2,.2
	Return Entity

End Function

Function CreateSpellBallMesh(subtype)

	entity=CreateSphere(4)
	UseMagicColor(Entity,subtype)
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
		;ShowMessageOnce("!IceBlock with a Data3 that isn’t 0 or 1 will be invisible in-game.", 2000)
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

Function CreateMagicMirrorMesh()

	Entity=CreateCube()
	ScaleMesh Entity,3.5,2.59,.52
	;EntityColor Entity,255,0,0
	;EntityAlpha Entity,.5
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
	CalculateUV(CurrentTile\Terrain\Texture,0,0,CurrentTile\Terrain\Rotation,8,1)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\Texture,1,0,CurrentTile\Terrain\Rotation,8,1)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\Texture,0,1,CurrentTile\Terrain\Rotation,8,1)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\Texture,1,1,CurrentTile\Terrain\Rotation,8,1)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)
	
	AddTriangle (CurrentSurface,i*20+0,i*20+1,i*20+2)
	AddTriangle (CurrentSurface,i*20+1,i*20+3,i*20+2)
	
	; north face
	CalculateUV(CurrentTile\Terrain\SideTexture,0,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,0,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,99,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,99,1,ChunkTileU,ChunkTileV)

	
	AddTriangle (CurrentSurface,i*20+4,i*20+5,i*20+6)
	AddTriangle (CurrentSurface,i*20+5,i*20+7,i*20+6)
	
	
	; east face
	CalculateUV(CurrentTile\Terrain\SideTexture,0,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,0,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,99,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,99,1,ChunkTileU,ChunkTileV)

	
	AddTriangle (CurrentSurface,i*20+8,i*20+9,i*20+10)
	AddTriangle (CurrentSurface,i*20+9,i*20+11,i*20+10)
	
	; south face
	CalculateUV(CurrentTile\Terrain\SideTexture,0,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,0,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,99,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,99,-1,ChunkTileU,ChunkTileV)

	
	AddTriangle (CurrentSurface,i*20+12,i*20+13,i*20+14)
	AddTriangle (CurrentSurface,i*20+13,i*20+15,i*20+14)
	
	; west face
	CalculateUV(CurrentTile\Terrain\SideTexture,0,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,0,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,99,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,99,-1,ChunkTileU,ChunkTileV)

	
	AddTriangle (CurrentSurface,i*20+16,i*20+17,i*20+18)
	AddTriangle (CurrentSurface,i*20+17,i*20+19,i*20+18)

	
	UpdateNormals CurrentMesh
	EntityTexture CurrentMesh,LevelTexture
	
	; and the water tile
	; top face
	
	mySurface=CurrentWaterTileSurface

	CalculateUV(CurrentTile\Water\Texture,0,0,CurrentTile\Water\Rotation,4,LevelDetail)
	VertexTexCoords mySurface,0,ChunkTileU,ChunkTileV
	CalculateUV(CurrentTile\Water\Texture,LevelDetail,0,CurrentTile\Water\Rotation,4,LevelDetail)
	VertexTexCoords mySurface,1,ChunkTileU,ChunkTileV
	CalculateUV(CurrentTile\Water\Texture,0,LevelDetail,CurrentTile\Water\Rotation,4,LevelDetail)
	VertexTexCoords mySurface,2,ChunkTileU,ChunkTileV
	CalculateUV(CurrentTile\Water\Texture,LevelDetail,LevelDetail,CurrentTile\Water\Rotation,4,LevelDetail)
	VertexTexCoords mySurface,3,ChunkTileU,ChunkTileV
	
	EntityTexture CurrentWaterTile,WaterTexture
	
End Function

Function BuildCurrentObjectModel()

	BuildObjectModel(CurrentObject,0,0,300+CurrentObject\Position\Z)
	
	FinalizeCurrentObject()

End Function

Function BuildObjectAccessories(Obj.GameObject)

	If Obj\Model\HatEntity>0
		
		If Obj\Model\HatTexture=0
			UseErrorColor(Obj\Model\HatEntity)
		Else
			EntityTexture Obj\Model\HatEntity,Obj\Model\HatTexture
		EndIf
		ScaleEntity Obj\Model\HatEntity,Obj\Attributes\YScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\ZScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\XScale*Obj\Attributes\ScaleAdjust
		
		;RotateEntity Obj\Model\Entity,Obj\Attributes\PitchAdjust,Obj\Attributes\YawAdjust,Obj\Attributes\RollAdjust
;		RotateEntity Obj\Model\HatEntity,0,0,0
;		TurnEntity Obj\Model\HatEntity,Obj\Attributes\PitchAdjust,0,Obj\Attributes\RollAdjust
;		TuObj\Attributes\YawAdjustel,0,Obj\Attributes\YawAdjust-90,0
		
		;bone=FindChild(Obj\Model\Entity,"hat_bone")
	
;		PositionEntity Obj\Model\HatEntity,0+Obj\Attributes\XAdjust,300+Obj\AttribuObj\Attributes\YawAdjustectZ+.1+.84*Obj\Attributes\ZScale/.035,0-Obj\Attributes\YAdjust

		TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)

	EndIf
	
	If Obj\Model\AccEntity>0
		
		If Obj\Model\AccTexture=0
			UseErrorColor(Obj\Model\AccEntity)
		Else
			EntityTexture Obj\Model\AccEntity,Obj\Model\AccTexture
		EndIf
		ScaleEntity Obj\Model\AccEntity,Obj\Attributes\YScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\ZScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\XScale*Obj\Attributes\ScaleAdjust
		
		;RotateEntity Obj\Model\Entity,Obj\Attributes\PitchAdjust,Obj\Attributes\YawAdjust,Obj\Attributes\RollAdjust
;		RotateEntity Obj\Model\AccEntity,0,0,0
;		TurnEntity Obj\Model\AccEntity,Obj\Attributes\PitchAdjust,0,Obj\Attributes\RollAdjust
;		TurnEntity Obj\Model\AccEntity,0,Obj\Attributes\YawAdjust-90,0
		
		;bone=FindChild(Obj\Model\Entity,"hat_bone")
	
		;PositionEntity Obj\Attributes\YawAdjustentAccModel,0+Obj\Attributes\XAdjust,300+Obj\Attributes\ZAdjust+Obj\Position\Z+.1+.84*Obj\Attributes\ZScale/.035,0-Obj\Attributes\YAdjust

		TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)

	EndIf

End Function

Function BuildObjectModel(Obj.GameObject,x#,y#,z#)

	FreeObjectModel(Obj\Model)
	
	

	If Obj\Attributes\ModelName$="!Button"
		If Obj\Attributes\LogicSubType=16 And Obj\Attributes\Data2=1 Then Obj\Attributes\LogicSubType=17
		If Obj\Attributes\LogicSubType=17 And Obj\Attributes\Data2=0 Then Obj\Attributes\LogicSubType=16
		If Obj\Attributes\LogicSubType=16+32 And Obj\Attributes\Data2=1 Then Obj\Attributes\LogicSubType=17+32
		If Obj\Attributes\LogicSubType=17+32 And Obj\Attributes\Data2=0 Then Obj\Attributes\LogicSubType=16+32

		Obj\Model\Entity=CreateButtonMesh(Obj\Attributes\LogicSubType,Obj\Attributes\Data0,Obj\Attributes\Data1,Obj\Attributes\Data2,Obj\Attributes\Data3)
		
		
	Else If Obj\Attributes\ModelName$="!CustomModel"
		
		If FileType("UserData\Custom\Models\"+Obj\Attributes\TextData0+".3ds")<>1 Or FileType("UserData\Custom\Models\"+Obj\Attributes\TextData0+".jpg")<>1
			Print "Couldn't Load 3ds/jpg."
			Print "Reverting to 'Default' Custom Model."
 			Delay 2000
			
			Obj\Attributes\TextData0="Default"
		EndIf
		Obj\Model\Entity=LoadMesh("UserData\Custom\Models\"+Obj\Attributes\TextData0+".3ds")
		Obj\Model\Texture=LoadTexture("UserData\Custom\Models\"+Obj\Attributes\TextData0+".jpg")
		EntityTexture Obj\Model\Entity,Obj\Model\Texture

		
	
	
	Else If Obj\Attributes\ModelName$="!Teleport"
		Obj\Model\Entity=CreateTeleporterMesh(Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!Item"
		Obj\Model\Entity=CreatePickupItemMesh(Obj\Attributes\Data2)
	Else If Obj\Attributes\ModelName$="!Stinker" Or Obj\Attributes\ModelName$="!NPC"
		Obj\Model\Entity=CopyEntity(StinkerMesh)
		
		If Obj\Attributes\Data0=5
			Obj\Model\Texture=Waterfalltexture(0) ;MyLoadTexture("Data\leveltextures\waterfall.jpg",1)
		Else If Obj\Attributes\Data0=6
			Obj\Model\Texture=Waterfalltexture(1) ;MyLoadTexture("Data\leveltextures\waterfalllava.jpg",1)
		Else If Obj\Attributes\Data0<1 Or Obj\Attributes\Data0>8 Or Obj\Attributes\Data1<0 Or Obj\Attributes\Data1>4
			; prevent out-of-bounds texture usage
			UseErrorColor(GetChild(Obj\Model\Entity,3))
		Else
			Obj\Model\Texture=MyLoadTexture("data/models/stinker/body00"+Str$(Obj\Attributes\Data0)+Chr$(65+Obj\Attributes\Data1)+".jpg",1)
		EndIf
		
		If Obj\Model\Texture>0
			EntityTexture GetChild(Obj\Model\Entity,3),Obj\Model\Texture
		EndIf
		
		
		If Obj\Attributes\Data2>0	; hat
			Obj\Model\HatEntity=CreateAccEntity(Obj\Attributes\Data2)
			Obj\Model\HatTexture=CreateHatTexture(Obj\Attributes\Data2,Obj\Attributes\Data3)
			
			;TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)
		EndIf
		
		If Obj\Attributes\Data4>0 ;100 ; acc
			Obj\Model\AccEntity=CreateAccEntity(Obj\Attributes\Data4)
			Obj\Model\AccTexture=CreateGlassesTexture(Obj\Attributes\Data4,Obj\Attributes\Data5)
			
			;TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)
		EndIf

		


	
	
	Else If Obj\Attributes\ModelName$="!ColourGate"
		Obj\Model\Entity=CreateColourGateMesh(Obj\Attributes\Data2,Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!Transporter"
		Obj\Model\Entity=CreateTransporterMesh(Obj\Attributes\Data0,3)
		RotateMesh Obj\Model\Entity,0,90*Obj\Attributes\Data2,0
		
	Else If Obj\Attributes\ModelName$="!Conveyor"
		If Obj\Attributes\Data4=4
			Obj\Model\Entity=CreateCloudMesh(Obj\Attributes\Data0)
		Else
			Obj\Model\Entity=CreateTransporterMesh(Obj\Attributes\Data0,Obj\Attributes\Data4)
		EndIf
		RotateMesh Obj\Model\Entity,0,-90*Obj\Attributes\Data2,0
		If Obj\Attributes\LogicType=46 ScaleMesh Obj\Model\Entity,.5,.5,.5

	Else If Obj\Attributes\ModelName$="!Autodoor"
		Obj\Model\Entity=CopyEntity(AutodoorMesh)
		
		
		
	Else If Obj\Attributes\ModelName$="!Key"
		Obj\Model\Entity=CreateKeyMesh(Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!KeyCard" 
		Obj\Model\Entity=CreateKeyCardMesh(Obj\Attributes\Data0)

		
	Else If Obj\Attributes\ModelName$="!StinkerWee"
		Obj\Model\Entity=CopyEntity(StinkerWeeMesh)
		EntityTexture Obj\Model\Entity,StinkerWeeTexture(Obj\Attributes\Data8+1)
	Else If Obj\Attributes\ModelName$="!Cage"
		Obj\Model\Entity=CopyEntity(CageMesh)
		Else If Obj\Attributes\ModelName$="!StarGate"
		Obj\Model\Entity=CopyEntity(StarGateMesh)
	Else If Obj\Attributes\ModelName$="!Scritter"
		Obj\Model\Entity=CopyEntity(ScritterMesh)
		EntityTexture Obj\Model\Entity,ScritterTexture(Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!RainbowBubble"
		Obj\Model\Entity=CreateSphere()
		;ScaleMesh Obj\Model\Entity,.4,.4,.4
		;PositionMesh Obj\Model\Entity,0,1,0
		ScaleMesh Obj\Model\Entity,.5,.5,.5
		EntityTexture Obj\Model\Entity,Rainbowtexture2
		
	Else If Obj\Attributes\ModelName$="!IceBlock"
		Obj\Model\Entity=CreateIceBlockMesh(Obj\Attributes\Data3)
		
	Else If Obj\Attributes\ModelName$="!PlantFloat"
		Obj\Model\Entity=CreatePlantFloatMesh()
		;Obj\Model\Entity=CreateSphere()
		;ScaleMesh Obj\Model\Entity,.4,.1,.4
;		PositionMesh Obj\Model\Entity,0,1,0
		;EntityTexture Obj\Model\Entity,Rainbowtexture
		
	Else If Obj\Attributes\ModelName$="!IceFloat"
		Obj\Model\Entity=CreateIceFloatMesh()
		;Obj\Model\Entity=CreateSphere()
		;ScaleMesh Obj\Model\Entity,.4,.1,.4
;		PositionMesh Obj\Model\Entity,0,1,0




	Else If Obj\Attributes\ModelName$="!Chomper"
		Obj\Model\Entity=CopyEntity(ChomperMesh)
		If Obj\Attributes\LogicSubType=1 
			EntityTexture Obj\Model\Entity,WaterChomperTexture
		Else If Obj\Attributes\Data1=3 
			EntityTexture Obj\Model\Entity,MechaChomperTexture
		Else
			EntityTexture Obj\Model\Entity,ChomperTexture
		EndIf
	Else If Obj\Attributes\ModelName$="!Bowler"
		Obj\Model\Entity=CopyEntity(BowlerMesh)
		Direction=Obj\Attributes\Data0
		If Obj\Attributes\Data1<>2
			Direction=Direction*2
		EndIf
		Obj\Attributes\YawAdjust=(-45*Direction +3600) Mod 360
	Else If Obj\Attributes\ModelName$="!Turtle"
		Obj\Model\Entity=CopyEntity(TurtleMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360
	Else If Obj\Attributes\ModelName$="!Thwart"
		Obj\Model\Entity=CopyEntity(ThwartMesh)
		EntityTexture Obj\Model\Entity,ThwartTexture(Obj\Attributes\Data2)
	Else If Obj\Attributes\ModelName$="!Tentacle"
		Obj\Model\Entity=CopyEntity(TentacleMesh)
		Animate GetChild(Obj\Model\Entity,3),1,.1,1,0
	Else If Obj\Attributes\ModelName$="!Lurker"
		Obj\Model\Entity=CopyEntity(LurkerMesh)
	Else If Obj\Attributes\ModelName$="!Ghost"
		Obj\Model\Entity=CopyEntity(GhostMesh)
	Else If Obj\Attributes\ModelName$="!Wraith"
		Obj\Model\Entity=CopyEntity(WraithMesh)
		EntityTexture Obj\Model\Entity,WraithTexture(Obj\Attributes\Data2)

	

	Else If Obj\Attributes\ModelName$="!Crab"
		Obj\Model\Entity=CopyEntity(CrabMesh)
		If Obj\Attributes\LogicSubType=0 Then EntityTexture Obj\Model\Entity,CrabTexture2
	Else If Obj\Attributes\ModelName$="!Troll"
		Obj\Model\Entity=CopyEntity(TrollMesh)
	Else If Obj\Attributes\ModelName$="!Kaboom"
		Obj\Model\Entity=CopyEntity(KaboomMesh)
		EntityTexture Obj\Model\Entity,KaboomTexture(Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!BabyBoomer"
		Obj\Model\Entity=CopyEntity(KaboomMesh)
		EntityTexture Obj\Model\Entity,KaboomTexture(1)



	Else If Obj\Attributes\ModelName$="!FireFlower"
		Obj\Model\Entity=CopyEntity(FireFlowerMesh)
		If Obj\Attributes\LogicSubType<>1
			Obj\Attributes\YawAdjust=(-45*Obj\Attributes\Data0 +3600) Mod 360
		Else
			Obj\Attributes\YawAdjust=0
		EndIf
		If Obj\Attributes\Data1=1
			EntityTexture Obj\Model\Entity,FireFlowerTexture2
		EndIf
		
	Else If Obj\Attributes\ModelName$="!BurstFlower"
		Obj\Model\Entity=CopyEntity(BurstFlowerMesh)

	Else If Obj\Attributes\ModelName$="!Busterfly"
		Obj\Model\Entity=CopyEntity(BusterflyMesh)
		;AnimateMD2 Obj\Model\Entity,2,.4,2,9
		
		
	Else If Obj\Attributes\ModelName$="!GlowWorm"  Or Obj\Attributes\ModelName$="!Zipper"
		Obj\Model\Entity=CreateSphere(12)
		ScaleMesh Obj\Model\Entity,.1,.1,.1
		EntityColor Obj\Model\Entity,Obj\Attributes\Data5,Obj\Attributes\Data6,Obj\Attributes\Data7
	Else If Obj\Attributes\ModelName$="!Void"
		;Obj\Model\Entity=CreateSphere(12)
		;ScaleMesh Obj\Model\Entity,.4,.15,.4
		Obj\Model\Entity=CreateVoidMesh()
	Else If Obj\Attributes\ModelName$="!Rubberducky"
		Obj\Model\Entity=CopyEntity(RubberduckyMesh)

	Else If Obj\Attributes\ModelName$="!Barrel1"
		Obj\Model\Entity=CopyEntity(BarrelMesh)
		EntityTexture Obj\Model\Entity,BarrelTexture1
	Else If Obj\Attributes\ModelName$="!Barrel2"
		Obj\Model\Entity=CopyEntity(BarrelMesh)
		EntityTexture Obj\Model\Entity,BarrelTexture2
	Else If Obj\Attributes\ModelName$="!Barrel3"
		Obj\Model\Entity=CopyEntity(BarrelMesh)
		EntityTexture Obj\Model\Entity,BarrelTexture3
	Else If Obj\Attributes\ModelName$="!Cuboid"
		Obj\Model\Entity=CreateCube()
		ScaleMesh Obj\Model\Entity,0.4,0.4,0.4
		PositionMesh Obj\Model\Entity,0,0.5,0
		If Obj\Attributes\Data0<0 Or Obj\Attributes\Data0>8 Then Obj\Attributes\Data0=0
		EntityTexture Obj\Model\Entity,TeleporterTexture(Obj\Attributes\Data0)
		
	Else If Obj\Attributes\ModelName$="!Prism"
		Obj\Model\Entity=CopyEntity(PrismMesh)
		EntityTexture Obj\Model\Entity,PrismTexture
			
	Else If  Obj\Attributes\ModelName$="!Obstacle10" 
		Obj\Model\Entity=CopyEntity(  ObstacleMesh(10 ))
		EntityTexture Obj\Model\Entity, MushroomTex(  (Abs(Obj\Attributes\Data0)) Mod 3)

	

		
	Else If  Obj\Attributes\ModelName$="!Obstacle51" Or Obj\Attributes\ModelName$="!Obstacle55" Or Obj\Attributes\ModelName$="!Obstacle59"
		ObstacleId=ObstacleNameToObstacleId(Obj\Attributes\ModelName$)
		Obj\Model\Entity=TryGetObstacleMesh(ObstacleId+Obj\Attributes\Data0)
		Obj\Model\Entity=TryUseObstacleTexture(Obj\Model\Entity,ObstacleId+Obj\Attributes\Data1)

	Else If Left$(Obj\Attributes\ModelName$,9)="!Obstacle"
		ObstacleId=ObstacleNameToObstacleId(Obj\Attributes\ModelName$)
		Obj\Model\Entity=TryGetObstacleMesh(ObstacleId)

	Else If Obj\Attributes\ModelName$="!WaterFall"
		Obj\Model\Entity=CreateWaterFallMesh(Obj\Attributes\Data0)
	Else If Obj\Attributes\ModelName$="!Star"
		Obj\Model\Entity=CopyEntity(StarMesh)
		EntityTexture Obj\Model\Entity,GoldStarTexture
	Else If Obj\Attributes\ModelName$="!Wisp"
		Obj\Model\Entity=CopyEntity(StarMesh)
		If CurrentObject\Attributes\Data0>=0 And CurrentObject\Attributes\Data0<=9
			EntityTexture Obj\Model\Entity,WispTexture(Obj\Attributes\Data0)
		Else
			Obj\Model\Entity=RemoveEntityTexture(Obj\Model\Entity)
			UseErrorColor(Obj\Model\Entity)
		EndIf
	
	
	Else If Obj\Attributes\ModelName$="!Portal Warp"
		Obj\Model\Entity=CopyEntity(PortalWarpMesh)
		If Obj\Attributes\Data1=0
			EntityTexture Obj\Model\Entity,StarTexture
		Else
			EntityTexture Obj\Model\Entity,RainbowTexture
		EndIf
		
	Else If Obj\Attributes\ModelName$="!Sun Sphere1"
		Obj\Model\Entity=CreateSphere()
		EntityColor Obj\Model\Entity,Obj\Attributes\Data0,Obj\Attributes\Data1,Obj\Attributes\Data2
		EntityBlend Obj\Model\Entity,3
		
	Else If Obj\Attributes\ModelName$="!Sun Sphere2"
		Obj\Model\Entity=CreateSphere()
		ScaleMesh Obj\Model\Entity,.5,.5,.5



	Else If Obj\Attributes\ModelName$="!Coin"
		Obj\Model\Entity=CopyEntity(CoinMesh)
		EntityTexture Obj\Model\Entity,GoldCoinTexture
		If Obj\Attributes\LogicType=425 EntityTexture Obj\Model\Entity,Retrorainbowcointexture
	Else If Obj\Attributes\ModelName$="!Token"
		Obj\Model\Entity=CopyEntity(CoinMesh)
		EntityTexture Obj\Model\Entity,TokenCoinTexture
	Else If Obj\Attributes\ModelName$="!Gem"
		;If Obj\Attributes\Data0<0 Or Obj\Attributes\Data0>2 Then Obj\Attributes\Data0=0
		;If Obj\Attributes\Data1<0 Or Obj\Attributes\Data1>7 Then Obj\Attributes\Data1=0
		
		; Note that the vanilla WA3E player will kill you without hesitation if you have a Data0 (gem mesh) outside this range.
		Data0=Obj\Attributes\Data0
		If Data0<0 Or Data0>2 Then Data0=0
		
		Obj\Model\Entity=CopyEntity(GemMesh(Data0))
		
		Data1=Obj\Attributes\Data1
		If Data1<0 Or Data1>8
			UseErrorColor(Obj\Model\Entity)
		Else
			EntityTexture Obj\Model\Entity,TeleporterTexture(Data1)
		EndIf
	Else If Obj\Attributes\ModelName$="!Crystal"
		Obj\Model\Entity=CopyEntity(GemMesh(2))
		If Obj\Attributes\Data0=0
			EntityTexture Obj\Model\Entity,rainbowtexture
		Else
			EntityTexture Obj\Model\Entity,ghosttexture
		EndIf
			


	Else If Obj\Attributes\ModelName$="!Sign"
		Obj\Model\Entity=CreateSignMesh(Obj\Attributes\Data0,Obj\Attributes\Data1)


	Else If Obj\Attributes\ModelName$="!CustomItem"
		Obj\Model\Entity=CreateCustomItemMesh(Obj\Attributes\Data0)

		
	Else If Obj\Attributes\ModelName$="!SteppingStone"
		Obj\Model\Entity=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
		If Obj\Attributes\Data0<0 Or Obj\Attributes\Data0>3
			UseErrorColor(Obj\Model\Entity)
		Else
			EntityTexture Obj\Model\Entity,SteppingStoneTexture(Obj\Attributes\Data0)
		EndIf
	Else If Obj\Attributes\ModelName$="!Spring" 
		Obj\Model\Entity=MyLoadMesh("data\models\bridges\cylinder1.b3d",0)
		RotateMesh Obj\Model\Entity,90,0,0
		Obj\Attributes\YawAdjust=(-45*Obj\Attributes\Data2 +3600) Mod 360


		EntityTexture Obj\Model\Entity,Springtexture
	Else If Obj\Attributes\ModelName$="!Suctube" 
		Obj\Model\Entity=CreateSuctubemesh(Obj\Attributes\Data3,Obj\Attributes\Data0,True)
		
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data2 +3600) Mod 360
		
		Redosuctubemesh(Obj\Model\Entity, Obj\Attributes\Data0, Obj\Attributes\Active, Obj\Attributes\Data2, Obj\Attributes\YawAdjust)

	Else If Obj\Attributes\ModelName$="!SuctubeX" 
		Obj\Model\Entity=CreateSuctubeXmesh(Obj\Attributes\Data3)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data2 +3600) Mod 360


		

		
	Else If Obj\Attributes\ModelName$="!FlipBridge"
		;Obj\Model\Entity=CreateCube()
		;ScaleMesh Obj\Model\Entity,.35,.1,.5
		
		Obj\Model\Entity=CreateFlipBridgeMesh(Obj\Attributes\Data0)
		;EntityTexture Obj\Model\Entity,GateTexture
		
		Obj\Attributes\YawAdjust=(-45*Obj\Attributes\Data2 +3600) Mod 360
	
	Else If Obj\Attributes\ModelName$="!Door"
		Obj\Model\Entity=MyLoadmesh("data\models\houses\door01.3ds",0)
		
	Else If Obj\Attributes\ModelName$="!Cylinder"
		Obj\Model\Entity=CopyEntity(cylinder)
		
	Else If Obj\Attributes\ModelName$="!Square"
		Obj\Model\Entity=MyLoadmesh("Data\models\squares\square1.b3d",0)
		
	Else If Obj\Attributes\ModelName$="!SpellBall"
		Obj\Model\Entity=CreateSpellBallMesh(7) ; use white magic spellball mesh
		
	Else If Obj\Attributes\ModelName$="!FencTrue
		Obj\Model\Entity=CopyEntity(fence1)
	Else If Obj\Attributes\ModelName$="!Fence2"
		Obj\Model\Entity=CopyEntity(fence2)
	Else If Obj\Attributes\ModelName$="!Fencepost"
		Obj\Model\Entity=CopyEntity(fencepost)
	Else If Obj\Attributes\ModelName$="!Fountain"
		Obj\Model\Entity=MyLoadmesh("data\models\houses\fountain01.b3d",0)
		EntityTexture Obj\Model\Entity,FountainTexture
		
	Else If Obj\Attributes\ModelName$="!Retrobox"
		Obj\Model\Entity=CopyEntity(RetroBoxMesh)
		
	Else If Obj\Attributes\ModelName$="!Retrocoily"
		Obj\Model\Entity=CopyEntity(RetroCoilyMesh)
		
	Else If Obj\Attributes\ModelName$="!Retroscouge"
		Obj\Model\Entity=CopyEntity(RetroScougeMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360
	
	Else If Obj\Attributes\ModelName$="!Retrozbot"
		Obj\Model\Entity=CopyEntity(RetroZbotMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360
		
	Else If Obj\Attributes\ModelName$="!Retroufo"
		Obj\Model\Entity=CopyEntity(RetroUFOMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360
	
	Else If Obj\Attributes\ModelName$="!Retrolasergate"
		Obj\Model\Entity=CreateretrolasergateMesh(Obj\Attributes\Data0)
		
	Else If Obj\Attributes\ModelName$="!Weebot"
		Obj\Model\Entity=CopyEntity(WeebotMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360
		
	Else If Obj\Attributes\ModelName$="!Zapbot"
		Obj\Model\Entity=CopyEntity(ZapbotMesh)
		Obj\Attributes\YawAdjust=(-90*Obj\Attributes\Data0 +3600) Mod 360

	Else If Obj\Attributes\ModelName$="!Pushbot"
		Obj\Model\Entity=CreatePushbotMesh(Obj\Attributes\Data0,Obj\Attributes\Data3)
		
		If Obj\Attributes\LogicType=432 ; Moobot
			Obj\Attributes\YawAdjust=-Obj\Attributes\Data2*90
		Else
			Obj\Attributes\YawAdjust=0 ; Unfortunately hardcoded in adventures.bb.
		EndIf
		
	Else If Obj\Attributes\ModelName$="!ZbotNPC"
		Obj\Model\Entity=CopyEntity(ZbotNPCMesh)
		If Obj\Attributes\Data2<0 Or Obj\Attributes\Data2>7
			UseErrorColor(Obj\Model\Entity)
		Else
			EntityTexture Obj\Model\Entity,ZBotNPCTexture(Obj\Attributes\Data2)
		EndIf
	
	Else If Obj\Attributes\ModelName$="!Mothership"
		Obj\Model\Entity=CopyEntity(MothershipMesh)


		
	Else If Obj\Attributes\ModelName="!FloingOrb" ; not toObj\Attributes\YawAdjustingBubble
		Obj\Model\Entity=CreateSphere()
		ScaleMesh Obj\Model\Entity,.3,.3,.3
		EntityColor Obj\Model\Entity,255,0,0
	
	Else If Obj\Attributes\ModelName="!MagicMirror"
		Obj\Model\Entity=CreateMagicMirrorMesh()

	
	
	Else If Obj\Attributes\ModelName$="!SkyMachineMap"
		Obj\Model\Entity=CreateCube()
		ScaleMesh Obj\Model\Entity,2.5,.01,2.5
		PositionMesh Obj\Model\Entity,0,0,-1
		EntityTexture Obj\Model\Entity,SkyMachineMapTexture
		EntityBlend Obj\Model\Entity,3
		
	
	Else If Obj\Attributes\ModelName$="!GrowFlower"
		Obj\Model\Entity=CreateGrowFlowerMesh(Obj\Attributes\Data0)

	Else If Obj\Attributes\ModelName$="!FloingBubble"
		Obj\Model\Entity=CreateFloingBubbleMesh()

		
	Else If Obj\Attributes\ModelName$="!None"
		Obj\Model\Entity=CreateNoneMesh()
		
		If Obj\Attributes\LogicType=50 ; spellball
			UseMagicColor(Obj\Model\Entity,Obj\Attributes\LogicSubType)
		EndIf
		
	Else ;unknown model
		Obj\Model\Entity=CreateErrorMesh()
	

	EndIf

	If Obj\Attributes\ModelName$="!FlipBridge"
		TextureTarget=GetChild(Obj\Model\Entity,1)
	Else
		TextureTarget=Obj\Model\Entity
	EndIf

	If Obj\Attributes\TexName$="!None" 
		Obj\Model\Texture=0
	Else If Obj\Attributes\TexName$="!Door"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If Obj\Attributes\Data5>2 Then Obj\Attributes\Data5=2
		If DoorTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,DoorTexture(Obj\Attributes\Data5)
	Else If Obj\Attributes\TexName$="!Cottage"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If CottageTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,CottageTexture(Obj\Attributes\Data5)	
	Else If Obj\Attributes\TexName$="!Townhouse"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If HouseTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,HouseTexture(Obj\Attributes\Data5)	
	Else If Obj\Attributes\TexName$="!Windmill"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If WindmillTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,WindmillTexture(Obj\Attributes\Data5)	
	Else If Obj\Attributes\TexName$="!Fence"
		If Obj\Attributes\Data5<0 Then Obj\Attributes\Data5=0
		If FenceTexture(Obj\Attributes\Data5)=0 Then Obj\Attributes\Data5=0
		EntityTexture TextureTarget,FenceTexture(Obj\Attributes\Data5)	
	Else If Obj\Attributes\TexName$="!FireTrap"
		EntityTexture TextureTarget,FireTrapTexture

	Else If Left$(Obj\Attributes\TexName$,2)="!T"
		
		
		EntityTexture TextureTarget,StinkerTexture

		For i=1 To CountChildren(TextureTarget)
			child=GetChild(TextureTarget,i)
			EntityTexture child,StinkerTexture
		Next
	Else If Obj\Attributes\TexName$="!GloveTex"
		EntityTexture TextureTarget,GloveTex

	Else If Left(Obj\Attributes\TexName$,1)="?"
		; custom texture For existing objects
		If Lower(Right(Obj\Attributes\TexName$,4))=".jpg" Or Lower(Right(Obj\Attributes\TexName$,4))=".bmp" Or Lower(Right(Obj\Attributes\TexName$,4))=".png"
			tname$="UserData\Custom\Objecttextures\"+Right(Obj\Attributes\TexName$,Len(Obj\Attributes\TexName$)-1)
		Else
			tname$="UserData\Custom\Objecttextures\"+Right(Obj\Attributes\TexName$,Len(Obj\Attributes\TexName$)-1)+".jpg"
		EndIf
		If FileType(tname$)<>1 
			tname$="UserData\Custom\Objecttextures\default.jpg"
			Obj\Attributes\TexName$="?Default"
		EndIf
		
		If Lower(Right(tname$,4))=".png"
			; if png load texture with alpha map
			Obj\Model\Texture=LoadTexture(tname$,3)
		Else
			Obj\Model\Texture=LoadTexture(tname$,4)
		EndIf
		EntityTexture TextureTarget,Obj\Model\Texture
		
	Else If Obj\Attributes\TexName$<>"" And Obj\Attributes\TexName$<>"!None" And Left$(Obj\Attributes\TexName$,1)<>"!"  And Obj\Attributes\ModelName$<>"!Button"
		If myFileType(Obj\Attributes\TexName$)=1 Or FileType(Obj\Attributes\TexName$)=1
			Obj\Model\Texture=myLoadTexture(Obj\Attributes\TexName$,4)
			EntityTexture TextureTarget,Obj\Model\Texture
		Else
			Locate 0,0
			Color 255,255,255
			Print "WARNING!"
			Print "Couldn't load texture: " + Obj\Attributes\TexName$
			Print "The adventure may be unplayable in game"
			Delay 2000
		EndIf
		
				
	EndIf
	
	;If ObjectAdjusterScaleAdjust\Absolute
	;	If Obj\Attributes\ScaleAdjust=0.0 Then Obj\Attributes\ScaleAdjust=1.0
	;EndIf
	
	If Obj\Attributes\ModelName$<>"!None"
		ScaleEntity Obj\Model\Entity,Obj\Attributes\XScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\ZScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\YScale*Obj\Attributes\ScaleAdjust
		;RotateEntity Obj\Model\Entity,Obj\Attributes\PitchAdjust,Obj\Attributes\YawAdjust,Obj\Attributes\RollAdjust
		RotateEntity Obj\Model\Entity,0,0,0
		TurnEntity Obj\Model\Entity,Obj\Attributes\PitchAdjust,0,Obj\Attributes\RollAdjust
		TurnEntity Obj\Model\Entity,0,Obj\Attributes\YawAdjust,0
		
		If Obj\Attributes\ModelName$="!Kaboom" Or Obj\Attributes\ModelName$="!BabyBoomer" Then TurnEntity Obj\Model\Entity,0,90,0


	;	PositionEntity Obj\Model\Entity,Obj\Attributes\XAdjust,Obj\Attributes\ZAdjust+Obj\Position\Z,-Obj\Attributes\YAdjust
		
	EndIf
	
	If Obj\Attributes\LogicType=200 Or Obj\Attributes\LogicType=201 ; glovecharge or glovedischarge
		EntityFX Obj\Model\Entity,2
		For ii=0 To 3
			Col=Obj\Attributes\Data0
			red=GetMagicColor(Col,0)
			green=GetMagicColor(Col,1)
			blue=GetMagicColor(Col,2)
			VertexColor GetSurface(Obj\Model\Entity,1),ii,red,green,blue
		Next
	EndIf
	
	PositionEntityWithXYZAdjust(Obj\Model\Entity,x#,y#,z#,Obj\Attributes)
	
	BuildObjectAccessories(Obj)

End Function

Function PositionEntityWithXYZAdjust(Entity,x#,y#,z#,Attributes.GameObjectAttributes)

	PositionEntity Entity,x#+Attributes\XAdjust,z#+Attributes\ZAdjust,-y#-Attributes\YAdjust
	
End Function

Function FinalizeCurrentObject()

	ShowCurrentObjectMoveXYGoal()
	ShowWorldAdjusterPositions()
	CalculateCurrentObjectTargetIDs()
	CalculateCurrentObjectActivateIDs()

End Function


Function ColorToID(Col,SubCol)

	Return 500+5*Col+SubCol

End Function


Function CalculateCurrentObjectTargetIDs()

	;CurrentObjectTargetIDCount=0
	For i=0 To CurrentObjectTargetIDCount-1
		CurrentObjectTargetIDEnabled(i)=False
	Next
	
	If ObjectAdjusterLogicType\Absolute
		If CurrentObject\Attributes\LogicType=90 ; Buttons
			If ObjectAdjusterLogicSubType\Absolute
				If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
					;CurrentObjectTargetIDCount=4
					CurrentObjectTargetID(0)=ColorToID(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data4)
					CurrentObjectTargetIDEnabled(0)=ObjectAdjusterData0\Absolute And ObjectAdjusterData4\Absolute
					CurrentObjectTargetID(1)=ColorToID(CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data5)
					CurrentObjectTargetIDEnabled(1)=ObjectAdjusterData1\Absolute And ObjectAdjusterData5\Absolute
					CurrentObjectTargetID(2)=ColorToID(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data6)
					CurrentObjectTargetIDEnabled(2)=ObjectAdjusterData2\Absolute And ObjectAdjusterData6\Absolute
					CurrentObjectTargetID(3)=ColorToID(CurrentObject\Attributes\Data3,CurrentObject\Attributes\Data7)
					CurrentObjectTargetIDEnabled(3)=ObjectAdjusterData3\Absolute And ObjectAdjusterData7\Absolute
				Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; ColorX2Y
					;CurrentObjectTargetIDCount=1
					CurrentObjectTargetID(0)=ColorToID(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2)
					CurrentObjectTargetIDEnabled(0)=ObjectAdjusterData0\Absolute And ObjectAdjusterData2\Absolute
				Else If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; Rotator or ???
					;CurrentObjectTargetIDCount=1
					CurrentObjectTargetID(0)=ColorToID(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
					CurrentObjectTargetIDEnabled(0)=ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
				Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
					If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
						CalculateCommandTargetIDs(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
					EndIf
				Else If (CurrentObject\Attributes\LogicSubType Mod 32)=11 ; NPC Modifier
					If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
						If CurrentObject\Attributes\Data0=2 ; NPC Exclamation
							If CurrentObject\Attributes\Data1<>-1 ; Ignore if targeting the player
								;CurrentObjectTargetIDCount=1
								CurrentObjectTargetID(0)=CurrentObject\Attributes\Data1
								CurrentObjectTargetIDEnabled(0)=True
							EndIf
						Else
							;CurrentObjectTargetIDCount=1
							CurrentObjectTargetID(0)=CurrentObject\Attributes\Data1
							CurrentObjectTargetIDEnabled(0)=True
						EndIf
					EndIf
				EndIf
			EndIf
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
				CalculateCommandTargetIDs(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=165 ; Arcade Cabinet
			If ObjectAdjusterData0\Absolute
				Data0=CurrentObject\Attributes\Data0
				CurrentObjectTargetID(0)=Data0
				CurrentObjectTargetIDEnabled(0)=True
				TargetCount=1
				While True
					Data0=Data0+1
					If ((Data0-200) Mod 3)=0
						Exit
					Else
						CurrentObjectTargetID(TargetCount)=Data0
						CurrentObjectTargetIDEnabled(TargetCount)=True
						TargetCount=TargetCount+1
					EndIf
				Wend
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=242 ; Cuboid
			If ObjectAdjusterData2\Absolute And ObjectAdjusterData3\Absolute
				CalculateCommandTargetIDs(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
			EndIf
		EndIf
	EndIf
	
End Function

Function CalculateCommandTargetIDs(Command,Data1)

	Select Command
	Case 1,2,3,4,5,51,52,61,62,63
		;CurrentObjectTargetIDCount=1
		CurrentObjectTargetID(0)=Data1
		CurrentObjectTargetIDEnabled(0)=True
	Case 16,64
		If Data1<>-1 ; Ignore if targeting the player
			;CurrentObjectTargetIDCount=1
			CurrentObjectTargetID(0)=Data1
			CurrentObjectTargetIDEnabled(0)=True
		EndIf
	End Select

End Function

Function CalculateCurrentObjectActivateIDs()

	;CurrentObjectActivateIdCount=0
	For i=0 To CurrentObjectActivateIDCount-1
		CurrentObjectActivateIDEnabled(i)=False
	Next
	
	If ObjectAdjusterLogicType\Absolute
		If CurrentObject\Attributes\LogicType=90 Or CurrentObject\Attributes\LogicType=210 ; button or transporter
			;CurrentObjectActivateIdCount=1
			CurrentObjectActivateId(0)=CurrentObject\Attributes\Data8
			CurrentObjectActivateIdEnabled(0)=ObjectAdjusterData8\Absolute
		ElseIf ObjectAdjusterLogicSubType\Absolute
			If IsObjectLogicAutodoor(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
				;CurrentObjectActivateIdCount=3
				CurrentObjectActivateId(0)=CurrentObject\Attributes\Data4
				CurrentObjectActivateIdEnabled(0)=ObjectAdjusterData4\Absolute
				CurrentObjectActivateId(1)=CurrentObject\Attributes\Data5
				CurrentObjectActivateIdEnabled(1)=ObjectAdjusterData5\Absolute
				CurrentObjectActivateId(2)=CurrentObject\Attributes\Data6
				CurrentObjectActivateIdEnabled(2)=ObjectAdjusterData6\Absolute
			EndIf
		EndIf
	EndIf
	
End Function


Function SetWorldAdjusterPosition(index,x,y)

	ShowEntity WorldAdjusterPositionMarker(index)
	SetEntityPositionInWorld(WorldAdjusterPositionMarker(index),x+0.5,y+0.5,0.0)

End Function


Function ShowWorldAdjusterPositions()

	For i=0 To 3
		HideEntity WorldAdjusterPositionMarker(i)
	Next

	Select CurrentObject\Attributes\LogicType
	Case 50 ; spellball
		If CurrentObject\Attributes\Data0<>-1 And CurrentObject\Attributes\Data1<>-1
			SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
		EndIf
	Case 51,52 ; magic shooter, meteor shooter
		SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2)
	Case 80,81,82,83,84,85,86,87 ; Keyblock
		ShowWorldAdjusterPositionsCmd(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3,CurrentObject\Attributes\Data4)
	Case 90 ; button
		If CurrentObject\Attributes\LogicSubType=10 ; levelexit
			If CurrentObject\Attributes\Data1=CurrentLevelNumber
				SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
			EndIf
		ElseIf CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=0 ; NPC move
			SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		ElseIf CurrentObject\Attributes\LogicSubType=15 ; general command
			ShowWorldAdjusterPositionsCmd(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3,CurrentObject\Attributes\Data4)
		EndIf
	Case 242 ; cuboid
		ShowWorldAdjusterPositionsCmd(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3,CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5,CurrentObject\Attributes\Data6)
	Case 434 ; mothership
		SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		SetWorldAdjusterPosition(1,CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5)
		SetWorldAdjusterPosition(2,CurrentObject\Attributes\Data6,CurrentObject\Attributes\Data7)
		SetWorldAdjusterPosition(3,CurrentObject\Attributes\Data8,CurrentObject\Attributes\Data9)
	End Select

End Function

Function ShowWorldAdjusterPositionsCmd(Cmd,Data1,Data2,Data3,Data4)

	Select Cmd
	Case 7,120
		If Data1=CurrentLevelNumber
			SetWorldAdjusterPosition(0,Data2,Data3)
		EndIf
	Case 11
		SetWorldAdjusterPosition(0,Data2,Data3)
	Case 17,18
		SetWorldAdjusterPosition(0,Data1,Data2)
	Case 41,42
		SetWorldAdjusterPosition(0,Data1,Data2)
		SetWorldAdjusterPosition(1,Data3,Data4)
	Case 61
		SetWorldAdjusterPosition(0,Data2,Data3)
	End Select

End Function

Function ShowCurrentObjectMoveXYGoal()

	; Check if we're using a pathfinding MovementType.
	If (ObjectAdjusterMovementType\Absolute And CurrentObject\Attributes\MovementType>9 And CurrentObject\Attributes\MovementType<19) Or (ObjectAdjusterMoveXGoal\Absolute And ObjectAdjusterMoveYGoal\Absolute And (CurrentObject\Attributes\MoveXGoal<>0 Or CurrentObject\Attributes\MoveYGoal<>0))
		ShowEntity CurrentObjectMoveXYGoalMarker
		SetEntityPositionInWorld(CurrentObjectMoveXYGoalMarker,CurrentObject\Attributes\MoveXGoal+0.5,CurrentObject\Attributes\MoveYGoal+0.5,0.0)
	Else
		HideEntity CurrentObjectMoveXYGoalMarker
	EndIf

End Function

Function SetCurrentObjectTargetLocation(x,y)

	Select CurrentObject\Attributes\LogicType
	Case 50 ; spellball
		CurrentObject\Attributes\Data0=x
		CurrentObject\Attributes\Data1=y
		CurrentObjectWasChanged()	
	Case 51,52 ; magic shooter, meteor shooter
		CurrentObject\Attributes\Data1=x
		CurrentObject\Attributes\Data2=y
		CurrentObjectWasChanged()
	Case 80,81,82,83,84,85,86,87 ; Keyblock
		SetCurrentObjectTargetLocationCmd(CurrentObject\Attributes\Data0,1,2,3,4,x,y)
	Case 90 ; button
		If CurrentObject\Attributes\LogicSubType=10 ; levelexit
			CalculateLevelExitTo(1,2,3,4,CurrentLevelNumber,x,y)
			CurrentObjectWasChanged()
		ElseIf CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=0 ; NPC move
			CurrentObject\Attributes\Data2=x
			CurrentObject\Attributes\Data3=y
			CurrentObjectWasChanged()
		ElseIf CurrentObject\Attributes\LogicSubType=15 ; general command
			SetCurrentObjectTargetLocationCmd(CurrentObject\Attributes\Data0,1,2,3,4,x,y)
		Else
			GenerateLevelExitTo(CurrentLevelNumber,x,y)
		EndIf
	Case 242 ; cuboid
		SetCurrentObjectTargetLocationCmd(CurrentObject\Attributes\Data2,3,4,5,6,x,y)
	Default
		GenerateLevelExitTo(CurrentLevelNumber,x,y)
	End Select
	
	; Necessary for if the changing Data values modify an object's appearance.
	BuildCurrentObjectModel()

End Function

Function SetCurrentObjectTargetLocationCmd(Cmd,D1,D2,D3,D4,x,y)

	Select Cmd
	Case 7,120
		CalculateLevelExitTo(D1,D2,D3,D4,CurrentLevelNumber,x,y)
		CurrentObjectWasChanged()
	Case 17,18
		SetDataByIndex(CurrentObject\Attributes,D1,x)
		SetDataByIndex(CurrentObject\Attributes,D2,y)
		CurrentObjectWasChanged()
	Case 11,61
		SetDataByIndex(CurrentObject\Attributes,D2,x)
		SetDataByIndex(CurrentObject\Attributes,D3,y)
		CurrentObjectWasChanged()
	Default
		GenerateLevelExitTo(CurrentLevelNumber,x,y)
	End Select

End Function

Function CalculateLevelExitTo(D1,D2,D3,D4,level,x,y)

	SetDataByIndex(CurrentObject\Attributes,D1,level)
	SetDataByIndex(CurrentObject\Attributes,D2,x)
	SetDataByIndex(CurrentObject\Attributes,D3,y)
	
	StartingYaw=0 ; south
	; examine surroundings to infer player facing direction
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\LogicType=90 And LevelObjects(i)\Attributes\LogicSubType=10 ; LevelExit
			TileX=LevelObjects(i)\Position\TileX
			TileY=LevelObjects(i)\Position\TileY
			If x=TileX
				If y+1=TileY
					; player face north
					StartingYaw=180
				EndIf
			ElseIf x+1=TileX
				If y-1=TileY
					; player face southwest
					StartingYaw=45
				ElseIf y=TileY
					; player face west
					StartingYaw=90
				ElseIf y+1=TileY
					; player face northwest
					StartingYaw=135
				EndIf
			ElseIf x-1=TileX
				If y-1=TileY
					; player face southeast
					StartingYaw=315
				ElseIf y=TileY
					; player face east
					StartingYaw=270
				ElseIf y+1=TileY
					; player face northeast
					StartingYaw=225
				EndIf
			EndIf
		EndIf
	Next
	
	SetDataByIndex(CurrentObject\Attributes,D4,StartingYaw)
	
	If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=10 ; LevelExit
		CurrentObject\Attributes\YawAdjust=180-StartingYaw
		If CurrentObject\Attributes\YawAdjust<0
			CurrentObject\Attributes\YawAdjust=CurrentObject\Attributes\YawAdjust+360
		EndIf
	EndIf

End Function

Function GenerateLevelExitTo(level,x,y)

	BlankObjectPreset("!Button",90,10)
	CalculateLevelExitTo(1,2,3,4,level,x,y)
	ClearObjectSelection()

End Function


Function ResetLevel()

	ClearObjectSelection()

	For i=0 To MaxLevelCoordinate
		For j=0 To MaxLevelCoordinate
			LevelTiles(i,j)\Terrain\Texture=0 ; corresponding to squares in LevelTexture
			LevelTiles(i,j)\Terrain\Rotation=0 ; 0-3 , and 4-7 for "flipped"
			LevelTiles(i,j)\Terrain\SideTexture=13 ; texture for extrusion walls
			LevelTiles(i,j)\Terrain\SideRotation=0 ; 0-3 , and 4-7 for "flipped"
			LevelTiles(i,j)\Terrain\Random=0 ; random height pertubation of tile
			LevelTiles(i,j)\Terrain\Height=0 ; height of "center" - e.g. to make ditches and hills
			LevelTiles(i,j)\Terrain\Extrusion=0; extrusion with walls around it 
			LevelTiles(i,j)\Terrain\Rounding=0; 0-no, 1-yes: are floors rounded if on a drop-off corner
			LevelTiles(i,j)\Terrain\EdgeRandom=0; 0-no, 1-yes: are edges rippled
			LevelTiles(i,j)\Terrain\Logic=0
		Next
	Next
	
	For i=0 To MaxLevelCoordinate
		For j=0 To MaxLevelCoordinate
			LevelTiles(i,j)\Water\Height=-0.2
			LevelTiles(i,j)\Water\Texture=0
			LevelTiles(i,j)\Water\Rotation=0
			LevelTiles(i,j)\Water\Turbulence=0.1
		Next
	Next
	
	ResetParticles("data/graphics/particles.bmp")

End Function

Function CopyLevel()

	For i=0 To MaxLevelCoordinate
		For j=0 To MaxLevelCoordinate
			CopyTile(LevelTiles(i,j),CopyLevelTiles(i,j))
		Next
	Next

End Function

Function CopyTile(Source.Tile,Dest.Tile)

	Dest.Tile\Terrain\Texture=Source.Tile\Terrain\Texture
	Dest.Tile\Terrain\Rotation=Source.Tile\Terrain\Rotation
	Dest.Tile\Terrain\SideTexture=Source.Tile\Terrain\SideTexture
	Dest.Tile\Terrain\SideRotation=Source.Tile\Terrain\SideRotation
	Dest.Tile\Terrain\Random=Source.Tile\Terrain\Random
	Dest.Tile\Terrain\Height=Source.Tile\Terrain\Height
	Dest.Tile\Terrain\Extrusion=Source.Tile\Terrain\Extrusion
	Dest.Tile\Terrain\Rounding=Source.Tile\Terrain\Rounding
	Dest.Tile\Terrain\EdgeRandom=Source.Tile\Terrain\EdgeRandom
	Dest.Tile\Terrain\Logic=Source.Tile\Terrain\Logic

	Dest.Tile\Water\Height=Source.Tile\Water\Height
	Dest.Tile\Water\Texture=Source.Tile\Water\Texture
	Dest.Tile\Water\Rotation=Source.Tile\Water\Rotation
	Dest.Tile\Water\Turbulence=Source.Tile\Water\Turbulence

End Function

Function SwapTiles(Tile1.Tile,Tile2.Tile)
	
	CopyTile(Tile1,TempTile)
	CopyTile(Tile2,Tile1)
	CopyTile(TempTile,Tile2)
	
End Function

Function CopyLevelTile(SourceX,SourceY,DestX,DestY)

	CopyTile(CopyLevelTiles(SourceX,SourceY),LevelTiles(DestX,DestY))

End Function

Function PasteLevelFromCopy()

	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyLevelTile(i,j,i,j)
		Next
	Next

End Function


Function ReSizeLevel()

	If LevelWidth+WidthLeftChange>MaxLevelSize
		WidthLeftChange=MaxLevelSize-LevelWidth
	EndIf
	If LevelWidth+WidthRightChange>MaxLevelSize
		WidthRightChange=MaxLevelSize-LevelWidth
	EndIf
	If LevelHeight+HeightTopChange>MaxLevelSize
		HeightTopChange=MaxLevelSize-LevelHeight
	EndIf
	If LevelHeight+HeightBottomChange>MaxLevelSize
		HeightBottomChange=MaxLevelSize-LevelHeight
	EndIf

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			If (WidthLeftChange>=0 Or i>=-WidthLeftChange) And (HeightTopChange>=0 Or j>=-HeightTopChange)
				CopyLevelTile(i,j,i+WidthLeftChange,j+HeightTopChange)
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
					CopyLevelTile(0,j,k,j)
				Next
			Next
		EndIf
		If WidthRightChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthRightChange-1
					CopyLevelTile(LevelWidthOld-1,j,LevelWidthOld+k,j)
				Next
			Next
		EndIf
		If HeightTopChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightTopChange-1
					CopyLevelTile(i,0,i,k)
				Next
			Next
		EndIf
		If HeightBottomChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightBottomChange-1
					CopyLevelTile(i,LevelHeightOld-1,i,LevelHeightOld+k)
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
			LevelObjects(i)\Position\X=LevelObjects(i)\Position\X+WidthLeftChange
			ChangeObjectTileX(i,LevelObjects(i)\Position\TileX+WidthLeftChange) ; Also handles spellballs etc.
			ResizeLevelFixObjectTargets(LevelObjects(i))
			If Floor(LevelObjects(i)\Position\X)<0 Or Floor(LevelObjects(i)\Position\X)>100
				DeleteObject(i)
			Else
				UpdateObjectPosition(i)
			EndIf
		Next
		RecalculateDragSize()
	EndIf
	If HeightTopChange<>0
		For i=0 To NofObjects-1
			LevelObjects(i)\Position\Y=LevelObjects(i)\Position\Y+HeightTopChange
			ChangeObjectTileY(i,LevelObjects(i)\Position\TileY+HeightTopChange)
			ResizeLevelFixObjectTargets(LevelObjects(i))
			If Floor(LevelObjects(i)\Position\Y)<0 Or Floor(LevelObjects(i)\Position\Y)>100
				DeleteObject(i)
			Else
				UpdateObjectPosition(i)
			EndIf
		Next
		RecalculateDragSize()
	EndIf
	
	ResizeLevelFixObjectTargets(CurrentObject)

	
	WidthLeftChange=0
	WidthRightChange=0
	HeightTopChange=0
	HeightBottomChange=0
	
	;SomeTileWasChanged()
	SomeObjectWasChanged()
	
	AddUnsavedChange()

End Function

Function ResizeLevelFixObjectTargets(Obj.GameObject)

	If Obj\Attributes\MoveXGoal<>0
		Obj\Attributes\MoveXGoal=Obj\Attributes\MoveXGoal+WidthLeftChange
	EndIf
	If Obj\Attributes\MoveYGoal<>0
		Obj\Attributes\MoveYGoal=Obj\Attributes\MoveYGoal+HeightTopChange
	EndIf

	Select Obj\Attributes\LogicType
	Case 50 ; spellball
		Obj\Attributes\Data0=Obj\Attributes\Data0+WidthLeftChange
		Obj\Attributes\Data1=Obj\Attributes\Data1+HeightTopChange
	Case 51,52 ; magic shooter, meteor shooter
		Obj\Attributes\Data1=Obj\Attributes\Data1+WidthLeftChange
		Obj\Attributes\Data2=Obj\Attributes\Data2+HeightTopChange
	Case 80,81,82,83,84,85,86,87 ; Keyblock
		ResizeLevelFixObjectTargetsCmd(Obj\Attributes,Obj\Attributes\Data0,1,2,3,4)
	Case 90 ; button
		If Obj\Attributes\LogicSubType=10 ; levelexit
			If Obj\Attributes\Data1=CurrentLevelNumber
				Obj\Attributes\Data2=Obj\Attributes\Data2+WidthLeftChange
				Obj\Attributes\Data3=Obj\Attributes\Data3+HeightTopChange
			EndIf
		ElseIf Obj\Attributes\LogicSubType=11 And Obj\Attributes\Data0=0 ; NPC move
			Obj\Attributes\Data2=Obj\Attributes\Data2+WidthLeftChange
			Obj\Attributes\Data3=Obj\Attributes\Data3+HeightTopChange
		ElseIf CurrentObject\Attributes\LogicSubType=15 ; general command
			ResizeLevelFixObjectTargetsCmd(Obj\Attributes,Obj\Attributes\Data0,1,2,3,4)
		EndIf
	Case 242 ; cuboid
		ResizeLevelFixObjectTargetsCmd(Obj\Attributes,Obj\Attributes\Data2,3,4,5,6)
	Case 434 ; mothership
		Obj\Attributes\Data2=Obj\Attributes\Data2+WidthLeftChange
		Obj\Attributes\Data3=Obj\Attributes\Data3+HeightTopChange
		Obj\Attributes\Data4=Obj\Attributes\Data4+WidthLeftChange
		Obj\Attributes\Data5=Obj\Attributes\Data5+HeightTopChange
		Obj\Attributes\Data6=Obj\Attributes\Data6+WidthLeftChange
		Obj\Attributes\Data7=Obj\Attributes\Data7+HeightTopChange
		Obj\Attributes\Data8=Obj\Attributes\Data8+WidthLeftChange
		Obj\Attributes\Data9=Obj\Attributes\Data9+HeightTopChange
	End Select

End Function

Function ResizeLevelFixObjectTargetsCmd(Attributes.GameObjectAttributes,Cmd,D1,D2,D3,D4)

	Select Cmd
	Case 7
		If GetDataByIndex(Attributes,D1)=CurrentLevelNumber
			SetDataByIndex(Attributes,D2,GetDataByIndex(Attributes,D2)+WidthLeftChange)
			SetDataByIndex(Attributes,D3,GetDataByIndex(Attributes,D3)+HeightTopChange)
		EndIf
	Case 11,61
		SetDataByIndex(Attributes,D2,GetDataByIndex(Attributes,D2)+WidthLeftChange)
		SetDataByIndex(Attributes,D3,GetDataByIndex(Attributes,D3)+HeightTopChange)
	Case 41,42
		SetDataByIndex(Attributes,D1,GetDataByIndex(Attributes,D1)+WidthLeftChange)
		SetDataByIndex(Attributes,D2,GetDataByIndex(Attributes,D2)+HeightTopChange)
		SetDataByIndex(Attributes,D3,GetDataByIndex(Attributes,D3)+WidthLeftChange)
		SetDataByIndex(Attributes,D4,GetDataByIndex(Attributes,D4)+HeightTopChange)
	End Select

End Function


Function RawSetObjectTileX(i,tilex)

	Obj.GameObject=LevelObjects(i)
	Obj\Position\TileX=tilex
	Obj\Position\TileX2=tilex
	
	If Obj\Attributes\LogicType=50 ; spellball
		Obj\Attributes\Data2=Obj\Position\TileX
		Obj\Attributes\Data4=Obj\Position\TileX
		If CurrentObject\Attributes\LogicType=50 And (IsOnlyObjectSelected(i) Or i=NofObjects)
			CurrentObject\Attributes\Data2=Obj\Attributes\Data2
			CurrentObject\Attributes\Data4=Obj\Attributes\Data4
		EndIf
	EndIf

End Function

Function RawSetObjectTileY(i,tiley)

	Obj.GameObject=LevelObjects(i)
	Obj\Position\TileY=tiley
	Obj\Position\TileY2=tiley
	
	If Obj\Attributes\LogicType=50 ; spellball
		Obj\Attributes\Data3=Obj\Position\TileY
		Obj\Attributes\Data5=Obj\Position\TileY
		If CurrentObject\Attributes\LogicType=50 And (IsOnlyObjectSelected(i) Or i=NofObjects)
			CurrentObject\Attributes\Data3=Obj\Attributes\Data3
			CurrentObject\Attributes\Data5=Obj\Attributes\Data5
		EndIf
	EndIf

End Function

Function SetObjectTileX(i,tilex)

	RawSetObjectTileX(i,tilex)
	IncrementLevelTileObjectCountFor(LevelObjects(i)\Position)

End Function

Function SetObjectTileY(i,tiley)

	RawSetObjectTileY(i,tiley)
	IncrementLevelTileObjectCountFor(LevelObjects(i)\Position)

End Function

Function SetObjectTileXY(i,tilex,tiley)

	RawSetObjectTileX(i,tilex)
	RawSetObjectTileY(i,tiley)
	IncrementLevelTileObjectCountFor(LevelObjects(i)\Position)

End Function

Function ChangeObjectTileX(i,tilex)

	DecrementLevelTileObjectCountFor(LevelObjects(i)\Position)
	SetObjectTileX(i,tilex)

End Function

Function ChangeObjectTileY(i,tiley)

	DecrementLevelTileObjectCountFor(LevelObjects(i)\Position)
	SetObjectTileY(i,tiley)

End Function

Function ChangeObjectTileXY(i,tilex,tiley)

	DecrementLevelTileObjectCountFor(LevelObjects(i)\Position)
	SetObjectTileXY(i,tilex,tiley)

End Function

Function FlipLevelX()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyLevelTile(LevelWidth-1-i,j,i,j)
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
		LevelObjects(i)\Position\X=Float(LevelWidth)-LevelObjects(i)\Position\X
		
		ChangeObjectTileX(i,LevelWidth-1-LevelObjects(i)\Position\TileX)
		
		UpdateObjectPosition(i)
	Next
	
	;SomeTileWasChanged()
	SomeObjectWasChanged()
	
End Function

Function FlipLevelY()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyLevelTile(i,LevelHeight-1-j,i,j)
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
		LevelObjects(i)\Position\Y=Float(LevelHeight)-LevelObjects(i)\Position\Y
		
		ChangeObjectTileY(i,LevelHeight-1-LevelObjects(i)\Position\TileY)
		
		UpdateObjectPosition(i)
	Next
	
	;SomeTileWasChanged()
	SomeObjectWasChanged()
	
End Function


Function FlipLevelXY()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyLevelTile(i,j,j,i)
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
		x2#=LevelObjects(i)\Position\X
		LevelObjects(i)\Position\X=LevelObjects(i)\Position\Y
		LevelObjects(i)\Position\Y=x2#
		ChangeObjectTileXY(i,LevelObjects(i)\Position\TileY,LevelObjects(i)\Position\TileX)
		
		
		UpdateObjectPosition(i)
	Next
	
	;SomeTileWasChanged()
	SomeObjectWasChanged()
	
End Function


			

Function CalculateUV(Texture,i2,j2,Rotation,size,Detail)

	; calculuates UV coordinates of a point on "texture" (0-7... ie the field on the 256x256 big texture)
	; at position i2/j2 (with resolution Detail) and given Rotation (0-7)
	
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
	
	Select Rotation
	Case 0
		ChunkTileu#=Float((Texture Mod size))/size+(Float(i2)/Float(Detail))/size+uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(Detail))/size+voverlap
	Case 1
		ChunkTileu#=Float(((Texture Mod size)+0))/size+(Float(j2)/Float(Detail))/size+voverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(i2)/Float(Detail))/size-uoverlap
	Case 2
		ChunkTileu#=Float(((Texture Mod size)+1))/size-(Float(i2)/Float(Detail))/size-uoverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(j2)/Float(Detail))/size-voverlap
	Case 3
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(j2)/Float(Detail))/size-voverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(i2)/Float(Detail))/size+uoverlap
	Case 4
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(i2)/Float(Detail))/size-uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(Detail))/size+voverlap
	Case 7
		ChunkTileu#=Float(((Texture Mod size))+0)/size+(Float(j2)/Float(Detail))/size+voverlap
		ChunkTilev#=Float(((Texture)/size)+0)/size+(Float(i2)/Float(Detail))/size+uoverlap
	Case 6
		ChunkTileu#=Float(((Texture Mod size)+0))/size+(Float(i2)/Float(Detail))/size+uoverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(j2)/Float(Detail))/size-voverlap
	Case 5
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(j2)/Float(Detail))/size-voverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(i2)/Float(Detail))/size-uoverlap

	Default
		ChunkTileu#=Float((Texture Mod size))/size+(Float(i2)/Float(Detail))/size+uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(Detail))/size+voverlap
	End Select

End Function


Function GetLevelEdgeStyleChar$(Value)

	Select Value
	Case 1
		Return "-"
	Case 2
		Return "B"
	Case 3
		Return "X"
	Case 4
		Return "N"
	Default
		Return Value
	End Select

End Function

Function GetLevelEdgeStyleName$(Value)

	Select Value
	Case 1
		Return "Default"
	Case 2
		Return "Border"
	Case 3
		Return "Border X"
	Case 4
		Return "None"
	Default
		Return Value
	End Select

End Function


Function GetBrushModeName$(Value)

	Select Value
	Case BrushModeNormal
		Return "NORMAL"
	Case BrushModeBlock
		Return "BLOCK"
	Case BrushModeBlockPlacing
		Return ">BLOCK<"
	Case BrushModeFill
		Return "FILL"
	Case BrushModeInlineSoft
		Return "INLINE SOFT"
	Case BrushModeInlineHard
		Return "INLINE HARD"
	Case BrushModeOutlineSoft
		Return "OUTLINE SOFT"
	Case BrushModeOutlineHard
		Return "OUTLINE HARD"
	Case BrushModeRow
		Return "ROW"
	Case BrushModeColumn
		Return "COLUMN"
	Case BrushModeTestLevel
		Return "TEST LEVEL"
	Case BrushModeSetMirror
		If DupeMode=DupeModeXPlusY
			Return "SET AXES"
		Else
			Return "SET AXIS"
		EndIf
	Default
		Return "UNKNOWN"
	End Select

End Function


Function GetBrushModeColor$(Value,index)

	If Value=BrushModeFill
		; yellow
		r=255
		g=255
		b=0
	ElseIf Value=BrushModeBlock Or Value=BrushModeBlockPlacing
		; blue
		r=0
		g=255
		b=255
	ElseIf Value=BrushModeInlineSoft
		; orange
		r=255
		g=80
		b=0
	ElseIf Value=BrushModeInlineHard
		; red
		r=255
		g=0
		b=0
	ElseIf Value=BrushModeOutlineHard
		; indigo
		r=0
		g=0
		b=255
	ElseIf Value=BrushModeOutlineSoft
		; lighter indigo
		r=0
		g=80
		b=255
	ElseIf Value=BrushModeRow Or Value=BrushModeColumn
		; green
		r=0
		g=255
		b=0
	ElseIf Value=BrushModeTestLevel
		; rainbow
		r=GetAnimatedRainbowRed()
		g=GetAnimatedRainbowGreen()
		b=GetAnimatedRainbowBlue()
	ElseIf Value=BrushModeSetMirror
		; gray
		r=140
		g=140
		b=140
	Else ; normal brush mode, AKA BrushModeNormal
		; white
		r=255
		g=255
		b=255
	EndIf
	
	If index=0
		Return r
	ElseIf index=1
		Return g
	Else
		Return b
	EndIf

End Function


Function GetAnimatedRainbowRed()
	Return 128+120*Sin(Leveltimer Mod 360)
End Function
Function GetAnimatedRainbowGreen()
	Return 128+120*Cos(Leveltimer Mod 360)
End Function
Function GetAnimatedRainbowBlue()
	Return 128-120*Sin(Leveltimer Mod 360)
End Function


Function GetAnimatedFlashing(Timer)
	Return 150+105*Sin(Timer*8)
End Function


Function ChangeBrushModeByDelta(Delta)

	SetBrushMode(BrushMode+Delta)
	While BrushMode=BrushModeBlockPlacing
		SetBrushMode(BrushMode+Delta)
	Wend
	
	If BrushMode<0
		SetBrushMode(MaxBrushMode)
	ElseIf BrushMode>MaxBrushMode
		SetBrushMode(0)
	EndIf

End Function


Function GetDupeModeName$(Value)

	Select Value
	Case DupeModeNormal
		Return "NO DUPE"
	Case DupeModeX
		Return "DUPE X"
	Case DupeModeY
		Return "DUPE Y"
	Case DupeModeXPlusY
		Return "DUPE X+Y"
	Default
		Return "DUPE UNKNOWN"
	End Select

End Function


Function GetAccessoryName$(AccessoryId)

	Select AccessoryId
	Case 0
		Return "None"
	Case 1
		Return "Cap"
	Case 2
		Return "Top Hat"
	Case 3
		Return "Builder"
	Case 4
		Return "Farmer"
	Case 5
		Return "Wizard"
	Case 6
		Return "Bowler"
	Case 7
		Return "BaseBall"
	Case 8
		Return "Beanie"
	Case 9
		Return "Crown"
	Case 10
		Return "Cape"
	Case 11
		Return "Clown"
	Case 12
		Return "Jewels"
	Case 13
		Return "Feather"
	Case 14
		Return "Flowerpot"
	Case 15
		Return "SillyBase"
	Case 16
		Return "Pirate"
	Case 17
		Return "Safari"
	Case 18
		Return "RobinHood"
	Case 19
		Return "Snowball"
	Case 20
		Return "Sombrero"
	Case 21
		Return "ZBot"
	Case 22
		Return "Santa"
	Case 23
		Return "Captain"
	Case 24
		Return "Bicorn"
	Case 25
		Return "Cowboy"
	Case 26
		Return "FlatRed"
	Case 27
		Return "Flower1"
	Case 28
		Return "Flower2"
	Case 29
		Return "Legion"
	Case 30
		Return "Hat-Ring"
	Case 31
		Return "BandRing1"
	Case 32
		Return "BandRing2"
	Case 33
		Return "Fedora"
	Case 34
		Return "Leaf"
	Case 35
		Return "Nest"
	Case 36
		Return "Pirate1"
	Case 37
		Return "Pirate2"
	Case 38
		Return "Sailor1"
	Case 39
		Return "Sailor2"
	Case 40
		Return "Wrap"
	Case 41
		Return "Sunhat"
	Case 42
		Return "Helmet"
	Case 43
		Return "Fez"
	Case 44
		Return "Sunhat2"
	Case 45
		Return "Chef"
	Case 46
		Return "Bowtie"
	Case 47
		Return "Helmet2"
	Case 48
		Return "Headphone"
	Case 49
		Return "Viking"
	Case 50
		Return "Welder"
	Case 51
		Return "Punk"
	Case 52
		Return "Ninja"
	Case 53
		Return "Bike"
	Case 54
		Return "RainbwCap"
	Case 55
		Return "Antenna"
	Case 56
		Return "Janet"
	Case 101
		Return "Thick Frame"
	Case 102
		Return "Thin Large"
	Case 103
		Return "Eyepatch L"
	Case 104
		Return "Eyepatch R"
	Case 105
		Return "Goggles"
	Case 106
		Return "Parrot"
	Case 107
		Return "Square"
	Case 108
		Return "Round"
	Case 109
		Return "Pink"
	Case 110
		Return "Sword"
	Case 111
		Return "Moustache"
	Case 112
		Return "Rose"
	Case 113
		Return "3D"
	Case 114
		Return "Bolt"
	Case 115
		Return "Monocle"
	Case 116
		Return "Bowtie"
	Default
		Return "NotVanilla"
	End Select

End Function


Function GetAccessoryColorName$(AccessoryId,ColorId)

	Select AccessoryId
	Case 1
		Select ColorId
		Case 1
			Return "Blue"
		Case 2
			Return "Rainbow"
		Case 3
			Return "Red"
		Case 4
			Return "Green"
		Case 5
			Return "Orange"
		Case 6
			Return "LightBlue"
		Case 7
			Return "Purple"
		Default
			Return "NotVanilla"
		End Select
	Case 2
		Select ColorId
		Case 1
			Return "Blue"
		Case 2
			Return "Purple"
		Case 3
			Return "Red"
		Case 4
			Return "Green"
		Case 5
			Return "Orange"
		Default
			Return "NotVanilla"
		End Select
	Case 3
		Select ColorId
		Case 1
			Return "Red"
		Case 2
			Return "Green"
		Case 3
			Return "Blue"
		Default
			Return "NotVanilla"
		
		End Select
	Case 5
		Select ColorId
		Case 1
			Return "Red"
		Case 2
			Return "Orange"
		Case 3
			Return "Yellow"
		Case 4
			Return "Green"
		Case 5
			Return "Blue"
		Case 6
			Return "Indigo"
		Case 7
			Return "Purple"
		Default
			Return "NotVanilla"
			
		End Select
	Case 6
		Select ColorId
		Case 1
			Return "Black"
		Case 2
			Return "Blue"
		Case 3
			Return "Red"
		Default
			Return "NotVanilla"
		
		End Select
	Case 7
		Select ColorId
		Case 1
			Return "WS"
		Case 2
			Return "Red"
		Case 3
			Return "Blue S"
		Default
			Return "NotVanilla"
		
		End Select
	Case 10
		Select ColorId
		Case 1
			Return "Blue"
		Case 2
			Return "Purple"
		Default
			Return "NotVanilla"
			
		End Select
	Case 27
		Select ColorId
		Case 1
			Return "Red"
		Case 2
			Return "Purple"
		Case 3
			Return "Gold"
		Case 4
			Return "Green"
		Default
			Return "NotVanilla"
		
		End Select
	Case 28
		Select ColorId
		Case 1
			Return "RedYel"
		Case 2
			Return "YelGreen"
		Case 3
			Return "BluePurp"
		Case 4
			Return "PurpRed"
		Default
			Return "NotVanilla"
		
		End Select

	Case 46
		Select ColorId
		Case 1
			Return "RedPink"
		Case 2
			Return "BlueGold"
		Case 3
			Return "GreeWhit"
		Case 4
			Return "Fall"
		Case 5
			Return "Frosty"
		Case 6
			Return "FullPink"
		Default
			Return "NotVanilla"
		
		End Select

	Case 101
		Select ColorId
		Case 1
			Return "Normal"
		Case 2
			Return "Sunglass"
		Default
			Return "NotVanilla"
		
		End Select
	Case 102
		Select ColorId
		Case 1
			Return "Black"
		Case 2
			Return "Red"
		Default
			Return "NotVanilla"
			
		End Select

	Default
		If AccessoryId<1
			Return "None"
		Else
			If ColorId=1 And IsAccessoryIdVanilla(AccessoryId)
				Return "Default"
			Else
				Return "NotVanilla"
			EndIf
		EndIf
	End Select

End Function


Function IsAccessoryIdVanilla(AccessoryId)

	Return (AccessoryId>-1 And AccessoryId<57) Or (AccessoryId>100 And AccessoryId<117)

End Function


Function GetAccessoryColorNameWithColorInt$(AccessoryId,ColorId)

	ColorName$=GetAccessoryColorName$(AccessoryId,ColorId)
	Return ColorId+"/"+ColorName$

End Function

Const AccessoryDirectory$="data/models/stinker/"

Function GetAccFilenameStart$(AccessoryId)

	; This is done because only the last three digits of the accessory ID are read in-game due to funky string handling.
	AccessoryId=AccessoryId Mod 1000
	
	If AccessoryId>99
		Prefix$="accessory"
	ElseIf AccessoryId>9 ; two digit
		Prefix$="accessory0"
	Else
		Prefix$="accessory00"
	EndIf

	Return Prefix$+Str$(AccessoryId)
	
End Function

Function GetAccFilenameModel$(AccessoryId)

	Return GetAccFilenameStart$(AccessoryId)+".3ds"	

End Function

Function GetAccFilenameTexture$(AccessoryId,ColorId)

	Return GetAccFilenameStart$(AccessoryId)+Chr$(64+ColorId)+".jpg"

End Function


Function CreateAccEntity(AccessoryId)

	FilePath$=AccessoryDirectory$+GetAccFilenameModel$(AccessoryId)
	If FileExistsModel(FilePath$)
		Return MyLoadMesh(FilePath$,0)
	Else
		;ShowMessage("YOU FAIL!!! "+FileName$+" IS NOT EVEN REAL!!!", 1000)
		Return CreateAccEntityNotExist()
	EndIf

End Function

Function CreateAccEntityNotExist()

	Entity=CreateSphere()
	ScaleMesh Entity,10,10,10
	Return Entity

End Function

Function CreateAccTexture(AccessoryId,ColorId)

	FilePath$=AccessoryDirectory$+GetAccFilenameTexture$(AccessoryId,ColorId)
	If FileExistsTexture(FilePath$)
		Return MyLoadTexture(FilePath$,4)
	Else
		;ShowMessage("YOU FAIL!!! "+FileName$+" IS NOT EVEN REAL!!!", 1000)
		Return 0
	EndIf

End Function

Function CreateHatTexture(Data2,Data3)

	Return CreateAccTexture(Data2,Data3)

End Function

Function CreateGlassesTexture(Data4,Data5)

	; The color ID on glasses is ahead of hats by 1.
	Return CreateAccTexture(Data4,Data5+1)

End Function

Function TransformAccessoryEntityGeneric(Entity,XScale#,YScale#,ZScale#,Yaw#,Pitch#,Roll#,X#,Y#,Z#)

	ScaleEntity Entity,XScale#,ZScale#,YScale#
	
	GameLikeRotation(Entity,Yaw#-90.0,Pitch#,Roll#)

	PositionEntity Entity,X#,Z#+.1+.84*ZScale#/.035,-Y#

End Function

Function TransformAccessoryEntityOntoBone(Entity,BoneHaver)

	bone=FindChild(BoneHaver,"hat_bone")

	PositionEntity Entity,EntityX(bone,True),EntityY(bone,True),EntityZ(bone,True)

	RotateEntity Entity,EntityPitch(bone,True),EntityYaw(bone,True),EntityRoll(bone,True)

	;GameLikeRotation(Entity,EntityYaw(bone,True),EntityRoll(bone,True),-EntityPitch(bone,True))

End Function


Function BuildLevelObjectModel(Dest)

	Obj.GameObject=LevelObjects(Dest)

	BuildObjectModel(Obj,Obj\Position\X,Obj\Position\Y,Obj\Position\Z)
	
	UpdateObjectAnimation(Obj)
	
	UpdateObjectVisibility(Obj)
	
	;PositionEntity Obj\Model\Entity,ObjectSumX#(Obj),ObjectSumZ#(Obj),-ObjectSumY#(Obj)
	
	;If Obj\Model\HatEntity>0
	;	TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)
	;EndIf
	;If Obj\Model\AccEntity>0
	;	TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)
	;EndIf

End Function


Function UpdateLevelObjectModel(Dest)

	;ShowMessage("Freeing object model "+Dest+": "+ObjectModelName$(Dest),10)

	FreeObjectModel(LevelObjects(Dest)\Model)
	
	;ShowMessage("Creating object model "+Dest+": "+ObjectModelName$(Dest),10)
	
	BuildLevelObjectModel(Dest)

End Function


Function CountObjectTypes(TargetType)

	Count=0
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\LogicType=TargetType
			Count=Count+1
		EndIf
	Next
	Return Count

End Function


Function CountObjectEffectiveIDs(EffectiveID)

	Count=0
	For i=0 To NofObjects-1
		If CalculateEffectiveID(LevelObjects(i)\Attributes)=EffectiveID
			Count=Count+1
		EndIf
	Next
	Return Count

End Function


Function CountObjectLogics(TargetType,TargetSubType)

	Count=0
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\LogicType=TargetType And LevelObjects(i)\Attributes\LogicSubType=TargetSubType
			Count=Count+1
		EndIf
	Next
	Return Count

End Function

Function CountObjectModelNames(TargetModelName$)

	Count=0
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\ModelName$=TargetModelName$
			Count=Count+1
		EndIf
	Next
	Return Count

End Function

Function CountObjectTextureNames(TargetTextureName$)

	Count=0
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\TexName$=TargetTextureName$
			Count=Count+1
		EndIf
	Next
	Return Count

End Function

Function IsObjectSubTypeFourColorButton(TargetSubType)

	Return (TargetSubType Mod 32)<5 ; square, round, diamond, diamondonce, star

End Function

Function IsObjectLogicFourColorButton(TargetType,TargetSubType)

	If TargetType=90
		Return IsObjectSubTypeFourColorButton(TargetSubType)
	Else
		Return False
	EndIf

End Function

Function IsObjectLogicAutodoor(TargetType,TargetSubType)

	Return TargetType=10 And TargetSubType=9

End Function

Function IsObjectTypeKeyblock(TargetType)

	Select TargetType
	Case 80,81,82,83,84,85,86,87
		Return True
	Default
		Return False
	End Select

End Function

Function GetAutodoorActivateName$(DataValue)

	If DataValue>=0
		Return "ActivateID"
	Else
		Return "ActivateType"
	EndIf

End Function

Function GetAutodoorActivateValueName$(DataValue)

	If DataValue=0
		Return "Creatures" ;"The Living"
	ElseIf DataValue<0
		Return Str$(-DataValue)+"/"+GetTypeString$(-DataValue)
	Else
		Return DataValue
	EndIf

End Function

; i hate blitz because it won't let me have arrays in custom types
Function SetDataByIndex(Attributes.GameObjectAttributes,i,Value)
	Select i
	Case 0
		Attributes\Data0=Value
	Case 1
		Attributes\Data1=Value
	Case 2
		Attributes\Data2=Value
	Case 3
		Attributes\Data3=Value
	Case 4
		Attributes\Data4=Value
	Case 5
		Attributes\Data5=Value
	Case 6
		Attributes\Data6=Value
	Case 7
		Attributes\Data7=Value
	Case 8
		Attributes\Data8=Value
	Case 9
		Attributes\Data9=Value
	End Select
End Function

; screw blitzard
Function GetDataByIndex(Attributes.GameObjectAttributes,i)

	Select i
	Case 0
		Return Attributes\Data0
	Case 1
		Return Attributes\Data1
	Case 2
		Return Attributes\Data2
	Case 3
		Return Attributes\Data3
	Case 4
		Return Attributes\Data4
	Case 5
		Return Attributes\Data5
	Case 6
		Return Attributes\Data6
	Case 7
		Return Attributes\Data7
	Case 8
		Return Attributes\Data8
	Case 9
		Return Attributes\Data9
	End Select

End Function

Function SetThreeOtherDataIfNotEqual(DTo1,DTo2,DTo3,DFrom,OldData)

	Attributes.GameObjectAttributes=CurrentObject\Attributes
	If GetDataByIndex(Attributes,DTo1)=OldData And GetDataByIndex(Attributes,DTo2)=OldData And GetDataByIndex(Attributes,DTo3)=OldData
		NewValue=GetDataByIndex(Attributes,DFrom)
		SetDataByIndex(Attributes,DTo1,NewValue)
		SetDataByIndex(Attributes,DTo2,NewValue)
		SetDataByIndex(Attributes,DTo3,NewValue)
	EndIf

End Function


Function CameraControls()
	MouseDeltaX = MouseXSpeed()
	MouseDeltaY = MouseYSpeed()
	mx = MouseX()
	my = MouseY()
	
	Adj#=0.1
	IntAdj=1
	If ShiftDown()
		Adj#=0.4
		IntAdj=4
	EndIf
	
	; Still doesn't really work, and also doesn't behave well with orthographic mode yet.
	;If KeyPressed(idk) ; Formerly 20, which is T
	;	ToggleGameCamera()
	;EndIf
	
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
	
	If EditorMode=3 And mx>=SidebarX+195 And my>=SidebarY+305 And mx<=SidebarX+295 And my<=SidebarY+430 ; camera4 viewport space
		Target=Camera4 ; object camera
	;ElseIf EditorMode=0 And mx>=510 And mx<710 And my>=20 And my<240
	;	Target=-1 ; tile camera
	Else
		Target=Camera1 ; level camera
	EndIf
	
	If Target=-1
		Return
	EndIf
	
	If Not CtrlDown()

		If KeyDown(75) Or KeyDown(203) Or KeyDown(KeyMoveWest) ; numpad 4 or left arrow
				
			TranslateEntity Target,-Adj,0,0
		EndIf
		If KeyDown(77) Or KeyDown(205) Or KeyDown(KeyMoveEast) ; numpad 6 or right arrow
			
			TranslateEntity Target,Adj,0,0
		EndIf
		If KeyDown(72) Or KeyDown(200) Or KeyDown(KeyMoveNorth) ; numpad 8 or up arrow
		
			TranslateEntity Target,0,0,Adj
		EndIf
		If KeyDown(80) Or KeyDown(208) Or KeyDown(KeyMoveSouth) ; numpad 2 or down arrow
		
			TranslateEntity Target,0,0,-Adj
		EndIf
		If KeyDown(73) Or KeyDown(18) ; numpad 9 or E
		
			TranslateEntity Target,0,Adj,0
		EndIf
		If KeyDown(81) Or KeyDown(46) ; numpad 3 or C
		
			TranslateEntity Target,0,-Adj,0
		EndIf
		If KeyDown(71) Or KeyDown(16) ; numpad 7 or Q
			
			TurnEntity Target,IntAdj,0,0
		EndIf
		If KeyDown(79) Or KeyDown(44) ; numpad 1 or Z
		
			TurnEntity Target,-IntAdj,0,0
		EndIf
		If KeyDown(181) Or KeyDown(23) ;Or KeyDown(3) ; numpad / or I
			RotateEntity Target,EntityPitch#(Target),EntityYaw#(Target)+IntAdj,EntityRoll#(Target)
		EndIf
		If KeyDown(55) Or KeyDown(24) ;Or KeyDown(4) ; numpad * or O
			RotateEntity Target,EntityPitch#(Target),EntityYaw#(Target)-IntAdj,EntityRoll#(Target)
		EndIf
		
		If KeyDown(76) Or KeyDown(45) ; numpad 5 or X
			; reset camera position and rotation
			If Target=Camera1
				RotateEntity Camera1,65,0,0
				If ShiftDown()
					CenterCameraInLevel()
					If Camera1Proj=2 ; orthographic
						TheY#=Camera1StartY*2
					Else ; probably perspective
						TheY#=Camera1StartY
					EndIf
					PositionEntity Camera1,EntityX(Camera1),TheY#,EntityZ(Camera1)
				EndIf
			ElseIf Target=Camera4
				RotateEntity Camera4,25,0,0
				PositionEntity Camera4,0,303.8,-8
				Camera4Zoom#=8
				CameraZoom Camera4,Camera4Zoom#
			EndIf
		EndIf
		
	EndIf
	
	If MouseScroll<>0
		; mouse position check here because we don't want to move the camera when using scroll wheel on object adjusters
		If Target=Camera1 And mx<LevelViewportWidth+10 And my>=0 And my<LevelViewportHeight
			If ShiftDown()
				SetBrushWidth(BrushWidth+MouseScroll)
			EndIf
			If CtrlDown()
				SetBrushHeight(BrushHeight+MouseScroll)
			EndIf
			If (Not ShiftDown()) And (Not CtrlDown())
				; level camera
				If Camera1Proj=1 ; perspective
					SpeedFactor#=3.0*Adj
					TranslateEntity Camera1,0,-MouseScroll * SpeedFactor,0
				ElseIf Camera1Proj=2 ; orthographic
					ZoomSpeed#=12.0*Adj
					If MouseScroll>0
						Camera1Zoom#=Camera1Zoom#*ZoomSpeed
					ElseIf MouseScroll<0
						Camera1Zoom#=Camera1Zoom#/ZoomSpeed
					EndIf
					If Camera1Zoom#<0.001
						Camera1Zoom#=0.001
					EndIf
					CameraZoom Camera1,Camera1Zoom#
				EndIf
			EndIf
		ElseIf Target=Camera4
			; object camera
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


Function ToggleGameCamera()

	GameCamera=Not GameCamera

	If GameCamera
		RotateEntity Camera1,55,0,0
		PositionEntity Camera1,EntityX(Camera1),12,EntityZ(Camera1) ; y=12 in-game
		;If Not widescreen
		If Not displayfullscreen
			CameraZoom Camera1,2
		Else
			CameraZoom Camera1,1.5
		EndIf
	Else
		RotateEntity Camera1,65,0,0
		PositionEntity Camera1,EntityX(Camera1),6,EntityZ(Camera1) ; 7,6,-14
		CameraZoom Camera1,1
	EndIf

End Function


Function SaveLevel()
	
	file=WriteFile (GetAdventureDir$()+currentlevelnumber+".wlv")
	
	If (currentlevelnumber>94 And currentlevelnumber<99) Or Left$(Upper$(adventurefilename$),5)="ZACHY"
		; WA3 VAULTS
		WriteInt file,LevelWidth+121
	Else
		WriteInt file,LevelWidth
	EndIf
	WriteInt file,LevelHeight
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			WriteInt file,LevelTiles(i,j)\Terrain\Texture ; corresponding to squares in LevelTexture
			WriteInt file,LevelTiles(i,j)\Terrain\Rotation ; 0-3 , and 4-7 for "flipped"
			WriteInt file,LevelTiles(i,j)\Terrain\SideTexture ; texture for extrusion walls
			WriteInt file,LevelTiles(i,j)\Terrain\SideRotation ; 0-3 , and 4-7 for "flipped"
			WriteFloat file,LevelTiles(i,j)\Terrain\Random ; random height pertubation of tile
			WriteFloat file,LevelTiles(i,j)\Terrain\Height ; height of "center" - e.g. to make ditches and hills
			WriteFloat file,LevelTiles(i,j)\Terrain\Extrusion; extrusion with walls around it 
			WriteInt file,LevelTiles(i,j)\Terrain\Rounding; 0-no, 1-yes: are floors rounded if on a drop-off corner
			WriteInt file,LevelTiles(i,j)\Terrain\EdgeRandom; 0-no, 1-yes: are edges rippled
			
			
			WriteInt file,LevelTiles(i,j)\Terrain\Logic
			

			
		Next
	Next
	
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			WriteInt file,LevelTiles(i,j)\Water\Texture
			WriteInt file,LevelTiles(i,j)\Water\Rotation
			WriteFloat file,LevelTiles(i,j)\Water\Height
			WriteFloat file,LevelTiles(i,j)\Water\Turbulence
		Next
	Next
	
	; Globals
	WriteInt file,WaterFlow
	WriteInt file,WaterTransparent
	WriteInt file,WaterGlow
	
	WriteString file,CurrentLevelTextureName$()
	WriteString file,CurrentWaterTextureName$()

	
	
	
	; Objects
	
	PlayerIndex=ObjectIndexEditorToGameInner(NofObjects)
	
	WriteInt file,NofObjects
	For i=0 To NofObjects-1
		Obj.GameObject=LevelObjects(i)
		
		WriteString file,Obj\Attributes\ModelName$
		WriteString file,Obj\Attributes\TexName$
		WriteFloat file,Obj\Attributes\XScale
		WriteFloat file,Obj\Attributes\YScale
		WriteFloat file,Obj\Attributes\ZScale
		WriteFloat file,Obj\Attributes\XAdjust
		WriteFloat file,Obj\Attributes\YAdjust
		WriteFloat file,Obj\Attributes\ZAdjust
		WriteFloat file,Obj\Attributes\PitchAdjust
		WriteFloat file,Obj\Attributes\YawAdjust
		WriteFloat file,Obj\Attributes\RollAdjust
	
		WriteFloat file,Obj\Position\X
		WriteFloat file,Obj\Position\Y
		WriteFloat file,Obj\Position\Z
		WriteFloat file,Obj\Position\OldX
		WriteFloat file,Obj\Position\OldY
		WriteFloat file,Obj\Position\OldZ

		WriteFloat file,Obj\Attributes\DX
		WriteFloat file,Obj\Attributes\DY
		WriteFloat file,Obj\Attributes\DZ
	
		WriteFloat file,Obj\Attributes\Pitch
		WriteFloat file,Obj\Attributes\Yaw
		WriteFloat file,Obj\Attributes\Roll
		WriteFloat file,Obj\Attributes\Pitch2
		WriteFloat file,Obj\Attributes\Yaw2
		WriteFloat file,Obj\Attributes\Roll2

		WriteFloat file,Obj\Attributes\XGoal
		WriteFloat file,Obj\Attributes\YGoal
		WriteFloat file,Obj\Attributes\ZGoal
	
		WriteInt file,Obj\Attributes\MovementType
		WriteInt file,Obj\Attributes\MovementTypeData
		WriteFloat file,Obj\Attributes\Speed
		WriteFloat file,Obj\Attributes\Radius
		WriteInt file,Obj\Attributes\RadiusType
	
		WriteInt file,Obj\Attributes\Data10
	
		WriteFloat file,Obj\Attributes\PushDX
		WriteFloat file,Obj\Attributes\PushDY
	
		WriteInt file,Obj\Attributes\AttackPower
		WriteInt file,Obj\Attributes\DefensePower
		WriteInt file,Obj\Attributes\DestructionType
	
		WriteInt file,Obj\Attributes\ID
		WriteInt file,Obj\Attributes\LogicType
		WriteInt file,Obj\Attributes\LogicSubType
	
		WriteInt file,Obj\Attributes\Active
		WriteInt file,Obj\Attributes\LastActive
		WriteInt file,Obj\Attributes\ActivationType
		WriteInt file,Obj\Attributes\ActivationSpeed
	
		WriteInt file,Obj\Attributes\Status
		WriteInt file,Obj\Attributes\Timer
		WriteInt file,Obj\Attributes\TimerMax1
		WriteInt file,Obj\Attributes\TimerMax2
	
		WriteInt file,Obj\Attributes\Teleportable
		WriteInt file,Obj\Attributes\ButtonPush
		WriteInt file,Obj\Attributes\WaterReact
	
		WriteInt file,Obj\Attributes\Telekinesisable
		WriteInt file,Obj\Attributes\Freezable
	
		WriteInt file,Obj\Attributes\Reactive

		;WriteInt file,ObjectChild
		WriteInt file,ObjectIndexEditorToGame(Obj\Attributes\Child, PlayerIndex)
		;WriteInt file,ObjectParent
		WriteInt file,ObjectIndexEditorToGame(Obj\Attributes\Parent, PlayerIndex)
	
		WriteInt file,Obj\Attributes\Data0
		WriteInt file,Obj\Attributes\Data1
		WriteInt file,Obj\Attributes\Data2
		WriteInt file,Obj\Attributes\Data3
		WriteInt file,Obj\Attributes\Data4
		WriteInt file,Obj\Attributes\Data5
		WriteInt file,Obj\Attributes\Data6
		WriteInt file,Obj\Attributes\Data7
		WriteInt file,Obj\Attributes\Data8
		WriteInt file,Obj\Attributes\Data9
		WriteString file,Obj\Attributes\TextData0
		WriteString file,Obj\Attributes\TextData1
		WriteString file,Obj\Attributes\TextData2
		WriteString file,Obj\Attributes\TextData3
		
		WriteInt file,Obj\Attributes\Talkable
		WriteInt file,Obj\Attributes\CurrentAnim
		WriteInt file,Obj\Attributes\StandardAnim
		WriteInt file,Obj\Position\TileX
		WriteInt file,Obj\Position\TileY
		WriteInt file,Obj\Position\TileX2
		WriteInt file,Obj\Position\TileY2
		WriteInt file,Obj\Attributes\MovementTimer
		WriteInt file,Obj\Attributes\MovementSpeed
		WriteInt file,Obj\Attributes\MoveXGoal
		WriteInt file,Obj\Attributes\MoveYGoal
		WriteInt file,Obj\Attributes\TileTypeCollision
		WriteInt file,Obj\Attributes\ObjectTypeCollision
		WriteInt file,Obj\Attributes\Caged
		WriteInt file,Obj\Attributes\Dead
		WriteInt file,Obj\Attributes\DeadTimer
		WriteInt file,Obj\Attributes\Exclamation
		WriteInt file,Obj\Attributes\Shadow
		;WriteInt file,-1;ObjectLinked
		WriteInt file,ObjectIndexEditorToGame(Obj\Attributes\Linked, PlayerIndex)
		;WriteInt file,-1;ObjectLinkBack
		WriteInt file,ObjectIndexEditorToGame(Obj\Attributes\LinkBack, PlayerIndex)
		WriteInt file,Obj\Attributes\Flying
		WriteInt file,Obj\Attributes\Frozen
		WriteInt file,Obj\Attributes\Indigo
		WriteInt file,Obj\Attributes\FutureInt24
		WriteInt file,Obj\Attributes\FutureInt25
		WriteFloat file,Obj\Attributes\ScaleAdjust
		WriteFloat file,Obj\Attributes\ScaleXAdjust
		WriteFloat file,Obj\Attributes\ScaleYAdjust
		WriteFloat file,Obj\Attributes\ScaleZAdjust
		WriteFloat file,Obj\Attributes\FutureFloat5
		WriteFloat file,Obj\Attributes\FutureFloat6
		WriteFloat file,Obj\Attributes\FutureFloat7
		WriteFloat file,Obj\Attributes\FutureFloat8
		WriteFloat file,Obj\Attributes\FutureFloat9
		WriteFloat file,Obj\Attributes\FutureFloat10
		WriteString file,Obj\Attributes\FutureString1$
		WriteString file,Obj\Attributes\FutureString2$
		
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
	
	; NEVER save extra data at the end of the wlv file.
	; Saving extra bytes at the end of the file confuses the vanilla player into changing the state of every tile in the level.
	
	;WriteInt file,-2
	;WriteInt file,WidescreenRangeLevel
	
	CloseFile file

	UnsavedChanges=0

End Function


Function CenterCameraInLevel()

	PositionCameraInLevel(LevelWidth/2,LevelHeight/2)

End Function


Function AccessLevelAt(levelnumber,FocusOnTileX,FocusOnTileY)

	AccessLevel(levelnumber)
	PositionCameraInLevel(FocusOnTileX,FocusOnTileY)

End Function


Function AccessLevelAtCenter(levelnumber)

	AccessLevel(levelnumber)
	CenterCameraInLevel()

End Function


Function SetCurrentLevelNumber(levelnumber)

	If OpenedFirstLevelYet And CurrentLevelNumber<>levelnumber
		; Add the current level number to the ring buffer that's tracking previous level numbers.
		PreviousLevelNumberBuffer(PreviousLevelNumberBufferCurrent)=CurrentLevelNumber
		If PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferMax
			PreviousLevelNumberBufferCurrent=0
		Else
			PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferCurrent+1
		EndIf
		If PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferStart
			If PreviousLevelNumberBufferStart=PreviousLevelNumberBufferMax
				PreviousLevelNumberBufferStart=0
			Else
				PreviousLevelNumberBufferStart=PreviousLevelNumberBufferStart+1
			EndIf
		EndIf
	Else
		OpenedFirstLevelYet=True
	EndIf
	
	CurrentLevelNumber=levelnumber

End Function


Function TryPopPreviousLevel()

	If PreviousLevelNumberBufferStart<>PreviousLevelNumberBufferCurrent
		If AskToSaveLevelAndExit()
			If PreviousLevelNumberBufferCurrent=0
				PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferMax
			Else
				PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferCurrent-1
			EndIf
			CurrentLevelNumber=PreviousLevelNumberBuffer(PreviousLevelNumberBufferCurrent)
			AccessLevelAtCenter(CurrentLevelNumber)
		EndIf
	EndIf

End Function


Function LoadLevel(levelnumber)

	SetCurrentLevelNumber(levelnumber)

	resetlevel()
	
	; clear current objects first
	;ShowMessage("Freeing " + NofObjects + " objects...", 1000)
	For i=0 To NofObjects-1
		;DeleteObject(i)
		FreeObject(i)
	Next
	
	file=ReadFile (GetAdventureDir$()+levelnumber+".wlv")
	
	LevelWidth=-999
	; This loop will bypass the protection on MOFI and WA3 Beta1 level files.
	; MOFI levels have only one extra -999 integer.
	While LevelWidth=-999
		LevelWidth=ReadInt(File)
	Wend
	
	If LevelWidth>121
		; WA3 VAULTS
		LevelWidth=LevelWidth-121
	EndIf

	LevelHeight=ReadInt(File)
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			LevelTiles(i,j)\Terrain\Texture=ReadInt(file) ; corresponding to squares in LevelTexture
			LevelTiles(i,j)\Terrain\Rotation=ReadInt(file) ; 0-3 , and 4-7 for "flipped"
			LevelTiles(i,j)\Terrain\SideTexture=ReadInt(file) ; texture for extrusion walls
			LevelTiles(i,j)\Terrain\SideRotation=ReadInt(file) ; 0-3 , and 4-7 for "flipped"
			LevelTiles(i,j)\Terrain\Random=ReadFloat(file) ; random height pertubation of tile
			LevelTiles(i,j)\Terrain\Height=ReadFloat(file) ; height of "center" - e.g. to make ditches and hills
			LevelTiles(i,j)\Terrain\Extrusion=ReadFloat(file); extrusion with walls around it 
			LevelTiles(i,j)\Terrain\Rounding=ReadInt(file); 0-no, 1-yes: are floors rounded if on a drop-off corner
			LevelTiles(i,j)\Terrain\EdgeRandom=ReadInt(file); 0-no, 1-yes: are edges rippled
			LevelTiles(i,j)\Terrain\Logic=ReadInt(file)
			
			LevelTileObjectCount(i,j)=0
			
		Next
	Next
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			LevelTiles(i,j)\Water\Texture=ReadInt(file)
			LevelTiles(i,j)\Water\Rotation=ReadInt(file)
			LevelTiles(i,j)\Water\Height=ReadFloat(file)
			LevelTiles(i,j)\Water\Turbulence=ReadFloat(file)
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
			WaterTexture=myLoadTexture("data\Leveltextures\"+WaterTexturename$(CurrentWaterTexture),1)
		EndIf
	Else
		waterTexture=myLoadTexture("data\Leveltextures\"+waterTexturename$(CurrentWaterTexture),1)
	EndIf
	
	leftmousereleased=False
		



	NofObjects=0
	ReadObjectCount=ReadInt(file)
	For i=0 To ReadObjectCount-1
		LevelObject.GameObject=LevelObjects(i)
		Attributes.GameObjectAttributes=LevelObject\Attributes
		Position.GameObjectPosition=LevelObject\Position
		
		Attributes\ModelName$=ReadString$(file)
		Attributes\TexName$=ReadString$(file)
		Attributes\XScale=ReadFloat(file)
		Attributes\YScale=ReadFloat(file)
		Attributes\ZScale=ReadFloat(file)
		Attributes\XAdjust=ReadFloat(file)
		Attributes\YAdjust=ReadFloat(file)
		Attributes\ZAdjust=ReadFloat(file)
		Attributes\PitchAdjust=ReadFloat(file)
		Attributes\YawAdjust=ReadFloat(file)
		Attributes\RollAdjust=ReadFloat(file)
	
		Position\X=ReadFloat(file)
		Position\Y=ReadFloat(file)
		Position\Z=ReadFloat(file)
		Position\OldX=ReadFloat(file)
		Position\OldY=ReadFloat(file)
		Position\OldZ=ReadFloat(file)

		Attributes\DX=ReadFloat(file)
		Attributes\DY=ReadFloat(file)
		Attributes\DZ=ReadFloat(file)
	
		Attributes\Pitch=ReadFloat(file)
		Attributes\Yaw=ReadFloat(file)
		Attributes\Roll=ReadFloat(file)
		Attributes\Pitch2=ReadFloat(file)
		Attributes\Yaw2=ReadFloat(file)
		Attributes\Roll2=ReadFloat(file)

		Attributes\XGoal=ReadFloat(file)
		Attributes\YGoal=ReadFloat(file)
		Attributes\ZGoal=ReadFloat(file)
	
		Attributes\MovementType=ReadInt(file)
		Attributes\MovementTypeData=ReadInt(file)
		Attributes\Speed=ReadFloat(file)
		Attributes\Radius=ReadFloat(file)
		Attributes\RadiusType=ReadInt(file)
	
		Attributes\Data10=ReadInt(file)
	
		Attributes\PushDX=ReadFloat(file)
		Attributes\PushDY=ReadFloat(file)
	
		Attributes\AttackPower=ReadInt(file)
		Attributes\DefensePower=ReadInt(file)
		Attributes\DestructionType=ReadInt(file)
	
		Attributes\ID=ReadInt(file)
		
		Attributes\LogicType=ReadInt(file)
		Attributes\LogicSubType=ReadInt(file)
	
		Attributes\Active=ReadInt(file)
		Attributes\LastActive=ReadInt(file)
		Attributes\ActivationType=ReadInt(file)
		Attributes\ActivationSpeed=ReadInt(file)
	
		Attributes\Status=ReadInt(file)
		Attributes\Timer=ReadInt(file)
		Attributes\TimerMax1=ReadInt(file)
		Attributes\TimerMax2=ReadInt(file)
	
		Attributes\Teleportable=ReadInt(file)
		Attributes\ButtonPush=ReadInt(file)
		Attributes\WaterReact=ReadInt(file)
	
		Attributes\Telekinesisable=ReadInt(file)
		Attributes\Freezable=ReadInt(file)
	
		Attributes\Reactive=ReadInt(file)

		Attributes\Child=ReadInt(file)
		Attributes\Parent=ReadInt(file)

		Attributes\Data0=ReadInt(file)
		Attributes\Data1=ReadInt(file)
		Attributes\Data2=ReadInt(file)
		Attributes\Data3=ReadInt(file)
		Attributes\Data4=ReadInt(file)
		Attributes\Data5=ReadInt(file)
		Attributes\Data6=ReadInt(file)
		Attributes\Data7=ReadInt(file)
		Attributes\Data8=ReadInt(file)
		Attributes\Data9=ReadInt(file)
	
		Attributes\TextData0=ReadString$(file)
		Attributes\TextData1=ReadString$(file)
		Attributes\TextData2=ReadString$(file)
		Attributes\TextData3=ReadString$(file)
		
		Attributes\Talkable=ReadInt(file)
		Attributes\CurrentAnim=ReadInt(file)
		Attributes\StandardAnim=ReadInt(file)
		;ObjectTileX=ReadInt(file)
		;ObjectTileY=ReadInt(file)
		tilex=ReadInt(file)
		tiley=ReadInt(file)
		SetObjectTileXY(i,tilex,tiley)
		ReadInt(file) ;ObjectTileX2=ReadInt(file)
		ReadInt(file) ;ObjectTileY2=ReadInt(file)
		Attributes\MovementTimer=ReadInt(file)
		Attributes\MovementSpeed=ReadInt(file)
		Attributes\MoveXGoal=ReadInt(file)
		Attributes\MoveYGoal=ReadInt(file)
		Attributes\TileTypeCollision=ReadInt(file)
		Attributes\ObjectTypeCollision=ReadInt(file)
		Attributes\Caged=ReadInt(file)
		Attributes\Dead=ReadInt(file)
		Attributes\DeadTimer=ReadInt(file)
		Attributes\Exclamation=ReadInt(file)
		Attributes\Shadow=ReadInt(file)
		Attributes\Linked=ReadInt(file)
		Attributes\LinkBack=ReadInt(file)
		Attributes\Flying=ReadInt(file)
		Attributes\Frozen=ReadInt(file)
		Attributes\Indigo=ReadInt(file)
		Attributes\FutureInt24=ReadInt(file)
		Attributes\FutureInt25=ReadInt(file)
		Attributes\ScaleAdjust=ReadFloat(file)
		Attributes\ScaleXAdjust=ReadFloat(file)	
		Attributes\ScaleYAdjust=ReadFloat(file)
		Attributes\ScaleZAdjust=ReadFloat(file)
		Attributes\ScaleXAdjust=1.0
		Attributes\ScaleYAdjust=1.0
		Attributes\ScaleZAdjust=1.0
		Attributes\FutureFloat5=ReadFloat(file)
		Attributes\FutureFloat6=ReadFloat(file)
		Attributes\FutureFloat7=ReadFloat(file)	
		Attributes\FutureFloat8=ReadFloat(file)
		Attributes\FutureFloat9=ReadFloat(file)
		Attributes\FutureFloat10=ReadFloat(file)
		Attributes\FutureString1$=ReadString(file)
		Attributes\FutureString2$=ReadString(file)
		
		For k=0 To 30
			;ObjectAdjusterString$(Dest,k)=ReadString(file)
			ReadString(file)
		Next
		
		BuildLevelObjectModel(i)


		NofObjects=NofObjects+1
		

		CreateObjectPositionMarker(i)
	Next
	
	
	
	; finalize object data
	PlayerIndex=NofObjects
	For j=0 To NofObjects-1
		LevelObject.GameObject=LevelObjects(j)
		LevelObject\Attributes\Linked=ObjectIndexGameToEditor(LevelObject\Attributes\Linked, PlayerIndex)
		LevelObject\Attributes\LinkBack=ObjectIndexGameToEditor(LevelObject\Attributes\LinkBack, PlayerIndex)
		LevelObject\Attributes\Parent=ObjectIndexGameToEditor(LevelObject\Attributes\Parent, PlayerIndex)
		LevelObject\Attributes\Child=ObjectIndexGameToEditor(LevelObject\Attributes\Child, PlayerIndex)
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
	
	LightingWasChanged()
	
	ReBuildLevelModel()
	
	UpdateLevelTexture()
	UpdateWaterTexture()
	
	BuildCurrentTileModel()
	
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next
	
	OpenedLevel()

	If Not Eof(file)
		ReadString(file)
	EndIf
	
	If Not Eof(file)
		ReadInt(file)
	EndIf
	
	If Not Eof(file)
		WidescreenRangeLevel=ReadInt(file)
		AddUnsavedChange()
	EndIf
	
	CloseFile file
	
End Function

Function NewLevel(levelnumber)

	; new level
	SetCurrentLevelNumber(levelnumber)
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
	waterTexture=myLoadTexture("data\Leveltextures\"+waterTexturename$(CurrentWaterTexture),1)

	UpdateLevelTexture()
	UpdateWaterTexture()

	rebuildlevelmodel()
	BuildCurrentTileModel()
	
	OpenedLevel()

End Function

Function CompileLevel()

End Function

Function OpenedLevel()

	UnsavedChanges=0

	; Don't restore master here, because AccessLevel is used by StartTestModeAt.
	RestoreOriginal1Wlv()

	WhereWeEndedUpAlpha#=0.0

	If BrushMode=BrushModeTestLevel
		BrushMode=BrushModeNormal
	EndIf
	
	BrushCursorProbablyModifiedTiles()
	
	UpdateMusic()

End Function

Function AccessLevel(levelnumber)

	If LevelExists(levelnumber)=True
		LoadLevel(levelnumber)
	Else
		NewLevel(levelnumber)
	EndIf

End Function

Function OpenTypedLevel()

	Typed$=InputString$("Enter wlv number to open: ")
	If Typed$<>""
		AccessLevelAtCenter(Typed$)
		Return True
	Else
		Return False
	EndIf

End Function

Function OpenTypedDialog()

	Typed$=InputString$("Enter dia number to open: ")
	If Typed$<>""
		AccessDialog(Typed$)
		Return True
	Else
		Return False
	EndIf

End Function

Function GetAdventuresDir$(CurrentArchive)

	Select CurrentArchive ; The corresponding global variable is AdventureCurrentArchive.
	Case AdventureCurrentArchiveArchive
		Return globaldirname$+"\Custom\Editing\Archive\"
	Case AdventureCurrentArchivePlayer
		Return globaldirname$+"\Custom\Adventures\"
	Case AdventureCurrentArchiveDataAdventures
		Return "Data\Adventures\"
	Default
		Return globaldirname$+"\Custom\Editing\Current\"
	End Select

End Function

Function GetAdventureFolder$()
	
	ex2$=GetAdventuresDir$(AdventureCurrentArchive)
	Return ex2$+AdventureFileName$
	
End Function

Function GetAdventureDir$()
	
	Return GetAdventureFolder$()+"\"
	
End Function

Function MoveFile(numbersource,numberdest,ext$)
	
	dirbase$=GetAdventureDir$()
	CopyFile(dirbase$+numbersource+ext$,dirbase$+numberdest+ext$)
	DeleteFile(dirbase$+numbersource+ext$)

End Function

Function DuplicateMaster(SourceName$,DestinationName$)

	dirbase$=GetAdventureDir$()
	CopyFile(dirbase$+SourceName$+".dat",dirbase$+DestinationName$+".dat")

End Function

Function DeleteMaster(TargetName$)

	dirbase$=GetAdventureDir$()
	DeleteFile(dirbase$+TargetName$+".dat")

End Function

Function RestoreOriginalMaster()

	dirbase$=GetAdventureDir$()
	If FileExists(dirbase$+OriginalMasterDat$+".dat")
		DeleteMaster("master")
		DuplicateMaster(OriginalMasterDat$,"master")
		DeleteMaster(OriginalMasterDat$)
	EndIf

End Function

Function RestoreOriginal1Wlv()

	dirbase$=GetAdventureDir$()
	If FileExists(dirbase$+Original1Wlv$+".wlv")
		DeleteLevel(1)
		DuplicateLevel(Original1Wlv$,1)
		DeleteLevel(Original1Wlv$)
	EndIf

End Function

Function DeleteLevel(TargetLevelName$)

	dirbase$=GetAdventureDir$()
	DeleteFile(dirbase$+TargetLevelName$+".wlv")

End Function

Function DuplicateLevel(levelnumbersource$,levelnumberdest$)

	dirbase$=GetAdventureDir$()
	CopyFile(dirbase$+levelnumbersource+".wlv",dirbase$+levelnumberdest+".wlv")

End Function

Function MoveLevel(levelnumbersource,levelnumberdest)

	MoveFile(levelnumbersource,levelnumberdest,".wlv")
	MasterLevelList(levelnumbersource)=0
	MasterLevelList(levelnumberdest)=1

End Function

Function MoveDialog(levelnumbersource,levelnumberdest)

	MoveFile(levelnumbersource,levelnumberdest,".wlv")
	MasterLevelList(levelnumbersource)=0
	MasterLevelList(levelnumberdest)=1

End Function

Function SwapFile(levelnumber1,levelnumber2,ext$,Exists1,Exists2)

	If Exists1 And Exists2
		dirbase$=GetAdventureDir$()
		CopyFile(dirbase$+levelnumber1+ext$,dirbase$+"temp"+levelnumber1+ext$)
		DeleteFile(dirbase$+levelnumber1+ext$)
		CopyFile(dirbase$+levelnumber2+ext$,dirbase$+levelnumber1+ext$)
		DeleteFile(dirbase$+levelnumber2+ext$)
		CopyFile(dirbase$+"temp"+levelnumber1+ext$,dirbase$+levelnumber2+ext$)
		DeleteFile(dirbase$+"temp"+levelnumber1+ext$)
	ElseIf Exists1=True And Exists2=False
		MoveFile(levelnumber1,levelnumber2,ext$)
	ElseIf Exists1=False And Exists2=True
		MoveFile(levelnumber2,levelnumber1,ext$)
	EndIf

End Function

Function SwapLevel(levelnumber1,levelnumber2)
	
	Exists1=LevelExists(levelnumber1)
	Exists2=LevelExists(levelnumber2)
	SwapFile(levelnumber1,levelnumber2,".wlv",Exists1,Exists2)
	MasterLevelList(levelnumber1)=Exists2
	MasterLevelList(levelnumber2)=Exists1

End Function

Function SwapDialog(dialognumber1,dialognumber2)

	Exists1=DialogExists(dialognumber1)
	Exists2=DialogExists(dialognumber2)
	SwapFile(dialognumber1,dialognumber2,".dia",Exists1,Exists2)
	MasterDialogList(dialognumber1)=Exists2
	MasterDialogList(dialognumber2)=Exists1

End Function

Function CreateButtonMesh(btype,col1,col2,col3,col4)
	; texture is the available "colour" from 0-15
	btype=btype Mod 32
	;IsGeneralCommand=False
	
	If btype=15
		;IsGeneralCommand=True
		Return CreateGeneralCommandTextMesh(col1)
		
		btype=11
	EndIf
	
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
	
;	If IsGeneralCommand
;	
;		ExtraEntity=CreateGeneralCommandTextMesh(col1)
;
;		EntityParent ExtraEntity,Entity
;	
;	EndIf
	
	Return Entity

End Function

Function CreateGeneralCommandTextMesh(col1)

	ExtraEntity=CreateMesh()
	Surface=CreateSurface(ExtraEntity)
	
	Command$=col1
	
	r=GetCommandColor(col1,0)
	g=GetCommandColor(col1,1)
	b=GetCommandColor(col1,2)
	
	scaling#=0.3
	yoffset#=-0.10
	
	;outlinesize#=0.01
	
	BuildTextSurface(Surface,Command$,-0.015,scaling#,0,0.0,yoffset#)
	
	EntityColor ExtraEntity,r,g,b
	
	RotateMesh ExtraEntity,90,0,0
	
	UpdateNormals ExtraEntity
	
	
	BackdropEntity=CreateMesh(ExtraEntity)
	Surface=CreateSurface(BackdropEntity)
	
	;scaling#=scaling#*2
	
	offset#=0.02
	
	BuildTextSurface(Surface,Command$,-0.014,scaling#,0,offset#,yoffset#)
	BuildTextSurface(Surface,Command$,-0.014,scaling#,1,-offset#,yoffset#)
	BuildTextSurface(Surface,Command$,-0.014,scaling#,2,0.0,yoffset#+offset#)
	BuildTextSurface(Surface,Command$,-0.014,scaling#,3,0.0,yoffset#-offset#)
	
	;radius#=0.45
	
	;AddVertex (surface,-radius,0.01,radius)
	;AddVertex (surface,radius,0.01,radius)
	;AddVertex (surface,-radius,0.01,-radius)
	;AddVertex (surface,radius,0.01,-radius)
	;AddTriangle (surface,0,1,2)
	;AddTriangle (surface,1,3,2)
	
	;EntityAlpha BackdropEntity,0.3
	
	EntityColor BackdropEntity,0,0,0
	;EntityColor BackdropEntity,255,255,255
	;EntityColor BackdropEntity,255-r,255-g,255-b
	
	RotateMesh BackdropEntity,90,0,0
	
	UpdateNormals BackdropEntity
	
	; Comment these out to debug the glyph placement.
	EntityTexture ExtraEntity,TextTexture
	EntityTexture BackdropEntity,TextTexture
	
	Return ExtraEntity

End Function

Function BuildTextSurface(mySurface,myString$,z#,scaling#,letteroffset=0,xoffset#=0.0,yoffset#=0.0)

	xsize#=scaling#
	ysize#=scaling#

	For i=1 To Len(myString$)
		let=Asc(Mid$(myString$,i,1))-32
		letternumber=i-1
		
		x#=-xsize#*(Len(myString$)-1)*0.5+(i-1)*xsize#
		y#=0
		AddTextToSurface(mySurface,letternumber+letteroffset*Len(myString$),let,x#+xoffset#,y#+yoffset#,z#,0.5*scaling#)
	Next

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

Function MyProcessFileNameTexture$(ex$)

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
	
	Return ex2$

End Function

Function MyLoadTexture(ex$,flag)

;	MyWriteString(debugfile,"Tex: "+ex$)

	exbackup$=ex$
	
	ex2$=MyProcessFileNameTexture$(ex$)
	
 
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

Function MyProcessFileNameModel$(ex$)

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
	
	Return ex2$

End Function

Function MyLoadMesh(ex$,parent)
;	MyWriteString(debugfile,"Mesh: "+ex$)

	ex2$=MyProcessFileNameModel$(ex$)
	
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
		ShowMessage("Couldn't Load Mesh:"+ex$,5000)
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

Function MyLoadSound(ex$)
;	MyWriteString(debugfile,"Sound: "+ex$)

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
	a=LoadSound(ex2$)
	If a=0
		jj=0
		Repeat
			jj=jj+1
		Until FileType("debug."+Str$(jj))=0
		
		debugfile=WriteFile ("debug."+Str$(jj))
	;	Print "Couldn't Load MD2:"+ex$
	;	Delay 5000
		WriteString debugfile,ex$
		WriteString debugfile,ex2$
		
		WriteInt debugfile,TotalVidMem()
		WriteInt debugfile,AvailVidMem()
	;	End
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
		AddLetter(let,-.97+(x+(i-1)*widthmult)*CharWidth#,.5-(y-4+j)*CharHeight#,1,0,.04,0,0,0,0,0,0,0,0,0,red,green,blue)
	Next
	
End Function

Function DisplayCenteredText2(mytext$,x#,y#,red,green,blue,widthmult#=1.0)

	DisplayText2(mytext$,x#-Len(mytext$)/2,y#,red,green,blue,widthmult#)

End Function

Function AbsoluteDifference(a,b)

	Return Abs(a-b)

End Function

Function MouseTextEntryTrackMouseMovement()

	If AbsoluteDifference(MouseX(),OldMouseX)>3 Or AbsoluteDifference(MouseY(),OldMouseY)>3
		ShowPointer
	EndIf

End Function

Function GetNextMouseTextEntryLine(ScreenId,y)

	Max=MouseTextEntryLineMax(ScreenId)
	i=Max
	While i>=0
		If y>=MouseTextEntryLineY(ScreenId,i)
			If i>=Max
				Return Max
			Else
				Return i+1
			EndIf
		EndIf
		i=i-1
	Wend
	Return Max

End Function

Function GetPreviousMouseTextEntryLine(ScreenId,y)

	Max=MouseTextEntryLineMax(ScreenId)
	i=1
	While i<=Max
		If y<=MouseTextEntryLineY(ScreenId,i)
			Return i-1
		EndIf
		i=i+1
	Wend
	Return 0

End Function

Function MouseTextEntryGetMoveX(x)

	Return LetterX(x)+LetterWidth/3 ;x*18+9

End Function

Function MouseTextEntryGetMoveY(y,yadjust)

	Return LetterHeight*4.8+y*LetterHeight+yadjust ;87+y*20+yadjust+10

End Function

Function MouseTextEntryMoveMouse(x,y,yadjust)

	newx=MouseTextEntryGetMoveX(x)
	newy=MouseTextEntryGetMoveY(y,yadjust)
	MoveMouse newx,newy
	OldMouseX=newx
	OldMouseY=newy
	HidePointer()

End Function

Function MouseTextEntry$(tex$,let,x,y,yadjust,ScreenId)

	If let>=32 And let<=122
		; place letter (let is the letter to place)
		tex$=Left$(tex$,x)+Chr$(let)+Right$(tex$,Len(tex$)-x)
		If Len(tex$)=39
			tex$=Left$(tex$,38)
		EndIf
		; and advance cursor
		If x<37
			MouseTextEntryMoveMouse(x+1,y,yadjust)
		EndIf
		ColEffect=-1
		TxtEffect=-1
		
		AddUnsavedChange()
	EndIf
	If CtrlDown() And MouseDown(1)
		; ctrl+click
		tex$=Left$(tex$,x)+InputString$("String to insert: ")+Right$(tex$,Len(tex$)-x)
		
		ColEffect=-1
		TxtEffect=-1
		
		AddUnsavedChange()
	EndIf
	If KeyDown(14)
		; backspace
		If x>0 
			tex$=Left$(tex$,x-1)+Right$(tex$,Len(tex$)-x)
			MouseTextEntryMoveMouse(x-1,y,yadjust)
			Delay CharacterDeleteDelay
		EndIf
		ColEffect=-1
		TxtEffect=-1
		
		AddUnsavedChange()
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
		
		AddUnsavedChange()
	EndIf
	
	; cursor movement
	If (KeyDown(200) Or KeyDown(72))
		; up arrow or numpad 8
		i=GetPreviousMouseTextEntryLine(ScreenId,y)
		MouseTextEntryMoveMouse(x,MouseTextEntryLineY(ScreenId,i),MouseTextEntryLineYAdjust(ScreenId,i))
		Delay 100
		ColEffect=-1
		TxtEffect=-1
	EndIf
	If (KeyDown(208) Or KeyDown(28) Or KeyDown(156)) And ReturnKeyReleased=True
		; down arrow or enter or numpad enter
		i=GetNextMouseTextEntryLine(ScreenId,y)
		MouseTextEntryMoveMouse(x,MouseTextEntryLineY(ScreenId,i),MouseTextEntryLineYAdjust(ScreenId,i))
		Delay 100
		ColEffect=-1
		TxtEffect=-1
	EndIf
	
	If (KeyDown(203)) And x>0
		; left arrow
		MouseTextEntryMoveMouse(x-1,y,yadjust)
		Delay 100
		ColEffect=-1
		TxtEffect=-1
	EndIf
	If (KeyDown(205)) And x<Len(tex$)
		; right arrow
		MouseTextEntryMoveMouse(x+1,y,yadjust)
		Delay 100
		ColEffect=-1
		TxtEffect=-1
	EndIf
	
	If KeyDown(199) ; home
		MouseTextEntryMoveMouse(0,y,yadjust)
		ColEffect=-1
		TxtEffect=-1
	EndIf
	
	If KeyDown(207) ; end
		endx=Len(tex$)
		If endx>37
			endx=37
		EndIf
		MouseTextEntryMoveMouse(endx,y,yadjust)
		ColEffect=-1
		TxtEffect=-1
	EndIf
	
	Return tex$

End Function

Function GetMouseLetterX()

	x=(MouseX()-LetterX(0))/LetterWidth-0.5
	If x<0
		x=0
	EndIf
	Return x

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
	
	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()
		
End Function

Function UserSelectScreen()

	leveltimer=leveltimer+1
	
	my=MouseY()
	StartY=LetterHeight*15
	If mY>=StartY
		EditorUserNameSelected=(my-StartY-LetterHeight*0.5)/LetterHeight
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
	If (KeyPressed(28) Or KeyPressed(156)) And ReturnKeyReleased=True
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
	
	FinishDrawing()
	
	If waitflag=True Delay 2000


End Function

Function SetAdventureFileNamesListedStart(Target)

	AdventureFileNamesListedStart=Target	

	If AdventureFileNamesListedStart>NofAdventureFileNames-19 Then AdventureFileNamesListedStart=NofAdventureFileNames-19
	If AdventureFileNamesListedStart<0 Then AdventureFileNamesListedStart=0

End Function

Function AdventureFileNamesListPageUp()
	AdventureFileNamesListedStart=AdventureFileNamesListedStart-19
	If AdventureFileNamesListedStart<0 Then AdventureFileNamesListedStart=0
End Function

Function AdventureFileNamesListPageDown()
	AdventureFileNamesListedStart=AdventureFileNamesListedStart+19
	If AdventureFileNamesListedStart>NofAdventureFileNames-19 Then AdventureFileNamesListedStart=NofAdventureFileNames-19
	If AdventureFileNamesListedStart<0 Then AdventureFileNamesListedStart=0
End Function

Function StartAdventureSelectScreen()

	SetEditorMode(5)
	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()
	
	
	GetAdventures()
	
	
	AdventureNameEntered$=""
	


End Function

Function AdventureSelectScreen()

	leveltimer=leveltimer+1

	mx=MouseX()
	my=MouseY()
	If mY>=LetterHeight*8 And my<LetterHeight*28 And mx>LetterX(2.5) And mx<LetterX(41.5)
		AdventureNameSelected=(my-LetterHeight*8.5)/LetterHeight
	Else
		AdventureNameSelected=-1
	EndIf


	If EditorMode=5
		DisplayText2(Versiontext$,0,0,TextMenusR,TextMenusG,TextMenusB)
		
		;DisplayText2("================================",0,1,TextMenusR,TextMenusG,TextMenusB)
		;DisplayText2("            ====================",0,1,TextMenusR,TextMenusG,TextMenusB)
		;DisplayText2("            ================================",0,1,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2("            ======================",0,1,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("                                  (Settings)",0,1,255,255,255)
		
	;	If displayfullscreen=True
	;		DisplayText2("                                (FullScreen)",0,1,255,255,255)
	;	Else
	;		DisplayText2("                                ( Windowed )",0,1,255,255,255)
	;	EndIf
		;hubmode=True
		If hubmode=True
			DisplayText2("(   Hubs   )",0,1,255,255,255)
			DisplayText2("Enter New Hub Filename (e.g. 'TestHub12345')",0,3,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("(Adventures)",0,1,255,255,255)
			DisplayText2("Enter New Adventure Filename (e.g. 'Test34')",0,3,TextMenusR,TextMenusG,TextMenusB)
		EndIf
		
		DisplayText2("Or Select Existing To Edit:                 ",0,6,TextMenusR,TextMenusG,TextMenusB)
		
		
		DisplayText2("User:",0,28,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(EditorUserName$,6,28,255,255,255)
		DisplayText2("(CHANGE)",36,28,TextMenusR,TextMenusG,TextMenusB)
	ElseIf EditorMode=12
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
		
		DisplayText2("Or Select Existing To Add:                 ",0,6,TextMenusR,TextMenusG,TextMenusB)
		
		DisplayText2("(BACK)",0,28,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	
	DisplayText2("============================================",0,27,TextMenusR,TextMenusG,TextMenusB)
	
	
	If leveltimer Mod 100 <50
		DisplayText2(":",Len(AdventureNameEntered$),4,255,255,255)
	EndIf
	DisplayText2(AdventureNameEntered$,0,4,255,255,255)
	
	DisplayText2("============================================",0,7,TextMenusR,TextMenusG,TextMenusB)
	
;	If hubmode=False
;		If AdventureCurrentArchive=0
;			DisplayText2("Current",28,6,255,255,255)
;			DisplayText2("Archive",37,6,155,155,155)
;		Else
;			DisplayText2("Current",28,6,155,155,155)
;			DisplayText2("Archive",37,6,255,255,255)
;		EndIf
;		DisplayText2("/",35.5,5.9,TextMenusR,TextMenusG,TextMenusB)

		Select AdventureCurrentArchive
		Case AdventureCurrentArchiveArchive
			TheString$="Archive"
		Case AdventureCurrentArchivePlayer
			TheString$="Player"
		Case AdventureCurrentArchiveDataAdventures
			TheString$="Data/Adventures"
		Default
			TheString$="Current"
		End Select
		DisplayText2(TheString$,28,6,255,255,255)
;	EndIf
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
	If (KeyPressed(28) Or KeyPressed(156)) And ReturnKeyReleased=True
		; Enter
		If hubmode And EditorMode=5
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
			Else If FileType(GetAdventuresDir$(AdventureCurrentArchiveCurrent)+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Current!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GetAdventuresDir$(AdventureCurrentArchiveArchive)+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Archive!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GetAdventuresDir$(AdventureCurrentArchivePlayer)+EditorUserName$+"#"+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Player!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GetAdventuresDir$(AdventureCurrentArchiveDataAdventures)+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Data\Adventures!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else
				DisplayText2("--> STARTING MAIN EDITOR - Please Wait!",0,5,TextMenusR,TextMenusG,TextMenusB)
				
				AdventureCurrentArchive=AdventureCurrentArchiveCurrent ; Set to current.
				
				CreateDir GetAdventuresDir$(AdventureCurrentArchiveCurrent)+AdventureNameEntered$
	
				AdventureFileName$=AdventureNameEntered$
				
				GetAdventures()
				
				For i=0 To NofAdventureFileNames-1	
					If AdventureFileName$=AdventureFileNamesListed$(i)
						ThisEditorMode=EditorMode
					
						AdventureNameSelected=i	
						Repeat
						Until MouseDown(1)=0
						StartMaster()
						
						If ThisEditorMode=12
							HubAdventuresFilenames$(HubSelectedAdventure)=AdventureCurrentArchiveToHubPrefix$()+AdventureFileNamesListed$(AdventureNameSelected)
							HubAdventuresMissing(HubSelectedAdventure)=False
							If HubSelectedAdventure>HubTotalAdventures
								HubTotalAdventures=HubSelectedAdventure
							EndIf
						EndIf
					EndIf
				Next
			EndIf
		EndIf
		waitflag=True
	EndIf
	
	
	
;	If hubmode=False
		If my>LetterHeight*6 And my<LetterHeight*7 And mx>LetterX(28) And mx<LetterX(44) And MouseDebounceFinished()
			If MouseDown(1) Or MouseScroll>0
				SetAdventureCurrentArchive(AdventureCurrentArchive+1)
				GetAdventures()
				If MouseScroll=0 Then MouseDebounceSet(10)
			EndIf
			If MouseDown(2) Or MouseScroll<0
				SetAdventureCurrentArchive(AdventureCurrentArchive-1)
				GetAdventures()
				If MouseScroll=0 Then MouseDebounceSet(10)
			EndIf
		EndIf
		;If my>LetterHeight*6 And my<LetterHeight*7 And mx>LetterX(36) And AdventureCurrentArchive=0
		;	AdventureCurrentArchive=1
		;	GetAdventures()
		;EndIf
;	EndIf
	
	
	If MouseDown(1)
		If EditorMode=5
			If mx>LetterX(36) And my>LetterHeight*28
				; change user
				StartUserSelectScreen()
				Repeat
				Until MouseDown(1)=0
			EndIf
			
			If mx>LetterX(34) And my<LetterHeight*2
				; switch window/fullscreen
	;			DisplayFullScreen = Not DisplayFullScreen
	;			filed=WriteFile (globaldirname$+"\display-ed.wdf")
	;			If filed>0
	;			
	;				WriteInt filed,DisplayFullScreen
	;				CloseFile filed
	;			EndIf
	;			
	;			; and restart
	;			Cls
	;			Flip
	;			Print "Note: Screenmode will be switched upon next restart."
	;			Delay 4000
				
				;ShowMessage("Here we go!",1000)
				SetEditorMode(13)
				Repeat
				Until MouseDown(1)=0
				;ShowMessage("We're here!",1000)
				
			EndIf

			If mx<LetterX(12) And my>LetterHeight And my<LetterHeight*2 ;hubmode
				hubmode=Not hubmode
				If hubmode
					GetHubs()
				Else
					GetAdventures()
				EndIf
				Repeat
				Until MouseDown(1)=0
			EndIf
			
			If AdventureNameSelected>=0
				Repeat
				Until MouseDown(1)=0
				SetEditorMode(6)
			EndIf
		ElseIf EditorMode=12
			If mx<LetterX(7) And my>LetterHeight*28
				; go back to hub menu
				SetEditorMode(11)
				If HubAdventuresFilenames$(HubSelectedAdventure)=""
					HubSelectedAdventure=-1
				EndIf
				Repeat
				Until MouseDown(1)=0
			EndIf
			
			If AdventureNameSelected>=0
				Repeat
				Until MouseDown(1)=0
				SetEditorMode(11)
				HubAdventuresFilenames$(HubSelectedAdventure)=AdventureCurrentArchiveToHubPrefix$()+AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
				HubAdventuresMissing(HubSelectedAdventure)=False
				If HubSelectedAdventure>HubTotalAdventures
					HubTotalAdventures=HubSelectedAdventure
				EndIf
			EndIf
		EndIf
		
		

	EndIf

	If my>LetterHeight*8 And my<LetterHeight*28
		If MouseScroll<>0
			Speed=1
			If ShiftDown()
				Speed=10
			EndIf
			SetAdventureFileNamesListedStart(AdventureFileNamesListedStart-MouseScroll*Speed)
		EndIf
		If (mx<LetterX(2.5) Or mx>LetterX(41.5)) And MouseDown(1)
			If my>LetterHeight*8 And my<LetterHeight*12
				; Page Up
				AdventureFileNamesListPageUp()
				Repeat
				Until MouseDown(1)=0
			ElseIf my>LetterHeight*24 And my<LetterHeight*28
				; Page Down
				AdventureFileNamesListPageDown()
				Repeat
				Until MouseDown(1)=0
			EndIf
		EndIf
	EndIf
	
	If KeyPressed(201) ; page up
		AdventureFileNamesListPageUp()
	EndIf
	If KeyPressed(209) ; page down
		AdventureFileNamesListPageDown()
	EndIf
		
	
	RenderLetters()
	UpdateWorld 
	RenderWorld
	
	FinishDrawing()
	If waitflag=True Delay 2000
	

	
End Function
Function AdventureSelectScreen2()

	mx=MouseX()
	my=MouseY()
	StartY=LetterHeight*9
	If mx>LetterX(15) And mx<LetterX(29) And my>StartY And my<StartY+LetterHeight*8
		Selected=(my-StartY-LetterHeight*0.5)/(LetterHeight*2)
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

	If AdventureCurrentArchive=AdventureCurrentArchiveCurrent Or AdventureCurrentArchive=AdventureCurrentArchiveDataAdventures
		If Selected=0
			DisplayText2("EDIT",20,9,255,255,255)
		Else
			DisplayText2("EDIT",20,9,155,155,155)
		EndIf
	EndIf
	If hubmode=False
		If AdventureCurrentArchive=AdventureCurrentArchiveCurrent
			If Selected=1 
				DisplayText2("MOVE TO ARCHIVE",14.5,11,255,255,255)
			Else
				DisplayText2("MOVE TO ARCHIVE",14.5,11,155,155,155)
			EndIf
		ElseIf AdventureCurrentArchive=AdventureCurrentArchiveArchive
			If Selected=1
				DisplayText2("MOVE TO CURRENT",14.5,11,255,255,255)
			Else
				DisplayText2("MOVE TO CURRENT",14.5,11,155,155,155)
			EndIf
		Else
			If Selected=1
				DisplayText2("COPY TO CURRENT",14.5,11,255,255,255)
			Else
				DisplayText2("COPY TO CURRENT",14.5,11,155,155,155)
			EndIf
		EndIf
	EndIf
	
	If AdventureCurrentArchive=AdventureCurrentArchiveCurrent
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

	If MouseDown(1) And Selected<>-1
		Repeat
		Until MouseDown(1)=0
		
		; Check again to make sure the mouse is still on the button.
		OldSelected=Selected
		
		mx=MouseX()
		my=MouseY()
		
		If mx>LetterX(15) And mx<LetterX(29) And my>StartY And my<StartY+LetterHeight*8
			Selected=(my-StartY-LetterHeight*0.5)/(LetterHeight*2)
		Else 
			Selected=-1
		EndIf
		
		If Selected=OldSelected
			If selected=0 And AdventureCurrentArchive=AdventureCurrentArchiveCurrent Or AdventureCurrentArchive=AdventureCurrentArchiveDataAdventures
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
				
				FromDir$=GetAdventureDir$()
				If AdventureCurrentArchive=AdventureCurrentArchiveCurrent
					ToDir$=GetAdventuresDir$(AdventureCurrentArchiveArchive)+ex$
				Else
					ToDir$=GetAdventuresDir$(AdventureCurrentArchiveCurrent)+ex$
				EndIf
				
				CreateDir ToDir$
				dirfile=ReadDir(FromDir$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						CopyFile FromDir$+ex2$,ToDir$+"\"+ex2$
						
						If AdventureCurrentArchive=AdventureCurrentArchiveCurrent Or AdventureCurrentArchive=AdventureCurrentArchiveArchive
							DeleteFile FromDir$+"\"+ex2$
						EndIf
					EndIf
				Until ex2$=""
				CloseDir dirfile
				
				If AdventureCurrentArchive=AdventureCurrentArchiveCurrent Or AdventureCurrentArchive=AdventureCurrentArchiveArchive
					DeleteDir FromDir$
				EndIf
				
				GetAdventures()
				
				SetEditorMode(5)
			EndIf
			If selected=2 And AdventureCurrentArchive=AdventureCurrentArchiveCurrent
				SetEditorMode(7)
			EndIf
	
			If selected=3
				SetEditorMode(5)
			EndIf
		EndIf
	EndIf
			
	RenderLetters()
	UpdateWorld 
	RenderWorld
	
	FinishDrawing()
	If waitflag=True Delay 2000

	
End Function

Function AdventureSelectScreen3()

	MX=MouseX()
	my=MouseY()
	StartY=LetterHeight*9
	If mx>LetterX(15) And mx<LetterX(29) And my>StartY And my<StartY+LetterHeight*8
		Selected=(my-StartY-LetterHeight*0.5)/(LetterHeight*2)
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
			Else
				TheDir$=GetAdventuresDir$(AdventureCurrentArchive)+ex$
				dirfile=ReadDir(TheDir$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						DeleteFile TheDir$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile
				DeleteDir TheDir$
				GetAdventures()
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
	
	FinishDrawing()
	If waitflag=True Delay 2000

	
End Function

Function SetAdventureCurrentArchive(NewValue)

	AdventureCurrentArchive=NewValue
	If AdventureCurrentArchive<0
		AdventureCurrentArchive=MaxAdventureCurrentArchive
	ElseIf AdventureCurrentArchive>MaxAdventureCurrentArchive
		AdventureCurrentArchive=0
	EndIf

End Function

Function SettingsMainLoop()
	MX=MouseX()
	my=MouseY()
	StartY=LetterHeight*9
	If mx>LetterX(15) And mx<LetterX(29) And my>StartY And my<StartY+LetterHeight*8
		Selected=(my-StartY-LetterHeight*0.5)/(LetterHeight*2)
	Else 
		Selected=-1
	EndIf


	DisplayText2("Settings",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("============================================",0,1,TextMenusR,TextMenusG,TextMenusB)
	
	If Selected=0
		DisplayCenteredText2("Controls",22,9,255,255,255)
	Else
		DisplayCenteredText2("Controls",22,9,155,155,155)
	EndIf
	ex$=Str$(MyGfxModeWidth(GfxMode))+"x"+Str$(MyGfxModeHeight(GfxMode))+"x"+Str$(MyGfxModeDepth(GfxMode))
	If Selected=1
		DisplayCenteredText2("Resolution "+ex$,22,11,255,255,255)
	Else
		DisplayCenteredText2("Resolution "+ex$,22,11,155,155,155)
	EndIf
	If Selected=2
		DisplayCenteredText2("Apply Resolution",22,13,255,255,255)
	Else
		DisplayCenteredText2("Apply Resolution",22,13,155,155,155)
	EndIf
	If Selected=3
		DisplayText2("Back",20,15,255,255,255)
	Else
		DisplayText2("Back",20,15,155,155,155)
	EndIf
	
	If MouseDown(1)
		If selected=0			
			ConfigureControls()
			
			Repeat
			Until MouseDown(1)=0
		EndIf
		If selected=1
			GfxMode=GfxMode+1
			If GfxMode=NofMyGfxModes Then GfxMode=0
			
			Repeat
			Until MouseDown(1)=0
		EndIf
		If selected=2
			GfxWidth=MyGfxModeWidth(gfxmode)
			GfxHeight=MyGfxModeHeight(gfxmode)
			GfxDepth=MyGfxModeDepth(gfxmode)
			
			;Graphics3D GfxWidth,GfxHeight,GfxDepth,GfxWindowed
			;ResolutionWasChanged()
			
			WriteDisplayFile()
			
			Repeat
			Until MouseDown(1)=0
			
			ExecFile ("editor3d.exe")
			EndApplication()
			
		EndIf
		If selected=3
			SetEditorMode(5)
			
			Repeat
			Until MouseDown(1)=0
		EndIf
	EndIf
	If MouseDown(2)
		If selected=1
			GfxMode=GfxMode-1
			If GfxMode=-1 Then GfxMode=NofMyGfxModes-1
			
			Repeat
			Until MouseDown(2)=0
		EndIf
	EndIf
	
	RenderLetters()
	UpdateWorld 
	RenderWorld
	
	FinishDrawing()

End Function


Function GetAdventures()

	NofAdventureFileNames=0
	AdventureFileNamesListedStart=0
	
	TheDir$=GetAdventuresDir$(AdventureCurrentArchive)
	dirfile=ReadDir(TheDir$)
	
	Repeat
		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(TheDir$+ex$)=2
			; check if there's a hash or name is too long
			flag=True
			
			;For i=1 To Len(ex$)
			;	If Mid$(ex$,i,1)="#" Then flag=False
			;Next
			;If Len(ex$)>38 Then flag=False
			
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
	AdventureCurrentArchive=AdventureCurrentArchiveCurrent
	
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

	If FileType(GetAdventureDir$()+Str$(levelnumber)+".wlv")=1
		Return True
	Else
		Return False
	EndIf

End Function

Function DialogExists(dialognumber)

	If FileType(GetAdventureDir$()+Str$(dialognumber)+".dia")=1
		Return True
	Else
		Return False
	EndIf

End Function

Function AdventureTitleWithoutAuthor$(ex$)

	exWithoutUsername$=""
	FoundHash=False
	For i=1 To Len(ex$)
		TheChar$=Mid$(ex$,i,1)
		If FoundHash
			exWithoutUsername$=exWithoutUsername$+TheChar$
		ElseIf TheChar$="#"
			FoundHash=True
		EndIf
	Next
	
	If FoundHash=False
		Return ex$
	Else
		Return exWithoutUsername$
	EndIf

End Function

Function GetAuthorFromAdventureTitle$(ex$)

	TheUsername$=""
	FoundHash=False
	For i=1 To Len(ex$)
		TheChar$=Mid$(ex$,i,1)	
		If TheChar$="#"
			FoundHash=True
		ElseIf Not FoundHash
			TheUsername$=TheUsername$+TheChar$
		EndIf
	Next
	
	If FoundHash=False
		Return EditorUserName$
	Else
		Return TheUsername$
	EndIf

End Function

Function StartMaster()
	RestoreOriginalMaster()
	RestoreOriginal1Wlv()
	
	StopMusic()
	
	AdventureUserName$=GetAuthorFromAdventureTitle$(AdventureFileName$)
	
	ResetPreviousLevelNumberBuffer()
	
	PreviewCurrentDialog=0
	
	SetEditorMode(8)
	
	CopyingLevel=StateNotSpecial
	CopyingDialog=StateNotSpecial

	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()
	
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
	If FileType(GetAdventureDir$()+"master.dat")=1
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
		AdventureStartDir=180 ;0
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
	CopyingLevel=StateNotSpecial
	If FileType(globaldirname$+"\Custom\editing\Hubs\"+HubFileName$+"\hub.dat")=1
		LoadHubFile()
	Else
		HubTitle$=""
		HubDescription$=""
		HubTotalAdventures=0
	EndIf
	
	For i=0 To HubAdvMax
		HubAdventuresIncludeInTotals(i)=True
	Next
End Function

Function ResumeMaster()

	RestoreOriginalMaster()
	RestoreOriginal1Wlv()
	
	StopMusic()

	SetEditorMode(8)
	
	CopyingLevel=StateNotSpecial
	CopyingDialog=StateNotSpecial
	
	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()

	
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
	
	If CtrlDown() And KeyDown(20) ; Ctrl+T
		StartTestMode()
	EndIf
	
	If HotkeySave()
		SaveMasterFile()
	EndIf
	
	dialogtimer=dialogtimer+1
	adj=1
	If KeyDown(42) Or KeyDown(54) Then adj=10
	
	DisplayText2("Adventure File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(AdventureFileName$,0,1,255,255,255)
	If MouseY()<LetterHeight And MouseX()>LetterX(24) And MouseX()<LetterX(38) ; x: 430 to 700
		DisplayText2("                        (Adv. Options)",0,0,255,255,255)
	Else
		DisplayText2("                        (Adv. Options)",0,0,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	
	WlvColumnLeft=LetterX(38.5)
	
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
		
		If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5) And MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4 ; X: 700 to 750
			DisplayText2("-",39.5,3,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("-",39.5,3,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf 
		If MouseX()>LetterX(41.5) And MouseX()<GfxWidth And MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4
			DisplayText2("-",42.5,3,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("-",42.5,3,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf
		
		
		DigitSpaceMult#=0.8
		For i=1 To 20
			flag=False
			If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5) And MouseY()>LetterHeight*3+i*LetterHeight And MouseY()<=LetterHeight*4+i*LetterHeight
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
			Else If CopyingLevel=StateCopying And CopiedLevel=i+MasterLevelListStart
				r=0
				g=255
				b=255
			Else If CopyingLevel=StateSwapping And CopiedLevel=i+MasterLevelListStart
				r=255
				g=0
				b=255
			Else
				r=210
				g=210
				b=210
			EndIf
			DisplayText2(ex$,38.7,3+i,r,g,b,DigitSpaceMult) ; previously, x=39
			
			flag=False
			If MouseX()>LetterX(41.5) And MouseX()<GfxWidth And MouseY()>LetterHeight*3+i*LetterHeight And MouseY()<=LetterHeight*4+i*LetterHeight
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
			Else If CopyingDialog=StateCopying And CopiedDialog=i+MasterDialogListStart
				r=0
				g=255
				b=255
			Else If CopyingDialog=StateSwapping And CopiedDialog=i+MasterDialogListStart
				r=255
				g=0
				b=255
			Else
				r=210
				g=210
				b=210
			EndIf
			DisplayText2(ex$,41.8,3+i,r,g,b,DigitSpaceMult) ; previously, x=41.5
	
		Next
		
		If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5) And MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25
			DisplayText2("+",39.5,24,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("+",39.5,24,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf 
		If MouseX()>LetterX(41.5) And MouseX()<GfxWidth And MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25
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
		;displaytext2(Str$((adventurestartdir+180) Mod 360),20,14,255,255,255)
		displaytext2(Str$(adventurestartdir),20,14,255,255,255)

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
		
	

	
	If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	
	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
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
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33)
			DisplayText2("COMPILE",35,27,255,255,255)
			DisplayText2("+EXIT",36,28,255,255,255)
		Else
			DisplayText2("COMPILE",35,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("+EXIT",36,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		EndIf
	EndIf
	
	AdventureExitButtonsMinX=LetterX(24)
	AdventureExitButtonsMaxX=LetterX(38)
	AdventureExitButtonsStartY=LetterHeight*20
	AdventureExitButtonsGapY=LetterHeight ;20
	If MouseX()>AdventureExitButtonsMinX And MouseX()<AdventureExitButtonsMaxX And MouseY()>AdventureExitButtonsStartY And MouseY()<AdventureExitButtonsStartY+AdventureExitButtonsGapY
		r=255
		g=255
		b=255
		
		If MouseDown(1)
			adventureexitwonlevel=adventureexitlostlevel
			adventureexitwonx=adventureexitlostx
			adventureexitwony=adventureexitlosty
		EndIf
	Else
		r=TextMenuButtonR
		g=TextMenuButtonG
		b=TextMenuButtonB
	EndIf
	DisplayText2("Set To LostExt",24,20,r,g,b)
	
	If MouseX()>AdventureExitButtonsMinX And MouseX()<AdventureExitButtonsMaxX And MouseY()>AdventureExitButtonsStartY+AdventureExitButtonsGapY And MouseY()<AdventureExitButtonsStartY+AdventureExitButtonsGapY*2
		r=255
		g=255
		b=255
		
		If MouseDown(1)
			adventureexitlostlevel=adventureexitwonlevel
			adventureexitlostx=adventureexitwonx
			adventureexitlosty=adventureexitwony
		EndIf
	Else
		r=TextMenuButtonR
		g=TextMenuButtonG
		b=TextMenuButtonB
	EndIf
	DisplayText2("Set To WonExit",24,21,r,g,b)

		
		; Mouse
		MouseTextEntryTrackMouseMovement()
		; Mouse Pos
		Entering=0
		
		x=GetMouseLetterX()
		y=(MouseY()-LetterHeight*5)/LetterHeight
	
		debug1=MouseY()
		debug2=y
		
		; cursor
		If x<38 And MouseY()>=LetterHeight*4 And y>2 And y<8 
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
			
			tex$=MouseTextEntry$(tex$,let,x,y,0,0)
			
			Select entering
			Case 1
				AdventureTextline$(y-3)=tex$
			Case 2
				AdventureTitle$=tex$
		;	Case 3
		;		AdventureHelpLine$(y-10)=tex$
				
			End Select
	
	
		EndIf
		

	EndIf
	
	mb=0
	If MouseDown(1) mb=1
	If MouseDown(2) mb=2
	If MouseDown(3) mb=3
	
	DelayTime=10
	
	; level list start
	If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5)
		If MouseDebounceFinished()
			If (MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25 And mb>0) Or MouseScroll<0
				MasterLevelListStart=MasterLevelListStart+adj
				If MasterLevelListStart>MaxLevel-20 Then MasterLevelListStart=MaxLevel-20
				If MouseScroll=0 Then MouseDebounceSet(DelayTime)
			Else If (MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4 And mb>0) Or MouseScroll>0
				MasterLevelListStart=MasterLevelListStart-adj
				If MasterLevelListStart<0 Then MasterLevelListStart=0
				If MouseScroll=0 Then MouseDebounceSet(DelayTime)
			EndIf
		EndIf
		If KeyPressed(201) ; page up
			MasterLevelListStart=MasterLevelListStart-20
			If MasterLevelListStart<0 Then MasterLevelListStart=0
		EndIf
		If KeyPressed(209) ; page down
			MasterLevelListStart=MasterLevelListStart+20
			If MasterLevelListStart>MaxLevel-20 Then MasterLevelListStart=MaxLevel-20
		EndIf
	EndIf
	
	; dialog list start
	If MouseX()>LetterX(41.5) And MouseX()<GfxWidth
		If MouseDebounceFinished()
			If (MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25 And mb>0) Or MouseScroll<0
				MasterDialogListStart=MasterDialogListStart+adj
				If MasterDialogListStart>MaxDialog-20 Then MasterDialogListStart=MaxDialog-20
				If MouseScroll=0 Then MouseDebounceSet(DelayTime)
			Else If (MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4 And mb>0) Or MouseScroll>0
				MasterDialogListStart=MasterDialogListStart-adj
				If MasterDialogListStart<0 Then MasterDialogListStart=0
				If MouseScroll=0 Then MouseDebounceSet(DelayTime)
			EndIf
		EndIf
		If KeyPressed(201) ; page up
			MasterDialogListStart=MasterDialogListStart-20
			If MasterDialogListStart<0 Then MasterDialogListStart=0
		EndIf
		If KeyPressed(209) ; page down
			MasterDialogListStart=MasterDialogListStart+20
			If MasterDialogListStart>MaxDialog-20 Then MasterDialogListStart=MaxDialog-20
		EndIf
	EndIf
	
	
	If mb>0
	
		;new advanced mode 2019
		If MouseY()<LetterHeight And  MouseX()>LetterX(24) And MouseX()<WlvColumnLeft
			SetEditorMode(10)
			Repeat
			Until MouseDown(1)=0 And MouseDown(2)=0
		EndIf
	
	EndIf

	; Change Adventure

	; change textures

	; startpos
	If MouseY()>LetterHeight*14 And MouseY()<LetterHeight*15
		If MouseX()>LetterX(0) And MouseX()<LetterX(7)
			adventurestartx=AdjustInt("Adventure start X: ", adventurestartx, 1, 10, DelayTime)
		EndIf
		If MouseX()>LetterX(8) And MouseX()<LetterX(15)
			adventurestarty=AdjustInt("Adventure start Y: ", adventurestarty, 1, 10, DelayTime)
		EndIf
		If MouseX()>LetterX(16) And MouseX()<LetterX(23)
			adventurestartdir=AdjustInt("Adventure start direction: ", adventurestartdir, 45, 45, DelayTime)
			adventurestartdir=adventurestartdir Mod 360
			; this if block is necessary because Mod can return negative numbers for some terrible reason
			If adventurestartdir<0
				adventurestartdir=adventurestartdir+360
			EndIf
		EndIf
	EndIf
		
		
			

	; PUT BACK IN FOR ME
	If MASTERUSER=True
		; change hub data
		For i=0 To 5
			For j=0 To 4
				XMin=LetterX(7.5+i*5)
				XMax=LetterX(12.5+i*5)
				YMin=LetterHeight*18+(j+2)*LetterHeight
				YMax=LetterHeight*19+(j+2)*LetterHeight
				TooltipX=XMin+LetterWidth*2.5
				TooltipY=YMin
				If MouseX()>XMin And MouseX()<XMax And MouseY()>YMin And MouseY()<YMax
					If j=0 And i=0
						Adventureexitwonlevel=AdjustInt("Adventure exit won level: ", Adventureexitwonlevel, 1, 10, DelayTime)
					EndIf
					If j=0 And i=1
						Adventureexitwonx=AdjustInt("Adventure exit won X: ", Adventureexitwonx, 1, 10, DelayTime)
					EndIf
					If j=0 And i=2
						Adventureexitwony=AdjustInt("Adventure exit won Y: ", Adventureexitwony, 1, 10, DelayTime)
					EndIf
					If j=1 And i=0
						Adventureexitlostlevel=AdjustInt("Adventure exit lost level: ", Adventureexitlostlevel, 1, 10, DelayTime)
					EndIf
					If j=1 And i=1
						Adventureexitlostx=AdjustInt("Adventure exit lost X: ", Adventureexitlostx, 1, 10, DelayTime)
					EndIf
					If j=1 And i=2
						Adventureexitlosty=AdjustInt("Adventure exit lost Y: ", Adventureexitlosty, 1, 10, DelayTime)
					EndIf
					If j=2 Or j=3 Or j=4
						Select i
						Case 0
							cmdbit$="level"
						Case 1
							cmdbit$="command"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCommandName$(AdventureWonCommand(j-2,1)))
						Case 2
							cmdbit$="Data1"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCMDData1NameAndValue$(AdventureWonCommand(j-2,1),AdventureWonCommand(j-2,2),": "))
						Case 3
							cmdbit$="Data2"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCMDData2NameAndValue$(AdventureWonCommand(j-2,1),AdventureWonCommand(j-2,3),": "))
						Case 4
							cmdbit$="Data3"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCMDData3NameAndValue$(AdventureWonCommand(j-2,1),AdventureWonCommand(j-2,3),AdventureWonCommand(j-2,4),": "))
						Case 5
							cmdbit$="Data4"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCMDData4NameAndValue$(AdventureWonCommand(j-2,1),AdventureWonCommand(j-2,5),": "))
						End Select
						Adventurewoncommand(j-2,i)=AdjustInt("Adventure won command "+cmdbit$+": ", Adventurewoncommand(j-2,i), 1, 10, DelayTime)
					EndIf
				EndIf
				
			Next
		Next
	
	
	
	
	EndIf
	
	
	; adventure goal
	If MouseY()>LetterHeight*17 And MouseY()<LetterHeight*18 And MouseX()<LetterX(25)
		AdventureGoal=AdjustInt("Adventure goal: ", AdventureGoal, 1, 10, DelayTime)
		If AdventureGoal<=-1 Then AdventureGoal=nofwinningconditions-1
		If AdventureGoal>=Nofwinningconditions Then adventuregoal=0
	EndIf
	
	; GateKeyVersion
	If MouseY()>LetterHeight*14 And MouseY()<LetterHeight*15 And MouseX()>LetterX(26) And MouseX()<LetterX(38)
		GateKeyVersion=AdjustInt("Gate/key version: ", GateKeyVersion, 1, 10, DelayTime)
		If GateKeyVersion<=0 Then GateKeyVersion=3
		If GateKeyVersion>=4 Then GateKeyVersion=1
		FreeTexture buttontexture
		FreeTexture gatetexture
		UpdateButtonGateTexture()
	EndIf


	
		
	; custom icon
	If mb>0 And MouseY()>LetterHeight*17 And MouseY()<LetterHeight*18 And MouseX()>LetterX(26) And MouseX()<LetterX(38)
		
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

	
	
	If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5) And MouseY()<LowerButtonsCutoff
		If CtrlDown() And mb>0
			If OpenTypedLevel()
				StartEditorMainLoop()
			EndIf
		Else
			For i=1 To 20
				If MouseY()>LetterHeight*3+i*LetterHeight And MouseY()<=LetterHeight*4+i*LetterHeight
					SelectedLevel=i+MasterLevelListStart
					If mb=1
						If CopyingLevel=StateCopying And LevelExists(SelectedLevel)=False
							; copy from CopiedLevel
							dirbase$=GetAdventureDir$()
							CopyFile(dirbase$+CopiedLevel+".wlv",dirbase$+SelectedLevel+".wlv")
							MasterLevelList(SelectedLevel)=1
							
							CopyingLevel=StateNotSpecial
						ElseIf CopyingLevel=StateSwapping
							SwapLevel(CopiedLevel,SelectedLevel)
							CopyingLevel=StateNotSpecial
						Else
							AccessLevelAtCenter(SelectedLevel)
							StartEditorMainLoop()
						EndIf
						
						Repeat
						Until MouseDown(1)=0
						
						mb=0
						Exit
					ElseIf mb=2 And LevelExists(SelectedLevel)=True
						If CopyingLevel=StateCopying And SelectedLevel=CopiedLevel
							CopyingLevel=StateNotSpecial
						Else
							CopyingLevel=StateCopying
							CopiedLevel=SelectedLevel
						EndIf
						
						Repeat
						Until MouseDown(2)=0
						
						mb=0
						Exit
					ElseIf mb=3 And LevelExists(SelectedLevel)=True
						If CopyingLevel=StateSwapping And SelectedLevel=CopiedLevel
							CopyingLevel=StateNotSpecial
						Else
							CopyingLevel=StateSwapping
							CopiedLevel=SelectedLevel
						EndIf
						
						Repeat
						Until MouseDown(3)=0
						
						mb=0
						Exit
					EndIf
				EndIf
			Next
		EndIf
	EndIf
	
	; load dialog
	StartX=LetterX(41.5)
	If MouseX()>StartX And MouseX()<GfxWidth And MouseY()<LowerButtonsCutoff
		If (CtrlDown() And mb>0) Or HotkeyOpen()
			If OpenTypedDialog()
				StartDialog()
			EndIf
		Else
			For i=1 To 20
				StartY=LetterHeight*3+i*LetterHeight
				If MouseY()>StartY And MouseY()<=StartY+LetterHeight
					SelectedDialog = i+MasterDialogListStart
					ShowTooltipRightAligned(StartX,StartY+LetterHeight*2,PreviewDialog$(SelectedDialog,0))
					
					If mb=1
						If CopyingDialog=StateCopying And DialogExists(SelectedDialog)=False
							; copy from CopiedDialog
							dirbase$=GetAdventureDir$()
							CopyFile(dirbase$+CopiedDialog+".dia",dirbase$+SelectedDialog+".dia")
							MasterDialogList(SelectedDialog)=1
							
							CopyingDialog=StateNotSpecial
						ElseIf CopyingDialog=StateSwapping
							SwapDialog(CopiedDialog,SelectedDialog)
							CopyingDialog=StateNotSpecial
						Else
							AccessDialog(SelectedDialog)
							StartDialog()
						EndIf
						
						Repeat
						Until MouseDown(1)=0
						
						mb=0
						Exit
					ElseIf mb=2 And DialogExists(SelectedDialog)=True
						If CopyingDialog=StateCopying And SelectedDialog=CopiedDialog
							CopyingDialog=StateNotSpecial
						Else
							CopyingDialog=StateCopying
							CopiedDialog=SelectedDialog
						EndIf
						
						Repeat
						Until MouseDown(2)=0
						
						mb=0
						Exit
					ElseIf mb=3 And DialogExists(SelectedDialog)=True
						If CopyingDialog=StateSwapping And SelectedDialog=CopiedDialog
							CopyingDialog=StateNotSpecial
						Else
							CopyingDialog=StateSwapping
							CopiedDialog=SelectedDialog
						EndIf
						
						Repeat
						Until MouseDown(3)=0
						
						mb=0
						Exit
					EndIf
				EndIf
			Next
		EndIf
	Else
		; load level
		If HotkeyOpen()
			If OpenTypedLevel()
				StartEditorMainLoop()
			EndIf
		EndIf
	EndIf

		
		
	If mb>0
		

		If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)	
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf
		EndIf
		
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
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
		
		; SAVE+TEST
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
			DisplayText2(">       <",23,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",23,28,TextMenusR,TextMenusG,TextMenusB)
			StartTestMode()
		EndIf
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33) And hubmode=False
			If CtrlDown()
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
	
	FinishDrawing()
	
	If waitflag=True Delay 1000
	



End Function

Function MasterAdvancedLoop()

	If CtrlDown() And KeyDown(20) ; Ctrl+T
		StartTestMode()
	EndIf
	
	adj=1
	If KeyDown(42) Or KeyDown(54) Then adj=10
	
	DisplayText2("Adventure File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(AdventureFileName$,0,1,255,255,255)
	DisplayText2("--------------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)
	
	If MouseY()<LetterHeight And  MouseX()>LetterX(30)
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
	
	
	
	If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)		
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	
	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)		
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
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33)
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
		If MouseY()<LetterHeight And MouseX()>LetterX(30)
			SetEditorMode(8)
			Repeat
			Until MouseDown(1)=0 And MouseDown(2)=0
		EndIf
		
		If MouseY()>LetterHeight*7 And MouseY()<LetterHeight*8
			If MouseX()<LetterX(14.6)
				StarterItems=StarterItems Xor 1
				Repeat
				Until MouseDown(1)=0 And MouseDown(2)=0
			EndIf
			
			If MouseX()>LetterX(14.6) And MouseX()<LetterX(29.2)
				StarterItems=StarterItems Xor 2
				Repeat
				Until MouseDown(1)=0 And MouseDown(2)=0
			EndIf
			
			If MouseX()>LetterX(29.2)
				StarterItems=StarterItems Xor 4
				Repeat
				Until MouseDown(1)=0 And MouseDown(2)=0
			EndIf
			
		EndIf 
		
		If MouseY()>LetterHeight*9 And MouseY()<LetterHeight*10 And MouseX()<LetterX(24)
			WidescreenRange=Not WidescreenRange
			Repeat
			Until MouseDown(1)=0 And MouseDown(2)=0
		EndIf
		
		If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)	
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf
		EndIf
		
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
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
		
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
			DisplayText2(">       <",23,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",23,28,TextMenusR,TextMenusG,TextMenusB)
			StartTestMode()
		EndIf
		
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33) And hubmode=False
			If CtrlDown()
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
		
		For i=0 To 5
			For j=0 To 2
				If MouseX()>LetterX(10+i*5) And MouseX()<LetterX(14+i*5) And MouseY()>LetterHeight*9+(j+2)*LetterHeight And MouseY()<LetterHeight*10+(j+2)*LetterHeight And MouseDebounceFinished()
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
					MouseDebounceSet(10)
				EndIf				
			Next
		Next
		
		If MouseY()>LetterHeight*15 And MouseY()<LetterHeight*16 And MouseX()>LetterX(16)
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
	
	FinishDrawing()
	
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
	If ShiftDown() Then adj=10
		
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
		If MouseX()<LetterX(4) And MouseY()>11*LetterHeight+i*LetterHeight And MouseY()<=12*LetterHeight+i*LetterHeight
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
		If flag ; mouse over
			ColorR=255
			ColorG=255
			ColorB=100
		ElseIf CopyingLevel=StateCopying And CopiedLevel=c
			ColorR=0
			ColorG=255
			ColorB=255
		ElseIf CopyingLevel=StateSwapping And CopiedLevel=c
			ColorR=255
			ColorG=0
			ColorB=255
		ElseIf HubAdventuresFilenames$(c)<>"" And c<=HubTotalAdventures
			ColorR=210
			ColorG=210
			ColorB=210
		Else
			ColorR=100
			ColorG=100
			ColorB=100
		EndIf
		DisplayText2(s$,0.5,11+i,ColorR,ColorG,ColorB)
		
		If c<=HubTotalAdventures
			If HubAdventuresMissing(c)
				ColorR=255
				ColorG=0
				ColorB=0
			ElseIf Not HubAdventuresIncludeInTotals(c)
				ColorR=255
				ColorG=155
				ColorB=0
			Else
				ColorR=210
				ColorG=210
				ColorB=210
			EndIf
			DisplayText2(HubAdventuresFilenames$(c),5,11+i,ColorR,ColorG,ColorB)
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
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*13 And MouseY()<LetterHeight*14
			DisplayText2("                                   EDIT",0.5,13,255,255,255)
		Else
			DisplayText2("                                   EDIT",0.5,13,180,180,180)
		EndIf
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*17 And MouseY()<LetterHeight*18
			DisplayText2("                                  REPLACE",0,17,255,255,255)
		Else
			DisplayText2("                                  REPLACE",0,17,180,180,180)
		EndIf
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*21 And MouseY()<LetterHeight*22
			DisplayText2("                                  REMOVE",0.5,21,255,255,255)
		Else
			DisplayText2("                                  REMOVE",0.5,21,180,180,180)
		EndIf
	Else
	DisplayText2("                                   EDIT",0.5,13,100,100,100)
	DisplayText2("                                  REPLACE",0,17,100,100,100)
	DisplayText2("                                  REMOVE",0.5,21,100,100,100)
	EndIf
	If MouseX()<LetterX(4) And MouseY()>LetterHeight*10 And MouseY()<LetterHeight*11
		DisplayText2(" -",0.5,10,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2(" -",0.5,10,TextMenuXR,TextMenuXG,TextMenuXB)
	EndIf
	If MouseX()<LetterX(4) And MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25
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
	
	
	If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	
	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)		
		DisplayText2("BUILD",25,27,255,255,255)
		DisplayText2("+EXIT",25,28,255,255,255)
	Else
		DisplayText2("BUILD",25,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",25,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	
	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33)
		DisplayText2("COMPILE",35,27,255,255,255)
		DisplayText2("+EXIT",36,28,255,255,255)
	Else
		DisplayText2("COMPILE",35,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",36,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf
		
	mb=0
	If MouseDown(1) mb=1
	If MouseDown(2) mb=2
	If MouseDown(3) mb=3
	
	;If MouseY()<22 And  MouseX()>540
	;	SetEditorMode(8)
	;	Repeat
	;	Until MouseDown(1)=0 And MouseDown(2)=0
	;EndIf
	If MouseX()<LetterX(4)
		If MouseScroll<>0
			adj=-MouseScroll
			If ShiftDown()
				adj=adj*10
			EndIf
			HubAdvStart=HubAdvStart+adj
		ElseIf mb>0 And MouseDebounceFinished()
			If MouseY()>LetterHeight*10 And MouseY()<LetterHeight*11
				HubAdvStart=HubAdvStart-adj
				MouseDebounceSet(10)
			EndIf
		
			If MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25
				HubAdvStart=HubAdvStart+adj
				MouseDebounceSet(10)
			EndIf
			
			;If CtrlDown()
			;	HubSelectedAdventure=InputInt("Enter adventure number to select: ")
			;	LeftMouseReleased=False
			;EndIf
		EndIf
		
		If HubAdvStart<0
			HubAdvStart=0
		ElseIf HubAdvStart+12>HubAdvMax
			HubAdvStart=HubAdvMax-12
		EndIf
	EndIf
		
	If mb>0 And LeftMouseReleased=True
		For i=0 To 12
			If MouseX()<LetterX(4) And MouseY()>LetterHeight*11+i*LetterHeight And MouseY()<=LetterHeight*12+i*LetterHeight
				HubSelectedAdventure=HubAdvStart+i
				If mb=1 And LeftMouseReleased=True
					If CopyingLevel=StateCopying And HubAdventuresFilenames$(HubSelectedAdventure)=""
						HubAdventuresFilenames$(HubSelectedAdventure)=HubAdventuresFilenames$(CopiedLevel)
						HubAdventuresMissing(HubSelectedAdventure)=HubAdventuresMissing(CopiedLevel)
						
						CopyingLevel=StateNotSpecial
						
						If HubSelectedAdventure>HubTotalAdventures
							HubTotalAdventures=HubSelectedAdventure
						EndIf
					ElseIf CopyingLevel=StateSwapping
						TempFilename$=HubAdventuresFilenames$(HubSelectedAdventure)
						TempMissing=HubAdventuresMissing(HubSelectedAdventure)
						HubAdventuresFilenames$(HubSelectedAdventure)=HubAdventuresFilenames$(CopiedLevel)
						HubAdventuresMissing(HubSelectedAdventure)=HubAdventuresMissing(CopiedLevel)
						HubAdventuresFilenames$(CopiedLevel)=TempFilename$
						HubAdventuresMissing(CopiedLevel)=TempMissing
						
						CopyingLevel=StateNotSpecial
						
						If HubSelectedAdventure>HubTotalAdventures
							HubTotalAdventures=HubSelectedAdventure
						EndIf
					ElseIf HubAdventuresFilenames$(HubSelectedAdventure)="" Or HubSelectedAdventure>HubTotalAdventures
						CopyingLevel=StateNotSpecial
						SetAdventureCurrentArchive(0)
						GetAdventures()
						AdventureNameEntered$=""
						SetEditorMode(12)
					Else
						CopyingLevel=StateNotSpecial
						
						If KeyDown(45) ; x key
							HubAdventuresIncludeInTotals(HubSelectedAdventure)=Not HubAdventuresIncludeInTotals(HubSelectedAdventure)
						EndIf
					EndIf
					
					Repeat
					Until MouseDown(1)=0
					
					mb=0
					Exit
				ElseIf HubAdventuresFilenames$(HubSelectedAdventure)="" Or HubSelectedAdventure>HubTotalAdventures
					CopyingLevel=StateNotSpecial
					SetAdventureCurrentArchive(0)
					GetAdventures()
					AdventureNameEntered$=""
					SetEditorMode(12)
				ElseIf mb=2 
					If CopyingLevel=StateCopying And HubSelectedAdventure=CopiedLevel
						CopyingLevel=StateNotSpecial
					Else
						CopyingLevel=StateCopying
						CopiedLevel=HubSelectedAdventure
					EndIf
					
					Repeat
					Until MouseDown(2)=0
					
					mb=0
					Exit
				ElseIf mb=3
					If CopyingLevel=StateSwapping And HubSelectedAdventure=CopiedLevel
						CopyingLevel=StateNotSpecial
					Else
						CopyingLevel=StateSwapping
						CopiedLevel=HubSelectedAdventure
					EndIf
					
					Repeat
					Until MouseDown(3)=0
					
					mb=0
					Exit
				EndIf
			EndIf
		Next
		
		
		; edit
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*13 And MouseY()<LetterHeight*14 And HubSelectedAdventure>=0
			AdventureFileName$=TrimHubAdventureName$(HubAdventuresFilenames$(HubSelectedAdventure))
			MasterDialogListStart=0
			MasterLevelListStart=0
			StartMaster()
			Repeat
			Until MouseDown(1)=0 
		EndIf
		
		; replace
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*17 And MouseY()<LetterHeight*18 And HubSelectedAdventure>=0
			SetAdventureCurrentArchive(0)
			GetAdventures()
			SetEditorMode(12)
			Repeat
			Until MouseDown(1)=0 
		EndIf
		
		; remove
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*21 And MouseY()<LetterHeight*22 And HubSelectedAdventure>=0
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
		If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)	
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			hubmode=False
			StartAdventureSelectScreen()
		EndIf
		
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
			DisplayText2(">       <",12,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",12,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			SaveHubFile()
			hubmode=False
			StartAdventureSelectScreen()

		EndIf
		
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
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
		
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33)
			DisplayText2(">       <",34,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",34,28,TextMenusR,TextMenusG,TextMenusB)
			If CtrlDown()
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
	MouseTextEntryTrackMouseMovement()
	
	Entering=0
	x=GetMouseLetterX()
	y=(MouseY()-LetterHeight*5)/LetterHeight
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
		
		tex$=MouseTextEntry$(tex$,let,x,y,0,2)
		
		Select entering
		Case 1
			HubDescription$=tex$
		Case 2
			HubTitle$=tex$
	;	Case 3
	;		AdventureHelpLine$(y-10)=tex$
			
		End Select

	EndIf	
		
	
	RenderLetters()
	RenderWorld()
	
	FinishDrawing()
	
	If waitflag=True Delay 1000
End Function

Function ReadTestFile()

	testfile=ReadFile(globaldirname$+"\temp\test.dat")
	If testfile<>0 ;FileType(globaldirname$+"\custom\editing\")
		AdventureFileName$=ReadString$(testfile)
		HubFileName$=ReadString$(testfile)
		If HubFileName$<>""
			hubmode=True
			StartHub()
		EndIf
		StartMaster()
		SetEditorMode(ReadInt(testfile))
		
		If EditorMode=EditorModeTile Or EditorMode=EditorModeObject
			level=ReadInt(testfile)
			x=ReadInt(testfile)
			y=ReadInt(testfile)
			AccessLevelAt(level,x,y)
			StartEditorMainLoop()
		EndIf
		
		CloseFile testfile
		DeleteFile globaldirname$+"\temp\test.dat"
		
		
	EndIf

End Function

Function StartTestMode(TestAtX=0,TestAtY=0)
	
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
	
	If EditorMode=EditorModeTile Or EditorMode=EditorModeObject
		WriteInt file,CurrentLevelNumber
		WriteInt file,TestAtX
		WriteInt file,TestAtY
	EndIf
	
	CloseFile file
	
	ExecFile ("wg.exe")
	EndApplication()
	
End Function

Const OriginalMasterDat$="__master_ORIGINAL__.bak"
Const Original1Wlv$="__1_ORIGINAL__.bak"

; This is hacky, but it should work regardless of the modded executable's version.
Function StartTestModeAt(level,x,y)

	RestoreOriginalMaster()
	RestoreOriginal1Wlv()
	
	SaveMasterFile()
	DuplicateMaster("master",OriginalMasterDat$)
	; Change adventure start coordinates to be far out-of-bounds.
	; Using coordinates that are farther out than about -100 seems to cause MAVs in-game.
	; Objects at negative coordinates like this DO NOT work in vanilla without MAVing!
	AdventureStartX=-100
	AdventureStartY=-50
	; master.dat gets saved in StartTestMode, so we don't have to save it here.
	
	If CurrentLevelNumber<>1
		AccessLevel(1)
		; If 1.wlv does not exist, create the file.
		If Not LevelExists(1)
			SaveLevel()
		EndIf
	EndIf
	DuplicateLevel(1,Original1Wlv$)
	; Place a level transition at the adventure start coordinates.
	GenerateLevelExitTo(level,x,y)
	CurrentObject\Attributes\ZAdjust=1000.0 ; Move the LevelExit out of view in-game.
	PreventPlacingObjectsOutsideLevel=False
	PlaceThisObject(AdventureStartX,AdventureStartY,CurrentObject)
	SaveLevel()
	CurrentLevelNumber=level ; Necessary so that the editor knows what level to return to when re-opening after testing.
	StartTestMode(x,y)

End Function

Function FileExists(FileName$)

	If FileType(FileName$)=1
		Return True
	Else
		Return False
	EndIf

End Function

Function FileExistsModel(FileName$)

	Return FileExists(MyProcessFileNameModel$(FileName$))

End Function

Function FileExistsTexture(FileName$)

	Return FileExists(MyProcessFileNameTexture$(FileName$))

End Function

Function MasterFileExists()

	Return FileExists(GetAdventureDir$()+"master.dat")

End Function

Function LoadMasterFile()	

	file=ReadFile (GetAdventureDir$()+"master.dat")
	
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
		
	adventurestartdir=180 ;0
	If Eof(file)=False Adventurestartdir=ReadInt(file)+180
	
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

Function RemoveLeft$(TheString$,Length)

	Return Right$(TheString$,Len(TheString$)-Length)

End Function

Function AdventureCurrentArchiveToHubPrefix$()

	Select AdventureCurrentArchive
	Case AdventureCurrentArchiveArchive
		Return ":Archive:"
	Case AdventureCurrentArchivePlayer
		Return ":Player:"
	Case AdventureCurrentArchiveDataAdventures
		Return ":Data:"
	Default
		Return ""
	End Select

End Function

Function GetHubAdventurePath$(Filename$)

	If Left$(Filename$,9)=":Archive:"
		Return GetAdventuresDir$(AdventureCurrentArchiveArchive)+RemoveLeft$(Filename$,9)
	ElseIf Left$(Filename$,8)=":Player:"
		Return GetAdventuresDir$(AdventureCurrentArchivePlayer)+RemoveLeft$(Filename$,8)
	ElseIf Left$(Filename$,6)=":Data:"
		Return GetAdventuresDir$(AdventureCurrentArchiveDataAdventures)+RemoveLeft$(Filename$,6)
	Else
		Return GetAdventuresDir$(AdventureCurrentArchiveCurrent)+Filename$
	EndIf

End Function

Function TrimHubAdventureName$(Filename$)

	If Left$(Filename$,9)=":Archive:"
		AdventureCurrentArchive=AdventureCurrentArchiveArchive
		Return RemoveLeft$(Filename$,9)
	ElseIf Left$(Filename$,8)=":Player:"
		AdventureCurrentArchive=AdventureCurrentArchivePlayer
		Return RemoveLeft$(Filename$,8)
	ElseIf Left$(Filename$,6)=":Data:"
		AdventureCurrentArchive=AdventureCurrentArchiveDataAdventures
		Return RemoveLeft$(Filename$,6)
	Else
		AdventureCurrentArchive=AdventureCurrentArchiveCurrent
		Return Filename$
	EndIf

End Function

Function IsHubMissingAdventures()

	For i=0 To HubTotalAdventures
		If HubAdventuresMissing(i)=True
			Return True
		EndIf
	Next
	
	Return False

End Function

Function LoadHubFile()
	file=ReadFile(globaldirname$+"\Custom\editing\Hubs\"+HubFileName$+"\hub.dat")
	version=ReadInt(file)
;	flag=False
	If version=0
		HubTitle$=ReadString(file)
		HubDescription$=ReadString(file)
		HubTotalAdventures=ReadInt(file)
		For i=0 To HubTotalAdventures
			HubAdventuresFilenames$(i)=ReadString(file)
			HubAdventuresMissing(i)=False
			If FileType(GetHubAdventurePath$(HubAdventuresFilenames$(i)))<>2
				HubAdventuresMissing(i)=True
				;HubAdventuresFilenames$(i)="" ; remove
;				If HubTotalAdventures=i
					;find the new HubTotalAdventures
;					For i=HubTotalAdventures To 1 Step -1
;						If HubAdventuresFilenames$(i)<>""
;							Exit
;						EndIf
;						HubTotalAdventures=HubTotalAdventures-1
;					Next
;					flag=True
;				EndIf
			EndIf
		Next
;		If flag
;			Cls
;			Locate 0,0
;			Print "Warning: At least one adventure is missing."
;			Print "Missing adventures are removed from the hub automatically."
;			Delay 3000
;		EndIf
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

Function SaveMasterFile()

	file=WriteFile (GetAdventureDir$()+"master.dat")


	WriteString file,adventuretitle$
	For i=0 To 4
		WriteString file,adventuretextline$(i)
	Next
	
	WriteString file,AdventureUserName$
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
	
	WriteInt file,AdventureStartDir-180
	
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
		SetAnswer(0)
		ColEffect=-1
		TxtEffect=-1
	EndIf
	
	WhichInterChange=i
	
	If WhichInterChange>=NofInterChanges
		NofInterChanges=WhichInterChange+1
	EndIf
	
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
	
	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()

End Function

Function AccessDialog(TargetDialog)

	CurrentDialog=TargetDialog
	
	SetInterChange(0)
	WhichAnswer=0
	WhichAskAbout=0
	
	ColEffect=-1
	TxtEffect=-1
	
	; check existence of this dialog file
	If DialogExists(CurrentDialog)
		LoadDialogFile()
	Else
		NewDialog()
	EndIf
	
End Function

Function NewDialog()

	ClearDialogFile()
	
	UnsavedChanges=0

End Function

Function TryPreviewDialog(TargetDialog)

	If DialogExists(TargetDialog)
		If PreviewCurrentDialog<>TargetDialog
			PreviewCurrentDialog=TargetDialog
			PreviewDialogFile()
		EndIf
		Return True
	Else
		Return False
	EndIf

End Function

Function DialogGetFirstLine$(Interchange)

	For j=0 To 6
		tex$=PreviewInterChangeTextLine$(Interchange,j)
		If tex$<>""
			Return tex$
		EndIf
	Next

	Return "< EMPTY >"

End Function

Function DialogGetFirstTwoLines$(Interchange)

	Result$=""
	ResultCount=0
	For j=0 To 6
		tex$=PreviewInterChangeTextLine$(Interchange,j)
		If tex$<>""
			If ResultCount=0
				Result$=tex$
			Else
				Result$=Result$+" "+tex$
				Return Result$
			EndIf
			ResultCount=ResultCount+1
		EndIf
	Next

	If Result$=""
		Return "< EMPTY >"
	Else
		Return Result$
	EndIf

End Function

Function CurrentDialogGetFirstTwoLines$(Interchange)

	Result$=""
	ResultCount=0
	For j=0 To 6
		tex$=InterChangeTextLine$(Interchange,j)
		If tex$<>""
			If ResultCount=0
				Result$=tex$
			Else
				Result$=Result$+" "+tex$
				Return Result$
			EndIf
			ResultCount=ResultCount+1
		EndIf
	Next

	If Result$=""
		Return "< EMPTY >"
	Else
		Return Result$
	EndIf

End Function

Function PreviewDialog$(DialogNumber,InterchangeNumber)

	If TryPreviewDialog(DialogNumber)
		If InterchangeNumber>=0 And InterchangeNumber<=100
			Return DialogGetFirstTwoLines$(InterchangeNumber)
		Else
			Return "< INVALID INTERCHANGE >"
		EndIf
	Else
		Return "< DIALOG DOES NOT EXIST >"
	EndIf

End Function

Function PreviewCurrentDialog$(InterchangeNumber)

	If InterchangeNumber>=0 And InterchangeNumber<=100
		Return CurrentDialogGetFirstTwoLines$(InterchangeNumber)
	Else
		Return "< INVALID INTERCHANGE >"
	EndIf

End Function

Function PreviewAskAbout$(DialogNumber,AskAboutNumber)

	If TryPreviewDialog(DialogNumber)
		If AskAboutNumber>-1 And AskAboutNumber<101
			tex$=PreviewAskAboutText$(AskAboutNumber)
			If tex$=""
				Return "< EMPTY >"
			Else
				Return tex$
			EndIf
		Else
			Return "< INVALID ASKABOUT >"
		EndIf
	Else
		Return "< DIALOG DOES NOT EXIST >"
	EndIf

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
	
	AddUnsavedChange()

End Function

Function ReplaceDialogTextCommand(k,NewEffect$)

	DialogTextCommand$(WhichInterChange,k)=NewEffect$
	
	AddUnsavedChange()

End Function

Function SortDialogTextCommands()

	; insertion sort
	i=1
	While i<NofTextCommands(WhichInterChange)
		j=i
		While DialogTextCommandPos(WhichInterChange,j-1)>DialogTextCommandPos(WhichInterChange,j)
			SwapDialogTextCommand(j,j-1)
			j=j-1
			If j=0
				Exit ; Needed instead of a conditional because Blitz3D doesn't short-circuit And...
			EndIf
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
	
	If HotkeySave()
		SaveDialogFile()
;	ElseIf HotKeyOpen()
;		If AskToSaveDialogAndExit()
;			OpenTypedDialog()
;		EndIf
	EndIf
	
	If CtrlDown()
		If KeyPressed(209) ; Ctrl+PageDown
			If AskToSaveDialogAndExit()
				AccessDialog(CurrentDialog+1)
			EndIf
		ElseIf KeyPressed(201) ; Ctrl+PageUp
			If AskToSaveDialogAndExit()
				AccessDialog(CurrentDialog-1)
			EndIf
		EndIf
	EndIf
	
	
	DisplayText2("Adventure: "+Left$(AdventureFileName$,20),0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Dialog #"+Str$(CurrentDialog),36-Len(Str$(CurrentDialog)),0,TextMenusR,TextMenusG,TextMenusB)
	
	
	
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
	DisplayText2("B",41,6,GetAnimatedFlashing(DialogTimer),GetAnimatedFlashing(DialogTimer),GetAnimatedFlashing(DialogTimer))
	DisplayText2("W",43,6,GetAnimatedFlashing(DialogTimer),60,60)
	
	DisplayText2("NO SH",39,7,255,255,255)
	DisplayText2("JI WA",39,8,255,255,255)
	DisplayText2("BO ZO",39,9,255,255,255)
	DisplayText2("ZS CR",39,10,255,255,255)
	DisplayText2("EI UD",39,11,255,255,255)
	DisplayText2("LR RT",39,12,255,255,255)
	DisplayText2("CLEAR",39,14,255,255,0)
	
	
	If MouseX()>LetterX(38) And MouseY()>23*LetterHeight And MouseY()<25*LetterHeight
		DisplayText2("CANCEL",38.2,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("+EXIT",38.7,24,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("CANCEL",38.2,23,255,255,255)
		DisplayText2("+EXIT",38.7,24,255,255,255)

	EndIf
	
	If MouseX()>LetterX(38) And MouseY()>27*LetterHeight
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
						DialogCurrentRed=GetAnimatedFlashing(DialogTimer)
						DialogCurrentGreen=GetAnimatedFlashing(DialogTimer)
						DialogCurrentBlue=GetAnimatedFlashing(DialogTimer)
					Case "CWAR"
						DialogCurrentRed=GetAnimatedFlashing(DialogTimer)
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
			
			charx#=(i)+xoff
			chary#=YOffset/2.0+j+yoff
			
			
			AddLetter(Asc(Mid$(InterChangeTextLine$(WhichInterChange,j),i+1,1))-32,(-.97+charx*.045*size*spacing)/1.0,(.5-chary*.05*size*spacing)/1.0,1.0,rot,.04*size/1.0,0,0,0,0,0,0,0,0,0,dialogcurrentred,dialogcurrentgreen,dialogcurrentblue)
		
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
	MouseTextEntryTrackMouseMovement()
	; Mouse Pos
	Entering=0
	
	x=GetMouseLetterX()
	If MouseY()<LetterHeight*14
		y=(MouseY()-LetterHeight*5)/LetterHeight
	Else If MouseY()<LetterHeight*15
		y=(MouseY()-LetterHeight*4.8)/LetterHeight ; 4.5
	Else 
		y=(MouseY()-LetterHeight*4.6)/LetterHeight ; 4
	EndIf
	
	debug1=MouseY()
	debug2=y
	
	; cursor
	If x<CharactersPerLine And MouseY()>=LetterHeight*4 And y<7 And y>-1
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
						ReplaceDialogTextCommand(k,Effect$)
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
						ReplaceDialogTextCommand(k,TCommands(TxtEffect))
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
		
		InterChangeTextLine$(WhichInterChange,y)=MouseTextEntry$(InterChangeTextLine$(WhichInterChange,y),let,x,y,0,1)

	EndIf
	If Entering=2
		
		InterChangeReplyText$(WhichInterChange,WhichAnswer)=MouseTextEntry$(InterChangeReplyText$(WhichInterChange,WhichAnswer),let,x,y,0,1)

	EndIf
	If Entering=3
		
		AskaboutText$(WhichAskAbout)=MouseTextEntry$(AskaboutText$(WhichAskAbout),let,x,y,-8,1)

	EndIf
	If Entering=4
		
		AskaboutTopText$=MouseTextEntry$(AskaboutTopText$,let,x,y,-8,1)

	EndIf


	
	mb=0
	If MouseDown(1) mb=1
	If MouseDown(2) mb=2
	Modified=mb<>0 Or MouseScroll<>0
	If mb>0
		; Change Adventure
		; Load/Save
		
		If MouseX()>LetterX(38) And MouseY()>LetterHeight*23 And MouseY()<LetterHeight*25
			If AskToSaveDialogAndExit()
				ClearDialogFile()
				ResumeMaster()
			EndIf
			Repeat
			Until MouseDown(1)=0
		EndIf
	
		If MouseX()>LetterX(38) And MouseY()>LetterHeight*27
			SaveDialogAndExit()
			Repeat
			Until MouseDown(1)=0

		EndIf

	EndIf

	RawInput=CtrlDown()
	DelayTime=10

	; Change InterChange
	If MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4 And MouseX()>LetterX(5) And MouseX()<LetterX(21)
		target=AdjustInt("Interchange: ", WhichInterChange, 1, 10, DelayTime)
		SetInterChange(target)
	EndIf
	
	; Change Answer
	If MouseY()>LetterHeight*13 And MouseY()<LetterHeight*14 And MouseX()>LetterX(5) And MouseX()<LetterX(21)
		target=AdjustInt("Answer: ", WhichAnswer, 1, 10, DelayTime)
		SetAnswer(target)
	EndIf
	; Change AnswerData
	; thanks to tooltips this is now awesome
	If MouseY()>LetterHeight*15 And MouseY()<LetterHeight*17
		MouseXToUse=(MouseX()-LetterX(0))/(LetterWidth*6)
		TooltipX=MouseXToUse*LetterWidth*6+LetterX(0)
		TooltipY=LetterHeight*19.5
		Select MouseXToUse
		Case 0
			OldValue=InterChangeReplyFunction(WhichInterChange,WhichAnswer)
			InterChangeReplyFunction(WhichInterChange,WhichAnswer)=AdjustInt("FNC: ", InterChangeReplyFunction(WhichInterChange,WhichAnswer), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyFunction(WhichInterChange,WhichAnswer)
				AddUnsavedChange()
			EndIf
			
			ShowTooltipCenterAligned(TooltipX, TooltipY, ReplyFunctionToName$(InterChangeReplyFunction(WhichInterChange,WhichAnswer)))
		Case 1
			OldValue=InterChangeReplyData(WhichInterChange,WhichAnswer)
			InterChangeReplyData(WhichInterChange,WhichAnswer)=AdjustInt("Data: ", InterChangeReplyData(WhichInterChange,WhichAnswer), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyData(WhichInterChange,WhichAnswer)
				AddUnsavedChange()
			EndIf
			
			Fnc=InterChangeReplyFunction(WhichInterChange,WhichAnswer)
			tex$=ReplyFunctionToDataName$(Fnc)
			If Fnc=1 Or Fnc=2
				tex$=tex$+": "+PreviewCurrentDialog$(InterChangeReplyData(WhichInterchange,WhichAnswer))
			EndIf
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		Case 2
			OldValue=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			InterChangeReplyCommand(WhichInterChange,WhichAnswer)=AdjustInt("CMD: ", InterChangeReplyCommand(WhichInterChange,WhichAnswer), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommand(WhichInterChange,WhichAnswer)
				AddUnsavedChange()
			EndIf
			
			ShowTooltipCenterAligned(TooltipX, TooltipY, GetCommandName$(InterChangeReplyCommand(WhichInterChange,WhichAnswer)))
		Case 3
			OldValue=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)=AdjustInt("Data1: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
				AddUnsavedChange()
			EndIf
			
			Cmd=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			Data1=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
			
			tex$=GetCMDData1NameAndValue(Cmd, Data1, ": ")+WithJoinerIfNotEmpty$(GetCmdData1ExtraInfo$(Cmd,Data1),": ")
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		Case 4
			OldValue=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)=AdjustInt("Data2: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)
				AddUnsavedChange()
			EndIf
			
			Cmd=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			Data1=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
			Data2=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)
			
			tex$=GetCMDData2NameAndValue(Cmd, Data2, ": ")+WithJoinerIfNotEmpty$(GetCmdData2ExtraInfo$(Cmd,Data1,Data2),": ")
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		Case 5
			OldValue=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)=AdjustInt("Data3: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)
				AddUnsavedChange()
			EndIf
			
			Cmd=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			Data1=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
			Data2=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)
			Data3=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)
			
			tex$=GetCMDData3NameAndValue(Cmd, Data2, Data3, ": ")+WithJoinerIfNotEmpty$(GetCmdData3ExtraInfo$(Cmd,Data1,Data3),": ")
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		Case 6
			OldValue=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)=AdjustInt("Data4: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)
				AddUnsavedChange()
			EndIf
			
			Cmd=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			Data4=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)
			
			tex$=GetCMDData4NameAndValue(Cmd, Data4, ": ")
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		End Select
		
		If Modified
			ColEffect=-1
			TxtEffect=-1
		EndIf
	EndIf
		

	
	; Change Askabout
	If MouseY()>LetterHeight*22 And MouseY()<LetterHeight*23 And MouseX()>LetterX(5) And MouseX()<LetterX(21)
		target=AdjustInt("AskAbout: ", WhichAskabout, 1, 10, DelayTime)
		SetAskabout(target)
	EndIf
	; Change AskaboutData
	If MouseY()>LetterHeight*24.5 And MouseY()<LetterHeight*26.5
		TooltipY=LetterHeight*28.5
		If MouseX()<LetterX(8)
			OldValue=AskAboutActive(WhichAskAbout)
			AskAboutActive(WhichAskAbout)=AdjustInt("Active: ", AskAboutActive(WhichAskAbout), 1, 10, DelayTime)
			If OldValue<>AskAboutActive(WhichAskAbout)
				AddUnsavedChange()
			EndIf
			
			ShowTooltipCenterAligned(LetterX(4),TooltipY,GetAskAboutActiveName$(AskAboutActive(WhichAskAbout)))

		Else If MouseX()<LetterX(22.5)
			OldValue=AskAboutInterChange(WhichAskAbout)
			AskAboutInterChange(WhichAskAbout)=AdjustInt("Interchange: ", AskAboutInterChange(WhichAskAbout), 1, 10, DelayTime)
			If OldValue<>AskAboutInterChange(WhichAskAbout)
				AddUnsavedChange()
			EndIf
			
			ShowTooltipCenterAligned(LetterX(15.25),TooltipY,"Destination Interchange: "+PreviewCurrentDialog$(AskAboutInterChange(WhichAskAbout)))
		Else If MouseX()<LetterX(38)
			OldValue=AskAboutRepeat(WhichAskAbout)
			AskAboutRepeat(WhichAskAbout)=AdjustInt("Repeat: ", AskAboutRepeat(WhichAskAbout), 1, 10, DelayTime)
			If OldValue<>AskAboutRepeat(WhichAskAbout)
				AddUnsavedChange()
			EndIf
			
			ShowTooltipCenterAligned(LetterX(27.5),TooltipY,GetAskAboutRepeatName$(AskAboutRepeat(WhichAskAbout)))
		EndIf
		
		If Modified
			ColEffect=-1
			TxtEffect=-1
		EndIf
	EndIf
	
	; Colours/Effects
	If LeftMouse=True And LeftMouseReleased=True
		LeftMouseReleased=False
		StartX=39
		For i=0 To 11
			If MouseX()>=LetterX(StartX+(i Mod 3)*2) And MouseX()<=LetterX(StartX+1+(i Mod 3)*2) And MouseY()>=LetterHeight*3 + LetterHeight*(i/3) And MouseY()<LetterHeight*4+LetterHeight*(i/3)
				ToggleColEffect(i)
			EndIf
		Next
		For i=0 To 11
			If MouseX()>=LetterX(StartX+(i Mod 2)*2.5) And MouseX()<=LetterX(StartX+2.5+(i Mod 2)*2.5) And MouseY()>=LetterHeight*7 + LetterHeight*(i/2) And MouseY()<LetterHeight*8+LetterHeight*(i/2)
				ToggleTxtEffect(i)
			EndIf
		Next
		If MouseX()>LetterX(StartX) And MouseY()>LetterHeight*14 And MouseY()<LetterHeight*15
			; clear text colors and effects
			For i=0 To NofTextCommands(WhichInterChange)-1
				DialogTextCommand$(WhichInterChange,i)=""
				DialogTextCommandpos(WhichInterChange,i)=-1
			Next
			NofTextCommands(WhichInterChange)=0
			
			AddUnsavedChange()
		EndIf
	EndIf
		
			
	If CtrlDown() ; ctrl+...
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
	

	FinishDrawing()
	
	


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
	For i=0 To MaxInterChanges ;-1
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
			
			; Make the default FNC end the dialog and return to this Interchange.
			InterChangeReplyFunction(i,j)=1
			InterChangeReplyData(i,j)=i
			
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

Function ClearDialogPreview()
	; first clear all data
	PreviewNofInterchanges=1
	For i=0 To MaxInterChanges ;-1
		PreviewNofInterChangeTextLines(i)=0	
		For j=0 To 6
			PreviewInterChangeTextLine$(i,j)=""
		Next
		PreviewNofTextCommands(i)=0
		For j=0 To 199
			PreviewDialogTextCommand$(i,j)=""
			PreviewDialogTextCommandPos(i,j)=-1
		Next
		PreviewNofInterChangeReplies(i)=1
		For j=0 To 7
			PreviewInterChangeReplyText$(i,j)=""
			
			; Make the default FNC end the dialog and return to this Interchange.
			PreviewInterChangeReplyFunction(i,j)=1
			PreviewInterChangeReplyData(i,j)=i
			
			PreviewInterChangeReplyCommand(i,j)=0
			For k=0 To 3
				PreviewInterChangeReplyCommandData(i,j,k)=0
			Next
		Next
		
	Next
	PreviewNofAskAbouts=0
	PreviewAskAboutTopText$=""
	For i=0 To MaxAskAbouts-1
		PreviewAskAboutText$(i)=""
		PreviewAskAboutActive(i)=-2
		PreviewAskAboutInterchange(i)=0
		PreviewAskAboutRepeat(i)=-1
	Next
End Function

Function LoadDialogFile()

	ClearDialogFile()
		
	; yep - load
	file=ReadFile(GetAdventureDir$()+Str$(currentdialog)+".dia")

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
	
	UnsavedChanges=0

End Function

Function PreviewDialogFile()

	ClearDialogPreview()
		
	; yep - load
	file=ReadFile(GetAdventureDir$()+Str$(PreviewCurrentDialog)+".dia")

	PreviewNofInterchanges=ReadInt(file)
	For i=0 To PreviewNofInterchanges-1
		PreviewNofInterChangeTextLines(i)=ReadInt(file)	
		For j=0 To PreviewNofInterChangeTextLines(i)-1
			PreviewInterChangeTextLine$(i,j)=ReadString$(file)
		Next
		PreviewNofTextCommands(i)=ReadInt(file)
		
		For j=0 To PreviewNofTextCommands(i)-1
			PreviewDialogTextCommand$(i,j)=ReadString$(file)
			PreviewDialogTextCommandPos(i,j)=ReadInt(file)
		Next
		PreviewNofInterChangeReplies(i)=ReadInt(file)
		For j=0 To PreviewNofInterChangeReplies(i)-1
			PreviewInterChangeReplyText$(i,j)=ReadString$(file)
			PreviewInterChangeReplyFunction(i,j)=ReadInt(file)
			PreviewInterChangeReplyData(i,j)=ReadInt(file)
			PreviewInterChangeReplyCommand(i,j)=ReadInt(file)
			For k=0 To 3
				PreviewInterChangeReplyCommandData(i,j,k)=ReadInt(file)
			Next
		Next
	Next
	PreviewNofAskAbouts=ReadInt(file)
	PreviewAskAboutTopText$=ReadString$(file)
	For i=0 To PreviewNofAskAbouts-1
		PreviewAskAboutText$(i)=ReadString$(File)
		PreviewAskAboutActive(i)=ReadInt(file)
		PreviewAskAboutInterchange(i)=ReadInt(file)
		PreviewAskAboutRepeat(i)=ReadInt(file)
	Next
	CloseFile file

End Function

Function SaveDialogFile()
	
	file=WriteFile(GetAdventureDir$()+Str$(currentdialog)+".dia")
	
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
	
	UnsavedChanges=0
	
End Function

Function GetCommandName$(id)
	Select id
	Case 1
		Return "Activate"
	Case 2
		Return "Deactivate"
	Case 3
		Return "Toggle"
	Case 4
		Return "Change Int"
	Case 5
		Return "Destroy"
	Case 6
		Return "Change Light"
	Case 7
		Return "Warp Lvl"
	Case 8
		Return "Start Adv"
	Case 9
		Return "Earthquake"
	Case 10
		Return "Global SFX"
	Case 11
		Return "Local SFX"
	Case 12
		Return "Adjust Music"
	Case 13
		Return "Change Weather"
	Case 14
		Return "(MOD) Set Music"
	Case 15
		Return "(MOD) Set Magic"
	Case 16
		Return "(MOD) Set Cam"
	Case 17
		Return "(MOD) TileLogic"
	Case 18
		Return "(MOD) Set OTL"
	Case 21
		Return "Start Dialog"
	Case 22
		Return "Chng Dia Start"
	Case 23
		Return "Acti. AskAbout"
	Case 24
		Return "Deac. AskAbout"
	Case 25
		Return "Togg. AskAbout"
	Case 26
		Return "Set AA Active"
	Case 27
		Return "Set AskAbt Dia"
	Case 28
		Return "Acti. MstrAA"
	Case 29
		Return "Deac. MstrAA"
	Case 30
		Return "Togg. MstrAA"
	Case 41
		Return "Silent CopyObj"
	Case 42
		Return "Flashy CopyObj"
	Case 51
		Return "Chng MvmtType"
	Case 52
		Return "Chng MvTp+Data"
	Case 61
		Return "Move NPC"
	Case 62
		Return "Change NPC 1"
	Case 63
		Return "Change NPC 2"
	Case 64
		Return "Exclamation"
	Case 65
		Return "(MOD) Plyr Face"
	Case 101
		Return "(MOD) Cutscene"
	Case 102
		Return "Cutscene 1"
	Case 103
		Return "Cutscene 2"
	Case 104
		Return "Cutscene 3"
	Case 111
		Return "Refill Lamp"
	Case 112
		Return "4^2 Inventory"
	Case 113
		Return "5^2 Inventory"
	Case 114
		Return "Enable Shards"
	Case 115
		Return "Floing Menu"
	Case 116
		Return "Change Hat"
	Case 117
		Return "Chng Accessory"
	Case 118
		Return "(MOD) Hub End"
	Case 119
		Return "(MOD) Dischrge"
	Case 120
		Return "(MOD) POTZ End"
	Default
		Return "N/A"
	End Select
End Function

Function GetCMDData1Name$(id)
	Select id
	Case 1,2,3,4,5,16,51,52,64
		Return "Target ID"
	Case 6
		Return "Red"
	Case 7,120
		Return "Level"
	Case 8
		Return "Adv. No"
	Case 9
		Return "Duration"
	Case 10,11
		Return "Sound"
	Case 12
		Return "TargetVolume"
	Case 13
		Return "Weather"
	Case 14
		Return "Music ID"
	Case 15
		Return "Magic"
	Case 17,18
		Return "Tile X"
	Case 21,22,23,24,25,26,27
		Return "Dialog No"
	Case 28,29,30
		Return "MstrAA"
	Case 41,42
		Return "Source X"
	Case 61,62,63
		Return "NPC ID"
	Case 65
		Return "Expression"
	Case 101
		Return "Cutscene ID"
	Default
		Return "N/A (1)"
	End Select
End Function

Function GetCMDData2Name$(id)
	Select id
	Case 4
		Return "Int"
	Case 6
		Return "Green"
	Case 7
		Return "Start X"
	Case 11
		Return "Source X"
	Case 12
		Return "MaxVolumeStep" ;"Volume Step"
	Case 15
		Return "Charges"
	Case 17,18
		Return "Tile Y"
	Case 21,22
		Return "Interchange"
	Case 23,24,25,26,27
		Return "AskAbout"
	Case 41,42
		Return "Source Y"
	Case 51
		Return "Obsolete"
	Case 61,120
		Return "Dest X"
	Case 52
		Return "MvmtType"
	Case 62
		Return "Dialog"
	Case 63
		Return "WalkAnim"
	Case 64
		Return "Particle ID"
	Default
		Return "N/A (2)"
	End Select
End Function

Function GetCMDData3Name$(id)
	Select id
	Case 4,26
		Return "Value"
	Case 6
		Return "Blue"
	Case 7
		Return "Start Y"
	Case 11
		Return "Source Y"
	Case 12
		Return "TargetPitch"
	Case 15
		Return "Homing"
	Case 17
		Return "Logic"
	Case 18
		Return "ObjectTileLogic"
	Case 21
		Return "Obsolete?"
	Case 27
		Return "Interchange"
	Case 41,42
		Return "Dest X"
	Case 51
		Return "Obsolete"
	Case 61,120
		Return "Dest Y"
	Case 52
		Return "MvTpData"
	Case 62
		Return "Expression"
	Case 63
		Return "Turn"
	Case 64
		Return "Count"
	Default
		Return "N/A (3)"
	End Select
End Function

Function GetCMDData4Name$(id)
	Select id
	Case 4
		Return "Level?"
	Case 7
		Return "PlayerYaw"
	Case 12
		Return "MaxPitchStep"
	Case 41,42
		Return "Dest Y"
	Case 51
		Return "MvmtType"
	Case 62
		Return "Yaw"
	Case 63
		Return "IdleAnim"
	Default
		Return "N/A (4)"
	End Select
End Function

Function GetCmdData1ValueName$(Cmd, Data1)
	
	Select Cmd
	Case 6
		Return GetLightColorAndAmbientString$(Data1)
	Case 10,11
		Return Data1+"/"+GetSoundName$(Data1)
	Case 12
		If Data1=0
			Return "No Change"
		Else
			Return Data1+"%"
		EndIf
	Case 13
		Return GetWeatherName$(Data1)
	Case 14
		Return GetMusicName$(Data1)
	Case 15
		Return GetMagicNameAndId$(Data1)
	Case 16,64
		If Data1=-1
			Return "Pla"
		Else
			Return Data1
		EndIf
	Case 65
		Return GetStinkerExpressionName$(Data1)
	Default
		Return Data1
	End Select

End Function

Function GetCmdData2ValueName$(Cmd, Data2)

	Select Cmd
	Case 4
		Return Data2+"/"+GetCmd4Data2ValueName$(Data2)
	Case 6
		Return GetLightColorAndAmbientString$(Data2)
	Case 12
		If Data2=0
			Return "Instant"
		Else
			Return Data2+"%"
		EndIf
	Case 21
		If Data2=-1
			Return "Current"
		Else
			Return Data2
		EndIf
	Case 52
		Return Data2+"/"+GetMovementTypeString$(Data2)
	Case 62
		If Data2=0
			Return "None"
		ElseIf Data2=-1
			Return "No Change"
		Else
			Return Data2
		EndIf
	Case 63
		If Data2=-1
			Return "No Change"
		Else
			Return GetStinkerNPCWalkAnimName$(Data2)
		EndIf
	Default
		Return Data2
	End Select

End Function

Function GetCmdData3ValueName$(Cmd, Data2, Data3)

	Select Cmd
	Case 4
		Select Data2
		Case 1 ; MovementType
			Return Data3+"/"+GetMovementTypeString$(Data3)
		Case 9 ; Type
			Return Data3+"/"+GetTypeString$(Data3)
		Case 12 ; ActivationType
			Return GetActivationTypeString$(Data3)
		Default
			Return Data3
		End Select
	Case 6
		Return GetLightColorAndAmbientString$(Data3)
	Case 12
		If Data3=0
			Return "No Change"
		Else
			Return Data3+" kHz"
		EndIf
	Case 15
		Return Data3+"/"+OneToYes$(Data3)
	Case 17
		Return LogicIdToLogicName$(Data3)
	Case 26
		Return GetAskAboutActiveNameShort$(Data3)
	Case 62
		If Data3=-1
			Return "No Change"
		Else
			Return GetStinkerExpressionName$(Data3)
		EndIf
	Case 63
		If Data3=-1
			Return "No Change"
		Else
			Return GetNPCTurningName$(Data3)
		EndIf
	Default
		Return Data3
	End Select

End Function

Function GetCmdData4ValueName$(Cmd, Data4)

	Select Cmd
	Case 4
		If Data4=0
			Return "Current"
		Else
			Return Data4+"/MAV?"
		EndIf
	Case 7
		DisplayedRotation=(Data4+180) Mod 360
		Return GetDirectionString$(DisplayedRotation)
	Case 12
		If Data4=0
			Return "Instant"
		Else
			Return Data4+" kHz"
		EndIf
	Case 51
		Return Data4+"/"+GetMovementTypeString$(Data4)
	Case 62
		If Data4=-1
			Return "No Change"
		Else
			Return Data4
			;DisplayedRotation=Data4 Mod 360
			;Return GetDirectionString$(DisplayedRotation)
		EndIf
	Case 63
		If Data4=-1
			Return "No Change"
		Else
			Return GetStinkerNPCIdleAnimName$(Data4)
		EndIf
	Default
		Return Data4
	End Select

End Function

Function GetCmd4Data2ValueName$(value)

	Select value
	Case 1
		Return "MovementType"
	Case 2
		Return "MovementTypeData"
	Case 3
		Return "RadiusType"
	Case 4
		Return "Data10"
	Case 5
		Return "AttackPower"
	Case 6
		Return "DefensePower"
	Case 7
		Return "DestructionType"
	Case 8
		Return "ID"
	Case 9
		Return "Type"
	Case 10
		Return "SubType"
	Case 11
		Return "Active"
	Case 12
		Return "ActivationType"
	Case 13
		Return "ActivationSpeed"
	Case 14
		Return "Status"
	Case 15
		Return "Timer"
	Case 16
		Return "TimerMax1"
	Case 17
		Return "TimerMax2"
	Case 18
		Return "Teleportable"
	Case 19
		Return "ButtonPush"
	Case 20
		Return "WaterReact"
	Case 21
		Return "Telekinesisable"
	Case 22
		Return "Freezable"
	Case 23
		Return "Data0"
	Case 24
		Return "Data1"
	Case 25
		Return "Data2"
	Case 26
		Return "Data3"
	Case 27
		Return "Data4"
	Case 28
		Return "Data5"
	Case 29
		Return "Data6"
	Case 30
		Return "Data7"
	Case 31
		Return "Data8"
	Case 32
		Return "Data9"
	Default
		Return "None"
	End Select

End Function

Function GetLightColorAndAmbientString$(ColorValue)

	Return ColorValue+" (Amb: "+ColorValue/2+")"

End Function

Function GetMovementTypeString$(value)

	Select value
	Case 0
		Return "Stationary"
	Case 10
		Return "HPathfind0" ;"HPathfind0" "GoalRange0" "HQ A* To 0" ""A*+ Range0""
	Case 11
		Return "HPathfind1"
	Case 12
		Return "HPathfind2"
	Case 13
		Return "MPathfind0"
	Case 14
		Return "MPathfind1"
	Case 15
		Return "MPathfind2"
	Case 16
		Return "LPathfind0"
	Case 17
		Return "LPathfind1"
	Case 18
		Return "LPathfind2"
	Case 20
		Return "PlayerPath"
	Case 30
		Return "FleeRange0"
	Case 31
		Return "FleeRange1"
	Case 32
		Return "FleeRange2"
	Case 33
		Return "FleeRange3"
	Case 34
		Return "FleeRange4"
	Case 41
		Return "NorthLeft"
	Case 42
		Return "NorthRight"
	Case 43
		Return "EastLeft"
	Case 44
		Return "EastRight"
	Case 45
		Return "SouthLeft"
	Case 46
		Return "SouthRight"
	Case 47
		Return "WestLeft"
	Case 48
		Return "WestRight"
	Case 51,52
		Return "NorthSwim"
	Case 53,54
		Return "EastSwim"
	Case 55,56
		Return "SouthSwim"
	Case 57,58
		Return "WestSwim"
	Case 71
		Return "NorthBounc"
	Case 72
		Return "NorthEast"
	Case 73
		Return "EastBounc"
	Case 74
		Return "SouthEast"
	Case 75
		Return "SouthBounc"
	Case 76
		Return "SouthWest"
	Case 77
		Return "WestBounc"
	Case 78
		Return "NorthWest"
	Case 81
		Return "MoobotNL"
	Case 82
		Return "MoobotNR"
	Case 83
		Return "MoobotEL"
	Case 84
		Return "MoobotER"
	Case 85
		Return "MoobotSL"
	Case 86
		Return "MoobotSR"
	Case 87
		Return "MoobotWL"
	Case 88
		Return "MoobotWR"
	Default
		Return "NotVanilla"
	End Select

End Function

Function GetActivationTypeString$(value)

	Select value
	Case 1
		Return "GrowZ"
	Case 2
		Return "GrowXYZ"
	Case 3
		Return "GrowXY"
	Case 11
		Return "GoDown"
	Case 12
		Return "Bridge1"
	Case 13
		Return "Bridge2"
	Case 14
		Return "Bridge3"
	Case 15
		Return "Bridge4"
	Case 16
		Return "Bridge5"
	Case 17
		Return "GoNorth"
	Case 18
		Return "GoEast"
	Case 19
		Return "GoSouth"
	Case 20
		Return "GoWest"
	Case 21
		Return "Fade"
	Case 31
		Return "Cage"
	Case 41
		Return "DungeonDoor"
	Default
		Return value
	End Select

End Function

Function GetMusicName$(value)

	Select value
	Case -1
		Return "Beach"
	Case 0
		Return "No Music"
	Case 1
		Return "WA Intro"
	Case 2
		Return "Pastoral"
	Case 3
		Return "WonderTown"
	Case 4
		Return "Dark/Sewer"
	Case 5
		Return "Cave/Woods"
	Case 6
		Return "Scary/Void"
	Case 7
		Return "WondrFalls"
	Case 8
		Return "Jungle"
	Case 9
		Return "KaboomTown"
	Case 10
		Return "Acid Pools"
	Case 11
		Return "Retro"
	Case 12
		Return "Cave"
	Case 13
		Return "POTZ Intro"
	Case 14
		Return "Uo Sound"
	Case 15
		Return "Z-Ambience"
	Case 16
		Return "Z-Synchron"
	Case 17
		Return "RetroScary"
	Case 18
		Return "DesertWind"
	Case 19
		Return "DesertCave"
	Case 20
		Return "Star World"
	Case 21
		Return "Piano"
	Default
		Return value
	End Select

End Function

Function GetWeatherName$(value)

	Select value
	Case 0
		Return "Clear Sky"
	Case 1
		Return "Light Snow"
	Case 2
		Return "Heavy Snow"
	Case 3
		Return "BlizzardRL"
	Case 4
		Return "BlizzardLR"
	Case 5
		Return "Rain"
	Case 6
		Return "Weird"
	Case 7
		Return "ThundrStrm"
	Case 8
		Return "Alarm"
	Case 9
		Return "Light Rise"
	Case 10
		Return "Light Fall"
	Case 11
		Return "Rainb Rise"
	Case 12
		Return "Rainb Fall"
	Case 13
		Return "Foggy"
	Case 14
		Return "FoggyGreen"
	Case 15
		Return "Leaves"
	Case 16
		Return "Sand Storm"
	Case 17
		Return "Abstract"
	Default
		Return "NotVanilla"
	End Select

End Function

Function GetSoundName$(value)

	Select value
	Case 0
		Return "GetStar"
	Case 1
		Return "TollGate"
	Case 10
		Return "SpringHit"
	Case 11
		Return "GetGem"
	Case 12
		Return "GetCoin"
	Case 13
		Return "CrystalTone"
	Case 14
		Return "Wakka"
	Case 15
		Return "Destroy"
	Case 16
		Return "ElectricZap"
	Case 20
		Return "IceSlide"
	Case 21
		Return "ButtonDown"
	Case 22
		Return "Rotator"
	Case 23
		Return "ButtonTimer"
	Case 24
		Return "ColorChange"
	Case 28
		Return "Ghost"
	Case 29
		Return "WraithAppear"
	Case 30
		Return "FireTrapOn"
	Case 31
		Return "FireTrapLoop"
	Case 32
		Return "CageFall"
	Case 33
		Return "BrdgWaterUp"
	Case 34
		Return "BrdgWaterDown"
	Case 35
		Return "BrdgMechaUp"
	Case 36
		Return "BrdgMechaDown"
	Case 37
		Return "DungeonDoor"
	Case 38
		Return "AutoDoorOpen"
	Case 39
		Return "AutoDoorClose"
	Case 40
		Return "TransportLoop"
	Case 41
		Return "TransportStop"
	Case 42
		Return "TeleporterUse"
	Case 43
		Return "SucTubeUse2"
	Case 44
		Return "SucTubeUse1"
	Case 45
		Return "PlayerOof"
	Case 50
		Return "WeeHiThere"
	Case 51
		Return "WeeHi"
	Case 52
		Return "WeeMorning"
	Case 53
		Return "WeeHello"
	Case 54
		Return "WeeYooHoo"
	Case 55
		Return "WeeYeah?"
	Case 56
		Return "WeeHmm?"
	Case 57
		Return "WeeWhat?"
	Case 58
		Return "WeeUhHuh?"
	Case 59
		Return "WeeSnore"
	Case 60
		Return "WeeOkay"
	Case 61
		Return "WeeSndsGood"
	Case 62
		Return "WeeOkeeDokee"
	Case 63
		Return "WeeHereIGo"
	Case 64
		Return "WeeYee"
	Case 65
		Return "WeeDeath"
	Case 66
		Return "WeeOhNo"
	Case 67
		Return "WeeDrown"
	Case 68
		Return "WeeImBored"
	Case 69
		Return "WeeImTired"
	Case 70
		Return "WeeWoo"
	Case 71
		Return "WeeThankYou"
	Case 72
		Return "WeeByeBye"
	Case 73
		Return "WeeYay"
	Case 74
		Return "BoomerKaboom?"
	Case 75
		Return "BoomerKaboom1"
	Case 76
		Return "BoomerKaboom2"
	Case 77
		Return "BoomerKa"
	Case 78
		Return "BoomerKaboom!"
	Case 79
		Return "BoomerScared"
	Case 80
		Return "MagicCharge"
	Case 81
		Return "Blinked"
	Case 82
		Return "CastSpell"
	Case 83
		Return "MakeBrrFloat"
	Case 84
		Return "IceShatter"
	Case 85
		Return "StinkerFrozen"
	Case 86
		Return "ChomperFrozen"
	Case 87
		Return "ThwartFrozen"
	Case 88
		Return "SpellIceWall"
	Case 90
		Return "TeleporterOn"
	Case 91
		Return "TeleporterOff"
	Case 92
		Return "NewGrowFlower"
	Case 93
		Return "FloingBubble"
	Case 95
		Return "MothrshipLoop"
	Case 96
		Return "MothrshipDie"
	Case 97
		Return "LurkerChomp"
	Case 98
		Return "MoobotMove"
	Case 99
		Return "MoobotStop"
	Case 100
		Return "ScritterMove"
	Case 101
		Return "ChomperNyak"
	Case 102
		Return "FireFlwrOn"
	Case 103
		Return "FireFlwrSpit"
	Case 104
		Return "FireFlwrHurt"
	Case 105
		Return "FireFlwrDie"
	Case 106
		Return "ThwartStep"
	Case 107
		Return "ThwartLaugh"
	Case 108
		Return "TurtleGetWet"
	Case 109
		Return "SpikeyBaLoop"
	Case 110
		Return "CuboidBreak"
	Case 111
		Return "TentacleUp"
	Case 112
		Return "TentacleDown"
	Case 113
		Return "IceTrllGrunt"
	Case 114
		Return "IceTrllFroze"
	Case 115
		Return "CrabMove"
	Case 116
		Return "CrabAwaken"
	Case 117
		Return "CrabPowed"
	Case 118
		Return "CoilyBounce"
	Case 119
		Return "MechaNyak"
	Case 120
		Return "Waterfall1"
	Case 121
		Return "DuckQuack"
	Case 122
		Return "Earthquake"
	Case 123
		Return "VoidLoop"
	Case 124
		Return "WaterDroplet"
	Case 125
		Return "Waterfall2"
	Case 126
		Return "Ocean1"
	Case 127
		Return "Ocean2"
	Case 128
		Return "Seagulls1"
	Case 129
		Return "Seagulls2"
	Case 130
		Return "MenuSelect"
	Case 131
		Return "DialogOpen"
	Case 132
		Return "DialogClose"
	Case 133
		Return "VoLoadGame"
	Case 134
		Return "VoSaveGame"
	Case 135
		Return "VoAreUSure?"
	Case 136
		Return "ReplySelect"
	Case 137
		Return "VoPleaseWait"
	Case 138
		Return "DeepWind"
	Case 139
		Return "Harp"
	Case 140
		Return "ZBotElimin8"
	Case 141
		Return "ZBotIAmAZBot"
	Case 142
		Return "ZBotWeAreThe"
	Case 143
		Return "ZBotIntruder"
	Case 144
		Return "ZBotIAmError"
	Case 145
		Return "ZBotNoCmpute"
	Case 146
		Return "ZBotEndOLine"
	Case 147
		Return "ZBotChicken"
	Case 148
		Return "ZBotYourBase"
	Case 149
		Return "ZBotFutile"
	Case 150
		Return "StinkerAAHHH"
	Case 151
		Return "StinkerOw"
	Case 152
		Return "StinkerDrown"
	Case 153
		Return "GettingHot"
	Case 154
		Return "OwHotHotHot"
	Case 155
		Return "Thunder1"
	Case 156
		Return "Thunder2"
	Case 157
		Return "Thunder3"
	Case 158
		Return "ZBotStnkrDie"
	Case 159
		Return "ZBotElimTheS"
	Case 160
		Return "PlayerLose1"
	Case 161
		Return "PlayerLose2"
	Case 162
		Return "PlayerLose3"
	Case 163
		Return "PlayerLose4"
	Case 164
		Return "PlayerStart1"
	Case 165
		Return "PlayerStart2"
	Case 166
		Return "PlayerStart3"
	Case 167
		Return "PlayerStart4"
	Case 168
		Return "PlayerStart5"
	Case 169
		Return "PlayerGreet"
	Case 170
		Return "PlayerSlide1"
	Case 171
		Return "PlayerSlide2"
	Case 172
		Return "PlayerSlide3"
	Case 173
		Return "GetCustItem"
	Case 174
		Return "GetCustItem"
	Case 175
		Return "PlayerAww"
	Case 176
		Return "PlayerUse"
	Case 177
		Return "NPCNice2CU"
	Case 180
		Return "StnkrIceYoof"
	Case 181
		Return "StnkrIceWoo1"
	Case 182
		Return "StnkrIceWoo2"
	Case 187
		Return "NPCHiWatUDng"
	Case 188
		Return "NPCItNice2CU"
	Case 189
		Return "NPCHowRThee"
	Case 190
		Return "NPCHello!"
	Case 191
		Return "NPCHiHowRYa"
	Case 192
		Return "NPCYooloo"
	Case 193
		Return "NPCHloNic2CU"
	Case 194
		Return "NPCHelloFem"
	Case 195
		Return "NPCWatsCookn"
	Case 196
		Return "NPCHello"
	Case 197
		Return "NPCYup"
	Case 198
		Return "NPCWhatUDoin"
	Case 199
		Return "NPCNice2CU!"	
	Default
		Return "N/A"
	End Select

End Function

Function GetStinkerExpressionName$(Value)

	Select Value
	Case 0
		Return "Happy"
	Case 1
		Return "Surprised"
	Case 2
		Return "Sad"
	Case 3
		Return "Asleep"
	Case 4
		Return "Angry"
	Default
		Return Value+"/Unknown"
	End Select

End Function

Function GetStinkerNPCWalkAnimName$(Value)

	Select Value
	Case -1
		Return "All"
	Case 0
		Return "Waddle"
	Case 1
		Return "Walk"
	Case 2
		Return "Run"
	Case 3
		Return "CastSpell1"
	Case 4
		Return "CastSpell2"
	Case 5
		Return "Reach"
	Case 6
		Return "CastSpell4"
	Case 7
		Return "WaveAndDrop"
	Case 8
		Return "FootTap"
	Case 9
		Return "Sway"
	Case 10
		Return "Panic1"
	Case 11
		Return "Dance"
	Case 12
		Return "Sit1"
	Case 13
		Return "Sit2"
	Case 14
		Return "WaveFast"
	Case 15
		Return "PraiseTheSun"
	Case 16
		Return "Sit3"
	Case 17
		Return "SitVibrate"
	Case 18
		Return "Panic2"
	Case 19
		Return "WaveNormal"
	Case 20
		Return "Sit5"
	Case 21
		Return "Sit6"		
	Default
		Return Value+"/None"
	End Select

End Function

Function GetNPCTurningName$(Value)

	tex$=Value+"/Unknown"
	If (Value Mod 10)=0 tex$="Fixed"
	If (Value Mod 10)=1 tex$="Player"
	If (Value Mod 10)=2 tex$="Clock Slow"
	If (Value Mod 10)=3 tex$="Clock Fast"
	If (Value Mod 10)=4 tex$="Count Slow"
	If (Value Mod 10)=5 tex$="Count Fast"
	If Value>=10 And Value<20 tex$=tex$+"Bounce"
	If Value>=20 And Value<30 tex$=tex$+"BouFas"
	If Value>=30 tex$=tex$+"SpdAnm"
	Return tex$

End Function

Function GetStinkerNPCIdleAnimName$(Value)

	Select Value
	Case 0
		Return "Sway"
	Case 1
		Return "WaveSometime"
	Case 2
		Return "WaveConstant"
	Case 3
		Return "FootSometime"
	Case 4
		Return "FootConstant"
	Case 5
		Return "Dance"
	Case 6
		Return "SitConstant"
	Case 7
		Return "Sit/Stand"
	Case 8
		Return "Sit/StandWave"
	Case 9
		Return "PanicSometime"
	Case 10
		Return "PanicConstant"
	Default
		Return Value+"/NotVanilla"
	End Select

End Function

Function GetItemFnName$(value)

	Select value
	Case 1
		Return "OpenRucksack"
	Case 2
		Return "CloseRucksack"
	Case 3
		Return "Objective"
	Case 4
		Return "Menu"
	Case 1001
		Return "Gloves"
	Case 1002,1003,1004,1005,1006,1007,1008,1009,1010
		Return "GloveIcon"+(value-1001)
	Case 2001
		Return "Lamp"
	Case 2002
		Return "LampActive"
	Case 2011
		Return "GlowGem"
	Case 2012
		Return "GlowGemActive"
	Case 2021
		Return "Spy-Eye"
	Case 2022
		Return "Spy-EyeActive"
	Case 2031
		Return "Token"
	Case 2041
		Return "Glyph"
	Case 2051
		Return "MapPiece"
	Case 3001
		Return "Key"
	Case 3011
		Return "Whistle"
	Case 3021
		Return "Shard"
	Case 3091
		Return "WonAdventure"
	Case 4001
		Return "None"
	Case 4101
		Return "None *"
	Case 4201
		Return "None +"
	Case 4011
		Return "Win Adventure"
	Case 4111
		Return "Win Adventure *"
	Case 4211
		Return "Win Adventure +"
	Case 4021
		Return "ID On Local"
	Case 4121
		Return "ID On Local *"
	Case 4221
		Return "ID On Local +"
	Case 4031
		Return "ID Off Local"
	Case 4131
		Return "ID Off Local *"
	Case 4231
		Return "ID Off Local +"
	Case 4041
		Return "ID Tog Local"
	Case 4141
		Return "ID Tog Local *"
	Case 4241
		Return "ID Tog Local +"
	Case 4051
		Return "ID On Global"
	Case 4151
		Return "ID On Global *"
	Case 4251
		Return "ID On Global +"
	Case 4061
		Return "ID Off Global"
	Case 4161
		Return "ID Off Global *"
	Case 4261
		Return "ID Off Global +"
	Case 4071
		Return "ID Tog Global"
	Case 4171
		Return "ID Tog Global *"
	Case 4271
		Return "ID Tog Global +"
	Case 9091
		Return "Empty"
	Default
		Return value
	End Select

End Function

Function GetCMDData1NameAndValue$(Cmd,Data1,Joiner$)

	Return GetCmdData1Name$(Cmd)+Joiner$+GetCmdData1ValueName$(Cmd,Data1)

End Function

Function GetCMDData2NameAndValue$(Cmd,Data2,Joiner$)

	Return GetCmdData2Name$(Cmd)+Joiner$+GetCmdData2ValueName$(Cmd,Data2)

End Function

Function GetCMDData3NameAndValue$(Cmd,Data2,Data3,Joiner$)

	Return GetCmdData3Name$(Cmd)+Joiner$+GetCmdData3ValueName$(Cmd,Data2,Data3)

End Function

Function GetCMDData4NameAndValue$(Cmd,Data4,Joiner$)

	Return GetCmdData4Name$(Cmd)+Joiner$+GetCmdData4ValueName$(Cmd,Data4)

End Function

Function GetCmdData1ExtraInfo$(Cmd,Data1)

	Select Cmd
	Case 21,22,23,24,25,26,27
		Return PreviewDialog$(Data1,0)
	End Select
	
	Return ""

End Function

Function GetCmdData2ExtraInfo$(Cmd,Data1,Data2)

	Select Cmd
	Case 21,22
		If Data2>-1
			Return PreviewDialog$(Data1,Data2)
		EndIf
	Case 23,24,25,26,27
		Return PreviewAskAbout$(Data1,Data2)
	End Select
	
	Return ""

End Function

Function GetCmdData3ExtraInfo$(Cmd,Data1,Data3)

	Select Cmd
	Case 27
		Return PreviewDialog$(Data1,Data3)
	End Select
	
	Return ""

End Function

Function WithJoinerIfNotEmpty$(TheString$,Joiner$)

	If TheString$=""
		Return TheString$
	Else
		Return Joiner$+TheString$
	EndIf

End Function

Function GetTypeString$(value)

	Select value
	Case 0
		Return "None"
	Case 1
		Return "Player"
	Case 10
		Return "Gate"
	Case 11
		Return "Tollgate"
	Case 20
		Return "Fire Trap"
	Case 30
		Return "Teleporter"
	Case 40
		Return "SteppingStone"
	Case 45
		Return "Conveyor Lead"
	Case 46
		Return "Conveyor Tail"
	Case 50
		Return "Spellball"
	Case 51
		Return "Magic Shooter"
	Case 52
		Return "Meteor Shooter"
	Case 53
		Return "Meteorite"
	Case 54
		Return "Magic Mirror"
	Case 60
		Return "IceCub/FlngBub"
	Case 70
		Return "BetaPickupItem"
	Case 71
		Return "BetaUsedItem"
	Case 80,81
		Return "WallblockNever"
	Case 82,83
		Return "KeyblockNever"
	Case 84
		Return "KeyblockSpam"
	Case 85
		Return "WallblockOnce"
	Case 86
		Return "WallblockSpam"
	Case 87
		Return "KeyblockOnce"
	Case 90
		Return "Button"
	Case 100
		Return "StinkerHatAcc"
	Case 101
		Return "Shadow"
	Case 110;,111,112,113,114,115,116,117,118,119
		Return "Stinker NPC"
	Case 120
		Return "Wee Stinker"
	Case 130
		Return "Stinker Exit"
	Case 140
		Return "Cage"
	Case 150
		Return "Scritter"
	Case 151
		Return "RainbowBubble"
	Case 160
		Return "SolidScenery"
	Case 161
		Return "Waterfall"
	Case 162
		Return "Cottage"
	Case 163
		Return "WindmillRotor"
	Case 164
		Return "Fountain"
	Case 165
		Return "ArcadeMachine"
	Case 166
		Return "SkyMachineMap"
	Case 170
		Return "Gold Star"
	Case 171
		Return "Coin"
	Case 172
		Return "Key/Keycard"
	Case 173
		Return "Gem"
	Case 174
		Return "Token"
	Case 179
		Return "Custom Item"
	Case 180
		Return "Sign"
	Case 190
		Return "ParticleSpawn"
	Case 200
		Return "Magic Charger"
	Case 210
		Return "Transporter"
	Case 220
		Return "Turtle"
	Case 230
		Return "FireFlower"
	Case 240
		Return "Barrel Reg"
	Case 241
		Return "Barrel TNT"
	Case 242
		Return "Cuboid"
	Case 250
		Return "Chomper"
	Case 260
		Return "Spikeyball"
	Case 270
		Return "Btrfly/Glworm"
	Case 271
		Return "Zipper"
	Case 280
		Return "Spring"
	Case 281
		Return "Suctube -"	
	Case 282
		Return "Suctube X"
	Case 290
		Return "Thwart"
	Case 300
		Return "Brr Float"
	Case 301
		Return "RainbowFloat"
	Case 310
		Return "RubberDucky"
	Case 320
		Return "Void"
	Case 330
		Return "Wysp"
	Case 340
		Return "Tentacle"
	Case 350
		Return "GrowFlower"
	Case 360
		Return "FloingBubble"
	Case 370
		Return "Crab"
	Case 380
		Return "Ice Troll"
	Case 390
		Return "Kaboom NPC"
	Case 400
		Return "Baby Boomer"
	Case 410
		Return "Flipbridge"
	Case 420
		Return "Coily"
	Case 421
		Return "Scouge"
	Case 422
		Return "Retro UFO"
	Case 423
		Return "Retro Z-Bot"
	Case 424
		Return "Laser Gate"
	Case 425
		Return "Rainbow Coin"
	Case 426
		Return "RetroTollgate"
	Case 430
		Return "Zipbot"
	Case 431
		Return "Zapbot"
	Case 432
		Return "Moobot"
	Case 433
		Return "Z-Bot NPC"
	Case 434
		Return "Mothership"
	Case 441
		Return "Sun Sphere 1"
	Case 442
		Return "Sun Sphere 2"
	Case 450
		Return "Lurker"
	Case 460
		Return "Burstflower"
	Case 470
		Return "Ghost"
	Case 471
		Return "Wraith"
	Case 472
		Return "MODDED NPC"
	Case 805
		Return "IceBlk Again?"
		
	Default	
		Return "NotVanilla"
		
	End Select

End Function

Function GetDirectionString$(DisplayedRotation)

	If DisplayedRotation=0
		Return "North"
	ElseIf DisplayedRotation=45
		Return "Northeast"
	ElseIf DisplayedRotation=90
		Return "East"
	ElseIf DisplayedRotation=135
		Return "Southeast"
	ElseIf DisplayedRotation=180
		Return "South"
	ElseIf DisplayedRotation=225
		Return "Southwest"
	ElseIf DisplayedRotation=270
		Return "West"
	ElseIf DisplayedRotation=315
		Return "Northwest"
	Else
		Return DisplayedRotation
	EndIf

End Function

Function GetCommandColor(id,index)

	dark=100

	Select id
	Case 5 ; death
		r=GetAnimatedFlashing(LevelTimer)
		g=60
		b=60
	Case 7,8,101,102,103,104,115,120 ; change level
		; red
		r=255
		g=0
		b=0
	Case 21,22,23,24,25,26,27,28,29,30,118 ; dialogs
		; orange
		r=255
		g=dark
		b=0
	Case 61,62,63,64,65 ; NPC manipulation
		; yellow
		r=255
		g=255
		b=0
	Case 1,3 ; change object interactivity
		; green
		r=0
		g=255
		b=0
	Case 2
		; dark green
		r=0
		g=dark
		b=0
	Case 6,9,10,11,12,13,14,16 ; environment
		; cyan
		r=0
		g=255 ;200 ;120 ;dark ;255
		b=255
	Case 4,17,18,51,52 ; alter object attributes / dark magic
		; indigo
		r=0
		g=100
		b=255
		
		; #e079cb
		;r=224 ;0
		;g=121 ;0
		;b=203 ;255
	Case 15,41,42,119 ; object spawning
		; purple
		r=255
		g=0
		b=255
	Case 111,112,113,114,116,117 ; global state
		; white
		r=255
		g=255
		b=255
	Default ; not a valid command
		; gray
		r=dark
		g=dark
		b=dark
	End Select
	
	If index=0
		Return r
	ElseIf index=1
		Return g
	Else
		Return b
	EndIf
	
End Function

Function HubChecks()

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
	
	If IsHubMissingAdventures()
		Print "ERROR: Hub is missing adventures (see red names in hub editor)."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	Return True

End Function

Function BuildHub()
	Cls
	Locate 0,0
	Print ""
	Print "Building..."
	Print "------------"
	Print ""
	Print ""
	
	If HubChecks()=False
		Return False
	EndIf
	
	fn$=HubTitle$
	If HubDescription$<>""
		fn$=HubTitle$+"#"+HubDescription$
	EndIf
	HubDir$=globaldirname$+"\Custom\hubs\"+fn$
	CreateDir(HubDir$)
	
	If FileType(HubDir$)<>2
		Print "ERROR: Unable to create directory."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	; clear directory first
	dirfileclear=ReadDir(HubDir$)
	Repeat
		f$=NextFile$(dirfileclear)
		If FileType(HubDir$+"\"+f$)=1 And f$<>""
			DeleteFile HubDir$+"\"+f$
		ElseIf FileType(HubDir$+"\"+f$)=2 And f$<>"." And f$<>".." And f$<>""
			dirfileclearsub=ReadDir(HubDir$+"\"+f$)
			Repeat
				f1$=NextFile$(dirfileclearsub)
				If FileType(HubDir$+"\"+f$+"\"+f1$)=1
					DeleteFile HubDir$+"\"+f$+"\"+f1$
				EndIf
			Until f1$=""
			DeleteDir HubDir$+"\"+f$
		EndIf
	Until f$=""
	;WaitKey()
	
	NofWlvFiles=0
	NofDiaFiles=0
	
	; copy files
	For i=0 To HubTotalAdventures
		AdvFilename$=""
		If i=0
			AdvFilename$="Hub"
		Else
			AdvFilename$="Adventure"+Str(i)
		EndIf
		If HubAdventuresFilenames$(i)<>""
			
			CreateDir(HubDir$+"\"+AdvFilename$)
			dirfile=ReadDir(GetHubAdventurePath$(HubAdventuresFilenames$(i)))
			Print "Building "+AdvFilename$+"..."

			Repeat
				ex$=NextFile$(dirfile)
				If FileSatisfiesCompiler(ex$,HubAdventuresIncludeInTotals(i))
					Print "Copying... "+ex$
					CopyFile GetHubAdventurePath$(HubAdventuresFilenames$(i))+"\"+ex$, HubDir$+"\"+AdvFilename$+"\"+ex$
				EndIf
			Until ex$=""
			
		EndIf
	Next
	
	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		Print "Copying hublogo.jpg..."
		CopyFile globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg", HubDir$+"\hublogo.jpg"
	EndIf
	
	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")
		Print "Copying wonderlandadventures.bmp..."		CopyFile globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp", HubDir$+"\wonderlandadventures.bmp"
	EndIf
	
	Print ""
	Print ""
	Color 0,255,0
	Print NofWlvFiles+" wlv files and "+NofDiaFiles+" dia files in total."
	Color 255,255,255
	Print ""
	Print ""
	
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
	
	If HubChecks()=False
		Return False
	EndIf
	
	fn$=HubTitle$
	If HubDescription$<>""
		fn$=HubTitle$+"#"+HubDescription$
	EndIf
	
	NofWlvFiles=0
	NofDiaFiles=0

	For i=0 To HubTotalAdventures
		AdvFilename$=""
		If i=0
			AdvFilename$="Hub"
		Else
			AdvFilename$="Adventure"+Str(i)
		EndIf
		If HubAdventuresFilenames$(i)<>""
			
			ThePath$=GetHubAdventurePath$(HubAdventuresFilenames$(i))
			If PackContent
				SearchForCustomContent(ThePath$)
			EndIf
			dirfile=ReadDir(ThePath$)
			Print ""
			Print "Reading "+AdvFilename$+"..."
			NofHubCompilerFiles(i)=0
			Repeat
				ex$=NextFile$(dirfile)
				If FileSatisfiesCompiler(ex$,HubAdventuresIncludeInTotals(i))
					Print "Reading... "+ex$
					HubCompilerFileName$(i,NofHubCompilerFiles(i))=ex$
					HubCompilerFileSize(i,NofHubCompilerFiles(i))=FileSize(ThePath$+"\"+ex$)
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
		
				file2=ReadFile(GetHubAdventurePath$(HubAdventuresFilenames$(k))+"\"+HubCompilerFileName$(k,i))
				
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
	
	Print ""
	Print ""
	Color 0,255,0
	Print NofWlvFiles+" wlv files and "+NofDiaFiles+" dia files in total."
	Color 255,255,255
	
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

Function MyCreateDir(dirpath$)
	; creates all the sub directories along with their parent directories
	For i=1 To Len(dirpath$)
		If Mid$(dirpath$,i,1)=Chr$(47) Then
			folder$=Left$(dirpath$,i-1)
			d=ReadDir(folder$)
			If d Then 
				CloseDir(d)
			Else
			    CreateDir(folder$)
				If FileType(folder$)=0 Then RuntimeError("Couldn't create directory: "+folder$)
			EndIf
		EndIf
	Next
End Function

Function CleanDir(ex$)
	; completely cleans the specified directory and also deletes it
	dir=ReadDir(ex$)
	Repeat
		ex2$=NextFile$(dir)
		If ex2$<>"" And ex2$<>"." And ex2$<>".."
			filename$=ex$+Chr$(47)+ex2$
			If FileType(filename$)=1
				DeleteFile filename$
			Else If FileType(filename$)=2
				CleanDir(filename$)
			EndIf
		EndIf
	Until ex2$=""
	DeleteDir ex$
End Function

Function GetFileNameFromPath$(path$)
	; fetches the exact file name from path and removes the rest of the path
	For i=Len(path$) To 1 Step -1
		If Mid$(path$,i,1)=Chr$(47) Or Mid$(path$,i,1)=Chr$(47)
			Return Right$(path,Len(path$)-i)
		EndIf
	Next
End Function

Function CompileAdventure(PackCustomContent)

	AdventureFileNameWithoutAuthor$=AdventureTitleWithoutAuthor$(AdventureFileName$)

	If AdventureFileNameWithoutAuthor$=AdventureFileName$
		OutputFileName$=AdventureUserName$+"#"+AdventureFileName$+".wa3"
	Else
		OutputFileName$=AdventureFileName$+".wa3"
	EndIf

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
	
	AdventureFolder$=GetAdventureFolder$()
	
	If FileType(AdventureFolder$+"\1.wlv")=0		
		Print "ERROR: No Level 1 present."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
	
	If PackCustomContent
		; find custom content
		Print "Finding Custom Content..."
		SearchForCustomContent(AdventureFolder$)
		Print "Number of Custom Content Files: " + NofCustomContentFiles
		Print
	EndIf
	
	; now go through directory and check names and sizes of files
	dirfile=ReadDir(AdventureFolder$)
	
	NofCompilerFiles=0
	NofWlvFiles=0
	NofDiaFiles=0
	
	Repeat
		ex$=NextFile$(dirfile)
		
		If FileSatisfiesCompiler(ex$,True)
			Print "Reading... "+ex$
			CompilerFileName$(NofCompilerFiles)=ex$
			CompilerFileSize(NofCompilerFiles)=FileSize(AdventureFolder$+"\"+ex$)
			NofCompilerFiles=NofCompilerFiles+1			
		EndIf
	Until ex$=""
	
	If PackCustomContent And NofCustomContentFiles>0
		; read custom content
		Print "Copying custom content files to: "+GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content"
		If FileType(GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content")=2
			CleanDir(GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content")
		EndIf
		CreateDir(GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content")
		For i=0 To NofCustomContentFiles-1
			Print "Copying... "+GetFileNameFromPath$(CustomContentFile$(i))
			MyCreateDir GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content/"+CustomContentFile$(i)
			CopyFile CustomContentFile$(i),GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content/"+CustomContentFile$(i)
		Next
		; now cleanup old Values
		For i=0 To 499
			CustomContentFile$(i)=""
		Next
		Print ""
		Print ""
	EndIf
	
	Delay 1000
	; and now make the master file
	Print ""
	Print ""
	Print "Writing WA3 File to Downloads Inbox..."
	Print ""
	Print ""
	file1=WriteFile(globaldirname$+"\Custom\downloads inbox\"+OutputFileName$)
	If file1=0
		Print "ERROR: Cannot Write "+globaldirname$+"\Custom\downloads inbox\"+OutputFileName$
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf
		
	WriteInt file1,NofCompilerFiles

	For i=0 To NofCompilerFiles-1
		Print "Writing... "+CompilerFileName$(i)
		
		WriteString file1,CompilerFileName$(i)
		WriteInt file1,CompilerFileSize(i)

		file2=ReadFile(AdventureFolder$+"\"+CompilerFileName$(i))
		If Not file2
			file2=ReadFile(CompilerFileName$(i))
		EndIf
		
		For j=0 To CompilerFileSize(i)-1
			WriteByte file1,ReadByte (file2)
		Next
		
		CloseFile file2
	Next
	CloseFile file1
	
	Print ""
	Print ""
	Color 0,255,0
	Print NofWlvFiles+" wlv files and "+NofDiaFiles+" dia files in total."
	Color 255,255,255
	
	Delay 1000
	Print ""
	Print ""
	Print "Copying File to Downloads Outbox..."
	CopyFile globaldirname$+"\Custom\downloads inbox\"+OutputFileName$,globaldirname$+"\Custom\downloads outbox\"+OutputFileName$
	Print ""
	Print ""
	Delay 1000
	Print "Compile Completed... Filename: "+OutputFileName$
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
		If ObjectHasShadow(LevelObjects(i)\Attributes\ModelName$)
			Result=Result+1
		EndIf
		Result=Result+ObjectCountAccessories(LevelObjects(i)\Attributes)
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
		If ObjectHasShadow(LevelObjects(i)\Attributes\ModelName$)
			Result=Result-1
		EndIf
		Result=Result-ObjectCountAccessories(LevelObjects(i)\Attributes)
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

; Reflects the in-game logic for spawning shadows (see CreateObjectShadow in adventures.bb).
Function ObjectHasShadow(ModelName$)

	Select ModelName$

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
	Case "!NPC" ; Normally this shadow is created by the CreateStinkerModel function rather than CreateObjectShadow.
		Return True
	Default
		Return False
	End Select

End Function


Function ObjectCountAccessories(Attributes.GameObjectAttributes)

	If Attributes\ModelName$="!NPC"
		Code$="!T"
		If Attributes\Data0<10
			Code$=Code$+"00"+Str$(Attributes\Data0)
		Else If Attributes\Data0<100
			Code$=Code$+"0"+Str$(Attributes\Data0)
		Else
			Code$=Code$+Str$(Attributes\Data0)
		EndIf
		Code$=Code$+Chr$(65+Attributes\Data1)
		If Attributes\Data2>0
			If Attributes\Data2<10
				 Code$=Code$+"00"+Str$(Attributes\Data2)
			Else If Attributes\Data2<100
				 Code$=Code$+"0"+Str$(Attributes\Data2)
			Else
				 Code$=Code$+""+Str$(Attributes\Data2)
			EndIf
			Code$=Code$+Chr$(64+Attributes\Data3)+"0"
		EndIf
		If Attributes\Data4>0 And Attributes\Data2>0 Then Code$=Code$+" "
		If Attributes\Data4>0
			If Attributes\Data4<10
				 Code$=Code$+"00"+Str$(Attributes\Data4)
			Else If Attributes\Data4<100
				 Code$=Code$+"0"+Str$(Attributes\Data4)
			Else
				 Code$=Code$+""+Str$(Attributes\Data4)
			EndIf
			Code$=Code$+Chr$(65+Attributes\Data5)+"0"
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

; Returns False if the current object Type has no default TrueMovement.
Function RetrieveDefaultTrueMovement()

	Select CurrentObject\Attributes\LogicType
	
	Case 1 ; Player
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^3+2^4+2^5+2^6+2^8
	
	Case 50 ; Spellball
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^13+2^14
		CurrentObject\Attributes\ObjectTypeCollision=0 ; -1 in-game, but probably doesn't make a difference.
	
	Case 110 ; Stinker NPC
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6
		;If CurrentObject\Attributes\MoveXGoal=0 And CurrentObject\Attributes\MoveYGoal=0
			;CurrentObject\Attributes\MoveXGoal=Floor(CurrentObject\Position\X)
			;CurrentObject\Attributes\MoveYGoal=Floor(CurrentObject\Position\Y) 
			;CurrentObject\Attributes\CurrentAnim=10
		;EndIf
	
	Case 120 ; Wee Stinker
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=35
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6+2^8
		
		CurrentObject\Attributes\XScale=0.025
		CurrentObject\Attributes\YScale=0.025
		CurrentObject\Attributes\ZScale=0.025
	
	Case 150 ; Scritter
		CurrentObject\Attributes\MovementSpeed=50
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6
		
	Case 151 ; Rainbow Bubble
		CurrentObject\Attributes\MovementType=33
		CurrentObject\Attributes\MovementSpeed=25
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		
	Case 220 ; Dragon Turtle
		CurrentObject\Attributes\MovementType=41+CurrentObject\Attributes\Data0*2+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=25
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6
		
	Case 230 ; Fireflower
		CurrentObject\Attributes\TileTypeCollision=2^0
		
	Case 250 ; Chomper
		CurrentObject\Attributes\MovementType=13
		CurrentObject\Attributes\MovementSpeed=20+5*CurrentObject\Attributes\Data0
		
		If CurrentObject\Attributes\LogicSubType=0 ; Non-Water Chomper
			CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		Else ; Water Chomper
			CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14	
		EndIf
		
		If CurrentObject\Attributes\Data1=1 ; Ghost Chomper
			CurrentObject\Attributes\ObjectTypeCollision=2^1+2^4+2^6
		Else ; Non-Ghost Chomper
			CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6
		EndIf
		
	Case 260 ; Spikeyball
		CurrentObject\Attributes\MovementSpeed=25+5*CurrentObject\Attributes\Data2
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^2+2^3+2^6+2^9
		
		Data0=CurrentObject\Attributes\Data0 Mod 8
		If CurrentObject\Attributes\Data1=0 Or CurrentObject\Attributes\Data1=1
			; zbot movement
			CurrentObject\Attributes\MovementType=41+Data0*2+CurrentObject\Attributes\Data1
		Else If CurrentObject\Attributes\Data1=2
			; bounce movement
			CurrentObject\Attributes\MovementType=71+Data0	
		EndIf
		
	Case 270 ; Busterfly/Glowworm
	
		CurrentObject\Attributes\TileTypeCollision=1 ; -1 in-game, but probably doesn't matter.
		
		If CurrentObject\Attributes\ModelName$="!Busterfly"

			CurrentObject\Attributes\XScale=.01
			CurrentObject\Attributes\YScale=.01
			CurrentObject\Attributes\ZScale=.01
			CurrentObject\Attributes\Roll2=90
			
		EndIf
		
	Case 271 ; Zipper
	
		CurrentObject\Attributes\TileTypeCollision=1 ; -1 in-game, but probably doesn't matter.
		CurrentObject\Attributes\Data1=Rand(0,360)
		CurrentObject\Attributes\Data2=Rand(1,4)
	
	Case 290 ; Thwart
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^4+2^6
		;If CurrentObject\Attributes\MoveXGoal=0 And CurrentObject\Attributes\MoveYGoal=0
			;CurrentObject\Attributes\MoveXGoal=Floor(CurrentObject\Position\X)
			;CurrentObject\Attributes\MoveYGoal=Floor(CurrentObject\Position\Y) 
			;CurrentObject\Attributes\CurrentAnim=10
		;EndIf
		
	Case 310 ; Rubberducky
		CurrentObject\Attributes\MovementSpeed=4
		CurrentObject\Attributes\TileTypeCollision=2^2 ; -1 in-game, but probably doesn't make a difference.
		CurrentObject\Attributes\Data1=Rand(1,3)
		CurrentObject\Attributes\Data2=Rand(0,360)
		
	Case 330 ; Wysp
		CurrentObject\Attributes\MovementType=10
		CurrentObject\Attributes\MovementSpeed=45
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6+2^8
		
	Case 370 ; Crab
		CurrentObject\Attributes\MovementSpeed=40
		CurrentObject\Attributes\ObjectTypeCollision=2^6
		
		If CurrentObject\Attributes\LogicSubType=0 ; green
			CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
			Select CurrentObject\Attributes\Data1
			Case 0
				; normal
				CurrentObject\Attributes\MovementType=0
			Case 1
				; curious
				CurrentObject\Attributes\MovementType=14
			Case 2,3
				; asleep
				CurrentObject\Attributes\MovementType=0
				;AnimateMD2 ObjectEntity(i),3,1,48,49
			End Select
			CurrentObject\Attributes\XScale=0.006
			CurrentObject\Attributes\YScale=0.006
			CurrentObject\Attributes\ZScale=0.006
		Else ; red
			CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
			Select CurrentObject\Attributes\Data1
			Case 0
				; normal
				CurrentObject\Attributes\MovementType=32
			Case 1
				; curious
				CurrentObject\Attributes\MovementType=14
			Case 2,3
				; asleep
				CurrentObject\Attributes\MovementType=0
				;AnimateMD2 ObjectEntity(i),3,1,48,49
			End Select

		EndIf
		
	Case 380 ; Ice Troll
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^4+2^6
		
	Case 390 ; Kaboom! NPC
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6
		
	Case 400 ; Baby Boomer
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=35
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6+2^8
		
	Case 420 ; Coily
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=30
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^9+2^10+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6
	
	Case 422 ; UFO
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=20
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^3+2^6
	
	Case 423 ; Retro Z-Bot
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=60
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6
		
	Case 430 ; Zipbot
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=120
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6
		
	Case 431 ; Zapbot
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=20*CurrentObject\Attributes\Data2
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^3+2^6
		
	Case 432 ; Moobot
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=60
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6
		
		CurrentObject\Attributes\ID=500+CurrentObject\Attributes\Data0*5+CurrentObject\Attributes\Data1
		
	Case 433 ; Z-Bot NPC
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6
		
	Case 434 ; Mothership
		CurrentObject\Attributes\MovementSpeed=10
		CurrentObject\Attributes\TileTypeCollision=0
		CurrentObject\Attributes\ObjectTypeCollision=0
		
		CurrentObject\Attributes\Data1=-1
		CurrentObject\Position\Z=4
		
	Case 470 ; Ghost
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=5+5*CurrentObject\Attributes\Data1
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6
		
	Case 471 ; Wraith
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=20+5*CurrentObject\Attributes\Data0
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6
		
	Default
		Return False
	End Select
	
	CurrentObjectWasChanged()
	BuildCurrentObjectModel()
	Return True

End Function


Function TurnObjectTowardDirection(i,dx#,dy#,speed,Adjust)
	
	; Turns Object by speed degrees toward the angle made by dx/dy. 
	; Adjust is a fixed angle added to goal (in case the base models orientation is off, for example) 
	
	If dx<>0 Or dy<>0
		
		ObjectYawGoal=ATan2(-dy,dx)+90+Adjust
		While ObjectYawGoal>180 
			ObjectYawGoal=ObjectYawGoal-360
		Wend
		While ObjectYawGoal<=-180 
			ObjectYawGoal=ObjectYawGoal+360
		Wend

		If Abs(ObjectYawGoal-SimulatedObjectYaw(i))>speed
			dyaw=speed
		Else
			dyaw=1
		EndIf

		If SimulatedObjectYaw(i)>ObjectYawGoal
			If SimulatedObjectYaw(i)-ObjectYawGoal<180
				SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-dyaw
			Else
				SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+dyaw
			EndIf
		Else If SimulatedObjectYaw(i)<ObjectYawGoal
			If ObjectYawGoal-SimulatedObjectYaw(i)<180
				SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+dyaw
			Else
				SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-dyaw
			EndIf
		EndIf
		If SimulatedObjectYaw(i)>180 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-360
		If SimulatedObjectYaw(i)<-180 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+360
		
	EndIf

End Function

Function PlayerTileX()

	Return BrushCursorX

End Function

Function PlayerTileY()

	; Make player-facing objects turn approximately south when cursor is not present.
	If BrushCursorY=BrushCursorInvalid
		Return 100000000
	Else
		Return BrushCursorY
	EndIf

End Function

Function PlayerX#()
	
	Return PlayerTileX()+0.5
	
End Function

Function PlayerY#()
	
	Return PlayerTileY()+0.5
	
End Function


Function ControlParticleEmitters(i)
	
	;If ObjectActive(i)=0 Then Return
	
	TheX#=SimulatedObjectXAdjust(i)+LevelObjects(i)\Position\TileX+0.5
	TheY#=SimulatedObjectYAdjust(i)+LevelObjects(i)\Position\TileY+0.5
	TheZ#=SimulatedObjectZAdjust(i)
	
	Select SimulatedObjectSubType(i)
	Case 1
		; steam
		If SimulatedObjectStatus(i)=0
			; not steaming - check if start
			If Rand(0,400)<=SimulatedObjectData(i,1)*2
				SimulatedObjectStatus(i)=1
			EndIf
		Else
			If Rand(0,200*SimulatedObjectData(i,1))<2
				
				SimulatedObjectStatus(i)=0
				
			EndIf
			Select SimulatedObjectData(i,2)
			Case 0
				If Rand(0,25)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,.03,0,0,.01,0,0,0,100,3)
			Case 1
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,-.03,0,0,.01,0,0,0,100,3)
			Case 2
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.03,0,0,0,.01,0,0,0,100,3)
			Case 3
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,-.03,0,0,0,.01,0,0,0,100,3)
			Case 4
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,0,.03,0,.01,0,0,0,100,3)
			Case 5
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,0,-.03,0,.01,0,0,0,100,3)
			End Select
					
				
		EndIf
	Case 2
		; splish
		If Rand (0,1000)<=SimulatedObjectData(i,1)*2
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.01,0,0,0,0,.01,0,0,0,100,4)
		EndIf
		
	Case 3
		; fountain
		Select SimulatedObjectData(i,2)
		Case 0
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(-.01,.01),Rnd(.05,.07),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 1
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(-.01,.01),-Rnd(0,.02),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 2
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(.05,.07),Rnd(.02,.01),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 3
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,-Rnd(.05,.07),Rnd(.02,.01),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 4
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(-.01,.01),Rnd(.02,.01),Rnd(.05,.07),0,.001,0,-.001,0,100,3)
		Case 5
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(-.01,.01),Rnd(.02,.01),-Rnd(.05,.07),0,.001,0,-.001,0,100,3)
		End Select
		
	Case 4
		; sparks
		If Rand(0,1000)<SimulatedObjectData(i,1)*SimulatedObjectData(i,1)
			For j=0 To SimulatedObjectData(i,1)*30
				Select SimulatedObjectData(i,2)
				Case 0
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(-.01,.01),Rnd(.09,.11),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 1
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(-.01,.01),-Rnd(0,.02),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 2
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(.01,.04),Rnd(.03,.01),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 3
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,-Rnd(.01,.04),Rnd(.03,.01),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 4
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(-.01,.01),Rnd(.03,.01),Rnd(.01,.04),0,.0001,0,-.0015,0,50,3)
				Case 5
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(-.01,.01),Rnd(.03,.01),-Rnd(.01,.04),0,.0001,0,-.0015,0,50,3)
				End Select
			Next
		EndIf
	Case 5
		; blinker
		If (SimulatedObjectData(i,4)=0 And Rand(0,200)<SimulatedObjectData(i,1)) Or (SimulatedObjectData(i,4)=1 And LevelTimer Mod (500-SimulatedObjectData(i,1)*100)=0)

			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.4,0,0,0,0,.0005,0,0,0,100,3)
			
		EndIf
		
	Case 6
		; circle
		If (SimulatedObjectData(i,4)=0 And Rand(0,200)<SimulatedObjectData(i,1)) Or (SimulatedObjectData(i,4)=1 And LevelTimer Mod (500-SimulatedObjectData(i,1)*100)=0)

			For j=0 To 44
				Select SimulatedObjectData(i,2)
				Case 0,1
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.01*SimulatedObjectData(i,1)*Cos(j*8),0,.01*SimulatedObjectData(i,1)*Sin(j*8),0,.001,0,0,0,100,3)
				Case 2,3
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,.01*SimulatedObjectData(i,1)*Cos(j*8),.01*SimulatedObjectData(i,1)*Sin(j*8),0,.001,0,0,0,100,3)
				Case 4,5
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.01*SimulatedObjectData(i,1)*Cos(j*8),.01*SimulatedObjectData(i,1)*Sin(j*8),0,0,.001,0,0,0,100,3)
				
				End Select
			Next
		EndIf

		
	Case 7
		; spiral
		Select SimulatedObjectData(i,2)
		Case 0
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 2
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 4
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,0,.001,0,0,0,100,3)
		
		Case 1
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,-.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 3
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,-.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 5
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,-.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,0,.001,0,0,0,100,3)

		End Select
	
	
	End Select

End Function


Function ControlWaterfall(i)

	If SimulatedObjectYawAdjust(i)=0
		k1=1
		k2=0
	EndIf
	If SimulatedObjectYawAdjust(i)=90
		k1=0
		k2=1
	EndIf
	If SimulatedObjectYawAdjust(i)=-90 Or SimulatedObjectYawAdjust(i)=270
		k1=0
		k2=-1
	EndIf

	If Rand(0,100)<10  	
		If SimulatedObjectData(i,0)=1
			AddParticle(1,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.55,0.6),SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*Rnd(0.55,0.6),0,.11,k1*Rnd(-.005,0.005)+k2*Rnd(0,.005),Rnd(0.01,0.03),-k1*Rnd(0,.001)+k2*Rnd(-.005,0.005),0,0,0,-0.0004,0,100,3)
		Else 
			AddParticle(5,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.55,0.6),SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*Rnd(0.55,0.6),0,.11,k1*Rnd(-.005,0.005)+k2*Rnd(0,.005),Rnd(0.01,0.03),-k1*Rnd(0,.001)+k2*Rnd(-.005,0.005),0,0,0,-0.0004,0,100,3)
		EndIf
	EndIf

	If Rand(0,100)<3 
		If SimulatedObjectData(i,0)=0
			AddParticle(6,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(SimulatedObjectZAdjust(i),SimulatedObjectZAdjust(i)+SimulatedObjectZScale(i)/2.0),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		Else If SimulatedObjectData(i,0)=1
			AddParticle(24,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(SimulatedObjectZAdjust(i),SimulatedObjectZAdjust(i)+SimulatedObjectZScale(i)/2.0),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		Else
			AddParticle(27,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(SimulatedObjectZAdjust(i),SimulatedObjectZAdjust(i)+SimulatedObjectZScale(i)/2.0),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		EndIf
	EndIf
	If Rand(0,100)<10 
		If SimulatedObjectData(i,0)=1
			AddParticle(32,SimulatedObjectX(i)+k1*Rnd(-.35*SimulatedObjectXScale(i),.35*SimulatedObjectXScale(i))+k2*0.5,(-.2*SimulatedObjectZScale(i))+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+k2*Rnd(-.35*SimulatedObjectXScale(i),.35*SimulatedObjectXScale(i))-k1*0.5,0,.1,0,0,0,0,.012,0,0,0,100,4)
		Else 
			AddParticle(4,SimulatedObjectX(i)+k1*Rnd(-.35*SimulatedObjectXScale(i),.35*SimulatedObjectXScale(i))+k2*0.5,(-.2*SimulatedObjectZScale(i))+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+k2*Rnd(-.35*SimulatedObjectXScale(i),.35*SimulatedObjectXScale(i))-k1*0.5,0,.2,0,0,0,0,.012,0,0,0,100,4)
		EndIf
	EndIf


End Function


Function ControlVoid(i)

	Obj.GameObject=LevelObjects(i)

	If SimulatedObjectData(i,0)=0
		SimulatedObjectData(i,0)=Rand(1,360)
	EndIf

	SimulatedObjectZ(i)=-.2
	
	PositionTexture voidtexture,((leveltimer/10) Mod 100)/100.0,((leveltimer/10) Mod 100)/100.0
	
	SimulatedObjectXScale(i)=(.8+.4*Sin((leveltimer+SimulatedobjectData(i,0)) Mod 360))*(1.0+Float(SimulatedObjectData(i,2)))
	SimulatedObjectYScale(i)=(.8+.4*Sin((leveltimer+SimulatedobjectData(i,0)) Mod 360))*(1.0+Float(SimulatedObjectData(i,2)))

	SimulatedObjectZScale(i)=(1.3+.6*Sin((leveltimer*2+SimulatedobjectData(i,0)) Mod 360));*(1.0+Float(ObjectData(i,2)))

		
	TurnEntity Obj\Model\Entity,0,.1,0
	
	If Obj\Attributes\ModelName$="!Void"
		surface=GetSurface(Obj\Model\Entity,1)
		For i=0 To 17
			VertexCoords surface,i*2,VertexX(surface,i*2),(1+.6*Sin(i*80+((LevelTimer*4) Mod 360))),VertexZ(surface,i*2)
		Next
		For i=18 To 35
			VertexCoords surface,i*2,VertexX(surface,i*2),(.5+.4*Sin(i*160+((LevelTimer*4) Mod 360))),VertexZ(surface,i*2)
		Next
	EndIf

	
End Function


Function ControlTeleporter(i)

	If Rand(0,100)<5 And (SimulatedObjectActive(i)>0 Or SimulationLevel<2)
		a=Rand(0,360)
		b#=Rnd(0.002,0.006)
		AddParticle(23,SimulatedObjectX(i)+0.5*Sin(a),0,-SimulatedObjectY(i)-0.5*Cos(a),0,.2,b*Sin(a),0.015,-b*Cos(a),1,0,0,0,0,150,3)
	EndIf
	
	MyId=CalculateEffectiveID(LevelObjects(i)\Attributes)
	
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

	ModelName$=LevelObjects(i)\Attributes\ModelName$

	; (no control, but used to adjust leveltilelogic)
	If ModelName$="!Obstacle03" ; volcano
		If Rand(0,40)=0
			AddParticle(Rand(24,26),SimulatedObjectX(i)+Rnd(-.7,.7),1.8+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+Rnd(-.9,.7),0,.2,0,Rnd(0.01,0.03),0,0,.03,0,0,0,100,3)
		EndIf
		If Rand(0,10)=0
			If Rand(0,5)=0
				part22=1
			Else
				part22=0
			EndIf
			AddParticle(part22,SimulatedObjectX(i)+Rnd(-.3,.3),1.5+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+Rnd(-.5,.3),0,.6,0,Rnd(0.01,0.03),0,0,.01,0,0,0,100,3)
		EndIf
	Else If ModelName$="!Obstacle04" ; acid pool
		If Rand(0,100)=0
			AddParticle(27,SimulatedObjectX(i)+Rnd(-.5,.5),1+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+Rnd(-.7,.5),0,.11,0,Rnd(0.01,0.03),0,0,.01,0,0,0,100,3)
		EndIf
		If Rand(0,100)=0
			AddParticle(35,SimulatedObjectX(i)+Rnd(-.3,.6),2.0+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+Rnd(-.6,.3),0,.04,0,0,0,0,.001,0,0,0,100,4)
		EndIf
		
	Else If ModelName$="!Obstacle45" ; waterwheel
		If SimulatedObjectYawAdjust(i)=0
			SimulatedObjectRoll(i)=SimulatedObjectRoll(i)+2
		EndIf
		If SimulatedObjectYawAdjust(i)=180
			SimulatedObjectRoll(i)=SimulatedObjectRoll(i)-2
		EndIf
		If SimulatedObjectYawAdjust(i)=90
			SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+2
		EndIf
		If SimulatedObjectYawAdjust(i)=270
			SimulatedObjectPitch(i)=SimulatedObjectPitch(i)-2
		EndIf
	
	Else If ModelName$="!Obstacle48" ; UFO - by mistake in here
		If SimulatedObjectData(i,0)=0
			SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+1
		EndIf
	Else If ModelName$="!Crystal" ; UFO - by mistake in here
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+1

					
	

	EndIf
	If ModelName$="!CustomModel"	; Custom Model 
		ControlCustomModel(i)
	EndIf

End Function


Function ControlCustomModel(i)
	
	Obj.GameObject=LevelObjects(i)

;	If ObjectOldX(i)=-999;0 And ObjectOldY(i)=0 And ObjectOldZ(i)=0
;		ObjectOldX(i)=ObjectXAdjust(i)
;		ObjectOldY(i)=ObjectYAdjust(i)
;		ObjectOldZ(i)=ObjectZAdjust(i)
;	EndIf
	


	;ObjectScaleAdjust(i)*(1.5+0.8*Sin((leveltimer+ObjectData(i,7)+30) Mod 360))
	
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+SimulatedObjectData(i,0)
	If SimulatedObjectYaw(i)>360 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-360
	If SimulatedObjectYaw(i)<0 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+360
	
	SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+SimulatedObjectData(i,1)
	If SimulatedObjectPitch(i)>360 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)-360
	If SimulatedObjectPitch(i)<0 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+360
	
	SimulatedObjectroll(i)=SimulatedObjectroll(i)+SimulatedObjectData(i,2)
	If SimulatedObjectroll(i)>360 Then SimulatedObjectroll(i)=SimulatedObjectroll(i)-360
	If SimulatedObjectroll(i)<0 Then SimulatedObjectroll(i)=SimulatedObjectroll(i)+360
	
	BaseX#=Obj\Attributes\XAdjust
	BaseY#=Obj\Attributes\YAdjust
	BaseZ#=Obj\Attributes\ZAdjust
	
	If SimulatedObjectData(i,3)>0
		; Technically these ObjectX/Y/ZAdjust instances should be OldX/Y/Z. But no one's crazy enough to edit OldX/Y/Z directly, right?
		SimulatedObjectXAdjust(i)=BaseX#+Float(SimulatedObjectData(i,3))*Sin((leveltimer Mod 36000)*Float(SimulatedObjectData(i,6)/100.0))
	Else
		SimulatedObjectXAdjust(i)=BaseX#+Float(SimulatedObjectData(i,3))*Cos((leveltimer Mod 36000)*Float(SimulatedObjectData(i,6)/100.0))
	EndIf
	If SimulatedObjectData(i,4)>0
		SimulatedObjectYAdjust(i)=BaseY#+Float(SimulatedObjectData(i,4))*Sin((leveltimer Mod 36000)*Float(SimulatedObjectData(i,7)/100.0))
	Else
		SimulatedObjectYAdjust(i)=BaseY#+Float(SimulatedObjectData(i,4))*Cos((leveltimer Mod 36000)*Float(SimulatedObjectData(i,7)/100.0))
	EndIf
	If SimulatedObjectData(i,5)>0
		SimulatedObjectZAdjust(i)=BaseZ#+Float(SimulatedObjectData(i,5))*Sin((leveltimer Mod 36000)*Float(SimulatedObjectData(i,8)/100.0))
	Else
		SimulatedObjectZAdjust(i)=BaseZ#+Float(SimulatedObjectData(i,5))*Cos((leveltimer Mod 36000)*Float(SimulatedObjectData(i,8)/100.0))
	EndIf
	
End Function


Function ControlGoldStar(i)

	Obj.GameObject=LevelObjects(i)

	SimulatedObjectZ(i)=.8
;	If AdventureCurrentStatus=0
;		ObjectXScale(i)=.5
;		ObjectYScale(i)=.5
;		ObjectZScale(i)=.5
;		ObjectZ(i)=.4
;	EndIf

	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2

	If SimulatedObjectActive(i)<1001 And (SimulatedObjectActive(i)<>0 Or SimulationLevel>=2) Then Return

	a=Rand(0,300)
	If a<50
		AddParticle(19,Obj\Position\TileX+0.5,.7+SimulatedObjectZAdjust(i),-Obj\Position\TileY-0.5,Rand(0,360),0.16,Rnd(-.015,.015),0.03,Rnd(-.015,.015),0,0.001,0,-.00025,0,100,3)
	EndIf

End Function


Function ControlGoldCoin(i)

	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0
		Pos.GameObjectPosition=LevelObjects(i)\Position
	
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10
		
		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=.2+Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=1.2
		EndIf
		If SimulatedObjectActive(i)=400
			; Little Spark
			For j=1 To 20
				AddParticle(19,Pos\TileX+0.5,1.6,-Pos\TileY-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
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

End Function


Function ControlGem(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	;If ObjectActive(i) Mod 2=1 Then ShowEntity ObjectEntity(i) ; What did MS mean by this?
	
	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=.6+Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=1.6
		EndIf
		If SimulatedObjectActive(i)=400
			
			; Little Spark
			For j=1 To 20
				AddParticle(19,Pos\TileX+0.5,1.6,-Pos\TileY-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
			If WaEpisode=1 And (adventurecurrentnumber>=200 And adventurecurrentnumber<=203) 
				; not in pacman level or WA1
			
			Else If Obj\Attributes\ID=-1
				If SimulatedObjectData(i,0)=0 Then AddParticle(14,Pos\TileX+0.5,1.6,-Pos\TileY-0.5,0,1,0,0.01,0,0,.01,0,0,0,50,3)
				If SimulatedObjectData(i,0)=1 Then AddParticle(15,Pos\TileX+0.5,1.6,-Pos\TileY-0.5,0,1,0,0.01,0,0,.01,0,0,0,50,3)
			EndIf
		EndIf
		If SimulatedObjectActive(i)<600
			SimulatedObjectScaleXAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleYAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleZAdjust(i)=Float(SimulatedObjectActive(i))/600.0
		EndIf
		
		
		
	Else
		If SimulatedObjectData(i,0)=2
			If Rand(0,10)<3
				
				AddParticle(16+SimulatedObjectData(i,1)+Rand(0,1)*8,Pos\TileX+.5,Rnd(0,1),-Pos\TileY-.5,0,.01,Rnd(-.01,.01),Rnd(-.01,.01),Rnd(0,.02),Rnd(-4,4),.01,0,0,0,70,3)
			EndIf
		EndIf
	
		If SimulatedObjectYaw(i)=0 And SimulatedObjectData(i,0)<>1
			SimulatedObjectRoll(i)=Rand(-10,10)
			SimulatedObjectYaw(i)=Rand(1,180)
	
		EndIf
		If SimulatedObjectData(i,0)=0 Or SimulatedObjectData(i,0)=2 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Rnd(1.8,2.2)
		If SimulatedObjectData(i,0)=1 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+Rnd(2,3)+(i Mod 3)/3.0
		SimulatedObjectZ(i)=.4
	EndIf
	
End Function


Function ControlKey(i)

	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0
	
		; picked up animation
		
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=.6+2*Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.6
		EndIf
		If SimulatedObjectActive(i)=400
			Pos.GameObjectPosition=LevelObjects(i)\Position
			
			; Little Spark
			For j=1 To 60
				AddParticle(Rnd(16,23),Pos\TileX+0.5,2.6,-Pos\TileY-0.5,Rand(0,360),0.2,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
		If SimulatedObjectActive(i)<600
			SimulatedObjectScaleXAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleYAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleZAdjust(i)=Float(SimulatedObjectActive(i))/600.0
		EndIf
		
		
		
	Else
		Obj.GameObject=LevelObjects(i)
		
	;	ObjectYaw(i)=ObjectYaw(i)+2
		If Obj\Attributes\ModelName$="!KeyCard"
			SimulatedObjectYaw(i)=((leveltimer) Mod 90)*4
		Else
			SimulatedObjectRoll(i)=30*Sin((leveltimer) Mod 360)
		EndIf
		SimulatedObjectZ(i)=.4
	EndIf

End Function


Function ControlCustomItem(i)
	
	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10
		


		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=.6+2*Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.6
		EndIf
		If SimulatedObjectActive(i)=400
			Pos.GameObjectPosition=LevelObjects(i)\Position
		
			; Little Spark
			For j=1 To 60
				AddParticle(Rnd(16,23),Pos\TileX+0.5,2.6,-Pos\TileY-0.5,Rand(0,360),0.2,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
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
	
	
	Select SimulatedObjectData(i,2)
	
	Case 0
		; nuthin'
	Case 1
		; Sway
		SimulatedObjectPitch(i)=5*Sin((leveltimer*1.5) Mod 360)
		SimulatedObjectYaw(i)=5*Sin((leveltimer/2) Mod 360)
		SimulatedObjectRoll(i)=5*Sin(leveltimer Mod 360)

	Case 2
		; Bounce
		SimulatedObjectScaleZAdjust(i)=1.0+0.15*Sin((Leveltimer*4) Mod 360)
		SimulatedObjectRoll(i)=5*Sin((leveltimer*2) Mod 360)
	Case 3
		; turn
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+3
	End Select
	
End Function


Function ControlRetroRainbowCoin(i)
	
	If SimulatedObjectActive(i)<1001
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10
		


		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=1.2+Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.2
		EndIf
		If SimulatedObjectActive(i)=400
			Pos.GameObjectPosition=LevelObjects(i)\Position
		
			; Little Spark
			For j=1 To 20
				AddParticle(19,Pos\TileX+0.5,2.6,-Pos\TileY-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
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

End Function


Function ControlWisp(i)

	Obj.GameObject=LevelObjects(i)
	EntityFX Obj\Model\Entity,1
	
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
	If Rand(0,100)<3 And SimulatedObjectActive(i)=1001
		Pos.GameObjectPosition=Obj\Position
		AddParticle(Rand(16,23),Pos\TileX+0.5,.7,-Pos\TileY-0.5,Rand(0,360),0.16,Rnd(-.015,.015),0.03,Rnd(-.015,.015),0,0.001,0,-.00025,0,100,3)
	EndIf

End Function


Function ControlRetroZbotUfo(i)

	Attributes.GameObjectAttributes=LevelObjects(i)\Attributes
	If Attributes\LogicType<>423 And Attributes\LogicType<>430
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2
	EndIf

	;SimulateObjectRotation(i)

End Function


Function ControlRetroLaserGate(i)
	
	If SimulatedObjectYawAdjust(i)=0 Or SimulatedObjectYawAdjust(i)=180
		SimulatedObjectPitch(i)=(SimulatedObjectPitch(i)+2) Mod 360
	Else
		SimulatedObjectRoll(i)=(SimulatedObjectRoll(i)+2) Mod 360
	EndIf
	
	Obj.GameObject=LevelObjects(i)
	; This behavior is OpenWA-exclusive.
	EntityAlpha Obj\Model\Entity,0.5
			
End Function


Function ControlTentacle(i)

	If SimulatedObjectData(i,0)=0 Then SimulatedObjectData(i,0)=Rand(-10,10)
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Float(SimulatedObjectData(i,0))/10.0
	
	;SimulateObjectRotation(i)
	
End Function


Function ControlRainbowBubble(i)

	Obj.GameObject=LevelObjects(i)

	If SimulatedObjectStatus(i)=0
		SimulatedObjectStatus(i)=1
		SimulatedObjectData(i,2)=Rand(0,360)
	EndIf

	EntityAlpha Obj\Model\Entity,.8
	EntityBlend Obj\Model\Entity,3
	
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
		
		;CreateShadow(i,ObjectScaleAdjust(i)*5)
	EndIf
	
	
	If SimulatedObjectStatus(i)<0
		
		SimulatedObjectRoll(i)=((SimulatedObjectRoll(i)+.3) Mod 360)

		SimulatedObjectPitch(i)=((SimulatedObjectPitch(i)-.1) Mod 360)
		
		AddParticle(Rand(0,3),SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZ(i)+Rnd(-.1,.1),-SimulatedObjectY(i)+Rnd(-.1,.1),0,Rnd(0.1,.5),Rnd(-.1,.1),Rnd(-.01,.01),Rnd(-.1,.1),3,.02,0,0,0,125,3)

;		If SimulatedObjectStatus(i) Mod 30 = 0
;			PlaySoundFX(96,SimulatedObjectX(i),SimulatedObjectY(i))
;		EndIf
	
		SimulatedObjectStatus(i)=SimulatedObjectStatus(i)-1
		;If ObjectStatus(i)=-200
		;	destroyobject(i,0)
		;	NofZBotsInAdventure=NofZBotsInAdventure-1
		;EndIf
	
	Else
		SimulatedObjectSubType(i)=SimulatedObjectSubType(i)+1
		If SimulatedObjectSubType(i)>100
			SimulatedObjectSubType(i)=0
;			PlaySoundFX(95,SimulatedObjectX(i),SimulatedObjectY(i))
		EndIf
	EndIf
	
End Function


Function ControlRubberducky(i)

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=-1
		SimulatedObjectData(i,1)=Rand(1,3)
		SimulatedObjectData(i,2)=Rand(0,360)
	EndIf	
		
	SimulatedObjectroll(i)=1*SimulatedObjectData(i,1)*Sin((LevelTimer+SimulatedObjectData(i,2)) Mod 360)
	SimulatedObjectpitch(i)=2*SimulatedObjectData(i,1)*Cos((LevelTimer*3+SimulatedObjectData(i,2))  Mod 360)

End Function


Function ControlGloveCharge(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position
	
	SubType=Obj\Attributes\LogicSubType

	SimulatedObjectZ(i)=0.04
	
	myparticle=16+SimulatedObjectData(i,0)
	
	SimulatedObjectData(i,3)=1
	
	j2=Rand(0,359)
	If SubType=1 ; one time charge
		If leveltimer Mod 5 = 0
			AddParticle(myparticle,Pos\TileX+.5+.1*Sin(j2*3),0,-Pos\TileY-.5-.1*Cos(j2*3),0,.2,0,.03,0,4,0,0,0,0,50,3)
		EndIf
	Else If SubType=0; multi-charge
		If leveltimer Mod 2 = 0
			AddParticle(myparticle,Pos\TileX+.5+.3*Sin(j2*3),0,-Pos\TileY-.5-.3*Cos(j2*3),0,.3,0,.04,0,4,0,0,0,0,50,3)
		EndIf
	EndIf
	
End Function


Function ControlWindmillRotor(i)

	If SimulatedObjectYawAdjust(i)=0 Or SimulatedObjectYawAdjust(i)=180
		SimulatedObjectRoll(i)=SimulatedObjectRoll(i)+1
	Else 
		SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+1
	EndIf
	
	;SimulatedObjectZ(i)=5.65
	Obj.GameObject=LevelObjects(i)
	Obj\Attributes\ZAdjust=5.65

End Function


Function ControlIceFloat(i)	
	SimulatedObjectPitch(i)=2*SimulatedObjectData(i,2)*Sin((LevelTimer + SimulatedObjectData(i,1)) Mod 360)
	SimulatedObjectRoll(i)=3*SimulatedObjectData(i,3)*Cos((LevelTimer+ SimulatedObjectData(i,1))  Mod 360)
	
	;SimulateObjectRotation(i)

End Function


Function ControlPlantFloat(i)

	Obj.GameObject=LevelObjects(i)

	If SimulatedObjectData(i,2)=0 Then SimulatedObjectData(i,2)=Rand(1,360)

	l=leveltimer+SimulatedObjectData(i,2)
	EntityColor Obj\Model\Entity,128+120*Cos(l Mod 360),128+120*Sin(l Mod 360),200+50*Cos(l Mod 360)
	
	;ObjectPitch(i)=4*ObjectData(i,2)*Sin((LevelTimer + ObjectData(i,1)) Mod 360)
	;Objectroll(i)=6*ObjectData(i,3)*Cos((LevelTimer+ ObjectData(i,1))  Mod 360)
	SimulatedObjectYaw(i)=leveltimer Mod 360
	
	;SimulateObjectRotation(i)

End Function


Function ControlBurstFlower(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=1
		SimulatedObjectData(i,0)=Rand(0,360)
	EndIf
	
	SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)+1) Mod 720
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+.5*Sin(SimulatedObjectData(i,0)/2)
	SimulatedObjectXScale(i)=0.3+0.02*Cos(SimulatedObjectData(i,0)*2)
	SimulatedObjectYScale(i)=0.3+0.02*Cos(SimulatedObjectData(i,0)*2)

	If SimulatedObjectData(i,1)>=0 And Rand(0,100)<2 And Obj\Attributes\Indigo=0 AddParticle(7,SimulatedObjectX(i),0.5,-SimulatedObjectY(i),Rand(0,360),0.4,0,0.02,0,Rnd(0,2),.01,0,0,0,50,4)

	If SimulatedObjectData(i,1)<0 Then SimulatedObjectData(i,1)=SimulatedObjectData(i,1)+1
	
	x=Pos\TileX
	y=Pos\TileY
	; player near or other stinkers near? increase burst timer
	If Obj\Attributes\Indigo=0
		flag=0
		For j=0 To nofobjects-1
			OtherObj.GameObject=LevelObjects(j)
			OtherPos.GameObjectPosition=OtherObj\Position
			OtherType=OtherObj\Attributes\LogicType
			If (OtherType=1 Or OtherType=110 Or OtherType=120) And Abs(x-OtherPos\TileX)<4 And Abs(y-OtherPos\TileY)<4
				; close enough
				flag=1
				Simulatedobjectdata(i,1)=SimulatedObjectData(i,1)+1
				If SimulatedObjectData(i,1)>0 And SimulatedObjectData(i,1) Mod 3 =0
					 AddParticle(8,SimulatedObjectX(i),0.8,-SimulatedObjectY(i),Rand(0,360),SimulatedObjectData(i,1)/200.0+.5,0,0,0,Rnd(0,2),0,0,0,0,30,4)
				EndIf

				If SimulatedObjectData(i,1)=150
					SimulatedObjectData(i,1)=-1000
					; fire spellballs
				EndIf
			EndIf
		Next
		If (Abs(x-PlayerTileX())<4 And Abs(y-PlayerTileY())<4)
			; close enough
			flag=1
			Simulatedobjectdata(i,1)=SimulatedObjectData(i,1)+1
			If SimulatedObjectData(i,1)>0 And SimulatedObjectData(i,1) Mod 3 =0
				 AddParticle(8,SimulatedObjectX(i),0.8,-SimulatedObjectY(i),Rand(0,360),SimulatedObjectData(i,1)/200.0+.5,0,0,0,Rnd(0,2),0,0,0,0,30,4)
			EndIf

			If SimulatedObjectData(i,1)=150
				SimulatedObjectData(i,1)=-1000
				; fire spellballs
			EndIf
		EndIf
		
		If flag=0 And SimulatedObjectData(i,1)>0
			SimulatedObjectData(i,1)=SimulatedObjectData(i,1)-1
		EndIf
	EndIf

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

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	For i2=1 To 4
		; heating up smoke
		If SimulatedObjectData(i,6)>i2*120 And Rand(1,9)<4
			AddParticle(0+Floor(i2/2),SimulatedObjectX(i)+Rnd(-.1,.1),0.5,-SimulatedObjectY(i)+Rnd(-.1,.1),0,0.2,Rnd(-0.012,0.012),Rnd(0,0.12),Rnd(-0.012,0.012),5,0.01,0,0,0,35,3)
		EndIf
	Next

	If Obj\Attributes\ModelName$<>"!StinkerWee"
		Return
	EndIf

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		SimulatedObjectMovementSpeed(i)=35	
		SimulatedObjectSubType(i)=0  ; -2 dying, -1 exiting, 0- asleep, 1-follow, 2-directive, 3-about to fall asleep (still walking), 4 caged
		MaybeAnimateMD2(Obj\Model\Entity,1,Rnd(.002,.008),217,219,1)
		SimulatedObjectCurrentAnim(i)=1 ; 1-asleep, 2-getting up, 3-idle, 4-wave, 5-tap, 6-walk, 7 sit down, 8-fly, 9-sit on ice	
		SimulatedObjectXScale(i)=0.025
		SimulatedObjectYScale(i)=0.025
		SimulatedObjectZScale(i)=0.025
	EndIf
	
	If Obj\Attributes\Dead=1
		; spinning out of control
		SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360
		;ObjectZ(i)=ObjectZ(i)+.01
		;ObjectSubType(i)=-2
		Return
	EndIf
	If Obj\Attributes\Dead=3
		; drowning
		SimulatedObjectYaw(i)=90
		;ObjectZ(i)=ObjectZ(i)-.005
		;ObjectSubType(i)=-2
		Return
	EndIf
	
	If SimulatedObjectSubType(i)=-1 
		TurnObjectTowardDirection(i,0,1,4,0)
		Return ; already on its way out
	EndIf
	
	If Obj\Attributes\Caged=True And SimulatedObjectSubType(i)<>4 And SimulatedObjectSubType(i)<>5
		; just Caged
		EntityTexture Obj\Model\Entity,StinkerWeeTextureSad(SimulatedObjectData(i,8)+1)
		;PlaySoundFX(66,Pos\TileX,Pos\TileY)
		If SimulatedObjectSubType(i)=2
			SimulatedObjectSubType(i)=5
		Else
			SimulatedObjectSubType(i)=4
		EndIf
		
		MaybeAnimateMD2(Obj\Model\Entity,1,.2,108,114,1)
	EndIf
	If Obj\Attributes\Caged=False And (SimulatedObjectSubType(i)=4 Or SimulatedObjectSubType(i)=5)
		; just released
		EntityTexture Obj\Model\Entity,StinkerWeeTexture(SimulatedObjectData(i,8)+1)

		SimulatedObjectSubType(i)=SimulatedObjectSubType(i)-3
		MaybeAnimateMD2(Obj\Model\Entity,1,.4,1,20,1)
		SimulatedObjectCurrentAnim(i)=4
		;SimulatedObjectMovementTypeData(i)=0
	EndIf

	
	If Obj\Attributes\Caged=True
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,3,0)
		Return
	EndIf
	
	If SimulatedObjectSubType(i)=0 
		EntityTexture Obj\Model\Entity,StinkerWeeTextureSleep(SimulatedObjectData(i,8)+1)

	Else
		EntityTexture Obj\Model\Entity,StinkerWeeTexture(SimulatedObjectData(i,8)+1)

	EndIf
	
	If SimulatedObjectSubType(i)=3 ; asleep after walking
		; fall asleep now
		;EntityTexture ObjectEntity(i),StinkerWeeTextureSleep
		MaybeAnimateMD2(Obj\Model\Entity,3,.2,201,217,1)
		SimulatedObjectCurrentAnim(i)=7
		SimulatedObjectData(i,0)=0
		SimulatedObjectData(i,1)=0
		SimulatedObjectData(i,2)=4
		SimulatedObjectSubType(i)=2
		;ObjectMoveXGoal(i)=Pos\TileX
		;ObjectMoveYGoal(i)=Pos\TileY
	Else If SimulatedObjectData(i,2)<4
		; stopped - but wait a few frames before switching animation
		; (to avoid start/stop animation)
		SimulatedObjectData(i,2)=SimulatedObjectData(i,2)+1
		
	Else
		; not walking
		If SimulatedObjectSubType(i)=0 ; asleep
			SimulatedObjectData(i,2)=SimulatedObjectData(i,2)+1
			If SimulatedObjectData(i,2)>200
				If Rand(0,100)<3  
					AddParticle(9,Pos\TileX+.5,.9,-Pos\TileY-.5,0,0.5,0,0.01,0,0,.001,0,0,0,200,3)
					SimulatedObjectData(i,2)=0
					;PlaySoundFX(59,Pos\TileX,Pos\TileY)
				EndIf
				
				
			EndIf
			If SimulatedObjectCurrentAnim(i)<>1
				MaybeAnimateMD2(Obj\Model\Entity,1,Rnd(.002,.008),217,219,1)
				SimulatedObjectCurrentAnim(i)=1
			EndIf
			If SimulatedObjectYaw(i)<>180 Then TurnObjectTowardDirection(i,0,1,5,0)
		

		Else ; either in follow or directive mode, but standing
			
			If SimulatedObjectCurrentAnim(i)<>7
				; turn toward player unless sitting
				TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,3,0)
			EndIf
			If SimulatedObjectCurrentAnim(i)<>3 And SimulatedObjectCurrentAnim(i)<>4 And SimulatedObjectCurrentAnim(i)<>5 And SimulatedObjectCurrentAnim(i)<>7
				MaybeAnimateMD2(Obj\Model\Entity,1,Rnd(.01,.08),141,160,1)
				SimulatedObjectCurrentAnim(i)=3
				SimulatedObjectData(i,0)=0
			Else If SimulatedObjectCurrentAnim(i)=3
				; possible wave/tap animation when in directive mode
				If Rand(0,1000)<2 And SimulatedObjectData(i,1)>100
					; do an animation
					If (Rand(0,100)<50) ; wave
						;PlaySoundFX(Rand(50,54),Pos\TileX,Pos\TileY)
						MaybeAnimateMD2(Obj\Model\Entity,3,.2,101,120,1)
						SimulatedObjectCurrentAnim(i)=4
					Else If SimulatedObjectSubtype(i)=2	; tap
						MaybeAnimateMD2(Obj\Model\Entity,1,.2,121,140,1)
						SimulatedObjectCurrentAnim(i)=5
					EndIf			
				EndIf
			Else If SimulatedObjectCurrentAnim(i)=4
				SimulatedObjectData(i,0)=SimulatedObjectData(i,0)+1
				If SimulatedObjectData(i,0)>100
					SimulatedObjectData(i,0)=0
					MaybeAnimateMD2(Obj\Model\Entity,1,Rnd(.01,.03),141,160,1)
					SimulatedObjectCurrentAnim(i)=3
				EndIf
;			Else If SimulatedObjectCurrentAnim(i)=5
;				SimulatedObjectData(i,0)=SimulatedObjectData(i,0)+1
;				If SimulatedObjectData(i,0)>1500
;					SimulatedObjectData(i,0)=0
;					MaybeAnimateMD2(ObjectEntity(i),1,Rnd(.01,.03),141,160,1)
;					SimulatedObjectCurrentAnim(i)=3
;				EndIf
;			Else If SimulatedObjectCurrentAnim(i)=7
;				SimulatedObjectData(i,0)=SimulatedObjectData(i,0)+1
;				If SimulatedObjectYaw(i)<>180 Then TurnObjectTowardDirection(i,0,1,1,0)
;
;				If SimulatedObjectData(i,0)>100
;					; asleep
;					MaybeAnimateMD2(ObjectEntity(i),1,Rnd(.002,.008),217,219,1)
;					SimulatedObjectCurrentAnim(i)=1
;					SimulatedObjectSubType(i)=0
;				EndIf
			EndIf
			
			; If in directive mode - use timer to see if falling asleep again
;			If ObjectSubType(i)=1
;				ObjectData(i,1)=ObjectData(i,1)+1
;				If ObjectData(i,1)>5000 And leveltimer Mod 5000=0
;					; bored!
;					PlaySoundFX(68,Pos\TileX,Pos\TileY)
;					ObjectData(i,1)=0
; 					EndIf
;			EndIf

;			If ObjectSubType(i)=2
;				ObjectData(i,1)=ObjectData(i,1)+1
;				
;				If ObjectData(i,1)>4800
;					; fell asleep again
;					PlaySoundFX(69,Pos\TileX,Pos\TileY)
;			;		EntityTexture ObjectEntity(i),StinkerWeeTextureSleep
;					AnimateMD2 ObjectEntity(i),3,.2,201,217,1
;					ObjectCurrentAnim(i)=7
;					ObjectData(i,0)=0
;					ObjectData(i,1)=0
;				EndIf
;			EndIf
		EndIf
	EndIf


End Function

Function ControlStinkerWeeExit(i)
	If LevelTimer Mod 3 = 0
		Pos.GameObjectPosition=LevelObjects(i)\Position
		AddParticle(Rand(16,23),Pos\TileX+0.5+0.2*Sin(LevelTimer*10),0,-Pos\TileY-0.5-0.2*Cos(LevelTimer*10),Rand(0,360),0.1,0,0.02,0,0,0.005,0,0,0,100,3)
	EndIf
End Function


Function ControlScritter(i)

	;If ObjectMovementTimer(i)>0
	;	SimulatedObjectZ(i)=0.4*Abs(Sin(ObjectMovementTimer(i)*360/1000))
	;	TurnObjectTowardDirection(i,Pos\TileX2-Pos\TileX,Pos\TileY2-Pos\TileY,10,0)
	;Else
		Pos.GameObjectPosition=LevelObjects(i)\Position
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,6,0)
	;EndIf

End Function


Function ControlCrab(i)

	Obj.GameObject=LevelObjects(i)

	;subtype -0-male, 1-female
	;data1 - 0-normal,1-curious, 2- asleep, 3- disabled
	;status - 0 normal, 2 submerged

	If SimulatedObjectTileTypeCollision(i)=0
		; First time
		If SimulatedObjectSubType(i)=0
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
;			Select SimulatedObjectData(i,1)
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
	
	If Obj\Attributes\ModelName$="!Crab"
		; anim
		; 1-idle
		; 2-walk
		; 3-walk (used for non-stop animation between tiles)
		; 4-retract
		; 5-come out
		If Obj\Attributes\Frozen>0
			MaybeAnimateMD2(Obj\Model\Entity,2,.01,1,2)
			SimulatedObjectCurrentAnim(i)=0
		Else If Obj\Attributes\MovementTimer=0 And (SimulatedObjectCurrentAnim(i)=0 Or SimulatedObjectCurrentAnim(i)=3) And SimulatedObjectdata(i,1)<2
			
			
			MaybeAnimateMD2(Obj\Model\Entity,2,Rnd(.02,.04),1,13)
			SimulatedObjectCurrentAnim(i)=1
			
			
		Else If SimulatedObjectCurrentAnim(i)=2 
			SimulatedObjectCurrentAnim(i)=3
		Else If Obj\Attributes\MovementTimer>0 And (SimulatedObjectCurrentAnim(i)=0 Or SimulatedObjectCurrentAnim(i)=1 Or SimulatedObjectCurrentAnim(i)=20)
			MaybeAnimateMD2(Obj\Model\Entity,1,1,1,30)
			SimulatedObjectCurrentAnim(i)=2
		Else If SimulatedObjectCurrentAnim(i)>=5 And SimulatedObjectCurrentAnim(i)<20
			; delay for coming out anim so it doesn' immediately go into walking
			SimulatedObjectCurrentAnim(i)=SimulatedObjectCurrentAnim(i)+1
		EndIf
	EndIf
	
	If SimulatedObjectStatus(i)=0 And SimulatedObjectData(i,1)<2
		SimulatedObjectZ(i)=0
		;If Obj\Attributes\MovementTimer>0 
		;	TurnObjectTowardDirection(i,-(Pos\TileX2-Pos\TileX),-(Pos\TileY2-Pos\TileY),10,0)
		;Else
			TurnObjectTowardDirection(i,-(PlayerX()-SimulatedObjectX(i)),-(PlayerY()-SimulatedObjectY(i)),6,0)
		;EndIf
	EndIf
	
End Function


Function ControlTrap(i)

	Obj.GameObject=LevelObjects(i)

	SimulatedObjectZ(i)=0.04

	If SimulatedObjectActive(i)=1001 Or SimulationLevel<2
		If SimulatedObjectStatus(i)=0
			; currently off
			SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
			If SimulatedObjectTimer(i)<=0
				; turn on
				SimulatedObjectTimer(i)=Obj\Attributes\TimerMax1
				SimulatedObjectStatus(i)=1
			EndIf
		Else
			; currently on
			SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
			If SimulatedObjectTimer(i)<=0
				; turn off
				SimulatedObjectTimer(i)=Obj\Attributes\TimerMax2
				SimulatedObjectStatus(i)=0
			EndIf
		EndIf
	
		
		Select SimulatedObjectSubType(i)
			Case 0
				; fire - create particle when on
				
			If SimulatedObjectStatus(i)=1
				;If Rand(0,100)<50 AddParticle(2,Pos\X+Rnd(-.1,.1),ObjectZAdjust(i),-ObjectY(i),0-Rnd(-.1,.1),.5,Rnd(-.005,.005),.05,Rnd(-.005,.005),0,.01,0,-.0001,0,50,0)
				If Rand(0,100)<50 AddParticle(2,SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZAdjust(i),-SimulatedObjectY(i),0-Rnd(-.1,.1),.5,Rnd(-.005,.005),.05,Rnd(-.005,.005),0,.01,0,-.0001,0,50,4)
			EndIf
			
		End Select
	Else
		
		;If Rand(0,100)<2 AddParticle(0,Pos\X+Rnd(-.1,.1),ObjectZAdjust(i),-ObjectY(i),0-Rnd(-.1,.1),.3,Rnd(-.005,.005),.02,Rnd(-.005,.005),0,.01,0,-.0001,0,50,0)
		If Rand(0,100)<2 AddParticle(0,SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZAdjust(i),-SimulatedObjectY(i),0-Rnd(-.1,.1),.3,Rnd(-.005,.005),.02,Rnd(-.005,.005),0,.01,0,-.0001,0,50,4)
		
	EndIf
	
	;SimulateObjectPosition(i)

End Function


Function ControlFireFlower(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If Attributes\ModelName$<>"!FireFlower"
		Return
	EndIf

	If (SimulatedObjectTimer(i)>=0 And SimulatedObjectData(i,2)=0) Or (SimulatedObjectData(i,2)=2 And SimulatedObjectTimer(i)=Attributes\TimerMax1)
		SimulatedObjectData(i,2)=1
		MaybeAnimateMD2(Obj\Model\Entity,1,.2,1,20,1)
	EndIf
	
	;If ObjectActive(i)<1001
	;	SimulatedObjectTimer(i)=ObjectTimerMax1(i)
	;EndIf
	
	If Attributes\Indigo=0 SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
	SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)+80) Mod 8
	SimulatedObjectYawAdjust(i)=0 ; Necessary to make the model face the correct way due to the weird special case handling of its rotation.
	
	dx#=0
	dy#=0
	If SimulatedObjectSubType(i)=1
		; follow player
		dx=PlayerX()-SimulatedObjectX(i)
		dy=PlayerY()-SimulatedObjectY(i)
		total#=Sqr(dx^2+dy^2)
		dx=dx/total
		dy=dy/total
		
	Else
		; turn or static
		If SimulatedObjectData(i,0)>0 And SimulatedObjectData(i,0)<4
			dx=1
		EndIf
		If SimulatedObjectData(i,0)>4
			dx=-1
		EndIf
		If SimulatedObjectData(i,0)<2 Or SimulatedObjectData(i,0)>6
			dy=-1
		EndIf
		If SimulatedObjectData(i,0)>2 And SimulatedObjectData(i,0)<6
			dy=1
		EndIf
	EndIf
	If SimulatedObjectTimer(i)>-10
		TurnObjectTowardDirection(i,dx,dy,3,180)
	EndIf
	
	If SimulatedObjectTimer(i)<0

		If SimulatedObjectData(i,2)=1
			MaybeAnimateMD2(Obj\Model\Entity,1,.5,21,60,1)
			SimulatedObjectData(i,2)=0
		EndIf
	
		If SimulatedObjectTimer(i)=-80
			SimulatedObjectTimer(i)=Attributes\TimerMax1
		EndIf
		
		; and fire
		If SimulatedObjectTimer(i)=-60

			If SimulatedObjectSubType(i)=2
				SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)+1) Mod 8
			EndIf
			If SimulatedObjectSubType(i)=3
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
	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	; switches objects from activating to deactivating or vice versa
	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i) Mod 2 =0
		If Attributes\LogicType=410
			;ActivateFlipBridge(i)
		Else
			SimulatedObjectActive(i)=SimulatedObjectActive(i)+Attributes\ActivationSpeed+1
			
		EndIf
		If SimulatedObjectActive(i)>1001 Then SimulatedObjectActive(i)=1001
	Else If SimulatedObjectActive(i)>0 And SimulatedObjectActive(i) Mod 2 =1
		If Attributes\LogicType=410
			;DeActivateFlipBridge(i)
		Else
			SimulatedObjectActive(i)=SimulatedObjectActive(i)-Attributes\ActivationSpeed-1
			
		EndIf
		If SimulatedObjectActive(i)<0 Then SimulatedObjectActive(i)=0
	EndIf
	If Attributes\LogicType=281 And Attributes\ModelName$="!SucTube"
		Redosuctubemesh(Obj\Model\Entity, SimulatedObjectData(i,0), SimulatedObjectActive(i), SimulatedObjectData(i,2), SimulatedObjectYawAdjust(i))
	EndIf
	
End Function


Function ControlChangeActive(i)
	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If SimulatedObjectActive(i)>0 And SimulatedObjectActive(i) Mod 2 = 0
		; deactivating
		SimulatedObjectActive(i)=SimulatedObjectActive(i)-Attributes\ActivationSpeed
	Else If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i) Mod 2=1
		; activating
		SimulatedObjectActive(i)=SimulatedObjectActive(i)+Attributes\ActivationSpeed
		
	EndIf
	If SimulatedObjectActive(i)<0 	SimulatedObjectActive(i)=0
	If SimulatedObjectActive(i)>1001 SimulatedObjectActive(i)=1001

End Function


Function ControlSteppingStone(i)
	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	; Data(2) - Alternating?
	If SimulatedObjectData(i,2)=2
		SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
		If SimulatedObjectTimer(i)<0 Then SimulatedObjectTimer(i)=Attributes\TimerMax1
		If SimulatedObjectTimer(i)=0
			ToggleObject(i)
			SimulatedObjectTimer(i)=Attributes\TimerMax1
		EndIf
		
		ControlChangeActive(i)
	EndIf
		
	
	; 0-submerged, 1001-surfaced
	If (SimulatedObjectActive(i)<1001-4*Attributes\ActivationSpeed) And SimulatedObjectLastActive(i)>=1001-4*Attributes\ActivationSpeed

		; just submerged
		If SimulatedObjectData(i,3)=0
			AddParticle(4,Floor(SimulatedObjectX(i))+0.5,LevelTiles(Floor(SimulatedObjectX(i)),Floor(SimulatedObjectY(i)))\Water\Height-0.2,-Floor(SimulatedObjectY(i))-0.5,0,.6,0,0,0,0,.006,0,0,0,50,4)
		EndIf
	
	EndIf
	
	If (SimulatedObjectActive(i)=>1001-4*Attributes\ActivationSpeed) And SimulatedObjectLastActive(i)<1001-4*Attributes\ActivationSpeed
		
		; just emerged
		If SimulatedObjectData(i,3)=0
			AddParticle(4,Floor(SimulatedObjectX(i))+0.5,LevelTiles(Floor(SimulatedObjectX(i)),Floor(SimulatedObjectY(i)))\Water\Height-0.2,-Floor(SimulatedObjectY(i))-0.5,0,1,0,0,0,0,.006,0,0,0,100,4)
		EndIf
				
	EndIf

	
End Function



Function ControlFlipBridge(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes
	
	YScale#=6.6
	
	If (SimulatedObjectActive(i)<>0 And SimulatedObjectActive(i)<>1001) Or SimulationLevel>=2
		YScale#=1+5.6*Float(SimulatedObjectActive(i))/1001.0
	EndIf
	
	If Attributes\ModelName$="!FlipBridge"
		ScaleEntity GetChild(Obj\Model\Entity,1),1.0,1.0,YScale#
	Else
		SimulatedObjectScaleYAdjust(i)=YScale#
	EndIf

End Function


Function ControlSpring(i)

	SimulatedObjectZ(i)=.5

End Function


Function ControlBabyBoomer(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If Attributes\Dead=1
		; spinning out of control
		SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360
		;ObjectZ(i)=ObjectZ(i)+.01
		;ObjectSubType(i)=-2
		Return
	EndIf
	If Attributes\Dead=3
		; drowning
		SimulatedObjectYaw(i)=90
		;ObjectZ(i)=ObjectZ(i)-.005
		;ObjectSubType(i)=-2
		Return
	EndIf
	
	If SimulatedObjectData(i,8)=1
		; lit
;		For j=1 To 5
			If Rand(0,100)<20
				AddParticle(23,SimulatedObjectX(i),Rnd(0.7,0.8),-SimulatedObjectY(i),0,.05,Rnd(-0.005,0.005),Rnd(0,0.005),Rnd(-0.005,0.005),0,.004,0,0,0,50,3)
			EndIf
;		Next
	EndIf
	
	If SimulatedObjectData(i,8)>0
		;EntityTexture Obj\Model\Entity,KaboomTextureSquint
		; lit and burning
		For j=1 To 5
			If Rand(0,100)<SimulatedObjectData(i,8)
				AddParticle(Rand(16,18),SimulatedObjectX(i),Rnd(0.7,0.8),-SimulatedObjectY(i),0,.1,Rnd(-0.02,0.02),Rnd(0,0.02),Rnd(-0.02,0.02),0,.004,0,-.0001,0,50,3)
			EndIf
		Next
	EndIf

End Function


Function ControlSuctube(i)

	If SimulatedObjectActive(i)<>1001 Then Return
	
	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes
	Pos.GameObjectPosition=Obj\Position

	suck=True
	blow=True

	
	; check if sucking/blowing active (e.g. if another tube in front of it)
	For j=0 To NofObjects-1
		OtherObj.GameObject=LevelObjects(j)
		OtherAttributes.GameObjectAttributes=Obj\Attributes
		OtherPos.GameObjectPosition=Obj\Position
		
		If OtherAttributes\LogicType=281 And i<>j
			; found another suctube
			If Attributes\Data2=OtherAttributes\Data2 And Attributes\Data0=OtherAttributes\Data0 And Attributes\Data1=OtherAttributes\Data1
				; same direction
				If SimulatedObjectData(i,2)=0 
					If Pos\TileX=OtherPos\TileX And Pos\TileY=OtherPos\TileY-1
						suck=False
					EndIf
					If Pos\TileX=OtherPos\TileX And Pos\TileY=OtherPos\TileY+1
						blow=False
					EndIf
				Else If SimulatedObjectData(i,2)=1 
					If Pos\TileX=OtherPos\TileX+1 And Pos\TileY=OtherPos\TileY
						suck=False
					EndIf
					If Pos\TileX=OtherPos\TileX-1 And Pos\TileY=OtherPos\TileY
						blow=False
					EndIf
				Else If SimulatedObjectData(i,2)=2 
					If Pos\TileX=OtherPos\TileX And Pos\TileY=OtherPos\TileY+1
						suck=False
					EndIf
					If Pos\TileX=OtherPos\TileX And Pos\TileY=OtherPos\TileY-1
						blow=False
					EndIf
				Else If SimulatedObjectData(i,2)=3 
					If Pos\TileX=OtherPos\TileX-1 And Pos\TileY=OtherPos\TileY
						suck=False
					EndIf
					If Pos\TileX=OtherPos\TileX+1 And Pos\TileY=OtherPos\TileY
						blow=False
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
	If SimulatedObjectData(i,5)=0
		; particle effects
		If Rand(0,100)<30
			psize#=Rnd(0.1,0.2)
			pspeed#=Rnd(1,2)
			parttex=Rand(16,23)
			If suck=True
				Select SimulatedObjectData(i,2)
				Case 0
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-SimulatedObjectY(i)-Rnd(1.0,1.9),0,psize,0.0,0.0,-Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 1
					AddParticle(parttex,SimulatedObjectX(i)-Rnd(1.0,1.5),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(-1,1),0,psize,Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		Case 2
				;	AddParticle(0,0,Rnd(0.5,5.5),0,0,5,0.0,0.0,Rnd(-0.01,-0.02),0,0,0,0,0,Rand(10,50),3)
		
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(1.0,1.9),0,psize,0.0,0.0,Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 3
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(1.0,1.5),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(-1,1),0,psize,-Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		End Select
			EndIf
			If blow=True
				Select SimulatedObjectData(i,2)
				Case 0
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(0.0,0.5),0,psize,0.0,0.0,-Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 1
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(0,0.5),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(-1,1),0,psize,Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		Case 2
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-SimulatedObjectY(i)-Rnd(0.0,0.5),0,psize,0.0,0.0,Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 3
					AddParticle(parttex,SimulatedObjectX(i)-Rnd(0.0,0.5),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(-1,1),0,psize,-Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		End Select
			EndIf
		EndIf
	EndIf


End Function


Function ControlThwart(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	TurnObjectTowardDirection(i,-PlayerX()+SimulatedObjectX(i),-PlayerY()+SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))
	
	; shooting?
	If SimulatedObjectData(i,6)>0 And Attributes\Indigo=0
		dx#=PlayerX()-SimulatedObjectX(i)
		dy#=PlayerY()-SimulatedObjectY(i)
		total#=Sqr(dx^2+dy^2)
		dx=dx/total
		dy=dy/total
		
		SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
		
		If SimulatedObjectTimer(i)<0
			If SimulatedObjectTimer(i)=-10
				; aquire target now
				SimulatedObjectData(i,4)=dx*10000
				SimulatedObjectData(i,5)=dy*10000
			EndIf
			If SimulatedObjectTimer(i)=-1
				If Attributes\ModelName$="!Thwart"
					MaybeAnimateMD2(Obj\Model\Entity,3,1,81,120,1)
				EndIf
			EndIf
		
			If SimulatedObjectTimer(i)=-40
				SimulatedObjectTimer(i)=SimulatedObjectData(i,7)
			EndIf
		EndIf
	EndIf

End Function


Function ControlTroll(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	TurnObjectTowardDirection(i,-PlayerX()+SimulatedObjectX(i),-PlayerY()+SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))
	
	; shooting?
	If SimulatedObjectData(i,6)>0 And SimulatedObjectActive(i)=1001 And Attributes\Indigo=0
		dx#=PlayerX()-SimulatedObjectX(i)
		dy#=PlayerY()-SimulatedObjectY(i)
		total#=Sqr(dx^2+dy^2)
		dx=dx/total
		dy=dy/total
		
		SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
		
		If SimulatedObjectTimer(i)<0
			If SimulatedObjectTimer(i)=-10
				; aquire target now
				SimulatedObjectData(i,4)=dx*10000
				SimulatedObjectData(i,5)=dy*10000
			EndIf
			If SimulatedObjectTimer(i)=-1
				If Attributes\ModelName$="!Troll"
					MaybeAnimateMD2(Obj\Model\Entity,3,1,81,119,1)
				EndIf
			EndIf
		
			If SimulatedObjectTimer(i)=-40
				SimulatedObjectTimer(i)=SimulatedObjectData(i,7)
			EndIf
		EndIf
	EndIf

End Function


Function ControlRetroCoily(i)

	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2

End Function


Function ControlCuboid(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectData(i,5)<>Pos\TileX Or SimulatedObjectData(i,6)<>Pos\TileY
		SimulatedObjectData(i,5)=0
		SimulatedObjectData(i,6)=0
	EndIf

	SimulatedObjectXScale(i)=.9+.1*Sin((LevelTimer*2) Mod 360)
	SimulatedObjectYScale(i)=.9+.1*Sin((LevelTimer*2) Mod 360)
	SimulatedObjectZScale(i)=.9+.1*Sin((LevelTimer*2) Mod 360)
	
	If SimulatedObjectData(i,1)<>0 SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+1

End Function


Function ControlFountain(i)

	If SimulatedObjectActive(i)>0
		Obj.GameObject=LevelObjects(i)
		Pos.GameObjectPosition=Obj\Position
		
		AddParticle(SimulatedObjectData(i,0),Pos\TileX+.5,SimulatedObjectZAdjust(i)+.5,-Pos\TileY-.5,0,.1,Rnd(-.01,.01),Rnd(.07,.099),Rnd(-.01,.01),0,.001,0,-.001,0,150,3)
	EndIf

End Function

Function ControlMeteorite(i)

	AddParticle(Rand(0,3),SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZ(i)+Rnd(-.1,.1),-SimulatedObjectY(i)+Rnd(-.1,.1),0,Rnd(0.1,.5),Rnd(-.01,.01),Rnd(-.01,.01),Rnd(-.01,.01),3,.02,0,0,0,125,3)
	
End Function

Function ControlZipper(i)
	
	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=-1 ; not really used
		
		Obj.GameObject=LevelObjects(i)
		
		EntityBlend Obj\Model\Entity,3
		
		SimulatedObjectData(i,1)=Rand(0,360)
		SimulatedObjectData(i,2)=Rand(1,4)
		
	EndIf

	;zz#=.05*Sin(((Leveltimer+ObjectData(i,1))) Mod 360)
	SimulatedObjectZ(i)=0
	size#=.7+.1*Sin(leveltimer Mod 360) 
	If size<0 Then size=0

	SimulatedObjectXScale(i)=size
	SimulatedObjectYScale(i)=size
	SimulatedObjectZScale(i)=size
	If leveltimer Mod 4=1 AddParticle(Rand(24,30),SimulatedObjectX(i),SimulatedObjectZ(i),-SimulatedObjectY(i),0,.4*size,0,0.00,0,3,0,0,0,0,25,3)

End Function

Function ControlButterfly(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes
	
	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=-1 ; not really used
		
		If Attributes\ModelName$="!Busterfly"

			SimulatedObjectXScale(i)=.01
			SimulatedObjectYScale(i)=.01
			SimulatedObjectZScale(i)=.01
			SimulatedObjectRoll2(i)=90
			
			;AnimateMD2 Obj\Model\Entity,2,.4,2,9
		Else
			EntityBlend Obj\Model\Entity,3
		EndIf
		
		SimulatedObjectData(i,1)=Rand(0,360)
		SimulatedObjectData(i,2)=Rand(1,4)
		
	EndIf
	

	If Attributes\ModelName$="!Busterfly"
		zz#=.2*Sin((SimulatedObjectData(i,2)*(Leveltimer+SimulatedObjectData(i,1))) Mod 360)
		;TurnObjectTowardDirection(i,ObjectDX(i),ObjectDY(i),2,90)
		SimulatedObjectZ(i)=.4+zz
	Else
		zz#=.2*Sin(((Leveltimer+SimulatedObjectData(i,1))) Mod 360)
		SimulatedObjectZ(i)=.4+zz
		size#=.4+2*zz
		If size<0 Then size=0

		SimulatedObjectXScale(i)=size
		SimulatedObjectYScale(i)=size
		SimulatedObjectZScale(i)=size
		If leveltimer Mod 4=1 AddParticle(Rand(24,30),SimulatedObjectX(i)-3*Attributes\DX,SimulatedObjectZ(i),-SimulatedObjectY(i)+3*Attributes\DY,0,.3*size,0,0.00,0,3,0,0,0,0,15,3)

	EndIf

End Function

Function ControlSpellBall(i)

	If SimulatedObjectSubType(i)<8
		myparticle=24+SimulatedObjectSubType(i)
	Else
		myparticle=Rand(24,31)
	EndIf
	
	; do the trail
	If (LevelTimer Mod 2=0) And SimulatedObjectData(i,8)<>-99
		AddParticle(myparticle,SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZ(i)+Rnd(-.1,.1),-SimulatedObjectY(i)+Rnd(-.1,.1),0,0.5,0,0.00,0,3,.01,0,0,0,75,3)
	EndIf

End Function

Function ControlChomper(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectTileTypeCollision(i)=0
		;AnimateMD2 Obj\Model\Entity,1,.6,1,29
		SimulatedObjectYawAdjust(i)=0
		SimulatedObjectMovementSpeed(i)=20+5*SimulatedObjectData(i,0)
		;SimulatedPos\TileX=Floor(SimulatedObjectX(i))
		;SimulatedPos\TileY=Floor(SimulatedObjectY(i))
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^1+2^3+2^6
		;SimulatedObjectMovementType(i)=13
		If SimulatedObjectData(i,1)=1
			;SimulatedObjectObjectTypeCollision(i)=2^1+2^6+2^4
			EntityBlend Obj\Model\Entity,3
			
		EndIf
		If SimulatedObjectData(i,1)=2
			EntityFX Obj\Model\Entity,1
		EndIf
	EndIf
	
	
	If SimulatedObjectData(i,1)=1
		If leveltimer Mod 360<180
			EntityAlpha Obj\Model\Entity,Abs(Sin(LevelTimer Mod 360))
		Else
			EntityAlpha Obj\Model\Entity,0.3*Abs(Sin(LevelTimer Mod 360))

		EndIf
	EndIf
	
	
	;If Obj\Attributes\MovementTimer>0
	;	TurnObjectTowardDirection(i,Pos\TileX2-Pos\TileX,Pos\TileY2-Pos\TileY,3,180)
	;Else
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,1,180)
	;EndIf

End Function

Function ControlNPC(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes
	Pos.GameObjectPosition=Obj\Position

	If Attributes\ModelName$<>"!NPC" Return ; don't want to risk a MAV
	
	

	If SimulatedObjectFrozen(i)=1 Or SimulatedObjectFrozen(i)=10001 Or SimulatedObjectFrozen(i)=-1
		; freeze
		If SimulatedObjectFrozen(i)=10001 
			SimulatedObjectFrozen(i)=SimulatedObjectFrozen(i)+999
		Else
			SimulatedObjectFrozen(i)=1000*SimulatedObjectFrozen(i)
		EndIf
		SimulatedObjectCurrentAnim(i)=11
		MaybeAnimate(GetChild(Obj\Model\Entity,3),3,1,11)
		;PlaySoundFX(85,ObjectX(i),ObjectY(i))

	EndIf
	If SimulatedObjectFrozen(i)=2 Or SimulatedObjectFrozen(i)=10002
		; revert
		SimulatedObjectFrozen(i)=0
		SimulatedObjectCurrentAnim(i)=10
		MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)

	EndIf
;	If SimulatedObjectFrozen(i)>2 Or SimulatedObjectFrozen(i)<0
;		; frozen
;		SimulatedObjectFrozen(i)=SimulatedObjectFrozen(i)-1
;		
;		Return
;	EndIf

	;dist=100 ; Distance from player
	Dist=maximum2(Abs(Pos\TileX-BrushCursorX),Abs(Pos\TileY-BrushCursorY))
	; Exclamation
	If SimulatedObjectExclamation(i)>=100 And SimulatedObjectExclamation(i)<200 And Dist>4
		SimulatedObjectExclamation(i)=SimulatedObjectExclamation(i)-100
	EndIf
	If SimulatedObjectExclamation(i)>=0 And SimulatedObjectExclamation(i)<100 And Dist<4
		
		AddParticle(SimulatedObjectExclamation(i),Pos\TileX+.5,1.3,-Pos\TileY-.5,0,.5,0,0.0125,0,0,.004,0,-.0001,0,150,3)
		SimulatedObjectExclamation(i)=SimulatedObjectExclamation(i)+100
	EndIf


	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectData10(i)=-1

		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^6
		If SimulatedObjectMoveXGoal(i)=0 And SimulatedObjectMoveYGoal(i)=0
			SimulatedObjectMoveXGoal(i)=Floor(SimulatedObjectX(i))
			SimulatedObjectMoveYGoal(i)=Floor(SimulatedObjectY(i))
			;ObjectMovementType(i)=0
			;ObjectMovementTimer(i)=0
			;ObjectSubType(i)=0  
			SimulatedObjectCurrentAnim(i)=10
		EndIf

	EndIf
	
	If Attributes\Linked=-1 And SimulatedObjectData10(i)>=0
		
		; just restarted after talking and/or after transporter
	;	If ObjectMoveXGoal(i)=Pos\TileX And ObjectMoveYGoal(i)=Pos\TileY
			SimulatedObjectMoveXGoal(i)=SimulatedObjectData10(i) Mod 200
			SimulatedObjectMoveYGoal(i)=SimulatedObjectData10(i) / 200
			;SimulatedObjectMovementType(i)=10
	;	EndIf
		SimulatedObjectData10(i)=-1
	EndIf
	
	If Attributes\Flying/10=1
		; flying
		If SimulatedObjectCurrentAnim(i)<>11
			MaybeAnimate(GetChild(Obj\Model\Entity,3),1,1,11)
			SimulatedObjectCurrentAnim(i)=11
		EndIf
		TurnObjectTowardDirection(i,-(Pos\TileX-Pos\TileX2),-(Pos\TileY-Pos\TileY2),10,-SimulatedObjectYawAdjust(i))
	Else If Attributes\Flying/10=2
		; on ice
		If SimulatedObjectCurrentAnim(i)<>13
			MaybeAnimate(GetChild(Obj\Model\Entity,3),3,2,13)
			SimulatedObjectCurrentAnim(i)=13
		EndIf

	Else 
		; standing controls
		
		; Turning?
		Select SimulatedObjectData(i,7) Mod 10
		Case 0
			; Turn toward ObjectYawAdjust, i.e. Angle 0
			If SimulatedObjectYaw(i)<>0
				TurnObjectTowardDirection(i,0,1,4,0)
			EndIf
		Case 1
			; Turn Toward Player
			TurnObjectTowardDirection(i,PlayerX()-SimulatedObjectX(i),PlayerY()-SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))
		
		Case 2
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+.5) Mod 360
		Case 3
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+2) Mod 360
		Case 4
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)-.5) Mod 360
		Case 5
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)-2) Mod 360
		End Select
		
		; Jumping?
		If SimulatedObjectData(i,7)/10=1
			SimulatedObjectZ(i)=0.4*Abs(Sin((Float(Leveltimer)*3.6) Mod 360))
		Else If SimulatedObjectData(i,7)/10=2
			SimulatedObjectZ(i)=0.2*Abs(Sin((Float(Leveltimer)*7.2) Mod 360))
		EndIf
		
		; Animation?
		Select SimulatedObjectData(i,8)
		Case 0
			; Just Swaying
			If SimulatedObjectCurrentAnim(i)<>10
				SimulatedObjectCurrentAnim(i)=10
				MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)
			EndIf
		Case 1
			; Wave from time to Time
			If SimulatedObjectCurrentAnim(i)=10
				If Rand(1,10)<5 And Leveltimer Mod 120 =0 
					SimulatedObjectCurrentAnim(i)=8
					MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.2,8)
				EndIf
			Else If Animating (GetChild(Obj\Model\Entity,3))=False
				SimulatedObjectCurrentAnim(i)=10
				MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)
			EndIf


		Case 2
			; Wave All The Time
			If SimulatedObjectCurrentAnim(i)<>15
				SimulatedObjectCurrentAnim(i)=15
				MaybeAnimate(GetChild(Obj\Model\Entity,3),2,.2,15)
			EndIf
		Case 3
			; Foottap from time to Time
			If SimulatedObjectCurrentAnim(i)=10
				If Rand(1,10)<5 And Leveltimer Mod 240 =0 
					SimulatedObjectCurrentAnim(i)=9
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.4,9)
				EndIf
			Else 
				If Rand(0,1000)<2
					SimulatedObjectCurrentAnim(i)=10
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)
				EndIf
			EndIf
	
		Case 4
			; Foottap All The Time
			If SimulatedObjectCurrentAnim(i)<>9
				SimulatedObjectCurrentAnim(i)=9
				MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.2,9)
			EndIf
			
		Case 5
			; Dance
			If SimulatedObjectCurrentAnim(i)<>12
				SimulatedObjectCurrentAnim(i)=12
				If SimulatedObjectData(i,7)>=20
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.4,12)
				Else
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.2,12)
				EndIf
			EndIf
		Case 6
			; Just Sit
			If SimulatedObjectCurrentAnim(i)<>14
				SimulatedObjectCurrentAnim(i)=14
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.2,14)
			EndIf
		Case 7
			; Sit if far from player, otherwise stand
			
			If SimulatedObjectCurrentAnim(i)<>14 And dist>3
				SimulatedObjectCurrentAnim(i)=14
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.4,14)
			EndIf
			If SimulatedObjectCurrentAnim(i)<>114 And dist<=3
				SimulatedObjectCurrentAnim(i)=114
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,-.4,14)
			EndIf
		Case 8
			; Sit if far from player, otherwise stand and wave fast
			Dist=maximum2(Abs(Pos\TileX-PlayerTileX()),Abs(Pos\TileY-PlayerTileY()))
			If SimulatedObjectCurrentAnim(i)<>14 And dist>3
				SimulatedObjectCurrentAnim(i)=14
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.4,14)
			EndIf
			If SimulatedObjectCurrentAnim(i)<>114 And dist<=3
				SimulatedObjectCurrentAnim(i)=114
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,-.4,14)
			EndIf
			If SimulatedObjectCurrentAnim(i)=114 And Animating(GetChild(Obj\Model\Entity,3))=False
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.4,15)
			EndIf




		Case 9
			; Deathwave from time to Time (+Jumping)
			If SimulatedObjectCurrentAnim(i)=10
				If Rand(1,10)<5 And Leveltimer Mod 240 =0 
					SimulatedObjectCurrentAnim(i)=11
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.4,11)
					If SimulatedObjectData(i,y)<10 Then SimulatedObjectData(i,7)=SimulatedObjectData(i,7)+20
				EndIf
			Else 
				If Leveltimer Mod 120 =0 
					SimulatedObjectCurrentAnim(i)=10
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)
					SimulatedObjectData(i,7)=SimulatedObjectData(i,7)-20
					SimulatedObjectZ(i)=0
				EndIf
			EndIf

		Case 10
			; Deathwave All The Time
			If SimulatedObjectCurrentAnim(i)<>11
				SimulatedObjectCurrentAnim(i)=11
				If SimulatedObjectData(i,7)>=20
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.4,11)
				Else
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.2,11)
				EndIf

			EndIf
		End Select




	EndIf


End Function


Function ControlKaboom(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes
	Pos.GameObjectPosition=Obj\Position

	If Attributes\ModelName$<>"!Kaboom" Return
	
	If SimulatedObjectTileTypeCollision(i)=0
		; First time (should later be put into object creation at level editor)
		SimulatedObjectData10(i)=-1

		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^6
		If SimulatedObjectMoveXGoal(i)=0 And SimulatedObjectMoveYGoal(i)=0
			SimulatedObjectMoveXGoal(i)=Floor(SimulatedObjectX(i))
			SimulatedObjectMoveYGoal(i)=Floor(SimulatedObjectY(i))
			;ObjectMovementType(i)=0
			;ObjectMovementTimer(i)=0
			;ObjectSubType(i)=0  
			SimulatedObjectCurrentAnim(i)=10
			AnimateMD2 Obj\Model\Entity,0,.2,1,2

		EndIf
		
				
		
	EndIf
	
	
	If Attributes\Dead=1
		; spinning out of control
		SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360
		SimulatedObjectZ(i)=SimulatedObjectZ(i)+.01
		
		Return
	EndIf
	If Attributes\Dead=3
		; drowning
		SimulatedObjectYaw(i)=0
		SimulatedObjectZ(i)=SimulatedObjectZ(i)-.005
		
		Return
	EndIf
	
	
	;dist=100 ; Distance to player
	Dist=maximum2(Abs(Pos\TileX-PlayerTileX()),Abs(Pos\TileY-PlayerTileY()))
	; Exclamation
	If SimulatedObjectExclamation(i)>=100 And SimulatedObjectExclamation(i)<200 And Dist>4
		SimulatedObjectExclamation(i)=SimulatedObjectExclamation(i)-100
	EndIf
	If SimulatedObjectExclamation(i)>=0 And SimulatedObjectExclamation(i)<100 And Dist<4
		
		AddParticle(SimulatedObjectExclamation(i),Pos\TileX+.5,1.3,-Pos\TileY-.5,0,.5,0,0.0125,0,0,.004,0,-.0001,0,150,3)
		SimulatedObjectExclamation(i)=SimulatedObjectExclamation(i)+100
	EndIf
	
	If Attributes\Flying/10=1
		; flying
		If SimulatedObjectCurrentAnim(i)<>11
			;Animate GetChild(Obj\Model\Entity,3),1,1,11
			AnimateMD2 Obj\Model\Entity,3,2,31,60
			SimulatedObjectCurrentAnim(i)=11
		EndIf
		TurnObjectTowardDirection(i,-(Pos\TileX-Pos\TileX2),-(Pos\TileY-Pos\TileY2),10,-SimulatedObjectYawAdjust(i))
	Else If Attributes\Flying/10=2
		; on ice
		If SimulatedObjectCurrentAnim(i)<>11
			;Animate GetChild(Obj\Model\Entity,3),3,2,13
			AnimateMD2 Obj\Model\Entity,3,2,31,60
			SimulatedObjectCurrentAnim(i)=11
		EndIf

	Else 
		; standing controls
		
		; Turning?
		Select SimulatedObjectData(i,7) Mod 10
		Case 0
			; Turn toward ObjectYawAdjust, i.e. Angle 0
			If SimulatedObjectYaw(i)<>0
				TurnObjectTowardDirection(i,0,1,4,0)
			EndIf
		Case 1
			; Turn Toward Player
			TurnObjectTowardDirection(i,PlayerX()-SimulatedObjectX(i),PlayerY()-SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))
		
		Case 2
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)-.5) Mod 360
		Case 3
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)-2) Mod 360
		Case 4
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+.5) Mod 360
		Case 5
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+2) Mod 360
		End Select
		; Jumping?
		If SimulatedObjectData(i,7)/10=1
			SimulatedObjectZ(i)=0.4*Abs(Sin((Float(Leveltimer)*3.6) Mod 360))
		Else If SimulatedObjectData(i,7)/10=2
			SimulatedObjectZ(i)=0.2*Abs(Sin((Float(Leveltimer)*7.2) Mod 360))
		EndIf
		; Animation?
		Select SimulatedObjectData(i,8)
		Case 0
			; Just Swaying
			If SimulatedObjectCurrentAnim(i)<>10
				SimulatedObjectCurrentAnim(i)=10
				;Animate GetChild(Obj\Model\Entity,3),1,.05,10
				AnimateMD2 Obj\Model\Entity,0,.2,1,2
			EndIf
		
		Case 1
			; Just Sit
			If SimulatedObjectCurrentAnim(i)<>13
				SimulatedObjectCurrentAnim(i)=13
				;Animate GetChild(Obj\Model\Entity,3),3,.2,14
				AnimateMD2 Obj\Model\Entity,3,.5,31,50
			EndIf
		Case 2
			; Sit if far from player, otherwise stand
			
			If SimulatedObjectCurrentAnim(i)<>13 And dist>3
				SimulatedObjectCurrentAnim(i)=13
				;Animate GetChild(Obj\Model\Entity,3),3,.4,14
				AnimateMD2 Obj\Model\Entity,3,.5,31,50
			EndIf
			If SimulatedObjectCurrentAnim(i)<>113 And dist<=3
				SimulatedObjectCurrentAnim(i)=113
				;Animate GetChild(Obj\Model\Entity,3),3,-.4,14
				AnimateMD2 Obj\Model\Entity,3,-.5,50,31
			EndIf
		

		Case 3
			; Shiver from time to time
			If SimulatedObjectCurrentAnim(i)=10
				If Rand(1,10)<5 And Leveltimer Mod 240 =0 
					SimulatedObjectCurrentAnim(i)=15
					;Animate GetChild(Obj\Model\Entity,3),1,.4,11
					AnimateMD2 Obj\Model\Entity,2,.5,55,70
					
				EndIf
			Else 
				If Leveltimer Mod 240 =0 
					SimulatedObjectCurrentAnim(i)=10
					;Animate GetChild(Obj\Model\Entity,3),1,.05,10
					AnimateMD2 Obj\Model\Entity,3,-.2,70,53
					
					
				EndIf
			EndIf

		Case 4
			; Shiver All The Time
			If SimulatedObjectCurrentAnim(i)<>15
				SimulatedObjectCurrentAnim(i)=15
				AnimateMD2 Obj\Model\Entity,2,.5,59,70
				
			EndIf
		Case 5
			; Bounce
			If SimulatedObjectCurrentAnim(i)<>16
				SimulatedObjectCurrentAnim(i)=16
				;Animate GetChild(Obj\Model\Entity,3),3,.2,14
				AnimateMD2 Obj\Model\Entity,2,.5,31,50
			EndIf

		End Select




	EndIf



End Function


Function ControlZbotNPC(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If SimulatedObjectData(i,0)>0 And Attributes\Indigo=0
		SimulatedObjectData(i,0)=SimulatedObjectData(i,0)+1
		If SimulatedObjectData(i,0)=120 
			;DestroyObject(i,0)
			Return
		EndIf
	EndIf

	If SimulatedObjectData(i,0)>0 And Attributes\Indigo=0
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Float(SimulatedObjectData(i,0))/10.0
		SimulatedObjectZ(i)=SimulatedObjectZ(i)+0.002
		Return
	EndIf

	; particle effects
	If SimulatedObjectActive(i)>0 And SimulatedObjectActive(i)<1001 ; currently activating or deactivating
		If Rand(0,100)<50
			a=Rand(0,360)
			b#=Rnd(0.002,0.006)
			AddParticle(23,SimulatedObjectX(i)+0.5*Sin(a),0,-SimulatedObjectY(i)-0.5*Cos(a),0,.2,b*Sin(a),0.015,-b*Cos(a),1,0,0,0,0,150,3)
		EndIf
	EndIf
	
	If SimulatedObjectData(i,2)=0
		TurnObjectTowardDirection(i,-PlayerX()+SimulatedObjectX(i),-PlayerY()+SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))
	EndIf

End Function


Function ControlMirror(i)

	Obj.GameObject=LevelObjects(i)

	Select SimulatedObjectSubtype(i)	

	Case 0	; inactive
		;ObjectActivationSpeed(i)=20
		;DeActivateObject(i)
		
	Case 1,2,3,4,5	; fire, ice, time, acid, home
		;ObjectActivationSpeed(i)=4
		;ActivateObject(i)
		EntityTexture Obj\Model\Entity,MirrorTexture(SimulatedObjectSubtype(i))
		PositionTexture MirrorTexture(SimulatedObjectSubtype(i)),Sin(Leveltimer/10.0),Cos(leveltimer/17.0)
		ScaleTexture mirrortexture(Simulatedobjectsubtype(i)),0.5+0.1*Sin(leveltimer/7.0),0.5+0.1*Cos(leveltimer/11.0)
		RotateTexture mirrortexture(Simulatedobjectsubtype(i)),leveltimer / 24.0
		
		;If Leveltimer Mod 400 = 0 playsoundfx(123,objectx(i),objectY(i))
		
		
		
	End Select
		


End Function


Function ControlGhost(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectTileTypeCollision(i)=0
	
		SimulatedObjectYawAdjust(i)=0
		;SimulatedObjectMovementSpeed(i)=5+5*ObjectData(i,1)
		;SimulatedPos\TileX=Floor(ObjectX(i))
		;SimulatedPos\TileY=Floor(ObjectY(i))
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^1+2^3+2^6
		;SimulatedObjectMovementType(i)=0
		
	EndIf

	;If ObjectMovementTimer(i)>0
	;	TurnObjectTowardDirection(i,Pos\TileX2-Pos\TileX,Pos\TileY2-Pos\TileY,3,180)
	;Else
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,1,180)
	;EndIf
	
	If SimulatedObjectStatus(i)=0
		If Rand(0,100)<5
			If SimulatedObjectData(i,8)=1
				a=Rand(0,360)
				b#=Rnd(0.002,0.003)
	
				AddParticle(30,SimulatedObjectX(i)+0.2*Sin(a),0,-SimulatedObjectY(i)-0.2*Cos(a),0,.2,b*Sin(a),0.005,-b*Cos(a),1,0,0,0,0,80,3)
	
			EndIf
		EndIf
		
		If SimulationLevel>=2	
			EntityAlpha Obj\Model\Entity,Float(SimulatedObjectData(i,9))/60.0
		EndIf
		If SimulatedObjectData(i,9)>0 Then SimulatedObjectData(i,9)=SimulatedObjectData(i,9)-1
		
		;ObjectMovementType(i)=0
		If Abs(SimulatedObjectX(i)-PlayerX())<=SimulatedObjectData(i,0) And Abs(SimulatedObjectY(i)-PlayerY())<=SimulatedObjectData(i,0)

			; in range
			SimulatedObjectStatus(i)=1
			;SoundPitch SoundFX(28),21000

			;PlaySoundFX(28,ObjectX(i),ObjectY(i))
		EndIf
			
	
	Else If SimulatedObjectStatus(i)=1
		SimulatedObjectData(i,8)=1
		;ObjectMovementTYpe(i)=13
		If SimulationLevel>=2
			EntityAlpha Obj\Model\Entity,Float(SimulatedObjectData(i,9))/60.0
		EndIf
		If SimulatedObjectData(i,9)<50 Then SimulatedObjectData(i,9)=SimulatedObjectData(i,9)+2
		
		If Abs(SimulatedObjectX(i)-PlayerX())>SimulatedObjectData(i,0) Or Abs(SimulatedObjectY(i)-PlayerY())>SimulatedObjectData(i,0)

			SimulatedObjectStatus(i)=0
			;PlaySoundFX(102,ObjectX(i),ObjectY(i))
		EndIf


	EndIf


End Function


Function ControlWraith(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectTileTypeCollision(i)=0
	
		SimulatedObjectYawAdjust(i)=0
		;ObjectMovementSpeed(i)=20+5*ObjectData(i,0)
		;Pos\TileX=Floor(ObjectX(i))
		;Pos\TileY=Floor(ObjectY(i))
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^1+2^3+2^6
		;SimulatedObjectMovementType(i)=0
		
	EndIf

	;If ObjectMovementTimer(i)>0
	;	TurnObjectTowardDirection(i,Pos\TileX2-Pos\TileX,Pos\TileY2-Pos\TileY,3,180)
	;Else
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,1,180)
	;EndIf
	
	If SimulatedObjectStatus(i)=0
		If SimulationLevel>=2
			EntityAlpha Obj\Model\Entity,Float(SimulatedObjectData(i,9))/60.0
		EndIf
			
		If SimulatedObjectData(i,9)>0 Then SimulatedObjectData(i,9)=SimulatedObjectData(i,9)-1
		
		;ObjectMovementType(i)=0
		If Abs(SimulatedObjectX(i)-PlayerX())<=SimulatedObjectData(i,0) And Abs(SimulatedObjectY(i)-PlayerY())<=SimulatedObjectData(i,0)


			; in range
			SimulatedObjectStatus(i)=1
			;PlaySoundFX(29,ObjectX(i),ObjectY(i))
		EndIf
		SimulatedObjectData(i,8)=0

			
	
	Else If SimulatedObjectStatus(i)=1
		
		If SimulationLevel>=2
			EntityAlpha Obj\Model\Entity,Float(SimulatedObjectData(i,9))/60.0
		EndIf
		
		
		If SimulatedObjectData(i,9)<50 Then SimulatedObjectData(i,9)=SimulatedObjectData(i,9)+2
		
		If SimulatedObjectData(i,8)<SimulatedObjectData(i,1)
			SimulatedObjectData(i,8)=SimulatedObjectData(i,8)+1
		 	If SimulatedObjectData(i,8)=SimulatedObjectData(i,1)-20
				Select SimulatedObjectData(i,2)
					
					Case 0
						part=25
					Case 1
						part=28
					Case 2
						part=27
					
				End Select
				
				For xx=1 To 30
					AddParticle(part,SimulatedObjectX(i)+Sin(xx*12),1.1,-SimulatedObjectY(i)+Cos(xx*12),Rand(0,360),.3,-0.05*Sin(xx*12),0,-0.05*Cos(xx*12),Rnd(0,2),0,0,0,0,30,4)
				Next
				;If SimulatedObjectData(i,2)=2
				;	SimulatedObjectData(i,6)=ObjectTileX2(PlayerObject)*100+50
				;	SimulatedObjectData(i,7)=ObjectTileY2(PlayerObject)*100+50
				;Else
				;	SimulatedObjectData(i,6)=(ObjectX(PlayerObject)*100+0)
				;	SimulatedObjectData(i,7)=ObjectY(PlayerObject)*100+0
				;EndIf

				

			EndIf	
		Else
			;fire
;			If ObjectData(i,6)=-1
;				If ObjectData(i,2)=2
;					ObjectData(i,6)=ObjectTileX2(PlayerObject)*100+50
;
;					ObjectData(i,7)=ObjectTileY2(PlayerObject)*100+50
;
;				Else
;					ObjectData(i,6)=(ObjectX(PlayerObject)*100+0)
;					ObjectData(i,7)=ObjectY(PlayerObject)*100+0
;				EndIf
;
;			EndIf
			SimulatedObjectData(i,8)=0

;			SimulatedObjectData(i,6)=-1

			
		EndIf
		
		If Abs(SimulatedObjectX(i)-PlayerX())>SimulatedObjectData(i,0) Or Abs(SimulatedObjectY(i)-PlayerY())>SimulatedObjectData(i,0)

			SimulatedObjectData(i,8)=0
			
			SimulatedObjectStatus(i)=0
			;PlaySoundFX(102,ObjectX(i),ObjectY(i))
		EndIf


	EndIf

End Function


Function ControlPickUpItem(i)

	SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+2) Mod 360
	
	SimulatedObjectPitch2(i)=10*Sin(LevelTimer Mod 360)

End Function


Function ControlUsedItem(i)
		
	SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360

	If SimulatedObjectTimer(i)=120
		For j=0 To 29
			k=Rand(0,360)
			AddParticle(23,SimulatedObjectX(i)+1.8*Sin(k),SimulatedObjectZ(i),-SimulatedObjectY(i)-1.8*Cos(k),0,.4,-0.06*Sin(k),0,0.06*Cos(k),5,0,0,0,0,30,3)
		Next
	EndIf
	
	SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
	
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

Function StartMusic()

	LevelMusicCustomVolume=100
	
	If levelmusic<>CurrentMusic And GlobalMusicVolume2>0
		StopMusic()
		
		If levelmusic>0
			If levelmusic=21
				MusicChannel=PlayMusic ("data\models\ladder\valetfile.ogg")
			Else
				MusicChannel=PlayMusic ("data\music\"+levelmusic+".ogg")
			EndIf
		EndIf	
		
		CurrentMusic=levelmusic
	EndIf
	
	ChannelVolume musicchannel,GlobalMusicVolume*Float(LevelMusicCustomVolume)/100.0
	
	
	LevelMusicCustomPitch=44
	If LevelMusic=12 Then LevelMusicCustomPitch=22
	ChannelPitch MusicChannel,LevelMusicCustomPitch*1000

End Function

Function StopMusic()

	If ChannelPlaying (MusicChannel)=1
		StopChannel (MusicChannel)
	EndIf

End Function

Function UpdateMusic()

	If SimulationLevel>=SimulationLevelMusic
		StartMusic()
	Else
		StopMusic()
		CurrentMusic=0
	EndIf

End Function

Function LoopMusic()

	If currentmusic>0 ;And (gamemode<10 Or currentmenu<>10)
		; music looping
		If ChannelPlaying(musicchannel)=0
			
		
			If currentmusic=21
				MusicChannel=PlayMusic("data\models\ladder\valetfile.ogg")

			Else
				MusicChannel=PlayMusic("Data\music\"+currentmusic+".ogg")
			EndIf
			;ChannelVolume musicchannel,GlobalMusicVolume
			ChannelVolume MusicChannel,GlobalMusicVolume * Float(LevelMusicCustomVolume)/100.0
			ChannelPitch MusicChannel,LevelMusicCustomPitch*1000

			
				
		EndIf
	EndIf

End Function

Function ControlSoundscapes()

	If currentmusic=-1 ; beach
		If leveltimer Mod 250 = 0 
			sfxa=126+Rand(0,1)
			SoundPitch SoundFX(sfxa),Rand(10000,12000)
			playSoundfx(sfxa,-1,-1)
		EndIf
		If Rand(0,300)=0 
			sfxa=128+Rand(0,1)
			If sfxa=128
				If Rand(0,10)=4 
					SoundPitch SoundFX(sfxa),Rand(10000,12000)
				Else
					SoundPitch SoundFX(sfxa),Rand(19000,22000)
				EndIf
			Else
				SoundPitch SoundFX(sfxa),Rand(10000,12000)
			EndIf
			playSoundfx(sfxa,-1,-1)
		EndIf
	EndIf

End Function

Function LoadSounds()

	Restore SoundFXNames
	For i=0 To 159
;		If i=50
;			PositionEntity cube2,.8,-1,4
;			RenderWorld
;			Flip
;		EndIf
;		If i=110
;			PositionEntity cube2,.9,-1,4
;			RenderWorld
;			Flip
;		EndIf
		Read a$
		If a$<>"---"
			
			SoundFX(i)=myLoadSound("data\sound\"+a$+".wav")
			
			
		
			
			
			
		EndIf

	Next
	
	For i=160 To 186
		Read a$
	Next
	
	For i=187 To 199
		
		Read a$
		If a$<>"---"
			
			SoundFX(i)=myLoadSound("data\sound\"+a$+".wav")
			
			
		
			
			
			
		EndIf

	Next

	;PlaySoundFX(21,-1,-1)

	For i=200 To 209
		
		Read a$
		If a$<>"---"
			
			SoundFX(i)=myLoadSound("data\sound\"+a$+".wav")
			;debuglog "sound "+a$+" loaded at "+i
			
		
			
			
			
		EndIf

	Next

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
	
		Obj.GameObject=LevelObjects(i)
		Attributes.GameObjectAttributes=Obj\Attributes
		Pos.GameObjectPosition=Obj\Position
	
		If Attributes\Reactive=True
			
			; Get Scale
			ObjXScale#=SimulatedObjectXScale(i)*SimulatedObjectScaleXAdjust(i)
			ObjYScale#=SimulatedObjectYScale(i)*SimulatedObjectScaleYAdjust(i)
			ObjZScale#=SimulatedObjectZScale(i)*SimulatedObjectScaleZAdjust(i)
		
			;If (SimulatedObjectActive(i)<>0 And SimulatedObjectActive(i)<>1001) Or SimulationLevel>=2
			If SimulatedObjectActive(i)<>0 Or SimulationLevel>=2
			
				; Select Visual Animation	
				Select Attributes\ActivationType
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
					SimulatedObjectZ#(i)=-(Attributes\ActivationType-6)-.01+(Attributes\ActivationType-11)*Float(SimulatedObjectActive(i))/1001.0
					
				Case 17 ; *** THESE ONLY WORK FOR AUTODOORS - OBJECTTILEX MUST BE PRE_SET
					; push north
					SimulatedObjectY#(i)=Pos\TileY+0.5-SimulatedObjectYScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
				Case 18
					; push East
					SimulatedObjectX#(i)=Pos\TileX+0.5+SimulatedObjectXScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
			
				Case 19
					; push south
					SimulatedObjectY#(i)=Pos\TileY+0.5+SimulatedObjectYScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
			
				Case 20
					; push west
					SimulatedObjectX#(i)=Pos\TileX+0.5-SimulatedObjectXScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
			
			
			
				
				
				Case 21
					If Attributes\ModelName$="!NPC" Or Attributes\ModelName$="!Tentacle"
						Entity=GetChild(Obj\Model\Entity,3)
					Else
						Entity=Obj\Model\Entity
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
			
			
			
			
			
			Select Attributes\LogicType
			
			Case 20
				ControlTrap(i)
			Case 30
				ControlTeleporter(i)
			Case 40
				ControlSteppingStone(i)
			Case 50
				ControlSpellBall(i)
			Case 53
				ControlMeteorite(i)
			Case 54
				ControlMirror(i)
			Case 70
				ControlPickUpItem(i)
			Case 71
				ControlUsedItem(i)
			Case 110
				ControlNPC(i)
			Case 120
				ControlStinkerWee(i)
			Case 130
				ControlStinkerWeeExit(i)
			Case 150
				ControlScritter(i)
			Case 151
				ControlRainbowBubble(i)
			Case 160
				ControlObstacle(i)
			Case 161
				ControlWaterfall(i)
			Case 163
				ControlWindmillRotor(i)
			Case 164
				ControlFountain(i)
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
			Case 242
				ControlCuboid(i)
			Case 250
				ControlChomper(i)
			Case 260
				ControlBowler(i)
			Case 270
				ControlButterfly(i)
			Case 271
				ControlZipper(i)
			Case 280
				ControlSpring(i)
			Case 281
				ControlSucTube(i)
			Case 290 
				ControlThwart(i)
			Case 300
				ControlIceFloat(i)
			Case 301
				ControlPlantFloat(i)
			Case 310
				ControlRubberducky(i)
			Case 320
				ControlVoid(i)
			Case 330
				ControlWisp(i)
			Case 340
				ControlTentacle(i)
			Case 370
				ControlCrab(i)
			Case 380
				ControlTroll(i)
			Case 390
				ControlKaboom(i)
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
			Case 433
				ControlZbotNPC(i)
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
			Case 470
				ControlGhost(i)
			Case 471
				ControlWraith(i)
				
			End Select
			
			
			
			SimulateObjectPosition(i)
			SimulateObjectRotation(i)
			ScaleEntity Obj\Model\Entity,ObjXScale#,ObjZScale#,ObjYScale#
			
			SimulatedObjectLastActive(i)=SimulatedObjectActive(i)
			
		;Else
		;	AddParticle(2,ObjectXAdjust(i)+Pos\TileX+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-Pos\TileY-.5,0,.2,0,.03,0,0,.01,0,0,0,100,3)
		
			If Obj\Model\HatEntity>0
				TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)
			EndIf
			If Obj\Model\AccEntity>0
				TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)
			EndIf
			
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




Include "particles.bb"
Include "sound.bb"
Include "customcontent.bb"


.winning
Data "None (e.g. collect star)","Rescue All Stinkers","Capture/Destroy Scritters","Collect All Gems","Destroy All Bricks","Destroy FireFlowers","Race","Capture/Destroy Crabs","Rescue All BabyBoomers","Destroy All ZBots"
Data "Done"

.Commands
Data "CWHI","CGRY","CRED","CORA","CYEL","CGRE","CCYA","CBLU","CPUR","CRAI","CBLI","CWAR"
Data "ENON","ESHI","EJIT","EWAV","EBOU","EZOO","EZSH","ECIR","EEIG","EUPD","ELER","EROT"

.SoundFXNames
;0
Data "adventurewon1","adventurewon2","---","---","---","---","---","---","---","---"
;10
Data "pinball2","pickupjewel","pickupcoin2","shard","wakka","explosion","electricshock","---","---","---"
;20
Data "ice","buttonoff","buttonturn","buttontimer","colourchange","---","---","---","ghost","wraith"
;30
Data "fireon2","fireduring2","cageclose","bridgeup","bridgedown","gateon1","gateoff1","gatedoor","autodooropen","autodoorclose"
;40
Data "transporter","transporterflip","teleport","suctubein","suctubeout","flystop","---","---","---","---"
;50
Data "weehello1","weehello2","weehello3","weehello4","weehello5","weeyes1","weeyes2","weeyes3","weeyes4","weezee"
;60
Data "weeok1","weeok2","weeok3","weeok4","weeok5","weeouch1","weeouch2","weeouch3","weebored","weetired"
;70
Data "weewoo","weethanks","weebye","weeyeay","kaboom1","kaboom2","kaboom3","kaboom4","kaboom5","kaboom6"
;80
Data "magiccharge","magicblink2","magiccast","freeze","freezebreak","freezestinker","freezechomper","freezethwart","spellballbounce","---"
;90
Data "magicon","magicoff","grow","floingbubble","---","saucerfly","saucerdie","lurker","moobot1","moobot2"
;100
Data "scritterhop","chomper","flowergrow","flowerfire","flowerhit","flowerdie","thwart","thwartpickup","splosh","spikeyball"
;110
Data "brick","tentacleup","tentacledown","trollwalk","trollice","crabwalk","crabup","crabdown","bounce","chomper2"
;120
Data "waterfall","quack","quake","void","bubble","splash","surf1","surf2","seagull1","seagull2"
;130
Data "menuclick","invenopen","invenclose","loadgame","savegame","areyousure","menuclick2","loadinggame","mystery1","mystery2"
;140
Data "zbot10","zbot1","zbot2","zbot3","zbot4","zbot5","zbot6","zbot7","zbot8","zbot9"
;150
Data "voices\death1","voices\death2","voices\death3","voices\hot1","voices\hot2","lightning1","lightning2","lightning3","zbot11","zbot12"
;160
Data "lost1","lost2","lost3","lost4","start1","start2","start3","start4","start5","hello"
;170
Data "ice1","ice2","ice3","coin","item","item-no","aha","intro","---","---"
;180
Data "suc1","suc2","suc3","---","---","---","---","voices\1\extra1","voices\1\extra2","voices\1\extra3"
;190
Data "voices\1\hello","voices\1\intro","voices\2\hello","voices\2\intro","voices\3\hello","voices\3\intro","voices\4\hello","voices\4\coin","voices\3\extra1","voices\3\extra2"
;200
Data "---","---","---","---","---","---","---","---","---","---"
;Data "inflate","prism","---","---","---","---","---","---","---","---"