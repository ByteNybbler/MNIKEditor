Function SearchForCustomContent(ex$)
	Local mydir = ReadDir(ex$)
	Repeat
		dirfile$ = NextFile(mydir)
		filename$=ex$+Chr$(47)+dirfile$
		If Upper$(Right$(dirfile$,3))="WLV"
			; it is a level, search in level!
			SearchForCustomContentInLevel(filename$)
		EndIf
		If Upper$(dirfile$)="MASTER.DAT"
			file=ReadFile (filename$)
			ReadString$(file)
			For i=0 To 4
				ReadString$(file)
			Next
			ReadString$(file) ;user (not loaded)
			; we get custom icon
			CustomIconName$=ReadString$(file)
			If CustomIconName$<>"Standard" And FileType(GlobalDirName$+"/Custom/Icons/icons "+CustomIconName$+".bmp")=1
				AddCustomContent(GlobalDirName$+"/Custom/Icons/icons "+CustomIconName$+".bmp")
			EndIf
			CloseFile file
		EndIf
	Until dirfile$=""
	;For i=0 To NofCustomContent-1
	;	Print "Custom Content is: " + CustomContentFile$(i)
	;Next
End Function

Function SearchForCustomContentInLevel(filename$)
	file=ReadFile (filename$)
	LevelWidth=ReadInt(File)
	If LevelWidth>121
		; WA3 VAULTS
		LevelWidth=LevelWidth-121
	EndIf
	LevelHeight=ReadInt(File)
	; skip tiles and other unused things
	SeekFile file,(FilePos(file)+(LevelWidth*LevelHeight)*(4*14)) + (4*3)
	; now get custom texture
	CurrentLevelTexture=-1
	CurrentWaterTexture=-1
	a$=ReadString$(file)
	For i=0 To NofLevelTextures-1
		If a$=LevelTextureName$(i) Then CurrentLevelTexture=i
	Next
	If CurrentLevelTexture=-1
		LevelTextureCustomName$=a$
	EndIf
	a$=ReadString$(file)
	For i=0 To NofWaterTextures-1
		If a$=WaterTextureName$(i) Then CurrentWaterTexture=i
	Next
	If CurrentWaterTexture=-1
		WaterTextureCustomName$=a$
	EndIf
	; found custom LevelTexture
	If CurrentLevelTexture=-1
		LevelTextureCustomExt$=Lower(Right(LevelTextureCustomName$,4))
		If LevelTextureCustomExt$=".dds" Or LevelTextureCustomExt$=".png" Or LevelTextureCustomExt$=".tga" Or LevelTextureCustomExt$=".bmp" Or LevelTextureCustomExt$=".jpg"
			LevelTextureCustomFilename$=Left(LevelTextureCustomName$,Len(LevelTextureCustomName$)-4)
		Else
			LevelTextureCustomFilename$=LevelTextureCustomName$
			LevelTextureCustomExt$=".bmp"
		EndIf
		If FileType(GlobalDirName$+"/Custom/LevelTextures/leveltex "+LevelTextureCustomFilename$+LevelTextureCustomExt$)=1 And FileType(GlobalDirName$+"/Custom/LevelTextures/backgroundtex "+LevelTextureCustomFilename$+"1"+LevelTextureCustomExt$) And FileType(GlobalDirName$+"/Custom/LevelTextures/backgroundtex "+LevelTextureCustomFilename$+"2"+LevelTextureCustomExt$)
			; add level texture
			AddCustomContent(GlobalDirName$+"/Custom/LevelTextures/leveltex "+LevelTextureCustomFilename$+LevelTextureCustomExt$)
			AddCustomContent(GlobalDirName$+"/Custom/LevelTextures/backgroundtex "+LevelTextureCustomFilename$+"1"+LevelTextureCustomExt$)
			AddCustomContent(GlobalDirName$+"/Custom/LevelTextures/backgroundtex "+LevelTextureCustomFilename$+"2"+LevelTextureCustomExt$)
		EndIf
	EndIf
	If CurrentWaterTexture=-1 And FileType(GlobalDirName$+"/Custom/LevelTextures/watertex "+WaterTextureCustomName$+".jpg")
		WaterTextureCustomExt$=Lower(Right(WaterTextureCustomName$,4))
		If WaterTextureCustomExt$=".dds" Or WaterTextureCustomExt$=".png" Or WaterTextureCustomExt$=".tga" Or WaterTextureCustomExt$=".bmp" Or WaterTextureCustomExt$=".jpg"
			WaterTextureCustomFilename$=Left(WaterTextureCustomName$,Len(WaterTextureCustomName$)-4)
		Else
			WaterTextureCustomFilename$=WaterTextureCustomName$
			WaterTextureCustomExt$=".jpg"
		EndIf
		If FileType(GlobalDirName$+"/Custom/LevelTextures/watertex "+WaterTextureCustomFilename$+WaterTextureCustomExt$)=1
			AddCustomContent(GlobalDirName$+"/Custom/LevelTextures/watertex "+WaterTextureCustomFilename$+WaterTextureCustomExt$)
		EndIf
	EndIf
	;OBJECTS
	n=ReadInt(file)
	For i=0 To n-1
		; get modelname and texture name
		ModelName$=Replace$(ReadString$(file),Chr$(92),Chr$(47))
		TexName$=Replace$(ReadString$(file),Chr$(92),Chr$(47))
		; skip unused adjusters
		SeekFile file,FilePos(file)+((57+10)*4)
		; get TextData0
		TextData0$=Replace$(ReadString$(file),Chr$(92),Chr$(47))
		For k=1 To 3
			ReadString$(file)
		Next
		; skip unused adjusters
		SeekFile file,FilePos(file)+(35*4)
		ReadString$(file)
		ReadString$(file)
		For k=0 To 30
			ReadString(file)
		Next
		; ok now add content accordingly
		If Left$(TexName$,1)<>"!" And Left$(TexName$,1)<>"?"
			; add unoffical custom texture used
			If FileType(TexName$)=1
				AddCustomContent(TexName$)
			EndIf
		Else If Left$(TexName$,1)="?"
			; add official custom texture used
			textension$=Lower(Right(TexName$,4))
			If textension$=".jpg" Or textension$=".bmp" Or textension$=".png" Or textension$=".tga" Or textension$=".dds"
				tname$="UserData/Custom/ObjectTextures/"+Right(TexName$,Len(TexName$)-1)
			Else
				tname$="UserData/Custom/ObjectTextures/"+Right(TexName$,Len(TexName$)-1)+".jpg"
			EndIf
			If FileType(tname$)=1
				AddCustomContent(tname$)
			EndIf
		EndIf
		If ModelName$="!CustomModel"
			; vanilla custom models
			If FileType("UserData/Custom/Models/"+TextData0$+".x")=1
				mname$="UserData/Custom/Models/"+TextData0$+".x"
			Else If FileType("UserData/Custom/Models/"+TextData0$+".b3d")=1
				mname$="UserData/Custom/Models/"+TextData0$+".b3d"
			Else If FileType("UserData/Custom/Models/"+TextData0$+".3ds")=1
				mname$="UserData/Custom/Models/"+TextData0$+".3ds"
			EndIf					
			If FileType("UserData/Custom/Models/"+TextData0$+".dds")=1
				textension$=".dds"
			Else If FileType("UserData/Custom/Models/"+TextData0$+".png")=1
				textension$=".png"
			Else If FileType("UserData/Custom/Models/"+TextData0$+".tga")=1
				textension$=".tga"
			Else If FileType("UserData/Custom/Models/"+TextData0$+".bmp")=1
				textension$=".bmp"
			Else If FileType("UserData/Custom/Models/"+TextData0$+".jpg")=1
				textension$=".jpg"
			EndIf					
			If textension$<>""
				AddCustomContent(mname$)
				AddCustomContent("UserData/Custom/Models/"+TextData0$+textension$)
				If FileType("UserData/Custom/Models/"+TextData0$+".mask"+textension$)=1 And (textension$=".png" Or textension$=".tga" Or textension$=".dds")
					AddCustomContent("UserData/Custom/Models/"+TextData0$+".mask"+textension$)
				EndIf
			EndIf
		Else If Left$(ModelName$,1)="?"
			; newer 10.2.0 custom models
			; first fetch actual file name
			mname$="UserData/Custom/Models/"+Right(ModelName$,Len(ModelName$)-1)
			; now check if mname$ has is having any extension in its end or not
			If Not (Lower(Right(mname$,2))=".x" Or Lower(Right(mname$,4))=".b3d" Or Lower(Right(mname$,4))=".3ds")
				If FileType(mname$+".x")=1
					mname$=mname$+".x"
				Else If FileType(mname$+".b3d")=1
					mname$=mname$+".b3d"
				Else If FileType(mname$+".3ds")=1
					mname$=mname$+".3ds"
				EndIf
			EndIf
			If FileType(mname$)=1
				; now add file
				AddCustomContent(mname$)
			EndIf
			; now get texture
			tname$="UserData/Custom/Models/"+Right(ModelName$,Len(ModelName$)-1)
			; remove extension if there
			If Lower(Right(tname$,2))=".x"
				tname$=Left$(tname$,Len(tname$)-2)
			Else If Lower(Right(tname$,4))=".b3d" Or Lower(Right(tname$,4))=".3ds"
				tname$=Left$(tname$,Len(tname$)-4)
			EndIf
			; ok now get texture
			If FileType(tname$+".dds")=1
				textension$=".dds"
			Else If FileType(tname$+".png")=1
				textension$=".png"
			Else If FileType(tname$+".tga")=1
				textension$=".tga"
			Else If FileType(tname$+".bmp")=1
				textension$=".bmp"
			Else If FileType(tname$+".jpg")=1
				textension$=".jpg"
			EndIf
			If FileType(tname$+textension$)=1
				; now add file
				AddCustomContent(tname$+textension$)
			EndIf
		EndIf
	Next
	CloseFile file
End Function

Function AddCustomContent(filename$)
	DebugLog "AddCustomContent: "+filename$
	If Lower$(filename$)="userdata/custom/models/default.3ds" Or Lower$(filename$)="userdata/custom/models/default.jpg"
		Return ;we don't need to pack default custom textures
	EndIf
	For i=0 To NofCustomContentFiles-1
		If Lower$(CustomContentFile$(i))=Lower$(filename$)
			; file already exists, we do need need to add it again now
			; so we return
			Return
		EndIf
	Next
	; not there, should be new file
	; now check if file exists or not
	If FileType(filename$)=1
		; exists, add!
		CustomContentFile$(NofCustomContentFiles)=filename$
		NofCustomContentFiles=NofCustomContentFiles+1
	Else
		; uh-oh, file doesn't exist
		ConsoleWarn("File doesn't exist: " + filename$)
	EndIf
End Function


Function ConsoleError(tex$)
	Color 255,0,0
	Print "ERROR: " + tex$
	Print "Aborting..."
	Delay 3000
	Color 255,255,255
	Locate 0,0
End Function

Function ConsoleWarn(tex$)
	Color 255,255,0
	Print "WARNING: " + tex$
	Delay 1000
	Color 255,255,255
End Function