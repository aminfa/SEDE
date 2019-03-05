/* service_radialblur_scpu.c */

#include "service_radialblur.h"

int32_t run_service_radialblur_scpu(PixelPacket *pixpack_target, PixelPacket *pixpack_source, int32_t rows, int32_t columns) {

	int32_t centerx = columns / 2;
	int32_t centery = rows / 2;
	int32_t temp_red = 0, temp_green = 0, temp_blue = 0;
	int32_t i, j, pos;

	int32_t max_rad = sqrt(centerx * centerx + centery * centery) + 0.5;
	PixelPacket *coord = (PixelPacket *) malloc(sizeof(PixelPacket) * rows * (columns));
	if (NULL == coord) {
		fprintf(stderr, "Couldn't allocate memory (file: %s, line: %i)\n", __FILE__, __LINE__);
		return -1;
	}

	int32_t cartx, carty, rad, src_x, src_y, newpos, rasterx, rastery;
	double theta;

	for (i = 0; i < rows; i++) {
		for (j = 0; j < columns; j++) {
			pos = i * columns + j;
			cartx = j - centerx;
			carty = centery - i;
			rad = i * max_rad / rows;
			theta = j * 2.0 * M_PI / columns;
			src_x = rad * cos(theta);
			src_y = rad * sin(theta);
			if (src_y > centery) src_y = centery;
			rasterx = centerx + src_x;
			rastery = centery - src_y;
			newpos = rastery * columns + rasterx;
			if (newpos < rows * columns) {
				coord[pos].red = pixpack_source[newpos].red;
				coord[pos].green = pixpack_source[newpos].green;
				coord[pos].blue = pixpack_source[newpos].blue;
				coord[pos].opacity = pixpack_source[newpos].opacity;
			}
		}
	}
	
	int32_t n;
	for (n = 0; n < 4; n++) {
		for (i = 1; i < rows - 1; i++) {
			for (j = 1; j < columns - 1; j++) {
				pos = i * columns + j;
				int32_t la_pos = (i - 1) * columns + (j - 1);
				int32_t a_pos = (i - 1) * columns + (j);
				int32_t ra_pos = (i - 1) * columns + (j + 1);
				int32_t l_pos = (i) * columns + (j - 1);
				int32_t r_pos = (i) * columns + (j + 1);
				int32_t lu_pos = (i + 1) * columns + (j - 1);
				int32_t u_pos = (i + 1) * columns + (j);
				int32_t ru_pos = (i + 1) * columns + (j + 1);
				temp_red = coord[pos].red
				           + coord[la_pos].red
				           + coord[a_pos].red
				           + coord[ra_pos].red
				           + coord[l_pos].red
				           + coord[r_pos].red
				           + coord[lu_pos].red
				           + coord[u_pos].red
				           + coord[ru_pos].red;
				temp_green = coord[pos].green
				             + coord[la_pos].green
				             + coord[a_pos].green
				             + coord[ra_pos].green
				             + coord[l_pos].green
				             + coord[r_pos].green
				             + coord[lu_pos].green
				             + coord[u_pos].green
				             + coord[ru_pos].green;
				temp_blue = coord[pos].blue
				            + coord[la_pos].blue
				            + coord[a_pos].blue
				            + coord[ra_pos].blue
				            + coord[l_pos].blue
				            + coord[r_pos].blue
				            + coord[lu_pos].blue
				            + coord[u_pos].blue
				            + coord[ru_pos].blue;

				coord[pos].red = temp_red / 9;
				coord[pos].green = temp_green / 9;
				coord[pos].blue = temp_blue / 9;
			}
		}
	}



	for (i = 0; i < rows; i++) {
		for (j = 0; j < columns; j++) {
			pos = i * columns + j;
			cartx = j - centerx;
			carty = centery - i;
			rad = sqrt(cartx * cartx + carty * carty);
			theta = atan2(carty, cartx);
			if (theta < 0) theta += 2.0 * M_PI;
			src_y = (rad * rows) / max_rad;
			src_x = (theta / (2.0 * M_PI)) * columns;
			newpos = src_y * columns + src_x;
			if (newpos < rows * columns) {
				pixpack_target[pos].red = coord[newpos].red;
				pixpack_target[pos].green = coord[newpos].green;
				pixpack_target[pos].blue = coord[newpos].blue;
			}
		}
	}

	free(coord);
	return 0;
}
