// Mozilla User Preferences
// based on: https://support.mozilla.org/en-US/questions/1197798
//           https://kb.mozillazine.org/User.js_file
//           https://github.com/arkenfox/user.js/blob/master/user.js
//
// This should be placed in the Profile folder, which can be located
// through about:support
//
// History:
//
// 10/18/2023   updates based on arkenfox v118
// 11/28/2023   updates based on arkenfox v119
// 02/14/2024   updates based on arkenfox v122
// 08/22/2024   disable privacy preserving ad attribution
// 08/26/2024   updates based on arkenfox v126, 128
//
// Quick and dirty validation:
//
// $ awk '/^\/\// { next; } /^user_pref\(\".*\)\;$/ { next; } (NF > 0) { print; }' User.js

// don't warn when using about:config

user_pref("browser.aboutConfig.showWarning", false);

// always start in private browsing mode

user_pref("browser.privatebrowsing.autostart", true);

// Stop automatic connections
// From: https://support.mozilla.org/en-US/kb/how-stop-firefox-making-automatic-connections?redirectslug=Firefox+makes+unrequested+connections&redirectlocale=en-US

// disable prefetching

user_pref("network.dns.disablePrefetch", true);
user_pref("network.dns.disablePrefetchFromHTTPS", true);
user_pref("network.predictor.enabled", false);
user_pref("network.predictor.enable-prefetch", false);
user_pref("network.prefetch-next", false);
user_pref("network.websocket.enabled", false);
user_pref("media.peerconnection.enabled", false);
user_pref("network.http.speculative-parallel-limit", 0);
user_pref("browser.places.speculativeConnect.enabled", false);

// disable contextual features and notifications

user_pref("browser.newtabpage.activity-stream.feeds.asrouterfeed", false);
user_pref("browser.newtabpage.activity-stream.asrouter.userprefs.cfr.addons", false);
user_pref("browser.newtabpage.activity-stream.asrouter.userprefs.cfr.features", false);
user_pref("browser.newtabpage.activity-stream.feeds.topsites", false);
user_pref("browser.newtabpage.activity-stream.improvesearch.topSiteSearchShortcuts.havePinned", "");
user_pref("browser.newtabpage.activity-stream.showSearch", false);

// disable experiments and studies

user_pref("messaging-system.rsexperimentloader.enabled", false);
user_pref("app.normandy.optoutstudies.enabled", false);
user_pref("app.normandy.enable", false);
user_pref("app.normandy.enabled", false);
user_pref("app.normandy.first_run", false);
user_pref("app.shield.optoutstudies.enabled", false);
user_pref("app.normandy.api_url", "");

// disable geolocation
// From: https://securityspread.com/2013/08/13/configuring-browser-privacy-security-firefox/

user_pref("browser.search.geoip.url", "");
user_pref("geo.enabled", false);
user_pref("geo.wifi.uri", "http://127.0.0.1");
user_pref("geo.provider.network.url", "");
user_pref("geo.provider.network.logging.enabled", "");
user_pref("geo.provider.ms-windows-location", false);
user_pref("geo.provider.use_corelocation", false);
user_pref("geo.provider.use_gpsd", false);
user_pref("geo.provider.use_geoclue", false);

// disable "What's New" page

user_pref("browser.startup.homepage_override.mstone", "ignore");

// disable add-on metadata updates / recommendations

user_pref("extensions.getAddons.showPane", false);
user_pref("extensions.getAddons.cache.enabled", false);
user_pref("extensions.htmlaboutaddons.recommendations.enabled", false);
user_pref("browser.discovery.enabled", false);
user_pref("browser.messaging-system.whatsNewPanel.enabled", false);

// disable penH264 codec

user_pref("media.gmp-gmpopenh264.enabled", false);

// enable extended validation certification info and identify
// insecure connections
// From: https://www.ghacks.net/2019/10/28/how-to-restore-the-green-lock-icon-in-firefoxs-address-bar/

user_pref("security.identityblock.show_extended_validation", true);
user_pref("security.insecure_connection_text.enabled", true);
user_pref("security.insecure_connection_text.pbmode.enabled", true);
user_pref("security.ssl.treat_unsafe_negotiation_as_broken", true);
user_pref("browser.xul.error_pages.expert_bad_cert", true);

// disable javascript pdf viewer
// https://support.mozilla.org/en-US/questions/950988

user_pref("pdfjs.disabled", true);

// prevent javascript from interfering with cut and paste
// From: https://utcc.utoronto.ca/~cks/space/blog/web/FirefoxClipboardeventsIssue

user_pref("pdfjs.enableScripting", false);

// disable push services
// From: https://support.mozilla.org/en-US/questions/126151
//       https://support.mozilla.org/en-US/questions/1139372

user_pref("dom.push.enabled", false);
user_pref("dom.push.serverURL", "");
user_pref("dom.push.connection.enabled", false);
user_pref("dom.webnotifications.enabled", false);
user_pref("dom.webnotifications.serviceworker.enabled", false);

// show IDN punycode for additional phishing protection
// From: https://ma.ttias.be/show-idn-punycode-firefox-avoid-phishing-urls/

user_pref("network.IDN_show_punycode", true);

// secure fonts
// From: https://www.ghacks.net/2022/09/17/how-to-block-web-fonts-to-improve-privacy/

user_pref("gfx.downloadable_fonts.woff2.enabled", false);
user_pref("gfx.downloadable_fonts.fallback_delay", -1);
user_pref("gfx.font_rendering.opentype_svg.enabled", false);

// disabled because icons sometimes fail to work
// https://support.mozilla.org/en-US/questions/1289999
// user_pref("gfx.downloadable_fonts.enabled", false);

// disabled by arkenfox v118

// user_pref("layout.css.font-visibility.private", 1);
// user_pref("layout.css.font-visibility.standard", 1);
// user_pref("layout.css.font-visibility.trackingprotection", 1);

// enable query stripping
// From: https://www.ghacks.net/2022/06/29/firefox-remove-known-tracking-parameters-from-urls-in-all-modes/

user_pref("privacy.query_stripping.enabled", true);
user_pref("privacy.query_stripping.enabled.pbmode", true);

// more privacy settings
// From: https://privacyinternational.org/guide-step/4330/firefox-adjusting-settings-enhance-your-online-privacy

user_pref("media.navigator.enabled", false);
user_pref("privacy.firstparty.isolate", true);
user_pref("privacy.fingerprintingProtection.remoteOverrides.enabled", false);
user_pref("privacy.resistFingerprinting", true);
user_pref("privacy.resistFingerprinting.pbmode", true);
user_pref("privacy.resistFingerprinting.block_mozAddonManager", true);
user_pref("privacy.resistFingerprinting.letterboxing", true);
user_pref("privacy.trackingprotection.enabled", true);
user_pref("privacy.trackingprotection.emailtracking.enabled", true);
user_pref("privacy.trackingprotection.socialtracking.enabled", true);
user_pref("privacy.trackingprotection.fingerprinting.enabled", true);
user_pref("privacy.trackingprotection.cryptomining.enabled", true);
user_pref("privacy.userContext.enabled", true);
user_pref("browser.link.open_newwindow.restriction", 0);

// disable click tracking

user_pref("browser.send_pings", false);
user_pref("browser.send_pings.require_same_host", true);

// disable pocket

user_pref("extensions.pocket.enabled", false);

// disable battery information sharing

user_pref("dom.battery.enabled", false);

// disable cut, copy, paste notifications

user_pref("dom.event.clipboardevents.enabled", false);

// disable beacon

user_pref("beacon.enabled", false);

// disable snippets

user_pref("browser.aboutHomeSnippets.updateUrl", "");

// disable webgl

user_pref("webgl.disabled", true);

// disable crash reports

user_pref("breakpad.reportURL", "");
user_pref("browser.tabs.crashReporting.sendReport", false);
user_pref("browser.crashReports.unsubmittedCheck.enabled", false);
user_pref("browser.crashReports.unsubmittedCheck.autoSubmit2", false);
user_pref("extensions.webcompat-reporter.enabled", false);

// prevent accident quits
// From: https://lifehacker.com/5819311/how-to-prevent-yourself-from-accidentally-quitting-firefox-or-chrome

user_pref("browser.tabs.warnOnClose", true);

// disable auto updating

user_pref("app.update.auto", false);

// disable blinking and scrolling text

user_pref("browser.blink_allowed", false);
user_pref("browser.display.enable_marquee", false);

// search

user_pref("browser.search.widget.inNavBar", true);
user_pref("keyword.enabled", false);
user_pref("browser.urlbar.showSearchTerms.enabled", false);

// always start with a blank page and clear topsites

user_pref("browser.startup.homepage", "about:blank");
user_pref("browser.startup.homepage", "chrome://browser/content/blanktab.html");
user_pref("browser.newtabpage.enabled", false);
user_pref("browser.newtabpage.activity-stream.default.sites", "");

// disable sponsored content

user_pref("browser.newtabpage.activity-stream.showSponsored", false);
user_pref("browser.newtabpage.activity-stream.showSponsoredTopSites", false);

// disable suggestions

user_pref("browser.search.suggest.enabled", false);
user_pref("browser.urlbar.clipboard.featureGate", false);
user_pref("browser.urlbar.quicksuggest.enabled", false);
user_pref("browser.urlbar.speculativeConnect.enabled", false);
user_pref("browser.urlbar.suggest.engines", false);
user_pref("browser.urlbar.suggest.quicksuggest.nonsponsored", false);
user_pref("browser.urlbar.suggest.quicksuggest.sponsored", false);
user_pref("browser.urlbar.suggest.searches", false);
user_pref("browser.urlbar.suggest.topsites", false);
user_pref("browser.urlbar.maxRichResults", 0);

// disabled in arkenfox v118

// user_pref("browser.fixup.alternate.enabled", "");
// user_pref("browser.urlbar.dnsResolveSingleWordsAfterSearch", "");

// disable telemetry

user_pref("datareporting.healthreport.uploadEnabled", false);
user_pref("datareporting.policy.dataSubmissionEnabled", false);
user_pref("toolkit.telemetry.unified", false);
user_pref("toolkit.telemetry.enabled", false);
user_pref("toolkit.telemetry.server", "data:,");
user_pref("toolkit.telemetry.archive.enabled", false);
user_pref("toolkit.telemetry.newProfilePing.enabled", false);
user_pref("toolkit.telemetry.shutdownPingSender.enabled", false);
user_pref("toolkit.telemetry.updatePing.enabled", false);
user_pref("toolkit.telemetry.bhrPing.enabled", false);
user_pref("toolkit.telemetry.firstShutdownPing.enabled", false);
user_pref("toolkit.telemetry.coverage.opt-out", true);
user_pref("toolkit.coverage.opt-out", true);
user_pref("toolkit.coverage.endpoint.base", "");
user_pref("browser.ping-centre.telemetry", false);
user_pref("browser.newtabpage.activity-stream.feeds.telemetry", false);
user_pref("browser.newtabpage.activity-stream.telemetry", false);

// disable form filling

user_pref("browser.formfill.enable", false);
user_pref("signon.rememberSignons", false);
user_pref("signon.autofillForms", false);
user_pref("signon.formlessCapture.enabled", false);
user_pref("extensions.formautofill.addresses.enabled", false);
user_pref("extensions.formautofill.creditCards.enabled", false);
user_pref("browser.urlbar.autoFill", false);

// disable autoplay

user_pref("media.autoplay.default", 5);

// disable camera access, notifications, location services,
// microphone access

user_pref("permissions.default.camera", 2);
user_pref("permissions.default.desktop-notification", 2);
user_pref("permissions.default.geo", 2);
user_pref("permissions.default.microphone", 2);
user_pref("permissions.default.xr", 2);

// disable history

user_pref("places.history.enabled", false);

// clear everything on shutdown
// From: https://support.mozilla.org/en-US/questions/1275887
//       https://github.com/arkenfox/user.js/issues/1846

user_pref("privacy.sanitize.sanitizeOnShutdown", true);

// disabled by arkenfox v135
// https://github.com/arkenfox/user.js/issues/1941
//user_pref("privacy.clearOnShutdown.cache", true);
//user_pref("privacy.clearOnShutdown.cookies", true);
//user_pref("privacy.clearOnShutdown.downloads", true);
//user_pref("privacy.clearOnShutdown.formdata", true);
//user_pref("privacy.clearOnShutdown.history", true);
//user_pref("privacy.clearOnShutdown.offlineApps", true);
//user_pref("privacy.clearOnShutdown.sessions", true);

// new from arkenfox user.js v135
user_pref("privacy.clearHistory.browsingHistoryAndDownloads", true);
user_pref("privacy.clearHistory.formdata", true);
user_pref("privacy.clearOnShutdown_v2.browsingHistoryAndDownloads", true);
user_pref("privacy.clearOnShutdown_v2.downloads", true);
user_pref("privacy.clearOnShutdown_v2.formdata", true);
user_pref("privacy.clearSiteData.browsingHistoryAndDownloads", true);
user_pref("privacy.clearSiteData.formdata", true);

user_pref("privacy.clearOnShutdown.openWindows", true);
user_pref("privacy.clearOnShutdown.siteSettings", true);
user_pref("privacy.clearOnShutdown_v2.cookiesAndStorage", true);
user_pref("privacy.clearOnShutdown_v2.cache", true);
user_pref("privacy.clearOnShutdown_v2.historyFormDataAndDownloads", true);
user_pref("privacy.clearOnShutdown_v2.siteSettings", false);
user_pref("privacy.clearOnShutdown_v2.cookiesAndStorage", true);
user_pref("privacy.clearHistory.cache", true);
user_pref("privacy.clearHistory.historyFormDataAndDownloads", true);
user_pref("privacy.clearHistory.cookiesAndStorage", false);
user_pref("privacy.clearHistory.siteSettings", false);
user_pref("privacy.clearSiteData.cache", true);
user_pref("privacy.clearSiteData.cookiesAndStorage", false);
user_pref("privacy.clearSiteData.historyFormDataAndDownloads", true);
user_pref("privacy.clearSiteData.siteSettings", false);
user_pref("browser.helperApps.deleteTempFileOnExit", true);

// do not remember signons

user_pref("signon.rememberSignons", false);

// don't allow cross-origin HTTP authentication dialogs

user_pref("network.auth.subresource-http-auth-allow", 1);

// disable unnecessary disk usage

user_pref("browser.chrome.site_icons", false);
user_pref("browser.cache.disk.enable", false);
user_pref("browser.privatebrowsing.forceMediaMemoryCache", true);
user_pref("browser.sessionstore.privacy_level", 2);
user_pref("browser.sessionstore.max_tabs_undo", 0);
user_pref("browser.sessionstore.resume_from_crash", false);
user_pref("browser.shell.shortcutFavicons", false);
user_pref("toolkit.winRegisterApplicationRestart", false);
user_pref("permissions.memory_only", true);
user_pref("security.nocertdb", true);
user_pref("media.memory_cache_max_size", 65536);

// SSL / TLS

user_pref("security.ssl.require_safe_negotiation", true);
user_pref("security.tls.enable_0rtt_data", false);
user_pref("security.tls.version.enable-deprecated", false);

// enable OCSP and only proceed if a valid cert if found

user_pref("security.OCSP.enabled", 1);
user_pref("security.OCSP.require", true);

// Certificates - enforce pinning, enable CRLite, and enforce
// Revoked and Not Revoked results

user_pref("security.cert_pinning.enforcement_level", 2);
user_pref("security.remote_settings.crlite_filters.enabled", true);
user_pref("security.pki.crlite_mode", 2);

// enable HTTPS only mode

user_pref("security.mixed_content.block_display_content", true);
user_pref("dom.security.https_only_mode", true);
user_pref("dom.security.https_only_mode_pbm", true);
user_pref("dom.security.https_only_mode_send_http_background_request", false);

// containers

user_pref("privacy.userContext.enabled", true);
user_pref("privacy.userContext.ui.enabled", true);

// disable window resizing

user_pref("dom.disable_window_move_resize", true);

// disable keyboard shortcut overrides

user_pref("permissions.default.shortcuts", 2);

// disable the UI tour

user_pref("browser.uitour.enabled", false);

// disable remote debugging

user_pref("devtools.debugger.remote-enabled", false);

// disable special permissions for mozilla domains

user_pref("permissions.manager.defaultsUrl", "");
user_pref("webchannel.allowObject.urlWhitelist", "");

// disabled by arkenfox v118

// disable permission delegation
// user_pref("permissions.delegation.enabled", false);

// downloads security / usability

user_pref("browser.download.alwaysOpenPanel", false);
user_pref("browser.download.manager.addToRecentDocs", false);
user_pref("browser.download.always_ask_before_handling_new_types", true);

// enhanced tracking protection

user_pref("browser.contentblocking.category", "strict");
user_pref("privacy.antitracking.enableWebcompat", false);

// disabled by arkenfox v118

// user_pref("privacy.partition.serviceWorkers", true);
// user_pref("privacy.partition.always_partition_third_party_non_cookie_storage", true);
// user_pref("privacy.partition.always_partition_third_party_non_cookie_storage.exempt_sessionstorage", false);

// disable asm.js, webassembly, Ion and baseline JIT

user_pref("javascript.options.asmjs", false);
user_pref("javascript.options.wasm", false);
user_pref("javascript.options.ion", false);
user_pref("javascript.options.baselinejit", false);
user_pref("javascript.options.jit_trustedprincipals", true);

// enforce Firefox blocklist

user_pref("extensions.blocklist.enabled", true);
user_pref("extensions.quarantinedDomains.enabled", true);

// enforce no referrer spoofing

user_pref("network.http.referer.spoofSource", false);

// other privacy settings

user_pref("identity.fxaccounts.enabled", false);
user_pref("identity.fxaccounts.toolbar.enabled", false);
user_pref("network.http.referer.disallowCrossSiteRelaxingDefault.top_navigation", true);
user_pref("dom.popup_allowed_events", "click dblclick mousedown pointerdown");
user_pref("browser.pagethumbnails.capturing_disabled", true);

// force pop-up windows to open in a new tab
// from: https://codeyarns.com/tech/2023-10-13-firefox-configuration-editor.html

user_pref("browser.link.open_newwindow.restriction", 0);

// disable autocomplete popup
// from: https://www-archive.mozilla.org/unix/customizing.html

user_pref("browser.urlbar.autocomplete.enabled", false);
user_pref("browser.urlbar.showPopup", false);
user_pref("browser.urlbar.showSearch", false);

// added in arkenfox v118

user_pref("browser.download.start_downloads_in_tmp_dir", true);
user_pref("browser.shopping.experience2023.enabled", false);
user_pref("browser.urlbar.addons.featureGate", false);
user_pref("browser.urlbar.mdn.featureGate", false);
user_pref("browser.urlbar.pocket.featureGate", false);
user_pref("browser.urlbar.trending.featureGate", false);
user_pref("browser.urlbar.weather.featureGate", false);

// added in arkenfox v119

user_pref("browser.search.separatePrivateDefault", true);
user_pref("browser.search.separatePrivateDefault.ui.enabled", true);

// added in arkenfox v122

user_pref("browser.migrate.chrome.get_permissions.enabled", true);
user_pref("browser.newtabpage.activity-stream.system.showSponsored", false);
user_pref("browser.search.newSearchConfig.enabled", false);
user_pref("browser.shell.checkDefaultPDF", true);
user_pref("browser.shell.checkDefaultPDF.silencedByUser", false);
user_pref("browser.shopping.experience2023.ads.exposure", false);
user_pref("browser.startup.windowsLaunchOnLogin.disableLaunchOnLoginPrompt", false);
user_pref("browser.startup.windowsLaunchOnLogin.enabled", false);
user_pref("browser.theme.windows.accent-color-in-tabs.enabled", false);
user_pref("browser.urlbar.quicksuggest.rustEnabled", false);
user_pref("browser.urlbar.suggest.recentsearches", true);
user_pref("browser.urlbar.switchTabs.searchAllContainers", false);
user_pref("clipboard.imageAsFile.enabled", true);
user_pref("cookiebanners.bannerClicking.pollingInterval", 500);
user_pref("cookiebanners.bannerClicking.timeoutAfterDOMContentLoaded", 20000);
user_pref("cookiebanners.bannerClicking.timeoutAfterLoad", 5000);
user_pref("cookiebanners.service.enableGlobalRules.subFrames", true);
user_pref("dom.iframe_lazy_loading.enabled", false);
user_pref("dom.security.https_first_schemeless", false);
user_pref("dom.w3c_pointer_events.getcoalescedevents_only_in_securecontext", false);
user_pref("dom.webgpu.testing.assert-hardware-adapter", false);
user_pref("dom.webgpu.workers.enabled", false);
user_pref("editor.block_inline_check.use_computed_style", false);
user_pref("extensions.formautofill.heuristics.interactivityCheckMode", "focusability");
user_pref("extensions.script_about_blank_without_permission", false);
user_pref("gfx.canvas.remote.allow-in-parent", false);
user_pref("gfx.canvas.remote.texture-timeout-ms", 10000);
user_pref("gfx.canvas.remote.worker-threads", -1);
user_pref("gfx.font_rendering.fallback.unassigned_chars", false);
user_pref("gfx.video.convert-yuv-to-nv12.image-host-win", true);
user_pref("image.mem.max_legal_imgframe_size_kb", -1);
user_pref("javascript.options.wasm_tail_calls", false);
user_pref("layout.css.always_underline_links", false);
user_pref("layout.css.text-wrap-balance-after-clamp.enabled", true);
user_pref("layout.css.text-wrap-balance.enabled", false);
user_pref("layout.css.text-wrap-balance.limit", 10);
user_pref("layout.css.zoom.enabled", false);
user_pref("layout.details.force-block-layout", true);
user_pref("media.wmf.hevc.enabled", 0);
user_pref("messaging-system.askForFeedback", true);
user_pref("network.auth.use_redirect_for_retries", false);
user_pref("network.http.http2.move_to_pending_list_after_network_change", false);
user_pref("network.url.strict_protocol_setter", true);
user_pref("print.enabled", true);
user_pref("privacy.query_stripping.strip_on_share.enableTestMode", false);
user_pref("toolkit.shopping.environment", "prod");
user_pref("webgl.gl_khr_no_error", false);

// disable privacy preserving ad attribution
// https://www.theregister.com/2024/06/18/mozilla_buys_anonym_betting_privacy/

user_pref("dom.private-attribution.submission.enabled", false);

// new from arkenfox user.js v126, v128

user_pref("privacy.spoof_english", 1);
user_pref("browser.urlbar.yelp.featureGate", false);
user_pref("browser.contentanalysis.default_allow", false);
user_pref("browser.contentanalysis.enabled", false);
user_pref("browser.contentanalysis.default_result", 0);
