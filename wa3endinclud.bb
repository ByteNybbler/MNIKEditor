






Function dowa3ending()

TweenPeriod=1000/40;60;85
TweenTime=MilliSecs()-TweenPeriod

	PositionEntity camera,0,0,-1
	
	RotateEntity camera,0,0,0
	CameraZoom camera,1

	AmbientLight 100,100,100


	If ChannelPlaying(musicchannel)=1
		StopChannel(musicchannel)
	EndIf
	
	timer=0
	
	LightColor levellight,0,0,0
	LightColor spotlight,0,0,0
	
	light=CreateLight (1)
	PositionEntity light,20,0,0
	PointEntity light,camera
	
	endmenu()
	
	CameraRange Camera,0.1,1000
	CameraFogMode camera,1
	CameraClsColor camera,0,0,0


	; load stuff
	ResetText("data/graphics/font.bmp")

	
	wa3endsong=LoadSound("data\leveltextures\summary.wdf")

wa3endtrumpet=LoadSound("data\sound\mjhiuojoh2.wdf")
wa3endexplo1=LoadSound("data\sound\mjhiuojoh1.wdf")
wa3endexplo2=LoadSound("data\sound\mjhiuojoh3.wdf")

wa3endshipsound=LoadSound("data\sound\kfu.wdf")


	wa3endvoidtexture=LoadTexture("data\models\void\wpje.wdf")
	
	For i=0 To 5
		wa3endvoidmodel(i)=CreateCube()
		EntityTexture wa3endvoidmodel(i),wa3endvoidtexture
		TurnEntity  wa3endvoidmodel(i),i*5,i*10,i*21
	Next
	


	wa3endstartex=LoadTexture("data\graphics\logos\tu2.wdf")
	ScaleTexture wa3endstartex,.3,.3;10,10
	wa3endStarfield=CreateSphere()
	ScaleEntity wa3endStarfield,100,100,100
	FlipMesh wa3endStarfield
	EntityTexture wa3endStarfield,wa3endstartex
	
	wa3endshipmodel=myLoadMesh("data\models\other\starship.3ds",0)
	wa3endshiptexture=LoadTexture("data\models\other\tubstijq.wdf",1)
	EntityTexture wa3endshipmodel,wa3endshiptexture
	ScaleEntity wa3endshipmodel,.02,.02,.02
;	HideEntity wa3endshipmodel
	RotateEntity wa3endshipmodel,0,0,20
	
	
	wa3endstartexture=LoadTexture("data\models\star\hpmetubs.wdf",1)
	For i=0 To 30
		wa3endstarmodel(i)=myLoadMesh("data\models\star\star.3ds",0)
		RotateEntity wa3endstarmodel(i),-90,0,0
		ScaleEntity wa3endstarmodel(i),.2,.2,.2
		TurnEntity wa3endstarmodel(i),0,0,Rnd(0,360)
		EntityTexture wa3endstarmodel(i),wa3endstartexture
		PositionEntity wa3endstarmodel(i),0,0,30
		HideEntity wa3endstarmodel(i)
	Next
	
	wa3endbarren=CreateSphere(64)
	wa3endbarrentexture=LoadTexture("data\graphics\logos\qk1.wdf",1)
	EntityTexture wa3endbarren,wa3endbarrentexture
	ScaleEntity wa3endbarren,5,5,5
	PositionEntity wa3endbarren,7,0,-7
	HideEntity wa3endbarren
	TurnEntity wa3endbarren,0,180,0
	wa3endbarrencloud=CreateSphere(64)
	wa3endbarrencloudtexture=LoadTexture("data\graphics\logos\dm1.wdf",2)
	EntityTexture wa3endbarrencloud,wa3endbarrencloudtexture
	ScaleEntity wa3endbarrencloud,5.1,5.1,5.1
	PositionEntity wa3endbarrencloud,7,0,-7
	EntityAlpha wa3endbarrencloud,.5
	HideEntity wa3endbarrencloud
	
	wa3endjaava=CreateSphere(64)
	wa3endjaavatexture=LoadTexture("data\graphics\logos\qk2.wdf",1)
	EntityTexture wa3endjaava,wa3endjaavatexture
	ScaleEntity wa3endjaava,8,8,8
	RotateEntity wa3endjaava,30,50,20
	HideEntity wa3endjaava


	PositionEntity wa3endjaava,25,3,50
;	HideEntity wa3endbarren
;	TurnEntity wa3endbarren,0,180,0

	Wa3enduo=CreateSphere()
	PositionEntity Wa3enduo,-70,15,10
	ScaleEntity Wa3enduo,5,5,5
	wa3enduotexture=LoadTexture("data\graphics\logos\qk3.wdf",1)
	EntityTexture wa3enduo,wa3enduotexture
	
	wa3endwonderlandtexture=LoadTexture("data\graphics\logos\qk4.wdf",1)
	wa3endwonderlandcloudtexture=LoadTexture("data\graphics\logos\dm2.wdf",2)






	wa3endsun=CreateSphere(64)
	wa3endsuntexture=LoadTexture("data\graphics\logos\qk5.wdf",1)

	EntityTexture wa3endsun,wa3endsuntexture
	ScaleEntity wa3endsun,3,3,3
	EntityFX wa3endsun,1
	PositionEntity wa3endsun,22,0,-10

timer=0	

Repeat


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
			
			



;-------------------------------


;wa3endsuntexture



If timer<400
		For i=0 To 5
			ScaleEntity wa3endvoidmodel(i),.7+.3*Sin(timer+i*20),.6+.4*Cos(timer+i*30),.8-.2*Sin(timer+i*40)
			TurnEntity wa3endvoidmodel(i),(i+1)*.5,-i*.1,i*.3
		Next
	Else
		For i=0 To 5
			HideEntity wa3endvoidmodel(i)
		Next
	EndIf
		
	
	SetBuffer BackBuffer()
	
	
	
	
	
	If timer <400 	PositionEntity camera,0,0,-1-(Float(timer)/100.0)^2
	
	timer=timer+1
	
;	If timer>360
	
;		PositionEntity camera,-1.3*Sin((Float(timer-360)/10.0) Mod 3600),Sin((Float(timer-360)/10.0) Mod 3600),-3*Cos((Float(timer-360)/10.0) Mod 360)
		
;	EndIf

	If timer<2000 TurnEntity wa3enduo,.1,0,0

	If timer>200 And timer<400
		TurnEntity wa3endshipmodel,0,2,0
		TranslateEntity wa3endshipmodel,.0015,-.0007,-.1
	EndIf	
	If timer=230
		PlaySound wa3endshipsound
	EndIf
	
	If timer=400
		For i=0 To 5
			HideEntity wa3endvoidmodel(i)
		Next
		For i=0 To 30
			ShowEntity wa3endstarmodel(i)
		Next
		CameraFogRange camera,0.01,1
		CameraFogMode camera,1
		CameraFogColor camera,255,100,255
		PlaySound wa3endexplo1
		PlaySound wa3endexplo2
		PositionEntity camera,0,0,0
	EndIf
	If timer>450 And timer<550
		
		CameraFogColor camera,255-(timer-450)*2.5,100-(timer-450)*1,255-(timer-450)*2.5
	EndIf
	If timer>450 And timer<550
		
		CameraFogRange camera,0.01,(timer-450)*2

	EndIf
	If timer=550
		CameraFogColor camera,0,0,0
	EndIf
	
	If timer>450 And timer<650
		For i=0 To 30
			TranslateEntity wa3endstarmodel(i),(.1+.05)*Sin(i*47+60)*Sin(i*10),(.1+.05)*Sin(i*47+60)*Cos(i*10),0
			TurnEntity wa3endstarmodel(i),0,0,3
		Next
	EndIf
	If timer<400 And (timer Mod 50)=1 
		SoundPitch wa3endtrumpet,5000+timer*100
		PlaySound wa3endtrumpet
	EndIf
	If timer=600 
		PlaySound wa3endsong
		wa3endsongstart=MilliSecs()
	EndIf
	
	If timer>600 And timer<650
		For i=0 To 30
			EntityAlpha wa3endstarmodel(i),Float(650-timer)/50.0
		Next
	EndIf
	
	If timer=800
		For i=0 To 30
			PositionEntity wa3endstarmodel(i),Rnd(-70,-40),Rnd(-2,2),Rnd(-5,1)
			
		Next
	EndIf
	If timer=802
		For i=0 To 30
			EntityAlpha  wa3endstarmodel(i),1
			
		Next
	EndIf


	If timer>800  And timer<1700
		;RotateEntity camera,0,Float(timer-800)/5.0,0
		TurnEntity camera,0,0.2,0
		For i=0 To 30
			TranslateEntity wa3endstarmodel(i),.1,0,0
			TurnEntity wa3endstarmodel(i),0,0,3

			
		Next
		


	EndIf
	
	If timer=1400
		RotateEntity wa3endshipmodel,0,0,0
		ScaleEntity wa3endshipmodel,.08,.08,.08
		
		ShowEntity wa3endbarren
		ShowEntity wa3endbarrencloud
		
	


	EndIf 
	
	
	If timer>1400 And timer<1700
		PositionEntity wa3endshipmodel,7-6*Sin(Float(timer-1400)/3.3),-1,-7-6*Cos(Float(timer-1400)/3.3)
		TurnEntity wa3endshipmodel,0,1,0
		TurnEntity wa3endbarren,0,.1,0
		TurnEntity wa3endbarrencloud,0,.15,0


	EndIf
	
	If timer=1700
		For i=0 To 30
			;PositionEntity wa3endstarmodel(i),Rnd(8,20),Rnd(-5,10),Rnd(-3,3)
			EntityAlpha wa3endstarmodel(i),0;,0,0,90

			
		Next
	EndIf
	
	
	If timer>=1700 And timer<1940
	
		PositionEntity wa3endshipmodel,7-6*Sin(Float(timer-1400)/3.3),-1,-7-6*Cos(Float(timer-1400)/3.3)
		TurnEntity wa3endshipmodel,0,1,0
		TurnEntity wa3endbarren,0,.1,0
		TurnEntity wa3endbarrencloud,0,.2,0
		
		TurnEntity camera,0,.2,0
		If timer>1900 TurnEntity camera,0,-.2*Sin((timer-1900)*2),0
		
		
		
		EntityColor wa3endsun,240+10*Sin(timer),240+10*Cos(timer*3),30-30*Sin(timer*2)

		If timer>1850 TurnEntity wa3endshipmodel,0,9.0*Sin(Float(timer-1850)),0
	EndIf
	
	
	
	If timer>=1940 And timer<2440
		TranslateEntity wa3endshipmodel,.06,0,.01
		TurnEntity wa3endshipmodel,0,10,0

		TurnEntity wa3endbarren,0,.1,0
		TurnEntity wa3endbarrencloud,0,.2,0
		
	;	TranslateEntity camera,
		PositionEntity camera,(Float(timer-1940)/100.0)^2,0,0
		PositionEntity wa3endstarfield,(Float(timer-1940)/100.0)^2,0,0
		
		TurnEntity camera,0,.2,0

	
		
			EntityColor wa3endsun,240+10*Sin(timer),240+10*Cos(timer*3),30-30*Sin(timer*2)

		TurnEntity wa3endjaava,0,.2,0
		
	EndIf
	If timer=1701
		For i=0 To 30
			
			PositionEntity wa3endstarmodel(i),Rnd(-5,20),Rnd(-5,10),Rnd(-10,-1)
			

			
		Next
	EndIf
	
	If timer=2250
		For i=0 To 30
			
			PositionEntity wa3endstarmodel(i),Rnd(26,48),Rnd(-5,10),Rnd(-30,00)
			
			
			
		Next
	EndIf


	If timer=2248
		PositionEntity wa3endbarrencloud,-25,3,0
	EndIf

	
	If timer=2250
		ShowEntity wa3endjaava
		PositionEntity wa3endbarrencloud,25,3,50
		ScaleEntity wa3endbarrencloud,8.2,8.2,8.2
		
		For i=0 To 30
	;		ShowEntity wa3endstarmodel(i)
			;PositionEntity wa3endstarmodel(i),Rnd(22,25),Rnd(1,5),Rnd(40,45)
			;TranslateEntity wa3endstarmodel(i),0,-.1,.01
			TurnEntity wa3endstarmodel(i),0,2,0

			
		Next

	EndIf
	
	If timer>2440
		Text 0,10,EntityX(camera)
		Text 0,20,EntityX(camera)
		Text 0,30,EntityX(camera)
		Text 0,40,EntityYaw(camera)
	EndIf
		
		
	
	If timer>=2440 And timer<2530  ;now out at +25
	
		TranslateEntity wa3endshipmodel,.06,0,.01
		TurnEntity wa3endshipmodel,0,10,0

		TurnEntity camera,0,.2*Cos(timer-2440),0
		TranslateEntity camera,0.1*Cos(Float(timer-2440)),0,0;0.05*Sin(Float(timer-2440)/10.0),0.1*Sin(Float(timer-2440)/10.0)
		;TurnEntity camera,0,.2,0
		TurnEntity wa3endjaava,0,.3,0
		TurnEntity wa3endbarrencloud,0,.5,0
		
		For i=0 To 30
			EntityAlpha wa3endstarmodel(i),1
		;	PositionEntity wa3endstarmodel(i),Rnd(22,25),Rnd(1,5),Rnd(40,45)
			TranslateEntity wa3endstarmodel(i),.01,0,.08

			TurnEntity wa3endstarmodel(i),0,2,0

			
		Next

		
	;	EntityAlpha wa3endstarfield,Float(2530-timer)/100.0

	EndIf
	
	If timer>2530 And timer<2800
	
	;	HideEntity wa3endstarfield
	
		TurnEntity wa3endjaava,0,.3,0
		TurnEntity wa3endbarrencloud,0,.5,0
		
		TranslateEntity wa3endshipmodel,0,0,.02
		TurnEntity wa3endshipmodel,0,10,0

		
		TranslateEntity wa3endjaava,-.015,-.02,-.2
		TranslateEntity wa3endbarrencloud,-.015,-.02,-.2
		
		For i=0 To 30
		;	ShowEntity wa3endstarmodel(i)
		;	PositionEntity wa3endstarmodel(i),Rnd(22,25),Rnd(1,5),Rnd(40,45)
			TranslateEntity wa3endstarmodel(i),.01,0,.08



			TurnEntity wa3endstarmodel(i),0,2,0

			
		Next


		
	;	TranslateEntity camera,-0.04*Sin(Float(timer-2440)/10.0),0.2*Sin(Float(timer-2440)/10.0),1.0*Sin(Float(timer-2440)/10.0)
	;	If timer>2650
	;		CameraFogRange camera,0.1,150.2-(timer-2650)*3
	;		CameraFogColor camera,(timer-2650)*5,(timer-2650)*5,(timer-2650)*5


			
	;	EndIf
	EndIf
	
	If timer=2788
		EntityAlpha wa3endbarrencloud,0
		ScaleEntity wa3endbarrencloud,100,100,1
	EndIf
	
	If timer=2799
		PositionEntity wa3endbarrencloud,80,4,60
	;	RotateEntity wa3endbarrencloud,0,-30,0
	EndIf
	If timer>2800 And timer<3100
		
		If timer<3100 EntityAlpha wa3endbarrencloud,Float(timer-2800)/300.0
		TranslateEntity wa3endshipmodel,0,0,.02
		TurnEntity wa3endshipmodel,0,10,0
		TranslateEntity wa3endbarrencloud,0,0,0
		If timer>2900 And timer<3050
			CameraFogRange camera,0.1,150.2-(timer-2900)*1.5
			CameraFogColor camera,(timer-2900)*1,(timer-2900)*1,(timer-2900)*1


			
		EndIf
		If timer>3050 And timer<3100
			
			CameraFogColor camera,250-(timer-3050)*5,250-(timer-3050)*5,250-(timer-3050)*5
		EndIf
		
		For i=0 To 30
		;	ShowEntity wa3endstarmodel(i)
		;	PositionEntity wa3endstarmodel(i),Rnd(22,25),Rnd(1,5),Rnd(40,45)
			TranslateEntity wa3endstarmodel(i),.01,0,.08

			TurnEntity wa3endstarmodel(i),0,2,0

			
		Next


 
		
	EndIf
	
	If timer=3099
		PositionEntity camera,0,0,0
		RotateEntity camera,0,0,0
		PositionEntity wa3endbarren,0,0,60
		PositionEntity wa3endshipmodel,0,1,0
		AmbientLight 0,0,0
		LightColor light,0,0,0
		;HideEntity wa3endbarrencloud
		PositionEntity wa3endbarrencloud,0,0,60
		EntityTexture wa3endbarren,wa3endwonderlandtexture
		EntityTexture wa3endbarrencloud,wa3endwonderlandcloudtexture
		ScaleEntity wa3endbarrencloud,5.2,5.2,5.2
		CameraFogRange camera,0.1,150
		CameraFogColor camera,0,0,0
		PositionEntity light,10,5,0
		PointEntity light,wa3endbarren
		
		For i=0 To 30
			r=Rnd(3,5)
			angl=Rnd(0,360)
			PositionEntity wa3endstarmodel(i),r*Sin(angl),r*Cos(angl),Rnd(-50,0)
			ShowEntity wa3endstarmodel(i)

			
		Next

		
	EndIf
	If timer=3100
		If MilliSecs()-wa3endsongstart<64000
			timer=3099
		Else
			timer=3999
		EndIf
	EndIf
	
	If timer>=4000 And timer<4100
		AmbientLight timer-4000,timer-4000,timer-4000
		LightColor light,(timer-4000)*2.5,(timer-4000)*2.5,(timer-4000)*2.5
	EndIf
	
	If timer>4000 And timer<4900
		TranslateEntity wa3endbarren,0,-.002,-.05
		TranslateEntity wa3endbarrencloud,0,-.002,-.05
		
		TurnEntity wa3endbarren,0,.1,0
		TurnEntity wa3endbarrencloud,0,.2,0
		For i=0 To 30
			TranslateEntity wa3endstarmodel(i),0,0,.05
			TurnEntity wa3endstarmodel(i),0,1,0
			

			
		Next
		
		TurnEntity wa3endshipmodel,0,1,0
		TranslateEntity wa3endshipmodel,0,0,.01

		If timer>4800
			AmbientLight (4900-timer),(4900-timer),(4900-timer)
			LightColor light,255-(timer-4800)*2.5,255-(timer-4800)*2.5,255-(timer-4800)*2.5
		EndIf


	EndIf
	
	If timer=4900
		LightColor light,0,0,0
		FreeTexture wa3endbarrentexture
		FreeTexture wa3endbarrencloudtexture
		FreeEntity wa3endbarren
		FreeEntity wa3endbarrencloud
		FreeTexture wa3endstartex
		FreeEntity wa3endstarfield
		wa3endstartex=LoadTexture("data\graphics\logos\tu.wdf");starpaper2.jpg");.bmp")
		wa3endstarfield=CreatePlane()
		ScaleTexture wa3endstartex,6,12
		RotateEntity wa3endstarfield,-90,0,0
		TurnEntity wa3endstarfield,0,180,0
		PositionEntity wa3endstarfield,0,-7.5,6
		EntityTexture wa3endstarfield,wa3endstartex
		
		wa3endbarren=CreateCube()
		ScaleEntity wa3endbarren,3.1,3,.0001
		TurnEntity wa3endbarren,2,0,0
	;	RotateEntity wa3endbarren,-90,0,0
	;	TurnEntity wa3endbarren,0,180,0

		PositionEntity wa3endbarren,0,-3.5,3
		wa3endbarrentexture=LoadTexture("data\graphics\logos\foe1.wdf",4)
	;	ScaleTexture wa3endbarrentexture,3.5,3.5
		EntityTexture wa3endbarren,wa3endbarrentexture
		
		wa3endbarrencloud=CreateCube()
		ScaleEntity wa3endbarrencloud,3,3,.01
		PositionEntity wa3endbarrencloud,0,-3.7,2.8
		wa3endbarrencloudtexture=LoadTexture("data\graphics\logos\foe2.wdf",4)
	;	ScaleTexture wa3endbarrentexture,3.5,3.5
		EntityTexture wa3endbarrencloud,wa3endbarrencloudtexture

		


		
		
		PositionEntity camera,0,0,0
		For i=0 To 30
			ShowEntity wa3endstarmodel(i)
			RotateEntity wa3endstarmodel(i),0,90,90
			ScaleEntity wa3endstarmodel(i),.1,.1,.1
			PositionEntity wa3endstarmodel(i),5*Sin(i*226),4.0+Float(i)/4.0,5
		Next
		PositionEntity wa3endshipmodel,0,1.78,2
		ScaleEntity wa3endshipmodel,.08,.08,.08
		RotateEntity wa3endshipmodel,-20,0,0
		
	EndIf	
	If timer>4900 And timer<7000
		TranslateEntity wa3endstarfield,0,Float(4.0/1600.0),0
		For i=0 To 30
			TranslateEntity wa3endstarmodel(i),-.0005,-.005,0
			TurnEntity wa3endstarmodel(i),0,2*Sin(i*160+5),0
		Next
		TranslateEntity wa3endbarren,0,0.0020*Cos(Float(timer-4900)*90.0/2100.0),0
		
		TranslateEntity wa3endbarrencloud,0,0.0034*Cos(Float(timer-4900)*90.0/2100.0),0

	EndIf
	If timer>4900 And timer<5000
		AmbientLight (timer-4900)*2,(timer-4900)*2,(timer-4900)*2
	;	LightColor light,255-(timer-4800)*2.5,255-(timer-4800)*2.5,255-(timer-4800)*2.5
	EndIf
	
	If timer>5500 And timer<7000
		TranslateEntity wa3endshipmodel,0,-.0011,0
		TurnEntity wa3endshipmodel,0,1,0
	EndIf
	
	
	If timer>6800 And timer<7000
		AmbientLight Float(7000-timer)/1.0,Float(7000-timer)/1.0,Float(7000-timer)/1.0
	;	LightColor light,255-(timer-4800)*2.5,255-(timer-4800)*2.5,255-(timer-4800)*2.5
	EndIf

	

		
		
	
; -----------------------------

	Next
			
		
		
		
	
	
		
	


		RenderWorld tween
	
	
		
	
	
		
	


		
	
	Flip	
	

;	If KeyDown(1) Then timer=7000
		


		
		
		
Until timer>=7000

For i=0 To 30
	FreeEntity wa3endstarmodel(i)
Next


FreeSound wa3endsong;=LoadSound("data\leveltextures\summary.wdf")

FreeSound	wa3endtrumpet;=LoadSound("data not crypted\sound\fireon2.wav")
FreeSound	wa3endexplo1;=LoadSound("data not crypted\sound\lightning1.wav")
FreeSound	wa3endexplo2;=LoadSound("data not crypted\sound\lightning3.wav")
FreeSound	wa3endshipsound
FreeTexture	wa3endvoidtexture;=LoadTexture("data not crypted\models\void\void.jpg")

FreeTexture wa3endstartex;=LoadTexture("data not crypted\graphics\logos\st2.bmp")
	
FreeEntity	wa3endStarfield;=CreateSphere()
	
	
FreeEntity	wa3endshipmodel;=LoadMesh("data not crypted\models\other\starship.3ds",0)
FreeTexture	wa3endshiptexture;=LoadTexture("data not crypted\models\other\starship.jpg",1)
	
	
FreeTexture	wa3endstartexture;=LoadTexture("data not crypted\models\star\goldstar.jpg",1)
	
	
FreeEntity	wa3endbarren;=CreateSphere(64)
FreeTexture	wa3endbarrentexture
FreeEntity	wa3endbarrencloud;=CreateSphere(64)
FreeTexture	wa3endbarrencloudtexture
FreeEntity	wa3endjaava;=CreateSphere(64)
FreeTexture	wa3endjaavatexture

FreeEntity	Wa3enduo;=CreateSphere()
	
FreeTexture	wa3enduotexture
	
FreeTexture	wa3endwonderlandtexture;=LoadTexture("data not crypted\graphics\logos\pl4.jpg",1)
FreeTexture	wa3endwonderlandcloudtexture;=LoadTexture("data not crypted\graphics\logos\cl2.jpg",2)






FreeEntity	wa3endsun
FreeTexture wa3endsuntexture
	


FreeEntity light


RotateEntity Camera,55,0,0
CameraRange Camera,0.1,1000
CameraZoom Camera,2
CameraFogMode camera,0
CameraRange camera, 0.1,1000
CameraFogColor camera,0,0,0



		
starthublevel(-90,14,15,False)

TweenPeriod=1000/60;85
TweenTime=MilliSecs()-TweenPeriod



End Function