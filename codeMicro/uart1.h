/**
 * \file      uart1.h
 * \author    MS
 * \date      12/12/2011
 * \version   00
 * \brief     header du module uart1.c
 *
 * \details
 *		- Projet		  : 
 *		- Compilateur     : c51  V5.02    1995 KEIL ELECTRONIC
 *		- Linling locator : l51  V3.52    1995 KEIL ELECTRONIC
 *		- Formatter       : o51  V2.1     1995 KEIL ELECTRONIC
 */


/* ______ WRAPPEUR _________________________________________	*/
#ifndef UART1
#define UART1 1

/* ______ DEFINE-DECLARATIONS ______________________________	*/
#define EMISSION_UART1_EN_COURS 0x69 /**< Emission en cours UART1*/
#define EMISSION_UART1_TERMINEE 0x96 /**< Emission terminée UART1*/



/* ______ PROTOTYPES _______________________________________	*/

/** \brief Initialise les Fifos Uart1
  *
  * \return void
  *
  *
  */
void Init_Uart1(void);

/** \brief Ecriture statut emission de UART1
  *
  * \param[in] u8_statut_tx_uart1 : #EMISSION_UART1_EN_COURS ou #EMISSION_UART1_TERMINEE
  *
  * \return void
  *
  * \details Si positionnement de l'entree a :
  *				- #EMISSION_UART1_TERMINEE : il n'y a plus de donnees a emettre vers l'UART1
  *				- #EMISSION_UART1_EN_COURS : il a des donnees a emettre vers l'UART1
  *
  * <b>Ressource :</b> - #u8_amorcage_tx_uart1 <br>
  * <b>Ressource :</b> - #IE Special Function Register <br>
  * <b>Ressource :</b> - #SFRPAGE Special Function Register <br>
  * <b>Ressource :</b> - #ES0 sbit <br>
  *
  */
void Ecriture_Statut_Emission_Uart1(u8sod u8_statut_tx_uart1);

/** \brief Autorise les interruptions de l'uart1
  *
  * \return void
  *
  * <b>Ressource :</b> - #ES0 Special Function Register <br>
  *
  */
void Autorise_Interruption_Uart1(void);

/** \brief Empile un octet dans la fifo TX
  *		renvoie #FIFO_DIAL_OK si l'octet a ete empile, #FIFO_DIAL_KO sinon
  *
  * \param[in] u8_octet_tx : donnee a ecrire dans la fifo (0 a 255)
  *
  * \return loc_u8_retour_EDFTX: #FIFO_DIAL_OK ou #FIFO_DIAL_KO
  *
  * \author SR
  *
  * <b>Ressource :</b> - #s_fifo_dialcpu_tx <br>
  * <b>Ressource :</b> - #ES0 sbit <br>
  *
  */
u8sod u8EcritDansFifoTxDialCPU (u8sod u8_octet_tx);

/** \brief Calcul d'un CRC-16 base sur le polynome X16+X15+X2+1.<br>
  *
  * \param[in] u16_crc16 : la valeur du CRC-16 en cours de calcul
  * \param[in] u8_donnee : la nouvelle donnee a ajouter au calcul de crc

  * \return la nouvelle valeur du crc-16
  *
  */
u8sod u8NbOctetsDansFifoRxDialCPU( void);

/** \brief Renvoie le sommet de la fifo rx (avec l'offset passe)
  *		mais sans depiler. renvoie 0 s'il n'y a pas d'octets dans la fifo
  *
  * \param[in] u8_offset: numero de l'octet que l'on souhaite recuperer (0 a #TAILLE_MAX_FIFO_DIALCPU-1)
  *
  * \return loc_u8_retour_DUODFRX: un octet (0 a 255)
  *
  *
  * <b>Ressource :</b> - #s_fifo_dialcpu_rx <br>
  *
  */
u8sod u8SommetDeFifoRxDialCPU(u8sod u8_offset);

/** \brief Depile un octet de la fifo des octets recus
  *		renvoie 0 s'il n'y a pas d'octets dans la fifo
  *
  * \return loc_u8_retour: un octet (0 a 255)
  *
  * <b>Ressource :</b> - #s_fifo_dialcpu_rx <br>
  *
  */
u8sod u8DepileUnOctetDeFifoRxDialCPU(void);

#endif
