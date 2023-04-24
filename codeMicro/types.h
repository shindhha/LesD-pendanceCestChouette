/**
 * \file      types.h
 * \author    JP/MS/CM
 * \date      04/12/2012
 * \version   00
 * \brief     Déclarations des types utilisés sur le projet
 *
 * \details
 *		- Projet		  : SAFeasy-200
 *		- Compilateur     : c51  V5.02    1995 KEIL ELECTRONIC
 *		- Linling locator : l51  V3.52    1995 KEIL ELECTRONIC
 *		- Formatter       : o51  V2.1     1995 KEIL ELECTRONIC
 */

/* ______ WRAPPEUR _________________________________________	*/
#ifndef TYPES_
#define TYPES_ 1

/* ______ DEFINE-DECLARATIONS ______________________________	*/

/**
 *	\name Déclaration des types NON SIGNES
 */
/**@{*/

/**
 *		\brief  8 bits, 1 octet, [ 0..255 ]
 */
typedef unsigned char u8sod;

/**
 *		\brief  16 bits, 2 octets, [ 0..65535 ]
 */
typedef unsigned short u16sod;

/**
 *		\brief  32 bits, 4 octets, [ 0..4294967295 ]
 */
typedef unsigned long u32sod;

/**@}*/

/**
 *	\name Déclaration des types SIGNES
 */
/**@{*/

/**
 *		\brief  8 bits, 1 octet, [ -128..+127 ]
 */
typedef char s8sod;

/**
 *		\brief  16 bits, 2 octets, [ -32768..+32767 ]
 */
typedef short s16sod;

/**
 *		\brief  32 bits, 4 octets, [ -2147483648..+2147483647 ]
 */
typedef signed long s32sod;

/**
 *		\brief  32 bits, 4 octets, [ +/-1.175494E-38..+/-3.402823E+38 ]
 */
typedef float f32sod;

/**
 *		\brief  64 bits, 8 octets, IEEE 754-1985 double precision
 */
typedef double f64sod;
/**@}*/

/* ______ PROTOTYPES _______________________________________	*/
#endif