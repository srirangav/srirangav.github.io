# common.mk - common make rules for public_html
# $Id: common.mk 1070 2009-03-30 00:49:08Z ranga $

index: $(RULE_INDEX)

genindex:
	if [ X"$(MKDIR)" = "X" ] ; then \
	       echo "ERROR: Make Directory not set!" 1>&2 ; \
	       exit 1; \
	fi
	if [ X"$(TITLE)" = "X" ] ; then \
           echo "ERROR: Page Title not set!" 1>&2 ; \
           exit 1; \
    fi
	$(MKDIR)/genindex.pl -r "$(MKDIR)/.." \
	                     -t "$(TITLE)" \
	                     -c "$(CAPTION)" \
	                     -h "mk/header.html.in" \
	                     -b "dir" \
	                     -f "mk/footer.html.in" . > index.html

genblankindex:
	if [ X"$(MKDIR)" = "X" ] ; then \
	       echo "ERROR: Make Directory not set!" 1>&2 ; \
	       exit 1; \
	fi
	$(MKDIR)/genindex.pl -r "$(MKDIR)/.." \
	                     -t "Blank" \
	                     -h "mk/header-blank.html.in" \
	                     -b "mk/body-blank.html.in" \
	                     -f "mk/footer.html.in" . > index.html

clean: $(PRE_CLEAN) common_clean $(POST_CLEAN)

common_clean:
	find . -type f -name .DS_Store -print | xargs rm -f
	find . -type f -name "*~" -print | xargs rm -f
	find . -type f -name "*.log" -print | xargs rm -f
	find . -type f -name "*.aux" -print | xargs rm -f

rmindex:
	rm -f index.html

pdf: 
	pdflatex $(PDFSRC)
