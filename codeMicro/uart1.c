/**
 * \file      uart1.c
 * \author    MS
 * \date      09/12/2012
 * \version   00
 * \brief     Gestion de l'UART1
 *
 * \details
 *		- Projet		  : 
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
#include "uart1.h"

#include "c8051F12.h"



/* ______ DEFINE-DECLARATIONS ______________________________	*/

/*	STRUCTURE DES FIFOS */
/*	Taille d'un message Tx ou Rx				*/
#define TAILLE_MAX_FIFO_DIALCPU (220U) /**< Taille fifo  UART1*/

/**
 * \struct	S_T_FIFO_RX_DIALCPU
 * \brief	definition de la structure pour la fifo de r�ception de l'UART1
 */
typedef struct T_FIFO_RX_DIALCPU
{
	u8sod u8_status;								/**< taux de remplissage de la fifo	*/
	u8sod u8_ptr_lecture;							/**< indice de lecture				*/
	u8sod u8_ptr_ecriture;							/**< indice d'ecriture				*/
	u8sod u8_donnee[TAILLE_MAX_FIFO_DIALCPU];	/**< tableau de donnees				*/
}S_T_FIFO_RX_DIALCPU;

static S_T_FIFO_RX_DIALCPU s_fifo_dialcpu_rx; /**< Fifo de r�ception UART1*/

/**
 * \brief	definition de la structure pour la fifo d'�mission de l'UART1
 */
typedef struct T_FIFO_TX_DIALCPU
{
	u8sod u8_status;							/**< taux de remplissage de la fifo	*/
	u8sod u8_ptr_lecture;						/**< indice de lecture				*/
	u8sod u8_ptr_ecriture;						/**< indice d'ecriture				*/
	u8sod u8_donnee[TAILLE_MAX_FIFO_DIALCPU];	/**< tableau de donnees				*/
}S_T_FIFO_TX_DIALCPU;
static S_T_FIFO_TX_DIALCPU s_fifo_dialcpu_tx; /**< Fifo d'emission UART1*/



u8sod u8_amorcage_tx_uart1;

/*	R�sultat de traitement							*/
#define FIFO_DIAL_OK 0x69	/**< Traitement OK */
#define FIFO_DIAL_KO 0x96	/**< Traitement KO */

/*_______IV - PROTOTYPES IMPORTES _____________________MODULE______________*/
static void IT_Uart1 (void);

/*_______V - PROCEDURE ____________________________________________________*/

/* Delimitation de la zone d'entete du fichier pour l'analyse LDRA */
/*LDRA_HEADER_END */


/** \brief Renvoi le status de la fifo tx uart1
  *
  * \return s_fifo_dialcpu_tx.u8_status : nombre d'octets dans fifo tx
  *
  * <b>Ressource :</b> - #s_fifo_dialcpu_tx <br>
  *
  */
u8sod u8NbOctetsDansFifoTxDialCPU( void)
{
	return (s_fifo_dialcpu_tx.u8_status);	
} /* u8NbOctetsDansFifoTxDialCPU */

/** \brief Renvoi le status de la fifo rx uart1
  *
  * \return s_fifo_dialcpu_rx.u8_status : nombre d'octets dans fifo rx
  *
  * \author JP
  *
  * <b>Ressource :</b> - #s_fifo_dialcpu_rx <br>
  *
  */
u8sod u8NbOctetsDansFifoRxDialCPU( void)
{
	return (s_fifo_dialcpu_rx.u8_status);	
} /* u8NbOctetsDansFifoRxDialCPU */


/** \brief Empile un octet dans la fifo RX
  *		renvoie #FIFO_DIAL_OK si l'octet a ete empile, #FIFO_DIAL_KO sinon
  *
  * \param[in] u8_octet_rx : donnee a ecrire dans la fifo (0 a 255)
  *
  * \return loc_u8_retour_EDFRX: #FIFO_DIAL_OK ou #FIFO_DIAL_KO
  *
  *
  * <b>Ressource :</b> - #s_fifo_dialcpu_rx <br>
  *
  */
u8sod u8EcritDansFifoRxDialCPU (u8sod u8_octet_rx)
{
	/*	Retour de fonction													*/
	u8sod loc_u8_retour_EDFRX;

	/*	par defaut, Pas d'octet empile										*/
	loc_u8_retour_EDFRX = (u8sod)(FIFO_DIAL_KO);

	/*	A-t-on de la place dans la fifo?									*/
	if (s_fifo_dialcpu_rx.u8_status < (u8sod)(TAILLE_MAX_FIFO_DIALCPU))
	{
		/*	OUI => on y ajoute l'octet a empiler							*/
		s_fifo_dialcpu_rx.u8_donnee[s_fifo_dialcpu_rx.u8_ptr_ecriture] = u8_octet_rx;

		/*	MAJ du pointeur d'ecriture										*/
		s_fifo_dialcpu_rx.u8_ptr_ecriture = (u8sod)(s_fifo_dialcpu_rx.u8_ptr_ecriture + 1U);

		/*	Gestion de la rotation de la fifo								*/
		if (s_fifo_dialcpu_rx.u8_ptr_ecriture >= (u8sod)(TAILLE_MAX_FIFO_DIALCPU))
		{
			s_fifo_dialcpu_rx.u8_ptr_ecriture = 0U;
		}

		/*	MAJ du nombre de donnees dans la fifo							*/
		s_fifo_dialcpu_rx.u8_status = (u8sod)(s_fifo_dialcpu_rx.u8_status + 1U);

		/*	Octet empile!													*/
		loc_u8_retour_EDFRX = (u8sod)(FIFO_DIAL_OK);
	}

	return (loc_u8_retour_EDFRX);
}	/* u8EcritDansFifoRxDialCPU */

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
  * <b>Ressource :</b> - #ES1 sbit <br>
  *
  */
u8sod u8EcritDansFifoTxDialCPU (u8sod u8_octet_tx)
{
	/*	Retour de fonction													*/
	u8sod loc_u8_retour_EDFTX;

	/*	par defaut, Pas d'octet empile										*/
	loc_u8_retour_EDFTX = (u8sod)(FIFO_DIAL_KO);


	/*	A-t-on de la place dans la fifo?									*/
	if (s_fifo_dialcpu_tx.u8_status < (u8sod)(TAILLE_MAX_FIFO_DIALCPU))
	{
		/*------------------DEBUT DE ZONE ININTERRUPTIBLE- -----------------*/
		EIE2 &= ~0x40U; /* on masque l'it Uart 1 */

		/*	OUI => on y ajoute l'octet a empiler							*/
		s_fifo_dialcpu_tx.u8_donnee[s_fifo_dialcpu_tx.u8_ptr_ecriture] = u8_octet_tx;

		/*	MAJ du pointeur d'ecriture										*/
		s_fifo_dialcpu_tx.u8_ptr_ecriture = (u8sod)(s_fifo_dialcpu_tx.u8_ptr_ecriture + 1U);

		/*	Gestion de la rotation de la fifo								*/
		if (s_fifo_dialcpu_tx.u8_ptr_ecriture >= (u8sod)(TAILLE_MAX_FIFO_DIALCPU))
		{
			s_fifo_dialcpu_tx.u8_ptr_ecriture = 0U;
		}

		/*	MAJ du nombre de donnees dans la fifo							*/
		s_fifo_dialcpu_tx.u8_status = (u8sod)(s_fifo_dialcpu_tx.u8_status + 1U);

		/*	Octet empile!													*/
		loc_u8_retour_EDFTX = (u8sod)(FIFO_DIAL_OK);

		/* it autorise */
		EIE2 |= 0x40;
		/*-------------------FIN  DE ZONE ININTERRUPTIBLES -----------------*/
	}

	return (loc_u8_retour_EDFTX);
}	/* u8EcritDansFifoTxDialCPU */


/** \brief Depile un octet de la fifo Tx
  *		renvoie 0 s'il n'y a pas d'octets dans la fifo
  *
  * \return loc_u8_retour_DUODFTX: un octet (0 a 255)
  *
  * <b>Ressource :</b> - #s_fifo_dialcpu_tx <br>
  *
  */
u8sod u8DepileUnOctetDeFifoTxDialCPU(void)
{
	/*	Retour de fonction												   */
	u8sod loc_u8_retour_DUODFTX;

	/*	A-t-on un octet a depiler?										   */
	if(s_fifo_dialcpu_tx.u8_status != 0U)
	{
		/*	OUI => on depile!											   */

		/*	Lecture de la valeur a depiler								   */
		loc_u8_retour_DUODFTX = s_fifo_dialcpu_tx.u8_donnee[s_fifo_dialcpu_tx.u8_ptr_lecture];

		/*	MAJ du pointeur de lecture									   */
		s_fifo_dialcpu_tx.u8_ptr_lecture = (u8sod)(s_fifo_dialcpu_tx.u8_ptr_lecture + 1U);

		/*	Gestion de la rotation de la fifo							   */
		if (s_fifo_dialcpu_tx.u8_ptr_lecture >= (u8sod)(TAILLE_MAX_FIFO_DIALCPU))
		{
			s_fifo_dialcpu_tx.u8_ptr_lecture = 0U;
		}

		/*	MAJ du nombre d'octets de la fifo							   */
		s_fifo_dialcpu_tx.u8_status = (u8sod)(s_fifo_dialcpu_tx.u8_status - 1U);

	}
	else
	{
		/*	NON => on renvoie la valeur 0								   */
		loc_u8_retour_DUODFTX = 0U;
	}

	return (loc_u8_retour_DUODFTX);

}	/* u8DepileUnOctetDeFifoTxDialCPU */


/** \brief Depile un octet de la fifo des octets recus
  *		renvoie 0 s'il n'y a pas d'octets dans la fifo
  *
  * \return loc_u8_retour: un octet (0 a 255)
  *
  * <b>Ressource :</b> - #s_fifo_dialcpu_rx <br>
  *
  */
u8sod u8DepileUnOctetDeFifoRxDialCPU(void)
{
	/*	Retour de fonction													*/
	u8sod loc_u8_retour;

	/*	A-t-on un octet a depiler?											*/
	if(s_fifo_dialcpu_rx.u8_status != 0U)
	{
		/*	OUI => on depile!												*/
		/*------------------DEBUT DE ZONE ININTERRUPTIBLES -----------------*/
		EIE2 &= ~0x40U; /* on masque l'it Uart 0 */

		/*	Lecture de la valeur a depiler									*/
		loc_u8_retour = s_fifo_dialcpu_rx.u8_donnee[s_fifo_dialcpu_rx.u8_ptr_lecture];

		/*	MAJ du pointeur de lecture										*/
		s_fifo_dialcpu_rx.u8_ptr_lecture = (u8sod)(s_fifo_dialcpu_rx.u8_ptr_lecture + 1U);

		/*	Gestion de la rotation de la fifo								*/
		if (s_fifo_dialcpu_rx.u8_ptr_lecture >= (u8sod)(TAILLE_MAX_FIFO_DIALCPU))
		{
			s_fifo_dialcpu_rx.u8_ptr_lecture = 0U;
		}

		/*	MAJ du nombre d'octets de la fifo								*/
		s_fifo_dialcpu_rx.u8_status = (u8sod)(s_fifo_dialcpu_rx.u8_status - 1U);

		/* it autorise */
		EIE2 |= 0x40U;
		/*--------------------FIN DE ZONE ININTERRUPTIBLES -----------------*/
	}
	else
	{
		/*	NON => on renvoie la valeur 0									*/
		loc_u8_retour = 0U;
	}

	return (loc_u8_retour);

}	/* u8DepileUnOctetDeFifoRxDialCPU */


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
u8sod u8SommetDeFifoRxDialCPU(u8sod u8_offset)
{
	/*	Retour de fonction													*/
	u8sod loc_u8_retour_DUODFRX;
	u16sod loc_u16_ptr_lec;

	/*	A-t-on un octet a lire ?											*/
	if(s_fifo_dialcpu_rx.u8_status >= u8_offset)
	{
		/*	OUI => on calcul l'adresse de lecture							*/
		loc_u16_ptr_lec = (u16sod)(s_fifo_dialcpu_rx.u8_ptr_lecture) + (u16sod)(u8_offset);
		/* on gere la rotation */
		if (loc_u16_ptr_lec >= (u16sod)(TAILLE_MAX_FIFO_DIALCPU))
		{
			loc_u16_ptr_lec -= (u16sod)(TAILLE_MAX_FIFO_DIALCPU);
		}
		/* on lit l'octet */
		loc_u8_retour_DUODFRX = s_fifo_dialcpu_rx.u8_donnee[(u8sod)(loc_u16_ptr_lec)];
	}
	else
	{
		/*	NON => on renvoie la valeur 0									*/
		loc_u8_retour_DUODFRX = 0U;
	}

	return (loc_u8_retour_DUODFRX);
} /* u8SommetDeFifoRxDialCPU */


/** \brief Initialise les Fifos Uart1
  *
  * \return void
  *
  *
  */
void Init_Uart1(void)
{
	/*	Compteur de boucle																*/
	u8sod loc_u8_cpt;


	/*	Amor�age de l'interruption TX de l'UART1							*/
	u8_amorcage_tx_uart1 = EMISSION_UART1_TERMINEE; /* INIT */ 
	Ecriture_Statut_Emission_Uart1((u8sod)(EMISSION_UART1_TERMINEE));

	/*	RAZ pointeur d'ecriture														*/
	s_fifo_dialcpu_rx.u8_ptr_ecriture = 0U;
	s_fifo_dialcpu_tx.u8_ptr_ecriture = 0U;

	/*	RAZ pointeur de lecture														*/
	s_fifo_dialcpu_rx.u8_ptr_lecture = 0U;
	s_fifo_dialcpu_tx.u8_ptr_lecture = 0U;

	/*	RAZ taux de remplissage de la fifo											*/
	s_fifo_dialcpu_rx.u8_status = 0U;
	s_fifo_dialcpu_tx.u8_status = 0U;

	/*	RAZ des donnees	dans la fifo												*/
	for(loc_u8_cpt=0U ;loc_u8_cpt<(u8sod)(TAILLE_MAX_FIFO_DIALCPU);loc_u8_cpt++)
	{
		s_fifo_dialcpu_rx.u8_donnee[(u8sod)(loc_u8_cpt)]=0U;
		s_fifo_dialcpu_tx.u8_donnee[(u8sod)(loc_u8_cpt)]=0U;
	}
}/* Init_Uart1 */

/** \brief Ecriture statut emission de UART1
  *
  * \param[in] u8_statut_tx_uart1 : #EMISSION_UART1_EN_COURS ou
  * #EMISSION_UART1_TERMINEE
  *
  * \return void
  *
  * \details Si positionnement de l'entree a :
  *				- #EMISSION_UART1_TERMINEE : il n'y a
  *				plus de donnees a emettre vers l'UART1
  *				- #EMISSION_UART1_EN_COURS : il a des
  *				donnees a emettre vers l'UART1
  *
  * <b>Ressource :</b> - #u8_amorcage_tx_uart1 <br>
  * <b>Ressource :</b> - #IE Special Function Register <br>
  * <b>Ressource :</b> - #SFRPAGE Special Function Register <br>
  * <b>Ressource :</b> - #ES1 sbit <br>
  *
  */
void Ecriture_Statut_Emission_Uart1(u8sod u8_statut_tx_uart1)
{
	u8sod loc_u8_sfrpage;
	u8sod loc_u8_eie2;

	/* si l'interruption est autorisee, il faut la masquer */
	loc_u8_eie2 = EIE2;
	EIE2 &= ~0x40U;

	/*	Test de la validite des donnees en entree de la fonction			*/
	if (((u8sod)(EMISSION_UART1_EN_COURS) == u8_statut_tx_uart1)||
		  ((u8sod)(EMISSION_UART1_TERMINEE) == u8_statut_tx_uart1))
	{
		/*	Amor�age de l'interruption TX de l'UART	(si pas d�j� amorc�e)	*/
		if(EMISSION_UART1_TERMINEE == u8_amorcage_tx_uart1)
		{
			if ((u8sod)(EMISSION_UART1_EN_COURS) == u8_statut_tx_uart1)
			{
				/* dans le cas du passage en emission, il faut autoriser l'interruption */
				loc_u8_sfrpage = SFRPAGE;
				SFRPAGE = UART1_PAGE;
				TI1=1;
				SFRPAGE = loc_u8_sfrpage;
			}
		}
		
		/*	MAJ de l'amor�age de l'interruption TX de
		 *	l'UART1				*/
		u8_amorcage_tx_uart1 = u8_statut_tx_uart1;
	}

	/* on remet le registre EIE2 */
	EIE2 = loc_u8_eie2;

}/* Ecriture_Statut_Emission_Uart1 */

/** \brief Autorise les interruptions de l'uart1
  *
  * \return void
  *
  * <b>Ressource :</b> - #ES1 Special Function Register <br>
  *
  */
void Autorise_Interruption_Uart1(void)
{
	/* on autorise l'it Uart1 */
	EIE2 |= 0x40U;
}

/** \brief ROUTINE D'INTERRUPTION (IT UART1)
  *
  * \return void
  *
  * <b>SOURCE      :</b> IT_Uart1 <br>
  * <b>FREQUENCE   :</b> xxxx bauds <br>
  * <b>PRIORITE    :</b> 0 <br>
  *
  * <b>Ressource :</b> - #SFRPAGE Special Function Register <br>
  * <b>Ressource :</b> - #RI1 et #TI1 Special Function Register <br>
  * <b>Ressource :</b> - #SBUF0 Special Function Register <br>
  */

/*	COMPILATIONS SUR CIBLE LOWS				*/
static void IT_Uart1 (void) interrupt 20
{
	u8sod 	loc_u8_sfrpage;

	/*	M�morisation de l'index courant du tableau des registres sp�ciaux du		*/
	/*	microcontroleur																*/
	loc_u8_sfrpage = SFRPAGE;

	/*	Acc�s aux registres de configuration de l'UART1								*/
	SFRPAGE = UART1_PAGE;

	/*	***************************************************************************	*/
	/*	Interruption g�n�r�e par la r�ception d'un octet							*/
	/*	***************************************************************************	*/
	if (1U==RI1)
	{
		/*	Interruption acquit�e													*/ 
		RI1 = 0U;

		/*	Memorisation de l'octet receptionne dans une fifo						*/
		u8EcritDansFifoRxDialCPU((u8sod)(SBUF0));
	}

	/*	***************************************************************************	*/
	/*	Interruption g�n�r�e pour l'�mission d'un octet								*/
	/*	***************************************************************************	*/
	if (1U==TI1)
	{
		/*	Interruption acquit�e												*/ 
		TI1=0U;

		/*	Est-ce qu'il y reste des octets � �mettre?								*/
		if (u8NbOctetsDansFifoTxDialCPU() != 0U)
		{
			/*	OUI	=> on �met														*/
			SBUF0 = u8DepileUnOctetDeFifoTxDialCPU();
		}
		else
		{
			/*	NON => Emission termin�e!											*/
			Ecriture_Statut_Emission_Uart1((u8sod)(EMISSION_UART1_TERMINEE));
		}
	}

	/*	Repositionnement de l'index courant du tableau des registres sp�ciaux		*/
	/*	du microcontroleur � sa valeur initiale										*/
	SFRPAGE = loc_u8_sfrpage;

}/* IT_Uart1 */

