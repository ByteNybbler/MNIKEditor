SeedRnd MilliSecs()
AppTitle "Wonderland Adventures Editor"

Global GfxWidth=800;800;1280;640
Global GfxHeight=600;600;768;480
Global GfxDepth=32
Global GfxWindowed=1

Global widescreen=True

Global GlobalDirName$ = "UserData" 

;If FileType("localsaveon.txt")=1 Or player
;
;	GlobalDirName$="UserData"
;	CreateDir(GlobalDirName$)
;
;	
;Else
;	
;	
;	GlobalDirName$ = SpecialFolderLocation($1C)+"\Midnight Synergy"
;	
;	; is the folder valid 
;	If FileType(GlobalDirName$) <> 2 Then
;		CreateDir(GlobalDirName$)
;		GiveDirectoryUserFullAccess(GlobalDirName$)
;	EndIf
;	
;	GlobalDirName$ = SpecialFolderLocation($1C)+"\Midnight Synergy\WA POTZ"
;	
;	; is the folder valid 
;	If FileType(GlobalDirName$) <> 2 Then
;		CreateDir(GlobalDirName$)
;		GiveDirectoryUserFullAccess(GlobalDirName$)
;	EndIf
;EndIf


CreateDir GlobalDirName$+"\Temp"
;GiveDirectoryUserFullAccess(GlobalDirName$+"\Temp")

Global NofMyGfxModes, MyGfxMode
Dim MyGfxModeWidth(1000),MyGfxModeHeight(1000),MyGfxModeDepth(1000), GfxTime(1000), IsWideScreen(1000)

soundfx1=LoadSound("data\sound\nfovdmjdl.wdf")
a=Rand(1,4)
b=Rand(1,5)
soundfx2=LoadSound("data\sound\voices\"+Str(a)+"\tubsu"+Str(b)+".wdf")

; check if global ini file exists

file=ReadFile(globaldirname$+"\display.wdf")

If file>0
	; load existing options from Global.ini file
	NofMyGfxModes=ReadInt(file)
	For i=0 To NofMyGfxModes-1
		MyGfxModeWidth(i)=ReadInt(file)
		MyGfxModeHeight(i)=ReadInt(file)
		MyGfxModeDepth(i)=ReadInt(file)
	Next
	GfxMode=ReadInt(file)
	GfxWindowed=ReadInt(file)
	
	; Check that this one does indeed exist
	If GfxMode<0 Or GfxMode>=1000
		file=0
	Else If GfxMode3DExists (MyGfxModeWidth(GfxMode),MyGfxModeHeight(GfxMode),MyGfxModeDepth(GfxMode))=False
		; no - something is wrong, re-test
		file=0
	EndIf
	
EndIf


If file=0
	; no global ini file, so we should do a system check on first startup
	
		
	For i=1 To CountGfxModes3D()
		ratio#=Float(GfxModeWidth(i))/Float(GfxModeHeight(i))
		If ratio#>1.33 And ratio#<1.34 And GfxModeWidth(i)>=640
			; list all 4:3 modes above 640x480
			MyGfxModeWidth(j)=GfxModeWidth(i)
			MyGfxModeHeight(j)=GfxModeHeight(i)
			MyGfxModeDepth(j)=GfxModeDepth(i)
			IsWideScreen(j)=False
			j=j+1
		ElseIf ratio#>1.77 And ratio#<1.78 And GfxModeWidth(i)>=640 And widescreen=True
			MyGfxModeWidth(j)=GfxModeWidth(i)
			MyGfxModeHeight(j)=GfxModeHeight(i)
			MyGfxModeDepth(j)=GfxModeDepth(i)
			IsWideScreen(j)=True
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
	
	
	
		
	
EndIf

If GfxMode=-1
	Print "Note:
	Print "-----"
	Print "Your Computer's 3D Graphics Drivers appear"
	Print "to not be functioning properly."
	Print ""
	Print "If this is your first time running"
	Print "Wonderland Adventures, please ensure"
	Print "that you update your video card drivers"
	Print "to the latest version."
	Print ""
	Print "If you have run the game before without"
	Print "difficulties, and are now receiving this"
	Print "error, you may wish to also run a virus"
	Print "and spyware check to ensure that no other"
	Print "program is interfering with your computer."
	Print "You may also wish to delete 'display.wdf'"
	Print "in the Wonderland Adventure's directory"
	Print "in order to attempt a restore."
	Print ""
	Print "Aborting... Press any Key."
	

	WaitKey()
	End
EndIf	
	

v1=0
v2=9
v3=9
file=ReadFile("version.wdf")
If file>0
	v1=ReadInt(file)
	v2=ReadInt(file)
	v3=ReadInt(file)
	CloseFile(file)
EndIf


; now start the actual loader

Graphics 640,480,0,2

img=LoadImage("data\graphics\logos\mpbefs.wdf")


DrawImage img,0,0

ex$=Str$(MyGfxModeWidth(GfxMode))+"x"+Str$(MyGfxModeHeight(GfxMode))+"x"+Str$(MyGfxModeDepth(GfxMode))

Color 0,0,0
Rect 210,265,220,75,True
Color 255,255,255
Text 320,270,"DISPLAY SETTINGS",True
Text 320,275,"________________",True
Text 320,300,ex$,True
If GfxWindowed=1 
	Text 320,320,"Fullscreen",True 
Else
	Text 320,320,"Windowed",True
EndIf

Color 150,150,150
ex$="v"+Str$(v1)+"."+Str$(v2)+Str$(v3)
;Text 588,466,"Beta2"


ShowPointer 

endgame=False
Repeat

	mx=MouseX()
	my=MouseY()
	mb1=0 
	mb2=0
	If MouseDown(1) mb1=1
	If MouseDown(2) mb2=1
	
	If mx>230 And my>390 And mx<410 And my<470
		Color 255,255,0
	Else
		Color 0,0,0
	EndIf
	Text 320,438,"____________",True
	
	If mb1=1 Or mb2=1
		If mx>210 And mx<430 And my>298 And my<318
			If mb1=1
				GfxMode=GfxMode+1
				If GfxMode=NofMyGfxModes Then GfxMode=0
				PlaySound(soundfx1)
			Else
				GfxMode=GfxMode-1
				If GfxMode=-1 Then GfxMode=NofMyGfxModes-1
				PlaySound(soundfx1)
			EndIf
		EndIf
		If mx>210 And mx<430 And my>318 And my<338
			GfxWindowed=3-GfxWindowed
			PlaySound(soundfx1)
		EndIf
		Color 0,0,0
		Rect 210,265,220,75,True
		Color 255,255,255
		ex$=Str$(MyGfxModeWidth(GfxMode))+"x"+Str$(MyGfxModeHeight(GfxMode))+"x"+Str$(MyGfxModeDepth(GfxMode))

		Text 320,270,"DISPLAY SETTINGS",True
		Text 320,275,"________________",True
		Text 320,300,ex$,True
		If GfxWindowed=1 
			Text 320,320,"Fullscreen",True 
		Else
			Text 320,320,"Windowed",True
		EndIf
		
		If mx>230 And my>390 And mx<410 And my<470
			endgame=True
			PlaySound(soundfx1)
			chn=PlaySound(soundfx2)
		EndIf
		Delay 200
	EndIf

	Flip				

Until endgame=True Or KeyDown(1)

EndGraphics 

file=WriteFile(globaldirname$+"\display.wdf")
		
WriteInt file,NofMyGfxModes

For i=0 To NofMyGfxModes-1
	WriteInt file,MyGfxModeWidth(i)
	WriteInt file,MyGfxModeHeight(i)
	WriteInt file,MyGfxModeDepth(i)
	
	
Next

WriteInt file,GfxMode
WriteInt file,GfxWindowed

CloseFile file
If endgame=True Then Delay 810

While ChannelPlaying(chn)
Wend

ExecFile ("wg.exe")

End


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
		