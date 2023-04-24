/**
 * \file      init_uc.c
 * \author    JP
 * \date      29/09/2014
 * \version   00
 * \brief     module d'initialisation du microcontroleur C8051F126
 *
 * \details
 *		- Projet		  : SAFeasy-200
 *		- Compilateur     : c51  V5.02    1995 KEIL ELECTRONIC
 *		- Linling locator : l51  V3.52    1995 KEIL ELECTRONIC
 *		- Formatter       : o51  V2.1     1995 KEIL ELECTRONIC
 */
/*======================================================================== */ 
/*Historique  :                                                            */
/*======================================================================== */ 

/* ______ INCLUDE - DIRECTIVES _____________________________	*/
#include "types.h"
#include "main.h"
#include "C51F120.h"
#include "init_uc.h"

/* ______ DEFINE-DECLARATIONS ______________________________	*/

/**
 *    \name Declaration des SFR (Special Function Register) 
 */
/**@{*/
#if (TEST_GCC)
/*	COMPILATIONS SUR PC						*/
u8sod SFRPAGE;    /**< SFR PAGE SELECT                              */
u8sod TCON   ;    /**< TIMER CONTROL                                */
u8sod TMOD   ;    /**< TIMER MODE                                   */
u8sod PLL0CN ;    /**< PLL 0 CONTROL                                */
u8sod TL0    ;    /**< TIMER 0 - LOW BYTE                           */
u8sod OSCICN ;    /**< INTERNAL OSCILLATOR CONTROL                  */
u8sod OSCXCN ;    /**< EXTERNAL OSCILLATOR CONTROL                  */
u8sod TL1    ;    /**< TIMER 1 - LOW BYTE                           */
u8sod TH0    ;    /**< TIMER 0 - HIGH BYTE                          */
u8sod TH1    ;    /**< TIMER 1 - HIGH BYTE                          */
u8sod PLL0DIV;    /**< PLL 0 DIVIDER                                */
u8sod CKCON  ;    /**< TIMER 0/1 CLOCK CONTROL                      */
u8sod PLL0MUL;    /**< PLL 0 MULTIPLIER                             */
u8sod PLL0FLT;    /**< PLL 0 FILTER                                 */
u8sod CLKSEL ;    /**< SYSTEM CLOCK SELECT                          */
u8sod SPI0CFG;    /**< SPI 0 CONFIGURATION                          */
u8sod P4MDOUT;    /**< PORT 4 OUTPUT MODE                           */
u8sod SPI0CKR;    /**< SPI 0 CLOCK RATE CONTROL                     */
u8sod P5MDOUT;    /**< PORT 5 OUTPUT MODE                           */
u8sod P5     ;    /**< PORT 5                                       */
u8sod EMI0TC ;    /**< EMIF TIMING CONTROL                          */
u8sod CCH0CN ;    /**< CACHE CONTROL                                */
u8sod EMI0CF ;    /**< EMIF CONFIGURATION                           */
u8sod P2MDOUT;    /**< PORT 2 OUTPUT MODE CONFIGURATION             */
u8sod P3MDOUT;    /**< PORT 3 OUTPUT MODE CONFIGURATION             */
u8sod FLSCL  ;    /**< FLASH SCALE                                  */
u8sod SMB0CN ;    /**< SMBUS 0 CONTROL                              */
u8sod SMB0CR ;    /**< SMBUS 0 CLOCK RATE                           */
u8sod XBR0   ;    /**< CROSSBAR CONFIGURATION REGISTER 0            */
u8sod XBR1   ;    /**< CROSSBAR CONFIGURATION REGISTER 1            */
u8sod XBR2   ;    /**< CROSSBAR CONFIGURATION REGISTER 2            */
u8sod RSTSRC ;    /**< RESET SOURCE                                 */
u8sod SPI0CN ;    /**< SPI 0 CONTROL                                */	
u8sod WDTCN  ;    /**< WATCHDOG TIMER CONTROL                       */
u8sod IE     ;    /**< INTERRUPT ENABLE                             */
u8sod EIE1   ;    /**< EXTERNAL INTERRUPT ENABLE 1                  */
u8sod EIE2   ;	  /**< EXTERNAL INTERRUPT ENABLE 2  */
u8sod TMR3CF ;    /**< TIMER 3 CONFIGURATION                        */
u8sod RCAP3H ;    /**< TIMER 3 CAPTURE REGISTER - HIGH BYTE         */
u8sod RCAP3L ;    /**< TIMER 3 CAPTURE REGISTER - LOW BYTE          */
u8sod TMR4CF ;    /**< TIMER 4 CONFIGURATION                        */
u8sod RCAP4L ;    /**< TIMER 4 CAPTURE REGISTER - LOW BYTE          */
u8sod RCAP4H ;    /**< TIMER 4 CAPTURE REGISTER - HIGH BYTE         */
u8sod TMR4CN ;    /**< TIMER 4 CONTROL                              */
u8sod TMR4L ;     /**< TIMER 4 - LOW BYTE                           */
u8sod P6MDOUT;    /**< PORT 6 OUTPUT MODE                           */
u8sod P7MDOUT;    /**< PORT 7 OUTPUT MODE                           */
u8sod P1MDOUT;    /**< PORT 1 OUTPUT MODE                           */
u8sod P1     ;    /**< PORT 1                                       */
u8sod P0MDOUT;    /**< PORT 0 OUTPUT MODE                           */
u8sod P0     ;    /**< PORT 0                                       */	
u8sod CPT0CN ;	  /**< COMPARATOR 0 CONTROL                         */
u8sod CPT1CN ;    /**< COMPARATOR 1 CONTROL                         */
u8sod P2     ;    /**< PORT 2                                          */
u8sod P3     ;    /**< PORT 3                                          */
u8sod P4     ;    /**< PORT 4                                          */
u8sod REF0CN ;	  /**< VOLTAGE REFERENCE 0 CONTROL					 */
u8sod AMX0CF;	  /**< ADC 0 MUX CONFIGURATION						 */
u8sod AMX0SL;	  /**< ADC 0 MUX CHANNEL SELECTION					 */
u8sod ADC0CF;	  /**< ADC 0 CONFIGURATION							 */
u8sod ADC0CN;	  /**< ADC 0 CONTROL									 */	
u8sod DAC0H;      /**< DAC0 High byte			    */
u8sod DAC0L;      /**< DAC0 Low byte			    */
u8sod DAC0CN;     /**< DAC0 Control   			    */
u8sod P6;         /**< PORT 6                                       */
u8sod P7;         /**< PORT 7                                       */

#else
/*	COMPILATIONS SUR CIBLE				*/
sfr SFRPAGE  = 0x84U;    /**< SFR PAGE SELECT                              */
sfr TCON     = 0x88U;    /**< TIMER CONTROL                                */
sfr TMOD     = 0x89U;    /**< TIMER MODE                                   */
sfr PLL0CN   = 0x89U;    /**< PLL 0 CONTROL                                */
sfr TL0      = 0x8AU;    /**< TIMER 0 - LOW BYTE                           */
sfr OSCICN   = 0x8AU;    /**< INTERNAL OSCILLATOR CONTROL                  */
sfr OSCXCN   = 0x8CU;    /**< EXTERNAL OSCILLATOR CONTROL                  */
sfr TL1      = 0x8BU;    /**< TIMER 1 - LOW BYTE                           */
sfr TH0      = 0x8CU;    /**< TIMER 0 - HIGH BYTE                          */
sfr TH1      = 0x8DU;    /**< TIMER 1 - HIGH BYTE                          */
sfr PLL0DIV  = 0x8DU;    /**< PLL 0 DIVIDER                                */
sfr CKCON    = 0x8EU;    /**< TIMER 0/1 CLOCK CONTROL                      */
sfr PLL0MUL  = 0x8EU;    /**< PLL 0 MULTIPLIER                             */
sfr PLL0FLT  = 0x8FU;    /**< PLL 0 FILTER                                 */
sfr CLKSEL   = 0x97U;    /**< SYSTEM CLOCK SELECT                          */
sfr SPI0CFG  = 0x9AU;    /**< SPI 0 CONFIGURATION                          */
sfr P4MDOUT  = 0x9CU;    /**< PORT 4 OUTPUT MODE                           */
sfr SPI0CKR  = 0x9DU;    /**< SPI 0 CLOCK RATE CONTROL                     */
sfr P5MDOUT  = 0x9DU;    /**< PORT 5 OUTPUT MODE                           */
sfr P5       = 0xD8U;    /**< PORT 5                                       */
sfr EMI0TC   = 0xA1U;    /**< EMIF TIMING CONTROL                          */
sfr CCH0CN   = 0xA1U;    /**< CACHE CONTROL                                */
sfr EMI0CF   = 0xA3U;    /**< EMIF CONFIGURATION                           */
sfr P2MDOUT  = 0xA6U;    /**< PORT 2 OUTPUT MODE CONFIGURATION             */
sfr P3MDOUT  = 0xA7U;    /**< PORT 3 OUTPUT MODE CONFIGURATION             */
sfr FLSCL    = 0xB7U;    /**< FLASH SCALE                                  */
sfr SMB0CN   = 0xC0U;    /**< SMBUS 0 CONTROL                              */
sfr SMB0CR   = 0xCFU;    /**< SMBUS 0 CLOCK RATE                           */
sfr XBR0     = 0xE1U;    /**< CROSSBAR CONFIGURATION REGISTER 0            */
sfr XBR1     = 0xE2U;    /**< CROSSBAR CONFIGURATION REGISTER 1            */
sfr XBR2     = 0xE3U;    /**< CROSSBAR CONFIGURATION REGISTER 2            */
sfr RSTSRC   = 0xEFU;    /**< RESET SOURCE                                 */
sfr SPI0CN   = 0xF8U;    /**< SPI 0 CONTROL                                */	
sfr WDTCN    = 0xFFU;    /**< WATCHDOG TIMER CONTROL                       */
sfr IE       = 0xA8U;    /**< INTERRUPT ENABLE                             */
sfr EIE1     = 0xE6U;    /**< EXTERNAL INTERRUPT ENABLE 1                  */
sfr EIE2     = 0xE7U;	/**< EXTERNAL INTERRUPT ENABLE 2  */
sfr TMR3CF   = 0xC9U;    /**< TIMER 3 CONFIGURATION                        */
sfr RCAP3H   = 0xCBU;    /**< TIMER 3 CAPTURE REGISTER - HIGH BYTE         */
sfr RCAP3L   = 0xCAU;    /**< TIMER 3 CAPTURE REGISTER - LOW BYTE          */
sfr TMR4CF   = 0xC9U;    /**< TIMER 4 CONFIGURATION                        */
sfr RCAP4L   = 0xCAU;    /**< TIMER 4 CAPTURE REGISTER - LOW BYTE          */
sfr RCAP4H   = 0xCBU;    /**< TIMER 4 CAPTURE REGISTER - HIGH BYTE         */
sfr TMR4CN   = 0xC8U;    /**< TIMER 4 CONTROL                              */
sfr TMR4L    = 0xCCU;    /**< TIMER 4 - LOW BYTE                           */
sfr P6MDOUT  = 0x9EU;    /**< PORT 6 OUTPUT MODE                           */
sfr P7MDOUT  = 0x9FU;    /**< PORT 7 OUTPUT MODE                           */
sfr P1MDOUT  = 0xA5U;    /**< PORT 1 OUTPUT MODE                           */
sfr P1       = 0x90U;    /**< PORT 1                                       */
sfr P0MDOUT  = 0xA4U;    /**< PORT 0 OUTPUT MODE                           */
sfr P0       = 0x80U;    /**< PORT 0                                       */	
sfr CPT0CN   = 0x88U;	/**< COMPARATOR 0 CONTROL                         */
sfr CPT1CN   = 0x88U;    /**< COMPARATOR 1 CONTROL                         */
sfr P2       = 0xA0U;    /**< PORT 2                                          */
sfr P3       = 0xB0U;    /**< PORT 3                                          */
sfr P4       = 0xC8U;    /**< PORT 4                                          */
sfr REF0CN   = 0xD1U;	/**< VOLTAGE REFERENCE 0 CONTROL					*/
sfr AMX0CF   = 0xBAU;	/**< ADC 0 MUX CONFIGURATION						*/
sfr AMX0SL   = 0xBBU;	/**< ADC 0 MUX CHANNEL SELECTION					*/
sfr ADC0CF   = 0xBCU;	/**< ADC 0 CONFIGURATION							*/
sfr ADC0CN   = 0xE8U;	/**< ADC 0 CONTROL								*/	
sfr DAC0H    = 0xD3U;    /**< DAC0 High byte			    */
sfr DAC0L    = 0xD2U;    /**< DAC0 Low byte			    */
sfr DAC0CN   = 0xD4U;    /**< DAC0 Control   			    */
sfr P6       = 0xE8U;    /**< PORT 6                                       */
sfr P7       = 0xF8U;    /**< PORT 7                                       */
#endif
/**@}*/

/* prototypes locaux */
static void Init_Reset_Sources(void);
static void Init_Oscillateur(void);
static void Init_Timer4 (void);
static void Init_Port_IO(void);
static u8sod u8_Test_Pll_Lock(void);
static void DAC_Init(void);

/* ______ PROCEDURES _______________________________________	*/
/* Delimitation de la zone d'entete du fichier pour l'analyse LDRA */
/*LDRA_HEADER_END */

/** \brief Configuration des sources de reset interne
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register WDTCN <br>
  * <b>Ressource :</b> - Special Function Register SFRPAGE <br>
  * <b>Ressource :</b> - Special Function Register RSTSRC <br>
  */
static void Init_Reset_Sources(void)
{
	/* desactivation du watchdog interne au microcontroleur */
	/* sequence d'ecriture de 0xDE suivi de 0xAD dans WDTCN */
    WDTCN     = 0xDEU;
    WDTCN     = 0xADU;
	
	/* selection du banc de special registers */
    SFRPAGE   = LEGACY_PAGE;
	
	/* Desactivation de toutes les source de reset interne  */
    RSTSRC    = 0x00U;
}/*Init_Reset_Sources*/

/** \brief Test du verouillage de la PLL interne au microcontroleur
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register PLL0CN <br>
  */
static u8sod u8_Test_Pll_Lock(void)
{
	return((u8sod)(PLL0CN & 0x10U));
}/*u8_Test_Pll_Lock*/

/** \brief Configuration de l'oscillateur et de la PLL interne
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register SFRPAGE <br>
  * <b>Ressource :</b> - Special Function Register OSCICN <br>
  * <b>Ressource :</b> - Special Function Register CCH0CN <br>
  * <b>Ressource :</b> - Special Function Register FLSCL <br>
  * <b>Ressource :</b> - Special Function Register CCH0CN <br>
  * <b>Ressource :</b> - Special Function Register PLL0CN <br>
  * <b>Ressource :</b> - Special Function Register PLL0DIV <br>
  * <b>Ressource :</b> - Special Function Register PLL0FLT <br>
  * <b>Ressource :</b> - Special Function Register PLL0MUL <br>
  * <b>Ressource :</b> - Special Function Register CLKSEL <br>
  */
static void Init_Oscillateur(void)
{
	u16sod loc_u16_cpt = 0U;

	/* selection du banc de special registers */
	SFRPAGE   = CONFIG_PAGE;
	/*activation de l'horloge CMOS externe */
	OSCXCN    = 0x20U;
	/*PLL Reference Clock Source is External Oscillator*/
	PLL0CN    = 0x04U;
	/* configuration des temps d'acces en memoire flash */
	/* desactivation du cache prefetch engine */
	CCH0CN    &= 0xDFU;
	 /* selection du banc de special registers */
	SFRPAGE   = LEGACY_PAGE;
	/* config temps d'acces */
	FLSCL     = 0x90U;
	/* selection du banc de special registers */
	SFRPAGE   = CONFIG_PAGE;
	 /* activation du cache prefetch engine */
	CCH0CN    |= 0x20U;

#if (TYPE_OSC_EXTERNE == TYPE_OSC_27MHZ)
	/* configuration de la PLL interne */
	/* activation de la PLL interne */
	PLL0CN    |= 0x01U;
	/* division de la reference par 6 */
	PLL0DIV   = 0x06U;
	/* selection du filtre de la PLL */
	PLL0FLT   = 0x21U;
	/* multiplication par 11 de la frequence */
	PLL0MUL   = 0x0BU;
#else
	/* configuration de la PLL interne */
	/* activation de la PLL interne */
	PLL0CN    |= 0x01U;
	/* division de la reference */
	PLL0DIV   = 0x01U;
	/* selection du filtre de la PLL */
	PLL0FLT   = 0x21U;
	/* multiplication par 2 de la frequence */
	PLL0MUL   = 0x02U;
#endif

	/* boucle d'attente de l'initialisation de la PLL */
	for (loc_u16_cpt = 0U; loc_u16_cpt < 15U; loc_u16_cpt++)
		{
			;  /* duree environ 5 us */
		}

	/* activation de la PLL */
	PLL0CN    |= 0x02U;

	/* attente de verrouillage de la PLL */
/* suppression de la regle LDRA 28D - "Potentially infinite loop found" */
/* suppression de la regle LDRA 139S - "Construct leads to infeasible code" */
/* cette boucle est normalement non infinie, mais en cas de problème, le watchdog va réseter le micro*/
/*LDRA_INSPECTED 139 S */
/*LDRA_INSPECTED 28 D */
	while (u8_Test_Pll_Lock() == 0U)
	{
		; /* en cas de probleme, le watchdog externe va nous resetter */
	}

	/* configuration de la source d'horloge systeme = sortie de la PLL */
	CLKSEL    = 0x02U;
	/* désactivation de l'oscillateur interne*/
	OSCICN    &= 0x7FU;
	
}/*Init_Oscillateur*/

/** \brief Initialisation du Timer4
  *
  * \return void
  *
  */
static void Init_Timer4 (void)
{
	u8sod 	loc_u8_sfrpage_save;	
	loc_u8_sfrpage_save = SFRPAGE;		/* sauvegarde du registre SFRPAGE */

	SFRPAGE   = TMR4_PAGE;				/* Sélection de la page */
	TMR4CF    = 0x08U;					/* SYSCLK ; No toggle */
	TMR4CN    = 0x04U;					/* Timer4 enabled ; AutoReload Mode */
#if (TYPE_OSC_EXTERNE == TYPE_OSC_27MHZ)
	RCAP4H    = 0xFFU;					/* Pour un Timer a 5us */
	RCAP4L    = 0x09U;
#else
	RCAP4H    = 0xFFU;					/* Pour un Timer a 5us */
	RCAP4L    = 0x06U;
#endif	

	SFRPAGE = loc_u8_sfrpage_save;		/* restauration du registre SFRPAGE */

}/*Init_Timer4*/

/** \brief Configuration des ports et des peripheriques internes
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register SFRPAGE <br>
  * <b>Ressource :</b> - Special Function Register XBR0, XBR1 et XBR2 <br>
  * <b>Ressource :</b> - Special Function Register P0 et P0MDOUT <br>
  * <b>Ressource :</b> - Special Function Register P1 et P1MDOUT <br>
  * <b>Ressource :</b> - Special Function Register P2MDOUT, P3MDOUT, P4MDOUT <br>
  * <b>Ressource :</b> - Special Function Register P5, P5MDOUT <br>
  * <b>Ressource :</b> - Special Function Register P6MDOUT, P7MDOUT <br>
  */
static void Init_Port_IO(void)
{

	/* PORT  -  CROSSBAR                            - SIGNAL SCHEMA
	 *
	 *
	 * P0.0  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P0.1  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P0.2  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P0.3  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P0.4  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P0.5  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P0.6  -  Unassigned,  Push-Pull,  Digital	--> AUDIO_WDOG_UC
     * P0.7  -  Unassigned,  Open-Drain, Digital	<-- unused
																				 
	 * P1.0  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P1.1  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P1.2  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P1.3  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P1.4  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P1.5  -  Unassigned,  Open-Drain, Digital	<-- unused
     * P1.6  -  Unassigned,  Push-Pull,  Digital	--> LED_DEBUG
     * P1.7  -  Unassigned,  Open-Drain, Digital	<-- unused
																				 
     * P2.0  -  INPUT,	Open-Drain, Digital			<-- CMD_11_A
     * P2.1  -  INPUT,	Open-Drain, Digital			<-- CMD_11_B
     * P2.2  -  INPUT,	Open-Drain, Digital			<-- CMD_21_A
     * P2.3  -  INPUT,	Open-Drain, Digital			<-- CMD_21_B
     * P2.4  -  INPUT,	Open-Drain, Digital			<-- ETAT_21_A
     * P2.5  -  INPUT,	Open-Drain, Digital			<-- ETAT_21_B
     * P2.6  -  INPUT,	Open-Drain, Digital			<-- SWITCH_AUDIO_1
     * P2.7  -  Unassigned,  Open-Drain, Digital	<-- unused
	 																			 
     * P3.0  -  OUTPUT,		pushpull, Digital		--> ETAT_11_A
     * P3.1  -  OUTPUT,		pushpull, Digital		--> ETAT_11_B
     * P3.2  -  OUTPUT,		pushpull, Digital		--> SGL_VIE_11
	 * P3.3  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P3.4  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P3.5  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P3.6  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P3.7  -  Unassigned,	Open-Drain, Digital		<-- unused
																				 
     * P4.0  -  OUTPUT,  pushpull, Digital			--> P11
     * P4.1  -  OUTPUT,  pushpull, Digital			--> P12
     * P4.2  -  Unassigned,	Open-Drain, Digital		--> SW_ANN_ON_1
	 * P4.3  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P4.4  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P4.5  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P4.6  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P4.7  -  Unassigned,	Open-Drain, Digital		<-- unused
																				 
	 * P5.0  -  Unassigned,	Open-Drain, Digital		<-- unused
	 * P5.1  -  Unassigned,	Open-Drain, Digital		<-- unused
	 * P5.2  -  Unassigned,	Open-Drain, Digital		<-- unused
	 * P5.3  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P5.4  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P5.5  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P5.6  -  Unassigned,	Open-Drain, Digital		<-- unused
     * P5.7  -  Unassigned,	Open-Drain, Digital		<-- unused
	*/

	/* Configuration PROTOTYPE */
	SFRPAGE   = CONFIG_PAGE;
	P0MDOUT   = 0x40U;		P0 = 0xFFU;
	P1MDOUT   = 0x40U;		P1 = 0xFFU;
	P2MDOUT   = 0x00U;		P2 = 0xFFU;
	P3MDOUT   = 0x07U;		P3 = 0xFFU;
	P4MDOUT   = 0x03U;		P4 = 0xFFU;
	P5MDOUT   = 0x00U;		P5 = 0xFFU;
	P6MDOUT   = 0x00U;		P6 = 0xFFU;
	P7MDOUT   = 0x00U;		P7 = 0xFFU;
	/* autorisation du crossbar */
	XBR2      = 0x40U;

}/*Init_Port_IO*/

/** \brief Inhibition de l'interruption TIMER4 du microcontroleur
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register EIE2 <br>
  */
void Masque_Interruption_Timer4(void)
{
	/* on autorise l'interruption timer4									   */
	EIE2 &= ~(0x04U);
}/*Masque_Interruption_Timer4*/

/** \brief Autorisation de l'interruption TIMER4 du microcontroleur
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register EIE2 <br>
  */
void Demasque_Interruption_Timer4(void)
{
	/* on autorise l'interruption timer4									   */
	EIE2 |= 0x04U;
}/*Demasque_Interruption_Timer4*/

/** \brief Autorisation de toutes les interruptions du ucontroleur
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register IE <br>
  */
void Autorise_toutes_interruptions(void)
{
	/* mise a un du flag d'autorisation des interruptions */
	IE = IE | 0x80U;
}/*Autorise_toutes_interruptions*/

/** \brief Configuration du DAC0 du microcontroleur interne
  *
  * \return void
  *
  */
static void DAC_Init(void)
{
	u8sod 	loc_u8_sfrpage_save;	
	loc_u8_sfrpage_save = SFRPAGE;		/* sauvegarde du registre SFRPAGE */

	
	/* selection de la page de registre du DAC0*/
	SFRPAGE   = DAC0_PAGE;
	/* init du DAC0:
	 * DAC0 autorise: 1XXXXXXX
	 * DAC output updates occur on Timer 4 overflow: XXX10XXX
	 * DAC data format bits left justified: XXXXX1XX
	 */
	DAC0CN    = 0x94U;

	/* par defaut, on sort du silence */
	DAC0L = 0x00U;
	DAC0H = 0x80U;


	/* il faut autoriser le bias interne */
	SFRPAGE = ADC0_PAGE;
	REF0CN = 0x02U;

	SFRPAGE = loc_u8_sfrpage_save;		/* restauration du registre SFRPAGE */
}/*DAC_Init*/


/** \brief Initialisation du microcontroleur interne
  *
  * \return void
  *
  */
void Init_micro_interne(void)
{
	/* init des sources de reset */
	Init_Reset_Sources();

	/* init des Timers materiels*/
	Init_Timer4();

	/* init du crossbar et des ports IO */
	Init_Port_IO();

	/* Initialisation du module Commande : fait asap apres la config des ports */
	//Init_Commandes();

	/* init de l'oscillateur interne */
	//Init_Oscillateur();

	/* init DAC */
	//DAC_Init();
	
}/*Init_micro_interne*/

