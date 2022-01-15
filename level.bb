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
		ex$=Mid$(leveltexturename$,10,Len(leveltexturename$)-9-4)
		
		
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
	
	ex$=Mid$(leveltexturename$,10,Len(leveltexturename$)-9-4)
	
	If usecustomleveltexture=True
		EntityTexture LevelEntity(CurrentLevelChunk),customleveltexture
	Else
		EntityTexture LevelEntity(CurrentLevelChunk),LevelTexture(WhichLevelTexture(ex$))	
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
	
	ex$=Mid$(waterTextureName$,10,Len(waterTextureName$)-9-4)
	
	If usecustomwatertexture=True
		EntityTexture WaterEntity(CurrentLevelChunk),customwatertexture
	Else
		EntityTexture WaterEntity(CurrentLevelChunk),WaterTexture(WhichWaterTexture(ex$))
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

Function CalculateUV(Texture,i2,j2,Rotation,Size)

	; calculuates UV coordinates of a point on "texture" (0-(size^2-1)... ie the field on the 256x256 big texture)
	; at position i2/j2 (with resolution LevelDetail) and given Rotation (0-7)
	
	; Size is the number of textures in a row (e.g. 8 in a 64 texture field)
	
	; returns results as Globals ChunkTileU/ChunkTileV
	uoverlap#=0
	voverlap#=0
	
	ChunkTileu#=Float((Texture Mod size))/size+(Float(i2)/Float(LevelDetail))/size
	ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(LevelDetail))/size
	
	
	If j2=0 Or j2=LevelDetail Or i2=0 Or i2=LevelDetail
		If i2=0 
			uoverlap#=.002
		Else If i2=LevelDetail
			uoverlap#=-.002
		Else
			uoverlap#=0
		EndIf
		If j2=0 
			voverlap#=.002
		Else If j2=LevelDetail
			voverlap#=-.002
		Else
			voverlap#=0
		EndIf
	EndIf
	
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
		PositionTexture WaterTexture(0),0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
		PositionTexture WaterTexture(1),0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
		PositionTexture WaterTexture(2),0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
		PositionTexture WaterTexture(3),0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0

		If customwatertexture>0 PositionTexture customwatertexture,0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0

;		PositionTexture WaterTexture,0,-((LevelTimer*WaterFlow) Mod 10000)/10000.0
	EndIf
	If waterflow<0
		; rock
		PositionTexture WaterTexture(0),0,0.5+0.125*WaterFlow/4*Sin(-(4*LevelTimer*WaterFlow)/10.0)
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

Function SetupCamera()

	Camera=CreateCamera()
	
	RotateEntity Camera,55,0,0
	CameraRange Camera,0.1,1000
	;CameraZoom Camera,2
	;widescreen
	If Not widescreen
		CameraZoom Camera,2
		CameraZoomLevel=2;.005
	Else
		CameraZoom Camera,1.5
		CameraZoomLevel=1.5
	EndIf
	CameraViewport camera,0,0,GFXWidth,GfxHeight
	
	;CameraProjMode camera,2
	
End Function

Function ControlCamera()

	speed#=0.01

	If CameraAddXCurrent>CameraAddX+speed
		CameraAddXCurrent=CameraAddXCurrent-(CameraAddXCurrent-CameraAddX)*0.05
	Else If CameraAddXCurrent<CameraAddX-speed
		CameraAddXCurrent=CameraAddXCurrent-(CameraAddXCurrent-CameraAddX)*0.05
	Else
		CameraAddXCurrent=CameraAddX
	EndIf
	
	If CameraAddYCurrent>CameraAddY+speed
		CameraAddYCurrent=CameraAddYCurrent-(CameraAddYCurrent-CameraAddY)*0.05
	Else If CameraAddYCurrent<CameraAddY-speed
		CameraAddYCurrent=CameraAddYCurrent-(CameraAddYCurrent-CameraAddY)*0.05
	Else
		CameraAddYCurrent=CameraAddY
	EndIf
	
	If CameraAddZCurrent>CameraAddZ+speed
		CameraAddZCurrent=CameraAddZCurrent-(CameraAddZCurrent-CameraAddZ)*0.05
	Else If CameraAddZCurrent<CameraAddZ-speed
		CameraAddZCurrent=CameraAddZCurrent-(CameraAddZCurrent-CameraAddZ)*0.05
	Else
		CameraAddZCurrent=CameraAddZ
	EndIf
	
	speed#=0.0001
	If CameraAddZoomCurrent>CameraAddZoom+speed
		CameraAddZoomCurrent=CameraAddZoomCurrent-speed
	Else If CameraAddZoomCurrent<CameraAddZoom-speed
		CameraAddZoomCurrent=CameraAddZoomCurrent+speed
	Else
		CameraAddZoomCurrent=CameraAddZoom
	EndIf
	CameraZoom Camera,CameraZoomLevel+CameraAddZoom


		

End Function

Function SetupLight()

	LevelLight=CreateLight()
	RotateEntity levellight,35,-35,0


	SpotLight=CreateLight(3)
	RotateEntity SpotLight,60,0,0
	LightConeAngles SpotLight,0,60


	LightRed=0
	LightGreen=0
	LightBlue=0
	AmbientRed=0
	AmbientGreen=0
	AmbientBlue=0
	
End Function

Function SetLight(red,green,blue,speed,ared,agreen,ablue,aspeed)
	LightRedGoal=Red
	LightGreenGoal=Green
	LightBlueGoal=Blue
	LightChangeSpeed=speed
	
	AmbientRedGoal=aRed
	AmbientGreenGoal=aGreen
	AmbientBlueGoal=aBlue
	AmbientChangeSpeed=aSpeed
End Function

Function SetLightNow(red,green,blue,ared,agreen,ablue)
	LightRed=Red
	LightGreen=Green
	LightBlue=Blue
	LightRedGoal=Red
	LightGreenGoal=Green
	LightBlueGoal=Blue
	
	AmbientRed=aRed
	AmbientBlue=aBlue
	AmbientGreen=aGreen
	AmbientRedGoal=aRed
	AmbientGreenGoal=aGreen
	AmbientBlueGoal=aBlue
End Function



Function ControlLight()
	
	If LightRed>LightRedGoal
		LightRed=LightRed-LightChangeSpeed
		If LightRed<LightRedGoal Then LightRed=LightRedGoal
	Else If LightRed<LightRedGoal
		LightRed=LightRed+LightChangeSpeed
		If LightRed>LightRedGoal Then LightRed=LightRedGoal
	EndIf
	If LightGreen>LightGreenGoal
		LightGreen=LightGreen-LightChangeSpeed
		If LightGreen<LightGreenGoal Then LightGreen=LightGreenGoal
	Else If LightGreen<LightGreenGoal
		LightGreen=LightGreen+LightChangeSpeed
		If LightGreen>LightGreenGoal Then LightGreen=LightGreenGoal
	EndIf
	If LightBlue>LightBlueGoal
		LightBlue=LightBlue-LightChangeSpeed
		If LightBlue<LightBlueGoal Then LightBlue=LightBlueGoal
	Else If LightBlue<LightBlueGoal
		LightBlue=LightBlue+LightChangeSpeed
		If LightBlue>LightBlueGoal Then LightBlue=LightBlueGoal
	EndIf

	If AmbientRed>AmbientRedGoal
		
		AmbientRed=AmbientRed-AmbientChangeSpeed
		If AmbientRed<AmbientRedGoal Then AmbientRed=AmbientRedGoal
	Else If AmbientRed<AmbientRedGoal
		
		AmbientRed=AmbientRed+AmbientChangeSpeed
		If AmbientRed>AmbientRedGoal Then AmbientRed=AmbientRedGoal
	EndIf
	If AmbientGreen>AmbientGreenGoal
		AmbientGreen=AmbientGreen-AmbientChangeSpeed
		If AmbientGreen<AmbientGreenGoal Then AmbientGreen=AmbientGreenGoal
	Else If AmbientGreen<AmbientGreenGoal
		AmbientGreen=AmbientGreen+AmbientChangeSpeed
		If AmbientGreen>AmbientGreenGoal Then AmbientGreen=AmbientGreenGoal
	EndIf
	If AmbientBlue>AmbientBlueGoal
		AmbientBlue=AmbientBlue-AmbientChangeSpeed
		If AmbientBlue<AmbientBlueGoal Then AmbientBlue=AmbientBlueGoal
	Else If AmbientBlue<AmbientBlueGoal
		AmbientBlue=AmbientBlue+AmbientChangeSpeed
		If AmbientBlue>AmbientBlueGoal Then AmbientBlue=AmbientBlueGoal
	EndIf

	
	LightColor LevelLight,LightRed,LightGreen,LightBlue
	
	;And LightRedGoal+LightBlueGoal+LightGreenGoal+AmbientRedGoal+AmbientBlueGoal+AmbientGreenGoal<800
	If (CurrentCharm=1 Or CurrentCharm=2)  And LevelTimer<1000000000 And Leveltimer>70 And CurrentLightPower<>0
		LightColor SpotLight,10,4,0;LightRed,LightGreen,LightBlue;,4,0;LightRed,LightGreen,-LightBlue
	Else
		LightColor SpotLight,0,0,0
	EndIf
	
	AmbientLight AmbientRed,AmbientGreen,AmbientBlue

	
	

End Function



	