/*---------------------------------------------------------------------------
;	Copyright (C) 2002 CYGNAL INTEGRATED PRODUCTS, INC.
; 	All rights reserved.
;
;
; 	FILE NAME  	: C8051F120.H 
; 	TARGET MCUs	: C8051F120, 'F121, 'F122, 'F123, 'F124, 'F125, "F126, 'F127 
; 	DESCRIPTION	: Register/bit definitions for the C8051F12x product family.  
;
; 	REVISION 1.41 	
;
;---------------------------------------------------------------------------*/

#ifndef C51F120_
#define C51F120_ 1

/* SFR PAGE DEFINITIONS */
#define  CONFIG_PAGE       0x0FU     /* SYSTEM AND PORT CONFIGURATION PAGE */
#define  LEGACY_PAGE       0x00U     /* LEGACY SFR PAGE                    */
#define  TIMER01_PAGE      0x00U     /* TIMER 0 AND TIMER 1                */
#define  CPT0_PAGE         0x01U     /* COMPARATOR 0                       */
#define  CPT1_PAGE         0x02U     /* COMPARATOR 1                       */
#define  UART0_PAGE        0x00U     /* UART 0                             */
#define  UART1_PAGE        0x01U     /* UART 1                             */
#define  SPI0_PAGE         0x00U     /* SPI 0                              */
#define  EMI0_PAGE         0x00U     /* EXTERNAL MEMORY INTERFACE          */
#define  ADC0_PAGE         0x00U     /* ADC 0                              */
#define  ADC2_PAGE         0x02U     /* ADC 2                              */
#define  SMB0_PAGE         0x00U     /* SMBUS 0                            */
#define  TMR2_PAGE         0x00U     /* TIMER 2                            */
#define  TMR3_PAGE         0x01U     /* TIMER 3                            */
#define  TMR4_PAGE         0x02U     /* TIMER 4                            */
#define  DAC0_PAGE         0x00U     /* DAC 0                              */
#define  DAC1_PAGE         0x01U     /* DAC 1                              */
#define  PCA0_PAGE         0x00U     /* PCA 0                              */
#define  PLL0_PAGE         0x0FU     /* PLL 0                              */

#endif
