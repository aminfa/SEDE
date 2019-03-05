/* service_canny_gpu.c */

#include "service_canny.h"

int32_t run_service_canny_gpu(PixelPacket *pixpack_target, PixelPacket *pixpack_source, int32_t rows, int32_t columns, float Thigh, float Tlow) {
	int32_t i, j, pos;
	float pixangle;
	int32_t sobelX, sobelY;
	int32_t temp_red = 0, temp_green = 0, temp_blue = 0;

	//vector length
	PixelPacket *length = malloc(columns * rows * sizeof(PixelPacket));
	if (NULL == length) {
		fprintf(stderr, "Couldn't allocate memory. (file: %s, line: %i)\n", __FILE__, __LINE__);
		return -1;
	}
	//gauss
	PixelPacket *gauss = malloc(columns * rows * sizeof(PixelPacket));
	if (NULL == gauss) {
		fprintf(stderr, "Couldn't allocate memory. (file: %s, line: %i)\n", __FILE__, __LINE__);
		return -1;
	}
	//NMS
	PixelPacket *nms = malloc(columns * rows * sizeof(PixelPacket));
	if (NULL == nms) {
		fprintf(stderr, "Couldn't allocate memory. (file: %s, line: %i)\n", __FILE__, __LINE__);
		return -1;
	}
	//hysteresis
	PixelPacket *hysteresis = malloc(columns * rows * sizeof(PixelPacket));
	if (NULL == hysteresis) {
		fprintf(stderr, "Couldn't allocate memory. (file: %s, line: %i)\n", __FILE__, __LINE__);
		return -1;
	}
	//angles
	int *angle = malloc(columns * rows * sizeof(int));
	if (NULL == angle) {
		fprintf(stderr, "Couldn't allocate memory. (file: %s, line: %i)\n", __FILE__, __LINE__);
		return -1;
	}
	
	if (Tlow > Thigh || Tlow > 1 || Thigh >1) {
		fprintf(stderr, "Wrong Parameter Tlow must be smaller than Thigh and Tlow and Thigh must be smaller than one. ");
		return -1;
	}

	//Gauss Filter -> blur image


#pragma acc kernels copyin(pixpack_source[0:columns*rows]) copyout(pixpack_target[0:columns*rows]) create(gauss[0:columns*rows]) create(length[0:columns*rows]) create(nms[0:columns*rows]) create(hysteresis[0:columns*rows]) create(angle[0:columns*rows])
	{
#pragma acc loop independent
		for (i = 2; i < rows - 2; ++i) {
			int32_t temp_red = 0, temp_green = 0, temp_blue = 0;
#pragma acc loop independent private(pos, temp_red, temp_green, temp_blue)
			for (j = 2; j < columns - 2; ++j) {
				pos = i * columns + j;
				temp_red	=	   pixpack_source[pos - (2 * columns) - 2].red
				                   +	 5 * pixpack_source[pos - (2 * columns) - 1].red
				                   +	 7 * pixpack_source[pos - (2 * columns)  ].red
				                   +	 5 * pixpack_source[pos - (2 * columns) + 1].red
				                   +	   pixpack_source[pos - (2 * columns) + 2].red;
				temp_green	=	   pixpack_source[pos - (2 * columns) - 2].green
				                   +	 5 * pixpack_source[pos - (2 * columns) - 1].green
				                   +	 7 * pixpack_source[pos - (2 * columns)  ].green
				                   +	 5 * pixpack_source[pos - (2 * columns) + 1].green
				                   +	   pixpack_source[pos - (2 * columns) + 2].green;
				temp_blue	=	   pixpack_source[pos - (2 * columns) - 2].blue
				                   +	 5 * pixpack_source[pos - (2 * columns) - 1].blue
				                   +	 7 * pixpack_source[pos - (2 * columns)  ].blue
				                   +	 5 * pixpack_source[pos - (2 * columns) + 1].blue
				                   +	   pixpack_source[pos - (2 * columns) + 2].blue;

				temp_red	+=	 5 * pixpack_source[pos - (columns) - 2].red
				                 +	20 * pixpack_source[pos - (columns) - 1].red
				                 +	33 * pixpack_source[pos - (columns)  ].red
				                 +	20 * pixpack_source[pos - (columns) + 1].red
				                 +	 5 * pixpack_source[pos - (columns) + 2].red;
				temp_green	+=	 5 * pixpack_source[pos - (columns) - 2].green
				                 +	20 * pixpack_source[pos - (columns) - 1].green
				                 +	33 * pixpack_source[pos - (columns)  ].green
				                 +	20 * pixpack_source[pos - (columns) + 1].green
				                 +	 5 * pixpack_source[pos - (columns) + 2].green;
				temp_blue	+=	 5 * pixpack_source[pos - (columns) - 2].blue
				                 +	20 * pixpack_source[pos - (columns) - 1].blue
				                 +	33 * pixpack_source[pos - (columns)  ].blue
				                 +	20 * pixpack_source[pos - (columns) + 1].blue
				                 +	 5 * pixpack_source[pos - (columns) + 2].blue;

				temp_red	+=	 7 * pixpack_source[pos - 2].red
				                 +	33 * pixpack_source[pos - 1].red
				                 +	55 * pixpack_source[pos  ].red
				                 +	33 * pixpack_source[pos + 1].red
				                 +	 7 * pixpack_source[pos + 2].red;
				temp_green	+=	 7 * pixpack_source[pos - 2].green
				                 +	33 * pixpack_source[pos - 1].green
				                 +	55 * pixpack_source[pos  ].green
				                 +	33 * pixpack_source[pos + 1].green
				                 +	 7 * pixpack_source[pos + 2].green;
				temp_blue	+=	 7 * pixpack_source[pos - 2].blue
				                 +	33 * pixpack_source[pos - 1].blue
				                 +	55 * pixpack_source[pos  ].blue
				                 +	33 * pixpack_source[pos + 1].blue
				                 +	 7 * pixpack_source[pos + 2].blue;

				temp_red	+=	 5 * pixpack_source[pos + (columns) - 2].red
				                 +	20 * pixpack_source[pos + (columns) - 1].red
				                 +	33 * pixpack_source[pos + (columns)  ].red
				                 +	20 * pixpack_source[pos + (columns) + 1].red
				                 +	 5 * pixpack_source[pos + (columns) + 2].red;
				temp_green	+=	 5 * pixpack_source[pos + (columns) - 2].green
				                 +	20 * pixpack_source[pos + (columns) - 1].green
				                 +	33 * pixpack_source[pos + (columns)  ].green
				                 +	20 * pixpack_source[pos + (columns) + 1].green
				                 +	 5 * pixpack_source[pos + (columns) + 2].green;
				temp_blue	+=	 5 * pixpack_source[pos + (columns) - 2].blue
				                 +	20 * pixpack_source[pos + (columns) - 1].blue
				                 +	33 * pixpack_source[pos + (columns)  ].blue
				                 +	20 * pixpack_source[pos + (columns) + 1].blue
				                 +	 5 * pixpack_source[pos + (columns) + 2].blue;

				temp_red	+=	   pixpack_source[pos + (2 * columns) - 2].red
				                   +	 5 * pixpack_source[pos + (2 * columns) - 1].red
				                   +	 7 * pixpack_source[pos + (2 * columns)  ].red
				                   +	 5 * pixpack_source[pos + (2 * columns) + 1].red
				                   +	   pixpack_source[pos + (2 * columns) + 2].red;
				temp_green	+=	   pixpack_source[pos + (2 * columns) - 2].green
				                   +	 5 * pixpack_source[pos + (2 * columns) - 1].green
				                   +	 7 * pixpack_source[pos + (2 * columns)  ].green
				                   +	 5 * pixpack_source[pos + (2 * columns) + 1].green
				                   +	   pixpack_source[pos + (2 * columns) + 2].green;
				temp_blue	+=	   pixpack_source[pos + (2 * columns) - 2].blue
				                   +	 5 * pixpack_source[pos + (2 * columns) - 1].blue
				                   +	 7 * pixpack_source[pos + (2 * columns)  ].blue
				                   +	 5 * pixpack_source[pos + (2 * columns) + 1].blue
				                   +	   pixpack_source[pos + (2 * columns) + 2].blue;

				gauss[pos].red		= temp_red		/ 339;
				gauss[pos].green	= temp_green	/ 339;
				gauss[pos].blue	= temp_blue		/ 339;
				gauss[pos].opacity	= 0;
			}
		}
		


#pragma acc loop independent private (sobelX, sobelY, pixangle)
		for (i = columns + 1; i < rows * columns - columns - 1; ++i) { //start at (1,1)
			/* SobelX:
			 * 1 0 -1
			 * 2 0 -2
			 * 1 0 -1
			 * SobelY:
			 * 1 2 1
			 * 0 0 0
			 * -1 -2 -1
			 */
			sobelX = 1 * gauss[i - columns - 1].red
			         + 0 * gauss[i - columns].red
			         + -1 * gauss[i - columns + 1].red
			         + 2 * gauss[i - 1].red
			         + 0 * gauss[i].red
			         + -2 * gauss[i + 1].red
			         + 1 * gauss[i + columns - 1].red
			         + 0 * gauss[i + columns].red
			         + -1 * gauss[i + columns + 1].red;

			sobelY = 1 * gauss[i - columns - 1].red
			         + 2 * gauss[i - columns].red
			         + 1 * gauss[i - columns + 1].red
			         + 0 * gauss[i - 1].red
			         + 0 * gauss[i].red
			         + 0 * gauss[i + 1].red
			         + -1 * gauss[i + columns - 1].red
			         + -2 * gauss[i + columns].red
			         + -1 * gauss[i + columns + 1].red;

			//vector length
			length[i].red = (uint16_t)sqrt(sobelX * sobelX + sobelY * sobelY);
			length[i].green = length[i].red;
			length[i].blue = length[i].red;

			//vector angle
			//float type from -90 to 90
			pixangle = atan2(sobelY * 1.0, sobelX * 1.0);
			pixangle *= 180;
			pixangle /= PI;

			//round vector angle
			if (pixangle >= -180.0 && pixangle < -157.5) {
				angle[i] = 0;
			}
			if (pixangle >= -157.5 && pixangle < -112.5) {
				angle[i] = 45;
			}
			if (pixangle >= -112.5 && pixangle < -67.5) {
				angle[i] = 90;
			}
			if (pixangle >= -67.5 && pixangle < -22.5) {
				angle[i] = 135;
			}
			if (pixangle >= -22.5 && pixangle < 22.5) {
				angle[i] = 0;
			}
			if (pixangle >= 22.5 && pixangle < 67.5) {
				angle[i] = 45;
			}
			if (pixangle >= 67.5 && pixangle < 112.5) {
				angle[i] = 90;
			}
			if (pixangle > 112.5 && pixangle < 157.5) {
				angle[i] = 135;
			}
			if (pixangle > 157.5 && pixangle <= 180.0) {
				angle[i] = 0;
			}
		}

		//#pragma omp barrier

#pragma acc loop independent private(i)
		for (i = columns * 2 + 2; i < rows * columns - columns * 2 - 2; ++i) { //start at (2,2)
			//NMS (non-maximum-suspression)
			//delete mismatching pixel
			if (angle[i] == 0) {
				if (length[i].red > length[i - 1].red && length[i].red > length[i + 1].red) {
					nms[i].red = length[i].red;
					nms[i].green = length[i].red;
					nms[i].blue = length[i].red;
				} else {
					nms[i].red = 0;
					nms[i].green = 0;
					nms[i].blue = 0;
				}
			}

			if (angle[i] == 45) {
				if (length[i].red > length[i - columns - 1].red && length[i].red > length[i + columns + 1].red) {
					nms[i].red = length[i].red;
					nms[i].green = length[i].red;
					nms[i].blue = length[i].red;
				} else {
					nms[i].red = 0;
					nms[i].green = 0;
					nms[i].blue = 0;
				}
			}
			if (angle[i] == 90) {
				if (length[i].red > length[i - columns].red && length[i].red > length[i + columns].red) {
					nms[i].red = length[i].red;
					nms[i].green = length[i].red;
					nms[i].blue = length[i].red;
				} else {
					nms[i].red = 0;
					nms[i].green = 0;
					nms[i].blue = 0;
				}
			}
			if (angle[i] == 135) {
				if (length[i].red > length[i - columns + 1].red && length[i].red > length[i + columns - 1].red) {
					nms[i].red = length[i].red;
					nms[i].green = length[i].red;
					nms[i].blue = length[i].red;
				} else {
					nms[i].red = 0;
					nms[i].green = 0;
					nms[i].blue = 0;
				}
			}
		}

#pragma acc loop independent
		for (i = columns * 2 + 2; i < rows * columns - columns * 2 - 2; i += 1) { //start at (2,2)
			//hysteresis
			float temp_thigh = Thigh * 65535;
			if (nms[i].red > (int32_t)temp_thigh) {
				hysteresis[i].red = 65535;
				hysteresis[i].green = 65535;
				hysteresis[i].blue = 65535;
			} else {
				int32_t j;
				int32_t k;
				if (angle[i] == 0) {
#pragma acc loop independent
					for (j = i; j < rows * columns - columns * 2 - 2; j += columns) {
						if (nms[j].red > (int32_t)Tlow * 65535) {
							hysteresis[j].red = 65535;
							hysteresis[j].green = 65535;
							hysteresis[j].blue = 65535;
						} else {
							break;
						}
					}
#pragma acc loop independent
					for (k = i; k > rows * 2 + 2; k -= columns) {
						if (nms[k].red > (int32_t)Tlow * 65535) {
							hysteresis[k].red = 65535;
							hysteresis[k].green = 65535;
							hysteresis[k].blue = 65535;
						} else {
							break;
						}
					}
				}
				if (angle[i] == 45) {
#pragma acc loop independent
					for (j = i; j < rows * columns - columns * 2 - 2; j += columns - 1) {
						if (nms[j].red > (int32_t)Tlow * 65535) {
							hysteresis[j].red = 65535;
							hysteresis[j].green = 65535;
							hysteresis[j].blue = 65535;
						} else {
							break;
						}
					}
#pragma acc loop independent
					for (k = i; k > rows * 2 + 2; k -= columns + 1) {
						if (nms[k].red > (int32_t)Tlow * 65535) {
							hysteresis[k].red = 65535;
							hysteresis[k].green = 65535;
							hysteresis[k].blue = 65535;
						} else {
							break;
						}
					}
				}
				if (angle[i] == 90) {
					int32_t j;
					int32_t k;
#pragma acc loop independent
					for (j = i; j < rows * columns - columns * 2 - 2; j += 1) {
						if (nms[j].red > (int32_t)Tlow * 65535) {
							hysteresis[j].red = 65535;
							hysteresis[j].green = 65535;
							hysteresis[j].blue = 65535;
						} else {
							break;
						}
					}
#pragma acc loop independent
					for (k = i; k > rows * 2 + 2; k -= 1) {
						if (nms[k].red > (int32_t)Tlow * 65535) {
							hysteresis[k].red = 65535;
							hysteresis[k].green = 65535;
							hysteresis[k].blue = 65535;
						} else {
							break;
						}
					}
				}

				if (angle[i] == 135) {
					int32_t j;
					int32_t k;
#pragma acc loop independent
					for (j = i; j < rows * columns - columns * 2 - 2; j += columns + 1) {
						if (nms[j].red > (int32_t)Tlow * 65535) {
							hysteresis[j].red = 65535;
							hysteresis[j].green = 65535;
							hysteresis[j].blue = 65535;
						} else {
							break;
						}
					}
#pragma acc loop independent
					for (k = i; k > rows * 2 + 2; k -= columns - 1) {
						if (nms[k].red > (int32_t)Tlow * 65535) {
							hysteresis[k].red = 65535;
							hysteresis[k].green = 65535;
							hysteresis[k].blue = 65535;
						} else {
							break;
						}
					}
				}

			}
			float temp_tlow = Tlow * 65535;
			if (nms[i].red < (int32_t)temp_tlow) {
				hysteresis[i].red = 0;
				hysteresis[i].green = 0;
				hysteresis[i].blue = 0;
			}
			
			uint16_t val = hysteresis[i].red;
			pixpack_target[i].red = val;
			pixpack_target[i].green = val;
			pixpack_target[i].blue = val;
			pixpack_target[i].opacity = 0;
		}

	}
	free(length);
	free(gauss);
	free(nms);
	free(hysteresis);
	free(angle);
	return 0;
}
