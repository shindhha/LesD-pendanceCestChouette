/**
 * \file      init_uc.h
 * \author    JP
 * \date      19/04/2013
 * \version   00
 * \brief     header du module de init_uc.c
 *
 * \details
 *		- Projet		  : SAFeasy-200
 *		- Compilateur     : c51  V5.02    1995 KEIL ELECTRONIC
 *		- Linling locator : l51  V3.52    1995 KEIL ELECTRONIC
 *		- Formatter       : o51  V2.1     1995 KEIL ELECTRONIC
 */

/* ______ WRAPPEUR _________________________________________	*/
#ifndef INIT_UC_
#define INIT_UC_ 1

/* ______ DEFINE-DECLARATIONS ______________________________	*/

/* ______ PROTOTYPES _______________________________________	*/

/** \brief Initialisation du microcontroleur interne
  *
  * \return void
  *
  */
void Init_micro_interne(void);

/** \brief Autorisation de toutes les interruptions du ucontroleur
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register IE <br>
  */
void Autorise_toutes_interruptions(void);

/** \brief Inhibition de l'interruption TIMER4 du microcontroleur
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register EIE2 <br>
  */
void Masque_Interruption_Timer4(void);

/** \brief Autorisation de l'interruption TIMER4 du microcontroleur
  *
  * \return void
  *
  * <b>Ressource :</b> - Special Function Register EIE2 <br>
  */
void Demasque_Interruption_Timer4(void);

#endif
