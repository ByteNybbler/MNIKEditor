

AppTitle "Wonderland Adventures Editor"
; Global Definitions

Include "particles-define.bb"
Include "level-define.bb"
Include "menu-define.bb"
Include "adventures-define.bb"
Include "sound-define.bb"

Dim wa3endvoidmodel(5)
Global wa3endvoidtexture

Global wa3endshipmodel
Global wa3endshiptexture

Dim wa3endstarmodel(30)
Global wa3endstartexture

Global wa3endbarren,wa3endbarrentexture,wa3endbarrencloud,wa3endbarrencloudtexture,wa3endjaava,wa3endjaavatexture
Global wa3endwonderland,wa3endwonderlandtexture,wa3endsuntexture

Global wa3endsun
Global wa3enduo,wa3enduotexture


Global DebugDisplay=False
Global TestMode=False
Global TestAdventureFilename$

Global WAEpisode=0	; 0-editor 
				   	; 1-Quest For the Rainbow Shards
					; 2-Mysteries of Fire Island
					; 3-Planet of the Z-Bots

Global VersionText$ = "v10.04 OpenSource"

Global GlobalDirName$ 

Global Leveltimer2


If FileType("localsaveon.txt")=1 Or WAEpisode=0

	GlobalDirName$="UserData"
	CreateDir(GlobalDirName$)

	
Else


	GlobalDirName$= SpecialFolderLocation($1c)+"\Midnight Synergy"



	
	
	; is the folder valid 
	If FileType(GlobalDirName$) <> 2 Then
		CreateDir(GlobalDirName$)
;		GiveDirectoryUserFullAccess(GlobalDirName$)
	EndIf
	
	GlobalDirName$ = SpecialFolderLocation($1c)+"\Midnight Synergy\WA POTZ"
	
EndIf

; is the folder valid 
If FileType(GlobalDirName$) <> 2 Then
	CreateDir(GlobalDirName$)
;	GiveDirectoryUserFullAccess(GlobalDirName$)
EndIf





CreateDir GlobalDirName$+"\Temp"
CreateDir GlobalDirName$+"\Player Profiles"

CreateDir GlobalDirName$+"\Custom"
CreateDir GlobalDirName$+"\Custom\Adventures"
CreateDir GlobalDirName$+"\Custom\Hubs"
; ... also Hubs/Textures/Models/etc

CreateDir GlobalDirName$+"\Custom\Editing"
CreateDir GlobalDirName$+"\Custom\Editing\Archive"
CreateDir GlobalDirName$+"\Custom\Editing\Current"

CreateDir GlobalDirName$+"\Custom\Downloads Inbox"
CreateDir GlobalDirName$+"\Custom\Downloads Outbox"

CreateDir GlobalDirName$+"\Custom\Leveltextures"
CreateDir GlobalDirName$+"\Custom\Icons"

Global waterplane,waterplanetexture,cloudplane,cloudplanetexture


					
					
					
Goto skiplocker	

Dim lockertext$(4)				

;If (Upper$(Mid$(CurrentDate$(),4,3))<>"SEP" And  Upper$(Mid$(CurrentDate$(),4,3))<>"OCT") Or FileType("Local")=0
	; expire
;	Restore expired
;	Read a$
;	Print a$
;	Print "-------------------------------"
;	Read a$
;	Print a$
;	Read a$
;	Print a$
;	DeleteFile "local"
;	Delay 5000
;	End
;EndIf
;Print "This is Version BETA2 of"
;Print "Wonderland Adventures: Planet of the Z-Bots"
;Print "-------------------------------------------"
;Print ""
;Print "NOT FOR DISTRIBUTION"
;Print ""
;Print "www.midnightsynergy.com"
;Delay 3000

Goto skiplocker

If lockercrypt()=False

	Restore nolockercrypt
	Read a$
	Print a$
	Print "-------------------------------"
	Read a$
	Print a$
	Read a$
	Print a$
	DeleteFile "local"
	Delay 5000
	End

EndIf	



.skiplocker


If WAEpisode=0

	; Check for New Compiled Levels
	CheckForNewCompiledLevels(False)
	
	; Check for New Compiled Hubs
	CheckForNewCompiledHubs(False)
	
EndIf
	


Global FullVersion=True
Global PortalVersion=False ;1-true, 0-false
Global AffiliateID=0

Global ExitAfterTrailer=False
Global TrailerAlreadyPlayed=False
Global TimeOfLastGameSaved=0
Global ExitAfterThisSave=False

Global GfxWidth=800;1280;800;1280;640
Global GfxHeight=600;960;600;768;480
Global GfxDepth=16
Global GfxWindowed=1
Global OldGfxWIndowed


Global NofMyGfxModes, MyGfxMode
Dim MyGfxModeWidth(1000),MyGfxModeHeight(1000),MyGfxModeDepth(1000)

Global FPSDisplay=False
Global MidnightVault=0
Global MidnightVaultTimer
Global WallBlinking=False
Global BackSpaceDown
Global LevelFormat104=False
Global Menukeydown=False

Global StarterItems

Global scoreflag

file=ReadFile (globaldirname$+"\display.wdf")
If file>0

	j=ReadInt(file)
	For i=0 To j-1
		MyGfxModeWidth(i)=ReadInt(file)
		MyGfxModeHeight(i)=ReadInt(file)
		MyGfxModeDepth(i)=ReadInt(file)
	Next
	mygfxmode=ReadInt(file)
	GfxWindowed=ReadInt(file)
	GfxWidth=MyGfxModeWidth(mygfxmode)
	GfxHeight=MyGfxModeHeight(mygfxmode)
	GfxDepth=MyGfxModeDepth(mygfxmode)
	
	
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
	
Else
	GfxWidth=800
	GfxHeight=600
	GfxWindowed=1
	GfxDepth=16


	
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

Global NoOfShards=7
Global CustomShardEnabled
Dim CustomShardCMD(NoOfShards,5)
Global CustomShardEnabledHub
Dim CustomShardCMDHub(NoOfShards,5)
;Dim CustomShardData1(NoOfShards)
;Dim CustomShardData2(NoOfShards)
;Dim CustomShardData3(NoOfShards)
;Dim CustomShardData4(NoOfShards)

Global NoOfGlyphs=5
Global CustomGlyphEnabled
Dim CustomGlyphCMD(NoOfGlyphs,5)
Global CustomGlyphEnabledHub
Dim CustomGlyphCMDHub(NoOfGlyphs,5)

Global CustomMapName$

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



Global KeyboardMode=2	; 1- mouse only, 2- mouse/keyboard, 3- gamepad?
						
Global MouseGameMode=-1	; What Game Mode the Mouse is Responsive to 
						; -1 = responsive to movement, icons, anything
						; -2 = non responsive - wait for button release
Global Mouse1, Mouse2, EscPressed

Global Tween#,TweenPeriod,TweenTime,TweenElapsed,TweenTicks
Global justregainedfocus

Type AltXType
	Field KeyPressed, InUse, Reset
	Field X#,Y#,Z#
	Field Roll#,Yaw#,Pitch#
End Type
Global AltX.AltXType=New AltXType

Dim ConsoleData(6)

; global ini file


OldGfxWindowed=GfxWindowed




; Main

Graphics3D GfxWidth,GfxHeight,GfxDepth,GfxWindowed
SetBuffer BackBuffer()
HidePointer 


file=ReadFile(globaldirname$+"\global.wdf")
If file=0
	PlayerName$=""
	PlayerCharacterName$=""
	GlobalSoundVolume2=5
	GlobalMusicVolume2=3
	KeyBoardMode=2
	DialogContrast=0
Else
	playername$=ReadString$(file)
	playercharactername$=ReadString$(file)
	GlobalSoundVolume2=ReadInt(file)
	GlobalMusicVolume2=ReadInt(file)
	KeyboardMode=ReadInt(file)
	DialogContrast=ReadInt(file)
	CloseFile file
	GlobalSoundVolume=Float(globalsoundvolume2)*0.2
	GlobalMusicVolume=Float(globalMusicvolume2)*0.2

EndIf
	
; Randomize
For bla= 1 To MilliSecs() Mod 100
	blabla=Rand(0,44)
Next

SetupCamera()


If globalmusicvolume2>0
	MusicChannel=PlayMusic ("Data\music\8.ogg")
	ChannelVolume MusicChannel,GlobalMusicVolume
	currentmusic=8
EndIf
 LevelMusicCustomVolume=100.0
 LevelMusicCustomPitch=44


StartofTitleMusic=MilliSecs()



; Loading Screen
AmbientLight 0,0,0
cube=CreateCube(camera)
Global cube2=CreateCube(camera)
ScaleEntity cube2,.4,.5,.5
PositionEntity cube2,0,-1,4
EntityColor cube2,64,64,64
EntityBlend cube2,2
If portalversion=0
	cubetex=myLoadTexture("load.jpg",1)
	EntityTexture cube,cubetex
	PositionEntity cube,0,0.1,5
	
	RenderWorld
Else
	cubetex=LoadTexture("logo.jpg",1)
	EntityTexture cube,cubetex
	PositionEntity cube,0,0,5
	
	RenderWorld
	Text GfxWidth*0.5,Gfxheight*25/28,"...Loading...",True
	Text GfxWidth*0.5,Gfxheight*26/28,"...Please Wait...",True


EndIf




For i=0 To 255 Step 3
	AmbientLight i,i,i
	RenderWorld
	Flip
Next


PositionEntity cube2,0.1,-1,4
RenderWorld
Flip
PreLoadModels()
PositionEntity cube2,0.9,-1,4
RenderWorld

Flip



PositionEntity cube2,1,-1,4
RenderWorld

Flip

For i=255 To 0 Step -5
	AmbientLight i,i,i
	RenderWorld
	Flip
Next

FreeTexture cubetex
FreeEntity cube
FreeEntity cube2

SetupMenu()
SetupLight()









ResetParticles("data/graphics/particles.bmp")
ResetText("data/graphics/font.bmp")




; Start Main Menu
If playername$="" Or FileType(globaldirname$+"\player profiles\"+playername$+"\current\playerfile.wpf")<>1
	; no player is availalbe - make a default (Stinky)
	PlayerName$="Default"
	PlayerCharacterName$="Stinky"
	PlayerTextureBody=1
	PlayerAcc1=1
	PlayerTexAcc1=1
	PlayerAcc2=0
	PlayerTexAcc2=0
	PlayerSizeX#=0.035
	PlayerSizeY#=0.035
	PlayerSizeZ#=0.035
	PlayerVoice=1
	PlayerPitch=1
	CreateNewPlayer()
Else

	loadplayer(globaldirname$+"\player profiles\"+playername$+"\current\playerfile.wpf")
EndIf



If WAEpisode=0
	testfile=ReadFile(globaldirname$+"\temp\test.dat")
	If testfile<>0 ;FileType(globaldirname$+"\custom\editing\")
		TestAdventureFilename$=ReadString$(testfile)
		CloseFile testfile
		TestMode=True
		HideEntity TitleMenuEntity(79)
		StartAdventure(globaldirname$+"\custom\editing\current\"+TestAdventureFilename,2,0)
	Else
		StartMenu(11)
	EndIf
Else
	StartMenu(10)
EndIf





AltX\InUse=True


fpscounter=-1
fps=0
fpstime=0


TweenPeriod=1000/60;85
TweenTime=MilliSecs()-TweenPeriod

Global EndGame=False

Repeat

	If HasFocus()
	
		If justregainedfocus=1
			justregainedfocus=0
			If currentmusic>0 Then ResumeChannel(musicchannel)
		EndIf
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
			
			; do the game in here
			
			If GameMode<10
				UpdateGame()
			;Else If GameMode=10
			;	UpdateAdventureTitle()

			;Else If gamemode=12
				; Menus
			;	UpdateMenu()
			EndIf
			
				
			If GameMode<>12
				ControlLight()
				LevelTimer=LevelTimer+1	
				AdventureTimer=AdventureTimer+1	
				
				UpdateWorld
			EndIf
		
		Next
		
		
		
		
		If GameMode=10
			UpdateAdventureTitle()

		Else If gamemode=12
			; Menus
			UpdateMenu()
		EndIf
	
	
		
		If fpscounter=-1
			fpstime=MilliSecs()
			fpscounter=100
		Else If fpscounter=0
			timepassed=MilliSecs()-fpstime
			fps=100000/timepassed
			fpscounter=100
			fpstime=MilliSecs()
		Else
			fpscounter=fpscounter-1
		EndIf
		MX=Floor(MousePickX)
		MY=Floor(MousePickY)
	
		If FPSDisplay=True;False
			DisplayText("FPS: "+fps,0,46,.5,1,255,255,255)
			
			
		EndIf
	
		If DebugDisplay=True Then
			DisplayText("Tile   Logic: "+LevelTileLogic(Mx,MY),0,0,1,1,255,255,255)
			DisplayText("Object Logic: "+objecttilelogic(mx,my),0,1,1,1,255,255,255)
			DisplayText("Level  Timer: "+Leveltimer,0,2,1,1,255,255,255)
		EndIf

	
	
		; MESSAGELINE
		If MessageLineTimer>0 And MessageLineText1$<>""
			ShowEntity DialogBackGroundEntity2
			If MessageLineTimer<30
				PositionEntity DialogBackGroundEntity2,0,.4-.4*Float(30-MessageLineTimer)/30.0,0

				DisplayText(MessageLineText1$,24.5-Len(MessageLineText1$)/2.0,24-2*MessageLineTimer/30.0,1,1,255,255,0)
				DisplayText(MessageLineText2$,24.5-Len(MessageLineText2$)/2.0,25-2*MessageLineTimer/30.0,1,1,255,255,0)
	
			Else 
				PositionEntity DialogBackGroundEntity2,0,.4,0
				DisplayText(MessageLineText1$,24.5-Len(MessageLineText1$)/2.0+.25*Sin(16*(messagelinetimer-30)),22,1,1,255,255,0)
				DisplayText(MessageLineText2$,24.5-Len(MessageLineText2$)/2.0+.25*Sin(16*(messagelinetimer-30)),23,1,1,255,255,0)
	
			EndIf
		Else
			HideEntity DialogBackGroundEntity2
		EndIf
		If MessageLineTimer>0 Then MessageLineTimer=MessageLineTimer-1
	
		; ICON SUBTEXTS
		exv#=0
		exv2#=0
		If widescreen And wideicons
			exv=16.3
			exv2=10.8
		EndIf
		For i=0 To 79
			ex#=exv
			If (i Mod 10)<2 And ex>0
				ex=-ex
			EndIf
			If IconEntity(i)>0 And IconSize(i)>=1001
				If IconSize(i)=1201 
					DisplayText(IconHelpText$(i),3.2+10.1*(i Mod 10)-(Len(IconHelpText$(i))-1.0)/2.0+ex,4.3+6*Floor(i/10),.5,1,255,255,255)
				Else
					DisplayText(IconSubText$(i),3.2+10.1*(i Mod 10)-(Len(IconSubText$(i))-1.0)/2.0+ex,3.9+6*Floor(i/10),.5,1,255,255,255)
				EndIf
			EndIf
		Next
		If GameMode=5
			DisplayText("Items:",40.6-6.8*(InventorySize-3)+exv2,2.67,.75,1,255,255,0)
		EndIf
		If GameMode=6
			DisplayText("Swap Item:",40.6-6.8*(InventorySize-3)+exv2,2.67,0.75,1,255,255,0)
		EndIf	
		If GameMode=5 Or GameMode=6
			
			DisplayText("Stars",49.65-4*.5+exv2,16+(InventorySize-3)*4,.75,1,255,255,0)
			DisplayText("Coins",56.3-4*.5+exv2,16+(InventorySize-3)*4,.75,1,255,255,0)
			DisplayText("Gems",43.5-4*.5+exv2,16+(InventorySize-3)*4,.75,1,255,255,0)

			DisplayText("Score: "+PlayerScore,52.3-Len(playerscore)+exv2,20+(InventorySize-3)*4,.75,1,255,255,0)
	
			
			If PlayerStars<10
				k=0
			Else If PlayerStars<100
				k=1
			Else If PlayerStars<1000
				k=2
			Else If PlayerStars<10000
				k=3
			Else
				k=4
			EndIf
	
			DisplayText(Str$(PlayerStars),49.65-k*0.5+exv2,18.8+(InventorySize-3)*4,.75,1,255,255,0)
			
			If PlayerCoins<10
				k=0
			Else If PlayerCoins<100
				k=1
			Else If PlayerCoins<1000
				k=2
			Else If PlayerCoins<10000
				k=3
			Else
				k=4
			EndIf
			DisplayText(Str$(PlayerCoins),56.3-k*0.5+exv2,18.8+(InventorySize-3)*4,.75,1,255,255,0)
			
			
			If PlayerGems<10
				k=0
			Else If PlayerGems<100
				k=1
			Else If PlayerGems<1000
				k=2
			Else If PlayerGems<10000
				k=3
			Else
				k=4
			EndIf
			DisplayText(Str$(PlayerGems),43-k*0.5+exv2,18.8+(InventorySize-3)*4,.75,1,255,255,0)
		EndIf
		
		

		
		; TEXT DIALOG
		If GameMode=8
			DisplayDialog()
			
		EndIf	
		
		If KeyDown(63) Then FpsDisplay=True
		
	;	If MidnightVault=0 And KeyDown(6)
	;		MidnightVault=1
	;		MidnightVaultTimer=MilliSecs()
	;	Else If MidnightVault=1 And KeyDown(9)
	;		MidnightVault=2
	;	Else If MidnightVault=2 And KeyDown(3)
	;		MidnightVault=3
	;	Else If MidnightVault=3 And KeyDown(6)
;	;		MidnightVault=4
	;		MessageLineText1$="Sorry. I don't have the"
	;		MessageLineText2$="key to the Vault ;-)"
	;		MessageLineTimer=100
	;	Else If KeyDown(2) Or KeyDown(4) Or KeyDown(5) Or KeyDown(8) Or KeyDown(10)
	;		MidnightVault=0
	;	EndIf
		If KeyDown(48) And (KeyDown(29) Or KeyDown(157))
			WallBlinking=True
			MessageLineText1$="Wall-Blinking Activated"
			MessageLineText2$="(use at your own risk)"
			MessageLineTimer=100
		EndIf

		If MidnightVaultTimer>0 And MidnightVault<5
			If MilliSecs()-MidnightVaultTimer>1500
				MidnightVault=0
				MidnightVaultTimer=0
			EndIf
		EndIf
		
	
		;If KeyDown(29) 
		;	TweenPeriod=1000
		;E;lse 
	;		tweenperiod=1000/60
	;	EndIf
		
		
		
		
		
		If KeyDown(1)
			If EscPressed=False
				EscPressed=True
			
			
				; select via game mode
				If gamemode<10
					; in game
					If delaycommand=7 Or delaycommand=8
					
					Else If CurrentCharm=3 Or CurrentCharm=4
						; turn off spy eye\map
						ShowEntity LevelCursor
						CurrentCharm=0
						TurnOnIcons()
						DeleteIcon(1)
						MouseGameMode=-2
						AutoGlowGem()
						
					Else If LevelTimer<1000001000 Or LevelTimer>=1000001500
						; not in adventure won animation
						PlaySoundFX(131,-1,-1)
						HideEntity LevelCursor
						StartMenu(0)
					Else 
						; in adventure won animation
						LevelTimer=1000001498

					EndIf
				Else If gamemode=10
					; in adventure start animation
					LevelTimer=1000000000
				Else If gamemode=12
					; in menu
					Select currentmenu
					Case 0
						; in game 
						endmenu()
						startmenu(3)
					Case 1,2,4,5,6
						endmenu()
					Case 3
						; in game exit are you sure
						EndMenu()
						endLevel()
						endadventure()
						StartMenu(11)
					
					Case 11
						; main menu
						endmenu()
						startmenu(19)
					Case 16,18,26   ;,14,15 - don't escape from 14/15 since new profile name already entered
						endmenu()
						startmenu(11)
					Case 20
						endmenu()
						PlayerName$=OldPlayerName$
						PlayerCharacterName$=OldPlayerCharacterName$
						startmenu(11)

						
					Case 12
						If OldPlayerName$<>"" And OldPlayerCharacterName$<>""
							PlayerName$=OldPlayerName$
							PlayerCharacterName$=OldPlayerCharacterName$

							endmenu()
							startmenu(11)
						EndIf
					Case 13
						If OldPlayerName$<>"" And OldPlayerCharacterName$<>""
							PlayerName$=OldPlayerName$
							PlayerCharacterName$=OldPlayerCharacterName$
							endmenu()
							startmenu(11)
						EndIf
					Case 14
						If OldPlayerCharacterName$=".." ; can only exit if from within options menu
							PlayerCharacterName$=OldPlayerCharacterName2$
							OldPlayerCharacterName$=""
							endmenu()
							startmenu(11)
						EndIf
					Case 15
						endmenu()
						startmenu(14)
					Case 17
						endmenu()
						startmenu(12)	
						
					Case 19
						End	
					Case 100
						endcustomselectmenu()
						startmenu(11)
						MouseGameMode=2
					Case 101
						CurrentMenu=100
						CustomLevelListSelected=-1

							

					
					End Select
					
				EndIf
				
			EndIf
		Else
			EscPressed=False
			
			
		EndIf
		
		If GfxWindowed=2 ; remove mousecursor if at endge of screen in windowed mode
			If MouseX()<10 Or MouseY()<10 Or MouseX()>GfxWidth-10 Or MouseY()>GfxHeight-10
				HideEntity MouseCursor
			Else If MouseCursorVisible=True
				ShowEntity MouseCursor
			EndIf
		EndIf

		
		RenderGame()
		


	Else
		; out of focus
		If currentmusic>0
			PauseChannel musicchannel
		EndIf
		justregainedfocus=1
		Repeat
			Delay 200
		Until HasFocus()
		TweenTime=MilliSecs()
		
		
	EndIf	
	
Until endgame=True

; clear memory
ClearWorld()
EndGraphics()

; play exit sound
SeedRnd MilliSecs()
byesnd=Rand(1,2)

If byesnd=1 mysndfile$ = "weebye" ;71
If byesnd=2 mysndfile$ = "weethanks" ;72

byesndfx = myLoadSound("Data\Sound\" + mysndfile$+".wav")
SoundVolume byesndfx,GlobalSoundVolume
byesndchn=PlaySound(byesndfx)

While ChannelPlaying(byesndchn)
Wend

End



Function UpdateGame()

	ResetSounds()
	
	
	
	
	Mouse1=False
	Mouse2=False
	If MouseDown(1)=True Or MouseHit(1)>0
		Mouse1=True
	EndIf
	If MouseDown(2)=True Or MouseHit(2)>0
		Mouse2=True
	EndIf
	;widescreen
	If widescreen
		PositionEntity MouseCursor,-13.28+26.625*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	Else 
		PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	EndIf
	If GameMode=0 

		ShowEntity LevelCursor
	EndIf



	CameraControls()
	ControlCamera()
	ControlIcons()
	ControlDialog()
	ControlObjects()
	ControlParticles()

	
	
	; Check End-of-Adventure
	If LevelTimer<1000001000 Or LevelTimer>=1000002000
		Flag=False
		Select AdventureGoal
		Case 0
			; no goal
		Case 1
			If NofWeeStinkersInAdventure=0
				Flag=True
			EndIf
		Case 2
			If NofScrittersInAdventure=0
				Flag=True
			EndIf
		Case 3
			If NofGemsInAdventure=0
				Flag=True
			EndIf
		Case 4
			If NofBricksInAdventure=0
				Flag=True
			EndIf
		Case 5
			If NofFireFlowersInAdventure=0
				Flag=True
			EndIf
		Case 6
			; race - no goal
		Case 7
			If NofCrabsInAdventure=0
				Flag=True
			EndIf
		Case 8
			If NofBabyBoomersInAdventure=0
				Flag=True
			EndIf
		Case 9
			If NofZBotsInAdventure=0
				Flag=True
			EndIf



		End Select
		If Flag=True ; And MidnightVault>0)
			
			AdventureWon()
			

		EndIf
		
		If KeyDown(88) And (KeyDown(29) Or KeyDown(157)) And AdventureCurrentStatus>0 
		
			; some levels don't accept cheats
			If WaEpisode=2 And (AdventureCurrentNumber=130 Or AdventureCurrentNumber=59 Or AdventureCurrentNumber=141 Or AdventureCurrentNumber=142 Or AdventureCurrentNumber=143 Or AdventureCurrentNumber=144)
			
			Else If WAEpisode=3 And AdventureCurrentNumber=165
			
			Else
				; cheat to finish level
				AdventureCancelData()
				;AdventureTimer=600000
				CurrentAdventureTime=0
				CurrentAdventureGems=0
				CurrentAdventureCoins=0
				AdventureWon()
				
				; special case - give Floing Orb/Gloves
				If adventurecurrentnumber=28 And waepisode=2
					AddItemToInventory(-1,4001+2*10,191,12+64,"Item","Use")
	
				EndIf
				If adventurecurrentnumber=3 And waepisode=2
					AddItemToInventory(-1,1001,-1,23,"Gloves","Examine")
				EndIf
			EndIf
		EndIf
	EndIf
	
	

	
	; Do End of Adventure Animations
	If LevelTimer>1000001000 And LevelTimer<1000002000
		AdventureWonUpdate()
		
	EndIf
	If LevelTimer>1000002000 And LevelTimer<1000003000
		AdventureLostUpdate()
		
	EndIf

	
	If Mouse1=False And Mouse2=False 
		MouseGameMode=-1
		If currentcharm<>3 And currentcharm<>4 
			For i=0 To 79
				; activate icons
				If IconEntity(i)>0 Then EntityPickMode IconEntity(i),2
			Next
		EndIf
	EndIf
		
	
	If DelayCommand=7 And LevelTimer=1000000050
	
		
		If AdventureCurrentStatus=0
		
			
		
		
			; Currently in Hub
			CurrentReplayAdventure=0
			DelayCommand=0
			
			; save levelstate
			SaveLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+Str$(-AdventureCurrentLevel)+".wlv")
			EndLevel()
			
			
			
			If DelayData1=27 And FullVersion=False
				
				; demo end
				startmenu(25)
				Return
			Else
			
			
			
				If Abs(delaydata1)=50 And waepisode=2; Mystery Level - always reset
					CopyFile "Data\Adventures\Hub\50.wlv",GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\50.wlv"
				EndIf
				
				; and start the new level
				StartLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+Str$(Delaydata1)+".wlv",False)
	
				CreatePlayer(Delaydata2+.5,Delaydata3+.5)
				
				AdventureCurrentLevel=-DelayData1
				GetHubLevelName()
			EndIf

			
		Else
			; Currently in Adventure
			DelayCommand=0
			; save levelstate
			SaveLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Adventure\"+Str$(AdventureCurrentLevel)+".wlv")
			EndLevel()
			
			; and start the new level
			StartLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Adventure\"+Str$(Delaydata1)+".wlv",False)

			AdventureCurrentLevel=DelayData1
			CreatePlayer(Delaydata2+.5,Delaydata3+.5)
		EndIf
	EndIf
	If DelayCommand=8 And LevelTimer=1000000050
		; Start an Adventure 
		DelayCommand=0
		; save levelstate
		SaveLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+Str$(-AdventureCurrentLevel)+".wlv")
		EndLevel()
		
		; and start the adventure
		If delaydata1>0
		
			If InCustomHub=0
				advdirname$="data\adventures\Adventure"
			Else
				advdirname$=incustomhubname$+"Adventure"
			EndIf
			
			If currentreplayadventure=0
				; regular
				StartAdventure(advdirname$+delaydata1,1,delaydata1)
			Else
				;replay
				StartAdventure(advdirname$+delaydata1,3,delaydata1)
			EndIf
				
		Else
			; custom
			StartAdventure(AdventureCurrentName$,2,0)
		EndIf

		
		
		

			
		
		
	EndIf
	If delaycommand=102
		delaycommand=0
		enddialog()
		SaveLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+Str$(-AdventureCurrentLevel)+".wlv")

		endlevel()
		menutimer=0
		startmenu(21)	
	EndIf
	If delaycommand=103
		delaycommand=0
		enddialog()
		SaveLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+Str$(-AdventureCurrentLevel)+".wlv")

		endlevel()
		menutimer=0
		startmenu(22)	
	EndIf
	
	If delaycommand=104
		delaycommand=0
		enddialog()
		SaveLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+Str$(-AdventureCurrentLevel)+".wlv")

		endlevel()
		menutimer=0
		startmenu(23)	
	EndIf


	If delaycommand=115
		delaycommand=0
		enddialog()
		startmenu(24)
	EndIf
	
	
	; soundscapes
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
	
	; weather animations etc
	
	
	; get centre for weather particles
	If currentcharm<>3
		CentreX#=Objectx(CameraFocusObject)
		CentreY#=-ObjectY(CameraFocusObject)
	Else
	
		mpx#=MousePickX
		mpy#=MousePickY
		If mpx<0 Then mpx=0
		If mpy<0 Then mpy=0
		If mpx>Float(LevelWidth) Then mpx=Float(LevelWidth)
		If mpy>Float(LevelHeight) Then mpy=Float(LevelHeight)
		
		CentreX#=(mpx+(mpx+ObjectX(CameraFocusObject))/2.0)/2.0
		CentreY#=-(mpy+(mpy+ObjectY(CameraFocusObject))/2.0)/2.0
	EndIf


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
					playsoundfx(Rand(155,157),-1,-1)
				EndIf
			EndIf
			
			SetLight(lightningstorm,lightningstorm,lightningstorm,10,70,70,70,10)
		EndIf
	Case 8
		If leveltimer<1000000000
			; red alert
			

			alarm=leveltimer Mod 240
			If alarm=1 Then playsoundfxnow(98)
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

	PositionTexture StarTexture,0,Float(leveltimer Mod 1000) / 1000.0
	PositionTexture RainbowTexture,0,Float(leveltimer Mod 1000) / 1000.0
	PositionTexture GhostTexture,0,Float(leveltimer Mod 1000) / 1000.0
	For i=0 To 2
		PositionTexture WraithTexture(i),Float(leveltimer Mod 100) / 100.0,0
	Next
	
	; cheats
	If MidnightVault=5
		If KeyDown(72) ObjectFlying(PlayerObject)=10
		If KeyDown(73) ObjectFlying(PlayerObject)=11
		If KeyDown(77) ObjectFlying(PlayerObject)=12
		If KeyDown(81) ObjectFlying(PlayerObject)=13
		If KeyDown(80) ObjectFlying(PlayerObject)=14
		If KeyDown(79) ObjectFlying(PlayerObject)=15
		If KeyDown(75) ObjectFlying(PlayerObject)=16
		If KeyDown(71) ObjectFlying(PlayerObject)=17
		If KeyDown(76) ObjectFlying(PlayerObject)=0
		; cheats
	;
	EndIf
	If KeyDown(11) ;And KeyDown(11)
		MidnightVault=1
		

	;	Print midnightvault
	;	Delay 1000
		
	EndIf
	If KeyDown(3) 
		If MidnightVault=1 Or MidnightVault=2
			MidnightVault=2
		Else If Midnightvault<5
			Midnightvault=0
		EndIf
	;	Print midnightvault
	;	Delay 1000
	;	MessageLineText1$="Keyboard Error"
;		MessageLineText2$="Midnight Vault
	;	MessageLineTimer=20

		

	EndIf
	If KeyDown(10)  
		If MidnightVault=2 Or MidnightVault=3

			MidnightVault=3
		Else If Midnightvault<5
			Midnightvault=0
		EndIf

	;	Print midnightvault
	;	Delay 1000
		


		

	EndIf
	If KeyDown(9)  
		If MidnightVault=3 Or MidnightVault=4

			MidnightVault=4
		Else If Midnightvault<5
			Midnightvault=0
		EndIf

		;MidnightVault=4
	;	Print midnightvault
	;	Delay 1000
		


	EndIf
	
	If KeyDown(8)  
		If MidnightVault=4 Or MidnightVault=5

			MidnightVault=5
				MessageLineText1$="Midnight Vault"
				MessageLineText2$="has been unlocked!"
				MessageLineTimer=100
		Else If Midnightvault<5
			Midnightvault=0
		EndIf
 
		
	;	Print midnightvault
	;	Delay 1000
		


	EndIf


	If KeyDown(4) Or KeyDown(5) Or KeyDown(6) Or KeyDown(7) Or KeyDown(2) 
		If Midnightvault<5 Then MidnightVault=0
	;	Print midnightvault
	;	Delay 1000

		


	EndIf

	
	If (KeyDown(16) And KeyDown(18))  And Midnightvault=3
		
		
	    PlayerCoins=100
		PlaySoundFXNow(10)
		MessageLineText1$="Coin"
		MessageLineText2$=""
		MessageLineTimer=100

		Delay 200
		
	EndIf
	If (KeyDown(17) And KeyDown(19)) And Midnightvault=3
		
	    PlayerStars=PlayerStars+1
		PlaySoundFXNow(10)
		MessageLineText1$="Star"
		MessageLineText2$=""
		MessageLineTimer=100


	
		Delay 200
		
	EndIf

	

	If KeyDown(14) 
		BackSpaceDown=BackSpaceDown+1
		If MidnightVault=5 Or BackSpaceDown=3000
			Repeat
			Until KeyDown(14)=False
			Locate 0,0
			Color 0,0,0
			Rect 0,0,300,200,True
			Color 255,255,255
			c=Input ("Command:")
			d1=Input ("Data 1:")
			d2=Input ("Data 2:")
			d3=Input ("Data 3:")
			d4=Input ("Data 4:")
			activatecommand(c,d1,d2,d3,d4)
			Repeat
			Until KeyDown(14)=False

		EndIf
	Else
		BackSpaceDown=0	
	EndIf
	
	If Midnightvault=5
		instamagic=0
		If KeyDown(42) Or KeyDown(54)
			If KeyDown(2) Then instamagic=1
			If KeyDown(3) Then instamagic=2
			If KeyDown(4) Then instamagic=3
			If KeyDown(5) Then instamagic=4
			If KeyDown(6) Then instamagic=5
			If KeyDown(7) Then instamagic=6
			If KeyDown(8) Then instamagic=7
			If KeyDown(9) Then instamagic=9
			If KeyDown(10) Then instamagic=10
		EndIf
		For j=0 To 99 ;check for gloves
			If InventoryItem(j)=1001
			; recharge!
		If instamagic>0	And (currentspellpower<9 Or currentspell<>instamagic-1)	
			PlaySoundFX(80,-1,-1)
			CurrentSpell=instamagic-1
			CurrentSpellPower=9
						
			; activate glove icon
			DeleteIcon(0)
			If CurrentSpell=5 And IndigoActive=3
				CreateIcon(0,0,16+CurrentSpell,1002+CurrentSpell,"X 9 X","Activate")
			Else
				CreateIcon(0,0,16+CurrentSpell,1002+CurrentSpell,"- 9 -","Activate")
	
			EndIf
		EndIf
		EndIf
		Next
						
	EndIf
		

	PlayAllSounds()

End Function



Function RenderGame()
	
	; Only called once per frame 
	; - any timing related issues should be dependent on the change in LevelTimer (not just +1)
	; 
	
	RenderParticles()
	RenderLetters()
	If GameMode<10
	;	If CurrentCharm<>3
	;		UpdateWater(ObjectX(CameraFocusObject),ObjectY(CameraFocusObject)-3,12)
	;	Else
			UpdateWater(EntityX(Camera),-EntityZ(Camera)-12,12)

	;	EndIf
	EndIf
	
	;numTriangles = CountTriangles(TextSurface)
	;DebugLog "Number of Triangles on Text Surface: "+numTriangles
	
	tween2#=tween 
	If tween<=0.0 Or tween>=1.0
		
		RenderWorld
	Else
	
		RenderWorld tween
	EndIf
	Flip;  False
	
	
	
	
	
End Function

Function UpdateAdventureTitle()

	If Leveltimer=0 Then Return
	
	If Leveltimer<1000000000 Then leveltimer2=leveltimer

	CameraPick (camera,MouseX(),MouseY())
	
	If LevelTimer=10
		ShowEntity LevelCursor
		EntityAlpha LevelCursor,.5
		;For i=1 To 3
		;	If AdventureTitleSprite(i)>0 Then PositionEntity AdventureTitleSprite(i),-0.75+(i-1)*0.75,-.95,5
		;Next
		PositionEntity AdventureTitleStar,0,0.27,4.7
		PositionEntity AdventureTitleSprite(0),0,-.9+.6*Float(LevelTimer)/100.0,1+3*Float(LevelTimer)/100.0

	EndIf
	If LevelTimer=16
		ShowEntity AdventureTitleStar
	EndIf

	;If LevelTimer<100
		AddParticle(Rand(16,23),PickedX(),PickedY(),PickedZ()-.011,0,0.01,Rnd(-.005,.005),Rnd(-.004,.008),0,5,.001,0,-.00005,0,75,3)
	;EndIf
	;If LevelTimer>100 And LevelTimer<1000000000
	;	AddParticle(Rand(16,23),PickedX(),PickedY(),PickedZ()-.011,0,0.01,Rnd(-.005,.005),Rnd(-.004,.008),0,5,.004,0,-.00005,0,75,3)
	;EndIf
	PositionEntity LevelCursor,PickedX(),PickedY(),PickedZ()
	ScaleEntity LevelCursor,.2,.2,.2
	PointEntity LevelCursor,Camera,(LevelTimer*4) Mod 360
	TurnEntity LevelCursor,90,0,0
	
	ControlParticles()



	PositionTexture AdventureTitleBackgroundTex,(LevelTimer2 Mod 5000)/5000.0,(LevelTimer2 Mod 7000)/7000.0
	TurnEntity AdventureTitleStar,0,0,2
	If LevelTimer>15 And LevelTimer<300
		EntityAlpha AdventureTitleBackground,Float(LevelTimer)/300.0
	
	EndIf

	If LevelTimer>15 And LevelTimer<100
		PositionEntity AdventureTitleSprite(0),0,0.2+.6*Float(LevelTimer)/100.0,1+3*Float(LevelTimer)/100.0
		EntityAlpha AdventureTitleSprite(0),Float(LevelTimer)/100.0
		

	EndIf
	If leveltimer<1000
		EntityAlpha AdventureTitleStar,Float(LevelTimer)/1000.0
	;	If leveltimer=199
	;		EntityBlend AdventureTitleStar,3
;
;		EndIf
	EndIf

	
	If LevelTimer<1000000000
		RotateEntity AdventureTitleSprite(0),0,0,10*Sin((LevelTimer-100))
		ScaleEntity AdventureTitleSprite(0),0.9+0.1*Sin(LevelTimer Mod 1000000000),.90+0.1*Sin(leveltimer),0.001
	EndIf
	
;	If Leveltimer>200 And LevelTimer<250
;		For i=1 To 3
;			If AdventureTitleSprite(i)>0
;				ScaleSprite AdventureTitleSprite(i),0.006*(Leveltimer-200),0.006*(LevelTImer-200)
;			EndIf
;		Next
;	EndIf
	
	If LevelTimer<1000000000
		col=0
		If Leveltimer>200 And LevelTimer<250
			col=(LevelTimer-200)*5
		Else If LevelTimer>=250
			col=255
		EndIf
;		If LevelTimer>200 
;			DisplayText(AdventureHelpLine$(1),34.4-0.5*Len(AdventureHelpLine$(1)),39.5,0.5,1,col,col,col)
;			DisplayText(AdventureHelpLine$(2),49.5-0.5*Len(AdventureHelpLine$(2)),39.5,0.5,1,col,col,col)
;			DisplayText(AdventureHelpLine$(3),64.8-0.5*Len(AdventureHelpLine$(3)),39.5,0.5,1,col,col,col)
;		EndIf
		
		col=255
		If leveltimer<64
			col=leveltimer*4.0
		EndIf
		If Leveltimer<Len(AdventureTitle$)*5
			DisplayText(Left$(AdventureTitle$,leveltimer/5),25.0/1.5-0.5*Len(AdventureTitle$),8+.2*Sin(leveltimer*2),1.5,1,col,col,0)
		Else 
			DisplayText(AdventureTitle$,25.0/1.5-0.5*Len(AdventureTitle$),8+.2*Sin(leveltimer*2),1.5,1,col,col,0)
		EndIf
		col=255
		If leveltimer<256
			col=leveltimer/1.0
		EndIf
	
		
		For i=0 To 4
			DisplayText(AdventureTextLine$(i),25.0-0.5*Len(AdventureTextLine$(i)),14+i,1,1,col,col,col)
		Next
		
		col=256*2/3
		If leveltimer<512
			col=leveltimer/3.0
		EndIf

		TurnEntity Titlemenuentity(79),0,0,0.006

		;If leveltimer>150 
			DisplayText("(Click To Start The Adventure)",25.0-0.5*30,21,1,1,col,col,0)
		;EndIf
	EndIf


	If (MouseDown(1)=True Or KeyDown(57) Or KeyDown(28) Or KeyDown(156)) And LevelTimer<1000000000 And LevelTimer>40
		LevelTimer=1000000000
	EndIf
	
	If LevelTimer>1000000000
		;For i=0 To 3
		;	If AdventureTitleSprite(i)>0 Then EntityAlpha AdventureTitleSprite(i),0.5-Float(LevelTimer-1000000000)/100.0
		;Next
		EntityBlend AdventureTitleStar,1
		EntityAlpha AdventureTitleStar,0.5-Float(LevelTimer-1000000000)/100.0
		EntityAlpha AdventureTitleBackGround,0.5-Float(LevelTimer-1000000000)/100.0
		EntityAlpha AdventureTitleSprite(0),0.5-Float(LevelTimer-1000000000)/100.0
		
		
		TranslateEntity AdventureTitleSprite(0),0,-.025,-.1


	EndIf
	
	If LevelTimer=1000000050
		For i=0 To 0
			If AdventureTitleSprite(i)>0
				FreeEntity AdventureTitleSprite(i)
				AdventureTitleSprite(i)=0
			EndIf
		;	If AdventureTitleSpriteTex(i)>0
		;		FreeTexture AdventureTitleSpriteTex(i)
		;		AdventureTitleSpriteTex(i)=0
		;	EndIf
		Next
		FreeEntity AdventureTitleBackground
		AdventureTitleBackground=0
	;	FreeTexture AdventureTitleBackgroundTex
	;	FreeEntity AdventureTitleStar
	;	AdventureTitleStar=0
	;	FreeTexture AdventureTitleStarTex
		AdventureTitleStartex=0
		
		GameMode=0
	
		; Start Level 1 and Create Player
		
		StartLevel(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Adventure\"+Str$(AdventureCurrentLevel)+".wlv",False)
		CreatePlayer(AdventureStartX,AdventureStartY)
		MouseGameMode=-2
		
		ResetParticles("data/graphics/particles.bmp")
		ScaleEntity LevelCursor,1,1,1
		RotateEntity LevelCursor,0,0,0
		
		If NofZBotNPCsInAdventure=0 ; play start SFX except if Zbot around
			PlaySoundFXNow(Rand(164,168))
		EndIf
		
		; Menu
		CreateIcon(9,0,4,4,"Menu","Open")
		; Rucksack
		If NofInventoryItems>0 Then CreateIcon(8,0,0,1,"Items","Open")
		
		ShowEntity MouseCursor
		MouseCursorVisible=True
	EndIf
	

End Function



Function CameraControls()
	; Camera Controls
	; TurnControl On and Off
	If KeyDown(56) And KeyDown(45) And AltX\KeyPressed=False ; Alt-X
		AltX\KeyPressed=True
		If CameraControl=True
			AltX\InUse=False
		Else
			AltX\InUse=True
		EndIf
	Else
		AltX\KeyPressed=False
	EndIf
	; Use NumPad to Control
	If AltX\KeyPressed
		If KeyDown(75) 
			If AltX\Reset = 0 StoreCameraSettings()
			TranslateEntity Camera,-0.1,0,0
		EndIf
		If KeyDown(77) 
			If AltX\Reset = 0 StoreCameraSettings()
			TranslateEntity Camera,0.1,0,0
		EndIf
		If KeyDown(72) 
			If AltX\Reset = 0 StoreCameraSettings()
			TranslateEntity Camera,0,0,0.1
		EndIf
		If KeyDown(80) 
			If AltX\Reset = 0 StoreCameraSettings()
			TranslateEntity Camera,0,0,-0.1
		EndIf
		If KeyDown(73)
			If AltX\Reset = 0 StoreCameraSettings()
			TranslateEntity Camera,0,0.1,0
		EndIf
		If KeyDown(81)
			If AltX\Reset = 0 StoreCameraSettings()
			TranslateEntity Camera,0,-0.1,0
		EndIf
		If KeyDown(71)
			If AltX\Reset = 0 StoreCameraSettings()
			TurnEntity Camera,1,0,0
		EndIf
		If KeyDown(79)
			If AltX\Reset = 0 StoreCameraSettings()
			TurnEntity Camera,-1,0,0
		EndIf
		If KeyDown(181)
			If AltX\Reset = 0 StoreCameraSettings()
			TurnEntity Camera,0,1,0
		EndIf
		If KeyDown(55)
			If AltX\Reset = 0 StoreCameraSettings()
			TurnEntity Camera,0,-1,0
		EndIf
		If KeyDown(76) And AltX\Reset=1
			RestoreCameraSettings()
		EndIf
		
	EndIf
	
End Function

Function StoreCameraSettings()
	AltX\Reset=1
	AltX\X=EntityX(Camera)
	AltX\Y=EntityY(Camera)
	AltX\Z=EntityZ(Camera)
	AltX\Roll=EntityRoll(Camera)
	AltX\Yaw=EntityYaw(Camera)
	AltX\Pitch=EntityPitch(Camera)
End Function
Function RestoreCameraSettings()
	AltX\Reset=0
	RotateEntity Camera,AltX\Pitch,AltX\Yaw,AltX\Roll
	PositionEntity Camera,AltX\X,AltX\Y,AltX\Z
End Function

Function CheckForNewCompiledLevels(silent)
	If silent=False
		Print "Checking for New Custom Adventures:"
		Print ""
	EndIf
	;Print globaldirname$+"\custom\downloads inbox"
	dirfile=ReadDir(globaldirname$+"\custom\downloads inbox")
	Repeat
		ex$=NextFile$(dirfile)
		If Upper$(Right$(ex$,4))=".WA3"
		
			ex$=Left$(ex$,Len(ex$)-4)


			; got one - unpack it 
			If silent=False Print "Found: "+ex$

						
			; First we unpack to the temp directory			
			
			; clear the temp directory
			dirfile2=ReadDir(Globaldirname$+"\temp")
			Repeat
				ex2$=NextFile$(dirfile2)
				If ex2$<>"" And FileType(Globaldirname$+"\temp\"+ex2$)=1
					DeleteFile Globaldirname$+"\temp\"+ex2$
				EndIf
			Until ex2$=""
			CloseDir dirfile2
												
			file=ReadFile(globaldirname$+"\custom\downloads inbox\"+ex$+".wa3")
			NofFiles=ReadInt(file)
			For i=0 To NofFiles-1
				FileName$=ReadString$(file)
				FileSize2=ReadInt(File)
				If Upper$(Right$(FileName$,3))="WLV" Or Upper$(Right$(FileName$,3))="DIA" Or Upper$(FileName$)="MASTER.DAT"
					file2=WriteFile(globaldirname$+"\temp\"+Filename$)
					For j=0 To FileSize2-1
						;a=ReadByte(file)
						WriteByte file2,ReadByte(file)
					Next
					CloseFile file2
				Else
					MyCreateDir(FileName$)
					file2=WriteFile(FileName$)
					For j=0 To FileSize2-1
						;a=ReadByte(file)
						WriteByte file2,ReadByte(file)
					Next
					CloseFile file2
				EndIf
			Next
			CloseFile file
			
			; now compare this adventure with possible previously unpacked adventure
			If CompareAdventures(ex$)=False
				; yes, it's an updated adventure
				
				; clean out the destination directory
				dirfile2=ReadDir(Globaldirname$+"\custom\adventures\"+ex$)
				Repeat
					ex2$=NextFile$(dirfile2)
					If ex2$<>"" And FileType(Globaldirname$+"\custom\adventures\"+ex$+"\"+ex2$)=1
						DeleteFile Globaldirname$+"\custom\adventures\"+ex$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile2

				; clean out dest directory
				; copy files over
				dirfile2=ReadDir(Globaldirname$+"\temp")
				Repeat
					ex2$=NextFile$(dirfile2)
					If ex2$<>"" And FileType(Globaldirname$+"\temp\"+ex2$)=1
						CopyFile Globaldirname$+"\temp\"+ex2$,Globaldirname$+"\custom\adventures\"+ex$+"\"+ex2$
					EndIf
				Until ex2$=""
						
				CloseDir dirfile2
				If silent=False Print "          Unpacking..."

			Else
				If silent=False Print "          Already Exists."

			EndIf
			

				
					
			; and clean up
			DeleteFile globaldirname$+"\custom\downloads inbox\"+ex$+".wa3"
			; clean out unpack directory
			dirfile2=ReadDir(Globaldirname$+"\temp")
			Repeat
				ex2$=NextFile$(dirfile2)
				If ex2$<>"" And FileType(Globaldirname$+"\temp\"+ex2$)=1
					DeleteFile Globaldirname$+"\temp\"+ex2$
				EndIf
			Until ex2$=""
					
			CloseDir dirfile2
						
			
		EndIf
		
	Until ex$=""
	
	CloseDir dirfile
	
	
			

End Function

Function CheckForNewCompiledHubs(silent)
	If silent=False
		Print "Checking for New Custom Hubs:"
		Print ""
	EndIf
	;Print globaldirname$+"\custom\downloads inbox"
	dirfile=ReadDir(globaldirname$+"\custom\downloads inbox")
	Repeat
		ex$=NextFile$(dirfile)
		If Upper$(Right$(ex$,4))=".WAH"
			ex$=Left$(ex$,Len(ex$)-4)


			; got one - unpack it 
			If silent=False Print "Found: "+ex$

						
			; First we unpack to the temp directory			
			
			; clear the temp directory
			dirfile2=ReadDir(Globaldirname$+"\temp")
			Repeat
				ex2$=NextFile$(dirfile2)
				If ex2$<>"" And FileType(Globaldirname$+"\temp\"+ex2$)=1
					DeleteFile Globaldirname$+"\temp\"+ex2$
				EndIf
			Until ex2$=""
			CloseDir dirfile2
			
			file=ReadFile(globaldirname$+"\custom\downloads inbox\"+ex$+".wah")
			HubTotalAdventures=ReadInt(file)
			For k=0 To HubTotalAdventures-1
				AdvNo=ReadInt(file) ;adventure number
				NofFiles=ReadInt(file)
				AdvName$="Adventure"+Str(AdvNo)
				If AdvNo=0
					AdvName$="Hub"
				EndIf
				CreateDir(globaldirname$+"\temp\"+AdvName$)
				For i=0 To NofFiles-1
					FileName$=ReadString$(file)
					FileSize2=ReadInt(File)
					file2=WriteFile(globaldirname$+"\temp\"+AdvName$+"\"+Filename$)
					For j=0 To FileSize2-1
						WriteByte file2,ReadByte(file)
					Next
					CloseFile file2
				Next
			Next
			
			NofCustomContent=ReadInt(file)
			If NofCustomContent>0
				For i=0 To NofCustomContent-1
					CustomContentName$=ReadString(file)
					MyCreateDir(CustomContentName$)
					file2=WriteFile(CustomContentName$)
					CustomContentSize=ReadInt(file)
					For j=0 To CustomContentSize-1
						WriteByte file2,ReadByte(file)
					Next
				Next
			EndIf
			
			logo1=ReadInt(File)
			If logo1>0
				;Print "hublogo.jpg found"
				file2=WriteFile(globaldirname$+"\temp\hublogo.jpg")
				For j=0 To logo1-1
					WriteByte file2,ReadByte(file)
				Next
				CloseFile file2
			EndIf
			logo2=ReadInt(File)
			If logo2>0
				;Print "wonderlandadventures.bmp found"
				file2=WriteFile(globaldirname$+"\temp\wonderlandadventures.bmp")
				For j=0 To logo2-1
					WriteByte file2,ReadByte(file)
				Next
				CloseFile file2
			EndIf
			CloseFile file
			
			; check if Hub is new
			If CompareHub(ex$)=False
				; yes, it's an updated hub, copy over!
				
				; first cleanup here
				CreateDir(Globaldirname$+"\custom\hubs\"+ex$)
				dirfile2=ReadDir(Globaldirname$+"\custom\hubs\"+ex$)
				Repeat
					ex2$=NextFile$(dirfile2)
					If ex2$<>"" And FileType(Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$)=1
						DeleteFile Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$
					ElseIf ex2$<>"" And ex2$<>".." And ex2$<>"." And FileType(Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$)=2
						dirfile3=ReadDir(Globaldirname$+"\temp\"+ex2$)
						Repeat
							ex3$=NextFile$(dirfile3)
							If ex3$<>"" And FileType(Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$+"\"+ex3$)=1
								DeleteFile Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$+"\"+ex3$
							EndIf
						Until ex3$=""
						DeleteDir Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$
					EndIf
				
				Until ex2$=""
				DeleteDir Globaldirname$+"\custom\hubs\"+ex$
			
				;now copy new files over
				CreateDir(Globaldirname$+"\custom\hubs\"+ex$)
				dirfile2=ReadDir(Globaldirname$+"\temp")
				Repeat
					ex2$=NextFile$(dirfile2)
					If ex2$<>"" And FileType(Globaldirname$+"\temp\"+ex2$)=1
						CopyFile Globaldirname$+"\temp\"+ex2$,Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$
					ElseIf ex2$<>"" And ex2$<>".." And ex2$<>"." And FileType(Globaldirname$+"\temp\"+ex2$)=2
						CreateDir(Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$)
						dirfile3=ReadDir(Globaldirname$+"\temp\"+ex2$)
						Repeat
							ex3$=NextFile$(dirfile3)
							If ex3$<>"" And FileType(Globaldirname$+"\temp\"+ex2$+"\"+ex3$)=1
								CopyFile Globaldirname$+"\temp\"+ex2$+"\"+ex3$,Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$+"\"+ex3$
							EndIf
						Until ex3$=""
					EndIf
				Until ex2$=""
					
				CloseDir dirfile2
				If silent=False Print "          Unpacking..."
			Else
				If silent=False Print "          Already Exists!"
			EndIf
			
			; now clean up
			DeleteFile globaldirname$+"\custom\downloads inbox\"+ex$+".wah"
			
			; clean out unpack directory
			dirfile2=ReadDir(Globaldirname$+"\temp")
			Repeat
				ex2$=NextFile$(dirfile2)
				If ex2$<>"" And FileType(Globaldirname$+"\temp\"+ex2$)=1
					DeleteFile Globaldirname$+"\temp\"+ex2$
				ElseIf ex2$<>"" And ex2$<>".." And ex2$<>"." And FileType(Globaldirname$+"\temp\"+ex2$)=2
					dirfile3=ReadDir(Globaldirname$+"\temp\"+ex2$)
					Repeat
						ex3$=NextFile$(dirfile3)
						If ex3$<>"" And FileType(Globaldirname$+"\temp\"+ex2$+"\"+ex3$)=1
							;Print Globaldirname$+"\temp\"+ex2$+"\"+ex3$
							DeleteFile Globaldirname$+"\temp\"+ex2$+"\"+ex3$
						EndIf
					Until ex3$=""
					DeleteDir Globaldirname$+"\temp\"+ex2$
				EndIf
				
			Until ex2$=""
					
			CloseDir dirfile2
			
		EndIf
	Until ex$=""
	
End Function



Function CompareAdventures(ex$)

	; does the directory even exist?
	If FileType(Globaldirname$+"\custom\adventures\"+ex$)<>2 
		CreateDir Globaldirname$+"\custom\adventures\"+ex$
		
		Return False
	EndIf
	
	; check each file from the unpack directory (should only be master, wlv and dia)

	dirfile=ReadDir(Globaldirname$+"\temp\")
	Repeat
		ex2$=NextFile$(dirfile)
		If ex2$<>"" And FileType(Globaldirname$+"\temp\"+ex2$)=1
			; check this file ex$\ex2$
			If FileType(Globaldirname$+"\custom\adventures\"+ex$+"\"+ex2$)<>1
				; file doesn't exist in destination
				
				Return False
			EndIf
			
			sourcefile=ReadFile (Globaldirname$+"\temp\"+ex2$)
			destfile=ReadFile(Globaldirname$+"\custom\adventures\"+ex$+"\"+ex2$)
			
			flag=True ; set to false if not equal
			Repeat
				a=ReadByte (sourcefile)
				b=ReadByte (destfile)
				If a<>b Or (Eof(destfile)=True And Eof(sourcefile)=False)
					flag=False
				EndIf
			Until flag=False Or Eof(sourcefile)=True
			CloseFile sourcefile
			CloseFile destfile
			
			If flag=False 
				
				Return False
			EndIf
			
			; files are equal - continue
		EndIf
	
	Until ex2$=""
			
	CloseDir dirfile2
	
	; all files checked out - equal
	Return True

End Function

Function CompareHub(ex$)
	; does the directory even exist?
	If FileType(Globaldirname$+"\custom\hubs\"+ex$)<>2 
		CreateDir Globaldirname$+"\custom\hubs\"+ex$

		Return False
	EndIf

	dir = ReadDir(Globaldirname$+"\temp\")
	Repeat
		ex2$=NextFile(dir)
		
		If FileType(Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$)=0
			; file/directory doesn't exist in destination
		
			Return False
		EndIf
		
		If FileType(Globaldirname$+"\temp\"+ex2$)=2 And ex2$<>".." And ex2$<>"."
			dir2=ReadDir(Globaldirname$+"\temp\"+ex2$)
			Repeat
				; check each file from the unpack directory (should only be master, wlv and dia)
				ex3$=NextFile(dir2)
				If ex3$<>"" And FileType(Globaldirname$+"\temp\"+ex2$+"\"+ex3$)=1
					; check this file ex$\ex2$\ex3$
					If FileType(Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$+"\"+ex3$)<>1
						; file doesn't exist in destination

						Return False
					EndIf
			
					sourcefile=ReadFile (Globaldirname$+"\temp\"+ex2$+"\"+ex3$)
					destfile=ReadFile(Globaldirname$+"\custom\hubs\"+ex$+"\"+ex2$+"\"+ex3$)
			
					flag=True ; set to false if not equal
					
					Repeat
						a=ReadByte (sourcefile)
						b=ReadByte (destfile)
						If a<>b Or (Eof(destfile)=True And Eof(sourcefile)=False)
							flag=False
						EndIf
					Until flag=False Or Eof(sourcefile)=True
					CloseFile sourcefile
					CloseFile destfile
			
					If flag=False 
				
						Return False
					EndIf
				EndIf
			Until ex3$=""
		EndIf
	Until ex2$=""

	CloseDir dirfile2
	
	; all files checked out - equal
	Return True

End Function

Function MyCreateDir(dirpath$)
	For i=1 To Len(dirpath$)
		If Mid$(dirpath$,i,1)="\" Then
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


Function decode$(ex$)
	output$=""
	For i=1 To Len(ex$)
		output$=output$+Chr$(Asc(Mid$(ex$,i,1))-1)
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



Function lockercrypt()
	file=ReadFile("local")
	
	If file=0 
		Return False
		
	EndIf
	
	For i=0 To 3
		length=ReadInt(file)
		Print length
		lockertext$(i)=""
		For j=1 To length
			b=ReadInt(file)
			a=(10000-b)/11
			lockertext$(i)=lockertext$(i)+Chr$(a)
		Next
		
	Next
	
	If lockertext$(1)<>GetEnv$("SYSTEMROOT") Return False
	
	If lockertext$(2)<>GetEnv$("COMPUTERNAME") Return False

	If lockertext$(3)<>GetEnv$("USERNAME") Return False

	Return True

End Function
	
Function ExitTestMode()
	ExecFile ("editor3d.exe")
	End
End Function



; Level
		
.expired
Data "THIS VERSION HAS EXPIRED.","PLEASE CONTACT WWW.MIDNIGHTSYNERGY.COM","FOR MORE INFORMATION."

.nolockercrypt
Data "CANNOT RUN BETA ON THIS COMPUTER.","PLEASE CONTACT WWW.MIDNIGHTSYNERGY.COM","FOR MORE INFORMATION."


Include "particles.bb"
Include "level.bb"
Include "adventures.bb"
Include "menu.bb"
Include "sound.bb"
Include "preload.bb"
Include "wa3endinclud.bb"