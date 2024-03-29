export function welcome(): void {
  console.log(`
 ______          __  _      __               ___           __               __
/_  __/__ ____  / /_| | /| / /__ ________   / _ )___ _____/ /_____ ___  ___/ /
 / / / _ \`/ _ \\/  '_/ |/ |/ / _ \`/ __(_-<  / _  / _ \`/ __/  '_/ -_) _ \\/ _  / 
/_/  \\_,_/_//_/_/\\_\\|__/|__/\\_,_/_/ /___/ /____/\\_,_/\\__/_/\\_\\\\__/_//_/\\_,_/  
                                                                               `);

  console.log(`⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡀⠀⠀⠀⢀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣤⣴⣶⣶⣶⣦⣤⣤⣤⣤⣶⣾⣿⣿⣿⡗⠲⠲⠿⠿⠿⠷⠦⠤⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
  ⢀⣤⣶⣦⠤⠤⣄⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠟⢉⡽⠟⠛⠛⠛⠛⣻⡿⠷⣄⠈⢿⠀⠸⡆⡇⠀⠀⠀⠀⠀⠀⠀⠀⠈⢧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
  ⠸⣿⣿⡿⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠙⠒⠒⠒⠒⠠⠤⠤⠤⣄⣀⣰⣯⣴⣯⣤⣄⣀⣠⠖⠋⠁⠀⠀⢸⠀⣾⠀⢀⣷⣸⠀⣤⣤⣄⣠⣶⡄⣶⣦⠸⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀
  ⠀⠈⠉⠙⠓⠒⠒⠲⠤⠤⠤⢤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⣿⣆⠀⠈⠳⡄⠀⠀⠀⠀⢸⠀⡇⠀⢻⢹⡀⠀⣿⡄⢻⣿⣄⣻⣿⡘⣧⢻⠀⠀⠀⠀⠀⠀⠀⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠒⠦⣤⣤⣤⣀⡴⣿⠏⠀⠀⢀⠇⠀⠀⠀⠀⡆⢸⠇⠀⠈⠛⡟⢆⠘⣧⠈⣿⢹⣻⡿⢷⣽⠈⣧⣄⣀⣀⣀⣀⠀⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⣋⣀⠈⣿⣚⣓⣒⣒⣚⣉⠲⣤⣄⣀⣰⣷⠟⠀⠀⠀⠀⢹⢸⠀⠙⢿⣟⣈⣿⠧⠤⠝⠒⠋⠉⠁⠀⠈⣿⡄⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣤⠶⠶⢶⣿⣿⣿⣿⣽⣿⣻⣿⣿⣛⣻⣿⢿⣿⣟⣿⣿⣿⣿⣿⠖⠒⣲⣶⣶⠾⠶⠓⠋⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢘⡆⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⡿⠁⣀⣴⣋⠉⣹⡇⣠⣿⠿⣿⡟⠉⠉⠉⠁⠉⠉⠉⠉⣛⡛⠛⣿⢛⣿⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡤⠤⢴⣶⣾⣟⡽⠿⢤⣀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠋⠁⠘⠛⢻⣿⣿⠵⢫⣾⡿⠁⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⢺⣿⣿⡏⢹⠸⣿⣿⣃⣀⣠⣤⠴⠖⠒⠒⣿⣏⣩⠤⠾⢷⣖⣉⣉⣉⣷⡶⠖⢚⣿
  ⠀⠀⠀⢀⣴⠞⠉⠉⠉⠉⢉⡽⠛⣳⡶⠂⠀⠀⣠⣼⡿⠋⠀⠀⠀⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⣿⠁⠉⣿⡏⠉⠉⢀⣀⠬⢿⣶⡒⠊⠉⠉⢙⣿⣦⡤⠔⠛⣿⠉⢀⣴⣷⣾⣿⣿
  ⠀⠀⠀⣾⠁⠀⠀⠀⠀⡰⠋⣀⣴⡟⠁⠀⢠⣾⣿⣏⣀⣀⣀⣀⣀⣿⡇⠀⠀⣠⠾⠛⠉⠉⠉⠐⢒⡾⠛⠳⣄⡀⠀⠀⠀⣀⣉⢿⡶⠒⠋⠉⠀⣿⡄⣀⣴⣿⣾⣿⣿⣿⣿⣿⢹
  ⠀⠀⠀⢷⣄⣀⣀⣀⣀⡇⣼⣿⡟⠀⠠⣤⠀⠉⠉⡉⣉⡉⠉⠛⠛⠿⠇⠀⣰⠋⠀⠀⠀⠀⠀⢠⠋⠀⠀⢀⡠⠝⢷⠊⠉⠁⠀⠀⣧⠀⠀⣠⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇⠀
  ⠀⠀⠀⠀⢈⣿⣿⣿⣿⣿⣿⣿⣿⣍⠉⠻⠶⠶⣾⠦⠤⠬⣿⣄⣀⣼⣲⣾⡿⠀⠀⠀⠀⠀⠀⡇⠀⣠⠞⢉⣠⣤⣾⣷⣶⣶⣦⣤⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠀⠀
  ⠀⠀⠀⠀⠘⣿⣿⣿⣿⣿⣿⣿⣧⠉⠳⣄⠀⠀⠀⠀⠀⠀⠀⠉⠉⠛⢹⣿⢿⣶⣤⣤⣤⣄⣤⡁⢰⠃⣴⣿⣿⣿⠛⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣾⡿⣿⣿⡿⠟⠉⠀⠀⠀⠀
  ⠀⠀⠀⠀⠀⠘⢿⣿⣿⣿⣿⣿⣿⣷⣶⣾⣷⣶⣶⣶⣤⣤⣤⣄⣀⣀⣀⠀⠀⠹⣿⣿⣿⣿⣿⣿⣿⣾⣿⣿⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⣯⣿⠟⠉⠀⠀⠀⠀⠀⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠈⠛⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠉⠀⠀⠀⠈⠉⠉⠙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣼⣿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠛⠛⠛⠛⠛⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠛⠛⠻⠟⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀`);

  console.log(' ');
}
