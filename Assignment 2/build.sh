set -x
echo "  > Compilar todo o codigo (Makefile)"
make;

echo "  > Criar directorio para guardar ficheiros JAR, senao estiver criado"
if [[ ! -e JARS ]]; then
    mkdir JARS;
fi

echo "  > Gerar JARs"
(cd __bin;
jar cfe Party.jar serverSide.assault_party.AssaultPartyMain ./
jar cfe Party2.jar serverSide.assault_party.AssaultParty2Main ./
jar cfe Concentration.jar serverSide.concentration_site.ConcentrationSiteMain ./
jar cfe Control.jar serverSide.control_collect_site.ControlCollectionSiteMain ./
jar cfe Museum.jar serverSide.museum.MuseumMain ./
jar cfe Repository.jar serverSide.general_info_repo.LogMain ./
jar cfe Master.jar clientSide.Master.MasterMain ./
jar cfe Thieves.jar clientSide.Thieves.ThievesMain ./
mv *.jar ../JARS/)