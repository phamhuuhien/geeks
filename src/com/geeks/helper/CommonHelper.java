package com.geeks.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.geeks.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class CommonHelper {
	/*
     * subString: to get your phone without your country code example:
     * ("12345678","123") -> "45678"
     */
    public static String cutString(String yourFullPhone, String yourCountryCode) {
    	char[] mArrCountryCode = yourCountryCode.toCharArray();
    	char[] mArrFullPhone = yourFullPhone.toCharArray();
    	String countryCodeTemp = yourFullPhone.substring(0,
    			mArrCountryCode.length);
    	if (countryCodeTemp.equals(yourCountryCode)) {
    		String newString = yourFullPhone.substring(mArrCountryCode.length,
    				mArrFullPhone.length);
    		return newString;
    	} else {
    		return yourFullPhone;
    	}
    }
    
    /*
     * To cut all space inside a string
     * example:"Sent SMS testing"->"SentSMStesting"
     */
    public static String cutSpace(String str) {
    	String[] mArrStr = str.split("(?!^) ");
    	String strNew = "";
    	for (String k : mArrStr) {
    		if (k.trim().equals("")) {
    			continue;
    		} else {
    			strNew += k;
    		}
    	}

    	return strNew;
    }
    
    public static void hiddenKeyBoard(View v, InputMethodManager imm) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    
    /*
     * Cut all zero before phone example: "00012345678"->"12345678"
     */
    public static String getStringWithNoZeroBefore(String phone) {
    	String[] mArrStr = phone.split("(?!^)");

    	boolean chk = false;
    	String phoneNew = "";
    	for (String k : mArrStr) {
    		if (k.equals("0")) {
    			if (chk) {
    				phoneNew += k;
    			} else {
    				continue;
    			}
    		} else {
    			phoneNew += k;
    			chk = true;
    		}
    	}

    	return phoneNew;
    }
    
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
        	NetworkInfo[] info = connectivity.getAllNetworkInfo();
        	if (info != null) {
        		for (int i = 0; i < info.length; i++) {
        			if (info[i].getState() == NetworkInfo.State.CONNECTED) {
        				return true;
        			}
        		}
        	}
        }
        return false;
    }
    
    // Check email address validation
    public static boolean eMailValidation(String emailstring) {
    	Pattern emailPattern = Pattern
    			.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
    	Matcher emailMatcher = emailPattern.matcher(emailstring);
    	return emailMatcher.matches();
    }

    // Show Toast
    public static void showToast(Context context, String str_message) {
    	Toast toast = Toast.makeText(context, str_message, Toast.LENGTH_SHORT);
    	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
    	toast.setDuration(500);
    	toast.show();
    }
    
    // Show warning alert
    public static void showWarningDialog(Context context, String str_message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setTitle(context.getString(R.string.app_name))
    		.setMessage(str_message)
    		.setCancelable(false).setPositiveButton("OK", null);
    	builder.create().show();
    }
    
    public static void showWarningDialog(Context context, String title, String str_message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setTitle(title)
    		.setMessage(str_message)
    		.setCancelable(false).setPositiveButton("OK", null);
    	builder.create().show();
    }
    
    public static void popUp(Context context, String title, String message,
    		String nameBtnLeft, String nameBtnRight) {

    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setTitle(title);
    	builder.setInverseBackgroundForced(true);
    	builder.setMessage(message)
                    .setPositiveButton(nameBtnLeft,
                    		new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int id) {
                    		dialog.dismiss();
                    	}
                    })
                    .setNegativeButton(nameBtnRight,
                    		new DialogInterface.OnClickListener() {
                    	public void onClick(DialogInterface dialog, int id) {
                    		dialog.dismiss();
                    	}
                    });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    public static String getCountryName(final int mcc, final int mnc, final String iso) {
    	switch (mcc) {
    	case 213:
    		return "Andorra";
    	case 424:
    		return "United Arab Emirates";
    	case 412:
    		return "Afghanistan";
    	case 344:
    		return "Antigua And Barbuda";
    	case 365:
    		return "Anguilla";
    	case 276:
    		return "Albania";
    	case 283:
    		return "Armenia";
    	case 362:
    		return "Netherlands Antilles";
    	case 631:
    		return "Angola";
    	case 901:
    		return "Antarctica";
    	case 722:
    		return "Argentina";
    	case 544:
    		return "American Samoa";
    	case 232:
    		return "Austria";

    	case 363:
    		return "Aruba";
    	case 400:
    		return "Azerbaijan";
    	case 218:
    		return "Bosnia And Herzegovina";
    	case 342:
    		return "Barbados";
    	case 470:
    		return "Bangladesh";
    	case 206:
    		return "Belgium";
    	case 613:
    		return "Burkina Faso";
    	case 284:
    		return "Bulgaria";
    	case 426:
    		return "Bahrain";
    	case 642:
    		return "Burundi";
    	case 616:
    		return "Benin";
    	case 350:
    		return "Bermuda";
    	case 736:
    		return "Bolivia";
    	case 724:
    		return "Brazil";
    	case 364:
    		return "Bahamas";
    	case 402:
    		return "Bhutan";
    	case 652:
    		return "Botswana";
    	case 257:
    		return "Belarus";
    	case 702:
    		return "Belize";
    	case 302:
    		return "Canada";

    	case 401:
    		return "Kazakstan";
    	case 629: // return "Congo";
    		return "Congo, D.P.R";
    	case 623:
    		return "Central African Republic";
    	case 228:
    		return "Switzerland";
    	case 548:
    		return "Cook Islands";
    	case 730:
    		return "Chile";
    	case 624:
    		return "Cameroon";
    	case 460:
    		return "China";
    	case 732:
    		return "Colombia";
    	case 712:
    		return "Costa Rica";
    	case 368:
    		return "Cuba";
    	case 625:
    		return "Cape Verde";
    	case 280:
    		return "Cyprus";
    	case 230:
    		return "Czech Republic";
    	case 262:
    		return "Germany";
    	case 638:
    		return "Djibouti";
    	case 238:
    		return "Denmark";
    	case 366:
    		return "Dominica";
    	case 370:
    		return "Dominican Republic";
    	case 603:
    		return "Algeria";
    	case 740:
    		return "Ecuador";
    	case 248:
    		return "Estonia";
    	case 602:
    		return "Egypt";
    	case 657:
    		return "Eritrea";
    	case 214:
    		return "Spain";
    	case 636:
    		return "Ethiopia";
    	case 244:
    		return "Finland";
    	case 542:
    		return "Fiji";
    	case 550:
    		return "Micronesia";
    	case 288:
    		return "Faroe Islands";
    	case 208:
    		return "France";
    	case 628:
    		return "Gabon";
    	case 234:
    		return "United Kingdom";
    	case 352:
    		return "Grenada";
    	case 282:
    		return "Georgia";
    	case 620:
    		return "Ghana";
    	case 266:
    		return "Gibraltar";
    	case 290:
    		return "Greenland";
    	case 607:
    		return "Gambia";
    	case 611:
    		return "Guinea";

    	case 627:
    		return "Equatorial Guinea";
    	case 202:
    		return "Greece";
    	case 704:
    		return "Guatemala";
    	case 535:
    		return "Guam";
    	case 632:
    		return "Guinea-bissau";
    	case 738:
    		return "Guyana";
    	case 454:
    		return "Hong Kong";
    	case 708:
    		return "Honduras";
    	case 219:
    		return "Croatia";
    	case 372:
    		return "Haiti";
    	case 216:
    		return "Hungary";
    	case 510:
    		return "Indonesia";
    	case 272:
    		return "Ireland";
    	case 425:
    		return "Israel";
    	case 404:
    		return "India";
    	case 418:
    		return "Iraq";
    	case 432:
    		return "Iran, Islamic Republic Of";
    	case 274:
    		return "Iceland";
    	case 222:
    		return "Italy";
    	case 338:
    		return "Jamaica";
    	case 416:
    		return "Jordan";
    	case 440:
    		return "Japan";
    	case 639:
    		return "Kenya";
    	case 437:
    		return "Kyrgyzstan";
    	case 456:
    		return "Cambodia";
    	case 545:
    		return "Kiribati";

    	case 356:
    		return "Saint Kitts And Nevis";
    	case 450:
    		return "Korea, D.P.R.";
    	case 467:
    		return "Korea, Republic Of";
    	case 419:
    		return "Kuwait";
    	case 346:
    		return "Cayman Islands";
    	case 457:
    		return "Laos";
    	case 415:
    		return "Lebanon";
    	case 358:
    		return "Saint Lucia";
    	case 295:
    		return "Liechtenstein";
    	case 413:
    		return "Sri Lanka";
    	case 618:
    		return "Liberia";
    	case 651:
    		return "Lesotho";
    	case 246:
    		return "Lithuania";
    	case 270:
    		return "Luxembourg";
    	case 247:
    		return "Latvia";
    	case 606:
    		return "Libyan Arab Jamahiriya";
    	case 604:
    		return "Morocco";
    	case 212:
    		return "Monaco";
    	case 259:
    		return "Moldova, Republic Of";
    	case 646:
    		return "Madagascar";
    	case 294:
    		return "Macedonia";
    	case 610:
    		return "Mali";
    	case 414:
    		return "Myanmar";
    	case 428:
    		return "Mongolia";
    	case 455:
    		return "Macau";

    	case 609:
    		return "Mauritania";
    	case 354:
    		return "Montserrat";
    	case 278:
    		return "Malta";
    	case 617:
    		return "Mauritius";
    	case 472:
    		return "Maldives";
    	case 650: 
    		return "Malawi";
    	case 334:
    		return "Mexico";
    	case 502:
    		return "Malaysia";
    	case 643:
    		return "Mozambique";
    	case 649:
    		return "Namibia";
    	case 546:
    		return "New Caledonia";
    	case 614:
    		return "Niger";

    	case 621:
    		return "Nigeria";
    	case 710:
    		return "Nicaragua";
    	case 204:
    		return "Netherlands";
    	case 242:
    		return "Norway";
    	case 429:
    		return "Nepal";
    	case 536:
    		return "Nauru";
    	case 555:
    		return "Niue";
    	case 530:
    		return "New Zealand";
    	case 422:
    		return "Oman";
    	case 714:
    		return "Panama";
    	case 716:
    		return "Peru";
    	case 547:
    		return "French Polynesia";
    	case 537:
    		return "Papua New Guinea";
    	case 515:
    		return "Philippines";
    	case 410:
    		return "Pakistan";
    	case 260:
    		return "Poland";
    	case 308:
    		return "Saint Pierre And Miquelon";
    	case 330:
    		return "Puerto Rico";
    	case 268:
    		return "Portugal";
    	case 552:
    		return "Palau";
    	case 744:
    		return "Paraguay";
    	case 427:
    		return "Qatar";
    	case 647:
    		return "Reunion";
    	case 226:
    		return "Romania";
    	case 250:
    		return "Russian Federation";
    	case 635:
    		return "Rwanda";
    	case 420:
    		return "Saudi Arabia";
    	case 540:
    		return "Solomon Islands";
    	case 633:
    		return "Seychelles";
    	case 634:
    		return "Sudan";
    	case 240:
    		return "Sweden";
    	case 525:
    		return "Singapore";
    	case 293:
    		return "Slovenia";
    	case 231:
    		return "Slovakia";
    	case 619:
    		return "Sierra Leone";
    	case 292:
    		return "San Marino";
    	case 608:
    		return "Senegal";
    	case 637:
    		return "Somalia";
    	case 746:
    		return "Suriname";
    	case 626:
    		return "Sao Tome And Principe";
    	case 706:
    		return "El Salvador";
    	case 653:
    		return "Swaziland";
    	case 376:
    		return "Turks And Caicos Islands";
    	case 622:
    		return "Chad";
    	case 615:
    		return "Togo";
    	case 520:
    		return "Thailand";
    	case 436:
    		return "Tajikistan";
    	case 438:
    		return "Turkmenistan";
    	case 605:
    		return "Tunisia";
    	case 539:
    		return "Tonga";
    	case 514:
			return "East Timor";
    	case 286:
    		return "Turkey";
    	case 374:
    		return "Trinidad And Tobago";
    	case 553:
    		return "Tuvalu";
    	case 466:
    		return "Taiwan";
    	case 640:
    		return "Tanzania, United Republic Of";
    	case 255:
    		return "Ukraine";
    	case 641:
    		return "Uganda";
    	case 311:
    		return "United States";
    	case 313:
    		return "United States";
    	case 316:
    		return "United States";
    	case 748:
    		return "Uruguay";
    	case 434:
    		return "Uzbekistan";
    	case 225:
    		return "Vatican City";
    	case 360:
    		return "St Vincent/Grenadines";
    	case 734:
    		return "Venezuela";
    	case 348:
    		return "Virgin Islands, British";
    	case 310:
    		return "Virgin Islands, U.S.";
    	case 452:
    		return "Vietnam";
    	case 541:
    		return "Vanuatu";
    	case 421:
    		return "Yemen";
    	case 220:
    		return "Yugoslavia";
    	case 655:
    		return "South Africa";
    	case 645:
    		return "Zambia";
    	case 648:
    		return "Zimbabwe";
    	case 505:
    		if (mnc == 10)
    			return "Norfolk Island";
    		else
    			return "Australia";
    	case 340:
    		// return "French Guiana";
    		if (!TextUtils.isEmpty(iso)) {
	    		if (iso.equals("mq")) {
	    			return "Martinique";
	    		} else if (iso.equals("gp")) {
	    			return "Guadeloupe";
	    		} else {
	    			return null;
	    		}
    		} else {
    			if (mnc == 8 || mnc == 3)
    				return "Guadeloupe";
    			else if (mnc == 1 || mnc == 2 || mnc == 20)
    				return "Martinique";
    			else
    				return null;
    		}
    	case 564:
    		if (!TextUtils.isEmpty(iso)) {
    			if (iso.equals("yt")) {
    				return "Mayotte";
    			} else if (iso.equals("km")) {
    				return "Comoros";
    			} else {
    				return "Comoros";
    			}
    		} else {
    			return "Comoros";
    		}
    	case 417:
    		return "Syrian Arab Republic";
    	case 612:
    		return "Cote D'ivoire";
    	case 528:
    		return "Brunei Darussalam";
/*    	case -1:
    		return "Cocos Keeling Islands";
    	case -5:
    		return "Christmas Island";
    	case -6:
    		return "Falkland Islands Malvinas";
    	case -8:
    		return "Marshall Islands";
    	case -9:
    		return "Northern Mariana Islands";
    	case -10:
    		return "Palestine";
    	case -11:
    		return "Saint Helena";
    	case -13:
    		return "Tokelau";
    	case -14:
    		return "Wallis And Futuna";
*/
    	default:
    		return null;
    	}
    }
}
