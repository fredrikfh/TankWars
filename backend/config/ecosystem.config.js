// this is config for the pm2 process manager
module.exports = {
  apps: [
    {
      name: 'backend',
      script: 'dist/src/index.js',
      max_memory_restart: '1000M',
      env: {
        NODE_ENV: 'development',
      },
      env_production: {
        NODE_ENV: 'production',
      },
    },
  ],
};