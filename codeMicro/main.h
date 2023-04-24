/**
 * \file      main.h
 * \author    JP
 * \date      27/11/2012
 * \version   00
 * \brief     header du module main.c
 *
 * \details
 *		- Projet		  : SAFeasy-200
 *		- Compilateur     : c51  V5.02    1995 KEIL ELECTRONIC
 *		- Linling locator : l51  V3.52    1995 KEIL ELECTRONIC
 *		- Formatter       : o51  V2.1     1995 KEIL ELECTRONIC
 */

/* ______ WRAPPEUR _________________________________________	*/
#ifndef WRAPPEUR_MAIN_
#define WRAPPEUR_MAIN_ 1

/* ______ DEFINE-DECLARATIONS ______________________________	*/

/** \brief VERSION BE */
#define VERSION_BE	"BE09 18/09/2015"

/** \brief sélection du type de compilation		*/
/*	0 : CIBLE LOWS						*/
/*	1 : TESTS UNITAIRES					*/
#define TEST_GCC 0   

/** \brief Utilisation ou non de la sonde JTAG */
#define UTILISATION_SONDE_JTAG		0   /* 0: désactivé; 1: activé*/

/**
 * \name Oscillateur Externe à 25 ou 27 MHz ?
 */
/**@{*/
#define TYPE_OSC_25MHZ		25U	/**<l'oscillateur externe est un 25MHz*/
#define TYPE_OSC_27MHZ		27U	/**<l'oscillateur externe est un 27MHz*/
#define TYPE_OSC_EXTERNE	TYPE_OSC_27MHZ
/**@}*/

/**
 * \name Constante symbolique définie pour LDRA Testbed uniquement 
 */
/**@{*/
#define  u8sfrstatic static u8sod
/**@}*/

/* ______ PROTOTYPES _______________________________________	*/

/** \brief Inverse l'etat de la broche d'activation du chien de garde
  *
  * \return void
  *
  * <b>Ressource:</b> - #SFRPAGE (modifie mais restaure en fin de fonction) <br>
  * <b>Ressource:</b> - #sb_AUDIO_WDOG_UC_L est complemente <br>
  */
void relance_chien_de_garde(void);

/** \brief Initialisation des commandes
  *
  * \return void
  *
  */
void Init_Commandes(void);

#endif