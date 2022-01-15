









; BUTTONS
Function SetupMenu()
	
	
	DialogBackGroundEntity=CreateMesh(camera)
	surface=CreateSurface(DialogBackGroundEntity)
	AddVertex surface,-2,1.3,5,0,0
	AddVertex surface,2,1.3,5,1,0
	AddVertex surface,-2,-0.5,5,0,1
	AddVertex surface,2,-0.5,5,1,1
	AddTriangle surface,0,1,2
	AddTriangle surface,2,1,3
	For i=0 To 3
		VertexColor surface,i,0,0,0
	Next
	
	EntityColor DialogBackGroundEntity,200,50,30
	EntityAlpha DialogBackGroundEntity,.5
	EntityOrder DialogBackGroundEntity,-9
	
	HideEntity DialogBackGroundEntity
	
	DialogBackGroundEntity2=CreateMesh(camera)
	surface=CreateSurface(DialogBackGroundEntity2)
	;widescreen
	If widescreen
		AddVertex surface,-4,-1.9,5,0,0
		AddVertex surface,4,-1.9,5,1,0
		AddVertex surface,-4,-2.3,5,0,1
		AddVertex surface,4,-2.3,5,1,1
	Else
		AddVertex surface,-3,-1.9,5,0,0
		AddVertex surface,3,-1.9,5,1,0
		AddVertex surface,-3,-2.3,5,0,1
		AddVertex surface,3,-2.3,5,1,1
	EndIf
	AddTriangle surface,0,1,2
	AddTriangle surface,2,1,3
	For i=0 To 3
		VertexColor surface,i,0,0,0
	Next

	
	EntityColor DialogBackGroundEntity2,120,100,80
	EntityAlpha DialogBackGroundEntity2,.5
	EntityOrder DialogBackGroundEntity2,-9
	
	SetDialogContrast()
	
	;HideEntity DialogBackGroundEntity2
	
	; Standard Title Entities
	
	If WAEpisode=0
		; editor 
		
		titlemenuentity(1)=CreateSphere(64,camera)
		titlemenutexture(1)=myLoadTexture("data\graphics\logos\moon.jpg",1)
		EntityTexture titlemenuentity(1),titlemenutexture(1)
		ScaleEntity titlemenuentity(1),1,1,1
		If widescreen
			PositionEntity titlemenuentity(1),8.2,4.2,15
		Else
			PositionEntity titlemenuentity(1),6.2,4.2,15
		EndIf
		EntityAlpha titlemenuentity(1),0
		
		titlemenuentity(2)=CreateSquare()
		titlemenutexture(2)=myLoadTexture("data\graphics\logos\synergy.jpg",4)
		EntityTexture titlemenuentity(2),titlemenutexture(2)
		RotateEntity titlemenuentity(2),-90,0,0
		ScaleEntity titlemenuentity(2),3,3,3
		If widescreen
			PositionEntity titlemenuentity(2),-7.9,4.8,15
		Else
			PositionEntity titlemenuentity(2),-5.9,4.8,15
		EndIf
		EntityAlpha Titlemenuentity(2),0
		EntityFX titlemenuentity(2),1
		
		; logo
		titlemenuentity(60)=CreateSquare()
		titlemenutexture(60)=myLoadTexture("data\graphics\logos\wonderlandadventures.bmp",4)
		EntityTexture titlemenuentity(60),titlemenutexture(60)
		RotateEntity titlemenuentity(60),-90,0,0
		PositionEntity titlemenuentity(60),0,.15,1
		ScaleEntity titlemenuentity(60),1,1,1
		EntityFX titlemenuentity(60),1
		HideEntity TItlemenuentity(60)
		EntityAlpha Titlemenuentity(60),0
		
		; hublogo
		titlemenuentity(61)=CreateSquare()
		titlemenutexture(61)=myLoadTexture("data\graphics\logos\pj4.jpg",1)
		EntityTexture titlemenuentity(61),titlemenutexture(61)
		RotateEntity titlemenuentity(61),-90,0,0
		If widescreen
			PositionEntity titlemenuentity(61),.592,.053,2.1
		Else
			PositionEntity titlemenuentity(61),.392,.053,2.1
		EndIf
		ScaleEntity titlemenuentity(61),1,1,1
		EntityFX titlemenuentity(61),1
		HideEntity TItlemenuentity(61)
	

		
		; volcano
		titlemenuentity(78)=CreateCube()
		PositionEntity titlemenuentity(78),-1000,0,0
		ScaleEntity titlemenuentity(78),.07,.07,.07
		EntityColor titlemenuentity(78),10,0,30
		

		
	
			
		; water plane
		titlemenuentity(79)=CreateCube()
	;	RotateEntity titlemenuentity(79),-90,0,0
	;	TurnEntity titlemenuentity(79),0,180,0
		PositionEntity titlemenuentity(79),0,0,151
		;widescreen
		If widescreen
			ScaleEntity titlemenuentity(79),115.5,115.5,.1
		Else
			ScaleEntity titlemenuentity(79),100,100,.1
		EndIf
		titlemenutexture(79)=myLoadTexture("data\graphics\logos\starpaper.jpg",1)
	;	PositionTexture titlemenutexture(79),.5,.5
	;	ScaleTexture titlemenutexture(79),100,50
		EntityTexture titlemenuentity(79),titlemenutexture(79)
;		EntityFX titlemenuentity(79),1
		;EntityAlpha Titlemenuentity(79),0.5

			
		; background plane
		titlemenuentity(80)=CreateCube()
		PositionEntity titlemenuentity(80),0,-30,130
		;widescreen
		If widescreen
			ScaleEntity titlemenuentity(80),88,52,.1
		Else
			ScaleEntity titlemenuentity(80),65,40,.1
		EndIf
		;RotateEntity titlemenuentity(80),-90,0,0
		;TurnEntity titlemenuentity(80),0,180,0
		titlemenutexture(80)=myLoadTexture("data\graphics\logos\end1.jpg",4)
		
		;PositionTexture titlemenutexture(80),.5,.5
		;ScaleTexture titlemenutexture(80),100,70
		EntityTexture titlemenuentity(80),titlemenutexture(80)
	;	EntityFX titlemenuentity(80),1
	;	EntityAlpha Titlemenuentity(80),0
		
		
		; falling stars
		For i=81 To 99
			titlemenuentity(i)=CopyEntity(starmesh)
			RotateEntity Titlemenuentity(i),-90,0,0
			sc#=Rnd(0.8,1)
			ScaleEntity titlemenuentity(i),sc,sc,sc
			EntityFX titlemenuentity(i),1
			PositionEntity titlemenuentity(i),Rnd(-60,90),Rnd(0,50),145
			EntityAlpha titlemenuentity(i),0			

		Next
	EndIf

	
	If WAEpisode=2
		; editor 
		
		titlemenuentity(1)=CreateSphere(64,camera)
		titlemenutexture(1)=myLoadTexture("data\graphics\logos\moon.jpg",1)
		EntityTexture titlemenuentity(1),titlemenutexture(1)
		ScaleEntity titlemenuentity(1),0.5,0.5,0.5
		PositionEntity titlemenuentity(1),-.9,1.1,5
		EntityAlpha titlemenuentity(1),0
		
		titlemenuentity(2)=CreateSquare()
		titlemenutexture(2)=myLoadTexture("data\graphics\logos\synergy.jpg",4)
		EntityTexture titlemenuentity(2),titlemenutexture(2)
		RotateEntity titlemenuentity(2),-90,0,0
		PositionEntity titlemenuentity(2),0,.3,1.5
		EntityAlpha Titlemenuentity(2),0
		EntityFX titlemenuentity(2),1
		
		; logo
		titlemenuentity(60)=CreateSquare()
		titlemenutexture(60)=myLoadTexture("data\graphics\logos\wonderlandadventures.bmp",4)
		EntityTexture titlemenuentity(60),titlemenutexture(60)
		RotateEntity titlemenuentity(60),-90,0,0
		PositionEntity titlemenuentity(60),0,.35,2
		ScaleEntity titlemenuentity(60),.8,.8,.8
		EntityFX titlemenuentity(60),1
		HideEntity TItlemenuentity(60)
		EntityAlpha Titlemenuentity(60),0
		
		; volcano
		titlemenuentity(78)=CopyEntity(obstaclemesh(3))
		PositionEntity titlemenuentity(78),-55,-10,140;-5,-10,150
		ScaleEntity titlemenuentity(78),.07,.07,.07
		EntityColor titlemenuentity(78),10,0,30
		EntityFX Titlemenuentity(78),1
		EntityAlpha Titlemenuentity(78),0
			
		; water plane
		titlemenuentity(79)=CreatePlane()
		PositionEntity titlemenuentity(79),30,-10,0
		titlemenutexture(79)=myLoadTexture("data\graphics\logos\waternight.jpg",1)
		ScaleTexture titlemenutexture(79),95,135
		EntityTexture titlemenuentity(79),titlemenutexture(79)
;		EntityFX titlemenuentity(79),1
		EntityAlpha Titlemenuentity(79),0.5

			
		; background plane
		titlemenuentity(80)=CreatePlane()
		PositionEntity titlemenuentity(80),0,-10,150
		RotateEntity titlemenuentity(80),-90,0,0
		TurnEntity titlemenuentity(80),0,180,0
		titlemenutexture(80)=myLoadTexture("data\graphics\logos\starpaper2.jpg",1)
		ScaleTexture titlemenutexture(80),70,70
		EntityTexture titlemenuentity(80),titlemenutexture(80)
	;	EntityFX titlemenuentity(80),1
		EntityAlpha Titlemenuentity(80),0
		
		
		; falling stars
		For i=81 To 99
			titlemenuentity(i)=CopyEntity(starmesh)
			RotateEntity Titlemenuentity(i),-90,0,0
			sc#=Rnd(0.8,1)
			ScaleEntity titlemenuentity(i),sc,sc,sc
			EntityFX titlemenuentity(i),1
			PositionEntity titlemenuentity(i),Rnd(-60,90),Rnd(70,130),145
			EntityAlpha titlemenuentity(i),0			

		Next
	EndIf
	
	
	If WAEpisode=3
		; editor 
		
		titlemenuentity(1)=CreateSphere(64,camera)
		titlemenutexture(1)=myLoadTexture("data\graphics\logos\moon.jpg",1)
		EntityTexture titlemenuentity(1),titlemenutexture(1)
		ScaleEntity titlemenuentity(1),0.5,0.5,0.5
		PositionEntity titlemenuentity(1),-.9,1.1,-5
		EntityAlpha titlemenuentity(1),0
		
		titlemenuentity(2)=CreateSquare()
		titlemenutexture(2)=myLoadTexture("data\graphics\logos\synergy.jpg",4)
		EntityTexture titlemenuentity(2),titlemenutexture(2)
		RotateEntity titlemenuentity(2),-90,0,0
		PositionEntity titlemenuentity(2),0,.3,1.5
		EntityAlpha Titlemenuentity(2),0
		EntityFX titlemenuentity(2),1
		
		; logo
		titlemenuentity(60)=CreateSquare()
		titlemenutexture(60)=myLoadTexture("data\graphics\logos\wonderlandadventures.bmp",4)
		EntityTexture titlemenuentity(60),titlemenutexture(60)
		RotateEntity titlemenuentity(60),-90,0,0
		PositionEntity titlemenuentity(60),0,.35,2
		ScaleEntity titlemenuentity(60),.8,.8,.8
		EntityFX titlemenuentity(60),1
		HideEntity TItlemenuentity(60)
		EntityAlpha Titlemenuentity(60),0
		
		
			
		; water plane
		titlemenuentity(79)=CreatePlane()
		PositionEntity titlemenuentity(79),30,-10,0
		titlemenutexture(79)=myLoadTexture("data\graphics\logos\ma.jpg",1)
		ScaleTexture titlemenutexture(79),50,100;,95,135
		EntityTexture titlemenuentity(79),titlemenutexture(79)
;		EntityFX titlemenuentity(79),1
		EntityAlpha Titlemenuentity(79),0
		
		; planet z-bot
		titlemenuentity(78)=CreateCube()
		ScaleEntity titlemenuentity(78),.1,.1,.1
		PositionEntity titlemenuentity(78),-60,35,260
	;	titlemenutexture(78)=myLoadTexture("data\graphics\logos\p.jpg",4)

	;	PositionEntity titlemenuentity(78),-60,35,160;-5,-10,150
	;	ScaleEntity titlemenuentity(78),8,8,8
	;	EntityTexture titlemenuentity(78),titlemenutexture(78)
	;	EntityFX titlemenuentity(78),1
		
		EntityAlpha Titlemenuentity(78),0

			
		; background plane
		titlemenuentity(80)=CreatePlane()
		PositionEntity titlemenuentity(80),0,-10,180
		RotateEntity titlemenuentity(80),-90,0,0
		TurnEntity titlemenuentity(80),0,180,0
		titlemenutexture(80)=myLoadTexture("data\graphics\logos\st.jpg",1)
		ScaleTexture titlemenutexture(80),80,80
		EntityTexture titlemenuentity(80),titlemenutexture(80)
	;	EntityFX titlemenuentity(80),1
		EntityAlpha Titlemenuentity(80),0
		
		
		; falling stars
		For i=81 To 99
			titlemenuentity(i)=CopyEntity(starmesh)
			RotateEntity Titlemenuentity(i),-90,0,0
			sc#=Rnd(0.8,1)
			ScaleEntity titlemenuentity(i),sc,sc,sc
			EntityFX titlemenuentity(i),1
			PositionEntity titlemenuentity(i),Rnd(-40,40),Rnd(-10,50),Rnd(200,350)
			EntityAlpha titlemenuentity(i),0			

		Next
	EndIf
	
	
	
	
	; Save Slot Picture
	
	
	For i=0 To 9
		SaveSlotEntity(i)=CreateMesh(camera)
		surface=CreateSurface (SaveSlotEntity(i))
		AddVertex (surface,-1,1,0,0,0)
		AddVertex (surface,1,1,0,1,0)
		AddVertex (surface,-1,-0.5,0,0,0.75)
		AddVertex (surface,1,-0.5,0,1,0.75)
		AddTriangle (surface,0,1,2)
		AddTriangle (surface,1,3,2)
		UpdateNormals SaveSlotEntity(i)
		EntityFX SaveSlotEntity(i),1
		If i=0
			ScaleEntity SaveSlotEntity(i),2,2,2
			TranslateEntity SaveSlotEntity(i),0,-0.6,12
		Else
			If widescreen
				TranslateEntity SaveSlotEntity(i),2*(i-5),2.5,16
			Else
				TranslateEntity SaveSlotEntity(i),1.5*(i-5),2.5,16
			EndIf
			ScaleEntity SaveSlotEntity(i),0.5,0.5,0.5
		EndIf
		
		
		EntityOrder SaveSlotEntity(i),-10
		HideEntity SaveSlotEntity(i)
	Next
	
	If WaEpisode=2 Or WaEpisode=0
		For i=0 To 7
			MappieceEntity(i)=CreateCube(Camera)
			ScaleEntity MappieceEntity(i),1,1,0.01
			TranslateEntity  MapPieceEntity(i),-3+2*(i Mod 4),1-2*Floor(i/4),10
		
	
			EntityTexture MapPieceEntity(i),MapPieceTexture(i+1)
			EntityOrder MapPieceEntity(i),-100
			EntityAlpha MapPieceEntity(i),0
		Next
	EndIf

	Return
	
	SaveSlotImage=CreateImage(256,256)
	For i=0 To 9
		SaveSlotEntity(i)=CreateMesh(camera)
		surface=CreateSurface (SaveSlotEntity(i))
		AddVertex (surface,-1,1,0,0.01,0.01)
		AddVertex (surface,1,1,0,.99,0.01)
		AddVertex (surface,-1,-0.5,0,0.01,0.74)
		AddVertex (surface,1,-0.5,0,.99,0.74)
		AddTriangle (surface,0,1,2)
		AddTriangle (surface,1,3,2)
		UpdateNormals SaveSlotEntity(i)
		EntityFX SaveSlotEntity(i),1
		If i=0
			ScaleEntity SaveSlotEntity(i),2,2,2
			TranslateEntity SaveSlotEntity(i),-2,-1.15,9.7
			xxx=LoadTexture("saveslot.bmp")
			EntityTexture SaveSlotEntity(i),xxx
		Else
			TranslateEntity SaveSlotEntity(i),1.5*(i-5),2.2,16
			ScaleEntity SaveSlotEntity(i),0.5,0.5,0.5
		EndIf
		
		
		EntityOrder SaveSlotEntity(i),-10
	Next
	
	
		



		



End Function

Function CreateIcon(x,y,tex,btype,SubText$,HelpText$)

	

	; types:	0 - stinker
	
	; check if button is already active
	If IconEntity(x+y*10)>0 Then Return

	;widescreen
	If widescreen
		If wideicons
			If x<2
				IconX(x+y*10)=-.92+.150904849*x	
			Else
				IconX(x+y*10)=-.44+.150904849*x
			EndIf
		Else
			IconX(x+y*10)=-.68+.150904849*x
		EndIf
		IconY(x+y*10)=.489-.1407506*y
	Else
		IconX(x+y*10)=-.9+.2*x
		IconY(x+y*10)=.65-.187*y
	EndIf
	IconTexture(x+y*10)=tex
	
	IconEntity(x+y*10)=CreateMesh(camera)
	surface=CreateSurface(IconEntity(x+y*10))
	AddVertex surface,-.04,.04,0,(tex Mod 8)*0.125+0.001,Floor((tex Mod 64)/8)*0.125+0.001
	AddVertex surface,.04,.04,0,(tex Mod 8)*0.125+0.1249,Floor((tex Mod 64)/8)*0.125+0.001
	AddVertex surface,-.04,-.04,0,(tex Mod 8)*0.125+0.001,Floor((tex Mod 64)/8)*0.125+0.1249
	AddVertex surface,.04,-.04,0,(tex Mod 8)*0.125+0.1249,Floor((tex Mod 64)/8)*0.125+0.1249

	AddTriangle surface,0,1,2
	AddTriangle surface,1,3,2
	UpdateNormals IconEntity(x+y*10)
	
	If tex>=64
		EntityTexture IconEntity(x+y*10),IconTextureCustom
	Else
		EntityTexture IconEntity(x+y*10),IconTextureStandard
	EndIf
		
	EntityOrder IconEntity(x+y*10),-10
	EntityPickMode IconEntity(x+y*10),2
	EntityFX IconEntity(x+y*10),1
	
	ScaleEntity IconEntity(x+y*10),0,0,0
	PositionEntity IconEntity(x+y*10),IconX(x+y*10)/CameraZoomLevel,IconY(x+y*10)/CameraZoomLevel,1
	
	IconSize(x+y*10)=1
	;If btype=0 Then IconSize(x+y*10)=100
	
	IconType(x+y*10)=btype

	IconSubText$(x+y*10)=SubText$
	IconHelpText$(x+y*10)=HelpText$
		
		
End Function	

Function ControlIcons()

	Entity=CameraPick(camera,MouseX(),MouseY())
	
	
	IconPicked=-1
	For i=0 To 79
		; go through eachpossible icon position
		If IconEntity(i)>0 
		
	
		
			; if active/exists
			If IconSize(i)=1201 Then IconSize(i)=1001
			
			; check if mouse is currently pointing at it
			If Entity=IconEntity(i) And IconSize(i)=1001 And MouseGameMode=-1 And PlayerControlMode=0
				yep=False
				
				; which icons can be used in different gameplay modes
				If GameMode=0  Or (GameMode=12 And (oldGameMode<>8 Or IconType(i)=4))
					; in-game or in Menu
					yep=True
				Else If GameMode=5 Or GameMode=6
					; inventory / swapping
					If IconType(i)=2 Or IconType(i)=4 Or (IconType(i)>=1000 And ((IconType(i)-1001) Mod 10)=0) 
						yep=True	
					EndIf
					If Mouse2=True And Mouse1=False
						; can select active spell/charm when in inventory
						If (IconType(i)>=1000 And ((IconType(i)-1001) Mod 10)=1)
							yep=True
						EndIf
					EndIf
				Else If GameMode=8
					; Dialog
					If IconType(i)=4
						yep=True
					Else
						yep=False
					EndIf
				
					
				EndIf
			
				If yep=True
					IconSize(i)=1201
					IconPicked=i
					EntityAlpha MouseCursor,.8
					HideEntity LevelCursor
					
					If Mouse1=True
						ActivateIcon(i,1)
						MouseGameMode=-2
						For j=0 To 79
							; deactivate icons
							If IconEntity(j)>0 Then EntityPickMode IconEntity(j),0
						Next

					Else If Mouse2=True
						ActivateIcon(i,2)
						MouseGameMode=-2
						For j=0 To 79
							; deactivate icons
							If IconEntity(j)>0 Then EntityPickMode IconEntity(j),0
						Next

					EndIf
				EndIf

			EndIf
		
			; zooming in/out
			If IconSize(i)>0 And (IconSize(i) Mod 2)=0
				; deactivating
				IconSize(i)=IconSize(i)-50
				If IconSize(i)<0 
					; turn off
					IconSize(i)=0
				EndIf
			Else If IconSize(i)<1001 And (IconSize(i) Mod 2)=1
				; activating
				IconSize(i)=IconSize(i)+50
				If IconSize(i)>1001
					; turn on
					IconSize(i)=1001
				EndIf
			EndIf
			
			If IconEntity(i)>0
				ScaleEntity IconEntity(i),Float(IconSize(i))/1001.0,Float(IconSize(i))/1001.0,Float(IconSize(i))/1001.0
			EndIf
			
			; mouse is on it?
	;		If IconSize(i)=1201
	;			IconSize(i)=1001
	;		EndIf
			
			
			; delete
			If IconSize(i)=0
				DeleteIcon(i)
			EndIf
		EndIf
	Next
	
	; Special Case Icons
	If GameMode<10
		If LevelTimer<1000000000
			If NofWeeStinkersInAdventure>0 And AdventureGoal=1
				CreateIcon(7,0,8,3,Str$(NofWeeStinkersInAdventure),"Rescue")
				IconSubText$(7)=Str$(NofWeeStinkersInAdventure)	
			EndIf
			If NofGemsInAdventure>0 And AdventureGoal=3
				CreateIcon(7,0,9,3,Str$(NofGemsInAdventure),"Collect")
				IconSubText$(7)=Str$(NofGemsInAdventure)	
			EndIf
			If NofScrittersInAdventure>0 And AdventureGoal=2
				CreateIcon(7,0,10,3,Str$(NofScrittersInAdventure),"Capture")
				IconSubText$(7)=Str$(NofScrittersInAdventure)	
			EndIf
			If NofFireFlowersInAdventure>0 And AdventureGoal=5
				CreateIcon(7,0,11,3,Str$(NofFireFlowersInAdventure),"Destroy")
				IconSubText$(7)=Str$(NofFireFlowersInAdventure)	
			EndIf
			If NofBricksInAdventure>0 And AdventureGoal=4
				CreateIcon(7,0,12,3,Str$(NofBricksInAdventure),"Destroy")
				IconSubText$(7)=Str$(NofBricksInAdventure)	
			EndIf
			If NofCrabsInAdventure>0 And AdventureGoal=7
				CreateIcon(7,0,13,3,Str$(NofCrabsInAdventure),"Capture")
				IconSubText$(7)=Str$(NofCrabsInAdventure)	
			EndIf
			If NofBabyBoomersInAdventure>0 And AdventureGoal=8
				CreateIcon(7,0,14,3,Str$(NofBabyBoomersInAdventure),"Rescue")
				IconSubText$(7)=Str$(NofBabyBoomersInAdventure)	
			EndIf
			If NofZBotsInAdventure>0 And AdventureGoal=9
				CreateIcon(7,0,15,3,Str$(NofZBotsInAdventure),"Destroy")
				IconSubText$(7)=Str$(NofZBotsInAdventure)	
			EndIf



		EndIf
		
		If currentcharm=1 And currentlightpower>0
			; lamp - drain
			If leveltimer Mod 800 =0
				currentlightpower=currentlightpower-1
				IconSubText$(1)="- "+currentlightpower+" -"
				If currentlightpower=0
					MessageLineText1$="Your lamp has run out."
					MessageLineText2$=""
	
					MessageLineTimer=100
					deleteicon(1)
					currentcharm=0
				EndIf
				For j=0 To 99
					If InventoryItem(j)=2001 Then InventoryHelpText$(j)="- "+currentlightpower+" -"
				Next
			EndIf
		EndIf
	
	
	
		
		If SpellActive=True 
	;		For j=0 To 359 Step 10
		
		;	If leveltimer Mod 10=0
		;		k=Rand(0,360)
		;		AddParticle2(16+CurrentSpell,-2.25+.1*Sin(k),1.65+.1*Cos(k),5,0,.05,.001*Sin(k),.001*Cos(k),0,0,.001,0,0,0,50,2)
		;	;	k=(k+180) Mod 360
		;	;	AddParticle2(16+CurrentSpell,-2.25+.14*Sin(k),1.65+.14*Cos(k),5,0,.05,0,0,0,0,0,0,0,0,50,2)
		;	EndIf
			
			ex#=0
			If widescreen And wideicons
				ex=-0.82
			EndIf
		
			If leveltimer Mod 3=0
				r#=.1+.04*Sin((leveltimer) Mod 360)
				k=(leveltimer*4) Mod 360
				AddParticle2(Rand(16,23),-2.25+r*Sin(k)+ex,1.65+r*Cos(k),5,0,.05,.0005*Sin(k),.0005*Cos(k),0,.04,.001,0,0,0,50,2)
				k=(k+180) Mod 360
				AddParticle2(Rand(16,23),-2.25+r*Sin(k)+ex,1.65+r*Cos(k),5,0,.05,.0005*Sin(k),.0005*Cos(k),0,.04,.001,0,0,0,50,2)
			EndIf
			
	;				Next
		EndIf
		
		; shard melody
		If ShardHitCounter=4
			ShardMelodyTimer=ShardMelodyTimer+1
			If ShardMelodyTimer Mod 60 = 0
				k=(ShardMelodyTimer/60)-1
				SoundPitch SoundFX(13),22000+22000*InventoryID(ShardMelody(k))/7
				PlaySoundFX(13,-1,-1)
				
				k2=19-InventorySize+(ShardMelody(k) Mod inventorysize) + 10*Floor(ShardMelody(k)/inventorysize)
				IconSize(k2)=401
				
				If k=6
					; end song
					ShardHitCounter=5
					ShardMelodyTimer=0
				EndIf
			EndIf
		EndIf
	EndIf

			
				
End Function

Function ActivateIcon(i,MouseButton)

	; if playing shard melody, cancel
	If shardhitcounter=4
		shardhitcounter=0
		shardlasthit=-1
	EndIf

	Select IconType(i)

	Case 1
		; Rucksack - closed
		; Open it
		If levelTimer<1000000000 Or leveltimer>1000000050

			OpenRucksack(i)
			PlaySoundFX(131,-1,-1)
		EndIf

	Case 2
		; Rucksack - open
		; Close it
		CloseRucksack(i)
		PlaySoundFX(132,-1,-1)
		
	Case 3
		; Stinker/gem/etc counter
		; no action
		
	Case 4
		; Menu
		If GameMode<>12
			PlaySoundFX(131,-1,-1)
			StartMenu(0)
			
		Else
			endmenu()
		EndIf
		
		

	
	; **** Important: 1001+ x*10 is always an inventory item
				
	Case 1001
		; Glove in Inventory
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
			EndIf
			
		Else If MouseButton=1
			; Examine the glove
			Select CurrentSpell
			Case -1
				PlaySoundFX(175,-1,-1)
				MessageLineText1$="It's a pair of Rainbow Gloves."
				MessageLineText2$="They are currently not charged."

				MessageLineTimer=100
			Case 0
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Floing' Magic!"

				MessageLineTimer=100
			Case 1
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Pow' Magic!"

				MessageLineTimer=100
			Case 2
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Pop' Magic!"

				MessageLineTimer=100
				
			Case 3
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Grow' Magic!"

				MessageLineTimer=100




			Case 4
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Brr' Magic!"

				MessageLineTimer=100
			Case 5
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Flash' Magic!"

				MessageLineTimer=100

			Case 6
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Blink' Magic!"


				MessageLineTimer=100
			Case 7
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Null' Magic!"
				MessageLineTimer=100
			Case 8
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Bounce' Magic!"
				MessageLineTimer=100
			Case 9
				PlaySoundFX(176,-1,-1)

				MessageLineText1$="These gloves are charged"
				MessageLineText2$="with 'Barrel' Magic!"
				MessageLineTimer=100
			End Select
		
				
		Else If MouseButton=2
			; go into swap mode
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf
		
	Case 1002,1003,1004,1005,1006,1007,1008,1009,1010
		; Glove Icon In Upper Left Corner
		
		; Toggle Active
		ToggleSpell()
		

		
	
	
			
	Case 2001,2011 ; lamp/light
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1
			; Select
			If TooDark()=False
				PlaySoundFX(175,-1,-1)

				MessageLineText1$="This item will activate automatically"
				MessageLineText2$="if you enter a dark area."
				MessageLineTimer=180
			Else
				DeleteIcon(1)
				If IconTYpe(i)=2001
					CreateIcon(1,0,66,2002,"- "+CurrentLightPower+" -","Remove")
					CurrentCharm=1
				Else
					CreateIcon(1,0,67,2012,"Light","Remove")
					CurrentCharm=2
				EndIf
				
				CloseRuckSack(8)
			EndIf

		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf

	Case 2021 ; spy eye
		; "Charms" in Inventory 
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1
			; Select
			If ObjectMovementTimer(PlayerObject)>0
				MessageLineText1$="You must be standing still"
				MessageLineText2$="to use the Spy-Eye."
				MessageLineTimer=180
				
			Else If ObjectFrozen(PlayerObject)>0
				MessageLineText1$="You cannot use the Spy-Eye"
				MessageLineText2$="when you are frozen."
				MessageLineTimer=180

			Else
			
				
	
				PlaySoundFX(176,-1,-1)

				DeleteIcon(1)
				CreateIcon(1,0,68,2022,"Spy-Eye","Remove")
				CurrentCharm=3
				CloseRuckSack(8)
				TurnOffIcons()
				MoveMouse GfxWidth/2,9*GfxHeight/24
				
			EndIf
		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf
	Case 2031 ; token
		
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1
			; Select
			; look for an active arcade in neighbourhood
			For j=0 To NofObjects-1
				If objectType(j)=165
				
					If Abs(Floor(ObjectX(j))-Floor(ObjectX(PlayerObject)))<1.1 And Abs(Floor(ObjectY(j))-Floor(ObjectY(PlayerObject)))<1.1
						; got one
						If ObjectSubType(j)=1
							; not active
							j=NofObjects+10
						Else
							row=Floor(i/10)-1
							col=(i Mod 10)-(9-InventorySize)
							InvenItem=row*InventorySize+col
							PlaySoundFX(12,-1,-1)

							activatecommand(1,ObjectData(j,0),0,0,0)
							ObjectData(j,0)=ObjectData(j,0)+1
							
								If ((ObjectData(j,0)-200) Mod 3) = 0
									; played all three
									ObjectSubType(j)=1
								EndIf
							
							; and delete
							RemoveItemFromInventory(InvenItem)
							CloseRuckSack(8)

						
							j=NofObjects+20
						EndIf
					EndIf
				EndIf
			Next
			If j<NofObjects+10
			
				MessageLineText1$="To use a token you must find and stand"
				MessageLineText2$="next to an active arcade game machine."
				MessageLineTimer=180
				CloseRuckSack(8)

			Else If j<NofObjects+20
				MessageLineText1$="You have already played all three levels"
				MessageLineText2$="on this arcade game."
				MessageLineTimer=180
				CloseRuckSack(8)

			EndIf


	
		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf
	Case 2041 ; glyph
		
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1
			; Select
		
	
			PlaySoundFX(24,-1,-1)

			CloseRuckSack(8)
			TurnOffIcons()
			
			
			; are we in the mirror room
			flag=False
			For j=0 To nofobjects-1
				If objecttype(j)=54 And Abs(objectx(j)-objectx(PlayerObject))<4 And Abs(objecty(j)-objecty(PlayerObject))<6
					; yep
					flag=True
					If objectsubtype(j)=icontexture(i)-79
						flag=False
					EndIf
					ObjectSubtype(j)=icontexture(i)-79
				EndIf
			Next
			If flag=True
				MessageLineText1$="The mirror changes colour."
				MessageLineText2$="A strange sound can be heard."
				MessageLineTimer=180
			Else
				MessageLineText1$="Nothing happens."
				MessageLineText2$=""
				MessageLineTimer=180
			EndIf

						
				
			
		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf
		
	Case 2051	; map
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1
			; Select

	
		
	
			
			
			PlaySoundFX(176,-1,-1)

			
			CurrentCharm=4
			CloseRuckSack(8)
			TurnOffIcons()
			MoveMouse GfxWidth/2,9*GfxHeight/24

			
			For i=0 To 7
				If mappiecefound(i)=True
					EntityTexture mappieceentity(i),mappiecetexture(i+1)
				Else
					EntityTexture mappieceentity(i),mappiecetexture(0)
				EndIf
				PositionEntity MapPieceEntity(i),-3+2*(i Mod 4),1-2*Floor(i/4),10
				EntityAlpha Mappieceentity(i),.9
			Next
			
		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf




		
			
	Case 2002,2012
		; Lamp/Light Gem active
		
		;remove
		DeleteIcon(1)
		CurrentCharm=0

	
	Case 2022
		; Spyglass active
		
		; never used - deactived in playercontrol
		
	
		
		
		
	
		
		
	Case 3001
		; Key Item in inventory - ID is the ID of the item it can open
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1
			; look in neighbourhood of player to see if a keyblock exists with matching ID
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InvenItem=row*InventorySize+col
			x=Floor(ObjectX(PlayerObject))
			y=Floor(ObjectY(PlayerObject))
			flag=False
			For j=0 To NofObjects-1
				If (ObjectType(j)=10) And ObjectExists(j)=True  And ObjectActive(j)=1001
					If Abs(ObjectTileX(j)-x)<=1 And Abs(ObjectTileY(j)-y)<=1
						flag=True
						If ObjectID(j)=InventoryID(InvenItem)
							PlaySoundFX(176,-1,-1)
	
							; Open Gate
							DeActivateObject(j)
							
							j=NofObjects+5
							RemoveItemFromInventory(InvenItem)
							;DeActivateIcon(i)
							
							CloseRuckSack(8)
						EndIf
					EndIf
				EndIf
			Next
			If j<NofObjects+5
				If flag=True
					; gate doesn't match
					MessageLineText1$="Your key doesn't fit"
					messagelinetext2$="this lock."

				Else
					; no item found
					
	
					MessageLineText1$="To use a key, stand directly next to"
					messagelinetext2$="a gate with matching colours."
				EndIf
				PlaySoundFX(175,-1,-1)
				MessageLineTimer=200
				CloseRuckSack(8)
			EndIf

		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf
	Case 3011
		
		; Stinker Whistle in inventory
		AddParticle(4,ObjectX(PlayerObject),0.1,-ObjectY(PlayerObject),0,1,0,0,0,0,.05,0,0,0,60,4)
		Animate GetChild(objectentity(playerobject),3),3,.3,8
		
		; wake up stinkers
		For j=0 To NofObjects-1
			If ObjectType(j)=120 And (ObjectSubType(j)=0 Or ObjectSubType(j)=3) And ObjectExists(j)=True 
				
				If Abs(ObjectTileX(j)-ObjectTileX(PlayerObject))<=3 And Abs(ObjectTileY(j)-ObjectTileY(PlayerObject))<=3
					ObjectSubType(j)=1
					ObjectMovementType(j)=14
					EntityTexture ObjectEntity(j),StinkerWeeTexture(1)
					

				EndIf
			EndIf
		Next
		CloseRuckSack(8)
		
	Case 3021
		; Rainbow Shard
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1

			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InvenItem=row*InventorySize+col
	
			If ShardsAreActive=False
				MessageLineText1$="The shard makes a beautiful sound,"
				MessageLineText2$="but nothing happens."
				MessageLineTimer=200
			Else If AdventureCurrentLevel=-93 Or AdventureCurrentLevel=-94 Or AdventureCurrentLevel=-95
				; in void - can't use
				MessageLineText1$="The shard makes a dull sound."
				MessageLineText2$="Something is wrong."
				MessageLineTimer=200

			Else
			
				If AdventureCurrentStatus>0
					; in Adventure
					MessageLineText1$="You cannot use the shards"
					MessageLineText2$="in an adventure."
					MessageLineTimer=200
				

				
				
				Else If ShardHitCounter<4
					If InventoryID(InvenItem)=ShardLastHit
						ShardHitCounter=ShardHitCounter+1
					Else
						ShardHitCounter=1
					EndIf
					Select ShardHitCounter
						Case 2
							MessageLineText1$="The shard has begun to vibrate..."
							MessageLineText2$="gently at first, but growing stronger."
							MessageLineTimer=200
						Case 3
							MessageLineText1$="A beautiful song begins to play."
							MessageLineText2$=""
							MessageLineTimer=200
							ShardHitCounter=4
							StopChannel (musicchannel)
							currentmusic=0
							For j=0 To 6
								Repeat
									k=Rand(0,InventorySize^2-1)
								Until InventoryItem(k)=3021
								ShardMelody(j)=k
							Next
							ShardMelodyTimer=0
						Default
							ShardHitCounter=1
							MessageLineText1$="A gentle and beautiful glow"
							MessageLineText2$="has begun to eminate from the shard."
							MessageLineTimer=200
					End Select
					
					ShardLastHit=InventoryID(InvenItem)
				Else If ShardHitCounter=5
					; replaying music
					If InvenItem=ShardMelody(ShardMelodyTimer)
						ShardMelodyTimer=ShardMelodyTimer+1
						If ShardMelodyTimer=7
							; you did it - warp!
							
							If WaEpisode=0 And CustomShardEnabled>0 And AdventureCurrentStatus>0
								ActivateCommand(CustomShardCMD(ShardLastHit,0),CustomShardCMD(ShardLastHit,1),CustomShardCMD(ShardLastHit,2),CustomShardCMD(ShardLastHit,3),CustomShardCMD(ShardLastHit,4))
							ElseIf WaEpisode=0 And CustomShardEnabledHub>0 And AdventureCurrentStatus=0
								ActivateCommand(CustomShardCMDHub(ShardLastHit,0),CustomShardCMDHub(ShardLastHit,1),CustomShardCMDHub(ShardLastHit,2),CustomShardCMDHub(ShardLastHit,3),CustomShardCMDHub(ShardLastHit,4))
							Else

								Select ShardLastHit
								Case 0 ; red goes to Forest's End
									ActivateCommand(7,-70,11,15,0)
								Case 1 ; orange goes to Windy Hills
									ActivateCommand(7,-12,17,19,0)
								Case 2	 ; yellow goes to Plains
									ActivateCommand(7,-44,34,14,0)
								Case 3  ; green goes to Wasteland
									ActivateCommand(7,-90,16,15,0)
								Case 4		; blue goes to Foggy Mountains
									ActivateCommand(7,-64,14,12,0)
								Case 5		; indigo goes to Wondertown
									ActivateCommand(7,-21,10,16,0)
								Case 6		; purple goes to Lonely Top
									ActivateCommand(7,-86,11,12,0)
								End Select
							CloseRucksack(8)
							EndIf

						EndIf
					Else
						ShardHitCounter=0
						ShardLastHit=-1
						MessageLineText1$="I think you played"
						MessageLineText2$="a wrong note."
						MessageLineTimer=200
					EndIf
				
				EndIf
			EndIf
					
	
	
						
	
			
			SoundPitch SoundFX(13),22000+22000*InventoryID(InvenItem)/7
			
			PlaySoundFX(13,-1,-1)
		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf



	Case 3091
		; Special Item from Won Adventure
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1
			MessageLineText1$="You cannot use this item here."
			MessageLineText2$=""
			MessageLineTimer=100
		
			CloseRuckSack(8)
			


		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf
		
	Case 4001,4011,4021,4031,4041,4051,4061,4071,4081,4091,4101,4111,4121,4131,4141,4151,4161,4171,4181,4191,4201,4211,4221,4231,4241,4251,4261,4271,4281,4291
		; Custom Item
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
				
			EndIf
			
		Else If MouseButton=1
		
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InvenItem=row*InventorySize+col
		
			If WAEpisode=2 And InventoryID(InvenItem)=137 And IconTexture(i)=64+13
				; special case for mystery item
				x=Floor(ObjectX(PlayerObject))
				y=Floor(ObjectY(PlayerObject))

				flag=False
				If Abs(AdventureCurrentLevel)=45
					If (x=14 And y=10) Or (x=17 And y=11) Or (x=20 And y=13) Or (x=21 And y=16) Or (x=20 And y=19) Or (x=17 And y=21) Or (x=14 And y=22) Or (x=11 And y=21) Or (x=8 And y=19) Or (x=7 And y=16) Or (x=8 And y=13) Or (x=11 And y=11)
						flag = True
						
						Select mysterynumberpos Mod 10
						Case 0
							MessageLineText1$="You don't want to do this."
							MessageLineText2$="Trust me."
							MessageLineTimer=100
						Case 1
							MessageLineText1$="You REALLY don't want to do this."
							MessageLineText2$=""
							MessageLineTimer=100
						Case 2
							MessageLineText1$="Stop now, before it"
							MessageLineText2$="is too late!"
							MessageLineTimer=100
						Case 3
							MessageLineText1$="Too late."
							MessageLineText2$="Now you've done it."
							MessageLineTimer=100

						End Select


						
						activatecommand(1,550+(mysterynumberpos Mod 10),0,0,0) ; make the next central bridge in any case
						If mysterynumberpos<10
							z=mysterynumber(mysterynumberpos)
							If (z=12 And x=14 And y=10) Or (z=1 And x=17 And y=11) Or (z=2 And x=20 And y=13) Or (z=3 And x=21 And y=16) Or (z=4 And x=20 And y=19) Or (z=5 And x=17 And y=21) Or (z=6 And x=14 And y=22) Or (z=7 And x=11 And y=21) Or (z=8 And x=8 And y=19) Or (z=9 And x=7 And y=16) Or (z=10 And x=8 And y=13) Or (z=11 And x=11 And y=11)
								; correct 
								mysterynumberpos=mysterynumberpos+1
								If mysterynumberpos=4
									; all correct
									For iii=550 To 553
										activatecommand(2,iii,0,0,0)
									Next
									activatecommand(1,554,0,0,0)
									For iii=500 To 530 Step 5
										activatecommand(1,iii,0,0,0)
										activatecommand(1,iii+1,0,0,0)
									Next
									PlaySoundFX(139,-1,-1)
									
									MessageLineText1$="CONGRATULATIONS!!!"
									MessageLineText2$="You have unlocked a secret passage."
									MessageLineTimer=100

								EndIf
							Else
								playsoundfx(138,-1,-1)
								mysterynumberpos=mysterynumberpos+11
							EndIf
						Else
							playsoundfx(138,-1,-1)
							mysterynumberpos=mysterynumberpos+1
						EndIf
						
						If (mysterynumberpos Mod 10) = 4 mysterynumberpos=0
						
						RemoveItemFromInventory(InvenItem)

					EndIf
				EndIf
						
						
				
				If flag=False	
					MessageLineText1$="Nothing happens."
					MessageLineText2$="This is a very strange item."
	
					MessageLineTimer=100
				EndIf
				
				
				
				
			Else If WAEpisode=3 And IconTexture(i)=64+8
				; special case for blue flower
				x=Floor(ObjectX(PlayerObject))
				y=Floor(ObjectY(PlayerObject))

				flag=False
				If Abs(AdventureCurrentLevel)=30 And x=6 And y=19
					;CloseRuckSack(8)
					Mouse1=False
					Mousebutton=0
					ActivateCommand(2,500,0,0,0)
				Else
					MessageLineText1$="This is a"
					MessageLineText2$="beautiful blue flower!"

					MessageLineTimer=100
				EndIf
			

					
					
	
						
						
					
					

		
				

			Else
		
				btype=Floor((IconType(i)-4001) / 10)
				btype2=btype Mod 10
			
				
				flag=False
				
				
				
				
				If btype2=1 
					; AdventureWon() - disable, this should only ever happen on pickup itself
				Else If btype2>=2 And btype2<=7
				
					; look (for some types only in neighbourhood of player) to see if a keyblock exists with matching ID
					
					x=Floor(ObjectX(PlayerObject))
					y=Floor(ObjectY(PlayerObject))
					For j=0 To NofObjects-1
						If ObjectExists(j)=True And ObjectID(j)=InventoryID(InvenItem)
						
							If (Abs(ObjectTileX(j)-x)<=1 And Abs(ObjectTileY(j)-y)<=1) Or btype2=5 Or btype2=6 Or btype2=7
								
								; got something - open/close/toggle
								
								If btype2=2 Or btype2=5 
									If (ObjectActive(j) Mod 2)=0
										ActivateObject(j)
										flag=True
									Else
										flag=False
									EndIf
								Else If btype2=3 Or btype2=6
									If (ObjectActive(j) Mod 2)=1
										DeActivateObject(j)
										flag=True
									Else
										flag=False
									EndIf
	
									
								Else If btype2=4 Or btype2=7
									ToggleObject(j)
									flag=True
								EndIf
								
							EndIf
							
						EndIf
					Next
				EndIf
				If flag=False
					; no item found
					MessageLineText1$="You cannot use this item here."
					MessageLineText2$=""
	
					MessageLineTimer=100
					
				Else
					If btype<10
						; destroy object in inventory
						RemoveItemFromInventory(InvenItem)
					EndIf
				EndIf
			EndIf
				
			
			CloseRuckSack(8)
			


		Else If MouseButton=2
			
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
			
		EndIf

			
	

					
	Case 9091
		; Empty Inventory Item
		If GameMode=6
			; Swap places
			If InventorySwapIcon<>i
				SwapItem(i)
			EndIf
		Else If MouseButton=2
;			Print GameMode
			GameMode=6	
			row=Floor(i/10)-1
			col=(i Mod 10)-(9-InventorySize)
			InventorySwapItem=row*InventorySize+col
			InventorySwapIcon=i
		EndIf

		

	
;	Case 1
;		; stinkers - disengage
;		For i=0 To NofStinkers-1
;;			StinkerFollowing(i)=False
;			DeActivateIcon(1)
;		Next	
	End Select
End Function

Function DeActivateIcon(i)
	
	
	If IconSize(i)>=1001
		IconSize(i)=1000
	Else If IconSize(i) Mod 2 =1 And IconSize(i)>0
		IconSize(i)=IconSize(i)-1
	EndIf	
End Function	

Function DeleteIcon(i)

	If IconEntity(i)>0 FreeEntity IconEntity(i)
	IconEntity(i)=0
		
End Function

Function TurnOffIcons()

	For i=0 To 79
		If IconEntity(i)>0
			EntityPickMode IconEntity(i),0
		EndIf
	Next
End Function

Function TurnOnIcons()

	For i=0 To 79
		If IconEntity(i)>0
			EntityPickMode IconEntity(i),2
		EndIf
	Next
End Function



Function OpenRucksack(i)

	
	ShardHitCounter=0
	ShardLastHit=-1


	If GameMode=12 Then EndMenu()
	
	IconType(i)=2
	tex=1
	VertexTexCoords GetSurface(IconEntity(i),1),0,(tex Mod 8)*0.125+0.001,Floor((tex Mod 64)/8)*0.125+0.001
	VertexTexCoords GetSurface(IconEntity(i),1),1,(tex Mod 8)*0.125+0.1249,Floor((tex Mod 64)/8)*0.125+0.001
	VertexTexCoords GetSurface(IconEntity(i),1),2,(tex Mod 8)*0.125+0.001,Floor((tex Mod 64)/8)*0.125+0.1249
	VertexTexCoords GetSurface(IconEntity(i),1),3,(tex Mod 8)*0.125+0.1249,Floor((tex Mod 64)/8)*0.125+0.1249
	IconHelpText$(i)="Close"
	; Create Inventory Icons
	j=0
	For y=1 To InventorySize
		For x=9-InventorySize To 8
			;If InventoryItem(j)>0
				If IconSize(x+y*10)>0 And IconSize(x+y*10) Mod 2 = 0
					IconSize(x+y*10)=1001
				Else
					CreateIcon(x,y,InventoryTexture(j),InventoryItem(j),InventorySubText$(j),InventoryHelpText$(j))
				EndIf
			;Else
			;	CreateIcon(x,y,3,9091,"","Empty")
			;EndIf
			j=j+1
		Next
	Next
	If IconSize(6+(InventorySize+1)*10)>0 And IconSize(6+(InventorySize+1)*10) Mod 2 = 0
		IconSize(6+(InventorySize+1)*10)=1001
	Else
		CreateIcon(6,InventorySize+1,47,-1,"","")
	EndIf

	If IconSize(7+(InventorySize+1)*10)>0 And IconSize(7+(InventorySize+1)*10) Mod 2 = 0
		IconSize(7+(InventorySize+1)*10)=1001
	Else
		CreateIcon(7,InventorySize+1,6,-1,"","")
	EndIf
	If IconSize(8+(InventorySize+1)*10)>0 And IconSize(8+(InventorySize+1)*10) Mod 2 = 0
		IconSize(8+(InventorySize+1)*10)=1001
	Else
		CreateIcon(8,InventorySize+1,7,-1,"","")
	EndIf

	
	
	GameMode=5
End Function

Function CloseRucksack(i)

	
	ShardHitCounter=0
	ShardLastHit=-1


;	If NofInventoryItems>0	

		IconType(i)=1
		tex=0
		VertexTexCoords GetSurface(IconEntity(i),1),0,(tex Mod 8)*0.125+0.001,Floor((tex Mod 64)/8)*0.125+0.001
		VertexTexCoords GetSurface(IconEntity(i),1),1,(tex Mod 8)*0.125+0.1249,Floor((tex Mod 64)/8)*0.125+0.001
		VertexTexCoords GetSurface(IconEntity(i),1),2,(tex Mod 8)*0.125+0.001,Floor((tex Mod 64)/8)*0.125+0.1249
		VertexTexCoords GetSurface(IconEntity(i),1),3,(tex Mod 8)*0.125+0.1249,Floor((tex Mod 64)/8)*0.125+0.1249
		IconHelpText$(i)="Open"
;	EndIf
	; Close Inventory Icons
	For x=9-InventorySize To 8
		For y=1 To InventorySize
			DeActivateIcon(x+y*10)
		Next
	Next
	DeActivateIcon(6+(InventorySize+1)*10)

	DeActivateIcon(7+(InventorySize+1)*10)
	DeActivateIcon(8+(InventorySize+1)*10)
	GameMode=0
End Function

Function AddItemToInventory(Spot,Item,ID,Tex,SubText$,HelpText$)

	If NofInventoryItems=0 And usedinventoryonce=False
		CreateIcon(8,0,0,1,"Items","Open")
		usedInventoryonce=True
	EndIf


	If Spot=-1
		; none specified - just picked first one
		For Spot=0 To 100
			If InventoryItem(Spot)=9091
				Exit
			EndIf
		Next
	EndIf
		
	InventoryItem(Spot)=Item
	InventoryID(Spot)=ID
	InventoryTexture(Spot)=Tex
	InventorySubText$(Spot)=SubText$
	InventoryHelpText$(Spot)=HelpText$	
	NofInventoryItems=NofInventoryItems+1
	
	; check if inventory is open
	IconSpotX=9-InventorySize+(Spot Mod InventorySize)
	IconSpotY=(Floor(Spot/InventorySize)+1)
	If IconEntity(IconSpotX+IconSpotY*10)>0
		DeleteIcon(IconSpotX+IconSpotY*10)
		CreateIcon(IconSpotX,IconSpotY,Tex,Item,SubText$,HelpText$)
	EndIf
	

		
End Function

Function RemoveItemFromInventory(i)

	
	InventoryItem(i)=9091
	InventoryID(i)=-1
	InventoryTexture(i)=3
	InventorySubText$(i)=""
	InventoryHelpText$(i)="Empty"
	
	NofInventoryItems=NofInventoryItems-1
	
	
	
End Function

Function SelectSpell(i,InvenIcon)

	Restore SpellData
	For j=1 To i
		Read a$
		Read b$
	Next

	DeleteIcon(0)
	CreateIcon(0,0,16+i-1,1002+(i-1)*10,a$,b$)
	
	
	If InvenIcon>=0
		row=Floor(InvenIcon/10)-1
		col=(InvenIcon Mod 10)-(9-InventorySize)
		RemoveItemFromInventory(row*InventorySize+col)
	EndIf

	If CurrentSpell<>i And CurrentSpell>0
		Restore .SpellData
		For j=1 To CurrentSpell
			Read a$
			Read b$
		Next
		If InvenItem=-1
			AddItemToInventory(-1,1001+(CurrentSpell-1)*10,-1,16+CurrentSpell-1,a$,"Put On")
		Else
			AddItemToInventory(row*InventorySize+col,1001+(CurrentSpell-1)*10,-1,16+CurrentSpell-1,a$,"Put On")
		EndIf
	EndIf
	
	CurrentSpell=i
End Function

Function ToggleSpell()

	; turn the glove from active to deactive and vice versa
	SpellActive=Not SpellActive
	If SpellActive=True
		ObjectMoveXGoal(PlayerObject)=ObjectTileX2(PlayerObject)
		ObjectMoveYGoal(PlayerObject)=ObjectTileY2(PlayerObject)
		PlayerTalkToGoalObject=-1
		If CurrentSpell=5 And IndigoActive=3
			IconSubText$(0)="X "+Str$(CurrentSpellPower)+" X"
		Else
			IconSubText$(0)="- "+Str$(CurrentSpellPower)+" -"
		EndIf
		IconHelpText$(0)="Remove"
		;PlaySoundFX(90,-1,-1)
	Else
		If CurrentSpell=5 And IndigoActive=3
			IconSubText$(0)="X "+Str$(CurrentSpellPower)+" X"
		Else
			IconSubText$(0)="- "+Str$(CurrentSpellPower)+" -"
		EndIf
		IconHelpText$(0)="Activate"
		;PlaySoundFX(91,-1,-1)
	EndIf
		
End Function


Function SelectCharm(i,InvenIcon)

	Restore Charmdata
	For j=1 To i
		Read a$
		Read b$
	Next

	DeleteIcon(1)
	CreateIcon(1,0,32+i-1,2002+(i-1)*10,a$,b$)
	
	If InvenIcon>=0	
		row=Floor(InvenIcon/10)-1
		col=(InvenIcon Mod 10)-(9-InventorySize)
		RemoveItemFromInventory(row*InventorySize+col)
	EndIf
	
	If CurrentCharm<>i And CurrentCharm>0
		Restore Charmdata
		For j=1 To CurrentCharm
			Read a$
			Read b$
		Next
		
		If invenitem=-1
			AddItemToInventory(-1,2001+(CurrentCharm-1)*10,-1,32+CurrentCharm-1,a$,"Wear")
		Else
			AddItemToInventory(row*InventorySize+col,2001+(CurrentCharm-1)*10,-1,32+CurrentCharm-1,a$,"Wear")
		EndIf
		
	EndIf		
	CurrentCharm=i

End Function

Function TooDark()
	
	If LightRedgoal+LightBluegoal+LightGreengoal+AmbientRedgoal+AmbientBluegoal+AmbientGreengoal<500
		
		Return True
	EndIf
	Return False
End Function
Function AutoGlowGem()
	If toodark()=True
	
	; check inventory for light
	For j=0 To 99
		If (InventoryItem(j)=2001 Or InventoryItem(j)=2011) 
			
				
				DeleteIcon(1)
				If InventoryItem(j)=2001
					CreateIcon(1,0,66,2002,"- "+CurrentLightPower+" -","Remove")
					CurrentCharm=1
				Else
					CreateIcon(1,0,67,2012,"Light","Remove")
					CurrentCharm=2
				EndIf
				
			

		EndIf

	Next
	EndIf


End Function


Function TurnOnLightSpell()


End Function


Function SwapItem(i)
	; i is the current icon number
	; other icon/item is stored in SwapItem/SwapIcon
	GameMode=5
				
	row=Floor(i/10)-1
	col=(i Mod 10)-(9-InventorySize)
	ThisItem=row*InventorySize+col

	SwapItem=InventoryItem(ThisItem)
	SwapID=InventoryID(ThisItem)
	SwapTexture=InventoryTexture(ThisItem)
	SwapSubText$=InventorySubText$(ThisItem)
	SwapHelpText$=InventoryHelpText$(ThisItem)
	
	j=InventorySwapItem
	AddItemToInventory(ThisItem,InventoryItem(j),InventoryID(j),InventoryTexture(j),InventorySubText$(j),InventoryHelpText$(j))
	;IconSize(i)=1001
	
	AddItemToInventory(InventorySwapItem,SwapItem,SwapID,SwapTexture,SwapSubText$,SwapHelpText$)
	NofInventoryItems=NofInventoryItems-2
	;IconSize(InventorySwapIcon)=1001
End Function

Function StartDialog(i,i2,Full)

	; starts conversation #i (i.e. entire file) with starting interchange #i2
	;						(UNLESS i2=-1, in which case the characters set starting conversation is used)

	GameMode=8
	PlayerControlMode=0

	DialogObject1=-1 ; toward what object player should turn (as default:none)
	DialogObject2=-1 ; what object should turn toward player (as default:none)

	
	; stop the player
	ObjectMoveXGoal(PlayerObject)=ObjectTileX2(PlayerObject)
	ObjectMoveYGoal(PlayerObject)=ObjectTileY2(PlayerObject)


	
	CurrentDialog=i
	CurrentInterChange=i2
	If Full=0
		DialogLineLength=38
	Else 
		DialogLineLength=30
	EndIf
	
	; load data from dialog i
	LoadDialog(i)
	
	
	If i2<0 Then CurrentInterChange=StartingInterChange
	StartInterChange()
	
	MoveMouse GfxWidth/2.0,GfxHeight*14/24.0

	
	ShowEntity DialogBackgroundEntity
	
	

End Function

Function EndDialog()
	
	; save data 
	SaveDialog(CurrentDialog)
	CameraAddZoom=0
	CameraAddX=0
	CameraAddY=0
	CameraAddZ=0
	
	GameMode=0
		
	DialogObject1=-1
	DialogObject2=-1

End Function

Function LoadDialog(dia)
	

	; first clear all data
	StartingInterChange=0
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
		AskAboutActive(i)=0
		AskAboutInterchange(i)=0
		AskAboutRepeat(i)=0
	Next
	
	; Now load
	If AdventureCurrentStatus=0
		; In Hub
		
		file=ReadFile(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+Str$(dia)+".dia")
	Else
		; in Adventure
		file=ReadFile(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Adventure\"+Str$(dia)+".dia")
	EndIf

	Repeat
		NofInterchanges=ReadInt(file)
	Until nofinterchanges<>-999
	;If nofinterchanges=-999 Then nofinterchanges=ReadInt(file) ; crypted
	
	For i=0 To NofInterchanges-1
		NofInterChangeTextLines(i)=ReadInt(file)	
		For j=0 To NofInterChangeTextLines(i)-1
			InterChangeTextLine$(i,j)=ReadString$(file)
			
			; Mystery!
			If WAEpisode=2 And dia=68
				
				If (i=23 Or i=25) And j=3
					InterChangeTextLine$(i,j)=Str$(MysteryNumber(0))
				EndIf
				If (i=28 Or i=29) And j=3
					InterChangeTextLine$(i,j)=Str$(MysteryNumber(1))
				EndIf
				If (i=35 Or i=38) 
					If j=3
						InterChangeTextLine$(i,j)=Str$(MysteryNumber(2))
					Else If j=5
						InterChangeTextLine$(i,j)=Str$(MysteryNumber(3))
					EndIf
						
				EndIf
			EndIf
			
			; WA3BlueFLower
			If WaEpisode=3 And dia=56 And Adventurecurrentstatus=0 
				If Wa3BlueFlowerStatus=0 Then Wa3BlueFlowerStatus=1
				If i=9 And j=0
					Restore flowertext
					For kkk=1 To Abs(WA3BlueFlower)
						Read kkk1
						Read kkk3$
						Read kkk2
					Next
					kkk4$=Mid$(kkk3$,3,1)+Mid$(kkk3$,1,1)+Mid$(kkk3$,7,1)+Mid$(kkk3$,5,1)+Mid$(kkk3$,11,1)+Mid$(kkk3$,9,1)
					InterChangeTextLine$(i,j)=Str$(kkk1)+" "+Str$(kkk2)+" "+kkk4$
					
				EndIf
			EndIf
			
			; WA3Vault
			If WaEpisode=3 And (dia=159 Or dia=160) And Adventurecurrentstatus=0 
				wa3vtex$=interchangeTextline$(i,j)
				interchangetextline$(i,j)=""
				For kkk=1 To Len(wa3vtex$)
					kkk2=Asc(Mid$(wa3vtex$,kkk,1))-32+18
					If kkk2>=91
						kkk2=kkk2-91
					EndIf
					interchangetextline$(i,j)=interchangetextline$(i,j)+Chr$(kkk2+32)
				Next
			EndIf



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
	StartingInterChange=ReadInt(file)
	CloseFile file
	

	
	

	

End Function

Function SaveDialog(dia)

	If AdventureCurrentStatus=0

		; In Hub
		file=WriteFile(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+Str$(dia)+".dia")
	Else
		file=WriteFile(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Adventure\"+Str$(dia)+".dia")
	EndIf

	WriteInt file,-999
	WriteInt File,NofInterchanges
	For i=0 To NofInterchanges-1
				
		WriteInt File,NofInterChangeTextLines(i)
		For j=0 To NofInterChangeTextLines(i)-1
			
			; WA3Vault
			If WaEpisode=3 And (dia=159 Or dia=160) And Adventurecurrentstatus=0 
				wa3vtex$=interchangeTextline$(i,j)
				interchangetextline$(i,j)=""
				For kkk=1 To Len(wa3vtex$)
					kkk2=Asc(Mid$(wa3vtex$,kkk,1))-32-18
					If kkk2<0
						kkk2=kkk2+91
					EndIf
					interchangetextline$(i,j)=interchangetextline$(i,j)+Chr$(kkk2+32)
				Next
			EndIf

		
		
			WriteString file,InterChangeTextLine$(i,j)
		Next
		WriteInt file,NofTextCommands(i)
		For j=0 To NofTextCommands(i)-1
			WriteString file,DialogTextCommand$(i,j)
			WriteInt File,DialogTextCommandPos(i,j)
		Next
		
		

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

	WriteInt File,NofAskAbouts
	WriteString file,AskAboutTopText$
	For i=0 To NofAskAbouts-1
		WriteString file,AskAboutText$(i)
		WriteInt File,AskAboutActive(i)
		WriteInt File,AskAboutInterchange(i)
		WriteInt File,AskAboutRepeat(i)
	Next
	WriteInt file,StartingInterChange
	CloseFile file
	
	
	
End Function


Function StartInterChange()

	
	
	Width=38
	
	
	; re-size the box
	surface=GetSurface(DialogBackGroundEntity,1)
	VertexCoords surface,0,-2,1.3,5
	VertexCoords surface,1,2,1.3,5
	VertexCoords surface,2,-2,-0.5-(NofInterChangeReplies(CurrentInterChange)-1)*0.16,5
	VertexCoords surface,3,2,-0.5-(NofInterChangeReplies(CurrentInterChange)-1)*0.16,5
	
	
	DialogTimer=0
	
	MouseGameMode=-2
	
	

End Function

Function DisplayDialog()

	DialogcurrentRed=255
	DialogcurrentGreen=255
	DialogcurrentBlue=255
	DialogcurrentEffect=0
	
	If CurrentInterChange=-1 ; in Ask-About List
		DisplayAskAbout()
		Return
	EndIf
	
	Width=38
	
	
	; display the text
	If NofInterChangeTextLines(CurrentInterChange)<7 Then Yoffset=(7-NofInterChangeTextLines(CurrentInterChange))
	totalletters=0

	For i=0 To NofInterChangeTextLines(CurrentInterChange)-1
		; how much of this line is to be displayed?
		If totalletters+Len(InterChangeTextLine$(CurrentInterChange,i))<=DialogTimer
			; display entire line
			length=Len(InterChangeTextLine$(CurrentInterChange,i))
		Else If totalletters>DialogTimer
			; don't display line
			length=0
		Else
			; display part of line
			length=dialogtimer-totalletters
		EndIf
		DialogLineLength=Len(InterChangeTextLine$(CurrentInterChange,i))
		; now add the letters of this line
		For j=1 To length
			
			For k=0 To NofTextCommands(CurrentInterChange)
				If j+i*38-1=DialogTextCommandPos(CurrentInterChange,k); Or (j=length And j+i*38=DialogTextCommandPos(CurrentInterChange,k))
						; yes, special command
					Select DialogTextCommand$(CurrentInterChange,k)
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
				If dialogtimer<totalletters
					size=0.00001
				Else If dialogtimer<totalletters+10
					size=4.0-3.0*Sin((dialogtimer-totalletters)*9)
					spacing=1.0/size
				EndIf
			Case 6
				If dialogtimer<totalletters
					size=0.00001
				Else If dialogtimer<totalletters+10
					size=4.0-3.0*Sin((dialogtimer-totalletters)*9)
					spacing=1.0/size
				EndIf
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
			






			End Select
			
			x#=5+(40-Width)/2.0+(Width-DialogLineLength)/2+(j-1)+xoff
			y#=4+YOffset/2.0+i+yoff
			let=Asc(Mid$(InterChangeTextLine$(CurrentInterChange,i),j,1))-32
			;widescreen
			If widescreen
				AddLetter(let,(-.97+x*.0396*size*spacing)/CameraZoomLevel,(.7-y*.0623*size*spacing)/CameraZoomLevel,1.33,0,.044*size/CameraZoomLevel,0,0,0,0,0,0,0,0,0,dialogcurrentred,dialogcurrentgreen,dialogcurrentblue)
			Else
				AddLetter(let,(-.97+x*.0396*size*spacing)/CameraZoomLevel,(.7-y*.0623*size*spacing)/CameraZoomLevel,1.0,0,.044*size/CameraZoomLevel,0,0,0,0,0,0,0,0,0,dialogcurrentred,dialogcurrentgreen,dialogcurrentblue)
			EndIf
			
			totalletters=totalletters+1
		Next
			
	Next
	
	
	
	; now display responses
	If InterChangeReplyText$(CurrentInterChange,0)<>"(OK)"
		DisplayText("Choose Your Response:",49.5-21/2.0,12.5*2,0.5,1,255,255*0.9,0)
	EndIf
	MouseFlag=False
	For i=0 To NofInterChangeReplies(CurrentInterChange)-1
		size2#=1.0
		If DialogTimer<25
			size2#=Float(DialogTimer)/25.0
		EndIf
		If i=CurrentInterChangeReply
			MouseFlag=True
			size#=(0.25*Sin(LevelTimer*4 Mod 360)+1)*size2
			col#=1.0
		Else
			
			size#=1.0*size2
			col#=0.75
		EndIf
		If InterChangeReplyText$(CurrentInterChange,i)<>"(OK)"
			length=Len(InterChangeReplyText$(CurrentInterChange,i))+2
			DisplayText(Chr$(34)+InterChangeReplyText$(CurrentInterChange,i)+Chr$(34),25-length/2.0,13.5+i,size,1.0/size,255,255*col,0)
		Else
			length=Len(InterChangeReplyText$(CurrentInterChange,i))
			DisplayText(InterChangeReplyText$(CurrentInterChange,i),25-length/2.0,13.5+i,size,1.0/size,255,255*col,0)
		EndIf
	Next
	If MouseFlag=True
		EntityAlpha MouseCursor,.4
	Else
		EntityAlpha MouseCursor,.8
	EndIf
		

	
End Function

Function StartAskAbout()

	


	DialogTextLine$(0)=AskAboutTopText$
	j=1
	
	For i=0 To NofAskAbouts-1
		; check if this askabout topic is active
		If AskAboutRepeat(i)<>0
			If AskAboutActive(i)>=0 ; extra test if tied to masterlist
				If MasterAskAboutActive(AskAboutActive(i))=1
					DialogTextLine$(j)=AskAboutText$(i)
					j=j+1
				EndIf
			Else If AskaboutActive(i)=-2
				DialogTextLine$(j)=AskAboutText$(i)
				j=j+1
			EndIf
		EndIf
	Next
		
	CurrentInterChangeNofLines=j
	
	; responses
	
	; re-size the box
	surface=GetSurface(DialogBackGroundEntity,1)
	VertexCoords surface,2,-2,-0.5,5
	VertexCoords surface,3,2,-0.5,5
	
	
	DialogTimer=0
	
	

End Function

Function DisplayAskAbout()
	
	
	
	
	; display the text
	If CurrentInterChangeNofLines<10 Then j=(10-CurrentInterChangeNofLines)
	DisplayText(DialogTextLine$(0),(50-Len(DialogTextLine$(0)))/2,4+j/2.0,1,1,255,255*0.9,0)
	MouseFlag=False
	For i=1 To CurrentInterChangeNofLines-1
		size2#=1.0
		If DialogTimer<25
			size2#=Float(DialogTimer)/25.0
		EndIf
		If i=CurrentInterChangeReply
			size#=(0.25*Sin(LevelTimer*4 Mod 360)+1)*size2
			col#=1.0
			MouseFlag=True
		Else
			size#=1.0*size2
			col#=0.75
		EndIf

			
		DisplayText(DialogTextLine$(i),(50-Len(DialogTextLine$(i)))/2,4.5+j/2.0+i,size,1.0/size,255,255*col,0)
	Next
	If MouseFlag=True
		EntityAlpha MouseCursor,.4
	Else
		EntityAlpha MouseCursor,.8
	EndIf
		
		

	
End Function


Function ControlDialog()

	If menukeydown=True
		If KeyDown(208)=False And KeyDown(200)=False And KeyDown(156)=False And KeyDown(28)=False
			menukeydown=False
		EndIf
	EndIf
	
	
	DialogTimer=DialogTimer+1
	
	; do zoom in/out of background box 
	PositionEntity DialogBackGroundEntity,0,0,20-20*Sin(DialogBackGroundSize)
	If DialogContrast=3
		EntityAlpha DialogBackGroundEntity,0
	Else
		EntityAlpha DialogBackGroundEntity,Float(DialogBackGroundSize)/180.0
	EndIf
	If GameMode=8
		If DialogBackGroundSize<90 
			DialogBackGroundSize=DialogBackGroundSize+2	
			Return
		EndIf
	Else
		If DialogBackGroundSize>0 
			DialogBackGroundSize=DialogBackGroundSize-2
		Else
			HideEntity DialogBackGroundEntity
		EndIf
	EndIf
	
	
	
	
	; but if not in dialog mode, do nothing else
	If GameMode<>8 Return
	
	;EntityAlpha MouseCursor,.8
	HideEntity LevelCursor


	
	; check mouse position
	CurrentInterChangeReply=-1
	mx#=MouseX()*50.0/Float(GfxWidth)
	my#=MouseY()*24.0/Float(GfxHeight)
	
	
	
	
	If CurrentInterChange=-1
		; in ask about
		j=0
		If CurrentInterChangeNofLines<10 Then j=(10-CurrentInterChangeNofLines)
		For i=1 To CurrentInterChangeNofLines-1
			If mx>15 And mx<45 And my>5+i+j/2.0 And my<6+i+j/2.0
				CurrentInterChangeReply=i
			EndIf
		Next
		
		If KeyDown(200) And MenuKeydown=False
			MenuKeydown=True
			
			
			If CurrentInterChangeReply=-1 
				CurrentInterChangeReply=1
			Else
				CurrentInterChangeReply=CurrentInterChangeReply-1
				If CurrentInterChangeReply=0 
					CurrentInterChangeReply=CurrentInterChangeNofLines-1
				EndIf
			EndIf
			MoveMouse GfxWidth/2.0,(5.5+CurrentInterChangeReply+j/2.0)*Float(GfxHeight)/24.0
			
		
		EndIf
		If KeyDown(208) And menukeydown=False
			menukeydown=True
			If CurrentInterChangeReply=-1 
				CurrentInterChangeReply=1
			Else
				CurrentInterChangeReply=CurrentInterChangeReply+1
				If CurrentInterChangeReply=CurrentInterChangeNofLines
 					CurrentInterChangeReply=1
				EndIf
			EndIf
			MoveMouse GfxWidth/2.0,(5.5+CurrentInterChangeReply+j/2.0)*Float(GfxHeight)/24.0
			
		
		EndIf

		keyboardenterflag=False
		If (KeyDown(28) Or KeyDown(156)) And menukeydown=False 
			keyboardenterflag=True
			If currentinterchangereply=-1
				currentinterchangereply=1
			EndIf
		EndIf
		
		If (Mouse1 Or keyboardenterflag) And MouseGameMode<>-2 And CurrentInterChangeReply>=0
			menukeydown=True
			; start the interchange associated with this askabout
			; skip the 'hidden' ones to figure which one this is
			k=0
			For j=0 To 20
				If AskAboutRepeat(j)<>0
					If AskAboutActive(j)>=0 ; extra test if tied to masterlist
						If MasterAskAboutActive(AskAboutActive(j))=1
							k=k+1
						EndIf
					Else If AskAboutActive(j)=-2
						k=k+1
					EndIf
				EndIf
				If k=CurrentInterChangeReply Then Exit
			Next
			; check if this topic has limited use
			If AskAboutRepeat(j)>0 Then AskAboutRepeat(j)=AskAboutRepeat(j)-1
			; and start the associated interchange
			PlaysoundfxNow(136)

			CurrentInterChange=AskAboutInterChange(j)
			StartInterChange()
		EndIf

		
	Else
		; in regular reply mode
		For i=0 To NofInterChangeReplies(CurrentInterChange)-1
			If mx>5 And mx<45 And my>13.5+i And my<14.5+i ; And mx<2+DialogReplyXpos(i)+Len(InterChangeReplyText$(CurrentInterChange,i)) And my>13 And my<15
				CurrentInterChangeReply=i
			EndIf
		Next
		
		If KeyDown(200) And MenuKeydown=False
			MenuKeydown=True
			
			
			If CurrentInterChangeReply=-1 
				CurrentInterChangeReply=0
			Else
				CurrentInterChangeReply=CurrentInterChangeReply-1
				If CurrentInterChangeReply=-1 
					CurrentInterChangeReply=NofInterChangeReplies(CurrentInterChange)-1
				EndIf
			EndIf
			MoveMouse GfxWidth/2.0,(14.0+CurrentInterChangeReply)*Float(GfxHeight)/24.0
			
		
		EndIf
		If KeyDown(208) And menukeydown=False
			menukeydown=True
			If CurrentInterChangeReply=-1 
				CurrentInterChangeReply=0
			Else
				CurrentInterChangeReply=CurrentInterChangeReply+1
				If CurrentInterChangeReply=NofInterChangeReplies(CurrentInterChange)
 					CurrentInterChangeReply=0
				EndIf
			EndIf
			MoveMouse GfxWidth/2.0,(14.0+CurrentInterChangeReply)*Float(GfxHeight)/24.0
			
		
		EndIf
		
		keyboardenterflag=False
		If (KeyDown(28) Or KeyDown(156)) And menukeydown=False 
			keyboardenterflag=True
			If currentinterchangereply=-1
				currentinterchangereply=0
			EndIf
		EndIf

	
		; do control of replies
		If (Mouse1  Or keyboardenterflag=True) And MouseGameMode<>-2 And CurrentInterChangeReply>=0
			Menukeydown=True
			; first do the general adventure command associated with it
			ActivateCommand(InterChangeReplyCommand(CurrentInterChange,CurrentInterChangeReply),InterChangeReplyCommandData(CurrentInterChange,CurrentInterChangeReply,0),InterChangeReplyCommandData(CurrentInterChange,CurrentInterChangeReply,1),InterChangeReplyCommandData(CurrentInterChange,CurrentInterChangeReply,2),InterChangeReplyCommandData(CurrentInterChange,CurrentInterChangeReply,3))
		
			; then the interchange reply function
			PlaysoundfxNow(136)
			Data1=InterChangeReplyData(CurrentInterChange,CurrentInterChangeReply)
			Select InterChangeReplyFunction(CurrentInterChange,CurrentInterChangeReply)
			Case 1
				; Stop dialog and set 
				If Data1>=0 Then StartingInterChange=Data1
				EndDialog()
			Case 2
				; Go to interchange Data1
				CurrentInterChange=Data1
				StartInterChange()
			Case 3
				; Open Ask About
				CurrentInterChange=-1
				StartAskAbout()	
			Case 4
				; do a gold check
				If PlayerCoins>=Data1
					PlayerCoins=PlayerCoins-Data1
					CurrentInterChange=CurrentInterCHange+2
					StartInterChange()
				Else
					CurrentInterChange=CurrentInterCHange+1
					StartInterChange()
				EndIf
				
			Case 5,6
				; do an item check (6 is non-destructive)
				flag=False
				For j=0 To 99
					If InventoryID(j)=Data1
						flag=True
						i=j
					EndIf
				Next
				If flag=True
					If InterChangeReplyFunction(CurrentInterChange,CurrentInterChangeReply)=5 Then RemoveItemFromInventory(i)
					CurrentInterChange=CurrentInterCHange+2
					StartInterChange()
				Else
					CurrentInterChange=CurrentInterCHange+1
					StartInterChange()
				EndIf

					
				
				
			End Select
				
			MouseGameMode=-2
		EndIf
	EndIf
		


	
End Function

Function StartMenu(menu)


	
	
	; general clean up for all menus
	CurrentMenu=menu
	CurrentMenuNofItems=0
	For i=0 To 9
		MenuActive(menu,i)=2
		If MenuText(menu,i)<>""
			CurrentMenuNofItems=i+1
		EndIf
	Next
	
	For i=0 To 9
		HideEntity SaveSlotEntity(i)
	Next
		
	menutime=MilliSecs()
	RotateEntity levellight,35,-35,0
	
	
	
	If menu=11 Or menu=12 
		
			
		For i=78 To 99
			
			EntityAlpha Titlemenuentity(i),1
		Next
		If WAEpisode=3 EntityAlpha Titlemenuentity(79),.5

		
		EntityAlpha titlemenuentity(60),1
	
		PositionEntity titlemenuentity(60),0,.30,2	
	EndIf
	
	If menu=24 Then currentmenuNofItems=8


	If Menu=0	
		; In-Game Main Menu
		
		ExitAfterThisSave=False
		
		; save slot image - prepare each time in-game menu is started
		If SaveSlotImage>0 FreeImage SaveSlotImage
		SaveSlotImage=CreateImage(256,256)
		
		If SaveSlotImage2>0 FreeImage SaveSlotImage2
		
		SaveSlotImage2=CreateImage(2048,2048)
		GrabImage SaveSlotImage2,0,0
					
		If GameMode=8 ; disable load/save if in dialog
			MenuActive(menu,1)=1
			MenuActive(menu,2)=1
		EndIf
		If AdventureCurrentStatus=0 ; disable adventure options if in hub
			MenuActive(menu,3)=1
			MenuActive(menu,4)=1
		EndIf
		If LevelTimer>=1000002000 And LevelTimer<=1000002500
			; game lost - don't allow save
			MenuActive(menu,2)=1
		EndIf
		
	Else If Menu=1 Or Menu=2 Or menu=3
		; abort - yes/no
		MenuActive(menu,0)=3
		MenuActive(menu,1)=3
		MenuActive(menu,2)=3
	
	Else If menu=4 Or menu=5 Or menu=16
		MenuActive(menu,0)=3
		MenuActive(menu,1)=3
		; load/save	- prepare info on save slots
		For i=1 To 9
			
			If SaveSlotTexture(i)>0
				FreeTexture SaveSlotTexture(i)
				SaveSlotTexture(i)=0
			EndIf
			If FileType (GlobalDirName$+"\Player Profiles\"+playername$+"\SaveFiles\"+Str$(i)+"\wla")=0
				SaveSlotLevelName$(i)=""
				SaveSlotDateTime$(i)=""
				SaveSlotTexture(i)=myLoadTexture("data\graphics\emptyslot.jpg",1)
				EntityTexture SaveSlotEntity(i),SaveSlotTexture(i)
				ShowEntity SaveSlotEntity(i)
				
			Else
				
				file=ReadFile (GlobalDirName$+"\Player Profiles\"+playername$+"\SaveFiles\"+Str$(i)+"\wla")	
				SaveSlotDateTime$(i)=ReadString$(file)
				SaveSlotLevelName$(i)=ReadString$(file)
				screenshotnumber=ReadInt(file)
				CloseFile file
				
				SaveSlotTexture(i)=LoadTexture(GlobalDirName$+"\Player Profiles\"+playername$+"\SaveFiles\"+Str$(i)+"\"+screenshotnumber+".bmp")
				If SaveSlotTexture(i)>0
					EntityTexture SaveSlotEntity(i),SaveSlotTexture(i)
					ShowEntity SaveSlotEntity(i)
				EndIf
				
			EndIf
		Next
		
	Else If menu=6
		MenuActive(menu,0)=3
		MenuActive(menu,1)=3
		MenuActive(menu,2)=3
		MenuActive(menu,3)=3
		MenuText$(menu,1)=GameNameToBeSaved$
		
	Else If menu=8
		; Floingint - replay old adventures
		currentmenuNofItems=8
		CurrentReplayAdventure=1
		Select WAEpisode
		Case 0
			CurrentReplayAdventureName$="First Adventure"
		Case 1
			CurrentReplayAdventureName$="Stinker Rescue"
		Case 2
			CurrentReplayAdventureName$="Stalagmite Cavern"
		Case 3
			CurrentReplayAdventureName$="Bridge Control Alpha"
		End Select
		

	Else If menu=10
	
		; title intro
		PositionEntity camera,0,0,0
		RotateEntity camera,0,0,0
		RotateEntity levellight,-90,0,0
		TurnEntity levellight,0,-45,0
		AmbientLight 0,0,0
		LightColor levellight,0,0,0
		
				
		ShowEntity titlemenuentity(60)

		HideEntity LevelCursor
		HideEntity MouseCursor
		
	
	
		For i=0 To 9
			MenuActive(menu,i)=0
		Next
		
	;	titlemenuentity(4)=LoadAnimMesh("data/models/stinker/body.b3d")

	;	ScaleEntity titlemenuentity(4),.035,.035,.035
		;CreateStinkerModel(99,.0035,.0035,.0035,"!T001c001a")
	;	PositionEntity titlemenuentity(4),0,0,6.5
	;	EntityOrder titlemenuentity(4),-20
	;	EntityFX titlemenuentity(4),1

		
	

	Else If menu=11
		CameraClsColor camera,0,0,0
		
		ShowEntity titlemenuentity(60)

		; main menu
		
		menuactive(menu,0)=3
		
		
		file=WriteFile(globaldirname$+"\global.wdf")
		WriteString file,PlayerName$
		WriteString file,PlayerCharacterName$
		WriteInt file,GlobalSoundVolume2
		WriteInt file,GlobalMusicVolume2
		WriteInt file,KeyBoardMode
		WriteInt file,DialogContrast
		CloseFile file
		

		
		; are there saved games?
		
		menuactive(menu,5)=1
		
		For i=1 To 9
		
			If FileType (GlobalDirName$+"\Player Profiles\"+playername$+"\SaveFiles\"+Str$(i)+"\wla")=1
				
				menuactive(menu,5)=2
			EndIf
		Next
		
		If Fullversion=False Or PortalVersion=True
			menutext$(11,7)="Exit Game"
			menutext$(11,8)=""
		EndIf

			
			
	Else If menu=12
	
		
		
		
		
		If playername$<>"" 
			OldPlayerName$=PlayerName$
			OldPlayerCharacterName$=PlayerCharacterName$
			PlayerName$=""
			PlayerCharacterName$=""
		EndIf

	
		; select player profile
		MenuActive(menu,0)=3
	
		For i=1 To 8
			menutext$(menu,i)=""
		Next
		; check what profiles exist
		dirfile=ReadDir(GlobalDirName$+"\Player Profiles")
	
		i=1
		ex$=NextFile$(dirfile)
		Repeat
			If FileType(GlobalDirName$+"\Player Profiles\"+ex$)=2 And ex$<>"." And ex$<>".."
				menutext$(menu,i)=ex$;Upper$(Left$(ex$,1))+Lower$(Right$(ex$,Len(ex$)-1))
				i=i+1
			EndIf
			ex$=NextFile$(dirfile)
		Until ex$="" Or i=9
		CloseDir dirfile
		
		If i=9
			menutext$(menu,9)=""
			menuactive(menu,9)=3
		Else
			menutext$(menu,9)="Create New Profile"
			menuactive(menu,9)=2
		EndIf
		
		If i=1 ; no players yet
			startmenu(13)
;			menutext$(menu,i)="Default"
		EndIf
		
		
			
		
		
	Else If menu=13
		; enter new profile name
		
		menuactive(menu,0)=3
		menuactive(menu,2)=3
		menutext$(menu,2)="_"
	Else If menu=14
		; select a character
		menuactive(menu,0)=3
		menuactive(menu,9)=0
		For i=5 To 9
			titlemenuentity(i)=CopyEntity(stinkermesh)
			
			If i=9
;				EntityColor GetChild(titlemenuentity(i),3),10,10,10
				EntityTexture GetChild(titlemenuentity(i),3),stinkertexture(1,0)
			Else
				EntityTexture GetChild(titlemenuentity(i),3),stinkertexture(i-4,0)
			EndIf
			
		Next
		; stinky
		PositionEntity titlemenuentity(5),-1,0.2,7
		ScaleEntity titlemenuentity(5),.035,.035,.035
		PointEntity titlemenuentity(5),camera
		TurnEntity titlemenuentity(5),-20,180,0
		titlemenuentity(10)=myLoadMesh("data/models/stinker/accessory001.3ds",0)
		titlemenuTexture(10)=myLoadTexture("data/models/stinker/accessory001a.jpg",4)
		EntityTexture titlemenuentity(10),titlemenutexture(10)
		ScaleEntity titlemenuentity(10),.035,.035,.035
		Animate GetChild(titlemenuentity(5),3),1,.05,10

		
		; loof
		PositionEntity titlemenuentity(6),1,0.2,7
		ScaleEntity titlemenuentity(6),.027,.027,.027
		PointEntity titlemenuentity(6),camera
		TurnEntity titlemenuentity(6),-20,180,0
		titlemenuentity(11)=myLoadMesh("data/models/stinker/accessory001.3ds",0)
		titlemenuTexture(11)=myLoadTexture("data/models/stinker/accessory001b.jpg",4)
		EntityTexture titlemenuentity(11),titlemenutexture(11)
		ScaleEntity titlemenuentity(11),.027,.027,.027
		Animate GetChild(titlemenuentity(6),3),1,.12,10


		
		; qookie
		PositionEntity titlemenuentity(7),-2,-1.4,7
		ScaleEntity titlemenuentity(7),.038,.038,.034
		PointEntity titlemenuentity(7),camera
		TurnEntity titlemenuentity(7),-25,180,0
		titlemenuentity(12)=myLoadMesh("data/models/stinker/accessory001.3ds",0)
		titlemenuTexture(12)=myLoadTexture("data/models/stinker/accessory001c.jpg",4)
		EntityTexture titlemenuentity(12),titlemenutexture(12)
		ScaleEntity titlemenuentity(12),.038,.034,.038
		Animate GetChild(titlemenuentity(7),3),1,.08,10
		EntityPickMode titlemenuentity(7),3


		
		; peegue
		PositionEntity titlemenuentity(8),0,-1.4,7
		ScaleEntity titlemenuentity(8),.035,.035,.035
		PointEntity titlemenuentity(8),camera
		TurnEntity titlemenuentity(8),-20,180,0
		titlemenuentity(13)=myLoadMesh("data/models/stinker/accessory002.3ds",0)
		titlemenuTexture(13)=myLoadTexture("data/models/stinker/accessory002a.jpg",4)
		EntityTexture titlemenuentity(13),titlemenutexture(13)
		ScaleEntity titlemenuentity(13),.035,.035,.035
		Animate GetChild(titlemenuentity(8),3),1,.1,10


		
		; custom
		PositionEntity titlemenuentity(9),2,-1.4,7
		ScaleEntity titlemenuentity(9),.035,.035,.035
		PointEntity titlemenuentity(9),camera
		TurnEntity titlemenuentity(9),-20,180,0
		Animate GetChild(titlemenuentity(9),3),1,.02,10

		titlemenupointat=0

			
	Else If menu=15
		; create custom stinker
		menuactive(15,0)=3
		
		titlemenuentity(5)=CopyEntity(stinkermesh)
		EntityTexture GetChild(titlemenuentity(5),3),stinkertexture(1,0)
		titlemenuentity(6)=0
		titlemenuentity(7)=0
			
		PlayerCharacterName$=""
		PlayerTextureBody=1
		PlayerAcc1=0
		PlayerTexAcc1=0
		PlayerAcc2=0
		PlayerTexAcc2=0
		PlayerSizeX#=0.035
		PlayerSizeY#=0.035
		PlayerSizeZ#=0.035
		PlayerVoice=1

		
		
		PositionEntity titlemenuentity(5),-1,-.2,5
		Animate GetChild(titlemenuentity(5),3),1,.02,10
		
		PointEntity titlemenuentity(5),camera
		TurnEntity titlemenuentity(5),-45,180,0
	Else If menu=17
		; replace existing profile
		For i=0 To 5
			menuactive(menu,i)=3
		Next
	Else If menu=18
		; options
		menuactive(menu,0)=3
		; control
		If KeyBoardMode=1
			MenuText$(18,3)="Mouse Control"
		Else
			MenuText$(18,3)="Mouse/Keyboard"
		EndIf
		; volume
		MenuText$(18,4)="Sound Volume: "+GlobalSoundVolume2
		MenuText$(18,5)="Music Volume: "+GlobalMusicVolume2
		Select DialogContrast
		Case 0
			MenuText$(18,6)="Dialog: Normal"
		Case 1
			MenuText$(18,6)="Dialog: High Contrast"
		Case 2
			MenuText$(18,6)="Dialog: Black"
		Case 3
			MenuText$(18,6)="Dialog: None"
		

		End Select
		
	Else If menu=19
		menuactive(menu,0)=3
		menuactive(menu,1)=3
		menuactive(menu,2)=3
		
	Else If menu=20
		menutext$(20,0)="Delete: "+Profile2BDeleted$
		MenuActive(menu,0)=3
		MenuActive(menu,1)=3
		MenuActive(menu,2)=3
		

		
	Else If menu=21 Or menu=22 Or menu=23
		HideEntity MouseCursor
		HideEntity LevelCursor
		
	Else If menu=24
		; Floingint - replay old adventures
		currentmenuNofItems=8
		CurrentReplayAdventure=1
		Select WAEpisode
		Case 0
			; custom hub - get the first adventure name
			CurrentReplayAdventure=0
			Repeat
				CurrentReplayAdventure=CurrentReplayAdventure+1
			Until AdventureCompleted(CurrentReplayAdventure)=1 And FileType(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")=1
			file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
			CurrentReplayAdventureName$=ReadString$(file)
			CloseFile file
		Case 1
			CurrentReplayAdventureName$="Stinker Rescue"
		Case 2
			CurrentReplayAdventureName$="Stalagmite Cavern"
		Case 3
			CurrentReplayAdventureName$="Bridge Control Alpha"
		End Select

		
	Else If menu=25
		; end of demo
		
		traileralreadyplayed=True
		For i=5 To 8
			TitlemenuTexture(i)=MyLoadTexture("data\graphics\demo"+Str$(i-4)+".jpg",1)
			TitlemenuEntity(i)=CreateCube(camera)
			ScaleEntity TitlemenuEntity(i),1,1,0.01
			EntityTexture TitlemenuEntity(i),TitlemenuTexture(i)
		Next
		
		menutimer=0
	

		
	Else If Menu=26
		; credits
		MenuActive(menu,0)=3
		MenuActive(menu,1)=3
		credittimer=0

	
		
	
	
	EndIf
	
	If GameMode=5 Or GameMode=6
		GameMode=0
		CloseRuckSack(8)
	EndIf

	OldGameMode=GameMode
	GameMode=12

	IconSize(9)=1
	DialogTimer=0
	IconHelpText$(9)="Close"
	
	extra#=0
	If Widescreen And (menu=4 Or Menu=5) ;we need a wider background for save load menus
		extra=0.8
	EndIf
	surface=GetSurface(DialogBackGroundEntity,1)
	VertexCoords surface,0,-2.2-extra,.2+0.16*CurrentMenuNofItems,5
	VertexCoords surface,1,2.2+extra,.2+0.16*CurrentMenuNofItems,5
	VertexCoords surface,2,-2.2-extra,-0.16*CurrentMenuNofItems,5
	VertexCoords surface,3,2.2+extra,-0.16*CurrentMenuNofItems,5
	
	
	If menu<10 Or menu=24
	
		; General In-Game Menus
		ShowEntity DialogBackgroundEntity
		
		
	Else If menu<>21 And menu<>22 And menu<>23
	
		; General Out-Game Menus
		
		; Show Stars
		HideEntity DialogBackgroundEntity
		For i=78 To 99
			ShowEntity TitleMenuEntity(i)
		Next
		
		; Main Music
		If currentmusic<>8
			If globalmusicvolume2>0
				If ChannelPlaying(musicchannel)
					StopChannel musicchannel
				EndIf
				MusicChannel=PlayMusic ("Data\music\8.ogg")
				ChannelVolume MusicChannel,GlobalMusicVolume
				currentmusic=8
				LevelMusicCustomVolume=100.0
				LevelMusicCustomPitch=44

			Else 
				currentmusic=-99 ; remember that e.g. -1 is soundscape beach
			EndIf

			
		EndIf
	
		; Camera/Lights
		PositionEntity camera,0,0,0
		RotateEntity camera,0,0,0
		AmbientLight 255,255,255
		RotateEntity levellight,35,-35,0
		CameraFogMode camera,0
		CameraRange camera, 0.1,1000
				
		; no icons		
		For i=0 To 79
			deleteicon(i)
		Next
		
		
	EndIf

	DialogBackGroundSize=70
	
	
	
End Function

Function UpdateMenu()

	For i=7 To 8
		If IconSize(i)>0 And ((IconSize(i) Mod 2)=1 Or IconSize(i)=1000) IconSize(i)=1
	Next

	If CurrentMenu<100 Or CurrentMenu=115
		UpdateOldMenu()
	Else
		Select CurrentMenu
		Case 100
			CustomSelectMenu()
		Case 101
			CustomDeleteMenu()
		Case 102
			WaEditorTitleMenu()
		Case 103
			CustomArchiveMenu()
			
		Case 120
			CustomHubSelectMenu()

			
	
		End Select
	EndIf
		
End Function
Function UpdateOldMenu()

	; general for all menus
	UpdateWorld

	Mouse1=False
	Mouse2=False
	If MouseDown(1)=True Or MouseHit(1)>0
		Mouse1=True
	EndIf
	If MouseDown(2)=True Or MouseHit(2)>0
		Mouse2=True
	EndIf

;	DisplayText(MouseX()+" "+MouseY(),0,0,1,1,255,255,255)

	If currentmenu<>10 And currentmenu<>21 And currentmenu<>22 And currentmenu<>23
		ShowEntity MouseCursor
		MouseCursorVisible=True
	EndIf
	
	; general out-of-game menus
	If CurrentMenu>=10 And CurrentMenu<>22 And currentmenu<>23
;		PositionTexture TitleMenuTexture(80),-(menutimer Mod 2500)/2500.0,(menutimer Mod 5000)/5000.0


		If WAEpisode=0
			;PositionEntity TitleMenuEntity(79),30+2*Sin((menutimer/3) Mod 360),-10,10+6*Cos((menutimer/10) Mod 360)
			EntityAlpha Titlemenuentity(1),1
			EntityAlpha Titlemenuentity(2),1

			TurnEntity Titlemenuentity(1),0,0.04,0
			TurnEntity Titlemenuentity(79),0,0,0.006

			
			ShowEntity Titlemenuentity(1)
			ShowEntity Titlemenuentity(2)
			
			If CurrentMenu<>11
				HideEntity Titlemenuentity(1)
				HideEntity Titlemenuentity(2)
			EndIf
			

			
			For i=81 To 99
				; move stars
				TranslateEntity TitleMenuEntity(i),-(Float(i)/10000.0),-(Float(i*2)/10000.0),0
		;		If Rand(0,10)=0 AddParticle(Rand(0,1),EntityX(TitleMenuEntity(i)),EntityY(TitleMenuEntity(i)),EntityZ(TitleMenuEntity(i)),0,0.01,0,0,0,0,0.003,0,0,0,500,3)
	
				If  EntityY(TitleMenuEntity(i))<-12
					FreeEntity TitleMenuEntity(i)
					
					titlemenuentity(i)=CopyEntity(starmesh)
					
					RotateEntity Titlemenuentity(i),-90,0,0
					
					
					sc#=Rnd(0.8,1)
					ScaleEntity titlemenuentity(i),sc,sc,sc
					EntityFX titlemenuentity(i),1
					PositionEntity titlemenuentity(i),Rnd(-60,90),65,145
	
	
		
				EndIf
				TurnEntity TitleMenuEntity(i),0,.5,0
			Next
			
			RotateEntity TitleMenuEntity(60),-90,0,0
			
			TurnEntity TitleMenuEntity(60),-0,5*Sin(menutimer Mod 360),0
		EndIf

		If WAEpisode=2
			PositionEntity TitleMenuEntity(79),30+2*Sin((menutimer/3) Mod 360),-10,10+6*Cos((menutimer/10) Mod 360)
	
			For i=81 To 99
				; move stars
				TranslateEntity TitleMenuEntity(i),-(Float(i)/10000.0),-(Float(i*2)/10000.0),0
		;		If Rand(0,10)=0 AddParticle(Rand(0,1),EntityX(TitleMenuEntity(i)),EntityY(TitleMenuEntity(i)),EntityZ(TitleMenuEntity(i)),0,0.01,0,0,0,0,0.003,0,0,0,500,3)
	
				If  EntityY(TitleMenuEntity(i))<-12
					FreeEntity TitleMenuEntity(i)
					
					titlemenuentity(i)=CopyEntity(starmesh)
					
					RotateEntity Titlemenuentity(i),-90,0,0
					
					
					sc#=Rnd(0.8,1)
					ScaleEntity titlemenuentity(i),sc,sc,sc
					EntityFX titlemenuentity(i),1
					PositionEntity titlemenuentity(i),Rnd(-60,90),65,145
	
	
		
				EndIf
				TurnEntity TitleMenuEntity(i),0,.5,0
			Next
			
			RotateEntity TitleMenuEntity(60),-90,0,0
			
			TurnEntity TitleMenuEntity(60),-0,5*Sin(menutimer Mod 360),0
		EndIf
		If WAEpisode=3
			;PositionEntity TitleMenuEntity(79),30+2*Sin((menutimer/3) Mod 360),-10,10+6*Cos((menutimer/10) Mod 360)
			PositionTexture titlemenutexture(79),0,-Float(menutimer Mod 3600)/3600.0
			For i=81 To 99
				; move stars
				TranslateEntity TitleMenuEntity(i),0,0,-(Float(i)/200.0)
		;		If Rand(0,10)=0 AddParticle(Rand(0,1),EntityX(TitleMenuEntity(i)),EntityY(TitleMenuEntity(i)),EntityZ(TitleMenuEntity(i)),0,0.01,0,0,0,0,0.003,0,0,0,500,3)
	
				If  EntityZ(TitleMenuEntity(i))<1 Or EntityZ(titlemenuentity(i))>170
					EntityAlpha titlemenuentity(i),0
				Else If currentmenu=11
					EntityAlpha titlemenuentity(i),1
				EndIf
				If  EntityZ(TitleMenuEntity(i))<.1
				

			;		FreeEntity TitleMenuEntity(i)
					
			;		titlemenuentity(i)=CopyEntity(starmesh)
					
			;		RotateEntity Titlemenuentity(i),-90,0,0
					
					
			;		sc#=Rnd(0.8,1)
			;		ScaleEntity titlemenuentity(i),sc,sc,sc
			;		EntityFX titlemenuentity(i),1
					PositionEntity titlemenuentity(i),Rnd(-50,50),Rnd(-10,50),175
	
	
		
				EndIf
				TurnEntity TitleMenuEntity(i),0,2,0
			Next
			
			TurnEntity titlemenuentity(78),.1,-.15,.02
			
			RotateEntity TitleMenuEntity(60),-90,0,0
			
			TurnEntity TitleMenuEntity(60),-0,5*Sin(menutimer Mod 360),0

		
		EndIf
	;	controlparticles()
	;	renderparticles()
	EndIf
		
	

	; special cases
	
	

	
	
	If CurrentMenu=4
		; save game
		flag=False
		HideEntity SaveSlotEntity(0)
		For i=1 To 9
			If MouseX()>i*75*GfxWidth/800.0 And MouseX()<(i*75+50)*GfxWidth/800.0 And MouseY()>150*GfxHeight/600.0 And MouseY()<200*GfxHeight/600.0
				ScaleEntity SaveSlotEntity(i),.7,.7,.7
				
				If SaveSlotDateTime$(i)<>""
					ShowEntity SaveSlotEntity(0)
					EntityTexture SaveSlotEntity(0),SaveSlotTexture(i)
										
					DisplayText(SaveSlotLevelName$(i),25-0.5*Len(SaveSlotLevelName$(i)),16.5,1,1,255,255,255)
					DisplayText(SaveSlotDateTime$(i),25-0.5*Len(SaveSlotDateTime$(i)),17.5,1,1,255,255,255)
				EndIf
				
			Else
				ScaleEntity SaveSLotEntity(i),.5,.5,.5
			EndIf
			If SaveSlotDateTime$(i)<>"" Then flag=True
		Next
		If flag=False
			; all save slots free 
			b$="Select one of the nine 'Save Slots' above."
			DisplayText(b$,25-0.5*Len(b$),12,1,1,200,200,200)
			b$="Once saved, you can resume your game from"
			DisplayText(b$,25-0.5*Len(b$),13,1,1,200,200,200)
			b$="this position using the Load Game function."
			DisplayText(b$,25-0.5*Len(b$),14,1,1,200,200,200)
		EndIf
	Else If CurrentMenu=5 Or currentmenu=16
		; load game
		flag=False
		HideEntity SaveSlotEntity(0)
		For i=1 To 9
			If MouseX()>i*75*GfxWidth/800.0 And MouseX()<(i*75+50)*GfxWidth/800.0 And MouseY()>150*GfxHeight/600.0 And MouseY()<200*GfxHeight/600.0
					
				If SaveSlotDateTime$(i)<>""
					ScaleEntity SaveSlotEntity(i),.7,.7,.7
					ShowEntity SaveSlotEntity(0)
					EntityTexture SaveSlotEntity(0),SaveSlotTexture(i)
										
					DisplayText(SaveSlotLevelName$(i),25-0.5*Len(SaveSlotLevelName$(i)),16.5,1,1,255,255,255)
					DisplayText(SaveSlotDateTime$(i),25-0.5*Len(SaveSlotDateTime$(i)),17.5,1,1,255,255,255)
				EndIf
				
			Else
				ScaleEntity SaveSLotEntity(i),.5,.5,.5
			EndIf
			If SaveSlotDateTime$(i)<>"" Then flag=True
		Next
		If flag=False
			; all save slots free 
			b$="You have no Saved Games available."
			DisplayText(b$,25-0.5*Len(b$),12,1,1,200,200,200)
			b$="You need to use the Save Game function"
			DisplayText(b$,25-0.5*Len(b$),13,1,1,200,200,200)
			b$="to be able to resume from a saved spot."
			DisplayText(b$,25-0.5*Len(b$),14,1,1,200,200,200)
		EndIf
		
	Else If currentmenu=8
		; Floinging
		ex$="Select Adventure To Replay:"
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,7,1,1,255,255,0)
		ex$=CurrentReplayAdventureName
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,9,1,1,255,255,255)
		
		ex$="Coins: "+adventurecompletedcoins(currentreplayadventure)+" of "+adventurecompletedcoinstotal(currentreplayadventure)
		ex$=ex$+", Gems: "+adventurecompletedgems(currentreplayadventure)+" of "+adventurecompletedgemstotal(currentreplayadventure)
		ex$=ex$+", Score: "+adventurecompletedscore(currentreplayadventure)
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,10,1,1,255,255,255)
		ex$="<Back 10>   <Back>   <PLAY>   <Next>   <Next 10>"
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,12,1,1,255,255,0)
		ex$="<CANCEL>"
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,14,1,1,255,255,0)

		If MouseGameMode=-1 And Mouse1=True
			
			MouseGameMode=-2
			If Float(MouseX())/Float(GfxWidth)>0.42 And Float(MouseX())/Float(GfxWidth)<0.58 And Float(MouseY())/Float(GfxHeight)>0.59 And Float(MouseY())/Float(GfxHeight)<0.65
				; cancel
				currentreplayadventure=0
				endmenu()
				PlaysoundfxNow(130)
			EndIf
	
			If Float(MouseX())/Float(GfxWidth)>0.62 And Float(MouseX())/Float(GfxWidth)<0.75 And Float(MouseY())/Float(GfxHeight)>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; forward
				Repeat
					CurrentReplayAdventure=CurrentReplayAdventure+1
					If CurrentReplayAdventure=500 Then CurrentReplayAdventure=1
				Until AdventureCompleted(CurrentReplayAdventure)=1
			
				; get the name
				If incustomhub=1
					file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
				Else
					file=ReadFile("data\adventures\adventure"+currentreplayadventure+"\master.dat")
				EndIf
				currentreplayadventurename$=ReadString$(file)
				CloseFile file
			EndIf
			If Float(MouseX())/Float(GfxWidth)>0.80 And Float(MouseY())/600.0>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; forward ten
				For j=1 To 10
					Repeat
						CurrentReplayAdventure=CurrentReplayAdventure+1
						If CurrentReplayAdventure=500 Then CurrentReplayAdventure=1
					Until AdventureCompleted(CurrentReplayAdventure)=1
				Next
			
				; get the name
				If incustomhub=1
					file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
				Else
					file=ReadFile("data\adventures\adventure"+currentreplayadventure+"\master.dat")
				EndIf

				currentreplayadventurename$=ReadString$(file)
				CloseFile file
			EndIf
			
			If Float(MouseX())/Float(GfxWidth)>0.25 And Float(MouseX())/Float(GfxWidth)<0.38 And Float(MouseY())/Float(GfxHeight)>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; back
				Repeat
					CurrentReplayAdventure=CurrentReplayAdventure-1
					If CurrentReplayAdventure=0 Then CurrentReplayAdventure=499
				Until AdventureCompleted(CurrentReplayAdventure)=1
			
				; get the name
				If incustomhub=1
					file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
				Else
					file=ReadFile("data\adventures\adventure"+currentreplayadventure+"\master.dat")
				EndIf

				currentreplayadventurename$=ReadString$(file)
				CloseFile file
			EndIf
			If Float(MouseX())/Float(GfxWidth)<0.2 And Float(MouseY())/Float(GfxHeight)>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; back ten
				For j=1 To 10
					Repeat
						CurrentReplayAdventure=CurrentReplayAdventure-1
						If CurrentReplayAdventure=0 Then CurrentReplayAdventure=499
					Until AdventureCompleted(CurrentReplayAdventure)=1
				Next
			
				; get the name
				If incustomhub=1
					file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
				Else
					file=ReadFile("data\adventures\adventure"+currentreplayadventure+"\master.dat")
				EndIf

				currentreplayadventurename$=ReadString$(file)
				CloseFile file
			EndIf
			
			If Float(MouseX())/Float(GfxWidth)>0.44 And Float(MouseX())/Float(GfxWidth)<0.56 And Float(MouseY())/Float(GfxHeight)>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; PLAY
				endmenu()
				AdventureCurrentStatus=3
				activatecommand(8,currentreplayadventure,0,0,0)
				playsoundfxnow(42)

			EndIf




			
		EndIf
		
		
	Else If currentmenu=10
		;title animation
		HideEntity MouseCursor
		MouseCursorVisible=False
		HideEntity LevelCursor
		HideEntity DialogBackGroundEntity
		
		
		DialogTimer=DialogTimer+1
		
		UpdateTitleMenu()

		
		If mouse1=True Or mouse2=True Or escpressed=True
			
			MouseGameMode=-2
			
			EntityAlpha TitleMenuEntity(1),0
			EntityAlpha TitleMenuEntity(2),0
			
			If Playername$="" Or FileType(globaldirname$+"\player profiles\"+playername$+"\current\playerfile.wpf")<>1
				startmenu(12)
			Else
				startmenu(11)
			EndIf
			
			
	
			

		

	
			
			CameraFogRange camera,.1,1000
			
		EndIf
		
		
	
	Else If currentmenu=11
		;ex$="Beta2"+Lockertext$(0)
		;displaytext(ex$,(50.0*1.33-Len(ex$))/2.0,0,.75,1,255,255,0)

		ex$="Player: "+PlayerName$
		displaytext(ex$,(50.0*1.33-Len(ex$))/2.0,21.5*1.33,.75,1,255,255,0)
		ex$="(click to change)"
		displaytext(ex$,(50.0*1.33-Len(ex$))/2.0,22.5*1.33,.75,1,255,255,0)

	
		If (Mouse1=True And MouseGameMode=-1) And MouseY()>GfxHeight*0.9
			MouseGameMode=-2
			Endmenu()
			Startmenu(12)
		EndIf
		If widescreen
			displaytext(VersionText$,-12,38,0.6,1,255,255,0)
		Else
			displaytext(VersionText$,0,38,0.6,1,255,255,0)
		EndIf
		If fullversion=False
			displaytext("      Demo",0,38,0.6,1,255,255,0)
		EndIf

	Else If currentmenu=12
	
		If menutext$(12,2)<>""
			displaytext("To Delete a Profile (other than Default):",4.5,22,1,1,200,200,0)
			displaytext("Highlight with Mouse,then press DELETE key.",3.5,23,1,1,200,200,0)
		EndIf
		
			

	
	Else If currentmenu=13
	
		If menutimer Mod 60<30
			menutext$(13,2)=Left$(menutext$(13,2),Len(menutext$(13,2))-1)+" "
		Else
			menutext$(13,2)=Left$(menutext$(13,2),Len(menutext$(13,2))-1)+"_"
		EndIf
		
		key=GetKey()
		
		If key=13
			playername$=Left$(menutext$(13,2),Len(menutext$(13,2))-1)
			; delete leading spaces
			If Left$(playername$,1)=" "
				Repeat
					playername$=Right$(playername$,Len(playername$)-1)
				Until Left$(playername$,1)<>" "
			EndIf

			
			If playername$<>""
				; ok
				If FileType(GlobalDirName$+"\Player Profiles\"+playername$)=2
					endmenu()
					;PlaysoundfxNow(135)

					startmenu(17)
				Else
					endmenu()
					PlaysoundfxNow(131)

					startmenu(14)
				EndIf
			EndIf

		Else If key=8
			;menutext$(13,2)="_"
			If Len(menutext$(13,2))>2
				menutext$(13,2)=Left$(menutext$(13,2),Len(menutext$(13,2))-2)+"_"
			Else
				menutext$(13,2)="_"
			EndIf
		Else If ((key>=65 And key<90) Or (key>=97 And key<=122) Or (key>=48 And key<=57) Or key=32) And Len(menutext$(13,2))<21
			;If menutext$(13,2)="PLAYER1_" Or menutext$(13,2)="PLAYER1 "
			;	menutext$(13,2)=Chr$(key)+"_"
			;	
			;Else
			menutext$(13,2)=Left$(menutext$(13,2),Len(menutext$(13,2))-1)+Chr$(key)+"_"
			;EndIf
		EndIf
	Else If currentmenu=14
		; select character
		For i=5 To 8
			bone=FindChild(titlemenuentity(i),"hat_bone")
			PositionEntity titlemenuentity(i+5),EntityX(bone,True),EntityY(bone,True),EntityZ(bone,True)
			RotateEntity titlemenuentity(i+5),EntityPitch(bone,True),EntityYaw(bone,True),EntityRoll(bone,True)
		Next
		mx=MouseX()
		my=MouseY()
		
		If (mx-GfxWidth*287/800)^2+(my-GfxHeight*224/600)^2<((75*GfxWidth)/800)^2
			picked=1
		EndIf
		If (mx-GfxWidth*519/800)^2+(my-GfxHeight*229/600)^2<((75*GfxWidth)/800)^2
			picked=2
		EndIf
		If (mx-GfxWidth*173/800)^2+(my-GfxHeight*414/600)^2<((75*GfxWidth)/800)^2
			picked=3
		EndIf
		If (mx-GfxWidth*408/800)^2+(my-GfxHeight*414/600)^2<((75*GfxWidth)/800)^2
			picked=4
		EndIf
		If (mx-GfxWidth*631/800)^2+(my-GfxHeight*414/600)^2<((75*GfxWidth)/800)^2
			picked=5
		EndIf

		If picked=1 And titlemenupointat<>1
			Animate GetChild(titlemenuentity(5),3),2,.1,15
		EndIf
		If picked<>1 And titlemenupointat=1
			Animate GetChild(titlemenuentity(5),3),1,0.05,10
		EndIf
		If picked=2 And titlemenupointat<>2
			Animate GetChild(titlemenuentity(6),3),1,.6,11
		EndIf
		If picked<>2 And titlemenupointat=2
			Animate GetChild(titlemenuentity(6),3),1,0.12,10
		EndIf
		If picked=3 And titlemenupointat<>3
			Animate GetChild(titlemenuentity(7),3),1,.3,12
		EndIf
		If picked<>3 And titlemenupointat=3
			Animate GetChild(titlemenuentity(7),3),1,0.08,10
		EndIf
		If picked=4 And titlemenupointat<>4
			Animate GetChild(titlemenuentity(8),3),1,.4,9
		EndIf
		If picked<>4 And titlemenupointat=4
			Animate GetChild(titlemenuentity(8),3),1,0.1,10
		EndIf
		If picked=5 
			TurnEntity Titlemenuentity(9),0,2,0
		EndIf
		If picked<>5
			
		EndIf



		
		
		titlemenupointat=picked
		
		
		If Mouse1=True And MouseGameMode=-1 And picked>0
			mousegamemode=-2
			If picked>=1 And picked<=4
				endmenu()
				FreeSound soundfx(177)
				SoundFX(177)=myLoadSound("data\sound\voices\"+Str$(picked)+"\intro.wav")

				PlaysoundfxNow(132)
				
				timeflag=MilliSecs()
	;			
	;			ResetText("data/graphics/font.bmp")
	;			AmbientLight 255,255,255
	;			cube=CreateCube(camera)
	;			cubetex=myLoadTexture("load.jpg",1)
	;			EntityTexture cube,cubetex
	;			PositionEntity cube,0,0,10
	;			
	;			RenderWorld
	;			Text GfxWidth*0.5,Gfxheight*2/7,"...Creating Player...",True
	;			Text GfxWidth*0.5,Gfxheight*2/3,"...Please Wait...",True
	;			Flip
				
			EndIf

			Select picked
			Case 1
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
			;	endmenu()
				CreateNewPlayer()
				

				startmenu(11)
			Case 2
				PlayerCharacterName$="Loof"
				PlayerTextureBody=2
				PlayerAcc1=1
				PlayerTexAcc1=2
				PlayerAcc2=0
				PlayerTexAcc2=0
				PlayerSizeX#=0.029
				PlayerSizeY#=0.029
				PlayerSizeZ#=0.029
				PlayerVoice=2
			;	endmenu()
				CreateNewPlayer()
				

				startmenu(11)
			Case 3
				PlayerCharacterName$="Qookie"
				PlayerTextureBody=3
				PlayerAcc1=1
				PlayerTexAcc1=3
				PlayerAcc2=0
				PlayerTexAcc2=0
				PlayerSizeX#=0.038
				PlayerSizeY#=0.038
				PlayerSizeZ#=0.034
				PlayerVoice=3
			;	endmenu()
				CreateNewPlayer()
				

				startmenu(11)
			Case 4
				PlayerCharacterName$="Peegue"
				PlayerTextureBody=4
				PlayerAcc1=2
				PlayerTexAcc1=1
				PlayerAcc2=0
				PlayerTexAcc2=0
				PlayerSizeX#=0.035
				PlayerSizeY#=0.035
				PlayerSizeZ#=0.035
				PlayerVoice=4
			;	endmenu()
				CreateNewPlayer()
				
				startmenu(11)
			Case 5
				endmenu()
				startmenu(15)
			End Select
			If picked>=1 And picked<=4
				
	;			FreeTexture cubetex
	;			FreeEntity cube
				Repeat
				Until MilliSecs()-timeflag>600				

			EndIf
		EndIf
		
		If currentmenu=14 ; still
			DisplayText(mytext$,x#,y#,size#,spacing#,red,green,blue)
	
			Displaytext("Stinky",14.8,11.2,1.0,1.0,255,255,255)
			Displaytext("Loof",30.3,11.2,1.0,1.0,255,255,255)
			Displaytext("Qookie",7.5,18.6,1.0,1.0,255,255,255)
			Displaytext("Peegue",22,18.6,1.0,1.0,255,255,255)
			Displaytext("Custom",36.5,18.6,1.0,1.0,255,255,255)
		EndIf

	Else If currentmenu=15
	
		textline=-1
		If MouseX()>(400.0*GfxWidth)/800.0 And MouseX()<(720.0*gfxwidth)/800.0
			textline=Floor(((MouseY()-234.0*GfxHeight/600.0)/((151.0*GfxHeight/600.0)/6.0)))
		EndIf

		TurnEntity titlemenuentity(5),0,.5,0
		ScaleEntity titlemenuentity(5),PlayerSizeX,PlayerSizeZ,PlayerSizeY
		EntityTexture GetChild(titlemenuentity(5),3),Stinkertexture(PlayerTextureBody,0)
		For i=6 To 7
			If titlemenuentity(i)>0
				bone=FindChild(titlemenuentity(5),"hat_bone")
				PositionEntity titlemenuentity(i),EntityX(bone,True),EntityY(bone,True),EntityZ(bone,True)
				RotateEntity titlemenuentity(i),EntityPitch(bone,True),EntityYaw(bone,True),EntityRoll(bone,True)
				ScaleEntity titlemenuentity(i),PlayerSizeX,PlayerSizeZ,PlayerSizeY

			EndIf
		Next
		
		;If menutimer Mod 60 <30
			ex$="Customize:"
		;Else
		;	ex$="Name: "+PlayerCharacterName$+" "
		;EndIf
		
		DisplayText(ex$,(18+(30-Len(ex$))/2.0)/1.5,7/1.5,1.5,1,255,255,0)
		;isplayText("----------------",27,8,1,1,255,255,255)
		
		col=255
		If textline=0 Then col=0
		ex$="Hat"
		DisplayText(ex$,20+(30-Len(ex$))/2.0,9,1,1,255,255,col)
		col=255
		If textline=1 Then col=0
		ex$="Eyes/Shoes"
		DisplayText(ex$,20+(30-Len(ex$))/2.0,10,1,1,255,255,col)
		col=255
		If textline=2 Then col=0
		ex$="Glasses"
		DisplayText(ex$,20+(30-Len(ex$))/2.0,11,1,1,255,255,col)
		col=255
		If textline=3 Then col=0
		If playersizez<0.03
			ex$="Height: Tiny"
		Else If playersizez<0.033
			ex$="Height: Small"
		Else If playersizez<0.036
			ex$="Height: Medium"
		Else If playersizez<0.039
			ex$="Height: Large"
		Else 
			ex$="Height: Huge"
		EndIf

		DisplayText(ex$,20+(30-Len(ex$))/2.0,12,1,1,255,255,col)
		col=255
		If textline=4 Then col=0
		If playersizex<0.03
			ex$="Width: Tiny"
		Else If playersizex<0.033
			ex$="Width: Small"
		Else If playersizex<0.036
			ex$="Width: Medium"
		Else If playersizex<0.039
			ex$="Width: Large"
		Else 
			ex$="Width: Huge"
		EndIf

		DisplayText(ex$,20+(30-Len(ex$))/2.0,13,1,1,255,255,col)
		col=255
		If textline=5 Then col=0
		
		Select playervoice
		Case 1
			ex$="Voice: Stinky"
		Case 2
			ex$="Voice: Loof"
		Case 3
			ex$="Voice: Qookie"
		Case 4
			ex$="Voice: Peegue"
		End Select
		DisplayText(ex$,20+(30-Len(ex$))/2.0,14,1,1,255,255,col)
		col=255
		If textline=6 Then col=0
		
		col=255
		ex$="(click any above to change)"
		DisplayText(ex$,20+(30-Len(ex$))/2.0,15.5,1,1,255,255,255)
		
		

		key=GetKey()
		
		

		If key=8
			If Len(playercharactername$)>1 
				playercharactername$=Left$(playercharactername$,Len(playercharactername$)-1)
				
			Else
				playercharactername$=""
			EndIf
		Else If ((key>=65 And key<90) Or (key>=97 And key<=122) Or (key>=48 And key<=57) Or key=32) And Len(playercharactername$)<12
			;If menutext$(13,2)="PLAYER1_" Or menutext$(13,2)="PLAYER1 "
			;	menutext$(13,2)=Chr$(key)+"_"
			;	
			;Else
			playercharactername$=playercharactername$+Chr$(key)
			;EndIf
		EndIf
		
		If (mouse1=True Or mouse2=True) And mousegamemode=-1 And textline>=0 And textline<=6

			mousegamemode=-2
			
						
			Select textline
			Case 0
				PlaysoundfxNow(130)

				If mouse1=True
					playertexacc1=playertexacc1+1
				Else
					playertexacc1=playertexacc1-1
				EndIf
				If playeracc1>9 
					ex$=""
				Else
					ex$="0"
				EndIf
				If myFileType("data\models\stinker/accessory0"+ex$+Str$(playeracc1)+Chr$(64+playertexacc1)+".jpg")=0
					If titlemenuentity(6)>0
						FreeEntity titlemenuentity(6)
						FreeTexture titlemenutexture(6)
						titlemenuentity(6)=0
						titlemenutexture(6)=0
					EndIf
					playertexacc1=1
					If mouse1=True
						playeracc1=playeracc1+1
					Else
						playeracc1=playeracc1-1
						playertexacc1=9
						Repeat
							playertexacc1=playertexacc1-1
						Until myFileType("data\models\stinker/accessory0"+ex$+Str$(playeracc1)+Chr$(64+playertexacc1)+".jpg")=1 Or playertexacc1=1
					EndIf
					If playeracc1>9 
						ex$=""
					Else
						ex$="0"
					EndIf

					If playeracc1=56 
						playeracc1=0
					
					Else If playeracc1=0
					
					Else
						If playeracc1=-1 
							playeracc1=55
							ex$=""
						EndIf
						titlemenuentity(6)=myLoadMesh("data\models\stinker/accessory0"+ex$+Str$(playeracc1)+".3ds",0)
						titlemenutexture(6)=myLoadTexture("data\models\stinker/accessory0"+ex$+Str$(playeracc1)+Chr$(64+playertexacc1)+".jpg",4)
						EntityTexture titlemenuentity(6),titlemenutexture(6)
						ScaleEntity titlemenuentity(6),.01,.01,.01
					EndIf
				Else
					FreeTexture titlemenutexture(6)
					titlemenutexture(6)=myLoadTexture("data\models\stinker/accessory0"+ex$+Str$(playeracc1)+Chr$(64+playertexacc1)+".jpg",4)
					EntityTexture titlemenuentity(6),titlemenutexture(6)

		
				EndIf
					
			
			Case 1
				PlaysoundfxNow(130)

				PlayerTextureBody=PlayerTextureBody+1
				If PlayerTextureBody=5 Then PlayerTextureBody=1
				
			Case 2
				PlaysoundfxNow(130)
				
				If mouse1=True
					playertexacc2=playertexacc2+1
				Else
					playertexacc2=playertexacc2-1
				EndIf

			
				If myFileType("data\models\stinker/accessory"+Str$(playeracc2)+Chr$(64+playertexacc2)+".jpg")=0
					If titlemenuentity(7)>0
						FreeEntity titlemenuentity(7)
						FreeTexture titlemenutexture(7)
						titlemenuentity(7)=0
						titlemenutexture(7)=0
					EndIf
					playertexacc2=1
					If mouse1=True
						playeracc2=playeracc2+1
					Else
						playeracc2=playeracc2-1
						playertexacc2=9
						Repeat
							playertexacc2=playertexacc2-1
						Until playertexacc2=1 Or myFileType("data\models\stinker/accessory"+Str$(playeracc2)+Chr$(64+playertexacc2)+".jpg")=1
					
					EndIf
					
					If playeracc2=1 Then playeracc2=101
					If playeracc2=100 Then playeracc2=0
					

					If playeracc2=117
						playeracc2=0
					Else If playeracc2=0
					
					Else
						If playeracc2=-1
							playeracc2=116
						EndIf
						titlemenuentity(7)=myLoadMesh("data\models\stinker/accessory"+Str$(playeracc2)+".3ds",0)
						titlemenutexture(7)=myLoadTexture("data\models\stinker/accessory"+Str$(playeracc2)+Chr$(64+playertexacc2)+".jpg",4)
						EntityTexture titlemenuentity(7),titlemenutexture(7)
						ScaleEntity titlemenuentity(7),.01,.01,.01

					EndIf
				Else
					FreeTexture titlemenutexture(7)
					titlemenutexture(7)=myLoadTexture("data\models\stinker/accessory"+Str$(playeracc2)+Chr$(64+playertexacc2)+".jpg",4)
					EntityTexture titlemenuentity(7),titlemenutexture(7)

		
				EndIf
			Case 3
				PlaysoundfxNow(130)

				playersizeZ=playersizeZ+0.003
				If playersizeZ>=0.041 Then playersizeZ=0.029
			Case 4
				PlaysoundfxNow(130)

				playersizex=playersizex+0.003
				If playersizex>=0.041 Then playersizex=0.029
				playersizeY=playersizex
			
			Case 5
				playervoice=playervoice+1
				If playervoice=5 Then playervoice=1
				FreeSound soundfx(177)
				SoundFX(177)=myLoadSound("data\sound\voices\"+Str$(playervoice)+"\intro.wav")

				PlaysoundfxNow(177)

				
				
			End Select
		EndIf
		
	Else If currentmenu=21
		;cutscene1 animation
		HideEntity MouseCursor
		MouseCursorVisible=False
		HideEntity LevelCursor
		HideEntity DialogBackGroundEntity
		
		
		DialogTimer=DialogTimer+1
		
		UpdateCutScene1()
	Else If currentmenu=22
		;cutscene1 animation
		HideEntity MouseCursor
		MouseCursorVisible=False
		HideEntity LevelCursor
		HideEntity DialogBackGroundEntity
		
		
		DialogTimer=DialogTimer+1
		
		UpdateCutScene2()
		
	Else If currentmenu=23
		;cutscene1 animation
		HideEntity MouseCursor
		MouseCursorVisible=False
		HideEntity LevelCursor
		HideEntity DialogBackGroundEntity
		
		
		DialogTimer=DialogTimer+1
		
		UpdateCutScene3()


	Else If currentmenu=24
		; Floinging
		ex$="Select Adventure To Replay:"
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,7,1,1,255,255,0)
		ex$=CurrentReplayAdventureName
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,9,1,1,255,255,255)
		
		ex$="Coins: "+adventurecompletedcoins(currentreplayadventure)+" of "+adventurecompletedcoinstotal(currentreplayadventure)
		ex$=ex$+", Gems: "+adventurecompletedgems(currentreplayadventure)+" of "+adventurecompletedgemstotal(currentreplayadventure)
		ex$=ex$+", Score: "+adventurecompletedscore(currentreplayadventure)
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,10,1,1,255,255,255)
		ex$="<Back 10>   <Back>   <PLAY>   <Next>   <Next 10>"
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,12,1,1,255,255,0)
		ex$="<CANCEL>"
		DisplayText(ex$,Float((50-Len(ex$)))/2.0,14,1,1,255,255,0)

		If MouseGameMode=-1 And Mouse1=True
			MouseGameMode=-2

			If Float(MouseX())/Float(GfxWidth)>0.42 And Float(MouseX())/Float(GfxWidth)<0.58 And Float(MouseY())/Float(GfxHeight)>0.59 And Float(MouseY())/Float(GfxHeight)<0.65
				; cancel
				currentreplayadventure=0
				endmenu()
				PlaysoundfxNow(130)
			EndIf
	
			If Float(MouseX())/Float(GfxWidth)>0.62 And Float(MouseX())/Float(GfxWidth)<0.75 And Float(MouseY())/Float(GfxHeight)>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; forward
				Repeat
					CurrentReplayAdventure=CurrentReplayAdventure+1
					If CurrentReplayAdventure=500 Then CurrentReplayAdventure=1
				Until AdventureCompleted(CurrentReplayAdventure)=1
			
				; get the name
				If incustomhub=1
					file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
				Else
					file=ReadFile("data\adventures\adventure"+currentreplayadventure+"\master.dat")
				EndIf

				currentreplayadventurename$=ReadString$(file)
				CloseFile file
			EndIf
			If Float(MouseX())/Float(GfxWidth)>0.80 And Float(MouseY())/600.0>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; forward ten
				For j=1 To 10
					Repeat
						CurrentReplayAdventure=CurrentReplayAdventure+1
						If CurrentReplayAdventure=500 Then CurrentReplayAdventure=1
					Until AdventureCompleted(CurrentReplayAdventure)=1
				Next
			
				; get the name
				If incustomhub=1
					file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
				Else
					file=ReadFile("data\adventures\adventure"+currentreplayadventure+"\master.dat")
				EndIf

				currentreplayadventurename$=ReadString$(file)
				CloseFile file
			EndIf
			
			If Float(MouseX())/Float(GfxWidth)>0.25 And Float(MouseX())/Float(GfxWidth)<0.38 And Float(MouseY())/Float(GfxHeight)>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; back
				Repeat
					CurrentReplayAdventure=CurrentReplayAdventure-1
					If CurrentReplayAdventure=0 Then CurrentReplayAdventure=499
				Until AdventureCompleted(CurrentReplayAdventure)=1
			
				; get the name
				If incustomhub=1
					file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
				Else
					file=ReadFile("data\adventures\adventure"+currentreplayadventure+"\master.dat")
				EndIf

				currentreplayadventurename$=ReadString$(file)
				CloseFile file
			EndIf
			If Float(MouseX())/Float(GfxWidth)<0.2 And Float(MouseY())/Float(GfxHeight)>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; back ten
				For j=1 To 10
					Repeat
						CurrentReplayAdventure=CurrentReplayAdventure-1
						If CurrentReplayAdventure=0 Then CurrentReplayAdventure=499
					Until AdventureCompleted(CurrentReplayAdventure)=1
				Next
			
				; get the name
				If incustomhub=1
					file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+incustomhubnametruncated$+"\adventure"+currentreplayadventure+"\master.dat")
				Else
					file=ReadFile("data\adventures\adventure"+currentreplayadventure+"\master.dat")
				EndIf

				currentreplayadventurename$=ReadString$(file)
				CloseFile file
			EndIf
			
			If Float(MouseX())/Float(GfxWidth)>0.44 And Float(MouseX())/Float(GfxWidth)<0.56 And Float(MouseY())/Float(GfxHeight)>0.50 And Float(MouseY())/Float(GfxHeight)<0.56
				; PLAY
				endmenu()
				activatecommand(8,currentreplayadventure,0,0,0)
				playsoundfxnow(42)

			EndIf




			
		EndIf
		
	Else If currentmenu=25
	
		For i=0 To 3
			RotateEntity Titlemenuentity(i+5),0,0,15*Sin((menutimer+i*60) Mod 360)
		Next
		
		If ExitAfterTrailer=True
			ex$="Thank you for playing the demo version of"
			displaytext(ex$,(50-Len(ex$))/2.0+Cos(menutimer Mod 360),0.5,1,1,255,255,0)
			ex$="Wonderland Adventures: Mysteries of Fire Island."
			displaytext(ex$,(50-Len(ex$))/2.0+Sin(menutimer Mod 360),1.5,1,1,255,255,0)
		

		Else
			ex$="Congratulations!"
			displaytext(ex$,(50-Len(ex$))/2.0+Cos(menutimer Mod 360),0.5,1,1,255,255,0)
			If menutimer>60
				ex$="You have reached the end of the demo version."
				displaytext(ex$,(50-Len(ex$))/2.0+Sin(menutimer Mod 360),1.5,1,1,255,255,0)
			EndIf
		EndIf
		If menutimer>120
			ex$="Your journey has only begun!"
			If menutimer<110+33*2
				ex$=Left$(ex$,(menutimer-110)/2)
			EndIf
			displaytext(ex$,4,2,1.4,1,255,255,255)
		EndIf
		
		If menutimer>250 And menutimer<300
			PositionEntity TitleMenuEntity(5),-1.2,1.3,Float(menutimer-250)/6.0
		EndIf
		If menutimer>270
			ex$="Explore a vast new world,"
			DisplayText(ex$,24,5,1,1,255,255,255)
			ex$=" filled with adventure,"
			DisplayText(ex$,24,6,1,1,255,255,255)
			ex$=" excitement, and fun."
			DisplayText(ex$,24,7,1,1,255,255,255)

		EndIf
		If menutimer>650 And menutimer<700
			PositionEntity TitleMenuEntity(6),-3.0,0.5,Float(menutimer-650)/6.0-0.1
		EndIf
		If menutimer>670
			ex$="Discover new magic powers,"
			DisplayText(ex$,24,8.5,1,1,255,255,255)
			ex$=" make new friends,"
			DisplayText(ex$,24,9.5,1,1,255,255,255)
			ex$=" and save Wonderland!"
			DisplayText(ex$,24,10.5,1,1,255,255,255)

		EndIf
		
		If menutimer>1050 And menutimer<1100
			PositionEntity TitleMenuEntity(8),-1.1,-0.5,Float(menutimer-1050)/6.0-0.2
		EndIf
		If menutimer>1070
			ex$="Exercise your mind,"
			DisplayText(ex$,24,12,1,1,255,255,255)
			ex$=" solve clever puzzles"
			DisplayText(ex$,24,13,1,1,255,255,255)
			ex$=" and devious challenges."
			DisplayText(ex$,24,14,1,1,255,255,255)
		EndIf
		
		If menutimer>1450 And menutimer<1500
			PositionEntity TitleMenuEntity(7),-3.0,-1.2,Float(menutimer-1450)/6.0-0.3
		EndIf
		If menutimer>1470
			ex$="Reveal ancient secrets,"
			DisplayText(ex$,24,15.5,1,1,255,255,255)
			ex$=" and unlock the Mysteries"
			DisplayText(ex$,24,16.5,1,1,255,255,255)
			ex$=" of Fire Island."
			DisplayText(ex$,24,17.5,1,1,255,255,255)
		EndIf


		If menutimer>1800
			
			ex$="Wonderland needs your help! Are you ready?"
			displaytext(ex$,(50-Len(ex$))/2.0+Cos(menutimer Mod 360),19.5,1,1,255,255,0)
		EndIf
		If menutimer>1860
			If MouseY()>540.0*GfxHeight/600.0 And MouseY()<564.0*GfxHeight/600.0
				col=255
			Else
				col=0
			EndIf
			ex$="Yes, I want to get the full game!"
			displaytext(ex$,(50-Len(ex$))/2.0,21.5,1,1,255,255,col)
			If MouseY()>564.0*GfxHeight/600.0
				col=255
			Else
				col=0
			EndIf
			If ExitAfterTrailer=True
				ex$="No, just exit to Windows please."
			Else
				ex$="No, just exit to the Main Menu."
			EndIf
			displaytext(ex$,(50-Len(ex$))/2.0,22.5,1,1,255,255,col)
		EndIf




	
		
		If menutimer>1860
			If (mouse1=True Or mouse2=True) And MouseGameMode=-1  
			  If MouseY()<564.0*GfxHeight/600.0
				StopChannel (musicchannel)
				;EndGraphics
				If AffiliateID=0
;					ExecFile("https://www.plimus.com/jsp/buynow.jsp?contractId=1700878&referrer=grubbygames&templateId=129023")

					ExecFile("http://www.midnightsynergy.com/adventures2/order.htm")
				Else
;					ExecFile("https://www.plimus.com/jsp/buynow.jsp?contractId=1700878&referrer=grubbygames&templateId=129023")

					ExecFile("https://secure.bmtmicro.com/ECommerce-OffSite/1043order.html?AID="+Str$(AffiliateID))
				EndIf	
				End
			 
			  Else
				If exitaftertrailer=True
				StopChannel (musicchannel)
					;EndGraphics()
					End
				Else
					MouseGameMode=-2
					PlaysoundfxNow(132)
					endmenu()
					startmenu(11)
				EndIf
					
		      EndIf
			EndIf
			
		EndIf




	Else If currentmenu=26
		; credits
		
		
		creditlight#=1.0

		
		credittimer=credittimer+1
		If credittimer>115*100 Then credittimer=0
		For i=0 To 199
			If i-credittimer/100.0>=0 And i-credittimer/100.0<10
				If Float(i)-credittimer/100.0<1 
					creditlight#=Float(i)-credittimer/100.0
				Else If Float(i)-credittimer/100.0>9
					creditlight#=10.0-(Float(i)-credittimer/100.0)
				Else
					creditlight#=1.0
				EndIf
				
				

				displaytext(credittext$(i),.5+(50-Len(credittext$(i)))/2.0,5+i-credittimer/100.0,1,1,255*creditlight,255*creditlight,creditcolour(i)*255*creditlight)
			EndIf
		Next
	
		

	EndIf

	yposstart#=1+.5*(10-CurrentMenuNofItems)
	
	If DialogBackGroundSize<90 Then DialogBackGroundSize=DialogBackGroundSize+2
	
	If currentmenu=24
		PositionEntity DialogBackGroundEntity,0,0,20-21*Sin(DialogBackGroundSize)
	Else
		PositionEntity DialogBackGroundEntity,0,0,20-20*Sin(DialogBackGroundSize)
	EndIf
	If DialogContrast=3
		EntityAlpha DialogBackGroundEntity,0
	Else
		EntityAlpha DialogBackGroundEntity,Float(DialogBackGroundSize)/100.0
	EndIf

	
	
	ControlIcons()

	;PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	;widescreen
	If widescreen
		PositionEntity MouseCursor,-13.28+26.625*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	Else 
		PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	EndIf

	If Mouse1=False And Mouse2=False 
		MouseGameMode=-1
		For i=0 To 79
			; activate icons
			If IconEntity(i)>0 Then EntityPickMode IconEntity(i),2
		Next
	EndIf
	
	MenuTimer=MenuTimer+1
	DialogTimer=DialogTimer+1
	
	; find what item we're on
	CurrentMenuLine=-1
	If MouseX()>GfxWidth/5 And MouseX()<GfxWidth*4/5
		CurrentMenuLine=((MouseY()-(-10+50*yposstart)*GfxHeight/600)/(50*GfxHeight/600))-.5
	EndIf
	
	; Display the menu
	For i=0 To CurrentMenuNofItems-1
		If MenuActive(currentmenu,i)=1
			width#=1.0
			tex$=MenuText$(currentmenu,i)
			DisplayText(tex$,(26.0-Len(tex$)*width)/(width*2.0),(yposstart+i)/width,2.0,width,100,100,0)

		Else If MenuActive(currentmenu,i)=2

			If i<>CurrentMenuLine
				width#=1.05+.05*Sin((DialogTimer*2)+i*60)
				tex$=MenuText$(currentmenu,i)
				DisplayText(tex$,(26.0-Len(tex$)*width)/(width*2.0),(yposstart+i)/width,2.0,width,255,255,0)
			Else

				width#=1.1+.1*Sin((DialogTimer*2)+i*60)
				tex$=MenuText$(currentmenu,i)
				DisplayText(tex$,(26.0-Len(tex$)*width)/(width*2.0),(yposstart+i)/width,2.0,width,255,255,255)
			EndIf
		Else If MenuActive(currentmenu,i)=3
			width#=1.0
			tex$=MenuText$(currentmenu,i)
			If currentmenu=12 And i=0
				fluxcompensator#=0.4
			Else
				fluxcompensator#=0.0
			EndIf
			DisplayText(tex$,(26.0-Len(tex$)*width)/(width*2.0),(yposstart+i-fluxcompensator)/width,2.0,width,255,255,100)

		EndIf
	Next
	
	
	If Mouse1=True And MouseGameMode=-1 And (CurrentMenu=4 Or CurrentMenu=5 Or CurrentMenu=16)
		; load/save
		For i=1 To 9
			If MouseX()>i*75*GfxWidth/800.0 And MouseX()<(i*75+50)*GfxWidth/800.0 And MouseY()>150*GfxHeight/600.0 And MouseY()<200*GfxHeight/600.0
				
				If CurrentMenu=4
					; save
					If SaveSlotDateTime$(i)=""
						; empty slot - just save
						If LightRedGoal2=-1
							LightRedGoal2=LightRedGoal
							LightGreenGoal2=LightGreenGoal
							LightBlueGoal2=LightBlueGoal
							AmbientRedGoal2=AmbientRedGoal
							AmbientGreenGoal2=AmbientGreenGoal
							AmbientBlueGoal2=AmbientBlueGoal
						EndIf
						ex$=Mid$(CurrentDate$(),4,4)+Left$(CurrentDate$(),2)+" "
						ex$=ex$+" "+CurrentTime$()
						SaveGame(i,ex$,adventuretitle4saving$)
						EndMenu()
						MessageLineText1$="Game Saved in Slot #"+Str$(i)
						MessageLineText2$=""
						MessageLineTimer=300
						PlaysoundfxNow(132)
						TimeOfLastGameSaved=MilliSecs()
	
						If ExitAfterThisSave=True
							EndLevel()
							endadventure()
							StartMenu(11)
						EndIf
					
					Else
						; overwrite
						EndMenu()
						GameSlotToBeSaved=i
						GameNameToBeSaved$=SaveSlotDateTime$(i)
						PlaysoundfxNow(135)
						
						MenuText$(6,0)="Overwrite Slot #"+Str$(i)+"?"
				
						StartMenu(6)
					EndIf
					
					i=10
					
				Else If SaveSlotDateTime$(i)<>""

					; load
					
					EndMenu()
					EndLevel()
					EndAdventure()

					PlaysoundfxNow(132)

					LoadGame(i)
					
					MessageLineText1$="Game Loaded From Slot #"+Str$(i)
					MessageLineText2$=""
					MessageLineTimer=300
					
					i=10
					
					
				EndIf
					
				
			EndIf
		Next
	EndIf
	
	If (KeyDown(14) Or KeyDown(211)) And CurrentMenu=12
		; delete player profile
		
		Select currentmenuline
		Case 1,2,3,4,5,6,7,8
			If Menutext$(currentmenu,currentmenuline)<>"" And Menutext$(currentmenu,currentmenuline)<>"Default" And Menutext$(currentmenu,currentmenuline)<>"Create New Profile"

				Profile2BDeleted$=Menutext$(currentmenu,currentmenuline)
				
				
				
				
			;	If file=0
			;		Profile2BDeleted$=""

			;	Else
					Profile2BDeleted$=Menutext$(currentmenu,currentmenuline)

			;		CloseFile file
					PlaysoundfxNow(131)
					PlaysoundfxNow(135)


					endmenu()
					startmenu(20)
			;	EndIf
;				endmenu()

		
				
			EndIf
		End Select
	EndIf

	
	
	If Mouse1=True And MouseGameMode=-1 And CurrentMenuLine>=0 And CurrentMenuLine<CurrentMenuNofItems 
		If MenuActive(CurrentMenu,CurrentMenuLine)=2
		
			Select CurrentMenu
			
			Case 0 ; in -game
				Select CurrentMenuLine
				Case 0
					; resume game
					EndMenu()
					PlaysoundfxNow(130)

				Case 1
					; load game
					EndMenu()
					StartMenu(5)
					PlaysoundfxNow(133)


				Case 2
					; save game
					EndMenu()
					StartMenu(4)
					PlaysoundfxNow(134)


				Case 3
					; restart adventure
					EndMenu()
					StartMenu(2)
					PlaysoundfxNow(131)

				Case 4
					; abort adventure
					
					endmenu()
					startmenu(1)
					PlaysoundfxNow(131)

					
				Case 5
					; exit game
					
					endmenu()
					
					startmenu(3)
					PlaysoundfxNow(131)

				End Select
			Case 1 ; abort - are you sure?
				Select CurrentMenuLine
				Case 3
					; Yes
					
							
					EndLevel()
					EndAdventure()
					AdventureCancelData()
					
					
					Select AdventureCurrentStatus
					Case 0,1; hub (shouldn't happen) or regular adventure
						 If WAEpisode=3 And AdventureCompleted(178)=True And (AdventureCurrentNumber=161 Or AdventureCurrentNumber=162 Or AdventureCurrentNumber=163 Or AdventureCurrentNumber=164 Or AdventureCurrentNumber=167 Or AdventureCurrentNumber=168)
							StartHubLevel(-43,10,14,False)
						Else
							StartHubLevel(-AdventureExitLostLevel,AdventureExitLostX,AdventureExitLostY,False)
						EndIf
					Case 2 ; custom
						HideEntity DialogBackGroundEntity
						If TestMode
							ExitTestMode()
						Else
							StartCustomSelectMenu()
						EndIf
					Case 3 ; replayadventure
						StartHubLevel(PreReplayAdventureLevel,PreReplayAdventureX,PreReplayAdventureY,False)

					End Select

				Case 4
					; No
					EndMenu()
					PlaysoundfxNow(130)

				End Select
			Case 2 ; restart - are you sure?
				Select CurrentMenuLine
				Case 3
					; Yes
					
					Endmenu()
					PlaysoundfxNow(132)

					
					AdventureCancelData()
								
					If AdventureCurrentStatus=1 Or AdventureCurrentStatus=3
						; regular adventure or replay adventure
						ActivateCommand(8,AdventureCurrentNumber,0,0,0)
					Else If AdventureCurrentStatus=2
						; custom adventure
						ActivateCommand(8,0,0,0,0)
					EndIf

				Case 4
					; No
					PlaysoundfxNow(130)

					EndMenu()
				End Select
			Case 3 ; exit - are you sure?
				Select CurrentMenuLine
				Case 3
					; Yes
					EndMenu()
					
					If WAEpisode=0 And InCustomHub=0
						EndLevel()
						endadventure()
						If TestMode
							ExitTestMode()
						Else
							PlaysoundfxNow(132)
							StartMenu(11)
						EndIf

					Else
					
					
						If MilliSecs()-TimeOfLastGameSaved>60000
							PlaysoundfxNow(135)
							StartMenu(7)					
						Else
							EndLevel()
							endadventure()
							PlaysoundfxNow(132)
							StartMenu(11)
						EndIf
					EndIf
				Case 4
					; No
					PlaysoundfxNow(130)

					EndMenu()
				End Select

			Case 4 ; save game
				If CurrentMenuLine=9
					; Cancel (everything else taken care above separately)
					PlaysoundfxNow(130)

					EndMenu()
							
				EndIf
			Case 5 ; load game
				If CurrentMenuLine=9 

					; Cancel (everything else above)
					PlaysoundfxNow(130)

					EndMenu()
				
						
				
				EndIf

			Case 6 ; overwrite saved game - are you sure?
				Select CurrentMenuLine
				Case 4
					; Yes
					PlaysoundfxNow(132)

					If LightRedGoal2=-1
						LightRedGoal2=LightRedGoal
						LightGreenGoal2=LightGreenGoal
						LightBlueGoal2=LightBlueGoal
						AmbientRedGoal2=AmbientRedGoal
						AmbientGreenGoal2=AmbientGreenGoal
						AmbientBlueGoal2=AmbientBlueGoal
						
					EndIf
					ex$=Mid$(CurrentDate$(),4,4)+Left$(CurrentDate$(),2)+" "
					ex$=ex$+" "+CurrentTime$()

					SaveGame(GameSlotToBeSaved,ex$,adventuretitle4saving$)
					EndMenu()
					MessageLineText1$="Game Saved in Slot #"+Str$(GameSlotToBeSaved)
					MessageLineText2$=""
					MessageLineTimer=300
					TimeOfLastGameSaved=MilliSecs()
					
					If ExitAfterThisSave=True
						EndLevel()
						endadventure()
						StartMenu(11)
					EndIf



				Case 5
					; No
					PlaysoundfxNow(130)

					EndMenu()
					StartMenu(4)
				End Select
			
			Case 7
				; save before exit?
				Select currentmenuline
				Case 4
					; don't save
					endmenu()
					endadventure()
					startmenu(11)
				Case 5
					; save
					If OldGameMode=8
						endmenu()
					Else
						endmenu()
						startmenu(4)
						ExitAfterThisSave=True
					EndIf
				
					
				End Select
			
			
			
			
				
			Case 11
				; main menu
				Select currentmenuline
				Case 3
					PlaysoundfxNow(131)
					endmenu()
					
					If WaEpisode=0
						StartCustomSelectMenu()
					Else
						endmenu()
						
						HideEntity LevelCursor
						HideEntity MouseCursor
						HideEntity DialogBackGroundEntity
						
						CameraFogMode camera,0
						CameraClsColor camera,0,0,0
						CameraFogColor camera,0,0,0
						CameraFogRange camera,200,1000
						CameraRange camera,0.1,1000
						EntityAlpha LevelCursor,.5
															
						For p.Letter = Each Letter
							Delete p
						Next
					
						cube=CreateCube(camera)
						cube2=CreateCube(camera)
						ScaleEntity cube2,.4,.5,.5
						PositionEntity cube2,0,-1,4
						EntityColor cube2,64,64,64
						EntityBlend cube2,2
						EntityFX cube,1
						EntityFX cube2,1
						
						cubetex=myLoadTexture("load.jpg",1)
						EntityTexture cube,cubetex
						PositionEntity cube,0,0.1,5
						
						RenderLetters()
						RenderParticles()
						UpdateWorld
						RenderWorld
						Flip


					
;						
						; copy the hub data
						dir=ReadDir ("Data\Adventures\Hub")
						j=0
						Repeat
							dirfile$=NextFile$(dir)
							If FileType("Data\Adventures\Hub\"+dirfile$)=1
								CopyFile "Data\Adventures\Hub\"+dirfile$,GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+dirfile$
								If j Mod 10 = 0
					
									PositionEntity cube2,0.004*j,-1,4
									RenderWorld
									Flip
				
									
								EndIf
								j=j+1

							EndIf
						Until dirfile$=""
						CloseDir dir
				
						CreateNewPlayer()
						PositionEntity cube2,0.9,-1,4
						RenderWorld
						Flip

						FreeTexture cubetex
						FreeEntity cube
						FreeEntity cube2
	
	
						ShowEntity LevelCursor
						ShowEntity MouseCursor


						starthublevel(-1,10,28,False)
						
						
						
					EndIf
				Case 4
					; custom hubs
					endmenu()
					PlaysoundfxNow(131)
					StartCustomHubSelectMenu()

				
				
				Case 5
					endmenu()
					PlaysoundfxNow(133)

					startmenu(16)
				Case 6
					endmenu()
					PlaysoundfxNow(131)

					startmenu(18)
				
				Case 7
					
				;	If FullVersion=False Or PortalVerison=True
				;		Endmenu()
				;		PlaysoundfxNow(131)
		;
		;				startmenu(19)
;
;					
;					Else		
;						ExecFile("http://www.midnightsynergy.com/adventures/levelex/index.html")
;						End	
;					EndIf
;					
;
;						
;				Case 8
					If FullVersion=False Or PortalVersion=True


					Else
						Endmenu()
						PlaysoundfxNow(131)

						startmenu(19)
					EndIf
				End Select
				
				
			Case 12
				; select profile
				Select currentmenuline
				Case 1,2,3,4,5,6,7,8
					If Menutext$(currentmenu,currentmenuline)<>""
						PlayerCharacterName$=""
						PlayerName$=Menutext$(currentmenu,currentmenuline)
						file=ReadFile(GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Playerfile.wpf")
						If file=0
							PlayerCharacterName$=""
							PlayerTimePlayed=0
						Else
							ReadInt(file)
							ReadString$(file)
							ReadString$(file)
							ReadString$(file)
							ReadInt(file)
							playername$=ReadString$(file)
							playercharactername$=ReadString$(file)
							CloseFile file
						EndIf
						endmenu()
						If PlayerCharacterName$=""
							PlaysoundfxNow(131)
							startmenu(14)
						Else
							PlaysoundfxNow(132)
							loadplayer(globaldirname$+"\player profiles\"+playername$+"\current\playerfile.wpf")

							startmenu(11)
						EndIf
					EndIf
				Case 9
					endmenu()
					PlaysoundfxNow(131)

					startmenu(13)
				End Select
			Case 13
				; enter new name
				Select currentmenuline
				Case 4
					
					playername$=Left$(menutext$(13,2),Len(menutext$(13,2))-1)
					; delete leading spaces
					If Left$(playername$,1)=" "
						Repeat
							playername$=Right$(playername$,Len(playername$)-1)
						Until Left$(playername$,1)<>" "
					EndIf
					If playername$<>""
						; ok
						If FileType(GlobalDirName$+"\Player Profiles\"+playername$)=2
							endmenu()
							;PlaysoundfxNow(135)

							startmenu(17)
						Else
							endmenu()
							PlaysoundfxNow(131)

							startmenu(14)
						EndIf
					EndIf
				Case 5
					; cancel
					endmenu()
					PlaysoundfxNow(130)

					startmenu(12)
				End Select
			Case 15
				; custom stinker
				If currentmenuline=8
					; delete leading spaces
					If Left$(playercharactername$,1)=" "
						Repeat
							playercharactername$=Right$(playercharactername$,Len(playercharactername$)-1)
						Until Left$(playercharactername$,1)<>" "
					EndIf

					If playercharactername$="" Then PlayerCharacterName$="Anonymous"
					endmenu()
					
					timeflag=MilliSecs()
					PlaysoundfxNow(132)
					ResetText("data/graphics/font.bmp")

			;		AmbientLight 255,255,255
			;		cube=CreateCube(camera)
			;		cubetex=myLoadTexture("load.jpg",1)
			;		EntityTexture cube,cubetex
			;		PositionEntity cube,0,0,10
					
			;		RenderWorld
			;		Text GfxWidth*0.5,Gfxheight*2/7,"...Creating Player...",True
			;		Text GfxWidth*0.5,Gfxheight*2/3,"...Please Wait...",True
			;		Flip

					
					CreateNewPlayer()
					

					Repeat
					Until MilliSecs()-timeflag>600
			;		FreeTexture cubetex
			;		FreeEntity cube

					startmenu(11)
					
				EndIf
				If currentmenuline=9
					PlaysoundfxNow(130)

					endmenu()
					startmenu(14)
				EndIf
			Case 16
				; load saved game from main menu
				
				If CurrentMenuLine=9 ; (only do cancel - all else above)

					; Cancel
					EndMenu()
					PlaysoundfxNow(130)

					startmenu(11)
				
						
				
				EndIf

			
			Case 17
				; profile already exists
				Select currentmenuline
				Case 6
					; yes
					endmenu()
					PlaysoundfxNow(131)

					startmenu(12)

				
				End Select	
			Case 18
				; options
				Select currentmenuline
				Case 2
					Endmenu()
					PlaysoundfxNow(131)

					startmenu(12)
					
					
				
			;	Case 3
			;		endmenu()
			;		PlaySoundfxnow(131)
			;		Startmenu(14)
			;		OldPlayerCharacterName$=".."
			;		OldPlayerCharacterName2$=PlayerCharacterName$
				Case 3
					PlaysoundfxNow(130)

					Keyboardmode=3-Keyboardmode
					endmenu()
					Startmenu(18)
				Case 4
				
					; sound
					GlobalSoundVolume2=(GlobalSoundVolume2+1) Mod 6
					GlobalSoundVolume=Float(globalsoundvolume2)*0.2
					MenuText$(18,4)="Sound Volume: "+GlobalSoundVolume2
					PlaysoundfxNow(130)


				Case 5
					; music
					GlobalMusicVolume2=(GlobalMusicVolume2+1) Mod 6
					GlobalMusicVolume=Float(globalMusicvolume2)*0.2
					MenuText$(18,5)="Music Volume: "+GlobalMusicVolume2
					If ChannelPlaying(musicchannel)=1
						StopChannel(musicchannel)
					EndIf
					If globalmusicvolume2>0
						MusicChannel=PlayMusic ("data\music\8.ogg")
						ChannelVolume MusicChannel,GlobalMusicVolume
						currentmusic=8
						LevelMusicCustomVolume=100.0
						LevelMusicCustomPitch=44

					Else 
						currentmusic=-99 ; remember that -1 is soundscape beach
					EndIf
					PlaysoundfxNow(130)
					
				Case 6
					PlaysoundfxNow(130)

					DialogContrast=(DialogContrast+1) Mod 4
					SetDialogContrast()
					endmenu()
					Startmenu(18)


				Case 7
					; credits
					endmenu()
					Startmenu(26)

				Case 8
					PlaysoundfxNow(132)

					endmenu()
					startmenu(11)
				End Select
			Case 19
				; exit to windows - are you sure
				Select currentmenuline
				Case 3
					; yes
					If FullVersion=True Or TrailerAlreadyPlayed=True
						EndGame = True
						;End
					Else
						endmenu()
						startmenu(25)
						exitaftertrailer=True
					EndIf
				Case 4
					; no
					endmenu()
					PlaysoundfxNow(130)

					startmenu(11)
				End Select
				
			Case 20
				; delete profile - are you sure
				Select currentmenuline
				Case 3
					; yes
					
					; delete everything
					
					
					
					ex$=GlobalDirName$+"\Player Profiles\"+profile2bdeleted$
					
					; savefiles
					For i=1 To 9
						If FileType(ex$+"\Savefiles\"+Str$(i)+"\Adventure")=2
							dirfile=ReadDir(ex$+"\Savefiles\"+Str$(i)+"\Adventure")
							Repeat
								ex2$=NextFile$(dirfile)
								DeleteFile ex$+"\Savefiles\"+Str$(i)+"\Adventure\"+ex2$
							Until ex2$=""
							DeleteDir ex$+"\Savefiles\"+Str$(i)+"\Adventure"
						EndIf

						If FileType(ex$+"\Savefiles\"+Str$(i)+"\Hub")=2

							dirfile=ReadDir(ex$+"\Savefiles\"+Str$(i)+"\Hub")
							Repeat
								ex2$=NextFile$(dirfile)
								DeleteFile ex$+"\Savefiles\"+Str$(i)+"\Hub\"+ex2$
							Until ex2$=""
							DeleteDir ex$+"\Savefiles\"+Str$(i)+"\Hub"
						EndIf
						
						If FileType(ex$+"\Savefiles\"+Str$(i))=2

							dirfile=ReadDir(ex$+"\Savefiles\"+Str$(i))
							Repeat
								ex2$=NextFile$(dirfile)
								DeleteFile ex$+"\Savefiles\"+Str$(i)+"\"+ex2$
							Until ex2$=""
							DeleteDir ex$+"\Savefiles\"+Str$(i)
						EndIf
					Next
					
					If FileType(ex$+"\Savefiles")=2
						DeleteDir ex$+"\Savefiles"
					EndIf

					
					If FileType(ex$+"\Current\Adventure")=2
						dirfile=ReadDir(ex$+"\Current\Adventure")
						Repeat
							ex2$=NextFile$(dirfile)
							DeleteFile ex$+"\Current\Adventure\"+ex2$
						Until ex2$=""
						DeleteDir ex$+"\Current\Adventure"
					EndIf

					If FileType(ex$+"\Current\Hub")=2
						dirfile=ReadDir(ex$+"\Current\Hub")
						Repeat
							ex2$=NextFile$(dirfile)
							DeleteFile ex$+"\Current\Hub\"+ex2$
						Until ex2$=""
						DeleteDir ex$+"\Current\Hub"
					EndIf
					
					

					
					If FileType(ex$+"\Current")=2
						
						dirfile=ReadDir(ex$+"\Current")
						Repeat
						
							ex2$=NextFile$(dirfile)
							
						
							
							DeleteFile ex$+"\Current\"+ex2$
							
							
							
						Until ex2$=""
						DeleteDir ex$+"\Current"
					EndIf
					
					If FileType(ex$)=2

						dirfile=ReadDir(ex$)
						Repeat
							ex2$=NextFile$(dirfile)
							DeleteFile ex$+"\"+ex2$
						Until ex2$=""
						DeleteDir ex$
					EndIf

			
						
						
										
					endmenu()
					PlaysoundfxNow(130)
					
					playername$="Default"
					playercharactername$="Stinky"

					startmenu(11)

					
				Case 4
					; no
					endmenu()
					PlaysoundfxNow(130)
					PlayerName$=OldPlayerName$
					PlayerCharacterName$=OldPlayerCharacterName$

					startmenu(11)
				End Select
				
			Case 26
				; credits
				Select currentmenuline
				Case 9
					
					; no
					endmenu()
					PlaysoundfxNow(130)

					startmenu(11)
				End Select


			End Select
		
			MouseGameMode=-2
		EndIf
	EndIf


End Function

Function EndMenu()
	GameMode=OldGameMode
	IconHelpText$(9)="Open"
	CameraProjMode  camera,1

	
	If GameMode=8 And CurrentInterChange>=0
		surface=GetSurface(DialogBackGroundEntity,1)
		VertexCoords surface,0,-2,1.3,5
		VertexCoords surface,1,2,1.3,5
		VertexCoords surface,2,-2,-0.5-(NofInterChangeReplies(CurrentInterChange)-1)*0.16,5
		VertexCoords surface,3,2,-0.5-(NofInterChangeReplies(CurrentInterChange)-1)*0.16,5
		DialogTimer=1000
	EndIf
	
	If WAEpisode=0
		For i=3 To 10
			If titlemenuentity(i)>0
				FreeEntity titlemenuentity(i)
				
				titlemenuentity(i)=0
			EndIf
			If titlemenutexture(i)>0
				FreeTexture titlemenutexture(i)
				
				titlemenutexture(i)=0
			EndIf
	
		Next
	Else
		For i=0 To 10
			If titlemenuentity(i)>0
				FreeEntity titlemenuentity(i)
				
				titlemenuentity(i)=0
			EndIf
			If titlemenutexture(i)>0
				FreeTexture titlemenutexture(i)
				
				titlemenutexture(i)=0
			EndIf
	
		Next

		
	EndIf
	
	For i=11 To 30
		If titlemenuentity(i)>0
			FreeEntity titlemenuentity(i)
			titlemenuentity(i)=0
		EndIf

	Next
	
	HideEntity TitleMenuEntity(60)
	For i=78 To 99
		HideEntity TitleMenuEntity(i)
	Next

	If currentmenu>=10 And currentmenu<>24	endlevel()
	
	For i=0 To 9
		HideEntity SaveSlotEntity(i)
	Next

	
End Function
Function UpdateTitleMenu()

;	If MilliSecs()-StartofTitleMusic<15000 
;		LevelTimer=0
;		MenuTimer=0
;		EntityAlpha TitleMenuEntity(1),0
;		PositionEntity titlemenuentity(1),1.5,1,.5
;		
;
;		Return
;	EndIf

	LevelTimer=LevelTimer+1
;	ControlParticles()
;	ControlObjects()
	
;	displaytext(Str$(leveltimer),0,0,1,1,255,255,255)
;	displaytext(Str$(menutimer),0,1,1,1,255,255,255)


	If menutimer<1000 Then menutimer=menutimer+1
	
	If WAEpisode=3
		If menutimer>1000
			For i=78 To 99
				;If  EntityZ(TitleMenuEntity(i))<1 Or EntityZ(titlemenuentity(i))>170
			;		EntityAlpha titlemenuentity(i),0
			;	Else
					If i<>79 EntityAlpha Titlemenuentity(i),Float(menutimer-1000)/1000.0
			;	EndIf
			Next
			EntityAlpha Titlemenuentity(79),Float(menutimer-1000)/2000.0
			
		Else
			For i=78 To 99
				 EntityAlpha Titlemenuentity(i),0
			Next
		EndIf
		
	EndIf


	

	TurnEntity titlemenuentity(1),0,-.2,0

	If MenuTimer<500
		PositionEntity titlemenuentity(1),1.5-2.35*(Float(menutimer)/500.0),1+.1*(Float(Menutimer)/500.0),.5+4.5*(Float(Menutimer)/500.0)
		EntityAlpha TitleMenuEntity(1),1
		EntityAlpha TitleMenuEntity(2),0
	Else If MenuTimer<700
		EntityAlpha TitleMenuEntity(1),1-Float(menutimer-500)/200.0
		EntityAlpha TitleMenuEntity(2),Float(menutimer-500)/300.0
	Else If menutimer<800
		EntityAlpha TitleMenuEntity(1),0
		EntityAlpha TitleMenuEntity(2),Float(menutimer-500)/300.0
		displaytext("PRESENTS",21,7.5,.8,1.25,55+(menutimer-700)/2,55+(menutimer-700)/2,55+(menutimer-700)/2)
	Else If menutimer<900
		displaytext("PRESENTS",21,7.5,.8,1.25,55+(menutimer-700)/2,55+(menutimer-700)/2,55+(menutimer-700)/2)

		;(mytext$,x#,y#,size#,spacing#,red,green,blue)
	Else If menutimer<1000
		EntityAlpha TitleMenuEntity(2),(999-menutimer)/100.0
		displaytext("PRESENTS",21,7.5,.8,1.25,(1000-menutimer),(1000-menutimer),(1000-menutimer))

	Else If menutimer<2000
		
	
		
		If menutimer<1010
			
		;	For i=78 To 99
		;		EntityAlpha Titlemenuentity(i),1
		;	Next
		EndIf
		
		If menutimer<1800
			EntityAlpha titlemenuentity(60),(menutimer-1000.0)/800.0
		EndIf
		
		PositionEntity titlemenuentity(60),0,(menutimer-1000.0)*.00035,.2+(menutimer-1000.0)*0.0018

		

	
		CameraFogMode camera,1
		
		CameraClsColor camera,0,0,0
		CameraFogColor camera,0,0,0
		CameraFogRange camera,(menutimer-1000)*.0001,(menutimer-1000)*1
	Else 
		startmenu(11)

	EndIf
	
	

		
End Function


Function UpdateCutScene1()
	
	LightColor SpotLight,0,0,0
	LightColor LevelLight,0,0,0
	

	UpdateWater(48,83,20)
	
	ControlParticles()
	ControlObjects()
	leveltimer=leveltimer+1
	
	If menutimer<2000
		PositionEntity camera,30-menutimer/200.0,2+menutimer/100.0,-50+menutimer/60.0
		RotateEntity camera,20+menutimer/50.0,menutimer/75.0,0
	EndIf
	
	If menutimer<98
		menutimer=99
	
	
	
	Else If menutimer=100
	
		LoadLevel("Data\Adventures\Hub\9.wlv",False,True)
		CreateLevel()
		waterplane=CreatePlane()
		EntityColor waterplane,0,0,255
		EntityAlpha waterplane,.6
		PositionEntity waterplane,0,-.20,0
		
	
		AmbientLight 0,0,0
		
		
		If ChannelPlaying(musicchannel)=1
			StopChannel(musicchannel)
		EndIf
		MusicChannel=PlayMusic ("data\music\7.ogg")
		ChannelVolume MusicChannel,GlobalMusicVolume
		currentmusic=7
		
	

		
		
	Else If menutimer<612
		AmbientLight (menutimer-100)/2,(menutimer-100)/2,(menutimer-100)/2
		

	Else If menutimer<1500	
		
	
	Else If menutimer<1756
		AmbientLight 255-(menutimer-1500),255-(menutimer-1500),255-(menutimer-1500)
		

	Else 


		

		

	EndIf
	
	

	If menutimer<300
	Else If menutimer<1600
		displaytext("And so it came to pass",14,20,1,1,255,255,255)
		displaytext("that the Stinkers mounted an expedition",5.5,21,1,1,255,255,255)
		displaytext("to cross the waters and reach            .",4,22,1,1,255,255,255)
		displaytext("                              FIRE ISLAND",4,22,1,1,255,Rand(240,250),0)
	Else If menutimer<1758
		If menutimer=1756

	
			FreeEntity waterplane
			waterplane=0
		EndIf
		
	Else If menutimer<2000
		displaytext("Surely, nothing could go       on this voyage?",2,20,1,1,255-(menutimer-1750),255-(menutimer-1750),255-(menutimer-1750))
		displaytext("                         wrong ",2,20,1,1,255-(menutimer-1750)/2,255-(menutimer-1750)/2,255-(menutimer-1750)/2)

		If menutimer>1900
			ChannelVolume MusicChannel,GlobalMusicVolume*(2000.0-menutimer)/100.0
		EndIf
	
	Else If menutimer=2000
		If ChannelPlaying(musicchannel)=1
			StopChannel(musicchannel)
		EndIf
		currentmusic=-99
		Playsoundfxnow(156)
		PositionEntity camera,60,5,-75+55
		RotateEntity camera,20,135,0
		
		EndLevel()
		LoadLevel("Data\Adventures\Hub\10.wlv",False,True)
		CreateLevel()
	
		
	Else If menutimer<2150
	
	
	Else If menutimer=2150
		Playsoundfxnow(155)
		AmbientLight 255,255,255
		CameraClsColor camera,0,0,30
		CameraFogMode camera,1
		CameraFogRange camera,.1,40
		CameraFogColor camera,255,255,255
		PositionEntity camera,55,1,-72
		RotateEntity camera,-10,135,0

	Else If menutimer<2250
		AmbientLight 255-(menutimer-2150)*2.5,255-(menutimer-2150)*2.5,255-(menutimer-2150)*2.5
		CameraFogColor camera,255-(menutimer-2150)*2.5,255-(menutimer-2150)*2.5,255-(menutimer-2150)*2.5

		PositionEntity camera,65-(menutimer-2150.0)/10.0,1,-62-(menutimer-2150.0)/10.0+55
	
		
	Else If menutimer=2250
		Playsoundfxnow(155)
		Playsoundfxnow(156)
		AmbientLight 255,255,255
		CameraClsColor camera,0,0,30
		CameraFogMode camera,1
		CameraFogRange camera,.1,20
		CameraFogColor camera,255,255,255
	Else If menutimer<2450
		AmbientLight 255-(menutimer-2250)*1.25,255-(menutimer-2250)*1.25,255-(menutimer-2250)*1.25
		If menutimer<2350 CameraFogColor camera,255-(menutimer-2250)*2.5,255-(menutimer-2250)*2.5,255-(menutimer-2250)*2.5

		PositionEntity camera,60-(menutimer-2250.0)/20.0,5,-75-(menutimer-2250.0)/10.0+55
		RotateEntity camera,20,135-(menutimer-2250.0)/2,0
	Else If menutimer=2450
		AmbientLight 0,0,0
		CameraFogColor camera,0,0,0
	Else If menutimer<2650
	
	Else If menutimer<2800
		displaytext("After the tempest...",5,20,1,1,255,255,255)
	Else If menutimer<2950
		displaytext("After the tempest...",5,20,1,1,255,255,255)
		displaytext("silence...",20,21,1,1,255,255,255)
	Else If menutimer<3100
		displaytext("After the tempest...",5,20,1,1,255,255,255)
		displaytext("silence...",20,21,1,1,255,255,255)
		displaytext("and darkness.",32,22,1,1,255,255,255)



	Else If menutimer=3150
		playsoundfxnow(126)

		
	Else If menutimer=3300
		
		endlevel()
		endmenu()
		CameraFogMode camera,0	
		starthublevel(-21,26,26,False)




	
		
		
	EndIf
	



		
	





End Function

Function UpdateCutScene2()
	
	LightColor SpotLight,0,0,0
	LightColor LevelLight,0,0,0
	
	If menutimer<40
		AmbientLight (menutimer-10)*8,(menutimer-10)*8,(menutimer-10)*8
	EndIf

	If menutimer<10
		

	Else If menutimer=10
		Playsoundfxnow(155)
		If waterplane>0
			FreeEntity waterplane
			waterplane=0
		EndIf
		waterplane =CreateCube ()
		ScaleEntity waterplane,1.3,1,1
		PositionEntity waterplane,0,0,2
		If waterplanetexture>0
			FreeTexture waterplanetexture
		EndIf
		waterplanetexture=myLoadTexture ("Data/graphics/map/endmap1.jpg",1)
		EntityTexture waterplane,waterplanetexture
		
		PositionEntity camera,0,0,0
		RotateEntity camera,0,0,0
		
		cloudplanetexture=myLoadTexture("Data/graphics/map/cloud.jpg",1)

		cloudplane=CreatePlane()
		EntityTexture cloudplane,cloudplanetexture
		RotateEntity cloudplane,-90,0,0
		EntityAlpha cloudplane,0
		PositionEntity cloudplane,0,0,.5
		ScaleTexture cloudplanetexture,1,1
	
	Else If menutimer<350
		TranslateEntity camera,0,0,-.0035
		
			EntityAlpha cloudplane,0.4+Float(menutimer)/580.0
			TranslateEntity cloudplane,0.0025,0.001,0
	
		
		If menutimer=200
			playsoundfxnow(123)
		EndIf

	
		
	Else If menutimer=350
	;	Delay 1000
		ScaleEntity waterplane,2.7/0.65,2.0/0.65,1
;		PositionEntity waterplane,0,0,0.51
		FreeTexture waterplanetexture
		waterplanetexture=myLoadTexture ("Data/graphics/map/endmap2.jpg",1)
		EntityTexture waterplane,waterplanetexture
		Playsoundfx(156,-1,-1)

		
		
	;Else If menutimer=351
	;	Delay 1000
		
	Else If menutimer<1000
		If menutimer=400 Or menutimer=700 Or menutimer=850
			playsoundfxnow(123)
		EndIf
		TranslateEntity camera,0,0,-.005-Float(menutimer-350)/85000.0
		
	
			EntityAlpha cloudplane,0.2+35.0/45.0-0.3*Sin(Float(menutimer-350)/3.61)

			TranslateEntity cloudplane,(0.0025+(menutimer-350.0)/130000.0),0.001,0
		
		ScaleTexture cloudplanetexture,1+(menutimer-350.0)/900.0,1+(menutimer-350.0)/900.0

		ChannelVolume MusicChannel,GlobalMusicVolume*(1.0-(menutimer-350.0)/650.0)

		
	Else If menutimer=1000
	;	Delay 1000
		ScaleEntity waterplane,20,20,1
;		PositionEntity waterplane,0,0,0.51
		FreeTexture waterplanetexture
		waterplanetexture=myLoadTexture ("Data/graphics/map/endmap3.jpg",4)
		EntityTexture waterplane,waterplanetexture
		EntityAlpha waterplane,0
			
	Else If menutimer<3000
	
		If menutimer=1300
			For i=31 To 69
				If i<>60
					titlemenuentity(i)=CopyEntity(starmesh)
					RotateEntity Titlemenuentity(i),-90,0,0
					sc#=Rnd(1.1,2.1)
					ScaleEntity titlemenuentity(i),sc,sc,sc
					Repeat
						a=Rnd(-90,90)
						b=Rnd(-70,70)
					Until a<-40 Or b<-40 Or a>40 Or b>40
					PositionEntity titlemenuentity(i),a,b,50
				EndIf
			Next
			For i=71 To 77
				titlemenuentity(i)=CopyEntity(ObstacleMesh(48))
				PositionEntity titlemenuentity(i),16*Sin(i*50+i*i*5),8*Cos(i*50+i*i*5),-((i*10) Mod 70 + 90)
				RotateEntity titlemenuentity(i),0,90,0;((i*30) Mod 60)-30
			Next
		EndIf
		
		TurnEntity waterplane,0,0,.04

		If menutimer>1300
			For i=31 To 69
				If i<>60	TurnEntity titlemenuentity(i),0,i/20.0,i/100.0
			Next
			For i=71 To 77
				TurnEntity titlemenuentity(i),0,.4,0;,(((i*30) Mod 100)-50)/100.0
			Next

		EndIf

		If menutimer=2050
			
			If ChannelPlaying(musicchannel)=1
				StopChannel(musicchannel)
			EndIf
			MusicChannel=PlayMusic ("data\music\6.ogg")
			ChannelVolume MusicChannel,GlobalMusicVolume
			currentmusic=6
		EndIf
	
	
	
		If menutimer<1500
			TranslateEntity camera,0,0,-.007-Float(menutimer-1000)/4000.0
		Else
			TranslateEntity camera,0,0,-.132+0.132*Float(menutimer-1500)/2500
		EndIf
		
		
			EntityAlpha cloudplane,.88-Float(menutimer-1000)/200.0
			EntityAlpha waterplane,Float(menutimer-1000)/50.0

			TranslateEntity cloudplane,(0.0025+(menutimer-350.0)/130000.0),0.001,0

		If menutimer>2900
			AmbientLight 250-(menutimer-2900)*2.5,250-(menutimer-2900)*2.5,250-(menutimer-2900)*2.5
		EndIf
		
	Else If menutimer=3000
		For i=31 To 77
		If i<>70 And i<>60 HideEntity Titlemenuentity(i)
		Next
		HideEntity cloudplane
		HideEntity waterplane
	Else If menutimer=3001
		AmbientLight 255,255,255
		
	Else If menutimer<3040
			
	Else If menutimer<3400
		
		displaytext("IS THIS THE END OF WONDERLAND?",10,20,1,1,255*Sin((menutimer-3040)/2),0,0)
		ChannelVolume MusicChannel,GlobalMusicVolume*(1.0-(menutimer-3040.0)/360.0)

		
		
	Else If menutimer=3400
		For i=31 To 69
			If i<>60
				ShowEntity Titlemenuentity(i)
				PositionEntity Titlemenuentity(i),0,0,0
			EndIf
		Next
		titlemenuentity(70)=CopyMesh(starmesh)
		RotateEntity Titlemenuentity(70),-90,0,0
		EntityTexture titlemenuentity(70),wisptexture(0)
		PositionEntity titlemenuentity(70),0,0,-173
		If ChannelPlaying(musicchannel)=1
			StopChannel(musicchannel)
		EndIf
		MusicChannel=PlayMusic ("data\music\7.ogg")
		ChannelVolume MusicChannel,GlobalMusicVolume
		currentmusic=7

	Else If menutimer<3900
	
		For i=31 To 69
			If i<>60 PositionEntity Titlemenuentity(i),.20*(menutimer-3400)*Sin((i-31)*9.47),.20*(menutimer-3400)*Cos((i-31)*9.47),-120
		Next
		TurnEntity Titlemenuentity(70),0,.05*Sin(menutimer-3400),0
		displaytext("'Not if I have anything to do with it!!'",5,20+0.2*Sin(menutimer*10),1,1,255,255,255)

	Else If menutimer=3900
		HideEntity titlemenuentity(70)
		ShowEntity waterplane
		SoundPitch soundfx(80),11025
		playsoundfxnow(80)
	Else If menutimer<4000
		PositionEntity camera,0,0,-180+1.79*(menutimer-3900)
		

	
	Else If menutimer=4000
		FreeEntity waterplane
		waterplane=0
		FreeEntity cloudplane
		cloudplane=0
		For i=31 To 77
			If i<>60 FreeEntity titlemenuentity(i)
				
		Next
		SoundPitch soundfx(80),22050

		
		
		
		endmenu()
		
		starthublevel(-75,12,45,False)

		

			
		
	EndIf
		
	
	
	
	
	


		
	





End Function

Function UpdateCutScene3()

	LightColor SpotLight,0,0,0
	LightColor LevelLight,0,0,0

	ControlParticles()
		
	If menutimer<40
		AmbientLight (menutimer-10)*8,(menutimer-10)*8,(menutimer-10)*8
	EndIf

	If menutimer<10

	Else If menutimer=10
		If waterplane>0
			FreeEntity waterplane
			waterplane=0
		EndIf
		waterplane =CreateCube ()
		ScaleEntity waterplane,4,2,1
		PositionEntity waterplane,0,-2,5
		If waterplanetexture>0
			FreeTexture waterplanetexture
		EndIf
		waterplanetexture=myLoadTexture ("Data/graphics/map/endmap3.jpg",4)
		EntityTexture waterplane,waterplanetexture
		
		PositionEntity camera,0,0,0
		RotateEntity camera,0,0,10
		
		For i=31 To 69
			If i<>60
				titlemenuentity(i)=CopyEntity(starmesh)
				RotateEntity Titlemenuentity(i),-90,0,0
				sc#=Rnd(1.1,2.1)
				ScaleEntity titlemenuentity(i),sc,sc,sc
				;Repeat
					a=Rnd(-80,80)
					b=Rnd(0,60)
				;Until a<-40 Or b<-40 Or a>40 Or b>40
				PositionEntity titlemenuentity(i),a,b,150
			EndIf
		Next
		
			
		titlemenuentity(70)=CopyEntity(ObstacleMesh(48))
				PositionEntity titlemenuentity(70),0,-1,5
				RotateEntity titlemenuentity(i),0,90,0;((i*30) Mod 60)-30
				ScaleEntity titlemenuentity(70),.1,.1,.1


		
		
	
	Else If menutimer<1000
	
		AddParticle(Rand(16,23),EntityX(titlemenuentity(70)),EntityY(titlemenuentity(70)),EntityZ(titlemenuentity(70)),0,0.0005,Rnd(-.002,.002),Rnd(-.002,.002),Rnd(-.001,.001),0.1,0.0005,0,0,0,100,3)

	
		If menutimer<500 ScaleEntity titlemenuentity(70),menutimer/5000.0,menutimer/5000.0,menutimer/5000.0
	
		If menutimer=290 Then playsoundfxnow(80)
		If menutimer=360 Then playsoundfxnow(15)
	
		If menutimer<360
			PositionEntity titlemenuentity(70),-1+2*Sin(menutimer/6.0),-1+2*Sin(menutimer/12.0),5-3*Sin(menutimer/4.0)
		Else
			PositionEntity titlemenuentity(70),-1+2*Sin(menutimer/6.0),(menutimer-360)*(menutimer-360)/50.0,2+(menutimer-360)*(menutimer-360)/10.0;12-10*Sin(menutimer/4.0)
		EndIf
		
		If menutimer=390
			For i=1 To 100
				AddParticle(Rand(16,23),EntityX(titlemenuentity(70)),EntityY(titlemenuentity(70)),EntityZ(titlemenuentity(70)),0,0.01,Rnd(-.1,.1),Rnd(-.1,.1),Rnd(-.001,.001),0.1,0.01,0,0,0,100,3)
			Next
		EndIf
			
	
		TranslateEntity camera,0,0,-.0027
		
		TurnEntity camera,0,0,0.02
		
		TurnEntity titlemenuentity(70),0,1,0
		
		For i=31 To 69
			If i<>60 TurnEntity titlemenuentity(i),0,(i Mod 4)-1,0
		Next
		
		
			
		If menutimer>900
			AmbientLight 250-2.5*(menutimer-900),250-2.5*(menutimer-900),250-2.5*(menutimer-900)
		EndIf	
		
	Else If menutimer=1000
		FreeEntity waterplane
		waterplane=0
	
		For i=31 To 70
			If i<>60 FreeEntity titlemenuentity(i)
		Next
		
	Else If menutimer<1360
		
		displaytext("And so, the Stinkers embarked on",9,20,1,1,255*Sin((menutimer-1000)/2),255*Sin((menutimer-1000)/2),255*Sin((menutimer-1000)/2))
		displaytext("another adventure to save Wonderland.",6.5,21,1,1,255*Sin((menutimer-1000)/2),255*Sin((menutimer-1000)/2),255*Sin((menutimer-1000)/2))
		
	Else If menutimer<1720
		
		displaytext("An adventure that would take them",8.5,20,1,1,255*Sin((menutimer-1360)/2),255*Sin((menutimer-1360)/2),255*Sin((menutimer-1360)/2))
		displaytext("to strange worlds far beyond the stars,",5.5,21,1,1,255*Sin((menutimer-1360)/2),255*Sin((menutimer-1360)/2),255*Sin((menutimer-1360)/2))
		displaytext("and all the way to the PLANET OF THE Z-BOTS.",2,22,1,1,255*Sin((menutimer-1360)/2),255*Sin((menutimer-1360)/2),255*Sin((menutimer-1360)/2))
		
	Else If menutimer<2180
	
		displaytext("But that is another story",12.5,20,1,1,255*Sin((menutimer-1720)/2),255*Sin((menutimer-1720)/2),255*Sin((menutimer-1720)/2))
		displaytext("for another day.",17,21,1,1,255*Sin((menutimer-1720)/2),255*Sin((menutimer-1720)/2),255*Sin((menutimer-1720)/2))
		
	Else If menutimer<2540
	
		displaytext("THE END",21.5,20,1,1,255*Sin((menutimer-2180)/2),255*Sin((menutimer-2180)/2),0)
	;	displaytext("Thank you for playing.",10,22,1,1,255*Sin((menutimer-1360),255*Sin((menutimer-1360),255*Sin((menutimer-1360))
	
	Else If menutimer<2800
	
	Else If menutimer<3500
		displaytext("Congratulations!",17,12,1,1,255,255,0)
		displaytext("You have completed",16,16,1,1,255,255,255)
		displaytext("WONDERLAND ADVENTURES",14.5,17,1,1,255,255,0)
		displaytext("MYSTERIES OF FIRE ISLAND",13,18,1,1,255,255,0)
		
		displaytext("Thank you for playing!",14,22,1,1,255,255,255)

		If menutimer>3400
			ChannelVolume MusicChannel,GlobalMusicVolume*(1.0-(menutimer-3400.0)/100.0)
		EndIf



	Else If menutimer=3500


		endmenu()
		startmenu(11)

		

		

			
	EndIf


End Function

Function createsquare()

	m=CreateMesh(camera)
	s=CreateSurface(m)
	AddVertex (s,-.5,0,.5,0,0)
	AddVertex (s,.5,0,.5,1,0)
	AddVertex (s,-.5,0,-.5,0,1)
	AddVertex (s,.5,0,-.5,1,1)
	AddTriangle (s,0,1,2)
	AddTriangle (s,1,3,2)
	UpdateNormals m
	Return m

End Function	

Function StartCustomSelectMenu()

	InCustomHub=0

	GameMode=12
	CurrentMenu=100
	MenuTimer=0
	
	; camera/lights
	PositionEntity camera,0,0,0
	RotateEntity camera,0,0,0
	AmbientLight 255,255,255
	RotateEntity levellight,35,-35,0
	CameraFogMode camera,0
	CameraRange camera, 0.1,1000
	
	; music	
	If currentmusic<>8
		If globalmusicvolume2>0
			If ChannelPlaying(musicchannel)
				StopChannel musicchannel
			EndIf
			MusicChannel=PlayMusic ("data\music\8.ogg")
			ChannelVolume MusicChannel,GlobalMusicVolume
			LevelMusicCustomVolume=100.0
			LevelMusicCustomPitch=44

			currentmusic=8
		Else 
			currentmusic=-99 ; remember that -1 is soundscape beach
		EndIf
	EndIf
	
	; standard entities
	For i=79 To 99
		ShowEntity TitleMenuEntity(i)
	Next
	
	For i=0 To 79
		deleteicon(i)
	Next

	
	ShowEntity MouseCursor
	MouseCursorVisible=True

	
	CheckForNewCompiledLevels(True)
	
	
	; refresh the tag list
	LoadCustomTags()
	
	
	
	; get levels
	If CustomCurrentArchive=0
		GetCurrentCustomLevels()
;	Else
;		GetArchiveCustomLevels()
	EndIf
	
	CustomLevelListSelected=-1
	


End Function

Function GetCurrentCustomLevels()

	; clear
	For nofcustomlevels=0 To 4999
		CustomLevelListFileName$(NofCustomLevels)=""
		CustomLevelListName$(NofCustomLevels)=""
		CustomLevelListCreator$(NofCustomLevels)=""		
		CustomLevelListGems(NofCustomLevels)=0
		CustomLevelListGemsTotal(NofCustomLevels)=0
		CustomLevelListCoins(NofCustomLevels)=0
		CustomLevelListCoinsTotal(NofCustomLevels)=0
		CustomLevelListScore(NofCustomLevels)=0
	Next
	
	CustomCurrentArchive=0
	NofCustomLevels=0
	dirfile=ReadDir (GlobalDirName$+"\Custom\Adventures")
	Repeat
		CustomLevelListFileName$(NofCustomLevels)=""
		CustomLevelListName$(NofCustomLevels)=""
		CustomLevelListCreator$(NofCustomLevels)=""		
		CustomLevelListGems(NofCustomLevels)=0
		CustomLevelListGemsTotal(NofCustomLevels)=0
		CustomLevelListCoins(NofCustomLevels)=0
		CustomLevelListCoinsTotal(NofCustomLevels)=0
		CustomLevelListScore(NofCustomLevels)=0

		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(GlobalDirName$+"\Custom\Adventures\"+ex$)=2
		
			file=ReadFile(GlobalDirName$+"\Custom\Adventures\"+ex$+"\master.dat")
			If file>0
			
				; ok, a potentially valid adventure is found
				
				; now check if it matches our current "tag"
				flag2=False
				; check if there is a tag at all (for unarchived)
				dir2=ReadDir (GlobalDirName$+"\Custom\Adventures\"+ex$)
				Repeat
					dirfile2$=NextFile$(dir2)
					If Right$(Upper$(dirfile2$),4)=".TAG" Then  flag2=True
				Until dirfile2$=""
				CloseDir dir2
				
				
				If CurrentCustomTag$="All" Or (CurrentCustomTag$="Not Archived" And flag2=False) Or FileType(GlobalDirName$+"\Custom\Adventures\"+ex$+"\"+currentcustomtag$+".tag")=1
			
					CustomLevelListFileName$(NofCustomLevels)=ex$
					CustomLevelListName$(NofCustomLevels)=ReadString$(file)
					For i=0 To 4
						ReadString$(file)
					Next
					CustomLevelListCreator$(NofCustomLevels)=ReadString$(file)
					If CustomLevelListCreator$(NofCustomLevels)="" Then CustomLevelListCreator$(NofCustomLevels)="(Unknown)"
					CloseFile(file)
					CustomLevelListCompleted(NofCustomLevels)=0
					file=ReadFile(GlobalDirName$+"\Custom\Adventures\"+ex$+"\"+playername$+".score")
					If file>0
						
						ReadInt(file)
						ReadInt(file)
						CustomLevelListGems(NofCustomLevels)=ReadInt(file)
						CustomLevelListGemsTotal(NofCustomLevels)=ReadInt(file)
						CustomLevelListCoins(NofCustomLevels)=ReadInt(file)
						CustomLevelListCoinsTotal(NofCustomLevels)=ReadInt(file)
						CustomLevelListScore(NofCustomLevels)=ReadInt(file)
						CloseFile(file)
						CustomLevelListCompleted(NofCustomLevels)=1
						If CustomLevelListCoinsTotal(NofCustomLevels)=CustomLevelListCoins(NofCustomLevels) And CustomLevelListGemsTotal(NofCustomLevels)=CustomLevelListGems(NofCustomLevels) Then CustomLevelListCompleted(NofCustomLevels)=2
					EndIf
				
					NofCustomLevels=NofCustomLevels+1
				Else
					CloseFile (file)
				EndIf
			EndIf
		EndIf
	Until ex$=""
	
	CloseDir dirfile
	
	If NofCustomLevels=0 And CurrentCustomTag$<>"All" And CurrentCustomTag$<>"Not Archived"
		; no levels in this tag - remove tag and move on
		RemoveCurrentCustomTag()
		GetCurrentCustomlevels()
	EndIf
		

End Function



Function CustomSelectMenu()

	menutimer=menutimer+1
	If MouseDown(1)=False And MouseDown(2)=False 
		MouseGameMode=-1
	EndIf
	
	;If widescreen
	;	widthw#=Gfxwidth
	;Else
	;	widthw#=800
	;EndIf
	
	;PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	;widescreen
	If widescreen
		PositionEntity MouseCursor,-13.28+26.625*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	Else 
		PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	EndIf

	HideEntity Titlemenuentity(1)
	HideEntity Titlemenuentity(2)
	TurnEntity Titlemenuentity(79),0,0,0.006

	For i=81 To 99
		; move stars
		TranslateEntity TitleMenuEntity(i),-(Float(i)/10000.0),-(Float(i*2)/10000.0),0
		If  EntityY(TitleMenuEntity(i))<-12
			FreeEntity TitleMenuEntity(i)
			titlemenuentity(i)=CopyEntity(starmesh)
			RotateEntity Titlemenuentity(i),-90,0,0
			sc#=Rnd(0.8,1)
			ScaleEntity titlemenuentity(i),sc,sc,sc
			EntityFX titlemenuentity(i),1
			PositionEntity titlemenuentity(i),Rnd(-60,90),65,145
		EndIf
		TurnEntity TitleMenuEntity(i),0,.5,0
	Next
	
	RotateEntity TitleMenuEntity(60),-90,0,0
	
	TurnEntity TitleMenuEntity(60),-0,5*Sin(menutimer Mod 360),0

	
	
	
	DisplayText("Select A Custom Adventure:",25-13,0.7,1,1,255,255,0)
	If widescreen
		DisplayText("Adventure Name                                         Creator                    Complete",-11,3,0.7,1,255,255,0)
		DisplayText("-----------------------------------------------------------------------------------------------",-12,3.6,0.7,1,255,255,0)
	Else 
		DisplayText("Adventure Name                         Creator            Complete",1,3,0.7,1,255,255,0)
		DisplayText("-----------------------------------------------------------------------",0,3.6,0.7,1,255,255,0)
	EndIf
	For i=0 To 14
		If CustomLevelListName$(i+CustomLevelListStart)<>""
			col1=200
			col2=200
			shiver#=0.0
			If CustomLevelListSelected=-1
				; selecting level
				If MouseY()>=(90.0+i*26.5)*gfxheight/600.0 And MouseY()<(90.0+(i+1)*26.5)*gfxheight/600.0 And MouseX()<750*Gfxwidth/800.0
					col1=255
					col2=150
					shiver#=0.2
				EndIf
			Else If CustomLevelListSelected=i+CustomLevelListStart
				; in play/delete phase
				col1=255
				col2=255
				shiver#=0.0

			EndIf
			extra=0
			If widescreen
				DisplayText(CustomLevelListName$(i+CustomLevelListStart),-11+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,col1,col1,col2)
				DisplayText(CustomLevelListCreator$(i+CustomLevelListStart),44+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,col1,col1,col2)
				extra=12
			Else
				DisplayText(CustomLevelListName$(i+CustomLevelListStart),1+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,col1,col1,col2)
				DisplayText(CustomLevelListCreator$(i+CustomLevelListStart),40+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,col1,col1,col2)
			EndIf
			Select CustomLevelListCompleted(i+CustomLevelListStart)
			Case 0
				DisplayText("NO",62+extra+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,255,100,100)
			Case 1
				DisplayText("YES",61.5+extra+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,0,255,0)
			Case 2
				DisplayText("GOLD",61+extra+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,255,255,0)
			End Select
		EndIf
					
	Next
	
	If NofCustomLevels>15
		extra=0
		If widescreen
			extra=12
		EndIf
		For i=0 To 14
			DisplayText(":  :",67+extra,4.5+i*1.5,0.7,1,255,255,0)
			
		Next
		If MouseX()>750*Gfxwidth/800.0 And MouseY()>82*gfxheight/600.0 And MouseY()<142*gfxheight/600.0
			DisplayText("Pg",68+extra+0.1*Sin(menutimer Mod 360),4.5+0.1*Cos(menutimer Mod 360),0.7,1,255,255,155)
			DisplayText("Up",68+extra+0.1*Sin(menutimer Mod 360),6+0.1*Cos(menutimer Mod 360),0.7,1,255,255,155)
		Else
			DisplayText("Pg",68+extra,4.5,0.7,1,200,200,200)
			DisplayText("Up",68+extra,6,0.7,1,200,200,200)
		EndIf		
		DisplayText("--",68+extra,6.9,0.7,1,255,255,0)
		DisplayText("--",68+extra,23.1,0.7,1,255,255,0)
		If MouseX()>750*Gfxwidth/800.0 And MouseY()>422*gfxheight/600.0 And MouseY()<482*gfxheight/600.0
			DisplayText("Pg",68+extra+0.1*Sin(menutimer Mod 360),24+0.1*Cos(menutimer Mod 360),0.7,1,255,255,155)
			DisplayText("Dn",68+extra+0.1*Sin(menutimer Mod 360),25.5+0.1*Cos(menutimer Mod 360),0.7,1,255,255,155)
		Else
			DisplayText("Pg",68+extra,24,0.7,1,200,200,200)
			DisplayText("Dn",68+extra,25.5,0.7,1,200,200,200)
		EndIf	
	
	EndIf
	If widescreen
		DisplayText("-----------------------------------------------------------------------------------------------",-12,26.3,0.7,1,255,255,0)
	Else 
		DisplayText("-----------------------------------------------------------------------",0,26.3,0.7,1,255,255,0)
	EndIf
	
	If CustomLevelListSelected>=0
		DisplayText(CustomLevelListName$(CustomLevelListSelected),25-0.5*Len(CustomLevelListName$(CustomLevelListSelected)),19.3,1,1,255,255,255)
		
		If CustomLevelListCompleted(CustomLevelListSelected)=0
			b$="This Adventure Has Not Been Completed."
		Else
			b$="Score: "+CustomLevelListScore(CustomLevelListSelected)+"  Gems: "+CustomLevelListGems(CustomLevelListSelected)+" of "+CustomLevelListGemsTotal(CustomLevelListSelected)+"  Coins: "+CustomLevelListCoins(CustomLevelListSelected)+" of "+CustomLevelListCoinsTotal(CustomLevelListSelected)
		EndIf
		DisplayText(b$,25-0.5*Len(b$),20.3,1,1,255,255,255)
		
		If widescreen
			If MouseY()>540*gfxheight/600.0 And MouseX()>110*Gfxwidth/800.0 And MouseX()<210*Gfxwidth/800.0
				DisplayText(">PLAY<",2+0.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("PLAY",3,22,1,1,200,200,200)
			EndIf
			
			If MouseY()>540*gfxheight/600.0 And MouseX()>270*Gfxwidth/800.0 And MouseX()<370*Gfxwidth/800.0
				DisplayText(">CANCEL<",14+.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("CANCEL",15,22,1,1,200,200,200)
			EndIf
			
			If MouseY()>540*gfxheight/600.0 And MouseX()>430*Gfxwidth/800.0 And MouseX()<530*Gfxwidth/800.0
				DisplayText(">ARCHIVE<",27.5+.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("ARCHIVE",28.5,22,1,1,200,200,200)
			EndIf

			
			If MouseY()>540*gfxheight/600.0 And MouseX()>590*Gfxwidth/800.0 And MouseX()<690*Gfxwidth/800.0
				DisplayText(">DELETE<",41.5+0.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("DELETE",42.5,22,1,1,200,200,200)
			EndIf
		Else
			If MouseY()>540*gfxheight/600.0 And MouseX()>110*Gfxwidth/800.0 And MouseX()<210*Gfxwidth/800.0
				DisplayText(">PLAY<",7+0.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("PLAY",8,22,1,1,200,200,200)
			EndIf
			
			If MouseY()>540*gfxheight/600.0 And MouseX()>270*Gfxwidth/800.0 And MouseX()<370*Gfxwidth/800.0
				DisplayText(">CANCEL<",16+.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("CANCEL",17,22,1,1,200,200,200)
			EndIf
			
			If MouseY()>540*gfxheight/600.0 And MouseX()>430*Gfxwidth/800.0 And MouseX()<530*Gfxwidth/800.0
				DisplayText(">ARCHIVE<",25.5+.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("ARCHIVE",26.5,22,1,1,200,200,200)
			EndIf

			
			If MouseY()>540*gfxheight/600.0 And MouseX()>590*Gfxwidth/800.0 And MouseX()<690*Gfxwidth/800.0
				DisplayText(">DELETE<",36.5+0.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("DELETE",37.5,22,1,1,200,200,200)
			EndIf
		EndIf
	Else
		If currentcustomtag$="All" Or currentcustomtag$="Not Archived"
			b$="Show: "+currentcustomtag$
		Else
			b$="Archive: "+currentcustomtag$
		EndIf
		If MouseY()>500*gfxheight/600.0 And MouseY()<530*gfxheight/600.0 And MouseX()>150*Gfxwidth/800.0 And MouseX()<660*Gfxwidth/800.0
			DisplayText(">"+b$+"<",24-0.5*Len(b$)+0.1*Sin(menutimer Mod 360),20+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
		Else
			DisplayText(b$,25-0.5*Len(b$),20,1,1,200,200,200)
		EndIf

	
		If MouseY()>540*gfxheight/600.0 And MouseX()>350*Gfxwidth/800.0 And MouseX()<460*Gfxwidth/800.0
			DisplayText(" >EXIT<",21+0.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
		Else
			DisplayText(" EXIT",22,22,1,1,200,200,200)
		EndIf

		

	EndIf

	mouse1=MouseDown(1)
	mouse2=MouseDown(2)

	If (mouse1 Or mouse2) And MouseGameMode=-1
		MouseGameMode=2
		
		For i=0 To 14
			If MouseY()>=(90.0+i*26.5)*gfxheight/600.0 And MouseY()<(90.0+(i+1)*26.5)*gfxheight/600.0 And MouseX()<750*Gfxwidth/800.0
				If CustomLevelListName$(i)<>"" Then CustomLevellistselected=i+CustomLevelListStart
				PlaysoundfxNow(130)
			EndIf
		Next
		
			
		If Customlevellistselected>=0
			If MouseY()>540*gfxheight/600.0 And MouseX()>110*Gfxwidth/800.0 And MouseX()<210*Gfxwidth/800.0
				;play
				PlaysoundfxNow(131)
				EndCustomSelectMenu()
				b$=globaldirname$+"\custom\adventures\"
				
			
				
				StartAdventure(b$+CustomLevelListFileName$(customlevellistselected),2,0)
			EndIf
		
			If MouseY()>540*gfxheight/600.0 And MouseX()>270*Gfxwidth/800.0 And MouseX()<370*Gfxwidth/800.0
				PlaysoundfxNow(132)
				;cancel
				CustomLevelListselected=-1
			EndIf
			
			If MouseY()>540*gfxheight/600.0 And MouseX()>430*Gfxwidth/800.0 And MouseX()<530*Gfxwidth/800.0
				PlaysoundfxNow(132)
				;archive
				StartCustomArchiveMenu()
			EndIf

			
			If MouseY()>540*gfxheight/600.0 And MouseX()>590*Gfxwidth/800.0 And MouseX()<690*Gfxwidth/800.0
				;delete
				PlaysoundfxNow(135)
				StartCustomDeleteMenu()
			
			EndIf
		Else
			If MouseY()>500*gfxheight/600.0 And MouseY()<530*gfxheight/600.0 And MouseX()>150*Gfxwidth/800.0 And MouseX()<660*Gfxwidth/800.0
			
				;change tag
				If mouse1
 				
					currentcustomtagnumber=currentcustomtagnumber+1
					If currentcustomtagnumber>=nofcustomtags
						currentcustomtagnumber=-2
						currentcustomtag$="Not Archived"
					Else If currentcustomtagnumber=-1
						currentcustomtag$="All"
					Else
						currentcustomtag$=customtag$(currentcustomtagnumber)
					EndIf
				Else
					currentcustomtagnumber=currentcustomtagnumber-1
					If currentcustomtagnumber=-3
						currentcustomtagnumber=nofcustomtags-1
						currentcustomtag$=customtag$(currentcustomtagnumber)
						
					Else If currentcustomtagnumber=-2
						currentcustomtag$="Not Archived"
					Else If currentcustomtagnumber=-1
						currentcustomtag$="All"
					Else
						currentcustomtag$=customtag$(currentcustomtagnumber)
					EndIf
				EndIf
				If nofcustomtags>0 
					;endcustomselectmenu()
					;startcustomselectmenu()
					GetCurrentCustomLevels()
				EndIf
			EndIf
		
			If MouseY()>540*gfxheight/600.0 And MouseX()>350*Gfxwidth/800.0 And MouseX()<460*Gfxwidth/800.0

				;exit
				PlaysoundfxNow(132)
				EndCustomSelectMenu()
				StartMenu(11)
				
			EndIf

		
		EndIf
		
		If MouseX()>750*Gfxwidth/800.0 And MouseY()>82*gfxheight/600.0 And MouseY()<142*gfxheight/600.0
			CustomLevelListStart=CustomLevelListStart-15
			PlaysoundfxNow(130)
			
		EndIf
		If MouseX()>750*Gfxwidth/800.0 And MouseY()>422*gfxheight/600.0 And MouseY()<482*gfxheight/600.0
			CustomLevelListStart=CustomLevelListStart+15
			PlaysoundfxNow(130)
			
		EndIf
	EndIf
		
	If CustomLevelListStart>NofCustomLevels-15 Then CustomLevelListStart=NofCustomLevels-15
	If CustomLevelListStart<0 Then CustomLevelListStart=0
	
	; refresh with f12
	If KeyDown(88) 
		StartCustomSelectMenu()
		PlaysoundfxNow(130)
	EndIf		

	
		
;	displaytext nofcustomlevels,0,0,1,1,255,255,255		


End Function

Function EndCustomSelectMenu()
	For i=79 To 99
		HideEntity TitleMenuEntity(i)
	Next
End Function

Function LoadCustomTags()

	nofcustomtags=0
	CurrentCustomTagNumber=-2
	file=ReadFile(globaldirname$+"\custom\adventures\current.tags")
	If file>0
		Repeat
			If Eof(file)=False
				CustomTag$(nofcustomtags)=ReadString$(file)

			EndIf
			If CurrentCustomTag$=customtag$(nofcustomtags) currentcustomtagnumber=nofcustomtags
			nofcustomtags=nofcustomtags+1
		Until Eof(file)
		CloseFile file
		If currentcustomtagnumber=-1 Or nofcustomtags=0 Then currentcustomtag$="All"
	Else
		CurrentCustomTag$="Not Archived"
		CurrentCustomTagNumber=-2
	EndIf
End Function

Function SaveCustomTags()
	
	file=WriteFile(globaldirname$+"\custom\adventures\current.tags")
	For i=0 To NofCustomTags-1
		WriteString file,CustomTag$(i)
	Next
	CloseFile file
		

End Function

Function RemoveCurrentCustomTag()

	If CurrentCustomTagNumber=NofCustomTags-1
		; on the last one
		NofCustomTags=NofCustomTags-1
		CustomTag$(NofCustomTags)=""
		CurrentCustomTagNumber=-1
		CurrentCustomTag$="All"
	Else
		For i=CurrentCustomTagNumber To NofCustomTags-1
			CustomTag$(i)=CustomTag$(i+1)
		Next
		NofCustomTags=NofCustomTags-1
		CustomTag$(NofCustomTags)=""
		CurrentCustomTag$=CustomTag$(CurrentCustomTagNumber)
	EndIf
	SaveCustomTags()
	
	
		
			

End Function

Function StartCustomDeleteMenu()
	CurrentMenu=101
	
	
End Function

Function CustomDeleteMenu()

	menutimer=menutimer+1
	If MouseDown(1)=False And MouseDown(2)=False 
		MouseGameMode=-1
	EndIf
	
	;PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	;widescreen
	If widescreen
		PositionEntity MouseCursor,-13.28+26.625*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	Else 
		PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	EndIf
	
	HideEntity Titlemenuentity(1)
	HideEntity Titlemenuentity(2)
	TurnEntity Titlemenuentity(79),0,0,0.006

	For i=81 To 99
		; move stars
		TranslateEntity TitleMenuEntity(i),-(Float(i)/10000.0),-(Float(i*2)/10000.0),0
		If  EntityY(TitleMenuEntity(i))<-12
			FreeEntity TitleMenuEntity(i)
			titlemenuentity(i)=CopyEntity(starmesh)
			RotateEntity Titlemenuentity(i),-90,0,0
			sc#=Rnd(0.8,1)
			ScaleEntity titlemenuentity(i),sc,sc,sc
			EntityFX titlemenuentity(i),1
			PositionEntity titlemenuentity(i),Rnd(-60,90),65,145
		EndIf
		TurnEntity TitleMenuEntity(i),0,.5,0
	Next
	
	RotateEntity TitleMenuEntity(60),-90,0,0
	
	TurnEntity TitleMenuEntity(60),-0,5*Sin(menutimer Mod 360),0

	
	b$="Delete Custom Adventure?"
	DisplayText(b$,25-0.5*Len(b$),4.5,1,1,255,255,0)
	b$="------------------------"
	DisplayText(b$,25-0.5*Len(b$),5.3,1,1,255,255,0)

	b$=CustomLevelListName$(CustomLevelListSelected)
	DisplayText(b$,25-0.5*Len(b$),7.5,1,1,255,255,255)
	b$="by"
	DisplayText(b$,25-0.5*Len(b$),8.45,1,1,255,255,0)
	b$=CustomLevelListCreator$(CustomLevelListSelected)
	DisplayText(b$,25-0.5*Len(b$),9.5,1,1,255,255,255)
	
	b$="Please Confirm:"
	DisplayText(b$,25-0.5*Len(b$),12,1,1,255,255,0)
	b$="Yes, Delete This Adventure"
	If MouseY()>340*GfxHeight/600.0 And MouseY()<370*GfxHeight/600.0
		DisplayText(">"+b$+"<",24-0.5*Len(b$)+0.1*Sin(menutimer Mod 360),13.5+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
	Else
		DisplayText(b$,25-0.5*Len(b$),13.5,1,1,200,200,200)
	EndIf
	b$="No, Keep This Adventure"
	If MouseY()>370*GfxHeight/600.0 And MouseY()<405*GfxHeight/600.0
		DisplayText(">"+b$+"<",24-0.5*Len(b$)+0.1*Sin(menutimer Mod 360),15+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
	Else
		DisplayText(b$,25-0.5*Len(b$),15,1,1,200,200,200)
	EndIf


	If MouseDown(1) And MouseGameMode=-1
		MouseGameMode=2
	
		If MouseY()>340*GfxHeight/600.0 And MouseY()<370*GfxHeight/600.0
			PlaysoundfxNow(132)
			dirfile=ReadDir(GlobalDirName$+"\Custom\Adventures\"+CustomLevelListFileName$(CustomLevelListSelected))
			Repeat
				ex2$=NextFile$(dirfile)
				If ex2$<>"" And ex2$<>"." And ex2$<>".."
					DeleteFile GlobalDirName$+"\Custom\Adventures\"+CustomLevelListFileName$(CustomLevelListSelected)+"\"+ex2$
				EndIf
			Until ex2$=""
			CloseDir dirfile
			DeleteDir GlobalDirName$+"\Custom\Adventures\"+CustomLevelListFileName$(CustomLevelListSelected)
			StartCustomSelectMenu()

		EndIf
	
		If MouseY()>370*GfxHeight/600.0 And MouseY()<405*GfxHeight/600.0
			PlaysoundfxNow(130)
			CurrentMenu=100
			CustomLevelListSelected=-1
		EndIf

	EndIf
	


	


	
;	displaytext MouseX()+" "+MouseY(),0,0,1,1,255,255,255		


End Function

Function StartCustomArchiveMenu()
	CurrentMenu=103
	NewTagNameEntered$=""
	Repeat
	Until GetKey()=False
End Function

Function CustomArchiveMenu()

	
	menutimer=menutimer+1
	If MouseDown(1)=False And MouseDown(2)=False 
		MouseGameMode=-1
	EndIf
	
	;PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	;widescreen
	If widescreen
		PositionEntity MouseCursor,-13.28+26.625*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	Else 
		PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	EndIf
	
	HideEntity Titlemenuentity(1)
	HideEntity Titlemenuentity(2)
	TurnEntity Titlemenuentity(79),0,0,0.006

	For i=81 To 99
		; move stars
		TranslateEntity TitleMenuEntity(i),-(Float(i)/10000.0),-(Float(i*2)/10000.0),0
		If  EntityY(TitleMenuEntity(i))<-12
			FreeEntity TitleMenuEntity(i)
			titlemenuentity(i)=CopyEntity(starmesh)
			RotateEntity Titlemenuentity(i),-90,0,0
			sc#=Rnd(0.8,1)
			ScaleEntity titlemenuentity(i),sc,sc,sc
			EntityFX titlemenuentity(i),1
			PositionEntity titlemenuentity(i),Rnd(-60,90),65,145
		EndIf
		TurnEntity TitleMenuEntity(i),0,.5,0
	Next
	
	RotateEntity TitleMenuEntity(60),-90,0,0
	
	TurnEntity TitleMenuEntity(60),-0,5*Sin(menutimer Mod 360),0
	If widescreen
		b$="Select Archive Folder:"
		DisplayText(b$,-8,2,1,1,255,255,0)
		b$="------------------------------------------------------------------"
		DisplayText(b$,-8,3,1,1,255,255,0)
		ex=3
	Else
		b$="Select Archive Folder:"
		DisplayText(b$,0,2,1,1,255,255,0)
		b$="--------------------------------------------------"
		DisplayText(b$,0,3,1,1,255,255,0)
		ex=0
	EndIf
	For i=0 To 11
		DisplayText(":",16-ex,4+i,1,1,255,255,0)
		DisplayText(":",33+ex,4+i,1,1,255,255,0)
	Next
	
	currentselectedtag=-2
	If MouseY()>=105*GfxHeight/600.0 And MouseY()<405*gfxHeight/600.0
		; in archive selection - get #
;		DisplayText(MouseY(),0,1,1,1,255,255,0)
;		DisplayText(MouseY()*600.0/GfxHeight,0,2,1,1,255,255,0)
;		DisplayText(MouseY()*600.0/GfxHeight-105.0,3,2,1,1,255,255,0)
;		DisplayText((MouseY()*600.0/GfxHeight-105.0)/25.0,0,4,1,1,255,255,0)
		row=Floor((Floor(MouseY()*600.0/GfxHeight)-105.0)/25.0)
		If row<0 Then row=0
		If row>11 Then row=11
		column=0
		If MouseX()>268*GfxWidth/800.0	column=1
		If MouseX()>268*2*GfxWidth/800.0 column=2
		currentselectedtag=row+column*12-1
	EndIf

	If currentselectedtag=-1
		col1=255
		col2=200
		shiver#=0.1
	Else
		col1=200
		col2=200
		shiver#=0.0
	EndIf
	If widescreen
		DisplayText("(Un-Archive)",0-8+shiver*Sin(menutimer Mod 360),4+shiver*Cos(menutimer Mod 360),1,1,col1,col1,col2)
	Else
		DisplayText("(Un-Archive)",0+shiver*Sin(menutimer Mod 360),4+shiver*Cos(menutimer Mod 360),1,1,col1,col1,col2)
	EndIf
	
	For i=0 To NofCustomTags-1
	
		If currentselectedtag=i
			col1=255
			col2=200
			shiver#=0.1
		Else
			col1=200
			col2=200
			shiver#=0.0
		EndIf
		If widescreen
			DisplayText(CustomTag$(i),Floor((i+1)/12)*(22+0.5*((Floor((i+1)/12)+1) Mod 2))-8+shiver*Sin(menutimer Mod 360),4+((i+1) Mod 12)+shiver*Cos(menutimer Mod 360),1,1,col1,col1,col2)
			;DisplayText(CustomTag$(i),Floor((i+1)/12)*22-8+shiver*Sin(menutimer Mod 360),4+((i+1) Mod 12)+shiver*Cos(menutimer Mod 360),1,1,col1,col1,col2)
		Else
			DisplayText(CustomTag$(i),Floor((i+1)/12)*17+shiver*Sin(menutimer Mod 360),4+((i+1) Mod 12)+shiver*Cos(menutimer Mod 360),1,1,col1,col1,col2)
		EndIf
	Next
		
	If widescreen
		b$="------------------------------------------------------------------"
		DisplayText(b$,-8,16,1,1,255,255,0)
	Else
		b$="--------------------------------------------------"
		DisplayText(b$,0,16,1,1,255,255,0)
	EndIf
	b$="OR Enter New Archive Folder Name:"+NewTagNameEntered$
	
	If menutimer Mod 100<50 Then b$=b$+"_"
	If widescreen
		DisplayText(b$,-8,17,1,1,255,255,0)
		b$="------------------------------------------------------------------"
		DisplayText(b$,-8,18,1,1,255,255,0)
	Else
		DisplayText(b$,0,17,1,1,255,255,0)
		b$="--------------------------------------------------"
		DisplayText(b$,0,18,1,1,255,255,0)
	EndIf
	If MouseY()>395*GfxHeight/480.0 And MouseY()<425*gfxHeight/480.0
		b$=">CANCEL<"
		col1=255
		col2=0
		shiver#=0.1
	Else
		b$="CANCEL"
		col1=200
		col2=200
		shiver#=0.0
	EndIf
	DisplayText(b$,25-0.5*Len(b$)+shiver*Sin(menutimer Mod 360),20+shiver*Cos(menutimer Mod 360),1,1,col1,col1,col2)



	

	


	If MouseDown(1) And MouseGameMode=-1
		MouseGameMode=2
		
		If CurrentSelectedTag>=-1
			
			; delete any tags from dir
			dir=ReadDir (globaldirname$+"\Custom\Adventures\"+CustomLevelListFileName$(customlevellistselected))
			Repeat
				dirfile$=NextFile$(dir)
				If Upper$(Right$(dirfile$,4))=".TAG"
					DeleteFile globaldirname$+"\Custom\Adventures\"+CustomLevelListFileName$(customlevellistselected)+"\"+dirfile$
		
				EndIf
			Until dirfile$=""
			CloseDir dir
		
			
			If currentselectedtag>=0
				; make new tag
				file=WriteFile(globaldirname$+"\Custom\Adventures\"+CustomLevelListFileName$(customlevellistselected)+"\"+CustomTag$(currentselectedtag)+".tag")
				CloseFile file
			EndIf
			
			PlaysoundfxNow(130)
			CurrentMenu=100
			CustomLevelListSelected=-1
			GetCurrentCustomLevels()

			
			
		EndIf
			
		; cancel
		If MouseY()>395*GfxHeight/480.0 And MouseY()<425*GfxHeight/480.0
			PlaysoundfxNow(130)
			CurrentMenu=100
			CustomLevelListSelected=-1
		EndIf

	EndIf
	
	
	key=GetKey()
		
	If key=13 And Lower$(newtagnameentered$)<>"all" And Lower$(newtagnameentered$)<>"not archived"
		customtag$(nofcustomtags)=newtagnameentered$
					
		
		; delete leading spaces
		If Left$(newtagnameentered$,1)=" "
			Repeat
				newtagnameentered$=Right$(newtagnameentered$,Len(newtagnameentered$)-1)
			Until Left$(newtagnameentered$,1)<>" "
		EndIf

		flag=True ; check if it's not a duplicate tag name
		For i=0 To nofcustomtags-1
			If Upper$(customtag$(i))=Upper$(newtagnameentered$) Then flag=False
		Next
		
		
		If newtagnameentered$<>""
			; ok - make this tag
			If flag=True
				customtag$(nofcustomtags)=newtagnameentered$
				nofcustomtags=nofcustomtags+1
				SaveCustomTags()
			EndIf
			; delete any tags from dir
			dir=ReadDir (globaldirname$+"\Custom\Adventures\"+CustomLevelListFileName$(customlevellistselected))
			Repeat
				dirfile$=NextFile$(dir)
				If Upper$(Right$(dirfile$,4))=".TAG"
					DeleteFile globaldirname$+"\Custom\Adventures\"+CustomLevelListFileName$(customlevellistselected)+"\"+dirfile$
				EndIf
			Until dirfile$=""
			CloseDir dir
			
			
			; make new tag
			file=WriteFile(globaldirname$+"\Custom\Adventures\"+CustomLevelListFileName$(customlevellistselected)+"\"+newtagnameentered$+".tag")
			CloseFile file
		
			PlaysoundfxNow(130)
			CurrentMenu=100
			CustomLevelListSelected=-1
			GetCurrentCustomLevels()
		
			
		EndIf

	Else If key=8
		newtagnameentered$=""
	Else If ((key>=65 And key<90) Or (key>=97 And key<=122) Or (key>=48 And key<=57) Or key=32) And Len(newtagnameentered$)<20
		
		newtagnameentered$=newtagnameentered$+Chr$(key)
		
	EndIf

	


	


	
;	displaytext MouseX()+" "+MouseY(),0,0,1,1,255,255,255		


End Function



Function StartWAEditorTitleMenu()
	CurrentMenu=102
	
	GameMode=12
	

	; camera/lights
	PositionEntity camera,0,0,0
	RotateEntity camera,0,0,0
	AmbientLight 255,255,255
	RotateEntity levellight,35,-35,0
	CameraFogMode camera,0
	CameraRange camera, 0.1,1000
	
	; music	
	If currentmusic<>13
		If globalmusicvolume2>0
			If ChannelPlaying(musicchannel)
				StopChannel musicchannel
			EndIf
			MusicChannel=PlayMusic ("data\music\13.ogg")
			ChannelVolume MusicChannel,GlobalMusicVolume
			currentmusic=13
			LevelMusicCustomVolume=100.0
			LevelMusicCustomPitch=44

		Else 
			currentmusic=-99 ; remember that soundscape -1 beach
		EndIf
	EndIf
	
	; standard entities
	For i=60 To 61
		ShowEntity TitleMenuEntity(i)
	Next
	For i=80 To 99
		ShowEntity TitleMenuEntity(i)
	Next
	
	For i=0 To 79
		deleteicon(i)
	Next

	
	ShowEntity MouseCursor
	MouseCursorVisible=True

		
	
End Function

Function WAEditorTitleMenu()

	menutimer=menutimer+1
	If MouseDown(1)=False And MouseDown(2)=False 
		MouseGameMode=-1
	EndIf
	
;	PositionTexture TitleMenuTexture(80),-(menutimer Mod 2500)/2500.0,(menutimer Mod 5000)/5000.0
	
	
	PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20

	For i=81 To 99
		; move stars
		TranslateEntity TitleMenuEntity(i),-(Float(i)/20000.0),-(Float(i*2)/20000.0),0
		If EntityX(TitleMenuEntity(i))<-14 Or EntityY(TitleMenuEntity(i))<0
			FreeEntity TitleMenuEntity(i)
			
			titlemenuentity(i)=CopyEntity(starmesh)
			RotateEntity Titlemenuentity(i),-90,0,0
			EntityFX titlemenuentity(i),1
			PositionEntity titlemenuentity(i),Rnd(-10,20),14,Rnd(20,30)

		EndIf
		TurnEntity TitleMenuEntity(i),0,1,0
	Next
	; move signs
	RotateEntity TitleMenuEntity(60),-90,0,0
	TurnEntity TitleMenuEntity(60),-0,5*Sin(menutimer Mod 360),0
	For i=0 To 3
		RotateEntity titlemenuentity(61+i),0,(12+(i Mod 2)*4)*Sin((((0.4+(i Mod 3)/6.0)*menutimer+i*70)) Mod 360),0
	Next

	b$="@ 2008 Midnight Synergy"
	DisplayText(b$,50-0.5*Len(b$),43,0.5,1,255,255,0)
	b$="www.midnightsynergy.com"
	DisplayText(b$,50-0.5*Len(b$),44.5,0.5,1,255,255,0)

	

End Function

Function EndWaEditorTitleMenu()


	For i=60 To 61
		HideEntity TitleMenuEntity(i)
	Next

	For i=80 To 99
		HideEntity TitleMenuEntity(i)
	Next
	
	


End Function

Function SetDialogContrast()

	Select DialogContrast
	
	Case 0
		EntityColor DialogBackGroundEntity,200,50,30
		EntityAlpha DialogBackGroundEntity,.5
		EntityFX DialogBackGroundEntity,0
		
	
		EntityColor DialogBackGroundEntity2,120,100,80
		EntityAlpha DialogBackGroundEntity2,.5
		EntityFX DialogBackGroundEntity2,0
		
	Case 1
		EntityColor DialogBackGroundEntity,0,0,0
		EntityAlpha DialogBackGroundEntity,.5
		EntityFX DialogBackGroundEntity,0
		
	
		EntityColor DialogBackGroundEntity2,0,0,0
		EntityAlpha DialogBackGroundEntity2,.5
		EntityFX DialogBackGroundEntity2,0



	
	Case 2
		EntityColor DialogBackGroundEntity,0,0,0
		EntityAlpha DialogBackGroundEntity,.5
		EntityFX DialogBackGroundEntity,14

		
	
		EntityColor DialogBackGroundEntity2,0,0,0
		EntityAlpha DialogBackGroundEntity2,.5
		EntityFX DialogBackGroundEntity2,14


		
	Case 3
		EntityColor DialogBackGroundEntity,0,0,0
		EntityAlpha DialogBackGroundEntity,0
		EntityFX DialogBackGroundEntity,0

		
	
		EntityColor DialogBackGroundEntity2,0,0,0
		EntityAlpha DialogBackGroundEntity2,0
		EntityFX DialogBackGroundEntity2,0

	End Select




End Function

Function StartCustomHubSelectMenu()

	GameMode=12
	CurrentMenu=120
	MenuTimer=0
	
	HideEntity titlemenuentity(60)
	ShowEntity titlemenuentity(61)
	
	
	; camera/lights
	PositionEntity camera,0,0,0
	RotateEntity camera,0,0,0
	AmbientLight 255,255,255
	RotateEntity levellight,35,-35,0
	CameraFogMode camera,0
	CameraRange camera, 0.1,1000
	
	; music	
	If currentmusic<>8
		If globalmusicvolume2>0
			If ChannelPlaying(musicchannel)
				StopChannel musicchannel
			EndIf
			MusicChannel=PlayMusic ("data\music\8.ogg")
			ChannelVolume MusicChannel,GlobalMusicVolume
			LevelMusicCustomVolume=100.0
			LevelMusicCustomPitch=44

			currentmusic=8
		Else 
			currentmusic=-99 ; remember that -1 is soundscape beach
		EndIf
	EndIf
	
	; standard entities
	For i=79 To 99
		ShowEntity TitleMenuEntity(i)
	Next
	
	For i=0 To 79
		deleteicon(i)
	Next

	
	ShowEntity MouseCursor
	MouseCursorVisible=True

	
;	CheckForNewCompiledLevels(True)
	
	
	
	; get hubs
	
	GetCurrentCustomHubs()
;	
	
	CustomLevelListSelected=-1
	


End Function

Function GetCurrentCustomHubs()

	; clear
	For nofcustomhubs=0 To 4999
		CustomHubListFileName$(NofCustomHubs)=""	
		CustomHubListName$(NofCustomHubs)=""
		CustomhubListSubTitle$(NofCustomHubs)=""		
	
	Next
	
	
	NofCustomhubs=0
	dirfile=ReadDir (GlobalDirName$+"\Custom\Hubs")
	Repeat
		CustomHubListFileName$(NofCustomHubs)=""
		CustomHubListName$(NofCustomHubs)=""
		CustomHubListSubTitle$(NofCustomHubs)=""		
		

		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(GlobalDirName$+"\Custom\Hubs\"+ex$)=2
		
			CustomHubListFileName$(NofCustomHubs)=ex$
			CustomHubListName$(NofCustomHubs)=ex$
			; parse
			For i=1 To Len(ex$)
				If Mid$(ex$,i,1)="#"
					CustomHubListName$(NofCustomHubs)=Left$(ex$,i-1)
					CustomHubListSubTitle$(NofCustomHubs)=Right$(ex$,Len(ex$)-i)
					i=10000
				EndIf
			Next
			If Len(CustomHubListName$(NofCustomHubs))>30
				CustomHubListName$(NofCustomHubs)=Left$(CustomHubListName$(NofCustomHubs),30)
			EndIf
			
			
			
			NofCustomHubs=NofCustomHubs+1
			
		EndIf
	Until ex$=""
	
	CloseDir dirfile
	
	
	
	
		

End Function



Function CustomHubSelectMenu()

	menutimer=menutimer+1
	If MouseDown(1)=False And MouseDown(2)=False 
		MouseGameMode=-1
	EndIf
	
	
	
	;PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	;widescreen
	If widescreen
		PositionEntity MouseCursor,-13.28+26.625*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	Else 
		PositionEntity MouseCursor,-10+20*Float(MouseX())/Float(GFXWidth),7.5-15*Float(MouseY())/Float(GfxHeight),20
	EndIf

	HideEntity Titlemenuentity(1)
	HideEntity Titlemenuentity(2)
	TurnEntity Titlemenuentity(79),0,0,0.006

	For i=81 To 99
		; move stars
		TranslateEntity TitleMenuEntity(i),-(Float(i)/10000.0),-(Float(i*2)/10000.0),0
		If  EntityY(TitleMenuEntity(i))<-12
			FreeEntity TitleMenuEntity(i)
			titlemenuentity(i)=CopyEntity(starmesh)
			RotateEntity Titlemenuentity(i),-90,0,0
			sc#=Rnd(0.8,1)
			ScaleEntity titlemenuentity(i),sc,sc,sc
			EntityFX titlemenuentity(i),1
			PositionEntity titlemenuentity(i),Rnd(-60,90),65,145
		EndIf
		TurnEntity TitleMenuEntity(i),0,.5,0
	Next
	
	RotateEntity TitleMenuEntity(60),-90,0,0
	
	TurnEntity TitleMenuEntity(60),-0,5*Sin(menutimer Mod 360),0

	
	
	
	
	
	;DisplayText("Hub Name                         ",1,3,0.7,1,255,255,0)
	;DisplayText("-----------------------------------------------------------------------",0,3.6,0.7,1,255,255,0)
	If widescreen
		DisplayText("Select A Custom Hub:",-8,1,1,1,255,255,0)
		DisplayText("-----------------------------------------------------------------------------------------------",-12,3.6,0.7,1,255,255,0)
	Else 
		DisplayText("Select A Custom Hub:",1,1,1,1,255,255,0)
		DisplayText("-----------------------------------------------------------------------",0,3.6,0.7,1,255,255,0)
	EndIf
	For i=0 To 14
		If CustomHubListName$(i+CustomLevelListStart)<>""
			col1=200
			col2=200
			shiver#=0.0
			If CustomLevelListSelected=-1
				; selecting level
				If MouseY()>=(90.0+i*26.5)*gfxheight/600.0 And MouseY()<(90.0+(i+1)*26.5)*gfxheight/600.0 And MouseX()<750*Gfxwidth/800.0
					col1=255
					col2=150
					shiver#=0.2
				EndIf
			Else If CustomLevelListSelected=i+CustomLevelListStart
				; in play/delete phase
				col1=255
				col2=255
				shiver#=0.0

			EndIf
			If widescreen
				DisplayText(CustomHubListName$(i+CustomLevelListStart),-11+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,col1,col1,col2)
			Else
				DisplayText(CustomHubListName$(i+CustomLevelListStart),1+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,col1,col1,col2)
			EndIf
			;DisplayText(CustomLevelListCreator$(i+CustomLevelListStart),40+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,col1,col1,col2)
			;Select CustomLevelListCompleted(i+CustomLevelListStart)
			;Case 0
		;		DisplayText("NO",62+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,255,100,100)
		;	Case 1
		;		DisplayText("YES",61.5+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,0,255,0)
		;	Case 2
		;		DisplayText("GOLD",61+shiver*Sin(menutimer Mod 360),4.5+shiver*Cos(menutimer Mod 360)+i*1.5,0.7,1,255,255,0)
		;	End Select
		EndIf
					
	Next
	
	If NofCustomHubs>15
		extra=0
		If widescreen
			extra=12
		EndIf
		For i=0 To 14
			DisplayText(":  :",67+extra,4.5+i*1.5,0.7,1,255,255,0)
			
		Next
		If MouseX()>750*Gfxwidth/800.0 And MouseY()>82*gfxheight/600.0 And MouseY()<142*gfxheight/600.0
			DisplayText("Pg",68+extra+0.1*Sin(menutimer Mod 360),4.5+0.1*Cos(menutimer Mod 360),0.7,1,255,255,155)
			DisplayText("Up",68+extra+0.1*Sin(menutimer Mod 360),6+0.1*Cos(menutimer Mod 360),0.7,1,255,255,155)
		Else
			DisplayText("Pg",68+extra,4.5,0.7,1,200,200,200)
			DisplayText("Up",68+extra,6,0.7,1,200,200,200)
		EndIf		
		DisplayText("--",68+extra,6.9,0.7,1,255,255,0)
		DisplayText("--",68+extra,23.1,0.7,1,255,255,0)
		If MouseX()>750*Gfxwidth/800.0 And MouseY()>422*gfxheight/600.0 And MouseY()<482*gfxheight/600.0
			DisplayText("Pg",68+extra+0.1*Sin(menutimer Mod 360),24+0.1*Cos(menutimer Mod 360),0.7,1,255,255,155)
			DisplayText("Dn",68+extra+0.1*Sin(menutimer Mod 360),25.5+0.1*Cos(menutimer Mod 360),0.7,1,255,255,155)
		Else
			DisplayText("Pg",68+extra,24,0.7,1,200,200,200)
			DisplayText("Dn",68+extra,25.5,0.7,1,200,200,200)
		EndIf	
	
	EndIf
	
	If widescreen
		DisplayText("-----------------------------------------------------------------------------------------------",-12,26.3,0.7,1,255,255,0)
	Else
		DisplayText("-----------------------------------------------------------------------",0,26.3,0.7,1,255,255,0)
	EndIf
	If CustomLevelListSelected>=0
	
		ShowEntity TitleMenuEntity(61)
		DisplayText(CustomHubListName$(CustomLevelListSelected),25-0.5*Len(CustomHubListName$(CustomLevelListSelected)),19.3,1,1,255,255,0)
		DisplayText(CustomHubListSubTitle$(CustomLevelListSelected),25-0.5*Len(CustomHubListSubTitle$(CustomLevelListSelected)),20.3,1,1,200,150,00)
	
		
		If widescreen
			If MouseY()>540*gfxheight/600.0 And MouseX()>270*Gfxwidth/800.0 And MouseX()<370*Gfxwidth/800.0
				DisplayText(">PLAY<",15+.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("PLAY",16,22,1,1,200,200,200)
			EndIf
			
			If MouseY()>540*gfxheight/600.0 And MouseX()>430*Gfxwidth/800.0 And MouseX()<530*Gfxwidth/800.0
				DisplayText(">CANCEL<",26.5+.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("CANCEL",27.5,22,1,1,200,200,200)
			EndIf
		Else
			If MouseY()>540*gfxheight/600.0 And MouseX()>270*Gfxwidth/800.0 And MouseX()<370*Gfxwidth/800.0
				DisplayText(">PLAY<",16+.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("PLAY",17,22,1,1,200,200,200)
			EndIf
			
			If MouseY()>540*gfxheight/600.0 And MouseX()>430*Gfxwidth/800.0 And MouseX()<530*Gfxwidth/800.0
				DisplayText(">CANCEL<",25.5+.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
			Else
				DisplayText("CANCEL",26.5,22,1,1,200,200,200)
			EndIf
		EndIf

		
		
		
		
	Else
		
		HideEntity TitleMenuEntity(61)

		If MouseY()>540*gfxheight/600.0 And MouseX()>350*Gfxwidth/800.0 And MouseX()<460*Gfxwidth/800.0
			DisplayText(" >EXIT<",21+0.1*Sin(menutimer Mod 360),22+0.1*Cos(menutimer Mod 360),1,1,255,255,100)
		Else
			DisplayText(" EXIT",22,22,1,1,200,200,200)
		EndIf

		

	EndIf

	mouse1=MouseDown(1)
	mouse2=MouseDown(2)

	If (mouse1 Or mouse2) And MouseGameMode=-1
		MouseGameMode=2
		
		For i=0 To 14
			If MouseY()>=(90.0+i*26.5)*gfxheight/600.0 And MouseY()<(90.0+(i+1)*26.5)*gfxheight/600.0 And MouseX()<750*Gfxwidth/800.0
				If CustomHubListName$(i)<>"" Then CustomLevellistselected=i+CustomLevelListStart
				PlaysoundfxNow(130)
				
				If CustomLevellistselected>-1
					FreeTexture titlemenutexture(61)
					; Get the Logo
					If FileType(GlobalDirName$+"\Custom\Hubs\"+CustomHubListFileName$(CustomLevellistselected)+"\hublogo.jpg")=1
						titlemenutexture(61)=LoadTexture(GlobalDirName$+"\Custom\Hubs\"+CustomHubListFileName$(CustomLevellistselected)+"\hublogo.jpg")
					Else
						
						titlemenutexture(61)=myLoadTexture("data\graphics\logos\wonderlandadventures.bmp",4)
					EndIf
					EntityTexture titlemenuentity(61),titlemenutexture(61)
				EndIf

				
			EndIf
		Next
		
			
		If Customlevellistselected>=0
			If MouseY()>540*gfxheight/600.0 And MouseX()>270*Gfxwidth/800.0 And MouseX()<370*Gfxwidth/800.0

				;play
				PlaysoundfxNow(131)
				EndCustomHubSelectMenu()
				
				
				; STARTING HUB!
				b$=globaldirname$+"\custom\adventures\"
				
				
				;;;;
				endmenu()
						
				HideEntity LevelCursor
				HideEntity MouseCursor
				HideEntity DialogBackGroundEntity
				
				CameraFogMode camera,0
				CameraClsColor camera,0,0,0
				CameraFogColor camera,0,0,0
				CameraFogRange camera,200,1000
				CameraRange camera,0.1,1000
				EntityAlpha LevelCursor,.5
													
				For p.Letter = Each Letter
					Delete p
				Next
			
				cube=CreateCube(camera)
				cube2=CreateCube(camera)
				ScaleEntity cube2,.4,.5,.5
				PositionEntity cube2,0,-1,4
				EntityColor cube2,64,64,64
				EntityBlend cube2,2
				EntityFX cube,1
				EntityFX cube2,1
				
				cubetex=myLoadTexture("load.jpg",1)
				EntityTexture cube,cubetex
				PositionEntity cube,0,0.1,5
				
				RenderLetters()
				RenderParticles()
				UpdateWorld
				RenderWorld
				Flip


			
;						
				; copy the hub data
				fromdir$=GlobalDirName$+"\Custom\Hubs\"+CustomHubListFileName$(CustomLevellistselected)+"\Hub\"
				
				dir=ReadDir (fromdir$)
				j=0
				Repeat
					dirfile$=NextFile$(dir)
					If FileType(fromdir$+dirfile$)=1
						CopyFile fromdir$+dirfile$,GlobalDirName$+"\Player Profiles\"+playername$+"\Current\Hub\"+dirfile$
						If j Mod 10 = 0
			
							PositionEntity cube2,0.004*j,-1,4
							RenderWorld
							Flip
		
							
						EndIf
						j=j+1

					EndIf
				Until dirfile$=""
				CloseDir dir
		
				CreateNewPlayer()
				PositionEntity cube2,0.9,-1,4
				RenderWorld
				Flip

				FreeTexture cubetex
				FreeEntity cube
				FreeEntity cube2


				ShowEntity LevelCursor
				ShowEntity MouseCursor

				; get starting position from master file
				file=ReadFile (fromdir$+"master.dat")
				ReadString$(file)
				For i=0 To 4
					ReadString$(file)
				Next
				ReadString$(file); adventureusername$ - don't think we need this
				IconName$=ReadString$(file)
				CustomMapName$=ReadString$(file)
				For i=0 To 7 ;reset the map
					mappiecefound(i)=False
				Next
				ex$=ReadString$(file)
				ex$=ReadString$(file)
				ex$=ReadString$(file)
				ex$=ReadString$(file)
				
						
				ReadInt(file)
				ReadInt(file)
				ReadInt(file); at what hub level and x/y do you reappear if won.
				ReadInt(file)
				ReadInt(file)
				ReadInt(file); at what hub level and x/y do you reappear if won.
				
				ReadInt(file)	
				
				For i=0 To 2
					For j=0 To 5
						ReadInt(file)
					Next
				Next
				
				adventurestartx=ReadInt(file)
				adventurestarty=ReadInt(file)
				
				GateKeyVersion=1
	
				If Eof(file)=False GateKeyVersion=ReadInt(file)
				
				PlayerLevelStartingYaw=0
				If Eof(file)=False PlayerLevelStartingYaw=ReadInt(file)
				If Eof(file)=False ReadInt(file) ;unused for hubs
				FitForWidescreenGlobal=0
				If Eof(file)=False FitForWidescreenGlobal=ReadInt(file)
				
				;shards and glyph custom cmds
				CustomShardEnabledHub=0
				CustomGlyphEnabledHub=0
				For i=0 To NoOfShards-1
					For j=0 To 4
						CustomShardCMDHub(i,j)=0
					Next
				Next
				For i=0 To NoOfGlyphs-1
					For j=0 To 4
						CustomGlyphCMDHub(i,j)=0
					Next
				Next

				If Eof(file)=False
					CustomShardEnabledHub=ReadInt(file)
					If CustomShardEnabledHub>0
						For i=0 To CustomShardEnabledHub-1
							For j=0 To 4
								CustomShardCMDHub(i,j)=ReadInt(file)
							Next
						Next
					EndIf
					CustomGlyphEnabledHub=ReadInt(file)
					If CustomGlyphEnabledHub>0
						For i=0 To CustomGlyphEnabledHub-1
							For j=0 To 4
								CustomGlyphCMDHub(i,j)=ReadInt(file)
							Next
						Next
					EndIf
				EndIf

				CloseFile(file)
				
				FreeTexture buttontexture
				FreeTexture GateTexture
				FreeTexture IconTextureStandard
				
				ButtonTexture=myLoadTexture("data/graphics/buttons"+Str$(GateKeyVersion)+".bmp",4)
				GateTexture=myLoadTexture("data/graphics/gates"+Str$(GateKeyVersion)+".bmp",1)
				IconTextureStandard=myLoadTexture("data/graphics/icons-standard"+Str$(GateKeyVersion)+".bmp",4)
				
				; Load Custom Icons

				FreeTexture icontexturecustom
				icontexturecustom=0
				
				
				
				If FileType(GlobalDirName$+"\Custom\Icons\icons "+iconname$+".bmp")=1
					IconTextureCustom=LoadTexture(GlobalDirName$+"\Custom\Icons\icons "+iconname$+".bmp",4)
					
					
				Else
					IconTextureCustom=myLoadTexture("data\Graphics\icons-custom.bmp",4)
				EndIf
				If icontexturecustom=0
					IconTextureCustom=myLoadTexture("data\Graphics\icons-custom.bmp",4)
				EndIf
				
				; Custom map
				If CustomMapName$<>""
					For i=0 To 8
						FreeTexture MappieceTexture(i)
						If FileType(GlobalDirName$+"\Custom\Maps\"+CustomMapName$+"\mappiece"+Str$(i)+".bmp")=1
							MappieceTexture(i)=LoadTexture(GlobalDirName$+"\Custom\Maps\"+CustomMapName$+"\mappiece"+Str$(i)+".bmp",16+32)
						Else
							MappieceTexture(i)=myLoadTexture("data\graphics\logos\adventurelost.bmp",4) ;oh no, some pieces are missing
						EndIf
					Next
				Else
					For i=0 To 8
						FreeTexture MappieceTexture(i)
						MappieceTexture(i)=myLoadTexture("data\graphics\logos\st2.bmp",1) ;placeholder
					Next
				EndIf
				
				;
				; Load Adventure Scores (for Floinging) if available
		;		If FileType(GlobalDirName$+"\Custom\Hubs\"+CustomHubListFileName$(CustomLevellistselected)+"\"+playername$+".floing")=1
		;			file=ReadFile(GlobalDirName$+"\Custom\Hubs\"+CustomHubListFileName$(CustomLevellistselected)+"\"+playername$+".floing")
		;			; found previous scores - load them
		;			For ix=0 To 499
		;				AdventureCompleted(ix)=ReadInt(file)
		;				AdventureCompletedTime(ix)=ReadInt(file)
		;				AdventureCompletedGems(ix)=ReadInt(file)
		;				AdventureCompletedGemsTotal(ix)=ReadInt(file)
		;				AdventureCompletedCoins(ix)=ReadInt(file)
		;				AdventureCompletedCoinsTotal(ix)=ReadInt(file)
		;				AdventureCompletedScore(ix)=ReadInt(file)
		;			Next
		;			CloseFile file
;
;				Else
					; no score -reset all
					For ix=0 To 499
						AdventureCompleted(ix)=0
						AdventureCompletedTime(ix)=0
						AdventureCompletedGems(ix)=0
						AdventureCompletedGemsTotal(ix)=0
						AdventureCompletedCoins(ix)=0
						AdventureCompletedCoinsTotal(ix)=0
						AdventureCompletedScore(ix)=0
					Next
;				EndIf


				;
				
				incustomhub=1
				incustomhubnametruncated$=CustomHubListFileName$(CustomLevellistselected)
				incustomhubname$=GlobalDirName$+"\Custom\Hubs\"+CustomHubListFileName$(CustomLevellistselected)+"\"
				
				incustomhubiconname$=iconname$

				starthublevel(-1,adventurestartx,adventurestarty,False)

				
				
				;;;;
			
				
				
			EndIf
		
			If MouseY()>540*gfxheight/600.0 And MouseX()>430*Gfxwidth/800.0 And MouseX()<530*Gfxwidth/800.0

				PlaysoundfxNow(132)
				;cancel
				CustomLevelListselected=-1
			EndIf
			

			
		Else
			
		
			If MouseY()>540*gfxheight/600.0 And MouseX()>350*Gfxwidth/800.0 And MouseX()<460*Gfxwidth/800.0

				;exit
				PlaysoundfxNow(132)
				EndCustomHubSelectMenu()
				StartMenu(11)
				
			EndIf

		
		EndIf
		
		If MouseX()>750*Gfxwidth/800.0 And MouseY()>82*gfxheight/600.0 And MouseY()<142*gfxheight/600.0
			CustomLevelListStart=CustomLevelListStart-15
			PlaysoundfxNow(130)
			
		EndIf
		If MouseX()>750*Gfxwidth/800.0 And MouseY()>422*gfxheight/600.0 And MouseY()<482*gfxheight/600.0
			CustomLevelListStart=CustomLevelListStart+15
			PlaysoundfxNow(130)
			
		EndIf
	EndIf
		
	If CustomLevelListStart>NofCustomHubs-15 Then CustomLevelListStart=NofCustomHubs-15
	If CustomLevelListStart<0 Then CustomLevelListStart=0
	
	; refresh with f12
	If KeyDown(88) 
		StartCustomHubSelectMenu()
		PlaysoundfxNow(130)
	EndIf		

	
		
;	displaytext nofcustomlevels,0,0,1,1,255,255,255		


End Function

Function EndCustomHubSelectMenu()
	For i=80 To 99
		HideEntity TitleMenuEntity(i)
	Next
	HideEntity titlemenuentity(61)
End Function




.SpellData
Data "Power", "Activate"
Data "Ice", "Activate"
Data "Fire", "Activate"
Data "Time", "Activate"

.CharmData
Data "Vision","Remove"
Data "Light","Detect"